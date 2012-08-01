// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.hardware;

import java.net.InetAddress;
import java.util.UUID;

/**
 * @author yuethomas
 */
public class HardwareNode extends Node {
  public InetAddress host;
  public String PAname;
  public UUID leftUUID;
  public UUID rightUUID;

  @Override
  public String getType() {
    return "HardwareNode";
  }
}