// Copyright (C) 2012, The SAVI Project.
package ca.savi.horse.service;

import javax.jws.WebService;

import ca.savi.camel.model.AddVlanRequest;
import ca.savi.camel.model.AddVlanResponse;
import ca.savi.camel.model.GenericOperationRequest;
import ca.savi.camel.model.GenericOperationResponse;
import ca.savi.camel.model.GetImageRequest;
import ca.savi.camel.model.GetImageResponse;
import ca.savi.camel.model.GetParamRequest;
import ca.savi.camel.model.GetParamResponse;
import ca.savi.camel.model.GetRequest;
import ca.savi.camel.model.GetResponse;
import ca.savi.camel.model.InitRequest;
import ca.savi.camel.model.InitResponse;
import ca.savi.camel.model.ListRequest;
import ca.savi.camel.model.ListResponse;
import ca.savi.camel.model.ProgramRequest;
import ca.savi.camel.model.ProgramResponse;
import ca.savi.camel.model.RebootRequest;
import ca.savi.camel.model.RebootResponse;
import ca.savi.camel.model.ReleaseRequest;
import ca.savi.camel.model.ReleaseResponse;
import ca.savi.camel.model.RemoveVlanRequest;
import ca.savi.camel.model.RemoveVlanResponse;
import ca.savi.camel.model.ResetRequest;
import ca.savi.camel.model.ResetResponse;
import ca.savi.camel.model.SaveImageRequest;
import ca.savi.camel.model.SaveImageResponse;
import ca.savi.camel.model.SetParamRequest;
import ca.savi.camel.model.SetParamResponse;
import ca.savi.camel.model.StatusRequest;
import ca.savi.camel.model.StatusResponse;
import ca.savi.camel.model.TerminateRequest;
import ca.savi.camel.model.TerminateResponse;
import ca.savi.camel.service.AORResourcePortType;
import ca.savi.horse.hardware.HardwareBpel2Java;
import ca.savi.horse.model.FPGA;
import ca.savi.horse.model.hwmarshaller.MarshalGenericOperationResponseParamsRequest;
import ca.savi.horse.model.hwmarshaller.MarshalGenericOperationResponseParamsResponse;
import ca.savi.horse.model.hwmarshaller.MarshalGetResponseParamsRequest;
import ca.savi.horse.model.hwmarshaller.MarshalGetResponseParamsResponse;
import ca.savi.horse.model.hwmarshaller.MarshalListResponseParamsRequest;
import ca.savi.horse.model.hwmarshaller.MarshalListResponseParamsResponse;
import ca.savi.horse.model.hwmarshaller.MarshalStatusResponseParamsRequest;
import ca.savi.horse.model.hwmarshaller.MarshalStatusResponseParamsResponse;
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

/**
 * This is Hardware Resource Web Service.
 * @author Hesam, Rahimi Koopayi <hesam.rahimikoopayi@utoronto.ca>
 * @version 0.1
 */
@WebService(
    serviceName = "AORResourceService",
    portName = "AORResourcePortTypeBindingPort",
    endpointInterface = "ca.savi.camel.service.AORResourcePortType",
    targetNamespace = "http://camel.savi.ca/AORResource")
public class HWResourceWebService implements AORResourcePortType {
  HardwareMarshaller hardwareMarshaller = null;
  HardwareUnmarshaller hardwareUnmarshaller = null;
  HardwareBpel2Java aorHardwareProcess = null;

  public HWResourceWebService() {
    hardwareMarshaller = new HardwareMarshaller(); // HardwareMarshallerPartnerLink
    hardwareUnmarshaller = new HardwareUnmarshaller(); // HardwareUnmarshallerPartnerLink
    aorHardwareProcess = new HardwareBpel2Java(); // AORHardwareProcessPartnerLink
    // This class is AORResourcePartnerLink
  }

  public GetResponse aorResourceGet(GetRequest aorResourceGetIn) {
    GetResponse aorResourceGetOut = new GetResponse();
    UnmarshalGetRequestParamsResponse unmarshalGetRequestParamsOut =
        new UnmarshalGetRequestParamsResponse();
    UnmarshalGetRequestParamsRequest unmarshalGetRequestParamsIn =
        new UnmarshalGetRequestParamsRequest();
    MarshalGetResponseParamsResponse marshalGetResponseParamsOut =
        new MarshalGetResponseParamsResponse();
    MarshalGetResponseParamsRequest marshalGetResponseParamsIn =
        new MarshalGetResponseParamsRequest();
    ca.savi.horse.model.hardwareprocess.ResourceGetResponse resourceGetOut =
        new ca.savi.horse.model.hardwareprocess.ResourceGetResponse();
    ca.savi.horse.model.hardwareprocess.ResourceGetRequest resourceGetIn =
        new ca.savi.horse.model.hardwareprocess.ResourceGetRequest();
    unmarshalGetRequestParamsIn.setXmlString(aorResourceGetIn.getXmlString());
    unmarshalGetRequestParamsOut =
        hardwareUnmarshaller
            .unmarshalGetRequestParams(unmarshalGetRequestParamsIn);
    for (int forEach4Counter = 0; forEach4Counter < unmarshalGetRequestParamsOut
        .getFPGA().size(); forEach4Counter++) {
      // if (resourceGetIn.getFPGA().isEmpty())
      // resourceGetIn.getFPGA().add(unmarshalGetRequestParamsOut.getFPGA().get(0));
      FPGA fpga = unmarshalGetRequestParamsOut.getFPGA().get(forEach4Counter);
      resourceGetIn.getFPGA().add(fpga);
      // resourceGetIn.getFPGA().get(orEach4Counter).setName(unmarshalGetRequestParamsOut.getFPGA().get(forEach4Counter).getName());
      // ***************************************************** THE "setLink"
      // METHOD IS NOT AVAILABLE IN FOLLOWING LINE IN FPGA CLASS
      // **************************
      // resourceGetIn.getFPGA().get(forEach4Counter).setLink(unmarshalGetRequestParamsOut.getFPGA().get(forEach4Counter).getLink());
      // resourceGetIn.getFPGA().get(forEach4Counter).getLink().add(forEach4Counter,
      // unmarshalGetRequestParamsOut.getFPGA().get(forEach4Counter).getLink().get(forEach4Counter));
      // is the above line correct????????????????????????????????
    }
    // Assign 17
    resourceGetIn.setUuid(unmarshalGetRequestParamsOut.getUuid());
    resourceGetIn.setLRWidth(unmarshalGetRequestParamsOut.getLRWidth());
    resourceGetIn.setRLWidth(unmarshalGetRequestParamsOut.getRLWidth());
    resourceGetIn.setRawEthernet(unmarshalGetRequestParamsOut.isRawEthernet());
    resourceGetIn.setQinQTag(aorResourceGetIn.getQinqTag());
    resourceGetIn.setDuration(aorResourceGetIn.getDuration());
    resourceGetOut = aorHardwareProcess.resourceGet(resourceGetIn);
    for (int forEach3Counter = 0; forEach3Counter < resourceGetOut.getFPGA()
        .size(); forEach3Counter++) {
      FPGA fpga = resourceGetOut.getFPGA().get(forEach3Counter);
      marshalGetResponseParamsIn.getFPGA().add(fpga);
      // marshalGetResponseParamsIn.getFPGA().get(forEach3Counter).setName(resourceGetOut.getFPGA().get(forEach3Counter).getName());
      // marshalGetResponseParamsIn.getFPGA().get(forEach3Counter).setUuid(resourceGetOut.getFPGA().get(forEach3Counter).getUuid());
    }
    aorResourceGetOut.setError(resourceGetOut.getValue());
    aorResourceGetOut.setSuccessful(resourceGetOut.isSuccessful());
    aorResourceGetOut.getUuidList().add(resourceGetOut.getUuid());
    return aorResourceGetOut;
  }

  public ReleaseResponse
      aorResourceRelease(ReleaseRequest aorResourceReleaseIn) {
    ReleaseResponse aorResourceReleaseOut = new ReleaseResponse();
    ca.savi.horse.model.hardwareprocess.ResourceReleaseResponse resourceReleaseOut =
        new ca.savi.horse.model.hardwareprocess.ResourceReleaseResponse();
    ca.savi.horse.model.hardwareprocess.ResourceReleaseRequest resourceReleaseIn =
        new ca.savi.horse.model.hardwareprocess.ResourceReleaseRequest();
    resourceReleaseIn.setUuid(aorResourceReleaseIn.getUuid());
    resourceReleaseOut = aorHardwareProcess.resourceRelease(resourceReleaseIn);
    aorResourceReleaseOut.setSuccessful(resourceReleaseOut.isSuccessful());
    aorResourceReleaseOut.setError(resourceReleaseOut.getValue());
    return aorResourceReleaseOut;
  }

  public StatusResponse aorResourceStatus(StatusRequest aorResourceStatusIn) {
    StatusResponse aorResourceStatusOut = new StatusResponse();
    MarshalStatusResponseParamsResponse marshalStatusResponseParamsOut =
        new MarshalStatusResponseParamsResponse();
    MarshalStatusResponseParamsRequest marshalStatusResponseParamsIn =
        new MarshalStatusResponseParamsRequest();
    ca.savi.horse.model.hardwareprocess.ResourceStatusResponse resourceStatusOut =
        new ca.savi.horse.model.hardwareprocess.ResourceStatusResponse();
    ca.savi.horse.model.hardwareprocess.ResourceStatusRequest resourceStatusIn =
        new ca.savi.horse.model.hardwareprocess.ResourceStatusRequest();
    resourceStatusIn.setUuid(aorResourceStatusIn.getUuid());
    resourceStatusOut = aorHardwareProcess.resourceStatus(resourceStatusIn);
    marshalStatusResponseParamsIn.setCode(resourceStatusOut.getCode());
    marshalStatusResponseParamsOut =
        hardwareMarshaller
            .marshalStatusResponseParams(marshalStatusResponseParamsIn);
    aorResourceStatusOut.setError(resourceStatusOut.getValue());
    aorResourceStatusOut.setSuccessful(true);
    aorResourceStatusOut.setXmlString(marshalStatusResponseParamsOut.getXml());
    return aorResourceStatusOut;
  }

  public ProgramResponse
      aorResourceProgram(ProgramRequest aorResourceProgramIn) {
    ProgramResponse aorResourceProgramOut = new ProgramResponse();
    aorResourceProgramIn.getStorageHttpTuple();
    UnmarshalProgramRequestParamsResponse unmarshalProgramRequestParamsOut =
        new UnmarshalProgramRequestParamsResponse();
    UnmarshalProgramRequestParamsRequest unmarshalProgramRequestParamsIn =
        new UnmarshalProgramRequestParamsRequest();
    ca.savi.horse.model.hardwareprocess.ProgramResourceResponse programResourceOut =
        new ca.savi.horse.model.hardwareprocess.ProgramResourceResponse();
    ca.savi.horse.model.hardwareprocess.ProgramResourceRequest programResourceIn =
        new ca.savi.horse.model.hardwareprocess.ProgramResourceRequest();
    unmarshalProgramRequestParamsIn.setXmlString(aorResourceProgramIn
        .getXmlString());
    unmarshalProgramRequestParamsOut =
        hardwareUnmarshaller
            .unmarshalProgramRequestParams(unmarshalProgramRequestParamsIn);
    programResourceIn.setBitstream(unmarshalProgramRequestParamsOut
        .getBitstream());
    programResourceIn.setIsAce(unmarshalProgramRequestParamsOut.isIsAce());
    programResourceIn.setUuid(aorResourceProgramIn.getUuid());
    programResourceOut = aorHardwareProcess.programResource(programResourceIn);
    aorResourceProgramOut.setSuccessful(programResourceOut.isSuccessful());
    aorResourceProgramOut.setError(programResourceOut.getValue());
    return aorResourceProgramOut;
  }

  public InitResponse aorResourceInit(InitRequest inputPart) {
    InitResponse aorResourceInitOut = new InitResponse();
    ca.savi.horse.model.hardwareprocess.InitResponse initOut =
        new ca.savi.horse.model.hardwareprocess.InitResponse();
    Object initIn = new Object();
    initOut = aorHardwareProcess.init(initIn);
    aorResourceInitOut.setError(initOut.getValue());
    aorResourceInitOut.setSuccessful(initOut.isSuccessful());
    return aorResourceInitOut;
  }

  public SaveImageResponse aorResourceSaveImage(SaveImageRequest inputPart) {
    // TODO implement this method
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public GetImageResponse aorResourceGetImage(GetImageRequest inputPart) {
    // TODO implement this method
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public ResetResponse aorResourceReset(ResetRequest aorResourceResetIn) {
    ResetResponse aorResourceResetOut = new ResetResponse();
    ca.savi.horse.model.hardwareprocess.ResourceResetResponse resourceResetOut =
        new ca.savi.horse.model.hardwareprocess.ResourceResetResponse();
    ca.savi.horse.model.hardwareprocess.ResourceResetRequest resourceResetIn =
        new ca.savi.horse.model.hardwareprocess.ResourceResetRequest();
    resourceResetIn.setUuid(aorResourceResetIn.getUuid());
    resourceResetOut = aorHardwareProcess.resourceReset(resourceResetIn);
    aorResourceResetOut.setError(resourceResetOut.getValue());
    aorResourceResetOut.setSuccessful(resourceResetOut.isSuccessful());
    return aorResourceResetOut;
  }

  public SetParamResponse aorResourceSetParam(SetParamRequest inputPart) {
    // TODO implement this method
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public GetParamResponse aorResourceGetParam(GetParamRequest inputPart) {
    // TODO implement this method
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public AddVlanResponse
      aorResourceAddVlan(AddVlanRequest aorResourceAddVlanIn) {
    AddVlanResponse aorResourceAddVlanOut = new AddVlanResponse();
    UnmarshalAddVlanRequestParamsResponse unmarshalAddVlanRequestParamsOut =
        new UnmarshalAddVlanRequestParamsResponse();
    UnmarshalAddVlanRequestParamsRequest unmarshalAddVlanRequestParamsIn =
        new UnmarshalAddVlanRequestParamsRequest();
    ca.savi.horse.model.hardwareprocess.AddVlanResponse addVlanOut =
        new ca.savi.horse.model.hardwareprocess.AddVlanResponse();
    ca.savi.horse.model.hardwareprocess.AddVlanRequest addVlanIn =
        new ca.savi.horse.model.hardwareprocess.AddVlanRequest();
    unmarshalAddVlanRequestParamsIn.setXmlString(aorResourceAddVlanIn
        .getXmlString());
    unmarshalAddVlanRequestParamsOut =
        hardwareUnmarshaller
            .unmarshalAddVlanRequestParams(unmarshalAddVlanRequestParamsIn);
    addVlanIn.setQinQTag(aorResourceAddVlanIn.getQinqTag());
    addVlanIn.setUuid(aorResourceAddVlanIn.getUuid());
    addVlanIn.setRawEthernet(unmarshalAddVlanRequestParamsOut.isRawEthernet());
    addVlanOut = aorHardwareProcess.addVlan(addVlanIn);
    aorResourceAddVlanOut.setError(addVlanOut.getValue());
    aorResourceAddVlanOut.setSuccessful(addVlanOut.isSuccessful());
    return aorResourceAddVlanOut;
  }

  public RemoveVlanResponse aorResourceRemoveVlan(
      RemoveVlanRequest aorResourceRemoveVlanIn) {
    RemoveVlanResponse aorResourceRemoveVlanOut = new RemoveVlanResponse();
    ca.savi.horse.model.hardwareprocess.RemoveVlanResponse removeVlanOut =
        new ca.savi.horse.model.hardwareprocess.RemoveVlanResponse();
    ca.savi.horse.model.hardwareprocess.RemoveVlanRequest removeVlanIn =
        new ca.savi.horse.model.hardwareprocess.RemoveVlanRequest();
    removeVlanIn.setUuid(aorResourceRemoveVlanIn.getUuid());
    removeVlanIn.setQinQTag(aorResourceRemoveVlanIn.getQinqTag());
    removeVlanOut = aorHardwareProcess.removeVlan(removeVlanIn);
    aorResourceRemoveVlanOut.setError(removeVlanOut.getValue());
    aorResourceRemoveVlanOut.setSuccessful(removeVlanOut.isSuccessful());
    return aorResourceRemoveVlanOut;
  }

  public RebootResponse aorResourceReboot(RebootRequest inputPart) {
    // TODO implement this method
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public TerminateResponse aorResourceTerminate(TerminateRequest inputPart) {
    TerminateResponse aorResourceTerminateOut = new TerminateResponse();
    ca.savi.horse.model.hardwareprocess.TerminateResponse terminateOut =
        new ca.savi.horse.model.hardwareprocess.TerminateResponse();
    // ca.savi.testbed.type.hardwareprocessschema.TerminateRequest terminateIn =
    // new ca.savi.testbed.type.hardwareprocessschema.TerminateRequest();
    Object terminateIn = new Object();
    terminateOut = aorHardwareProcess.terminate(terminateIn);
    aorResourceTerminateOut.setSuccessful(terminateOut.isSuccessful());
    aorResourceTerminateOut.setError(terminateOut.getValue());
    return aorResourceTerminateOut;
  }

  public ListResponse aorResourceList(ListRequest aorResourceListIn) {
    ListResponse aorResourceListOut = new ListResponse();
    UnmarshalListRequestParamsResponse unmarshalListRequestParamsOut =
        new UnmarshalListRequestParamsResponse();
    UnmarshalListRequestParamsRequest unmarshalListRequestParamsIn =
        new UnmarshalListRequestParamsRequest();
    MarshalListResponseParamsResponse marshalListResponseParamsOut =
        new MarshalListResponseParamsResponse();
    MarshalListResponseParamsRequest marshalListResponseParamsIn =
        new MarshalListResponseParamsRequest();
    ca.savi.horse.model.hardwareprocess.ResourceListResponse resourceListOut =
        new ca.savi.horse.model.hardwareprocess.ResourceListResponse();
    ca.savi.horse.model.hardwareprocess.ResourceListRequest resourceListIn =
        new ca.savi.horse.model.hardwareprocess.ResourceListRequest();
    unmarshalListRequestParamsIn.setXmlString(aorResourceListIn.getXmlString());
    unmarshalListRequestParamsOut =
        hardwareUnmarshaller
            .unmarshalListRequestParams(unmarshalListRequestParamsIn);
    resourceListIn.setVerbose(unmarshalListRequestParamsOut.isVerbose());
    resourceListOut = aorHardwareProcess.resourceList(resourceListIn);
    for (int forEach1Counter = 0; forEach1Counter < resourceListOut.getHwnode()
        .size(); forEach1Counter++) {
      marshalListResponseParamsIn.getHwnode().add(
          resourceListOut.getHwnode().get(forEach1Counter));
      // marshalListResponseParamsIn.getHwnode().get(forEach1Counter).setUuid(resourceListOut.getHwnode().get(forEach1Counter).getUuid());
      // marshalListResponseParamsIn.getHwnode().get(forEach1Counter).setTimestamp(resourceListOut.getHwnode().get(forEach1Counter).getTimestamp());
      // marshalListResponseParamsIn.getHwnode().get(forEach1Counter).setName(resourceListOut.getHwnode().get(forEach1Counter).getName());
      // marshalListResponseParamsIn.getHwnode().get(forEach1Counter).setDuration(resourceListOut.getHwnode().get(forEach1Counter).getDuration());
      // marshalListResponseParamsIn.getHwnode().get(forEach1Counter).setInUse(resourceListOut.getHwnode().get(forEach1Counter).isInUse());
      // marshalListResponseParamsIn.getHwnode().get(forEach1Counter).setIP(resourceListOut.getHwnode().get(forEach1Counter).getIP());
    }
    for (int forEach2Counter = 0; forEach2Counter < resourceListOut.getIcnode()
        .size(); forEach2Counter++) {
      marshalListResponseParamsIn.getIcnode().add(
          resourceListOut.getIcnode().get(forEach2Counter));
      // marshalListResponseParamsIn.getIcnode().get(forEach2Counter).setUuid(resourceListOut.getIcnode().get(forEach2Counter).getUuid());
      // marshalListResponseParamsIn.getIcnode().get(forEach2Counter).setName(resourceListOut.getIcnode().get(forEach2Counter).getName());
      // marshalListResponseParamsIn.getIcnode().get(forEach2Counter).setTimestamp(resourceListOut.getIcnode().get(forEach2Counter).getTimestamp());
      // marshalListResponseParamsIn.getIcnode().get(forEach2Counter).setDuration(resourceListOut.getIcnode().get(forEach2Counter).getDuration());
      // marshalListResponseParamsIn.getIcnode().get(forEach2Counter).setLinkLeft(resourceListOut.getIcnode().get(forEach2Counter).getLinkLeft());
      // marshalListResponseParamsIn.getIcnode().get(forEach2Counter).setLinkRight(resourceListOut.getIcnode().get(forEach2Counter).getLinkRight());
      // marshalListResponseParamsIn.getIcnode().get(forEach2Counter).setLRWidth(resourceListOut.getIcnode().get(forEach2Counter).getLRWidth());
      // marshalListResponseParamsIn.getIcnode().get(forEach2Counter).setRLWidth(resourceListOut.getIcnode().get(forEach2Counter).getRLWidth());
      // marshalListResponseParamsIn.getIcnode().get(forEach2Counter).setInUse(resourceListOut.getIcnode().get(forEach2Counter).isInUse());
    }
    marshalListResponseParamsIn.setVerbose(resourceListOut.isVerbose());
    marshalListResponseParamsIn.setCurrent(resourceListOut.getCurrent());
    marshalListResponseParamsIn.setMaximum(resourceListOut.getMaximum());
    marshalListResponseParamsOut =
        hardwareMarshaller
            .marshalListResponseParams(marshalListResponseParamsIn);
    aorResourceListOut.setXmlString(marshalListResponseParamsOut.getXml());
    aorResourceListOut.setSuccessful(true);
    return aorResourceListOut;
  }

  public GenericOperationResponse aorResourceGenericOperation(
      GenericOperationRequest aorResourceGenericOperationIn) {
    GenericOperationResponse aorResourceGenericOperationOut =
        new GenericOperationResponse();
    MarshalGenericOperationResponseParamsResponse marshalGenericOperationResponseParamsOut =
        new MarshalGenericOperationResponseParamsResponse();
    MarshalGenericOperationResponseParamsRequest marshalGenericOperationResponseParamsIn =
        new MarshalGenericOperationResponseParamsRequest();
    UnmarshalGenericOperationRequestParamsResponse unmarshalGenericOperationRequestParamsOut =
        new UnmarshalGenericOperationRequestParamsResponse();
    UnmarshalGenericOperationRequestParamsRequest unmarshalGenericOperationRequestParamsIn =
        new UnmarshalGenericOperationRequestParamsRequest();
    ca.savi.horse.model.hardwareprocess.UserRegisterInteractionResponse registerInteractionOut =
        new ca.savi.horse.model.hardwareprocess.UserRegisterInteractionResponse();
    ca.savi.horse.model.hardwareprocess.UserRegisterInteractionRequest registerInteractionIn =
        new ca.savi.horse.model.hardwareprocess.UserRegisterInteractionRequest();
    unmarshalGenericOperationRequestParamsIn
        .setXmlString(aorResourceGenericOperationIn.getXmlString());
    unmarshalGenericOperationRequestParamsOut =
        hardwareUnmarshaller
            .unmarshalGenericOperationRequestParams(unmarshalGenericOperationRequestParamsIn);
    registerInteractionIn
        .setSetRegValue(unmarshalGenericOperationRequestParamsOut
            .getSetRegisterValue());
    registerInteractionIn.setUuid(unmarshalGenericOperationRequestParamsOut
        .getUuid());
    registerInteractionOut =
        aorHardwareProcess.registerInteraction(registerInteractionIn);
    marshalGenericOperationResponseParamsIn
        .setReadRegisterValue(registerInteractionOut.getReadRegisterValue());
    marshalGenericOperationResponseParamsOut =
        hardwareMarshaller
            .marshalGenericOperationResponseParams(marshalGenericOperationResponseParamsIn);
    aorResourceGenericOperationOut
        .setXmlString(marshalGenericOperationResponseParamsOut.getXml());
    return aorResourceGenericOperationOut;
  }
}
