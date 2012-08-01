// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

import ca.savi.aor.messageexceptions.GenericException;

/**
 * @author Keith
 */
public class BroadcastListener extends Thread {
  ResourceBundle rb;
  List<StorageServer> serverList;
  List<String[]> ipPairs;
  boolean keepRunning;
  String gwIP;
  String keyfile;

  // GOELogger mylog = new GOELogger("/home/arbab/storageLogs/bl.log");
  public BroadcastListener(ResourceBundle resources,
      List<StorageServer> servList, List<String[]> pairs, String gatewayIP,
      String securityFile) {
    rb = resources;
    serverList = servList;
    ipPairs = pairs;
    keepRunning = true;
    gwIP = gatewayIP;
    keyfile = securityFile;
  }

  @Override
  public void interrupt() {
    keepRunning = false;
  }

  // ------------------------------------------------------------
  @Override
  public void run() {
    DatagramSocket ds;
    int numServers = Integer.valueOf(rb.getString("NumServers"));
    ServerDescription[] validServers = new ServerDescription[numServers];
    int expectedServers = 0;
    boolean addToFile = false, serverAdded;
    System.out.println("Started listening for subagent broadcasts");
    for (int i = 0; i < numServers; i++)
      validServers[i] = new ServerDescription();
    try {
      expectedServers = getExpectedServers(validServers);
    } catch (GenericException e) {
      System.out.println("Parse error in file, rejecting subagent");
    }
    int listenPort = Integer.valueOf(rb.getString("broadcastPort"));
    System.out.println("listen Port is " + listenPort);
    try {
      ds = new DatagramSocket(null);
      ds.setBroadcast(true);
      ds.setReuseAddress(true);
      ds.bind(new InetSocketAddress(listenPort));
    } catch (IOException e) {
      System.out.println("Error, broadcast socket not created successfully");
      System.out.println("Got message: " + e);
      return;
    }
    byte[] receiveData = new byte[1024];
    DatagramPacket receivePacket =
        new DatagramPacket(receiveData, receiveData.length);
    while (keepRunning) {
      try {
        ds.receive(receivePacket);
        System.out.println("Received a datagram packet");
        if (expectedServers < numServers)
          addToFile = true;
        else
          addToFile = false;
        serverAdded =
            respondToDatagram(receivePacket, validServers, addToFile,
                expectedServers);
        if (serverAdded)
          expectedServers++;
      } catch (IOException e) {
        System.out.println("Error, unable to connect with subagent");
        System.out.println("Got message: " + e);
      }
    } // end while (keepRunning)
  }

  // ------------------------------------------------------------
  private int getExpectedServers(ServerDescription[] validServers)
      throws GenericException {
    int expectedServers = 0;
    try {
      BufferedReader br =
          new BufferedReader(new FileReader(rb.getString("NewSubagentsFile")));
      String theLine;
      while (br.ready()) {
        theLine = br.readLine();
        if (theLine.split(",").length != 3)
          continue;
        String[] splitVals = theLine.split(",");
        System.out.println("IP: " + splitVals[0] + " Port: " + splitVals[1]
            + " server number: " + splitVals[2]);
        // mylog.write("IP: " + splitVals[0] + " Port: " + splitVals[1] +
        // " server number: " + splitVals[2] +"\n", true);
        try {
          validServers[expectedServers].setIP(splitVals[0]);
          validServers[expectedServers].port = Integer.valueOf(splitVals[1]);
          validServers[expectedServers].serverNumber =
              Integer.valueOf(splitVals[2]);
          expectedServers++;
        } catch (GenericException e) {
          br.close();
          throw new GenericException("");
        }
      } // End while(br.read());
      br.close();
    } catch (IOException e) {
      throw new GenericException("");
    }
    // mylog.write("returning " + expectedServers +"\n", true);
    return (expectedServers);
  }

  // ------------------------------------------------------------
  private boolean respondToDatagram(DatagramPacket receivePacket,
      ServerDescription[] validServers, boolean addToFile, int expectedServers)
      throws IOException {
    boolean serverAdded = false;
    StorageServer serv = new StorageServer();
    try {
      if (receivePacket.getData()[0] == MessageType.ADD_SERV_RQT.getType()) {
        extractPacketData(serv, receivePacket);
        int servNo = findServer(validServers, serv.IP, serv.port);
        int index = servAlreadyConnected(serv);
        /*
         * if(expectedServers == 0) { Message m = new
         * Message(MessageType.COPY_FILE_RQT); m.send(serv.sock); return(false);
         * } else if(expectedServers == 1) { Message m = new
         * Message(MessageType.COPY_FILE_RSP); m.send(serv.sock); return(false);
         * } else if(expectedServers == 2) { Message m = new
         * Message(MessageType.DELETE_DIR_RQT); m.send(serv.sock);
         * return(false); } else { Message m = new
         * Message(MessageType.DELETE_DIR_RSP); m.send(serv.sock); }
         * if(expectedServers > 2) { return false; }
         */
        if (servNo > 0 || addToFile) {
          if (index >= 0) {
            System.out
                .println("Reconnection of a server, removing stale values");
            serverList.remove(index);
          }
          Message m = new Message(MessageType.SEND_NEXT_PARAM);
          m.send(serv.sock);
          m = Message.receive(serv.sock);
          if (m.getMessageType() != MessageType.NEXT_PARAM_RQT) {
            m = new Message(MessageType.ERR_UNEXPECTED_CLT);
            m.send(serv.sock);
            return (false);
          } else {
            byte[] payload = new byte[2];
            payload = m.getPayload();
            serv.httpPort = payload[0] & 0xff;
            serv.httpPort <<= 8;
            serv.httpPort += payload[1] & 0xff;
            if (servNo > 0) {
              serv.servNo = servNo;
            }
            if (servNo < 0) {
              try {
                validServers[expectedServers].IP =
                    ServerDescription.IPStrToByteArr(serv.IP);
                validServers[expectedServers].port = serv.port;
                validServers[expectedServers].serverNumber =
                    findMinUnusedServNum(validServers);
                serv.servNo = validServers[expectedServers].serverNumber;
                addToFile(serv, validServers[expectedServers].serverNumber);
                serverAdded = true;
              } catch (GenericException e) {
                m = new Message(MessageType.ERR_UNEXPECTED_CLT);
                m.send(serv.sock);
                return (false);
              }
            }
            // By default, external IP = internal IP
            serv.externalName = serv.IP;
            Iterator itr = ipPairs.iterator();
            // if we find an external IP that is associated with the
            // internal IP for this server, change the external IP
            // address for this server to match
            while (itr.hasNext()) {
              String[] pair = (String[]) itr.next();
              // mylog.write("pair[0]: " + pair[0] + "servIP: " + serv.IP +
              // "\n", true);
              if (pair[0].equals(serv.IP)) {
                // mylog.write("found ext int match\n", true);
                serv.externalName = pair[1];
                /*
                 * Runtime runtime = Runtime.getRuntime(); String[] args = new
                 * String[]{"sh", "-c", "ssh -f -i " + keyfile + " root@" + gwIP
                 * + " \"perl stoptcppr.pl " + serv.externalIP + "\""}; Process
                 * proc = runtime.exec(args); args = new String[]{"sh", "-c",
                 * "ssh -f -i " + keyfile + " root@" + gwIP +
                 * " \"perl tcppr.pl " + serv.externalIP + " " + serv.IP +
                 * "\""}; proc = runtime.exec(args);
                 */
                break;
              }
            }
            serverList.add(serv);
            m = new Message(MessageType.ADD_SERV_RSP);
            payload[0] = (byte) ((servNo >> 8) & 0xff);
            payload[1] = (byte) (servNo & 0xff);
            m.addPayload(payload);
            m.send(serv.sock);
          }
        } else {
          Message m = new Message(MessageType.ERR_UNEXPECTED_CLT);
          m.send(serv.sock);
        }
      }
      return (serverAdded);
    } catch (IOException e) {
      Message m = new Message(MessageType.INTERNAL_SERVER_ERR);
      m.send(serv.sock);
      throw new IOException(e);
    } catch (GenericException e) {
      Message m = new Message(MessageType.INTERNAL_SERVER_ERR);
      m.send(serv.sock);
      throw new IOException(e.getMessage());
    }
  }

  // ------------------------------------------------------------
  private int findServer(ServerDescription[] validServers, String IP,
      int port) {
    byte[] byteIP = new byte[4];
    String[] splitVals = new String[4];
    if (IP.split("\\.").length != 4) {
      System.out.println("Parse error on IP");
      return (-1);
    }
    splitVals = IP.split("\\.");
    byteIP[0] = (byte) (Integer.valueOf(splitVals[0]) & 0xff);
    byteIP[1] = (byte) (Integer.valueOf(splitVals[1]) & 0xff);
    byteIP[2] = (byte) (Integer.valueOf(splitVals[2]) & 0xff);
    byteIP[3] = (byte) (Integer.valueOf(splitVals[3]) & 0xff);
    System.out.println("FindIP = " + IP);
    for (int i = 0; i < validServers.length; i++) {
      System.out.println("compIP = " + validServers[i].getIP());
      if (mycomp(validServers[i].IP, byteIP) && validServers[i].port == port) {
        System.out.println("Got a server!!!");
        return (validServers[i].serverNumber);
      }
    }
    System.out.println("Did not find a match for the server\n");
    return (-2);
  }

  // ------------------------------------------------------------
  private int servAlreadyConnected(StorageServer serv) {
    int index = -1, i = 0;
    ListIterator itr = serverList.listIterator();
    StorageServer tmpServ = new StorageServer();
    while (itr.hasNext()) {
      tmpServ = (StorageServer) itr.next();
      if (tmpServ.IP.equals(serv.IP) && tmpServ.port == serv.port)
        index = i;
      i++;
    }
    return (index);
  }

  // ------------------------------------------------------------
  private void extractPacketData(StorageServer serv,
      DatagramPacket receivePacket) throws IOException {
    try {
      byte[] temp = new byte[4];
      System.arraycopy(receivePacket.getData(), 3, temp, 0, 4);
      serv.IP = InetAddress.getByAddress(temp).getHostAddress();
      // System.out.println("serv.ip: " + serv.IP);
      // serv.IP = receivePacket.getAddress().getHostAddress();
      // System.out.println("serv.ip: " + serv.IP);
      // System.out.println("inetaddress.getbyname() : " +
      // InetAddress.getByName(gwIP))
      /*
       * serv.IP = String.valueOf(receivePacket.getData()[3]) + "." +
       * String.valueOf(receivePacket.getData()[4]) + "." +
       * String.valueOf(receivePacket.getData()[5]) + "." +
       * String.valueOf(receivePacket.getData()[6]);
       */serv.port =
          (int) (receivePacket.getData()[7] << 8) + receivePacket.getData()[8];
      System.out.println("Connecting to: " + serv.IP + ":" + serv.port);
      Socket s = new Socket(InetAddress.getByName(serv.IP), serv.port);
      serv.sock = s;
    } catch (IOException e) {
      System.out.println("BIG PROBLEM HERE BECAUSE YOU CANNOT CONNECT "
          + "BACK!!!!!!!! Trying other address");
      try {
        serv.IP = receivePacket.getAddress().getHostAddress();
        System.out.println("Connecting to: " + serv.IP + ":" + serv.port);
        Socket s = new Socket(InetAddress.getByName(serv.IP), serv.port);
        serv.sock = s;
      } catch (IOException f) {
        System.out.println("cannot connect here aswell\n");
        throw new IOException(f);
      }
    }
  }

  // ------------------------------------------------------------
  private void addToFile(StorageServer serv, int servNo) throws IOException {
    try {
      FileWriter fw = new FileWriter(rb.getString("NewSubagentsFile"), true);
      fw.write("\n" + serv.IP + "," + serv.port + "," + servNo);
      System.out.println("Added a server to the file");
      fw.close();
    } catch (IOException e) {
      throw new IOException(e);
    }
  }

  // ------------------------------------------------------------
  private int findMinUnusedServNum(ServerDescription[] validServers) {
    int min = 0;
    boolean found;
    do {
      found = false;
      min++;
      for (int i = 0; i < validServers.length; i++) {
        if (validServers[i].serverNumber == min) {
          found = true;
          break;
        }
      }
    } while (found);
    return (min);
  }

  // ------------------------------------------------------------
  private boolean mycomp(byte[] orig, byte[] comp) {
    if (orig.length != comp.length)
      return false;
    for (int i = 0; i < orig.length; i++)
      if (orig[i] != comp[i])
        return false;
    return true;
  }
}
