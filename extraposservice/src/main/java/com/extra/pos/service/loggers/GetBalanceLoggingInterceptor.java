package com.extra.pos.service.loggers;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class GetBalanceLoggingInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	LoggingService loggingService;

	private Log logger = LogFactory.getLog(GetBalanceLoggingInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name())
				&& request.getMethod().equals(HttpMethod.GET.name())) {
			loggingService.logRequest(request, null);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.debug("Request URL::" + request.getRequestURL().toString() + " Sent to Handler :: Current Time="
				+ System.currentTimeMillis());
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		logger.info("Request URL::" + request.getRequestURL().toString() + ":: Session Id" + request.getSession().getId());

	}

}
