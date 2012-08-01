// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.ip;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

/**
 * @author Keith
 */
public class VlanType {
  int vlanid;
  int Q_in_Q_tag;
  public boolean inUse;
  public IpType[] IpListofAttachedResource;
  private LinkedList<AttarchedResource> uuidListofAttachedResource;
  boolean rawEthernet;
  private int numberOfRealRes;

  VlanType(int vlan) {
    inUse = false;
    numberOfRealRes = 0;
    Q_in_Q_tag = 0;
    IpListofAttachedResource = new IpType[4096];
    uuidListofAttachedResource = new LinkedList();
    uuidListofAttachedResource.clear();
    rawEthernet = false;
    vlanid = vlan;
  }

  public void clear() {
    Q_in_Q_tag = 0;
    uuidListofAttachedResource.clear();
    for (int i = 0; i < 4096; i++)
      IpListofAttachedResource[i] = null;
  }

  /*
   * public void addUUIDToList(UUID uuid) { uuidListofAttachedResource.add(new
   * AttarchedResource(uuid)); }
   */
  private void addUUIDToList(UUID uuid, int resIndex, boolean autoAdded) {
    uuidListofAttachedResource.add(new AttarchedResource(uuid, resIndex,
        autoAdded));
    if (!autoAdded)
      numberOfRealRes++;
  }

  private int removeUUID(UUID uuid) {
    Iterator itr = uuidListofAttachedResource.iterator();
    while (itr.hasNext()) {
      AttarchedResource res = (AttarchedResource) itr.next();
      if (res.uuid.equals(uuid)) {
        uuidListofAttachedResource.remove(res);
        if (!res.autoAdded) {
          numberOfRealRes--;
        }
        releaseRIDNew(res.resIndex);
        return uuidListofAttachedResource.size();
      }
    }
    return (uuidListofAttachedResource.size());
  }

  public String findUuidIp(UUID uuid) {
    /*
     * for(int i=0; i<4096; i++) { if(IpList[i] == null) continue;
     * if(IpList[i].uuid.equals(uuid)) return(ridToIP(i)); }
     */
    int resIndex = getRIDOfResource(uuid);
    if (resIndex >= 0)
      return ridToIP(resIndex);
    return ("0.0.0.0");
  }

  public int getNumUUIDs() {
    return (uuidListofAttachedResource.size());
  }

  public int addUUIDToVlan(UUID uuid, int resId) {
    // First 100 IPs are reserved
    if (resId >= 0 && resId < 101) {
      IpListofAttachedResource[resId] = new IpType();
      IpListofAttachedResource[resId].uuid = uuid;
      IpListofAttachedResource[resId].autoAdded = true;
      addUUIDToList(uuid, resId, true);
    } else {
      for (int i = 101; i < 4095; i++) {
        if (IpListofAttachedResource[i] == null) {
          IpListofAttachedResource[i] = new IpType();
          IpListofAttachedResource[i].uuid = uuid;
          IpListofAttachedResource[i].autoAdded = false;
          addUUIDToList(uuid, i, false);
          return i;
        }
      }
    }
    return (-1);
  }

  public int clearUUID(UUID uuid) {
    /*
     * if(!inUse) return -1; for(int i=2; i<4095; i++) { if(IpList[i] == null)
     * continue; if(IpList[i].uuid.equals(uuid)) IpList[i] = null; }
     * if(uuidList.isEmpty()) return;
     */
    /*
     * Iterator itr = uuidList.iterator(); while(itr.hasNext()) { UUID next =
     * (UUID)itr.next(); if(next.equals(uuid)) uuidList.remove(uuid); }
     */
    // delete all instances of uuid from the list
    // uuidListofAttachedResource.remove(uuid);
    removeUUID(uuid);
    if (uuidListofAttachedResource.size() == 0)
      inUse = false;
    return numberOfRealRes;
  }

  public void releaseRIDNew(int rid) {
    if (rid >= 0 && rid < 4096)
      IpListofAttachedResource[rid] = null;
  }

  public String ridToIP(int rid) {
    byte[] rawAddr = new byte[4];
    String IP;
    // IP Address = 10.VID[11:4].{VID[3:0]RID[11:8]}.RID[7:0]
    // VID = vlan id, RID = resource id
    rawAddr[0] = 10;
    rawAddr[1] = (byte) (vlanid >> 4);
    rawAddr[2] = (byte) ((vlanid << 4) & 0xf0);
    rawAddr[2] += (byte) ((rid >> 8) & 0x0f);
    rawAddr[3] = (byte) (rid & 0xff);
    try {
      IP = InetAddress.getByAddress(rawAddr).getHostAddress();
    } catch (UnknownHostException e) {
      return ("0.0.0.0");
    }
    return (IP);
  }

  UUID getUUIDofAResource(int rid) {
    if (rid >= 0 && rid < 4096)
      return IpListofAttachedResource[rid].uuid;
    return null;
  }

  int getRIDOfResource(UUID uuid) {
    Iterator itr = uuidListofAttachedResource.iterator();
    while (itr.hasNext()) {
      AttarchedResource res = (AttarchedResource) itr.next();
      if (res.uuid.equals(uuid)) {
        return res.resIndex;
      }
    }
    return -1;
  }

  public boolean uuidExistAndLastUUID(UUID u) {
    boolean exist = false;
    ;
    boolean lastUUID = true;
    Iterator itr = uuidListofAttachedResource.iterator();
    while (itr.hasNext()) {
      AttarchedResource res = (AttarchedResource) itr.next();
      if (res.uuid.equals(u)) {
        exist = true;
      } else if (!res.autoAdded)
        lastUUID = false;
    }
    return lastUUID && exist;
  }

  private boolean anyNonAutoAddedUUIDExist() {
    Iterator itr = uuidListofAttachedResource.iterator();
    while (itr.hasNext()) {
      AttarchedResource res = (AttarchedResource) itr.next();
      if (!res.autoAdded)
        return true;
    }
    return false;
  }

  boolean AnyRealResourceConnectedToVlan() {
    return (numberOfRealRes > 0);
  }

  public boolean uuidExist(UUID u) {
    Iterator itr = uuidListofAttachedResource.iterator();
    while (itr.hasNext()) {
      AttarchedResource res = (AttarchedResource) itr.next();
      if (res.uuid.equals(u)) {
        return true;
      }
    }
    return false;
  }

  public String giveUuidName(UUID u) {
    String name = "";
    Iterator itr = uuidListofAttachedResource.iterator();
    while (itr.hasNext()) {
      AttarchedResource res = (AttarchedResource) itr.next();
      if (res.uuid.equals(u)) {
        name = "res-" + res.resIndex + "-" + Q_in_Q_tag;
      }
    }
    return name;
  }
}
