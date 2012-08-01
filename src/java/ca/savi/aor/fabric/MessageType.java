// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.fabric;

/**
 * @author yuethomas
 */
public enum MessageType {
  INIT_RQT(0x10, "Initialize a switch"), INIT_RSP(0x11,
      "Switch initialized successfully"), SET_MAX_VLANS_RQT(0x20,
      "attempt to set maximum allowed vlanid"), SET_MAX_VLANS_RSP(0x21,
      "maximum vlan successfully received"), SET_MAC_ADDRESS_RQT(0x22,
      "set the mac address for a port"), SET_MAC_ADDRESS_RSP(0x23,
      "mac address successfully set"), FREE_MAC_ADDRESS_RQT(0x24,
      "remove the mac address rule on a port"), FREE_MAC_ADDRESS_RSP(0x25,
      "mac address rule removed"),
  DELETE_ALL_VLANS_RQT(0x30, "delete all current vlans"),
  DELETE_ALL_VLANS_RSP(0x31, "All vlans successfully deleted"),
  CREATE_VLAN_RQT(0x32, "attempt to create a vlan"),
  CREATE_VLAN_RSP(0x33, "vlan successfully created"),
  DELETE_VLAN_RQT(0x34, "delete a vlan"),
  DELETE_VLAN_RSP(0x35, "vlan successfully deleted"),
  SET_VLAN_PARTICIPATION_RQT(0x36,
      "Attempt to set ports that participate in a vlan"),
  SET_VLAN_PARTICIPATION_RSP(0x37, "Successfully set the vlan ports"),
  // network-related messages
  USER_VLANID_RQT(0x50, "Ask the name of the user's vlan id"), USER_VLANID_RSP(
      0x51, "Respond with name of user's vlan id"),
  NETWORK_DELETE_VLAN_RQT(0x52,
      "Alert the network that user no longer has resources on this node"),
  NETWORK_DELETE_VLAN_RSP(0x53,
      "Respond that network is aware user no longer has resources"),
  ERR_NO_SUCH_USER(0x5a, "User does not exist"), TERM_RQT(0xe0,
      "Terminate subagent"), TERM_RSP(0xe1, "subagent terminated"),
  ERR_UNKNOWN(0xf0, "unknown error"), ERR_INVALID_TYPE(0xf1,
      "error, unknown request type"), ERR_NOT_INIT(0xf5,
      "error: subagent has not been send the maximum vlan"),
  ERR_INVALID_PAYLEN(0xf7, "error: payload length too small or too large"),
  ERR_INVALID_UUID(0xfa, "error: invalid uuid"), ERR_INVALID_VALUE(0xff,
      "error: sent in an invalid parameter");
  private byte type;
  private String description;

  MessageType(int t, String d) {
    type = (byte) t;
    description = d;
  }

  public byte getType() {
    return type;
  }

  public boolean isErrorType() {
    return (type >= 0xf0);
  }

  public String getDescription() {
    return description;
  }

  public static MessageType findType(byte t) {
    return findType((int) t);
  }

  public static MessageType findType(int t) {
    for (MessageType mt : MessageType.values())
      if (mt.type == (byte) t)
        return mt;
    return null;
  }

  public static MessageType findTypeByName(String s) {
    try {
      return MessageType.valueOf(s);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}