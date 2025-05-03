package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class Objective extends SnippetElement {
    private static final String ELEMENT_NAME = "objective";

    public Objective(DefaultHandler parent, XMLReader parser) {
        super(parent, parser, ELEMENT_NAME);
    }

    public static String getElementName() {
        return ELEMENT_NAME;
    }
}
