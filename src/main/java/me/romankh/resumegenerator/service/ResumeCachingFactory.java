package me.romankh.resumegenerator.service;

import me.romankh.resumegenerator.parser.Resume;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * @author Roman Khmelichek
 */
public interface ResumeCachingFactory {
  Resume getResume() throws IOException, SAXException;
}
