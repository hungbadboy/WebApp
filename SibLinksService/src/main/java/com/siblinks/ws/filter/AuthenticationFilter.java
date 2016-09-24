package com.siblinks.ws.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter implements Filter {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private HttpServletRequest context;
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		HttpSession session = context.getSession(true);
	    if(session != null){
	    	logger.info("username in sessison : " + session.getAttribute("username"));
	    }else{
	    	logger.info("Session null");
	    }
		
		chain.doFilter(req, res);
	}
	
	public static boolean isAuthed(HttpServletRequest context){
		HttpSession session = context.getSession(false);
	    if(session != null){
	    	if(session.getAttribute("username") != null){
	    		return true;
	    	}
	    }
		return true;
	}

	public void init(FilterConfig filterConfig) {}

	public void destroy() {}

}
