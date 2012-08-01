package ca.savi.cobra.model;

import java.util.ArrayList;
import java.util.List;
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
 *       &lt;sequence>
 *         &lt;element ref="{fabric.XSD.AOR}vlanType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="successful" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="Value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "vlanType" })
@XmlRootElement(name = "getVlansResponse")
public class GetVlansResponse {
  protected List<VlanType> vlanType;
  @XmlAttribute
  protected Boolean successful;
  @XmlAttribute(name = "Value")
  protected String value;

  /**
   * Gets the value of the vlanType property.
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the vlanType property.
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getVlanType().add(newItem);
   * </pre>
   * <p>
   * Objects of the following type(s) are allowed in the list {@link VlanType }
   */
  public List<VlanType> getVlanType() {
    if (vlanType == null) {
      vlanType = new ArrayList<VlanType>();
    }
    return this.vlanType;
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
}
