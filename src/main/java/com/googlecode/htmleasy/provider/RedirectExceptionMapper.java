package com.googlecode.htmleasy.provider;

import com.googlecode.htmleasy.RedirectException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Logger;

/**
 * This mapper allows us to use RedirectException to issue, uh, redirects.
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
@Provider
public class RedirectExceptionMapper implements ExceptionMapper<RedirectException>
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(RedirectExceptionMapper.class.getName());

	/* (non-Javadoc)
	 * @see jakarta.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	//@Override
	public Response toResponse(RedirectException ex)
	{
		return Response.status(ex.getStatus()).location(ex.getPath()).build();
	}

}
