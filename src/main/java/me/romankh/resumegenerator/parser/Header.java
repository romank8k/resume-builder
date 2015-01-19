package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Roman Khmelichek
 */
public class Header extends ResumeElement {
  private static final String ELEMENT_NAME = "header";
  private static final String NAME = "name";
  private static final String PHONE = "phone";
  private static final String EMAIL = "email";
  private static final String HOMEPAGE = "homepage";

  public Header(DefaultHandler parent, XMLReader parser) {
    super(parent, parser, ELEMENT_NAME, buildElementList(NAME, PHONE, EMAIL, HOMEPAGE),
        buildCompositeElementList(new CompositeElement(Address.class, Address.getElementName())));
  }

  public String getName() {
    return getElementByName(NAME).getValue();
  }

  public String getPhone() {
    return getElementByName(PHONE).getValue();
  }

  public String getEmail() {
    return getElementByName(EMAIL).getValue();
  }

  public String getHomepage() {
    return getElementByName(HOMEPAGE).getValue();
  }

  public Address getAddress() {
    return getCompositeElementByClass(Address.class).getValue();
  }

  public static String getElementName() {
    return ELEMENT_NAME;
  }
}
