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
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.siblinks.ws.common.LoggedInChecker;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.ManageVideoModel;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.model.SubCategoryModel;
import com.siblinks.ws.model.Tag;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.VideoService;
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
@RequestMapping("/siblinks/services/video")
public class VideoServiceImpl implements VideoService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ObjectDao dao;

    private final LoggedInChecker loggedInChecker;

    @Autowired
    VideoServiceImpl(final LoggedInChecker loggedInChecker) {
        this.loggedInChecker = loggedInChecker;
    }

    @Override
    @RequestMapping(value = "/getSubCategoryData", method = RequestMethod.POST)
    public ResponseEntity<Response> getSubCategoryData(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("subjectid", request.getRequest_data().getSubjectId());
        queryParams.put("categoryid", request.getRequest_data().getCid());

        String entityName = SibConstants.SqlMapper.SQL_VIDEO_SUBCATAGERY_READ;

        List<Object> readObject = null;
        readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/saveSubCategory", method = RequestMethod.POST)
    public ResponseEntity<Response> saveSubCategory(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        SubCategoryModel subCategoryModel = new SubCategoryModel();

        try {
            ObjectMapper mapper = new ObjectMapper();

            subCategoryModel = mapper.readValue(request.getRequest_data().getStringJson(), SubCategoryModel.class);

        } catch (JsonParseException e) {
            logger.error(e);
        } catch (JsonMappingException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }

        int vid = 0;
        boolean status = false;
        if (null != subCategoryModel) {
            Map<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("title", subCategoryModel.getTitle());
            queryParams.put("author", "siblinks@siblinks.com");
            queryParams.put("authorID", "6");
            queryParams.put("subject", subCategoryModel.getSubject());
            queryParams.put("topic", subCategoryModel.getSubject_category_name());
            queryParams.put("subTopic", subCategoryModel.getSubject_sub_category_name());
            queryParams.put("description", subCategoryModel.getDescription());
            queryParams.put("image", subCategoryModel.getImage());
            queryParams.put("url", subCategoryModel.getVideo_link());
            queryParams.put("videoEnable", subCategoryModel.getActive());
            queryParams.put("topicId", "" + subCategoryModel.getSubject_category_id());
            queryParams.put("subjectId", "" + subCategoryModel.getSubjectid());
            queryParams.put("runningtime", "4:50");
            queryParams.put("timeStamp", "now()");
            String entityName = SibConstants.SqlMapper.SQL_SIB_INSERT_VIDEO;
            status = dao.insertUpdateObject(entityName, queryParams);
            boolean tagStatus = true;
            String msg = "success";
            Map<String, String> queryParams1 = null;
            if (status) {
                entityName = SibConstants.SqlMapper.SQL_SIB_GET_VID;

                queryParams1 = new HashMap<String, String>();
                queryParams1.put("subjectId", "" + subCategoryModel.getSubjectid());
                queryParams1.put("topicId", "" + subCategoryModel.getSubject_category_id());
                queryParams1.put("subTopic", subCategoryModel.getSubject_sub_category_name());

                List<Object> readObject = dao.readObjects(entityName, queryParams1);
                List<Tag> tags = new ArrayList<Tag>();
                Tag tag0 = new Tag();
                tag0.setTag(subCategoryModel.getSubject());
                tags.add(tag0);

                Tag tag1 = new Tag();
                tag1.setTag(subCategoryModel.getSubject_category_name());
                tags.add(tag1);

                Tag tag2 = new Tag();
                tag2.setTag(subCategoryModel.getSubject_sub_category_name());
                tags.add(tag2);

                for (Tag tag : tags) {
                    entityName = SibConstants.SqlMapper.SQL_SIB_INSERT_TAG;
                    queryParams1 = new HashMap<String, String>();
                    queryParams1.put("vid", ((Map) readObject.get(0)).get("vid").toString());
                    queryParams1.put("tag", tag.getTag());
                    boolean flag = dao.insertUpdateObject(entityName, queryParams1);
                    if (!flag) {
                        tagStatus = false;
                        break;
                    }
                }
            }

            vid = Integer.valueOf(queryParams1.get("vid"));
            if (!status) {
                msg = "Failed to insert video";
                vid = 0;
            } else if (!tagStatus) {
                msg = "Failed to insert tags";
                vid = 0;
            }
        }

        SimpleResponse reponse = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), vid);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateSubCategory", method = RequestMethod.POST)
    public ResponseEntity<Response> updateSubCategory(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        ObjectMapper mapper = new ObjectMapper();

        SubCategoryModel subCategoryModel = new SubCategoryModel();

        try {
            subCategoryModel = mapper.readValue(request.getRequest_data().getStringJson(), SubCategoryModel.class);

        } catch (JsonParseException e) {
            logger.error(e);
        } catch (JsonMappingException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }

        String entityName = SibConstants.SqlMapper.SQL_SUBJECT_SUB_CATEGORY_UPDATE;

        boolean status = false;
        if (null != subCategoryModel) {
            Map<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("title", subCategoryModel.getTitle());
            queryParams.put("subTopic", subCategoryModel.getSubject_sub_category_name());
            queryParams.put("description", subCategoryModel.getDescription());
            queryParams.put("image", subCategoryModel.getImage());
            queryParams.put("url", subCategoryModel.getVideo_link());
            queryParams.put("videoEnable", subCategoryModel.getActive());
            queryParams.put("vid", subCategoryModel.getVid());
            status = dao.insertUpdateObject(entityName, queryParams);
        }

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), status);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/deleteSubCategory", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteSubCategory(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        String entityName = SibConstants.SqlMapper.SQL_SUBJECT_SUB_CATEGORY_DELETE;

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("vid", request.getRequest_data().getStringJson());
        boolean flag = dao.insertUpdateObject(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), flag);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getVideoDetails", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoDetails(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("subject_id", request.getRequest_data().getStringJson());

        String entityName = SibConstants.SqlMapper.SQL_VIDEO_SUBJECT_MAPPING_DATA_READ;

        List<Object> readObject = null;
        readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getMentorsOfVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> getMentorsOfVideo(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("subjectId", request.getRequest_data().getSubjectId());

        String entityName = SibConstants.SqlMapper.SQL_SIB_VIDEO_MENTORS_READ;

        List<Object> readObject = null;
        readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getMentorReviewsPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getMentorReviewsPN(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Object[] queryParams = { request.getRequest_data().getUid(), map.get(Parameters.FROM), map.get(Parameters.TO) };
        String entityName = SibConstants.SqlMapper.SQL_SIB_MENTOR_REVIEWS_READ;

        List<Object> readObject = null;
        readObject = dao.readObjects(entityName, queryParams);

        String count = null;
        if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
            count = dao.getCount(SibConstants.SqlMapper.SQL_SIB_MENTOR_REVIEWS_READ_COUNT, queryParams);
        }

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getVideoDetailsPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoDetailsPN(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());
        Object[] queryParams = { request.getRequest_data().getStringJson(), map.get(Parameters.FROM), map.get(Parameters.TO) };

        String entityName = SibConstants.SqlMapper.SQL_VIDEO_SUBJECT_MAPPING_DATA_READ_PN;

        List<Object> readObject = null;
        readObject = dao.readObjects(entityName, queryParams);
        String count = null;
        if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
            count = dao.getCount(SibConstants.SqlMapper.SQL_VIDEO_SUBJECT_MAPPING_DATA_READ_PN_COUNT, queryParams);
        }

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateVideoDetails", method = RequestMethod.POST)
    public ResponseEntity<Response> updateVideoDetails(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        ObjectMapper mapper = new ObjectMapper();
        ManageVideoModel manageVideoModel = null;
        try {
            manageVideoModel = mapper.readValue(request.getRequest_data().getStringJson(), ManageVideoModel.class);
        } catch (JsonParseException e) {
            logger.error(e);
        } catch (JsonMappingException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }

        String entityName = SibConstants.SqlMapper.SQL_VIDEO_SUBJECT_MAPPING_DATA_UPDATE;
        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("subject_category_id", "" + manageVideoModel.getSubject_category_id());
        queryParams.put("subject_category_name", manageVideoModel.getSubject_category_name());
        queryParams.put("description", manageVideoModel.getDescription());
        queryParams.put("active", manageVideoModel.getActive());
        boolean insertFlag = dao.insertUpdateObject(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), insertFlag);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    @RequestMapping(value = "/deleteVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteVideo(@RequestBody final RequestData request) {

        // if (!AuthenticationFilter.isAuthed(context)) {
        // ResponseEntity<Response> entity = new ResponseEntity<Response>(
        // new SimpleResponse(
        // "" + false,
        // "Authentication required."),
        // HttpStatus.FORBIDDEN);
        // return entity;
        // }

        // String entityName =
        // SibConstants.SqlMapper.SQL_VIDEO_SUBJECT_MAPPING_DATA_DELETE;
        //
        // Map<String, String> queryParams = new HashMap<String, String>();
        // queryParams.put("subject_category_id",
        // request.getRequest_data().getStringJson());
        // boolean flag = dao.insertUpdateObject(entityName, queryParams);
        SimpleResponse reponse = null;
        String msg = "";
        String vid = request.getRequest_data().getVid();
        String authorId = request.getRequest_data().getAuthorID();

        if (vid != null && vid.length() > 0 && authorId != null && authorId.length() > 0) {
            boolean status = deleteVideo(vid, authorId);
            if (status) {
                msg = "Delete video success";
                reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), msg);
            } else {
                msg = "Delete video fail";
                reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), msg);
            }
        } else {
            msg = "Missing vid or authorId. Check it again.";
            reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), msg);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    private boolean deleteVideo(final String vid, final String authorId) {
        boolean flag = false;

        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Object[] queryParams = new Object[] { vid };

            // delete video from Video_Comment
            String entityName = SibConstants.SqlMapperBROT43.SQL_DELETE_VIDEO_COMMENT;
            dao.insertUpdateObject(entityName, queryParams);

            // delete video from Video_Like
            entityName = SibConstants.SqlMapperBROT43.SQL_DELETE_VIDEO_LIKE;
            dao.insertUpdateObject(entityName, queryParams);

            // delete video from Video_Notes
            entityName = SibConstants.SqlMapperBROT43.SQL_DELETE_VIDEO_NOTE;
            dao.insertUpdateObject(entityName, queryParams);

            // delete video from Video_Tag
            entityName = SibConstants.SqlMapperBROT43.SQL_DELETE_VIDEO_TAG;
            dao.insertUpdateObject(entityName, queryParams);

            // delete video from Video_Rating
            entityName = SibConstants.SqlMapperBROT43.SQL_DELETE_VIDEO_RATING;
            dao.insertUpdateObject(entityName, queryParams);

            // delete video from Video_Playlist
            entityName = SibConstants.SqlMapperBROT43.SQL_DELETE_VIDEO_PLAYLIST;
            dao.insertUpdateObject(entityName, queryParams);

            // delete video from Video_Subscribe
            entityName = SibConstants.SqlMapperBROT43.SQL_GET_STUDENT_SUBSCRIBE;
            queryParams = new Object[] { authorId };
            List<Object> readObject = dao.readObjects(entityName, queryParams);

            if (readObject != null && !readObject.isEmpty() && readObject.size() > 0) {
                entityName = SibConstants.SqlMapperBROT43.SQL_DELETE_VIDEO_SUBCRIBLE;
                for (Object object : readObject) {
                    JsonObject json = new JsonParser().parse(object.toString()).getAsJsonObject();
                    long studentId = json.get("studentid").getAsLong();
                    queryParams = new Object[] { vid + ",", authorId, studentId };
                    dao.insertUpdateObject(entityName, queryParams);
                }
            }

            // delete video from Video
            queryParams = new Object[] { vid };
            entityName = SibConstants.SqlMapperBROT43.SQL_DELETE_VIDEO;
            dao.insertUpdateObject(entityName, queryParams);

            transactionManager.commit(status);
            logger.info("Delete Video success " + new Date());
            flag = true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            flag = false;
            throw e;
        }
        return flag;
    }

    @Override
    @RequestMapping(value = "/createVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> createVideo(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        List<ManageVideoModel> newVideoDetails = new ArrayList<ManageVideoModel>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            newVideoDetails = mapper.readValue(request.getRequest_data().getStringJson(), new TypeReference<List<ManageVideoModel>>() {
            });
        } catch (JsonParseException e) {
            logger.error(e);
        } catch (JsonMappingException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }

        String entityName = SibConstants.SqlMapper.SQL_VIDEO_SUBJECT_MAPPING_DATA_INSERT;

        Map<String, String> queryParams = new HashMap<String, String>();
        String active = null;
        boolean flag = true;
        if (null != newVideoDetails && !newVideoDetails.isEmpty() && newVideoDetails.size() > 0) {
            for (ManageVideoModel manageVideoModel : newVideoDetails) {
                queryParams.put("subject_id", "" + manageVideoModel.getSubject_id());
                queryParams.put("subject_category_name", manageVideoModel.getSubject_category_name());
                queryParams.put("description", manageVideoModel.getDescription());
                if (null != manageVideoModel.getActive()) {
                    if (manageVideoModel.getActive().equalsIgnoreCase("1")) {
                        active = "Y";
                    } else {
                        active = "N";
                    }
                }
                queryParams.put("active", active);
                queryParams.put("createdBy", loggedInChecker.getLoggedInUser().getUserid());
                boolean insertFlag = dao.insertUpdateObject(entityName, queryParams);
                if (!insertFlag) {
                    flag = false;
                    break;
                }
            }
        }

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), flag);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/uploadVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> uploadVideo(@RequestBody final RequestData request) {
        String entityName = null;

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("title", request.getRequest_data().getTitle());
        queryParams.put("author", request.getRequest_data().getAuthor());
        queryParams.put("authorID", request.getRequest_data().getAuthorID());
        queryParams.put("subject", request.getRequest_data().getSubject());
        queryParams.put("topic", request.getRequest_data().getTopic());
        queryParams.put("subTopic", request.getRequest_data().getSubtopic());
        queryParams.put("description", request.getRequest_data().getDescription());
        queryParams.put("image", request.getRequest_data().getImage());
        queryParams.put("url", request.getRequest_data().getUrl());
        queryParams.put("runningtime", request.getRequest_data().getRunningTime());
        queryParams.put("timeStamp", "now()");

        entityName = SibConstants.SqlMapper.SQL_SIB_INSERT_VIDEO;
        boolean status = dao.insertUpdateObject(entityName, queryParams);
        boolean tagStatus = true;
        String msg = "success";
        Map<String, String> queryParams1 = null;
        if (status) {
            entityName = SibConstants.SqlMapper.SQL_SIB_GET_VID;
            queryParams1 = new HashMap<String, String>();
            queryParams1.put("authorID", request.getRequest_data().getAuthorID());
            queryParams1.put("url", request.getRequest_data().getUrl());

            List<Object> readObject = dao.readObjects(entityName, queryParams1);

            for (Tag tag : request.getRequest_data().getTags()) {
                entityName = SibConstants.SqlMapper.SQL_SIB_INSERT_TAG;
                queryParams1 = new HashMap<String, String>();
                queryParams1.put("vid", ((Map) readObject.get(0)).get("vid").toString());
                queryParams1.put("tag", tag.getTag());
                boolean flag = dao.insertUpdateObject(entityName, queryParams1);
                if (!flag) {
                    tagStatus = false;
                    break;
                }
            }
        }

        int vid = Integer.valueOf(queryParams1.get("vid"));
        if (!status) {
            msg = "Failed to insert video";
            vid = 0;
        } else if (!tagStatus) {
            msg = "Failed to insert tags";
            vid = 0;
        }

        SimpleResponse reponse = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), vid);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideoInfo", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoInfo(@RequestBody final RequestData request) {

        String entityName = null;

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("vid", request.getRequest_data().getVid());
        queryParams.put("rateid", request.getRequest_data().getVid());

        entityName = SibConstants.SqlMapper.SQL_SIB_GET_VIDEO;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        entityName = SibConstants.SqlMapper.SQL_SIB_GET_TAGS;
        List<Object> readObject1 = dao.readObjects(entityName, queryParams);

        Map<String, Object> tags = null;

        try {
            for (Object obj : readObject1) {
                tags = (Map) obj;
                Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = it.next();
                    if (pairs.getKey().equals("vid")) {
                        it.remove();
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }

        Map<String, Object> mymap = new HashMap<String, Object>();
        mymap.put("tags", readObject1);

        readObject.add(mymap);

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getVideoCommentsPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoCommentsPN(@RequestBody final RequestData request) {

        String entityName = null;

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Object[] queryParams = { request.getRequest_data().getVid(), map.get(Parameters.FROM), map.get(Parameters.TO) };

        entityName = SibConstants.SqlMapper.SQL_SIB_GET_COMMENTS_PN;

        List<Object> readObject = dao.readObjects(entityName, queryParams);
        String count = null;
        if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
            count = dao.getCount(SibConstants.SqlMapper.SQL_SIB_GET_COMMENTS_PN_COUNT, queryParams);
        }

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getVideoComments", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoComments(@RequestBody final RequestData request) {

        String entityName = null;

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("vid", request.getRequest_data().getVid());

        entityName = SibConstants.SqlMapper.SQL_SIB_GET_COMMENTS;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/editVideoInfo", method = RequestMethod.POST)
    public ResponseEntity<Response> editVideoInfo(@RequestBody final RequestData request) {

        String entityName = null;

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("vid", request.getRequest_data().getVid());
        queryParams.put("title", request.getRequest_data().getTitle());
        queryParams.put("description", request.getRequest_data().getDescription());

        entityName = SibConstants.SqlMapper.SQL_SIB_EDIT_VIDEO;

        boolean status = dao.insertUpdateObject(entityName, queryParams);

        if (status) {
            entityName = SibConstants.SqlMapper.SQL_SIB_DELETE_TAGS;
            status = dao.insertUpdateObject(entityName, queryParams);

            entityName = SibConstants.SqlMapper.SQL_SIB_INSERT_TAG;
            Map<String, String> queryParams1 = null;
            for (Tag tag : request.getRequest_data().getTags()) {
                queryParams1 = new HashMap<String, String>();
                queryParams1.put("vid", request.getRequest_data().getVid());
                queryParams1.put("tag", tag.getTag());
                boolean flag = dao.insertUpdateObject(entityName, queryParams1);
            }
        }

        SimpleResponse reponse = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), request
            .getRequest_data()
            .getVid());
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosWithTopicPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosWithTopicPN(@RequestBody final RequestData request) {

        String entityName = null;

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Object[] queryParams = { request.getRequest_data().getSubjectId(), request.getRequest_data().getTopicId(), map.get(Parameters.FROM), map
            .get(Parameters.TO) };

        entityName = SibConstants.SqlMapper.SQL_GET_VIDEO_PN;

        List<Object> readObject1 = null;
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        String msg = " No data Found";
        if (readObject != null) {
            entityName = SibConstants.SqlMapper.SQL_SIB_GET_TAGS;
            readObject1 = dao.readObjects(entityName, queryParams);

            Map<String, Object> tags = null;

            try {
                for (Object obj : readObject1) {
                    tags = (Map) obj;
                    Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pairs = it.next();
                        if (pairs.getKey().equals("vid")) {
                            it.remove();
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
        } else {
            SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
            return entity;
        }

        Map<String, Object> mymap = new HashMap<String, Object>();
        mymap.put("tags", readObject1);
        String count = null;
        readObject.add(mymap);
        if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
            count = dao.getCount(SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_TOPIC_PN_COUNT, queryParams);
        }

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosWithTopic", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosWithTopic(@RequestBody final RequestData request) {

        String entityName = null;

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("topicId", request.getRequest_data().getTopicId().trim());

        entityName = SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_TOPIC;

        List<Object> readObject1 = null;
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        String msg = "No data Found";
        if (readObject != null) {
            entityName = SibConstants.SqlMapper.SQL_SIB_GET_TAGS;
            readObject1 = dao.readObjects(entityName, queryParams);

            Map<String, Object> tags = null;

            try {
                for (Object obj : readObject1) {
                    tags = (Map) obj;
                    Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pairs = it.next();
                        if (pairs.getKey().equals("vid")) {
                            it.remove();
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
        } else {
            SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
            return entity;
        }

        Map<String, Object> mymap = new HashMap<String, Object>();
        mymap.put("tags", readObject1);

        readObject.add(mymap);

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    // @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideos", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideos(@RequestParam final long uid, @RequestParam final int offset) {

        String entityName = null;

        // Map<String, String> queryParams = new HashMap<String, String>();
        Object[] queryParams = new Object[] { uid, offset };

        entityName = SibConstants.SqlMapperBROT43.SQL_GET_VIDEOS;
        SimpleResponse reponse = null;

        // List<Object> readObject1 = null;
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        if (readObject != null && readObject.size() > 0) {
            // entityName = SibConstants.SqlMapper.SQL_SIB_GET_TAGS;
            // readObject1 = dao.readObjects(entityName, queryParams);
            // Map<String, Object> tags = null;
            // try {
            // for (Object obj : readObject1) {
            // tags = (Map) obj;
            // Iterator<Entry<String, Object>> it =
            // tags.entrySet().iterator();
            // while (it.hasNext()) {
            // Map.Entry pairs = it.next();
            // if (pairs.getKey().equals("vid")) {
            // it.remove();
            // }
            // }
            // }
            // } catch (Exception e) {
            // logger.error(e);
            // }
            reponse = new SimpleResponse("" + true, "videos", "getVideos", readObject);
        } else {
            reponse = new SimpleResponse("" + true, "videos", "getVideos", SibConstants.NO_DATA);
        }

        // Map<String, Object> mymap = new HashMap<String, Object>();
        // mymap.put("tags", readObject1);

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosList", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosList(@RequestBody final RequestData request) {

        String entityName = null;

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("subjectId", request.getRequest_data().getSubjectId());
        if (request.getRequest_data().getSubjectId().equals("0")) {
            entityName = SibConstants.SqlMapper.SQL_GET_VIDEOS_LIST;
        } else {
            entityName = SibConstants.SqlMapper.SQL_GET_VIDEOS_LIST_SUBID;
        }

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getHistoryVideosList", method = RequestMethod.GET)
    public ResponseEntity<Response> getHistoryVideosList(@RequestParam("uid") final String uid) {

        String entityName = null;

        Object[] queryParams = new Object[] { "" + uid };

        entityName = SibConstants.SqlMapper.SQL_GET_HISTORY_VIDEOS_LIST;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("true", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/clearHistoryVideosList", method = RequestMethod.GET)
    public ResponseEntity<Response> clearHistoryVideosList(@RequestParam("uid") final String uid, @RequestParam("vid") final String vid) {

        String entityName = null;
        Map<String, String> queryParams = new HashMap<String, String>();

        if (vid != null && vid != "") {
            queryParams.put("uid", uid);
            queryParams.put("vid", vid);
            entityName = SibConstants.SqlMapper.SQL_DELETE_HISTORY_VIDEO_BY_VID;
        } else {
            queryParams.put("uid", uid);
            entityName = SibConstants.SqlMapper.SQL_CLEAR_HISTORY_VIDEOS_LIST;
        }

        boolean deleteObject = dao.insertUpdateObject(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("true", deleteObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosListPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosListPN(@RequestBody final RequestData request) {

        String entityName = null;
        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Object[] queryParams = { request.getRequest_data().getSubjectId(), map.get(Parameters.FROM), map.get(Parameters.TO) };

        if (request.getRequest_data().getSubjectId().equals("0")) {
            entityName = SibConstants.SqlMapper.SQL_GET_VIDEOS_LIST_PN;
        } else {
            entityName = SibConstants.SqlMapper.SQL_GET_VIDEOS_LIST_SUBID_PN;
        }

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        String count = null;
        if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {

            if (request.getRequest_data().getSubjectId().equals("0")) {
                count = dao.getCount(SibConstants.SqlMapper.SQL_GET_VIDEOS_LIST_PN_COUNT, queryParams);
            } else {
                count = dao.getCount(SibConstants.SqlMapper.SQL_GET_VIDEOS_LIST_SUBID_PN_COUNT, queryParams);
            }
        }

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosPN(@RequestBody final RequestData request) {

        String entityName = null;
        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("from", map.get("from"));
        queryParams.put("to", map.get("to"));

        entityName = SibConstants.SqlMapper.SQL_SIB_GET_VIDEOS_PN;

        List<Object> readObject1 = null;
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        String msg = "No data Found";
        if (readObject != null) {
            entityName = SibConstants.SqlMapper.SQL_SIB_GET_TAGS;
            readObject1 = dao.readObjects(entityName, queryParams);

            Map<String, Object> tags = null;

            try {
                for (Object obj : readObject1) {
                    tags = (Map) obj;
                    Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pairs = it.next();
                        if (pairs.getKey().equals("vid")) {
                            it.remove();
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
        } else {
            SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
            return entity;
        }

        Map<String, Object> mymap = new HashMap<String, Object>();
        mymap.put("tags", readObject1);
        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosWithSubTopic", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosWithSubTopic(@RequestBody final RequestData request) {

        String entityName = null;

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("vid", request.getRequest_data().getVid());
        queryParams.put("subjectId", request.getRequest_data().getSubjectId().trim());
        queryParams.put("topic", request.getRequest_data().getTopic().trim());
        queryParams.put("subtopic", request.getRequest_data().getSubtopic().trim());

        entityName = SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_SUB_TOPIC;

        List<Object> readObject1 = null;
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        String msg = " No data Found";
        if (readObject != null) {
            entityName = SibConstants.SqlMapper.SQL_SIB_GET_TAGS;
            readObject1 = dao.readObjects(entityName, queryParams);

            Map<String, Object> tags = null;

            try {
                for (Object obj : readObject1) {
                    tags = (Map) obj;
                    Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pairs = it.next();
                        if (pairs.getKey().equals("vid")) {
                            it.remove();
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
        } else {
            SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
            return entity;
        }

        Map<String, Object> mymap = new HashMap<String, Object>();
        mymap.put("tags", readObject1);
        readObject.add(mymap);

        SimpleResponse reponse = new SimpleResponse("true", request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosWithSubTopicPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosWithSubTopicPN(@RequestBody final RequestData request) {

        String entityName = null;

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("vid", request.getRequest_data().getVid());
        queryParams.put("subject", request.getRequest_data().getSubject().trim());
        queryParams.put("nameTopic", request.getRequest_data().getTopic().trim());
        queryParams.put("subtopic", request.getRequest_data().getSubtopic().trim());
        queryParams.put("from", map.get("from"));
        queryParams.put("to", map.get("to"));

        entityName = SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_SUB_TOPIC_PN;

        List<Object> readObject1 = null;
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        String msg = "No data Found";
        if (readObject != null) {
            entityName = SibConstants.SqlMapper.SQL_SIB_GET_TAGS;
            readObject1 = dao.readObjects(entityName, queryParams);

            Map<String, Object> tags = null;

            try {
                for (Object obj : readObject1) {
                    tags = (Map) obj;
                    Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pairs = it.next();
                        if (pairs.getKey().equals("vid")) {
                            it.remove();
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
        } else {
            SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
            return entity;
        }

        String count = null;
        if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
            count = dao.getCount(
                SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_SUB_TOPIC_PN_COUNT,
                new Object[] { request.getRequest_data().getSubject().trim(), request.getRequest_data().getSubtopic().trim(), request
                    .getRequest_data()
                    .getTopic()
                    .trim() });
        }

        Map<String, Object> mymap = new HashMap<String, Object>();
        mymap.put("tags", readObject1);
        readObject.add(mymap);

        SimpleResponse reponse = new SimpleResponse("true", request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosWithSubjectPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosWithSubjectPN(@RequestBody final RequestData request) {

        String entityName = null;

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("subject", request.getRequest_data().getSubjectId().trim());
        queryParams.put("from", map.get("from"));
        queryParams.put("to", map.get("to"));

        entityName = SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_SUBJECT_PN;

        List<Object> readObject1 = null;
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        String msg = "No data Found";
        if (readObject != null) {
            entityName = SibConstants.SqlMapper.SQL_SIB_GET_TAGS;
            readObject1 = dao.readObjects(entityName, queryParams);

            Map<String, Object> tags = null;

            try {
                for (Object obj : readObject1) {
                    tags = (Map) obj;
                    Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pairs = it.next();
                        if (pairs.getKey().equals("vid")) {
                            it.remove();
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
        } else {
            SimpleResponse reponse = new SimpleResponse("true", request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
            return entity;
        }

        Map<String, Object> mymap = new HashMap<String, Object>();
        mymap.put("tags", readObject1);

        readObject.add(mymap);
        String count = null;
        if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
            count = dao.getCount(
                SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_SUBJECT_PN_COUNT,
                new Object[] { request.getRequest_data().getSubjectId().trim() });
        }

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosWithSubject", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosWithSubject(@RequestBody final RequestData request) {

        String entityName = null;

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("subjectId", request.getRequest_data().getSubjectId().trim());

        entityName = SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_SUBJECT;

        List<Object> readObject1 = null;
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        String msg = "No data Found";
        if (readObject != null) {
            entityName = SibConstants.SqlMapper.SQL_SIB_GET_TAGS;
            readObject1 = dao.readObjects(entityName, queryParams);

            Map<String, Object> tags = null;

            try {
                for (Object obj : readObject1) {
                    tags = (Map) obj;
                    Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pairs = it.next();
                        if (pairs.getKey().equals("vid")) {
                            it.remove();
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
        } else {
            SimpleResponse reponse = new SimpleResponse("true", request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
            return entity;
        }

        Map<String, Object> mymap = new HashMap<String, Object>();
        mymap.put("tags", readObject1);
        readObject.add(mymap);

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/searchVideos", method = RequestMethod.POST)
    public ResponseEntity<Response> searchVideos(@RequestBody final RequestData request) {

        String entityName = null;

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("title", request.getRequest_data().getTitle().trim());
        queryParams.put("description", request.getRequest_data().getTitle().trim());

        entityName = SibConstants.SqlMapper.SQL_SIB_SEARCH_VIDEOS;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        entityName = SibConstants.SqlMapper.SQL_SIB_GET_TAGS;
        List<Object> readObject1 = dao.readObjects(entityName, queryParams);

        Map<String, Object> tags = null;

        try {
            for (Object obj : readObject1) {
                tags = (Map) obj;
                Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = it.next();
                    if (pairs.getKey().equals("vid")) {
                        it.remove();
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }

        Map<String, Object> mymap = new HashMap<String, Object>();
        mymap.put("tags", readObject1);

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/searchVideosPN", method = RequestMethod.POST)
    public ResponseEntity<Response> searchVideosPN(@RequestBody final RequestData request) {

        String entityName = null;

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("title", request.getRequest_data().getTitle().trim());
        queryParams.put("description", request.getRequest_data().getTitle().trim());
        queryParams.put("from", map.get("from"));
        queryParams.put("to", map.get("to"));

        entityName = SibConstants.SqlMapper.SQL_SIB_SEARCH_VIDEOS_PN;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        entityName = SibConstants.SqlMapper.SQL_SIB_GET_TAGS;
        List<Object> readObject1 = dao.readObjects(entityName, queryParams);

        Map<String, Object> tags = null;

        try {
            for (Object obj : readObject1) {
                tags = (Map) obj;
                Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = it.next();
                    if (pairs.getKey().equals("vid")) {
                        it.remove();
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }

        Map<String, Object> mymap = new HashMap<String, Object>();
        mymap.put("tags", readObject1);

        readObject.add(mymap);
        String count = null;
        if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
            count = dao.getCount(
                SibConstants.SqlMapper.SQL_SIB_SEARCH_VIDEOS_PN_COUNT,
                new Object[] { "%" + request.getRequest_data().getTitle().trim() + "%" });
        }

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/searchVideosUsingTag", method = RequestMethod.POST)
    public ResponseEntity<Response> searchVideosUsingTag(@RequestBody final RequestData request) {

        String entityName = null;

        Map<String, String> queryParams = new HashMap<String, String>();
        Map<String, String> queryParams1 = null;

        queryParams.put("tag", request.getRequest_data().getTag().trim());

        entityName = SibConstants.SqlMapper.SQL_SIB_SEARCH_VIDEO_TAG;

        List<Object> readObject = dao.readObjects(entityName, queryParams);
        List<Object> readObject3 = new ArrayList<Object>();

        if (readObject != null) {
            Map dataMap = null;
            entityName = SibConstants.SqlMapper.SQL_SIB_GET_TAGS;
            List<Object> readObject1 = null;
            List<Object> readObject2 = null;

            Map<String, Object> tags = null;

            for (Object obj : readObject) {
                readObject2 = new ArrayList<Object>();
                dataMap = (Map) obj;
                queryParams1 = new HashMap<String, String>();

                queryParams1.put("vid", dataMap.get("vid").toString());
                readObject1 = dao.readObjects(entityName, queryParams1);

                try {
                    for (Object obj1 : readObject1) {
                        tags = (Map) obj1;
                        Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pairs = it.next();
                            if (pairs.getKey().equals("vid")) {
                                it.remove();
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error(e);
                }

                readObject2.add(obj);
                readObject2.add(readObject1);
                readObject3.add(readObject2);
            }
        } else {
            SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
            return entity;
        }

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject3);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/searchVideosUsingTagPN", method = RequestMethod.POST)
    public ResponseEntity<Response> searchVideosUsingTagPN(@RequestBody final RequestData request) {

        String entityName = null;

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());
        Map<String, String> queryParams = new HashMap<String, String>();
        Map<String, String> queryParams1 = null;

        queryParams.put("tag", request.getRequest_data().getTag().trim());
        queryParams.put("tag1", request.getRequest_data().getTag().trim());
        queryParams.put("from", map.get("from"));
        queryParams.put("to", map.get("to"));

        entityName = SibConstants.SqlMapper.SQL_SIB_SEARCH_VIDEO_TAG_PN;

        List<Object> readObject = dao.readObjects(entityName, queryParams);
        List<Object> readObject3 = new ArrayList<Object>();

        if (readObject != null) {
            Map dataMap = null;
            entityName = SibConstants.SqlMapper.SQL_SIB_GET_TAGS;
            List<Object> readObject1 = null;
            List<Object> readObject2 = null;

            Map<String, Object> tags = null;

            for (Object obj : readObject) {
                readObject2 = new ArrayList<Object>();
                dataMap = (Map) obj;
                queryParams1 = new HashMap<String, String>();

                queryParams1.put("vid", dataMap.get("vid").toString());
                readObject1 = dao.readObjects(entityName, queryParams1);

                try {
                    for (Object obj1 : readObject1) {
                        tags = (Map) obj1;
                        Iterator<Entry<String, Object>> it = tags.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pairs = it.next();
                            if (pairs.getKey().equals("vid")) {
                                it.remove();
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error(e);
                }

                readObject2.add(obj);
                readObject2.add(readObject1);
                readObject3.add(readObject2);
            }
        } else {
            SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
            return entity;
        }

        String count = null;
        if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
            count = dao.getCount(SibConstants.SqlMapper.SQL_SIB_SEARCH_VIDEO_TAG_PN_COUNT, new Object[] { request.getRequest_data().getTag().trim(), request
                .getRequest_data()
                .getTag()
                .trim() });
        }

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject3, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ResponseEntity<Response> removeVideo(@RequestBody final RequestData request) {

        String entityName = null;

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("vid", request.getRequest_data().getVid());

        entityName = SibConstants.SqlMapper.SQL_SIB_REMOVE_VIDEO;
        boolean status = dao.insertUpdateObject(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), status);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/rateVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> rateVideo(@RequestBody final RequestData request) {
        String entityName = null;
        boolean status = false;
        if (!AuthenticationFilter.isAuthed(this.context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("false", "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus statusDao = transactionManager.getTransaction(def);
        try {

            Object[] queryParams = new Object[] { request.getRequest_data().getUid(), request.getRequest_data().getVid() };

            entityName = SibConstants.SqlMapper.SQL_SIB_CHECK_RATE_VIDEO;

            List<Object> videoRated = dao.readObjects(entityName, queryParams);

            boolean isRated = videoRated.size() > 0 ? true : false;
            String rate = request.getRequest_data().getRating();
            String vid = request.getRequest_data().getVid();

            if (!isRated) {
                entityName = SibConstants.SqlMapper.SQL_SIB_RATE_VIDEO;
                queryParams = new Object[] { vid, request.getRequest_data().getUid(), rate };
            } else {
                queryParams = new Object[] { rate, vid, request.getRequest_data().getUid() };
                entityName = SibConstants.SqlMapper.SQL_SIB_RATE_UPDATE_VIDEO;
            }
            Object[] queryUpdateRate = { rate, vid, rate, vid };
            dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_AVG_RATE, queryUpdateRate);
            status = dao.insertUpdateObject(entityName, queryParams);

            transactionManager.commit(statusDao);
            logger.info("Insert Menu success " + new Date());
        } catch (NullPointerException | DataAccessException e) {
            transactionManager.rollback(statusDao);
            throw e;
        }
        SimpleResponse reponse = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), request
            .getRequest_data()
            .getVid());
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getVideosListByUser", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosListByUser(@RequestBody final RequestData request) {

        String entityName = null;
        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("uid", request.getRequest_data().getUid());
        queryParams.put("from", map.get("from"));
        queryParams.put("to", map.get("to"));

        entityName = SibConstants.SqlMapper.SQL_GET_VIDEOS_LIST_BY_USER_PN;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        String count = null;
        if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
            count = dao.getCount(SibConstants.SqlMapper.SQL_GET_VIDEOS_LIST_BY_USER_PN_COUNT, new Object[] { request.getRequest_data().getUid() });
        }

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateVideo", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> updateVideo(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        // Map<String, String> queryParams = new HashMap<String, String>();
        // queryParams.put("vid", request.getRequest_data().getVid());
        // queryParams.put("title", request.getRequest_data().getTitle());
        // queryParams.put("description",
        // request.getRequest_data().getDescription());
        Object[] queryParams = new Object[] { request.getRequest_data().getTitle(), request.getRequest_data().getDescription(), request
            .getRequest_data()
            .getVid(), };
        String entityName = SibConstants.SqlMapper.SQL_UPDATE_VIDEO;
        boolean status = true;
        status = dao.insertUpdateObject(entityName, queryParams);
        String message = "";
        if (status) {
            message = "Done";
        } else {
            message = "Fail";
        }

        SimpleResponse reponse = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), message);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);

        return entity;
    }

    @Override
    @RequestMapping(value = "/searchAllVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> searchAllVideo(@RequestBody final RequestData request) {

        String entityName = null;
        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Object[] queryParams = { request.getRequest_data().getTitle().trim(), request.getRequest_data().getDescription().trim(), request
            .getRequest_data()
            .getTitle()
            .trim(), request.getRequest_data().getDescription().trim(), request.getRequest_data().getTitle().trim(), request
            .getRequest_data()
            .getTitle()
            .trim(), map.get(Parameters.FROM), map.get(Parameters.TO) };

        entityName = SibConstants.SqlMapper.SQL_SEARCH_ALL_VIDEO;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        String count = dao.getCount(SibConstants.SqlMapper.SQL_COUNT_SEARCH_ALL_VIDEO, new Object[] {});

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateViewVideo", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> updateViewVideo(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        // Map<String, String> queryParams = new HashMap<String, String>();
        // queryParams.put("vid", request.getRequest_data().getVid());
        Object[] queryParams = { request.getRequest_data().getVid() };
        String entityName = SibConstants.SqlMapper.SQL_UPDATE_NUMVIEW_VIDEO;
        boolean status = true;
        status = dao.insertUpdateObject(entityName, queryParams);
        String message = "";
        if (status) {
            message = "Done";
        } else {
            message = "Fail";
        }

        SimpleResponse reponse = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), message);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);

        return entity;
    }

    @Override
    @RequestMapping(value = "/updateVideoWatched", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> updateVideoWatched(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getVid() };

        boolean status = false;
        String entityName = SibConstants.SqlMapper.SQL_CHECK_USER_WATCHED_VIDEO;
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        if (readObject.size() < 1) {
            entityName = SibConstants.SqlMapper.SQL_UPDATE_USER_WATCHED_VIDEO;
            status = dao.insertUpdateObject(entityName, queryParams);
        }

        String message = "";
        if (status) {
            message = "Done";
        } else {
            message = "Fail";
        }

        SimpleResponse reponse = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), message);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);

        return entity;
    }

    @Override
    @RequestMapping(value = "/getIdVideoWatched", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> getIdVideoWatched(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("uid", request.getRequest_data().getUid());

        String entityName = SibConstants.SqlMapper.SQL_GET_ID_VIDEO_USER_WATCHED;
        List<Object> readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("", request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);

        return entity;
    }

    @Override
    @RequestMapping(value = "/getRating", method = RequestMethod.POST)
    public ResponseEntity<Response> getRating(@RequestBody final RequestData request) {

        String entityName = null;

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("vid", request.getRequest_data().getVid());

        entityName = SibConstants.SqlMapper.SQL_GET_RATING_VIDEO;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/checkUserRatingVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> checkUserRatingVideo(@RequestBody final RequestData request) {

        String entityName = null;
        Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getVid() };

        entityName = SibConstants.SqlMapper.SQL_SIB_CHECK_RATE_VIDEO;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getAllVideoByUserPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getAllVideoByUserPN(@RequestBody final RequestData request) {

        String entityName = null;
        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Object[] queryParams = { request.getRequest_data().getAuthorID(), request.getRequest_data().getAuthorID(), map.get(Parameters.FROM), map
            .get(Parameters.TO) };

        entityName = SibConstants.SqlMapper.SQL_SEARCH_ALL_VIDEO_BY_USER;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        String count = dao.getCount(SibConstants.SqlMapper.SQL_COUNT_SEARCH_ALL_VIDEO_BY_USER, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + true, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.siblinks.ws.service.VideoService#getVideosWithSubject(long,
     * long)
     */
    @RequestMapping(value = "/getVideoByUserSubject", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Response> getVideoByUserSubject(@RequestParam final long uId, @RequestParam final String limit, @RequestParam final String offset) {

        Object[] paramQueryGetSubjects = { uId };
        String subjectIdResult = dao.readObjects(SibConstants.SqlMapper.SQL_GET_SUBJECT_REG, paramQueryGetSubjects).toString();

        String subjectIds = subjectIdResult.substring(subjectIdResult.indexOf("=") + 1, subjectIdResult.lastIndexOf("}"));
        SimpleResponse response = null;
        if (subjectIds != null) {
            CommonUtil cmUtils = CommonUtil.getInstance();
            Map<String, String> pageLimit = cmUtils.getOffset(limit, offset);
            Object[] paramsGetVideos = { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
            String whereClause = "IN(" + subjectIds + ") LIMIT ? OFFSET ?;";
            List<Object> resultData = dao.readObjectsWhereClause(SibConstants.SqlMapper.SQL_GET_VIDEO_BY_SUBJECT, whereClause, paramsGetVideos);
            String count = String.valueOf(resultData.size());
            response = new SimpleResponse("" + Boolean.TRUE, "GET", "getVideoByUserSubject", resultData, count);
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/getVideoByViews", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Response> getVideoByViews(@RequestParam final String limit, @RequestParam final String offset) {
        CommonUtil cmUtils = CommonUtil.getInstance();
        Map<String, String> pageLimit = cmUtils.getOffset(limit, offset);
        Object[] paramsGetVideos = { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
        List<Object> resultData = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_BY_VIEW, paramsGetVideos);
        String count = String.valueOf(resultData.size());
        SimpleResponse response = new SimpleResponse("" + Boolean.TRUE, "GET", "getVideoByViews", resultData, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/getVideoByRate", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Response> getVideoByRate(@RequestParam final String limit, @RequestParam final String offset) {
        CommonUtil cmUtils = CommonUtil.getInstance();
        Map<String, String> pageLimit = cmUtils.getOffset(limit, offset);
        Object[] paramsGetVideos = { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
        List<Object> resultData = dao.readObjects(SibConstants.SqlMapper.SQL_VIDEO_BY_RATE, paramsGetVideos);
        String count = String.valueOf(resultData.size());
        SimpleResponse response = new SimpleResponse("" + Boolean.TRUE, "GET", "getVideoByRate", resultData, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/getVideoPlaylistNewest", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Response> getVideoPlaylistNewest(@RequestParam final String limit, @RequestParam final String offset) {

        CommonUtil cmUtil = CommonUtil.getInstance();
        Map<String, String> map = cmUtil.getOffset(limit, offset);
        Object[] params = { Integer.parseInt(map.get("limit")), Integer.parseInt(map.get("offset")) };
        List<Object> resultData = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_PLAYLIST_NEWEST, params);
        String count = String.valueOf(resultData.size());
        SimpleResponse response = new SimpleResponse("" + Boolean.TRUE, "GET", "getVideoPlaylistNewest", resultData, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/getVideoStudentSubcribe", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Response> getVideoStudentSubcribe(@RequestParam final long userId, @RequestParam final String limit, @RequestParam final String offset) {
        CommonUtil cmUtil = CommonUtil.getInstance();
        Map<String, String> map = cmUtil.getOffset(limit, offset);
        Object[] params = { userId, Integer.parseInt(map.get("limit")), Integer.parseInt(map.get("offset")) };
        List<Object> resultData = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_STUDENT_SUBCRIBE, params);
        String count = String.valueOf(resultData.size());
        SimpleResponse response = new SimpleResponse("" + Boolean.TRUE, "GET", "getVideoStudentSubcribe", resultData, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/setSubscribeMentor", method = RequestMethod.POST)
    public ResponseEntity<Response> setSubscribeMentor(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }
        Object[] queryParams = new Object[] { request.getRequest_data().getStudentId(), request.getRequest_data().getMentorId() };
        List<Object> readObject = null;
        // SELECT exists row
        String entityName = SibConstants.SqlMapper.SQL_FIND_STUDENT_SUBCRIBE;
        readObject = dao.readObjects(entityName, queryParams);
        boolean status = true;
        String message = "subs";
        if (readObject.size() == 0) {
            entityName = SibConstants.SqlMapper.SQL_SUBCRIBE_VIDEO_STUDENT;
            status = dao.insertUpdateObject(entityName, queryParams);

        } else {
            String subscribe = (String) ((Map) readObject.get(0)).get(Parameters.SUBCRIBE);
            if (subscribe != null && subscribe.equals("Y")) {
                message = "unsubs";
            }
            entityName = SibConstants.SqlMapper.SQL_SUBSCRIBE_UNSUBSCRIBE_MENTOR;
            status = dao.insertUpdateObject(entityName, queryParams);

        }

        SimpleResponse reponse = new SimpleResponse("" + status, message, request.getRequest_data_method(), readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;

    }

    public Map<String, String> getParamsSubcribe(final String sql, final String sql2, final Object[] queryParams, final RequestData request) {

        List<Object> readObject = null;
        Map<String, String> queryParamsIns = new HashMap<String, String>();

        readObject = dao.readObjects(sql, queryParams);
        if (!CollectionUtils.isEmpty(readObject)) {
            queryParamsIns.put("userId", ((Map) readObject.get(0)).get("StudentId").toString());
            queryParamsIns.put("authorId", queryParams[1].toString());
            queryParamsIns.put("type", "subcribe");
            queryParamsIns.put("title", "Following You!");
            queryParamsIns.put("notification", ((Map) readObject.get(0)).get("firstName").toString() +
                                               " " +
                                               ((Map) readObject.get(0)).get("lastName").toString() +
                                               " Watching you.");
            // }
        }
        return queryParamsIns;
    }

    /**
     * @author Tavv
     * @param userId
     *            id of user
     * @param typeGet
     *            is type you want get count {@value "home", "mentor",
     *            "subscription", "history"}
     * @return home - return number count of subscription, history,
     * 
     */
    @RequestMapping(value = "/getCountFactory", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Response> getCountFactory(@RequestParam final long userId, @RequestParam final String typeGet) {
        String entityName = null;
        Object[] params = null;
        String request_data_method = null;
        switch (typeGet) {
            case "home":
                entityName = SibConstants.SqlMapper.SQL_COUNT_HOME_CATEGORY;
                params = new Object[] { userId, userId, userId };
                request_data_method = "getCountHome";
                break;
            case "mentor":
                entityName = SibConstants.SqlMapper.SQL_GET_COUNT_INFO_MENTOR;
                params = new Object[] { userId, userId, userId };
                request_data_method = "getCountInfoMentor";
                break;
            case "subcription":
                entityName = SibConstants.SqlMapper.SQL_COUNT_SUBCRIBED;
                params = new Object[] { userId };
                request_data_method = "getCountSubcribed";
                break;
            case "history":
                params = new Object[] { userId };
                entityName = SibConstants.SqlMapper.SQL_COUNT_VIDEO_WATCHED;
                request_data_method = "getCountWatched";
                break;
            case "favourite":
                params = new Object[] { userId };
                entityName = SibConstants.SqlMapper.SQL_COUNT_VIDEO_LIKE;
                request_data_method = "getCountFavourite";
                break;
        }
        Object count = dao.readObjects(entityName, params);
        SimpleResponse response = new SimpleResponse("" + Boolean.TRUE, "count", request_data_method, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/getCountHomeCategory", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Response> getCountHomeCategory(@RequestParam final long userId) {
        String entityName = SibConstants.SqlMapper.SQL_COUNT_HOME_CATEGORY;
        Object[] params = { userId, userId, userId };
        Object count = dao.readObjects(entityName, params);
        SimpleResponse response = new SimpleResponse("" + Boolean.TRUE, "count", "getCountFactory", count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/getUnWatchedVideoSubcribe")
    @ResponseBody
    public ResponseEntity<Response> getUnWatchedVideoSubcribe(@RequestParam final long userId) {
        String entityName = SibConstants.SqlMapper.SQL_GET_VIDEO_UNWATCHED;
        Object[] params = { userId, userId };
        List<Object> dataResult = dao.readObjects(entityName, params);
        String count = String.valueOf(dataResult.size());
        SimpleResponse response = new SimpleResponse("" + Boolean.TRUE, "video", "getUnWatchedVideoSubcribe", dataResult, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/getNewVideoMentorSubscribe")
    @ResponseBody
    public ResponseEntity<Response> getNewVideoMentorSubscribe(@RequestParam final long userId, @RequestParam final String limit,
            @RequestParam final String offset) {
        String entityName = SibConstants.SqlMapper.SQL_GET_ALL_MENTOR_SUBSCRIBED;
        CommonUtil cmUtil = CommonUtil.getInstance();
        Map<String, String> map = cmUtil.getOffset(limit, offset);
        Object[] params = { userId, userId, Integer.parseInt(map.get("limit")), Integer.parseInt(map.get("offset")) };
        List<Object> dataResult = dao.readObjects(entityName, params);
        String count = String.valueOf(dataResult.size());
        SimpleResponse response = new SimpleResponse("" + Boolean.TRUE, "video", "getNewVideoMentorSubscribe", dataResult, count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getVideosBySubject", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideosBySubject(@RequestParam final long userid, @RequestParam final long subjectid, @RequestParam final int offset) {
        SimpleResponse reponse = null;
        Object[] queryParams = new Object[] { userid, subjectid, offset };

        String entityName = SibConstants.SqlMapperBROT43.SQL_GET_VIDEOS_BY_SUBJECT;

        List<Object> readObject = dao.readObjects(entityName, queryParams);
        if (readObject != null && readObject.size() > 0) {
            reponse = new SimpleResponse("" + true, "videos", "getVideos", readObject);
        } else {
            reponse = new SimpleResponse("" + true, "videos", "getVideos", SibConstants.NO_DATA);
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.siblinks.ws.service.VideoService#getVideosTopRated(long)
     */
    @Override
    @RequestMapping(value = "/getVideosTopRated", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideosTopRated(@RequestParam final long uid, @RequestParam final int offset) {

        Object[] queryParams = new Object[] { uid, offset };
        SimpleResponse reponse = null;

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapperBROT43.SQL_GET_VIDEOS_TOP_RATED, queryParams);
        if (readObject != null && readObject.size() > 0) {
            reponse = new SimpleResponse("" + true, "videos", "getVideosTopRated", readObject);
        } else {
            reponse = new SimpleResponse("" + true, "videos", "getVideosTopRated", SibConstants.NO_DATA);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.siblinks.ws.service.VideoService#getVideosTopViewed(long)
     */
    @Override
    @RequestMapping(value = "/getVideosTopViewed", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideosTopViewed(@RequestParam final long uid, @RequestParam final int offset) {

        Object[] queryParams = new Object[] { uid, offset };
        SimpleResponse reponse = null;

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapperBROT43.SQL_GET_VIDEOS_TOP_VIEWED, queryParams);
        if (readObject != null && readObject.size() > 0) {
            reponse = new SimpleResponse("" + true, "videos", "getVideosTopViewed", readObject);
        } else {
            reponse = new SimpleResponse("" + true, "videos", "getVideosTopViewed", SibConstants.NO_DATA);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.siblinks.ws.service.VideoService#getVideosRecently(long)
     */
    @Override
    @RequestMapping(value = "/getVideosRecently/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideosRecently(@PathVariable(value = "id") final long id) {
        Object[] queryParams = new Object[] { "" + id };
        SimpleResponse reponse = null;

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapperBROT43.SQL_GET_VIDEOS_RECENTLY, queryParams);
        if (readObject != null && readObject.size() > 0) {
            reponse = new SimpleResponse("" + true, "videos", "getVideosRecently", readObject);
        } else {
            reponse = new SimpleResponse("" + true, "videos", "getVideosRecently", SibConstants.NO_DATA);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.siblinks.ws.service.VideoService#insertVideo(com.siblinks.ws.model
     * .RequestData)
     */
    @Override
    @RequestMapping(value = "/insertVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> insertVideo(@RequestBody final RequestData request) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        String authorId = request.getRequest_data().getAuthorID();
        String plid = request.getRequest_data().getPlid();

        Object[] queryParams;

        String entityName = null;

        List<Object> readObject = null;
        try {
            // insert video
            entityName = SibConstants.SqlMapperBROT43.SQL_INSERT_VIDEO;
            queryParams = new Object[] { request.getRequest_data().getTitle(), request.getRequest_data().getDescription(), request.getRequest_data().getUrl(), request
                .getRequest_data()
                .getImage(), request.getRequest_data().getSubjectId(), authorId };
            long vid = dao.insertObject(entityName, queryParams);

            if (plid != null && plid.length() > 0) {
                queryParams = new Object[] { plid, vid };
                dao.insertUpdateObject(SibConstants.SqlMapperBROT126.SQL_ADD_VIDEOS_PLAYLIST, queryParams);
            }

            // get list users that subscribed mentor
            entityName = SibConstants.SqlMapperBROT43.SQL_GET_STUDENT_SUBSCRIBE;
            queryParams = new Object[] { authorId };
            readObject = dao.readObjects(entityName, queryParams);

            if (readObject != null && !readObject.isEmpty() && readObject.size() > 0) {
                entityName = SibConstants.SqlMapperBROT43.SQL_INSERT_VIDEO_SUBCRIBE;
                for (Object object : readObject) {
                    JsonObject json = new JsonParser().parse(object.toString()).getAsJsonObject();
                    long studentId = json.get("studentid").getAsLong();
                    queryParams = new Object[] { vid + ",", authorId, studentId };
                    dao.insertUpdateObject(entityName, queryParams);
                }
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.debug(e.getMessage());
        }

        SimpleResponse reponse = new SimpleResponse("" + true, "videos", "insertVideo", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.siblinks.ws.service.VideoService#getVideosPlaylist(long)
     */
    @Override
    @RequestMapping(value = "/getVideosPlaylist", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideosPlaylist(@RequestParam final long uid) {
        String entityName = null;

        Object[] queryParams = new Object[] { uid };

        entityName = SibConstants.SqlMapperBROT43.SQL_GET_VIDEOS_PLAYLIST;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + true, "videos", "getVideosPlaylist", readObject);

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getVideoById/{vid}", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoById(@PathVariable(value = "vid") final long vid) {
        String entityName = SibConstants.SqlMapperBROT43.SQL_GET_VIDEOS_BY_ID;
        Object[] queryParams = { vid };

        List<Object> readObject = dao.readObjects(entityName, queryParams);
        SimpleResponse reponse = new SimpleResponse("" + true, "Video", "getVideoById", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getVideoById/{vid}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoById(@PathVariable(value = "vid") final long vid, @PathVariable(value = "type") final String type) {
        String entityName = null;

        Object[] queryParams = { vid };
        if (Parameters.TYPE_PLAY_SOLO_VIDEO.equalsIgnoreCase(type)) {
            entityName = SibConstants.SqlMapper.SQL_GET_USER_POST_VIDEO;
        } else {
            entityName = SibConstants.SqlMapper.SQL_GET_VIDEOS_BY_PLAYLIST;
        }
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        SimpleResponse reponse = new SimpleResponse("" + true, "Video", "GetVideoById", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/getVideoRecommend", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoRecommend(@RequestParam final long studentId) {
        String entityName = SibConstants.SqlMapper.SQL_VIDEO_RECOMMENDED_FOR_YOU;
        Object[] params = { studentId };
        List<Object> results = dao.readObjects(entityName, params);
        MultiValueMap multiValueMap = new MultiValueMap();
        ArrayList<Object> readObject = new ArrayList<>();
        if (results != null) {
            for (int i = 0; i < results.size(); i++) {
                Map obj = (Map) results.get(i);
                // names.add(obj.get("userid").toString());
                multiValueMap.put(obj.get("name"), obj);
            }
        }
        Object[] key = multiValueMap.keySet().toArray();
        for (int i = 0; i < multiValueMap.keySet().size(); i++) {
            readObject.add(multiValueMap.get(key[i]));
        }
        // System.out.println(multiValueMap);
        // Map<String, Object> readObject = new HashMap<>();
        // for (int i = 0; i < names.size(); i++) {
        // readObject.put(names.get(i), multiValueMap.get(names.get(i)));
        // }
        SimpleResponse response = new SimpleResponse("" + Boolean.TRUE, "video", "getVideoRecommend", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.siblinks.ws.service.VideoService#getVideoBySubject(com.siblinks.ws.
     * model.RequestData)
     */
    @Override
    @RequestMapping(value = "/getVideoBySubject", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoBySubject(@RequestParam final long userId, @RequestParam final String subjectId, @RequestParam final String limit,
            @RequestParam final String offset) {

        Object result = getVideosFactory(subjectId, userId, limit, offset);
        SimpleResponse response = new SimpleResponse("" + Boolean.TRUE, "video", "getVideoBySubject", result);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    private Map<String, Object> getVideosFactory(final String subjectId, final long userId, final String limit, final String offset) {
        Map<String, Object> map = new HashMap<>();
        Object[] params = null;
        params = new Object[] { userId };

        CommonUtil cmUtils = CommonUtil.getInstance();
        Map<String, String> pageLimit = cmUtils.getOffset(limit, offset);
        Object subjectIdResult = dao.readObjects(SibConstants.SqlMapper.SQL_GET_SUBJECT_REG, params);
        List<Map<String, String>> objConvertor = (List<Map<String, String>>) subjectIdResult;
        String subjectIds = null;
        if (!CollectionUtils.isEmpty(objConvertor)) {
            for (Map<String, String> obj : objConvertor) {
                subjectIds = obj.get("defaultSubjectId");
            }

        }
        if (userId == -1) {
            if (subjectId.isEmpty() || subjectId.equals("-1")) {
                params = new Object[] { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
                List<Object> resultDataRecommended = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_BY_VIEW, params);
                List<Object> resultRecently = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_PLAYLIST_NEWEST, params);
                map.put("recommended", resultDataRecommended);
                map.put("recently", resultRecently);
            } else {
                params = new Object[] { subjectId, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
                List<Object> resultDataRecommended = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_VIEW_BY_SUBJECT, params);
                params = new Object[] { subjectId, subjectId, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
                List<Object> resultRecently = dao.readObjects(SibConstants.SqlMapper.VIDEO_PLAYLIST_NEWEST_BY_SUBJECT, params);
                map.put("recommended", resultDataRecommended);
                map.put("recently", resultRecently);
            }
        } else if (subjectId.equals("-1")) {
            if (subjectIds != null) {
                params = new Object[] { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
                String whereClause = "WHERE V.subjectId IN(" + subjectIds + ") ORDER BY V.timeStamp DESC LIMIT ? OFFSET ?;";
                List<Object> resultDataRecommended = dao.readObjectsWhereClause(SibConstants.SqlMapper.SQL_GET_VIDEO_BY_SUBJECT, whereClause, params);
                map.put("recommended", resultDataRecommended);
            }

            params = new Object[] { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
            List<Object> resultRecently = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_PLAYLIST_NEWEST, params);
            map.put("recently", resultRecently);

            String entityName = SibConstants.SqlMapper.SQL_VIDEO_RECOMMENDED_FOR_YOU;
            params = new Object[] { userId };
            List<Object> results = dao.readObjects(entityName, params);
            MultiValueMap multiValueMap = new MultiValueMap();
            ArrayList<Object> readObject = new ArrayList<>();
            if (results != null) {
                for (int i = 0; i < results.size(); i++) {
                    Map obj = (Map) results.get(i);
                    multiValueMap.put(obj.get("name"), obj);
                }
            }
            Object[] key = multiValueMap.keySet().toArray();
            for (int i = 0; i < multiValueMap.keySet().size(); i++) {
                readObject.add(multiValueMap.get(key[i]));
            }
            map.put("recommended_for_you", readObject);
        } else if (isValidatedForm(userId, subjectId)) {
            int subId;
            try {
                subId = Integer.parseInt(subjectId);
            } catch (Exception e) {
                subId = -2;
            }
            if (subId != -2) {
                if (subjectIds != null) {
                    String[] subjects = subjectIds.split(",");
                    if (ArrayUtils.contains(subjects, subjectId)) {
                        params = new Object[] { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
                        StringBuilder sBuilder = new StringBuilder();
                        for (String subject : subjects) {
                            String childSubjectId = CommonUtil.getAllChildCategory("" + subject, getAllSubjectIdCategory());
                            sBuilder.append(childSubjectId.concat(","));
                        }
                        String whereClause = "WHERE V.subjectId IN (" +
                                             sBuilder.toString().substring(0, sBuilder.toString().lastIndexOf(",")) +
                                             ") ORDER BY V.timeStamp DESC LIMIT ? OFFSET ?;";
                        List<Object> resultDataRecommended = dao.readObjectsWhereClause(SibConstants.SqlMapper.SQL_GET_VIDEO_BY_SUBJECT, whereClause, params);
                        map.put("recommended", resultDataRecommended);
                    }
                } else {
                    map.put("recommended", null);
                }
                params = new Object[] { userId, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
                String whereClause = "WHERE S.StudentId = ? AND S.Subcribe = 'Y' AND V.subjectId IN (" +
                                     subId +
                                     ") ORDER BY V.timeStamp DESC LIMIT ? OFFSET ? ";
                List<Object> resultRecently = dao.readObjectsWhereClause(SibConstants.SqlMapper.SQL_NEW_VIDEO_MENTOR_SUBSCRIBE, whereClause, params);
                map.put("recently", resultRecently != null ? resultRecently : null);

                String entityName = SibConstants.SqlMapper.SQL_VIDEO_RECOMMENDED_FOR_YOU_WITH_SUB_ID;
                params = new Object[] { subId, userId };
                List<Object> results = dao.readObjects(entityName, params);
                MultiValueMap multiValueMap = new MultiValueMap();
                ArrayList<Object> readObject = new ArrayList<>();
                if (results != null) {
                    for (int i = 0; i < results.size(); i++) {
                        Map obj = (Map) results.get(i);
                        multiValueMap.put(obj.get("name"), obj);
                    }
                }
                Object[] key = multiValueMap.keySet().toArray();
                for (int i = 0; i < multiValueMap.keySet().size(); i++) {
                    readObject.add(multiValueMap.get(key[i]));
                }
                map.put("recommended_for_you", readObject);
            }
        }
        return map;
    }

    private boolean isValidatedForm(final long userId, final String subjectId) {
        if (userId != -1 && !subjectId.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<Object> getAllSubjectIdCategory() {
        String entityName = SibConstants.SqlMapper.SQL_GET_ALL_SUBJECTID_CATEGORY;
        List<Object> subject = dao.readObjects(entityName, new Object[] {});
        return subject;
    }

    @Override
    @RequestMapping(value = "/deleteMultipleVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteMultipleVideo(@RequestBody final RequestData request) {
        String authorId = request.getRequest_data().getAuthorID();
        ArrayList<String> vids = request.getRequest_data().getVids();
        SimpleResponse response = null;

        if (authorId != null && authorId.length() > 0) {
            int countSuccess = 0;
            int countFailed = 0;
            boolean status = false;
            for (String vid : vids) {
                // delete video
                status = deleteVideo(vid, authorId);
                if (status) {
                    countSuccess++;
                } else {
                    countFailed++;
                }
            }

            String msg = String.format("Deleted success %d videos and fail %d videos", countSuccess, countFailed);
            response = new SimpleResponse("" + Boolean.TRUE, request.getRequest_data_type(), request.getRequest_data_method(), msg);
        } else {
            response = new SimpleResponse("" + Boolean.TRUE, request.getRequest_data_type(), request.getRequest_data_method(), "Missing authorId.");
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/searchVideos", method = RequestMethod.GET)
    public ResponseEntity<Response> searchVideos(@RequestParam final long uid, @RequestParam final String keyword, @RequestParam final int offset) {
        SimpleResponse reponse = null;
        Object[] queryParams = new Object[] { uid };

        String whereClause = String.format(
            " and a.title like '%%%s%%' OR a.description like '%%%s%%' order by timeStamp DESC limit 10 offset %d",
            keyword,
            keyword,
            offset);
        String entityName = SibConstants.SqlMapperBROT126.SQL_SEARCH_VIDEOS;

        List<Object> readObject = dao.readObjectsWhereClause(entityName, whereClause, queryParams);
        if (readObject != null && readObject.size() > 0) {
            reponse = new SimpleResponse("" + true, "videos", "searchVideos", readObject);
        } else {
            reponse = new SimpleResponse("" + true, "videos", "searchVideos", SibConstants.NO_DATA);
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/addVideosToPlaylist", method = RequestMethod.POST)
    public ResponseEntity<Response> addVideosToPlaylist(@RequestBody final RequestData request) {
        String authorId = request.getRequest_data().getAuthorID();
        String plid = request.getRequest_data().getPlid();
        ArrayList<String> vids = request.getRequest_data().getVids();
        List<Object> readObjects = null;
        SimpleResponse response = null;
        if (authorId != null && authorId.length() > 0 && plid != null && plid.length() > 0) {
            int countSuccess = 0;
            int countFail = 0;
            for (String vid : vids) {
                Object[] queryParams = new Object[] { plid, vid };
                // check vid and plid exists in Sib_Playlist_Video or not
                readObjects = dao.readObjects(SibConstants.SqlMapperBROT126.SQL_CHECK_VIDEO_IN_PLAYLIST, queryParams);
                if (readObjects != null && readObjects.size() > 0) {
                    // video has playlist already
                    // do nothing temporary
                } else {
                    // insert vid and plid into Sib_Playlist_Video
                    boolean status = dao.insertUpdateObject(SibConstants.SqlMapperBROT126.SQL_ADD_VIDEOS_PLAYLIST, queryParams);
                    if (status) {
                        countSuccess++;
                    } else {
                        countFail++;
                    }
                }
            }
            String msg = String.format("Insert success %d videos and fail %d videos into playlist", countSuccess, countFail);
            response = new SimpleResponse(true + "", request.getRequest_data_type(), request.getRequest_data_method(), msg);
        } else {
            response = new SimpleResponse(true + "", request.getRequest_data_type(), request.getRequest_data_method(), "Missing authorId or plid.");
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/searchVideo", method = RequestMethod.GET)
    public ResponseEntity<Response> searchVideo(@RequestParam final String keyword, @RequestParam final String limit, @RequestParam final String offset) {

        Map<String, String> searchLimit = CommonUtil.getInstance().getOffset(limit, offset);

        String strEntity = SibConstants.SqlMapper.SQL_SEARCH_VIDEO;

        String whereClause = String.format(
            " title LIKE '%%%s%%' OR description LIKE '%%%s%%' ORDER BY title, description LIMIT %d OFFSET %d",
            keyword,
            keyword,
            Integer.valueOf(searchLimit.get("limit")),
            Integer.valueOf(searchLimit.get("offset")));

        List<Object> searchResult = dao.readObjectsWhereClause(strEntity, whereClause, new Object[] {});

        SimpleResponse response;
        if (searchResult != null && !searchResult.isEmpty()) {
            String lengthOfResult = "" + searchResult.size();
            response = new SimpleResponse("" + Boolean.TRUE, "video", "searchVideo", searchResult, lengthOfResult);
        } else {
            response = new SimpleResponse("" + Boolean.FALSE, "video", "searchVideo", SibConstants.NO_DATA);
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;

    }

    @RequestMapping(value = "/getAllVideos", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllVideos() {

        String strEntity = SibConstants.SqlMapper.SQL_GET_ALL_VIDEO;

        List<Object> searchResult = dao.readObjects(strEntity, new Object[] {});

        SimpleResponse response;
        if (searchResult != null && !searchResult.isEmpty()) {
            String lengthOfResult = "" + searchResult.size();
            response = new SimpleResponse("" + Boolean.TRUE, "video", "getAllVideos", searchResult, lengthOfResult);
        } else {
            response = new SimpleResponse("" + Boolean.FALSE, "video", "getAllVideos", SibConstants.NO_DATA);
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;

    }
}
