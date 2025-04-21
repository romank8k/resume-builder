package me.romankh.resumegenerator.service.impl;

import com.google.common.io.CharStreams;
import me.romankh.resumegenerator.TestUtils;
import me.romankh.resumegenerator.model.Resume;
import me.romankh.resumegenerator.model.ResumeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import java.io.*;

/**
 * @author Roman Khmelichek
 */
public class ResumeGeneratorXSLTImplTest extends TestUtils {
  private static final Logger logger = LogManager.getLogger(ResumeGeneratorXSLTImplTest.class);

  private final ResumeUtils resumeUtils = new ResumeUtils();

  @Test
  public void resumeRenderTest() throws Exception {
    JAXBContext jaxbContext = resumeUtils.buildResumeJAXBContext();
    Resume resume = resumeUtils.buildResume();

    Marshaller marshaller = jaxbContext.createMarshaller();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    marshaller.marshal(resume, baos);

    InputStream xslIs = getResourceInputStream("resume.xsl");
    InputStream xmlIs = new ByteArrayInputStream(baos.toByteArray());

    String outputFile = "resume-test.pdf";
    OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));

    ResumeGeneratorXSLTImpl resumeGeneratorXSLT = new ResumeGeneratorXSLTImpl(new ResumeFactoryImpl());
    InputStream xslFoIs = resumeGeneratorXSLT.transformToFo(getResourceInputStream("resume.xsl"),
        new ByteArrayInputStream(baos.toByteArray()));

    InputStreamReader isr = new InputStreamReader(xslFoIs);
    StringWriter sw = new StringWriter();
    CharStreams.copy(isr, sw);
    logger.debug(sw.toString());
    resumeGeneratorXSLT.render(resume, xslIs, xmlIs, out);
  }
}
