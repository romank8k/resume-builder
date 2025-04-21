package me.romankh.resumegenerator.model;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class Role {
  @XmlAttribute(name = "include")
  private boolean include;

  @XmlElement(name = "title")
  private String title;

  @XmlElement(name = "timespan")
  private String timespan;

  @XmlElement(name = "department")
  private String department;

  @XmlElementWrapper(name = "accomplishments")
  @XmlElement(name = "accomplishment")
  private List<RoleAccomplishment> roleAccomplishments;

  public Role addAccomplishment(RoleAccomplishment roleAccomplishment) {
    if (roleAccomplishments == null)
      roleAccomplishments = new ArrayList<>();
    roleAccomplishments.add(roleAccomplishment);
    return this;
  }

  public boolean isInclude() {
    return include;
  }

  public Role setInclude(boolean include) {
    this.include = include;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public Role setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getTimespan() {
    return timespan;
  }

  public Role setTimespan(String timespan) {
    this.timespan = timespan;
    return this;
  }

  public String getDepartment() {
    return department;
  }

  public Role setDepartment(String department) {
    this.department = department;
    return this;
  }

  public List<RoleAccomplishment> getRoleAccomplishments() {
    return roleAccomplishments;
  }

  public Role setRoleAccomplishments(List<RoleAccomplishment> roleAccomplishments) {
    this.roleAccomplishments = roleAccomplishments;
    return this;
  }

  public static class RoleAccomplishment {
    @XmlAttribute(name = "include")
    private boolean include;

    @XmlValue
    @XmlAnyElement(value = HTMLHandler.class, lax = false)
    private String accomplishment;

    public boolean isInclude() {
      return include;
    }

    public RoleAccomplishment setInclude(boolean include) {
      this.include = include;
      return this;
    }

    public String getAccomplishment() {
      return accomplishment;
    }

    public RoleAccomplishment setAccomplishment(String accomplishment) {
      this.accomplishment = accomplishment;
      return this;
    }
  }
}
