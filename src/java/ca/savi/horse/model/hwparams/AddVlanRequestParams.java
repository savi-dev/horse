//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, vhudson-jaxb-ri-2.2-147
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2012.03.19 at 01:54:31 PM EDT
//
package ca.savi.horse.model.hwparams;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for addVlanRequestParams complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="addVlanRequestParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rawEthernet" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addVlanRequestParams", propOrder = { "rawEthernet" })
public class AddVlanRequestParams {
  protected Boolean rawEthernet;

  /**
   * Gets the value of the rawEthernet property.
   * @return possible object is {@link Boolean }
   */
  public Boolean isRawEthernet() {
    return rawEthernet;
  }

  /**
   * Sets the value of the rawEthernet property.
   * @param value allowed object is {@link Boolean }
   */
  public void setRawEthernet(Boolean value) {
    this.rawEthernet = value;
  }
}