package com.projectmaxwell.phiauth.model;

import org.codehaus.jackson.annotate.JsonProperty;

import com.projectmaxwell.exception.UndefinedAssertionTypeException;
import com.projectmaxwell.exception.UndefinedGrantTypeException;



public class TokenRequest {

	private GrantType grantType;
	private String username;
	private String password;
	private AssertionType assertionType;
	private String[] scope;
	private Assertion assertion;
	private String refreshToken;
	private String clientId;
	private String clientSecret;
	
	public AssertionType getAssertionType() {
		return assertionType;
	}
	
	public void setAssertionType(AssertionType assertionType) {
		this.assertionType = assertionType;
	}
	
	@JsonProperty("assertionType")
	public void setAssertionType(String assertionType){
		try{
			this.assertionType = AssertionType.valueOf(assertionType);
		}catch(Exception e){
			throw new UndefinedAssertionTypeException(String.valueOf(Math.random()),
					"Requested assertion type '" + assertionType + " is not a valid assertion type");
		}
	}

	public GrantType getGrantType() {
		return grantType;
	}

	public void setGrantType(GrantType grantType) {
		this.grantType = grantType;
	}
	
	@JsonProperty("grantType")
	public void setGrantType(String grantType){
		try{
			this.grantType = GrantType.valueOf(grantType);
		}catch(Exception e){
			throw new UndefinedGrantTypeException(String.valueOf(Math.random()),
					"Requested grant type '" + grantType + " is not a valid grant type");
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getScope() {
		return scope;
	}

	public void setScope(String[] scope) {
		this.scope = scope;
	}

	public Assertion getAssertion() {
		return assertion;
	}

	public void setAssertion(Assertion assertion) {
		this.assertion = assertion;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}
	
	public void setClientSecret(String clientSecret){
		this.clientSecret = clientSecret;
	}
}
