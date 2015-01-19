package me.romankh.resumegenerator.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import me.romankh.resumegenerator.configuration.Property;
import me.romankh.resumegenerator.parser.*;
import me.romankh.resumegenerator.service.ResumeGeneratorService;
import me.romankh.resumegenerator.annotations.binding.IsWebServer;
import me.romankh.resumegenerator.configuration.Prop;
import me.romankh.resumegenerator.service.ResumeCachingFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;
/**
 * This class is kept here as its a good example of using iText, but it is too inflexible, and difficult to customize.
 * The new FO stylesheet implementation @see {@link ResumeGeneratorXSLTImpl} is now the
 * preferred method of PDF generation.
 *
 * @author Roman Khmelichek
 */
@Deprecated
public class ResumeGeneratorITextImpl implements ResumeGeneratorService {
  @SuppressWarnings("deprecation")
  private static final Logger logger = LogManager.getLogger(ResumeGeneratorITextImpl.class);

  private boolean isWebServer;
  private final Boolean showPersonalDataOnWeb;
  private final ResumeCachingFactory resumeCachingFactory;

  private Font contentFont, contentBoldFont, contentItalicFont, contentBlockTitleFont, headerInfoFont, headerNameFont, headerHomepageFont, bulletFont;
  private Chunk bulletSymbol;

  // Spacing settings.
  private float contentLeading;
  private float contentBlockPadding;
  private float contentSubBlockPadding;
  private float contentTablePadding;
  private float contentListSpacing;
  private float contentSubHeaderPadding;
  private float contentSubSubHeaderPadding;

  @Inject
  public ResumeGeneratorITextImpl(@IsWebServer boolean isWebServer,
                                  @Prop(Property.SHOW_PERSONAL_DATA_ON_WEB) Boolean showPersonalDataOnWeb,
                                  ResumeCachingFactory resumeCachingFactory) throws FileNotFoundException {
    this.isWebServer = isWebServer;
    this.showPersonalDataOnWeb = showPersonalDataOnWeb;
    this.resumeCachingFactory = resumeCachingFactory;
  }

  public void render(OutputStream out) throws Exception {
    initFonts();
    initSpacings();

    Resume resume = resumeCachingFactory.getResume();

    Document document = new Document(PageSize.LETTER, resume.marginLeft, resume.marginRight, resume.marginTop, resume.marginBottom);
    PdfWriter.getInstance(document, out);
    document.open();
    document.add(getHeader(resume));
    document.add(getSeparator());
    document.add(getContent(resume));
    document.close();
  }

  public void initFonts() {
    FontFactory.registerDirectories();
    contentFont = FontFactory.getFont("Georgia", 10, Font.NORMAL);
    contentBoldFont = FontFactory.getFont("Georgia", 10, Font.BOLD);
    contentItalicFont = FontFactory.getFont("Georgia", 10, Font.ITALIC);
    contentBlockTitleFont = FontFactory.getFont("Trebuchet MS", 12, Font.BOLD);
    headerInfoFont = FontFactory.getFont("Trebuchet MS", 12, Font.NORMAL);
    headerNameFont = FontFactory.getFont("Trebuchet MS", 18, Font.BOLD);
    headerHomepageFont = FontFactory.getFont("Trebuchet MS", 12, Font.ITALIC);
    bulletFont = FontFactory.getFont("Georgia", 12, Font.BOLD);

    bulletSymbol = new Chunk("\u2022  ", bulletFont);
  }

  public void initSpacings() {
    contentBlockPadding = 16f;
    contentSubBlockPadding = 10f;
    contentLeading = 4f;
    contentTablePadding = 4f;
    contentListSpacing = 0f;
    contentSubHeaderPadding = 3f;
    contentSubSubHeaderPadding = 3f;
  }

  public void setCellDefaults(PdfPCell cell) {
    cell.setBorder(Rectangle.NO_BORDER);
    cell.setPadding(0);
  }

  public PdfPCell getDefaultCell() {
    PdfPCell cell = new PdfPCell();
    setCellDefaults(cell);
    return cell;
  }

  public PdfPCell getDefaultCell(Phrase p) {
    PdfPCell cell = getDefaultCell();
    cell.setPhrase(p);
    return cell;
  }

  public PdfPTable getDefaultTable(int numColumns) {
    PdfPTable table = new PdfPTable(numColumns);
    table.setWidthPercentage(100);
    setCellDefaults(table.getDefaultCell());
    return table;
  }

  public PdfPCell getLeftAlignedCell() {
    PdfPCell cell = getDefaultCell();
    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    return cell;
  }

  public PdfPCell getLeftAlignedCell(Phrase p) {
    PdfPCell cell = getDefaultCell(p);
    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    return cell;
  }

  public PdfPCell getRightAlignedCell() {
    PdfPCell cell = getDefaultCell();
    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    return cell;
  }

  public PdfPCell getRightAlignedCell(Phrase p) {
    PdfPCell cell = getDefaultCell(p);
    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    return cell;
  }

  public Element getHeader(Resume resume) throws DocumentException {
    final float nameAndHomepagePadding = 6f;
    final float infoPadding = 2f;

    PdfPTable headerTable = getDefaultTable(2);

    // Name.
    PdfPCell name = getLeftAlignedCell(new Phrase(resume.getHeader().getName(), headerNameFont));
    name.setPaddingBottom(nameAndHomepagePadding);
    headerTable.addCell(name);

    // Homepage.
    Anchor homepageAnchor = new Anchor(resume.getHeader().getHomepage(), headerHomepageFont);
    homepageAnchor.setReference(resume.getHeader().getHomepage());
    PdfPCell homepage = getRightAlignedCell(homepageAnchor);
    homepage.setVerticalAlignment(Element.ALIGN_BOTTOM);
    homepage.setPadding(nameAndHomepagePadding);
    headerTable.addCell(homepage);

    // Info.
    PdfPCell topLeftCell = getLeftAlignedCell(new Phrase());
    PdfPCell topRightCell = getRightAlignedCell(new Phrase());
    PdfPCell bottomLeftCell = getLeftAlignedCell(new Phrase());
    PdfPCell bottomRightCell = getRightAlignedCell(new Phrase());

    if (resume.getHeader().getPhone() != null) {
      topLeftCell = getLeftAlignedCell(new Phrase(resume.getHeader().getPhone(), headerInfoFont));
    }

    if (resume.getHeader().getEmail() != null) {
      Anchor emailAnchor = new Anchor(resume.getHeader().getEmail(), headerInfoFont);
      emailAnchor.setReference("mailto:" + resume.getHeader().getEmail());
      bottomLeftCell = getLeftAlignedCell(emailAnchor);
    }

    if (resume.getHeader().getAddress() != null) {
      topRightCell = getRightAlignedCell(new Phrase(
          (resume.getHeader().getAddress().getStreet() + (!resume.getHeader().getAddress().getApartment().isEmpty() ? (", " +
              "" + resume.getHeader().getAddress().getApartment()) : "")), headerInfoFont));
      bottomRightCell = getRightAlignedCell(new Phrase((resume.getHeader().getAddress().getCity() + ", " +
          "" + resume.getHeader().getAddress().getState() + " " + resume.getHeader().getAddress().getZip()),
          headerInfoFont));
    }

    if (!isWebServer || showPersonalDataOnWeb) {
      topLeftCell.setPaddingBottom(infoPadding);
      headerTable.addCell(topLeftCell);
      topRightCell.setPaddingBottom(infoPadding);
      headerTable.addCell(topRightCell);
      headerTable.addCell(bottomLeftCell);
      headerTable.addCell(bottomRightCell);
    }

    return headerTable;
  }

  public Element getSeparator() throws DocumentException {
    Paragraph separator = new Paragraph(10);
    separator.setSpacingBefore(0);
    separator.setSpacingAfter(10);
    separator.add(new Chunk(new LineSeparator(2.6f, 100, BaseColor.BLACK, Element.ALIGN_JUSTIFIED, 0)));
    return separator;
  }

  public Element getContent(Resume resume) throws DocumentException {
    PdfPTable contentTable = getDefaultTable(2);

    // We display sections in the order in which they have been defined in the XML file.
    for (ResumeElement.AbstractElement element : resume.getContent().getElementOrderList()) {
      ResumeElement.CompositeElement compositeElement = resume.getContent().getCompositeElementByName(element.getName());

      if (compositeElement != null) {
        Class compositeElementClass = compositeElement.getClazz();
        if (Objective.class.equals(compositeElementClass)) {
          objective(resume, contentTable);
        } else if (SummaryOfQualifications.class.equals(compositeElementClass)) {
          summaryOfQualifications(resume, contentTable);
        } else if (Education.class.equals(compositeElementClass)) {
          education(resume, contentTable);
        } else if (Awards.class.equals(compositeElementClass)) {
          awards(resume, contentTable);
        } else if (RelevantCourses.class.equals(compositeElementClass)) {
          relevantCourses(resume, contentTable);
        } else if (Projects.class.equals(compositeElementClass)) {
          projects(resume, contentTable);
        } else if (Experience.class.equals(compositeElementClass)) {
          experience(resume, contentTable);
        }
      }
    }

    // Set column widths.
    contentTable.setWidths(new int[]{20, 80});
    return contentTable;
  }

  public PdfPCell getContentBlockTitle(String title) {
    PdfPCell titleCell = getDefaultCell(new Phrase(title, contentBlockTitleFont));
    titleCell.setPaddingTop(contentBlockPadding);
    return titleCell;
  }

  public PdfPCell getBulletedList(java.util.List<java.util.List<SnippetElement.Snippet>> snippetedList) {
    PdfPCell listCell = getDefaultCell();
    List list = new List();
    list.setListSymbol(bulletSymbol);

    for (int i = 0; i < snippetedList.size(); i++) {
      ListItem item = new ListItem();
      item.setLeading(contentLeading, 1);

      for (Element element : getFormattedSnippets(snippetedList.get(i))) {
        item.add(element);
      }

      if (i < snippetedList.size() - 1)
        item.setSpacingAfter(contentListSpacing);

      list.add(item);
    }

    listCell.addElement(list);
    return listCell;
  }

  public java.util.List<Element> getFormattedSnippets(java.util.List<SnippetElement.Snippet> snippets) {
    java.util.List<Element> elements = new ArrayList<>();
    for (SnippetElement.Snippet snippet : snippets) {
      java.util.List<SnippetElement.Snippet.Modifier> modifierList = snippet.getOrderedModifierList();

      Font font;
      if (modifierList.contains(SnippetElement.Snippet.Modifier.BOLD)) {
        font = contentBoldFont;
      } else {
        font = contentFont;
      }

      Element currChunk;
      if (modifierList.contains(SnippetElement.Snippet.Modifier.HYPERLINK)) {
        currChunk = new Anchor(snippet.getText(), font);
        ((Anchor) currChunk).setReference(snippet.getText());
      } else {
        currChunk = new Chunk(snippet.getText(), font);
      }

      elements.add(currChunk);
    }
    return elements;
  }

  public void applyListPaddingTop(PdfPCell listCell) {
    // Makes content block title and adjacent content details line up
    // correctly. An offset is needed due to the list leading creating extra
    // space at the top.
    final float paddingOffset = 2.0f;
    listCell.setPaddingTop(contentBlockPadding - paddingOffset);
  }

  public void objective(Resume resume, PdfPTable contentTable) throws DocumentException {
    if (resume.getContent().getObjective() != null) {
      contentTable.addCell(getContentBlockTitle("OBJECTIVE"));
      PdfPCell objectiveCell = getDefaultCell();

      for (Element element : getFormattedSnippets(resume.getContent().getObjective().snippets)) {
        objectiveCell.addElement(element);
      }

      applyListPaddingTop(objectiveCell);
      contentTable.addCell(objectiveCell);
    }
  }

  public void summaryOfQualifications(Resume resume, PdfPTable contentTable) throws DocumentException {
    if (resume.getContent().getSummaryOfQualifications() != null) {
      contentTable.addCell(getContentBlockTitle("SUMMARY OF QUALIFICATIONS"));

      PdfPCell qualificationsCell = getBulletedList(resume.getContent().getSummaryOfQualifications().getQualifications());
      applyListPaddingTop(qualificationsCell);
      contentTable.addCell(qualificationsCell);
    }
  }

  public void education(Resume resume, PdfPTable contentTable) throws DocumentException {
    if (resume.getContent().getEducation() != null) {

      for (int i = 0; i < resume.getContent().getEducation().getInstitutions().size(); i++) {
        Institution institution = resume.getContent().getEducation().getInstitutions().get(i);

        if (i == 0)
          contentTable.addCell(getContentBlockTitle("EDUCATION"));
        else
          contentTable.addCell(getContentBlockTitle(""));

        PdfPTable educationTable = getDefaultTable(1);

        Phrase educationPhrase = new Phrase();
        educationPhrase.add(new Chunk(institution.getName(), contentBoldFont));
        educationPhrase.add(new Chunk((", " + institution.getLocation()), contentFont));

        PdfPCell educationCell = getDefaultCell(educationPhrase);
        educationCell.setPaddingBottom(contentSubHeaderPadding);
        educationTable.addCell(educationCell);

        for (int j = 0; j < institution.getDetails().size(); j++) {
          String detail = institution.getDetails().get(j);
          PdfPCell detailCell = getDefaultCell(new Phrase(detail, contentFont));

          if (j < institution.getDetails().size() - 1)
            detailCell.setPaddingBottom(contentTablePadding);
          else if (i < resume.getContent().getEducation().getInstitutions().size() - 1)
            detailCell.setPaddingBottom(contentSubBlockPadding);

          educationTable.addCell(detailCell);
        }

        PdfPCell educationWrapper = getDefaultCell();
        if (i == 0)
          educationWrapper.setPaddingTop(contentBlockPadding);
        educationWrapper.addElement(educationTable);
        contentTable.addCell(educationWrapper);
      }
    }
  }

  public void relevantCourses(Resume resume, PdfPTable contentTable) throws DocumentException {
    if (resume.getContent().getRelevantCourses() != null) {
      contentTable.addCell(getContentBlockTitle("RELEVANT COURSES"));

      PdfPTable relevantCoursesTable = getDefaultTable(2);
      for (int i = 0; i < resume.getContent().getRelevantCourses().getCourses().size(); i++) {
        String course = resume.getContent().getRelevantCourses().getCourses().get(i);
        PdfPCell courseCell = getDefaultCell(new Phrase(course, contentFont));

        // Don't want to set extra padding on the last row of the
        // relevant courses.
        if (!isLastRow(i, resume.getContent().getRelevantCourses().getCourses().size(), 2)) {
          courseCell.setPaddingBottom(contentTablePadding);
        }

        relevantCoursesTable.addCell(courseCell);

        // Need to add an extra empty cell if the last row is not complete.
        if (i == resume.getContent().getRelevantCourses().getCourses().size() - 1 && i % 2 != 1) {
          PdfPCell extraCell = getDefaultCell(new Phrase("", contentFont));
          relevantCoursesTable.addCell(extraCell);
        }
      }

      PdfPCell relevantCoursesTableWrapper = getDefaultCell();
      relevantCoursesTableWrapper.setPaddingTop(contentBlockPadding);
      relevantCoursesTableWrapper.addElement(relevantCoursesTable);
      contentTable.addCell(relevantCoursesTableWrapper);
    }
  }

  /**
   * Determines whether an item is in the last row of the table.
   * 
   * @param curr
   *          The index of the item to be tested [0, size).
   * @param size
   *          The total number of items in the table.
   * @param numColumns
   *          The number of columns of the table.
   * @return True if the item <code>curr</code> is in the last row of the resulting table with <code>numColumns</code> and <code>size</code> items.
   */
  public boolean isLastRow(int curr, int size, int numColumns) {
    int lastRowItems = (size % numColumns) == 0 ? numColumns : (size % numColumns);
    if (curr < size - lastRowItems) {
      return false;
    }
    return true;
  }

  public void awards(Resume resume, PdfPTable contentTable) throws DocumentException {
    if (resume.getContent().getAwards() != null) {

      contentTable.addCell(getContentBlockTitle("AWARDS"));

      PdfPTable awardsTable = getDefaultTable(2);
      for (int i = 0; i < resume.getContent().getAwards().getAwards().size(); i++) {
        Award award = resume.getContent().getAwards().getAwards().get(i);

        PdfPCell awardName = getLeftAlignedCell(new Phrase(award.getTitle(), contentFont));
        PdfPCell awardTimespan = getRightAlignedCell(new Phrase(award.getTimespan(), contentFont));

        // Don't want to set extra padding on the last row of the awards table.
        if (!isLastRow(i, resume.getContent().getAwards().getAwards().size(), 1)) {
          awardName.setPaddingBottom(contentTablePadding);
          awardTimespan.setPaddingBottom(contentTablePadding);
        }

        awardsTable.addCell(awardName);
        awardsTable.addCell(awardTimespan);
      }

      PdfPCell awardsTableWrapper = getDefaultCell();
      awardsTableWrapper.setPaddingTop(contentBlockPadding);
      awardsTableWrapper.addElement(awardsTable);
      contentTable.addCell(awardsTableWrapper);
    }
  }

  public void projects(Resume resume, PdfPTable contentTable) throws DocumentException {
    if (resume.getContent().getProjects() != null) {
      contentTable.addCell(getContentBlockTitle("PROJECTS"));

      PdfPCell projectsCell = getBulletedList(resume.getContent().getProjects().getProjects());
      applyListPaddingTop(projectsCell);
      contentTable.addCell(projectsCell);
    }
  }

  public void experience(Resume resume, PdfPTable contentTable) throws DocumentException {
    if (resume.getContent().getExperience() != null) {
      for (int i = 0; i < resume.getContent().getExperience().getJobs().size(); i++) {
        if (i == 0)
          contentTable.addCell(getContentBlockTitle("EXPERIENCE"));
        else
          contentTable.addCell(getContentBlockTitle(""));

        Job job = resume.getContent().getExperience().getJobs().get(i);

        PdfPTable experienceTable = getDefaultTable(1);

        PdfPTable table = getDefaultTable(2);

        Phrase employerPhrase = new Phrase();
        employerPhrase.add(new Chunk(job.getEmployer(), contentBoldFont));
        if (job.getDepartment().length() != 0)
          employerPhrase.add(new Chunk((", " + job.getDepartment()), contentFont));
        PdfPCell employerCell = getLeftAlignedCell(employerPhrase);
        employerCell.setPaddingBottom(contentSubHeaderPadding);
        table.addCell(employerCell);

        PdfPCell timespanCell = getRightAlignedCell(new Phrase(job.getTimespan(), contentFont));
        timespanCell.setPaddingBottom(contentSubHeaderPadding);
        table.addCell(timespanCell);

        table.setWidths(new float[] { 70.0f, 30.0f });
        experienceTable.addCell(table);

        if (job.getRole().length() != 0) {
          PdfPCell roleCell = getLeftAlignedCell(new Phrase(job.getRole(), contentItalicFont));
          roleCell.setPaddingBottom(contentSubSubHeaderPadding);
          experienceTable.addCell(roleCell);
        }

        if (job.getProject().length() != 0) {
          PdfPCell projectCell = getLeftAlignedCell(new Phrase(job.getProject(), contentItalicFont));
          projectCell.setPaddingBottom(contentSubSubHeaderPadding);
          experienceTable.addCell(projectCell);
        }

        PdfPCell listCell = getBulletedList(job.getAccomplishments());
        if (i < resume.getContent().getExperience().getJobs().size() - 1)
          listCell.setPaddingBottom(contentSubBlockPadding);
        experienceTable.addCell(listCell);

        PdfPCell experienceWrapper = getDefaultCell();
        if (i == 0)
          experienceWrapper.setPaddingTop(contentBlockPadding);
        experienceWrapper.addElement(experienceTable);
        contentTable.addCell(experienceWrapper);
      }
    }
  }
}
