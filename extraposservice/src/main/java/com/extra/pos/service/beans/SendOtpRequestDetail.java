package com.extra.pos.service.beans;


public class SendOtpRequestDetail {
	
	
	private String mobileNo;
	private PosService requestData;
	
	@Override
	public String toString() {
		return "{SendOtpRequestDetail: [{mobileNo=" + mobileNo + ", requestData=" + requestData.toString() + "}] }";
	}

	public PosService getRequestData() {
		return requestData;
	}

	public void setRequestData(PosService requestData) {
		this.requestData = requestData;
	}

	public SendOtpRequestDetail() {
		super();
	}



	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	
	

}
