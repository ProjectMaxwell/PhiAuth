package com.projectmaxwell.phiauth.service.dao.impl.mysql;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;

import com.projectmaxwell.exception.InvalidLoginException;
import com.projectmaxwell.exception.InvalidTokenException;
import com.projectmaxwell.exception.UndefinedGrantTypeException;
import com.projectmaxwell.phiauth.model.GrantType;
import com.projectmaxwell.phiauth.model.TokenRequest;
import com.projectmaxwell.phiauth.model.TokenResponse;
import com.projectmaxwell.phiauth.service.dao.TokenDAO;

public class TokenDAOImpl extends AbstractMySQLDAOImpl implements TokenDAO{
	


	@Override
	public TokenResponse getToken(TokenRequest tokenRequest) {
		validator.validateGrantType(tokenRequest);
		TokenResponse tr;
		switch(tokenRequest.getGrantType()){
		case PASSWORD:
			tr = getTokenForPasswordGrant(tokenRequest);
				break;
		case REFRESH:
			tr = getTokenForRefreshGrant(tokenRequest);
			break;
		default:
			throw new UndefinedGrantTypeException(String.valueOf(Math.random() * 100),
					"Grant Type '" + tokenRequest.getGrantType() + "' is not defined.");
		}
		return tr;
	}
	
	@Override
	public TokenResponse getTokenForPasswordGrant(TokenRequest tokenRequest) {
		validator.validatePasswordRequest(tokenRequest);
		TokenResponse tokenResponse = new TokenResponse();
		try{
			CallableStatement call = con.prepareCall("CALL login_user_by_password(?,?)");
			call.setInt(1, Integer.valueOf(tokenRequest.getUsername()));
			call.setString(2, tokenRequest.getPassword());
			ResultSet loginResult = call.executeQuery();
			if(!loginResult.next()){
				throw new InvalidLoginException(String.valueOf(Math.random()),"Could not complete login.  Either username/password were incorrect, " +
						"or this user does not have permission to login by username/password.");
			}else{
				tokenResponse.setUserId(loginResult.getInt("user_id"));
			}
			
			authorizeUser(tokenResponse);
		}catch(SQLException sqle){
			throw new WebApplicationException(sqle);
		}finally{
			releaseConnection();			
		}
		tokenResponse.setGrantType(GrantType.PASSWORD);
		
		return tokenResponse;
	}

	@Override
	public TokenResponse getTokenForRefreshGrant(TokenRequest tokenRequest) {
		validator.validateRefreshRequest(tokenRequest);
		TokenResponse tokenResponse = new TokenResponse();
		try {
			CallableStatement call = con.prepareCall("CALL remove_refresh_token(?)");
			call.setString(1, tokenRequest.getRefreshToken());
			ResultSet result = call.executeQuery();
			
			if(result.next()){
				tokenResponse.setUserId(result.getInt("user_id"));
				authorizeUser(tokenResponse);
			}else{
				throw new InvalidTokenException(String.valueOf(Math.random())
						,"Token '" + tokenRequest.getRefreshToken() + "' is either expired or invalid.");
			}
			return tokenResponse;
		} catch (SQLException sqle) {
			throw new WebApplicationException(sqle);
		}
	}

	@Override
	public TokenResponse getTokenForAssertionGrant(TokenRequest tokenRequest) {
		validator.validateAssertionRequest(tokenRequest);
		TokenResponse tokenResponse = new TokenResponse();
		/*try{
			CallableStatement call = con.prepareCall("CALL login_user_by_assertion(?,?)");
			call.setInt(1, Integer.valueOf(tokenRequest.getUsername()));
			call.setString(2, tokenRequest.getPassword());
			ResultSet loginResult = call.executeQuery();
			if(!loginResult.next()){
				throw new InvalidLoginException(String.valueOf(Math.random()),"Could not complete login.  Either username/password were incorrect, " +
						"or this user does not have permission to login by username/password.");
			}else{
				tokenResponse.setUserId(loginResult.getInt("user_id"));
				tokenResponse.setGrantType(GrantType.ASSERTION);
			}
			
			authorizeUser(tokenResponse);
		}catch(SQLException sqle){
			throw new WebApplicationException(sqle);
		}finally{
			releaseConnection();			
		}*/
		
		return tokenResponse;
	}

	private void authorizeUser(TokenResponse tokenResponse) throws SQLException {
		CallableStatement call;
		HashSet<String> scopes = new HashSet<String>();
		call = con.prepareCall("CALL get_scopes_for_user(?)");
		call.setInt(1, tokenResponse.getUserId());
		ResultSet result = call.executeQuery();
		while(result.next()){
			scopes.add(result.getString("scope_name"));
		}
		tokenResponse.setScopes(scopes.toArray(new String[scopes.size()]));
		tokenResponse.setAccessToken(UUID.randomUUID().toString());
		tokenResponse.setRefreshToken(UUID.randomUUID().toString());
		tokenResponse.setTtl(7200);
		
		PreparedStatement stmt = con.prepareStatement("CALL create_tokens_for_user(?,?,?,?)");
		stmt.setInt(1, tokenResponse.getUserId());
		stmt.setString(2, tokenResponse.getAccessToken());
		stmt.setString(3, tokenResponse.getRefreshToken());
		stmt.setInt(4,tokenResponse.getTtl());
//			stmt.setString(5, request.getGrantType().name());
		stmt.execute();
	}

	@Override
	public TokenResponse validateToken(String accessToken) {
		TokenResponse tr = new TokenResponse();
		try {
			CallableStatement call = con.prepareCall("CALL validate_access_token(?)");
			call.setString(1, accessToken);
			ResultSet result = call.executeQuery();
			if(result.next()){
				tr.setAccessToken(accessToken);
				tr.setUserId(result.getInt("user_id"));
				//Initialize array and do the first scope
				HashSet<String> scopes = new HashSet<String>();
				scopes.add(result.getString("scope_name"));
				//Iterate over the rest of the results
				while(result.next()){
					scopes.add(result.getString("scope_name"));
				}
				tr.setScopes(scopes.toArray(new String[scopes.size()]));
			}
			
		} catch (SQLException sqle) {
			throw new WebApplicationException(sqle);
		}
		
		return tr;
	}

}
