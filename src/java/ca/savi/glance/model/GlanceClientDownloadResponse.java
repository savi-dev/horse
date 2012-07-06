// Copyright (C) 2012, The SAVI Project.
package ca.savi.glance.model;

import java.io.InputStream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Jie Yu Lin (Eric) <jieyu.lin@mail.utoronto.ca>
 * @version 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "imageByteArray", "isSuccessful", "error" })
public class GlanceClientDownloadResponse {
  protected byte[] imageByteArray;
  protected InputStream imageInputStream;
  protected boolean isSuccessful;
  protected String error;

  /**
   * Sets the imageByteArray property. This is the image in byte array.
   * @param value
   */
  public void setImageByteArray(byte[] value) {
    imageByteArray = value;
  }

  /**
   * Sets the ImageInputStream property. InputStream of the image
   * @param value
   */
  public void setImageInputStream(InputStream value) {
    imageInputStream = value;
  }

  /**
   * Sets the inSuccessful property.
   * @param value
   */
  public void setIsSuccessful(boolean value) {
    isSuccessful = value;
  }

  /**
   * Sets the error property
   * @param value
   */
  public void setError(String value) {
    error = value;
  }

  /**
   * Gets the ImageByteArray property. This contains the byte array of the image
   * if storeByteArray in GlanceClientDownloadRequest is set to true.
   * @return
   */
  public byte[] getImageByteArray() {
    if (imageByteArray == null) {
      imageByteArray = new byte[0];
    }
    return this.imageByteArray;
  }

  /**
   * Gets the InputStream of the image. when both saveToDisk and storeByteArray
   * are set to false, Glance will return a InputStream of the image, which can
   * retrieved by this function
   * @return
   */
  public InputStream getImageInputStream() {
    return this.imageInputStream;
  }

  /**
   * Gets isSuccessful property
   * @return
   */
  public boolean getIsSuccessful() {
    return this.isSuccessful;
  }

  /**
   * Gets Error during downloading operation
   * @return
   */
  public String getError() {
    return this.error;
  }
}
