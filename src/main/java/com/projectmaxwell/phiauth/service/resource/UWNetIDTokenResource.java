package com.projectmaxwell.phiauth.service.resource;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.projectmaxwell.exception.InvalidTokenException;
import com.projectmaxwell.phiauth.model.TokenResponse;
import com.projectmaxwell.phiauth.model.uwnetid.UWNetIDTokenRequest;
import com.projectmaxwell.phiauth.model.uwnetid.UWNetIDTokenResponse;
import com.projectmaxwell.phiauth.service.dao.TokenDAO;
import com.projectmaxwell.phiauth.service.dao.UWNetIDTokenDAO;
import com.projectmaxwell.phiauth.service.dao.impl.mysql.TokenDAOImpl;
import com.projectmaxwell.phiauth.service.dao.impl.mysql.UWNetIDTokenDAOImpl;

@Path("/uwnetid_token")
@Consumes("application/json")
@Produces("application/json")
public class UWNetIDTokenResource extends AbstractResource {

	UWNetIDTokenDAO uwnetidTokenDAO;
	TokenDAO tokenDAO;
	
	public UWNetIDTokenResource(){
		super();
		uwnetidTokenDAO = new UWNetIDTokenDAOImpl();
		tokenDAO = new TokenDAOImpl();
	}
	
	@POST
	@RolesAllowed({"generate_uwnetid_token"})
	public UWNetIDTokenResponse createToken(UWNetIDTokenRequest tokenRequest, @HeaderParam("Authorization") String token){
		/*		TokenResponse validationResponse;
		try {
			validationResponse = tokenDAO.validateToken(token);
		} catch(Exception e){
			throw new InvalidTokenException(String.valueOf(Math.random()),
					"Could not validate token.");
		}
		String[] allowedScopes = {"generate_uwnetid_token"};
		hasScope(validationResponse, allowedScopes);*/
		return uwnetidTokenDAO.createToken(tokenRequest);
	}
}
