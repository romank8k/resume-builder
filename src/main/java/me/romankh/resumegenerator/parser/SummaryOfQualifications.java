package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class SummaryOfQualifications extends ResumeElement {
  private static final String ELEMENT_NAME = "summary_of_qualifications";

  public SummaryOfQualifications(DefaultHandler parent, XMLReader parser) {
    super(parent, parser, ELEMENT_NAME, null, buildCompositeElementList(new CompositeElement(Qualification.class,
        Qualification.getElementName())));
  }

  public List<List<SnippetElement.Snippet>> getQualifications() {
    List<List<SnippetElement.Snippet>> qualificationsSnippetList = new ArrayList<>();
    List<Qualification> qualificationList = getCompositeElementByName(Qualification.getElementName()).getResumeElementList();
    if (qualificationList != null) {
      for (Qualification qualification : qualificationList) {
        qualificationsSnippetList.add(qualification.snippets);
      }
    }
    return qualificationsSnippetList;
  }

  public static class Qualification extends SnippetElement {
    private static final String ELEMENT_NAME = "qualification";

    public Qualification(DefaultHandler parent, XMLReader parser) {
      super(parent, parser, ELEMENT_NAME);
    }

    public static String getElementName() {
      return ELEMENT_NAME;
    }
  }

  public static String getElementName() {
    return ELEMENT_NAME;
  }
}
