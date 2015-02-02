package me.romankh.resumegenerator;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import me.romankh.resumegenerator.configuration.Prop;
import me.romankh.resumegenerator.configuration.Property;
import me.romankh.resumegenerator.web.UrlPageBinding;
import me.romankh.resumegenerator.web.WebPackage;
import me.romankh.resumegenerator.web.pages.ResumeHtmlPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.EnumSet;

/**
 * @author Roman Khmelichek
 */
@Singleton
public class HTTPServer {
  private static final Logger logger = LogManager.getLogger(HTTPServer.class);

  private final Injector injector;
  private final String httpContextPath;
  private final GuiceFilter guiceFilter;

  // Our embedded Jetty server.
  private final Server server;

  @Inject
  public HTTPServer(Injector injector,
                    @Prop(Property.HTTP_BIND_ADDRESS) String httpBindAddress,
                    @Prop(Property.HTTP_PORT) int httpPort,
                    @Prop(Property.HTTP_CONTEXT_PATH) String httpContextPath,
                    GuiceFilter guiceFilter) {
    this.injector = injector;
    this.httpContextPath = httpContextPath;
    this.guiceFilter = guiceFilter;

    logger.info("Listening for HTTP connections on '{}:{}'", httpBindAddress, httpPort);

    Handler handler = buildHandler();
    server = new Server(InetSocketAddress.createUnresolved(httpBindAddress, httpPort));
    server.setHandler(handler);
    server.setStopAtShutdown(true);
  }

  public void run() {
    try {
      server.start();
      server.join();
    } catch (Exception e) {
      logger.error("Error starting Jetty", e);

      try {
        server.stop();
      } catch (Exception ee) {
        logger.error("Error stopping Jetty", ee);
      }
    }
  }

  public Handler buildHandler() {
    HandlerCollection handlerCollection = new HandlerCollection();
    handlerCollection.setHandlers(new Handler[]{
        buildStaticResourceContextHandler(),
        buildDynamicResourceContextHandler(),
        new DefaultHandler()
    });
    return handlerCollection;
  }

  public Handler buildDynamicResourceContextHandler() {
    ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
    ServletContextHandler servletContextHandler = new ServletContextHandler(
        contextHandlerCollection, httpContextPath, ServletContextHandler.SESSIONS);
    // Expose injector to web application.
    servletContextHandler.addEventListener(new GuiceServletContextListener() {
      @Override
      protected Injector getInjector() {
        return injector;
      }
    });

    // Add the JSTL to the Jetty classpath.
    URL tagLibResourceUrl = getClass().getClassLoader().getResource("taglib");
    logger.info("Tag lib location: " + tagLibResourceUrl);
    ClassLoader currClassLoader = Thread.currentThread().getContextClassLoader();
    URLClassLoader newClassLoader = new URLClassLoader(new URL[]{tagLibResourceUrl}, currClassLoader);
    Thread.currentThread().setContextClassLoader(newClassLoader);
    servletContextHandler.setClassLoader(newClassLoader);

    servletContextHandler.addServlet(new ServletHolder(new HttpServlet() {
      @Override
      protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.sendRedirect(req.getContextPath() + UrlPageBinding.getPageUrl(ResumeHtmlPage.class));
      }
    }), "/");

    String pages = ResumeHtmlPage.class.getPackage().getName().replace('.', '/');
    URL jspResourceUrl = getClass().getClassLoader().getResource(pages);
    logger.info("JSP location: " + jspResourceUrl);
    servletContextHandler.setBaseResource(Resource.newResource(jspResourceUrl));
    servletContextHandler.addServlet(new ServletHolder(new org.apache.jasper.servlet.JspServlet()), "*.jsp");

    FilterHolder guiceFilterHolder = new FilterHolder(guiceFilter);
    // We have to be careful here and not apply the filtering to dispatcher include/forward calls because we want to
    // bypass the Guice filter when rendering JSPs with Sitebricks. Otherwise, we get into an infinite filtering loop.
    servletContextHandler.addFilter(guiceFilterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));

    return contextHandlerCollection;
  }

  public Handler buildStaticResourceContextHandler() {
    String staticResources = WebPackage.class.getPackage().getName().replace('.', '/') + "/static";
    URL staticResourceUrl = getClass().getClassLoader().getResource(staticResources);
    logger.info("Static resources location: " + staticResourceUrl);

    ResourceHandler staticResourceHandler = new ResourceHandler();
    staticResourceHandler.setBaseResource(Resource.newResource(staticResourceUrl));

    ContextHandler contextHandler = new ContextHandler("/static");
    contextHandler.setHandler(staticResourceHandler);

    return contextHandler;
  }
}