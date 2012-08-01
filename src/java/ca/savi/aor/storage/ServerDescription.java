// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.storage;

import ca.savi.aor.messageexceptions.GenericException;

/**
 * @author Keith
 */
public class ServerDescription {
  byte[] IP;
  int port;
  int serverNumber;

  public ServerDescription() {
    IP = new byte[4];
  }

  public String getIP() {
    String returnVal = "";
    if (IP == null)
      return "0.0.0.0";
    for (int i = 0; i < 4; i++) {
      returnVal += Integer.toString(IP[i]);
      if (i != 3)
        returnVal += ".";
    }
    return returnVal;
  }

  public void setIP(String strIP) throws GenericException {
    try {
      IP = IPStrToByteArr(strIP);
    } catch (GenericException e) {
      throw new GenericException(e.getMessage());
    }
  }

  static public byte[] IPStrToByteArr(String strIP) throws GenericException {
    String[] splitVals = new String[4];
    byte[] byteIP = new byte[4];
    System.out.println("parsing IP");
    if (strIP.split("\\.").length != 4) {
      System.out.println("Parse error on IP");
      throw new GenericException("IP in invalid format");
    }
    splitVals = strIP.split("\\.");
    byteIP[0] = (byte) (Integer.valueOf(splitVals[0]) & 0xff);
    byteIP[1] = (byte) (Integer.valueOf(splitVals[1]) & 0xff);
    byteIP[2] = (byte) (Integer.valueOf(splitVals[2]) & 0xff);
    byteIP[3] = (byte) (Integer.valueOf(splitVals[3]) & 0xff);
    return (byteIP);
  }
}
