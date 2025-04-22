package com.googlecode.htmleasy.provider;

import com.googlecode.htmleasy.ViewException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Logger;

/**
 * This mapper allows us to use ViewException to trigger rendering of a view.
 * Sometimes this is easier than returning a View.
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
@Provider
public class ViewExceptionMapper implements ExceptionMapper<ViewException>
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ViewExceptionMapper.class.getName());

	/* (non-Javadoc)
	 * @see jakarta.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	//@Override
	public Response toResponse(ViewException ex)
	{
		return Response.ok().entity(ex.getView()).build();
	}

}
