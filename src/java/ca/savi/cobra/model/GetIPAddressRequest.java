package ca.savi.cobra.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *       &lt;attribute name="Q_in_Q_tag" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *       &lt;attribute name="uuid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "getIPAddressRequest")
public class GetIPAddressRequest {
  @XmlAttribute(name = "Q_in_Q_tag")
  @XmlSchemaType(name = "unsignedInt")
  protected Long qInQTag;
  @XmlAttribute
  protected String uuid;

  /**
   * Gets the value of the qInQTag property.
   * @return possible object is {@link Long }
   */
  public Long getQInQTag() {
    return qInQTag;
  }

  /**
   * Sets the value of the qInQTag property.
   * @param value allowed object is {@link Long }
   */
  public void setQInQTag(Long value) {
    this.qInQTag = value;
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
}
