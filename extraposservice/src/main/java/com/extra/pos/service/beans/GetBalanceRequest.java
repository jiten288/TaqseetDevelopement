package com.extra.pos.service.beans;


public class GetBalanceRequest {
	
	
	private String civilId;
	private PosService requestData;
	private boolean isBalanceCheck;
	
	public boolean isBalanceCheck() {
		return isBalanceCheck;
	}

	public void setBalanceCheck(boolean isBalanceCheck) {
		this.isBalanceCheck = isBalanceCheck;
	}

	@Override
	public String toString() {
		return "{SendOtpRequestDetail: [{mobileNo=" + civilId + ", requestData=" + requestData.toString() + "}] }";
	}

	public PosService getRequestData() {
		return requestData;
	}

	public void setRequestData(PosService requestData) {
		this.requestData = requestData;
	}

	public GetBalanceRequest() {
		super();
	}



	public String getcivilId() {
		return civilId;
	}

	public void setcivilId(String civilId) {
		this.civilId = civilId;
	}


	
	

}
