package me.romankh.resumegenerator.model;

import me.romankh.resumegenerator.TestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.testng.annotations.Test;

import javax.xml.bind.*;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import static org.testng.Assert.*;

/**
 * @author Roman Khmelichek
 */
public class ResumeMarshallingTest extends TestUtils {
  private static final Logger logger = LogManager.getLogger(ResumeMarshallingTest.class);

  private final ResumeUtils resumeUtils = new ResumeUtils();

  @Test(description = "Unmarshall from XML and verify contents")
  public void unmarshallFromXMLTest() throws Exception {
    JAXBContext jaxbContext = resumeUtils.buildResumeJAXBContext();
    Marshaller marshaller = jaxbContext.createMarshaller();

    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    unmarshaller.setEventHandler(new ValidationEventHandler() {
      @Override
      public boolean handleEvent(ValidationEvent event) {
        assertFalse(true);
        return false;
      }
    });

    InputStream is = getResourceInputStream("resume-test.xml");
    Resume resume = (Resume) unmarshaller.unmarshal(is);

    // We marshall/unmarshall again without pretty-printing so that tests can pass without trimming the <html> fields.
    StringWriter sw = new StringWriter();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
    marshaller.marshal(resume, sw);
    String marshaledResumeStr = sw.toString();

    StringReader sr = new StringReader(marshaledResumeStr);
    Resume unmarshalledResume = (Resume) unmarshaller.unmarshal(sr);
    verifyResume(unmarshalledResume);
  }

  @Test(description = "Unmarshall from JSON and verify contents")
  public void unmarshallFromJSONTest() throws Exception {
    JAXBContext jaxbContext = resumeUtils.buildResumeJAXBContext();

    Unmarshaller jsonUnmarshaller = jaxbContext.createUnmarshaller();
    jsonUnmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
    jsonUnmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, true);
    jsonUnmarshaller.setProperty(UnmarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);

    InputStream is = getResourceInputStream("resume-test.json");
    Resume resume = (Resume) jsonUnmarshaller.unmarshal(is);
    verifyResume(resume);
  }

  @Test(description = "Serialize POJO to XML, unmarshall back to POJO, and verify contents")
  public void xmlSerializationTest() throws Exception {
    JAXBContext jaxbContext = resumeUtils.buildResumeJAXBContext();
    Marshaller marshaller = jaxbContext.createMarshaller();
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

    Resume resume = resumeUtils.buildResume();
    // Marshall to stdout, pretty-printed.
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.marshal(resume, System.out);

    StringWriter sw = new StringWriter();
    // Marshall to string, no formatting so tests pass.
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
    marshaller.marshal(resume, sw);
    String marshaledResumeStr = sw.toString();

    StringReader sr = new StringReader(marshaledResumeStr);
    Resume unmarshalledResume = (Resume) unmarshaller.unmarshal(sr);
    verifyResume(unmarshalledResume);
  }

  @Test(description = "Serialize POJO to JSON, unmarshall back to POJO, and verify contents")
  public void jsonSerializationTest() throws Exception {
    JAXBContext jaxbContext = resumeUtils.buildResumeJAXBContext();

    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
    marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
    marshaller.setProperty(MarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);

    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
    unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, true);
    unmarshaller.setProperty(UnmarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);

    Resume resume = resumeUtils.buildResume();

    // TODO: MOXy currently produces incorrect JSON for our <html> fields.
    // Marshall to stdout, pretty-printed.
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.marshal(resume, System.out);

    StringWriter sw = new StringWriter();
    // Marshall to string, no formatting so tests pass.
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
    marshaller.marshal(resume, sw);
    String marshaledResumeStr = sw.toString();

    StringReader sr = new StringReader(marshaledResumeStr);
    Resume unmarshalledResume = (Resume) unmarshaller.unmarshal(sr);
    verifyResume(unmarshalledResume);
  }

  @Test
  void generateSchemaTest() throws IOException, JAXBException {
    JAXBContext jaxbContext = resumeUtils.buildResumeJAXBContext();
    jaxbContext.generateSchema(new SchemaOutputResolver() {
      @Override
      public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
        StreamResult result = new StreamResult(System.out);
        result.setSystemId(suggestedFileName);
        return result;
      }
    });
  }

  void verifyResume(Resume resume) {
    assertNotNull(resume);

    // Meta info.
    assertNotNull(resume.getResumeMeta());
    assertEquals(resume.getResumeMeta().getMarginLeft(), 30);
    assertEquals(resume.getResumeMeta().getMarginRight(), 30);
    assertEquals(resume.getResumeMeta().getMarginTop(), 30);
    assertEquals(resume.getResumeMeta().getMarginBottom(), 30);

    // Header.
    assertNotNull(resume.getResumeHeader());
    assertEquals(resume.getResumeHeader().getName(), "Johnny Appleseed");
    assertEquals(resume.getResumeHeader().getPhone(), "(123) 456-7890");
    assertEquals(resume.getResumeHeader().getEmail(), "johnny.appleseed@example.com");
    assertEquals(resume.getResumeHeader().getHomepage(), "http://example.com");
    // Address.
    assertNotNull(resume.getResumeHeader().getAddress());
    assertEquals(resume.getResumeHeader().getAddress().getStreet(), "123 Avenue X");
    assertEquals(resume.getResumeHeader().getAddress().getApartment(), "Apt.1A");
    assertEquals(resume.getResumeHeader().getAddress().getCity(), "Brooklyn");
    assertEquals(resume.getResumeHeader().getAddress().getState(), "NY");
    assertEquals(resume.getResumeHeader().getAddress().getZip(), "11230");

    // Content.
    assertNotNull(resume.getContent());

    // Objective.
    ObjectiveSection objectiveSection = resume.getContent().getObjectiveSection();
    assertNotNull(objectiveSection);
    assertEquals(objectiveSection.isInclude(), true);
    assertEquals(objectiveSection.getObjective(), "Find a job as a <b><i>software</i> engineer</b>");

    // Summary of Qualifications.
    SummaryOfQualificationsSection summaryOfQualificationsSection = resume.getContent().getSummaryOfQualificationsSection();
    assertNotNull(summaryOfQualificationsSection);
    assertEquals(summaryOfQualificationsSection.isInclude(), true);
    assertNotNull(summaryOfQualificationsSection.getQualifications());
    assertEquals(summaryOfQualificationsSection.getQualifications().size(), 3);
    assertEquals(summaryOfQualificationsSection.getQualifications().get(0).isInclude(), true);
    assertEquals(summaryOfQualificationsSection.getQualifications().get(0).getQualification(),
        "Qualification One");
    assertEquals(summaryOfQualificationsSection.getQualifications().get(1).isInclude(), true);
    assertEquals(summaryOfQualificationsSection.getQualifications().get(1).getQualification(),
        "Qualification Two");
    assertEquals(summaryOfQualificationsSection.getQualifications().get(2).isInclude(), false);
    assertEquals(summaryOfQualificationsSection.getQualifications().get(2).getQualification(),
        "Qualification Three");

    // Education.
    EducationSection educationSection = resume.getContent().getEducationSection();
    assertNotNull(educationSection);
    assertEquals(educationSection.isInclude(), true);
    assertNotNull(educationSection.getInstitutions());
    assertEquals(educationSection.getInstitutions().size(), 2);
    // College.
    assertEquals(educationSection.getInstitutions().get(0).getName(), "Polytechnic Institute of NYU");
    assertEquals(educationSection.getInstitutions().get(0).getLocation(), "Brooklyn, NY");
    assertNotNull(educationSection.getInstitutions().get(0).getDegrees());
    assertEquals(educationSection.getInstitutions().get(0).getDegrees().size(), 2);
    // Master of Science degree.
    assertEquals(educationSection.getInstitutions().get(0).getDegrees().get(0).isInclude(), true);
    assertEquals(educationSection.getInstitutions().get(0).getDegrees().get(0).getTitle(),
        "Master of Science in Computer Science");
    assertEquals(educationSection.getInstitutions().get(0).getDegrees().get(0).getTimespan(),
        "2009 - 2011");
    assertNotNull(educationSection.getInstitutions().get(0).getDegrees().get(0).getDegreeAccomplishments());
    assertEquals(educationSection.getInstitutions().get(0).getDegrees().get(0).getDegreeAccomplishments().size(), 1);
    assertNotNull(educationSection.getInstitutions().get(0).getDegrees().get(0).getDegreeAccomplishments().get(0));
    assertEquals(educationSection.getInstitutions().get(0).getDegrees().get(0).getDegreeAccomplishments().get(0).isInclude(), true);
    assertEquals(educationSection.getInstitutions().get(0).getDegrees().get(0).getDegreeAccomplishments().get(0).getAccomplishment(),
        "Masters thesis project");
    // Bachelor of Science degree.
    assertEquals(educationSection.getInstitutions().get(0).getDegrees().get(1).isInclude(), true);
    assertEquals(educationSection.getInstitutions().get(0).getDegrees().get(1).getTitle(),
        "Bachelor of Science in Computer Engineering");
    assertEquals(educationSection.getInstitutions().get(0).getDegrees().get(1).getTimespan(),
        "2006 - 2011");
    assertNotNull(educationSection.getInstitutions().get(0).getDegrees().get(1).getDegreeAccomplishments());
    assertEquals(educationSection.getInstitutions().get(0).getDegrees().get(1).getDegreeAccomplishments().size(), 1);
    assertNotNull(educationSection.getInstitutions().get(0).getDegrees().get(1).getDegreeAccomplishments().get(0));
    assertEquals(educationSection.getInstitutions().get(0).getDegrees().get(1).getDegreeAccomplishments().get(0).isInclude(), true);
    assertEquals(educationSection.getInstitutions().get(0).getDegrees().get(1).getDegreeAccomplishments().get(0).getAccomplishment(),
        "Summa cum laude");

    // High school.
    assertEquals(educationSection.getInstitutions().get(1).getName(), "Brooklyn Technical High School");
    assertEquals(educationSection.getInstitutions().get(1).getLocation(), "Brooklyn, NY");
    assertNotNull(educationSection.getInstitutions().get(1).getDegrees());
    assertEquals(educationSection.getInstitutions().get(1).getDegrees().size(), 1);
    // High school diploma.
    assertEquals(educationSection.getInstitutions().get(1).getDegrees().get(0).isInclude(), true);
    assertEquals(educationSection.getInstitutions().get(1).getDegrees().get(0).getTitle(),
        "Regents Diploma with Advanced Designation with Honors");
    assertEquals(educationSection.getInstitutions().get(1).getDegrees().get(0).getTimespan(),
        "2002 - 2006");
    assertNotNull(educationSection.getInstitutions().get(1).getDegrees().get(0).getDegreeAccomplishments());
    assertEquals(educationSection.getInstitutions().get(1).getDegrees().get(0).getDegreeAccomplishments().size(), 1);
    assertNotNull(educationSection.getInstitutions().get(1).getDegrees().get(0).getDegreeAccomplishments().get(0));
    assertEquals(educationSection.getInstitutions().get(1).getDegrees().get(0).getDegreeAccomplishments().get(0).isInclude(), true);
    assertEquals(educationSection.getInstitutions().get(1).getDegrees().get(0).getDegreeAccomplishments().get(0).getAccomplishment(),
        "Computer Science Major");

    // Awards.
    AwardsSection awardsSection = resume.getContent().getAwardsSection();
    assertNotNull(awardsSection);
    assertEquals(awardsSection.isInclude(), true);
    assertNotNull(awardsSection.getAwards());
    assertEquals(awardsSection.getAwards().size(), 1);
    assertEquals(awardsSection.getAwards().get(0).getTitle(), "Honors Scholarship");
    assertEquals(awardsSection.getAwards().get(0).getTimespan(), "Fall 2006 — Spring 2010");

    // Relevant Courses.
    RelevantCoursesSection relevantCoursesSection = resume.getContent().getRelevantCoursesSection();
    assertNotNull(relevantCoursesSection);
    assertEquals(relevantCoursesSection.isInclude(), true);
    assertNotNull(relevantCoursesSection.getCourses());
    assertEquals(relevantCoursesSection.getCourses().size(), 5);
    assertEquals(relevantCoursesSection.getCourses().get(0).isInclude(), true);
    assertEquals(relevantCoursesSection.getCourses().get(0).getCourse(), "Web Search Engines");
    assertEquals(relevantCoursesSection.getCourses().get(1).isInclude(), true);
    assertEquals(relevantCoursesSection.getCourses().get(1).getCourse(), "Machine Learning");
    assertEquals(relevantCoursesSection.getCourses().get(2).isInclude(), true);
    assertEquals(relevantCoursesSection.getCourses().get(2).getCourse(), "Databases");
    assertEquals(relevantCoursesSection.getCourses().get(3).isInclude(), true);
    assertEquals(relevantCoursesSection.getCourses().get(3).getCourse(), "Parallel and Distributed Systems");
    assertEquals(relevantCoursesSection.getCourses().get(4).isInclude(), false);
    assertEquals(relevantCoursesSection.getCourses().get(4).getCourse(), "Unix System Programming");

    // Projects.
    ProjectsSection projectsSection = resume.getContent().getProjectsSection();
    assertNotNull(projectsSection);
    assertEquals(projectsSection.isInclude(), true);
    assertNotNull(projectsSection.getProjects());
    assertEquals(projectsSection.getProjects().size(), 2);
    assertEquals(projectsSection.getProjects().get(0).isInclude(), true);
    assertEquals(projectsSection.getProjects().get(0).getProject(), "Project one");
    assertEquals(projectsSection.getProjects().get(1).isInclude(), false);
    assertEquals(projectsSection.getProjects().get(1).getProject(), "Project two");

    // Experience.
    ExperienceSection experienceSection = resume.getContent().getExperienceSection();
    assertNotNull(experienceSection);
    assertEquals(experienceSection.isInclude(), true);
    assertNotNull(experienceSection.getJobs());
    assertEquals(experienceSection.getJobs().size(), 1);
    assertEquals(experienceSection.getJobs().get(0).isInclude(), true);
    assertEquals(experienceSection.getJobs().get(0).getEmployer(), "Employer One");
    assertEquals(experienceSection.getJobs().get(0).getLocation(), "New York, NY");
    assertNotNull(experienceSection.getJobs().get(0).getRoles());
    assertEquals(experienceSection.getJobs().get(0).getRoles().size(), 1);
    assertEquals(experienceSection.getJobs().get(0).getRoles().get(0).isInclude(), true);
    assertEquals(experienceSection.getJobs().get(0).getRoles().get(0).getTitle(), "Software Developer");
    assertEquals(experienceSection.getJobs().get(0).getRoles().get(0).getDepartment(), "Testing and Automation");
    assertEquals(experienceSection.getJobs().get(0).getRoles().get(0).getTimespan(), "2010 - 2011");
    assertNotNull(experienceSection.getJobs().get(0).getRoles().get(0).getRoleAccomplishments());
    assertEquals(experienceSection.getJobs().get(0).getRoles().get(0).getRoleAccomplishments().size(), 1);
    assertTrue(experienceSection.getJobs().get(0).getRoles().get(0).getRoleAccomplishments().get(0).isInclude());
    assertEquals(experienceSection.getJobs().get(0).getRoles().get(0).getRoleAccomplishments().get(0).getAccomplishment(),
        "Wrote <b>automation</b> and <b>software testing</b> tools.");
  }
}
