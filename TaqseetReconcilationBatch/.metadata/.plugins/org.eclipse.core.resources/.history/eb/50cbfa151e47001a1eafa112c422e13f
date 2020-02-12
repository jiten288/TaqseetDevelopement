package com.reconcile;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

@SuppressWarnings("deprecation")
public class PosServiceUtil {

	static Logger logger = Logger.getLogger(PosServiceUtil.class);

	public org.json.simple.JSONObject callPosService(String serviceURL, String requestBody) {
		try {
			HttpURLConnection conn;

			conn = APIConfig(serviceURL);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; utf-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			if (requestBody != null) {
				try (OutputStream os = conn.getOutputStream()) {
					byte[] input = requestBody.getBytes("utf-8");
					os.write(input, 0, input.length);
				}
			}

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Client - Request Failed -  HTTP error code : " + conn.getResponseCode());
			} else {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {

					org.json.simple.JSONObject response = (org.json.simple.JSONObject) new JSONParser().parse(br);
					logger.info(response.toString());
					return response;
				} catch (Exception e) {

				}
			}
			conn.disconnect();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static HttpURLConnection APIConfig(String serviceUrl) throws IOException {

		URL url = new URL(serviceUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestProperty("Authorization", "NoAuth");
		conn.setRequestProperty("Content-Type", "application/json; charset=utf8");
		conn.setRequestProperty("Accept", "application/json");
		return conn;
	}

	public Boolean getTransactionStatus(String retailRefNo) {
		try {
			logger.info("Calling getTransactionStatus service : " + retailRefNo);
			StringBuilder url = new StringBuilder();
			url.append(this.getProperty("service.host"));
			url.append(this.getProperty("transactionStatus.endpoint"));
			url.append("/" + retailRefNo);
			url.append("/" + this.getProperty("apiKey"));

			org.json.simple.JSONObject response = this.callPosService(url.toString(), null);
			if (response != null) {
				org.json.simple.JSONArray data = (org.json.simple.JSONArray) response.get("Data");

				if (data != null && data.size() > 0) {
					String storeRefNo = ((String) ((org.json.simple.JSONObject) data.get(0)).get("STORE_REF_NUM"));
					if (storeRefNo != null && storeRefNo.equalsIgnoreCase(retailRefNo)) {
						return true;
					}
				} else {
					return false;
				}
			}
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

	}

	public void callPostTransactionReversal(CancelledTransactionDAO cancelTrans, String method) {

		try {
			logger.info("Calling PostTransactionReversal service...............");
			String serviceURL = this.getProperty("service.host") + this.getProperty("posttransactionreversal.endpoint");
			JSONObject json = new JSONObject();
			json.put("requestData", getRequestData(cancelTrans));
			json.put("mobileNumber", cancelTrans.getCivilId());
			json.put("reverseAmount", cancelTrans.getReverseAmt());
			json.put("otp", cancelTrans.getOtp());
			json.put("refNumber", cancelTrans.getRetailRefNo());
			org.json.simple.JSONObject response = this.callPosService(serviceURL, json.toString());
			if (response != null) {
				org.json.simple.JSONArray data = (org.json.simple.JSONArray) response.get("Data");

				if (data != null && data.size() > 0) {
					String status = ((String) ((org.json.simple.JSONObject) data.get(0)).get("Status"));
					if (status != null && status.equalsIgnoreCase("0")
							&& (method.equalsIgnoreCase(ReconcileBatchConstants.TRANS)
									|| method.equalsIgnoreCase(ReconcileBatchConstants.DATE))) {
						DBManager dbManager = DBManager.getInstance();
						dbManager.updateStatusOfReversTransaction(cancelTrans.getRetailRefNo());
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public String getProperty(String key) {

		Properties prop = new Properties();
		try {
			prop.load(PosServiceUtil.class.getClass().getResourceAsStream("/application.properties"));
		} catch (IOException e) {

			e.printStackTrace();
		}

		return prop.getProperty(key);
	}

	public PosRequestDAO getRequestData(CancelledTransactionDAO cancelTrans) {
		PosRequestDAO requestDAO = new PosRequestDAO();
		requestDAO.setApiKey(this.getProperty("apiKey"));
		requestDAO.setBusinesDate(cancelTrans.getBusinesDate());
		requestDAO.setRegisterId(cancelTrans.getRegisterId());
		requestDAO.setStoreId(cancelTrans.getStoreId());
		requestDAO.setTransactionId(cancelTrans.getTransactionId());

		return requestDAO;
	}

}
