package com.extra.pos.service.beans;

import java.math.BigDecimal;

public class ReverseTransaction {

	private String mobileNumber;
	private BigDecimal reverseAmount;
	private String otp;
	private String refNumber;
	private PosService requestData;

	public ReverseTransaction() {

	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public BigDecimal getReverseAmount() {
		return reverseAmount;
	}

	public void setReverseAmount(BigDecimal reverseAmount) {
		this.reverseAmount = reverseAmount;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public PosService getRequestData() {
		return requestData;
	}

	public void setRequestData(PosService requestData) {
		this.requestData = requestData;
	}

	@Override
	public String toString() {
		return "{ ReverseTransaction: [{ mobileNumber=" + mobileNumber + ", reverseAmount=" + reverseAmount + ", otp=" + otp
				+ ", refNumber=" + refNumber + ", requestData=" + requestData.toString() + "}] }";
	}

	
	

}
