package me.romankh.resumegenerator;

import com.google.common.io.CharStreams;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;

/**
 * @author Roman Khmelichek
 */
@Slf4j
public class TestUtils {
  private static final ClassLoader classLoader = TestUtils.class.getClassLoader();

  public String getResourceAsString(String path) throws IOException {
    URL baseUrl = classLoader.getResource(".");
    log.debug("Loading resource '{}' relative to base '{}'", path, baseUrl);

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
    log.debug("Getting resource '{}' input stream relative to base '{}'", path, baseUrl);
    return classLoader.getResourceAsStream(path);
  }

  URL getResourceBaseUrl() {
    return classLoader.getResource(".");
  }
}