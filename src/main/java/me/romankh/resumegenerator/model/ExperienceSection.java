package me.romankh.resumegenerator.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class ExperienceSection {
  @XmlAttribute(name = "include")
  private boolean include;

  @XmlElementWrapper(name = "jobs")
  @XmlElement(name = "job")
  private List<Job> jobs;

  public ExperienceSection addJob(Job job) {
    if (jobs == null)
      jobs = new ArrayList<>();
    jobs.add(job);
    return this;
  }

  public boolean isInclude() {
    return include;
  }

  public ExperienceSection setInclude(boolean include) {
    this.include = include;
    return this;
  }

  public List<Job> getJobs() {
    return jobs;
  }

  public ExperienceSection setJobs(List<Job> jobs) {
    this.jobs = jobs;
    return this;
  }
}
