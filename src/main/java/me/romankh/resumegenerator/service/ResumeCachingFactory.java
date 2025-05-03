package me.romankh.resumegenerator.service;

import me.romankh.resumegenerator.parser.ResumeParser;
import org.xml.sax.SAXException;

import java.io.IOException;

public interface ResumeCachingFactory {
    ResumeParser getResume() throws IOException, SAXException;
}
