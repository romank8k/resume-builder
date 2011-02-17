// Copyright (c) 2010, Roman Khmelichek
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
//  1. Redistributions of source code must retain the above copyright notice,
//     this list of conditions and the following disclaimer.
//  2. Redistributions in binary form must reproduce the above copyright notice,
//     this list of conditions and the following disclaimer in the documentation
//     and/or other materials provided with the distribution.
//  3. Neither the name of Roman Khmelichek nor the names of its contributors
//     may be used to endorse or promote products derived from this software
//     without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR IMPLIED
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
// EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
// PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
// OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
// WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
// OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
// ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package com.roman.resume;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.roman.resume.parser.Award;
import com.roman.resume.parser.Content;
import com.roman.resume.parser.Institution;
import com.roman.resume.parser.Job;
import com.roman.resume.parser.Resume;
import com.roman.resume.parser.SnippetState;

public class ResumeGenerator {
  private Resume resume;
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

  public ResumeGenerator(String filename) {
    try {
      resume = new Resume(filename);
    } catch (Exception e) {
      e.printStackTrace();
    }

    initFonts();
    initSpacings();

    Document document = new Document(PageSize.LETTER, resume.marginLeft, resume.marginRight, resume.marginTop, resume.marginBottom);

    try {
      // We assume the input filename has some sort of extension,
      // such as ".xml" and our output file will have the same name,
      // but with the extension ".pdf".
      String outputFile = filename.substring(0, filename.lastIndexOf('.')) + ".pdf";
      PdfWriter.getInstance(document, new FileOutputStream(outputFile));
      document.open();
      document.add(getHeader());
      document.add(getSeparator());
      document.add(getContent());

    } catch (DocumentException de) {
      de.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

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

  public Element getHeader() throws DocumentException {
    final float nameAndHomepagePadding = 6f;
    final float infoPadding = 2f;

    PdfPTable headerTable = getDefaultTable(2);

    // Name.
    PdfPCell name = getLeftAlignedCell(new Phrase(resume.header.name, headerNameFont));
    name.setPaddingBottom(nameAndHomepagePadding);
    headerTable.addCell(name);

    // Homepage.
    if (resume.header.homepage == null) {
      resume.header.homepage = new String();
    }
    Anchor homepageAnchor = new Anchor(resume.header.homepage, headerHomepageFont);
    homepageAnchor.setReference(resume.header.homepage);
    PdfPCell homepage = getRightAlignedCell(homepageAnchor);
    homepage.setVerticalAlignment(Element.ALIGN_BOTTOM);
    homepage.setPadding(nameAndHomepagePadding);
    headerTable.addCell(homepage);

    // Info.
    PdfPCell topLeftCell = getLeftAlignedCell(new Phrase());
    PdfPCell topRightCell = getRightAlignedCell(new Phrase());
    PdfPCell bottomLeftCell = getLeftAlignedCell(new Phrase());
    PdfPCell bottomRightCell = getRightAlignedCell(new Phrase());

    if (resume.header.phone != null) {
      topLeftCell = getLeftAlignedCell(new Phrase(resume.header.phone, headerInfoFont));
    }

    if (resume.header.email != null) {
      Anchor emailAnchor = new Anchor(resume.header.email, headerInfoFont);
      emailAnchor.setReference("mailto:" + resume.header.email);
      bottomLeftCell = getLeftAlignedCell(emailAnchor);
    }

    if (resume.header.address != null) {
      topRightCell = getRightAlignedCell(new Phrase(
          (resume.header.address.street + (!resume.header.address.apartment.isEmpty() ? (", " + resume.header.address.apartment) : "")), headerInfoFont));
      bottomRightCell = getRightAlignedCell(new Phrase((resume.header.address.city + ", " + resume.header.address.state + " " + resume.header.address.zip),
          headerInfoFont));
    }

    boolean fullInfo = false;
    boolean halfInfo = false;
    if (resume.header.phone == null && resume.header.email == null) {
      if (resume.header.address != null) {
        fullInfo = true;
      }
    } else if (resume.header.phone != null && resume.header.email == null) {
      if (resume.header.address != null) {
        fullInfo = true;
      } else {
        halfInfo = true;
      }
    } else if (resume.header.phone == null && resume.header.email != null) {
      topLeftCell = bottomLeftCell;
      bottomLeftCell = getLeftAlignedCell(new Phrase());
      if (resume.header.address != null) {
        fullInfo = true;
      } else {
        halfInfo = true;
      }
    } else if (resume.header.phone != null && resume.header.email != null) {
      fullInfo = true;
    }

    if (fullInfo) {
      topLeftCell.setPaddingBottom(infoPadding);
      headerTable.addCell(topLeftCell);
      topRightCell.setPaddingBottom(infoPadding);
      headerTable.addCell(topRightCell);
      headerTable.addCell(bottomLeftCell);
      headerTable.addCell(bottomRightCell);
    } else if (halfInfo) {
      headerTable.addCell(topLeftCell);
      headerTable.addCell(topRightCell);
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

  public Element getContent() throws DocumentException {
    PdfPTable contentTable = getDefaultTable(2);

    // We display sections in the order in which they have been defined in the XML file.
    for (Content.SectionNames section : resume.content.sectionOrder) {
      switch (section) {
        case OBJECTIVE:
          objective(contentTable);
          break;
        case SUMMARY_OF_QUALIFICATIONS:
          summaryOfQualifications(contentTable);
          break;
        case EDUCATION:
          education(contentTable);
          break;
        case AWARDS:
          awards(contentTable);
          break;
        case RELEVANT_COURSES:
          relevantCourses(contentTable);
          break;
        case PROJECTS:
          projects(contentTable);
          break;
        case EXPERIENCE:
          experience(contentTable);
          break;
        default:
          break;
      }
    }

    // Set column widths.
    contentTable.setWidths(new int[] { 20, 80 });
    return contentTable;
  }

  public PdfPCell getContentBlockTitle(String title) {
    PdfPCell titleCell = getDefaultCell(new Phrase(title, contentBlockTitleFont));
    titleCell.setPaddingTop(contentBlockPadding);
    return titleCell;
  }

  public PdfPCell getBulletedList(ArrayList<ArrayList<SnippetState.Snippet>> snippetedList) {
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

  public ArrayList<Element> getFormattedSnippets(ArrayList<SnippetState.Snippet> snippets) {
    ArrayList<Element> elements = new ArrayList<Element>();
    for (SnippetState.Snippet snippet : snippets) {
      Element currChunk;
      switch (snippet.modifier) {
        case HYPERLINK:
          currChunk = new Anchor(snippet.text, contentFont);
          ((Anchor) currChunk).setReference(snippet.text);
          break;
        case BOLD:
          currChunk = new Chunk(snippet.text, contentBoldFont);
          break;
        case NONE:
          currChunk = new Chunk(snippet.text, contentFont);
          break;
        default:
          currChunk = new Chunk(snippet.text, contentFont);
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

  public void objective(PdfPTable contentTable) throws DocumentException {
    if (resume.content.objective != null) {
      contentTable.addCell(getContentBlockTitle("OBJECTIVE"));
      PdfPCell objectiveCell = getDefaultCell();

      for (Element element : getFormattedSnippets(resume.content.objective.snippets)) {
        objectiveCell.addElement(element);
      }

      applyListPaddingTop(objectiveCell);
      contentTable.addCell(objectiveCell);
    }
  }

  public void summaryOfQualifications(PdfPTable contentTable) throws DocumentException {
    if (resume.content.summaryOfQualifications != null) {
      contentTable.addCell(getContentBlockTitle("SUMMARY OF QUALIFICATIONS"));

      PdfPCell qualificationsCell = getBulletedList(resume.content.summaryOfQualifications.qualifications);
      applyListPaddingTop(qualificationsCell);
      contentTable.addCell(qualificationsCell);
    }
  }

  public void education(PdfPTable contentTable) throws DocumentException {
    if (resume.content.education != null) {

      for (int i = 0; i < resume.content.education.institutions.size(); i++) {
        Institution institution = resume.content.education.institutions.get(i);

        if (i == 0)
          contentTable.addCell(getContentBlockTitle("EDUCATION"));
        else
          contentTable.addCell(getContentBlockTitle(""));

        PdfPTable educationTable = getDefaultTable(1);

        Phrase educationPhrase = new Phrase();
        educationPhrase.add(new Chunk(institution.name, contentBoldFont));
        educationPhrase.add(new Chunk((", " + institution.location), contentFont));

        PdfPCell educationCell = getDefaultCell(educationPhrase);
        educationCell.setPaddingBottom(contentSubHeaderPadding);
        educationTable.addCell(educationCell);

        for (int j = 0; j < institution.details.size(); j++) {
          String detail = institution.details.get(j);
          PdfPCell detailCell = getDefaultCell(new Phrase(detail, contentFont));

          if (j < institution.details.size() - 1)
            detailCell.setPaddingBottom(contentTablePadding);
          else if (i < resume.content.education.institutions.size() - 1)
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

  public void relevantCourses(PdfPTable contentTable) throws DocumentException {
    if (resume.content.relevantCourses != null) {
      contentTable.addCell(getContentBlockTitle("RELEVANT COURSES"));

      PdfPTable relevantCoursesTable = getDefaultTable(2);
      for (int i = 0; i < resume.content.relevantCourses.courses.size(); i++) {
        String course = resume.content.relevantCourses.courses.get(i);
        PdfPCell courseCell = getDefaultCell(new Phrase(course, contentFont));

        // Don't want to set extra padding on the last row of the
        // relevant courses.
        if (!isLastRow(i, resume.content.relevantCourses.courses.size(), 2)) {
          courseCell.setPaddingBottom(contentTablePadding);
        }

        relevantCoursesTable.addCell(courseCell);

        // Need to add an extra empty cell if the last row is not complete.
        if (i == resume.content.relevantCourses.courses.size() - 1 && i % 2 != 1) {
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

  public void awards(PdfPTable contentTable) throws DocumentException {
    if (resume.content.awards != null) {

      contentTable.addCell(getContentBlockTitle("AWARDS"));

      PdfPTable awardsTable = getDefaultTable(2);
      for (int i = 0; i < resume.content.awards.awards.size(); i++) {
        Award award = resume.content.awards.awards.get(i);

        PdfPCell awardName = getLeftAlignedCell(new Phrase(award.title, contentFont));
        PdfPCell awardTimespan = getRightAlignedCell(new Phrase(award.timespan, contentFont));

        // Don't want to set extra padding on the last row of the awards table.
        if (!isLastRow(i, resume.content.awards.awards.size(), 1)) {
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

  public void projects(PdfPTable contentTable) throws DocumentException {
    if (resume.content.projects != null) {
      contentTable.addCell(getContentBlockTitle("PROJECTS"));

      PdfPCell projectsCell = getBulletedList(resume.content.projects.projects);
      applyListPaddingTop(projectsCell);
      contentTable.addCell(projectsCell);
    }
  }

  public void experience(PdfPTable contentTable) throws DocumentException {
    if (resume.content.experience != null) {
      for (int i = 0; i < resume.content.experience.jobs.size(); i++) {
        if (i == 0)
          contentTable.addCell(getContentBlockTitle("EXPERIENCE"));
        else
          contentTable.addCell(getContentBlockTitle(""));

        Job job = resume.content.experience.jobs.get(i);

        PdfPTable experienceTable = getDefaultTable(1);

        PdfPTable table = getDefaultTable(2);

        Phrase employerPhrase = new Phrase();
        employerPhrase.add(new Chunk(job.employer, contentBoldFont));
        if (job.department.length() != 0)
          employerPhrase.add(new Chunk((", " + job.department), contentFont));
        PdfPCell employerCell = getLeftAlignedCell(employerPhrase);
        employerCell.setPaddingBottom(contentSubHeaderPadding);
        table.addCell(employerCell);

        PdfPCell timespanCell = getRightAlignedCell(new Phrase(job.timespan, contentFont));
        timespanCell.setPaddingBottom(contentSubHeaderPadding);
        table.addCell(timespanCell);

        table.setWidths(new float[] { 70.0f, 30.0f });
        experienceTable.addCell(table);

        if (job.role.length() != 0) {
          PdfPCell roleCell = getLeftAlignedCell(new Phrase(job.role, contentItalicFont));
          roleCell.setPaddingBottom(contentSubSubHeaderPadding);
          experienceTable.addCell(roleCell);
        }

        if (job.project.length() != 0) {
          PdfPCell projectCell = getLeftAlignedCell(new Phrase(job.project, contentItalicFont));
          projectCell.setPaddingBottom(contentSubSubHeaderPadding);
          experienceTable.addCell(projectCell);
        }

        PdfPCell listCell = getBulletedList(job.accomplishments);
        if (i < resume.content.experience.jobs.size() - 1)
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

  public static void main(String[] args) {
    if (args.length > 0) {
      new ResumeGenerator(args[0]);
    } else {
      new ResumeGenerator("resume.xml");
    }
  }
}
