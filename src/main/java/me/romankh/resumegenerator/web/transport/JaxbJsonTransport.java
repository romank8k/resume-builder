package me.romankh.resumegenerator.web.transport;

import com.google.inject.TypeLiteral;
import com.google.sitebricks.client.transport.Json;
import me.romankh.resumegenerator.model.HtmlTag;
import me.romankh.resumegenerator.model.MarkdownTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Roman Khmelichek
 */
@Singleton
public class JaxbJsonTransport extends Json {
  private static final Logger logger = LogManager.getLogger(JaxbJsonTransport.class);

  private final ConcurrentMap<Class, JAXBContext> jaxbContextMap;

  @Inject
  public JaxbJsonTransport() {
    this.jaxbContextMap = new ConcurrentHashMap<>();

    JAXBContext resumeJaxbContext;
    try {
      resumeJaxbContext = JAXBContext.newInstance(
          me.romankh.resumegenerator.model.Resume.class,
          HtmlTag.class,
          MarkdownTag.class);
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
    jaxbContextMap.put(me.romankh.resumegenerator.model.Resume.class, resumeJaxbContext);
  }

  @Override
  @SuppressWarnings(value = "unchecked")
  public <T> T in(InputStream in, Class<T> type) throws IOException {
    try {
      JAXBContext jaxbContext = jaxbContextMap.get(type);
      if (jaxbContext == null) {
        jaxbContext = JAXBContext.newInstance(type);
        jaxbContextMap.put(type, jaxbContext);
      }

      // TODO: These could be pooled per `type` (or per `jaxbContext`).
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
      unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, true);
      unmarshaller.setProperty(UnmarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);

      return (T) unmarshaller.unmarshal(in);
    } catch (JAXBException e) {
      logger.error(e.getMessage(), e);
    }

    return null;
  }

  @Override
  @SuppressWarnings(value = "unchecked")
  public <T> T in(InputStream in, TypeLiteral<T> type) throws IOException {
    Class<? super T> clazz = type.getRawType();
    Object obj = in(in, clazz);
    return (T) obj;
  }

  @Override
  public <T> void out(OutputStream out, Class<T> type, T data) throws IOException {
    try {
      JAXBContext jaxbContext = jaxbContextMap.get(type);
      if (jaxbContext == null) {
        jaxbContext = JAXBContext.newInstance(type);
        jaxbContextMap.put(type, jaxbContext);
      }

      // TODO: These could be pooled per `type` (or per `jaxbContext`).
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(data, out);
    } catch (JAXBException e) {
      logger.error(e.getMessage(), e);
    }
  }
}
