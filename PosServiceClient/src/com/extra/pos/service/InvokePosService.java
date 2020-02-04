package com.extra.pos.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.parser.JSONParser;

public class InvokePosService {

	public static void main(String[] args) {

		PosServiceUtil util = new PosServiceUtil();
	//	util.callSendOtpToClient();
		//util.callPostTransaction();
		util.callPostTransactionReversal();
		/*if (args[0].equals("sendotptoclient")) {
			util.callSendOtpToClient();
		} else if (args[0].equals("postTransaction")) {
			util.callPostTransaction();
		} else if (args[0].equals("postTransactionReversal")) {
			util.callPostTransactionReversal();
		}*/
		
	//	callPosService("https://betasite-7q3y9z2g5m.tasheelfinance.com/rest/pos/GetBalance?civilId=1033660802");
	}
	
	public static void callPosService(String serviceURL) {
		try {
			HttpURLConnection conn;

			conn = APIConfig(serviceURL);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json; utf-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
/*
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = requestBody.getBytes("utf-8");
				os.write(input, 0, input.length);
			}*/

			System.out.println("Calling service...............");
			/*if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Client - Request Failed -  HTTP error code : " + conn.getResponseCode());
			} else {*/
				try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
					// StringBuilder response = new StringBuilder();
					// String responseLine = null;
					/*
					 * while ((responseLine = br.readLine()) != null) {
					 * response.append(responseLine.trim()); }
					 */
					org.json.simple.JSONObject response = (org.json.simple.JSONObject) new JSONParser().parse(br);

					System.out.println(response.toString());
				} catch (Exception e) {

				}
			//}

			// BufferedReader br = new BufferedReader(new
			// InputStreamReader((conn.getInputStream())));
			// String output = br.readLine();

			// System.out.println("Response: " + output);
			// outputStream.close();
			conn.disconnect();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	public static HttpURLConnection APIConfig(String serviceUrl) throws IOException {
		trustAllCertificates();
		// String authString = "";
		// byte[] authEncBytes =
		// java.util.Base64.getEncoder().encode(authString.getBytes());
		// String authStringEnc = new String(authEncBytes);
		URL url = new URL(serviceUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestProperty("Authorization", "apikey "+"d4E9YfNq5CGJNbxXeYNhgpIvVuSiQrh0MJSiSIXz7wwO2djLs83MMXP5tf8d7OrTj47ZLJh5iBm1u5ofvhB4AO3o7jC4Ysi4M9BM");
		conn.setRequestProperty("Content-Type", "application/json; charset=utf8");
		conn.setRequestProperty("Accept", "application/json");
		return conn;
	}
	
	private static void trustAllCertificates() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					X509Certificate[] myTrustedAnchors = new X509Certificate[0];
					return myTrustedAnchors;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
