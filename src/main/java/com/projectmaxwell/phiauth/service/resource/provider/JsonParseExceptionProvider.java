package com.projectmaxwell.phiauth.service.resource.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.JsonParseException;

import com.projectmaxwell.exception.MalformedJSONException;

@Provider
public class JsonParseExceptionProvider implements ExceptionMapper<JsonParseException> {

	@Override
	public Response toResponse(JsonParseException arg0) {
		return (new MalformedJSONException(String.valueOf(Math.random()),"Malformed JSON, could not deserialize.  " + arg0.getMessage())).getResponse();
	}

}
