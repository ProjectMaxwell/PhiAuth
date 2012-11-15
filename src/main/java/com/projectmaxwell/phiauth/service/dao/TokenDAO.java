package com.projectmaxwell.phiauth.service.dao;

import com.projectmaxwell.phiauth.model.TokenRequest;
import com.projectmaxwell.phiauth.model.TokenResponse;

public interface TokenDAO {

	public TokenResponse getTokenForPasswordGrant(TokenRequest tokenRequest);

	public TokenResponse validateToken(String accessToken);

	public TokenResponse getTokenForRefreshGrant(TokenRequest tokenRequest);
	
	public TokenResponse getToken(TokenRequest tokenRequest);

	public TokenResponse getTokenForAssertionGrant(TokenRequest tokenRequest);
}
