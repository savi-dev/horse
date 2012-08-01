// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.ip;

import java.util.UUID;

/**
 * @author hadi
 */
class AttarchedResource {
  public UUID uuid;
  public int resIndex;
  public boolean autoAdded;

  AttarchedResource(UUID uuid) {
    this.resIndex = -1;
    this.uuid = uuid;
    this.autoAdded = false;
  }

  AttarchedResource(UUID uuid, int resIndex, boolean autoAdded) {
    this.resIndex = resIndex;
    this.uuid = uuid;
    this.autoAdded = autoAdded;
  }
}