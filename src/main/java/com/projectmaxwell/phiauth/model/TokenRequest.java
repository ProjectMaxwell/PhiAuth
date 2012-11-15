package com.projectmaxwell.phiauth.model;

import com.projectmaxwell.phiauth.model.validation.Assertion;


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

	public GrantType getGrantType() {
		return grantType;
	}

	public void setGrantType(GrantType grantType) {
		this.grantType = grantType;
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
