package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class Content extends ResumeElement {
  private static final String ELEMENT_NAME = "content";

  public Content(DefaultHandler parent, XMLReader parser) {
    super(parent, parser, ELEMENT_NAME, buildElementList(), buildCompositeElementList(
        new CompositeElement<>(Objective.class, Objective.getElementName()),
        new CompositeElement<>(SummaryOfQualifications.class, SummaryOfQualifications.getElementName()),
        new CompositeElement<>(Education.class, Education.getElementName()),
        new CompositeElement<>(Awards.class, Awards.getElementName()),
        new CompositeElement<>(RelevantCourses.class, RelevantCourses.getElementName()),
        new CompositeElement<>(Projects.class, Projects.getElementName()),
        new CompositeElement<>(Experience.class, Experience.getElementName())
        ));
  }

  public Objective getObjective() {
    return getCompositeElementByClass(Objective.class).getValue();
  }

  public SummaryOfQualifications getSummaryOfQualifications() {
    return getCompositeElementByClass(SummaryOfQualifications.class).getValue();
  }

  public Education getEducation() {
    return getCompositeElementByClass(Education.class).getValue();
  }

  public Awards getAwards() {
    return getCompositeElementByClass(Awards.class).getValue();
  }

  public RelevantCourses getRelevantCourses() {
    return getCompositeElementByClass(RelevantCourses.class).getValue();
  }

  public Projects getProjects() {
    return getCompositeElementByClass(Projects.class).getValue();
  }

  public Experience getExperience() {
    return getCompositeElementByClass(Experience.class).getValue();
  }

  public static String getElementName() {
    return ELEMENT_NAME;
  }
}
