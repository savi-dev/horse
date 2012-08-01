// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.storage;

/**
 * @author yuethomas
 */
public enum MessageType {
  // initialization/flow control
  INIT_RQT(0x00, "initial message"),
  INIT_RSP(0x01, "initial response"),
  SEND_NEXT_PARAM(0x02, "send the next parameter"),
  NEXT_PARAM_RQT(0x03, "sending next parameter"),
  ADD_SERV_RQT(0x06, "request to add a server to list"),
  ADD_SERV_RSP(0x07, "successfully added server"),
  SERV_FAILED_RQT(0x08, "one (or more) of the subagents have died"),
  SERV_FAILED_RSP(0x09, "successfully received SERV_FAILED msg"),
  NEED_INIT_RQT(0x0A, "one (or more) of the subagents requres initialization"),
  NEED_INIT_RSP(0x0B, "successfully received NEED_INIT msg"),
  ADD_VLAN_RQT(0x0C, "request to add vlan to fileserver"),
  ADD_VLAN_RSP(0x0D, "response to add vlan to fileserver"),
  REM_VLAN_RQT(0x0E, "request to remove vlan from fileserver"),
  REM_VLAN_RSP(0x0F, "response to remove vlan from fileserver"),
  // resourceList
  LIST_RQT(0x10, "resourceList request"),
  LIST_RSP_NONVERB(0x11, "resourceList response, not verbose"),
  LIST_RSP_VERB(0x12, "resourceList response, verbose"),
  // resourceGet
  GET_RQT(0x20, "resourceGet request"),
  GET_RSP(0x21, "resourceGet response"),
  GET_FILE_RQT(0x22, "get a file"),
  GET_FILE_RSP(0x23, "file ready to be retrieved"),
  GET_ERR_UUID(0x2a, "resourceGet error: invalid UUID"),
  GET_ERR_ALLOC(0x2b, "resourceGet error: can't allocate resources"),
  // resourceRelease
  RELEASE_RQT(0x30, "resourceRelease request"),
  RELEASE_RSP(0x31, "resourceRelease response"),
  RELEASE_ERR_UUID(0x3a, "resourceRelease error: invalid UUID"),
  RELEASE_ERR_NOT_PROPERLY_RELEASED(0x3b,
      "resourceRelease error: FPGA not responding"),
  // programResource
  PROGRAM_RQT(0x40, "programResource request"),
  PROGRAM_TRANS_RDY(0x41, "programResource transmission: ready for image"),
  PROGRAM_TRANS_CORR(0x42, "programResource transmission: image verified"),
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
  // Copy File Request
  COPY_FILE_RQT(0x60, "copy file request"),
  COPY_FILE_RSP(0x61, "copy file response"),
  PUT_FILE_RQT(0x63, "put a file"),
  PUT_FILE_RSP(0x65, "Ready to put file"),
  // Make Directory
  MK_DIR_RQT(0x70, "Make directory specified by path"),
  MK_DIR_RSP(0x71, "Directory made successfully"),
  MK_DIR_LOC_DNE_ERROR(0x72, "Error, path to directory does not exist"),
  MK_DIR_AE_ERROR(0x73, "Error, directory already exists"),
  // Delete File
  DELETE_FILE_RQT(0x80, "Delete file specified by path"),
  DELETE_FILE_RSP(0x81, "File successfully deleted"),
  // Delete Directory
  DELETE_DIR_RQT(0x90, "delete a directory, fail if not empty"),
  FORCE_DELETE_DIR_RQT(0x91, "delete a directory even if not empty"),
  DELETE_DIR_RSP(0x92, "directory successfully deleted"),
  // Add user/delete user
  ADD_USER_RQT(0x93, "Add a user"),
  ADD_USER_RSP(0x94, "Requested user added"),
  DELETE_USER_RQT(0x95, "Delete a user"),
  DELETE_USER_RSP(0x96, "User deleted successfully"),
  // Update User
  UPDATE_USER_RQT(0x97, "attempt to update user parameters"),
  UPDATE_USER_RSP(0x98, "successfully updated user parameters"),
  GET_USER_PARAMS_RQT(0x9a, "Attemtping to retrieve the user parameters"),
  GET_USER_PARAMS_RSP(0x9b, "successfully retrieved user parameters"),
  // resource register
  REGISTER_RQT(0xa0, "resource register request"),
  REGISTER_RSP(0xa1, "resource register response"),
  // Transaction added
  TRANS_ADDED_RSP(0xb0, "successfully added transaction to manager"),
  // List Files
  LIST_FILES_RQT(0xc0, "list files in directory specified"),
  LIST_FILES_RSP(0xc1, "correctly listing files in specified directory"),
  FILE_NAME_RQT(0xc2, "sending the name of a file in directory"),
  DIR_NAME_RQT(0xc3, "sending the name of a directory in directory"),
  // generic errors
  HANDSHAKE_FAILED(0xd0, "Communication failure"),
  ERR_NOT_ENOUGH_SPACE(0xd1, "error: attempted to use more space than allowed"),
  ERR_TOO_MANY_ARGS(0xd2, "error: transaction contains too many arguments"),
  // termination
  TERM_RQT(0xe0, "termination request"),
  TERM_RSP(0xe1, "termination response"),
  TERM_SERVER_RQT(0xe2, "stop server request"),
  TERM_SERVER_RSP(0xe3, "stop server response"),
  TERM_ERR(0xea, "termination error: can't shut down"),
  // generic errors
  ERR_UNKNOWN(0xf0, "error: unknown error"), ERR_INVALID_TYPE(0xf1,
      "error: invalid type"), ERR_UNEXPECTED_TYPE(0xf2,
      "error: unexpected type"), ERR_CRC(0xf3, "error: CRC mismatch"),
  ERR_LOCK(0xf4, "error: requested resource is locked"), ERR_NOT_INIT(0xf5,
      "error: server not initialized; send 0x00"), ERR_UNEXPECTED_CLT(0xf6,
      "error: unexpected client"), ERR_PASSWORD_INCORRECT(0xf7,
      "error: password returned was wrong"), ERR_UNEXPECTED_FILENAME(0xf8,
      "error: filename specified is not valid."), ERR_FILE_ALREADY_EXISTS(0xf9,
      "error: attempted to create a file/directory that already exists"),
  ERR_DIRECTORY_NOT_EMPTY(0xfa,
      "error: attempted to delete a directory that is not empty"),
  ERR_PARAM_TOO_LARGE(0xfb,
      "error: attempted to send in a parameter that was too large"),
  ERR_USER_ALREADY_EXISTS(0xfc,
      "error: attempted to create a user that already exists"),
  ERR_NEGATIVE_NUMBER(0xfd,
      "error: a negative number was specified when only a positive "
          + "number was allowed"), INTERNAL_DATABASE_ERR(0xfe,
      "error: problem with the AOR's internal database"), INTERNAL_SERVER_ERR(
      0xff, "error: problem with one of the servers in the AOR");
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
