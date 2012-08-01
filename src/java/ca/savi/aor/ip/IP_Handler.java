// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.ip;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import ca.savi.aor.fabric.VANI_Fabric;
import ca.savi.aor.messageexceptions.GenericException;

/**
 * @author Keith
 */
public class IP_Handler {
  public VlanType[] VlanList;
  VANI_Fabric f;

  public IP_Handler(VANI_Fabric vf) {
    VlanList = new VlanType[vf.endVlanId];
    for (int i = 0; i < vf.endVlanId; i++)
      VlanList[i] = new VlanType(i);
    f = vf;
  }

  // -----------------------------------------------------------
  // ----------------VLAN MANAGEMENT FUNCTIONS------------------
  public int createNewLocal(int Q_in_Q_tag, boolean rawEthernet)
      throws GenericException, Exception {
    // if Q_in_Q_tag already has a local tag.
    for (int i = 0; i < f.endVlanId; i++) {
      if (f.isReserved(i) || f.isBroadcast(i))
        continue;
      if (VlanList[i].inUse && VlanList[i].Q_in_Q_tag == Q_in_Q_tag)
        return i;
    }
    // attempt to allocate a new tag
    for (int i = 0; i < f.endVlanId; i++) {
      if (f.isReserved(i) || f.isBroadcast(i))
        continue;
      if (!VlanList[i].inUse) {
        VlanList[i].Q_in_Q_tag = Q_in_Q_tag;
        VlanList[i].inUse = true;
        VlanList[i].rawEthernet = rawEthernet;
        return i;
      }
    }
    // No more VLANs to be allocated
    throw new GenericException("Could not allocate a VLAN for local usage");
  }

  public void addUUID(int Q_in_Q_tag, UUID uuid, int resId)
      throws GenericException {
    int localTag;
    localTag = globalToLocal(Q_in_Q_tag);
    if (localTag < 0) {
      throw new GenericException(
          "Error, attempted to add a tag to a Q in Q user that "
              + "is not on this node");
    }
    VlanList[localTag].addUUIDToVlan(uuid, resId);
  }

  public int removeUUIDFromVlan(int Q_in_Q_tag, UUID uuid)
      throws GenericException {
    int localTag;
    localTag = globalToLocal(Q_in_Q_tag);
    if (localTag < 0) {
      throw new GenericException(
          "Error, Q in Q tag supplied is not on this VANI node");
    }
    return VlanList[localTag].clearUUID(uuid);
  }

  public void clearLocalTag(int vlanID) {
    if (vlanID >= 0 && vlanID < f.endVlanId) {
      VlanList[vlanID].inUse = false;
      VlanList[vlanID].clear();
    }
  }

  public void clearIfEmpty(int Q_in_Q_tag) throws GenericException {
    int vlanID = globalToLocal(Q_in_Q_tag);
    if (vlanID >= 0 && vlanID < f.endVlanId) {
      if (VlanList[vlanID].getNumUUIDs() == 0) {
        VlanList[vlanID].inUse = false;
        VlanList[vlanID].clear();
      }
    } else {
      throw new GenericException("Q in Q tag not allocated");
    }
  }

  // -----------------------------------------------------------
  // ---------------VLAN INFORMATION FUNCTIONS------------------
  public boolean isInUse(int vlanid) throws GenericException {
    if (vlanid < 0 || vlanid > f.endVlanId)
      throw new GenericException("Error, vlan id out of bounds");
    return VlanList[vlanid].inUse;
  }

  public boolean isRawEthernet(int vlanid) throws GenericException {
    if (vlanid < 0 || vlanid > f.endVlanId)
      throw new GenericException("Error, vlan id out of bounds");
    return VlanList[vlanid].rawEthernet;
  }

  // -----------------------------------------------------------
  // ----------------TAG TRANSLATION FUNCTIONS------------------
  public int globalToLocal(int Q_in_Q_tag) {
    for (int i = 0; i < f.endVlanId; i++) {
      if (f.isReserved(i) || f.isBroadcast(i))
        continue;
      if (VlanList[i].inUse && VlanList[i].Q_in_Q_tag == Q_in_Q_tag)
        return i;
    }
    return (-1);
  }

  public int localToGlobal(int vlanID) throws GenericException {
    if (vlanID < 0 || vlanID > f.endVlanId)
      throw new GenericException("Error, vlan id out of bounds");
    if (!VlanList[vlanID].inUse)
      throw new GenericException("Error, vlanID is not currently in use");
    return (VlanList[vlanID].Q_in_Q_tag);
  }

  // -----------------------------------------------------------
  // ---------------------IP FUNCTIONS--------------------------
  public boolean releaseIP(String IP) {
    int vlanid, resourceID;
    try {
      vlanid = vlanFromIP(IP);
      resourceID = RIDFromIP(IP);
    } catch (GenericException e) {
      return false;
    }
    VlanList[vlanid].releaseRIDNew(resourceID);
    return true;
  }

  public String getIP(int Q_in_Q_tag, UUID uuid) throws GenericException {
    boolean found = false;
    // cycle through all vlan ids
    int localTag;
    localTag = globalToLocal(Q_in_Q_tag);
    // If tag was not found, and there is at least one remaining unassigned vlan
    // id.
    if (localTag < 0) {
      throw new GenericException(
          "Error, attempted to get an IP for a Q in Q user "
              + "that is not on this node");
    } else {
      IP_And_Vlan temp =
          new IP_And_Vlan(localTag, VlanList[localTag].getRIDOfResource(uuid));
      return (temp.IP.getHostAddress());
    }
  }

  public String findIP(UUID uuid, int vlanId) {
    if (vlanId > 0 && vlanId < f.endVlanId && VlanList[vlanId] != null)
      return (VlanList[vlanId].findUuidIp(uuid));
    return "0.0.0.0";
  }

  public void clearUUID(UUID uuid) {
    for (int i = 0; i < f.endVlanId; i++)
      VlanList[i].clearUUID(uuid);
  }

  // -----------------------------------------------------------
  // --------------------HELPER FUNCTIONS-----------------------
  public int vlanFromIP(String IP) throws GenericException {
    InetAddress ipAddr;
    int vlanid;
    byte[] byteAddr;
    try {
      ipAddr = InetAddress.getByName(IP);
      byteAddr = ipAddr.getAddress();
      vlanid = ((int) byteAddr[1] << 16) + (int) ((byteAddr[2] & 0xf0) >> 4);
    } catch (UnknownHostException e) {
      throw new GenericException(e.getMessage());
    }
    return (vlanid);
  }

  public static int RIDFromIP(String IP) throws GenericException {
    InetAddress ipAddr;
    int resourceID;
    byte[] byteAddr;
    try {
      ipAddr = InetAddress.getByName(IP);
      byteAddr = ipAddr.getAddress();
      resourceID = ((int) (byteAddr[2] & 0x0f) << 8) + byteAddr[3];
    } catch (UnknownHostException e) {
      throw new GenericException(e.getMessage());
    }
    return (resourceID);
  }

  public UUID getUUIDFromIP(String IP) throws GenericException {
    InetAddress ipAddr;
    int vlanid, resourceID;
    vlanid = vlanFromIP(IP);
    resourceID = RIDFromIP(IP);
    if (vlanid > 0 && vlanid < f.endVlanId)
      return VlanList[vlanid].getUUIDofAResource(resourceID);
    /*
     * if(VlanList[vlanid].IpList[resourceID] != null)
     * return(VlanList[vlanid].IpList[resourceID].uuid);
     */
    throw new GenericException(
        "Error, IP address supplied has not been allocated");
  }

  public void addUUID(int Q_in_Q_tag, UUID uuid) {
  }

  public boolean AnyRealResourceConnectedToVlan(int vlanid)
      throws GenericException {
    if (vlanid > 0 && vlanid < f.endVlanId)
      return VlanList[vlanid].AnyRealResourceConnectedToVlan();
    throw new GenericException("Error, vlan specified is out of range");
  }
}