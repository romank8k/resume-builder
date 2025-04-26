package me.romankh.resumegenerator.web.resource;

import com.googlecode.htmleasy.View;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.romankh.resumegenerator.ResumeGeneratorConfig;
import me.romankh.resumegenerator.web.pages.ResumeHtmlPage;

@Path("/html")
@Produces(MediaType.TEXT_HTML)
@Consumes(MediaType.TEXT_HTML)
public class ResumeHtmlResource {
    private final ResumeHtmlPage resumeHtmlPage;

    @Inject
    public ResumeHtmlResource(ResumeGeneratorConfig cfg,
                              ResumeHtmlPage resumeHtmlPage) {
        this.resumeHtmlPage = resumeHtmlPage;
    }

    @GET
    @Path("/")
    public Response view() {
        return Response.ok(new View("/pages/resume.jsp", resumeHtmlPage)).build();
    }
}
