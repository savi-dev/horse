// Copyright (C) 2012, The SAVI Project.
package ca.savi.horse.service;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import ca.savi.horse.model.FPGA;
import ca.savi.horse.model.hwparams.AddVlanRequestParams;
import ca.savi.horse.model.hwparams.GenericOpRequestParams;
import ca.savi.horse.model.hwparams.GetRequestParams;
import ca.savi.horse.model.hwparams.ListRequestParams;
import ca.savi.horse.model.hwparams.ProgramRequestParams;
import ca.savi.horse.model.hwparams.SetParamRequestParams;
import ca.savi.horse.model.hwunmarshaller.UnmarshalAddVlanRequestParamsRequest;
import ca.savi.horse.model.hwunmarshaller.UnmarshalAddVlanRequestParamsResponse;
import ca.savi.horse.model.hwunmarshaller.UnmarshalGenericOperationRequestParamsRequest;
import ca.savi.horse.model.hwunmarshaller.UnmarshalGenericOperationRequestParamsResponse;
import ca.savi.horse.model.hwunmarshaller.UnmarshalGetRequestParamsRequest;
import ca.savi.horse.model.hwunmarshaller.UnmarshalGetRequestParamsResponse;
import ca.savi.horse.model.hwunmarshaller.UnmarshalListRequestParamsRequest;
import ca.savi.horse.model.hwunmarshaller.UnmarshalListRequestParamsResponse;
import ca.savi.horse.model.hwunmarshaller.UnmarshalProgramRequestParamsRequest;
import ca.savi.horse.model.hwunmarshaller.UnmarshalProgramRequestParamsResponse;
import ca.savi.horse.model.hwunmarshaller.UnmarshalSetParamRequestParamsRequest;
import ca.savi.horse.model.hwunmarshaller.UnmarshalSetParamRequestParamsResponse;

/**
 * This is Hardware Unmarshaller.
 * @author Hesam, Rahimi Koopayi <hesam.rahimikoopayi@utoronto.ca>
 * @version 0.1
 */
public class HardwareUnmarshaller {
  public UnmarshalAddVlanRequestParamsResponse unmarshalAddVlanRequestParams(
      UnmarshalAddVlanRequestParamsRequest inputPart) {
    UnmarshalAddVlanRequestParamsResponse r =
        new UnmarshalAddVlanRequestParamsResponse();
    try {
      JAXBContext jc;
      StringReader stringReader = new StringReader(inputPart.getXmlString());
      jc = JAXBContext.newInstance(AddVlanRequestParams.class);
      Unmarshaller u = jc.createUnmarshaller();
      AddVlanRequestParams params = new AddVlanRequestParams();
      JAXBElement<AddVlanRequestParams> root =
          u.unmarshal(new StreamSource(stringReader),
              AddVlanRequestParams.class);
      params = root.getValue();
      r.setRawEthernet(params.isRawEthernet());
      return r;
    } catch (Exception ex) {
      return null;
    }
  }

  public UnmarshalProgramRequestParamsResponse unmarshalProgramRequestParams(
      UnmarshalProgramRequestParamsRequest inputPart) {
    UnmarshalProgramRequestParamsResponse r =
        new UnmarshalProgramRequestParamsResponse();
    try {
      JAXBContext jc;
      StringReader stringReader = new StringReader(inputPart.getXmlString());
      jc = JAXBContext.newInstance(ProgramRequestParams.class);
      Unmarshaller u = jc.createUnmarshaller();
      ProgramRequestParams params = new ProgramRequestParams();
      JAXBElement<ProgramRequestParams> root =
          u.unmarshal(new StreamSource(stringReader),
              ProgramRequestParams.class);
      params = root.getValue();
      r.setBitstream(params.getBitstream());
      r.setIsAce(params.isIsAce());
      System.out.println("unmarshalling program request successfull");
      return r;
    } catch (Exception ex) {
      System.out.println("unmarshalling program request UN successfull");
      return null;
    }
  }

  public UnmarshalGetRequestParamsResponse unmarshalGetRequestParams(
      UnmarshalGetRequestParamsRequest inputPart) {
    UnmarshalGetRequestParamsResponse r =
        new UnmarshalGetRequestParamsResponse();
    try {
      JAXBContext jc;
      StringReader stringReader = new StringReader(inputPart.getXmlString());
      jc = JAXBContext.newInstance(GetRequestParams.class);
      Unmarshaller u = jc.createUnmarshaller();
      GetRequestParams params = new GetRequestParams();
      JAXBElement<GetRequestParams> root =
          u.unmarshal(new StreamSource(stringReader), GetRequestParams.class);
      params = root.getValue();
      r.setRawEthernet(params.isRawEthernet());
      r.setLRWidth(params.getLRWidth());
      r.setRLWidth(params.getRLWidth());
      r.setUuid(params.getUuid());
      for (FPGA f : params.getFPGA()) {
        FPGA fpga = new FPGA();
        fpga.setName(f.getName());
        for (String s : f.getLink()) {
          fpga.getLink().add(s);
        }
        r.getFPGA().add(fpga);
      }
      return r;
    } catch (Exception ex) {
      return null;
    }
  }

  public UnmarshalListRequestParamsResponse unmarshalListRequestParams(
      UnmarshalListRequestParamsRequest inputPart) {
    UnmarshalListRequestParamsResponse r =
        new UnmarshalListRequestParamsResponse();
    try {
      JAXBContext jc;
      StringReader stringReader = new StringReader(inputPart.getXmlString());
      jc = JAXBContext.newInstance(ListRequestParams.class);
      Unmarshaller u = jc.createUnmarshaller();
      ListRequestParams params = new ListRequestParams();
      JAXBElement<ListRequestParams> root =
          u.unmarshal(new StreamSource(stringReader), ListRequestParams.class);
      params = root.getValue();
      r.setVerbose(params.isVerbose());
      return r;
    } catch (Exception ex) {
      return null;
    }
  }

  public UnmarshalSetParamRequestParamsResponse unmarshalSetParamRequestParams(
      UnmarshalSetParamRequestParamsRequest inputPart) {
    UnmarshalSetParamRequestParamsResponse r =
        new UnmarshalSetParamRequestParamsResponse();
    try {
      JAXBContext jc;
      StringReader stringReader = new StringReader(inputPart.getXmlString());
      jc = JAXBContext.newInstance(SetParamRequestParams.class);
      Unmarshaller u = jc.createUnmarshaller();
      SetParamRequestParams params = new SetParamRequestParams();
      JAXBElement<SetParamRequestParams> root =
          u.unmarshal(new StreamSource(stringReader),
              SetParamRequestParams.class);
      params = root.getValue();
      r.setIP(params.getIP());
      r.setVlanID(params.getVlanID());
      return r;
    } catch (Exception ex) {
      return null;
    }
  }

  public UnmarshalGenericOperationRequestParamsResponse
      unmarshalGenericOperationRequestParams(
          UnmarshalGenericOperationRequestParamsRequest inputPart) {
    UnmarshalGenericOperationRequestParamsResponse r =
        new UnmarshalGenericOperationRequestParamsResponse();
    try {
      JAXBContext jc;
      StringReader stringReader = new StringReader(inputPart.getXmlString());
      jc = JAXBContext.newInstance(GenericOpRequestParams.class);
      Unmarshaller u = jc.createUnmarshaller();
      GenericOpRequestParams params = new GenericOpRequestParams();
      JAXBElement<GenericOpRequestParams> root =
          u.unmarshal(new StreamSource(stringReader),
              GenericOpRequestParams.class);
      params = root.getValue();
      r.setSetRegisterValue(params.getSetRegisterValue());
      r.setUuid(params.getUuid());
      return r;
    } catch (Exception ex) {
      return null;
    }
  }
}
