package me.romankh.resumegenerator.model;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
@XmlRootElement(name = "markdown")
public class MarkdownTag extends Tag {
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
