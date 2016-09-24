package com.siblinks.ws.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;

/**
 * Mock service for testing authentication requirements.
 * 
 * @author dhingey
 *
 */
@Controller
@RequestMapping("/siblinks/services/mock")
public class MockPostAuthService {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private HttpServletRequest context;

	@RequestMapping("/doMockStuff")
	public @ResponseBody ResponseEntity<Response> doMockStuff(
			@RequestParam(value = "message") String message) {

		// Make sure that the user is authenticated, returning "Forbidden"
		// status if they are not.
		if (!AuthenticationFilter.isAuthed(context)) {
			ResponseEntity<Response> entity = new ResponseEntity<Response>(
					new SimpleResponse("" + false, "Authentication required."),
					HttpStatus.FORBIDDEN);
			return entity;
		}

		// Get the username from the session
		String userName = context.getSession(false).getAttribute("username")
				.toString();

		String responseStr = "This is a message returned by the service. The session for this user is valid (session ID "
				+ context.getSession(false)
				+ "). The message the user sent was \""
				+ message
				+ ", and the username for this user is : " + userName;

		SimpleResponse reponse = new SimpleResponse("" + true, responseStr);
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				HttpStatus.OK);

		return entity;
	}

}
