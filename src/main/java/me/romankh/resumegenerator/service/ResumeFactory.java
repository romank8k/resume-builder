package me.romankh.resumegenerator.service;

import me.romankh.resumegenerator.model.Resume;
import me.romankh.resumegenerator.parser.ResumeParser;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;

public interface ResumeFactory {
  public ResumeParser buildSAXResumeModelFromXMLStream(InputStream is)
      throws IOException, SAXException;

  public Resume buildJAXBResumeModelFromXMLStream(InputStream is) throws JAXBException;

  public InputStream buildXMLStreamFromJAXBResumeModel(me.romankh.resumegenerator.model.Resume resume)
      throws JAXBException;
}
