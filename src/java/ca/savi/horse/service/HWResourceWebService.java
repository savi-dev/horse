// Copyright (C) 2012, The SAVI Project.
package ca.savi.horse.service;

import javax.jws.WebService;

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
import ca.savi.camel.service.NodeResourcePortType;
import ca.savi.horse.hardware.HardwareBpel2Java;
import ca.savi.horse.model.FPGA;
import ca.savi.horse.model.hwmarshaller.
MarshalGenericOperationResponseParamsRequest;
import ca.savi.horse.model.hwmarshaller.
MarshalGenericOperationResponseParamsResponse;
import ca.savi.horse.model.hwmarshaller.MarshalGetResponseParamsRequest;
import ca.savi.horse.model.hwmarshaller.MarshalGetResponseParamsResponse;
import ca.savi.horse.model.hwmarshaller.MarshalListResponseParamsRequest;
import ca.savi.horse.model.hwmarshaller.MarshalListResponseParamsResponse;
import ca.savi.horse.model.hwmarshaller.MarshalStatusResponseParamsRequest;
import ca.savi.horse.model.hwmarshaller.MarshalStatusResponseParamsResponse;
import ca.savi.horse.model.hwunmarshaller.
UnmarshalGenericOperationRequestParamsRequest;
import ca.savi.horse.model.hwunmarshaller.
UnmarshalGenericOperationRequestParamsResponse;
import ca.savi.horse.model.hwunmarshaller.UnmarshalGetRequestParamsRequest;
import ca.savi.horse.model.hwunmarshaller.UnmarshalGetRequestParamsResponse;
import ca.savi.horse.model.hwunmarshaller.UnmarshalListRequestParamsRequest;
import ca.savi.horse.model.hwunmarshaller.UnmarshalListRequestParamsResponse;

/**
 * This is Hardware Resource Web Service.
 * @author Hesam, Rahimi Koopayi <hesam.rahimikoopayi@utoronto.ca>
 * @version 0.1
 */
@WebService(serviceName = "HardwareWebService",
    portName = "NodeResourcePortTypeBindingPort",
    endpointInterface = "ca.savi.camel.service.NodeResourcePortType",
    targetNamespace = "http://camel.savi.ca/NodeResource")
public class HWResourceWebService implements NodeResourcePortType {
  HardwareMarshaller hardwareMarshaller = null;
  HardwareUnmarshaller hardwareUnmarshaller = null;
  HardwareBpel2Java nodeHardwareProcess = null;

  public HWResourceWebService() {
    hardwareMarshaller = new HardwareMarshaller();
    // HardwareMarshallerPartnerLink
    hardwareUnmarshaller = new HardwareUnmarshaller();
    // HardwareUnmarshallerPartnerLink
    nodeHardwareProcess = new HardwareBpel2Java();
    // NodeHardwareProcessPartnerLink
    // This class is NodeResourcePartnerLink
  }

  public GetResponse nodeResourceGet(GetRequest nodeResourceGetIn) {
    GetResponse nodeResourceGetOut = new GetResponse();
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
    unmarshalGetRequestParamsIn.setXmlString(nodeResourceGetIn.getXmlString());
    unmarshalGetRequestParamsOut =
        hardwareUnmarshaller
            .unmarshalGetRequestParams(unmarshalGetRequestParamsIn);
    for (int forEach4Counter = 0; forEach4Counter < unmarshalGetRequestParamsOut
        .getFPGA().size(); forEach4Counter++) {
      FPGA fpga = unmarshalGetRequestParamsOut.getFPGA().get(forEach4Counter);
      resourceGetIn.getFPGA().add(fpga);
      /*
       * resourceGetIn.getFPGA().get(orEach4Counter).setName(
       * unmarshalGetRequestParamsOut.getFPGA().get(forEach4Counter).getName());
       * **************************************************** THE "setLink"
       * METHOD IS NOT AVAILABLE IN FOLLOWING LINE IN FPGA CLASS
       * *************************
       * resourceGetIn.getFPGA().get(forEach4Counter).setLink
       * (unmarshalGetRequestParamsOut
       * .getFPGA().get(forEach4Counter).getLink());
       * resourceGetIn.getFPGA().get(
       * forEach4Counter).getLink().add(forEach4Counter,
       * unmarshalGetRequestParamsOut
       * .getFPGA().get(forEach4Counter).getLink().get(forEach4Counter)); is the
       * above line correct????????????????????????????????
       */
    }
    // Assign 17
    resourceGetIn.setUuid(unmarshalGetRequestParamsOut.getUuid());
    resourceGetIn.setLRWidth(unmarshalGetRequestParamsOut.getLRWidth());
    resourceGetIn.setRLWidth(unmarshalGetRequestParamsOut.getRLWidth());
    resourceGetIn.setRawEthernet(unmarshalGetRequestParamsOut.isRawEthernet());
    resourceGetIn.setQinQTag(nodeResourceGetIn.getQinqTag());
    resourceGetIn.setDuration(nodeResourceGetIn.getDuration());
    resourceGetOut = nodeHardwareProcess.resourceGet(resourceGetIn);
    for (int forEach3Counter = 0; forEach3Counter < resourceGetOut.getFPGA()
        .size(); forEach3Counter++) {
      FPGA fpga = resourceGetOut.getFPGA().get(forEach3Counter);
      marshalGetResponseParamsIn.getFPGA().add(fpga);
    }
    nodeResourceGetOut.setError(resourceGetOut.getValue());
    nodeResourceGetOut.setSuccessful(resourceGetOut.isSuccessful());
    nodeResourceGetOut.getUuidList().add(resourceGetOut.getUuid());
    return nodeResourceGetOut;
  }

  public ReleaseResponse nodeResourceRelease(
      ReleaseRequest nodeResourceReleaseIn) {
    ReleaseResponse nodeResourceReleaseOut = new ReleaseResponse();
    ca.savi.horse.model.hardwareprocess.ResourceReleaseResponse
    resourceReleaseOut =
        new ca.savi.horse.model.hardwareprocess.ResourceReleaseResponse();
    ca.savi.horse.model.hardwareprocess.ResourceReleaseRequest
    resourceReleaseIn =
        new ca.savi.horse.model.hardwareprocess.ResourceReleaseRequest();
    resourceReleaseIn.setUuid(nodeResourceReleaseIn.getUuid());
    resourceReleaseOut = nodeHardwareProcess.resourceRelease(resourceReleaseIn);
    nodeResourceReleaseOut.setSuccessful(resourceReleaseOut.isSuccessful());
    nodeResourceReleaseOut.setError(resourceReleaseOut.getValue());
    return nodeResourceReleaseOut;
  }

  public StatusResponse nodeResourceStatus(StatusRequest nodeResourceStatusIn) {
    StatusResponse nodeResourceStatusOut = new StatusResponse();
    MarshalStatusResponseParamsResponse marshalStatusResponseParamsOut =
        new MarshalStatusResponseParamsResponse();
    MarshalStatusResponseParamsRequest marshalStatusResponseParamsIn =
        new MarshalStatusResponseParamsRequest();
    ca.savi.horse.model.hardwareprocess.ResourceStatusResponse
    resourceStatusOut =
        new ca.savi.horse.model.hardwareprocess.ResourceStatusResponse();
    ca.savi.horse.model.hardwareprocess.ResourceStatusRequest resourceStatusIn =
        new ca.savi.horse.model.hardwareprocess.ResourceStatusRequest();
    resourceStatusIn.setUuid(nodeResourceStatusIn.getUuid());
    resourceStatusOut = nodeHardwareProcess.resourceStatus(resourceStatusIn);
    marshalStatusResponseParamsIn.setCode(resourceStatusOut.getCode());
    marshalStatusResponseParamsOut =
        hardwareMarshaller
            .marshalStatusResponseParams(marshalStatusResponseParamsIn);
    nodeResourceStatusOut.setError(resourceStatusOut.getValue());
    nodeResourceStatusOut.setSuccessful(true);
    nodeResourceStatusOut.setXmlString(marshalStatusResponseParamsOut.getXml());
    return nodeResourceStatusOut;
  }

  public ProgramResponse nodeResourceProgram(
      ProgramRequest nodeResourceProgramIn) {
    ProgramResponse nodeResourceProgramOut = new ProgramResponse();
    ca.savi.horse.model.hardwareprocess.ProgramResourceResponse
    programResourceOut =
        new ca.savi.horse.model.hardwareprocess.ProgramResourceResponse();
    ca.savi.horse.model.hardwareprocess.ProgramResourceRequest
    programResourceIn =
        new ca.savi.horse.model.hardwareprocess.ProgramResourceRequest();
    programResourceIn.setImageUuid(nodeResourceProgramIn.getImageUuid());
    programResourceIn.setUuid(nodeResourceProgramIn.getUuid());
    programResourceIn.setServiceEndPoint(nodeResourceProgramIn
        .getServiceEndpoint());
    programResourceOut = nodeHardwareProcess.programResource(programResourceIn);
    nodeResourceProgramOut.setSuccessful(programResourceOut.isSuccessful());
    nodeResourceProgramOut.setError(programResourceOut.getValue());
    return nodeResourceProgramOut;
  }

  public InitResponse nodeResourceInit(InitRequest inputPart) {
    InitResponse nodeResourceInitOut = new InitResponse();
    ca.savi.horse.model.hardwareprocess.InitResponse initOut =
        new ca.savi.horse.model.hardwareprocess.InitResponse();
    Object initIn = new Object();
    initOut = nodeHardwareProcess.init(initIn);
    nodeResourceInitOut.setError(initOut.getValue());
    nodeResourceInitOut.setSuccessful(initOut.isSuccessful());
    return nodeResourceInitOut;
  }

  public SaveImageResponse nodeResourceSaveImage(SaveImageRequest inputPart) {
    // TODO implement this method
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public GetImageResponse nodeResourceGetImage(GetImageRequest inputPart) {
    // TODO implement this method
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public ResetResponse nodeResourceReset(ResetRequest nodeResourceResetIn) {
    ResetResponse nodeResourceResetOut = new ResetResponse();
    ca.savi.horse.model.hardwareprocess.ResourceResetResponse resourceResetOut =
        new ca.savi.horse.model.hardwareprocess.ResourceResetResponse();
    ca.savi.horse.model.hardwareprocess.ResourceResetRequest resourceResetIn =
        new ca.savi.horse.model.hardwareprocess.ResourceResetRequest();
    resourceResetIn.setUuid(nodeResourceResetIn.getUuid());
    resourceResetOut = nodeHardwareProcess.resourceReset(resourceResetIn);
    nodeResourceResetOut.setError(resourceResetOut.getValue());
    nodeResourceResetOut.setSuccessful(resourceResetOut.isSuccessful());
    return nodeResourceResetOut;
  }

  public SetParamResponse nodeResourceSetParam(SetParamRequest inputPart) {
    // TODO implement this method
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public GetParamResponse nodeResourceGetParam(GetParamRequest inputPart) {
    // TODO implement this method
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public RebootResponse nodeResourceReboot(RebootRequest inputPart) {
    // TODO implement this method
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public TerminateResponse nodeResourceTerminate(TerminateRequest inputPart) {
    TerminateResponse nodeResourceTerminateOut = new TerminateResponse();
    ca.savi.horse.model.hardwareprocess.TerminateResponse terminateOut =
        new ca.savi.horse.model.hardwareprocess.TerminateResponse();
    // ca.savi.testbed.type.hardwareprocessschema.TerminateRequest terminateIn =
    // new ca.savi.testbed.type.hardwareprocessschema.TerminateRequest();
    Object terminateIn = new Object();
    terminateOut = nodeHardwareProcess.terminate(terminateIn);
    nodeResourceTerminateOut.setSuccessful(terminateOut.isSuccessful());
    nodeResourceTerminateOut.setError(terminateOut.getValue());
    return nodeResourceTerminateOut;
  }

  public ListResponse nodeResourceList(ListRequest nodeResourceListIn) {
    ListResponse nodeResourceListOut = new ListResponse();
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
    unmarshalListRequestParamsIn
        .setXmlString(nodeResourceListIn.getXmlString());
    unmarshalListRequestParamsOut =
        hardwareUnmarshaller
            .unmarshalListRequestParams(unmarshalListRequestParamsIn);
    resourceListIn.setVerbose(unmarshalListRequestParamsOut.isVerbose());
    resourceListOut = nodeHardwareProcess.resourceList(resourceListIn);
    for (int forEach1Counter = 0; forEach1Counter < resourceListOut.getHwnode()
        .size(); forEach1Counter++) {
      marshalListResponseParamsIn.getHwnode().add(
          resourceListOut.getHwnode().get(forEach1Counter));
      /*
       * marshalListResponseParamsIn.getHwnode().get(forEach1Counter).setUuid(
       * resourceListOut.getHwnode().get(forEach1Counter).getUuid());
       * marshalListResponseParamsIn
       * .getHwnode().get(forEach1Counter).setTimestamp
       * (resourceListOut.getHwnode().get(forEach1Counter).getTimestamp());
       * marshalListResponseParamsIn
       * .getHwnode().get(forEach1Counter).setName(resourceListOut
       * .getHwnode().get(forEach1Counter).getName());
       * marshalListResponseParamsIn
       * .getHwnode().get(forEach1Counter).setDuration
       * (resourceListOut.getHwnode().get(forEach1Counter).getDuration());
       * marshalListResponseParamsIn
       * .getHwnode().get(forEach1Counter).setInUse(resourceListOut
       * .getHwnode().get(forEach1Counter).isInUse());
       * marshalListResponseParamsIn
       * .getHwnode().get(forEach1Counter).setIP(resourceListOut
       * .getHwnode().get(forEach1Counter).getIP());
       */
    }
    for (int forEach2Counter = 0; forEach2Counter < resourceListOut.getIcnode()
        .size(); forEach2Counter++) {
      marshalListResponseParamsIn.getIcnode().add(
          resourceListOut.getIcnode().get(forEach2Counter));
      /*
       * marshalListResponseParamsIn.getIcnode().get(forEach2Counter).setUuid(
       * resourceListOut.getIcnode().get(forEach2Counter).getUuid());
       * marshalListResponseParamsIn
       * .getIcnode().get(forEach2Counter).setName(resourceListOut
       * .getIcnode().get(forEach2Counter).getName());
       * marshalListResponseParamsIn
       * .getIcnode().get(forEach2Counter).setTimestamp
       * (resourceListOut.getIcnode().get(forEach2Counter).getTimestamp());
       * marshalListResponseParamsIn
       * .getIcnode().get(forEach2Counter).setDuration
       * (resourceListOut.getIcnode().get(forEach2Counter).getDuration());
       * marshalListResponseParamsIn
       * .getIcnode().get(forEach2Counter).setLinkLeft
       * (resourceListOut.getIcnode().get(forEach2Counter).getLinkLeft());
       * marshalListResponseParamsIn
       * .getIcnode().get(forEach2Counter).setLinkRight
       * (resourceListOut.getIcnode().get(forEach2Counter).getLinkRight());
       * marshalListResponseParamsIn
       * .getIcnode().get(forEach2Counter).setLRWidth(
       * resourceListOut.getIcnode().get(forEach2Counter).getLRWidth());
       * marshalListResponseParamsIn
       * .getIcnode().get(forEach2Counter).setRLWidth(
       * resourceListOut.getIcnode().get(forEach2Counter).getRLWidth());
       * marshalListResponseParamsIn
       * .getIcnode().get(forEach2Counter).setInUse(resourceListOut
       * .getIcnode().get(forEach2Counter).isInUse());
       */
    }
    marshalListResponseParamsIn.setVerbose(resourceListOut.isVerbose());
    marshalListResponseParamsIn.setCurrent(resourceListOut.getCurrent());
    marshalListResponseParamsIn.setMaximum(resourceListOut.getMaximum());
    marshalListResponseParamsOut =
        hardwareMarshaller
            .marshalListResponseParams(marshalListResponseParamsIn);
    nodeResourceListOut.setXmlString(marshalListResponseParamsOut.getXml());
    nodeResourceListOut.setSuccessful(true);
    return nodeResourceListOut;
  }

  public GenericOperationResponse nodeResourceGenericOperation(
      GenericOperationRequest nodeResourceGenericOperationIn) {
    GenericOperationResponse nodeResourceGenericOperationOut =
        new GenericOperationResponse();
    MarshalGenericOperationResponseParamsResponse
    marshalGenericOperationResponseParamsOut =
        new MarshalGenericOperationResponseParamsResponse();
    MarshalGenericOperationResponseParamsRequest
    marshalGenericOperationResponseParamsIn =
        new MarshalGenericOperationResponseParamsRequest();
    UnmarshalGenericOperationRequestParamsResponse
    unmarshalGenericOperationRequestParamsOut =
        new UnmarshalGenericOperationRequestParamsResponse();
    UnmarshalGenericOperationRequestParamsRequest
    unmarshalGenericOperationRequestParamsIn =
        new UnmarshalGenericOperationRequestParamsRequest();
    ca.savi.horse.model.hardwareprocess.UserRegisterInteractionResponse
    registerInteractionOut =
        new ca.savi.horse.model.hardwareprocess.
        UserRegisterInteractionResponse();
    ca.savi.horse.model.hardwareprocess.UserRegisterInteractionRequest
    registerInteractionIn =
        new ca.savi.horse.model.hardwareprocess.
        UserRegisterInteractionRequest();
    unmarshalGenericOperationRequestParamsIn
        .setXmlString(nodeResourceGenericOperationIn.getXmlString());
    unmarshalGenericOperationRequestParamsOut =
        hardwareUnmarshaller
            .unmarshalGenericOperationRequestParams(
                unmarshalGenericOperationRequestParamsIn);
    registerInteractionIn
        .setSetRegValue(unmarshalGenericOperationRequestParamsOut
            .getSetRegisterValue());
    registerInteractionIn.setUuid(unmarshalGenericOperationRequestParamsOut
        .getUuid());
    registerInteractionOut =
        nodeHardwareProcess.registerInteraction(registerInteractionIn);
    marshalGenericOperationResponseParamsIn
        .setReadRegisterValue(registerInteractionOut.getReadRegisterValue());
    marshalGenericOperationResponseParamsOut =
        hardwareMarshaller
            .marshalGenericOperationResponseParams(
                marshalGenericOperationResponseParamsIn);
    nodeResourceGenericOperationOut
        .setXmlString(marshalGenericOperationResponseParamsOut.getXml());
    return nodeResourceGenericOperationOut;
  }
}
