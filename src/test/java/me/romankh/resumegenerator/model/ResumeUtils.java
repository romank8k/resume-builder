package me.romankh.resumegenerator.model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * @author Roman Khmelichek
 */
public class ResumeUtils {
  public JAXBContext buildResumeJAXBContext() throws JAXBException {
    return JAXBContext.newInstance(
        Resume.class,
        HtmlTag.class,
        MarkdownTag.class
    );
  }

  public Resume buildResume() {
    Resume resume = new Resume();

    resume.setResumeMeta(new ResumeMeta()
        .setMarginLeft(30)
        .setMarginRight(30)
        .setMarginTop(30)
        .setMarginBottom(30));

    resume.setResumeHeader(new ResumeHeader()
        .setName("Johnny Appleseed")
        .setPhone("(123) 456-7890")
        .setEmail("johnny.appleseed@example.com")
        .setHomepage("http://example.com")
        .setAddress(new Address()
            .setStreet("123 Avenue X")
            .setApartment("Apt.1A")
            .setCity("Brooklyn")
            .setState("NY")
            .setZip("11230")));

    resume.setContent(new Content()
        .setObjectiveSection(new ObjectiveSection()
            .setInclude(true)
            .setObjective("Find a job as a <b><i>software</i> engineer</b>"))
        .setSummaryOfQualificationsSection(new SummaryOfQualificationsSection()
            .setInclude(true)
            .addQualification(new SummaryOfQualificationsSection.Qualification()
                .setInclude(true)
                .setQualification("Qualification One"))
            .addQualification(new SummaryOfQualificationsSection.Qualification()
                .setInclude(true)
                .setQualification("Qualification Two"))
            .addQualification(new SummaryOfQualificationsSection.Qualification()
                .setInclude(false)
                .setQualification("Qualification Three")))
        .setEducationSection(new EducationSection()
            .setInclude(true)
            .addInstitution(new Institution()
                .setInclude(true)
                .setName("Polytechnic Institute of NYU")
                .setLocation("Brooklyn, NY")
                .addDegree(new Degree()
                    .setInclude(true)
                    .setTitle("Master of Science in Computer Science")
                    .setTimespan("2009 - 2011")
                    .addAccomplishment(new Degree.DegreeAccomplishment()
                        .setInclude(true)
                        .setAccomplishment("Masters thesis project")))
                .addDegree(new Degree()
                    .setInclude(true)
                    .setTitle("Bachelor of Science in Computer Engineering")
                    .setTimespan("2006 - 2011")
                    .addAccomplishment(new Degree.DegreeAccomplishment()
                        .setInclude(true)
                        .setAccomplishment("Summa cum laude"))))
            .addInstitution(new Institution()
                .setInclude(false)
                .setName("Brooklyn Technical High School")
                .setLocation("Brooklyn, NY")
                .addDegree(new Degree()
                    .setInclude(true)
                    .setTitle("Regents Diploma with Advanced Designation with Honors")
                    .setTimespan("2002 - 2006")
                    .addAccomplishment(new Degree.DegreeAccomplishment()
                        .setInclude(true)
                        .setAccomplishment("Computer Science Major")))))
        .setAwardsSection(new AwardsSection()
            .setInclude(true)
            .addAward(new AwardsSection.Award()
                .setInclude(true)
                .setTitle("Honors Scholarship")
                .setTimespan("Fall 2006 \u2014 Spring 2010")))
        .setRelevantCoursesSection(new RelevantCoursesSection()
            .setInclude(true)
            .addCourse(new RelevantCoursesSection.Course()
                .setInclude(true)
                .setCourse("Web Search Engines"))
            .addCourse(new RelevantCoursesSection.Course()
                .setInclude(true)
                .setCourse("Machine Learning"))
            .addCourse(new RelevantCoursesSection.Course()
                .setInclude(true)
                .setCourse("Databases"))
            .addCourse(new RelevantCoursesSection.Course()
                .setInclude(true)
                .setCourse("Parallel and Distributed Systems"))
            .addCourse(new RelevantCoursesSection.Course()
                .setInclude(false)
                .setCourse("Unix System Programming")))
        .setProjectsSection(new ProjectsSection()
            .setInclude(true)
            .addProject(new ProjectsSection.Project()
                .setInclude(true)
                .setProject("Project one"))
            .addProject(new ProjectsSection.Project()
                .setInclude(false)
                .setProject("Project two")))
        .setExperienceSection(new ExperienceSection()
            .setInclude(true)
            .addJob(new Job()
                .setInclude(true)
                .setEmployer("Employer One")
                .setLocation("New York, NY")
                .addRole(new Role()
                    .setInclude(true)
                    .setDepartment("Testing and Automation")
                    .setTitle("Software Developer")
                    .setTimespan("2010 - 2011")
                    .addAccomplishment(new Role.RoleAccomplishment()
                        .setInclude(true)
                        .setAccomplishment("Wrote <b>automation</b> and <b>software testing</b> tools."))))));

    return resume;
  }
}
