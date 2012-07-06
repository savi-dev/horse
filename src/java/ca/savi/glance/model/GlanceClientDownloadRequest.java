// Copyright (C) 2012, The SAVI Project.
package ca.savi.glance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Jie Yu Lin (Eric) <jieyu.lin@mail.utoronto.ca>
 * @version 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
public class GlanceClientDownloadRequest {
  protected String uuid;
  protected boolean storeByteArray;
  protected boolean saveToDisk;
  protected String downloadLocalPath;
  protected String imageName;

  /**
   * Set the UUID of the image to download
   * @param value
   */
  public void setUuid(String value) {
    this.uuid = value;
  }

  /**
   * Set the storeByteArray property to download the image in the host memory in
   * the form of bit stream (i.e. byte[])
   * @param value
   */
  public void setStoreByteArray(boolean value) {
    this.storeByteArray = value;
  }

  /**
   * Set the saveToDisk property to save the image to disk in the host machine
   * @param value
   */
  public void setSaveToDisk(boolean value) {
    this.saveToDisk = value;
  }

  /**
   * set the local path to download the image file. If this is equals to null,
   * image will not be saved to the host disk.
   * @param value
   */
  public void setDownloadLocalPath(String value) {
    this.downloadLocalPath = value;
  }

  /**
   * set the image name when saving the image to the host local storage. If this
   * is not set, Glance client will use the UUID of the image as the name of the
   * image;
   * @param value
   */
  public void setImageName(String value) {
    this.imageName = value;
  }

  /**
   * Gets the image UUID
   * @return
   */
  public String getUuid() {
    return this.uuid;
  }

  /**
   * Gets the storeByteArray property.
   * @return
   */
  public boolean getStoreByteArray() {
    return this.storeByteArray;
  }

  /**
   * Gets the saveToDisk property
   * @return
   */
  public boolean getSaveToDisk() {
    return this.saveToDisk;
  }

  /**
   * Gets the DownloadLocalPath property of the image.
   * @return
   */
  public String getDonwloadLocalPath() {
    return this.downloadLocalPath;
  }

  /**
   * Gets the imageName property
   * @return
   */
  public String getImageName() {
    return this.imageName;
  }
}
