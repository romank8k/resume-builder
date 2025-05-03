package me.romankh.resumegenerator.service;

import com.google.inject.ImplementedBy;
import me.romankh.resumegenerator.model.Resume;
import me.romankh.resumegenerator.service.impl.ResumeGeneratorXSLTImpl;
import org.apache.fop.apps.FOPException;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@ImplementedBy(ResumeGeneratorXSLTImpl.class)
public interface ResumeGeneratorService {
    void render(OutputStream pdfOs) throws Exception;

    void render(Resume resume, InputStream xslIs, InputStream xmlIs, OutputStream pdfOs)
            throws TransformerException, FOPException, IOException;

    InputStream transformToFo(InputStream xslIs, InputStream xmlIs) throws TransformerException;
}
