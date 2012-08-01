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
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="uuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numPorts" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element name="switchIP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="switchPort" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element ref="{description.fabric.xsd.aor}Port" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "name", "uuid", "numPorts", "switchIP",
    "switchPort", "port" })
@XmlRootElement(name = "switch", namespace = "description.fabric.xsd.aor")
public class Switch {
  @XmlElement(namespace = "description.fabric.xsd.aor", required = true)
  protected String name;
  @XmlElement(namespace = "description.fabric.xsd.aor", required = true)
  protected String uuid;
  @XmlElement(namespace = "description.fabric.xsd.aor")
  @XmlSchemaType(name = "unsignedInt")
  protected long numPorts;
  @XmlElement(namespace = "description.fabric.xsd.aor", required = true)
  protected String switchIP;
  @XmlElement(namespace = "description.fabric.xsd.aor")
  @XmlSchemaType(name = "unsignedInt")
  protected long switchPort;
  @XmlElement(name = "Port", namespace = "description.fabric.xsd.aor",
      required = true)
  protected List<Port> port;

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
   * Gets the value of the numPorts property.
   */
  public long getNumPorts() {
    return numPorts;
  }

  /**
   * Sets the value of the numPorts property.
   */
  public void setNumPorts(long value) {
    this.numPorts = value;
  }

  /**
   * Gets the value of the switchIP property.
   * @return possible object is {@link String }
   */
  public String getSwitchIP() {
    return switchIP;
  }

  /**
   * Sets the value of the switchIP property.
   * @param value allowed object is {@link String }
   */
  public void setSwitchIP(String value) {
    this.switchIP = value;
  }

  /**
   * Gets the value of the switchPort property.
   */
  public long getSwitchPort() {
    return switchPort;
  }

  /**
   * Sets the value of the switchPort property.
   */
  public void setSwitchPort(long value) {
    this.switchPort = value;
  }

  /**
   * Gets the value of the port property.
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the port property.
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getPort().add(newItem);
   * </pre>
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Port }
   */
  public List<Port> getPort() {
    if (port == null) {
      port = new ArrayList<Port>();
    }
    return this.port;
  }
}
