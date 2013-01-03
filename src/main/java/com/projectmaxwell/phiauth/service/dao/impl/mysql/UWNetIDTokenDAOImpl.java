package com.projectmaxwell.phiauth.service.dao.impl.mysql;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;

import com.projectmaxwell.exception.UWNetIDException;
import com.projectmaxwell.phiauth.model.uwnetid.UWNetIDTokenRequest;
import com.projectmaxwell.phiauth.model.uwnetid.UWNetIDTokenResponse;
import com.projectmaxwell.phiauth.service.dao.UWNetIDTokenDAO;

public class UWNetIDTokenDAOImpl extends AbstractMySQLDAOImpl implements UWNetIDTokenDAO {

	@Override
	public UWNetIDTokenResponse createToken(UWNetIDTokenRequest tokenRequest) {
		
		if(tokenRequest.getExpiration() == 0){
			tokenRequest.setExpiration((System.currentTimeMillis() / 1000) + 3600);
		}
		UUID token = UUID.randomUUID();
		
		CallableStatement call;
		try {
			call = con.prepareCall("CALL add_uwnetid_token(?,?,?)");
			
			call.setString(1, token.toString());
			call.setString(2, tokenRequest.getUwnetid());
			call.setLong(3, tokenRequest.getExpiration());
			int result = call.executeUpdate();
			if(!(result > 0)){
				throw new UWNetIDException(String.valueOf(Math.random()),"Could not add token for uwnetid.");
			}else{
				UWNetIDTokenResponse response = new UWNetIDTokenResponse();
				response.setExpiration(tokenRequest.getExpiration());
				response.setToken(token.toString());
				response.setUwnetid(tokenRequest.getUwnetid());
				return response;
			}
		}catch(SQLException sqle){
			if(sqle.getMessage().equalsIgnoreCase("LOGIN.DISABLED")){
				throw new UWNetIDException(String.valueOf(Math.random()),
						"This account is not permitted to login by UWNetID, so no token will be stored.");
			}else if(sqle.getMessage().equalsIgnoreCase("UNKNOWN.UWNETID")){
				throw new UWNetIDException(String.valueOf(Math.random()),
						"This is either not a valid UWNetID, or is not a UWNetID that has been mapped to a Maxwell user account.");
			}
			throw new WebApplicationException(sqle);
		}
	}

}
