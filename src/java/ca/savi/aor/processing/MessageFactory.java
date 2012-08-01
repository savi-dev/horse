// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.processing;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.UUID;

import ca.savi.aor.messageexceptions.GenericException;

/**
 * @author yuethomas
 */
// messageFactory provides wrapper methods for receiving and sending types of
// messages over a socket. It checks for payload validity automatically. All
// receiving methods throw errors if the type is unexpected, or if the CRC
// is invalid, or if the payload is corrupt.
public class MessageFactory {
  public static void saveImageRequest(Socket s, UUID uuid)
      throws GenericException {
    try {
      new Message(MessageType.SAVE_IMG_RQT).addPayload(
          Message.UUIDToByteArray(uuid)).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  public static Message saveImageResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.SAVE_IMG_RSP)
      return m;
    else
      throw new GenericException("Error, received unexpected type: "
          + m.getMessageType().getDescription());
  }

  public static void addVLANRequest(Socket s, UUID u, int vlanid, String IP)
      throws GenericException {
    System.out.println("IP: " + IP + " LENGHT IS "
        + Message.intToByteArray(IP.getBytes().length));
    try {
      new Message(MessageType.ADD_VLAN_RQT)
          .addPayload(Message.UUIDToByteArray(u))
          .addPayload(Message.intToByteArray(vlanid))
          .addPayload(Message.intToByteArray(IP.getBytes().length))
          .addPayload(IP.getBytes()).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  public static void addVLANResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.ADD_VLAN_RSP)
      return;
    else
      throw new GenericException("Error, received unexpected type: "
          + m.getMessageType().getDescription());
  }

  public static void removeVLANRequest(Socket s, UUID u, int vlanid)
      throws GenericException {
    try {
      new Message(MessageType.REMOVE_VLAN_RQT)
          .addPayload(Message.UUIDToByteArray(u))
          .addPayload(Message.intToByteArray(vlanid)).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  public static void removeVLANResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.REMOVE_VLAN_RSP)
      return;
    else
      throw new GenericException("Error, received unexpected type: "
          + m.getMessageType().getDescription());
  }

  public static void getImageRequest(Socket s, UUID uuid)
      throws GenericException {
    try {
      new Message(MessageType.GET_IMG_RQT).addPayload(
          Message.UUIDToByteArray(uuid)).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  public static Message getImageResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.GET_IMG_RSP)
      return m;
    else if (t.isErrorType())
      throw new GenericException("Server encountered an " + t.getDescription());
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  public static void getImageComplete(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.GET_IMG_CMP)
      return;
    else
      throw new GenericException(t.getDescription());
  }

  // send an initRequest message
  public static void initRequest(Socket s) throws GenericException {
    try {
      new Message(MessageType.INIT_RQT).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // send an initNextRequest message
  public static void initNextRequest(Socket s) throws GenericException {
    try {
      new Message(MessageType.INIT_NEXT).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // receive an initResponse message
  public static boolean initResponse(Socket s, ProcessingNode u)
      throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.INIT_RSP || t == MessageType.INIT_HAVE_MORE) {
      byte[] p = m.getPayload();
      if (p.length < 16)
        throw new GenericException(
            "Response from processing node not long enough");
      u.inUse = false;
      u.uuid = Message.byteArrayToUUID(Arrays.copyOfRange(p, 0, 16));
      if (t == MessageType.INIT_HAVE_MORE)
        return true;
      System.out.println("Received INIT has last");
      return false;
    } else if (t.isErrorType())
      throw new GenericException("Server encountered an " + t.getDescription());
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  public static void resourceGetRequest(Socket s, UUID uuid)
      throws GenericException {
    try {
      Message m = new Message(MessageType.GET_RQT);
      m.addPayload(Message.UUIDToByteArray(uuid));
      m.send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  public static void resourceGetResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.GET_RSP)
      return;
    else
      throw new GenericException(t.getDescription());
  }

  // send a ReleaseRequest message
  // uuid is the resource to release
  public static void resourceReleaseRequest(Socket s, UUID uuid)
      throws IOException {
    new Message(MessageType.RELEASE_RQT).addPayload(
        Message.UUIDToByteArray(uuid)).send(s);
  }

  // receive a releaseResponse message
  public static void resourceReleaseResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.RELEASE_RSP)
      return;
    else if (t == MessageType.RELEASE_ERR_UUID)
      throw new GenericException(t.getDescription());
    else if (t == MessageType.RELEASE_ERR_NOT_PROPERLY_RELEASED)
      throw new GenericException(t.getDescription());
    else if (t.isErrorType())
      throw new GenericException("Server encountered an " + t.getDescription());
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  public static void resourceRebootRequest(Socket s, UUID uuid)
      throws GenericException {
    try {
      new Message(MessageType.REBOOT_RQT).addPayload(
          Message.UUIDToByteArray(uuid)).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // receive a releaseResponse message
  public static void resourceRebootResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.REBOOT_RSP)
      return;
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  public static void securityFileRequest(Socket s, int fsize)
      throws GenericException {
    try {
      new Message(MessageType.SECURITY_FILE_RQT).addPayload(
          Message.intToByteArray(fsize)).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  public static void securityFileTransReady(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.SECURITY_TRANS_READY)
      return;
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  public static void securityFileResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.SECURITY_FILE_RSP)
      return;
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  public static void deleteSecurityFileRequest(Socket s)
      throws GenericException {
    try {
      new Message(MessageType.DEL_SECURITY_FILE_RQT).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  public static void deleteSecurityFileResponse(Socket s)
      throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.DEL_SECURITY_FILE_RSP)
      return;
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  // send a programResource message
  // uuid is the resource to program
  // image_size is the size of the image
  // crc is the crc of the image
  // f is the IP address to assign to the resource
  public static void programResourceRequest(Socket s, UUID uuid,
      String transactionString, String port, String ip, String tid)
      throws GenericException {
    // CHANGES MADE
    // The new message contains the uuid,transactionstring,port,ip,and tid
    try {
      /*
       * new Message (MessageType.PROGRAM_RQT) .addPayload
       * (Message.UUIDToByteArray(uuid)) .addPayload
       * (Message.intToByteArray(numImages)) .addPayload (Message.intToByteArray
       * (image_size)) .addPayload (Message.intToByteArray (crc)) .send (s);
       */
      String myData = ip + ":" + port + ":" + transactionString + ":" + tid;
      new Message(MessageType.PROGRAM_RQT)
          .addPayload(Message.UUIDToByteArray(uuid))
          .addPayload(Message.intToByteArray(myData.getBytes().length))
          .addPayload(myData.getBytes()).addPayload(tid.getBytes()).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // receive a resourceTransReady message
  public static void programResourceTransReady(Socket s)
      throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.PROGRAM_TRANS_RDY)
      return;
    else if (t == MessageType.PROGRAM_ERR_UUID
        || t == MessageType.PROGRAM_ERR_TCP)
      throw new GenericException(t.getDescription());
    else if (t.isErrorType())
      throw new GenericException("Server encountered an " + t.getDescription());
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  // receive a resourceTransCorrect message
  public static void programResourceTransCorrect(Socket s)
      throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.PROGRAM_TRANS_CORR) {
      return;
    } else if (t == MessageType.PROGRAM_ERR_LENGTH
        || t == MessageType.PROGRAM_ERR_CRC)
      throw new GenericException(t.getDescription());
    else if (t.isErrorType())
      throw new GenericException("Server encountered an " + t.getDescription());
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  // send a statusRequest message
  // uuid is the resource to request a status of
  public static void resourceStatusRequest(Socket s, UUID uuid)
      throws IOException {
    System.out.println("Enter resourceStatusRequest");
    new Message(MessageType.STATUS_RQT).addPayload(
        Message.UUIDToByteArray(uuid)).send(s);
  }

  // set the vlan id for a processing resource
  public static void setVLANIDRequest(Socket s, short vlanID)
      throws GenericException {
    try {
      new Message(MessageType.SET_VLAN_ID_RQT).addPayload(
          Message.shortToByteArray(vlanID)).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // get the set vlan id response
  public static void setVLANIDResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.SET_VLAN_ID_RSP)
      return;
    else
      throw new GenericException(m.getMessageType().getDescription());
  }

  public static void setIPAddressRequest(Socket s, InetAddress ia)
      throws GenericException {
    try {
      new Message(MessageType.SET_IP_RQT).addPayload(ia.getAddress()).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // get the set vlan id response
  public static void setIPAddressResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.SET_IP_RSP)
      return;
    else
      throw new GenericException(m.getMessageType().getDescription());
  }

  public static void releaseIPAddressRequest(Socket s) throws GenericException {
    try {
      new Message(MessageType.CLR_IP_RQT).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // get the set vlan id response
  public static void releaseIPAddressResponse(Socket s)
      throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.CLR_IP_RSP)
      return;
    else
      throw new GenericException(m.getMessageType().getDescription());
  }

  // receive a terminateResponse message
  public static int resourceStatusResponse(Socket s) throws GenericException {
    System.out.println("Enter resourceStatusResponse");
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.STATUS_RSP)
      return 0;
    else if (t == MessageType.STATUS_ERR_UUID)
      throw new GenericException(t.getDescription());
    else if (t == MessageType.STATUS_ERR_INVALID)
      throw new GenericException(t.getDescription());
    else if (t == MessageType.STATUS_ERR_TIMEOUT)
      throw new GenericException(t.getDescription());
    else if (t == MessageType.ERR_RESOURCE_BUSY)
      return 1;
    else
      throw new GenericException("Server's response is of an unexpected type.");
  }

  // send a terminateRequest message
  public static void terminateRequest(Socket s) throws IOException {
    new Message(MessageType.TERM_RQT).send(s);
  }

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

  // send a serverStopRequest message
  public static void serverStopRequest(Socket s) throws IOException {
    new Message(MessageType.TERM_SERVER_RQT).send(s);
  }

  // receive a serverStopResponse message
  public static void serverStopResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.TERM_SERVER_RSP)
      return;
    else if (t == MessageType.TERM_ERR)
      throw new GenericException(t.getDescription());
    else if (t.isErrorType())
      throw new GenericException("Server encountered an " + t.getDescription());
    else
      throw new GenericException("Server's response is of an unexpected type.");
  }
}
