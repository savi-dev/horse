// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.hardware;

/**
 * @author yuethomas
 */
public class HardwareSet {
  public HardwareUnit[] unit;
  public int DefaultPort;

  public HardwareSet(int UnitCount) {
    unit = new HardwareUnit[UnitCount];
  }
}