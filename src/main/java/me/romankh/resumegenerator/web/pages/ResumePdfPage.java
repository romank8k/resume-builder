package me.romankh.resumegenerator.web.pages;

import com.google.inject.Inject;
import me.romankh.resumegenerator.annotations.binding.XSLT;
import me.romankh.resumegenerator.service.CachingPDFRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * @author Roman Khmelichek
 */
public class ResumePdfPage {
  private static final Logger logger = LogManager.getLogger(ResumePdfPage.class);

  private final CachingPDFRenderer cachingPdfRenderer;

  @Inject
  public ResumePdfPage(@XSLT CachingPDFRenderer cachingPdfRenderer) {
    this.cachingPdfRenderer = cachingPdfRenderer;
  }

  public InputStream handler() {
    PipedInputStream pipedInputStream = new PipedInputStream();
    PipedOutputStream pipedOutputStream;
    try {
      pipedOutputStream = new PipedOutputStream(pipedInputStream);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }

    final OutputStream os = pipedOutputStream;
    final InputStream is = pipedInputStream;

    // Need to make sure to write to the output stream in a separate thread to make sure we do not deadlock.
    new Thread(
        new Runnable() {
          public void run() {
            try {
              cachingPdfRenderer.render(os);
            } catch (Exception e) {
              logger.error("Unable to render PDF", e);
              try {
                os.close();
              } catch (IOException ee) {
                logger.error(ee.getMessage(), e);
              }
            }
          }
        }
    ).start();

    return is;
  }
}
