package me.romankh.resumegenerator.service.impl;

import me.romankh.resumegenerator.annotations.binding.Defaults;
import me.romankh.resumegenerator.configuration.Property;
import me.romankh.resumegenerator.service.IText;
import me.romankh.resumegenerator.service.InputFileResolver;
import me.romankh.resumegenerator.service.ResumeGeneratorService;
import me.romankh.resumegenerator.configuration.Prop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * @author Roman Khmelichek
 */
@Singleton
public class CachingITextPDFRenderer extends AbstractCachingPDFRenderer {
  private static final Logger logger = LogManager.getLogger(CachingITextPDFRenderer.class);

  private final String resumeXmlPath;
  private final InputFileResolver inputFileResolver;
  private final ResumeGeneratorService resumeGeneratorService;

  private volatile byte[] cachedPdfBytes;
  private volatile Date resumeXmlLastModifiedDate;

  @Inject
  public CachingITextPDFRenderer(@Prop(Property.RESUME_XML_PATH) String resumeXmlPath,
                                 @Defaults boolean useDefaults,
                                 InputFileResolver inputFileResolver,
                                 @IText ResumeGeneratorService resumeGeneratorService) {
    this.resumeXmlPath = resumeXmlPath;
    this.inputFileResolver = inputFileResolver;
    this.resumeGeneratorService = resumeGeneratorService;
  }

  public synchronized void render(OutputStream os) throws Exception {
    Date modifiedSinceDate = inputFileResolver.getDateModifiedSince(resumeXmlPath, resumeXmlLastModifiedDate);
    if (cachedPdfBytes == null || modifiedSinceDate != null) {
      ByteArrayOutputStream cachingOutputStream = new ByteArrayOutputStream();
      resumeGeneratorService.render(cachingOutputStream);
      cachingOutputStream.flush();
      cachedPdfBytes = cachingOutputStream.toByteArray();
      resumeXmlLastModifiedDate = modifiedSinceDate;
    }

    writeCachedBytes(cachedPdfBytes, os);
  }
}
