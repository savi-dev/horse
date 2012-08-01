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
 *       &lt;attribute name="switchUUID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="portBit" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "removeMacRestrictionRequest")
public class RemoveMacRestrictionRequest {
  @XmlAttribute
  protected String switchUUID;
  @XmlAttribute
  @XmlSchemaType(name = "unsignedInt")
  protected Long portBit;

  /**
   * Gets the value of the switchUUID property.
   * @return possible object is {@link String }
   */
  public String getSwitchUUID() {
    return switchUUID;
  }

  /**
   * Sets the value of the switchUUID property.
   * @param value allowed object is {@link String }
   */
  public void setSwitchUUID(String value) {
    this.switchUUID = value;
  }

  /**
   * Gets the value of the portBit property.
   * @return possible object is {@link Long }
   */
  public Long getPortBit() {
    return portBit;
  }

  /**
   * Sets the value of the portBit property.
   * @param value allowed object is {@link Long }
   */
  public void setPortBit(Long value) {
    this.portBit = value;
  }
}
