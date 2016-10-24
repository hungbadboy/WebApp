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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.Notification.Helper.FireBaseNotification;
import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.PostService;
import com.siblinks.ws.service.UserService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.RandomString;
import com.siblinks.ws.util.SibConstants;
import com.siblinks.ws.util.StringUtil;

/**
 * {@link PostService}
 *
 * @author hungpd
 * @version 1.0
 */
@RestController
@RequestMapping("/siblinks/services/post")
public class PostServiceImpl implements PostService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private HttpServletRequest context;

    @Autowired
    ObjectDao dao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private Environment environment;

    @Autowired
    private UserService userservice;

    @Autowired
    private FireBaseNotification fireBaseNotification;

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/createPost", method = RequestMethod.POST)
    public ResponseEntity<Response> createPost(@RequestParam("content") final String content,
            @RequestParam("userId") final String userId, @RequestParam("file") final MultipartFile[] files,
            @RequestParam("subjectId") final String subjectId) {
        SimpleResponse simpleResponse = null;
        MultipartFile file = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                // Return authentication
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }
            long id = 0l;
            String error = validateFileImage(files);
            if (!StringUtil.isNull(error)) {
                // Return is not exist image
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "post", "createPost", error);
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
            }
            // Get Image files
            String filePath = "";
            String pathget = environment.getProperty("directoryGetImageQuestion");
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    file = files[i];
                    filePath += pathget + uploadFile(file, "directoryImageQuestion") + ".png";
                    if (i < files.length - 1) {
                        filePath += ";";
                    }
                }
            }

            // Insert question
            id = dao.insertObject(
                SibConstants.SqlMapper.SQL_CREATE_QUESTION,
                new Object[] { userId, subjectId, content, filePath });
            simpleResponse = new SimpleResponse("" + (id > 0), "post", "createPost", id);

        } catch (IOException | DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "post", "createPost", e.getMessage());
        }

        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/createAnswer", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> createAnswer(@RequestParam("pid") final String pid,
            @RequestParam("content") final String content, @RequestParam("studentId") final String studentId,
            @RequestParam("mentorId") final String mentorId, @RequestParam("file") final MultipartFile[] files,
            @RequestParam("subjectId") final String subjectId) {

        SimpleResponse simpleResponse = null;
        TransactionStatus statusBD = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            boolean status = false;
            long id = 0l;
            String error = validateFileImage(files);
            if (!StringUtil.isNull(error)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "post", "createAnswer", error);
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
            }
            TransactionDefinition def = new DefaultTransactionDefinition();
            statusBD = transactionManager.getTransaction(def);
            MultipartFile file = null;
            String filePath = "";
            String pathget = environment.getProperty("directoryGetImageAnswer");
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    file = files[i];
                    filePath += pathget + uploadFile(file, "directoryImageAnswer") + ".png";
                    if (i < files.length - 1) {
                        filePath += ";";
                    }
                }
            }

            Object[] queryParamsAnswer = { pid, mentorId, content, filePath };
            String contentNofi = content;
            if (!StringUtil.isNull(content) && content.length() > Parameters.MAX_LENGTH_TO_NOFICATION) {
                contentNofi = content.substring(0, Parameters.MAX_LENGTH_TO_NOFICATION);
            }
            id = dao.insertObject(SibConstants.SqlMapper.SQL_CREATE_ANSWER, queryParamsAnswer);
            Object[] queryParams = { mentorId, studentId, "answerQuestion", SibConstants.NOTIFICATION_TITLE_ANSWER_QUESTION, "answered a question: " +
                                                                                                                             contentNofi, subjectId, pid };
            dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_NUMREPLIES_QUESTION, new Object[] { pid });

            status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_QUESTION, queryParams);
            // Push message notification
            String toTokenId = userservice.getTokenUser(studentId);
            if (!StringUtil.isNull(toTokenId)) {

                fireBaseNotification.sendMessage(
                    toTokenId,
                    SibConstants.NOTIFICATION_TITLE_ANSWER_QUESTION,
                    "1",
                    pid,
                    content,
                    SibConstants.NOTIFICATION_ICON,
                    SibConstants.NOTIFICATION_PRIPORITY_HIGH);
            }
            if (id > 0 && status == true) {
                status = true;
            }
            transactionManager.commit(statusBD);
            logger.info("Insert Menu success " + new Date());
            simpleResponse = new SimpleResponse("" + status, "POST", "createAnswer", status);
        } catch (NullPointerException | NumberFormatException | DAOException | FileNotFoundException e) {
            transactionManager.rollback(statusBD);
            logger.info("Create answer Error:" + e.getMessage());
            simpleResponse = new SimpleResponse(e.getMessage(), "POST", "createAnswer", e.getMessage());
        }

        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/editPost", method = RequestMethod.POST)
    public ResponseEntity<Response> editPost(@RequestParam("content") final String content,
            @RequestParam("qid") final String qid, @RequestParam("file") final MultipartFile[] files,
            @RequestParam("subjectId") final String subjectId,
            @RequestParam("oldImagePathEdited") final String oldImagePathEdited,
            @RequestParam("oldImagePath") final String oldImagePath) {

        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }
            String error = validateFileImage(files);
            if (!StringUtil.isNull(error)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "post", "createPost", error);
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
            }
            MultipartFile file = null;
            String filePath = "";
            String filePathEdited = mergeImage(oldImagePath, oldImagePathEdited);
            // = getPathFile(oldImagePath,
            // oldImagePathEdited,"directoryImageQuestion");
            String maxLength = environment.getProperty("file.upload.image.length");

            if (files != null && files.length > 0) {
                if ((files.length + filePathEdited.split(";").length) > Integer.parseInt(maxLength)) {
                    error = "You only upload " + maxLength + " image";
                    Response reponse = new SimpleResponse("" + Boolean.FALSE, "post", "createPost", error);
                    ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
                    return entity;
                }
                String pathget = environment.getProperty("directoryGetImageQuestion");
                for (int i = 0; i < files.length; i++) {
                    file = files[i];
                    filePath += pathget + uploadFile(file, "directoryImageQuestion") + ".png";
                    if (i < files.length - 1) {
                        filePath += ";";
                    }
                }
            }

            // remove file when edit question
            removeFileEdit(oldImagePathEdited, "directoryImageQuestion");

            dao.insertUpdateObject(
                SibConstants.SqlMapper.SQL_POST_EDIT,
                new Object[] { fixFilePath(filePathEdited + filePath), content, subjectId, qid });
            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "post", "createPost", "");
        } catch (IOException | DAOException e1) {
            e1.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "EditPost", e1.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getPosts", method = RequestMethod.POST)
    public ResponseEntity<Response> getPosts(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            Map<String, String> queryParams = new HashMap<String, String>();

            List<Object> readObject = null;
            readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_POSTS, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "EditPost", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getPostInfo", method = RequestMethod.POST)
    public ResponseEntity<Response> getPostInfo(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {

            Object[] queryParams = new Object[] { request.getRequest_data().getPid() };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_POST_INFO, queryParams);

            List<Object> readObject1 = dao.readObjects(SibConstants.SqlMapper.SQL_POST_GET_TAGS, queryParams);

            Map<String, Object> tags = null;
            if (readObject1 != null) {
                for (Object obj : readObject1) {
                    tags = (Map) obj;
                    Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pairs = it.next();
                        if (pairs.getKey().equals("pid")) {
                            it.remove();
                        }
                    }
                }
            }

            Map<String, Object> mymap = new HashMap<String, Object>();
            mymap.put("tags", readObject1);

            readObject.add(mymap);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "getPostInfo", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getPostList", method = RequestMethod.POST)
    public ResponseEntity<Response> getPostList(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            Map<String, String> queryParams = new HashMap<String, String>();

            queryParams.put("subjectId", request.getRequest_data().getSubjectId());
            String sqlMapper = "";
            if (request.getRequest_data().getSubjectId().equals("0")) {
                sqlMapper = SibConstants.SqlMapper.SQL_GET_POST_LIST;
            } else {
                sqlMapper = SibConstants.SqlMapper.SQL_GET_POST_SUBID_LIST;
            }

            List<Object> readObject = null;
            readObject = dao.readObjects(sqlMapper, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "getPostInfo", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAllQuestions", method = RequestMethod.POST)
    public ResponseEntity<Response> getAllQuestions(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            List<Object> readObject = null;
            readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ALL_QUESTION, new Object[] {});

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "getPostInfo", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getPostById", method = RequestMethod.POST)
    public ResponseEntity<Response> getPostById(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        TransactionStatus statusBD = null;
        try {
            Object[] queryParams = { request.getRequest_data().getPid() };
            TransactionDefinition def = new DefaultTransactionDefinition();
            statusBD = transactionManager.getTransaction(def);
            dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_VIEW_POST, queryParams);
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_POST_BY_ID, queryParams);
            transactionManager.commit(statusBD);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (Exception e) {
            e.printStackTrace();
            if (statusBD != null) {
                transactionManager.rollback(statusBD);
            }
            logger.info("getPostById:" + e.getMessage());

            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "getPostById", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAnswerById", method = RequestMethod.POST)
    public ResponseEntity<Response> getAnswerById(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            Map<String, String> queryParams = new HashMap<String, String>();

            queryParams.put("pid", request.getRequest_data().getPid());
            List<Object> readObject = null;
            readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_POST_ANSWER_LASTEST, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "getPostById", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAnswerList", method = RequestMethod.POST)
    public ResponseEntity<Response> getAnswerList(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            Map<String, String> queryParams = new HashMap<String, String>();

            queryParams.put("pid", request.getRequest_data().getPid());
            String sqlMapper = "";
            if (request.getRequest_data().getPid().equals("0")) {
                sqlMapper = SibConstants.SqlMapper.SQL_GET_POST_ANSWER_LIST;
            } else {
                sqlMapper = SibConstants.SqlMapper.SQL_GET_POST_ANSWER_SUBID_LIST;
            }

            List<Object> readObject = null;
            readObject = dao.readObjects(sqlMapper, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "getPostById", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getPostListPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getPostListPN(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            CommonUtil util = CommonUtil.getInstance();

            Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

            Map<String, String> queryParams = new HashMap<String, String>();

            queryParams.put("authorId", request.getRequest_data().getUid());
            queryParams.put("subjectId", request.getRequest_data().getSubjectId());
            queryParams.put("tId", request.getRequest_data().getTopicId());
            queryParams.put("from", map.get("from"));
            queryParams.put("to", map.get("to"));

            List<Object> readObject = null;
            if (request.getRequest_data().getSubjectId().equals("0")) {
                readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_POST_LIST_PN, queryParams);
            } else {
                readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_POST_LIST_SUBID_PN, new Object[] { request
                    .getRequest_data()
                    .getSubjectId(), request.getRequest_data().getTopicId(), map.get(Parameters.FROM), map.get(Parameters.TO) });

            }
            List<List<Object>> resParentObject = new ArrayList<List<Object>>();

            if (readObject != null) {
                for (Object object : readObject) {
                    List<Object> resChildObject = new ArrayList<Object>();
                    Map<String, Object> map1 = (Map) object;
                    String pid = (String) map1.get("pid");
                    queryParams.put("pid", pid);
                    List<Object> readObject1 = dao.readObjects(SibConstants.SqlMapper.SQL_POST_GET_TAGS, queryParams);

                    Map<String, Object> tags = null;

                    try {
                        if (readObject1 != null) {
                            for (Object obj : readObject1) {
                                tags = (Map) obj;
                                Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry pairs = it.next();
                                    if (pairs.getKey().equals("pid")) {
                                        it.remove();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Map<String, Object> map2 = (Map) object;
                    String pid1 = (String) map2.get("pid");
                    queryParams.put("pid", pid1);
                    List<Object> readObject2 = dao.readObjects(SibConstants.SqlMapper.SQL_ONE_ANSWER_FORUM, queryParams);

                    Map<String, Object> answer = null;

                    try {
                        if (readObject2 != null) {
                            for (Object obj : readObject2) {
                                answer = (Map) obj;
                                Iterator<Entry<String, Object>> it = answer.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry pairs = it.next();
                                    if (pairs.getKey().equals("pid")) {
                                        it.remove();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error(e);
                    }

                    Map<String, Object> mymap = new HashMap<String, Object>();
                    mymap.put("tags", readObject1);
                    Map<String, Object> mymap2 = new HashMap<String, Object>();
                    mymap2.put("answer", readObject2);
                    resChildObject.add(map1);
                    resChildObject.add(mymap2);
                    resChildObject.add(mymap);
                    resParentObject.add(resChildObject);
                }
            }

            String count = null;
            count = dao.getCount("GET_POST_LIST_PN_COUNT", new Object[] { request.getRequest_data().getSubjectId() });

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                resParentObject,
                                                count);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "getPostById", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getAnswerListPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getAnswerListPN(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            CommonUtil util = CommonUtil.getInstance();

            Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

            List<Object> readObject = null;
            if (request.getRequest_data().getPid().equals("0")) {
                readObject = dao.readObjects(
                    SibConstants.SqlMapper.SQL_GET_POST_ANSWER_LIST_PN,
                    new Object[] { Integer.parseInt(map.get("from")), Integer.parseInt(map.get("to")) });
            } else {
                readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_POST_ANSWER_LIST_SUBID_PN, new Object[] { request
                    .getRequest_data()
                    .getPid(), Integer.parseInt(map.get("from")), Integer.parseInt(map.get("to")) });
            }
            List<List<Object>> resParentObject = new ArrayList<List<Object>>();

            if (readObject != null) {
                for (Object object : readObject) {
                    List<Object> resChildObject = new ArrayList<Object>();
                    Map<String, Object> map1 = (Map) object;
                    String pid = "" + map1.get("pid");
                    List<Object> readObject1 = dao.readObjects(SibConstants.SqlMapper.SQL_POST_GET_TAGS, new Object[] { pid });

                    Map<String, Object> tags = null;

                    try {
                        if (readObject1 != null) {
                            for (Object obj : readObject1) {
                                tags = (Map) obj;
                                Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry pairs = it.next();
                                    if (pairs.getKey().equals("pid")) {
                                        it.remove();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error(e);
                    }

                    Map<String, Object> mymap = new HashMap<String, Object>();
                    mymap.put("tags", readObject1);
                    resChildObject.add(map1);
                    resChildObject.add(mymap);
                    resParentObject.add(resChildObject);
                }
            }

            String count = null;

            count = dao.getCount(SibConstants.SqlMapper.SQL_GET_POST_ANSWER_LIST_PN_COUNT, new Object[] { request
                .getRequest_data()
                .getPid() });

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                resParentObject,
                                                count);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "getPostById", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getPostListMobile", method = RequestMethod.GET)
    public ResponseEntity<Response> getPostListMobile(@RequestParam(value = "uid") final long id,
            @RequestParam final String limit, @RequestParam final String offset, @RequestParam final String orderType,
            @RequestParam final String subjectid) {
        SimpleResponse simpleResponse = null;
        try {
            Object[] queryParams = {};

            String whereClause = "";
            if (id != -1l) {
                whereClause += " AND U.userid = " + id;
            }
            if (Parameters.ANSWERED.equalsIgnoreCase(orderType)) {
                whereClause += " AND P.NUMREPLIES > 0";
            }
            if (Parameters.UNANSWERED.equalsIgnoreCase(orderType)) {
                whereClause += " AND P.NUMREPLIES = 0";
            }

            if (!"-1".equals(subjectid)) {
                whereClause += " AND P.subjectId=  " + subjectid;
            }

            whereClause += " ORDER BY P.TIMESTAMP DESC";

            if (!StringUtil.isNull(limit)) {
                whereClause += " LIMIT " + limit;
            }
            if (!StringUtil.isNull(offset)) {
                whereClause += " OFFSET " + offset;
            }
            List<Object> readObject = dao.readObjectsWhereClause(
                SibConstants.SqlMapper.SQL_GET_STUDENT_POSTED,
                whereClause,
                queryParams);

            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "GET", "getPostListMobile", readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "getPostListMobile", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/removePost", method = RequestMethod.POST)
    public ResponseEntity<Response> removePost(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = { request.getRequest_data().getPid() };
            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_REMOVE_POST, queryParams);

            if (status) {
                dao.insertUpdateObject(SibConstants.SqlMapper.SQL_REMOVE_ANSWER_BY_POST, queryParams);
            }
            simpleResponse = new SimpleResponse(
                                                "" + status,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                status);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "removePost", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/searchPosts", method = RequestMethod.POST)
    public ResponseEntity<Response> searchPosts(@RequestBody final RequestData request) {

        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            Map<String, String> queryParams = new HashMap<String, String>();

            String value = request.getRequest_data().getContent().trim();
            queryParams.put("content", value);
            queryParams.put("content1", value);

            List<List<Object>> resParentObject = new ArrayList<List<Object>>();

            List<Object> readObject = null;
            readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SEARCH_POST, queryParams);
            if (readObject != null) {
                for (Object object : readObject) {
                    List<Object> resChildObject = new ArrayList<Object>();
                    Map<String, Object> map = (Map) object;
                    String pid = (String) map.get("pid");
                    queryParams.put("pid", pid);
                    List<Object> readObject1 = dao.readObjects(SibConstants.SqlMapper.SQL_POST_GET_TAGS, queryParams);

                    Map<String, Object> tags = null;

                    try {
                        if (readObject1 != null) {
                            for (Object obj : readObject1) {
                                tags = (Map) obj;
                                Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry pairs = it.next();
                                    if (pairs.getKey().equals("pid")) {
                                        it.remove();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error(e);
                    }

                    Map<String, Object> mymap = new HashMap<String, Object>();
                    mymap.put("tags", readObject1);
                    resChildObject.add(map);
                    resChildObject.add(mymap);
                    resParentObject.add(resChildObject);
                }
            }

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "removePost", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/editAnswer", method = RequestMethod.POST)
    public ResponseEntity<Response> editAnswer(@RequestParam("content") final String content,
            @RequestParam("aid") final String aid, @RequestParam("file") final MultipartFile[] files,
            @RequestParam("oldImagePathEdited") final String oldImagePathEdited,
            @RequestParam("oldImagePath") final String oldImagePath) {

        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            String error = validateFileImage(files);
            if (!StringUtil.isNull(error)) {
                simpleResponse = new SimpleResponse("" + Boolean.FALSE, "post", "editAnswer", error);
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
            }
            boolean status = true;
            MultipartFile file;
            String filePath = "";
            String filePathEdited = mergeImage(oldImagePath, oldImagePathEdited);
            String maxLength = environment.getProperty("file.upload.image.length");

            if (files != null && files.length > 0) {
                if ((files.length + filePathEdited.split(";").length) > Integer.parseInt(maxLength)) {
                    error = "You only upload " + maxLength + " image";
                    Response reponse = new SimpleResponse(SibConstants.FAILURE, "post", "editAnswer", error);
                    return new ResponseEntity<Response>(reponse, HttpStatus.OK);
                }
                String pathget = environment.getProperty("directoryGetImageAnswer");
                for (int i = 0; i < files.length; i++) {
                    file = files[i];
                    filePath += pathget + uploadFile(file, "directoryImageAnswer") + ".png";
                    if (i < files.length - 1) {
                        filePath += ";";
                    }
                }
            }

            // remove file when edit question
            removeFileEdit(oldImagePathEdited, "directoryImageAnswer");

            dao.insertUpdateObject(SibConstants.SqlMapper.SQL_ANSWER_EDIT, new Object[] { content, fixFilePath(filePathEdited +
                                                                                                               filePath), aid });
            simpleResponse = new SimpleResponse("" + status, "Post", "editAnswer", "");

        } catch (FileNotFoundException | DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "editAnswer", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/removeAnswer", method = RequestMethod.POST)
    public ResponseEntity<Response> removeAnswer(@RequestBody final RequestData request) {

        SimpleResponse simpleResponse = null;
        TransactionStatus statusDB = null;
        boolean status = true;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = { request.getRequest_data().getAid() };
            TransactionDefinition def = new DefaultTransactionDefinition();
            statusDB = transactionManager.getTransaction(def);

            status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_NUM_REPLIES_UPDATE_DELETE, queryParams);

            status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_REMOVE_ANSWER, queryParams);
            dao.insertUpdateObject(SibConstants.SqlMapper.SQL_REMOVE_ANSWER_LIKE, queryParams);

            transactionManager.commit(statusDB);
            simpleResponse = new SimpleResponse(
                                                "" + status,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                request.getRequest_data().getAid());
        } catch (DAOException e) {
            transactionManager.rollback(statusDB);
            logger.info("Delete answer Error:" + e.getMessage());
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "editAnswer", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateViewQuestion", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> updateViewQuestion(@RequestBody final RequestData request) {

        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = { request.getRequest_data().getPid() };

            boolean status = true;
            status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_VIEW_POST, queryParams);
            String message = "";
            if (status) {
                message = "Done";
            } else {
                message = "Fail";
            }

            simpleResponse = new SimpleResponse(
                                                "" + status,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                message);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "updateViewQuestion", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * This is method get newest question by user id
     */
    @RequestMapping(value = "/getStudentPosted", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public ResponseEntity<Response> getStudentPosted(@RequestParam(value = "uid") final long id,
            @RequestParam final String limit, @RequestParam final String offset, @RequestParam final String orderType,
            @RequestParam final String oldQid, @RequestParam final String subjectid) {
        SimpleResponse simpleResponse = null;
        try {
            Object[] queryParams = {};
            Map<String, Object> result_data = new HashMap<String, Object>();

            String whereClause = "";

            if (id != -1l) {
                whereClause += " AND U.userid = " + id;
            }
            if (Parameters.ANSWERED.equalsIgnoreCase(orderType)) {
                whereClause += " AND P.NUMREPLIES > 0";
            }
            if (Parameters.UNANSWERED.equalsIgnoreCase(orderType)) {
                whereClause += " AND P.NUMREPLIES = 0";
            }

            // if (!"-1".equals(oldQid)) {
            // whereClause += " AND P.PID > " + oldQid;
            // }

            if (!"-1".equals(subjectid)) {
                whereClause += " AND P.subjectId=  " + subjectid;
            }

            whereClause += " ORDER BY P.TIMESTAMP DESC";

            if (!StringUtil.isNull(limit)) {
                whereClause += " LIMIT " + limit;
            }
            if (!StringUtil.isNull(offset)) {
                whereClause += " OFFSET " + offset;
            }
            List<Object> readObject = dao.readObjectsWhereClause(
                SibConstants.SqlMapper.SQL_GET_STUDENT_POSTED,
                whereClause,
                queryParams);
            if (!CollectionUtils.isEmpty(readObject)) {
                // Put list question
                result_data.put("question", readObject);

                int length = readObject.size();
                List<Object> readObjectAnswers = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    String result = readObject.get(i).toString();
                    String temp = result.substring(0, result.indexOf(","));
                    long _id = Long.parseLong(temp.substring(temp.lastIndexOf("=") + 1));
                    Object objAnwser = getAnswersList(id, _id, "", "");
                    if (objAnwser == null) {
                        readObjectAnswers.add(null);
                    } else {
                        readObjectAnswers.add(objAnwser);
                        result_data.put("answers", readObjectAnswers);
                    }
                }
            }
            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "posted", "getStudentPosted", result_data);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "getStudentPosted", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/countQuestions", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public ResponseEntity<Response> countQuestions(@RequestParam(value = "uid") final long id,
            @RequestParam final String orderType, @RequestParam final String subjectid) {
        SimpleResponse simpleResponse = null;
        try {
            Object[] queryParams = {};
            String whereClause = "";
            if (id != -1l) {
                whereClause += " AND P.authorID =  " + id;
            }
            if (Parameters.ANSWERED.equalsIgnoreCase(orderType)) {
                whereClause += " AND P.NUMREPLIES > 0";
            }
            if (Parameters.UNANSWERED.equalsIgnoreCase(orderType)) {
                whereClause += " AND P.NUMREPLIES = 0";
            }
            if (!"-1".equalsIgnoreCase(subjectid)) {
                whereClause += " AND P.subjectId = " + subjectid;
            }

            List<Object> readObject = dao.readObjectsWhereClause(
                SibConstants.SqlMapper.SQL_GET_STUDENT_POSTED_COUNT,
                whereClause,
                queryParams);

            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "get", "countQuestions", readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "countQuestions", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getNewestQuestion")
    @ResponseBody
    public ResponseEntity<Response> getNewestQuestion(@RequestParam(value = "page") final String from, @RequestParam(
            value = "limit") final String limit) {
        SimpleResponse simpleResponse = null;
        try {
            CommonUtil util = CommonUtil.getInstance();
            Map<String, String> limitSelect = util.getLimit(from, limit);

            Object[] queryParams = { Integer.parseInt(limitSelect.get("from")), Integer.parseInt(limitSelect.get("to")) };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_NEWEST_QUESTION, queryParams);
            String count = String.valueOf(readObject.size());
            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "get", "getNewestQuestion", readObject, count);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "getNewestQuestion", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    public Object getAnswersList(final long uid, final long id, final String limit, final String offset) {
        SimpleResponse simpleResponse = null;
        try {

            String whereClause = "";
            Object[] queryParams = { uid, id };
            whereClause += " ORDER BY X.TIMESTAMP DESC";

            if (!StringUtil.isNull(limit)) {
                whereClause += " LIMIT " + limit;
            }
            if (!StringUtil.isNull(offset)) {
                whereClause += " OFFSET " + offset;
            }

            List<Object> readObject = dao.readObjectsWhereClause(
                SibConstants.SqlMapper.SQL_GET_ANSWER_BY_QID,
                whereClause,
                queryParams);

            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "get answer", "getAnswersList", readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "getAnswersList", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getNewestQuestionBySubject")
    @ResponseBody
    public ResponseEntity<Response> getNewestQuestionBySubject(@RequestParam final long uid, @RequestParam final String limit,
            @RequestParam final String offset) {
        SimpleResponse simpleResponse = null;
        try {
            Object[] paramQueryGetSubjects = { uid };
            String subjectIdResult = dao
                .readObjects(SibConstants.SqlMapper.SQL_GET_SUBJECT_REG, paramQueryGetSubjects)
                .toString();

            String subjects = subjectIdResult.substring(subjectIdResult.indexOf("=") + 1, subjectIdResult.lastIndexOf("}"));

            CommonUtil cmUtils = CommonUtil.getInstance();

            Map<String, String> pageLimit = cmUtils.getOffset(limit, offset);

            Object[] params = { uid, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };

            String whereClause = "WHERE P.subjectId IN (" + subjects + ") ORDER BY P.timeStamp DESC LIMIT ? OFFSET ?;";

            List<Object> readObjectTemp = dao.readObjectsWhereClause(
                SibConstants.SqlMapper.SQL_GET_NEWEST_QUESTION_SUBJECT,
                whereClause,
                params);
            String count = String.valueOf(readObjectTemp.size());
            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "GET", "getNewestQuestionBySubject", readObjectTemp, count);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "getNewestQuestionBySubject", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     *
     * @param uploadfile
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    private String uploadFile(final MultipartFile uploadfile, final String path) throws FileNotFoundException {

        String filename = "";
        BufferedOutputStream stream = null;
        try {
            String directory = environment.getProperty(path);
            if (directory != null) {
                RandomString randomName = new RandomString();
                Date date = new Date();
                filename = randomName.random() + date.getTime();

                String filepath = Paths.get(directory, filename + "." + "png").toString();
                // Save the file locally
                File file = new File(filepath);

                File parentDir = file.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                stream = new BufferedOutputStream(new FileOutputStream(file));
                stream.write(uploadfile.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                // Do nothing
            }
        }
        return filename;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAnswerByQid", method = RequestMethod.POST)
    public ResponseEntity<Response> getAnswerByQid(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            String limit = request.getRequest_data().getLimit();
            String offset = request.getRequest_data().getOffset();
            String qid = request.getRequest_data().getPid();
            String type = request.getRequest_data().getType();
            String uid = request.getRequest_data().getUid();
            Object[] queryParams = { uid, qid };

            String entityName = SibConstants.SqlMapper.SQL_GET_ANSWER_BY_QID;

            String whereClause = "";
            if (Parameters.NEWEST.equals(type)) {
                whereClause += " ORDER BY X.TIMESTAMP DESC";
            } else if (Parameters.LIKE.equals(type)) {
                whereClause += " ORDER BY X.numlike DESC";
            } else {// oldest

                whereClause += " ORDER BY X.TIMESTAMP  ASC";
            }
            if (!StringUtil.isNull(limit)) {
                whereClause += " LIMIT " + limit;
            }
            if (!StringUtil.isNull(offset)) {
                whereClause += " OFFSET " + offset;
            }

            List<Object> readObject = dao.readObjectsWhereClause(entityName, whereClause, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Post", "getAnswerByQid", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     *
     * @param filename
     * @param path
     */
    // delele file
    private void delefile(final String filename, final String path) {
        String directory = environment.getProperty(path);
        String name = FilenameUtils.getName(filename);
        File file = new File(Paths.get(directory, name).toString());
        file.delete();
    }

    /**
     *
     * @param oldImagePathEdited
     * @param path
     */
    // clean file after edit image
    private void removeFileEdit(final String oldImagePathEdited, final String path) {
        if (!StringUtil.isNull(oldImagePathEdited)) {
            for (String str : oldImagePathEdited.split(";")) {
                delefile(str, path);
            }
        }
    }

    /**
     * merge Image whene edit question
     *
     * @param oldImagePath
     * @param oldImagePathEdited
     * @return
     */
    private String mergeImage(final String oldImagePath, final String oldImagePathEdited) {
        String result = "";
        if (StringUtil.isNull(oldImagePathEdited)) {
            if (!StringUtil.isNull(oldImagePath)) {
                result = oldImagePath;
            }
        } else {
            for (String str : oldImagePath.split(";")) {
                if (!StringUtil.isNull(oldImagePath)) {
                    if (!oldImagePathEdited.contains(str)) {
                        result += str + ";";
                    }
                }
            }
        }
        return result + ";";
    }

    /**
     *
     * @param str
     * @return
     */
    private String fixFilePath(final String str) {
        String result = "";
        if (StringUtil.isNull(str)) {
            return "";
        }
        if (";".equals(str.trim())) {
            return "";
        }
        String path = "";
        for (int i = 0; i < str.split(";").length; i++) {
            path = str.split(";")[i];
            if (!StringUtil.isNull(path)) {
                result += path;
                if (i < str.split(";").length - 1) {
                    result += ";";
                }
            }
        }
        return result;
    }

    /**
     * This method is validate image file type, size, length
     *
     * @param files
     *            These are file upload image
     * @return Message error
     */
    private String validateFileImage(final MultipartFile[] files) {
        String error = "";
        String name = "";
        String sample = environment.getProperty("file.upload.image.type");
        String limitSize = environment.getProperty("file.upload.image.size");
        String maxLength = environment.getProperty("file.upload.image.length");
        long totalSize = 0;
        if (files != null && files.length > 0) {
            if (files.length > Integer.parseInt(maxLength)) {
                return "You only upload " + maxLength + " image";
            }
            for (MultipartFile file : files) {
                name = file.getOriginalFilename();
                if (!StringUtil.isNull(name)) {
                    String nameExt = FilenameUtils.getExtension(name.toLowerCase());
                    boolean status = sample.contains(nameExt);
                    if (!status) {
                        return "Error Format";
                    }
                }
                totalSize += file.getSize();
            }
            if (totalSize > Long.parseLong(limitSize)) {
            }
        }
        return error;
    }
}
