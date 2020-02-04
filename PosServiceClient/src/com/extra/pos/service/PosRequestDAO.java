package com.extra.pos.service;

public class PosRequestDAO {

	private String storeId;
	private String registerId;
	private int transactionId;
	private String businesDate;
	private String apiKey;
	
	
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getRegisterId() {
		return registerId;
	}
	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public String getBusinesDate() {
		return businesDate;
	}
	public void setBusinesDate(String businesDate) {
		this.businesDate = businesDate;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public PosRequestDAO(String storeId, String registerId, int transactionId, String businesDate, String apiKey) {
		super();
		this.storeId = storeId;
		this.registerId = registerId;
		this.transactionId = transactionId;
		this.businesDate = businesDate;
		this.apiKey = apiKey;
	}
	public PosRequestDAO() {
	}
/*	 Please do not change the format of the toString method, this may lead to error as a bad request. 
	It follows a particular format that is designed below.*/
	@Override
	public String toString() {
		StringBuilder jsonString = new StringBuilder();

		jsonString.append("{\"storeId\"=\""+storeId+"\"");
		jsonString.append(",\"registerId\"=\""+registerId+"\"");
		jsonString.append(",\"transactionId\"=\""+transactionId+"\"");
		jsonString.append(",\"businesDate\"=\""+businesDate+"\"");
		jsonString.append(",\"apiKey\"=\""+apiKey+"\"}");
		return jsonString.toString();
	}
	
}
