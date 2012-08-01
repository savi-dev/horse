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
 *       &lt;attribute name="isRawEthernet" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "createLocalTagRequest")
public class CreateLocalTagRequest {
  @XmlAttribute(name = "QinQTag")
  @XmlSchemaType(name = "unsignedInt")
  protected Long qinQTag;
  @XmlAttribute
  protected Boolean isRawEthernet;

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

  /**
   * Gets the value of the isRawEthernet property.
   * @return possible object is {@link Boolean }
   */
  public Boolean isIsRawEthernet() {
    return isRawEthernet;
  }

  /**
   * Sets the value of the isRawEthernet property.
   * @param value allowed object is {@link Boolean }
   */
  public void setIsRawEthernet(Boolean value) {
    this.isRawEthernet = value;
  }
}
