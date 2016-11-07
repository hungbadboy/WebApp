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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.siblinks.ws.filter.CORSFilter;
import com.siblinks.ws.service.impl.SibUserDetailServiceImp;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.SibConstants;
import com.siblinks.ws.util.SibConstants.RequestAuthentication;


/**
 * SibSecurityConfig configuration security for the authentication and
 * authorization
 *
 * @author hungpd
 * @version 1.0
 */

@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@ComponentScan(value = "com.siblinks.security")
public class SibSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String LOGIN_PATH = SibConstants.ROOT + SibConstants.User.LOGIN;
    private static final String LOGIN_OUT = SibConstants.ROOT + SibConstants.User.LOGOUT;
	@Autowired
    private SibUserDetailServiceImp userDetailsService;

	@Autowired
	private RESTAuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private RESTAuthenticationFailureHandler authenticationFailureHandler;

	@Autowired
	private RESTAuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	RESTLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private CORSFilter corsFilter;

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(
            new BCryptPasswordEncoder(SibConstants.LENGHT_AUTHENTICATION));
		return authenticationProvider;
	}

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }
	@Bean
	public SimpleUrlAuthenticationFailureHandler sibFailureHandler() {
		return new SimpleUrlAuthenticationFailureHandler();
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder builder) throws Exception {
		builder.authenticationProvider(authenticationProvider());
	}

    @Bean
    public CORSFilter corsFilter() {
        return new CORSFilter();
    }


	@Override
	protected void configure(final HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/*/**").permitAll()
            .antMatchers(RequestAuthentication.PATH_AUTHENTICATION)
            .authenticated();
        http.addFilterBefore(corsFilter, ChannelProcessingFilter.class);
		http.csrf().disable()
		.exceptionHandling()
		.authenticationEntryPoint(authenticationEntryPoint)
		.and()
		.formLogin()
		.loginProcessingUrl(LOGIN_PATH)
		.usernameParameter(Parameters.USER_NAME)
		.passwordParameter(Parameters.PASSWORD)
		.successHandler(authenticationSuccessHandler)
		.failureHandler(authenticationFailureHandler)
		.permitAll()
		.and()
            .logout()
            .logoutUrl(LOGIN_OUT)
            .logoutSuccessHandler(logoutSuccessHandler)
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
		.permitAll()
		.and()
		.rememberMe()
		.and()
		.sessionManagement()
		.maximumSessions(1);
	}
}
