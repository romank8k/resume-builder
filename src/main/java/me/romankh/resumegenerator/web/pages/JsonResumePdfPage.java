package me.romankh.resumegenerator.web.pages;

import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Post;
import me.romankh.resumegenerator.annotations.binding.XSLT;
import me.romankh.resumegenerator.model.Resume;
import me.romankh.resumegenerator.service.InputFileResolver;
import me.romankh.resumegenerator.service.ResumeFactory;
import me.romankh.resumegenerator.service.ResumeGeneratorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Singleton;
import javax.xml.bind.JAXBException;
import java.io.*;

/**
 * @author Roman Khmelichek
 */
@At("/resume-json-pdf")
@Service
@Singleton
public class JsonResumePdfPage {
  private static final Logger logger = LogManager.getLogger(JsonResumePdfPage.class);

  private final InputFileResolver inputFileResolver;
  private final ResumeGeneratorService resumeGeneratorService;
  private final ResumeFactory resumeFactory;

  @Inject
  public JsonResumePdfPage(InputFileResolver inputFileResolver,
                           @XSLT ResumeGeneratorService resumeGeneratorService,
                           ResumeFactory resumeFactory) {
    this.inputFileResolver = inputFileResolver;
    this.resumeGeneratorService = resumeGeneratorService;
    this.resumeFactory = resumeFactory;
  }

  @Post
  public Reply<InputStream> handler(Request<String> request) throws JAXBException {
    // Unmarshall from JSON.
    final Resume resume = request.read(Resume.class).as(Json.class);

    // Marshall to XML.
    final InputStream resumeXmlIs = resumeFactory.buildXMLStreamFromJAXBResumeModel(resume);

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
              resumeGeneratorService.render(resume, inputFileResolver.getResumeXslInputStream(), resumeXmlIs, os);
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

    return Reply.with(is).type("application/pdf");
  }
}
