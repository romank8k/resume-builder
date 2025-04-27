package me.romankh.resumegenerator.cmd;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import me.romankh.resumegenerator.ResumeGeneratorModule;
import me.romankh.resumegenerator.configuration.PropImpl;
import me.romankh.resumegenerator.configuration.Property;
import me.romankh.resumegenerator.service.ResumeGeneratorService;
import me.romankh.resumegenerator.annotations.binding.XSLT;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class RenderResumePDF {
  public static class Arguments {
    @Parameter(required = false, description = "Configuration properties file")
    private List<String> configPropertiesFiles = new ArrayList<>();

    @Parameter(help = true, names = "--help", description = "Usage")
    private Boolean help = false;
  }

  public static void main(String[] args) throws Exception {
    Arguments arguments = new Arguments();
    JCommander jCommander = new JCommander(arguments, args);
    jCommander.setProgramName(StartResumeGeneratorServer.class.getName());

    if (arguments.help) {
      jCommander.usage();
    } else {
      ResumeGeneratorModule resumeGeneratorModule;
      if (!arguments.configPropertiesFiles.isEmpty())
        resumeGeneratorModule = new ResumeGeneratorModule(false, arguments.configPropertiesFiles.get(0)) {
          @Override
          public void bindWebServices() {
          }
        };
      else
        resumeGeneratorModule = new ResumeGeneratorModule(false) {
          @Override
          public void bindWebServices() {
          }
        };

      Injector injector = Guice.createInjector(resumeGeneratorModule);
      ResumeGeneratorService resumeGeneratorService = injector.getInstance(Key.get(ResumeGeneratorService.class,
          XSLT.class));

      // We assume the input filename has some sort of extension,
      // such as ".xml" and our output file will have the same name,
      // but with the extension ".pdf".
      String resumeXmlPath = injector.getInstance(Key.get(String.class, new PropImpl(Property.RESUME_XML_PATH)));
      String outputFile = resumeXmlPath.substring(0, resumeXmlPath.lastIndexOf('.')) + ".pdf";
      OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));

      resumeGeneratorService.render(out);

      out.close();
    }
  }
}