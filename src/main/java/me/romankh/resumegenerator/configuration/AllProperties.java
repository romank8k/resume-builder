package me.romankh.resumegenerator.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Roman Khmelichek
 */
public class AllProperties {
  private static final Logger logger = LogManager.getLogger(AllProperties.class);

  private final Map<String, Property> propertyNameMap = new HashMap<>();
  private final Map<String, String> propertyValueMap = new HashMap<>();

  public AllProperties() {
    Property[] properties = Property.values();
    for (Property property: properties) {
      propertyNameMap.put(property.getName(), property);
    }
  }

  public boolean isProperty(String key) {
    return propertyNameMap.get(key) != null;
  }

  public void addProperty(String key, String value) {
    propertyValueMap.put(key, value);
  }

  public String getPropertyValue(Property property) {
    String value = propertyValueMap.get(property.getName());
    if (value == null) {
      value = property.getDefaultValue();
      logger.debug("Using default value for config property '{}: {}", property.getName(), value);
    }

    return value;
  }
}
