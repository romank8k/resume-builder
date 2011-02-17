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

public class Address extends ResumeElement {
  public String street = new String();
  private boolean streetP = false;
  public String apartment = new String();
  private boolean apartmentP = false;
  public String city = new String();
  private boolean cityP = false;
  public String state = new String();
  private boolean stateP = false;
  public String zip = new String();
  private boolean zipP = false;

  public Address(DefaultHandler parent, XMLReader parser) {
    super(parent, parser);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equals("street")) {
      System.out.println("START street");
      streetP = true;
    }

    if (qName.equals("apartment")) {
      System.out.println("START apartment");
      apartmentP = true;
    }

    if (qName.equals("city")) {
      System.out.println("START city");
      cityP = true;
    }

    if (qName.equals("state")) {
      System.out.println("START state");
      stateP = true;
    }

    if (qName.equals("zip")) {
      System.out.println("START zip");
      zipP = true;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("address")) {
      System.out.println("END address");
      parser.setContentHandler(parent);
    }

    if (qName.equals("street")) {
      System.out.println(street);
      System.out.println("END street");
      streetP = false;
    }

    if (qName.equals("apartment")) {
      System.out.println(apartment);
      System.out.println("END apartment");
      apartmentP = false;
    }

    if (qName.equals("city")) {
      System.out.println(city);
      System.out.println("END city");
      cityP = false;
    }

    if (qName.equals("state")) {
      System.out.println(state);
      System.out.println("END state");
      stateP = false;
    }

    if (qName.equals("zip")) {
      System.out.println(zip);
      System.out.println("END zip");
      zipP = false;
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    if (streetP) {
      street += new String(ch, start, length);
    }

    if (apartmentP) {
      apartment += new String(ch, start, length);
    }

    if (cityP) {
      city += new String(ch, start, length);
    }

    if (stateP) {
      state += new String(ch, start, length);
    }

    if (zipP) {
      zip += new String(ch, start, length);
    }
  }
}
