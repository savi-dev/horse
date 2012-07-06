// Copyright (C) 2012, The SAVI Project.
package ca.savi.glance.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Jie Yu Lin (Eric) <jieyu.lin@mail.utoronto.ca>
 * @version 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "Imnodes" })
public class GlanceClientGetInfoResponse {
  @XmlElement(name = "images")
  protected List<Imnode> Imnodes;

  /**
   * add an Imnode to the Imnodes list
   * @param node
   */
  public void setImnodes(Imnode node) {
    if (Imnodes == null) {
      Imnodes = new ArrayList<Imnode>();
    }
    Imnodes.add(node);
  }

  /**
   * get the Imnode list
   * @return
   */
  public List<Imnode> getImnodes() {
    if (Imnodes == null) {
      Imnodes = new ArrayList<Imnode>();
    }
    return Imnodes;
  }

  @Override
  public String toString() {
    String str = new String();
    for (int i = 0; i < Imnodes.size(); i++) {
      str = str + "IMAGE" + (i + 1) + "[" + Imnodes.get(i).toString() + "] ";
    }
    return str;
  }
}
