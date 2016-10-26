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

import org.apache.commons.lang3.StringEscapeUtils;
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

import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.managerQAService;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.SibConstants;
import com.siblinks.ws.util.StringUtil;

/**
 * {@link managerQAService}
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

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getListQuestionQA", method = RequestMethod.POST)
    public ResponseEntity<Response> getListQuestionQA(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }
            String subjectId = request.getRequest_data().getSubjectId();
            String userId = request.getRequest_data().getUid();
            String limit = request.getRequest_data().getLimit();
            String offset = request.getRequest_data().getOffset();
            String type = request.getRequest_data().getType();
            String search = request.getRequest_data().getContent();
            String subjects = request.getRequest_data().getSubjects();
            String whereCause = "";

            if (!StringUtil.isNull(search)) {
                search = StringEscapeUtils.escapeJava(search);
                whereCause += " AND X.content like '%" + search + "%' ";
            }
            if (Parameters.UNANSWERED.equals(type)) {
                whereCause += " AND X.numReplies = 0 ";
            }
            if (Parameters.ANSWERED.equals(type)) {
                whereCause += " AND X.numReplies > 0 ";
            }

            if (!StringUtil.isNull(subjectId) && !"-1".equals(subjectId)) {
                whereCause += " AND X.subjectId = " + subjectId;
            } else {

                if (StringUtil.isNull(subjects)) {
                    whereCause += " AND FIND_IN_SET(X.subjectId , (SELECT defaultSubjectId FROM Sib_Users where userid = " +
                                  userId +
                                  " ))";
                } else {
                    whereCause += " AND FIND_IN_SET(X.subjectId , '" + subjects + "')";

                }
            }

            Object[] queryParams = {};
            boolean status = true;

            whereCause += " ORDER BY X.datetime DESC ";
            if (!StringUtil.isNull(limit)) {
                whereCause += " LIMIT " + limit;
            }

            if (!StringUtil.isNull(offset)) {
                whereCause += " OFFSET " + offset;
            }

            List<Object> readObject = dao.readObjectsWhereClause(
                SibConstants.SqlMapper.SQL_GET_ALL_QUESTION_MENTOR_BY_SUBJ,
                whereCause,
                queryParams);
            simpleResponse = new SimpleResponse(
                                                "" + status,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
        return entity;
    }

}
