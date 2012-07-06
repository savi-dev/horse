// Copyright (C) 2012, The SAVI Project.
package ca.savi.horse.hardware;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.zip.CRC32;

import javax.ws.rs.core.UriBuilder;

import ca.savi.aor.hardware.BroadcastListener;
import ca.savi.aor.hardware.HardwareNode;
import ca.savi.aor.hardware.HardwareUnit;
import ca.savi.aor.hardware.InterchipNode;
import ca.savi.aor.hardware.Message;
import ca.savi.aor.hardware.MessageFactory;
import ca.savi.aor.hardware.Node;
import ca.savi.aor.messageexceptions.GenericException;
import ca.savi.glance.client.GlanceClient;
import ca.savi.glance.model.GlanceClientDownloadRequest;
import ca.savi.glance.model.GlanceClientDownloadResponse;
import ca.savi.horse.model.Hwnode;
import ca.savi.horse.model.hardware.AddVlanRequest;
import ca.savi.horse.model.hardware.AddVlanResponse;
import ca.savi.horse.model.hardware.InitResponse;
import ca.savi.horse.model.hardware.ProgramResourceRequest;
import ca.savi.horse.model.hardware.ProgramResourceResponse;
import ca.savi.horse.model.hardware.RemoveVlanRequest;
import ca.savi.horse.model.hardware.RemoveVlanResponse;
import ca.savi.horse.model.hardware.ResourceGetRequest;
import ca.savi.horse.model.hardware.ResourceGetResponse;
import ca.savi.horse.model.hardware.ResourceListRequest;
import ca.savi.horse.model.hardware.ResourceListResponse;
import ca.savi.horse.model.hardware.ResourceReleaseRequest;
import ca.savi.horse.model.hardware.ResourceReleaseResponse;
import ca.savi.horse.model.hardware.ResourceResetRequest;
import ca.savi.horse.model.hardware.ResourceResetResponse;
import ca.savi.horse.model.hardware.ResourceStatusRequest;
import ca.savi.horse.model.hardware.ResourceStatusResponse;
import ca.savi.horse.model.hardware.SetResourceParametersRequest;
import ca.savi.horse.model.hardware.SetResourceParametersResponse;
import ca.savi.horse.model.hardware.TerminateResponse;
import ca.savi.horse.model.hardware.UserRegisterInteractionRequest;
import ca.savi.horse.model.hardware.UserRegisterInteractionResponse;

/**
 * This is AORHardware.
 * @author Hesam, Rahimi Koopayi <hesam.rahimikoopayi@utoronto.ca>
 * @author Jie Yu Lin (Eric) <jieyu.lin@mail.utoronto.ca>
 * @version 0.1
 */
public class AORHardware {
  List<HardwareUnit> h;
  int DefaultPort;
  boolean connected;
  // these three items are used to create the selector to periodically listen
  // on the datagram socket for broadcasting subagents
  DatagramChannel dc;
  SelectionKey sk;
  Selector selector;
  boolean proceed = true;
  ResourceBundle rb;
  Timer ts, tc, tr;
  BroadcastListener bl;

  public AORHardware() {
    // true if only connected to all servers in .subagents file
    // (except those marked Subagent.Connect=false)
    connected = false;
  }

  @Override
  protected final void finalize() {
    bl.interrupt();
    try {
      super.finalize();
    } catch (Throwable e) {
    }
  }

  // from http://mindprod.com/jgloss/hex.html
  private static int charToNibble(char c) {
    if ('0' <= c && c <= '9') {
      return c - '0';
    } else if ('a' <= c && c <= 'f') {
      return c - 'a' + 0xa;
    } else if ('A' <= c && c <= 'F') {
      return c - 'A' + 0xa;
    } else {
      throw new IllegalArgumentException("Invalid hex character: " + c);
    }
  }

  // from http://mindprod.com/jgloss/hex.html
  public static byte[] fromHexString(String s) {
    int stringLength = s.length();
    if ((stringLength & 0x1) != 0) {
      throw new IllegalArgumentException(
          "fromHexString requires an even number of hex characters");
    }
    byte[] b = new byte[stringLength / 2];
    for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
      int high = charToNibble(s.charAt(i));
      int low = charToNibble(s.charAt(i + 1));
      b[j] = (byte) ((high << 4) | low);
    }
    return b;
  }

  // initialization request
  public InitResponse init(java.lang.Object m) {
    InitResponse r = new InitResponse();
    r.setSuccessful(false);
    rb = ResourceBundle.getBundle("AORService.Resources.properties");
    System.out.println("Starting Hardware Service");
    // read property file
    Properties p = new Properties();
    try {
      p.load(new FileInputStream((new File("resources").getAbsolutePath()
          + File.separator + rb.getString("SubagentsFile"))));
    } catch (Exception e) {
      r.setValue("error reading settings file!");
      return r;
    }
    // get list of subagents, delimited by comma
    String[] hwservers = p.getProperty("Hardware.Subagents").split(",");
    // get all the hardware subagents
    h = new LinkedList<HardwareUnit>();
    DefaultPort =
        Integer.valueOf(p.getProperty("Hardware.DefaultPort", "51001"));
    // iterate through each one
    for (String hw : hwservers) {
      // create the unit
      HardwareUnit hu = new HardwareUnit();
      System.out.println("Getting connect property of " + hw);
      // if we shouldn't connect to the unit, just keep going
      if (p.getProperty(hw + ".Connect").equals("false")) {
        continue;
      }
      // address
      hu.name = hw;
      System.out.println("Getting Address property of " + hw);
      String[] h_addr = p.getProperty(hw + ".Address").split(":");
      // try to get the inetaddress; if it cannot be reached, return an
      // error
      try {
        hu.host = InetAddress.getByName(h_addr[0]);
      } catch (UnknownHostException e) {
        System.out.println("error: " + e.getMessage());
        r.setValue(e.getMessage());
        return r;
      }
      // was a port defined as part of the address?
      hu.port = (h_addr.length >= 2 ? Integer.valueOf(h_addr[1]) : DefaultPort);
      System.out.println("server at " + hu.host.toString() + ":" + hu.port);
      // register
      try {
        hu.sock = new Socket(hu.host, hu.port);
        // timeout after 30 seconds of blocking
        hu.sock.setSoTimeout(30000);
        synchronized (hu.sock) {
          // initialize first
          MessageFactory.initRequest(hu.sock);
          MessageFactory.initResponse(hu.sock);
          // then register
          MessageFactory.registerRequest(hu.sock);
          // hardwareRegisterResponse instantiates the objects inside a
          // hardware unit
          hu = MessageFactory.hardwareRegisterResponse(hu.sock, hu);
        }
      } catch (Exception e) {
        r.setValue("error during registration: " + e.getMessage());
        return r;
      }
      // debug output
      System.out.println("Server " + hu.name + " (" + hu.host.getHostAddress()
          + ":" + hu.port + ") initialized with the following resources:");
      for (HardwareNode hn : hu.hwNode)
        System.out.println("hw node " + hn.name + ": uuid "
            + hn.uuid.toString());
      for (InterchipNode ic : hu.icNode)
        System.out.println("ic node " + ic.name + ": uuid "
            + ic.uuid.toString() + ", linking "
            + hu.getNodeFromUUID(ic.link[0]).name + " and "
            + hu.getNodeFromUUID(ic.link[1]).name);
      // end debug output
      h.add(hu);
    }
    // connect is set to true ONLY IF all subagents were connected
    // except those with Subagent.Connect = false
    connected = true;
    bl = new BroadcastListener(41001, h);
    bl.start();
    // start clean timer
    tc = new Timer();
    tc.scheduleAtFixedRate(new TimerTask() { // overload TimerTask to run the
                                             // cleanResources method
          public void run() {
            cleanResources();
          }
        }, 0, // start after 0 milliseconds
        1000); // period is 1000 milliseconds
    // start reset timer
    tr = new Timer();
    tr.scheduleAtFixedRate(new TimerTask() { // overload TimerTask to run the
                                             // cleanResources method
          public void run() {
            // resetUnresponsiveNodes();
          }
        }, 60000, // start after 60000 milliseconds
        60000); // period is 60000 milliseconds
    r.setSuccessful(true);
    r.setValue("Successful.");
    return r;
  }

  // resource list request
  public ResourceListResponse resourceList(ResourceListRequest m) {
    ResourceListResponse r = new ResourceListResponse();
    r.setVerbose(m.isVerbose());
    int current = 0;
    int maximum = 0;
    // iterate through all hardware units
    for (HardwareUnit u : h) {
      // null when the unit isn't instantiated (unit.Connect = false)
      if (u == null)
        continue;
      if (u.name == null)
        continue;
      maximum += u.hwNode.length;
      // iterate through all hardware nodes
      for (HardwareNode n : u.hwNode) {
        // if available
        if (!(n.inUse))
          current++;
        // if verbosity is set, all the details are returned
        if (m.isVerbose()) {
          // create the node and fill in the information
          Hwnode hn = new Hwnode();
          hn.setUuid(n.uuid.toString());
          hn.setName(n.name);
          hn.setTimestamp(BigInteger.valueOf(n.timestamp));
          hn.setDuration(BigInteger.valueOf(n.duration));
          hn.setInUse(n.inUse);
          hn.setLeftUuid(n.leftUUID.toString());
          hn.setRightUuid(n.rightUUID.toString());
          // add the node to the list
          r.getHwnode().add(hn);
        }
      }
      // if not verbose, interchips are ignored
      // if (m.isVerbose ())
      // for (InterchipNode n : u.icNode) {
      // create the node and fill in the information
      // ResourceListResponse.Icnode in = new ResourceListResponse.Icnode ();
      // in.setUuid (n.uuid.toString ());
      // in.setName (n.name);
      // in.setTimestamp (BigInteger.valueOf (n.timestamp));
      // in.setDuration (BigInteger.valueOf (n.duration));
      // in.setInUse (n.inUse);
      // in.setLinkLeft (n.link [0].toString ());
      // in.setLinkRight (n.link [1].toString ());
      // in.setLRWidth (Integer.valueOf (n.width [0]));
      // in.setRLWidth (Integer.valueOf (n.width [1]));
      //
      // add the node to the list
      // r.getIcnode ().add (in);
      // }
    }
    // current and maximum values
    r.setNumberUsed(BigInteger.valueOf(current));
    r.setTotalResources(BigInteger.valueOf(maximum));
    return r;
  }

  // resource get request
  // it's really two operations rolled into one. We explain each one here...
  // If uuid is specified in m:
  // then we try to get that particular resource, and return the uuid of that
  // resource inside the uuid of the response. Fails if the uuid is malformed,
  // if the uuid does not correspond to any resources, or if the uuid points
  // to an interchip and NOT both its links have been acquired.
  // if uuid is NOT specified in m:
  // now we do something I like to call "autoGet". The request message
  // specifies a topology of FPGA resources. Each FPGA element in the SOAP
  // request has two parameters:
  // name - optional, if not specified then it's not linkable
  // link - optional, if specified then the FPGA links to the specified FPGA
  // and the response does not set the uuid in the top element, but instead
  // specifies a group of FPGA elements, which are name-uuid pairs.
  public ResourceGetResponse resourceGet(ResourceGetRequest m) {
    ResourceGetResponse r = new ResourceGetResponse();
    r.setSuccessful(false);
    UUID uuid;
    HardwareUnit u = null;
    Node n = null;
    // if uuid is blank, use autoGet
    if (m.getUuid() == null || m.getUuid().equals("")) {
      // no topology specified
      // if (m.getFPGA ().size () == 0) {
      // r.setUuid ("");
      // r.setValue ("No uuid or topology specified!");
      // return r;
      // }
      // try {
      // autoGet returns a list of ResourceGetResponse.FPGA objects
      // given a list of ResourceGetRequest.FPGA objects
      // List<FPGA> f = autoGet (m.getFPGA (), m.getDuration ().longValue () *
      // 60000);
      // move the list to r
      // for (FPGA fpga : f)
      // r.getFPGA ().add (fpga);
      // uuid is set to blank for autoGet since the node UUIDs are
      // carried in the getFPGA list
      // r.setUuid ("");
      // r.setSuccessful(true);
      // r.setValue ("Successful.");
      // return r;
      // exceptions could happen when the topology is invalid or it cannot
      // be fulfilled
      // }
      // catch (GenericException e) {
      // r.setUuid ("");
      // r.setValue (e.getMessage ());
      // return r;
      // }
    }
    // NOT AUTOGET, so uuid is specified
    // cna't specify both uuid and topology
    // if (m.getFPGA ().size () > 0) {
    // r.setUuid ("");
    // r.setValue ("Cannot specify both UUID and topology!");
    // return r;
    // }
    // make sure the UUID we get is not malformed
    try {
      uuid = UUID.fromString(m.getUuid());
    } catch (IllegalArgumentException e) {
      r.setUuid("");
      r.setValue(e.getMessage());
      return r;
    }
    // find the unit corresponding to the uuid (search through all units)
    for (HardwareUnit unit : h) {
      Node node = unit.getNodeFromUUID(uuid);
      // if it cannot be found in this unit, null is returned by
      // getNodeFromUUID
      if (node == null)
        continue;
      u = unit;
      n = node;
      break;
    }
    // can't find the uuid in any unit
    if (n == null) {
      r.setUuid("");
      r.setValue("Supplied UUID (" + m.getUuid()
          + ") does not correspond to any nodes");
      return r;
    }
    // found a hardware node
    if (n instanceof HardwareNode) {
      // set or refresh the duration
      n.timestamp = System.currentTimeMillis();
      n.duration = m.getDuration().longValue() * 60000;
      n.inUse = true;
      // found an interchip node
    } else if (n instanceof InterchipNode) {
      InterchipNode in = (InterchipNode) n;
      // set or refresh the duration
      n.timestamp = System.currentTimeMillis();
      n.duration = m.getDuration().longValue() * 60000;
      if (!n.inUse) {
        // both FPGA nodes must be acquired before the interchip node
        // can be acquired
        if (!(u.getNodeFromUUID(in.link[0]).inUse && u
            .getNodeFromUUID(in.link[1]).inUse)) {
          r.setUuid("");
          r.setValue("The interchip requested (" + u.name
              + ") does not have both linked FPGA nodes ("
              + u.getNodeFromUUID(in.link[0]).name + ", "
              + u.getNodeFromUUID(in.link[1]).name + ") acquired.");
          return r;
        }
        // interchip widths, not currently in use
        // check for width correctness
        // both widths must be >= 0
        if (in.width[0] < 0 || in.width[1] < 0) {
          r.setUuid("");
          r.setValue("Negative widths requested for interchip " + u.name + ".");
          return r;
        }
        // sum of widths cannot exceed 128
        if (in.width[0] + in.width[1] > 128) {
          r.setUuid("");
          r.setValue("The widths requested (" + in.width[0] + ", "
              + in.width[1] + ") for interchip " + u.name
              + " are too large. The sum cannot exceed 128.");
          return r;
        }
        n.inUse = true;
        // in.width [0] = m.getLRWidth ();
        // in.width [1] = m.getRLWidth ();
      }
    }
    r.setUuid(n.uuid.toString());
    r.setValue("Successful.");
    r.setSuccessful(true);
    return r;
  }

  // -- BEGIN AUTOGET --
  // the helper class
  class AGNode {
    public String name;
    // public UUID uuid;
    public AGNode[] link;
    public HardwareNode t;
  }

  // the main autoGet function
  /*
   * private List<FPGA> autoGet (List<aor.xsd.hardware.ResourceGetRequest.FPGA>
   * f, long duration) throws GenericException { // find the units where this
   * topology /might/ be satisfiable System.out.println
   * ("Initiating autoGet..."); System.out.println ("Topology size: " + f.size
   * ()); // p [i] is the satisfiability of the ith hardwareUnit int hs = h.size
   * (); boolean [] p = new boolean [hs]; boolean pf = false; // for each
   * hardwareUnit, check the satisfiability // it's important that we do NOT use
   * the forEach notation here // since we cannot predict if h will be added to
   * during autoGet // so we must limit our index to the BEGINNING value for
   * (int i = 0; i < hs; i ++) { // uninitialized hardwareUnit if (h.get (i) ==
   * null || h.get (i).hwNode == null) continue; int count = 0; // count the
   * number of free nodes on i for (int j = 0; j < h.get (i).hwNode.length; j
   * ++) if (!h.get (i).hwNode [j].inUse) count ++; // boolean indicating
   * whether the count exceeds the requirement p [i] = (count >= f.size ()); //
   * debug output if (p [i]) System.out.println (h.get (i).name +
   * " can potentially satisfy the topology."); // if any of the booleans are
   * true then pf will be true pf = p [i] || pf; } // no hardwareUnit can
   * satisfy the topology if (!pf) throw new GenericException
   * ("No subagent has enough free FPGAs to accommodate the topology."); // cast
   * the structure to AGNodes AGNode [] n = new AGNode [f.size ()]; HashMap
   * <String, AGNode> m = new HashMap <String, AGNode> (); for (int i = 0; i <
   * n.length; i ++) { n [i] = new AGNode (); if (f.get (i).getName () != null)
   * { n [i].name = f.get (i).getName (); // add the name to the hashmap, if
   * possible if (m.containsKey (n [i].name)) throw new GenericException
   * ("Same name assigned to different FPGAs."); m.put (n [i].name, n [i]); }
   * else n [i].name = null; } // assign names to nodes with empty names int j =
   * 1; for (int i = 0; i < n.length; i ++) { if (n [i].name == null) { while
   * (m.containsKey ("AG-NODE-" + j)) j ++; n [i].name = "AG-NODE-" + j; } } //
   * create the links for (int i = 0; i < n.length; i ++) { // if there are no
   * links from this FPGA, skip it List<String> l = f.get (i).getLink (); if (l
   * == null || l.size () == 0) continue; n [i].link = new AGNode [l.size ()];
   * int c = 0; // for each link for (String link : l) { // find the node
   * referred to by the link if (m.containsKey (link)) { n [i].link [c] = m.get
   * (link); c ++; } else throw new GenericException
   * ("There is no node with name " + link + ", which node " + n [i].name +
   * " references as a link."); } } // these lists carry the resultant
   * assignments through the recursive // process // list of name-node
   * correspondences LinkedList <HashMap <String, HardwareNode>> l = new
   * LinkedList <HashMap <String, HardwareNode>> (); // list of hardwareUnits on
   * which the corresponding hashmaps are set LinkedList <HardwareUnit> lhu =
   * new LinkedList <HardwareUnit> (); // list of interchips corresponding to
   * the hashmaps LinkedList <Set <InterchipNode>> lic = new LinkedList <Set
   * <InterchipNode>> (); // recursively find all possible configurations and
   * store them in the // linked lists for (int i = 0; i < hs; i ++) if (p [i])
   * autoGetRecurse (n, 0, h.get (i), l, lhu, lic); System.out.println
   * ("autoGetRecurse terminated with " + l.size () + " results."); // no
   * results? if (l.size () == 0) throw new GenericException
   * ("There are no assignments that satisfy the topology."); // now we have a
   * list of possible assignments - we need to find the best // one int maxindex
   * = autoGetRank (l, lhu); // print results System.out.println
   * ("Ranker result: "); for (Entry <String, HardwareNode> e : l.get
   * (maxindex).entrySet ()) System.out.println ("Node " + e.getKey () +
   * " assigned to " + e.getValue ().name); // now we have the best assignment,
   * place it for (HardwareNode hn : l.get (maxindex).values ()) { hn.timestamp
   * = System.currentTimeMillis (); hn.duration = duration; hn.inUse = true; }
   * for (InterchipNode in : lic.get (maxindex)) { in.timestamp =
   * System.currentTimeMillis (); in.duration = duration; in.width [0] = 64;
   * in.width [1] = 64; in.inUse = true; } // cast it to the structure required
   * by the response, and return it LinkedList<FPGA> lf = new LinkedList<FPGA>
   * (); for (Entry <String, HardwareNode> e : l.get (maxindex).entrySet ()) {
   * FPGA fpga = new FPGA (); // the hashmap is a name-node pair, so the name is
   * the key and the // node is the value fpga.setName (e.getKey ());
   * fpga.setUuid (e.getValue ().uuid.toString ()); // add the fpga to the list
   * lf.add (fpga); } // return the list return lf; }
   */
  // the recursive function that tests all possible assignments for the nodes
  // the algorithm:
  // - each iteration of the function makes one assignment. Thus, the level
  // of recursion is determined by the number of nodes in the topology.
  // - after making one assignment, it checks to make sure the current
  // topology is still feasible (i.e. no placing nodes that should be
  // connected on diagonal corners).
  // - when a topology is completely assigned, it writes the assignment into
  // the linked list, and tries the next assignment.
  private void autoGetRecurse(AGNode[] n, int index, HardwareUnit u,
      List<HashMap<String, HardwareNode>> l, List<HardwareUnit> lhu,
      List<Set<InterchipNode>> lic) {
    // print the current recursion level
    // in each level we assign one node
    System.out.println("autoGetRecurse index = " + index);
    // loop through all the possible assignments at this level
    for (int i = 0; i < u.hwNode.length; i++) {
      // can't use a node that's already in use or acquired
      if (u.hwNode[i].inUse || u.hwNode[i].acquired)
        continue;
      // we have a node that's not in use
      n[index].t = u.hwNode[i];
      u.hwNode[i].acquired = true;
      // check whether, up to this point, the assignment is plausible
      // if the assignment is bad, just skip it
      if (!autoGetCheckTopo(n, u)) {
        System.out.println("autoGetCheckTopo failed, design tossed");
        n[index].t = null;
        u.hwNode[i].acquired = false;
        continue;
      }
      System.out.println("autoGetCheckTopo passed");
      // not all nodes have been assigned, then go onto the NEXT level of
      // recursion
      if (index < (n.length - 1)) {
        autoGetRecurse(n, index + 1, u, l, lhu, lic);
        // all nodes have been assigned (terminal case)
      } else {
        System.out.println("last level of recursion, writing assignment");
        // write the assignment
        HashMap<String, HardwareNode> m = new HashMap<String, HardwareNode>();
        for (AGNode node : n)
          m.put(node.name, node.t);
        HashSet<InterchipNode> ins = new HashSet<InterchipNode>();
        // check for all the links
        for (AGNode node : n)
          // if there are any links
          if (node.link != null)
            // iterate through all the links
            for (AGNode link : node.link)
              // find the interchip node that connects the two
              // nodes
              for (InterchipNode in : u.icNode)
                if ((in.link[0].equals(node.t.uuid) && in.link[1]
                    .equals(link.t.uuid))
                    || (in.link[0].equals(link.t.uuid) && in.link[1]
                        .equals(node.t.uuid)))
                  if (ins.add(in))
                    System.out.println("added interchip " + in.name + ".");
        l.add(m);
        lhu.add(u);
        lic.add(ins);
      }
      // unset the current assignment to prepare for the next
      n[index].t = null;
      u.hwNode[i].acquired = false;
    }
  }

  // returns whether the topology is potentially completable (i.e. whether
  // it contains no internal contradictions right now)
  private boolean autoGetCheckTopo(AGNode[] n, HardwareUnit u) {
    System.out.println("entering autoGetCheckTopo...");
    // debug print out
    for (int i = 0; i < n.length; i++) {
      System.out.println("node " + n[i].name + ":");
      System.out.println("  links: "
          + (n[i].link == null ? "none" : n[i].link.length));
      System.out.println("  assignment: "
          + (n[i].t == null ? "none" : n[i].t.name));
    }
    // iterate over all nodes (that have been assigned)
    for (int i = 0; i < n.length; i++) {
      // has not been assigned an fpga yet
      if (n[i].t == null)
        continue;
      // no links in the list, then nothing to check
      if (n[i].link == null)
        continue;
      // otherwise, iterate over all linked nodes
      for (int j = 0; j < n[i].link.length; j++) {
        // if the linked node hasn't been assigned, continue
        if (n[i].link[j].t == null)
          continue;
        // look for a link that connects the two nodes
        boolean haslink = false;
        for (InterchipNode in : u.icNode)
          if (haslink =
              haslink
                  || (in.link[0].equals(n[i].t.uuid) && in.link[1]
                      .equals(n[i].link[j].t.uuid))
                  || (in.link[1].equals(n[i].t.uuid) && in.link[0]
                      .equals(n[i].link[j].t.uuid)))
            break;
        if (!haslink)
          return false;
      }
    }
    return true;
  }

  // two ranking metrics:
  // 1. how many nodes are used up on the subagent after placing?
  // 2. how many links are free on the subagent after placing?
  // evaluate by metric 1 first, then if any assignments are tied, evaluate
  // by metric 2 to find the winner
  // return the index of that assignment
  private int autoGetRank(List<HashMap<String, HardwareNode>> l,
      List<HardwareUnit> lhu) {
    // class used for comparison and sorting
    class RankEntry implements Comparable<RankEntry> {
      public int index;
      public int rank;

      public int compareTo(RankEntry c) {
        return (rank - ((RankEntry) c).rank);
      }

      public RankEntry() {
        index = -1;
        rank = -1;
      }
    }
    System.out.println("Ranking " + l.size() + " topologies: ");
    RankEntry[] r = new RankEntry[l.size()];
    int maxrank = -1;
    for (int i = 0; i < l.size(); i++) {
      r[i] = new RankEntry();
      // set the assignment
      Collection<HardwareNode> hns = l.get(i).values();
      for (HardwareNode n : hns)
        n.acquired = true;
      // calculate the metrics for the assignment
      // metric 1.
      for (int j = 0; j < lhu.get(i).hwNode.length; j++)
        if (lhu.get(i).hwNode[j].inUse || lhu.get(i).hwNode[j].acquired)
          r[i].rank++;
      System.out.println("Index " + i + " has metric 1 rank " + r[i].rank);
      r[i].index = i;
      if (r[i].rank > maxrank)
        maxrank = r[i].rank;
      // unset the assignment
      for (HardwareNode n : hns)
        n.acquired = false;
    }
    // metric 2
    for (int i = 0; i < l.size(); i++) {
      if (r[i].rank == maxrank) {
        System.out.println("Index " + i + " qualifies for second metric");
        // set the assignment
        Collection<HardwareNode> hns = l.get(i).values();
        for (HardwareNode n : hns)
          n.acquired = true;
        // measure the rank
        r[i].rank += 4;
        for (int j = 0; j < lhu.get(i).icNode.length; j++)
          if (lhu.get(i).getNodeFromUUID(lhu.get(i).icNode[j].link[0]).inUse
              || lhu.get(i).getNodeFromUUID(
                  lhu.get(i).icNode[j].link[0]).acquired
              || lhu.get(i).getNodeFromUUID(lhu.get(i).icNode[j].link[1]).inUse
              || lhu.get(i).getNodeFromUUID(
                  lhu.get(i).icNode[j].link[1]).acquired)
            r[i].rank--;
        // unset the assignment
        for (HardwareNode n : hns)
          n.acquired = false;
      }
      System.out.println("Index " + i + " has composite rank " + r[i].rank);
    }
    // now sort based on the composite
    Arrays.sort(r);
    System.out.println("Returning index " + r[r.length - 1].index);
    return r[r.length - 1].index;
  }

  // -- END AUTOGET --
  // resource release request
  public ResourceReleaseResponse resourceRelease(ResourceReleaseRequest m) {
    ResourceReleaseResponse r = new ResourceReleaseResponse();
    UUID uuid;
    HardwareUnit u = null;
    Node n = null;
    r.setSuccessful(false);
    // make sure the UUID we get is not malformed
    try {
      uuid = UUID.fromString(m.getUuid());
    } catch (IllegalArgumentException e) {
      r.setValue(e.getMessage());
      return r;
    }
    // find the unit corresponding to the uuid (search through all units)
    for (HardwareUnit unit : h) {
      Node node = unit.getNodeFromUUID(uuid);
      if (node == null)
        continue;
      u = unit;
      n = node;
      break;
    }
    // can't find the uuid in any of the units
    if (n == null) {
      r.setValue("Supplied UUID (" + m.getUuid()
          + ") does not correspond to any nodes");
      return r;
    }
    // found a hardware node
    if (n instanceof HardwareNode) {
      // it's not in use!
      if (!n.inUse) {
        r.setValue("The FPGA to be released (" + n.name + ") is not in use.");
        return r;
      }
      // release it
      try {
        synchronized (u.sock) {
          MessageFactory.resourceReleaseRequest(u.sock, n.uuid);
          MessageFactory.resourceReleaseResponse(u.sock);
        }
        n.duration = 0;
        n.timestamp = 0;
        n.inUse = false;
      } catch (Exception e) {
        r.setValue(e.getMessage());
        return r;
      }
      // found the interchip node
    } else if (n instanceof InterchipNode) {
      InterchipNode in = (InterchipNode) n;
      // it's not in use!
      if (!in.inUse) {
        r.setValue("The interchip to be released (" + n.name
            + ") is not in use.");
        return r;
      }
      // it cannot be freed because both its linked FPGAs are in use
      if (u.getNodeFromUUID(in.link[0]).inUse
          && u.getNodeFromUUID(in.link[1]).inUse) {
        r.setValue("Cannot release interchip (" + n.name
            + ") because both its links (" + u.getNodeFromUUID(in.link[0]).name
            + ") and (" + u.getNodeFromUUID(in.link[1]).name + ") are in use.");
        return r;
      }
      // release it
      in.duration = 0;
      in.timestamp = 0;
      in.inUse = false;
      in.width[0] = 0;
      in.width[1] = 0;
    }
    r.setSuccessful(true);
    r.setValue("Successful.");
    return r;
  }

  // program a resource that's been acquired
  public ProgramResourceResponse programResource(ProgramResourceRequest m) {
    ProgramResourceResponse r = new ProgramResourceResponse();
    UUID uuid;
    HardwareUnit u = null;
    HardwareNode n = null;
    FileInputStream fin;
    File f;
    Socket tcps;
    OutputStream o;
    byte[] buf = new byte[1024];
    r.setSuccessful(false);
    int readlen = 0, tcpport = 0;
    // Get image from Glance
    String GlanceAuthToken = "";
    String hwUsername = "";
    String hwPassword = "";
    String hwTenant = "";
    String keystoneAddress = "";
    String GlanceServiceEndPoint = m.getServiceEndPoint();
    rb = ResourceBundle.getBundle("AORService.Resources.properties");
    Properties prop = new Properties();
    // Get the username and password for accessing the images
    try {
      prop.load(new FileInputStream(new File("resources").getAbsolutePath()
          + File.separator + rb.getString("glanceCredFile")));
      hwUsername = prop.getProperty("GlanceUsername");
      hwPassword = prop.getProperty("GlancePassword");
      hwTenant = prop.getProperty("GlanceTenant");
      keystoneAddress = prop.getProperty("KeystoneAddress");
    } catch (IOException e) {
      r.setValue("Error reading properties file: " + e);
      System.out.println("Error reading properties fie: " + e);
      return r;
    }
    /*
     * Get the token with the username and password KSDriver ksDriver =
     * KSDriver.getInstance(); ksDriver.setCredentialEndpoint("");
     * KSUserAuthResp ksUserAuthResp = ksDriver.authenticateUser( hwUsername,
     * hwPassword); if (ksUserAuthResp != null){ if (ksUserAuthResp.unathorized
     * != null){ r.setValue(
     * "Server Error: Error validating Hardware Resource Glance user" +
     * " Credential: " + ksUserAuthResp.unathorized.getMessage()); return r;
     * }else{ GlanceAuthToken = ksUserAuthResp.access.getToken().getId(); }
     * }else{ r.setValue(
     * "Server Error: Unable to validate Harware Resource Glance user " +
     * "credential"); return r; } Download the image and store to a temp file
     */
    try {
      f = File.createTempFile("aorhardwareImage", null);
    } catch (IOException e1) {
      e1.printStackTrace();
      System.out.println(e1);
      r.setValue("Server Error: Unable to create temp file for image" + e1);
      return r;
    }
    URI glance_uri = UriBuilder.fromPath(GlanceServiceEndPoint).build();
    GlanceClient gc = new GlanceClient(glance_uri);
    gc.GlanceCLConfigClient("/v1", GlanceAuthToken);
    GlanceAuthToken =
        gc.GlanceCLGetAuthToken(hwUsername, hwPassword, hwTenant,
            keystoneAddress);
    GlanceClientDownloadRequest gcDownloadRequest =
        new GlanceClientDownloadRequest();
    GlanceClientDownloadResponse gcDownloadResponse;
    gcDownloadRequest.setUuid(m.getImageUuid());
    gcDownloadRequest.setSaveToDisk(true);
    gcDownloadRequest.setDownloadLocalPath(f.getParent());
    gcDownloadRequest.setImageName(f.getName());
    gcDownloadResponse = gc.GlanceCLDownloadImage(gcDownloadRequest);
    if (!gcDownloadResponse.getIsSuccessful()) {
      r.setValue(gcDownloadResponse.getError());
      return r;
    }
    // make sure the uuid isn't malformed
    try {
      uuid = UUID.fromString(m.getUuid());
    } catch (IllegalArgumentException e) {
      r.setValue(e.getMessage());
      return r;
    }
    // find the node corresponding to the hardware
    for (HardwareUnit unit : h) {
      Node node = unit.getNodeFromUUID(uuid);
      if (node == null)
        continue;
      if (node instanceof HardwareNode) {
        u = unit;
        n = (HardwareNode) node;
        break;
        // found a node but it's an InterchipNode. NOT POSSIBLE!
      } else
        break;
    }
    // can't find the uuid in any of the units
    if (n == null) {
      r.setValue("Supplied UUID does not correspond to any hardware nodes");
      return r;
    }
    // initialize checksummer
    CRC32 crc = new CRC32();
    crc.reset();
    // calculate CRC
    try {
      fin = new FileInputStream(f);
      // read 1024 bytes at a time and update the crc buffer
      while ((readlen = fin.read(buf, 0, 1024)) != -1)
        crc.update(Arrays.copyOfRange(buf, 0, readlen));
      fin.close();
    } catch (IOException e) {
      r.setValue(e.getMessage());
      return r;
    }
    // send the request, receive the response
    try {
      // crc.getValue returns the crc checksum that's currently calculated
      // the returned type is LONG, but in Java LONG is 8 bytes while
      // a crc32 hash is only 4 bytes (0xffffffff). Only the less
      // significant half of the long is used, so it's safe to cast to an
      // int.
      synchronized (u.sock) {
        MessageFactory.programResourceRequest(u.sock, n.uuid, (int) f.length(),
            (int) crc.getValue());
        // the returned value from PROGRAM_TRANS_RDY is the tcp port that
        // we connect to
        tcpport = MessageFactory.programResourceTransReady(u.sock);
      }
    } catch (GenericException e) {
      r.setValue(e.getMessage());
      return r;
    }
    // set up tcp connction and write to it
    try {
      tcps = new Socket(u.host, tcpport);
      o = tcps.getOutputStream();
      fin = new FileInputStream(f);
      // read 1024 bytes at a time and write the buffer to the tcp socket
      while ((readlen = fin.read(buf, 0, 1024)) != -1)
        o.write(buf, 0, readlen);
      tcps.close();
      fin.close();
    } catch (IOException e) {
      r.setValue(e.getMessage());
      return r;
    }
    // wait for verification; any failure will result in a GenericException
    try {
      synchronized (u.sock) {
        MessageFactory.programResourceTransCorrect(u.sock);
      }
      System.out.println("bitstream transferred to " + n.name + " correctly.");
    } catch (GenericException e) {
      r.setValue(e.getMessage());
      return r;
    }
    // remove the temporary file
    f.delete();
    r.setSuccessful(true);
    r.setValue("Bitstream transferred to " + n.name + " correctly.");
    return r;
  }

  // resource status request
  public ResourceStatusResponse resourceStatus(ResourceStatusRequest m) {
    ResourceStatusResponse r = new ResourceStatusResponse();
    UUID uuid;
    HardwareUnit u = null;
    Node n = null;
    // if uuid is blank, check status of connected with subagents
    if (m.getUuid() == null || m.getUuid().equals("")) {
      // right now, use some placeholder status codes
      if (!connected) {
        r.setCode(0xff);
        r.setValue("Not connected.");
        r.setSuccessful(false);
        return r;
        // OK status
      } else {
        r.setCode(0x00);
        r.setValue("OK!");
        r.setSuccessful(true);
        return r;
      }
    } else {
      // make sure the UUID we get is not malformed
      try {
        uuid = UUID.fromString(m.getUuid());
      } catch (IllegalArgumentException e) {
        r.setCode(0x01);
        r.setValue(e.getMessage());
        r.setSuccessful(false);
        return r;
      }
      // find the unit corresponding to the uuid (search through all units)
      for (HardwareUnit unit : h) {
        Node node = unit.getNodeFromUUID(uuid);
        // if it cannot be found in this unit, null is returned by
        // getNodeFromUUID
        if (node == null)
          continue;
        u = unit;
        n = node;
        break;
      }
      // can't find the uuid in any unit
      if (n == null) {
        r.setCode(0x02);
        r.setValue("Supplied UUID (" + m.getUuid()
            + ") does not correspond to any nodes");
        r.setSuccessful(false);
        return r;
      }
      // found a hardware node
      if (n instanceof HardwareNode) {
        try {
          synchronized (u.sock) {
            MessageFactory.resourceStatusRequest(u.sock, uuid);
            MessageFactory.resourceStatusResponse(u.sock);
          }
        } catch (IOException e) {
          h.remove(u);
          r.setCode(0x03);
          r.setValue("IOException on " + u.name + ": " + e.getMessage());
          r.setSuccessful(false);
          return r;
        } catch (GenericException e) {
          r.setCode(0x04);
          r.setValue("Error, received message: " + e);
          r.setSuccessful(false);
          return r;
        }
        // found an interchip node
      } else {
        r.setCode(0x02);
        r.setValue("Supplied UUID (" + m.getUuid()
            + ") does not correspond to any hardware nodes");
        r.setSuccessful(false);
        return r;
      }
      r.setCode(0x00);
      r.setValue("OK!");
      r.setSuccessful(true);
      return r;
    }
  }

  // terminate request - sever the connection between WS and HS
  public TerminateResponse terminate(java.lang.Object m) {
    TerminateResponse r = new TerminateResponse();
    r.setSuccessful(false);
    // iterate through the subagents, remove the connection for each one
    for (HardwareUnit u : h) {
      if (u == null)
        continue;
      if (u.sock == null)
        continue;
      // try to stop this subagent
      try {
        synchronized (u.sock) {
          MessageFactory.terminateRequest(u.sock);
          MessageFactory.terminateResponse(u.sock);
        }
      } catch (IOException e) {
        h.remove(u);
        r.setValue("IOException on " + u.name + ": " + e.getMessage());
        return r;
      } catch (GenericException e) {
        r.setValue("GenericException on " + u.name + ": " + e.getMessage());
        return r;
      }
    }
    ts.cancel();
    tc.cancel();
    try {
      dc.socket().setReuseAddress(true);
    } catch (SocketException e) {
      r.setValue("cannot unbind: " + e.getMessage());
      return r;
    }
    dc.socket().close();
    dc = null;
    r.setSuccessful(true);
    r.setValue("Successful.");
    connected = false;
    return r;
  }

  // check for any advertisements from subagents
  public void checkBroadcast() {
    // watch for 100 milliseconds
    try {
      selector.select(100);
      // no new packets
      if (!selector.selectedKeys().contains(sk)) {
        return;
      }
    } catch (IOException e) {
      System.out.println("IOException in selector: " + e.getLocalizedMessage());
      return;
    }
    // new packet incoming
    if (sk.isReadable()) {
      proceed = false;
      // packet format:
      // 1 byte name length
      // x bytes name
      // 2 bytes port number
      byte[] buf;
      SocketAddress subAddr;
      // get the subagent name and port
      try {
        ByteBuffer bb = ByteBuffer.allocate(1024);
        subAddr = dc.receive(bb);
        if (subAddr == null) {
          proceed = true;
          return;
        }
        buf = bb.array();
      } catch (IOException e) {
        System.out.println("IOException in socket receive: "
            + e.getLocalizedMessage());
        proceed = true;
        return;
      }
      int namelen = buf[0];
      byte[] namebuf = new byte[namelen];
      byte[] portbuf = new byte[2];
      System.arraycopy(buf, 1, namebuf, 0, namelen);
      System.arraycopy(buf, 1 + namelen, portbuf, 0, 2);
      int saport = Message.byteArrayToShort(portbuf);
      System.out.println("Name resolved to be " + new String(namebuf));
      System.out.println("Port resolved to be " + saport);
      // instantiate the server
      HardwareUnit hu = new HardwareUnit();
      hu.host = ((InetSocketAddress) subAddr).getAddress();
      hu.port = saport;
      hu.name = new String(namebuf);
      try {
        hu.sock = new Socket(hu.host, hu.port);
        // timeout after 30 seconds of blocking on read
        hu.sock.setSoTimeout(30000);
      } catch (IOException e) {
        System.out.println("error setting up socket: "
            + e.getLocalizedMessage());
        proceed = true;
        return;
      }
      try {
        // first initialize
        synchronized (hu.sock) {
          MessageFactory.initRequest(hu.sock);
          MessageFactory.initResponse(hu.sock);
          // then register
          MessageFactory.registerRequest(hu.sock);
          // hardwareRegisterResponse instantiates the objects inside a
          // hardware unit
          hu = MessageFactory.hardwareRegisterResponse(hu.sock, hu);
        }
        // debug output
        System.out.println("Server " + hu.name + " ("
            + hu.host.getHostAddress() + ":" + hu.port
            + ") initialized with the following resources:");
        for (HardwareNode hn : hu.hwNode)
          System.out.println("hw node " + hn.name + ": uuid "
              + hn.uuid.toString());
        for (InterchipNode ic : hu.icNode)
          System.out.println("ic node " + ic.name + ": uuid "
              + ic.uuid.toString() + ", linking "
              + hu.getNodeFromUUID(ic.link[0]).name + " and "
              + hu.getNodeFromUUID(ic.link[1]).name);
        // end debug output
        // everything worked! yay!
        h.add(hu);
        proceed = true;
      } catch (GenericException e) {
        System.out.println(e.getMessage());
        proceed = true;
      }
      proceed = true;
    }
    return;
  }

  // release resources that are overdue
  public void cleanResources() {
    if (h == null)
      return;
    for (HardwareUnit u : h) {
      if (u.hwNode == null)
        continue;
      // hardware node
      // prerequisites for release:
      // - in use
      // - has a duration (duration = 0 means manual release only)
      // - duration has elapsed
      for (Node n : u.hwNode) {
        if (n.inUse && n.duration > 0
            && n.duration + n.timestamp < System.currentTimeMillis()) {
          n.timestamp = 0;
          n.duration = 0;
          n.inUse = false;
          // release it in the subagent (overwrite with default image)
          try {
            synchronized (u.sock) {
              MessageFactory.resourceReleaseRequest(u.sock, n.uuid);
              MessageFactory.resourceReleaseResponse(u.sock);
            }
          } catch (IOException e) {
            System.out.println(e.getMessage());
          } catch (GenericException e) {
            System.out.println(e.getMessage());
          }
        }
      }
      // interchip node
      // prerequisites for release:
      // - in use
      // - if it has a duration, it's elapsed
      // - at least one of the linked FPGAs is not in use
      for (InterchipNode n : u.icNode) {
        if (n.inUse
            && ((n.duration > 0 && n.duration + n.timestamp < System
                .currentTimeMillis()) || n.duration == 0)
            && !(u.getNodeFromUUID(n.link[0]).inUse && u
                .getNodeFromUUID(n.link[1]).inUse)) {
          n.timestamp = 0;
          n.duration = 0;
          n.inUse = false;
        }
      }
    }
  }

  // release resources that are overdue
  public void resetUnresponsiveNodes() {
    ResourceStatusRequest m = new ResourceStatusRequest();
    if (h == null)
      return;
    for (HardwareUnit u : h) {
      if (u.hwNode == null)
        continue;
      // hardware node
      for (Node n : u.hwNode) {
        m.setUuid(n.uuid.toString());
        ResourceStatusResponse r = resourceStatus(m);
        if (r.getCode() != 0x00) {
          try {
            synchronized (u.sock) {
              MessageFactory.resourceReleaseRequest(u.sock, n.uuid);
              MessageFactory.resourceReleaseResponse(u.sock);
            }
          } catch (IOException e) {
            // if pipe is broken, remove subagent
            System.out.println(e.getMessage());
            h.remove(u);
          } catch (GenericException e) {
            System.out.println(e.getMessage());
          }
        }
      }
    }
  }

  public RemoveVlanResponse removeVlan(RemoveVlanRequest m) {
    RemoveVlanResponse r = new RemoveVlanResponse();
    HardwareUnit u = null;
    HardwareNode n = null;
    UUID uuid;
    InetAddress ip;
    r.setSuccessful(false);
    // make sure the uuid isn't malformed
    try {
      uuid = UUID.fromString(m.getUuid());
    } catch (IllegalArgumentException e) {
      r.setValue(e.getMessage());
      return r;
    }
    // Check the vlan id
    if (m.getVlan() < 1 || m.getVlan() > 4095) {
      r.setValue("Error, vlan id must be between 1 and 4095");
      return r;
    }
    // find the node corresponding to the hardware
    for (HardwareUnit unit : h) {
      Node node = unit.getNodeFromUUID(uuid);
      if (node == null)
        continue;
      if (node instanceof HardwareNode) {
        u = unit;
        n = (HardwareNode) node;
        break;
        // found a node but it's an InterchipNode. NOT POSSIBLE!
      } else {
        r.setValue("Cannot set parameters of interchip node");
        return r;
      }
    }
    // can't find the uuid in any of the units
    if (n == null) {
      r.setValue("Supplied UUID does not correspond to any hardware nodes");
      return r;
    }
    try {
      synchronized (u.sock) {
        MessageFactory.removeVlanRequest(u.sock, uuid, Long
            .valueOf(m.getVlan()).intValue());
        MessageFactory.removeVlanResponse(u.sock);
      }
    } catch (GenericException e) {
      r.setValue(e.getMessage());
      return r;
    }
    r.setSuccessful(true);
    r.setValue("Successful");
    return r;
  }

  public AddVlanResponse addVlan(AddVlanRequest m) {
    AddVlanResponse r = new AddVlanResponse();
    HardwareUnit u = null;
    HardwareNode n = null;
    UUID uuid;
    InetAddress ip;
    r.setSuccessful(false);
    // make sure the uuid isn't malformed
    try {
      uuid = UUID.fromString(m.getUuid());
    } catch (IllegalArgumentException e) {
      r.setValue(e.getMessage());
      return r;
    }
    // Check the vlan id
    if (m.getVlan() < 1 || m.getVlan() > 4095) {
      r.setValue("Error, vlan id must be between 1 and 4095");
      return r;
    }
    // find the node corresponding to the hardware
    for (HardwareUnit unit : h) {
      Node node = unit.getNodeFromUUID(uuid);
      if (node == null)
        continue;
      if (node instanceof HardwareNode) {
        u = unit;
        n = (HardwareNode) node;
        break;
        // found a node but it's an InterchipNode. NOT POSSIBLE!
      } else {
        r.setValue("Cannot set parameters of interchip node");
        return r;
      }
    }
    // can't find the uuid in any of the units
    if (n == null) {
      r.setValue("Supplied UUID does not correspond to any hardware nodes");
      return r;
    }
    // m.getIp() is in format ip:gw:dns:name so we just need the IP address
    String ip_in = m.getIP();
    m.setIP(ip_in.substring(0, ip_in.indexOf(":")));
    try {
      synchronized (u.sock) {
        ip = InetAddress.getByName(m.getIP());
        MessageFactory.addVlanRequest(u.sock, n.uuid, ip.getAddress(), Long
            .valueOf(m.getVlan()).intValue());
        MessageFactory.addVlanResponse(u.sock);
      }
    } catch (GenericException e) {
      r.setValue(e.getMessage());
      return r;
    } catch (java.net.UnknownHostException e) {
      r.setValue(e.getMessage());
      return r;
    }
    r.setSuccessful(true);
    r.setValue("Successful");
    return r;
  }

  public UserRegisterInteractionResponse registerInteraction(
      UserRegisterInteractionRequest m) {
    UserRegisterInteractionResponse r = new UserRegisterInteractionResponse();
    HardwareUnit u = null;
    HardwareNode n = null;
    UUID uuid;
    InetAddress ip;
    byte[] getVal;
    // make sure the uuid isn't malformed
    try {
      uuid = UUID.fromString(m.getUuid());
    } catch (IllegalArgumentException e) {
      r.setSuccessful(false);
      r.setValue(e.getMessage());
      return r;
    }
    // find the node corresponding to the hardware
    for (HardwareUnit unit : h) {
      Node node = unit.getNodeFromUUID(uuid);
      if (node == null)
        continue;
      if (node instanceof HardwareNode) {
        u = unit;
        n = (HardwareNode) node;
        break;
        // found a node but it's an InterchipNode. NOT POSSIBLE!
      } else {
        r.setSuccessful(false);
        r.setValue("Interchip does not contain read/write registers" +
            "of interchip node");
        return r;
      }
    }
    // can't find the uuid in any of the units
    if (n == null) {
      r.setSuccessful(false);
      r.setValue("Supplied UUID does not correspond to any hardware nodes");
      return r;
    }
    try {
      synchronized (u.sock) {
        if (m.getSetRegValue() != null) {
          byte[] setVal = new byte[4];
          if (m.getSetRegValue().length <= 4) {
            System.arraycopy(m.getSetRegValue(), 0, setVal, 0,
                m.getSetRegValue().length);
          } else {
            System.arraycopy(m.getSetRegValue(), 0, setVal, 0, 4);
          }
          MessageFactory.setUserRegsRequest(u.sock, n.uuid, setVal);
          MessageFactory.setUserRegsResponse(u.sock);
        }
        MessageFactory.getUserRegsRequest(u.sock, n.uuid);
        getVal = MessageFactory.getUserRegsResponse(u.sock);
      }
    } catch (GenericException e) {
      r.setSuccessful(false);
      r.setValue(e.getMessage());
      return r;
    }
    byte[] formattedGetVal = new byte[4];
    if (getVal.length <= 4)
      System.arraycopy(getVal, 0, formattedGetVal, 0, getVal.length);
    else
      System.arraycopy(getVal, 0, formattedGetVal, 0, 4);
    r.setReadRegisterValue(formattedGetVal);
    r.setSuccessful(true);
    r.setValue("Successful");
    return r;
  }

  public ResourceResetResponse resourceReset(ResourceResetRequest m) {
    ResourceResetResponse r = new ResourceResetResponse();
    UUID uuid;
    HardwareUnit u = null;
    Node n = null;
    r.setSuccessful(false);
    // make sure the UUID we get is not malformed
    try {
      uuid = UUID.fromString(m.getUuid());
    } catch (IllegalArgumentException e) {
      r.setValue(e.getMessage());
      return r;
    }
    // find the unit corresponding to the uuid (search through all units)
    for (HardwareUnit unit : h) {
      Node node = unit.getNodeFromUUID(uuid);
      if (node == null)
        continue;
      u = unit;
      n = node;
      break;
    }
    // can't find the uuid in any of the units
    if (n == null) {
      r.setValue("Supplied UUID (" + m.getUuid()
          + ") does not correspond to any nodes");
      return r;
    }
    // found a hardware node
    if (n instanceof HardwareNode) {
      try {
        synchronized (u.sock) {
          MessageFactory.resourceReleaseRequest(u.sock, n.uuid);
          MessageFactory.resourceReleaseResponse(u.sock);
        }
      } catch (Exception e) {
        r.setValue(e.getMessage());
        return r;
      }
      try {
        synchronized (u.sock) {
          MessageFactory.resourceStatusRequest(u.sock, n.uuid);
          MessageFactory.resourceStatusResponse(u.sock);
        }
      } catch (Exception e) {
        r.setValue(e.getMessage());
        return r;
      }
      r.setSuccessful(true);
      r.setValue("Resource successfully reset");
      return r;
    }
    r.setSuccessful(false);
    r.setValue("UUID supplied not for a hardware resource");
    return r;
  }

  public SetResourceParametersResponse setParams(
      SetResourceParametersRequest req) {
    SetResourceParametersResponse resp = new SetResourceParametersResponse();
    UUID uuid;
    HardwareUnit u = null;
    Node n = null;
    InetAddress ip = null;
    String newMAC = null;
    System.out.println("Got a setParams request");
    resp.setSuccessful(false);
    // make sure the UUID we get is not malformed
    try {
      uuid = UUID.fromString(req.getUuid());
    } catch (IllegalArgumentException e) {
      resp.setValue(e.getMessage());
      return resp;
    }
    // find the unit corresponding to the uuid (search through all units)
    for (HardwareUnit unit : h) {
      Node node = unit.getNodeFromUUID(uuid);
      if (node == null)
        continue;
      u = unit;
      n = node;
      break;
    }
    // can't find the uuid in any of the units
    if (n == null) {
      resp.setValue("Supplied UUID (" + req.getUuid()
          + ") does not correspond to any nodes");
      return resp;
    }
    if (n instanceof HardwareNode) {
      if (req.getVlanID() != null && req.getIP() != null) {
        System.out.println("Sending setparams request");
        try {
          synchronized (u.sock) {
            ip = InetAddress.getByName(req.getIP());
            MessageFactory.setParametersRequest(u.sock, n.uuid,
                ip.getAddress(), Long.valueOf(req.getVlanID()).intValue());
            MessageFactory.setParametersResponse(u.sock);
          }
        } catch (Exception e) {
          System.out.println("open you hands to catch the exception");
          resp.setValue(e.getMessage());
          return resp;
        }
      } // End of sending vlan id and ip
      System.out.println("Set mac RIP");
      System.out.print(req.getMAC().size());
      if (req.getMAC() != null) {
        System.out.println("getmac not null");
        for (int index = 0; index < req.getMAC().size(); index++) {
          System.out.println("Inside getmac for loop");
          String[] parsedMAC = req.getMAC().get(index).getAddr().split(":");
          if (req.getMAC().get(index).getIndex() >= 0
              && req.getMAC().get(index).getIndex() < 4
              && parsedMAC.length == 6) { // Index should be from 0 to 3
            System.out.println("Sending setMAC request");
            newMAC = parsedMAC[0];
            newMAC = newMAC.concat(parsedMAC[1]);
            newMAC = newMAC.concat(parsedMAC[2]);
            newMAC = newMAC.concat(parsedMAC[3]);
            newMAC = newMAC.concat(parsedMAC[4]);
            newMAC = newMAC.concat(parsedMAC[5]);
            System.out.println(newMAC.length());
            System.out.println(newMAC);
            if (newMAC.length() == 12) {
              try {
                synchronized (u.sock) {
                  MessageFactory.setMACRequest(u.sock, n.uuid, req.getMAC()
                      .get(index).getIndex(), fromHexString(newMAC));
                  MessageFactory.setMACResponse(u.sock);
                }
              } catch (Exception e) {
                resp.setValue(e.getMessage());
                return resp;
              }
            } else {
              resp.setValue("Wrong length of MAC");
              return resp;
            }
          } else {
            resp.setValue("Incorrect index or incorrect MAC");
            return resp;
          }
        } // End of for loop over macs
      }
      resp.setSuccessful(true);
      resp.setValue("Resource parameters successfully set");
      return resp;
    }
    resp.setSuccessful(false);
    resp.setValue("UUID supplied not for a hardware resource");
    return resp;
  }
}
