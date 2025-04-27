package me.romankh.resumegenerator.model;

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

public class ObjectiveSection {
  @XmlAttribute(name = "include")
  private boolean include;

  @XmlAnyElement(value = HTMLHandler.class, lax = false)
  @XmlValue
  private String objective;

  public boolean isInclude() {
    return include;
  }

  public ObjectiveSection setInclude(boolean include) {
    this.include = include;
    return this;
  }

  public String getObjective() {
    return objective;
  }

  public ObjectiveSection setObjective(String objective) {
    this.objective = objective;
    return this;
  }
}
