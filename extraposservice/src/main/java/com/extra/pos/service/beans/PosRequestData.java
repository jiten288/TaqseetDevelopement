package com.extra.pos.service.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class PosRequestData {
	
	private PosService service;

	public PosService getService() {
		return service;
	}

	public void setService(PosService service) {
		this.service = service;
	}
	
}
