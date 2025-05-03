package me.romankh.resumegenerator.model;

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlMixed;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;
import org.eclipse.persistence.oxm.annotations.XmlValueExtension;

import java.util.List;

@XmlRootElement(name = "markdown")
public class MarkdownTag extends Tag {
    @XmlValueExtension
    @XmlValue
    @XmlMixed
    @XmlAnyElement
    private List<Object> value;

    public List<Object> getValue() {
        return value;
    }

    public MarkdownTag setValue(List<Object> value) {
        this.value = value;
        return this;
    }
}
