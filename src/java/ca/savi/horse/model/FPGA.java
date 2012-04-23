// Copyright (C) 2012, The SAVI Project.
package ca.savi.horse.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * This is FPGA class.
 * @author Hesam, Rahimi Koopayi <hesam.rahimikoopayi@utoronto.ca>
 * @version 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FPGA", propOrder = { "name", "uuid", "link" })
public class FPGA {
  protected String name;
  protected String uuid;
  protected List<String> link;

  public FPGA() {
  }

  public FPGA(String name) {
    this.name = name;
    this.link = null;
    this.uuid = null;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getUuid() {
    return uuid;
  }

  /**
   * Gets the value of the link property.
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the link property.
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getLink().add(newItem);
   * </pre>
   * <p>
   * Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getLink() {
    if (link == null) {
      link = new ArrayList<String>();
    }
    return link;
  }

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
}