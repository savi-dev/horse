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
 *         &lt;element name="subagentIP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="subagentPort" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element ref="{description.fabric.xsd.aor}switch" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "subagentIP", "subagentPort", "_switch" })
@XmlRootElement(name = "aggregate", namespace = "description.fabric.xsd.aor")
public class Aggregate {
  @XmlElement(namespace = "description.fabric.xsd.aor", required = true)
  protected String subagentIP;
  @XmlElement(namespace = "description.fabric.xsd.aor")
  @XmlSchemaType(name = "unsignedInt")
  protected long subagentPort;
  @XmlElement(name = "switch", namespace = "description.fabric.xsd.aor",
      required = true)
  protected List<Switch> _switch;

  /**
   * Gets the value of the subagentIP property.
   * @return possible object is {@link String }
   */
  public String getSubagentIP() {
    return subagentIP;
  }

  /**
   * Sets the value of the subagentIP property.
   * @param value allowed object is {@link String }
   */
  public void setSubagentIP(String value) {
    this.subagentIP = value;
  }

  /**
   * Gets the value of the subagentPort property.
   */
  public long getSubagentPort() {
    return subagentPort;
  }

  /**
   * Sets the value of the subagentPort property.
   */
  public void setSubagentPort(long value) {
    this.subagentPort = value;
  }

  /**
   * Gets the value of the switch property.
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the switch property.
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getSwitch().add(newItem);
   * </pre>
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Switch }
   */
  public List<Switch> getSwitch() {
    if (_switch == null) {
      _switch = new ArrayList<Switch>();
    }
    return this._switch;
  }
}
