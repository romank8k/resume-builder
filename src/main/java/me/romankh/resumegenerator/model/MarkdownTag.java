package me.romankh.resumegenerator.model;

import org.eclipse.persistence.oxm.annotations.XmlValueExtension;

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlMixed;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
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
