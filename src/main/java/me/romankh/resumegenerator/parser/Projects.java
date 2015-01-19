package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class Projects extends ResumeElement {
  private static final String ELEMENT_NAME = "projects";

  public Projects(DefaultHandler parent, XMLReader parser) {
    super(parent, parser, ELEMENT_NAME, null, buildCompositeElementList(new CompositeElement(Project.class,
        Project.getElementName())));
  }

  public List<List<SnippetElement.Snippet>> getProjects() {
    List<List<SnippetElement.Snippet>> projectsSnippetList = new ArrayList<>();
    List<Project> projectList = getCompositeElementByName(Project.getElementName()).getResumeElementList();
    if (projectList != null) {
      for (Project qualification : projectList) {
        projectsSnippetList.add(qualification.snippets);
      }
    }
    return projectsSnippetList;

  }

  public static String getElementName() {
    return ELEMENT_NAME;
  }

  public static class Project extends SnippetElement {
    private static final String ELEMENT_NAME = "project";

    public Project(DefaultHandler parent, XMLReader parser) {
      super(parent, parser, ELEMENT_NAME);
    }

    public static String getElementName() {
      return ELEMENT_NAME;
    }
  }
}
