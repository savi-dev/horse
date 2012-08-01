// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.storage;

import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Keith
 */
public class TransactionManager {
  public InetAddress host, retHost;
  public int port, retPort;
  public Socket sock;
  public Socket returnSock;
}
