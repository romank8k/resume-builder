package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class Job extends ResumeElement {
  private static final String ELEMENT_NAME = "job";

  private static final String EMPLOYER = "employer";
  private static final String LOCATION = "location";

  public Job(DefaultHandler parent, XMLReader parser) {
    super(parent, parser, ELEMENT_NAME, buildElementList(EMPLOYER, LOCATION),
        buildCompositeElementList(new CompositeElement<>(Role.class, Role.getElementName())));
  }

  public String getEmployer() {
    return getElementByName(EMPLOYER).getValue();
  }

  public String getLocation() {
    return getElementByName(LOCATION).getValue();
  }

  public List<Role> getRoles() {
    return getCompositeElementByClass(Role.class).getResumeElementList();
  }

  public static String getElementName() {
    return ELEMENT_NAME;
  }
}
