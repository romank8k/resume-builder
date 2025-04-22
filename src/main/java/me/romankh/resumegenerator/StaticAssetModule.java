package me.romankh.resumegenerator;

import com.google.inject.servlet.ServletModule;
import io.dropwizard.servlets.assets.AssetServlet;
import jakarta.inject.Singleton;

import java.nio.charset.StandardCharsets;

public class StaticAssetModule extends ServletModule {
    @Singleton
    public static class StaticAssetServlet extends AssetServlet {
        public StaticAssetServlet() {
            super("/me/romankh/resumegenerator/web/static", "/static", "index.html", StandardCharsets.UTF_8);
        }
    }

    @Override
    protected void configureServlets() {
        serve("/*").with(StaticAssetServlet.class);
    }
}
