package me.romankh.resumegenerator.service.impl;

import me.romankh.resumegenerator.annotations.binding.IsWebServer;
import me.romankh.resumegenerator.configuration.Prop;
import me.romankh.resumegenerator.configuration.Property;
import me.romankh.resumegenerator.parser.Resume;
import me.romankh.resumegenerator.service.InputFileResolver;
import me.romankh.resumegenerator.service.ResumeCachingFactory;
import me.romankh.resumegenerator.service.ResumeGeneratorService;
import org.apache.fop.apps.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * @author Roman Khmelichek
 */
@Singleton
public class ResumeGeneratorXSLTImpl implements ResumeGeneratorService {
  private static final Logger logger = LogManager.getLogger(ResumeGeneratorXSLTImpl.class);

  private boolean isWebServer;
  private final Boolean showPersonalDataOnWeb;
  private final InputFileResolver inputFileResolver;
  private final ResumeCachingFactory resumeCachingFactory;

  private final FopFactory fopFactory = FopFactory.newInstance();

  @Inject
  public ResumeGeneratorXSLTImpl(@IsWebServer boolean isWebServer,
                                 @Prop(Property.SHOW_PERSONAL_DATA_ON_WEB) Boolean showPersonalDataOnWeb,
                                 InputFileResolver inputFileResolver,
                                 ResumeCachingFactory resumeCachingFactory) throws Exception {
    this.isWebServer = isWebServer;
    this.showPersonalDataOnWeb = showPersonalDataOnWeb;
    this.inputFileResolver = inputFileResolver;
    this.resumeCachingFactory = resumeCachingFactory;

    try {
      URL fopConfUrl = getClass().getClassLoader().getResource("fop.xconf");
      fopFactory.setUserConfig(fopConfUrl.toString());

      // Ability to resolve fonts located on the classpath.
      FOURIResolver uriResolver = (FOURIResolver) fopFactory.getURIResolver();
      uriResolver.setCustomURIResolver(new ClasspathURIResolver());
    } catch (MalformedURLException e) {
      logger.error(e.getMessage(), e);
    }
  }

  public void render(OutputStream out) throws Exception {
    Resume resume = resumeCachingFactory.getResume();

    FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
    foUserAgent.setProducer("Resume Generator");
    foUserAgent.setCreator(resume.getHeader().getName());
    foUserAgent.setAuthor(resume.getHeader().getName());
    foUserAgent.setCreationDate(new Date());
    foUserAgent.setTitle(String.format("Resume - %s", resume.getHeader().getName()));

    try {
      Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

      TransformerFactory factory = TransformerFactory.newInstance();
      Transformer transformer = factory.newTransformer(new StreamSource(inputFileResolver.getResumeXslInputStream()));

      if (isWebServer) {
        // Set the value of a <param> tag in the stylesheet.
        transformer.setParameter("showPersonalData", showPersonalDataOnWeb);
      }

      Source source = new StreamSource(inputFileResolver.getResumeXmlInputStream());
      Result result = new SAXResult(fop.getDefaultHandler());
      transformer.transform(source, result);
    } finally {
      out.close();
    }
  }

  public static class ClasspathURIResolver implements URIResolver {
    @Override
    public Source resolve(String href, String base) throws TransformerException {
      Source source = null;
      InputStream inputStream = ClassLoader.getSystemResourceAsStream(href);
      if (inputStream != null) {
        source = new StreamSource(inputStream);
      }
      return source;
    }
  }
}
