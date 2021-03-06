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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.ManageSubjectModel;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.SubjectsService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.SibConstants;

/**
 *
 * {@link SubjectsService}
 * 
 * @author hungpd
 * @version 1.0
 */
@RestController
@RequestMapping("/siblinks/services/subjects")
public class SubjectsServiceImpl implements SubjectsService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ObjectDao dao;

    @Autowired
    private HttpServletRequest context;

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/listOfSubjects", method = RequestMethod.GET)
    public ResponseEntity<Response> listOfSubjects(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            Object[] queryParams = {};

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SUBJECTS_READ, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/listOfSubjectsWithTag", method = RequestMethod.POST)
    public ResponseEntity<Response> listOfSubjectsWithTag(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            Object[] queryParams = {};

            List<List<Object>> resParentObject = new ArrayList<List<Object>>();
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SUBJECTS_READ, queryParams);
            if (!CollectionUtils.isEmpty(readObject)) {
                for (Object object : readObject) {
                    List<Object> resChildObject = new ArrayList<Object>();
                    Map<String, Object> map1 = (Map) object;
                    String sid = map1.get(Parameters.SUBJECT_ID) + "";
                    Object[] queryParamsSid = { sid };
                    List<Object> readObject1 = dao.readObjects(SibConstants.SqlMapper.SQL_SUBJECT_GET_TAGS, queryParamsSid);

                    Map<String, Object> tags = null;
                    if (!CollectionUtils.isEmpty(readObject1)) {
                        for (Object obj : readObject1) {
                            tags = (Map) obj;
                            Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pairs = it.next();
                                if (pairs.getKey().equals("sid")) {
                                    it.remove();
                                }
                            }
                        }
                    }

                    Map<String, Object> mymap = new HashMap<String, Object>();
                    mymap.put("tags", readObject1);
                    resChildObject.add(map1);
                    resChildObject.add(mymap);
                    resParentObject.add(resChildObject);
                }
            }

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                resParentObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAllCategory", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllCategory() {
        SimpleResponse simpleResponse = null;
        try {
            Object[] queryParams = {};

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ALL_CATEGORY_TOPIC, queryParams);

            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "subjects", "getAllCategory", readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "subjects", "getAllCategory", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/fetchSubjects", method = RequestMethod.POST)
    public ResponseEntity<Response> fetchSubjects(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = {};

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapperBROT43.SQL_GET_SUBJECT, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/createSubject", method = RequestMethod.POST)
    public ResponseEntity<Response> createSubject(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }
            List<ManageSubjectModel> newSubjectDetails = new ArrayList<ManageSubjectModel>();
            ObjectMapper mapper = new ObjectMapper();
            try {
                newSubjectDetails = mapper.readValue(
                    request.getRequest_data().getStringJson(),
                    new TypeReference<List<ManageSubjectModel>>() {
                    });
            } catch (JsonParseException e) {
                logger.error(e);
            } catch (JsonMappingException e) {
                logger.error(e);
            } catch (IOException e) {
                logger.error(e);
            }

            String active = null;
            boolean flag = true;
            if (!CollectionUtils.isEmpty(newSubjectDetails)) {
                for (ManageSubjectModel manageSubjectModel : newSubjectDetails) {

                    if (null != manageSubjectModel.getStatus()) {
                        if (manageSubjectModel.getStatus().equalsIgnoreCase("1")) {
                            active = "Y";
                        } else {
                            active = "N";
                        }
                    }
                    Object[] queryParams = { manageSubjectModel.getName(), manageSubjectModel.getDescription(), new Date(), active, "1", "1" };

                    boolean insertFlag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SUBJECT_DATA_INSERT, queryParams);
                    if (!insertFlag) {
                        flag = false;
                        break;
                    }
                }
            }

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                flag);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/deleteSubject", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteSubject(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }
            List<ManageSubjectModel> newSubjectDetails = null;
            ObjectMapper mapper = new ObjectMapper();
            try {
                newSubjectDetails = mapper.readValue(
                    request.getRequest_data().getStringJson(),
                    new TypeReference<List<ManageSubjectModel>>() {
                    });
            } catch (JsonParseException e) {
                logger.error(e);
            } catch (JsonMappingException e) {
                logger.error(e);
            } catch (IOException e) {
                logger.error(e);
            }

            boolean flag = true;
            if (!CollectionUtils.isEmpty(newSubjectDetails)) {
                for (ManageSubjectModel manageSubjectModel : newSubjectDetails) {
                    Object[] queryParams = { "" + manageSubjectModel.getId() };
                    boolean insertFlag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SUBJECT_DATA_DELETE, queryParams);
                    if (!insertFlag) {
                        flag = false;
                        break;
                    }
                }
            }

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                flag);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/listOfTopics", method = RequestMethod.POST)
    public ResponseEntity<Response> listOfTopics(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = { request.getRequest_data().getSubjectId() };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_SUBJECT, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/listOfSubTopicsPn", method = RequestMethod.POST)
    public ResponseEntity<Response> listOfSubTopicsPn(@RequestBody final RequestData request) {

        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            CommonUtil util = CommonUtil.getInstance();

            Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());
            // Object[] queryParams = new HashMap<String, String>();
            //
            // queryParams.put("subjectId",
            // request.getRequest_data().getSubjectId());
            // queryParams.put("cid", request.getRequest_data().getCid());
            // queryParams.put("from", map.get("from"));
            // queryParams.put("to", map.get("to"));
            //
            Object[] params = { request.getRequest_data().getSubjectId(), request.getRequest_data().getCid(), map
                .get(Parameters.FROM), map.get(Parameters.TO) };
            // System.out.println("queryParams=="+queryParams);
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_SUB_TOPIC, params);
            String count = null;
            if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
                count = dao.getCount(SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_TOPICS_COUNT, params);
            }

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject,
                                                count);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

}
