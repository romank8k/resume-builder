package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class Awards extends ResumeElement {
    private static final String ELEMENT_NAME = "awards";

    public Awards(DefaultHandler parent, XMLReader parser) {
        super(parent, parser, ELEMENT_NAME, buildElementList(),
                buildCompositeElementList(new CompositeElement<>(Award.class, Award.getElementName())));
    }

    public List<Award> getAwards() {
        return getCompositeElementByClass(Award.class).getResumeElementList();
    }

    public static String getElementName() {
        return ELEMENT_NAME;
    }
}
