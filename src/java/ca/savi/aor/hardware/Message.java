// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.hardware;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.CRC32;

import ca.savi.aor.messageexceptions.GenericException;

/**
 * @author yuethomas
 */
public class Message {
  private MessageType mt;
  private byte[] payload;
  private byte[] buf;
  private int size = 3;
  private CRC32 crc;

  // create a new empty message.
  public Message() {
    this(MessageType.ERR_UNKNOWN);
  }

  // create a new message with type t.
  public Message(MessageType t) {
    payload = null;
    crc = new CRC32();
    crc.reset();
    setMessageType(t);
  }

  // return the message type.
  public MessageType getMessageType() {
    return mt;
  }

  // set the message type to t.
  public void setMessageType(MessageType t) {
    mt = t;
  }

  // return the payload of the message.
  public byte[] getPayload() {
    return payload;
  }

  // add p to the payload of the packet.
  public Message addPayload(byte[] p) {
    // create the payload if it's null right now
    if (payload == null)
      payload = new byte[0];
    // add the two lengths together to make the new buffer
    byte[] t = new byte[payload.length + p.length];
    // copy in the contents
    System.arraycopy(payload, 0, t, 0, payload.length);
    System.arraycopy(p, 0, t, payload.length, p.length);
    payload = t;
    return this;
  }

  // send the message with socket s to address ip:port.
  public void send(Socket s) throws IOException {
    // create packet
    // package format: [type][size][payload][crc]
    // if no payload, create dummy payload so i don't have change the rest
    // of this code...
    if (payload == null)
      payload = new byte[0];
    // create buffer
    buf = new byte[payload.length + 7];
    // set first byte of packet to type parameter
    buf[0] = mt.getType();
    // second and third bytes are the size
    byte[] sb = intToByteArray(payload.length);
    buf[1] = sb[2];
    buf[2] = sb[3];
    // then comes payload
    System.arraycopy(payload, 0, buf, 3, payload.length);
    // followed by CRC
    crc.update(Arrays.copyOfRange(buf, 0, payload.length + 3));
    // CRC returns a long, with only the lower 4 bytes set - same as an int
    int v = (int) crc.getValue();
    // write the CRC to the buffer
    System.arraycopy(intToByteArray(v), 0, buf, payload.length + 3, 4);
    // write the packet to the stream
    s.getOutputStream().write(buf);
  }

  // receive a message from socket s.
  public static Message receive(Socket s) throws GenericException {
    byte[] typebuf, plenbuf, paybuf, crcbuf, allbuf;
    InputStream i;
    Message m = new Message();
    int pcrc, ncrc;
    CRC32 crc = new CRC32();
    try {
      i = s.getInputStream();
      // message type
      typebuf = new byte[1];
      i.read(typebuf);
      if (MessageType.findType(typebuf[0]) != null)
        m.setMessageType(MessageType.findType(typebuf[0]));
      else
        throw new Exception("Servers's response has invalid type");
      // payload length
      plenbuf = new byte[2];
      i.read(plenbuf);
      // payload
      paybuf = new byte[Message.byteArrayToShort(plenbuf)];
      i.read(paybuf);
      // crc
      crcbuf = new byte[4];
      i.read(crcbuf);
      pcrc = byteArrayToInt(crcbuf);
    } catch (Exception e) {
      throw new GenericException(e.getMessage());
    }
    // check for CRC validity
    allbuf = new byte[3 + paybuf.length];
    System.arraycopy(typebuf, 0, allbuf, 0, 1);
    System.arraycopy(plenbuf, 0, allbuf, 1, 2);
    System.arraycopy(paybuf, 0, allbuf, 3, paybuf.length);
    crc.reset();
    crc.update(allbuf);
    ncrc = (int) crc.getValue();
    if (pcrc != ncrc)
      throw new GenericException("Server's response fails CRC check");
    // if crc passes, get the payload
    m.addPayload(paybuf);
    return (m);
  }

  // convert a 16-byte array to UUID
  public static UUID byteArrayToUUID(byte[] b) {
    ByteBuffer bb = ByteBuffer.allocate(16);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.put(b);
    bb.rewind();
    return new UUID(bb.getLong(), bb.getLong());
  }

  // convert an UUID to a 16-byte array
  public static byte[] UUIDToByteArray(UUID u) {
    byte[] b = new byte[16];
    long um = u.getMostSignificantBits();
    long ul = u.getLeastSignificantBits();
    ByteBuffer bb = ByteBuffer.allocate(16);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.putLong(um).putLong(ul);
    bb.rewind();
    bb.get(b);
    return b;
  }

  // convert an 8-byte array to a long number
  public static long byteArrayToLong(byte[] b) {
    ByteBuffer bb = ByteBuffer.allocate(8);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.put(b);
    bb.rewind();
    return bb.getLong();
  }

  // convert a long number to an 8-byte array
  public static byte[] longToByteArray(long l) {
    byte[] b = new byte[8];
    ByteBuffer bb = ByteBuffer.allocate(8);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.putLong(l);
    bb.rewind();
    bb.get(b);
    return (b);
  }

  // convert a 4-byte array to an int
  public static int byteArrayToInt(byte[] b) {
    ByteBuffer bb = ByteBuffer.allocate(4);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.put(b);
    bb.rewind();
    return bb.getInt();
  }

  // convert an int to a 4-byte array
  public static byte[] intToByteArray(int i) {
    byte[] b = new byte[4];
    ByteBuffer bb = ByteBuffer.allocate(4);
    bb.order(ByteOrder.BIG_ENDIAN);
    bb.putInt(i);
    bb.rewind();
    bb.get(b);
    return b;
  }

  // convert a 2-byte array to a short
  public static int byteArrayToShort(byte[] b) {
    return (0x000000FF & (int) b[0]) * 0x100 + (0x000000FF & (int) b[1]);
  }
}