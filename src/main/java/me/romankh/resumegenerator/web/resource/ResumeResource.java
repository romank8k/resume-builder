package me.romankh.resumegenerator.web.resource;

import com.google.common.io.ByteStreams;
import com.googlecode.htmleasy.View;
import com.googlecode.htmleasy.ViewException;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import lombok.extern.slf4j.Slf4j;
import me.romankh.resumegenerator.ResumeGeneratorConfig;
import me.romankh.resumegenerator.web.pages.ResumeHtmlPage;
import me.romankh.resumegenerator.web.pages.ResumePdfPage;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Path("/")
public class ResumeResource {
    private final ResumeHtmlPage resumeHtmlPage;
    private final ResumePdfPage resumePdfPage;

    @Inject
    public ResumeResource(ResumeGeneratorConfig cfg,
                          ResumeHtmlPage resumeHtmlPage,
                          ResumePdfPage resumePdfPage) {
        this.resumeHtmlPage = resumeHtmlPage;
        this.resumePdfPage = resumePdfPage;
    }

    @GET
    @Path("/html")
    @Produces(MediaType.TEXT_HTML)
    public Response html() {
        return Response.ok(new View("/pages/resume.jsp", resumeHtmlPage)).build();
    }

    @GET
    @Path("/pdf")
    @Produces("application/pdf")
    public Response pdf() {
        StreamingOutput stream;
        try {
            InputStream is = resumePdfPage.handler();
            stream = os -> {
                try {
                    ByteStreams.copy(is, os);
                } catch (IOException e) {
                    is.close();
                }
            };
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ViewException(new View("/error.jsp"));
        }

        Response.ResponseBuilder responseBuilder = Response.ok(stream)
                .header("Content-Disposition", "inline; filename=\"resume.pdf\"")
                .header("Content-Type", "application/pdf");
        return responseBuilder.build();
    }
}
