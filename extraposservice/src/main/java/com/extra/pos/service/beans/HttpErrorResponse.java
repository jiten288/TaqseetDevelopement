package com.extra.pos.service.beans;

public class HttpErrorResponse {
	
	
	private String status;
	private String errorMessage;
	@Override
	public String toString() {
		return "{Error; [{status=" + status + ", errorMessage=" + errorMessage + "}] }";
	}
	public HttpErrorResponse(String status, String errorMessage) {
		super();
		this.status = status;
		this.errorMessage = errorMessage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	

}
