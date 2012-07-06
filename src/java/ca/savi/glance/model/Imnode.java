// Copyright (C) 2012, The SAVI Project.
package ca.savi.glance.model;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Jie Yu Lin (Eric) <jieyu.lin@mail.utoronto.ca>
 * @version 0.1
 */
@XmlAccessorType(FIELD)
@XmlType(name = "", propOrder = { "name", "id", "size", "container_format",
    "disk_format", "checksum", "deleted", "created_at", "updated_at",
    "deleted_at", "status", "is_protected", "is_public", "min_ram", "min_disk",
    "owner" })
public class Imnode {
  protected String name;
  protected String id;
  protected String container_format;
  protected String disk_format;
  protected String checksum;
  protected String size;
  protected String deleted;
  protected String created_at;
  protected String updated_at;
  protected String deleted_at;
  protected String status;
  @XmlElement(name = "protected")
  protected String is_protected;
  protected String is_public;
  protected String min_ram;
  protected String min_disk;
  protected String owner;

  @Override
  public String toString() {
    return "Imnode: name=" + name + ", id=" + id + ", constainer_format="
        + container_format + ", disk_format=" + disk_format + ", checksum="
        + checksum + ", size=" + size + ", deleted=" + deleted
        + ", created_at=" + created_at + ", updated_at=" + updated_at
        + ", deleted_at=" + deleted_at + ", status=" + status + ", protected="
        + is_protected + ", is_public=" + is_public + ", min_ram=" + min_ram
        + ", min_disk" + min_disk + ", owner=" + owner;
  }

  /**
   * set the Image name
   * @param value
   */
  public void setName(String value) {
    this.name = value;
  }

  /**
   * set the image ID
   * @param value
   */
  public void setId(String value) {
    this.id = value;
  }

  /**
   * set the image container format
   * @param value
   */
  public void setContainer_format(String value) {
    this.container_format = value;
  }

  /**
   * set the image disk format
   * @param value
   */
  public void setDisk_format(String value) {
    this.disk_format = value;
  }

  /**
   * set the checksum of the image
   * @param value
   */
  public void setChecksum(String value) {
    this.checksum = value;
  }

  /**
   * set the size of the image
   * @param value
   */
  public void setSize(String value) {
    this.size = value;
  }

  /**
   * set the deleted condition of the image
   * @param value
   */
  public void setDeleted(String value) {
    this.deleted = value;
  }

  /**
   * set the creation time of the image
   * @param value
   */
  public void setCreated_at(String value) {
    this.created_at = value;
  }

  /**
   * set the updated time of the image (This could be the last time the image
   * meta data is changed)
   * @param value
   */
  public void setUpdated_at(String value) {
    this.updated_at = value;
  }

  /**
   * set the deleted time of the image
   * @param value
   */
  public void setDeleted_at(String value) {
    this.deleted_at = value;
  }

  /**
   * set the status of the image
   * @param value
   */
  public void setStatus(String value) {
    this.status = value;
  }

  /**
   * set the protected condition of the image
   * @param value
   */
  public void setIs_protected(String value) {
    this.is_protected = value;
  }

  /**
   * set the is_public condition of the image
   * @param value
   */
  public void setIs_public(String value) {
    this.is_public = value;
  }

  /**
   * set the min_ram size of the image
   * @param value
   */
  public void setMin_ram(String value) {
    this.min_ram = value;
  }

  /**
   * set the min_disk size of the image
   * @param value
   */
  public void setMin_disk(String value) {
    this.min_disk = value;
  }

  /**
   * set the owner of the image
   * @param value
   */
  public void setOwner(String value) {
    this.owner = value;
  }

  /**
   * get the name of the image
   * @return
   */
  public String getName() {
    return this.name;
  }

  /**
   * get the image id
   * @return
   */
  public String getId() {
    return this.id;
  }

  /**
   * get the container format of the image
   * @return
   */
  public String getContainer_format() {
    return this.container_format;
  }

  /**
   * get the disk format of the image
   * @return
   */
  public String getDisk_format() {
    return this.disk_format;
  }

  /**
   * get the checksum of the image
   * @return
   */
  public String getChecksum() {
    return this.checksum;
  }

  /**
   * get the size of the image
   * @return
   */
  public String getSize() {
    return this.size;
  }

  /**
   * get the deleted condition of the image
   * @return
   */
  public String getDeleted() {
    return this.deleted;
  }

  /**
   * get the creation time of the image
   * @return
   */
  public String getCreated_at() {
    return this.created_at;
  }

  /**
   * get the updating time of the image
   * @return
   */
  public String getUpdated_at() {
    return this.updated_at;
  }

  /**
   * get the Deleted time of the image
   * @return
   */
  public String getDeleted_at() {
    return this.deleted_at;
  }

  /**
   * get the status of the image
   * @return
   */
  public String getStatus() {
    return this.status;
  }

  /**
   * get the is_protected condition of the image
   * @return
   */
  public String getIs_protected() {
    return this.is_protected;
  }

  /**
   * get the is_public condition of the image
   * @return
   */
  public String getIs_public() {
    return this.is_public;
  }

  /**
   * get the min ram size of the image
   * @return
   */
  public String getMin_ram() {
    return this.min_ram;
  }

  /**
   * get the min disk size of the image
   * @return
   */
  public String getMin_disk() {
    return this.min_disk;
  }

  /**
   * get the owner of the image
   * @return
   */
  public String getOwner() {
    return this.owner;
  }
}
