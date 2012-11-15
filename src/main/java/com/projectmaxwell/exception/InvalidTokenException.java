package com.projectmaxwell.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
public class InvalidTokenException extends WebApplicationException {

	protected static final String errorCode = "INVALID_TOKEN";
	
	public InvalidTokenException(String errorId, String errorMessage) {
		super(Response.status(Response.Status.UNAUTHORIZED).entity(
				new MaxwellException(errorId, errorCode, errorMessage))
				.build()); 
		/*super(errorId, errorMessage);
		this.errorId = errorId;
		this.errorMessage = errorMessage;*/
	}
}
