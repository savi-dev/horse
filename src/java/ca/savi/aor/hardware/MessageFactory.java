// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.hardware;

import java.io.IOException;
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
  // send an initRequest message
  public static void initRequest(Socket s) throws GenericException {
    try {
      new Message(MessageType.INIT_RQT).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // receive an initResponse message
  public static void initResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.INIT_RSP)
      return;
    else if (t.isErrorType())
      throw new GenericException("Server encountered an " + t.getDescription());
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  // send a registerRequest message
  public static void registerRequest(Socket s) throws GenericException {
    try {
      new Message(MessageType.REGISTER_RQT).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // receive a registerResponse message for the hardware type
  public static HardwareUnit hardwareRegisterResponse(Socket s, HardwareUnit u)
      throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    int nlen, pp;
    if (t == MessageType.REGISTER_RSP) {
      byte[] p = m.getPayload();
      int l = p.length;
      // p [0] stores number of hardware nodes
      HardwareNode[] n = new HardwareNode[p[0] & 0x000000ff];
      // p [1] stores number of interchip nodes
      InterchipNode[] c = new InterchipNode[p[1] & 0x000000ff];
      pp = 2; // payload byte pointer
      // hardware nodes
      for (int i = 0; i < p[0]; i++) {
        n[i] = new HardwareNode();
        n[i].inUse = false;
        try {
          n[i].host = null;
        } catch (Exception e) {
        }
        // uuid
        n[i].uuid = Message.byteArrayToUUID(Arrays.copyOfRange(p, pp, pp + 16));
        pp += 16;
        // byte is signed - need to unsign it, sign byte-int cast assumes sign
        nlen = 0x000000ff & p[pp];
        // name length + name
        n[i].name =
            u.name + ":"
                + new String(Arrays.copyOfRange(p, pp + 1, pp + 1 + nlen));
        pp += 1 + nlen;
      }
      // interchip nodes
      for (int i = 0; i < p[1]; i++) {
        c[i] = new InterchipNode();
        c[i].inUse = false;
        c[i].width = new int[2];
        c[i].width[0] = 0;
        c[i].width[1] = 0;
        // uuid
        c[i].uuid = Message.byteArrayToUUID(Arrays.copyOfRange(p, pp, pp + 16));
        pp += 16;
        // byte is signed - need to unsign it, since byte-int cast assumes sign
        nlen = 0x000000ff & p[pp];
        // name length + name
        c[i].name =
            u.name + ":"
                + new String(Arrays.copyOfRange(p, pp + 1, pp + 1 + nlen));
        pp += 1 + nlen;
        // interchip links
        c[i].link = new UUID[2];
        c[i].link[0] =
            Message.byteArrayToUUID(Arrays.copyOfRange(p, pp, pp + 16));
        pp += 16;
        c[i].link[1] =
            Message.byteArrayToUUID(Arrays.copyOfRange(p, pp, pp + 16));
        pp += 16;
      }
      u.hwNode = n;
      u.icNode = c;
      // Set left and right uuids for each hardware node by checking each
      // interchip left and right node uuids.
      for (int hwIndex = 0; hwIndex < p[0]; hwIndex++) {
        for (int icIndex = 0; icIndex < p[1]; icIndex++) {
          if (u.icNode[icIndex].link[0].equals(u.hwNode[hwIndex].uuid))
            u.hwNode[hwIndex].rightUUID = u.icNode[icIndex].link[1];
          if (u.icNode[icIndex].link[1].equals(u.hwNode[hwIndex].uuid))
            u.hwNode[hwIndex].leftUUID = u.icNode[icIndex].link[0];
        }
      }
      return u;
    } else if (t.isErrorType())
      throw new GenericException("Server encountered an " + t.getDescription());
    else
      throw new GenericException("Server's response is of an unexpected type.");
  }

  // public static Message resourceListRequest (boolean verbose) {
  //
  // return new Message (MessageType.LIST_RQT)
  // .addPayload (new byte [] {verbose ? (byte) 1 : (byte) 0});
  //
  // }
  // resource list response probably not necessary, not writing it for now
  // send a getRequest message
  // discrete is used for hardware/processing services
  // uuid is the resource to get, and duration is how long to hold it
  public static Message resourceGetRequestDiscrete(UUID uuid, int duration) {
    return new Message(MessageType.GET_RQT).addPayload(Message
        .intToByteArray(duration));
  }

  // send a getRequest message
  // continuous is used for storage service
  // amount is the number of bytes of storage to get
  public static Message resourceGetRequestContinuous(UUID uuid, int duration,
      int amount) {
    return new Message(MessageType.GET_RQT)
        .addPayload(Message.UUIDToByteArray(uuid))
        .addPayload(Message.intToByteArray(duration))
        .addPayload(Message.intToByteArray(amount));
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

  public static void setParametersRequest(Socket s, UUID uuid, byte[] IP,
      int vlanID) throws GenericException {
    try {
      new Message(MessageType.SET_PARAMS_RQT)
          .addPayload(Message.UUIDToByteArray(uuid)).addPayload(IP)
          .addPayload(Message.intToByteArray(vlanID)).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // receive setParametersResponse message
  public static void setParametersResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.SET_PARAMS_RSP)
      return;
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  public static void addVlanRequest(Socket s, UUID uuid, byte[] IP, int vlanID)
      throws GenericException {
    try {
      new Message(MessageType.ADD_VLAN_RQT)
          .addPayload(Message.UUIDToByteArray(uuid)).addPayload(IP)
          .addPayload(Message.intToByteArray(vlanID)).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // receive addVlanResponse message
  public static void addVlanResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.ADD_VLAN_RSP)
      return;
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  public static void removeVlanRequest(Socket s, UUID uuid, int vlanID)
      throws GenericException {
    try {
      new Message(MessageType.REMOVE_VLAN_RQT)
          .addPayload(Message.UUIDToByteArray(uuid))
          .addPayload(Message.intToByteArray(vlanID)).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // receive addVlanResponse message
  public static void removeVlanResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.REMOVE_VLAN_RSP)
      return;
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  public static void setMACRequest(Socket s, UUID uuid, int index, byte[] MAC)
      throws GenericException {
    try {
      new Message(MessageType.SET_MAC_RQT)
          .addPayload(Message.UUIDToByteArray(uuid))
          .addPayload(Message.intToByteArray(index)).addPayload(MAC).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // receive setMACResponse message
  public static void setMACResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.SET_MAC_RSP)
      return;
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  public static void setUserRegsRequest(Socket s, UUID uuid, byte[] setVal)
      throws GenericException {
    System.out.println("Sending " + setVal.length
        + " bytes to the user registers");
    try {
      new Message(MessageType.SET_USER_REGS_RQT)
          .addPayload(Message.UUIDToByteArray(uuid)).addPayload(setVal).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // receive setUserRegsResponse message
  public static void setUserRegsResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.SET_USER_REGS_RSP)
      return;
    else
      throw new GenericException("Server's response is of an unexpected type: "
          + t.name());
  }

  public static void getUserRegsRequest(Socket s, UUID uuid)
      throws GenericException {
    try {
      new Message(MessageType.GET_USER_REGS_RQT).addPayload(
          Message.UUIDToByteArray(uuid)).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // receive getUserRegsResponse message
  public static byte[] getUserRegsResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.GET_USER_REGS_RSP)
      return m.getPayload();
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
      int image_size, int crc) throws GenericException {
    try {
      new Message(MessageType.PROGRAM_RQT)
          .addPayload(Message.UUIDToByteArray(uuid))
          .addPayload(Message.intToByteArray(image_size))
          .addPayload(Message.intToByteArray(crc)).send(s);
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
  }

  // receive a resourceTransReady message
  public static int programResourceTransReady(Socket s)
      throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.PROGRAM_TRANS_RDY)
      return Message.byteArrayToShort(m.getPayload());
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
    if (t == MessageType.PROGRAM_TRANS_CORR)
      return;
    else if (t == MessageType.PROGRAM_ERR_LENGTH
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
    new Message(MessageType.STATUS_RQT).addPayload(
        Message.UUIDToByteArray(uuid)).send(s);
  }

  // receive a terminateResponse message
  public static void resourceStatusResponse(Socket s) throws GenericException {
    Message m = Message.receive(s);
    MessageType t = m.getMessageType();
    if (t == MessageType.STATUS_RSP)
      return;
    else if (t == MessageType.STATUS_ERR_UUID)
      throw new GenericException(t.getDescription());
    else if (t == MessageType.STATUS_ERR_INVALID)
      throw new GenericException(t.getDescription());
    else if (t == MessageType.STATUS_ERR_TIMEOUT)
      throw new GenericException(t.getDescription());
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
}
