// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.fabric;

/**
 * @author hadi
 */
public class FabricInfo {
  public String rsa;
  public String ip;
  public String UUID;
  public int rid;

  public FabricInfo(String UUID, String ip, String rsa, String rid) {
    this.ip = ip;
    this.rsa = rsa;
    this.rid = Integer.valueOf(rid);
    this.UUID = UUID;
  }
}