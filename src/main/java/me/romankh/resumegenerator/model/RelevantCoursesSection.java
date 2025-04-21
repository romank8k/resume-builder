package me.romankh.resumegenerator.model;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class RelevantCoursesSection {
  @XmlAttribute(name = "include")
  private boolean include;

  @XmlElementWrapper(name = "courses")
  @XmlElement(name = "course")
  private List<Course> courses;

  public RelevantCoursesSection addCourse(Course course) {
    if (courses == null)
      courses = new ArrayList<>();
    courses.add(course);
    return this;
  }

  public boolean isInclude() {
    return include;
  }

  public RelevantCoursesSection setInclude(boolean include) {
    this.include = include;
    return this;
  }

  public List<Course> getCourses() {
    return courses;
  }

  public RelevantCoursesSection setCourses(List<Course> courses) {
    this.courses = courses;
    return this;
  }

  public static class Course {
    @XmlAttribute(name = "include")
    private boolean include;

    @XmlValue
    @XmlAnyElement(value = HTMLHandler.class, lax = false)
    private String course;

    public boolean isInclude() {
      return include;
    }

    public Course setInclude(boolean include) {
      this.include = include;
      return this;
    }

    public String getCourse() {
      return course;
    }

    public Course setCourse(String course) {
      this.course = course;
      return this;
    }
  }
}
