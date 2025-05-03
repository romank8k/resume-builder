package me.romankh.resumegenerator.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

import java.util.ArrayList;
import java.util.List;

public class AwardsSection {
    @XmlAttribute(name = "include")
    private boolean include;

    @XmlElementWrapper(name = "awards")
    @XmlElement(name = "award")
    private List<Award> awards;

    public AwardsSection addAward(Award award) {
        if (awards == null)
            awards = new ArrayList<>();
        awards.add(award);
        return this;
    }

    public boolean isInclude() {
        return include;
    }

    public AwardsSection setInclude(boolean include) {
        this.include = include;
        return this;
    }

    public List<Award> getAwards() {
        return awards;
    }

    public AwardsSection setAwards(List<Award> awards) {
        this.awards = awards;
        return this;
    }

    public static class Award {
        @XmlAttribute(name = "include")
        private boolean include;

        @XmlElement(name = "title")
        private String title;

        @XmlElement(name = "timespan")
        private String timespan;

        public boolean isInclude() {
            return include;
        }

        public Award setInclude(boolean include) {
            this.include = include;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public Award setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getTimespan() {
            return timespan;
        }

        public Award setTimespan(String timespan) {
            this.timespan = timespan;
            return this;
        }
    }
}
