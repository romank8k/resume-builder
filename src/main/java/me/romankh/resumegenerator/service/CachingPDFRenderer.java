package me.romankh.resumegenerator.service;

import java.io.OutputStream;

public interface CachingPDFRenderer {
    void render(OutputStream pdfOs) throws Exception;
}
