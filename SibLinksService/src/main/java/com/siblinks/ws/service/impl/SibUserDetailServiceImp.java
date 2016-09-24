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
package com.siblinks.ws.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.model.SibUser;
import com.siblinks.ws.model.SibUserDetails;
import com.siblinks.ws.util.Parameters;

/**
 *
 * @author hungpd
 *
 */
@Service
public class SibUserDetailServiceImp implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SibUserDetailServiceImp.class);

    @Autowired
    private ObjectDao dao;

	@Override
	@SuppressWarnings("unchecked")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOGGER.debug("Authenticating user with username = {}", username.replaceFirst("@.*", "@.***"));


		username = username.replace("'", " ");
		Object[] queryParams = {username};

		List<Object> readObject = dao.readObjects(Parameters.GET_USER_BY_USERNAME, queryParams);

		if (CollectionUtils.isEmpty(readObject)) {
            throw new UsernameNotFoundException(username);
        }

		Map<String, Object> userMap =(Map<String, Object>)readObject.get(0);
        List<GrantedAuthority> authorities = getAuthorities((String) userMap.get(Parameters.USER_TYPE));
		SibUser user = new SibUser();
		user.setUsername(username);
        user.setUserid(userMap.get(Parameters.USER_ID) + "");
		user.setPassword((String) userMap.get(Parameters.PASSWORD));
		user.setFirstname((String) userMap.get(Parameters.FIRST_NAME));
		user.setLastname((String) userMap.get(Parameters.LAST_NAME));
		user.setUserType((String) userMap.get(Parameters.USER_TYPE));
		return new SibUserDetails(user, authorities);
	}

	private List<GrantedAuthority> getAuthorities(final String role) {
        List<GrantedAuthority> authList = new ArrayList<>();
        if (role != null && role.trim().length() > 0) {
            if ("A".equals(role)) {
        		authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else if ("M".equals(role)) {
                authList.add(new SimpleGrantedAuthority("ROLE_MENTOR"));
            } else if ("S".equals(role)) {
        	    authList.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
            } else {
                authList.add(new SimpleGrantedAuthority("ROLE_GUEST"));
            }
        }
        return authList;
	}
}
