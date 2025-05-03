package me.romankh.resumegenerator.service.impl;

import me.romankh.resumegenerator.service.CachingPDFRenderer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

abstract class AbstractCachingPDFRenderer implements CachingPDFRenderer {
    void writeCachedBytes(byte[] cachedPdfBytes, OutputStream os) throws IOException {
        ByteArrayInputStream cachedInputStream = new ByteArrayInputStream(cachedPdfBytes);
        final int bufLen = 4096;
        final byte[] buf = new byte[bufLen];
        int ret;
        while ((ret = cachedInputStream.read(buf, 0, bufLen)) >= 0) {
            os.write(buf, 0, ret);
        }
        os.close();
    }
}
