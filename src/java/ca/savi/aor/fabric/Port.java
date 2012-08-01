// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.fabric;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import ca.savi.aor.messageexceptions.GenericException;

/**
 * @author Keith
 */
public class Port {
  public LinkedList<UUIDType> uuidList;
  boolean[] vlans;
  Switch p;
  public boolean valid;
  public boolean hasBeenDefined;
  int lastVlanIndex;
  public byte[] macAddress;
  public int interfaceIndex;

  public Port(Switch parent) {
    p = parent;
    valid = false;
    // set by the network builder, so that repeat portBits can be detected
    hasBeenDefined = false;
    vlans = new boolean[p.p.endVlanId];
    for (int i = 0; i < p.p.endVlanId; i++) {
      vlans[i] = false;
    }
    lastVlanIndex = 0;
    macAddress = new byte[6];
    clearMac();
    uuidList = new LinkedList<UUIDType>();
  }

  public void clearMac() {
    for (int i = 0; i < 6; i++)
      macAddress[i] = 0;
  }

  public UUID containsBridge() {
    Iterator itr = uuidList.iterator();
    while (itr.hasNext()) {
      UUIDType cur = (UUIDType) itr.next();
      if (cur.type == AccessTypes.BRIDGE)
        return cur.uuid;
    }
    return null;
  }

  public UUID containsGateway() {
    Iterator itr = uuidList.iterator();
    while (itr.hasNext()) {
      UUIDType cur = (UUIDType) itr.next();
      if (cur.type == AccessTypes.GATEWAY)
        return cur.uuid;
    }
    return null;
  }

  public void addUUID(String uuid, AccessTypes type) {
    try {
      UUID tempUUID = UUID.fromString(uuid);
      valid = true;
      UUIDType id = new UUIDType(tempUUID, type);
      uuidList.add(id);
    } catch (IllegalArgumentException e) {
    }
  }

  public void removeUUID(String uuid) {
    LinkedList<UUIDType> removeObjs = new LinkedList();
    try {
      UUID tempUUID = UUID.fromString(uuid);
      Iterator itr = uuidList.iterator();
      while (itr.hasNext()) {
        UUIDType id = (UUIDType) itr.next();
        if (id.uuid.equals(tempUUID))
          // uuidList.remove(id);
          removeObjs.add(id);
      }
      itr = removeObjs.iterator();
      while (itr.hasNext()) {
        UUIDType removeObj = (UUIDType) itr.next();
        uuidList.remove(removeObj);
      }
      if (uuidList.size() == 0)
        valid = false;
    } catch (IllegalArgumentException e) {
    }
  }

  public boolean containsUUID(UUID id) {
    Iterator itr = uuidList.iterator();
    while (itr.hasNext()) {
      UUIDType cur = (UUIDType) itr.next();
      if (cur.uuid.equals(id))
        return true;
    }
    return false;
  }

  public void addBroadcastVLAN(int vlanid) {
    Iterator itr = uuidList.iterator();
    while (itr.hasNext()) {
      UUIDType cur = (UUIDType) itr.next();
      if (!cur.onVlan(vlanid))
        cur.addVlan(vlanid);
    }
    vlans[vlanid] = true;
  }

  public boolean addVLAN(UUID id, int vlanid) {
    if (!valid) {
      System.out.println("Port is not valid");
      return false;
    }
    if (vlanid <= 1 || vlanid > p.p.endVlanId) {
      System.out.println("Error, invalid VLAN id: " + vlanid);
      return false;
    }
    Iterator itr = uuidList.iterator();
    while (itr.hasNext()) {
      UUIDType cur = (UUIDType) itr.next();
      if (cur.uuid.equals(id)) {
        if (!cur.onVlan(vlanid))
          cur.addVlan(vlanid);
        vlans[vlanid] = true;
      }
    }
    return true;
  }

  public boolean removeVLAN(UUID id, int vlanid) throws GenericException {
    if (!valid)
      throw new GenericException("Error, UUID specified is not valid");
    if (vlanid < 1 || vlanid >= p.p.endVlanId)
      throw new GenericException("Error, vlan out of range");
    Iterator itr = uuidList.iterator();
    while (itr.hasNext()) {
      UUIDType cur = (UUIDType) itr.next();
      if (cur.uuid.equals(id)) {
        cur.removeVlan(vlanid);
      }
    }
    updateMembership(vlanid);
    return true;
  }

  private void updateMembership(int vlanid) {
    boolean onVLAN = false;
    Iterator itr = uuidList.iterator();
    while (itr.hasNext()) {
      UUIDType cur = (UUIDType) itr.next();
      if (cur.onVlan(vlanid)) {
        onVLAN = true;
        break;
      }
    }
    if (!onVLAN) {
      vlans[vlanid] = false;
    }
  }

  public boolean isOnVlan(int vlanid) {
    if (vlanid <= 1 || vlanid > p.p.endVlanId)
      return false;
    return (vlans[vlanid]);
  }

  public boolean isOnVlanIgnoreALLs(int vlanid) {
    if (vlanid <= 1 || vlanid > p.p.endVlanId)
      return false;
    if (!vlans[vlanid])
      return false;
    Iterator itr = uuidList.iterator();
    while (itr.hasNext()) {
      UUIDType cur = (UUIDType) itr.next();
      if (cur.onVlan(vlanid) && cur.type != AccessTypes.ALL
          && cur.type != AccessTypes.BRIDGE && cur.type != AccessTypes.GATEWAY)
        return true;
    }
    return false;
  }
}