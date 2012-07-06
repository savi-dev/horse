// Copyright (C) 2012, The SAVI Project.
package ca.savi.glance.model;

import java.io.InputStream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import ca.savi.glance.client.GlanceClient_Util;

import com.sun.jersey.api.client.WebResource;

/**
 * @author Jie Yu Lin (Eric) <jieyu.lin@mail.utoronto.ca>
 * @version 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "imageName", "imageId", "storeMethod",
    "diskFormat", "containerFormat", "bodySize", "checksum", "isPublic",
    "minRam", "minDisk", "owner" })
public class GlanceClientUploadRequest {
  public static final String IMAGE_NAME = "x-image-meta-name";
  public static final String IMAGE_ID = "x-image-meta-id";
  public static final String STORE_METHOD = "x-image-meta-store";
  public static final String DISK_FORMAT = "x-image-meta-disk_format";
  public static final String CONTAINER_FORMAT = "x-image-meta-container_format";
  public static final String BODY_SIZE = "x-image-meta-size";
  public static final String CHECKSUM = "x-image-meta-checksum";
  public static final String IS_PUBLIC = "x-image-meta-is-public";
  public static final String MIN_RAM = "x-image-meta-min-ram";
  public static final String MIN_DISK = "x-image-meta-min-disk";
  public static final String OWNER = "x-image-meta-owner";
  public static final String PROPERTY = "x-image-meta-property-";
  protected String imageName;
  protected String imageId;
  protected String storeMethod;
  protected String diskFormat;
  protected String containerFormat;
  protected String bodySize;
  protected String checksum;
  protected String isPublic;
  protected String minRam;
  protected String minDisk;
  protected String owner;
  protected String imageLocalPath;
  protected String imageURI;
  protected InputStream imageInputStream;
  protected byte[] imageByteArray;

  public void setImageName(String value) {
    this.imageName = value;
  }

  public void setImageId(String value) {
    this.imageId = value;
  }

  public void setStoreMethod(String value) {
    this.storeMethod = value;
  }

  public void setDiskFormat(String value) {
    this.diskFormat = value;
  }

  public void setContainerFormat(String value) {
    this.containerFormat = value;
  }

  public void setBodySize(String value) {
    this.bodySize = value;
  }

  public void setChecksum(String value) {
    this.checksum = value;
  }

  public void setIsPublic(boolean value) {
    this.isPublic = "" + value;
  }

  public void setMinRam(String value) {
    this.minRam = value;
  }

  public void setMinDisk(String value) {
    this.minDisk = value;
  }

  public void setOwner(String value) {
    this.owner = value;
  }

  /**
   * set the local path of image file. Glance will get the file and upload to
   * the request storage
   * @param value
   */
  public void setImageLocalPath(String value) {
    this.imageLocalPath = value;
  }

  /**
   * Set the URI of the image file. Glance will get the image file from the
   * given URI and upload it to the request storage.
   * @param value
   */
  public void setImageURI(String value) {
    this.imageURI = value;
  }

  /**
   * Set the input stream the image to be uploaded. Glance will upload this
   * image from this input stream. This has higher priority than imageURI and
   * imageLocalPath. (i.e. when imageInputStream, imageURI and imageLocalPath
   * are all the supplied. Glance client will use only the imageInputStream)
   * @param value
   */
  public void setImageInputStream(InputStream value) {
    this.imageInputStream = value;
  }

  /**
   * Set the byte array of the image to be uploaded. In this case, the whole
   * image is stored in the host memory. This has higher priority over imageURI
   * , imageLocalPath and ImageInputStream.(i.e. when imageByteArray,
   * imageInputStream, imageURI and imageLocalPath are all the supplied. Glance
   * client will use only the imageByteArray)
   * @param value
   */
  public void setImageByteArray(byte[] value) {
    this.imageByteArray = value;
  }

  public String getImageName() {
    return this.imageName;
  }

  public String getImageId() {
    return this.imageId;
  }

  public String getStoreMethod() {
    return this.storeMethod;
  }

  public String getDiskFormat() {
    return this.diskFormat;
  }

  public String getContainerFormat() {
    return this.containerFormat;
  }

  public String getBodySize() {
    return this.bodySize;
  }

  public String getChecksum() {
    return this.checksum;
  }

  public String getIsPublic() {
    return this.isPublic;
  }

  public String getMinRam() {
    return this.minRam;
  }

  public String getMinDisk() {
    return this.minDisk;
  }

  public String getOwner() {
    return this.owner;
  }

  public String getImageLocalPath() {
    return this.imageLocalPath;
  }

  public String getImageURI() {
    return this.imageURI;
  }

  public InputStream getImageInputStream() {
    return this.imageInputStream;
  }

  public byte[] getImageByteArray() {
    return this.imageByteArray;
  }

  public WebResource.Builder add_image_headers_to_webresource(
      WebResource.Builder wrb) {
    if (GlanceClient_Util.isNotEmpty(imageName)) {
      wrb = wrb.header(IMAGE_NAME, imageName);
    }
    if (GlanceClient_Util.isNotEmpty(imageId)) {
      wrb = wrb.header(IMAGE_ID, imageId);
    }
    if (GlanceClient_Util.isNotEmpty(storeMethod)) {
      wrb = wrb.header(STORE_METHOD, storeMethod);
    }
    if (GlanceClient_Util.isNotEmpty(diskFormat)) {
      wrb = wrb.header(DISK_FORMAT, diskFormat);
    }
    if (GlanceClient_Util.isNotEmpty(containerFormat)) {
      wrb = wrb.header(CONTAINER_FORMAT, containerFormat);
    }
    if (GlanceClient_Util.isNotEmpty(bodySize)) {
      wrb = wrb.header(BODY_SIZE, bodySize);
    }
    if (GlanceClient_Util.isNotEmpty(checksum)) {
      wrb = wrb.header(CHECKSUM, checksum);
    }
    if (GlanceClient_Util.isNotEmpty(isPublic)) {
      wrb = wrb.header(IS_PUBLIC, isPublic);
    }
    if (GlanceClient_Util.isNotEmpty(minRam)) {
      wrb = wrb.header(MIN_RAM, minRam);
    }
    if (GlanceClient_Util.isNotEmpty(minDisk)) {
      wrb = wrb.header(MIN_DISK, minDisk);
    }
    if (GlanceClient_Util.isNotEmpty(owner)) {
      wrb = wrb.header(OWNER, owner);
    }
    return wrb;
  }

  public WebResource.Builder add_image_headers_to_webresource(WebResource wr) {
    WebResource.Builder wrb = wr.getRequestBuilder();
    return this.add_image_headers_to_webresource(wrb);
  }
}
