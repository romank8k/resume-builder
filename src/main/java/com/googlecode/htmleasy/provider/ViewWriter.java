package com.googlecode.htmleasy.provider;

import com.googlecode.htmleasy.ViewWith;
import com.googlecode.htmleasy.Viewable;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.core.ResteasyContext;
import org.jboss.resteasy.spi.InternalServerErrorException;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Logger;

/**
 * JAX-RS provider for viewable objects. Handles all media types so that it can look for relevant @ViewWith annotations.
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 * @author Vivian Steller <vivian@steller.info>
 */
@Provider
public class ViewWriter implements MessageBodyWriter<Object>
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ViewWriter.class.getName());
	
	private ViewResolver viewResolver = new ViewResolver();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jakarta.ws.rs.ext.MessageBodyWriter#getSize(java.lang.Object, java.lang.Class, java.lang.reflect.Type,
	 * java.lang.annotation.Annotation[], jakarta.ws.rs.core.MediaType)
	 */
	@Override
	public long getSize(Object obj, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		// No chance of figuring this out ahead of time
		return -1;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jakarta.ws.rs.ext.MessageBodyWriter#isWriteable(java.lang.Class, java.lang.reflect.Type,
	 * java.lang.annotation.Annotation[], jakarta.ws.rs.core.MediaType)
	 */
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return viewResolver.isResolvable(type, genericType, annotations);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jakarta.ws.rs.ext.MessageBodyWriter#writeTo(java.lang.Object, java.lang.Class, java.lang.reflect.Type,
	 * java.lang.annotation.Annotation[], jakarta.ws.rs.core.MediaType, jakarta.ws.rs.core.MultivaluedMap,
	 * java.io.OutputStream)
	 */
	@Override
	public void writeTo(Object obj, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException
	{
		Viewable viewingPleasure = viewResolver.getView(obj, type, genericType, annotations);
		
		if (viewingPleasure == null)
			throw new InternalServerErrorException("No " + ViewWith.class.getSimpleName() + " annotation found for object of type " + type.getName());
		
		HttpServletRequest request = ResteasyContext.getContextData(HttpServletRequest.class);
		HttpServletResponse response = ResteasyContext.getContextData(HttpServletResponse.class);
		
		try
		{
			viewingPleasure.render(request, response);
		}
		catch (ServletException ex)
		{
			throw new WebApplicationException(ex);
		}
	}
	
	public ViewResolver getViewResolver()
	{
		return viewResolver;
	}
	
	public void setViewResolver(ViewResolver viewResolver)
	{
		this.viewResolver = viewResolver;
	}
	
}
