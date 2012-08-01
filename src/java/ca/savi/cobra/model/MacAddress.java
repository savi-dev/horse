package ca.savi.cobra.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *       &lt;sequence>
 *         &lt;element name="MAC" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="interfaceIndex" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "mac", "interfaceIndex" })
@XmlRootElement(name = "MacAddress", namespace = "description.fabric.xsd.aor")
public class MacAddress {
  @XmlElement(name = "MAC", namespace = "description.fabric.xsd.aor",
      required = true)
  protected String mac;
  @XmlElement(namespace = "description.fabric.xsd.aor")
  @XmlSchemaType(name = "unsignedInt")
  protected long interfaceIndex;

  /**
   * Gets the value of the mac property.
   * @return possible object is {@link String }
   */
  public String getMAC() {
    return mac;
  }

  /**
   * Sets the value of the mac property.
   * @param value allowed object is {@link String }
   */
  public void setMAC(String value) {
    this.mac = value;
  }

  /**
   * Gets the value of the interfaceIndex property.
   */
  public long getInterfaceIndex() {
    return interfaceIndex;
  }

  /**
   * Sets the value of the interfaceIndex property.
   */
  public void setInterfaceIndex(long value) {
    this.interfaceIndex = value;
  }
}
