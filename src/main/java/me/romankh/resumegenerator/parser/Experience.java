package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class Experience extends ResumeElement {
  private static final String ELEMENT_NAME = "experience";

  public Experience(DefaultHandler parent, XMLReader parser) {
    super(parent, parser, ELEMENT_NAME, buildElementList(),
        buildCompositeElementList(new CompositeElement<>(Job.class, Job.getElementName())));
  }

  public List<Job> getJobs() {
    return getCompositeElementByClass(Job.class).getResumeElementList();
  }

  public static String getElementName() {
    return ELEMENT_NAME;
  }
}
