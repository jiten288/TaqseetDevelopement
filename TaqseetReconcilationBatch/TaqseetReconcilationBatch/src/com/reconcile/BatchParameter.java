package com.reconcile;

public enum BatchParameter {
	
	GENERATE_REPORT(1,"Generate Cancel Transaction Report"), TRANSACTION_REVERSAL(2,"Transaction Reversal");
	
	private final Integer code;
	private final String description;
	
	
	
	private BatchParameter(Integer code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public Integer getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
	
	

}
