// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.hardware;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * @author Keith
 */
public class BroadcastListener extends Thread {
  int UDPPort;
  List<HardwareUnit> h;
  boolean keepRunning;

  public BroadcastListener(int port, List<HardwareUnit> myList) {
    UDPPort = port;
    h = myList;
    keepRunning = true;
  }

  @Override
  public void interrupt() {
    keepRunning = false;
  }

  @Override
  public void run() {
    DatagramSocket ds;
    InetAddress clientAddr;
    System.out.println("Starting broadcast Listener");
    try {
      ds = new DatagramSocket(UDPPort);
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
    // while condition is monitored on BroadcastListenerServletListener
    while (keepRunning/* BroadcastListenerServletListener.hasBeenInit */) {
      try {
        ds.receive(receivePacket);
        System.out.println("Received a datagram packet");
      } catch (SocketTimeoutException e) { // put here so that the hasBeenInit
                                           // flag is checked every 0.5 seconds
        continue;
      } catch (IOException e) {
        System.out.println("Error, unable to connect with subagent");
        System.out.println("Got message: " + e);
        continue;
      }
      // Packet format:
      // - 1 byte namelen
      // - x bytes name
      // - 2 bytes port
      // byte [] ip = new byte [4];
      byte[] portbuf = new byte[2];
      byte namelen;
      byte[] name;
      /*
       * System.arraycopy (receivePacket.getData(), 0, ip, 0, 4);
       * System.arraycopy (receivePacket.getData(), 4, portbuf, 0, 2); namelen =
       * receivePacket.getData()[6]; name = new byte[namelen];
       * System.arraycopy(receivePacket.getData(), 7, name, 0, (int)namelen);
       * int saport = Message.byteArrayToShort (portbuf);
       */
      namelen = receivePacket.getData()[0];
      name = new byte[namelen];
      System.arraycopy(receivePacket.getData(), 1, name, 0, (int) namelen);
      System.arraycopy(receivePacket.getData(), namelen + 1, portbuf, 0, 2);
      int saport = Message.byteArrayToShort(portbuf);
      // create the unit
      HardwareUnit hu = new HardwareUnit();
      // address
      hu.name = new String(name);
      hu.host = receivePacket.getAddress();
      hu.port = saport;
      System.out.println("server at " + hu.host.toString() + ":" + hu.port);
      // register
      try {
        hu.sock = new Socket(hu.host, hu.port);
        // timeout after 600 seconds of blocking on read
        hu.sock.setSoTimeout(600000);
        synchronized (hu.sock) {
          // initialize first
          MessageFactory.initRequest(hu.sock);
          MessageFactory.initResponse(hu.sock);
          // then register
          MessageFactory.registerRequest(hu.sock);
          // hardwareRegisterResponse instantiates the objects inside a
          // hardware unit
          hu = MessageFactory.hardwareRegisterResponse(hu.sock, hu);
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
        continue;
      }
      // debug output
      System.out.println("Server " + hu.name + " (" + hu.host.getHostAddress()
          + ":" + hu.port + ") initialized with the following resources:");
      for (HardwareNode hn : hu.hwNode)
        System.out.println("hw node " + hn.name + ": uuid "
            + hn.uuid.toString());
      for (InterchipNode ic : hu.icNode)
        System.out.println("ic node " + ic.name + ": uuid "
            + ic.uuid.toString() + ", linking "
            + hu.getNodeFromUUID(ic.link[0]).name + " and "
            + hu.getNodeFromUUID(ic.link[1]).name);
      // end debug output
      h.add(hu);
    } // end while (BroadcastListenerServletListener.hasBeenInit)
    System.out.println("Listener thread has shut down");
  }
}