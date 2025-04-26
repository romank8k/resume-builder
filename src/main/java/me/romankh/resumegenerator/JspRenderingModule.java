package me.romankh.resumegenerator;

import com.google.inject.servlet.ServletModule;
import com.googlecode.htmleasy.HtmleasyProviders;

public class JspRenderingModule extends ServletModule {
    @Override
    protected void configureServlets() {
        // Ensure Htmleasy Provider classes are found.
        for (Class<?> c : HtmleasyProviders.getClasses()) {
            bind(c);
        }
    }
}
