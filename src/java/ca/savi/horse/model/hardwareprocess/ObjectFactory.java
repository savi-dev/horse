//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, vhudson-jaxb-ri-2.2-147
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2012.03.19 at 01:54:27 PM EDT
//
package ca.savi.horse.model.hardwareprocess;

import ca.savi.horse.model.FPGA;
import ca.savi.horse.model.Hwnode;
import ca.savi.horse.model.Icnode;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * ca.savinetwork.testbed.type.hardwareprocessschema package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {
  private final static QName _TerminateRequest_QNAME = new QName(
      "process.hardware.XSD.AOR", "terminateRequest");
  private final static QName _InitRequest_QNAME = new QName(
      "process.hardware.XSD.AOR", "initRequest");
  private final static QName _StopServerRequest_QNAME = new QName(
      "process.hardware.XSD.AOR", "stopServerRequest");

  /**
   * Create a new ObjectFactory that can be used to create new instances of
   * schema derived classes for package:
   * ca.savinetwork.testbed.type.hardwareprocessschema
   */
  public ObjectFactory() {
  }

  /**
   * Create an instance of {@link ResourceListResponse.Icnode }
   */
  public Icnode createResourceListResponseIcnode() {
    return new Icnode();
  }

  /**
   * Create an instance of {@link ResourceGetResponse.FPGA }
   */
  public FPGA createResourceGetResponseFPGA() {
    return new FPGA();
  }

  /**
   * Create an instance of {@link ResourceStatusResponse }
   */
  public ResourceStatusResponse createResourceStatusResponse() {
    return new ResourceStatusResponse();
  }

  /**
   * Create an instance of {@link ResourceListResponse }
   */
  public ResourceListResponse createResourceListResponse() {
    return new ResourceListResponse();
  }

  /**
   * Create an instance of {@link UserRegisterInteractionRequest }
   */
  public UserRegisterInteractionRequest createUserRegisterInteractionRequest() {
    return new UserRegisterInteractionRequest();
  }

  /**
   * Create an instance of {@link ResourceGetRequest }
   */
  public ResourceGetRequest createResourceGetRequest() {
    return new ResourceGetRequest();
  }

  /**
   * Create an instance of {@link ResourceReleaseRequest }
   */
  public ResourceReleaseRequest createResourceReleaseRequest() {
    return new ResourceReleaseRequest();
  }

  /**
   * Create an instance of {@link ResourceGetRequest.FPGA }
   */
  public FPGA createResourceGetRequestFPGA() {
    return new FPGA();
  }

  /**
   * Create an instance of {@link RemoveVlanRequest }
   */
  public RemoveVlanRequest createRemoveVlanRequest() {
    return new RemoveVlanRequest();
  }

  /**
   * Create an instance of {@link SetResourceParametersResponse }
   */
  public SetResourceParametersResponse createSetResourceParametersResponse() {
    return new SetResourceParametersResponse();
  }

  /**
   * Create an instance of {@link ResourceResetResponse }
   */
  public ResourceResetResponse createResourceResetResponse() {
    return new ResourceResetResponse();
  }

  /**
   * Create an instance of {@link InitResponse }
   */
  public InitResponse createInitResponse() {
    return new InitResponse();
  }

  /**
   * Create an instance of {@link ProgramResourceResponse }
   */
  public ProgramResourceResponse createProgramResourceResponse() {
    return new ProgramResourceResponse();
  }

  /**
   * Create an instance of {@link ResourceListResponse.Hwnode }
   */
  public Hwnode createResourceListResponseHwnode() {
    return new Hwnode();
  }

  /**
   * Create an instance of {@link AddVlanRequest }
   */
  public AddVlanRequest createAddVlanRequest() {
    return new AddVlanRequest();
  }

  /**
   * Create an instance of {@link UserRegisterInteractionResponse }
   */
  public UserRegisterInteractionResponse
      createUserRegisterInteractionResponse() {
    return new UserRegisterInteractionResponse();
  }

  /**
   * Create an instance of {@link TerminateResponse }
   */
  public TerminateResponse createTerminateResponse() {
    return new TerminateResponse();
  }

  /**
   * Create an instance of {@link ResourceListRequest }
   */
  public ResourceListRequest createResourceListRequest() {
    return new ResourceListRequest();
  }

  /**
   * Create an instance of {@link ResourceReleaseResponse }
   */
  public ResourceReleaseResponse createResourceReleaseResponse() {
    return new ResourceReleaseResponse();
  }

  /**
   * Create an instance of {@link StopServerResponse }
   */
  public StopServerResponse createStopServerResponse() {
    return new StopServerResponse();
  }

  /**
   * Create an instance of {@link ResourceStatusRequest }
   */
  public ResourceStatusRequest createResourceStatusRequest() {
    return new ResourceStatusRequest();
  }

  /**
   * Create an instance of {@link ResourceGetResponse }
   */
  public ResourceGetResponse createResourceGetResponse() {
    return new ResourceGetResponse();
  }

  /**
   * Create an instance of {@link ProgramResourceRequest }
   */
  public ProgramResourceRequest createProgramResourceRequest() {
    return new ProgramResourceRequest();
  }

  /**
   * Create an instance of {@link SetResourceParametersRequest }
   */
  public SetResourceParametersRequest createSetResourceParametersRequest() {
    return new SetResourceParametersRequest();
  }

  /**
   * Create an instance of {@link RemoveVlanResponse }
   */
  public RemoveVlanResponse createRemoveVlanResponse() {
    return new RemoveVlanResponse();
  }

  /**
   * Create an instance of {@link AddVlanResponse }
   */
  public AddVlanResponse createAddVlanResponse() {
    return new AddVlanResponse();
  }

  /**
   * Create an instance of {@link ResourceResetRequest }
   */
  public ResourceResetRequest createResourceResetRequest() {
    return new ResourceResetRequest();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
   */
  @XmlElementDecl(namespace = "process.hardware.XSD.AOR",
      name = "terminateRequest")
  public JAXBElement<Object> createTerminateRequest(Object value) {
    return new JAXBElement<Object>(_TerminateRequest_QNAME, Object.class, null,
        value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
   */
  @XmlElementDecl(namespace = "process.hardware.XSD.AOR", name = "initRequest")
  public JAXBElement<Object> createInitRequest(Object value) {
    return new JAXBElement<Object>(_InitRequest_QNAME, Object.class, null,
        value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
   */
  @XmlElementDecl(namespace = "process.hardware.XSD.AOR",
      name = "stopServerRequest")
  public JAXBElement<Object> createStopServerRequest(Object value) {
    return new JAXBElement<Object>(_StopServerRequest_QNAME, Object.class,
        null, value);
  }
}
