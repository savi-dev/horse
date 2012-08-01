// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.gateway;

import java.util.UUID;

/**
 * @author Keith
 */
public class IPResourceType {
  public String externalIP, internalIP;
  public boolean inUse;
  public UUID uuid;

  public IPResourceType(UUID setUUID, String exIP) {
    externalIP = exIP;
    uuid = setUUID;
    inUse = false;
    internalIP = null;
  }
}