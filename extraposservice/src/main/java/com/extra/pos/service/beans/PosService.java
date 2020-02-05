package com.extra.pos.service.beans;

import com.google.gson.Gson;

public class PosService {
	
	private int sequenceId;
	private String storeId;
	private String registerId;
	private int transactionId;
	private String businesDate;
	private String apiKey;
	
	
	public PosService() {
		super();
	}
	
	public int getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}
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

	

	@Override
	public String toString() {
		return "{ Request: [{sequenceId=" + sequenceId + ", storeId=" + storeId + ", registerId=" + registerId
				+ ", transactionId=" + transactionId + ", businesDate=" + businesDate + ", apiKey=" + apiKey + "}] }";
	}

	public PosService(int sequenceId, String storeId, String registerId, int transactionId, String businesDate, String apiKey) {
		super();
		this.sequenceId = sequenceId;
		this.storeId = storeId;
		this.registerId = registerId;
		this.transactionId = transactionId;
		this.businesDate = businesDate;
		this.apiKey = apiKey;
	}

	public PosService(String requestData) {
		Gson gson = new Gson();
		PosService request = gson.fromJson(requestData, this.getClass());
		this.setApiKey(request.getApiKey());
		this.setBusinesDate(request.getBusinesDate());
		this.setRegisterId(request.getRegisterId());
		this.setStoreId(request.getStoreId());
		this.setTransactionId(request.getTransactionId());
	}
	
}
