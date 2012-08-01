// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.processing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import ca.savi.aor.messageexceptions.GenericException;

/**
 * @author Keith
 */
public class BroadcastListener extends Thread {
  int UDPPort;
  LinkedList<ProcessingNode> p;
  public boolean keepRunning;

  public BroadcastListener(int port, LinkedList<ProcessingNode> myList) {
    UDPPort = port;
    p = myList;
    keepRunning = true;
  }

  @Override
  public void run() {
    DatagramSocket ds;
    InetAddress clientAddr;
    try {
      System.out.println("Creating listening socket on port: " + UDPPort);
      // ds = new DatagramSocket(UDPPort);
      ds = new DatagramSocket(null);
      ds.setBroadcast(true);
      ds.setReuseAddress(true);
      ds.bind(new InetSocketAddress(UDPPort));
      // timeout every 500 ms.
      ds.setSoTimeout(500);
    } catch (IOException e) {
      System.out.println("Error, broadcast socket not created successfully");
      System.out.println("Got message: " + e);
      return;
    }
    byte[] receiveData = new byte[1024];
    DatagramPacket receivePacket =
        new DatagramPacket(receiveData, receiveData.length);
    while (BroadcastListenerServletListener.hasBeenInit) {
      try {
        ds.receive(receivePacket);
        System.out.println("Received a datagram packet");
      } catch (SocketTimeoutException e) { // put here so that the keepRunning
                                           // flag is checked every 0.5 seconds
        continue;
      } catch (IOException e) {
        System.out.println("Error, unable to connect with subagent");
        System.out.println("Got message: " + e);
        continue;
      }
      // Packet format:
      // - 4 bytes IP address
      // - 2 bytes port
      // - 1 byte namelen
      // - x bytes name
      byte[] ip = new byte[4];
      byte[] portbuf = new byte[2];
      byte namelen;
      byte[] name;
      System.arraycopy(receivePacket.getData(), 0, ip, 0, 4);
      System.arraycopy(receivePacket.getData(), 4, portbuf, 0, 2);
      namelen = receivePacket.getData()[6];
      name = new byte[namelen];
      System.arraycopy(receivePacket.getData(), 7, name, 0, (int) namelen);
      int saport = Message.byteArrayToShort(portbuf);
      ProcessingNode pn = new ProcessingNode();
      try {
        pn.host = InetAddress.getByAddress(ip);
      } catch (UnknownHostException e) {
        System.out.println(e);
        continue;
      }
      pn.port = saport;
      pn.accessName = new String(name);
      System.out.println("Got a processor with name: " + pn.accessName);
      try {
        pn.sock = new Socket(pn.host, pn.port);
      } catch (IOException e) {
        System.out.println("error setting up socket: "
            + e.getLocalizedMessage());
        continue;
      }
      try {
        synchronized (pn.sock) {
          boolean moreUUIDs;
          MessageFactory.initRequest(pn.sock);
          moreUUIDs = MessageFactory.initResponse(pn.sock, pn);
          if (p == null) {
            System.out.println("Uh oh, list is null");
          } else if (pn == null)
            System.out.println("Error, new pn is null");
          // everything worked! yay!
          else
            p.add(pn);
          while (moreUUIDs) {
            // instantiate the server
            ProcessingNode nn = new ProcessingNode();
            nn.host = pn.host;
            nn.port = saport;
            nn.name = pn.name;
            nn.IP = null;
            nn.sock = pn.sock;
            MessageFactory.initNextRequest(pn.sock);
            moreUUIDs = MessageFactory.initResponse(pn.sock, nn);
            // debug output
            System.out.println("Server " + nn.uuid);
            // end debug output
            if (p == null) {
              System.out.println("Uh oh, list is null");
            } else if (pn == null)
              System.out.println("Error, new pn is null");
            // everything worked! yay!
            else
              p.add(nn);
          } // end while(moreUUIDs)
        } // end synchronized(pn.sock)
        // debug output
        System.out.println("Server " + pn.uuid.toString() + " ("
            + pn.host.getHostAddress() + ":" + pn.port + ") initialized.");
        // end debug output
      } catch (GenericException e) {
        System.out.println(e.getMessage());
        continue;
      }
    } // end while (BroadcastListenerServletListener.hasBeenInit)
    System.out.println("Listener thread has shut down");
  }
}
