package com.extra.pos.service.common;

import java.net.SocketTimeoutException;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.extra.pos.service.beans.ReverseTransaction;
import com.extra.pos.service.beans.HttpErrorResponse;
import com.extra.pos.service.beans.Transaction;


@Service
@PropertySources({ @PropertySource(value = { "classpath:webservice.properties" }) })
public class FiananceRestServiceUtil {

	@Value("${finance.service.host}")
	private String host;

	@Value("${finance.service.sendotp.endpoint}")
	private String sendOtpEndPoint;

	@Value("${finance.service.postTransaction.endpoint}")
	private String postTransEndPoint;

	@Value("${finance.service.reverseTransaction.endpoint}")
	private String reverseTransEndPoint;
	
	@Value("${finance.service.truststore.path}")
	private String trustStorePath;
	
	@Value("${finance.service.truststore.password}")
	private String trustStorePassword;
	
	@Value("${finance.service.getTransactionStatus.endpoint}")
	private String getTransactionStatusEndPoint;
	
	@Value("${finance.service.getBalance.endpoint}")
	private String getBalanceEndPoint;
	
	private Log logger = LogFactory.getLog(FiananceRestServiceUtil.class);

	private RestTemplate restTemplate;

	public String sendHttpRequest(HttpMethod method, String uri, String apiKey) {
		ResponseEntity<String> response = null;
		String res = null;
		System.setProperty("service.request",uri);
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		//TrustStore file

		System.setProperty("javax.net.ssl.trustStore",/*"C:\\dev\\Xstore\\01.DEV\\chl_pos\\res\\ssl\\.truststore"*/trustStorePath);

		System.setProperty("javax.net.ssl.trustStorePassword", /*"ret123ail"*/trustStorePassword);
		
		restTemplate = new RestTemplate();
		try {
			buildHttpComponents();
			logger.info("Endpoint url : " + uri);
			HttpEntity<String> request = new HttpEntity<>(getHttpHeader(apiKey));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri.toString());
			response = restTemplate.exchange(builder.build().encode().toUri(), method, request, String.class);
			res = response.getBody();
			logger.info("Response : " + res);
		} catch (HttpClientErrorException ce) {
			res = ce.getResponseBodyAsString();
			logger.info("Response: " + res + ", Status code: " + ce.getStatusCode().value());
		} catch(HttpServerErrorException ex){
			res = ex.getResponseBodyAsString();
			logger.info("Response: " + res + ", Status code: " + ex.getStatusCode().value());
		}
		catch (RestClientException e) {
			if (e.getRootCause() instanceof SocketTimeoutException) {
				logger.error("SocketTimeoutException", e);
			} else if (e.getRootCause() instanceof ConnectTimeoutException) {
				logger.error("ConnectTimeoutException", e);
			} else {
				logger.error("Some other exception", e);
			}
			res = e.getMessage();
		} catch (Exception e) {
			logger.error("Response: " + e.getMessage(), e);
			res = e.getMessage();
		}
		if(res != null)
		System.setProperty("service.response",res);
		return res;
	}

	public void buildHttpComponents() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(30000);
		factory.setConnectTimeout(30000);
		restTemplate.setRequestFactory(factory);
	}

	public HttpHeaders getHttpHeader(String apiKey) {
		HttpHeaders httpHeaders = new HttpHeaders();
		String authString = "apikey " + apiKey;
		httpHeaders.set("Authorization", authString);
		httpHeaders.set("Content-Type", "application/json; utf-8");
		httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return httpHeaders;
	}

	public Object callSendOtpToClientService(String mobileNo, String apiKey) {

		logger.info("Calling SentOtpToClient service.................................");
		if (mobileNo != null && !mobileNo.isEmpty() ) {
			if (apiKey != null) {
				StringBuilder URL = new StringBuilder();
				URL.append(host);
				URL.append(sendOtpEndPoint);
				URL.append("?civilid=" + mobileNo);
				String response = this.sendHttpRequest(HttpMethod.POST, URL.toString(), apiKey);
				return response;
			} else {
				return new HttpErrorResponse("FAILED", "API Key is invalid");
			}
		} else {
			return new HttpErrorResponse("FAILED","Mobile no is Invalid");
		}

	}

	public Object callPostTransactionService(Transaction transaction) {

		logger.info("Calling PostTransaction service.................................");
		if (transaction.getMobileNumber() != null) {
			if (transaction.getAmount() != null) {
				if (transaction.getOtp() != null) {
					if(transaction.getRequestData().getApiKey() != null){
						StringBuilder URL = new StringBuilder();
						URL.append(host);
						URL.append(postTransEndPoint);
						URL.append("?civilid=" + transaction.getMobileNumber());
						URL.append("&Amount="+transaction.getAmount());
						URL.append("&otp="+transaction.getOtp());
						URL.append("&retailer_ref="+transaction.getRetailerRefNo());
						String response = this.sendHttpRequest(HttpMethod.POST, URL.toString(), transaction.getRequestData().getApiKey());
						return response;
					}else{
					return new HttpErrorResponse("FAILED","OTP can not be empty");
					}
				} else {
					return new HttpErrorResponse("FAILED","API key is empty");
				}
			} else {
				return new HttpErrorResponse("FAILED","Amount can not be empty.");
			}
		} else {
			return new HttpErrorResponse("FAILED","Mobile no is invalid.");
		}
	}

	public Object callPostTransactionReversalService(ReverseTransaction transaction) {

		logger.info("Calling PostTransactionReversal service.................................");
		if (transaction.getMobileNumber() != null) {
			if (transaction.getReverseAmount() != null) {
				if (transaction.getOtp() != null) {
					if(transaction.getRequestData().getApiKey() != null){
						StringBuilder URL = new StringBuilder();
						URL.append(host);
						URL.append(reverseTransEndPoint);
						URL.append("?civilid=" + transaction.getMobileNumber());
						URL.append("&RefNumber="+transaction.getRefNumber());
						URL.append("&otp="+transaction.getOtp());
						URL.append("&Amount="+transaction.getReverseAmount());
						String response = this.sendHttpRequest(HttpMethod.POST, URL.toString(), transaction.getRequestData().getApiKey());
						return response;
					}else{
					return new HttpErrorResponse("FAILED","OTP can not be empty");
					}
				} else {
					return new HttpErrorResponse("FAILED","OTP can not be empty");
				}
			} else {
				return new HttpErrorResponse("FAILED","Amount can not be empty.");
			}
		} else {
			return new HttpErrorResponse("FAILED","Mobile no is invalid.");
		}
	}
	
	public Object getTransactionStatus(String retRefNumber, String apiKey){
		logger.info("Calling getTransactionStatus service.................................");
		if(retRefNumber!=null){
			if(apiKey!=null){
				StringBuilder URL = new StringBuilder();
				URL.append(host);
				URL.append(getTransactionStatusEndPoint);
				URL.append("?RetailerRef=" + retRefNumber);
				String response = this.sendHttpRequest(HttpMethod.GET, URL.toString(), apiKey);
				return response;
			}else{
				return new HttpErrorResponse("FAILED","No API Key provided from Client");
			}
		}else{
			return new HttpErrorResponse("FAILED","No retail reference number provided");
		}
	}
	
	public Object getBalances(String civilId, String apiKey){
		logger.info("Calling getBalance service.................................");
		if(civilId!=null){
			if(apiKey!=null){
				StringBuilder URL = new StringBuilder();
				URL.append(host);
				URL.append(getBalanceEndPoint);
				URL.append("?civilId=" + civilId);
				String response = this.sendHttpRequest(HttpMethod.GET, URL.toString(), apiKey);
				return response;
			}else{
				return new HttpErrorResponse("FAILED","No API Key provided from Client");
			}
		}else{
			return new HttpErrorResponse("FAILED","No Civil Id provided");
		}
	}

	

}
