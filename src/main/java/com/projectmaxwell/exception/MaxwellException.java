package com.projectmaxwell.exception;

public class MaxwellException {

	protected String errorId;
	protected String errorMessage;
	protected String errorCode;
	
	public MaxwellException(String errorId, String errorCode, String errorMessage){
		this.errorId = errorId;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public String getErrorId() {
		return errorId;
	}
	
	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
