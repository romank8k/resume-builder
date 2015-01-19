package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class Job extends ResumeElement {
  private static final String ELEMENT_NAME = "job";
  private static final String EMPLOYER = "employer";
  private static final String DEPARTMENT = "department";
  private static final String TIMESPAN = "timespan";
  private static final String ROLE = "role";
  private static final String PROJECT = "project";

  public Job(DefaultHandler parent, XMLReader parser) {
    super(parent, parser, ELEMENT_NAME, buildElementList(EMPLOYER, DEPARTMENT, TIMESPAN, ROLE, PROJECT),
        buildCompositeElementList(new CompositeElement(Accomplishment.class, Accomplishment.getElementName())));
  }

  public String getEmployer() {
    return getElementByName(EMPLOYER).getValue();
  }

  public String getDepartment() {
    return getElementByName(DEPARTMENT).getValue();
  }

  public String getTimespan() {
    return getElementByName(TIMESPAN).getValue();
  }

  public String getRole() {
    return getElementByName(ROLE).getValue();
  }

  public String getProject() {
    return getElementByName(PROJECT).getValue();
  }

  public List<List<SnippetElement.Snippet>> getAccomplishments() {
    List<List<SnippetElement.Snippet>> accomplishmentsSnippetList = new ArrayList<>();
    List<Accomplishment> accomplishmentList = getCompositeElementByName(Accomplishment.getElementName()).getResumeElementList();
    if (accomplishmentList != null) {
      for (Accomplishment accomplishment : accomplishmentList) {
        accomplishmentsSnippetList.add(accomplishment.snippets);
      }
    }
    return accomplishmentsSnippetList;
  }

  public static String getElementName() {
    return ELEMENT_NAME;
  }

  public static class Accomplishment extends SnippetElement {
    private static final String ELEMENT_NAME = "accomplishment";

    public Accomplishment(DefaultHandler parent, XMLReader parser) {
      super(parent, parser, ELEMENT_NAME);
    }

    public static String getElementName() {
      return ELEMENT_NAME;
    }
  }
}
