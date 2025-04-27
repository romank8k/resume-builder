package me.romankh.resumegenerator.service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

public interface InputFileResolver {
  InputStream getResumeXmlInputStream() throws FileNotFoundException;

  InputStream getResumeXslInputStream() throws FileNotFoundException;

  Date getDateModifiedSince(String xmlFilePath, Date cachedFileModifiedDate);
}
