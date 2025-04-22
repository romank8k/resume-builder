package me.romankh.resumegenerator;

import lombok.Data;
import org.gwizard.logging.LoggingConfigProperties;
import org.gwizard.web.WebConfigProperties;

@Data
public class ResumeGeneratorConfig {
    /**
     * A bit of configuration for your own app
     */
    private String foo;

    /**
     * Some standard bits of configuration
     */
    private LoggingConfigProperties logging = new LoggingConfigProperties();
    private WebConfigProperties web = new WebConfigProperties();
}
