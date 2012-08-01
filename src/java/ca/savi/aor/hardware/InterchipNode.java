// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.hardware;

import java.util.UUID;

/**
 * @author yuethomas
 */
public class InterchipNode extends Node {
  public UUID[] link; // 0 - left uuid, 1 - right uuid
  public int[] width; // 0 - left->right width, 1 - right->left width

  @Override
  public String getType() {
    return "InterchipNode";
  }
}