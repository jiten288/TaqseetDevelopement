package com.extra.pos.service.common;

import java.util.ArrayList;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.extra.pos.service.loggers.CustomResponseBodyAdvice;

@Configuration
@ComponentScan
public class WebConfig extends DelegatingWebMvcConfiguration {

	@Bean
    @Override
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = super.requestMappingHandlerAdapter();

        List<ResponseBodyAdvice<?>> responseBodyAdvices = new ArrayList<>();
        responseBodyAdvices.add(new CustomResponseBodyAdvice());
        requestMappingHandlerAdapter.setResponseBodyAdvice(responseBodyAdvices);

        return requestMappingHandlerAdapter;
    }

}
