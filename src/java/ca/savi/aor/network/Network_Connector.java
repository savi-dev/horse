// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
 * @author Keith
 */
public class Network_Connector {
  LinkedList<NetworkServer> servers;
  String filename;
  boolean initialized = false;

  public Network_Connector(String fname) {
    filename = fname;
  }

  // parse the initialization file to create the linked list of servers
  public boolean init() {
    servers = null;
    BufferedReader input;
    String line;
    initialized = false;
    String strIP, strPort;
    InetAddress IP;
    int port, lineCount;
    try {
      input = new BufferedReader(new FileReader(new File(filename)));
    } catch (FileNotFoundException e) {
      System.out.println("Warning, file does not exist, running with"
          + "no network-side connections");
      initialized = true;
      return true;
    }
    try {
      lineCount = 0;
      while ((line = input.readLine()) != null) {
        lineCount++;
        // Remove comments
        int delimiterIndex = line.indexOf('#');
        if (delimiterIndex > 0) {
          line = line.substring(0, delimiterIndex - 1);
        } else if (delimiterIndex == 0)
          continue;
        // trim whitespace
        line = line.trim();
        if (line.length() == 0)
          continue;
        delimiterIndex = line.indexOf(':');
        if (delimiterIndex <= 1)
          return false;
        strIP = line.substring(0, delimiterIndex - 1);
        strPort = line.substring(delimiterIndex + 1);
        try {
          IP = InetAddress.getByName(strIP);
          port = Integer.valueOf(strPort);
        } catch (NumberFormatException e) {
          System.out.println("Error, " + e);
          return false;
        } catch (UnknownHostException e) {
          System.out.println("Error, " + e);
          return false;
        }
        try {
          Socket tempSock = new Socket(IP, port);
          NetworkServer newServer = new NetworkServer();
          newServer.IP = IP;
          newServer.port = port;
          newServer.sock = tempSock;
          servers.add(newServer);
        } catch (IOException e) {
          System.out.println("Error, " + e);
          return false;
        }
      }
    } catch (IOException e) {
      System.out.println("Error, " + e);
      return false;
    }
    initialized = true;
    return true;
  }
  /*
   * //CHANGE THIS TO WEB-SERVICE BASED public int getUserVlanID(String
   * username) { int vlanid = -1; for(NetworkServer s : servers) { try { Message
   * m = MessageFactory.sendMessage(s.sock, username.getBytes(),
   * MessageType.USER_VLANID_RQT, MessageType.USER_VLANID_RQT); vlanid =
   * Message.byteArrayToInt(m.getPayload()); } catch (GenericException e) { } }
   * // end for s : servers return(vlanid); } //CHANGE THIS TO WEB-SERVICE BASED
   * public void deleteVlanId(String username) { for(NetworkServer s : servers)
   * { try { MessageFactory.sendMessage(s.sock, username.getBytes(),
   * MessageType.NETWORK_DELETE_VLAN_RQT, MessageType.NETWORK_DELETE_VLAN_RSP);
   * } catch (GenericException e) { } } }
   */
}
