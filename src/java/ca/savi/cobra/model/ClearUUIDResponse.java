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
 *       &lt;attribute name="lastUUID" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="vlan" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *       &lt;attribute name="Value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="successful" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "clearUUIDResponse")
public class ClearUUIDResponse {
  @XmlAttribute
  protected Boolean lastUUID;
  @XmlAttribute
  @XmlSchemaType(name = "unsignedInt")
  protected Long vlan;
  @XmlAttribute(name = "Value")
  protected String value;
  @XmlAttribute
  protected Boolean successful;

  /**
   * Gets the value of the lastUUID property.
   * @return possible object is {@link Boolean }
   */
  public Boolean isLastUUID() {
    return lastUUID;
  }

  /**
   * Sets the value of the lastUUID property.
   * @param value allowed object is {@link Boolean }
   */
  public void setLastUUID(Boolean value) {
    this.lastUUID = value;
  }

  /**
   * Gets the value of the vlan property.
   * @return possible object is {@link Long }
   */
  public Long getVlan() {
    return vlan;
  }

  /**
   * Sets the value of the vlan property.
   * @param value allowed object is {@link Long }
   */
  public void setVlan(Long value) {
    this.vlan = value;
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
