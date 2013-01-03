package com.projectmaxwell.phiauth.model.uwnetid;

public class UWNetIDTokenResponse {

	private String token;
	private String uwnetid;
	private long expiration;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getUwnetid() {
		return uwnetid;
	}
	
	public void setUwnetid(String uwnetid) {
		this.uwnetid = uwnetid;
	}
	
	public long getExpiration() {
		return expiration;
	}
	
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}
	
	
}
