package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class Address extends ResumeElement {
  private static final String ELEMENT_NAME = "address";
  private static final String STREET = "street";
  private static final String APARTMENT = "apartment";
  private static final String CITY = "city";
  private static final String STATE = "state";
  private static final String ZIP = "zip";

  public Address(DefaultHandler parent, XMLReader parser) {
    super(parent, parser, ELEMENT_NAME, buildElementList(STREET, APARTMENT, CITY, STATE, ZIP));
  }

  public String getStreet() {
    return getElementByName(STREET).getValue();
  }

  public String getApartment() {
    return getElementByName(APARTMENT).getValue();
  }

  public String getCity() {
    return getElementByName(CITY).getValue();
  }

  public String getState() {
    return getElementByName(STATE).getValue();
  }

  public String getZip() {
    return getElementByName(ZIP).getValue();
  }

  public static String getElementName() {
    return ELEMENT_NAME;
  }
}
