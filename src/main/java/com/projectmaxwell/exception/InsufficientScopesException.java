package com.projectmaxwell.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
public class InsufficientScopesException extends WebApplicationException {

	protected static final String errorCode = "INSUFFICIENT_SCOPES";
	
	public InsufficientScopesException(String errorId, String errorMessage) {
		super(Response.status(Response.Status.UNAUTHORIZED).entity(
				new MaxwellException(errorId, errorCode, errorMessage))
				.build()); 
		
	}
}
