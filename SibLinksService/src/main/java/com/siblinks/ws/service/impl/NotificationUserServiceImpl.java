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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.common.DAOException;
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
 * {@link NotificationUserService}
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

    /**
     * This API get notification of user who is not yet read content
     * 
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getNotificationNotReaded", method = RequestMethod.GET)
    public ResponseEntity<Response> getNotificationNotReaded(@RequestParam final String uid, @RequestParam final String status) {

        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            String count = dao.getCount(
                SibConstants.SqlMapper.SQL_GET_NOTIFICATION_NOT_READED_COUNT,
                new Object[] { status, uid });
            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "notification", "getNotificationNotReaded", "", count);
        } catch (DAOException e) {
            logger.error(e);
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "notification", "getNotificationNotReaded", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getNotificationReaded", method = RequestMethod.GET)
    public ResponseEntity<Response> getNotificationReaded(@RequestParam final String uid) {

        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            String count = dao.getCount(SibConstants.SqlMapper.SQL_GET_NOTIFICATION_READED_COUNT, new Object[] { uid });
            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "notification", "getNotificationReaded", "", count);
        } catch (DAOException e) {
            logger.error(e);
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "notification", "getNotificationReaded", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateStatusNotification", method = RequestMethod.POST)
    public ResponseEntity<Response> updateStatusNotification(@RequestBody final RequestData request) {

        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            boolean readObject = dao.insertUpdateObject(
                SibConstants.SqlMapper.SQL_UPDATE_STATUS_NOTIFICATION,
                new Object[] { request.getRequest_data().getNid(), request.getRequest_data().getStatus() });
            
            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            logger.error(e);
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "notification", "updateStatusNotification", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAllNotification", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllNotification(@RequestParam final String uid, @RequestParam final String pageno,
            @RequestParam final String limit) {

        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            CommonUtil util = CommonUtil.getInstance();

            Map<String, String> map = util.getLimit(pageno, limit);

            Object[] queryParams = { uid, Integer.parseInt(map.get(Parameters.FROM)), Integer.parseInt(map.get(Parameters.TO)) };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ALL_NOTIFICATION, queryParams);

            String count = null;
            if (readObject.size() > 0) {
                count = dao.getCount(SibConstants.SqlMapper.SQL_GET_ALL_NOTIFICATION_COUNT, new Object[] { uid });
            }

            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "notification", "getAllNotification", readObject, count);
        } catch (Exception e) {
            logger.error(e);
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "notification", "getAllNotification", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateStatusAllNotification", method = RequestMethod.POST)
    public ResponseEntity<Response> updateStatusAllNotification(@RequestBody final RequestData request) {

        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = { request.getRequest_data().getUid() };

            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_STATUS_ALL_NOTIFICATION, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                status);
        } catch (Exception e) {
            logger.error(e);
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "notification", "getNotificationNotReaded", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }
}
