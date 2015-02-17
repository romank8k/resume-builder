package me.romankh.resumegenerator;

import com.google.common.io.CharStreams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;

/**
 * @author Roman Khmelichek
 */
public class TestUtils {
  private static final Logger logger = LogManager.getLogger(TestUtils.class);

  private static final ClassLoader classLoader = TestUtils.class.getClassLoader();

  public String getResourceAsString(String path) throws IOException {
    URL baseUrl = classLoader.getResource(".");
    logger.debug("Loading resource '{}' relative to base '{}'", path, baseUrl);

    String resourceStr;
    InputStream is = classLoader.getResourceAsStream(path);
    if (is != null) {
      InputStreamReader isr = new InputStreamReader(is);
      StringWriter sw = new StringWriter();
      CharStreams.copy(isr, sw);
      resourceStr = sw.toString();
    } else {
      throw new FileNotFoundException(String.format("Resource '%s' not found on the classpath", path));
    }

    return resourceStr;
  }

  public File getResourceAsFile(String path) {
    URL baseUrl = classLoader.getResource(".");
    return new File(baseUrl.getPath(), path);
  }

  public InputStream getResourceInputStream(String path) throws IOException {
    URL baseUrl = classLoader.getResource(".");
    logger.debug("Getting resource '{}' input stream relative to base '{}'", path, baseUrl);
    return classLoader.getResourceAsStream(path);
  }

  URL getResourceBaseUrl() {
    return classLoader.getResource(".");
  }
}