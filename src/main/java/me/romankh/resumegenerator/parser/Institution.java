package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class Institution extends ResumeElement {
  private static final String ELEMENT_NAME = "institution";
  private static final String NAME = "name";
  private static final String LOCATION = "location";
  private static final String DETAIL = "detail";

  public Institution(DefaultHandler parent, XMLReader parser) {
    super(parent, parser, ELEMENT_NAME, buildElementList(NAME, LOCATION, DETAIL));
  }

  public String getName() {
    return getElementByName(NAME).getValue();
  }

  public String getLocation() {
    return getElementByName(LOCATION).getValue();
  }

  public List<String> getDetails() {
    return getElementByName(DETAIL).getValues();
  }

  public static String getElementName() {
    return ELEMENT_NAME;
  }
}
