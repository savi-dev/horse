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
 *         &lt;element name="reservedID" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="broadcastID" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{description.fabric.xsd.aor}aggregate" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "reservedID", "broadcastID", "aggregate" })
@XmlRootElement(name = "VANI_Fabric", namespace = "description.fabric.xsd.aor")
public class VANIFabric {
  @XmlElement(namespace = "description.fabric.xsd.aor", type = Long.class)
  @XmlSchemaType(name = "unsignedInt")
  protected List<Long> reservedID;
  @XmlElement(namespace = "description.fabric.xsd.aor", type = Long.class)
  @XmlSchemaType(name = "unsignedInt")
  protected List<Long> broadcastID;
  @XmlElement(namespace = "description.fabric.xsd.aor", required = true)
  protected List<Aggregate> aggregate;

  /**
   * Gets the value of the reservedID property.
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the reservedID property.
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getReservedID().add(newItem);
   * </pre>
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Long }
   */
  public List<Long> getReservedID() {
    if (reservedID == null) {
      reservedID = new ArrayList<Long>();
    }
    return this.reservedID;
  }

  /**
   * Gets the value of the broadcastID property.
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the broadcastID property.
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getBroadcastID().add(newItem);
   * </pre>
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Long }
   */
  public List<Long> getBroadcastID() {
    if (broadcastID == null) {
      broadcastID = new ArrayList<Long>();
    }
    return this.broadcastID;
  }

  /**
   * Gets the value of the aggregate property.
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the aggregate property.
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getAggregate().add(newItem);
   * </pre>
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Aggregate }
   */
  public List<Aggregate> getAggregate() {
    if (aggregate == null) {
      aggregate = new ArrayList<Aggregate>();
    }
    return this.aggregate;
  }
}
