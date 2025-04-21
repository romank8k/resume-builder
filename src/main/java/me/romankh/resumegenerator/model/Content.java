package me.romankh.resumegenerator.model;

import jakarta.xml.bind.annotation.XmlElement;

/**
 * @author Roman Khmelichek
 */
public class Content {
  @XmlElement(name = "objective")
  private ObjectiveSection objectiveSection;

  @XmlElement(name = "summary_of_qualifications")
  private SummaryOfQualificationsSection summaryOfQualificationsSection;

  @XmlElement(name = "awards")
  private AwardsSection awardsSection;

  @XmlElement(name = "relevant_courses")
  private RelevantCoursesSection relevantCoursesSection;

  @XmlElement(name = "experience")
  private ExperienceSection experienceSection;

  @XmlElement(name = "education")
  private EducationSection educationSection;

  @XmlElement(name = "projects")
  private ProjectsSection projectsSection;

  public ObjectiveSection getObjectiveSection() {
    return objectiveSection;
  }

  public Content setObjectiveSection(ObjectiveSection objectiveSection) {
    this.objectiveSection = objectiveSection;
    return this;
  }

  public SummaryOfQualificationsSection getSummaryOfQualificationsSection() {
    return summaryOfQualificationsSection;
  }

  public Content setSummaryOfQualificationsSection(SummaryOfQualificationsSection summaryOfQualificationsSection) {
    this.summaryOfQualificationsSection = summaryOfQualificationsSection;
    return this;
  }

  public AwardsSection getAwardsSection() {
    return awardsSection;
  }

  public Content setAwardsSection(AwardsSection awardsSection) {
    this.awardsSection = awardsSection;
    return this;
  }

  public RelevantCoursesSection getRelevantCoursesSection() {
    return relevantCoursesSection;
  }

  public Content setRelevantCoursesSection(RelevantCoursesSection relevantCoursesSection) {
    this.relevantCoursesSection = relevantCoursesSection;
    return this;
  }

  public ExperienceSection getExperienceSection() {
    return experienceSection;
  }

  public Content setExperienceSection(ExperienceSection experienceSection) {
    this.experienceSection = experienceSection;
    return this;
  }

  public EducationSection getEducationSection() {
    return educationSection;
  }

  public Content setEducationSection(EducationSection educationSection) {
    this.educationSection = educationSection;
    return this;
  }

  public ProjectsSection getProjectsSection() {
    return projectsSection;
  }

  public Content setProjectsSection(ProjectsSection projectsSection) {
    this.projectsSection = projectsSection;
    return this;
  }
}
