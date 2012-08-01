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
 *       &lt;attribute name="hostname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="qinqTag" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "getInternalIPRequest")
public class GetInternalIPRequest {
  @XmlAttribute
  protected String hostname;
  @XmlAttribute
  @XmlSchemaType(name = "unsignedInt")
  protected Long qinqTag;

  /**
   * Gets the value of the hostname property.
   * @return possible object is {@link String }
   */
  public String getHostname() {
    return hostname;
  }

  /**
   * Sets the value of the hostname property.
   * @param value allowed object is {@link String }
   */
  public void setHostname(String value) {
    this.hostname = value;
  }

  /**
   * Gets the value of the qinqTag property.
   * @return possible object is {@link Long }
   */
  public Long getQinqTag() {
    return qinqTag;
  }

  /**
   * Sets the value of the qinqTag property.
   * @param value allowed object is {@link Long }
   */
  public void setQinqTag(Long value) {
    this.qinqTag = value;
  }
}
