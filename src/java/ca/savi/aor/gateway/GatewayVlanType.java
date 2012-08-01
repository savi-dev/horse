// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.gateway;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import ca.savi.aor.messageexceptions.GenericException;

/**
 * @author Keith
 */
public class GatewayVlanType {
  public GatewayVlanType(int vlanid, String IP) {
    onGW = false;
    id = vlanid;
    thisIP = IP;
    uuids = new LinkedList();
  }

  public void addUUID(String gwIP, String keyfile, UUID uuid)
      throws GenericException {
    Iterator itr = uuids.iterator();
    while (itr.hasNext()) {
      UUID next = (UUID) itr.next();
      if (next.equals(uuid))
        return;
    }
    uuids.add(uuid);
    try {
      if (!onGW) {
        onGW = true;
        Runtime runtime = Runtime.getRuntime();
        String[] args =
            new String[] {
                "sh",
                "-c",
                "ssh -f -i " + keyfile + " root@" + gwIP + " \"./addvlan " + id
                    + " " + thisIP + "\"" };
        Process proc = runtime.exec(args);
      }
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  public void removeUUID(String gwIP, String keyfile, UUID uuid)
      throws GenericException {
    if (!onGW)
      return;
    uuids.remove(uuid);
    try {
      if (uuids.size() == 0) {
        onGW = false;
        Runtime runtime = Runtime.getRuntime();
        String[] args =
            new String[] {
                "sh",
                "-c",
                "ssh -f -i " + keyfile + " root@" + gwIP + " \"./remvlan " + id
                    + "\"" };
        Process proc = runtime.exec(args);
      }
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  public boolean onGW;
  public LinkedList<UUID> uuids;
  private int id;
  private String thisIP;
}