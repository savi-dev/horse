// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.fabric;

import java.io.IOException;
import java.net.Socket;
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
  public static Message sendMessage(Socket s, UUID uuid, byte[] payload,
      MessageType sendType, MessageType responseType) throws GenericException {
    Message m = new Message(sendType);
    m.addPayload(Message.UUIDToByteArray(uuid));
    // System.out.println(sendType.getType());
    if (payload != null) {
      m.addPayload(payload);
    }
    try {
      m.send(s);
      m = Message.receive(s);
      if (m.getMessageType() != responseType) {
        System.out
            .println("Error, got: " + m.getMessageType().getDescription());
        throw new GenericException(m.getMessageType().getDescription());
      }
    } catch (IOException e) {
      throw new GenericException(e.getMessage());
    }
    return m;
  }

  public static void setParticipation(Socket s, UUID uuid, int vlanid,
      byte[] payload, int paysize) throws GenericException {
    try {
      byte[] pay = new byte[paysize + 4];
      pay[0] = (byte) (vlanid >> 8);
      pay[1] = (byte) (vlanid & 0x000000FF);
      // add number of bytes in bitstream to payload
      pay[2] = (byte) (paysize >> 8);
      pay[3] = (byte) (paysize & 0x000000FF);
      // copy the bitstream into the payload
      System.arraycopy(payload, 0, pay, 4, paysize);
      MessageFactory.sendMessage(s, uuid, pay,
          MessageType.SET_VLAN_PARTICIPATION_RQT,
          MessageType.SET_VLAN_PARTICIPATION_RSP);
    } catch (GenericException e) {
      throw new GenericException(e.getMessage());
    }
  }
}