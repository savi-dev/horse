// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.storage;

import java.io.IOException;
import java.net.Socket;

import ca.savi.aor.messageexceptions.GenericException;

// import mylogger.logger.GOELogger;
/**
 * @author Keith
 */
public class MessageFactory {
  // -----------------------------------------------------------
  // ---------------SERVICE MANAGEMENT FUNCTIONS----------------
  // send an initRequest message
  public static void initRequest(Socket s, int numServs)
      throws GenericException {
    try {
      System.out.println("Sending init");
      Message m = new Message(MessageType.INIT_RQT);
      // 2 byte payload, maximum of 65536 'servers' per web service
      byte[] Payload =
          new byte[] { (byte) (numServs >> 24), (byte) (numServs >> 16),
              (byte) (numServs >> 8), (byte) (numServs) };
      m.addPayload(Payload);
      m.send(s);
      System.out.println("Sent init");
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // -----------------------------------------------------------
  // receive an initResponse message
  public static void initResponse(Socket s) throws GenericException {
    System.out.println("Receiving init");
    Message m = Message.receive(s);
    System.out.println("Init received");
    MessageType t = m.getMessageType();
    if (t == MessageType.INIT_RSP)
      return;
    else if (t.isErrorType())
      throw new GenericException("Server encountered an " + t.getDescription());
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  // -----------------------------------------------------------
  // send a terminateRequest message
  public static void terminateRequest(Socket s) throws IOException {
    byte[] b = new byte[4];
    Message m = new Message(MessageType.TERM_RQT);
    b[0] = 0;
    b[1] = 0;
    b[2] = 0;
    b[3] = 0;
    m.addPayload(b);
    m.send(s);
  }

  // -----------------------------------------------------------
  // receive a terminateResponse message
  public static void terminateResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.TERM_RSP)
      return;
    else if (t == MessageType.TERM_ERR)
      throw new GenericException(t.getDescription());
    else if (t.isErrorType())
      throw new GenericException("Server encountered an " + t.getDescription());
    else
      throw new GenericException("Server's response is of an unexpected type.");
  }

  // -----------------------------------------------------------
  // ---------------INFORMATIONAL FUNCTIONS---------------------
  public static void listFilesRequest(Socket s, String[] args, int numArgs)
      throws GenericException {
    try {
      sendArgs(s, args, numArgs, MessageType.LIST_FILES_RQT);
    } catch (GenericException e) {
      throw new GenericException("Error " + e);
    }
  }

  // -----------------------------------------------------------
  public static listFilesRequestType listFilesResponse(Socket s)
      throws GenericException {
    byte[] payload;
    int numFiles;
    Message m = Message.receive(s);
    if (m.getMessageType() != MessageType.LIST_FILES_RSP)
      throw new GenericException("Error, received invalid type: "
          + m.getMessageType());
    else {
      payload = m.getPayload();
      numFiles =
          (payload[0] << 24) + (payload[1] << 16) + (payload[2] << 8)
              + payload[3];
    }
    System.out.println(numFiles + " files to be received");
    listFilesRequestType fileList = new listFilesRequestType(numFiles);
    for (int i = 0; i < numFiles; i++) {
      // m.setMessageType(MessageType.SEND_NEXT_PARAM);
      m = new Message(MessageType.SEND_NEXT_PARAM);
      try {
        m.send(s);
      } catch (IOException e) {
        throw new GenericException("error " + e);
      }
      m = Message.receive(s);
      if (m.getMessageType() != MessageType.FILE_NAME_RQT
          && m.getMessageType() != MessageType.DIR_NAME_RQT) {
        throw new GenericException("Error, received invalid type: "
            + m.getMessageType());
      } else {
        payload = m.getPayload();
        fileList.name[i] = byteArrayToString(payload);
        if (m.getMessageType() == MessageType.FILE_NAME_RQT) {
          fileList.isDir[i] = false;
          System.out.println("File: " + fileList.name[i]);
        } else {
          fileList.isDir[i] = true;
          System.out.println("Directory: " + fileList.name[i]);
        }
      }
    } // end for
    return (fileList);
  }

  // -----------------------------------------------------------
  // ---------------FILE MANIPULATION FUNCTIONS-----------------
  public static void deleteFileRequest(Socket s, byte[][] args, int numArgs)
      throws GenericException {
    byte[] p = new byte[4];
    p[0] = (byte) ((numArgs >> 24) & 0xff);
    p[1] = (byte) ((numArgs >> 16) & 0xff);
    p[2] = (byte) ((numArgs >> 8) & 0xff);
    p[3] = (byte) (numArgs & 0xff);
    try {
      sendArg(s, p, MessageType.DELETE_FILE_RQT);
      sendArgs(s, args, numArgs, MessageType.NEXT_PARAM_RQT);
    } catch (GenericException e) {
      throw new GenericException("Error " + e);
    }
  }

  // -----------------------------------------------------------
  public static void copyFileRequest(Socket s, String username,
      String origName, String copyName) throws GenericException {
    try {
      Message m = new Message(MessageType.COPY_FILE_RQT);
      byte[] myPayload = { 0, 0, 0, 3 };
      m.addPayload(myPayload);
      m.send(s);
      Message rr = Message.receive(s);
      if (rr.getMessageType() != MessageType.SEND_NEXT_PARAM) {
        throw new GenericException("Got type: "
            + rr.getMessageType().toString() + " when "
            + MessageType.SEND_NEXT_PARAM.toString() + " was expected");
      }
      m = new Message(MessageType.NEXT_PARAM_RQT);
      m.addPayload(username.getBytes());
      m.send(s);
      rr = Message.receive(s);
      if (rr.getMessageType() != MessageType.SEND_NEXT_PARAM) {
        throw new GenericException("Got type: "
            + rr.getMessageType().toString() + " when "
            + MessageType.SEND_NEXT_PARAM.toString() + " was expected");
      }
      m = new Message(MessageType.NEXT_PARAM_RQT);
      m.addPayload(origName.getBytes());
      m.send(s);
      rr = Message.receive(s);
      if (rr.getMessageType() != MessageType.SEND_NEXT_PARAM) {
        throw new GenericException("Got type: "
            + rr.getMessageType().toString() + " when "
            + MessageType.SEND_NEXT_PARAM.toString() + " was expected");
      }
      m = new Message(MessageType.NEXT_PARAM_RQT);
      m.addPayload(copyName.getBytes());
      m.send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // -----------------------------------------------------------
  public static void copyFileResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    if (m.getMessageType() == MessageType.COPY_FILE_RSP)
      return;
    else
      throw new GenericException(m.getMessageType().getDescription());
  }

  // -----------------------------------------------------------
  // ------------------FILE GET/PUT FUNCTIONS-------------------
  public static void putFileRequest(Socket s, byte[][] args, int numArgs)
      throws GenericException {
    try {
      sendArgs(s, args, numArgs, MessageType.PUT_FILE_RQT);
    } catch (GenericException e) {
      throw new GenericException("Error " + e);
    }
  }

  // -----------------------------------------------------------
  public static void getPutFileRequest(Socket s, String[] args, int numArgs,
      byte type) throws GenericException {
    byte[] myPayload = new byte[4];
    myPayload[0] = (byte) ((numArgs >> 24) & 0xff);
    myPayload[1] = (byte) ((numArgs >> 16) & 0xff);
    myPayload[2] = (byte) ((numArgs >> 8) & 0xff);
    myPayload[3] = (byte) (numArgs & 0xff);
    Message m = new Message(MessageType.findType(type));
    m.addPayload(myPayload);
    try {
      m.send(s);
      m = Message.receive(s);
      if (m.getMessageType() != MessageType.SEND_NEXT_PARAM) {
        System.out.println("Error, unexpected type returned");
        throw new GenericException("Received wrong type");
      }
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
    try {
      sendArgs(s, args, numArgs, MessageType.NEXT_PARAM_RQT);
    } catch (GenericException e) {
      throw new GenericException("Error " + e);
    }
  }

  // -----------------------------------------------------------
  public static GetFileReturnType getFileResponse(Socket s)
      throws GenericException {
    // GOELogger log = new GOELogger("/home/arbab/storageLogs/ws.log");
    // System.out.println("INITIALIZED THE LOGGER");
    GetFileReturnType r = new GetFileReturnType();
    Message m = Message.receive(s);
    byte[] payload;
    // IP
    if (m.getMessageType() != MessageType.GET_FILE_RSP)
      throw new GenericException("Error, received invalid type: "
          + m.getMessageType());
    else
      payload = m.getPayload();
    r.IP = payload.toString();
    System.out.println("IP: " + r.IP);
    // Port
    m = new Message(MessageType.SEND_NEXT_PARAM);
    try {
      m.send(s);
    } catch (IOException e) {
      throw new GenericException("error " + e);
    }
    m = Message.receive(s);
    if (m.getMessageType() != MessageType.NEXT_PARAM_RQT) {
      throw new GenericException("Error, received invalid type: "
          + m.getMessageType());
    } else {
      payload = m.getPayload();
      r.Port = (payload[0] << 8) + payload[1];
    }
    // tid
    m = new Message(MessageType.SEND_NEXT_PARAM);
    try {
      m.send(s);
    } catch (IOException e) {
      throw new GenericException("error " + e);
    }
    m = Message.receive(s);
    if (m.getMessageType() != MessageType.NEXT_PARAM_RQT) {
      throw new GenericException("Error, received invalid type: "
          + m.getMessageType());
    } else {
      payload = m.getPayload();
      // r.tid = (long)(payload[0] << 24) + (long)(payload[1] << 16) +
      // (long)(payload[2] << 8) + (long)payload[3];
      // log.write("%%%%%% " + (long)payload[0] + " " + (long)payload[1] + " " +
      // (long)payload[2] + " " + (long)payload[3] + "\n", false);
      // log.write( (0x000000FF & ((long)payload[0])) + "\n",false);
      // log.write( (0x000000FF & ((long)payload[1])) + "\n",false);
      // log.write( (0x000000FF & ((long)payload[2])) + "\n",false);
      // log.write( (0x000000FF & ((long)payload[3])) + "\n",false);
      r.tid =
          (0x000000FF & ((long) payload[0] << 24))
              + (0x000000FF & ((long) payload[1] << 16))
              + (0x000000FF & ((long) payload[2] << 8))
              + (0x000000FF & ((long) payload[3]));
    }
    return r;
  }

  // -----------------------------------------------------------
  // --------------DIRECTORY MANIPULATION FUNCTIONS-------------
  public static void makeDirectoryRequest(Socket s, String username,
      String password, String directory) throws GenericException {
    try {
      Message m = new Message(MessageType.MK_DIR_RQT);
      byte[] myPayload = { 0, 0, 0, 3 };
      m.addPayload(myPayload);
      m.send(s);
      Message rr = Message.receive(s);
      if (rr.getMessageType() != MessageType.SEND_NEXT_PARAM) {
        throw new GenericException("Got type: "
            + rr.getMessageType().toString() + " when "
            + MessageType.SEND_NEXT_PARAM.toString() + " was expected");
      }
      m = new Message(MessageType.NEXT_PARAM_RQT);
      m.addPayload(username.getBytes());
      m.send(s);
      rr = Message.receive(s);
      if (rr.getMessageType() != MessageType.SEND_NEXT_PARAM) {
        throw new GenericException("Got type: "
            + rr.getMessageType().toString() + " when "
            + MessageType.SEND_NEXT_PARAM.toString() + " was expected");
      }
      m = new Message(MessageType.NEXT_PARAM_RQT);
      m.addPayload(password.getBytes());
      m.send(s);
      rr = Message.receive(s);
      if (rr.getMessageType() != MessageType.SEND_NEXT_PARAM) {
        throw new GenericException("Got type: "
            + rr.getMessageType().toString() + " when "
            + MessageType.SEND_NEXT_PARAM.toString() + " was expected");
      }
      m = new Message(MessageType.NEXT_PARAM_RQT);
      m.addPayload(directory.getBytes());
      m.send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // -----------------------------------------------------------
  public static void makeDirectoryResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    if (m.getMessageType() == MessageType.MK_DIR_RSP)
      return;
    else
      throw new GenericException(m.getMessageType().getDescription());
  }

  // -----------------------------------------------------------
  public static void deleteDirectoryRequest(Socket s, String username,
      String password, String directory) throws GenericException {
    try {
      Message m = new Message(MessageType.DELETE_DIR_RQT);
      byte[] myPayload = { 0, 0, 0, 3 };
      m.addPayload(myPayload);
      m.send(s);
      Message rr = Message.receive(s);
      if (rr.getMessageType() != MessageType.SEND_NEXT_PARAM) {
        throw new GenericException("Got type: "
            + rr.getMessageType().toString() + " when "
            + MessageType.SEND_NEXT_PARAM.toString() + " was expected");
      }
      m = new Message(MessageType.NEXT_PARAM_RQT);
      m.addPayload(username.getBytes());
      m.send(s);
      rr = Message.receive(s);
      if (rr.getMessageType() != MessageType.SEND_NEXT_PARAM) {
        throw new GenericException("Got type: "
            + rr.getMessageType().toString() + " when "
            + MessageType.SEND_NEXT_PARAM.toString() + " was expected");
      }
      m = new Message(MessageType.NEXT_PARAM_RQT);
      m.addPayload(password.getBytes());
      m.send(s);
      rr = Message.receive(s);
      if (rr.getMessageType() != MessageType.SEND_NEXT_PARAM) {
        throw new GenericException("Got type: "
            + rr.getMessageType().toString() + " when "
            + MessageType.SEND_NEXT_PARAM.toString() + " was expected");
      }
      m = new Message(MessageType.NEXT_PARAM_RQT);
      m.addPayload(directory.getBytes());
      m.send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // -----------------------------------------------------------
  // ----------USER ACCOUNT MANIPULATION FUNCTIONS--------------
  // send an addUserRequest message
  public static void addUserRequest(Socket s, byte[][] args, int numArgs)
      throws GenericException {
    byte[] p = new byte[4];
    p[0] = (byte) ((4 >> 24) & 0xff);
    p[1] = (byte) ((4 >> 16) & 0xff);
    p[2] = (byte) ((4 >> 8) & 0xff);
    p[3] = (byte) (4 & 0xff);
    try {
      sendArg(s, p, MessageType.ADD_USER_RQT);
      sendArgs(s, args, numArgs, MessageType.NEXT_PARAM_RQT);
    } catch (GenericException e) {
      throw new GenericException("Error " + e);
    }
  }

  // -----------------------------------------------------------
  public static void deleteUserRequest(Socket s, String[] args, int numArgs)
      throws GenericException {
    byte[] p = new byte[4];
    p[0] = (byte) ((numArgs >> 24) & 0xff);
    p[1] = (byte) ((numArgs >> 16) & 0xff);
    p[2] = (byte) ((numArgs >> 8) & 0xff);
    p[3] = (byte) (numArgs & 0xff);
    try {
      sendArg(s, p, MessageType.DELETE_USER_RQT);
      sendArgs(s, args, numArgs, MessageType.NEXT_PARAM_RQT);
    } catch (GenericException e) {
      throw new GenericException("Error " + e);
    }
  }

  // -----------------------------------------------------------
  public static void setParamsRequest(Socket s, String username,
      String password, boolean updatePassword, long storageLimit,
      boolean updateStorage, long bandwidthLimit, boolean updateBandwidth)
      throws GenericException {
    byte[][] args = new byte[8][];
    args[0] = Message.intToByteArray(7);
    args[1] = username.getBytes();
    args[2] = password.getBytes();
    args[3] = new byte[1];
    if (updatePassword)
      args[3][0] = (byte) 1;
    else
      args[3][0] = (byte) 0;
    args[4] = Message.longToByteArray(storageLimit);
    args[5] = new byte[1];
    if (updateStorage)
      args[5][0] = (byte) 1;
    else
      args[5][0] = (byte) 0;
    args[6] = Message.longToByteArray(bandwidthLimit);
    args[7] = new byte[1];
    if (updateBandwidth)
      args[7][0] = (byte) 1;
    else
      args[7][0] = (byte) 0;
    try {
      sendArgs(s, args, 8, MessageType.UPDATE_USER_RQT);
    } catch (GenericException e) {
      throw new GenericException("Error " + e);
    }
  }

  // -----------------------------------------------------------
  // --------------------HANDSHAKING FUNCTIONS------------------
  public static byte[] transactionAddedResponse(Socket s)
      throws GenericException {
    Message m = Message.receive(s);
    if (m.getMessageType() == MessageType.TRANS_ADDED_RSP) {
      System.out.println(m.getPayload().toString());
      // long tid = (long)(m.getPayload()[0]<<24) +
      // (long)(m.getPayload()[1]<<16) + (long)(m.getPayload()[2]<<8) +
      // (long)(m.getPayload()[3]);
      // if(m.getPayload().length > 4) {
      // }
      // return tid;
      return m.getPayload();
    } else {
      System.out.println("Error, got " + m.getMessageType() + " when "
          + MessageType.TRANS_ADDED_RSP + " was expected.");
      throw new GenericException(m.getMessageType().getDescription());
    }
  }

  public static void sendArg(Socket s, byte[] arg, MessageType type)
      throws GenericException {
    Message m = new Message(type);
    m.addPayload(arg);
    try {
      m.send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
    Message rr = Message.receive(s);
    if (rr.getMessageType() != MessageType.SEND_NEXT_PARAM) {
      System.out.println("Got a " + rr.getMessageType() + " message");
      throw new GenericException("Got type: " + rr.getMessageType().toString()
          + " when " + MessageType.SEND_NEXT_PARAM.toString() +
          " was expected");
    }
  }

  // -----------------------------------------------------------
  public static void sendArgs(Socket s, byte[][] args, int numArgs,
      MessageType type) throws GenericException {
    Message m = new Message(type);
    m.addPayload(args[0]);
    try {
      m.send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
    for (int i = 1; i < numArgs; i++) {
      Message rr = Message.receive(s);
      if (rr.getMessageType() != MessageType.SEND_NEXT_PARAM) {
        throw new GenericException("Got type: "
            + rr.getMessageType().toString() + " when "
            + MessageType.SEND_NEXT_PARAM.toString() + " was expected");
      }
      m = new Message(MessageType.NEXT_PARAM_RQT);
      m.addPayload(args[i]);
      try {
        m.send(s);
      } catch (IOException e) {
        throw new GenericException(e.getMessage());
      }
    }
  }

  // -----------------------------------------------------------
  public static void sendArgs(Socket s, String[] args, int numArgs,
      MessageType type) throws GenericException {
    Message m = new Message(type);
    m.addPayload(args[0].getBytes());
    try {
      m.send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
    for (int i = 1; i < numArgs; i++) {
      Message rr = Message.receive(s);
      if (rr.getMessageType() != MessageType.SEND_NEXT_PARAM) {
        throw new GenericException("Got type: "
            + rr.getMessageType().toString() + " when "
            + MessageType.SEND_NEXT_PARAM.toString() + " was expected");
      }
      m = new Message(MessageType.NEXT_PARAM_RQT);
      m.addPayload(args[i].getBytes());
      try {
        m.send(s);
      } catch (IOException e) {
        throw new GenericException(e.getMessage());
      }
    }
  }

  // -----------------------------------------------------------
  // --------------------UTILITY FUNCTIONS----------------------
  private static String byteArrayToString(byte[] array) {
    char[] charArray = new char[array.length];
    char c;
    String returnVal;
    for (int i = 0; i < array.length; i++) {
      c = (char) array[i];
      c &= 0x00ff;
      charArray[i] = (char) array[i];
    }
    return (String.copyValueOf(charArray));
  }
}
