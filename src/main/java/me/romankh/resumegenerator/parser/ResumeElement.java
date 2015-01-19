package me.romankh.resumegenerator.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
public abstract class ResumeElement extends DefaultHandler {
  private static final Logger logger = LogManager.getLogger(ResumeElement.class);

  protected final DefaultHandler parent;
  protected XMLReader parser;
  protected final String elementName;
  protected final List<SimpleElement> elementList;
  protected final List<CompositeElement> compositeElementList;
  protected final List<AbstractElement> elementOrderList = new ArrayList<>();

  private SimpleElement activeElement;

  public ResumeElement() {
    this.parent = null;
    this.parser = null;
    this.elementName = null;
    this.elementList = null;
    this.compositeElementList = null;
  }

  public ResumeElement(String elementName, List<SimpleElement> elementList) {
    this.parent = null;
    this.parser = null;
    this.elementName = elementName;
    this.elementList = elementList;
    this.compositeElementList = null;
  }

  public ResumeElement(XMLReader parser) {
    this.parent = null;
    this.parser = parser;
    this.elementName = null;
    this.elementList = null;
    this.compositeElementList = null;
  }

  public ResumeElement(DefaultHandler parent, XMLReader parser) {
    this.parent = parent;
    this.parser = parser;
    this.elementName = null;
    this.elementList = null;
    this.compositeElementList = null;
  }

  public ResumeElement(DefaultHandler parent, XMLReader parser, String elementName, List<SimpleElement> elementList) {
    this.parent = parent;
    this.parser = parser;
    this.elementName = elementName;
    this.elementList = elementList;
    this.compositeElementList = null;
  }

  public ResumeElement(DefaultHandler parent, XMLReader parser, String elementName,
                       List<SimpleElement> elementList, List<CompositeElement> compositeElementList) {
    this.parent = parent;
    this.parser = parser;
    this.elementName = elementName;
    this.elementList = elementList;
    this.compositeElementList = compositeElementList;
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equals(elementName)) {
      logger.info("START {}", elementName);
    }

    SimpleElement currElement = getElementByName(qName);
    if (currElement != null && include(attributes)) {
      logger.info("START {}", currElement.getName());
      activeElement = currElement;
      currElement.addValue();

      elementOrderList.add(currElement);
    }

    CompositeElement compositeElement = getCompositeElementByName(qName);
    if (compositeElement != null && include(attributes)) {
      logger.info("START COMPOSITE {}", compositeElement.getName());

      try {
        Constructor<?> ctor = compositeElement.getClazz().getConstructor(DefaultHandler.class, XMLReader.class);
        ResumeElement instance = (ResumeElement) ctor.newInstance(this, parser);
        compositeElement.addResumeElement(instance);
        parser.setContentHandler(instance);

        elementOrderList.add(compositeElement);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    SimpleElement currElement = getElementByName(qName);
    if (currElement != null) {
      logger.info("END {}: {}", currElement.getName(), currElement.getValue());
      activeElement = null;
    }

    if (qName.equals(elementName)) {
      logger.info("END {}", elementName);
      parser.setContentHandler(parent);
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    if (activeElement != null) {
      activeElement.append(ch, start, length);
    }
  }

  protected boolean include(Attributes attributes) {
    for (int i = 0; i < attributes.getLength(); i++) {
      if (attributes.getQName(i).equals("include")) {
        String val = attributes.getValue(i);
        return val.equals("true") ? true : false;
      }
    }

    // True by default.
    return true;
  }

  public static class AbstractElement {
    protected final String name;

    public AbstractElement(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  public static class SimpleElement extends AbstractElement {
    private final List<StringBuilder> stringBuilderList;

    public SimpleElement(String name) {
      super(name);
      this.stringBuilderList = new ArrayList<>();
    }

    public void append(char[] ch, int start, int length) {
      int i = stringBuilderList.size() - 1;
      assert i >= 0;
      StringBuilder sb = stringBuilderList.get(i);
      sb.append(ch, start, length);
    }

    public List<String> getValues() {
      List<String> values = new ArrayList<>();
      for (StringBuilder st : stringBuilderList) {
        values.add(st.toString());
      }
      return values;
    }

    public void addValue() {
      stringBuilderList.add(new StringBuilder());
    }

    public String getValue() {
      return getValue(0);
    }

    public String getValue(int i) {
      if (stringBuilderList.size() > i)
        return stringBuilderList.get(i).toString();

      return null;
    }
  }

  public SimpleElement getElementByName(String name) {
    if (elementList != null) {
      for (SimpleElement element : elementList) {
        if (element.name.equals(name))
          return element;
      }
    }

    return null;
  }

  public static class CompositeElement<T extends ResumeElement> extends AbstractElement {
    private final Class<T> clazz;
    private List<T> resumeElementList = new ArrayList<>();

    public CompositeElement(Class clazz, String name) {
      super(name);
      this.clazz = clazz;
    }

    public void addResumeElement(T resumeElement) {
      resumeElementList.add(resumeElement);
    }

    public Class<T> getClazz() {
      return clazz;
    }

    public List<T> getResumeElementList() {
      return resumeElementList;
    }

    public T getValue() {
      if (resumeElementList != null && !resumeElementList.isEmpty())
        return clazz.cast(resumeElementList.get(0));

      return null;
    }
  }

  public CompositeElement getCompositeElementByName(String name) {
    if (compositeElementList != null) {
      for (CompositeElement compositeElement : compositeElementList) {
        if (compositeElement.name.equals(name))
          return compositeElement;
      }
    }

    return null;
  }

  public <U extends ResumeElement> CompositeElement<U> getCompositeElementByClass(Class<U> clazz) {
    if (compositeElementList != null) {
      for (CompositeElement compositeElement : compositeElementList) {
        if (compositeElement.clazz.equals(clazz))
          return compositeElement;
      }
    }

    return null;
  }

  protected static List<SimpleElement> buildElementList(String... names) {
    List<SimpleElement> elementList = null;
    if (names != null) {
      elementList = new ArrayList<>(names.length);
      for (String name : names) {
        elementList.add(new SimpleElement(name));
      }
    }
    return elementList;
  }

  protected static List<CompositeElement> buildCompositeElementList(CompositeElement... compositeElements) {
    List<CompositeElement> compositeElementList = null;
    if (compositeElements != null) {
      compositeElementList = new ArrayList<>(compositeElements.length);
      for (CompositeElement compositeElement : compositeElements) {
        compositeElementList.add(compositeElement);
      }
    }
    return compositeElementList;
  }

  public List<AbstractElement> getElementOrderList() {
    return elementOrderList;
  }
}
