package com.projectmaxwell.phiauth.model.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.projectmaxwell.phiauth.model.AssertionType;
import com.projectmaxwell.phiauth.model.DanteAssertion;
import com.projectmaxwell.phiauth.model.GrantType;
import com.projectmaxwell.phiauth.model.TokenRequest;

public class TestRequestValidator {
	
	@Test
	@Ignore
	public void validateAssertionRequestTest(){
		RequestValidator requestValidator = new RequestValidator();
		
		TokenRequest tokenRequest = new TokenRequest();
		
		DanteAssertion assertion = new DanteAssertion();
//		assertion.setAssertionValue("");
		assertion.setUwnetid("olsone2");
		tokenRequest.setAssertion(assertion);
		
		tokenRequest.setClientId("fakeClient");
		
		tokenRequest.setClientSecret("fakeSecret");
		
		tokenRequest.setGrantType(GrantType.ASSERTION);
		
		tokenRequest.setAssertionType(AssertionType.UWNETID);
		
		boolean isValid = requestValidator.validateAssertionRequest(tokenRequest);
		assertEquals(true,isValid);

	}
}
