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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.siblinks.ws.service.NotificationUserService;
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
@RequestMapping("/siblinks/services/notification")
public class NotificationUserServiceImpl implements NotificationUserService {

    private final Log logger = LogFactory.getLog(NotificationUserServiceImpl.class);

    @Autowired
    private HttpServletRequest context;

    @Autowired
    ObjectDao dao;

    @Override
    @RequestMapping(value = "/getNotificationNotReaded", method = RequestMethod.POST)
    public ResponseEntity<Response> getNotificationNotReaded(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            SimpleResponse simpleResponse = new SimpleResponse("" + false, "Authentication required.");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            return entity;
        }

        Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getStatus() };

        List<Object> readObject;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_NOTIFICATION_NOT_READED, queryParams);

        String count = null;
        if (readObject.size() > 0) {
            count = dao.getCount(SibConstants.SqlMapper.SQL_GET_NOTIFICATION_NOT_READED_COUNT, queryParams);
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject,
                                                    count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getNotificationReaded", method = RequestMethod.POST)
    public ResponseEntity<Response> getNotificationReaded(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            SimpleResponse simpleResponse = new SimpleResponse("" + false, "Authentication required.");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            return entity;
        }

        Object[] queryParams = { request.getRequest_data().getUid() };

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_NOTIFICATION_READED, queryParams);

        String count = null;
        if (readObject.size() > 0) {
            count = dao.getCount(SibConstants.SqlMapper.SQL_GET_NOTIFICATION_READED_COUNT, queryParams);
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject,
                                                    count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateStatusNotification", method = RequestMethod.POST)
    public ResponseEntity<Response> updateStatusNotification(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            SimpleResponse simpleResponse = new SimpleResponse("" + false, "Authentication required.");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("nid", request.getRequest_data().getNid());
        queryParams.put("status", request.getRequest_data().getStatus());

        boolean readObject;
        readObject = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_STATUS_NOTIFICATION, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getAllNotification", method = RequestMethod.POST)
    public ResponseEntity<Response> getAllNotification(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            SimpleResponse simpleResponse = new SimpleResponse("" + false, "Authentication required.");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            return entity;
        }

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Object[] queryParams = { request.getRequest_data().getUid(), map.get(Parameters.FROM), map.get(Parameters.TO) };

        List<Object> readObject;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ALL_NOTIFICATION, queryParams);

        String count = null;
        if (readObject.size() > 0) {
            count = dao.getCount(SibConstants.SqlMapper.SQL_GET_ALL_NOTIFICATION_COUNT, queryParams);
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject,
                                                    count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateStatusAllNotification", method = RequestMethod.POST)
    public ResponseEntity<Response> updateStatusAllNotification(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            SimpleResponse simpleResponse = new SimpleResponse("" + false, "Authentication required.");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            return entity;
        }

        Object[] queryParams = { request.getRequest_data().getUid() };

        boolean status = false;
        status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_STATUS_ALL_NOTIFICATION, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    status);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }
}
