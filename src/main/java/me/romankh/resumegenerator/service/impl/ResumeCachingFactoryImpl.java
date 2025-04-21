package me.romankh.resumegenerator.service.impl;

import me.romankh.resumegenerator.configuration.Property;
import me.romankh.resumegenerator.parser.ResumeParser;
import me.romankh.resumegenerator.service.InputFileResolver;
import me.romankh.resumegenerator.configuration.Prop;
import me.romankh.resumegenerator.service.ResumeCachingFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.util.Date;

/**
 * @author Roman Khmelichek
 */
@Singleton
public class ResumeCachingFactoryImpl implements ResumeCachingFactory {
  private static final Logger logger = LogManager.getLogger(ResumeCachingFactoryImpl.class);

  private final String resumeXmlPath;
  private final InputFileResolver inputFileResolver;

  private volatile ResumeParser resume;
  private volatile Date resumeLastModifiedDate;

  @Inject
  public ResumeCachingFactoryImpl(@Prop(Property.RESUME_XML_PATH) String resumeXmlPath,
                                  InputFileResolver inputFileResolver) {
    this.resumeXmlPath = resumeXmlPath;
    this.inputFileResolver = inputFileResolver;
  }

  public synchronized ResumeParser getResume() throws IOException, SAXException {
    Date modifiedSinceDate = inputFileResolver.getDateModifiedSince(resumeXmlPath, resumeLastModifiedDate);
    if (resume == null || modifiedSinceDate != null) {
      if (resume != null) {
        // The file has been modified. Only update the cached Resume if we successfully parsed the updated one.
        ResumeParser newResume = null;
        try {
          newResume = new ResumeParser(inputFileResolver.getResumeXmlInputStream());
        } catch (IOException | SAXException e) {
          logger.error("Unable to parse modified resume", e);
        }

        if (newResume != null) {
          resume = newResume;
          resumeLastModifiedDate = modifiedSinceDate;
          logger.info("Successfully parsed modified XML resume '{}'", resumeXmlPath);
        }
      } else {
        // OK to throw an Exception since we haven't successfully parsed the resume yet.
        resume = new ResumeParser(inputFileResolver.getResumeXmlInputStream());
        resumeLastModifiedDate = modifiedSinceDate;
      }
    }

    return resume;
  }
}
