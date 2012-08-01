// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author Keith
 */
public class UserList {
  List<User> UList;

  public UserList(File f) throws IOException {
    UList = new LinkedList<User>();
    initialize(f);
  }

  public boolean findUser(String u) {
    Iterator<User> scanner = UList.iterator();
    while (scanner.hasNext()) {
      User myUser = scanner.next();
      if (u.equals(myUser.name))
        return true;
    }
    // If user is not found, return a NULL user
    return (false);
  }

  public User getUserFromName(String username) {
    Iterator<User> scanner = UList.iterator();
    while (scanner.hasNext()) {
      User myUser = scanner.next();
      if (username.equals(myUser.name))
        return myUser;
    }
    // If user is not found, return a NULL user
    return (new User("", "", 0, 0));
  }

  public boolean removeUser(String fName, User u) {
    boolean userFound = false;
    try {
      String line;
      BufferedReader in = new BufferedReader(new FileReader(fName));
      BufferedWriter fo = new BufferedWriter(new FileWriter("temp"));
      boolean firstLine = true;
      // Copy the file to a temporary file, neglect the user being deleted.
      while ((line = in.readLine()) != null) {
        String[] userInfo = line.split(",");
        if (u.name.equals(userInfo[0]))
          userFound = true;
        else {
          if (firstLine) {
            firstLine = false;
          } else {
            fo.newLine();
          }
          System.out.println("u.name: " + u.name + " userInfo[0]: "
              + userInfo[0]);
          System.out.println(line);
          fo.write(line);
        }
      }
      in.close();
      fo.close();
      in = null;
      fo = null;
      in = new BufferedReader(new FileReader("temp"));
      fo = new BufferedWriter(new FileWriter(fName));
      firstLine = true;
      // Copy temporary file back to original.
      while ((line = in.readLine()) != null) {
        if (firstLine) {
          firstLine = false;
        } else {
          fo.newLine();
        }
        fo.write(line);
      }
      in.close();
      fo.close();
    } catch (FileNotFoundException e) {
      return (false);
    } catch (IOException e) {
      return (false);
    }
    UList.remove(u);
    return (userFound);
  }

  public int addUser(String fName, User u) {
    if (u.name.equals(""))
      return (-4);
    FileOutputStream fs;
    try {
      fs = new FileOutputStream(fName, true);
    } catch (FileNotFoundException e) {
      return (-1);
    }
    if (findUser(u.name)) {
      try {
        fs.close();
      } catch (IOException E) {
        return (-2);
      }
      return (-2);
    } else {
      UList.add(u);
      try {
        fs.write(("\n" + u.name + "," + u.password).getBytes());
        fs.close();
      } catch (IOException E) {
        return (-3);
      }
      return (1);
    }
  }

  private void initialize(File f) throws IOException {
    Properties p = new Properties();
    p.load(new FileInputStream(f));
    Set<Object> keys = p.keySet();
    Iterator<Object> scanner = keys.iterator();
    String username, password;
    while (scanner.hasNext()) {
      Object myUser = scanner.next();
      String[] userInfo = myUser.toString().split(",");
      if (userInfo.length >= 4) {
        username = userInfo[0];
        password = userInfo[1];
        User temp =
            new User(username, password, Integer.valueOf(userInfo[2]),
                Integer.valueOf(userInfo[3]));
        UList.add(temp);
      } else {
        UList.add(new User(userInfo[0], "", 0, 0));
      }
    }
  }
}