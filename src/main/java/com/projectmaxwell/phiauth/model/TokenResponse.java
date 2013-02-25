package com.projectmaxwell.phiauth.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class TokenResponse {
	
	private String accessToken;
	private String refreshToken;
	private int ttl;
	private String[] scopes;
	private GrantType grantType;
	private Integer userId;
	private String clientId;
	private AssertionType assertionType;
	private Integer userTypeId;
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public String[] getScopes() {
		return scopes;
	}

	public void setScopes(String[] scopes) {
		this.scopes = scopes;
	}

	public GrantType getGrantType() {
		return grantType;
	}

	public void setGrantType(GrantType grantType) {
		this.grantType = grantType;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public AssertionType getAssertionType() {
		return assertionType;
	}

	public void setAssertionType(AssertionType assertionType2) {
		this.assertionType = assertionType2;
	}

	public Integer getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(int userType) {
		this.userTypeId = userType;
	}
}
