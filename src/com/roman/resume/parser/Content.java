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

package com.roman.resume.parser;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class Content extends ResumeElement {
  public enum SectionNames {
    OBJECTIVE, SUMMARY_OF_QUALIFICATIONS, EDUCATION, AWARDS, RELEVANT_COURSES, PROJECTS, EXPERIENCE
  }

  public Objective objective = new Objective(this, parser);
  public SummaryOfQualifications summaryOfQualifications = new SummaryOfQualifications(this, parser);
  public Education education = new Education(this, parser);
  public Awards awards = new Awards(this, parser);
  public RelevantCourses relevantCourses = new RelevantCourses(this, parser);
  public Projects projects = new Projects(this, parser);
  public Experience experience = new Experience(this, parser);

  public ArrayList<SectionNames> sectionOrder = new ArrayList<SectionNames>();

  public Content(DefaultHandler parent, XMLReader parser) {
    super(parent, parser);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equals("objective")) {
      sectionOrder.add(SectionNames.OBJECTIVE);
      if (include(attributes)) {
        System.out.println("START objective");
        parser.setContentHandler(objective);
      } else {
        objective = null;
      }
    }

    if (qName.equals("summary_of_qualifications")) {
      sectionOrder.add(SectionNames.SUMMARY_OF_QUALIFICATIONS);
      if (include(attributes)) {
        System.out.println("START summary_of_qualifications");
        parser.setContentHandler(summaryOfQualifications);
      } else {
        summaryOfQualifications = null;
      }
    }

    if (qName.equals("education")) {
      sectionOrder.add(SectionNames.EDUCATION);
      if (include(attributes)) {
        System.out.println("START education");
        parser.setContentHandler(education);
      } else {
        education = null;
      }
    }

    if (qName.equals("awards")) {
      sectionOrder.add(SectionNames.AWARDS);
      if (include(attributes)) {
        System.out.println("START awards");
        parser.setContentHandler(awards);
      } else {
        awards = null;
      }
    }

    if (qName.equals("relevant_courses")) {
      sectionOrder.add(SectionNames.RELEVANT_COURSES);
      if (include(attributes)) {
        System.out.println("START relevant_courses");
        parser.setContentHandler(relevantCourses);
      } else {
        relevantCourses = null;
      }
    }

    if (qName.equals("projects")) {
      sectionOrder.add(SectionNames.PROJECTS);
      if (include(attributes)) {
        System.out.println("START projects");
        parser.setContentHandler(projects);
      } else {
        projects = null;
      }
    }

    if (qName.equals("experience")) {
      sectionOrder.add(SectionNames.EXPERIENCE);
      if (include(attributes)) {
        System.out.println("START experience");
        parser.setContentHandler(experience);
      } else {
        experience = null;
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("content")) {
      System.out.println("END content");
      parser.setContentHandler(parent);
    }
  }
}
