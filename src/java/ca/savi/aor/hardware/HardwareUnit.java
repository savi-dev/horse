// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.hardware;

import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

/**
 * @author yuethomas
 */
public class HardwareUnit {
  public HardwareNode[] hwNode;
  public InterchipNode[] icNode;
  public String name;
  public InetAddress host;
  public int port;
  public Socket sock;

  public Node getNodeFromUUID(UUID uuid) {
    if (hwNode != null)
      for (HardwareNode hn : hwNode)
        if (hn.uuid.equals(uuid))
          return hn;
    if (icNode != null)
      for (InterchipNode ic : icNode)
        if (ic.uuid.equals(uuid))
          return ic;
    return null;
  }
}