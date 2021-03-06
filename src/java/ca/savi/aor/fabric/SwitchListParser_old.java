// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.fabric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @author Keith
 */
public class SwitchListParser_old {
  java.lang.Object p;

  enum SwitchParams {
    NULL, NAME, IP, SUBAGENT_PORT, PORTS, FILENAME, UUID
  }

  public SwitchListParser_old(java.lang.Object parent) {
    p = parent;
  }

  public boolean parse(String filename) {
    BufferedReader input;
    String line;
    SwitchParams curParam = SwitchParams.NULL;
    VANI_Fabric fabric = (VANI_Fabric) p;
    try {
      input =
          new BufferedReader(new FileReader(new File(fabric.rootDirectory
              + filename)));
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
      return false;
    }
    try {
      int n;
      int state = 0;
      int lineNumber = 1;
      String Sname = "", Sfilename = "", Sip = "";
      UUID Suuid = null;
      int Sports = -1, SsubPort = -1;
      while ((line = input.readLine()) != null) {
        while (!line.equals("")) {
          line = line.trim().toLowerCase();
          System.out.println("line: " + line);
          switch (state) {
          case 0:
            Sname = "";
            Sfilename = "";
            Sip = "";
            Sports = -1;
            Suuid = null;
            if ((n = line.indexOf("switch")) >= 0) {
              line = line.substring(n + 6).trim();
              state = 1;
            } else
              line = "";
            break;
          case 1:
            if ((n = line.indexOf("{")) == 0) {
              line = line.substring(1);
              state = 2;
            } else if (!line.equals(""))
              state = 0;
            break;
          case 2:
            if ((n = line.indexOf("name")) == 0) {
              // strlen("name") == 4
              line = line.substring(n + 4).trim();
              curParam = SwitchParams.NAME;
              state = 3;
              break;
            } else if ((n = line.indexOf("ip")) == 0) {
              // strlen("ip") == 2
              line = line.substring(n + 2).trim();
              curParam = SwitchParams.IP;
              state = 3;
              break;
            } else if ((n = line.indexOf("ports")) == 0) {
              // strlen("ports") == 5
              line = line.substring(n + 5).trim();
              curParam = SwitchParams.PORTS;
              state = 3;
              break;
            } else if ((n = line.indexOf("filename")) == 0) {
              // strlen("filename") == 8
              line = line.substring(n + 8).trim();
              curParam = SwitchParams.FILENAME;
              state = 3;
              break;
            } else if ((n = line.indexOf("subagent_port")) == 0) {
              // strlen("filename") == 13
              line = line.substring(n + 13).trim();
              curParam = SwitchParams.SUBAGENT_PORT;
              state = 3;
            } else if ((n = line.indexOf("uuid")) == 0) {
              line = line.substring(n + 13).trim();
              curParam = SwitchParams.UUID;
              state = 3;
            } else if ((n = line.indexOf("}")) == 0) {
              // strlen("}") == 1
              line = line.substring(n + 1).trim();
              try {
                System.out.println("Sname: " + Sname + " ip: " + Sip
                    + " SsubPort: " + SsubPort + " Sports: " + Sports
                    + " Sfilename: " + Sfilename);
                Socket sock = new Socket(Sip, SsubPort);
                // fabric.addSwitch(Sname, InetAddress.getByName(Sip), SsubPort,
                // sock, Sports, Sfilename, Suuid);
              } catch (UnknownHostException e) {
                System.out.println(e.getMessage());
                return false;
              }
              state = 0;
              break;
            } else {
              System.out.println("Error, unknown switch parameter on line "
                  + lineNumber);
              return (false);
            }
          case 3:
            if (line.charAt(0) == '=') {
              state = 4;
              line = line.substring(1).trim();
              break;
            } else {
              System.out.println("Error, expected \'=\' but got "
                  + line.charAt(0) + " on line: " + lineNumber);
              return (false);
            }
          case 4:
            String value;
            n = line.indexOf(',');
            if (n < 0) {
              value = line;
              line = "";
              state = 5;
            } else {
              value = line.substring(0, n);
              state = 2;
            }
            switch (curParam) {
            case NAME:
              Sname = value;
              // added this for WS implementation. WS will not
              // know the names of the files, so it will save
              // filenames based on teh name of the switch
              Sfilename = value;
              break;
            case IP:
              Sip = value;
              break;
            case PORTS:
              try {
                Sports = Integer.valueOf(value);
                break;
              } catch (NumberFormatException e) {
                System.out
                    .println("Error, invalid value for number of ports on line "
                        + lineNumber);
                return false;
              }
            case FILENAME:
              // Sfilename = value;
              break;
            case SUBAGENT_PORT:
              try {
                SsubPort = Integer.valueOf(value);
                break;
              } catch (NumberFormatException e) {
                System.out
                    .println("Error, invalid value for number of ports on line "
                        + lineNumber);
                return false;
              }
            case UUID:
              try {
                Suuid = UUID.fromString(value);
                break;
              } catch (NumberFormatException e) {
                System.out
                    .println("Error, invalid value for number of UUID on line "
                        + lineNumber);
                return false;
              }
              // should never occur
            default:
              System.out.println("Parser error, reached default");
              return (false);
            }
            line = line.substring(n + 1).trim();
            break;
          case 5:
            if (line.charAt(0) == '}') {
              line = line.substring(1).trim();
              try {
                System.out.println("Sname: " + Sname + " ip: " + Sip
                    + " SsubPort: " + SsubPort + " Sports: " + Sports
                    + " Sfilename: " + Sfilename + " Suuid: "
                    + Suuid.toString());
                Socket sock = new Socket(Sip, SsubPort);
                // fabric.addSwitch(Sname, InetAddress.getByName(Sip), SsubPort,
                // sock, Sports, Sfilename, Suuid);
              } catch (UnknownHostException e) {
                System.out.println(e.getMessage());
                return false;
              }
              state = 0;
              break;
            } else if (line.charAt(0) == ',') {
              line = line.substring(1);
              state = 2;
              break;
            } else {
              System.out.println("Error, expected \',\' or \'}\' but got "
                  + line.charAt(0) + " on line: " + lineNumber);
              return (false);
            }
          }
        }
        lineNumber++;
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }
}