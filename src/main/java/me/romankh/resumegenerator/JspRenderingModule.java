package me.romankh.resumegenerator;

import com.google.inject.servlet.ServletModule;
import com.googlecode.htmleasy.HtmleasyProviders;
import org.apache.jasper.servlet.JspServlet;

public class JspRenderingModule extends ServletModule {
    @Override
    protected void configureServlets() {
        // Ensure Htmleasy Provider classes are found.
        for (Class<?> c : HtmleasyProviders.getClasses()) {
            bind(c);
        }

        bind(JspServlet.class).in(com.google.inject.Singleton.class);
        serve("*.jsp").with(JspServlet.class);
    }
}
