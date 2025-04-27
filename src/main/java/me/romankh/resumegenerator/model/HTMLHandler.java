package me.romankh.resumegenerator.model;

import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.persistence.oxm.XMLRoot;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.ValidationEventHandler;
import jakarta.xml.bind.annotation.DomHandler;
import jakarta.xml.bind.util.JAXBResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class HTMLHandler implements DomHandler<String, JAXBResult> {
  private static final TransformerFactory tFactory = TransformerFactory.newInstance();

  private static final String HTML = "html";
  private static final String HTML_START_TAG = "<html>";
  private static final String HTML_END_TAG = "</html>";

  private static JAXBContext jaxbContext = createJAXBContext();

  @Override
  public JAXBResult createUnmarshaller(ValidationEventHandler errorHandler) {
    try {
      return new JAXBResult(jaxbContext);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getElement(JAXBResult rt) {
    try {
      Tag tag = (Tag) rt.getResult();
      if (tag instanceof HtmlTag) {
        return handleHtml(((HtmlTag) tag).getValue());
      } else if (tag instanceof MarkdownTag) {
        return handleMarkdown(((MarkdownTag) tag).getValue());
      } else {
        return null;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  String handleHtml(Object value) throws Exception {
    if (value instanceof Element) {
      Element element = (Element) value;
      return domToStr(element, false);
    } else if (value instanceof XMLRoot) {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = dbf.newDocumentBuilder();
      Document doc = builder.newDocument();
      Element element = doc.createElement(HTML);
      doc.appendChild(element);

      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.marshal(value, doc.getDocumentElement());

      // The JSON value is assumed to be a text node.
      // If we have HTML there, we need to unescape it.
      return domToStr(doc.getDocumentElement(), true);
    } else if (value instanceof String) {
      return (String) value;
    } else if (value instanceof List) {
      @SuppressWarnings(value = "unchecked")
      List<Object> values = (List<Object>) value;
      StringBuilder sb = new StringBuilder();
      for (Object val : values) {
        String str = handleHtml(val);
        if (str != null) {
          sb.append(str);
        }
      }

      return sb.toString();
    }

    return null;
  }

  String handleMarkdown(Object value) throws Exception {
    // TODO
    return null;
  }

  String domToStr(Element el, boolean unescape) throws Exception {
    DOMSource domSource = new DOMSource(el);
    StringWriter xmlWriter = new StringWriter();
    StreamResult result = new StreamResult(xmlWriter);
    Transformer transformer = tFactory.newTransformer();
    transformer.setOutputProperty("omit-xml-declaration", "yes");
    transformer.transform(domSource, result);
    String xml = xmlWriter.toString();
    if (unescape) {
      xml = StringEscapeUtils.unescapeXml(xml);
    }
    return xml;
  }

  @Override
  public Source marshal(String n, ValidationEventHandler errorHandler) {
    String xml = HTML_START_TAG + n.trim() + HTML_END_TAG;

    // TODO: Must make sure this is valid XML and clean unexpected HTML tags here.

    StringReader xmlReader = new StringReader(xml);
    return new StreamSource(xmlReader);
  }

  static JAXBContext createJAXBContext() {
    try {
      return JAXBContext.newInstance(
          HtmlTag.class,
          MarkdownTag.class
      );
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }
}
