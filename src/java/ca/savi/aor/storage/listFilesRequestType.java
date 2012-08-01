// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.storage;

/**
 * @author Keith
 */
public class listFilesRequestType {
  public String[] name;
  public boolean[] isDir;
  public int numFiles;

  public listFilesRequestType(int fileCount) {
    name = new String[fileCount];
    isDir = new boolean[fileCount];
    numFiles = fileCount;
  }
}
