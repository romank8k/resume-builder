package me.romankh.resumegenerator.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class EducationSection {
  @XmlAttribute(name = "include")
  private boolean include;

  @XmlElementWrapper(name = "institutions")
  @XmlElement(name = "institution")
  private List<Institution> institutions;

  public EducationSection addInstitution(Institution institution) {
    if (institutions == null)
      institutions = new ArrayList<>();
    institutions.add(institution);
    return this;
  }

  public boolean isInclude() {
    return include;
  }

  public EducationSection setInclude(boolean include) {
    this.include = include;
    return this;
  }

  public List<Institution> getInstitutions() {
    return institutions;
  }

  public EducationSection setInstitutions(List<Institution> institutions) {
    this.institutions = institutions;
    return this;
  }
}
