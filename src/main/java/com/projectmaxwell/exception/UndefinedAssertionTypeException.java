package com.projectmaxwell.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
public class UndefinedAssertionTypeException extends WebApplicationException {

	protected static final String errorCode = "UNDEFINED_ASSERTION_TYPE";
	
	public UndefinedAssertionTypeException(String errorId, String errorMessage) {
		super(Response.status(Response.Status.BAD_REQUEST).entity(
				new MaxwellException(errorId, errorCode, errorMessage))
				.build()); 
	}
}
