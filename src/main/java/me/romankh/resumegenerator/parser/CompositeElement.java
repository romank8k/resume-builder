package me.romankh.resumegenerator.parser;

import java.util.ArrayList;
import java.util.List;

public class CompositeElement<T extends ResumeElement> extends AbstractElement {
    private final Class<T> clazz;
    private final List<T> resumeElementList;

    public CompositeElement(Class<T> clazz, String name) {
        super(name);
        this.clazz = clazz;
        this.resumeElementList = new ArrayList<>();
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
        if (!resumeElementList.isEmpty())
            return clazz.cast(resumeElementList.get(0));

        return null;
    }
}
