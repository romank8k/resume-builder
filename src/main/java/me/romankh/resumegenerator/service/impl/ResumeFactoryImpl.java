package me.romankh.resumegenerator.service.impl;

import me.romankh.resumegenerator.model.HtmlTag;
import me.romankh.resumegenerator.model.MarkdownTag;
import me.romankh.resumegenerator.model.Resume;
import me.romankh.resumegenerator.parser.ResumeParser;
import me.romankh.resumegenerator.service.ResumeFactory;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.inject.Singleton;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Roman Khmelichek
 */
@Singleton
public class ResumeFactoryImpl implements ResumeFactory {
  private final JAXBContext jaxbResumeContext;

  @Inject
  public ResumeFactoryImpl() {
    try {
      jaxbResumeContext = buildJAXBResumeContext();
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ResumeParser buildSAXResumeModelFromXMLStream(InputStream is)
      throws IOException, SAXException {
    return new ResumeParser(is);
  }

  @Override
  public Resume buildJAXBResumeModelFromXMLStream(InputStream is) throws JAXBException {
    // TODO: Can re-use Unmarshaller instances, but not share them amongst multiple threads.
    Unmarshaller unmarshaller = jaxbResumeContext.createUnmarshaller();
    return (Resume) unmarshaller.unmarshal(is);
  }

  @Override
  public InputStream buildXMLStreamFromJAXBResumeModel(Resume resume)
      throws JAXBException {
    // TODO: Can re-use Marshaller instances, but not share them amongst multiple threads.
    Marshaller marshaller = jaxbResumeContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    marshaller.marshal(resume, baos);
    return new ByteArrayInputStream(baos.toByteArray());
  }

  JAXBContext buildJAXBResumeContext() throws JAXBException {
    return JAXBContext.newInstance(
        Resume.class,
        HtmlTag.class,
        MarkdownTag.class);
  }
}
