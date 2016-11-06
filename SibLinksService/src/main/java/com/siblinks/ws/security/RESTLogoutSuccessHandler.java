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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.util.SibConstants;

/**
 * Handling after user logout success
 *
 * @author hungpd
 * @version 1.0
 */
@Component
public class RESTLogoutSuccessHandler implements LogoutSuccessHandler {
@Autowired
	ObjectDao dao;
	@Override
	public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication)
            throws IOException {
		String uid = request.getParameter("uid");
		if (uid != null && !"".equals(uid)) {
			// update isonline
			try {
				dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_USER_LOGOUT, new Object[] { uid });
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
		response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        JsonObject json = new JsonObject();
        json.addProperty("status", true);
        writer.print(json.toString());
        writer.flush();
	}
}
