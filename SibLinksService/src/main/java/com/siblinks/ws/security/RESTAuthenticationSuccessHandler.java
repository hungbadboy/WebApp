/*
 * Copyright (c) 2016-2017, Tinhvan Outsourcing JSC. All rights reserved.
 *
 * No permission to use, copy, modify and distribute this software
 * and its documentation for any purpose is granted.
 * This software is provided under applicable license agreement only.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.siblinks.ws.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.model.SibUser;
import com.siblinks.ws.model.SibUserDetails;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.SibConstants;

/**
 * This class is handling Authentication for use login success.
 * 
 * @author hungpd
 * @version 1.0
 */
@Component
public class RESTAuthenticationSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RESTAuthenticationSuccessHandler.class);

	private final ObjectMapper mapper;

	@Autowired
	private ObjectDao dao;

	@Autowired
	RESTAuthenticationFailureHandler failure;

	@Autowired
	RESTAuthenticationSuccessHandler(
			final MappingJackson2HttpMessageConverter messageConverter) {
		this.mapper = messageConverter.getObjectMapper();
	}

	/**
	 * Handling return user information after user login successful
	 */
	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request,
			final HttpServletResponse response,
			final Authentication authentication) throws ServletException,
			IOException {

		PrintWriter writer = null;
		try {

			SibUserDetails userDetails = (SibUserDetails) authentication
					.getPrincipal();
			SibUser user = userDetails.getUser();
			String userType = request.getParameter(Parameters.USER_TYPE);
			if (userType != null && !userType.equals("")
					&& !userType.equals(user.getUserType())) {
				SecurityContextHolder.clearContext();
				failure.onAuthenticationFailure(request, response,
						new UsernameNotFoundException(user.getUsername()));
			} else {
				writer = response.getWriter();
				String token = request.getParameter(Parameters.TOKEN);
				user.setStatus(SibConstants.SUCCESS);
				user.setPassword(null);
				userDetails.setUser(user);
				// Update last online and isonline
				dao.insertUpdateObject(
						SibConstants.SqlMapper.SQL_UPDATELASTONLINETIME,
						new Object[] { token, user.getUsername() });
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentType(request.getContentType());

				LOGGER.info(userDetails.getUsername() + " got is connected ");
				mapper.writeValue(writer, user);
				writer.flush();
			}
		} catch (DAOException e) {
			e.printStackTrace();
			writer = response.getWriter();
			Map<String, String> mapError = new HashMap<String, String>();
			mapError.put(Parameters.STATUS, SibConstants.FAILURE);
			mapError.put(SibConstants.MessageKey.REQUEST_DATA_RESUTL,
					e.getMessage());
			mapper.writeValue(writer, mapError);
			writer.flush();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
