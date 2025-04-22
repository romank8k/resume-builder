/*
 * (c) Copyright 1999-2011 PaperCut Software Int. Pty. Ltd.
 * $Id$
 */
package com.googlecode.htmleasy;

import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

/**
 * Utility class to help with resolving paths to controllers (e.g. type-safe/refactor-save reverse routes).
 * It allows you to reference controller paths in your presentation layer (e.g. JSP) with a syntax like:
 * 
 *    <form method="POST" action="<%= Path.to(MyController.class) %>">
 *
 * or in your code:
 * 
 *    model.setPostUrl(Path.to(MyController.class));
 * 
 * 
 * @author Chris Dance <chris.dance@papercut.com>
 */
public class Path {
    
    /** Private constructor on utility class */
    private Path() {}

    /**
     * @param clazz
     *            A Path annotated class.
     * @return A string representing an absolute path to the annotated class (containing the context base).
     */
    public static String to(Class<?> clazz) {
        return to(clazz, null);
    }

    /**
     * @param clazz
     *            A Path annotated class.
     * @param method
     *            A Path annotated method on the corresponding class.
     * @return A string representing an absolute path to the annotated class (containing the context base).
     */
    public static String to(Class<?> clazz, String method) {
        ResteasyProviderFactory factory = ResteasyProviderFactory.getInstance(); // TODO:@roman obtain instance
        UriInfo uriinfo = factory.getContextData(UriInfo.class);

        String path = "";
        try {
            if (method != null) {
                path = uriinfo.getBaseUriBuilder().path(clazz).path(clazz, method).build().toURL().getPath();
            } else {
                path = uriinfo.getBaseUriBuilder().path(clazz).build().toURL().getPath();
            }
        } catch (Exception e) {
            throw new RuntimeException("Problem resolving path on: " + clazz.getName()
                    + " Is the class annotated with @Path?", e);
        }
        return path;

    }
}
