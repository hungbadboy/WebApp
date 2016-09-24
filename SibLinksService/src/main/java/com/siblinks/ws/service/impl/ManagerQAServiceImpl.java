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
import org.springframework.core.env.Environment;
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
import com.siblinks.ws.service.managerQAService;
import com.siblinks.ws.util.SibConstants;
import com.siblinks.ws.util.StringUtil;

/**
 *
 *
 * @author hungpd
 * @version 1.0
 */
@RestController
@RequestMapping("/siblinks/services/managerQA")
public class ManagerQAServiceImpl implements managerQAService {

	private final Log logger = LogFactory.getLog(ManagerQAServiceImpl.class);

	@Autowired
	private HttpServletRequest context;

	@Autowired
	ObjectDao dao;

	@Autowired
	private Environment environment;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.siblinks.ws.service.managerQAService#getListQuestionQA(com.siblinks.
	 * ws.model.RequestData)
	 */
	@Override
	@RequestMapping(value = "/getListQuestionQA", method = RequestMethod.POST)
	public ResponseEntity<Response> getListQuestionQA(@RequestBody final RequestData request) {
		if (!AuthenticationFilter.isAuthed(context)) {
			ResponseEntity<Response> entity = new ResponseEntity<Response>(
					new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
			return entity;
		}
		String categoryID = request.getRequest_data().getSubjectId();
		String topicID = request.getRequest_data().getTopicId();
		String limit = request.getRequest_data().getLimit();
		String whereCause = "";
		if (!StringUtil.isNull(categoryID) && categoryID.contains(",")) {
			categoryID = categoryID.substring(0, categoryID.length() - 1);
			whereCause += " AND SS.subjectId in(" + categoryID + ")";
		}
		if (!StringUtil.isNull(topicID) && topicID.contains(",")) {
			topicID = topicID.substring(0, topicID.length() - 1);
			whereCause += "  AND ST.topicId in (" + topicID + ") ";

		}
		Object[] queryParams = {};
		List<Object> readObject = null;
		boolean status = true;

		whereCause += "ORDER BY SFP.`timeStamp` DESC ";
		if (!StringUtil.isNull(limit)) {
			whereCause += " LIMIT " + limit;
		}

		readObject = dao.readObjectsWhereClause(SibConstants.SqlMapper.SQL_GET_ALL_QUESTION_MENTOR_BY_TOPIC_SUBJ,
				whereCause, queryParams);
		SimpleResponse reponse = new SimpleResponse("" + status, request.getRequest_data_type(),
				request.getRequest_data_method(), readObject);
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
		return entity;
	}

}
