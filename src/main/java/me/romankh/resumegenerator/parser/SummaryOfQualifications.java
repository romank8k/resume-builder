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
    super(parent, parser, ELEMENT_NAME, null,
        buildCompositeElementList(new CompositeElement<>(Qualification.class, Qualification.getElementName())));
  }

  public List<String> getQualifications() {
    List<String> qualificationSnippets = new ArrayList<>();
    List<Qualification> qualificationList = getCompositeElementByClass(Qualification.class).getResumeElementList();
    if (qualificationList != null) {
      for (Qualification qualification : qualificationList) {
        qualificationSnippets.add(qualification.getHtml());
      }
    }
    return qualificationSnippets;
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
