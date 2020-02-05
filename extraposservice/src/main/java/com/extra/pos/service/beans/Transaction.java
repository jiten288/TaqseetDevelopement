package com.extra.pos.service.beans;

import java.math.BigDecimal;

public class Transaction {
	
	private String mobileNumber;
	private BigDecimal amount;
	private String otp;
	private String retailerRefNo;
	private PosService requestData;

	public Transaction() {}


	public String getRetailerRefNo() {
		return retailerRefNo;
	}


	public void setRetailerRefNo(String retailerRefNo) {
		this.retailerRefNo = retailerRefNo;
	}


	public String getMobileNumber() {
		return mobileNumber;
	}


	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public String getOtp() {
		return otp;
	}


	public void setOtp(String otp) {
		this.otp = otp;
	}


	public PosService getRequestData() {
		return requestData;
	}


	public void setRequestData(PosService requestData) {
		this.requestData = requestData;
	}


	@Override
	public String toString() {
		return "{ Transaction: [{mobileNumber=" + mobileNumber + ", amount=" + amount + ", otp=" + otp + ", retailerRefNo="
				+ retailerRefNo + ", requestData=" + requestData.toString() + "}] }";
	}

	

	

	

	
	
}
