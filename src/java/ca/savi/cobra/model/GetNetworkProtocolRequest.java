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
 *       &lt;attribute name="QinQTag" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "getNetworkProtocolRequest")
public class GetNetworkProtocolRequest {
  @XmlAttribute(name = "QinQTag")
  @XmlSchemaType(name = "unsignedInt")
  protected Long qinQTag;

  /**
   * Gets the value of the qinQTag property.
   * @return possible object is {@link Long }
   */
  public Long getQinQTag() {
    return qinQTag;
  }

  /**
   * Sets the value of the qinQTag property.
   * @param value allowed object is {@link Long }
   */
  public void setQinQTag(Long value) {
    this.qinQTag = value;
  }
}
