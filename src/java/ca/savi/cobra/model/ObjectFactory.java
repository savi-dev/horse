package ca.savi.cobra.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the ca.savinetwork.testbed.aorFabricWebService
 * package.
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
      "fabric.XSD.AOR", "terminateRequest");
  private final static QName _InitializeRequest_QNAME = new QName(
      "fabric.XSD.AOR", "initializeRequest");
  private final static QName _RetrieveFabricDescriptionRequest_QNAME =
      new QName("fabric.XSD.AOR", "retrieveFabricDescriptionRequest");
  private final static QName _ResetRequest_QNAME = new QName("fabric.XSD.AOR",
      "resetRequest");

  /**
   * Create a new ObjectFactory that can be used to create new instances of
   * schema derived classes for package:
   * ca.savinetwork.testbed.aorFabricWebService
   */
  public ObjectFactory() {
  }

  /**
   * Create an instance of {@link RemoveUUIDFromFabricResponse }
   */
  public RemoveUUIDFromFabricResponse createRemoveUUIDFromFabricResponse() {
    return new RemoveUUIDFromFabricResponse();
  }

  /**
   * Create an instance of {@link RemoveVlanFromPortResponse }
   */
  public RemoveVlanFromPortResponse createRemoveVlanFromPortResponse() {
    return new RemoveVlanFromPortResponse();
  }

  /**
   * Create an instance of {@link RemoveMacRestrictionRequest }
   */
  public RemoveMacRestrictionRequest createRemoveMacRestrictionRequest() {
    return new RemoveMacRestrictionRequest();
  }

  /**
   * Create an instance of {@link ReleaseIPAddressRequest }
   */
  public ReleaseIPAddressRequest createReleaseIPAddressRequest() {
    return new ReleaseIPAddressRequest();
  }

  /**
   * Create an instance of {@link DescribeFabricResponse }
   */
  public DescribeFabricResponse createDescribeFabricResponse() {
    return new DescribeFabricResponse();
  }

  /**
   * Create an instance of {@link Port }
   */
  public Port createPort() {
    return new Port();
  }

  /**
   * Create an instance of {@link ClearUUIDRequest }
   */
  public ClearUUIDRequest createClearUUIDRequest() {
    return new ClearUUIDRequest();
  }

  /**
   * Create an instance of {@link ClearLocalIfEmptyResponse }
   */
  public ClearLocalIfEmptyResponse createClearLocalIfEmptyResponse() {
    return new ClearLocalIfEmptyResponse();
  }

  /**
   * Create an instance of {@link GetNetworkProtocolResponse }
   */
  public GetNetworkProtocolResponse createGetNetworkProtocolResponse() {
    return new GetNetworkProtocolResponse();
  }

  /**
   * Create an instance of {@link GetIPAddressResponse }
   */
  public GetIPAddressResponse createGetIPAddressResponse() {
    return new GetIPAddressResponse();
  }

  /**
   * Create an instance of {@link RemoveUUIDFromFabricRequest }
   */
  public RemoveUUIDFromFabricRequest createRemoveUUIDFromFabricRequest() {
    return new RemoveUUIDFromFabricRequest();
  }

  /**
   * Create an instance of {@link MacAddress }
   */
  public MacAddress createMacAddress() {
    return new MacAddress();
  }

  /**
   * Create an instance of {@link GetNetworkProtocolRequest }
   */
  public GetNetworkProtocolRequest createGetNetworkProtocolRequest() {
    return new GetNetworkProtocolRequest();
  }

  /**
   * Create an instance of {@link GetInternalIPResponse }
   */
  public GetInternalIPResponse createGetInternalIPResponse() {
    return new GetInternalIPResponse();
  }

  /**
   * Create an instance of {@link GetInternalIPRequest }
   */
  public GetInternalIPRequest createGetInternalIPRequest() {
    return new GetInternalIPRequest();
  }

  /**
   * Create an instance of {@link SetMacAddressResponse }
   */
  public SetMacAddressResponse createSetMacAddressResponse() {
    return new SetMacAddressResponse();
  }

  /**
   * Create an instance of {@link VlanType }
   */
  public VlanType createVlanType() {
    return new VlanType();
  }

  /**
   * Create an instance of {@link CreateLocalTagRequest }
   */
  public CreateLocalTagRequest createCreateLocalTagRequest() {
    return new CreateLocalTagRequest();
  }

  /**
   * Create an instance of {@link Aggregate }
   */
  public Aggregate createAggregate() {
    return new Aggregate();
  }

  /**
   * Create an instance of {@link CreateLocalTagResponse }
   */
  public CreateLocalTagResponse createCreateLocalTagResponse() {
    return new CreateLocalTagResponse();
  }

  /**
   * Create an instance of {@link ResetResponse }
   */
  public ResetResponse createResetResponse() {
    return new ResetResponse();
  }

  /**
   * Create an instance of {@link ClearLocalIfEmptyRequest }
   */
  public ClearLocalIfEmptyRequest createClearLocalIfEmptyRequest() {
    return new ClearLocalIfEmptyRequest();
  }

  /**
   * Create an instance of {@link GetMacAddressRequest }
   */
  public GetMacAddressRequest createGetMacAddressRequest() {
    return new GetMacAddressRequest();
  }

  /**
   * Create an instance of {@link AddUUIDToFabricResponse }
   */
  public AddUUIDToFabricResponse createAddUUIDToFabricResponse() {
    return new AddUUIDToFabricResponse();
  }

  /**
   * Create an instance of {@link GetVlansRequest }
   */
  public GetVlansRequest createGetVlansRequest() {
    return new GetVlansRequest();
  }

  /**
   * Create an instance of {@link RemoveMacRestrictionResponse }
   */
  public RemoveMacRestrictionResponse createRemoveMacRestrictionResponse() {
    return new RemoveMacRestrictionResponse();
  }

  /**
   * Create an instance of {@link Switch }
   */
  public Switch createSwitch() {
    return new Switch();
  }

  /**
   * Create an instance of {@link GetVlansResponse }
   */
  public GetVlansResponse createGetVlansResponse() {
    return new GetVlansResponse();
  }

  /**
   * Create an instance of {@link VANIFabric }
   */
  public VANIFabric createVANIFabric() {
    return new VANIFabric();
  }

  /**
   * Create an instance of {@link RemoveVlanFromPortRequest }
   */
  public RemoveVlanFromPortRequest createRemoveVlanFromPortRequest() {
    return new RemoveVlanFromPortRequest();
  }

  /**
   * Create an instance of {@link SetMacAddressRequest }
   */
  public SetMacAddressRequest createSetMacAddressRequest() {
    return new SetMacAddressRequest();
  }

  /**
   * Create an instance of {@link AddVlanToPortRequest }
   */
  public AddVlanToPortRequest createAddVlanToPortRequest() {
    return new AddVlanToPortRequest();
  }

  /**
   * Create an instance of {@link InitializeResponse }
   */
  public InitializeResponse createInitializeResponse() {
    return new InitializeResponse();
  }

  /**
   * Create an instance of {@link ClearUUIDResponse }
   */
  public ClearUUIDResponse createClearUUIDResponse() {
    return new ClearUUIDResponse();
  }

  /**
   * Create an instance of {@link AddVlanToPortResponse }
   */
  public AddVlanToPortResponse createAddVlanToPortResponse() {
    return new AddVlanToPortResponse();
  }

  /**
   * Create an instance of {@link GetIPAddressRequest }
   */
  public GetIPAddressRequest createGetIPAddressRequest() {
    return new GetIPAddressRequest();
  }

  /**
   * Create an instance of {@link RetrieveFabricDescriptionResponse }
   */
  public RetrieveFabricDescriptionResponse
      createRetrieveFabricDescriptionResponse() {
    return new RetrieveFabricDescriptionResponse();
  }

  /**
   * Create an instance of {@link ReleaseIPAddressResponse }
   */
  public ReleaseIPAddressResponse createReleaseIPAddressResponse() {
    return new ReleaseIPAddressResponse();
  }

  /**
   * Create an instance of {@link AddUUIDToFabricRequest }
   */
  public AddUUIDToFabricRequest createAddUUIDToFabricRequest() {
    return new AddUUIDToFabricRequest();
  }

  /**
   * Create an instance of {@link GetMacAddressResponse }
   */
  public GetMacAddressResponse createGetMacAddressResponse() {
    return new GetMacAddressResponse();
  }

  /**
   * Create an instance of {@link TerminateResponse }
   */
  public TerminateResponse createTerminateResponse() {
    return new TerminateResponse();
  }

  /**
   * Create an instance of {@link DescribeFabricRequest }
   */
  public DescribeFabricRequest createDescribeFabricRequest() {
    return new DescribeFabricRequest();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
   */
  @XmlElementDecl(namespace = "fabric.XSD.AOR", name = "terminateRequest")
  public JAXBElement<Object> createTerminateRequest(Object value) {
    return new JAXBElement<Object>(_TerminateRequest_QNAME, Object.class, null,
        value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
   */
  @XmlElementDecl(namespace = "fabric.XSD.AOR", name = "initializeRequest")
  public JAXBElement<Object> createInitializeRequest(Object value) {
    return new JAXBElement<Object>(_InitializeRequest_QNAME, Object.class,
        null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
   */
  @XmlElementDecl(namespace = "fabric.XSD.AOR",
      name = "retrieveFabricDescriptionRequest")
  public JAXBElement<Object>
      createRetrieveFabricDescriptionRequest(Object value) {
    return new JAXBElement<Object>(_RetrieveFabricDescriptionRequest_QNAME,
        Object.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
   */
  @XmlElementDecl(namespace = "fabric.XSD.AOR", name = "resetRequest")
  public JAXBElement<Object> createResetRequest(Object value) {
    return new JAXBElement<Object>(_ResetRequest_QNAME, Object.class, null,
        value);
  }
}
