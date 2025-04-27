package me.romankh.resumegenerator.parser;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;

public class ResumeParser extends ResumeElement {
  private static final String ELEMENT_NAME = "resume";

  private int marginLeft;
  private int marginRight;
  private int marginTop;
  private int marginBottom;

  public ResumeParser(InputStream is) throws IOException, SAXException {
    super(null, XMLReaderFactory.createXMLReader(), ELEMENT_NAME, buildElementList(),
        buildCompositeElementList(
            new CompositeElement<>(Header.class, Header.getElementName()),
            new CompositeElement<>(Content.class, Content.getElementName())));

    parser.setContentHandler(this);
    parser.parse(new InputSource(is));
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    super.startElement(uri, localName, qName, attributes);

    if (qName.equals("meta")) {
      for (int i = 0; i < attributes.getLength(); i++) {
        int margin = Integer.parseInt(attributes.getValue(i));
        if (attributes.getQName(i).equals("margin-left")) {
          marginLeft = margin;
        } else if (attributes.getQName(i).equals("margin-right")) {
          marginRight = margin;
        } else if (attributes.getQName(i).equals("margin-top")) {
          marginTop = margin;
        } else if (attributes.getQName(i).equals("margin-bottom")) {
          marginBottom = margin;
        }
      }
    }
  }

  public Header getHeader() {
    return getCompositeElementByClass(Header.class).getValue();
  }

  public Content getContent() {
    return getCompositeElementByClass(Content.class).getValue();
  }

  public static String getElementName() {
    return ELEMENT_NAME;
  }

  public int getMarginLeft() {
    return marginLeft;
  }

  public int getMarginRight() {
    return marginRight;
  }

  public int getMarginTop() {
    return marginTop;
  }

  public int getMarginBottom() {
    return marginBottom;
  }
}
