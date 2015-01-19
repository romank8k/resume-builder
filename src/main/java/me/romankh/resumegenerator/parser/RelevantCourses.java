package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class RelevantCourses extends ResumeElement {
  private static final String ELEMENT_NAME = "relevant_courses";
  private static final String COURSE = "course";

  public RelevantCourses(DefaultHandler parent, XMLReader parser) {
    super(parent, parser, ELEMENT_NAME, buildElementList(COURSE));
  }

  public List<String> getCourses() {
    return getElementByName(COURSE).getValues();
  }

  public static String getElementName() {
    return ELEMENT_NAME;
  }
}
