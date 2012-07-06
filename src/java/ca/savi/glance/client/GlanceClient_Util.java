// Copyright (C) 2012, The SAVI Project.
package ca.savi.glance.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ca.savi.glance.model.SaveImageToDisk;

/**
 * This is GlanceClient Utility.
 * @author Jie Yu Lin (Eric) <jieyu.lin@mail.utoronto.ca>
 * @version 0.1
 */
public class GlanceClient_Util {
  public static SaveImageToDisk save_image_locally(
      InputStream image_inputstream, String dest_path, int trunk_size) {
    byte[] temp_trunk = new byte[trunk_size];
    int size_read;
    int imageSize = 0;
    SaveImageToDisk saveImageToDisk = new SaveImageToDisk();
    // Delete the file if it already exist
    File f = new File(dest_path);
    System.out.println("Downloading ...");
    try {
      OutputStream outfile = new FileOutputStream(f);
      while (true) {
        size_read = image_inputstream.read(temp_trunk);
        imageSize = imageSize + size_read;
        if (size_read < 0) {
          outfile.close();
          break;
        }
        outfile.write(temp_trunk, 0, size_read);
      }
    } catch (IOException e) {
      System.out.println("IOException");
      System.out.println(e);
      saveImageToDisk.setSuccessful(false);
      saveImageToDisk.setError(e.toString());
      return saveImageToDisk;
    } catch (SecurityException e) {
      System.out.println("SecurityException!");
      System.out.println(e);
      saveImageToDisk.setSuccessful(false);
      saveImageToDisk.setError(e.toString());
      return saveImageToDisk;
    } catch (Exception e) {
      System.out.println(e);
      saveImageToDisk.setSuccessful(false);
      saveImageToDisk.setError(e.toString());
      return saveImageToDisk;
    }
    if (imageSize == 0) {
      saveImageToDisk.setSuccessful(false);
      saveImageToDisk.setError("Error: Image retrived from Glance is empty!");
      return saveImageToDisk;
    }
    saveImageToDisk.setSuccessful(true);
    saveImageToDisk.setError("");
    return saveImageToDisk;
  }

  public static SaveImageToDisk save_image_locally(byte[] imageByteArray,
      String dest_path) {
    SaveImageToDisk saveImageToDisk = new SaveImageToDisk();
    // Delete the file if it already exist
    File f = new File(dest_path);
    if (f.exists()) {
      try {
        f.delete();
      } catch (Exception e) {
        System.out.println(e);
        saveImageToDisk.setSuccessful(false);
        saveImageToDisk.setError(e.toString());
        return saveImageToDisk;
      }
    }
    try {
      OutputStream outfile = new FileOutputStream(f);
      outfile.write(imageByteArray, 0, imageByteArray.length);
    } catch (IOException e) {
      System.out.println("IOException");
      System.out.println(e);
      saveImageToDisk.setSuccessful(false);
      saveImageToDisk.setError(e.toString());
      return saveImageToDisk;
    } catch (SecurityException e) {
      System.out.println("SecurityException!");
      System.out.println(e);
      saveImageToDisk.setSuccessful(false);
      saveImageToDisk.setError(e.toString());
      return saveImageToDisk;
    } catch (Exception e) {
      saveImageToDisk.setSuccessful(false);
      saveImageToDisk.setError(e.toString());
      return saveImageToDisk;
    }
    saveImageToDisk.setSuccessful(true);
    saveImageToDisk.setError("");
    return saveImageToDisk;
  }

  public static InputStream get_inputstream_from_local_file(String file_path) {
    try {
      File f = new File(file_path);
      return new FileInputStream(f);
    } catch (IOException e) {
      System.out.println("IOException:");
      System.out.println(e);
      return null;
    } catch (SecurityException e) {
      System.out.println("SecurityException:");
      System.out.println(e);
      return null;
    }
  }

  public static boolean isEmpty(String s) {
    return (s == null || s.length() == 0);
  }

  public static boolean isNotEmpty(String s) {
    return !(s == null || s.length() == 0);
  }

  public static boolean isEmpty(byte[] b) {
    return (b == null || b.length == 0);
  }

  public static boolean isNotEmpty(byte[] b) {
    return !(b == null || b.length == 0);
  }
}
