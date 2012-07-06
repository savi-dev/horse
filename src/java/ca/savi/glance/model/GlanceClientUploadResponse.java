// Copyright (C) 2012, The SAVI Project.
package ca.savi.glance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Jie Yu Lin (Eric) <jieyu.lin@mail.utoronto.ca>
 * @version 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "imageInfo", "uploadSuccessful" })
public class GlanceClientUploadResponse {
  @XmlElement(name = "image")
  protected Imnode imageInfo;
  protected boolean uploadSuccessful;

  public void setImageInfo(Imnode value) {
    this.imageInfo = value;
  }

  public void setUploadSuccessful(boolean value) {
    this.uploadSuccessful = value;
  }

  public Imnode getImageInfo() {
    return this.imageInfo;
  }

  public boolean getUploadSuccessful() {
    return this.uploadSuccessful;
  }
}
