// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.storage;

import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Keith
 */
public class StorageServer {
  public String[] users;
  public String name;
  public InetAddress host;
  public int port;
  public int httpPort;
  public Socket sock;
  public int servNo;
  public String IP;
  public String externalName;

  public StorageServer() {
    numUsers = 0;
  }

  public synchronized boolean AddUser(String name) {
    if (numUsers < MaxUsers) {
      users[numUsers] = name.toLowerCase();
      numUsers++;
      return (true);
    } else
      return (false);
  }

  public int FindUser(String name) {
    int i;
    String lowerName = name.toLowerCase();
    for (i = 0; i < numUsers; i++) {
      if (users[i].equals(lowerName))
        return (i);
    }
    return (-1);
  }

  public synchronized boolean DeleteUser(String name) {
    int i;
    if (numUsers > 0) {
      i = FindUser(name);
      if (i < 0)
        return (false);
      else {
        for (; i < numUsers; i++)
          users[i] = users[i + 1];
        users[numUsers] = "";
        numUsers--;
        return (true);
      }
    } else
      return (false);
  }

  private int numUsers;
  private int MaxUsers = 65536;
}
