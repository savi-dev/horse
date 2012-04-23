// Copyright (C) 2012, The SAVI Project.
package ca.savi.horse.model;

/**
 * This is Hwnode class.
 * @author Hesam, Rahimi Koopayi <hesam.rahimikoopayi@utoronto.ca>
 * @version 0.1
 */
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "uuid", "name", "timestamp", "duration",
    "inUse", "ip" })
public class Hwnode {
  protected String uuid;
  protected String name;
  @XmlSchemaType(name = "unsignedLong")
  protected BigInteger timestamp;
  @XmlSchemaType(name = "unsignedLong")
  protected BigInteger duration;
  protected Boolean inUse;
  @XmlElement(name = "IP")
  @XmlSchemaType(name = "anyURI")
  protected String ip;
  @XmlAttribute(name = "leftUuid")
  protected String leftUuid;
  @XmlAttribute(name = "rightUuid")
  protected String rightUuid;

  public String getLeftUuid() {
    return leftUuid;
  }

  public String getRightUuid() {
    return rightUuid;
  }

  public void setLeftUuid(String leftUuid) {
    this.leftUuid = leftUuid;
  }

  public void setRightUuid(String rightUuid) {
    this.rightUuid = rightUuid;
  }

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

  /**
   * Gets the value of the ip property.
   * @return possible object is {@link String }
   */
  public String getIP() {
    return ip;
  }

  /**
   * Sets the value of the ip property.
   * @param value allowed object is {@link String }
   */
  public void setIP(String value) {
    this.ip = value;
  }
}
