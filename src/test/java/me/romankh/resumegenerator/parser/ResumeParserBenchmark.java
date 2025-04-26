package me.romankh.resumegenerator.parser;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import me.romankh.resumegenerator.TestUtils;
import me.romankh.resumegenerator.model.Resume;
import me.romankh.resumegenerator.model.ResumeUtils;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.testng.Assert.assertNotNull;

/**
 * @author Roman Khmelichek
 */
@Slf4j
public class ResumeParserBenchmark extends TestUtils {
  private final ResumeUtils resumeUtils = new ResumeUtils();
  private final JAXBContext jaxbContext = buildJaxbContext();
  private final Unmarshaller unmarshaller = buildUnmarshaller();

  @Test(description = "Benchmark JAXB vs custom SAX")
  public void bench() throws Exception {
    int numIterations = 100;

    InputStream is = getResourceInputStream("resume-test.xml");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    IOUtils.copy(is, baos);
    byte[] bytes = baos.toByteArray();

    long totalMillisSax = 0;
    long totalMillisJaxb = 0;
    for (int i = 0; i < numIterations; i++) {
      long startSax = System.currentTimeMillis();
      parseSax(bytes);
      long endSax = System.currentTimeMillis();
      totalMillisSax += (endSax - startSax);


      long startJaxb = System.currentTimeMillis();
      parseJaxb(bytes);
      long endJaxb = System.currentTimeMillis();
      totalMillisJaxb += (endJaxb - startJaxb);
    }

    log.info("SAX seconds: {}", (totalMillisSax / 1000.));
    log.info("JAXB seconds: {}", (totalMillisJaxb / 1000.));
  }

  void parseSax(byte[] bytes) throws Exception {
    ResumeParser resume = new ResumeParser(buildInputStream(bytes));
    assertNotNull(resume);
  }

  void parseJaxb(byte[] bytes) throws Exception {
    Resume resume = (Resume) unmarshaller.unmarshal(buildInputStream(bytes));
    assertNotNull(resume);
  }

  ByteArrayInputStream buildInputStream(byte[] bytes) {
    return new ByteArrayInputStream(bytes);
  }

  Unmarshaller buildUnmarshaller() {
    try {
      return jaxbContext.createUnmarshaller();
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  JAXBContext buildJaxbContext() {
    try {
      return resumeUtils.buildResumeJAXBContext();
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }
}
