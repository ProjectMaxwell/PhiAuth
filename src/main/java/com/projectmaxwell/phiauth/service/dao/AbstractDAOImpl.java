package com.projectmaxwell.phiauth.service.dao;

import javax.ws.rs.WebApplicationException;

import com.projectmaxwell.datasource.DatasourceConnection;
import com.projectmaxwell.phiauth.model.validation.RequestValidator;

public abstract class AbstractDAOImpl {
	
	protected RequestValidator validator;
	
	public AbstractDAOImpl() throws WebApplicationException{
		validator = new RequestValidator();
	}
}
