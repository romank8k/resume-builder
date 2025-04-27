package me.romankh.resumegenerator.web.pages;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import me.romankh.resumegenerator.annotations.binding.XSLT;
import me.romankh.resumegenerator.service.CachingPDFRenderer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

@Slf4j
public class ResumePdfPage {
    private final CachingPDFRenderer cachingPdfRenderer;

    @Inject
    public ResumePdfPage(@XSLT CachingPDFRenderer cachingPdfRenderer) {
        this.cachingPdfRenderer = cachingPdfRenderer;
    }

    public InputStream handler() throws IOException {
        final PipedInputStream is = new PipedInputStream(1024 * 1024);
        final PipedOutputStream os = new PipedOutputStream(is);

        // Need to make sure to write to the output stream in a separate thread to make sure we do not deadlock.
        new Thread(
                () -> {
                    try {
                        cachingPdfRenderer.render(os);
                    } catch (Exception e) {
                        log.error("Unable to render PDF", e);
                        try {
                            os.close();
                        } catch (IOException ee) {
                            log.error(ee.getMessage(), e);
                        }
                    }
                }
        ).start();

        return is;
    }
}
