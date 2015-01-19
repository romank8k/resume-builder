package me.romankh.resumegenerator.configuration;

import com.google.inject.TypeLiteral;

import java.util.List;

/**
 * @author Roman Khmelichek
 */
public enum Property {
  HTTP_BIND_ADDRESS(
      "httpBindAddress",
      "The address to which the HTTP server should bind to",
      new TypeLiteral<String>() {},
      "127.0.0.1"
  ),

  HTTP_PORT(
      "httpPort",
      "HTTP Server Port",
      new TypeLiteral<Integer>() {},
      "8080"
  ),

  HTTP_CONTEXT_PATH(
      "httpContextPath",
      "The servlet context path",
      new TypeLiteral<String>() {},
      "/"
  ),

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

  A_LIST_OF_INTS(
      "aListOfInts",
      "Just a list of integers",
      new TypeLiteral<List<Integer>>() {},
      "0, 1, 42"
  ),

  A_LIST_OF_STRINGS(
      "aListOfStrings",
      "Just a list of strings",
      new TypeLiteral<List<String>>() {},
      "hello, world"
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
