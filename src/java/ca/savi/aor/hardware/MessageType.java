// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.hardware;

/**
 * @author yuethomas
 */
public enum MessageType {
  // initialization
  INIT_RQT(0x00, "initial message"),
  INIT_RSP(0x01, "initial response"),
  // resourceList
  LIST_RQT(0x10, "resourceList request"),
  LIST_RSP_NONVERB(0x11, "resourceList response, not verbose"),
  LIST_RSP_VERB(0x12, "resourceList response, verbose"),
  // resourceGet
  GET_RQT(0x20, "resourceGet request"),
  GET_RSP(0x21, "resourceGet response"),
  GET_ERR_UUID(0x2a, "resourceGet error: invalid UUID"),
  GET_ERR_ALLOC(0x2b, "resourceGet error: can't allocate resources"),
  // resourceRelease
  RELEASE_RQT(0x30, "resourceRelease request"),
  RELEASE_RSP(0x31, "resourceRelease response"),
  RELEASE_ERR_UUID(0x3a, "resourceRelease error: invalid UUID"),
  RELEASE_ERR_NOT_PROPERLY_RELEASED(0x3b,
      "resourceRelease error: FPGA not responding"),
  // programResource
  PROGRAM_RQT(0x40, "programResource request"), PROGRAM_TRANS_RDY(0x41,
      "programResource transmission: ready for image"), PROGRAM_TRANS_CORR(
      0x42, "programResource transmission: image verified"), PROGRAM_ERR_TCP(
      0x4a, "programResource error: TCP socket failure"),
  PROGRAM_ERR_CRC(0x4b, "programResource error: image fails CRC check"),
  PROGRAM_ERR_CORRUPT(0x4c, "programResource error: corrupt image"),
  PROGRAM_ERR_UUID(0x4d, "programResource error: invalid UIUD"),
  PROGRAM_ERR_LENGTH(0x4e, "programResource error: file length mismatch"),
  // resourceStatus
  STATUS_RQT(0x50, "resourceStatus request"),
  STATUS_RSP(0x51, "resourceStatus response"),
  STATUS_ERR_UUID(0x52, "resourceStatus error: invalid UUID"),
  STATUS_ERR_INVALID(0x53,
      "resourceStatus error: incorrect response from resource"),
  STATUS_ERR_TIMEOUT(0x54, "resourceStatus error: no response from resource"),
  // setParameters
  SET_PARAMS_RQT(0x60, "set parameters request"), SET_PARAMS_RSP(0x61,
      "set parameters response"), SET_USER_REGS_RQT(0x62,
      "set user registers request"), SET_USER_REGS_RSP(0x63,
      "set user registers response"), GET_USER_REGS_RQT(0x64,
      "get user registers request"), GET_USER_REGS_RSP(0x65,
      "get user registers response"), ADD_VLAN_RQT(0x66, "add vlan to fpga"),
  ADD_VLAN_RSP(0x67, "vlan successfully added"), REMOVE_VLAN_RQT(0x68,
      "remove vlan from fpga"), REMOVE_VLAN_RSP(0x69,
      "vlan successfully removed"), SET_MAC_RQT(0x6a, "set mac address"),
  SET_MAC_RSP(0x6b, "mac address successfully set"),
  VLAN_ERR_INVALID(0x6c, "error"),
  // resource register
  REGISTER_RQT(0xa0, "resource register request"),
  REGISTER_RSP(0xa1, "resource register response"),
  // termination
  TERM_RQT(0xe0, "termination request"),
  TERM_RSP(0xe1, "termination response"), TERM_ERR(0xea,
      "termination error: can't shut down"),
  // generic errors
  ERR_UNKNOWN(0xf0, "error: unknown error"), ERR_INVALID_TYPE(0xf1,
      "error: invalid type"), ERR_UNEXPECTED_TYPE(0xf2,
      "error: unexpected type"), ERR_CRC(0xf3, "error: CRC mismatch"),
  ERR_LOCK(0xf4, "error: requested resource is locked"), ERR_NOT_INIT(0xf5,
      "error: server not initialized; send 0x00"), ERR_UNEXPECTED_CLT(0xf6,
      "error: unexpected client");
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
