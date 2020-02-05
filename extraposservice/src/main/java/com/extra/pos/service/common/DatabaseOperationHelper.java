package com.extra.pos.service.common;

import java.io.ByteArrayInputStream;
import java.sql.Types;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.stereotype.Repository;

import com.extra.pos.service.beans.GetBalanceRequest;
import com.extra.pos.service.beans.PosRequestData;
import com.extra.pos.service.beans.PosService;
import com.extra.pos.service.beans.ReverseTransaction;
import com.extra.pos.service.beans.SendOtpRequestDetail;
import com.extra.pos.service.beans.Transaction;

@Repository
public class DatabaseOperationHelper {

	@Autowired
	@Qualifier("namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private PosRequestData requestData;

	private String insertSqlPosServiceRequest = "Insert into EXTRA_POS_SERVICE_HANDLER (SESSION_ID,SERVICE_NAME,INBOUND_POS_REQ,OUTBOUND_POS_RESPONSE,"
			+ "OUTBOUND_ERP_REQ,INBOUND_ERP_RESPONSE,CREATE_DATE,UPDATE_DATE,SEQUENCE_ID,STORE_ID,REGISTER_ID,TRANSACTION_ID,BUSINESS_DATE) "
			+ "values (:sessionID,:serviceName,:InboundPosReq ,:OutboundPosResp,:OutboundErpReq,:inboundErpResp,:createDate,:updateDate,:sequenceId,:storeId,:registerId"
			+ ",:transactionId,:businessDate)";

	private String updateSqlPosServiceResponse = "Update EXTRA_POS_SERVICE_HANDLER set OUTBOUND_POS_RESPONSE = :OutboundPosResp, OUTBOUND_ERP_REQ=:OutboundErpReq, INBOUND_ERP_RESPONSE=:inboundErpResp, update_date=:updateDate where SEQUENCE_ID=:sequenceId";

	private String updateSqlSoapRequest = "Update EXTRA_POS_SERVICE_HANDLER set OUTBOUND_ERP_REQ=:OutboundErpReq, update_date=:updateDate where SEQUENCE_ID=:sequenceId";

	private String updateSqlSoapResponse = "Update EXTRA_POS_SERVICE_HANDLER set INBOUND_ERP_RESPONSE=:inboundErpResp, update_date=:updateDate where SEQUENCE_ID=:sequenceId";

	private String selectNextServiceId = "select Max(sequence_id)+1 from EXTRA_POS_SERVICE_HANDLER";

	public void createHttpRequestlog(String sessionId, String serviceName, String HttpRequest, Object requestBody) {

		System.setProperty("sequence.id", new Integer(getSequenceId()).toString());
		Date date = new Date();
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("sequenceId", getSequenceId());
		param.addValue("sessionID", sessionId);
		param.addValue("InboundPosReq", new SqlLobValue(new ByteArrayInputStream(HttpRequest.getBytes()),
				HttpRequest.getBytes().length, new DefaultLobHandler()), Types.BLOB);
		param.addValue("OutboundPosResp", null);
		param.addValue("OutboundErpReq", null);
		param.addValue("inboundErpResp", null);
		param.addValue("createDate", date);
		param.addValue("updateDate", date);

		if (requestBody instanceof SendOtpRequestDetail) {
			SendOtpRequestDetail detail = (SendOtpRequestDetail) requestBody;
			param.addValue("serviceName", "SendOTPToClient");
			param.addValue("storeId", detail.getRequestData().getStoreId());
			param.addValue("registerId", detail.getRequestData().getRegisterId());
			param.addValue("transactionId", detail.getRequestData().getTransactionId());
			param.addValue("businessDate", detail.getRequestData().getBusinesDate());
		} else if (requestBody instanceof Transaction) {
			param.addValue("serviceName", "PostTransaction");
			Transaction detail = (Transaction) requestBody;
			param.addValue("storeId", detail.getRequestData().getStoreId());
			param.addValue("registerId", detail.getRequestData().getRegisterId());
			param.addValue("transactionId", detail.getRequestData().getTransactionId());
			param.addValue("businessDate", detail.getRequestData().getBusinesDate());

		} else if (requestBody instanceof ReverseTransaction) {
			ReverseTransaction detail = (ReverseTransaction) requestBody;
			param.addValue("serviceName", "PostTransactionReversal");
			param.addValue("storeId", detail.getRequestData().getStoreId());
			param.addValue("registerId", detail.getRequestData().getRegisterId());
			param.addValue("transactionId", detail.getRequestData().getTransactionId());
			param.addValue("businessDate", detail.getRequestData().getBusinesDate());
		} else if(requestBody instanceof GetBalanceRequest){
			PosService posService = ((GetBalanceRequest)requestBody).getRequestData();
			param.addValue("serviceName", "GetBalance");
			param.addValue("storeId", posService.getStoreId());
			param.addValue("registerId", posService.getRegisterId());
			param.addValue("transactionId", posService.getTransactionId());
			param.addValue("businessDate", posService.getBusinesDate());
		}

		namedParameterJdbcTemplate.update(insertSqlPosServiceRequest, param);

	}

	public void updateHttpRespone(String sessionId, String HttpResponse) {

		PosService service = requestData.getService();
		if(service==null){
			return;
		}
		Date date = new Date();
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("sequenceId", service.getSequenceId());
		if (System.getProperty("service.request") != null) {
			param.addValue("OutboundErpReq",
					new SqlLobValue(new ByteArrayInputStream(System.getProperty("service.request").getBytes()),
							System.getProperty("service.request").getBytes().length, new DefaultLobHandler()),
					Types.BLOB);
		} else {
			param.addValue("OutboundErpReq", null);
		}
		if (System.getProperty("service.response") != null) {
			param.addValue("inboundErpResp",
					new SqlLobValue(new ByteArrayInputStream(System.getProperty("service.response").getBytes()),
							System.getProperty("service.response").getBytes().length, new DefaultLobHandler()),
					Types.BLOB);
		} else {
			param.addValue("inboundErpResp", null);
		}
		param.addValue("OutboundPosResp", new SqlLobValue(new ByteArrayInputStream(HttpResponse.getBytes()),
				HttpResponse.getBytes().length, new DefaultLobHandler()), Types.BLOB);
		param.addValue("updateDate", date);
		namedParameterJdbcTemplate.update(updateSqlPosServiceResponse, param);
		System.clearProperty("service.request");
		System.clearProperty("service.response");
	}

	public void updateSoapRequest(String soapRequest) {

		Date date = new Date();
		PosService service = requestData.getService();
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("sequenceId", service.getSequenceId());
		param.addValue("OutboundErpReq", new SqlLobValue(new ByteArrayInputStream(soapRequest.getBytes()),
				soapRequest.getBytes().length, new DefaultLobHandler()), Types.BLOB);
		param.addValue("updateDate", date);
		namedParameterJdbcTemplate.update(updateSqlSoapRequest, param);

	}

	public void updateSoapResponse(String soapResponse) {

		Date date = new Date();
		PosService service = requestData.getService();
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("sequenceId", service.getSequenceId());
		param.addValue("inboundErpResp", new SqlLobValue(new ByteArrayInputStream(soapResponse.getBytes()),
				soapResponse.getBytes().length, new DefaultLobHandler()), Types.BLOB);
		param.addValue("updateDate", date);
		namedParameterJdbcTemplate.update(updateSqlSoapResponse, param);

	}

	public int getSequenceId() {

		Integer sequenceId = namedParameterJdbcTemplate.queryForObject(selectNextServiceId,
				new BeanPropertySqlParameterSource(new Object()), Integer.class);

		if (sequenceId == null) {
			sequenceId = 1;
		}
		return sequenceId;

	}

}
