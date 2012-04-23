// Copyright (C) 2012, The SAVI Project.
package ca.savi.horse.server;

import java.io.IOException;

import ca.savi.front.FrontServer;
import ca.savi.horse.service.HWResourceWebService;

/**
 * Runs the front server for horse.
 * @author Hesam Rahimi Koopayi <hesam.rahimikoopayi@utoronto.ca>
 */
public class HorseServer {
  public static void main(String[] args) throws IOException {
    FrontServer f = new FrontServer("localhost", 9090, false);
    f.register(HWResourceWebService.class);
    f.start();
  }
}
