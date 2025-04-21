package me.romankh.resumegenerator.model;

import jakarta.xml.bind.annotation.XmlElement;

/**
 * @author Roman Khmelichek
 */
public class ResumeHeader {
  @XmlElement(name = "name")
  private String name;

  @XmlElement(name = "phone")
  private String phone;

  @XmlElement(name = "email")
  private String email;

  @XmlElement(name = "homepage")
  private String homepage;

  @XmlElement(name = "address")
  private Address address;

  public String getName() {
    return name;
  }

  public ResumeHeader setName(String name) {
    this.name = name;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public ResumeHeader setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public ResumeHeader setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getHomepage() {
    return homepage;
  }

  public ResumeHeader setHomepage(String homepage) {
    this.homepage = homepage;
    return this;
  }

  public Address getAddress() {
    return address;
  }

  public ResumeHeader setAddress(Address address) {
    this.address = address;
    return this;
  }
}
