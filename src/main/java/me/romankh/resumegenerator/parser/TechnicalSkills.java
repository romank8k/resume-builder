package me.romankh.resumegenerator.parser;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class TechnicalSkills extends ResumeElement {
    private static final String ELEMENT_NAME = "technical_skills";

    public TechnicalSkills(DefaultHandler parent, XMLReader parser) {
        super(parent, parser, ELEMENT_NAME, null,
                buildCompositeElementList(new CompositeElement<>(Skill.class, Skill.getElementName())));
    }

    public List<String> getSkills() {
        List<String> skillSnippets = new ArrayList<>();
        List<Skill> skillList = getCompositeElementByClass(Skill.class).getResumeElementList();
        if (skillList != null) {
            for (Skill skill : skillList) {
                skillSnippets.add(skill.getHtml());
            }
        }
        return skillSnippets;
    }

    public static String getElementName() {
        return ELEMENT_NAME;
    }

    public static class Skill extends SnippetElement {
        private static final String ELEMENT_NAME = "skill";

        public Skill(DefaultHandler parent, XMLReader parser) {
            super(parent, parser, ELEMENT_NAME);
        }

        public static String getElementName() {
            return ELEMENT_NAME;
        }
    }
}
