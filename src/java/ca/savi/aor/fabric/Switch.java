// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.fabric;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.UUID;

import ca.savi.aor.messageexceptions.GenericException;

/**
 * @author Keith
 */
public class Switch {
  public String name;
  String ip;
  VANI_Fabric p;
  public int numPorts, port;
  private boolean initialized;
  Port[] ports;
  public Socket subSocket;
  UUID switchUUID;

  public Switch(String id, String addr, int switchPort, Socket sock, int numP,
      VANI_Fabric parent, String filename, UUID uuid) {
    name = id;
    ip = addr;
    numPorts = numP;
    p = parent;
    ports = new Port[numPorts];
    for (int i = 0; i < numP; i++) {
      ports[i] = new Port(this);
    }
    port = switchPort;
    switchUUID = uuid;
    subSocket = sock;
  }

  public boolean init() {
    try {
      // Message Format:
      // 16 bytes uuid
      // 4 bytes IP
      // 2 bytes switchPort
      // 2 bytes numPorts
      byte[] pay = new byte[8];
      System.arraycopy(InetAddress.getByName(ip).getAddress(), 0, pay, 0, 4);
      pay[4] = (byte) ((port >> 8) & 0xff);
      pay[5] = (byte) (port & 0xff);
      pay[6] = (byte) ((numPorts >> 8) & 0xff);
      pay[7] = (byte) (numPorts & 0xff);
      MessageFactory.sendMessage(subSocket, switchUUID, pay,
          MessageType.INIT_RQT, MessageType.INIT_RSP);
    } catch (GenericException e) {
      return false;
    } catch (UnknownHostException e) {
      System.out.println("Error, unknown host exception: " + e);
      return false;
    }
    initialized = resetVlanConfig();
    return (initialized);
  }

  public boolean resetVlanConfig() {
    initialized = true;
    // Delete all VLANs that are not reserved
    for (int i = 0; i < numPorts; i++) {
      if (ports[i] != null) {
        Iterator itr = ports[i].uuidList.iterator();
        while (itr.hasNext()) {
          UUIDType id = (UUIDType) itr.next();
          for (int j = 0; j < p.endVlanId; j++) {
            if (!p.isReserved(j))
              try {
                ports[i].removeVLAN(id.uuid, j);
              } catch (GenericException e) {
              }
          }
        }
      }
    }
    // Add broadcasts to datatypes
    addBroadcasts();
    try {
      // DELETE ALL NON-RESERVED VLANS
      for (int i = 0; i < p.endVlanId; i++) {
        int vlanid = i;
        if (!p.isReserved(vlanid)) {
          byte[] pay = new byte[2];
          pay[0] = (byte) (vlanid >> 8);
          pay[1] = (byte) (vlanid & 0x000000FF);
          MessageFactory.sendMessage(subSocket, switchUUID, pay,
              MessageType.DELETE_VLAN_RQT, MessageType.DELETE_VLAN_RSP);
        } else {
          System.out.println("Not deleting reserved vlan: " + vlanid);
        }
      }
      int byteArrSize = (numPorts / 8) + 1;
      boolean createVlanFlag;
      // loop through each vlan
      for (int i = 0; i < p.endVlanId; i++) {
        byte[] pay = new byte[byteArrSize];
        createVlanFlag = false;
        for (int k = 0; k < pay.length; k++)
          pay[k] = 0;
        // loop through each port
        for (int j = 0; j < numPorts; j++) {
          if (ports[j] == null || !ports[j].valid)
            clearByteArrBit(pay, j);
          else if (ports[j].isOnVlan(i)) {
            addPortToByteArr(pay, j);
            createVlanFlag = true;
          } else
            clearByteArrBit(pay, j);
        }
        if (createVlanFlag) {
          String out = "Creating VLAN " + i;
          System.out.println(out);
          if (!createVlan(i))
            return false;
          out =
              "VLAN " + i + " created, setting participation: "
                  + Integer.toString(pay[0], 16) + " "
                  + Integer.toString(pay[1], 16) + " "
                  + Integer.toString(pay[2], 16);
          System.out.println(out);
          MessageFactory.setParticipation(subSocket, switchUUID, i, pay,
              byteArrSize);
          System.out.println("Participation set");
        }
      }
      // remove all MAC address rules, set new ones if necessary
      for (int i = 0; i < numPorts; i++) {
        byte[] pay = new byte[2];
        byte[] largePay = new byte[8];
        pay[0] = (byte) ((i >> 8) & 0x000000ff);
        pay[1] = (byte) ((i) & 0x000000ff);
        MessageFactory.sendMessage(subSocket, switchUUID, pay,
            MessageType.FREE_MAC_ADDRESS_RQT, MessageType.FREE_MAC_ADDRESS_RSP);
        // if MAC address has been set, let the subagent know.
        for (int j = 0; j < 6; j++) {
          if (ports[i].macAddress[j] != 0) {
            System.arraycopy(pay, 0, largePay, 0, 2);
            System.arraycopy(ports[i].macAddress, 0, largePay, 2, 6);
            MessageFactory.sendMessage(subSocket, switchUUID, largePay,
                MessageType.SET_MAC_ADDRESS_RQT,
                MessageType.SET_MAC_ADDRESS_RSP);
            break;
          }
        }
      }
    } catch (GenericException e) {
      String out = "Error: " + e;
      System.out.println(out);
      return false;
    }
    printPorts();
    return true;
  }

  public void freeMacAddress(int portBit) throws GenericException {
    byte[] pay = new byte[2];
    pay[0] = (byte) ((portBit >> 8) & 0x000000ff);
    pay[1] = (byte) ((portBit) & 0x000000ff);
    MessageFactory.sendMessage(subSocket, switchUUID, pay,
        MessageType.FREE_MAC_ADDRESS_RQT, MessageType.FREE_MAC_ADDRESS_RSP);
  }

  public byte[] findMac(UUID uuid, int interfaceIndex) throws Exception {
    for (int i = 0; i < numPorts; i++) {
      if (!ports[i].valid)
        continue;
      if (ports[i].containsUUID(uuid)
          && ports[i].interfaceIndex == interfaceIndex)
        return ports[i].macAddress;
    }
    throw new Exception("Error, unable to find uuid/interface index");
  }

  public void sendMacAddress(int portBit) throws GenericException {
    freeMacAddress(portBit);
    byte[] pay = new byte[8];
    pay[0] = (byte) ((portBit >> 8) & 0x000000ff);
    pay[1] = (byte) ((portBit) & 0x000000ff);
    System.arraycopy(ports[portBit].macAddress, 0, pay, 2, 6);
    MessageFactory.sendMessage(subSocket, switchUUID, pay,
        MessageType.SET_MAC_ADDRESS_RQT, MessageType.SET_MAC_ADDRESS_RSP);
  }

  private void addPortToByteArr(byte[] array, int port) {
    int portByte = port / 8;
    int portOff = port % 8;
    if (array.length > portByte)
      array[portByte] |= (1 << (7 - portOff));
  }

  private void clearByteArrBit(byte[] array, int port) {
    int portByte = port / 8;
    int portOff = port % 8;
    if (array.length > portByte) {
      String out;
      byte mask = (byte) 0xff;
      mask -= (byte) (1 << (7 - portOff));
      array[portByte] &= mask;
    }
  }

  public void printPorts() {
    if (!initialized)
      return;
    for (int i = 0; i < numPorts; i++) {
      if (ports[i] != null) {
        if (ports[i].valid) {
          Iterator itr = ports[i].uuidList.iterator();
          while (itr.hasNext()) {
            UUIDType cur = (UUIDType) itr.next();
            System.out.println("Port " + i + ": uuid " + cur.uuid.toString()
                + " accessType " + cur.type.toString());
            for (int j = 0; j < p.endVlanId; j++) {
              if (cur.onVlan(j))
                System.out.println("VLAN" + j);
            }
          }
        } else
          System.out.println("Port is invalid");
      } // end if (ports[i] != null)
    } // end for
  }

  public void addBroadcasts() {
    for (int i = 0; i < p.endVlanId; i++) {
      if (p.isBroadcast(i)) {
        for (int j = 0; j < numPorts; j++) {
          if (ports[j] == null || !ports[j].valid)
            continue;
          else
            ports[j].addBroadcastVLAN(i);
        }
      }
    }
  }

  private void setParticipation(int vlanid) throws GenericException {
    // Number of bytes required to describe which ports should be on a VLAN
    int byteArrSize = (numPorts / 8) + 1;
    byte[] bitstream = new byte[byteArrSize];
    // build a bitstream, value in bitstream = 1 if port is on the vlan being
    // modified, 0 otherwise.
    for (int i = 0; i < byteArrSize; i++) {
      bitstream[i] = 0;
      for (int j = 0; j < 8; j++) {
        if (i * 8 + j >= numPorts)
          break;
        if (ports[i * 8 + j] == null)
          continue;
        if (ports[i * 8 + j].isOnVlan(vlanid))
          bitstream[i] |= (1 << (7 - j));
      }
      int printable = 0;
      printable = (0x000000FF & (int) bitstream[i]);
      // System.out.println("Byte = " + printable);
    }
    // communicate with subagent here
    MessageFactory.setParticipation(subSocket, switchUUID, vlanid, bitstream,
        byteArrSize);
  }

  public boolean addVlanToPort(UUID id, int vlanid, boolean isRawEthernet)
      throws GenericException {
    if (!initialized)
      return (false);
    boolean returnVal = true, foundPort = false;
    // find the port
    for (int i = 0; i < numPorts; i++) {
      if (ports[i] != null) {
        if (ports[i].containsUUID(id)) {
          foundPort = true;
          // tell the port to add this vlan
          if (!ports[i].addVLAN(id, vlanid))
            returnVal = false;
        }
      }// end if ports[i] != null
    }// end for i
     // attempt to add to either bridge or gateway
    for (int i = 0; i < numPorts; i++) {
      if (ports[i] != null) {
        UUID bridgeID = ports[i].containsBridge();
        UUID gatewayID = ports[i].containsGateway();
        if (isRawEthernet && (bridgeID != null)) {
          ports[i].addVLAN(bridgeID, vlanid);
        } else if (!isRawEthernet && (gatewayID != null)) {
          ports[i].addVLAN(gatewayID, vlanid);
        }
      } // end if ports[i] != null
    } // end for i
    // update switch configuration
    setParticipation(vlanid);
    return true;
  }

  public boolean addVlanToPort(String id, int vlanid, boolean rawEthernet)
      throws GenericException {
    UUID uuid;
    try {
      uuid = UUID.fromString(id);
      return addVlanToPort(uuid, vlanid, rawEthernet);
    } catch (IllegalArgumentException e) {
      throw new GenericException(e.getMessage());
    }
  }

  public boolean removeVlanFromPort(UUID id, int vlanid, boolean isRawEthernet)
      throws GenericException {
    if (!initialized)
      return (false);
    boolean returnVal = true, foundPort = false, existedBefore = false;
    if (p.vlanExistsIgnoreALLs(vlanid))
      existedBefore = true;
    // clear the vlan from the uuid
    for (int i = 0; i < numPorts; i++) {
      if (ports[i] == null)
        continue;
      if (ports[i].containsUUID(id)) {
        foundPort = true;
        // if port is on vlan
        if (ports[i].isOnVlan(vlanid)) {
          // tell the port to remove this vlan
          if (!ports[i].removeVLAN(id, vlanid))
            returnVal = false;
          setParticipation(vlanid);
        }
      }
    }// end for (i)
    if (!p.vlanExistsIgnoreALLs(vlanid) && existedBefore) {
      p.deleteVlan(vlanid);
    }
    return (returnVal);
  }

  public boolean
      removeVlanFromPort(String id, int vlanid, boolean isRawEthernet)
          throws GenericException {
    UUID uuid;
    try {
      uuid = UUID.fromString(id);
      return removeVlanFromPort(uuid, vlanid, isRawEthernet);
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public boolean clearUUIDVlans(UUID id) throws GenericException {
    boolean returnVal = true;
    // Remove all non-broadcast, non-reserved vlanids
    for (int i = 0; i < p.endVlanId; i++) {
      int vlanid = i;
      // when clearing, remove all BRIDGE AND GATEWAY entries
      if (!p.isReserved(vlanid) && !p.isBroadcast(vlanid)) {
        removeVlanFromPort(id, vlanid, false);
        removeVlanFromPort(id, vlanid, true);
      }
    }
    return returnVal;
  }

  public boolean clearUUIDVlans(String id) throws GenericException {
    UUID uuid;
    try {
      uuid = UUID.fromString(id);
      return clearUUIDVlans(uuid);
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public boolean createVlan(int vlanid) {
    if (!initialized)
      return false;
    try {
      byte[] pay = new byte[2];
      pay[0] = (byte) (vlanid >> 8);
      pay[1] = (byte) (vlanid & 0x000000FF);
      MessageFactory.sendMessage(subSocket, switchUUID, pay,
          MessageType.CREATE_VLAN_RQT, MessageType.CREATE_VLAN_RSP);
      initializePortParticipation(vlanid);
    } catch (GenericException e) {
      return false;
    }
    return true;
  }

  public void deleteVlan(int vlanid) throws GenericException {
    if (!initialized)
      return;
    try {
      byte[] pay = new byte[2];
      pay[0] = (byte) (vlanid >> 8);
      pay[1] = (byte) (vlanid & 0x000000FF);
      MessageFactory.sendMessage(subSocket, switchUUID, pay,
          MessageType.DELETE_VLAN_RQT, MessageType.DELETE_VLAN_RSP);
    } catch (GenericException e) {
    }
    for (int i = 0; i < numPorts; i++) {
      Iterator itr = ports[i].uuidList.iterator();
      while (itr.hasNext()) {
        UUIDType cur = (UUIDType) itr.next();
        ports[i].removeVLAN(cur.uuid, vlanid);
      } // end while
    } // end for
  }

  private void initializePortParticipation(int vlanid) throws GenericException {
    // Number of bytes required to describe which ports should be on a VLAN
    for (int i = 0; i < numPorts; i++) {
      Iterator itr = ports[i].uuidList.iterator();
      while (itr.hasNext()) {
        UUIDType cur = (UUIDType) itr.next();
        if (cur.type == AccessTypes.ALL)
          ports[i].addVLAN(cur.uuid, vlanid);
      } // end while
    } // end for
    setParticipation(vlanid);
  }

  public boolean vlanExists(int vlanid) {
    for (int i = 0; i < numPorts; i++) {
      System.out.println("Checking port " + i);
      if (ports[i] == null)
        continue;
      if (ports[i].isOnVlan(vlanid)) {
        System.out.println("Returning true");
        return true;
      }
    }
    return false;
  }

  public boolean vlanExistsIgnoreALLs(int vlanid) {
    for (int i = 0; i < numPorts; i++) {
      if (ports[i] == null)
        continue;
      if (ports[i].isOnVlanIgnoreALLs(vlanid))
        return true;
    }
    return false;
  }

  public void terminate() {
    try {
      MessageFactory.sendMessage(subSocket, switchUUID, null,
          MessageType.TERM_RQT, MessageType.TERM_RSP);
    } catch (GenericException e) {
    }
    for (int i = 0; i < numPorts; i++) {
      ports[i] = null;
    }
    initialized = false;
  }
}