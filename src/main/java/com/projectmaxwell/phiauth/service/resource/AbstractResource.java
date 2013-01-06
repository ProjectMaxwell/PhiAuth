package com.projectmaxwell.phiauth.service.resource;

import java.util.HashSet;

import com.projectmaxwell.exception.InsufficientScopesException;
import com.projectmaxwell.phiauth.model.TokenResponse;

public abstract class AbstractResource {

	public boolean hasScope(TokenResponse response, String[] allowedScopes) throws InsufficientScopesException{
		if(allowedScopes == null || allowedScopes.length < 1){
			System.out.println("No allowable scopes defined for this call, not filtering.");
			return true;
		}
		HashSet<String> allowedScopesSet = new HashSet<String>();
		for(String scope : allowedScopes){
			allowedScopesSet.add(scope);
		}
		String[] grantedScopes = response.getScopes();
		if(grantedScopes == null){
			throw new InsufficientScopesException(String.valueOf(Math.random() * 100), 
					"Scopes list was null for given token.");			
		}
		for(String scope : grantedScopes){
			if(allowedScopesSet.contains(scope)){
				return true;
			}
		}
		throw new InsufficientScopesException(String.valueOf(Math.random() * 100), 
				"Token was valid but did not contain the appropriate scopes to perform this action on the requested resource.");
	}
}
