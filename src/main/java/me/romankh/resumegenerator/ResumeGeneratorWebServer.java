package me.romankh.resumegenerator;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.jetty.ee10.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.ee10.jsp.JettyJspServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.gwizard.web.EventListenerScanner;
import org.gwizard.web.HandlerScanner;
import org.gwizard.web.WebConfig;
import org.gwizard.web.WebServer;

@Singleton
public class ResumeGeneratorWebServer extends WebServer {
    @Inject
    public ResumeGeneratorWebServer(WebConfig webConfig, EventListenerScanner eventListenerScanner, HandlerScanner handlerScanner) {
        super(webConfig, eventListenerScanner, handlerScanner);
    }

    @Override
    protected ServletContextHandler createRootServletContextHandler() {
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");

        webAppContext.addServletContainerInitializer(new JettyJasperInitializer());
        webAppContext.addServlet(JettyJspServlet.class, "*.jsp");

        webAppContext.setBaseResource(ResourceFactory.of(webAppContext).newResource(
                getClass().getClassLoader().getResource("me/romankh/resumegenerator/web")));
        webAppContext.setClassLoader(Thread.currentThread().getContextClassLoader());

        // This pattern ensures JSTL JARs are scanned for TLDs.
        webAppContext.setAttribute(
                "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                ".*\\.jar$"
        );

        return webAppContext;
    }
}
