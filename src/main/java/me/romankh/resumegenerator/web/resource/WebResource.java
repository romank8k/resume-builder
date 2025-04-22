package me.romankh.resumegenerator.web.resource;

import com.googlecode.htmleasy.View;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.romankh.resumegenerator.ResumeGeneratorConfig;

@Path("/web")
@Produces(MediaType.TEXT_HTML)
@Consumes(MediaType.TEXT_HTML)
public class WebResource {
    private final HttpHeaders headers;

    @Inject
    public WebResource(final ResumeGeneratorConfig cfg, final HttpHeaders headers) {
        this.headers = headers;
    }

    @GET
    @Path("/test")
    public Response view() {
        TestModel testModel = new TestModel("ROMAN");
        return Response.ok(new View("/test.jsp", testModel)).build();
    }

    public static class TestModel {
        private final String name;

        public TestModel(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
