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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.LikeService;
import com.siblinks.ws.util.SibConstants;

/**
 *
 *
 * @author hungpd
 * @version 1.0
 */
@RestController
@RequestMapping("/siblinks/services/like")
public class LikeServiceImpl implements LikeService {

    private final Log logger = LogFactory.getLog(LikeServiceImpl.class);

    @Autowired
    private HttpServletRequest context;

    @Autowired
    ObjectDao dao;

    @Override
    @RequestMapping(value = "/likeComment", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> likeComment(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              false,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("authorID", request.getRequest_data().getAuthorID());
        queryParams.put("cid", request.getRequest_data().getCid());

        List<Object> readObject = null;
        String entityName = SibConstants.SqlMapper.SQL_LIKE_COMEMENT_READ;
        readObject = dao.readObjects(entityName, queryParams);

        boolean status = true, notifi = true;
        Map<String, String> queryParamsIns = null;
        String message = "";
        if (readObject.size() == 0) {
            queryParamsIns = new HashMap<String, String>();
            entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_INSERT;
            status = dao.insertUpdateObject(entityName, queryParams);

            queryParamsIns = getParamLikeComment(
                "GET_VIDEOID_OF_COMMENT",
                "GET_AUTHOR_COMMENT",
                "GET_VIDEO_WITH_VIDEOID",
                queryParams,
                request);
            if (!queryParamsIns.get("uid").toString().equalsIgnoreCase(request.getRequest_data().getAuthorID())) {
                entityName = SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_VIDEO;
                notifi = dao.insertUpdateObject(entityName, queryParamsIns);
            }
        } else {
            entityName = SibConstants.SqlMapper.SQL_LIKE_COMEMENT_READ_CHECK;
            readObject = dao.readObjects(entityName, queryParams);
            if (readObject.size() == 0) {
                queryParamsIns = new HashMap<String, String>();
                entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_UPDATE;
                status = dao.insertUpdateObject(entityName, queryParams);

                queryParamsIns = getParamLikeComment(
                    "GET_VIDEOID_OF_COMMENT",
                    "GET_AUTHOR_COMMENT",
                    "GET_VIDEO_WITH_VIDEOID",
                    queryParams,
                    request);
                if (!queryParamsIns.get("uid").toString().equalsIgnoreCase(request.getRequest_data().getAuthorID())) {
                    entityName = SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_VIDEO;
                    notifi = dao.insertUpdateObject(entityName, queryParamsIns);
                }
            } else {
                entityName = SibConstants.SqlMapper.SQL_UNLIKE_COMMENT_UPDATE;
                status = dao.insertUpdateObject(entityName, queryParams);
            }
        }
        entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_READ;
        readObject = dao.readObjects(entityName, queryParams);
        if (status) {
            message = "Done";
        } else {
            message = "Fail";
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    status,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/likeCommentMobile", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> likeCommentMobile(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              false,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("uid", request.getRequest_data().getUid());
        queryParams.put("cid", request.getRequest_data().getCid());
        List<Object> readObject = null;
        String entityName = SibConstants.SqlMapper.SQL_LIKE_COMEMENT_READ;
        readObject = dao.readObjects(entityName, queryParams);

        boolean status1 = false;
        queryParams.put("status", "Y");
        if (readObject == null || readObject.size() == 0) {
            entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_INSERT;
            boolean msgs = dao.insertUpdateObject(entityName, queryParams);
            entityName = SibConstants.SqlMapper.SQL_LIKE_UPDATE;
            dao.insertUpdateObject(entityName, queryParams);
            readObject = new ArrayList<Object>();
            if (msgs) {
                readObject.add(request.getRequest_data().getUid());
                readObject.add(request.getRequest_data().getCid());
                status1 = true;
            } else {
                readObject.add(request.getRequest_data().getUid());
                readObject.add(request.getRequest_data().getCid());
            }
        } else {
            entityName = SibConstants.SqlMapper.SQL_LIKE_COMEMENT_READ_CHECK;//
            readObject = dao.readObjects(entityName, queryParams);
            if (readObject == null) {
                entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_UPDATE;
                boolean msgs = dao.insertUpdateObject(entityName, queryParams);

                entityName = SibConstants.SqlMapper.SQL_LIKE_UPDATE;
                dao.insertUpdateObject(entityName, queryParams);

                readObject = new ArrayList<Object>();
                if (msgs) {
                    readObject.add(request.getRequest_data().getUid());
                    readObject.add(request.getRequest_data().getCid());
                    status1 = true;
                } else {
                    readObject.add(request.getRequest_data().getUid());
                    readObject.add(request.getRequest_data().getCid());
                }
            } else {
                status1 = true;
                readObject = new ArrayList<Object>();
                readObject.add(request.getRequest_data().getUid());
                readObject.add(request.getRequest_data().getCid());
            }

        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    status1,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);

        return entity;
    }

    @Override
    @RequestMapping(value = "/likeQuestion", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> likeQuestion(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              false,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        List<Object> readObject = null;
        Object[] queryParams = { request.getRequest_data().getAuthorID(), request.getRequest_data().getPid() };

        String entityName = SibConstants.SqlMapper.SQL_LIKE_POST_READ;
        readObject = dao.readObjects(entityName, queryParams);
        boolean status = true, notifi = true;
        String message = "";
        Map<String, String> queryParamsIns = new HashMap<String, String>();
        if (readObject.size() == 0) {
            status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_QUESTION_ID_LIKE, queryParams);

            queryParamsIns = getParamLikeQuestion(SibConstants.SqlMapper.SQL_GET_POST_WITH_POSTID, queryParams, request);
            if (!queryParamsIns.get("uid").toString().equalsIgnoreCase(request.getRequest_data().getAuthorID())) {
                entityName = SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_QUESTION;
                notifi = dao.insertUpdateObject(entityName, queryParamsIns);
            }
        } else {
            entityName = SibConstants.SqlMapper.SQL_SELECT_QUESTION;
            readObject = dao.readObjects(entityName, queryParams);
            if (readObject.size() == 0) {
                entityName = SibConstants.SqlMapper.SQL_UPDATE_LIKE_POST;
                status = dao.insertUpdateObject(entityName, queryParams);

                queryParamsIns = getParamLikeQuestion("GET_POST_WITH_POSTID", queryParams, request);
                if (!queryParamsIns.get("uid").toString().equalsIgnoreCase(request.getRequest_data().getAuthorID())) {
                    entityName = SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_QUESTION;
                    notifi = dao.insertUpdateObject(entityName, queryParamsIns);
                }
            } else {
                entityName = SibConstants.SqlMapper.SQL_UPDATE_UNLIKE_POST;
                status = dao.insertUpdateObject(entityName, queryParams);
            }
        }
        entityName = SibConstants.SqlMapper.SQL_LIKE_POST_READ;
        readObject = dao.readObjects(entityName, queryParams);

        if (status) {
            message = "Done";
        } else {
            message = "Fail";
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    status,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/likeAnswer", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> likeAnswer(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              false,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }
        Object[] queryParams = { request.getRequest_data().getAuthorID(), request.getRequest_data().getAid() };
        List<Object> readObject = null;
        String entityName = SibConstants.SqlMapper.SQL_LIKE_ANSWER_READ;
        readObject = dao.readObjects(entityName, queryParams);
        boolean status = true, notifi = true;
        String message = "";
        String statusType;
        if (readObject.size() == 0) {
            entityName = SibConstants.SqlMapper.SQL_ANSWER_ID_LIKE;
            status = dao.insertUpdateObject(entityName, queryParams);

            Object[] params = getParamLikeAnswer(
                SibConstants.SqlMapper.SQL_GET_POSTID_WITH_ANSWER,
                SibConstants.SqlMapper.SQL_GET_POST_WITH_POSTID,
                queryParams,
                request);
            if (!params[0].toString().equalsIgnoreCase(request.getRequest_data().getAuthorID())) {
                entityName = SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_QUESTION;
                notifi = dao.insertUpdateObject(entityName, params);
            }
            statusType = "like";
        } else {
            entityName = SibConstants.SqlMapper.SQL_SELECT_ANSWER;
            readObject = dao.readObjects(entityName, queryParams);
            if (readObject.size() == 0) {
                entityName = SibConstants.SqlMapper.SQL_UPDATE_LIKE_ANSWER;
                status = dao.insertUpdateObject(entityName, queryParams);

                Object[] params = getParamLikeAnswer("GET_POSTID_WITH_ANSWER", "GET_POST_WITH_POSTID", queryParams, request);
                if (!params[0].toString().equalsIgnoreCase(request.getRequest_data().getAuthorID())) {
                    entityName = SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_QUESTION;
                    notifi = dao.insertUpdateObject(entityName, params);
                }
                statusType = "like";
            } else {
                entityName = SibConstants.SqlMapper.SQL_UPDATE_UNLIKE_ANSWER;
                status = dao.insertUpdateObject(entityName, queryParams);
                statusType = "unlike";
            }
        }
        entityName = SibConstants.SqlMapper.SQL_LIKE_ANSWER_READ;
        readObject = dao.readObjects(entityName, queryParams);

        if (status) {
            message = "Done";
        } else {
            message = "Fail";
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "statusType: " +
                                                    statusType +
                                                    ",status: " +
                                                    message,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/unlikeComment", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> unlikeComment(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              false,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("userid", request.getRequest_data().getUid());
        queryParams.put("cid", request.getRequest_data().getCid());
        List<Object> readObject = null;
        String entityName = SibConstants.SqlMapper.SQL_LIKE_COMEMENT_READ_CHECK;
        readObject = dao.readObjects(entityName, queryParams);

        boolean status1 = false;

        if (readObject == null) {
            status1 = true;
            readObject = new ArrayList<Object>();
            readObject.add(request.getRequest_data().getCid());
        } else {
            queryParams.put("status", "N");

            entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_UPDATE;
            boolean msgs = dao.insertUpdateObject(entityName, queryParams);

            entityName = SibConstants.SqlMapper.SQL_UNLIKE_UPDATE;
            dao.insertUpdateObject(entityName, queryParams);
            readObject = new ArrayList<Object>();
            if (msgs) {
                readObject.add(request.getRequest_data().getCid());
                status1 = true;
            } else {
                readObject.add(request.getRequest_data().getCid());
            }
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    status1,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/likeVideo", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> likeVideo(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              false,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("userid", request.getRequest_data().getUid());
        queryParams.put("vid", request.getRequest_data().getVid());
        List<Object> readObject = null;
        String entityName = SibConstants.SqlMapper.SQL_LIKE_VIDEO_READ;
        readObject = dao.readObjects(entityName, queryParams);

        boolean status1 = false;
        queryParams.put("status", "Y");
        if (readObject == null) {
            entityName = SibConstants.SqlMapper.SQL_LIKE_VIDEO_INSERT;
            boolean msgs = dao.insertUpdateObject(entityName, queryParams);
            entityName = SibConstants.SqlMapper.SQL_VIDEO_LIKE_UPDATE;
            dao.insertUpdateObject(entityName, queryParams);
            readObject = new ArrayList<Object>();
            if (msgs) {
                readObject.add(request.getRequest_data().getVid());
                status1 = true;
            } else {
                readObject.add(request.getRequest_data().getVid());
            }
        } else {
            entityName = SibConstants.SqlMapper.SQL_LIKE_VIDEO_READ_CHECK;
            readObject = dao.readObjects(entityName, queryParams);
            if (readObject == null) {
                entityName = SibConstants.SqlMapper.SQL_LIKE_VIDEO_UPDATE;
                boolean msgs = dao.insertUpdateObject(entityName, queryParams);

                entityName = SibConstants.SqlMapper.SQL_VIDEO_LIKE_UPDATE;
                dao.insertUpdateObject(entityName, queryParams);

                readObject = new ArrayList<Object>();
                if (msgs) {
                    readObject.add(request.getRequest_data().getVid());
                    status1 = true;
                } else {
                    readObject.add(request.getRequest_data().getVid());
                }
            } else {
                status1 = true;
                readObject = new ArrayList<Object>();
                readObject.add(request.getRequest_data().getVid());
            }
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    status1,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/unlikeVideo", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> unlikeVideo(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              false,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("userid", request.getRequest_data().getUid());
        queryParams.put("vid", request.getRequest_data().getVid());
        List<Object> readObject = null;
        String entityName = SibConstants.SqlMapper.SQL_LIKE_VIDEO_READ_CHECK;
        readObject = dao.readObjects(entityName, queryParams);

        boolean status1 = false;

        if (readObject == null) {
            status1 = true;
            readObject = new ArrayList<Object>();
            readObject.add(request.getRequest_data().getVid());
        } else {
            queryParams.put("status", "N");
            entityName = SibConstants.SqlMapper.SQL_LIKE_VIDEO_UPDATE;
            boolean msgs = dao.insertUpdateObject(entityName, queryParams);

            entityName = SibConstants.SqlMapper.SQL_VIDEO_UNLIKE_UPDATE;
            dao.insertUpdateObject(entityName, queryParams);

            readObject = new ArrayList<Object>();
            if (msgs) {
                readObject.add(request.getRequest_data().getVid());
                status1 = true;
            } else {
                readObject.add(request.getRequest_data().getVid());
            }
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    status1,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/markVideoAsWatched", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> markVideoAsWatched(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              false,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("userid", request.getRequest_data().getUid());
        queryParams.put("vid", request.getRequest_data().getVid());
        List<Object> readObject = null;
        String entityName = SibConstants.SqlMapper.SQL_WATCH_VIDEO_READ;
        readObject = dao.readObjects(entityName, queryParams);

        boolean status1 = false;

        if (readObject == null) {
            entityName = SibConstants.SqlMapper.SQL_WATCH_VIDEO_INSERT;
            boolean msgs = dao.insertUpdateObject(entityName, queryParams);
            entityName = SibConstants.SqlMapper.SQL_WATCH_VIDEO_UPDATE;
            dao.insertUpdateObject(entityName, queryParams);
            readObject = new ArrayList<Object>();
            if (msgs) {
                readObject.add(request.getRequest_data().getVid());
                status1 = true;
            } else {
                readObject.add(request.getRequest_data().getVid());
            }
        } else {
            status1 = true;
            readObject = new ArrayList<Object>();

            readObject.add(request.getRequest_data().getVid());
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    status1,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getPostLikeByUser", method = RequestMethod.POST)
    public ResponseEntity<Response> getPostLikeByUser(@RequestBody final RequestData request) {

        String entityName = null;
        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              false,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("uid", request.getRequest_data().getUid());
        entityName = SibConstants.SqlMapper.SQL_GET_LIKE_POST_BY_USER;

        List<Object> readObject = null;
        readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    true,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/likeCommentArticle", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> likeCommentArticle(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              false,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("authorID", request.getRequest_data_article().getAuthorId());
        queryParams.put("cid", request.getRequest_data_article().getCid());

        List<Object> readObject = null;
        String entityName = SibConstants.SqlMapper.SQL_LIKE_COMEMENT_READ;
        readObject = dao.readObjects(entityName, queryParams);

        boolean status = true, notifi = true;
        Map<String, String> queryParamsIns = null;
        String message = "";
        if (readObject.size() == 0) {
            queryParamsIns = new HashMap<String, String>();
            entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_INSERT;
            status = dao.insertUpdateObject(entityName, queryParams);

            queryParamsIns = getParamLikeCommentArticle(
                "GET_ARTICLEID_OF_COMMENT",
                "GET_AUTHOR_COMMENT",
                "GET_ARTICLE_WITH_ARTICLEID",
                queryParams,
                request);
            if (!queryParamsIns.get("uid").toString().equalsIgnoreCase(request.getRequest_data_article().getAuthorId())) {
                entityName = SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_ARTICLE;
                notifi = dao.insertUpdateObject(entityName, queryParamsIns);
            }
        } else {
            entityName = SibConstants.SqlMapper.SQL_LIKE_COMEMENT_READ_CHECK;
            readObject = dao.readObjects(entityName, queryParams);
            if (readObject.size() == 0) {
                queryParamsIns = new HashMap<String, String>();
                entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_UPDATE;
                status = dao.insertUpdateObject(entityName, queryParams);

                queryParamsIns = getParamLikeCommentArticle(
                    "GET_ARTICLEID_OF_COMMENT",
                    "GET_AUTHOR_COMMENT",
                    "GET_ARTICLE_WITH_ARTICLEID",
                    queryParams,
                    request);
                if (!queryParamsIns.get("uid").toString().equalsIgnoreCase(request.getRequest_data_article().getAuthorId())) {
                    entityName = SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_ARTICLE;
                    notifi = dao.insertUpdateObject(entityName, queryParamsIns);
                }
            } else {
                entityName = SibConstants.SqlMapper.SQL_UNLIKE_COMMENT_UPDATE;
                status = dao.insertUpdateObject(entityName, queryParams);
            }
        }
        entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_READ;
        readObject = dao.readObjects(entityName, queryParams);
        if (status) {
            message = "Done";
        } else {
            message = "Fail";
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    status,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/likeCommentVideoAdmission", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> likeCommentVideoAdmission(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              false,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("authorID", request.getRequest_data_videoAdmission().getAuthorId());
        queryParams.put("cid", request.getRequest_data_videoAdmission().getCid());

        List<Object> readObject = null;
        String entityName = SibConstants.SqlMapper.SQL_LIKE_COMEMENT_READ;
        readObject = dao.readObjects(entityName, queryParams);

        boolean status = true, notifi = true;
        Map<String, String> queryParamsIns = null;
        String message = "";
        if (readObject.size() == 0) {
            queryParamsIns = new HashMap<String, String>();
            entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_INSERT;
            status = dao.insertUpdateObject(entityName, queryParams);

            queryParamsIns = getParamLikeCommentVideoAdmission(
                "GET_VIDEO_ADMISSION_ID_OF_COMMENT",
                "GET_AUTHOR_COMMENT",
                "GET_VIDEO_ADMISSION_WITH_VIDEOID",
                queryParams,
                request);
            if (!queryParamsIns.get("uid").toString().equalsIgnoreCase(request.getRequest_data_videoAdmission().getAuthorId())) {
                entityName = SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_VIDEO_ADMISSION;
                notifi = dao.insertUpdateObject(entityName, queryParamsIns);
            }
        } else {
            entityName = SibConstants.SqlMapper.SQL_LIKE_COMEMENT_READ_CHECK;
            readObject = dao.readObjects(entityName, queryParams);
            if (readObject.size() == 0) {
                queryParamsIns = new HashMap<String, String>();
                entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_UPDATE;
                status = dao.insertUpdateObject(entityName, queryParams);

                queryParamsIns = getParamLikeCommentVideoAdmission(
                    "GET_VIDEO_ADMISSION_ID_OF_COMMENT",
                    "GET_AUTHOR_COMMENT",
                    "GET_VIDEO_ADMISSION_WITH_VIDEOID",
                    queryParams,
                    request);
                if (!queryParamsIns
                    .get("uid")
                    .toString()
                    .equalsIgnoreCase(request.getRequest_data_videoAdmission().getAuthorId())) {
                    entityName = SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_VIDEO_ADMISSION;
                    notifi = dao.insertUpdateObject(entityName, queryParamsIns);
                }
            } else {
                entityName = SibConstants.SqlMapper.SQL_UNLIKE_COMMENT_UPDATE;
                status = dao.insertUpdateObject(entityName, queryParams);
            }
        }
        entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_READ;
        readObject = dao.readObjects(entityName, queryParams);
        if (status) {
            message = "Done";
        } else {
            message = "Fail";
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    status,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/likeCommentEssay", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> likeCommentEssay(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              false,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("authorID", request.getRequest_data().getAuthorID());
        queryParams.put("cid", request.getRequest_data().getCid());

        List<Object> readObject = null;
        String entityName = SibConstants.SqlMapper.SQL_LIKE_COMEMENT_READ;
        readObject = dao.readObjects(entityName, queryParams);

        boolean status = true, notifi = true;
        Map<String, String> queryParamsIns = null;
        String message = "";
        if (readObject.size() == 0) {
            queryParamsIns = new HashMap<String, String>();
            entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_INSERT;
            status = dao.insertUpdateObject(entityName, queryParams);

            queryParamsIns = getParamLikeCommentEssay(
                "GET_ESSAYID_OF_COMMENT",
                "GET_AUTHOR_COMMENT",
                "GET_ESSAY_WITH_ESSAYID",
                queryParams,
                request);
            if (!queryParamsIns.get("uid").toString().equalsIgnoreCase(request.getRequest_data().getAuthorID())) {
                entityName = SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_ESSAY;
                notifi = dao.insertUpdateObject(entityName, queryParamsIns);
            }
        } else {
            entityName = SibConstants.SqlMapper.SQL_LIKE_COMEMENT_READ_CHECK;
            readObject = dao.readObjects(entityName, queryParams);
            if (readObject.size() == 0) {
                queryParamsIns = new HashMap<String, String>();
                entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_UPDATE;
                status = dao.insertUpdateObject(entityName, queryParams);

                queryParamsIns = getParamLikeCommentEssay(
                    "GET_ESSAYID_OF_COMMENT",
                    "GET_AUTHOR_COMMENT",
                    "GET_ESSAY_WITH_ESSAYID",
                    queryParams,
                    request);
                if (!queryParamsIns.get("uid").toString().equalsIgnoreCase(request.getRequest_data().getAuthorID())) {
                    entityName = SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_ESSAY;
                    notifi = dao.insertUpdateObject(entityName, queryParamsIns);
                }
            } else {
                entityName = SibConstants.SqlMapper.SQL_UNLIKE_COMMENT_UPDATE;
                status = dao.insertUpdateObject(entityName, queryParams);
            }
        }
        entityName = SibConstants.SqlMapper.SQL_LIKE_COMMENT_READ;
        readObject = dao.readObjects(entityName, queryParams);
        if (status) {
            message = "Done";
        } else {
            message = "Fail";
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    status,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    public Map<String, String> getParamLikeComment(final String sql1, final String sql2, final String sql3,
            final Map<String, String> queryParams, final RequestData request) {

        List<Object> readObject = null;
        Map<String, String> queryParamsIns = new HashMap<String, String>();

        readObject = dao.readObjects(sql1, queryParams);
        queryParamsIns.put("vid", ((Map) readObject.get(0)).get("vid").toString());

        readObject = dao.readObjects(sql2, queryParams);
        queryParamsIns.put("uid", ((Map) readObject.get(0)).get("authorId").toString());

        readObject = dao.readObjects(sql3, queryParamsIns);
        queryParamsIns.put("authorID", request.getRequest_data().getAuthorID());
        queryParamsIns.put("subjectId", ((Map) readObject.get(0)).get("subjectId").toString());
        queryParamsIns.put("topicId", ((Map) readObject.get(0)).get("topicId").toString());
        queryParamsIns.put("type", "likeDiscussion");
        queryParamsIns.put("title", "Like discussion");
        queryParamsIns.put("notification", "liked your discussion");

        return queryParamsIns;
    }

    public Map<String, String> getParamLikeQuestion(final String sql, final Object[] queryParams, final RequestData request) {

        List<Object> readObject = null;
        Map<String, String> queryParamsIns = new HashMap<String, String>();

        readObject = dao.readObjects(sql, queryParams);
        queryParamsIns.put("authorID", request.getRequest_data().getAuthorID());
        queryParamsIns.put("pid", request.getRequest_data().getPid());
        queryParamsIns.put("uid", ((Map) readObject.get(0)).get("authorID").toString());
        queryParamsIns.put("subjectId", ((Map) readObject.get(0)).get("subjectId").toString());
        queryParamsIns.put("type", "likeQuestion");
        queryParamsIns.put("title", "Like question");
        queryParamsIns.put("notification", "liked your question");
        return queryParamsIns;
    }

    public Object[] getParamLikeAnswer(final String sql1, final String sql2, final Object[] queryParams,
            final RequestData request) {

        List<Object> readObject = null;
        Map<String, String> queryParamsIns = new HashMap<String, String>();
        Object[] paramsInsNotify = null;
        readObject = dao.readObjects(sql1, new Object[] { queryParams[1] });
        System.out.println(readObject);
        if (!CollectionUtils.isEmpty(readObject)) {
            queryParamsIns.put("pid", ((Map) readObject.get(0)).get("pid").toString());
            queryParamsIns.put("uid", ((Map) readObject.get(0)).get("authorID").toString());
            readObject = dao.readObjects(sql2, new Object[] { "" + ((Map) readObject.get(0)).get("pid") });
            System.out.println(readObject);
            if (!CollectionUtils.isEmpty(readObject)) {
                String contentNotify = "Liked your answer in question : " + ((Map) readObject.get(0)).get("title").toString();
                paramsInsNotify = new Object[] { queryParamsIns.get("uid"), request
                    .getRequest_data()
                    .getAuthorID(), "likeAnswer", "Like Answer", contentNotify, ((Map) readObject.get(0))
                        .get("subjectId"), ((Map) readObject.get(0)).get("topicId"), queryParamsIns.get("pid") };
            }
        }
        return paramsInsNotify;
    }

    public Map<String, String> paramLikeAnswer(final String sql1, final String sql2, final Object[] queryParams,
            final String authorID) {

        List<Object> readObject = null;
        Map<String, String> queryParamsIns = new HashMap<String, String>();

        readObject = dao.readObjects(sql1, new Object[] { queryParams[0] });
        if (!CollectionUtils.isEmpty(readObject)) {
            queryParamsIns.put("pid", ((Map) readObject.get(0)).get("pid").toString());
            queryParamsIns.put("uid", ((Map) readObject.get(0)).get("authorID").toString());
            readObject = dao.readObjects(sql2, new Object[] { "" + ((Map) readObject.get(0)).get("pid") });
            if (!CollectionUtils.isEmpty(readObject)) {
                queryParamsIns.put("authorID", authorID);
                queryParamsIns.put("subjectId", ((Map) readObject.get(0)).get("subjectId").toString());
                queryParamsIns.put("type", "likeAnswer");
                queryParamsIns.put("title", "Like answer");
                queryParamsIns.put("notification", "liked your answer");
            }
        }
        return queryParamsIns;
    }

    public Map<String, String> getParamLikeCommentArticle(final String sql1, final String sql2, final String sql3,
            final Map<String, String> queryParams, final RequestData request) {

        List<Object> readObject = null;
        Map<String, String> queryParamsIns = new HashMap<String, String>();

        readObject = dao.readObjects(sql1, queryParams);
        queryParamsIns.put("arId", ((Map) readObject.get(0)).get("arId").toString());

        readObject = dao.readObjects(sql2, queryParams);
        queryParamsIns.put("uid", ((Map) readObject.get(0)).get("authorId").toString());

        readObject = dao.readObjects(sql3, queryParamsIns);
        queryParamsIns.put("authorID", request.getRequest_data_article().getAuthorId());
        queryParamsIns.put("type", "likeDiscussionArticle");
        queryParamsIns.put("title", "Like discussion article");
        queryParamsIns.put("notification", "liked your discussion article");

        return queryParamsIns;
    }

    public Map<String, String> getParamLikeCommentVideoAdmission(final String sql1, final String sql2, final String sql3,
            final Map<String, String> queryParams, final RequestData request) {

        List<Object> readObject = null;
        Map<String, String> queryParamsIns = new HashMap<String, String>();

        readObject = dao.readObjects(sql1, queryParams);
        queryParamsIns.put("vid", ((Map) readObject.get(0)).get("vId").toString());

        readObject = dao.readObjects(sql2, queryParams);
        queryParamsIns.put("uid", ((Map) readObject.get(0)).get("authorId").toString());

        readObject = dao.readObjects(sql3, queryParamsIns);
        queryParamsIns.put("authorID", request.getRequest_data_videoAdmission().getAuthorId());
        queryParamsIns.put("type", "likeDiscussionVideoAdmission");
        queryParamsIns.put("title", "Like discussion video admission");
        queryParamsIns.put("notification", "liked your discussion video admission");

        return queryParamsIns;
    }

    public Map<String, String> getParamLikeCommentEssay(final String sql1, final String sql2, final String sql3,
            final Map<String, String> queryParams, final RequestData request) {

        List<Object> readObject = null;
        Map<String, String> queryParamsIns = new HashMap<String, String>();

        readObject = dao.readObjects(sql1, queryParams);
        queryParamsIns.put("essayId", ((Map) readObject.get(0)).get("eid").toString());

        readObject = dao.readObjects(sql2, queryParams);
        queryParamsIns.put("uid", ((Map) readObject.get(0)).get("authorId").toString());

        readObject = dao.readObjects(sql3, queryParamsIns);
        queryParamsIns.put("authorID", request.getRequest_data().getAuthorID());
        queryParamsIns.put("type", "likeDiscussionEssay");
        queryParamsIns.put("title", "Like discussion essay");
        queryParamsIns.put("notification", "liked your discussion essay");

        return queryParamsIns;
    }

}
