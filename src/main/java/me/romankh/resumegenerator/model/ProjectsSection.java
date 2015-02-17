package me.romankh.resumegenerator.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class ProjectsSection {
  @XmlAttribute(name = "include")
  private boolean include;

  @XmlElementWrapper(name = "projects")
  @XmlElement(name = "project")
  private List<Project> projects;

  public ProjectsSection addProject(Project project) {
    if (projects == null)
      projects = new ArrayList<>();
    projects.add(project);
    return this;
  }

  public boolean isInclude() {
    return include;
  }

  public ProjectsSection setInclude(boolean include) {
    this.include = include;
    return this;
  }

  public List<Project> getProjects() {
    return projects;
  }

  public ProjectsSection setProjects(List<Project> projects) {
    this.projects = projects;
    return this;
  }

  public static class Project {
    @XmlAttribute(name = "include")
    private boolean include;

    @XmlValue
    @XmlAnyElement(value = HTMLHandler.class, lax = false)
    private String project;

    public boolean isInclude() {
      return include;
    }

    public Project setInclude(boolean include) {
      this.include = include;
      return this;
    }

    public String getProject() {
      return project;
    }

    public Project setProject(String project) {
      this.project = project;
      return this;
    }
  }
}
