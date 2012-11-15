package com.projectmaxwell.phiauth.service.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.projectmaxwell.exception.InvalidLoginException;
import com.projectmaxwell.exception.UndefinedGrantTypeException;
import com.projectmaxwell.phiauth.model.TokenRequest;
import com.projectmaxwell.phiauth.model.TokenResponse;
import com.projectmaxwell.phiauth.service.dao.TokenDAO;
import com.projectmaxwell.phiauth.service.dao.impl.mysql.TokenDAOImpl;

@Path("/token")
@Consumes("application/json")
@Produces("application/json")
public class TokenResource {
	
	TokenDAO tokenDAO;
	
	public TokenResource(){
		tokenDAO = new TokenDAOImpl();
	}
	
	@POST
	public TokenResponse generateToken(TokenRequest tokenRequest){

		return tokenDAO.getToken(tokenRequest);
	}
	
	@GET
	@Path("/{access_token}")
	public TokenResponse validateToken(@PathParam("access_token") String accessToken){
		return tokenDAO.validateToken(accessToken);
	}
}
