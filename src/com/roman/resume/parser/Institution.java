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

public class Institution extends ResumeElement {
  public String name = new String();
  private boolean nameP = false;
  public String location = new String();
  boolean locationP = false;
  public ArrayList<String> details = new ArrayList<String>();
  private boolean detailP = false;
  private int detailC = 0;
  private int lastDetailC = 0;

  public Institution(DefaultHandler parent, XMLReader parser) {
    super(parent, parser);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equals("name")) {
      System.out.println("START name");
      nameP = true;
    }

    if (qName.equals("location")) {
      System.out.println("START location");
      locationP = true;
    }

    if (qName.equals("detail") && include(attributes)) {
      System.out.println("START detail");
      detailP = true;
      detailC++;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("institution")) {
      System.out.println("END institution");
      parser.setContentHandler(parent);
    }

    if (qName.equals("name")) {
      System.out.println(name);
      System.out.println("END name");
      nameP = false;
    }

    if (qName.equals("location")) {
      System.out.println(location);
      System.out.println("END location");
      locationP = false;
    }

    if (qName.equals("detail") && detailP) {
      if (details.size() > 0) {
        System.out.println(details.get(details.size() - 1));
      }

      System.out.println("END detail");
      detailP = false;
      detailC++;
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    if (nameP) {
      name += new String(ch, start, length);
    }

    if (locationP) {
      location += new String(ch, start, length);
    }

    if (detailP) {
      String str = new String(ch, start, length);

      if (detailC != lastDetailC) {
        details.add(str);
        lastDetailC = detailC;
      } else {
        details.set(details.size() - 1, details.get(details.size() - 1) + str);
      }
    }
  }
}
