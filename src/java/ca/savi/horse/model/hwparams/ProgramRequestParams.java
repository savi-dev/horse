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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for programRequestParams complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="programRequestParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="isAce" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="bitstream" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "programRequestParams", propOrder = { "isAce", "bitstream" })
public class ProgramRequestParams {
  protected Boolean isAce;
  @XmlElement(required = true)
  protected byte[] bitstream;

  /**
   * Gets the value of the isAce property.
   * @return possible object is {@link Boolean }
   */
  public Boolean isIsAce() {
    return isAce;
  }

  /**
   * Sets the value of the isAce property.
   * @param value allowed object is {@link Boolean }
   */
  public void setIsAce(Boolean value) {
    this.isAce = value;
  }

  /**
   * Gets the value of the bitstream property.
   * @return possible object is byte[]
   */
  public byte[] getBitstream() {
    return bitstream;
  }

  /**
   * Sets the value of the bitstream property.
   * @param value allowed object is byte[]
   */
  public void setBitstream(byte[] value) {
    this.bitstream = ((byte[]) value);
  }
}
