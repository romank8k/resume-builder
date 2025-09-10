package me.romankh.resumegenerator.service;

import jakarta.xml.bind.JAXBException;
import me.romankh.resumegenerator.model.Resume;
import me.romankh.resumegenerator.parser.ResumeParser;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

public interface ResumeFactory {
    ResumeParser buildSAXResumeModelFromXMLStream(InputStream is)
            throws IOException, SAXException;

    Resume buildJAXBResumeModelFromXMLStream(InputStream is) throws JAXBException;

    InputStream buildXMLStreamFromJAXBResumeModel(me.romankh.resumegenerator.model.Resume resume)
            throws JAXBException;
}
