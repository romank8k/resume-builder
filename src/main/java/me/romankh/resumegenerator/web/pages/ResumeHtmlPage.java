package me.romankh.resumegenerator.web.pages;

import com.google.inject.Inject;
import me.romankh.resumegenerator.configuration.Prop;
import me.romankh.resumegenerator.configuration.Property;
import me.romankh.resumegenerator.parser.*;
import me.romankh.resumegenerator.service.ResumeCachingFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeHtmlPage {
    private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("([A-Z][a-z]*)");

    private final Boolean showPersonalDataOnWeb;

    private final ResumeParser resume;

    @Inject
    public ResumeHtmlPage(@Prop(Property.SHOW_PERSONAL_DATA_ON_WEB) Boolean showPersonalDataOnWeb,
                          ResumeCachingFactory resumeCachingFactory) throws IOException, SAXException {
        this.showPersonalDataOnWeb = showPersonalDataOnWeb;

        this.resume = resumeCachingFactory.getResume();
    }

    public static class Section {
        private final String sectionName;
        private final String sectionId;
        private final Class<? extends ResumeElement> sectionClass;
        private final List<Section> subSections;

        public Section(String sectionName, String sectionId, Class<? extends ResumeElement> sectionClass) {
            this.sectionName = sectionName;
            this.sectionId = sectionId;
            this.sectionClass = sectionClass;
            this.subSections = new ArrayList<>();
        }

        public void addSubSection(Section section) {
            subSections.add(section);
        }

        public String getSectionName() {
            return sectionName;
        }

        public String getSectionId() {
            return sectionId;
        }

        public Class<? extends ResumeElement> getSectionClass() {
            return sectionClass;
        }

        public List<Section> getSubSections() {
            return subSections;
        }
    }

    public List<Section> getSections() {
        List<Section> sectionList = new ArrayList<>();

        for (AbstractElement element : resume.getContent().getElementOrderList()) {
            CompositeElement compositeElement = resume.getContent().getCompositeElementByName(element.getName());

            if (compositeElement != null) {
                Class compositeElementClass = compositeElement.getClazz();

                if (Objective.class.equals(compositeElementClass)) {
                    String sectionName = getObjectiveSectionName();
                    String sectionId = getObjectiveSectionId();
                    sectionList.add(new Section(sectionName, sectionId, Objective.class));
                } else if (SummaryOfQualifications.class.equals(compositeElementClass)) {
                    String sectionName = getSummaryOfQualificationsSectionName();
                    String sectionId = getSummaryOfQualificationsSectionId();
                    sectionList.add(new Section(sectionName, sectionId, SummaryOfQualifications.class));
                } else if (Education.class.equals(compositeElementClass)) {
                    Education education = (Education) compositeElement.getValue();
                    String sectionName = getEducationSectionName();
                    String sectionId = getEducationSectionId();

                    Section section = new Section(sectionName, sectionId, Education.class);
                    for (Institution institution : education.getInstitutions()) {
                        String subSectionName = institution.getName();
                        String subSectionId = generateSectionId("institution-", institution.getName(), "");

                        Section institutionSection = new Section(subSectionName, subSectionId, Institution.class);
                        for (Degree degree : institution.getDegrees()) {
                            institutionSection.addSubSection(new Section(
                                    degree.getTitle(),
                                    generateSectionId("degree-", degree.getTitle(), ""),
                                    Degree.class));
                        }
                        section.addSubSection(institutionSection);
                    }
                    sectionList.add(section);
                } else if (Awards.class.equals(compositeElementClass)) {
                    String sectionName = getAwardsSectionName();
                    String sectionId = getAwardsSectionId();
                    sectionList.add(new Section(sectionName, sectionId, Awards.class));
                } else if (RelevantCourses.class.equals(compositeElementClass)) {
                    String sectionName = getRelevantCoursesSectionName();
                    String sectionId = getRelevantCoursesSectionId();
                    sectionList.add(new Section(sectionName, sectionId, RelevantCourses.class));
                } else if (Projects.class.equals(compositeElementClass)) {
                    String sectionName = getProjectsSectionName();
                    String sectionId = getProjectsSectionId();
                    sectionList.add(new Section(sectionName, sectionId, Projects.class));
                } else if (Experience.class.equals(compositeElementClass)) {
                    Experience experience = (Experience) compositeElement.getValue();
                    String sectionName = getExperienceSectionName();
                    String sectionId = getExperienceSectionId();

                    Section section = new Section(sectionName, sectionId, Experience.class);
                    for (Job job : experience.getJobs()) {
                        String subSectionName = job.getEmployer();
                        String subSectionId = generateSectionId("employer-", job.getEmployer(), "");

                        Section jobSection = new Section(subSectionName, subSectionId, Job.class);
                        for (Role role : job.getRoles()) {
                            jobSection.addSubSection(new Section(
                                    role.getTitle(),
                                    generateSectionId("role-", role.getTitle(), ""),
                                    Role.class));
                        }
                        section.addSubSection(jobSection);
                    }
                    sectionList.add(section);
                }
            }
        }

        return sectionList;
    }

    public String generateSectionId(String prefix, String name, String postfix) {
        StringBuilder sb = new StringBuilder(prefix);
        sb.append(name.toLowerCase().replaceAll("[^0-9a-z]", ""));
        sb.append(postfix);
        return sb.toString();
    }

    public ResumeParser getResume() {
        return resume;
    }

    public String formatCamelCaseName(String camelCase) {
        Matcher matcher = CAMEL_CASE_PATTERN.matcher(camelCase);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String replacement;
            if (matcher.hitEnd())
                replacement = "$1";
            else
                replacement = "$1 ";
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    public Boolean getShowPersonalDataOnWeb() {
        return showPersonalDataOnWeb;
    }

    public String getObjectiveSectionName() {
        return formatCamelCaseName(Objective.class.getSimpleName());
    }

    public String getObjectiveSectionId() {
        return generateSectionId("section", Objective.class.getSimpleName(), "");
    }

    public String getSummaryOfQualificationsSectionName() {
        return formatCamelCaseName(SummaryOfQualifications.class.getSimpleName());
    }

    public String getSummaryOfQualificationsSectionId() {
        return generateSectionId("section", SummaryOfQualifications.class.getSimpleName(), "");
    }

    public String getEducationSectionName() {
        return formatCamelCaseName(Education.class.getSimpleName());
    }

    public String getEducationSectionId() {
        return generateSectionId("section", Education.class.getSimpleName(), "");
    }

    public String getAwardsSectionName() {
        return formatCamelCaseName(Awards.class.getSimpleName());
    }

    public String getAwardsSectionId() {
        return generateSectionId("section", Awards.class.getSimpleName(), "");
    }

    public String getRelevantCoursesSectionName() {
        return formatCamelCaseName(RelevantCourses.class.getSimpleName());
    }

    public String getRelevantCoursesSectionId() {
        return generateSectionId("section", RelevantCourses.class.getSimpleName(), "");
    }

    public String getProjectsSectionName() {
        return formatCamelCaseName(Projects.class.getSimpleName());
    }

    public String getProjectsSectionId() {
        return generateSectionId("section", Projects.class.getSimpleName(), "");
    }

    public String getExperienceSectionName() {
        return formatCamelCaseName(Experience.class.getSimpleName());
    }

    public String getExperienceSectionId() {
        return generateSectionId("section", Experience.class.getSimpleName(), "");
    }

    public String getResumePdfPageUrl() {
        return "resume.pdf";
    }
}
