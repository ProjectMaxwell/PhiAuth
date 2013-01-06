package com.projectmaxwell.phiauth.model.validation;

import java.util.UUID;

import com.projectmaxwell.exception.InvalidLoginException;
import com.projectmaxwell.exception.UndefinedAssertionTypeException;
import com.projectmaxwell.exception.UndefinedGrantTypeException;
import com.projectmaxwell.phiauth.model.Assertion;
import com.projectmaxwell.phiauth.model.AssertionType;
import com.projectmaxwell.phiauth.model.GrantType;
import com.projectmaxwell.phiauth.model.TokenRequest;

public class RequestValidator {

	public boolean validateRefreshRequest(TokenRequest tokenRequest){
		String refreshToken = tokenRequest.getRefreshToken();
		
		if(refreshToken == null || refreshToken.length() < 1
				){
			throw new InvalidLoginException(UUID.randomUUID().toString(),
					"Cannot grant token.  Refresh grants require a valid refresh token.");
		}
		return true;
	}
	
	public boolean validatePasswordRequest(TokenRequest tokenRequest){
		String password = tokenRequest.getPassword();
		String username = tokenRequest.getUsername();
		String clientId = tokenRequest.getClientId();
		
		if(password == null || password.length() < 1 
				|| username == null || username.length() < 1
				|| clientId == null || clientId.length() < 1){
			throw new InvalidLoginException(UUID.randomUUID().toString(),
					"Cannot grant token.  Password grants require a valid clientId, username, and password.");
		}
		return true;
	}
	
	public boolean validateAssertionRequest(TokenRequest tokenRequest){
		String clientId = tokenRequest.getClientId();
		AssertionType assertionType = tokenRequest.getAssertionType();
		Assertion assertion = tokenRequest.getAssertion();
		
		/**
		 * We'll leave the validation of client secret to be done on a per-assertionType basis
		 */
		if(assertionType == null
				|| clientId == null || clientId.length() < 1
				|| assertion == null){
			throw new InvalidLoginException(UUID.randomUUID().toString(),
					"Cannot grant token.  Assertion grants require a valid clientId, clientSecret, assertionType, and assertion.");
		}
		
		switch(tokenRequest.getAssertionType()){
		case UWNETID:
			return validateUWNetIDAssertion(tokenRequest.getAssertion());
		case FACEBOOK:
			return validateFacebookAssertion(tokenRequest);
		default:
			throw new UndefinedAssertionTypeException(UUID.randomUUID().toString(),
					"Assertion Type '" + tokenRequest.getAssertionType().toString() + "' is not yet implemented.");
		}
	}

	private boolean validateFacebookAssertion(TokenRequest tokenRequest) {
		throw new UndefinedAssertionTypeException(UUID.randomUUID().toString(),
				"Assertion Type '" + tokenRequest.getAssertionType().toString() + "' is not yet implemented.");
	}

	private boolean validateUWNetIDAssertion(Assertion assertion) {
		if((assertion == null) || (assertion.getAssertionValue() == null)){
			throw new UndefinedAssertionTypeException(UUID.randomUUID().toString(),
					"UWNetID assertion did not contain a valid assertion value.");
		}
		return true;
	}

	public boolean validateGrantType(TokenRequest tokenRequest){
		GrantType grantType = tokenRequest.getGrantType();
		if(grantType == null){
			throw new UndefinedGrantTypeException(UUID.randomUUID().toString(),
					"Cannot grant token due to missing grantType.  Valid values are 'PASSWORD' and 'ASSERTION'");
		}
		return true;
	}

	public boolean validateClientCredentialsRequest(TokenRequest tokenRequest) {
		String clientSecret = tokenRequest.getClientSecret();
		String clientId = tokenRequest.getClientId();
		
		if(clientId == null || clientId.length() < 1 
				|| clientSecret == null || clientSecret.length() < 1){
			throw new InvalidLoginException(UUID.randomUUID().toString(),
					"Cannot grant token.  Client credentials grants require a valid clientId and client secret.");
		}
		return true;		
	}
}
