// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.gateway;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import ca.savi.aor.messageexceptions.GenericException;

/**
 * @author Keith
 */
public class GatewayVlans {
  public GatewayVlans(String IP, String fname) {
    VLANs = new GatewayVlanType[4096];
    lock = new Object();
    for (int i = 0; i < 4096; i++) {
      // use integers for unsigned results (bytes result in negative numbers)
      int[] b = new int[4];
      b[0] = 10;
      b[1] = (int) (i >> 4);
      b[2] = (int) ((i & 0x0000000f) << 4);
      b[3] = 1;
      String thisIP =
          Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "."
              + Integer.toString(b[2]) + "." + Integer.toString(b[3]);
      VLANs[i] = new GatewayVlanType(i, thisIP);
    }
    gwIP = IP;
    keyfile = fname;
  }

  public void getIP(String IP, UUID uuid) throws GenericException {
    int vlanid = getVLANId(IP);
    VLANs[vlanid].addUUID(gwIP, keyfile, uuid);
  }

  public void releaseIP(String IP, UUID uuid) throws GenericException {
    int vlanid = getVLANId(IP);
    VLANs[vlanid].removeUUID(gwIP, keyfile, uuid);
  }

  private int getVLANId(String IP) throws GenericException {
    InetAddress ia;
    try {
      ia = InetAddress.getByName(IP);
    } catch (UnknownHostException e) {
      throw new GenericException(e.getMessage());
    }
    byte[] b = new byte[5];
    b = ia.getAddress();
    if (b[0] != (byte) 10) {
      throw new GenericException(
          "Error, IP address supplied does not match IP format required");
    }
    int vlanid;
    vlanid = b[2] & 0xf0;
    vlanid >>= 4;
    vlanid += (int) ((int) b[1] << 4);
    if (vlanid > 4095 || vlanid <= 0) {
      throw new GenericException(
          "Error, specified internal IP results in an invalid VLAN id");
    }
    return vlanid;
  }

  private GatewayVlanType[] VLANs;
  private String gwIP, keyfile;
  public final Object lock;
}