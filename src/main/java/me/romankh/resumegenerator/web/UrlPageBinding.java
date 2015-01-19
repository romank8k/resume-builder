package me.romankh.resumegenerator.web;

import com.google.sitebricks.At;

/**
 * @author Roman Khmelichek
 */
public class UrlPageBinding {
  public static String getPageUrl(Class<?> page) {
    At annotation = page.getAnnotation(At.class);
    if (annotation == null) {
      return null;
    }
    return annotation.value();
  }
}
