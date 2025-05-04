package me.romankh.resumegenerator.cmd;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import me.romankh.resumegenerator.JspRenderingModule;
import me.romankh.resumegenerator.ResumeGeneratorConfig;
import me.romankh.resumegenerator.ResumeGeneratorModule;
import me.romankh.resumegenerator.StaticAssetModule;
import org.gwizard.config.ConfigModule;
import org.gwizard.logging.LoggingModule;
import org.gwizard.rest.RestModule;
import org.gwizard.services.Run;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

            final Injector injector = Guice.createInjector(
                    resumeGeneratorModule,
                    new ConfigModule(new File("resume-generator.yml"), ResumeGeneratorConfig.class),
                    new LoggingModule(),
                    new JspRenderingModule(),
                    new RestModule("/web"),
                    new StaticAssetModule()
            );

            injector.getInstance(Run.class).start();
        }
    }
}
