package me.romankh.resumegenerator.service;

import java.io.OutputStream;

/**
 * @author Roman Khmelichek
 */
public interface ResumeGeneratorService {
  void render(OutputStream out) throws Exception;
}
