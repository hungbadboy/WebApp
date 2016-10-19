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
import java.util.Map;

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
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.AnswerService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.SibConstants;

/**
 *
 *
 * @author hungpd
 * @version 1.0
 */
@RestController
@RequestMapping("/siblinks/services/answer")
public class AnswerServiceImpl implements AnswerService {

    private final Log logger = LogFactory.getLog(AnswerServiceImpl.class);

    @Autowired
    ObjectDao dao;

    @Autowired
    Environment env;

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/answerArticle", method = RequestMethod.POST)
    public ResponseEntity<Response> answerArticle(@RequestBody final RequestData request) {
        boolean status = true;
        String message = "";
        try {
            Object[] queryParams = { request.getRequest_data_answerArticle().getAuthorId(), request
                .getRequest_data_answerArticle()
                .getArId(), request.getRequest_data_answerArticle().getContent() };
            status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_ANSWER_ARTICLE, queryParams);
            if (status) {
                message = "Answer article successfully";
            } else {
                message = "Answer article fail";
            }
        } catch (DAOException e) {
            logger.debug(e.getMessage());
            status = false;
            message = e.getMessage();
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + status,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    message);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAnswersArticle", method = RequestMethod.POST)
    public ResponseEntity<Response> getAnswerArticle(@RequestBody final RequestData request) {
        SimpleResponse reponse = null;
        try {
            CommonUtil util = CommonUtil.getInstance();
            Map<String, String> map = util.getLimit(request.getRequest_data_answerArticle().getPageno(), request
                .getRequest_data_answerArticle()
                .getLimit());

            Object[] queryParams = { request.getRequest_data_answerArticle().getArId(), map.get(Parameters.FROM), map
                .get(Parameters.TO) };
            List<Object> readObject = dao.readObjects(
                env.getProperty(SibConstants.SqlMapper.SQL_GET_ANSWERS_ARTICLE_PN),
                queryParams);

            String count = dao.getCount(env.getProperty(SibConstants.SqlMapper.SQL_GET_ANSWERS_ARTICLE_COUNT), queryParams);
            reponse = new SimpleResponse(
                                         SibConstants.SUCCESS,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         readObject,
                                         count);
        } catch (DAOException e) {
            logger.debug(e.getMessage());
            reponse = new SimpleResponse(
                                         SibConstants.FAILURE,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         e.getMessage());
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/editAnswer", method = RequestMethod.POST)
    public ResponseEntity<Response> editAnswer(@RequestBody final RequestData request) {
        boolean status = false;
        String message = "";
        try {
            Object[] queryParams = { request.getRequest_data().getAid(), request.getRequest_data().getContent() };
            status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_ANSWER, queryParams);
        } catch (DAOException e) {
            logger.debug(e.getMessage());
            message = e.getMessage();
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" + status,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    message);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }
}
