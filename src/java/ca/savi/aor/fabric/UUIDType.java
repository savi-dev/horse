// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.fabric;

import java.util.UUID;

/**
 * @author Keith
 */
public class UUIDType {
  UUID uuid;
  boolean[] vlans;
  AccessTypes type;

  public UUIDType(UUID id, AccessTypes t) {
    uuid = id;
    type = t;
    vlans = new boolean[4096];
    for (int i = 0; i < 4096; i++) {
      vlans[i] = false;
    }
  }

  public void addVlan(int vlanid) {
    vlans[vlanid] = true;
  }

  public void removeVlan(int vlanid) {
    vlans[vlanid] = false;
  }

  public boolean onVlan(int vlanid) {
    return (vlans[vlanid]);
  }
}