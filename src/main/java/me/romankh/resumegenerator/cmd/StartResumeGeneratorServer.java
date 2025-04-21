package me.romankh.resumegenerator.cmd;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import me.romankh.resumegenerator.ResumeGeneratorModule;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
public class StartResumeGeneratorServer {
  public static class Arguments {
    @Parameter(required = false, description = "Configuration properties file")
    private List<String> configPropertiesFiles = new ArrayList<>();

    @Parameter(help = true, names = "--help", description = "Usage")
    private Boolean help = false;
  }

  public static void main(String[] args) {
    Arguments arguments = new Arguments();
    JCommander jCommander = new JCommander(arguments, args);
    jCommander.setProgramName(StartResumeGeneratorServer.class.getName());

    if (arguments.help) {
      jCommander.usage();
    } else {
      AbstractModule resumeGeneratorModule = arguments.configPropertiesFiles.isEmpty() ?
          new ResumeGeneratorModule(true) : new ResumeGeneratorModule(true, arguments.configPropertiesFiles.get(0));

      Injector injector = Guice.createInjector(resumeGeneratorModule);
//      HTTPServer httpServer = injector.getInstance(HTTPServer.class);
//      httpServer.run();
    }
  }
}
