// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.ip;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ca.savi.aor.messageexceptions.GenericException;

/**
 * @author Keith
 */
public class IP_And_Vlan {
  public InetAddress IP;
  public int vlan;

  IP_And_Vlan() {
    vlan = -1;
  }

  IP_And_Vlan(int vlanid, int resourceID) throws GenericException {
    byte[] IpBytes = new byte[4];
    if (resourceID < 0) {
      throw new GenericException("No more IPs to allocate in requested VLAN");
    }
    // IP Address = 10.VID[11:4].{VID[3:0]RID[11:8]}.RID[7:0]
    // VID = vlan id, RID = resource id
    IpBytes[0] = 10;
    IpBytes[1] = (byte) (vlanid >> 4);
    IpBytes[2] = (byte) ((vlanid << 4) & 0xf0);
    IpBytes[2] += (byte) ((resourceID >> 8) & 0x0f);
    IpBytes[3] = (byte) (resourceID & 0xff);
    try {
      IP = InetAddress.getByAddress(IpBytes);
    } catch (UnknownHostException e) {
      throw new GenericException(e.getMessage());
    }
    if (vlanid > 0 && vlanid < 4095)
      vlan = vlanid;
    else
      throw new GenericException("No more VLANs to allocate");
  }
}
