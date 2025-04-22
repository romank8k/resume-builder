package me.romankh.resumegenerator;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.google.inject.util.Providers;
import io.dropwizard.jackson.Jackson;
import jakarta.inject.Singleton;
import me.romankh.resumegenerator.annotations.binding.ConfigFilePath;
import me.romankh.resumegenerator.annotations.binding.Defaults;
import me.romankh.resumegenerator.annotations.binding.IsWebServer;
import me.romankh.resumegenerator.annotations.binding.XSLT;
import me.romankh.resumegenerator.configuration.AllProperties;
import me.romankh.resumegenerator.configuration.PropImpl;
import me.romankh.resumegenerator.configuration.Property;
import me.romankh.resumegenerator.configuration.converters.CommaDelimitedListTypeConverter;
import me.romankh.resumegenerator.service.*;
import me.romankh.resumegenerator.service.impl.*;
import me.romankh.resumegenerator.web.resource.ApiResource;
import me.romankh.resumegenerator.web.resource.WebResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gwizard.logging.LoggingConfig;
import org.gwizard.web.WebConfig;
import org.gwizard.web.WebServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Roman Khmelichek
 */
public class ResumeGeneratorModule extends AbstractModule {
  private static final Logger logger = LogManager.getLogger(ResumeGeneratorModule.class);

  private static final String DEFAULT_PROPERTIES_FILE = "resume.config.properties";

  private final boolean isWebServer;
  private final String configPropertiesFile;
  private final Properties configProperties;
  private final boolean useDefaultProperties;

  public ResumeGeneratorModule(boolean isWebServer) {
    this.isWebServer = isWebServer;
    this.useDefaultProperties = true;
    this.configPropertiesFile = DEFAULT_PROPERTIES_FILE;
    logger.info("Loading default configuration from classpath: {}", DEFAULT_PROPERTIES_FILE);

    try {
      this.configProperties = loadPropertiesFromClasspath(DEFAULT_PROPERTIES_FILE);
    } catch (IOException e) {
      throw new RuntimeException("Unable to load default properties", e);
    }
  }

  public ResumeGeneratorModule(boolean isWebServer, String configPropertiesFile) {
    this.isWebServer = isWebServer;
    this.useDefaultProperties = false;
    this.configPropertiesFile = configPropertiesFile;
    logger.info("Loading configuration from file: {}", configPropertiesFile);

    try {
      this.configProperties = loadPropertiesFromFilePath(configPropertiesFile);
    } catch (IOException e) {
      throw new RuntimeException(String.format("Unable to load properties from file: %s", configPropertiesFile), e);
    }
  }

  @Override
  protected void configure() {
    bindConfigs();
    bindWebServices();
    bindServices();
    bindFactories();
  }

  @Provides
  @Singleton
  public ObjectMapper objectMapper() {
    return Jackson.newObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  @Provides
  public LoggingConfig loggingConfig(final ResumeGeneratorConfig cfg) {
    return cfg.getLogging();
  }

  @Provides
  public WebConfig webConfig(final ResumeGeneratorConfig cfg) {
    return cfg.getWeb();
  }

  private void bindConfigs() {
    // Inject type safe properties via Guice.
    // http://beust.com/weblog/2013/07/12/flexible-configuration-with-guice/
    AllProperties allProps = new AllProperties();
    for (Object keyObj : configProperties.keySet()) {
      String key = (String) keyObj;
      String value = configProperties.getProperty(key);

      if (allProps.isProperty(key)) {
        allProps.addProperty(key, value);

        logger.info("Loaded config '{}': {}", key, value);
      } else {
        logger.warn("Property with key '{}' is invalid", key);
      }
    }

    // A note on null configuration values:
    // If an injected value is allowed to be null, it must also be annotated with @Nullable,
    // otherwise Guice refuses to inject null values (and your program will crash when the dependency
    // is attempted to be injected --- which is a good reason to avoid null values altogether).
    Binder binder = binder();

    // Define custom converters for converting constant binding string values at runtime.
    CommaDelimitedListTypeConverter commaDelimitedListTypeConverter = new CommaDelimitedListTypeConverter();
    convertToTypes(Matchers.only(new TypeLiteral<List<String>>() {}), commaDelimitedListTypeConverter);
    convertToTypes(Matchers.only(new TypeLiteral<List<Integer>>() {}), commaDelimitedListTypeConverter);
    convertToTypes(Matchers.only(new TypeLiteral<List<Long>>() {}), commaDelimitedListTypeConverter);
    convertToTypes(Matchers.only(new TypeLiteral<List<Float>>() {}), commaDelimitedListTypeConverter);
    convertToTypes(Matchers.only(new TypeLiteral<List<Double>>() {}), commaDelimitedListTypeConverter);

    // The injector has not been initialized yet, but we still need to get property values (for logging purposes).
    Map<Key<?>, String> keyValueMap = new HashMap<>();

    for (Property prop : Property.values()) {
      TypeLiteral<Object> typeLiteral = prop.getValueType();

      String value = allProps.getPropertyValue(prop);
      if (value == null) {
        // Handle 'null' default values --- best to avoid these as they'll require a @NotNull annotation
        // in order to be injected.
        binder.bind(Key.get(typeLiteral, new PropImpl(prop))).toProvider(Providers.of(null));
      } else {
        // The string 'value' will be converted upon injection at runtime.
        // The type to convert to is determined by the type of the variable being injected.
        bindConstant().annotatedWith(new PropImpl(prop)).to(value);

        keyValueMap.put(Key.get(typeLiteral, new PropImpl(prop)), value);
      }
    }

    bind(boolean.class).annotatedWith(IsWebServer.class).toInstance(isWebServer);
    bind(String.class).annotatedWith(ConfigFilePath.class).toInstance(configPropertiesFile);
    bind(boolean.class).annotatedWith(Defaults.class).toInstance(useDefaultProperties);

    String resumeXmlPath = keyValueMap.get(Key.get(new TypeLiteral<String>(){}, new PropImpl(Property.RESUME_XML_PATH)));
    String resumeXslPath = keyValueMap.get(Key.get(new TypeLiteral<String>(){}, new PropImpl(Property.RESUME_XSL_PATH)));

    logger.info("Using resume XML file: {}", resumeXmlPath);
    logger.info("Using resume XSL file: {}", resumeXslPath);
  }

  public void bindWebServices() {
    bind(WebServer.class).to(ResumeGeneratorWebServer.class);
    bind(ApiResource.class);
    bind(WebResource.class);
  }

  @SuppressWarnings("deprecation")
  private void bindServices() {
    bind(ResumeGeneratorService.class).annotatedWith(XSLT.class).to(ResumeGeneratorXSLTImpl.class);
    bind(CachingPDFRenderer.class).annotatedWith(XSLT.class).to(CachingXSLTPDFRenderer.class);

    bind(InputFileResolver.class).to(InputFileResolverImpl.class);
    bind(ResumeFactory.class).to(ResumeFactoryImpl.class);
  }

  private void bindFactories() {
    // Parse the resume on startup so the cache is warm.
    bind(ResumeCachingFactory.class).to(ResumeCachingFactoryImpl.class).asEagerSingleton();
  }

  private Properties loadPropertiesFromClasspath(String propertiesFile) throws IOException {
    Properties properties = new Properties();
    InputStream propertiesStream = getClass().getClassLoader().getResourceAsStream(propertiesFile);
    properties.load(propertiesStream);
    return properties;
  }

  private Properties loadPropertiesFromFilePath(String propertiesFile) throws IOException {
    Properties properties = new Properties();
    InputStream propertiesStream = new FileInputStream(propertiesFile);
    properties.load(propertiesStream);
    return properties;
  }
}
