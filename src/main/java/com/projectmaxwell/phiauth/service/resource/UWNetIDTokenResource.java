package com.projectmaxwell.phiauth.service.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.projectmaxwell.phiauth.model.uwnetid.UWNetIDTokenRequest;
import com.projectmaxwell.phiauth.model.uwnetid.UWNetIDTokenResponse;
import com.projectmaxwell.phiauth.service.dao.UWNetIDTokenDAO;
import com.projectmaxwell.phiauth.service.dao.impl.mysql.UWNetIDTokenDAOImpl;

@Path("/uwnetid_token")
@Consumes("application/json")
@Produces("application/json")
public class UWNetIDTokenResource {

	UWNetIDTokenDAO uwnetidTokenDAO;
	
	public UWNetIDTokenResource(){
		super();
		uwnetidTokenDAO = new UWNetIDTokenDAOImpl();
	}
	
	@POST
	public UWNetIDTokenResponse createToken(UWNetIDTokenRequest tokenRequest){
		return uwnetidTokenDAO.createToken(tokenRequest);
	}
}
