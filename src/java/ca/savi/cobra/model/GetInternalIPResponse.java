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
 *       &lt;attribute name="ip" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="successful" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "getInternalIPResponse")
public class GetInternalIPResponse {
  @XmlAttribute
  protected String ip;
  @XmlAttribute(name = "Value")
  protected String value;
  @XmlAttribute
  protected Boolean successful;

  /**
   * Gets the value of the ip property.
   * @return possible object is {@link String }
   */
  public String getIp() {
    return ip;
  }

  /**
   * Sets the value of the ip property.
   * @param value allowed object is {@link String }
   */
  public void setIp(String value) {
    this.ip = value;
  }

  /**
   * Gets the value of the value property.
   * @return possible object is {@link String }
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value of the value property.
   * @param value allowed object is {@link String }
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Gets the value of the successful property.
   * @return possible object is {@link Boolean }
   */
  public Boolean isSuccessful() {
    return successful;
  }

  /**
   * Sets the value of the successful property.
   * @param value allowed object is {@link Boolean }
   */
  public void setSuccessful(Boolean value) {
    this.successful = value;
  }
}
