package me.romankh.resumegenerator.model;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

public class Degree {
    @XmlAttribute(name = "include")
    private boolean include;

    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "timespan")
    private String timespan;

    @XmlElementWrapper(name = "accomplishments")
    @XmlElement(name = "accomplishment")
    private List<DegreeAccomplishment> degreeAccomplishments;

    public Degree addAccomplishment(DegreeAccomplishment degreeAccomplishment) {
        if (degreeAccomplishments == null)
            degreeAccomplishments = new ArrayList<>();
        degreeAccomplishments.add(degreeAccomplishment);
        return this;
    }

    public boolean isInclude() {
        return include;
    }

    public Degree setInclude(boolean include) {
        this.include = include;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Degree setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTimespan() {
        return timespan;
    }

    public Degree setTimespan(String timespan) {
        this.timespan = timespan;
        return this;
    }

    public List<DegreeAccomplishment> getDegreeAccomplishments() {
        return degreeAccomplishments;
    }

    public Degree setDegreeAccomplishments(List<DegreeAccomplishment> degreeAccomplishments) {
        this.degreeAccomplishments = degreeAccomplishments;
        return this;
    }

    public static class DegreeAccomplishment {
        @XmlAttribute(name = "include")
        private boolean include;

        @XmlValue
        @XmlAnyElement(value = HTMLHandler.class, lax = false)
        private String accomplishment;

        public boolean isInclude() {
            return include;
        }

        public DegreeAccomplishment setInclude(boolean include) {
            this.include = include;
            return this;
        }

        public String getAccomplishment() {
            return accomplishment;
        }

        public DegreeAccomplishment setAccomplishment(String accomplishment) {
            this.accomplishment = accomplishment;
            return this;
        }
    }
}
