package me.romankh.resumegenerator.configuration;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AllProperties {
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
      log.debug("Using default value for config property '{}: {}", property.getName(), value);
    }

    return value;
  }
}
