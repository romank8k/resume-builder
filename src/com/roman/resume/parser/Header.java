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
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class Header extends ResumeElement {
  public String name = new String();
  private boolean nameP = false;
  public String phone = new String();
  private boolean phoneP = false;
  public String email = new String();
  private boolean emailP = false;
  public String homepage = new String();
  private boolean homepageP = false;
  public Address address = new Address(this, parser);

  public Header(DefaultHandler parent, XMLReader parser) {
    super(parent, parser);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equals("name")) {
      System.out.println("START name");
      nameP = true;
    }

    if (qName.equals("phone")) {
      if (include(attributes)) {
        System.out.println("START phone");
        phoneP = true;
      } else {
        phone = null;
      }
    }

    if (qName.equals("email")) {
      if (include(attributes)) {
        System.out.println("START email");
        emailP = true;
      } else {
        email = null;
      }
    }

    if (qName.equals("homepage")) {
      if (include(attributes)) {
        System.out.println("START homepage");
        homepageP = true;
      } else {
        homepage = null;
      }
    }

    if (qName.equals("address")) {
      if (include(attributes)) {
        System.out.println("START address");
        parser.setContentHandler(address);
      } else {
        address = null;
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("header")) {
      System.out.println("END header");
      parser.setContentHandler(parent);
    }

    if (qName.equals("name")) {
      System.out.println(name);
      System.out.println("END name");
      nameP = false;
    }

    if (qName.equals("phone") && phoneP) {
      System.out.println(phone);
      System.out.println("END phone");
      phoneP = false;
    }

    if (qName.equals("email") && emailP) {
      System.out.println(email);
      System.out.println("END email");
      emailP = false;
    }

    if (qName.equals("homepage") && homepageP) {
      System.out.println(homepage);
      System.out.println("END homepage");
      homepageP = false;
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    if (nameP) {
      name += new String(ch, start, length);
    }

    if (phoneP) {
      phone += new String(ch, start, length);
    }

    if (emailP) {
      email += new String(ch, start, length);
    }

    if (homepageP) {
      homepage += new String(ch, start, length);
    }
  }
}
