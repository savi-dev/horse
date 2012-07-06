//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, vhudson-jaxb-ri-2.2-147
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2012.03.19 at 01:54:27 PM EDT
//
package ca.savi.horse.model.hardwareprocess;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="bitstream"
 *         type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/all>
 *       &lt;attribute name="uuid"
 *       type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="isAce"
 *       type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "programResourceRequest")
public class ProgramResourceRequest {
  @XmlElement(required = true)
  protected String imageUuid;
  @XmlAttribute(name = "uuid")
  protected String uuid;
  @XmlAttribute(name = "isAce")
  protected Boolean isAce;
  @XmlElement(required = true)
  protected String serviceEndPoint;

  /**
   * Gets the value of the imageUuid property.
   * @return possible object is byte[]
   */
  public String getImageUuid() {
    return imageUuid;
  }

  /**
   * Sets the value of the imageUuid property.
   * @param value allowed object is byte[]
   */
  public void setImageUuid(String value) {
    this.imageUuid = value;
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
   * Gets the value of the isAce property.
   * @return possible object is {@link Boolean }
   */
  public Boolean isIsAce() {
    return isAce;
  }

  /**
   * Sets the value of the isAce property.
   * @param value allowed object is {@link Boolean }
   */
  public void setIsAce(Boolean value) {
    this.isAce = value;
  }

  public void setServiceEndPoint(String value) {
    this.serviceEndPoint = value;
  }

  public String getServiceEndPoint() {
    return this.serviceEndPoint;
  }
}
