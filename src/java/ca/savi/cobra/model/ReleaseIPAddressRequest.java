package ca.savi.cobra.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="IP" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "releaseIPAddressRequest")
public class ReleaseIPAddressRequest {
  @XmlAttribute(name = "IP")
  protected String ip;

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
