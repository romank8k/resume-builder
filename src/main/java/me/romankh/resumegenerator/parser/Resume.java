package me.romankh.resumegenerator.parser;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Roman Khmelichek
 */
public class Resume extends ResumeElement {
  private static final String ELEMENT_NAME = "resume";

  public Resume(InputStream is) throws IOException, SAXException {
    super(null, XMLReaderFactory.createXMLReader(), ELEMENT_NAME, buildElementList(),
        buildCompositeElementList(
            new CompositeElement<>(Header.class, Header.getElementName()),
            new CompositeElement<>(Content.class, Content.getElementName())));

    parser.setContentHandler(this);
    parser.parse(new InputSource(is));
  }

  // Document margins.
  public float marginLeft = 0;
  public float marginRight = 0;
  public float marginTop = 0;
  public float marginBottom = 0;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    super.startElement(uri, localName, qName, attributes);

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
}
