package ca.savi.cobra.model;

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
 *       &lt;attribute name="descriptionFile" type="{http://www.w3.org/2001/XMLSchema}base64Binary" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "describeFabricRequest")
public class DescribeFabricRequest {
  @XmlAttribute
  protected byte[] descriptionFile;

  /**
   * Gets the value of the descriptionFile property.
   * @return possible object is byte[]
   */
  public byte[] getDescriptionFile() {
    return descriptionFile;
  }

  /**
   * Sets the value of the descriptionFile property.
   * @param value allowed object is byte[]
   */
  public void setDescriptionFile(byte[] value) {
    this.descriptionFile = ((byte[]) value);
  }
}
