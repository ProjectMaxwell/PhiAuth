package com.projectmaxwell.phiauth.model.uwnetid;

public class UWNetIDTokenRequest {

	private String uwnetid;
	private long expiration;
	
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
