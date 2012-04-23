// Copyright (C) 2012, The SAVI Project.
package ca.savi.horse.model;

/**
 * This is Icnode class.
 * @author Hesam, Rahimi Koopayi <hesam.rahimikoopayi@utoronto.ca>
 * @version 0.1
 */
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "uuid", "name", "timestamp", "duration",
    "linkLeft", "linkRight", "lrWidth", "rlWidth", "inUse" })
public class Icnode {
  protected String uuid;
  protected String name;
  @XmlSchemaType(name = "unsignedLong")
  protected BigInteger timestamp;
  @XmlSchemaType(name = "unsignedLong")
  protected BigInteger duration;
  protected String linkLeft;
  protected String linkRight;
  @XmlElement(name = "LRWidth")
  @XmlSchemaType(name = "unsignedShort")
  protected Integer lrWidth;
  @XmlElement(name = "RLWidth")
  @XmlSchemaType(name = "unsignedShort")
  protected Integer rlWidth;
  protected Boolean inUse;

  /**
   * Gets the value of the uuid property.
   * @return possible object is {@link String }
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * Sets the value of the uuid property.
   * @param value allowed object is {@link String }
   */
  public void setUuid(String value) {
    this.uuid = value;
  }

  /**
   * Gets the value of the name property.
   * @return possible object is {@link String }
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the value of the name property.
   * @param value allowed object is {@link String }
   */
  public void setName(String value) {
    this.name = value;
  }

  /**
   * Gets the value of the timestamp property.
   * @return possible object is {@link BigInteger }
   */
  public BigInteger getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the value of the timestamp property.
   * @param value allowed object is {@link BigInteger }
   */
  public void setTimestamp(BigInteger value) {
    this.timestamp = value;
  }

  /**
   * Gets the value of the duration property.
   * @return possible object is {@link BigInteger }
   */
  public BigInteger getDuration() {
    return duration;
  }

  /**
   * Sets the value of the duration property.
   * @param value allowed object is {@link BigInteger }
   */
  public void setDuration(BigInteger value) {
    this.duration = value;
  }

  /**
   * Gets the value of the linkLeft property.
   * @return possible object is {@link String }
   */
  public String getLinkLeft() {
    return linkLeft;
  }

  /**
   * Sets the value of the linkLeft property.
   * @param value allowed object is {@link String }
   */
  public void setLinkLeft(String value) {
    this.linkLeft = value;
  }

  /**
   * Gets the value of the linkRight property.
   * @return possible object is {@link String }
   */
  public String getLinkRight() {
    return linkRight;
  }

  /**
   * Sets the value of the linkRight property.
   * @param value allowed object is {@link String }
   */
  public void setLinkRight(String value) {
    this.linkRight = value;
  }

  /**
   * Gets the value of the lrWidth property.
   * @return possible object is {@link Integer }
   */
  public Integer getLRWidth() {
    return lrWidth;
  }

  /**
   * Sets the value of the lrWidth property.
   * @param value allowed object is {@link Integer }
   */
  public void setLRWidth(Integer value) {
    this.lrWidth = value;
  }

  /**
   * Gets the value of the rlWidth property.
   * @return possible object is {@link Integer }
   */
  public Integer getRLWidth() {
    return rlWidth;
  }

  /**
   * Sets the value of the rlWidth property.
   * @param value allowed object is {@link Integer }
   */
  public void setRLWidth(Integer value) {
    this.rlWidth = value;
  }

  /**
   * Gets the value of the inUse property.
   * @return possible object is {@link Boolean }
   */
  public Boolean isInUse() {
    return inUse;
  }

  /**
   * Sets the value of the inUse property.
   * @param value allowed object is {@link Boolean }
   */
  public void setInUse(Boolean value) {
    this.inUse = value;
  }
}