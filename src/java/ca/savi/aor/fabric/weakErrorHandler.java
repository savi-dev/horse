// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.fabric;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Keith
 */
public class weakErrorHandler implements ErrorHandler {
  public void fatalError(SAXParseException e) throws SAXException {
    throw e;
  }

  public void error(SAXParseException e) throws SAXException {
    throw e;
  }

  public void warning(SAXParseException e) throws SAXException {
    // noop
  }
}