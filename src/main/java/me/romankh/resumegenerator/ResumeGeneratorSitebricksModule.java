package me.romankh.resumegenerator;

import com.google.sitebricks.SitebricksModule;
import com.google.sitebricks.SitebricksServletModule;
import me.romankh.resumegenerator.web.pages.ResumeHtmlPage;

/**
 * @author Roman Khmelichek
 */
public class ResumeGeneratorSitebricksModule extends SitebricksModule {
  private final String webResourcePath;

  public ResumeGeneratorSitebricksModule() {
    super();

    webResourcePath = getClass().getPackage().getName().replace('.', '/');
  }

  @Override
  protected SitebricksServletModule servletModule() {
    return new SitebricksServletModule() {
      @Override
      protected void configurePreFilters() {
      }

      @Override
      protected void configurePostFilters() {
      }

      @Override
      protected void configureCustomServlets() {
      }
    };
  }

  @Override
  protected void configureSitebricks() {
    scan(ResumeHtmlPage.class.getPackage());

    at("/css/resume.css").export(buildStaticResourcePath("web/css/resume.css"));
  }

  public String buildStaticResourcePath(String relativePath) {
    return String.format("/%s/", webResourcePath);
  }
}
