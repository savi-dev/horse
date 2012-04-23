// Copyright (C) 2012, The SAVI Project.
package ca.savi.horse.service;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import ca.savi.horse.model.FPGA;
import ca.savi.horse.model.Hwnode;
import ca.savi.horse.model.Icnode;
import ca.savi.horse.model.hwmarshaller.MarshalGenericOperationResponseParamsRequest;
import ca.savi.horse.model.hwmarshaller.MarshalGenericOperationResponseParamsResponse;
import ca.savi.horse.model.hwmarshaller.MarshalGetResponseParamsRequest;
import ca.savi.horse.model.hwmarshaller.MarshalGetResponseParamsResponse;
import ca.savi.horse.model.hwmarshaller.MarshalListResponseParamsRequest;
import ca.savi.horse.model.hwmarshaller.MarshalListResponseParamsResponse;
import ca.savi.horse.model.hwmarshaller.MarshalStatusResponseParamsRequest;
import ca.savi.horse.model.hwmarshaller.MarshalStatusResponseParamsResponse;
import ca.savi.horse.model.hwparams.GenericOpResponseParams;
import ca.savi.horse.model.hwparams.GetResponseParams;
import ca.savi.horse.model.hwparams.ListResponseParams;
import ca.savi.horse.model.hwparams.StatusResponseParams;
/**
 * This is Hardware Marshaller.
 * @author Hesam, Rahimi Koopayi <hesam.rahimikoopayi@utoronto.ca>
 * @version 0.1
 */
public class HardwareMarshaller {
  public MarshalGetResponseParamsResponse marshalGetResponseParams(
      MarshalGetResponseParamsRequest inputPart) {
    MarshalGetResponseParamsResponse r = new MarshalGetResponseParamsResponse();
    try {
      GetResponseParams params = new GetResponseParams();
      for (FPGA f : inputPart.getFPGA()) {
        FPGA fpga = new FPGA();
        fpga.setName(f.getName());
        fpga.setUuid(f.getUuid());
        params.getFPGA().add(fpga);
      }
      JAXBContext context;
      StringWriter stringWriter = new StringWriter();
      context = JAXBContext.newInstance(GetResponseParams.class);
      Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      m.marshal(new JAXBElement(
          new QName("", GetResponseParams.class.getName()),
          GetResponseParams.class, params), stringWriter);
      r.setXml(stringWriter.toString());
      return r;
    } catch (Exception ex) {
      return null;
    }
  }

  public MarshalListResponseParamsResponse marshalListResponseParams(
      MarshalListResponseParamsRequest inputPart) {
    MarshalListResponseParamsResponse r =
        new MarshalListResponseParamsResponse();
    try {
      ListResponseParams params = new ListResponseParams();
      params.setVerbose(inputPart.isVerbose());
      params.setMaximum(inputPart.getMaximum());
      params.setCurrent(inputPart.getCurrent());
      for (Hwnode hw : inputPart.getHwnode()) {
        Hwnode hwnode = new Hwnode();
        hwnode.setDuration(hw.getDuration());
        hwnode.setIP(hw.getIP());
        hwnode.setInUse(hw.isInUse());
        hwnode.setName(hw.getName());
        hwnode.setTimestamp(hw.getTimestamp());
        hwnode.setUuid(hw.getUuid());
        params.getHwnode().add(hwnode);
      }
      for (Icnode ic : inputPart.getIcnode()) {
        Icnode icnode = new Icnode();
        icnode.setDuration(ic.getDuration());
        icnode.setInUse(ic.isInUse());
        icnode.setLRWidth(ic.getLRWidth());
        icnode.setRLWidth(ic.getRLWidth());
        icnode.setLinkLeft(ic.getLinkLeft());
        icnode.setLinkRight(ic.getLinkRight());
        icnode.setName(ic.getName());
        icnode.setTimestamp(ic.getTimestamp());
        icnode.setUuid(ic.getUuid());
        params.getIcnode().add(icnode);
      }
      JAXBContext context;
      StringWriter stringWriter = new StringWriter();
      context = JAXBContext.newInstance(ListResponseParams.class);
      Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      m.marshal(
          new JAXBElement(new QName("", ListResponseParams.class.getName()),
              ListResponseParams.class, params), stringWriter);
      r.setXml(stringWriter.toString());
      return r;
    } catch (Exception ex) {
      return null;
    }
  }

  public MarshalStatusResponseParamsResponse marshalStatusResponseParams(
      MarshalStatusResponseParamsRequest inputPart) {
    MarshalStatusResponseParamsResponse r =
        new MarshalStatusResponseParamsResponse();
    try {
      StatusResponseParams params = new StatusResponseParams();
      params.setCode(inputPart.getCode());
      JAXBContext context;
      StringWriter stringWriter = new StringWriter();
      context = JAXBContext.newInstance(StatusResponseParams.class);
      Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      m.marshal(
          new JAXBElement(new QName("", StatusResponseParams.class.getName()),
              StatusResponseParams.class, params), stringWriter);
      r.setXml(stringWriter.toString());
      return r;
    } catch (Exception ex) {
      return null;
    }
  }

  public MarshalGenericOperationResponseParamsResponse
      marshalGenericOperationResponseParams(
          MarshalGenericOperationResponseParamsRequest inputPart) {
    MarshalGenericOperationResponseParamsResponse r =
        new MarshalGenericOperationResponseParamsResponse();
    try {
      GenericOpResponseParams params = new GenericOpResponseParams();
      params.setReadRegisterValue(inputPart.getReadRegisterValue());
      JAXBContext context;
      StringWriter stringWriter = new StringWriter();
      context = JAXBContext.newInstance(GenericOpResponseParams.class);
      Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      m.marshal(
          new JAXBElement(
              new QName("", GenericOpResponseParams.class.getName()),
              GenericOpResponseParams.class, params), stringWriter);
      r.setXml(stringWriter.toString());
      return r;
    } catch (Exception ex) {
      return null;
    }
  }
}
