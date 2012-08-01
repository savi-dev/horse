// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;
import java.util.Set;

import ca.savi.aor.messageexceptions.GenericException;

/**
 * @author yuethomas
 */
// ippool is the class responsible for dispensing and collecting IPs. It reads
// from the file specified in the constructor
public class IPPool {
  InetAddress[] IP;
  boolean[] inUse;

  // create a new ip pool from the file specified by f
  // for the format of the file, check the sample file given
  public IPPool(File f) throws IOException {
    Properties p = new Properties();
    p.load(new FileInputStream(f));
    Set<Object> s = p.keySet();
    IP = new InetAddress[s.size()];
    inUse = new boolean[s.size()];
    String[] x = (String[]) s.toArray(new String[0]);
    // create the IPs and inUse arrays
    for (int i = 0; i < s.size(); i++) {
      IP[i] = InetAddress.getByName(x[i]);
      inUse[i] = false;
    }
  }

  public IPPool() throws IOException {
    this(new File(".ippool"));
  }

  // get an IP that's not in use
  public InetAddress getIP() throws GenericException {
    for (int i = 0; i < IP.length; i++)
      if (!inUse[i]) {
        inUse[i] = true;
        return IP[i];
      }
    throw new GenericException("No IPs are available right now.");
  }

  // collect an IP
  public void releaseIP(InetAddress a) throws GenericException {
    // can't release a null IP can we
    if (a == null)
      return;
    // loop through the IPs and find the one that's being used
    for (int i = 0; i < IP.length; i++) {
      if (a.equals(IP[i])) {
        if (inUse[i]) {
          inUse[i] = false;
          return;
        } else
          throw new GenericException("IP to be released is not in use.");
      }
    }
    // can't find the one
    throw new GenericException("IP to be released is not valid.");
  }
}
