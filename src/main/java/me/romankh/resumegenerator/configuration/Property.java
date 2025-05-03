package me.romankh.resumegenerator.configuration;

import com.google.inject.TypeLiteral;

public enum Property {
    RESUME_XML_PATH(
            "resumeXmlPath",
            "Path to the XML resume",
            new TypeLiteral<String>() {},
            "resume.xml"
    ),

    RESUME_XSL_PATH(
            "resumeXslPath",
            "Path to the XSLT stylesheet for formatting the resume as a PDF",
            new TypeLiteral<String>() {},
            "resume.xsl"
    ),

    SHOW_PERSONAL_DATA_ON_WEB(
            "showPersonalDataOnWeb",
            "Whether to show phone, address, and email on web versions of resume",
            new TypeLiteral<Boolean>() {},
            "false"
    ),
    ;

    private String name;
    private String description;
    private TypeLiteral<?> type;
    private String defaultValue;

    private <V> Property(String name,
                         String description,
                         TypeLiteral<V> type,
                         String defaultValue) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @SuppressWarnings("unchecked")
    public <V> TypeLiteral<V> getValueType() {
        return (TypeLiteral<V>) type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
