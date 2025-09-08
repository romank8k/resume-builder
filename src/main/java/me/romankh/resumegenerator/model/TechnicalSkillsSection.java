package me.romankh.resumegenerator.model;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

public class TechnicalSkillsSection {
    @XmlAttribute(name = "include")
    private boolean include;

    @XmlElementWrapper(name = "skills")
    @XmlElement(name = "skill")
    private List<TechnicalSkill> skills;

    public TechnicalSkillsSection addSkill(TechnicalSkill skill) {
        if (skills == null)
            skills = new ArrayList<>();
        skills.add(skill);
        return this;
    }

    public boolean isInclude() {
        return include;
    }

    public TechnicalSkillsSection setInclude(boolean include) {
        this.include = include;
        return this;
    }

    public List<TechnicalSkill> getSkills() {
        return skills;
    }

    public TechnicalSkillsSection setSkills(List<TechnicalSkill> skills) {
        this.skills = skills;
        return this;
    }

    public static class TechnicalSkill {
        @XmlAttribute(name = "include")
        private boolean include;

        @XmlValue
        @XmlAnyElement(value = HTMLHandler.class, lax = false)
        private String skill;

        public boolean isInclude() {
            return include;
        }

        public TechnicalSkill setInclude(boolean include) {
            this.include = include;
            return this;
        }

        public String getSkill() {
            return skill;
        }

        public TechnicalSkill setSkill(String skill) {
            this.skill = skill;
            return this;
        }
    }
}
