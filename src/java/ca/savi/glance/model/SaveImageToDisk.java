// Copyright (C) 2012, The SAVI Project.
package ca.savi.glance.model;

/**
 * @author Jie Yu Lin (Eric) <jieyu.lin@mail.utoronto.ca>
 * @version 0.1
 */
public class SaveImageToDisk {
  protected boolean successful;
  protected String error;

  public void setSuccessful(boolean value) {
    successful = value;
  }

  public void setError(String value) {
    error = value;
  }

  public boolean getSuccessful() {
    return successful;
  }

  public String getError() {
    return error;
  }
}
