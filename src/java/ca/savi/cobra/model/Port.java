package ca.savi.cobra.model;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="portBit" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="uuid" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{description.fabric.xsd.aor}MacAddress" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "portBit", "type", "uuid", "macAddress" })
@XmlRootElement(name = "Port", namespace = "description.fabric.xsd.aor")
public class Port {
  @XmlElement(namespace = "description.fabric.xsd.aor")
  @XmlSchemaType(name = "unsignedInt")
  protected long portBit;
  @XmlElement(namespace = "description.fabric.xsd.aor", required = true)
  protected String type;
  @XmlElement(namespace = "description.fabric.xsd.aor")
  protected List<String> uuid;
  @XmlElement(name = "MacAddress", namespace = "description.fabric.xsd.aor")
  protected MacAddress macAddress;

  /**
   * Gets the value of the portBit property.
   */
  public long getPortBit() {
    return portBit;
  }

  /**
   * Sets the value of the portBit property.
   */
  public void setPortBit(long value) {
    this.portBit = value;
  }

  /**
   * Gets the value of the type property.
   * @return possible object is {@link String }
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the value of the type property.
   * @param value allowed object is {@link String }
   */
  public void setType(String value) {
    this.type = value;
  }

  /**
   * Gets the value of the uuid property.
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the uuid property.
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getUuid().add(newItem);
   * </pre>
   * <p>
   * Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getUuid() {
    if (uuid == null) {
      uuid = new ArrayList<String>();
    }
    return this.uuid;
  }

  /**
   * Gets the value of the macAddress property.
   * @return possible object is {@link MacAddress }
   */
  public MacAddress getMacAddress() {
    return macAddress;
  }

  /**
   * Sets the value of the macAddress property.
   * @param value allowed object is {@link MacAddress }
   */
  public void setMacAddress(MacAddress value) {
    this.macAddress = value;
  }
}
