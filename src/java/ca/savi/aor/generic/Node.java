// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.generic;

import java.util.UUID;

/**
 * @author yuethomas
 */
public class Node {
  public String name;
  public UUID uuid;
  public long timestamp;
  public long duration;
  public boolean inUse;
  public boolean acquired;
  public boolean functioning;

  public String getType() {
    return "unspecific";
  }
}