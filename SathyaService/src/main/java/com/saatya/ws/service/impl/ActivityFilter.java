package com.saatya.ws.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.saatya.ws.dao.DaoFactory;
import com.saatya.ws.dao.UserDao;

@Component
@WebFilter(urlPatterns = {"/**"}, filterName = "ActivityFilter", description = "Activity monitoring filter")
public class ActivityFilter extends OncePerRequestFilter {
	
	private final Log logger = LogFactory.getLog(ActivityFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest req,
			HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {

		// Log user activity to the DB
		HttpSession session = req.getSession(false);
		logger.info("Hello from filter! Request session is: " + session);
		if(session != null){
			String email = session.getAttribute("email").toString();
			logger.info("Session email is: " + email);
			DaoFactory factory = DaoFactory.getDaoFactory();
			UserDao dao = factory.getUserDao();
			Map<String, String> queryParams = new HashMap<String, String>() ;
			queryParams.put("emailid", email);
			String entityName = "USER_ACTIVITY_UPDATE";
			Object msgs = dao.updateUser(entityName, queryParams);
			if(msgs == null){
				logger.error("Could not update activity time for user: " + email);
			}
		}
		

		// Call the next filter in the chain
		chain.doFilter(req, res);
	}
	
}
