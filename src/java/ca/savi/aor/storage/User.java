// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.storage;

/**
 * @author Keith
 */
public class User {
  public String name;
  public String password;
  public int storageLimit;
  public int bandwidthLimit;

  public User(String n, String p, int sl, int bl) {
    name = n.toLowerCase();
    password = p;
    storageLimit = sl;
    bandwidthLimit = bl;
  }
}