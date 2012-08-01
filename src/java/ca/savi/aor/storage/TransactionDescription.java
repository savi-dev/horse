// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.storage;

/**
 * @author Keith
 */
public class TransactionDescription {
  public long tid;
  public boolean serviced;
  public long addedTime;
  public MessageType returnType;
  public byte[] payload;

  public void setServiced(boolean v) {
    serviced = v;
  }
}
