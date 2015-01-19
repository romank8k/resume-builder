package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class Education extends ResumeElement {
  private static final String ELEMENT_NAME = "education";

  public Education(DefaultHandler parent, XMLReader parser) {
    super(parent, parser, ELEMENT_NAME, buildElementList(),
        buildCompositeElementList(new CompositeElement(Institution.class, Institution.getElementName())));
  }

  public List<Institution> getInstitutions() {
    return getCompositeElementByClass(Institution.class).getResumeElementList();
  }

  public static String getElementName() {
    return ELEMENT_NAME;
  }
}
