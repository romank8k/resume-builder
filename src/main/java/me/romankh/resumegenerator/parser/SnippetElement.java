package me.romankh.resumegenerator.parser;

import org.apache.commons.lang.StringEscapeUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Roman Khmelichek
 */
public class SnippetElement extends ResumeElement {
  private String html;
  private StringBuilder htmlBuilder;
  private boolean voidTag;

  public SnippetElement(DefaultHandler parent, XMLReader parser, String elementName) {
    super(parent, parser, elementName, null);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    super.startElement(uri, localName, qName, attributes);

    if (htmlBuilder != null) {
      htmlBuilder.append('<');
      htmlBuilder.append(qName);
      if (attributes.getLength() > 0) {
        htmlBuilder.append(' ');
      }

      for (int i = 0; i < attributes.getLength(); i++) {
        String attrName = attributes.getQName(i);
        String attrValue = attributes.getValue(i);

        htmlBuilder.append(attrName);
        htmlBuilder.append('=');
        htmlBuilder.append('"');
        htmlBuilder.append(escapeAttribute(attrValue));
        htmlBuilder.append('"');
      }

      if (Html5VoidTag.findTag(qName) != null) {
        // HTML5 void tags should not have a corresponding closing tag (or they should be self-closing).
        // Make them self-closing for compatibility with XML.
        htmlBuilder.append('/');
        voidTag = true;
      }

      htmlBuilder.append('>');
    } else {
      if (qName.equalsIgnoreCase("html")) {
        htmlBuilder = new StringBuilder();
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equalsIgnoreCase("html")) {
      html = htmlBuilder.toString();
      htmlBuilder = null;
    } else {
      if (htmlBuilder != null) {
        if (!voidTag) {
          htmlBuilder.append('<');
          htmlBuilder.append('/');
          htmlBuilder.append(qName);
          htmlBuilder.append(">");
        } else {
          voidTag = false;
        }
      }
    }

    super.endElement(uri, localName, qName);
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    if (htmlBuilder != null) {
      if (voidTag) {
        // If the void tag incorrectly has content - ignore it.
        return;
      }
      htmlBuilder.append(ch, start, length);
    }
  }

  String escapeAttribute(String attr) {
    return StringEscapeUtils.escapeXml(attr);
  }

  public String getHtml() {
    return html;
  }
}
