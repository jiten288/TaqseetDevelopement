package com.extra.pos.service.loggers;

import java.util.Collection;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.extra.pos.service.beans.PosService;
import com.extra.pos.service.beans.ReverseTransaction;
import com.extra.pos.service.beans.SendOtpRequestDetail;
import com.extra.pos.service.beans.Transaction;
import com.extra.pos.service.common.DatabaseOperationHelper;

@Component
public class LoggingServiceImpl implements LoggingService {

	private static final Logger log = Logger.getLogger(LoggingServiceImpl.class);

	@Autowired
	private DatabaseOperationHelper dbOpHelper;
	
	@Override
	public void logRequest(HttpServletRequest httpServletRequest, Object body) {

		StringBuilder stringBuilder = new StringBuilder();
		

		Map<String, String> parameters = buildParametersMap(httpServletRequest);

		stringBuilder.append("REQUEST ");
		stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
		stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
		stringBuilder.append("headers=[").append(buildHeadersMap(httpServletRequest)).append("] ");
		if (!parameters.isEmpty()) {

			stringBuilder.append("parameters=[").append(parameters).append("] ");
		}
		if (body != null) {
			if (body instanceof SendOtpRequestDetail) {
				((SendOtpRequestDetail)body).getRequestData().setSequenceId(dbOpHelper.getSequenceId());
			} if (body instanceof Transaction) {
				((Transaction)body).getRequestData().setSequenceId(dbOpHelper.getSequenceId());
			}if (body instanceof ReverseTransaction) {
				((ReverseTransaction)body).getRequestData().setSequenceId(dbOpHelper.getSequenceId());
			}if(body instanceof PosService){
				((PosService)body).setSequenceId(dbOpHelper.getSequenceId());
			}
			stringBuilder.append("body=[" + body + "]");
			JSONObject resObj = new JSONObject(body);
			log.info(resObj);
			log.info(stringBuilder.toString());
		}
		
		dbOpHelper.createHttpRequestlog(httpServletRequest.getSession().getId(), httpServletRequest.getRequestURI(), (stringBuilder.toString()), body );

	}

	@Override

	public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object body) {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("RESPONSE ");
		stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
		stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
		//stringBuilder.append("responseHeaders=[").append(buildHeadersMap(httpServletResponse)).append("] ");
		stringBuilder.append("responseBody=[").append(body).append("] ");

		log.info(stringBuilder.toString());
		dbOpHelper.updateHttpRespone(httpServletRequest.getSession().getId(), stringBuilder.toString());

	}

	private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {

		Map<String, String> resultMap = new HashMap<>();
		Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String key = parameterNames.nextElement();
			String value = httpServletRequest.getParameter(key);
			resultMap.put(key, value);
		}

		return resultMap;

	}

	private Map<String, String> buildHeadersMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;

	}

	private Map<String, String> buildHeadersMap(HttpServletResponse response) {
		Map<String, String> map = new HashMap<>();
		Collection<String> headerNames = response.getHeaderNames();
		for (String header : headerNames) {
			map.put(header, response.getHeader(header));
		}
		return map;

	}
}
