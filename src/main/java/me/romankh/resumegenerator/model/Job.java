package me.romankh.resumegenerator.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

import java.util.ArrayList;
import java.util.List;

public class Job {
    @XmlAttribute(name = "include")
    private boolean include;

    @XmlElement(name = "employer")
    private String employer;

    @XmlElement(name = "location")
    private String location;

    @XmlElementWrapper(name = "roles")
    @XmlElement(name = "role")
    private List<Role> roles;

    public Job addRole(Role role) {
        if (roles == null)
            roles = new ArrayList<>();
        roles.add(role);
        return this;
    }

    public boolean isInclude() {
        return include;
    }

    public Job setInclude(boolean include) {
        this.include = include;
        return this;
    }

    public String getEmployer() {
        return employer;
    }

    public Job setEmployer(String employer) {
        this.employer = employer;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Job setLocation(String location) {
        this.location = location;
        return this;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public Job setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }
}
