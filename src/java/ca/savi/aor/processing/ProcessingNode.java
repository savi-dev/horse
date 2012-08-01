// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.processing;

import java.net.InetAddress;
import java.net.Socket;

import ca.savi.aor.generic.Node;

/**
 * @author Keith
 */
public class ProcessingNode extends Node {
  public InetAddress host;
  public String accessName;
  public String username;
  public int port;
  public Socket sock;
  public String IP;

  @Override
  public String getType() {
    return "ProcessingNode";
  }
}
