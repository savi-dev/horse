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
@XmlType(name = "", propOrder = { "gcDownloadRequest", "gcUploadRequest",
    "gcGetInfoRequest" })
public class GlanceClientRequest {
  protected GlanceClientDownloadRequest gcDownloadRequest;
  protected GlanceClientUploadRequest gcUploadRequest;
  protected GlanceClientGetInfoRequest gcGetInfoRequest;

  /**
   * set the GcDownloadRequest property
   * @param value
   */
  public void setGcDownloadRequest(GlanceClientDownloadRequest value) {
    this.gcDownloadRequest = value;
  }

  /**
   * set the GcUploadRequest property
   * @param value
   */
  public void setGcUploadRequest(GlanceClientUploadRequest value) {
    this.gcUploadRequest = value;
  }

  /**
   * sets the GcGetInfoRequest property
   * @param value
   */
  public void setGcGetInfoRequest(GlanceClientGetInfoRequest value) {
    this.gcGetInfoRequest = value;
  }

  /**
   * Gets the GcDownloadRquest property
   * @return
   */
  public GlanceClientDownloadRequest getGcDownloadRequest() {
    return this.gcDownloadRequest;
  }

  /**
   * Gets the GcUploadRequest property
   * @return
   */
  public GlanceClientUploadRequest getGcUploadRequest() {
    return this.gcUploadRequest;
  }

  /**
   * Gets the GcGetInfoRequest property
   * @return
   */
  public GlanceClientGetInfoRequest getGcGetInfoRequest() {
    return this.gcGetInfoRequest;
  }
}
