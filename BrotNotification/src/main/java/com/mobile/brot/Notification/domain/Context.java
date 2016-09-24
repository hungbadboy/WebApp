package com.mobile.brot.Notification.domain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import com.mobile.brot.Notification.context.SpringAppContext;


public class Context {
	
	private HttpServletRequest request;
	private HttpServletResponse response;

	private ApplicationContext  dispSpringAppContext = SpringAppContext.getAppcontext();
	
	public Context(HttpServletRequest req, HttpServletResponse res) {		
		this.request = req;
		this.response = res;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	

}
