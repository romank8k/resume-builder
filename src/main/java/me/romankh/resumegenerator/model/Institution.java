package me.romankh.resumegenerator.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class Institution {
  @XmlAttribute(name = "include")
  private boolean include;

  @XmlElement(name = "name")
  private String name;

  @XmlElement(name = "location")
  private String location;

  @XmlElementWrapper(name = "degrees")
  @XmlElement(name = "degree")
  private List<Degree> degrees;

  public Institution addDegree(Degree degree) {
    if (degrees == null)
      degrees = new ArrayList<>();
    degrees.add(degree);
    return this;
  }

  public boolean isInclude() {
    return include;
  }

  public Institution setInclude(boolean include) {
    this.include = include;
    return this;
  }

  public String getName() {
    return name;
  }

  public Institution setName(String name) {
    this.name = name;
    return this;
  }

  public String getLocation() {
    return location;
  }

  public Institution setLocation(String location) {
    this.location = location;
    return this;
  }

  public List<Degree> getDegrees() {
    return degrees;
  }

  public Institution setDegrees(List<Degree> degrees) {
    this.degrees = degrees;
    return this;
  }
}
