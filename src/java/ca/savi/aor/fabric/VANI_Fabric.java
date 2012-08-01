// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.fabric;

import java.io.File;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import ca.savi.aor.generic.BoolString;
import ca.savi.aor.ip.IP_Handler;
import ca.savi.aor.messageexceptions.GenericException;

/**
 * @author Keith
 */
public class VANI_Fabric {
  public boolean[] reservedVLANs, broadcastVLANs;
  public String networkDescriptionFilename, networkDescriptionSchema,
      rootDirectory;
  private boolean built;
  public Switch[] switches;
  private int maxSwitches, switchIndex;
  private Document DOM;
  public IP_Handler ipAddressList;
  public final int startVlanId;
  public final int endVlanId;

  public VANI_Fabric(String filename, String schemaName, String rd,
      int startVlanId, int endVlanId) {
    this.endVlanId = endVlanId;
    this.startVlanId = startVlanId;
    reservedVLANs = new boolean[endVlanId + 1];
    broadcastVLANs = new boolean[endVlanId + 1];
    for (int i = 0; i < endVlanId; i++) {
      reservedVLANs[i] = false;
      broadcastVLANs[i] = false;
    }
    reservedVLANs[0] = true;
    built = false;
    networkDescriptionFilename = filename;
    networkDescriptionSchema = schemaName;
    maxSwitches = 100;
    switchIndex = 0;
    switches = new Switch[maxSwitches];
    rootDirectory = rd;
    ipAddressList = new IP_Handler(this);
  }

  // -----------------------------------------------------------
  // -------------FABRIC CONFIGURATION FUNCTIONS----------------
  public void addSwitch(String id, String addr, int subPort, Socket sock,
      int numP, UUID uuid) {
    System.out.println("Adding switch: " + id);
    switches[switchIndex] =
        new Switch(id, addr, subPort, sock, numP, this, "", uuid);
    switchIndex++;
  }

  // -----------------------------------------------------------
  public void deleteSwitch(String id) {
    if (!built)
      return;
    for (int i = 0; i < switchIndex; i++) {
      if (switches[i].name.equals(id)) {
        Switch temp;
        temp = switches[i];
        switches[i] = switches[switchIndex - 1];
        temp = null;
        switchIndex--;
      }
    }
  }

  // -----------------------------------------------------------
  public void terminate() {
    for (int i = 0; i < switchIndex; i++) {
      switches[i].terminate();
      switches[i] = null;
    }
    built = false;
  }

  // -----------------------------------------------------------
  // ---------------INFORMATION FUNCTIONS-----------------------
  public boolean isinit() {
    return (built);
  }

  // -----------------------------------------------------------
  public boolean isReserved(int vlanid) {
    if (vlanid < 0 || vlanid > 4095)
      return false;
    return (reservedVLANs[vlanid]);
  }

  // -----------------------------------------------------------
  public boolean isBroadcast(int vlanid) {
    if (vlanid < 0 || vlanid > 4095)
      return false;
    return (broadcastVLANs[vlanid]);
  }

  // -----------------------------------------------------------
  public boolean exists(int vlanid) {
    for (int i = 0; i < switchIndex; i++) {
      System.out.println("Checking with switch");
      if (switches[i].vlanExists(vlanid))
        return true;
      else
        System.out.println("Switch returned false");
    }
    return false;
  }

  // -----------------------------------------------------------
  public boolean vlanExistsIgnoreALLs(int vlanid) {
    for (int i = 0; i < switchIndex; i++) {
      if (switches[i].vlanExistsIgnoreALLs(vlanid))
        return true;
    }
    return false;
  }

  // -----------------------------------------------------------
  public void printNet() {
    if (!built)
      return;
    /*
     * System.out.println("Broadcast VLANs:"); for(int i=0; i<4096; i++) {
     * if(broadcastVLANs[i]) System.out.println("VLAN " + i); }
     * System.out.println("Reserved VLANs:"); for(int i=0; i<4096; i++) {
     * if(reservedVLANs[i]) System.out.println("VLAN " + i); }
     */
    for (int i = 0; i < maxSwitches; i++) {
      if (switches[i] != null) {
        String out = "Printing Switch # " + i;
        System.out.println(out);
        switches[i].printPorts();
      }
    }
  }

  // -----------------------------------------------------------
  // ---------------VLAN CONFIGURATION FUNCTIONS----------------
  public boolean createVlan(int vlanid) {
    boolean returnVal = true;
    for (int i = 0; i < switchIndex; i++) {
      if (!switches[i].createVlan(vlanid))
        returnVal = false;
    }
    return returnVal;
  }

  // -----------------------------------------------------------
  public void deleteVlan(int vlanid) throws GenericException {
    for (int i = 0; i < switchIndex; i++)
      switches[i].deleteVlan(vlanid);
  }

  // -----------------------------------------------------------
  public boolean addVlanToPort(String id, int vlanid, int resId)
      throws GenericException {
    boolean returnVal = true, rawEthernet;
    // fail if vlanid is reserved
    if (isReserved(vlanid))
      throw new GenericException("Error, attempted to add a reserved vlan id");
    // fail if the vlanid has not been created yet
    if (!ipAddressList.isInUse(vlanid))
      throw new GenericException("Error, supplied vlan id is not in use");
    int Q_in_Q_tag = ipAddressList.localToGlobal(vlanid);
    UUID uuid;
    try {
      uuid = UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new GenericException(e.getMessage());
    }
    ipAddressList.addUUID(Q_in_Q_tag, uuid, resId);
    rawEthernet = ipAddressList.isRawEthernet(vlanid);
    // if vlan does not exist, create it:
    if (!exists(vlanid))
      createVlan(vlanid);
    // if the switches fail to add it, return failure
    for (int i = 0; i < switchIndex; i++) {
      if (!switches[i].addVlanToPort(id, vlanid, rawEthernet))
        returnVal = false;
    }
    // if adding the vlan failed, release the Q_in_Q_tag entry
    if (!returnVal)
      ipAddressList.removeUUIDFromVlan(Q_in_Q_tag, uuid);
    return returnVal;
  }

  // -----------------------------------------------------------
  public boolean removeVlanFromPort(String id, int vlanid)
      throws GenericException {
    boolean returnVal = true, rawEthernet;
    int numUUIDsRemaining;
    /*
     * if(!ipAddressList.isInUse(vlanid)) throw new
     * GenericException("Error, supplied vlan id is not in use");
     */
    int Q_in_Q_tag = ipAddressList.localToGlobal(vlanid);
    UUID uuid;
    try {
      uuid = UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new GenericException(e.getMessage());
    }
    numUUIDsRemaining = ipAddressList.removeUUIDFromVlan(Q_in_Q_tag, uuid);
    rawEthernet = ipAddressList.isRawEthernet(vlanid);
    /*
     * if(numUUIDsRemaining == 0) ipAddressList.clearLocalTag(vlanid);
     */
    if (isReserved(vlanid))
      return false;
    for (int i = 0; i < switchIndex; i++) {
      if (!switches[i].removeVlanFromPort(id, vlanid, rawEthernet))
        returnVal = false;
    }
    return returnVal;
  }

  // -----------------------------------------------------------
  public boolean clearUUIDVlans(String id) throws GenericException {
    boolean returnVal = true;
    UUID uuid;
    try {
      uuid = UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      return false;
    }
    clearUUIDVlans(uuid);
    return returnVal;
  }

  // -----------------------------------------------------------
  // --------------MAC RETRIEVAL FUNCTIONS----------------------
  public byte[] findMac(String uuid, int interfaceIndex) throws Exception {
    UUID id = UUID.fromString(uuid);
    for (int i = 0; i < switchIndex; i++) {
      try {
        // exception will be thrown if not found
        return (switches[i].findMac(id, interfaceIndex));
      } catch (Exception e) {
        continue;
      }
    }
    throw new Exception("Error, uuid and interfaceIndex combination not found");
  }

  // -----------------------------------------------------------
  // --------------IP RETRIEVAL FUNCTIONS-----------------------
  public String[] listIPs(String uuid) throws Exception {
    String[] ipList = new String[endVlanId];
    UUID id = UUID.fromString(uuid);
    for (int i = 0; i < endVlanId; i++)
      ipList[i] = null;
    for (int i = 1; i < endVlanId; i++) {
      for (int j = 0; j < switchIndex; j++) {
        for (int k = 0; k < switches[i].numPorts; k++) {
          if (switches[j].ports[k] == null)
            continue;
          if (switches[j].ports[k].containsUUID(id)
              && switches[j].ports[k].isOnVlan(i))
            ipList[i] = ipAddressList.findIP(id, i);
        }
      }
    }
    return ipList;
  }

  // -----------------------------------------------------------
  // --------------IP/VLAN DATABASE FUNCTIONS-------------------
  public boolean releaseIP(String IP) throws GenericException {
    return (ipAddressList.releaseIP(IP));
  }

  public String getIP(int Q_in_Q_tag, UUID uuid) throws GenericException {
    return (ipAddressList.getIP(Q_in_Q_tag, uuid));
  }

  public int global_Q_in_Q_to_Local(int Q_in_Q_tag) throws GenericException {
    return (ipAddressList.globalToLocal(Q_in_Q_tag));
  }

  public int createLocalUser(int Q_in_Q_tag, boolean rawEthernet)
      throws GenericException, Exception {
    return (ipAddressList.createNewLocal(Q_in_Q_tag, rawEthernet));
  }

  public boolean getIsEthernet(int Q_in_Q_tag) throws GenericException {
    return (ipAddressList
        .isRawEthernet(ipAddressList.globalToLocal(Q_in_Q_tag)));
  }

  public void clearIfEmpty(int Q_in_Q_tag) throws GenericException {
    ipAddressList.clearIfEmpty(ipAddressList.globalToLocal(Q_in_Q_tag));
  }

  // -----------------------------------------------------------
  // ----------------BUILD NETWORK FUNCTIONS--------------------
  /*
   * These functions take in an XML-based description of the fabric's
   * configuration, and creates from it an object in memory (DOM) that is used
   * for configuration of vlans
   */
  public void getMacInfo(int portBit, NodeList nl) throws Exception {
    int interfaceIndex;
    for (int i = 0; i < nl.getLength(); i++) {
      Node n = nl.item(i);
      if (n.getNodeType() == Node.ELEMENT_NODE) {
        System.out.println("Getting mac element");
        if (n.getNodeName().equals("MAC")) {
          String strMac = n.getFirstChild().getNodeValue();
          switches[switchIndex - 1].ports[portBit].macAddress =
              macFromString(strMac);
        } else if (n.getNodeName().equals("interfaceIndex")) {
          interfaceIndex = Integer.valueOf(n.getFirstChild().getNodeValue());
          if (interfaceIndex < 0) {
            throw new Exception(
                "Error, interface index must be a positive integer");
          }
          switches[switchIndex - 1].ports[portBit].interfaceIndex =
              interfaceIndex;
        }
      }
    }
  }

  // -----------------------------------------------------------
  public void getPortInfo(NodeList nl) throws Exception {
    int portBit = -1;
    AccessTypes t = AccessTypes.UNDEFINED;
    String id = null, type = null;
    for (int i = 0; i < nl.getLength(); i++) {
      Node n = nl.item(i);
      if (n.getNodeType() == Node.ELEMENT_NODE) {
        System.out.println("Getting port element");
        if (n.getNodeName().equals("portBit")) {
          try {
            portBit = Integer.valueOf(n.getFirstChild().getNodeValue());
            if (portBit < 0 || portBit >= switches[switchIndex - 1].numPorts)
              throw new Exception("Error, portBit value must be between 0 and "
                  + switches[switchIndex - 1].numPorts);
            if (switches[switchIndex - 1].ports[portBit].hasBeenDefined)
              throw new Exception("Error, repeated definition of port bit: "
                  + portBit);
            else
              switches[switchIndex - 1].ports[portBit].hasBeenDefined = true;
          } catch (IllegalArgumentException e) {
            throw new Exception("Error in portBit: Invalid Argument");
          }
        } else if (n.getNodeName().equals("type")) {
          type = n.getFirstChild().getNodeValue().trim().toLowerCase();
          if (type.equals("multiple"))
            t = AccessTypes.MULTIPLE;
          else if (type.equals("all"))
            t = AccessTypes.ALL;
          else if (type.equals("nobroadcast"))
            t = AccessTypes.NOBROADCAST;
          else if (type.equals("gateway")) {
            t = AccessTypes.GATEWAY;
            System.out.println("Got Gateway type");
          } else if (type.equals("bridge")) {
            t = AccessTypes.BRIDGE;
            System.out.println("Got Bridge type");
          } else
            throw new Exception("Error in port type: " + type
                + " is an Invalid Argument");
        } else if (n.getNodeName().equals("uuid")) {
          try {
            id = n.getFirstChild().getNodeValue();
            if (id == null)
              throw new Exception("Error in Port: UUID must be specified");
            System.out.println("Adding port to switch # " + (switchIndex - 1)
                + " named: " + switches[switchIndex - 1].name);
            switches[switchIndex - 1].ports[portBit].addUUID(id, t);
          } catch (IllegalArgumentException e) {
            throw new Exception("Error in UUID: " + id
                + " is an Invalid Argument");
          }
        } else if (n.getNodeName().equals("MacAddress")) {
          NodeList MacLst = n.getChildNodes();
          getMacInfo(portBit, MacLst);
        }
      }
    }
  }

  // -----------------------------------------------------------
  public void getSwitchInfo(NodeList nl, String AggIP, int AggPort,
      Socket AggSock) throws Exception {
    String name = null;
    String switchIP = null;
    int switchPort = -1;
    UUID uuid = null;
    int numPorts = -1;
    boolean switchCreated = false;
    System.out.println("Getting switch info");
    for (int i = 0; i < nl.getLength(); i++) {
      Node n = nl.item(i);
      if (n.getNodeType() == Node.ELEMENT_NODE) {
        if (n.getNodeName().equals("name")) {
          name = n.getFirstChild().getNodeValue();
        } else if (n.getNodeName().equals("uuid")) {
          try {
            uuid = UUID.fromString(n.getFirstChild().getNodeValue());
          } catch (IllegalArgumentException e) {
            throw new Exception("Error in UUID: Invalid Argument");
          }
        } else if (n.getNodeName().equals("numPorts")) {
          try {
            numPorts = Integer.valueOf(n.getFirstChild().getNodeValue());
          } catch (IllegalArgumentException e) {
            throw new Exception("Error in numPorts: Invalid Argument");
          }
        } else if (n.getNodeName().equals("switchIP")) {
          switchIP = n.getFirstChild().getNodeValue();
        } else if (n.getNodeName().equals("switchPort")) {
          try {
            switchPort = Integer.valueOf(n.getFirstChild().getNodeValue());
            if (switchPort < 1 || switchPort > 65535)
              throw new Exception(
                  "Error, switch port must be between 1 and 65536");
          } catch (IllegalArgumentException e) {
            throw new Exception("Error in numPorts: Invalid Argument");
          }
        } else if (n.getNodeName().equals("Port")) {
          if (!switchCreated) {
            this.addSwitch(name, switchIP, switchPort, AggSock, numPorts, uuid);
            switchCreated = true;
          }
          NodeList PortLst = n.getChildNodes();
          getPortInfo(PortLst);
        }
      }
    }
    switches[switchIndex - 1].init();
  }

  // -----------------------------------------------------------
  public void getAggregateInfo(NodeList nl) throws Exception {
    String AggIP = null;
    int AggPort = -1;
    Socket AggSock = null;
    for (int i = 0; i < nl.getLength(); i++) {
      Node n = nl.item(i);
      if (n.getNodeType() == Node.ELEMENT_NODE) {
        if (n.getNodeName().equals("subagentIP")) {
          AggIP = n.getFirstChild().getNodeValue();
        } else if (n.getNodeName().equals("subagentPort")) {
          try {
            AggPort = Integer.valueOf(n.getFirstChild().getNodeValue());
            if (AggPort < 0 || AggPort > 65535) {
              throw new Exception("Error, invalid value for subagentPort");
            }
            System.out.println("Got aggregate " + AggIP + ":" + AggPort);
            AggSock = new Socket(AggIP, AggPort);
          } catch (UnknownHostException e) {
            /* do nothing for now */
            throw new Exception(e.getMessage());
          }
        } else if (n.getNodeName().equals("switch")) {
          if (AggPort < 0 || AggIP == null)
            throw new Exception("Error, aggregate is missing port or IP value");
          System.out.println("Got agg");
          NodeList SwLst = n.getChildNodes();
          getSwitchInfo(SwLst, AggIP, AggPort, AggSock);
        }
      }
    }
  }

  // -----------------------------------------------------------
  public void getNetworkInfo(NodeList nl) throws Exception {
    int vlanID;
    try {
      for (int i = 0; i < nl.getLength(); i++) {
        Node n = nl.item(i);
        if (n.getNodeType() == Node.ELEMENT_NODE) {
          if (n.getNodeName().equals("reservedID")) {
            vlanID = Integer.valueOf(n.getFirstChild().getNodeValue());
            if (vlanID > 0 && vlanID < endVlanId)
              reservedVLANs[vlanID] = true;
          } else if (n.getNodeName().equals("broadcastID")) {
            vlanID = Integer.valueOf(n.getFirstChild().getNodeValue());
            if (vlanID > 0 && vlanID < endVlanId)
              broadcastVLANs[vlanID] = true;
          } else if (n.getNodeName().equals("aggregate")) {
            System.out.println("Got agg");
            NodeList AggLst = n.getChildNodes();
            getAggregateInfo(AggLst);
          }
        }
      }
    } catch (IllegalArgumentException e) {
      throw new Exception(e.getMessage());
    }
  }

  // -----------------------------------------------------------
  public BoolString buildNetwork() {
    BoolString returnVal = new BoolString();
    returnVal.bool = false;
    try {
      int vlanID;
      File file = new File(rootDirectory + networkDescriptionFilename);
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      dbf.setValidating(true);
      dbf.setAttribute(
          "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
          "http://www.w3.org/2001/XMLSchema");
      dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource",
          new File(rootDirectory + "fabricDescriptionSchema.xsd"));
      DocumentBuilder db = dbf.newDocumentBuilder();
      db.setErrorHandler(new weakErrorHandler());
      Document doc = db.parse(file);
      doc.getDocumentElement().normalize();
      DOM = doc;
      NodeList nodeLst = doc.getDocumentElement().getChildNodes();
      getNetworkInfo(nodeLst);
    } catch (SAXException e) {
      System.out.println("Got " + e.getMessage());
      returnVal.str = e.getMessage();
      return returnVal;
    } catch (UnknownHostException e) {
      returnVal.str = e.getMessage();
      return returnVal;
    } catch (Exception e) {
      System.out.println("Got " + e.getMessage());
      returnVal.str = e.getMessage();
      return returnVal;
    }
    built = true;
    printNet();
    returnVal.bool = true;
    returnVal.str = "Successful";
    return (returnVal);
  }

  // ----------------------------------------------------------------------
  // ---------------------ADJUST DOM FUNCTIONS-----------------------------
  public void prettyPrint(OutputStream out)
      throws TransformerConfigurationException,
      TransformerFactoryConfigurationError, TransformerException {
    Node xml = DOM.getDocumentElement();
    Transformer tf = TransformerFactory.newInstance().newTransformer();
    tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    tf.setOutputProperty(OutputKeys.INDENT, "yes");
    tf.transform(new DOMSource(xml), new StreamResult(out));
  }

  // -----------------------------------------------------------
  public boolean addUUIDToDOM(String switchUUID, int portBit, String portUUID,
      AccessTypes type) throws Exception {
    NodeList nl = DOM.getDocumentElement().getChildNodes();
    for (int i = 0; i < nl.getLength(); i++) {
      Node n = nl.item(i);
      if (n.getNodeType() != Node.ELEMENT_NODE)
        continue;
      // if it's not an aggregate, do nothing
      if (!(n.getNodeName().equals("aggregate")))
        continue;
      NodeList aggregateNl = n.getChildNodes();
      for (int j = 0; j < aggregateNl.getLength(); j++) {
        Node nn = aggregateNl.item(j);
        if (nn.getNodeName().equals("switch")
            && nodeUUIDMatches(switchUUID, nn.getChildNodes())) {
          NodeList switchNl = nn.getChildNodes();
          for (int k = 0; k < switchNl.getLength(); k++) {
            Node nnn = switchNl.item(k);
            // only care about ports
            if (!(nnn.getNodeName().equals("Port")))
              continue;
            NodeList portNl = nnn.getChildNodes();
            boolean found = false;
            for (int l = 0; l < portNl.getLength(); l++) {
              Node nnnn = portNl.item(l);
              if (found == true && nnnn.getNodeName().equals("uuid")
                  || nnnn.getNodeName().equals("MacAddress")) {
                Element portUUIDNode = DOM.createElement("uuid");
                Text portUUIDText = DOM.createTextNode(portUUID);
                portUUIDNode.appendChild(portUUIDText);
                nnn.insertBefore(portUUIDNode, nnnn);
                return true;
              }
              try {
                if (nnnn.getNodeName().equals("portBit")) {
                  int compVal =
                      Integer.valueOf(nnnn.getFirstChild().getNodeValue());
                  if (portBit == compVal)
                    found = true;
                  else
                    break;
                }
              } catch (IllegalArgumentException e) {
                throw new Exception(e.getMessage());
              }
            }
          }
          DocumentFragment fragment = DOM.createDocumentFragment();
          Element portNode, portBitNode, portUUIDNode, portAccessTypeNode;
          portBitNode = DOM.createElement("portBit");
          portUUIDNode = DOM.createElement("uuid");
          portAccessTypeNode = DOM.createElement("type");
          portNode = DOM.createElement("Port");
          Text portBitText = DOM.createTextNode(String.valueOf(portBit));
          portBitNode.appendChild(portBitText);
          Text portUUIDText = DOM.createTextNode(portUUID);
          portUUIDNode.appendChild(portUUIDText);
          Text portAccessTypeText;
          if (type.equals(AccessTypes.ALL))
            portAccessTypeText = DOM.createTextNode("ALL");
          else if (type.equals(AccessTypes.MULTIPLE))
            portAccessTypeText = DOM.createTextNode("MULTIPLE");
          else if (type.equals(AccessTypes.NOBROADCAST))
            portAccessTypeText = DOM.createTextNode("NOBROADCAST");
          else
            portAccessTypeText = DOM.createTextNode("UNDEFINED");
          portAccessTypeNode.appendChild(portAccessTypeText);
          portNode.appendChild(portBitNode);
          portNode.appendChild(portUUIDNode);
          portNode.appendChild(portAccessTypeNode);
          fragment.appendChild(portNode);
          nn.appendChild(fragment);
          return (true);
        }
      }// end for(int j=0;)
    }// end for (int i=0;)
    return (false);
  }

  // -----------------------------------------------------------
  public boolean removeUUIDFromDOM(String uuid) {
    boolean returnVal = false;
    NodeList nl = DOM.getDocumentElement().getChildNodes();
    for (int i = 0; i < nl.getLength(); i++) {
      Node n = nl.item(i);
      if (n.getNodeType() != Node.ELEMENT_NODE)
        continue;
      // if it's not an aggregate, continue
      if (!(n.getNodeName().equals("aggregate")))
        continue;
      NodeList aggregateNl = n.getChildNodes();
      for (int j = 0; j < aggregateNl.getLength(); j++) {
        Node nn = aggregateNl.item(j);
        if (nn.getNodeName().equals("switch")) {
          NodeList switchNl = nn.getChildNodes();
          for (int k = 0; k < switchNl.getLength(); k++) {
            Node nnn = switchNl.item(k);
            if (nnn.getNodeName().equals("Port") /*
                                                  * && nodeUUIDMatches(uuid,
                                                  * nnn.getChildNodes())
                                                  */) {
              NodeList portNl = nnn.getChildNodes();
              for (int l = 0; l < portNl.getLength(); l++) {
                Node nnnn = portNl.item(l);
                if (nnnn.getNodeName().equals("uuid")) {
                  if (uuid.equals(nnnn.getFirstChild().getNodeValue())) {
                    nnn.removeChild(nnnn);
                    l--;
                  }
                }
              } // end for (int l=0;)
              // nn.removeChild(nnn);
              returnVal = true;
            }
          } // end (for int k=0;)
        } // end(if nn.getNodeName().equals("switch")
      } // end for(int j=0;)
    } // end for(int i=0;)
    return returnVal;
  }

  // -----------------------------------------------------------
  public void addMacToDescription(String switchUUID, int portBit,
      byte[] macAddress, int interfaceIndex) throws Exception {
    UUID id = UUID.fromString(switchUUID);
    for (int i = 0; i < switchIndex; i++) {
      if (!(switches[i].switchUUID.equals(id)))
        continue;
      if (portBit >= switches[i].numPorts)
        throw new Exception(
            "Error, port bit is greater than number of ports for "
                + "the requested switch");
      if (!switches[i].ports[portBit].valid)
        throw new Exception("Error, specified port has not been initialized");
      switches[i].ports[portBit].macAddress = macAddress;
      switches[i].ports[portBit].interfaceIndex = interfaceIndex;
      switches[i].sendMacAddress(portBit);
      return;
    }
    throw new Exception("Error, was unable to find requested switch");
  }

  // -----------------------------------------------------------
  public void addMacToDOM(String switchUUID, int portBit, String macAddress,
      int interfaceIndex) throws Exception {
    NodeList switchNl = getSwitchNodeList(switchUUID);
    for (int k = 0; k < switchNl.getLength(); k++) {
      Node n = switchNl.item(k);
      // only care about ports
      if (!(n.getNodeName().equals("Port")))
        continue;
      NodeList portNl = n.getChildNodes();
      boolean found = false;
      for (int l = 0; l < portNl.getLength(); l++) {
        Node nn = portNl.item(l);
        if (found == true && nn.getNodeName().equals("MacAddress")) {
          n.removeChild(nn);
          break;
        }
        try {
          if (nn.getNodeName().equals("portBit")) {
            int compVal = Integer.valueOf(nn.getFirstChild().getNodeValue());
            if (portBit == compVal)
              found = true;
            else
              break;
          }
        } catch (IllegalArgumentException e) {
          throw new Exception(e.getMessage());
        }
      } // end (for l = 0)
      if (found) {
        Element macAddressNode = DOM.createElement("MacAddress");
        Element MACNode = DOM.createElement("MAC");
        Text macAddressText = DOM.createTextNode(macAddress);
        MACNode.appendChild(macAddressText);
        Element interfaceIndexNode = DOM.createElement("interfaceIndex");
        Text interfaceIndexText =
            DOM.createTextNode(String.valueOf(interfaceIndex));
        interfaceIndexNode.appendChild(interfaceIndexText);
        macAddressNode.appendChild(MACNode);
        macAddressNode.appendChild(interfaceIndexNode);
        n.appendChild(macAddressNode);
        return;
      }
    }
    throw new Exception(
        "Error, could not find specified switch/port combination");
  }

  // -----------------------------------------------------------
  public void removeMacFromDescription(String switchUUID, int portBit)
      throws Exception {
    UUID id = UUID.fromString(switchUUID);
    for (int i = 0; i < switchIndex; i++) {
      if (!(switches[i].switchUUID.equals(id)))
        continue;
      if (portBit >= switches[i].numPorts)
        throw new Exception(
            "Error, port bit is greater than number of ports for "
                + "the requested switch");
      if (!switches[i].ports[portBit].valid)
        throw new Exception("Error, specified port has not been initialized");
      switches[i].ports[portBit].clearMac();
      switches[i].ports[portBit].interfaceIndex = 0;
      switches[i].freeMacAddress(portBit);
      return;
    }
    throw new Exception("Error, was unable to find requested switch");
  }

  // -----------------------------------------------------------
  public void removeMacFromDOM(String switchUUID, int portBit)
      throws Exception {
    NodeList switchNl = getSwitchNodeList(switchUUID);
    for (int i = 0; i < switchNl.getLength(); i++) {
      Node n = switchNl.item(i);
      // only care about ports
      if (!(n.getNodeName().equals("Port")))
        continue;
      NodeList portNl = n.getChildNodes();
      boolean found = false;
      for (int j = 0; j < portNl.getLength(); j++) {
        Node nn = portNl.item(j);
        if (found == true && nn.getNodeName().equals("MacAddress")) {
          n.removeChild(nn);
          break;
        }
        try {
          if (nn.getNodeName().equals("portBit")) {
            int compVal = Integer.valueOf(nn.getFirstChild().getNodeValue());
            if (portBit == compVal)
              found = true;
            else
              break;
          }
        } catch (IllegalArgumentException e) {
          throw new Exception(e.getMessage());
        }
      } // end (for j = 0)
    } // end (for i = 0)
  }

  // -----------------------------------------------------------
  public BoolString removeUUIDFromDescription(String id)
      throws GenericException {
    BoolString returnVal = new BoolString();
    UUID uuid;
    returnVal.bool = false;
    try {
      uuid = UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      returnVal.str = "Error, invalid uuid: " + e.getMessage();
      return returnVal;
    }
    for (int i = 0; i < switchIndex; i++) {
      switches[i].clearUUIDVlans(id);
      for (int j = 0; j < switches[i].numPorts; j++) {
        switches[i].ports[j].removeUUID(id);
      }
    }
    returnVal.bool = true;
    returnVal.str = "Successfully cleared uuid " + id;
    return returnVal;
  }

  // -----------------------------------------------------------
  public BoolString addUUIDToDescription(String switchUUID, int portBit,
      String portUUID, AccessTypes type) {
    UUID uuid, pUUID;
    BoolString returnVal = new BoolString();
    returnVal.bool = false;
    try {
      uuid = UUID.fromString(switchUUID);
      pUUID = UUID.fromString(portUUID);
    } catch (IllegalArgumentException e) {
      returnVal.str = "Error, invalid uuid: " + e.getMessage();
      return returnVal;
    }
    for (int i = 0; i < switchIndex; i++) {
      // found requested switch
      if (switches[i].switchUUID.equals(uuid)) {
        if (portBit < 0 || portBit >= switches[i].numPorts) {
          returnVal.str =
              "Error, port bit must be between 0 and "
                  + (switches[i].numPorts - 1);
          return returnVal;
        } else {
          switches[i].ports[portBit].addUUID(portUUID, type);
          if (type == AccessTypes.MULTIPLE) {
            switches[i].addBroadcasts();
          } else if (type == AccessTypes.ALL) {
            // for each switch
            for (int j = 0; j < switchIndex; j++) {
              // for each port on each switch
              for (int k = 0; k < switches[j].numPorts; k++) {
                // for each vlan id
                for (int l = 0; l < endVlanId; l++)
                  // if the vlan exists on this port
                  if (switches[j].ports[k].isOnVlan(l) || isBroadcast(j)) {
                    try {
                      boolean rawEth = ipAddressList.isRawEthernet(l);
                      switches[i].addVlanToPort(pUUID, l, rawEth);
                    } catch (GenericException e) {
                    }
                  }
              }
            }
          }
          returnVal.bool = true;
          returnVal.str = "Successfully added uuid";
          return returnVal;
        }
      }
    }
    returnVal.str = "Unable to find requested switch";
    return returnVal;
  }

  // ----------------------------------------------------------------------
  // ------------------------UTILITY FUNCTIONS-----------------------------
  public static byte[] macFromString(String strMac) throws Exception {
    byte[] mac = new byte[6];
    String substr;
    strMac = strMac.toLowerCase();
    if (strMac.length() < 17)
      throw new Exception("Error, length of mac string is too short");
    for (int i = 0; i < 6; i++) {
      if (i != 5) {
        substr = strMac.substring(3 * i, 3 * i + 3);
        if (substr.charAt(2) != ':')
          throw new Exception("Error, mac address has invalid format");
      } else {
        substr = strMac.substring(3 * i, 3 * i + 2);
      }
      try {
        mac[i] =
            (byte) (charToByte(substr.charAt(0)) * 16 + charToByte(substr
                .charAt(1)));
      } catch (Exception e) {
        throw new Exception("Error, mac address has invalid format");
      }
    }
    return mac;
  }

  // ----------------------------------------------------------------------
  private static byte charToByte(char c) throws Exception {
    switch (c) {
    case '0':
    case '1':
    case '2':
    case '3':
    case '4':
    case '5':
    case '6':
    case '7':
    case '8':
    case '9':
      return ((byte) (c - '0'));
    case 'a':
    case 'b':
    case 'c':
    case 'd':
    case 'e':
    case 'f':
      return ((byte) (c - 'a' + 10));
    default:
      throw new Exception("Error, received invalid character");
    }
  }

  // -----------------------------------------------------------
  private boolean nodeUUIDMatches(String uuid, NodeList nl) {
    for (int i = 0; i < nl.getLength(); i++) {
      Node n = nl.item(i);
      if (n.getNodeName().equals("uuid")) {
        return (uuid.equals(n.getFirstChild().getNodeValue()));
      }
    } // end for (int i=0;)
    // could not find uuid tag
    return false;
  }

  // -----------------------------------------------------------
  private NodeList getSwitchNodeList(String switchUUID) throws Exception {
    NodeList nl = DOM.getDocumentElement().getChildNodes();
    for (int i = 0; i < nl.getLength(); i++) {
      Node n = nl.item(i);
      if (n.getNodeType() != Node.ELEMENT_NODE)
        continue;
      // if it's not an aggregate, do nothing
      if (!(n.getNodeName().equals("aggregate")))
        continue;
      NodeList aggregateNl = n.getChildNodes();
      for (int j = 0; j < aggregateNl.getLength(); j++) {
        Node nn = aggregateNl.item(j);
        if (nn.getNodeName().equals("switch")
            && nodeUUIDMatches(switchUUID, nn.getChildNodes())) {
          return (nn.getChildNodes());
        }
      }// end for(int j=0;)
    }// end for (int i=0;)
    throw new Exception("Error, switch not found");
  }

  public boolean clearUUIDVlans(UUID u) throws GenericException {
    boolean returnVal = true;
    ipAddressList.clearUUID(u);
    for (int i = 0; i < switchIndex; i++) {
      if (!switches[i].clearUUIDVlans(u))
        returnVal = false;
    }
    return returnVal;
  }

  public boolean addVlanToPort(String UUID, int vlanId)
      throws GenericException {
    return addVlanToPort(UUID, vlanId, -1);
  }
}