// Copyright (C) 2012, The SAVI Project.
package ca.savi.glance.model;

/**
 * @author Jie Yu Lin (Eric) <jieyu.lin@mail.utoronto.ca>
 * @version 0.1
 */
public class GlanceClientGetInfoRequest {
  protected boolean detail;

  /**
   * Sets detail property. More information of the image will be returned when
   * detail is set to true
   * @param value
   */
  public void setDetail(boolean value) {
    this.detail = value;
  }

  /**
   * Gets detail property. More information of the image will be returned when
   * detail is set to true
   * @return
   */
  public boolean getDetail() {
    return this.detail;
  }
}
