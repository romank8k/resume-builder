package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class Award extends ResumeElement {
  private static final String ELEMENT_NAME = "award";
  private static final String TITLE = "title";
  private static final String TIMESPAN = "timespan";

  public Award(DefaultHandler parent, XMLReader parser) {
    super(parent, parser, ELEMENT_NAME, buildElementList(TITLE, TIMESPAN));
  }

  public String getTitle() {
    return getElementByName(TITLE).getValue();
  }

  public String getTimespan() {
    return getElementByName(TIMESPAN).getValue();
  }

  public static String getElementName() {
    return ELEMENT_NAME;
  }
}
