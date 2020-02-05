package com.extra.pos.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.extra.pos.service.beans.GetBalanceRequest;
import com.extra.pos.service.beans.PosRequestData;
import com.extra.pos.service.beans.PosService;
import com.extra.pos.service.beans.ReverseTransaction;
import com.extra.pos.service.beans.SendOtpRequestDetail;
import com.extra.pos.service.beans.Transaction;
import com.extra.pos.service.common.FiananceRestServiceUtil;

@RestController
public class POSServiceHandleController {

	@Autowired
	private FiananceRestServiceUtil serviceUtil;

	@Autowired
	private PosRequestData requestData;

	// Code for get balance service
	@RequestMapping(value = "/v1/getBalance", method = RequestMethod.POST, headers = "Accept=application/json")

	@ResponseBody
	public ResponseEntity<Object> getBalance(
			@RequestBody GetBalanceRequest getBalanceRequest) {

		getBalanceRequest.getRequestData().setSequenceId(Integer.parseInt(System.getProperty("sequence.id")));
		requestData.setService(getBalanceRequest.getRequestData());
		Object response = serviceUtil.getBalances(getBalanceRequest.getcivilId(), getBalanceRequest.getRequestData().getApiKey());
		System.clearProperty("sequence.id");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/v1/sendOtpToClient", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<Object> sendOtpToClient(@RequestBody SendOtpRequestDetail requestDetail) {

		requestDetail.getRequestData().setSequenceId(Integer.parseInt(System.getProperty("sequence.id")));
		requestData.setService(requestDetail.getRequestData());
		Object response = serviceUtil.callSendOtpToClientService(requestDetail.getMobileNo(),
				requestDetail.getRequestData().getApiKey());
		System.clearProperty("sequence.id");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/v1/postTransaction", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<Object> postTransaction(@RequestBody Transaction transaction) {

		transaction.getRequestData().setSequenceId(Integer.parseInt(System.getProperty("sequence.id")));
		requestData.setService(transaction.getRequestData());
		Object response = serviceUtil.callPostTransactionService(transaction);
		System.clearProperty("sequence.id");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/v1/postTransactionReversal", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<Object> postTransactionReversal(@RequestBody ReverseTransaction reverseTransaction) {

		reverseTransaction.getRequestData().setSequenceId(Integer.parseInt(System.getProperty("sequence.id")));
		requestData.setService(reverseTransaction.getRequestData());
		Object response = serviceUtil.callPostTransactionReversalService(reverseTransaction);
		System.clearProperty("sequence.id");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/v1/getTransactionStatus/{retRefNumber}/{apiKey}", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<Object> getTransactionStatus(@PathVariable(value = "retRefNumber") String retRefNumber,
			@PathVariable(value = "apiKey") String apiKey) {
		Object response = serviceUtil.getTransactionStatus(retRefNumber, apiKey);
		System.clearProperty("sequence.id");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

}
