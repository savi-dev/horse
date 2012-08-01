// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.processing;

/**
 * @author yuethomas
 */
public enum MessageType {
  // initialization
  INIT_RQT(0x00, "initial message"),
  INIT_RSP(0x01, "initial response"),
  INIT_HAVE_MORE(0x02, "initial response, have more UUIDs to send"),
  INIT_NEXT(0x03, "init ready for more UUIDs"),
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
  REBOOT_RQT(0x32, "resource reboot request"),
  REBOOT_RSP(0x33, "resource reboot response"),
  RESET_RQT(0x34, "reset resource to locally saved image"),
  RESET_RSP(0x35, "resource reset to locally saved image"),
  RELEASE_ERR_UUID(0x3a, "resourceRelease error: invalid UUID"),
  RELEASE_ERR_NOT_PROPERLY_RELEASED(0x3b,
      "resourceRelease error: FPGA not responding"),
  // programResource
  PROGRAM_RQT(0x40, "programResource request"), PROGRAM_TRANS_RDY(0x41,
      "programResource transmission: ready for image"), PROGRAM_TRANS_CORR(
      0x42, "programResource transmission: image verified"), SECURITY_FILE_RQT(
      0x43, "Sending security file for ssh"), SECURITY_TRANS_READY(0x44,
      "Ready to receive security file"),
  SECURITY_FILE_RSP(0x45, "Security file for ssh received"),
  DEL_SECURITY_FILE_RQT(0x46, "Delete current security file"),
  DEL_SECURITY_FILE_RSP(0x47, "Deleted current security file"),
  PROGRAM_ERR_TCP(0x4a, "programResource error: TCP socket failure"),
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
  // get image
  GET_IMG_RQT(0x60, "Get the current image from subagent"),
  GET_IMG_RSP(0x61, "Get Img response, starting to send image"),
  GET_IMG_CMP(0x62, "Completed sending the image"),
  SAVE_IMG_RQT(0x63, "Save the current vserver as an image file"),
  SAVE_IMG_RSP(0x64, "Successfully saved image file"),
  FILE_DNE_ERR(0x6a, "Error, no such file"),
  // set parameters
  SET_VLAN_ID_RQT(0x70, "Set the vlanID of the processing resource"),
  SET_VLAN_ID_RSP(0x71, "vlanID properly set"), SET_IP_RQT(0x72,
      "Set the IP of the processing resource"), SET_IP_RSP(0x73,
      "IP properly set"), CLR_IP_RQT(0x74, "Release the external IP address"),
  CLR_IP_RSP(0x75, "External IP address released"),
  ADD_VLAN_RQT(0x76, "Add vlan to vserver"),
  ADD_VLAN_RSP(0x77, "Vlan added"),
  REMOVE_VLAN_RQT(0x78, "Remove vlan from vserver"),
  REMOVE_VLAN_RSP(0x79, "Vlan removed from vserver"),
  // resource register
  REGISTER_RQT(0xa0, "resource register request"),
  REGISTER_RSP(0xa1, "resource register response"),
  // termination
  TERM_RQT(0xe0, "termination request"),
  TERM_RSP(0xe1, "termination response"), TERM_SERVER_RQT(0xe2,
      "stop server request"), TERM_SERVER_RSP(0xe3, "stop server response"),
  TERM_ERR(0xea, "termination error: can't shut down"),
  // generic errors
  ERR_UNKNOWN(0xf0, "error: unknown error"), ERR_INVALID_TYPE(0xf1,
      "error: invalid type"), ERR_UNEXPECTED_TYPE(0xf2,
      "error: unexpected type"), ERR_CRC(0xf3, "error: CRC mismatch"),
  ERR_LOCK(0xf4, "error: requested resource is locked"), ERR_NOT_INIT(0xf5,
      "error: server not initialized; send 0x00"), ERR_UNEXPECTED_CLT(0xf6,
      "error: unexpected client"), ERR_INVALID_PAYLEN(0xf7,
      "error: payload length too small or too large"), CONFIG_ERR(0xf8,
      "error: Internal server configuration error"), ERR_PAYVAL_INVALID(0xf9,
      "error: value specified in payload was invalid"), ERR_RESOURCE_BUSY(0xfA,
      "error: resource is already handling a request");
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
