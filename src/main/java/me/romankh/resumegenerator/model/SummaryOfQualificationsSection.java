package me.romankh.resumegenerator.model;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

public class SummaryOfQualificationsSection {
  @XmlAttribute(name = "include")
  private boolean include;

  @XmlElementWrapper(name = "qualifications")
  @XmlElement(name = "qualification")
  private List<Qualification> qualifications;

  public SummaryOfQualificationsSection addQualification(Qualification qualification) {
    if (qualifications == null)
      qualifications = new ArrayList<>();
    qualifications.add(qualification);
    return this;
  }

  public boolean isInclude() {
    return include;
  }

  public SummaryOfQualificationsSection setInclude(boolean include) {
    this.include = include;
    return this;
  }

  public List<Qualification> getQualifications() {
    return qualifications;
  }

  public SummaryOfQualificationsSection setQualifications(List<Qualification> qualifications) {
    this.qualifications = qualifications;
    return this;
  }

  public static class Qualification {
    @XmlAttribute(name = "include")
    private boolean include;

    @XmlValue
    @XmlAnyElement(value = HTMLHandler.class, lax = false)
    private String qualification;

    public boolean isInclude() {
      return include;
    }

    public Qualification setInclude(boolean include) {
      this.include = include;
      return this;
    }

    public String getQualification() {
      return qualification;
    }

    public Qualification setQualification(String qualification) {
      this.qualification = qualification;
      return this;
    }
  }
}
