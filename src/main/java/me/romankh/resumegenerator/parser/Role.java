package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class Role extends ResumeElement {
    private static final String ELEMENT_NAME = "role";

    private static final String DEPARTMENT = "department";
    private static final String TIMESPAN = "timespan";
    private static final String TITLE = "title";

    public Role(DefaultHandler parent, XMLReader parser) {
        super(parent, parser, ELEMENT_NAME, buildElementList(DEPARTMENT, TIMESPAN, TITLE),
                buildCompositeElementList(new CompositeElement<>(Accomplishment.class, Accomplishment.getElementName())));
    }

    public String getDepartment() {
        return getElementByName(DEPARTMENT).getValue();
    }

    public String getTimespan() {
        return getElementByName(TIMESPAN).getValue();
    }

    public String getTitle() {
        return getElementByName(TITLE).getValue();
    }

    public List<String> getAccomplishments() {
        List<String> accomplishmentSnippets = new ArrayList<>();
        List<Accomplishment> accomplishmentList = getCompositeElementByClass(Accomplishment.class).getResumeElementList();
        if (accomplishmentList != null) {
            for (Accomplishment accomplishment : accomplishmentList) {
                accomplishmentSnippets.add(accomplishment.getHtml());
            }
        }
        return accomplishmentSnippets;
    }

    public static String getElementName() {
        return ELEMENT_NAME;
    }

    public static class Accomplishment extends SnippetElement {
        private static final String ELEMENT_NAME = "accomplishment";

        public Accomplishment(DefaultHandler parent, XMLReader parser) {
            super(parent, parser, ELEMENT_NAME);
        }

        public static String getElementName() {
            return ELEMENT_NAME;
        }
    }
}
