package me.romankh.resumegenerator.web.resource;

import com.codahale.metrics.annotation.Timed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import lombok.Value;
import me.romankh.resumegenerator.ResumeGeneratorConfig;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiResource {
    private final ResumeGeneratorConfig cfg;
    private final HttpHeaders headers;

    @Inject
    public ApiResource(final ResumeGeneratorConfig cfg, final HttpHeaders headers) {
        this.cfg = cfg;
        this.headers = headers;
    }

    @Value
    public static class Stuff {
        String foo;
    }

    @GET
    @Path("/stuff")
    public Stuff stuff() {
        return new Stuff(cfg.getFoo());
    }

    @Timed
    @GET
    @Path("/headers")
    public HttpHeaders headers() {
        return headers;
    }
}
