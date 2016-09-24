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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.faqService;
import com.siblinks.ws.util.SibConstants;

/**
 *
 *
 * @author hungpd
 * @version 1.0
 */
@RestController
@RequestMapping("/siblinks/services/faqs")
public class faqServiceImpl implements faqService {

    private final Log logger = LogFactory.getLog(faqServiceImpl.class);

	@Autowired
	private HttpServletRequest context;

    @Autowired
    ObjectDao dao;

	@Override
	@RequestMapping(value = "/fetchFaqs/top", method = RequestMethod.POST)
	public ResponseEntity<Response> topFetchFaqs(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            SimpleResponse simpleResponse = new SimpleResponse("" + Boolean.FALSE, "Authentication required.");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            return entity;
        }

        Object[] queryParams = { request.getRequest_data().getLimit(), request.getRequest_data().getOrder() };
		List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_FETCH_FAQ_TOP, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
				request.getRequest_data_type(),
				request.getRequest_data_method(), readObject);
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				HttpStatus.OK);
		return entity;
	}

	@Override
	@RequestMapping(value = "/fetchFaqs", method = RequestMethod.POST)
	public ResponseEntity<Response> fetchFaqs(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            SimpleResponse simpleResponse = new SimpleResponse("" + Boolean.FALSE, "Authentication required.");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            return entity;
        }

        Object[] queryParams = { request.getRequest_data().getFaqCategory(), request.getRequest_data().getLimit(), request
            .getRequest_data()
            .getPage() };
		List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_FETCH_FAQ, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
				request.getRequest_data_type(),
				request.getRequest_data_method(), readObject);
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				HttpStatus.OK);
		return entity;
	}
}
