package me.romankh.resumegenerator.service.impl;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import me.romankh.resumegenerator.annotations.binding.IsWebServer;
import me.romankh.resumegenerator.configuration.Prop;
import me.romankh.resumegenerator.configuration.Property;
import me.romankh.resumegenerator.model.Resume;
import me.romankh.resumegenerator.service.InputFileResolver;
import me.romankh.resumegenerator.service.ResumeCachingFactory;
import me.romankh.resumegenerator.service.ResumeFactory;
import me.romankh.resumegenerator.service.ResumeGeneratorService;
import org.apache.fop.apps.*;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

@Singleton
@Slf4j
public class ResumeGeneratorXSLTImpl implements ResumeGeneratorService {
    private final TransformerFactory transformerFactory = TransformerFactory.newInstance();

    private boolean isWebServer;
    private final boolean showPersonalDataOnWeb;
    private final InputFileResolver inputFileResolver;
    private final ResumeCachingFactory resumeCachingFactory;
    private final ResumeFactory resumeFactory;
    private final FopFactory fopFactory;

    ResumeGeneratorXSLTImpl(ResumeFactory resumeFactory) throws Exception {
        this.isWebServer = true;
        this.showPersonalDataOnWeb = true;
        this.inputFileResolver = null;
        this.resumeCachingFactory = null;
        this.resumeFactory = resumeFactory;
        this.fopFactory = buildFopFactory();
    }

    @Inject
    public ResumeGeneratorXSLTImpl(@IsWebServer boolean isWebServer,
                                   @Prop(Property.SHOW_PERSONAL_DATA_ON_WEB) boolean showPersonalDataOnWeb,
                                   InputFileResolver inputFileResolver,
                                   ResumeCachingFactory resumeCachingFactory,
                                   ResumeFactory resumeFactory) throws Exception {
        this.isWebServer = isWebServer;
        this.showPersonalDataOnWeb = showPersonalDataOnWeb;
        this.inputFileResolver = inputFileResolver;
        this.resumeCachingFactory = resumeCachingFactory;
        this.resumeFactory = resumeFactory;
        this.fopFactory = buildFopFactory();
    }

    FopFactory buildFopFactory() throws SAXException, IOException, URISyntaxException {
        InputStream fopConfIs = getClass().getClassLoader().getResourceAsStream("fop.xconf");
        FopConfParser fopConfParser = new FopConfParser(fopConfIs, new URI("classpath:/fop.xconf"));
        FopFactoryBuilder fopFactoryBuilder = fopConfParser.getFopFactoryBuilder();
        return fopFactoryBuilder.build();
    }

    @Override
    public void render(OutputStream pdfOs) throws Exception {
        Resume resume = resumeFactory.buildJAXBResumeModelFromXMLStream(inputFileResolver.getResumeXmlInputStream());
        InputStream resumeXmlIs = resumeFactory.buildXMLStreamFromJAXBResumeModel(resume);
        render(resume, inputFileResolver.getResumeXslInputStream(), resumeXmlIs, pdfOs);
    }

    public void render(Resume resume, InputStream xslIs, InputStream xmlIs, OutputStream pdfOs)
            throws TransformerException, FOPException, IOException {
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        foUserAgent.setProducer("Resume Builder");
        foUserAgent.setCreator(resume.getResumeHeader().getName());
        foUserAgent.setAuthor(resume.getResumeHeader().getName());
        foUserAgent.setCreationDate(new Date());
        foUserAgent.setTitle(String.format("Resume - %s", resume.getResumeHeader().getName()));

        try {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, pdfOs);

            Transformer transformer = transformerFactory.newTransformer(new StreamSource(xslIs));

            if (isWebServer) {
                transformer.setParameter("showPersonalData", showPersonalDataOnWeb);
            }

            Source source = new StreamSource(xmlIs);
            Result result = new SAXResult(fop.getDefaultHandler());
            transformer.transform(source, result);
        } finally {
            pdfOs.close();
        }
    }

    public InputStream transformToFo(InputStream xslIs, InputStream xmlIs) throws TransformerException {
        Source xmlSource = new StreamSource(xmlIs);
        Source xslSource = new StreamSource(xslIs);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(baos);
        Transformer transformer = transformerFactory.newTransformer(xslSource);
        transformer.transform(xmlSource, result);
        return new ByteArrayInputStream(baos.toByteArray());
    }
}
