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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

public class Resume extends ResumeElement {
  // Document margins.
  public float marginLeft = 0;
  public float marginRight = 0;
  public float marginTop = 0;
  public float marginBottom = 0;

  public Header header = new Header(this, parser);
  public Content content = new Content(this, parser);

  public Resume(String path) throws Exception {
    super(XMLReaderFactory.createXMLReader());
    parser.setContentHandler(this);
    parser.parse(path);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equals("resume"))
      System.out.println("START resume");

    if (qName.equals("meta")) {
      for (int i = 0; i < attributes.getLength(); i++) {
        float margin = Float.parseFloat(attributes.getValue(i));
        if (attributes.getQName(i).equals("margin_left")) {
          marginLeft = margin;
        } else if (attributes.getQName(i).equals("margin_right")) {
          marginRight = margin;
        } else if (attributes.getQName(i).equals("margin_top")) {
          marginTop = margin;
        } else if (attributes.getQName(i).equals("margin_bottom")) {
          marginBottom = margin;
        }
      }
    }

    if (qName.equals("header")) {
      System.out.println("START header");
      parser.setContentHandler(header);
    }

    if (qName.equals("content")) {
      System.out.println("START content");
      parser.setContentHandler(content);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("resume"))
      System.out.println("END resume");
  }
}
