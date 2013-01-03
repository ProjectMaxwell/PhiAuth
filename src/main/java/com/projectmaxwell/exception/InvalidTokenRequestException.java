package com.projectmaxwell.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
public class InvalidTokenRequestException extends WebApplicationException {

	protected static final String errorCode = "INVALID_TOKEN_REQUEST";
	
	public InvalidTokenRequestException(String errorId, String errorMessage) {
		super(Response.status(Response.Status.UNAUTHORIZED).entity(
				new MaxwellException(errorId, errorCode, errorMessage))
				.build()); 
		/*super(errorId, errorMessage);
		this.errorId = errorId;
		this.errorMessage = errorMessage;*/
	}
}
