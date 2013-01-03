package com.projectmaxwell.phiauth.service.dao;

import com.projectmaxwell.phiauth.model.uwnetid.UWNetIDTokenRequest;
import com.projectmaxwell.phiauth.model.uwnetid.UWNetIDTokenResponse;

public interface UWNetIDTokenDAO {

	public UWNetIDTokenResponse createToken(UWNetIDTokenRequest tokenRequest);
}
