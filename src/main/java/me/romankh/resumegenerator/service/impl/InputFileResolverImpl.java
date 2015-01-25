package me.romankh.resumegenerator.service.impl;

import me.romankh.resumegenerator.annotations.binding.ConfigFilePath;
import me.romankh.resumegenerator.annotations.binding.Defaults;
import me.romankh.resumegenerator.configuration.Prop;
import me.romankh.resumegenerator.configuration.Property;
import me.romankh.resumegenerator.service.InputFileResolver;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.util.Date;

/**
 * @author Roman Khmelichek
 */
@Singleton
public class InputFileResolverImpl implements InputFileResolver {
  private final String resumeXmlPath;
  private final String resumeXslPath;
  private final boolean useDefaults;
  private final String configFilePath;

  @Inject
  public InputFileResolverImpl(@Prop(Property.RESUME_XML_PATH) String resumeXmlPath,
                               @Prop(Property.RESUME_XSL_PATH) String resumeXslPath,
                               @Defaults boolean useDefaults,
                               @ConfigFilePath String configFilePath) {
    this.resumeXmlPath = resumeXmlPath;
    this.resumeXslPath = resumeXslPath;
    this.useDefaults = useDefaults;
    this.configFilePath = configFilePath;
  }

  public InputStream getResumeXmlInputStream() throws FileNotFoundException {
    return getInputStream(resumeXmlPath);
  }

  public InputStream getResumeXslInputStream() throws FileNotFoundException {
    return getInputStream(resumeXslPath);
  }

  public InputStream getInputStream(String filePath) throws FileNotFoundException {
    InputStream is;
    if (useDefaults) {
      is = getClass().getClassLoader().getResourceAsStream(filePath);
    } else {
      File file = resolveFilePath(filePath);
      is = new BufferedInputStream(new FileInputStream(file));
    }

    return is;
  }

  /**
   * Returns null if the file at 'filePath' has not been modified since it was cached ('cachedFileModifiedDate').
   * Null is also returned when using the default input files (since they are on the classpath, they can be in a jar)
   * or if the file can not be found.
   * Otherwise, returns the file's modification date.
   *
   * @param filePath
   * @param cachedFileModifiedDate
   * @return
   */
  public Date getDateModifiedSince(String filePath, Date cachedFileModifiedDate) {
    if (!useDefaults) {
      File file = resolveFilePath(filePath);
      if (file.exists()) {
        Date modifiedDate = new Date(file.lastModified());
        if (cachedFileModifiedDate == null || modifiedDate.after(cachedFileModifiedDate)) {
          return modifiedDate;
        }
      }
    }

    return null;
  }

  File resolveFilePath(String filePath) {
    // If the file path is not absolute, resolve it relative to the config properties file in which it is specified.
    File file = new File(filePath);
    if (!file.isAbsolute()) {
      File configFile = new File(configFilePath);
      file = new File(configFile.getParent(), filePath);
    }

    return file;
  }
}
