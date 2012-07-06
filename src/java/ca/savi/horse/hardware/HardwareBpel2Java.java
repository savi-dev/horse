// Copyright (C) 2012, The SAVI Project.
package ca.savi.horse.hardware;

import ca.savi.cobra.model.AddVlanToPortRequest;
import ca.savi.cobra.model.AddVlanToPortResponse;
import ca.savi.cobra.model.ClearLocalIfEmptyRequest;
import ca.savi.cobra.model.ClearLocalIfEmptyResponse;
import ca.savi.cobra.model.ClearUUIDRequest;
import ca.savi.cobra.model.ClearUUIDResponse;
import ca.savi.cobra.model.CreateLocalTagRequest;
import ca.savi.cobra.model.CreateLocalTagResponse;
import ca.savi.cobra.model.GetIPAddressRequest;
import ca.savi.cobra.model.GetIPAddressResponse;
import ca.savi.cobra.model.GetNetworkProtocolRequest;
import ca.savi.cobra.model.GetNetworkProtocolResponse;
import ca.savi.cobra.model.ReleaseIPAddressRequest;
import ca.savi.cobra.model.ReleaseIPAddressResponse;
import ca.savi.cobra.model.RemoveVlanFromPortRequest;
import ca.savi.cobra.model.RemoveVlanFromPortResponse;
import ca.savi.horse.model.hardwareprocess.AddVlanRequest;
import ca.savi.horse.model.hardwareprocess.AddVlanResponse;
import ca.savi.horse.model.hardwareprocess.InitResponse;
import ca.savi.horse.model.hardwareprocess.ProgramResourceRequest;
import ca.savi.horse.model.hardwareprocess.ProgramResourceResponse;
import ca.savi.horse.model.hardwareprocess.RemoveVlanRequest;
import ca.savi.horse.model.hardwareprocess.RemoveVlanResponse;
import ca.savi.horse.model.hardwareprocess.ResourceGetRequest;
import ca.savi.horse.model.hardwareprocess.ResourceGetResponse;
import ca.savi.horse.model.hardwareprocess.ResourceListRequest;
import ca.savi.horse.model.hardwareprocess.ResourceListResponse;
import ca.savi.horse.model.hardwareprocess.ResourceReleaseRequest;
import ca.savi.horse.model.hardwareprocess.ResourceReleaseResponse;
import ca.savi.horse.model.hardwareprocess.ResourceResetRequest;
import ca.savi.horse.model.hardwareprocess.ResourceResetResponse;
import ca.savi.horse.model.hardwareprocess.ResourceStatusRequest;
import ca.savi.horse.model.hardwareprocess.ResourceStatusResponse;
import ca.savi.horse.model.hardwareprocess.StopServerResponse;
import ca.savi.horse.model.hardwareprocess.UserRegisterInteractionRequest;
import ca.savi.horse.model.hardwareprocess.UserRegisterInteractionResponse;

/**
 * This is Hardware Bpel converted to Java.
 * @author Hesam, Rahimi Koopayi <hesam.rahimikoopayi@utoronto.ca>
 * @version 0.1
 */
public class HardwareBpel2Java {
  // PartnerLink1 is this class itself
  AORHardware aorHardware;// = null; //PartnerLink2
  // AORFabricService //PartnerLink3
  // variables: look at the hardwareBPEL.bpel, those variables that are in
  // fabric.WSDL.AOR and those that are with "internal..." prefix, should be
  // defined here. The rest should be defined as methods (functions) in this
  // class. There are 12 (also there are 12 <onMessage>s in the
  // hardwareBPEL.bpel)
  // variables
  RemoveVlanFromPortResponse removeVlanFromPortOut =
      new RemoveVlanFromPortResponse();
  RemoveVlanFromPortRequest removeVlanFromPortIn =
      new RemoveVlanFromPortRequest();
  ClearUUIDResponse clearUUIDOut = new ClearUUIDResponse();
  ClearUUIDRequest clearUUIDIn = new ClearUUIDRequest();
  AddVlanToPortResponse addVlanToPortOut = new AddVlanToPortResponse();
  AddVlanToPortRequest addVlanToPortIn = new AddVlanToPortRequest();
  ReleaseIPAddressResponse releaseIPAddressOut = new ReleaseIPAddressResponse();
  ReleaseIPAddressRequest releaseIPAddressIn = new ReleaseIPAddressRequest();
  GetIPAddressResponse getIPAddressOut = new GetIPAddressResponse();
  GetIPAddressRequest getIPAddressIn = new GetIPAddressRequest();
  ClearLocalIfEmptyResponse clearLocalIfEmptyOut =
      new ClearLocalIfEmptyResponse();
  ClearLocalIfEmptyRequest clearLocalIfEmptyIn = new ClearLocalIfEmptyRequest();
  GetNetworkProtocolResponse getNetworkProtocolOut =
      new GetNetworkProtocolResponse();
  GetNetworkProtocolRequest getNetworkProtocolIn =
      new GetNetworkProtocolRequest();
  CreateLocalTagResponse createLocalTagOut = new CreateLocalTagResponse();
  CreateLocalTagRequest createLocalTagIn = new CreateLocalTagRequest();
  // RemoveVlanResponse removeVlanOut = null;
  // RemoveVlanRequest removeVlanIn = null;
  ca.savi.horse.model.hardware.RemoveVlanResponse internalRemoveVlanOut =
      new ca.savi.horse.model.hardware.RemoveVlanResponse();
  ca.savi.horse.model.hardware.RemoveVlanRequest internalRemoveVlanIn =
      new ca.savi.horse.model.hardware.RemoveVlanRequest();
  // AddVlanResponse addVlanOut = null;
  // AddVlanRequest addVlanIn = null;
  ca.savi.horse.model.hardware.AddVlanResponse internalAddVlanOut =
      new ca.savi.horse.model.hardware.AddVlanResponse();
  ca.savi.horse.model.hardware.AddVlanRequest internalAddVlanIn =
      new ca.savi.horse.model.hardware.AddVlanRequest();
  // UserRegisterInteractionResponse registerInteractionOut = null;
  // UserRegisterInteractionRequest registerInteractionIn = null;
  ca.savi.horse.model.hardware.UserRegisterInteractionResponse
  internalRegisterInteractionOut =
      new ca.savi.horse.model.hardware.UserRegisterInteractionResponse();
  ca.savi.horse.model.hardware.UserRegisterInteractionRequest
  internalRegisterInteractionIn =
      new ca.savi.horse.model.hardware.UserRegisterInteractionRequest();
  // ResourceResetResponse resourceResetOut = null;
  // ResourceResetRequest resourceResetIn = null;
  ca.savi.horse.model.hardware.ResourceResetResponse internalResourceResetOut =
      new ca.savi.horse.model.hardware.ResourceResetResponse();
  ca.savi.horse.model.hardware.ResourceResetRequest internalResourceResetIn =
      new ca.savi.horse.model.hardware.ResourceResetRequest();
  // StopServerResponse stopServerOut = null;
  // Object stopServerIn = null; //because in AORHardwareProcess.xsd , the type
  // of StopServerRequest is "anyType". This is the case for InitRequest and
  // TerminateRequest
  // TerminateResponse terminateOut = null;
  // Object terminateIn = null;
  ca.savi.horse.model.hardware.TerminateResponse internalTerminateOut =
      new ca.savi.horse.model.hardware.TerminateResponse();
  Object internalTerminateIn = new Object();
  ca.savi.horse.model.hardware.ResourceStatusResponse
  internalResourceStatusOut =
      new ca.savi.horse.model.hardware.ResourceStatusResponse();
  ca.savi.horse.model.hardware.ResourceStatusRequest internalResourceStatusIn =
      new ca.savi.horse.model.hardware.ResourceStatusRequest();
  ca.savi.horse.model.hardware.ProgramResourceResponse
  internalProgramResourceOut =
      new ca.savi.horse.model.hardware.ProgramResourceResponse();
  ca.savi.horse.model.hardware.ProgramResourceRequest
  internalProgramResourceIn =
      new ca.savi.horse.model.hardware.ProgramResourceRequest();
  ca.savi.horse.model.hardware.ResourceReleaseResponse
  internalResourceReleaseOut =
      new ca.savi.horse.model.hardware.ResourceReleaseResponse();
  ca.savi.horse.model.hardware.ResourceReleaseRequest
  internalResourceReleaseIn =
      new ca.savi.horse.model.hardware.ResourceReleaseRequest();
  ca.savi.horse.model.hardware.ResourceGetResponse
  internalResourceGetOut =
      new ca.savi.horse.model.hardware.ResourceGetResponse();
  ca.savi.horse.model.hardware.ResourceGetRequest internalResourceGetIn =
      new ca.savi.horse.model.hardware.ResourceGetRequest();
  ca.savi.horse.model.hardware.ResourceListResponse
  internalResourceListOut =
      new ca.savi.horse.model.hardware.ResourceListResponse();
  ca.savi.horse.model.hardware.ResourceListRequest internalResourceListIn =
      new ca.savi.horse.model.hardware.ResourceListRequest();
  ca.savi.horse.model.hardware.InitResponse internalInitOut =
      new ca.savi.horse.model.hardware.InitResponse();
  Object internalInitIn = new Object();

  public HardwareBpel2Java() {
    aorHardware = new AORHardware();
  }

  public InitResponse init(Object initIn) {
    InitResponse initOut = new InitResponse();
    internalInitIn = initIn;
    internalInitOut = aorHardware.init(internalInitIn);
    initOut.setValue(internalInitOut.getValue());
    initOut.setSuccessful(internalInitOut.isSuccessful());
    return initOut;
  }

  public ResourceListResponse resourceList(ResourceListRequest resourceListIn) {
    ResourceListResponse resourceListOut = new ResourceListResponse();
    internalResourceListIn.setVerbose(resourceListIn.isVerbose());
    internalResourceListOut = aorHardware.resourceList(internalResourceListIn);
    resourceListOut.setVerbose(internalResourceListOut.isVerbose());
    resourceListOut.setCurrent(internalResourceListOut.getNumberUsed());
    resourceListOut.setMaximum(internalResourceListOut.getTotalResources());
    resourceListOut.getHwnode().addAll(internalResourceListOut.getHwnode());
    // resourceListOut.getHwnode()
    // ////////////////// THIS LINE SHOULD BE FIXED, (RIGHT BEFORE THE RETURN
    // /////////////////////////// internalResourceListOut.getHwnode();
    return resourceListOut;
  }

  public ResourceGetResponse resourceGet(ResourceGetRequest resourceGetIn) {
    ResourceGetResponse resourceGetOut = new ResourceGetResponse();
    createLocalTagIn.setIsRawEthernet(resourceGetIn.isRawEthernet());
    createLocalTagIn.setQinQTag(resourceGetIn.getQinQTag());
    createLocalTagOut = createLocalTag(createLocalTagIn);
    // createLocalTagOut = aorFabric.createLocalTag(createLocalTagIn);
    if (createLocalTagOut.isSuccessful()) {
    } else {
      resourceGetOut.setValue(createLocalTagOut.getValue());
      resourceGetOut.setSuccessful(createLocalTagOut.isSuccessful());
      return resourceGetOut;
    }
    // Assign27
    getNetworkProtocolIn.setQinQTag(resourceGetIn.getQinQTag());
    // Invoke14
    getNetworkProtocolOut = getNetworkProtocol(getNetworkProtocolIn);
    // getNetworkProtocolOut =
    // aorFabric.getNetworkProtocol(getNetworkProtocolIn);
    if (getNetworkProtocolOut.isSuccessful()) {
    } else {
      resourceGetOut.setValue(getNetworkProtocolOut.getValue());
      resourceGetOut.setSuccessful(getNetworkProtocolOut.isSuccessful());
      clearLocalIfEmptyIn.setQinQTag(resourceGetIn.getQinQTag());
      clearLocalIfEmptyOut = clearLocalIfEmpty(clearLocalIfEmptyIn);
      // clearLocalIfEmptyOut =
      // aorFabric.clearLocalIfEmpty(clearLocalIfEmptyIn);
      return resourceGetOut;
    }
    if (getNetworkProtocolOut.getProtocol().equals("Ethernet")
        && resourceGetIn.isRawEthernet() || !resourceGetIn.isRawEthernet()
        && getNetworkProtocolOut.getProtocol().equals("IP")) {
    } else {
      resourceGetOut.setSuccessful(Boolean.FALSE);
      resourceGetOut
          .setValue("Specified incorrect network protocol for this Q in Q tag");
      clearLocalIfEmptyIn.setQinQTag(resourceGetIn.getQinQTag());
      clearLocalIfEmptyOut = clearLocalIfEmpty(clearLocalIfEmptyIn);
      // clearLocalIfEmptyOut =
      // aorFabric.clearLocalIfEmpty(clearLocalIfEmptyIn);
      return resourceGetOut;
    }
    internalResourceGetIn.setUuid(resourceGetIn.getUuid());
    internalResourceGetIn.setDuration(resourceGetIn.getDuration());
    internalResourceGetOut = aorHardware.resourceGet(internalResourceGetIn);
    // If4
    if (internalResourceGetOut.isSuccessful()) {
    } else {
      resourceGetOut.setUuid(internalResourceGetOut.getUuid());
      resourceGetOut.setSuccessful(internalResourceGetOut.isSuccessful());
      resourceGetOut.setValue(internalResourceGetOut.getValue());
      clearLocalIfEmptyIn.setQinQTag(resourceGetIn.getQinQTag());
      clearLocalIfEmptyOut = clearLocalIfEmpty(clearLocalIfEmptyIn);
      // clearLocalIfEmptyOut =
      // aorFabric.clearLocalIfEmpty(clearLocalIfEmptyIn);
      return resourceGetOut;
    }
    // Assign31
    getIPAddressIn.setUuid(resourceGetIn.getUuid());
    getIPAddressIn.setQInQTag(resourceGetIn.getQinQTag());
    getIPAddressOut = getIPAddress(getIPAddressIn);
    // getIPAddressOut = aorFabric.getIPAddress(getIPAddressIn);
    // If6
    if (getIPAddressOut.isSuccessful()) {
    } else {
      clearLocalIfEmptyIn.setQinQTag(resourceGetIn.getQinQTag());
      internalResourceReleaseIn.setUuid(resourceGetIn.getUuid());
      resourceGetOut.setValue(getIPAddressOut.getValue());
      resourceGetOut.setSuccessful(getIPAddressOut.isSuccessful());
      clearLocalIfEmptyOut = clearLocalIfEmpty(clearLocalIfEmptyIn);
      // clearLocalIfEmptyOut =
      // aorFabric.clearLocalIfEmpty(clearLocalIfEmptyIn);
      internalResourceReleaseOut =
          aorHardware.resourceRelease(internalResourceReleaseIn);
      return resourceGetOut;
    }
    // Assign33
    internalAddVlanIn.setIP(getIPAddressOut.getIP());
    internalAddVlanIn.setVlan(createLocalTagOut.getLocalVlanId());
    internalAddVlanIn.setUuid(resourceGetIn.getUuid());
    internalAddVlanOut = aorHardware.addVlan(internalAddVlanIn);
    // If7
    if (internalAddVlanOut.isSuccessful()) {
    } else {
      releaseIPAddressIn.setIP(getIPAddressOut.getIP());
      clearLocalIfEmptyIn.setQinQTag(resourceGetIn.getQinQTag());
      internalResourceReleaseIn.setUuid(resourceGetIn.getUuid());
      resourceGetOut.setValue(internalAddVlanOut.getValue());
      resourceGetOut.setSuccessful(internalAddVlanOut.isSuccessful());
      releaseIPAddressOut = releaseIPAddress(releaseIPAddressIn);
      // releaseIPAddressOut = aorFabric.releaseIPAddress(releaseIPAddressIn);
      internalResourceReleaseOut =
          aorHardware.resourceRelease(internalResourceReleaseIn);
      clearLocalIfEmptyOut = clearLocalIfEmpty(clearLocalIfEmptyIn);
      // clearLocalIfEmptyOut =
      // aorFabric.clearLocalIfEmpty(clearLocalIfEmptyIn);
      return resourceGetOut;
    }
    // Assign35
    addVlanToPortIn.setVlan(createLocalTagOut.getLocalVlanId());
    addVlanToPortIn.setUUID(resourceGetIn.getUuid());
    addVlanToPortOut = addVlanToPort(addVlanToPortIn);
    // addVlanToPortOut = aorFabric.addVlanToPort(addVlanToPortIn);
    // If8
    if (addVlanToPortOut.isSuccessful()) {
    } else {
      releaseIPAddressIn.setIP(getIPAddressOut.getIP());
      clearLocalIfEmptyIn.setQinQTag(resourceGetIn.getQinQTag());
      internalResourceReleaseIn.setUuid(resourceGetIn.getUuid());
      resourceGetOut.setValue(addVlanToPortOut.getValue());
      resourceGetOut.setSuccessful(addVlanToPortOut.isSuccessful());
      releaseIPAddressOut = releaseIPAddress(releaseIPAddressIn);
      // releaseIPAddressOut = aorFabric.releaseIPAddress(releaseIPAddressIn);
      internalResourceReleaseOut =
          aorHardware.resourceRelease(internalResourceReleaseIn);
      clearLocalIfEmptyOut = clearLocalIfEmpty(clearLocalIfEmptyIn);
      // clearLocalIfEmptyOut =
      // aorFabric.clearLocalIfEmpty(clearLocalIfEmptyIn);
      return resourceGetOut;
    }
    // Assign6
    resourceGetOut.setUuid(internalResourceGetOut.getUuid());
    resourceGetOut.setSuccessful(internalResourceGetOut.isSuccessful());
    resourceGetOut.setValue(internalResourceGetOut.getValue());
    return resourceGetOut;
  }

  public ResourceReleaseResponse resourceRelease(
      ResourceReleaseRequest resourceReleaseIn) {
    ResourceReleaseResponse resourceReleaseOut = new ResourceReleaseResponse();
    // Assign7
    internalResourceReleaseIn.setUuid(resourceReleaseIn.getUuid());
    clearUUIDIn.setUuid(resourceReleaseIn.getUuid());
    clearUUIDOut = clearUUID(clearUUIDIn);
    // clearUUIDOut = aorFabric.clearUUID(clearUUIDIn);
    internalResourceReleaseOut =
        aorHardware.resourceRelease(internalResourceReleaseIn);
    // Assign8
    resourceReleaseOut.setValue(internalResourceReleaseOut.getValue());
    resourceReleaseOut.setSuccessful(internalResourceReleaseOut.isSuccessful());
    return resourceReleaseOut;
  }

  public ProgramResourceResponse programResource(
      ProgramResourceRequest programResourceIn) {
    ProgramResourceResponse programResourceOut = new ProgramResourceResponse();
    // Assign9
    internalProgramResourceIn.setImageUuid(programResourceIn.getImageUuid());
    internalProgramResourceIn.setUuid(programResourceIn.getUuid());
    internalProgramResourceIn.setServiceEndPoint(programResourceIn
        .getServiceEndPoint());
    internalProgramResourceOut =
        aorHardware.programResource(internalProgramResourceIn);
    // Assign10
    programResourceOut.setValue(internalProgramResourceOut.getValue());
    programResourceOut.setSuccessful(internalProgramResourceOut.isSuccessful());
    return programResourceOut;
  }

  public ResourceStatusResponse resourceStatus(
      ResourceStatusRequest resourceStatusIn) {
    ResourceStatusResponse resourceStatusOut = new ResourceStatusResponse();
    // Assign11
    internalResourceStatusIn.setUuid(resourceStatusIn.getUuid());
    internalResourceStatusOut =
        aorHardware.resourceStatus(internalResourceStatusIn);
    // Assign12
    resourceStatusOut.setValue(internalResourceStatusOut.getValue());
    resourceStatusOut.setCode(internalResourceStatusOut.getCode());
    return resourceStatusOut;
  }

  public ca.savi.horse.model.hardwareprocess.TerminateResponse terminate(
      Object terminateIn) {
    ca.savi.horse.model.hardwareprocess.TerminateResponse terminateOut =
        new ca.savi.horse.model.hardwareprocess.TerminateResponse();
    // Assign13
    internalTerminateIn = terminateIn;
    internalTerminateOut = aorHardware.terminate(internalTerminateIn);
    terminateOut.setValue(internalTerminateOut.getValue());
    terminateOut.setSuccessful(internalTerminateOut.isSuccessful());
    return terminateOut;
  }

  public StopServerResponse stopServer(Object stopServerIn) {
    StopServerResponse stopServerOut = new StopServerResponse();
    // Assign51
    stopServerOut.setSuccessful(Boolean.FALSE);
    stopServerOut.setValue("Unsupported operation");
    return stopServerOut;
  }

  public ResourceResetResponse resourceReset(
      ResourceResetRequest resourceResetIn) {
    ResourceResetResponse resourceResetOut = new ResourceResetResponse();
    // Assign17
    internalResourceResetIn.setUuid(resourceResetIn.getUuid());
    internalResourceResetOut =
        aorHardware.resourceReset(internalResourceResetIn);
    // Assign18
    resourceResetOut.setValue(internalResourceResetOut.getValue());
    resourceResetOut.setSuccessful(internalResourceResetOut.isSuccessful());
    return resourceResetOut;
  }

  public UserRegisterInteractionResponse registerInteraction(
      UserRegisterInteractionRequest registerInteractionIn) {
    UserRegisterInteractionResponse registerInteractionOut =
        new UserRegisterInteractionResponse();
    // Assign19
    internalRegisterInteractionIn.setSetRegValue(registerInteractionIn
        .getSetRegValue());
    internalRegisterInteractionIn.setUuid(registerInteractionIn.getUuid());
    internalRegisterInteractionOut =
        aorHardware.registerInteraction(internalRegisterInteractionIn);
    // Assign20
    registerInteractionOut.setReadRegisterValue(internalRegisterInteractionOut
        .getReadRegisterValue());
    registerInteractionOut.setValue(internalRegisterInteractionOut.getValue());
    registerInteractionOut.setSuccessful(internalRegisterInteractionOut
        .isSuccessful());
    return registerInteractionOut;
  }

  public AddVlanResponse addVlan(AddVlanRequest addVlanIn) {
    AddVlanResponse addVlanOut = new AddVlanResponse();
    // Assign37
    createLocalTagIn.setQinQTag(addVlanIn.getQinQTag());
    createLocalTagIn.setIsRawEthernet(addVlanIn.isRawEthernet());
    createLocalTagOut = createLocalTag(createLocalTagIn);
    // createLocalTagOut = aorFabric.createLocalTag(createLocalTagIn);
    // If9
    if (createLocalTagOut.isSuccessful()) {
    } else {
      addVlanOut.setSuccessful(createLocalTagOut.isSuccessful());
      addVlanOut.setValue(createLocalTagOut.getValue());
      return addVlanOut;
    }
    // Assign39
    getNetworkProtocolIn.setQinQTag(addVlanIn.getQinQTag());
    getNetworkProtocolOut = getNetworkProtocol(getNetworkProtocolIn);
    // getNetworkProtocolOut =
    // aorFabric.getNetworkProtocol(getNetworkProtocolIn);
    // If10
    if (getNetworkProtocolOut.isSuccessful()) {
    } else {
      clearLocalIfEmptyIn.setQinQTag(addVlanIn.getQinQTag());
      addVlanOut.setValue(getNetworkProtocolOut.getValue());
      addVlanOut.setSuccessful(getNetworkProtocolOut.isSuccessful());
      clearLocalIfEmptyOut = clearLocalIfEmpty(clearLocalIfEmptyIn);
      // clearLocalIfEmptyOut =
      // aorFabric.clearLocalIfEmpty(clearLocalIfEmptyIn);
      return addVlanOut;
    }
    // If11
    if (getNetworkProtocolOut.getProtocol().equals("Ethernet")
        && addVlanIn.isRawEthernet()
        || getNetworkProtocolOut.getProtocol().equals("IP")
        && !addVlanIn.isRawEthernet()) {
    } else {
      clearLocalIfEmptyIn.setQinQTag(addVlanIn.getQinQTag());
      addVlanOut.setSuccessful(Boolean.FALSE);
      addVlanOut
          .setValue("Specified Network protocol does not match" +
              "current value for that Q in Q tag");
      clearLocalIfEmptyOut = clearLocalIfEmpty(clearLocalIfEmptyIn);
      // clearLocalIfEmptyOut =
      // aorFabric.clearLocalIfEmpty(clearLocalIfEmptyIn);
      return addVlanOut;
    }
    // If12
    if (addVlanIn.isRawEthernet()) {
      getIPAddressIn.setQInQTag(addVlanIn.getQinQTag());
      getIPAddressIn.setUuid(addVlanIn.getUuid());
      getIPAddressOut = getIPAddress(getIPAddressIn);
      // getIPAddressOut = aorFabric.getIPAddress(getIPAddressIn);
      // If13
      if (getIPAddressOut.isSuccessful()) {
        internalAddVlanIn.setIP(getIPAddressOut.getIP());
      } else {
        // Assign45
        clearLocalIfEmptyIn.setQinQTag(addVlanIn.getQinQTag());
        addVlanOut.setValue(getIPAddressOut.getValue());
        addVlanOut.setSuccessful(getIPAddressOut.isSuccessful());
        clearLocalIfEmptyOut = clearLocalIfEmpty(clearLocalIfEmptyIn);
        // clearLocalIfEmptyOut =
        // aorFabric.clearLocalIfEmpty(clearLocalIfEmptyIn);
        return addVlanOut;
      }
    } else {
      // Assign43
      internalAddVlanIn.setIP("0.0.0.0");
    }
    // Assign21
    internalAddVlanIn.setUuid(addVlanIn.getUuid());
    internalAddVlanIn.setVlan(createLocalTagOut.getLocalVlanId());
    internalAddVlanOut = aorHardware.addVlan(internalAddVlanIn);
    // If14
    if (internalAddVlanOut.isSuccessful()) {
    } else {
      addVlanOut.setSuccessful(internalAddVlanOut.isSuccessful());
      addVlanOut.setValue(internalAddVlanOut.getValue());
      clearLocalIfEmptyIn.setQinQTag(addVlanIn.getQinQTag());
      releaseIPAddressIn.setIP(internalAddVlanIn.getIP());
      releaseIPAddressOut = releaseIPAddress(releaseIPAddressIn);
      // releaseIPAddressOut = aorFabric.releaseIPAddress(releaseIPAddressIn);
      clearLocalIfEmptyOut = clearLocalIfEmpty(clearLocalIfEmptyIn);
      // clearLocalIfEmptyOut =
      // aorFabric.clearLocalIfEmpty(clearLocalIfEmptyIn);
      return addVlanOut;
    }
    // Assign47
    addVlanToPortIn.setVlan(createLocalTagOut.getLocalVlanId());
    addVlanToPortIn.setUUID(addVlanIn.getUuid());
    addVlanToPortOut = addVlanToPort(addVlanToPortIn);
    // addVlanToPortOut = aorFabric.addVlanToPort(addVlanToPortIn);
    // Assign22
    addVlanOut.setValue(internalAddVlanOut.getValue());
    addVlanOut.setSuccessful(internalAddVlanOut.isSuccessful());
    // If15
    if (addVlanToPortOut.isSuccessful()) {
    } else {
      releaseIPAddressIn.setIP(internalAddVlanIn.getIP());
      clearLocalIfEmptyIn.setQinQTag(addVlanIn.getQinQTag());
      internalRemoveVlanIn.setUuid(addVlanIn.getUuid());
      internalRemoveVlanIn.setVlan(createLocalTagOut.getLocalVlanId());
      addVlanOut.setValue(addVlanToPortOut.getValue());
      releaseIPAddressOut = releaseIPAddress(releaseIPAddressIn);
      // releaseIPAddressOut = aorFabric.releaseIPAddress(releaseIPAddressIn);
      clearLocalIfEmptyOut = clearLocalIfEmpty(clearLocalIfEmptyIn);
      // clearLocalIfEmptyOut =
      // aorFabric.clearLocalIfEmpty(clearLocalIfEmptyIn);
      internalRemoveVlanOut = aorHardware.removeVlan(internalRemoveVlanIn);
      return addVlanOut;
    }
    // Assign49
    addVlanOut.setValue(internalAddVlanOut.getValue());
    addVlanOut.setSuccessful(internalAddVlanOut.isSuccessful());
    return addVlanOut;
  }

  public RemoveVlanResponse removeVlan(RemoveVlanRequest removeVlanIn) {
    RemoveVlanResponse removeVlanOut = new RemoveVlanResponse();
    createLocalTagIn.setQinQTag(removeVlanIn.getQinQTag());
    clearLocalIfEmptyIn.setQinQTag(removeVlanIn.getQinQTag());
    createLocalTagIn.setIsRawEthernet(Boolean.FALSE);
    createLocalTagOut = createLocalTag(createLocalTagIn);
    // createLocalTagOut = aorFabric.createLocalTag(createLocalTagIn);
    clearLocalIfEmptyOut = clearLocalIfEmpty(clearLocalIfEmptyIn);
    // clearLocalIfEmptyOut = aorFabric.clearLocalIfEmpty(clearLocalIfEmptyIn);
    // Assign50
    internalRemoveVlanIn.setUuid(removeVlanIn.getUuid());
    internalRemoveVlanIn.setVlan(createLocalTagOut.getLocalVlanId());
    removeVlanFromPortIn.setVlan(createLocalTagOut.getLocalVlanId());
    removeVlanFromPortIn.setUUID(removeVlanIn.getUuid());
    internalRemoveVlanOut = aorHardware.removeVlan(internalRemoveVlanIn);
    removeVlanFromPortOut = removeVlanFromPort(removeVlanFromPortIn);
    // removeVlanFromPortOut =
    // aorFabric.removeVlanFromPort(removeVlanFromPortIn);
    // Assign24
    removeVlanOut.setValue(internalRemoveVlanOut.getValue());
    removeVlanOut.setSuccessful(internalRemoveVlanOut.isSuccessful());
    return removeVlanOut;
  }

  /**
   * @param m
   * @return this function returns a hard-coded value for now and later will be
   *         modified to connect to the virtual network of an experiment.
   */
  private static CreateLocalTagResponse createLocalTag(
      ca.savi.cobra.model.CreateLocalTagRequest m) {
    // ca.savi.cobra.AORFabricService service =
    // new ca.savi.cobra.AORFabricService();
    // ca.savi.cobra.AORFabricPortType port =
    // service.getAORFabricPort();
    // return port.createLocalTag(m);
    CreateLocalTagResponse resp = new CreateLocalTagResponse();
    resp.setSuccessful(Boolean.TRUE);
    resp.setLocalVlanId(11L);
    resp.setValue("success");
    return resp;
  }

  /**
   * @param m
   * @return this function returns a hard-coded value for now and later will be
   *         modified to connect to the virtual network of an experiment.
   */
  private static ClearLocalIfEmptyResponse clearLocalIfEmpty(
      ca.savi.cobra.model.ClearLocalIfEmptyRequest m) {
    // ca.savi.cobra.AORFabricService service =
    // new ca.savi.cobra.AORFabricService();
    // ca.savi.cobra.AORFabricPortType port =
    // service.getAORFabricPort();
    // return port.clearLocalIfEmpty(m);
    ClearLocalIfEmptyResponse resp = new ClearLocalIfEmptyResponse();
    resp.setSuccessful(Boolean.TRUE);
    resp.setValue("success");
    return resp;
  }

  /**
   * @param m
   * @return this function returns a hard-coded value for now and later will be
   *         modified to connect to the virtual network of an experiment.
   */
  private static GetNetworkProtocolResponse getNetworkProtocol(
      ca.savi.cobra.model.GetNetworkProtocolRequest m) {
    // ca.savi.cobra.AORFabricService service =
    // new ca.savi.cobra.AORFabricService();
    // ca.savi.cobra.AORFabricPortType port =
    // service.getAORFabricPort();
    // return port.getNetworkProtocol(m);
    GetNetworkProtocolResponse resp = new GetNetworkProtocolResponse();
    resp.setProtocol("Ethernet");
    resp.setSuccessful(Boolean.TRUE);
    resp.setValue("success");
    return resp;
  }

  /**
   * @param m
   * @return this function returns a hard-coded value for now and later will be
   *         modified to connect to the virtual network of an experiment.
   */
  private static GetIPAddressResponse getIPAddress(
      ca.savi.cobra.model.GetIPAddressRequest m) {
    // ca.savi.cobra.AORFabricService service =
    // new ca.savi.cobra.AORFabricService();
    // ca.savi.cobra.AORFabricPortType port =
    // service.getAORFabricPort();
    // return port.getIPAddress(m);
    GetIPAddressResponse resp = new GetIPAddressResponse();
    resp.setIP("10.176.0.105:10.176.0.1:10.176.0.1:res-105-1001");
    resp.setSuccessful(Boolean.TRUE);
    resp.setValue("success");
    return resp;
  }

  /**
   * @param m
   * @return this function returns a hard-coded value for now and later will be
   *         modified to connect to the virtual network of an experiment.
   */
  private static ReleaseIPAddressResponse releaseIPAddress(
      ca.savi.cobra.model.ReleaseIPAddressRequest m) {
    // ca.savi.cobra.AORFabricService service =
    // new ca.savi.cobra.AORFabricService();
    // ca.savi.cobra.AORFabricPortType port =
    // service.getAORFabricPort();
    // return port.releaseIPAddress(m);
    ReleaseIPAddressResponse resp = new ReleaseIPAddressResponse();
    resp.setSuccessful(Boolean.TRUE);
    resp.setValue("success");
    return resp;
  }

  /**
   * @param m
   * @return this function returns a hard-coded value for now and later will be
   *         modified to connect to the virtual network of an experiment.
   */
  private static AddVlanToPortResponse addVlanToPort(
      ca.savi.cobra.model.AddVlanToPortRequest m) {
    // ca.savi.cobra.AORFabricService service =
    // new ca.savi.cobra.AORFabricService();
    // ca.savi.cobra.AORFabricPortType port =
    // service.getAORFabricPort();
    // return port.addVlanToPort(m);
    AddVlanToPortResponse resp = new AddVlanToPortResponse();
    resp.setSuccessful(Boolean.TRUE);
    resp.setValue("success");
    return resp;
  }

  /**
   * @param m
   * @return this function returns a hard-coded value for now and later will be
   *         modified to connect to the virtual network of an experiment.
   */
  private static ClearUUIDResponse clearUUID(
      ca.savi.cobra.model.ClearUUIDRequest m) {
    // ca.savi.cobra.AORFabricService service =
    // new ca.savi.cobra.AORFabricService();
    // ca.savi.cobra.AORFabricPortType port =
    // service.getAORFabricPort();
    // return port.clearUUID(m);
    ClearUUIDResponse resp = new ClearUUIDResponse();
    resp.setVlan(11L);
    resp.setLastUUID(Boolean.TRUE);
    resp.setSuccessful(Boolean.TRUE);
    resp.setValue("success");
    return resp;
  }

  /**
   * @param m
   * @return this function returns a hard-coded value for now and later will be
   *         modified to connect to the virtual network of an experiment.
   */
  private static RemoveVlanFromPortResponse removeVlanFromPort(
      ca.savi.cobra.model.RemoveVlanFromPortRequest m) {
    // ca.savi.cobra.AORFabricService service =
    // new ca.savi.cobra.AORFabricService();
    // ca.savi.cobra.AORFabricPortType port =
    // service.getAORFabricPort();
    // return port.removeVlanFromPort(m);
    RemoveVlanFromPortResponse resp = new RemoveVlanFromPortResponse();
    resp.setSuccessful(Boolean.TRUE);
    resp.setValue("success");
    return resp;
  }
}
