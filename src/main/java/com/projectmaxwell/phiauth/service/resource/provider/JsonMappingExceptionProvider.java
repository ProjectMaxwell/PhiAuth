package com.projectmaxwell.phiauth.service.resource.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.JsonMappingException;

import com.projectmaxwell.exception.InvalidJSONException;

@Provider
public class JsonMappingExceptionProvider implements ExceptionMapper<JsonMappingException> {

	@Override
	public Response toResponse(JsonMappingException arg0) {
		return (new InvalidJSONException(String.valueOf(Math.random()),"Could not deserialize JSON into object.  " + arg0.getMessage())).getResponse();
	}

}
