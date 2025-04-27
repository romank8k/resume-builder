package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class Projects extends ResumeElement {
  private static final String ELEMENT_NAME = "projects";

  public Projects(DefaultHandler parent, XMLReader parser) {
    super(parent, parser, ELEMENT_NAME, null,
        buildCompositeElementList(new CompositeElement<>(Project.class, Project.getElementName())));
  }

  public List<String> getProjects() {
    List<String> projectSnippets = new ArrayList<>();
    List<Project> projectList = getCompositeElementByClass(Project.class).getResumeElementList();
    if (projectList != null) {
      for (Project qualification : projectList) {
        projectSnippets.add(qualification.getHtml());
      }
    }
    return projectSnippets;
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
