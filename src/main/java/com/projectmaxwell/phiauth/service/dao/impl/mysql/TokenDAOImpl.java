package com.projectmaxwell.phiauth.service.dao.impl.mysql;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;

import com.projectmaxwell.exception.InvalidAssertionException;
import com.projectmaxwell.exception.InvalidLoginException;
import com.projectmaxwell.exception.InvalidTokenException;
import com.projectmaxwell.exception.InvalidTokenRequestException;
import com.projectmaxwell.exception.UWNetIDException;
import com.projectmaxwell.exception.UndefinedAssertionTypeException;
import com.projectmaxwell.exception.UndefinedGrantTypeException;
import com.projectmaxwell.phiauth.model.GrantType;
import com.projectmaxwell.phiauth.model.AssertionType;
import com.projectmaxwell.phiauth.model.TokenRequest;
import com.projectmaxwell.phiauth.model.TokenResponse;
import com.projectmaxwell.phiauth.service.dao.TokenDAO;

public class TokenDAOImpl extends AbstractMySQLDAOImpl implements TokenDAO{
	


	@Override
	public TokenResponse getToken(TokenRequest tokenRequest) {
		validator.validateGrantType(tokenRequest);
		
		if(!checkGrantTypeAllowed(tokenRequest)){
			
		}
		
		TokenResponse tr;
		switch(tokenRequest.getGrantType()){
		case PASSWORD:
			tr = getTokenForPasswordGrant(tokenRequest);
				break;
		case REFRESH:
			tr = getTokenForRefreshGrant(tokenRequest);
			break;
		case ASSERTION:
			tr = getTokenForAssertionGrant(tokenRequest);
			break;
		case CLIENT_CREDENTIALS:
			tr = getTokenForClientCredentialsGrant(tokenRequest);
			break;
		default:
			throw new UndefinedGrantTypeException(String.valueOf(Math.random() * 100),
					"Grant Type '" + tokenRequest.getGrantType() + "' is not defined.");
		}
		return tr;
	}

	private boolean checkGrantTypeAllowed(TokenRequest tokenRequest) {
		CallableStatement call;
		try {
			call = con.prepareCall("CALL check_grant_type_allowed(?,?,?)");
			call.setString(1,tokenRequest.getClientId());
			call.setString(2, tokenRequest.getGrantType().name());
			if(tokenRequest.getAssertionType() != null && tokenRequest.getGrantType().equals(GrantType.ASSERTION)){
				call.setString(3, tokenRequest.getAssertionType().name());
			}else{
				call.setString(3, null);
			}
		} catch(SQLException sqle){
			throw new WebApplicationException(sqle);
		}		
		
		try{
			ResultSet result = call.executeQuery();
			if(!result.next()){
				System.out.println("BAD THINGS");
			}else if(!result.getBoolean("allowed")){
				throw new InvalidTokenRequestException(String.valueOf(Math.random()),"Could not grant token."
						+  "  Client is not explicitly allowed to use this grant type in this manner.");
			}else{
				return true;
			}
		}catch(SQLException sqle){
			if(sqle.getMessage().equalsIgnoreCase("ASSERTION_TYPE.MISSING")){
				throw new InvalidTokenRequestException(String.valueOf(Math.random()),"Could not grant token."
						+  "  Grants of type 'assertion' must include a valid assertion type field.");
			}else{
				throw new InvalidTokenRequestException(String.valueOf(Math.random()),"Unknown error prevented token grant." + sqle.getMessage());
			}
		}

		return true;
	}

	@Override
	public TokenResponse getTokenForPasswordGrant(TokenRequest tokenRequest) {
		validator.validatePasswordRequest(tokenRequest);
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setGrantType(tokenRequest.getGrantType());
		tokenResponse.setClientId(tokenRequest.getClientId());
		
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
			
			
			applyUserScopes(tokenResponse);
			createTokens(tokenResponse);
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
		tokenResponse.setGrantType(tokenRequest.getGrantType());
		tokenResponse.setClientId(tokenRequest.getClientId());
		
		try {
			CallableStatement call = con.prepareCall("CALL remove_refresh_token(?,?)");
			call.setString(1, tokenRequest.getRefreshToken());
			call.setString(2, tokenRequest.getClientId());
			ResultSet result = call.executeQuery();
			
			if(result.next()){
				tokenResponse.setUserId(result.getInt("user_id"));
				tokenResponse.setGrantType(GrantType.valueOf(result.getString("grant_type_name")));
				String assertionTypeName = result.getString("assertion_type_name");
				if(assertionTypeName != null){
					tokenResponse.setAssertionType(AssertionType.valueOf(assertionTypeName));
				}
				
				//Initialize array and do the first scope
				HashSet<String> scopes = new HashSet<String>();
				scopes.add(result.getString("scope_name"));
				//Iterate over the rest of the results
				while(result.next()){
					scopes.add(result.getString("scope_name"));
				}
				tokenResponse.setScopes(scopes.toArray(new String[scopes.size()]));
				
				createTokens(tokenResponse);
			}else{
				throw new InvalidTokenException(String.valueOf(Math.random())
						,"Token '" + tokenRequest.getRefreshToken() + "' is either expired or invalid.");
			}
			return tokenResponse;
		} catch (SQLException sqle) {
			throw new WebApplicationException(sqle);
		}
	}
	
	private TokenResponse getTokenForClientCredentialsGrant(TokenRequest tokenRequest) {
		validator.validateClientCredentialsRequest(tokenRequest);
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setGrantType(tokenRequest.getGrantType());
		tokenResponse.setClientId(tokenRequest.getClientId());
		
		try {
			CallableStatement call = con.prepareCall("CALL authenticate_by_client_credentials(?,?)");
			call.setString(1, tokenRequest.getClientId());
			call.setString(2, tokenRequest.getClientSecret());
			ResultSet result = call.executeQuery();
			
			if(result.next()){
//				tokenResponse.setUserId(1);
//				authorizeUser(tokenResponse);
				createTokens(tokenResponse);
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
		
		switch(tokenRequest.getAssertionType()){
		case UWNETID:
			return getTokenForUWNetID(tokenRequest);
		default:
			throw new UndefinedAssertionTypeException(String.valueOf(Math.random()),
					"No data accessor defined for unknown assertion type.");
		}

	}

	protected TokenResponse getTokenForUWNetID(TokenRequest tokenRequest) {
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setGrantType(tokenRequest.getGrantType());
		tokenResponse.setAssertionType(tokenRequest.getAssertionType());
		tokenResponse.setClientId(tokenRequest.getClientId());
		
		String uwnetidToken = tokenRequest.getAssertion().getAssertionValue();
		String externalId;
		
		/**
		 * See if we've recieved a login notification from UWNetID for specified token
		 * If we have, find out what UWNetID it was for
		 */
		try {
			CallableStatement call = con.prepareCall("CALL get_uwnetid_by_token(?)");
			call.setString(1, uwnetidToken);
			
			ResultSet result = call.executeQuery();
			if(!result.next()){
				throw new UWNetIDException(String.valueOf(Math.random()),
						"No external id found for uwnetid token '" + uwnetidToken + "'.");
			}else{
				externalId = result.getString("uwnetid");
			}
		} catch (SQLException sqle) {
			throw new InvalidAssertionException(String.valueOf(Math.random()),
					"Could not lookup uwnetid for uwnetid token.");
		}
		
		/**
		 * For the UWNetID we retrieved above, find out which maxwell user it maps to
		 */
		try {
			CallableStatement call = con.prepareCall("CALL get_user_by_third_party_id(?,?)");
			call.setString(1, "UWNETID");
			call.setString(2, externalId);
			
			ResultSet result = call.executeQuery();
			if(!result.next()){
				throw new InvalidAssertionException(String.valueOf(Math.random()),
						"No user found for external id '" + externalId + "'.");
			}else{
				if(!result.getBoolean("login_enabled")){
					throw new InvalidAssertionException(String.valueOf(Math.random()),
							"Found valid mapping for this user, but login by this mapping is disallowed.");
				}
				tokenResponse.setUserId(result.getInt("user_id"));
			}
		} catch (SQLException sqle) {
			if(sqle.getMessage().equalsIgnoreCase("INVALID.EXTERNAL_SYSTEM")){
				throw new InvalidAssertionException(String.valueOf(Math.random()),
						"Could not lookup user in unknown external system.");
			}
		}
		

		try {
			applyUserScopes(tokenResponse);
			createTokens(tokenResponse);
		} catch (SQLException sqle) {
			throw new InvalidTokenRequestException(String.valueOf(Math.random()),
					"Could not create auth token for UWNetID assertion due to exception.  " + sqle.getMessage());
		}
		return tokenResponse;
	}
	
	private void applyUserScopes(TokenResponse tokenResponse) throws SQLException {
		CallableStatement call;
		
		HashSet<String> scopes;
		if(tokenResponse.getScopes() == null){
			scopes = new HashSet<String>();
		}else{
			scopes = new HashSet<String>(Arrays.asList(tokenResponse.getScopes()));
		}
		call = con.prepareCall("CALL get_scopes_for_user(?)");
		call.setInt(1, tokenResponse.getUserId());
		ResultSet result = call.executeQuery();
		while(result.next()){
			scopes.add(result.getString("scope_name"));
		}	
		
		tokenResponse.setScopes(scopes.toArray(new String[scopes.size()]));
	}
	
	private void applyClientScopes(TokenResponse tokenResponse) throws SQLException {
		CallableStatement call;
		
		HashSet<String> scopes;
		if(tokenResponse.getScopes() == null){
			scopes = new HashSet<String>();
		}else{
			scopes = new HashSet<String>(Arrays.asList(tokenResponse.getScopes()));
		}
		call = con.prepareCall("CALL get_client_scopes(?)");
		call.setString(1, tokenResponse.getClientId());
		ResultSet result = call.executeQuery();
		while(result.next()){
			scopes.add(result.getString("scope_name"));
		}	
		
		tokenResponse.setScopes(scopes.toArray(new String[scopes.size()]));
	}

	private void createTokens(TokenResponse tokenResponse) throws SQLException {
		
		applyClientScopes(tokenResponse);
		
		tokenResponse.setAccessToken(UUID.randomUUID().toString());
		tokenResponse.setRefreshToken(UUID.randomUUID().toString());
		tokenResponse.setTtl(7200);

//		CREATE ME AND DEPRECATE ABOVE
  		PreparedStatement stmt = con.prepareStatement("CALL create_tokens(?,?,?,?,?,?,?)");
		stmt.setString(1, tokenResponse.getAccessToken());
		stmt.setString(2, tokenResponse.getRefreshToken());
		if(tokenResponse.getUserId() == 0){
			stmt.setNull(3, Types.INTEGER);
		}else{
			stmt.setInt(3, tokenResponse.getUserId());
		}
		stmt.setString(4, tokenResponse.getClientId());
		stmt.setString(5, tokenResponse.getGrantType().name());
		if(tokenResponse.getAssertionType() != null){
			stmt.setString(6, tokenResponse.getAssertionType().name());
		}else{
			stmt.setNull(6, Types.VARCHAR);
		}
		stmt.setInt(7,tokenResponse.getTtl());
		stmt.execute();
		
		for(String scope : tokenResponse.getScopes()){
			stmt = con.prepareStatement("CALL add_scope_to_access_token(?,?)");
			stmt.setString(1, tokenResponse.getAccessToken());
			stmt.setString(2, scope);
			stmt.execute();
		}
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
				tr.setClientId(result.getString("client_id"));
				tr.setGrantType(GrantType.valueOf(result.getString("grant_type_name")));	
				String assertionTypeName = result.getString("assertion_type_name");
				if(assertionTypeName != null){
					tr.setAssertionType(AssertionType.valueOf(assertionTypeName));
				}		
				
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
			throw new InvalidTokenException(String.valueOf(Math.random()),"Could not validate token due to exception.");
		}
		
		return tr;
	}

}
