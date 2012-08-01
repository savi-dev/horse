// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.processing;

/**
 * @author Keith
 */
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class BroadcastListenerServletListener
    implements ServletContextListener {
  public static boolean hasBeenInit;

  public BroadcastListenerServletListener() {
    System.out.println("Creating servlet listener");
  }

  public void contextInitialized(ServletContextEvent event) {
    System.out.println("Servlet ready to handle contexts");
    hasBeenInit = true;
  }

  public void contextDestroyed(ServletContextEvent event) {
    System.out.println("Servlet shutdown");
    hasBeenInit = false;
  }
}