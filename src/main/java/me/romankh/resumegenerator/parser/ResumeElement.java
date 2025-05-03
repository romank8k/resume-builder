package me.romankh.resumegenerator.parser;

import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public abstract class ResumeElement extends DefaultHandler {
    protected final DefaultHandler parent;
    protected XMLReader parser;
    protected final String elementName;
    protected final List<SimpleElement> elementList;
    protected final List<CompositeElement<?>> compositeElementList;
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
                         List<SimpleElement> elementList, List<CompositeElement<?>> compositeElementList) {
        this.parent = parent;
        this.parser = parser;
        this.elementName = elementName;
        this.elementList = elementList;
        this.compositeElementList = compositeElementList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals(elementName)) {
            if (log.isTraceEnabled())
                log.trace("START {}", elementName);
        }

        SimpleElement currElement = getElementByName(qName);
        if (currElement != null && include(attributes)) {
            if (log.isTraceEnabled())
                log.trace("START {}", currElement.getName());
            activeElement = currElement;
            currElement.addValue();

            elementOrderList.add(currElement);
        }

        CompositeElement<? extends ResumeElement> compositeElement = getCompositeElementByName(qName);
        if (compositeElement != null && include(attributes)) {
            if (log.isTraceEnabled())
                log.trace("START COMPOSITE {}", compositeElement.getName());

            try {
                addCompositeElementInstance(compositeElement, parser, this);
                elementOrderList.add(compositeElement);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static <T extends ResumeElement> void addCompositeElementInstance(CompositeElement<T> compositeElement,
                                                                              XMLReader parser, ResumeElement currResumeElement) throws Exception {
        Constructor<T> ctor = compositeElement.getClazz().getConstructor(DefaultHandler.class,
                XMLReader.class);
        T instance = ctor.newInstance(currResumeElement, parser);
        compositeElement.addResumeElement(instance);
        parser.setContentHandler(instance);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        SimpleElement currElement = getElementByName(qName);
        if (currElement != null) {
            if (log.isTraceEnabled())
                log.trace("END {}: {}", currElement.getName(), currElement.getValue());
            activeElement = null;
        }

        if (qName.equals(elementName)) {
            if (log.isTraceEnabled())
                log.trace("END {}", elementName);
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
                return val.equals("true");
            }
        }

        // True by default.
        return true;
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

    public CompositeElement<? extends ResumeElement> getCompositeElementByName(String name) {
        if (compositeElementList != null) {
            for (CompositeElement<? extends ResumeElement> compositeElement : compositeElementList) {
                if (compositeElement.name.equals(name))
                    return compositeElement;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends ResumeElement> CompositeElement<T> getCompositeElementByClass(Class<T> clazz) {
        if (compositeElementList != null) {
            for (CompositeElement<? extends ResumeElement> compositeElement : compositeElementList) {
                if (compositeElement.getClazz().equals(clazz)) {
                    return (CompositeElement<T>) compositeElement;
                }
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

    protected static List<CompositeElement<?>> buildCompositeElementList(CompositeElement<?>... compositeElements) {
        List<CompositeElement<?>> compositeElementList = null;
        if (compositeElements != null) {
            compositeElementList = new ArrayList<>(compositeElements.length);
            Collections.addAll(compositeElementList, compositeElements);
        }
        return compositeElementList;
    }

    public List<AbstractElement> getElementOrderList() {
        return elementOrderList;
    }
}
