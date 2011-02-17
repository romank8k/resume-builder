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

import com.roman.resume.parser.SnippetState.Snippet;

public class Job extends ResumeElement {
  public String employer = new String();
  private boolean employerP = false;
  public String department = new String();
  private boolean departmentP = false;
  public String timespan = new String();
  private boolean timespanP = false;
  public String role = new String();
  private boolean roleP = false;
  public String project = new String();
  private boolean projectP = false;

  private SnippetState snippetState = new SnippetState();

  public ArrayList<ArrayList<Snippet>> accomplishments = new ArrayList<ArrayList<Snippet>>();
  private boolean accomplishmentP = false;
  private int accomplishmentC = 0;
  private int lastAccomplishmentC = 0;

  public Job(DefaultHandler parent, XMLReader parser) {
    super(parent, parser);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equals("employer")) {
      System.out.println("START employer");
      employerP = true;
    }

    if (qName.equals("department")) {
      System.out.println("START department");
      departmentP = true;
    }

    if (qName.equals("timespan")) {
      System.out.println("START timespan");
      timespanP = true;
    }

    if (qName.equals("role")) {
      System.out.println("START role");
      roleP = true;
    }

    if (qName.equals("project")) {
      System.out.println("START project");
      projectP = true;
    }

    snippetState.startSpecialSnippet(qName);

    if (qName.equals("accomplishment") && include(attributes)) {
      System.out.println("START accomplishment");
      accomplishmentP = true;
      accomplishmentC++;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("job")) {
      System.out.println("END job");
      parser.setContentHandler(parent);
    }

    if (qName.equals("employer")) {
      System.out.println(employer);
      System.out.println("END employer");
      employerP = false;
    }

    if (qName.equals("department")) {
      System.out.println(department);
      System.out.println("END department");
      departmentP = false;
    }

    if (qName.equals("timespan")) {
      System.out.println(timespan);
      System.out.println("END timespan");
      timespanP = false;
    }

    if (qName.equals("role")) {
      System.out.println(role);
      System.out.println("END role");
      roleP = false;
    }

    if (qName.equals("project")) {
      System.out.println(project);
      System.out.println("END project");
      projectP = false;
    }

    snippetState.endSpecialSnippet(qName);

    if (qName.equals("accomplishment") && accomplishmentP) {
      if (accomplishments.size() > 0) {
        ArrayList<Snippet> snippets = accomplishments.get(accomplishments.size() - 1);
        for (Snippet snippet : snippets) {
          System.out.print(snippet.text);
        }
        System.out.println();
      }

      System.out.println("END accomplishment");
      accomplishmentP = false;
      accomplishmentC++;
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    if (employerP) {
      employer += new String(ch, start, length);
    }

    if (departmentP) {
      department += new String(ch, start, length);
    }

    if (timespanP) {
      timespan += new String(ch, start, length);
    }

    if (roleP) {
      role += new String(ch, start, length);
    }

    if (projectP) {
      project += new String(ch, start, length);
    }

    if (accomplishmentP) {
      String str = new String(ch, start, length);
      snippetState.setCurrSnippet(str);

      if (accomplishmentC != lastAccomplishmentC) {
        ArrayList<Snippet> snippets = new ArrayList<Snippet>();
        snippets.add(snippetState.getSnippet(str));
        accomplishments.add(snippets);
        lastAccomplishmentC = accomplishmentC;
      } else {
        ArrayList<Snippet> snippets = accomplishments.get(accomplishments.size() - 1);
        snippets.add(snippetState.getSnippet(str));
      }
    }
  }
}
