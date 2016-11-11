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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.common.LoggedInChecker;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.ActivityLogData;
import com.siblinks.ws.model.ManageVideoModel;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.model.SubCategoryModel;
import com.siblinks.ws.model.Tag;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.ActivityLogService;
import com.siblinks.ws.service.VideoService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.SibConstants;
import com.siblinks.ws.util.StringUtil;

/**
 * {@link VideoService}
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

    @Autowired
    private PlatformTransactionManager transactionManager;

    private final LoggedInChecker loggedInChecker;

    @Autowired
    VideoServiceImpl(final LoggedInChecker loggedInChecker) {
        this.loggedInChecker = loggedInChecker;
    }

    @Autowired
    ActivityLogService activiLogService;

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getSubCategoryData", method = RequestMethod.POST)
    public ResponseEntity<Response> getSubCategoryData(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = new Object[] { request.getRequest_data().getSubjectId(), request.getRequest_data().getCid() };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_VIDEO_SUBCATAGERY_READ, queryParams);
            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/saveSubCategory", method = RequestMethod.POST)
    public ResponseEntity<Response> saveSubCategory(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            SubCategoryModel subCategoryModel = new SubCategoryModel();

            try {
                ObjectMapper mapper = new ObjectMapper();

                subCategoryModel = mapper.readValue(request.getRequest_data().getStringJson(), SubCategoryModel.class);

            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
                if (!status) {
                    vid = 0;
                } else if (!tagStatus) {
                    vid = 0;
                }
            }

            response = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), vid);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateSubCategory", method = RequestMethod.POST)
    public ResponseEntity<Response> updateSubCategory(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            ObjectMapper mapper = new ObjectMapper();

            SubCategoryModel subCategoryModel = new SubCategoryModel();

            try {
                subCategoryModel = mapper.readValue(request.getRequest_data().getStringJson(), SubCategoryModel.class);

            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), status);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/deleteSubCategory", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteSubCategory(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = new Object[] { request.getRequest_data().getStringJson() };
            boolean flag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SUBJECT_SUB_CATEGORY_DELETE, queryParams);

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), flag);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoDetails", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoDetails(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = new Object[] { request.getRequest_data().getStringJson() };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_VIDEO_SUBJECT_MAPPING_DATA_READ, queryParams);

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getMentorsOfVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> getMentorsOfVideo(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = new Object[] { request.getRequest_data().getSubjectId() };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_VIDEO_MENTORS_READ, queryParams);

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getMentorReviewsPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getMentorReviewsPN(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            //
            CommonUtil util = CommonUtil.getInstance();
            Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());
            Object[] queryParams = { request.getRequest_data().getUid(), map.get(Parameters.FROM), map.get(Parameters.TO) };
            String entityName = SibConstants.SqlMapper.SQL_SIB_MENTOR_REVIEWS_READ;

            List<Object> readObject = dao.readObjects(entityName, queryParams);

            String count = null;
            if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
                count = dao.getCount(SibConstants.SqlMapper.SQL_SIB_MENTOR_REVIEWS_READ_COUNT, queryParams);
            }

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoDetailsPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoDetailsPN(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            CommonUtil util = CommonUtil.getInstance();

            Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());
            Object[] queryParams = { request.getRequest_data().getStringJson(), map.get(Parameters.FROM), map.get(Parameters.TO) };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_VIDEO_SUBJECT_MAPPING_DATA_READ_PN, queryParams);
            String count = null;
            if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
                count = dao.getCount(SibConstants.SqlMapper.SQL_VIDEO_SUBJECT_MAPPING_DATA_READ_PN_COUNT, queryParams);
            }

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateVideoDetails", method = RequestMethod.POST)
    public ResponseEntity<Response> updateVideoDetails(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            ObjectMapper mapper = new ObjectMapper();
            ManageVideoModel manageVideoModel = null;
            try {
                manageVideoModel = mapper.readValue(request.getRequest_data().getStringJson(), ManageVideoModel.class);
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Object[] queryParams = null;
            boolean insertFlag = false;
            if (manageVideoModel != null) {
                queryParams = new Object[] { manageVideoModel.getSubject_category_id(), manageVideoModel.getSubject_category_name(), manageVideoModel
                    .getDescription(), manageVideoModel.getActive() };
                insertFlag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_VIDEO_SUBJECT_MAPPING_DATA_UPDATE, queryParams);
            }

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), insertFlag);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/deleteVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteVideo(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String msg = "";
            String vid = request.getRequest_data().getVid();
            String authorId = request.getRequest_data().getAuthorID();

            if (vid != null && vid.length() > 0 && authorId != null && authorId.length() > 0) {
                boolean status = deleteVideo(vid, authorId);
                if (status) {
                    activiLogService.insertActivityLog(new ActivityLogData(SibConstants.TYPE_VIDEO, "D", "You have deleted video", authorId, null));
                    msg = "Delete video success";
                    response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), msg);
                } else {
                    msg = "Delete video fail";
                    response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), msg);
                }
            } else {
                msg = "Missing vid or authorId. Check it again.";
                response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     *
     * @param vid
     * @param authorId
     * @return
     */
    private boolean deleteVideo(final String vid, final String authorId) {
        boolean flag = false;
        TransactionStatus status = null;
        try {
            TransactionDefinition def = new DefaultTransactionDefinition();
            status = transactionManager.getTransaction(def);
            Object[] queryParams = new Object[] { vid };

            // delete video from Video_Comment
            String entityName = SibConstants.SqlMapperBROT43.SQL_DELETE_VIDEO_COMMENT;
            dao.insertUpdateObject(entityName, queryParams);

            // delete video from Video_Like
            // entityName = SibConstants.SqlMapperBROT43.SQL_DELETE_VIDEO_LIKE;
            // dao.insertUpdateObject(entityName, queryParams);

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

            if (!CollectionUtils.isEmpty(readObject)) {
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
            e.printStackTrace();
            if (status != null) {
                transactionManager.rollback(status);
            }
            flag = false;
        }
        return flag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/createVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> createVideo(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            List<ManageVideoModel> newVideoDetails = new ArrayList<ManageVideoModel>();
            ObjectMapper mapper = new ObjectMapper();
            try {
                newVideoDetails = mapper.readValue(request.getRequest_data().getStringJson(), new TypeReference<List<ManageVideoModel>>() {
                });
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String entityName = SibConstants.SqlMapper.SQL_VIDEO_SUBJECT_MAPPING_DATA_INSERT;

            Map<String, String> queryParams = new HashMap<String, String>();
            String active = null;
            boolean flag = true;
            if (!CollectionUtils.isEmpty(newVideoDetails)) {
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

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), flag);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/uploadVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> uploadVideo(@RequestBody final RequestData request) {
        String entityName = null;
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
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

            int vid = 0;
            if (queryParams1 != null) {
                String svid = queryParams1.get("vid");
                if (svid != null && !"".equals(svid)) {
                    vid = Integer.parseInt(svid);
                }
            }
            if (!status) {
                vid = 0;
            } else if (!tagStatus) {
                vid = 0;
            }

            response = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), vid);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideoInfo", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoInfo(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = new Object[] { request.getRequest_data().getVid(), request.getRequest_data().getVid() };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_GET_VIDEO, queryParams);
            List<Object> readObject1 = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_GET_TAGS, queryParams);

            Map<String, Object> tags = null;
            if (!CollectionUtils.isEmpty(readObject1)) {
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
            }

            Map<String, Object> mymap = new HashMap<String, Object>();
            mymap.put("tags", readObject1);

            readObject.add(mymap);

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoCommentsPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoCommentsPN(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String entityName = null;

            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
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

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoComments", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoComments(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String entityName = null;

            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            Map<String, String> queryParams = new HashMap<String, String>();

            queryParams.put("vid", request.getRequest_data().getVid());

            entityName = SibConstants.SqlMapper.SQL_SIB_GET_COMMENTS;

            List<Object> readObject = dao.readObjects(entityName, queryParams);

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/editVideoInfo", method = RequestMethod.POST)
    public ResponseEntity<Response> editVideoInfo(@RequestBody final RequestData request) {

        String entityName = null;
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
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
                    dao.insertUpdateObject(entityName, queryParams1);
                }
            }
            response = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), request.getRequest_data().getVid());
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosWithTopicPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosWithTopicPN(@RequestBody final RequestData request) {

        String entityName = null;
        SimpleResponse response = null;
        try {
            CommonUtil util = CommonUtil.getInstance();

            Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

            Object[] queryParams = { request.getRequest_data().getSubjectId(), request.getRequest_data().getTopicId(), map.get(Parameters.FROM), map
                .get(Parameters.TO) };

            entityName = SibConstants.SqlMapper.SQL_GET_VIDEO_PN;

            List<Object> readObject1 = null;
            List<Object> readObject = dao.readObjects(entityName, queryParams);
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
                    e.printStackTrace();
                }
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
                ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
                return entity;
            }

            Map<String, Object> mymap = new HashMap<String, Object>();
            mymap.put("tags", readObject1);
            String count = null;
            readObject.add(mymap);
            if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
                count = dao.getCount(SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_TOPIC_PN_COUNT, queryParams);
            }

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosWithTopic", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosWithTopic(@RequestBody final RequestData request) {

        String entityName = null;
        SimpleResponse response = null;
        try {
            Map<String, String> queryParams = new HashMap<String, String>();

            queryParams.put("topicId", request.getRequest_data().getTopicId().trim());

            entityName = SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_TOPIC;

            List<Object> readObject1 = null;
            List<Object> readObject = dao.readObjects(entityName, queryParams);
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
                    e.printStackTrace();
                }
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
                ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
                return entity;
            }

            Map<String, Object> mymap = new HashMap<String, Object>();
            mymap.put("tags", readObject1);

            readObject.add(mymap);

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideos", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideos(@RequestParam final long uid, @RequestParam final int offset) {

        String entityName = null;
        SimpleResponse response = null;
        try {
            // Map<String, String> queryParams = new HashMap<String, String>();
            Object[] queryParams = new Object[] { uid, offset };

            entityName = SibConstants.SqlMapperBROT43.SQL_GET_VIDEOS;
            // List<Object> readObject1 = null;
            List<Object> readObject = dao.readObjects(entityName, queryParams);
            if (!CollectionUtils.isEmpty(readObject)) {

                // Get total count video of user
                List<Object> countObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_COUNT_VIDEOS, new Object[] { uid });
                response = new SimpleResponse(SibConstants.SUCCESS, "videos", "getVideos", readObject, ((Map<String, Long>) countObject.get(0))
                    .get("numVideos")
                    .toString());
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "videos", "getVideos", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideos", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideosList", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosList(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String entityName = null;

            Map<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("subjectId", request.getRequest_data().getSubjectId());
            if (request.getRequest_data().getSubjectId().equals("0")) {
                entityName = SibConstants.SqlMapper.SQL_GET_VIDEOS_LIST;
            } else {
                entityName = SibConstants.SqlMapper.SQL_GET_VIDEOS_LIST_SUBID;
            }

            List<Object> readObject = dao.readObjects(entityName, queryParams);

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getHistoryVideosList", method = RequestMethod.GET)
    public ResponseEntity<Response> getHistoryVideosList(@RequestParam("uid") final String uid) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = new Object[] { "" + uid };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_HISTORY_VIDEOS_LIST, queryParams);
            response = new SimpleResponse(SibConstants.SUCCESS, readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getHistoryVideosList", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/clearHistoryVideosList", method = RequestMethod.GET)
    public ResponseEntity<Response> clearHistoryVideosList(@RequestParam("uid") final String uid, @RequestParam("vid") final String vid) {
        SimpleResponse response = null;
        try {
            String entityName = null;
            Object[] queryParams = null;
            if (vid != null && !"".equals(vid)) {
                queryParams = new Object[] { uid, vid };
                entityName = SibConstants.SqlMapper.SQL_DELETE_HISTORY_VIDEO_BY_VID;
            } else {
                queryParams = new Object[] { uid };
                entityName = SibConstants.SqlMapper.SQL_CLEAR_HISTORY_VIDEOS_LIST;
            }

            boolean deleteObject = dao.insertUpdateObject(entityName, queryParams);
            if (deleteObject) {
                response = new SimpleResponse("true", deleteObject);
            } else {
                response = new SimpleResponse("false", "History is not exists video id " + vid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "clearHistoryVideosList", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosListPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosListPN(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
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

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosPN(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String entityName = null;
            CommonUtil util = CommonUtil.getInstance();

            Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

            Map<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("from", map.get("from"));
            queryParams.put("to", map.get("to"));

            entityName = SibConstants.SqlMapper.SQL_SIB_GET_VIDEOS_PN;

            List<Object> readObject1 = null;
            List<Object> readObject = dao.readObjects(entityName, queryParams);
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
                    e.printStackTrace();
                }
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
                ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
                return entity;
            }

            Map<String, Object> mymap = new HashMap<String, Object>();
            mymap.put("tags", readObject1);
            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosWithSubTopic", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosWithSubTopic(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String entityName = null;

            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            Map<String, String> queryParams = new HashMap<String, String>();

            queryParams.put("vid", request.getRequest_data().getVid());
            queryParams.put("subjectId", request.getRequest_data().getSubjectId().trim());
            queryParams.put("topic", request.getRequest_data().getTopic().trim());
            queryParams.put("subtopic", request.getRequest_data().getSubtopic().trim());

            entityName = SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_SUB_TOPIC;

            List<Object> readObject1 = null;
            List<Object> readObject = dao.readObjects(entityName, queryParams);
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
                    e.printStackTrace();
                }
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
                return new ResponseEntity<Response>(response, HttpStatus.OK);
            }

            Map<String, Object> mymap = new HashMap<String, Object>();
            mymap.put("tags", readObject1);
            readObject.add(mymap);

            response = new SimpleResponse("true", request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosWithSubTopicPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosWithSubTopicPN(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String entityName = null;

            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
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
                    e.printStackTrace();
                }
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
                ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
                return entity;
            }

            String count = null;
            if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
                count = dao.getCount(SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_SUB_TOPIC_PN_COUNT, new Object[] { request
                    .getRequest_data()
                    .getSubject()
                    .trim(), request.getRequest_data().getSubtopic().trim(), request.getRequest_data().getTopic().trim() });
            }

            Map<String, Object> mymap = new HashMap<String, Object>();
            mymap.put("tags", readObject1);
            readObject.add(mymap);

            response = new SimpleResponse("true", request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosWithSubjectPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosWithSubjectPN(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String entityName = null;

            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
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
                    e.printStackTrace();
                }
            } else {
                response = new SimpleResponse("true", request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
                ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
                return entity;
            }

            Map<String, Object> mymap = new HashMap<String, Object>();
            mymap.put("tags", readObject1);

            readObject.add(mymap);
            String count = null;
            if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
                count = dao.getCount(SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_SUBJECT_PN_COUNT, new Object[] { request
                    .getRequest_data()
                    .getSubjectId()
                    .trim() });
            }

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getVideosWithSubject", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosWithSubject(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String entityName = null;

            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            Map<String, String> queryParams = new HashMap<String, String>();

            queryParams.put("subjectId", request.getRequest_data().getSubjectId().trim());

            entityName = SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_WITH_SUBJECT;

            List<Object> readObject1 = null;
            List<Object> readObject = dao.readObjects(entityName, queryParams);
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
                    e.printStackTrace();
                }
            } else {
                response = new SimpleResponse("true", request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
                ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
                return entity;
            }

            Map<String, Object> mymap = new HashMap<String, Object>();
            mymap.put("tags", readObject1);
            readObject.add(mymap);

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/searchVideos", method = RequestMethod.POST)
    public ResponseEntity<Response> searchVideos(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            String entityName = "";
            Object[] queryParams = null;
//            String term = StringEscapeUtils.escapeJava(request.getRequest_data().getKeySearch().trim());
            String term = request.getRequest_data().getKeySearch().trim();
            term = term.replace("'", "\\'");
            int subjectId = request.getRequest_data().getSubjectId() != null ? Integer.parseInt(request.getRequest_data().getSubjectId()) : 0;
            int offset = request.getRequest_data().getOffset() != null ? Integer.parseInt(request.getRequest_data().getOffset()) : 0;
            int type = request.getRequest_data().getType() != null ? Integer.parseInt(request.getRequest_data().getType()) : 0;

            if (subjectId == 0) {
                queryParams = new Object[] { request.getRequest_data().getUid() };
                entityName = SibConstants.SqlMapperBROT126.SQL_SEARCH_VIDEOS;
            } else {
                queryParams = new Object[] { request.getRequest_data().getUid(), subjectId };
                entityName = SibConstants.SqlMapperBROT163.SQL_SEARCH_VIDEOS_WITH_SUBJECT;
            }
            String whereClause = "";
            if (type == 1) {
                whereClause = String.format(" and a.title like '%%%s%%' order by a.vid DESC limit 10 offset %d", term, offset);
            } else if (type == 2) {
                whereClause = String.format(" and a.title like '%%%s%%' order by a.numViews DESC limit 10 offset %d", term, offset);
            } else {
                whereClause = String.format(" and a.title like '%%%s%%' order by a.averageRating DESC limit 10 offset %d", term, offset);
            }

            List<Object> readObject = dao.readObjectsWhereClause(entityName, whereClause, queryParams);
            if (readObject != null && readObject.size() > 0) {
                response = new SimpleResponse(SibConstants.SUCCESS, "videos", "searchVideos", readObject);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "videos", "searchVideos", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/searchVideosPN", method = RequestMethod.POST)
    public ResponseEntity<Response> searchVideosPN(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String entityName = null;

            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
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
                e.printStackTrace();
            }

            Map<String, Object> mymap = new HashMap<String, Object>();
            mymap.put("tags", readObject1);

            readObject.add(mymap);
            String count = null;
            if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
                count = dao.getCount(SibConstants.SqlMapper.SQL_SIB_SEARCH_VIDEOS_PN_COUNT, new Object[] { "%" +
                                                                                                           request.getRequest_data().getTitle().trim() +
                                                                                                           "%" });
            }

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/searchVideosUsingTag", method = RequestMethod.POST)
    public ResponseEntity<Response> searchVideosUsingTag(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
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
                        e.printStackTrace();
                    }

                    readObject2.add(obj);
                    readObject2.add(readObject1);
                    readObject3.add(readObject2);
                }
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
                ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
                return entity;
            }

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject3);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/searchVideosUsingTagPN", method = RequestMethod.POST)
    public ResponseEntity<Response> searchVideosUsingTagPN(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
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
                        e.printStackTrace();
                    }

                    readObject2.add(obj);
                    readObject2.add(readObject1);
                    readObject3.add(readObject2);
                }
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), "No Data Found");
                return new ResponseEntity<Response>(response, HttpStatus.OK);
            }

            String count = null;
            if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
                count = dao.getCount(
                    SibConstants.SqlMapper.SQL_SIB_SEARCH_VIDEO_TAG_PN_COUNT,
                    new Object[] { request.getRequest_data().getTag().trim(), request.getRequest_data().getTag().trim() });
            }

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject3, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ResponseEntity<Response> removeVideo(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String entityName = null;

            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            Map<String, String> queryParams = new HashMap<String, String>();

            queryParams.put("vid", request.getRequest_data().getVid());

            entityName = SibConstants.SqlMapper.SQL_SIB_REMOVE_VIDEO;
            boolean status = dao.insertUpdateObject(entityName, queryParams);

            response = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), status);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/rateVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> rateVideo(@RequestBody final RequestData request) {
        String entityName = null;
        boolean status = false;
        SimpleResponse response = null;
        TransactionStatus statusDao = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            String vid = request.getRequest_data().getVid();
            String uid = request.getRequest_data().getUid();
            String rate = request.getRequest_data().getRating();

            // Return if vid or uid
            if (StringUtil.isNull(vid) || StringUtil.isNull(uid) || StringUtil.isNull(rate)) {
                response = new SimpleResponse(
                                              SibConstants.FAILURE,
                                              request.getRequest_data_type(),
                                              request.getRequest_data_method(),
                                              "Parameter cannot null or Emppty.");
                return new ResponseEntity<Response>(response, HttpStatus.OK);
            }

            TransactionDefinition def = new DefaultTransactionDefinition();
            statusDao = transactionManager.getTransaction(def);

            Object[] queryParams = new Object[] { uid, vid };

            entityName = SibConstants.SqlMapper.SQL_SIB_CHECK_RATE_VIDEO;

            List<Object> videoRated = dao.readObjects(entityName, queryParams);

            boolean isRated = videoRated.size() > 0 ? true : false;

            if (!isRated) {
                entityName = SibConstants.SqlMapper.SQL_SIB_RATE_VIDEO;
                queryParams = new Object[] { vid, uid, rate };
            } else {
                queryParams = new Object[] { rate, vid, uid };
                entityName = SibConstants.SqlMapper.SQL_SIB_RATE_UPDATE_VIDEO;
            }
            Object[] queryUpdateRate = { rate, vid, rate, vid };
            dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_AVG_RATE, queryUpdateRate);
            status = dao.insertUpdateObject(entityName, queryParams);

            transactionManager.commit(statusDao);
            logger.info("Insert Menu success " + new Date());
            response = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), request.getRequest_data().getVid());
        } catch (Exception e) {
            e.printStackTrace();
            if (transactionManager != null) {
                transactionManager.rollback(statusDao);
            }
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/rateVideoAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> rateVideoAdmission(@RequestBody final RequestData request) {
        String entityName = null;
        boolean status = false;
        SimpleResponse response = null;
        TransactionStatus statusDao = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            String vid = request.getRequest_data().getVid();
            String uid = request.getRequest_data().getUid();
            String rate = request.getRequest_data().getRating();
            // Return if vid or uid
            if (StringUtil.isNull(vid) || StringUtil.isNull(uid) || StringUtil.isNull(rate)) {
                response = new SimpleResponse(
                                              SibConstants.FAILURE,
                                              request.getRequest_data_type(),
                                              request.getRequest_data_method(),
                                              "Parameter cannot null or Emppty.");
                return new ResponseEntity<Response>(response, HttpStatus.OK);
            }
            TransactionDefinition def = new DefaultTransactionDefinition();
            statusDao = transactionManager.getTransaction(def);
            // Check user rated yet
            Object[] queryParams = new Object[] { request.getRequest_data().getUid(), request.getRequest_data().getVid() };
            List<Object> videoRated = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_CHECK_RATE_VIDEO_ADMISSION, queryParams);

            boolean isRated = videoRated.size() > 0 ? true : false;

            if (!isRated) {
                // New rating
                entityName = SibConstants.SqlMapper.SQL_SIB_RATE_VIDEO_ADMISSION;
                queryParams = new Object[] { vid, uid, rate };
                status = dao.insertUpdateObject(entityName, queryParams);

                Object[] queryUpdateRate = { rate, vid };
                dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_AVG_RATE_VIDEO_ADMISSION, queryUpdateRate);
                // Activity Log
                activiLogService.insertActivityLog(new ActivityLogData(
                                                                       SibConstants.TYPE_VIDEO,
                                                                       "C",
                                                                       "You rated a video",
                                                                       uid,
                                                                       String.valueOf(vid)));
            } else {
                Map<String, Double> object = (Map<String, Double>) videoRated.get(0);
                // Update rating
                queryParams = new Object[] { rate, vid, uid };
                entityName = SibConstants.SqlMapper.SQL_SIB_RATE_UPDATE_VIDEO_ADMISSION;
                Double rateOld = object.get("rate");
                Double rateNew = Double.parseDouble(rate);
                Object[] queryUpdateRate = { rateNew - rateOld };
                dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_AVG_RATE_VIDEO_ADMISSION_AGAIN, queryUpdateRate);
                // Activity Log
                activiLogService.insertActivityLog(new ActivityLogData(
                                                                       SibConstants.TYPE_VIDEO,
                                                                       "U",
                                                                       "You updated the rating a video",
                                                                       uid,
                                                                       String.valueOf(vid)));
            }

            transactionManager.commit(statusDao);
            logger.info("    " + new Date());

            response = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), request.getRequest_data().getVid());
        } catch (Exception e) {
            if (statusDao != null) {
                transactionManager.rollback(statusDao);
            }
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideosListByUser", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideosListByUser(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
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

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateVideo", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> updateVideo(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        TransactionStatus statusDao = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            TransactionDefinition def = new DefaultTransactionDefinition();
            statusDao = transactionManager.getTransaction(def);
            boolean status = true;
            String message = "";
            String description = request.getRequest_data().getDescription();
            if (description != null && description.length() > 1024) {
                response = new SimpleResponse(
                                              SibConstants.SUCCESS,
                                              request.getRequest_data_type(),
                                              request.getRequest_data_method(),
                                              "Description cannot longer than 1024 characters");
            } else {

                String vid = request.getRequest_data().getVid();
                Object[] queryParams = new Object[] { request.getRequest_data().getTitle(), request.getRequest_data().getDescription(), request
                    .getRequest_data()
                    .getSubjectId(), vid };
                status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_VIDEO, queryParams);
                if (status) {
                    String plid = request.getRequest_data().getPlid();
                    if (plid != null && plid.length() > 0) {
                        // remove old playlist
                        queryParams = new Object[] { vid };
                        status = dao.insertUpdateObject(SibConstants.SqlMapperBROT43.SQL_DELETE_VIDEO_PLAYLIST, queryParams);

                        // insert new playlist
                        queryParams = new Object[] { plid, vid };
                        status = dao.insertUpdateObject(SibConstants.SqlMapperBROT126.SQL_ADD_VIDEOS_PLAYLIST, queryParams);
                    } else {
                        // remove playlist if exists
                        queryParams = new Object[] { vid };
                        dao.insertUpdateObject(SibConstants.SqlMapperBROT43.SQL_DELETE_VIDEO_PLAYLIST, queryParams);
                    }
                }
                if (status) {
                    activiLogService.insertActivityLog(new ActivityLogData(SibConstants.TYPE_VIDEO, "U", "You updateded a video", request
                        .getRequest_data()
                        .getAuthorID(), String.valueOf(vid)));
                    transactionManager.commit(statusDao);
                    message = "Success";
                } else {
                    transactionManager.rollback(statusDao);
                    message = "Fail";
                }
                response = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), message);
            }
        } catch (Exception e) {
            if (statusDao != null) {
                transactionManager.rollback(statusDao);
            }
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/searchAllVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> searchAllVideo(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
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

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateViewVideo", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> updateViewVideo(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
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

            response = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), message);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateVideoWatched", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> updateVideoWatched(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
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

            response = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), message);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getIdVideoWatched", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> getIdVideoWatched(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            Map<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("uid", request.getRequest_data().getUid());

            String entityName = SibConstants.SqlMapper.SQL_GET_ID_VIDEO_USER_WATCHED;
            List<Object> readObject = dao.readObjects(entityName, queryParams);

            response = new SimpleResponse("", request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getRating", method = RequestMethod.POST)
    public ResponseEntity<Response> getRating(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String entityName = null;

            Map<String, String> queryParams = new HashMap<String, String>();

            queryParams.put("vid", request.getRequest_data().getVid());

            entityName = SibConstants.SqlMapper.SQL_GET_RATING_VIDEO;

            List<Object> readObject = dao.readObjects(entityName, queryParams);

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/checkUserRatingVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> checkUserRatingVideo(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String entityName = null;
            Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getVid() };

            entityName = SibConstants.SqlMapper.SQL_SIB_CHECK_RATE_VIDEO;

            List<Object> readObject = dao.readObjects(entityName, queryParams);

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/checkUserRatingVideoAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> checkUserRatingVideoAdmission(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getVid() };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_CHECK_RATE_VIDEO_ADMISSION, queryParams);
            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAllVideoByUserPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getAllVideoByUserPN(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String entityName = null;
            CommonUtil util = CommonUtil.getInstance();

            Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

            Object[] queryParams = { request.getRequest_data().getAuthorID(), request.getRequest_data().getAuthorID(), map.get(Parameters.FROM), map
                .get(Parameters.TO) };

            entityName = SibConstants.SqlMapper.SQL_SEARCH_ALL_VIDEO_BY_USER;

            List<Object> readObject = dao.readObjects(entityName, queryParams);

            String count = dao.getCount(SibConstants.SqlMapper.SQL_COUNT_SEARCH_ALL_VIDEO_BY_USER, queryParams);

            response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getVideoByUserSubject", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Response> getVideoByUserSubject(@RequestParam final long uId, @RequestParam final String limit, @RequestParam final String offset) {
        SimpleResponse response = null;
        try {
            Object[] paramQueryGetSubjects = { uId };
            String subjectIdResult = dao.readObjects(SibConstants.SqlMapper.SQL_GET_SUBJECT_REG, paramQueryGetSubjects).toString();

            String subjectIds = subjectIdResult.substring(subjectIdResult.indexOf("=") + 1, subjectIdResult.lastIndexOf("}"));
            if (subjectIds != null) {
                CommonUtil cmUtils = CommonUtil.getInstance();
                Map<String, String> pageLimit = cmUtils.getOffset(limit, offset);
                Object[] paramsGetVideos = { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
                String whereClause = "IN(" + subjectIds + ") LIMIT ? OFFSET ?;";
                List<Object> resultData = dao.readObjectsWhereClause(SibConstants.SqlMapper.SQL_GET_VIDEO_BY_SUBJECT, whereClause, paramsGetVideos);
                String count = String.valueOf(resultData.size());
                response = new SimpleResponse(SibConstants.SUCCESS, "GET", "getVideoByUserSubject", resultData, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideoByUserSubject", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getVideoByViews", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Response> getVideoByViews(@RequestParam final String limit, @RequestParam final String offset) {
        SimpleResponse response = null;
        try {
            CommonUtil cmUtils = CommonUtil.getInstance();
            Map<String, String> pageLimit = cmUtils.getOffset(limit, offset);
            Object[] paramsGetVideos = { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
            List<Object> resultData = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_BY_VIEW, paramsGetVideos);
            String count = String.valueOf(resultData.size());
            response = new SimpleResponse(SibConstants.SUCCESS, "GET", "getVideoByViews", resultData, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideoByViews", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getVideoByRate", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Response> getVideoByRate(@RequestParam final String limit, @RequestParam final String offset) {
        SimpleResponse response = null;
        try {
            CommonUtil cmUtils = CommonUtil.getInstance();
            Map<String, String> pageLimit = cmUtils.getOffset(limit, offset);
            Object[] paramsGetVideos = { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
            List<Object> resultData = dao.readObjects(SibConstants.SqlMapper.SQL_VIDEO_BY_RATE, paramsGetVideos);
            String count = String.valueOf(resultData.size());
            response = new SimpleResponse(SibConstants.SUCCESS, "GET", "getVideoByRate", resultData, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideoByRate", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getVideoPlaylistNewest", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Response> getVideoPlaylistNewest(@RequestParam final String limit, @RequestParam final String offset) {
        SimpleResponse response = null;
        try {
            CommonUtil cmUtil = CommonUtil.getInstance();
            Map<String, String> map = cmUtil.getOffset(limit, offset);
            Object[] params = { Integer.parseInt(map.get("limit")), Integer.parseInt(map.get("offset")) };
            List<Object> resultData = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_PLAYLIST_NEWEST, params);
            String count = String.valueOf(resultData.size());
            response = new SimpleResponse(SibConstants.SUCCESS, "GET", "getVideoPlaylistNewest", resultData, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideoPlaylistNewest", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getVideoStudentSubscribe", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Response> getVideoStudentSubscribe(@RequestParam final long userId, @RequestParam final String subjectId,
            @RequestParam final String limit, @RequestParam final String offset) {
        SimpleResponse response = null;
        try {
            CommonUtil cmUtil = CommonUtil.getInstance();
            Map<String, String> map = cmUtil.getOffset(limit, offset);
            Object[] params = { userId };
            String whereClause;
            if (StringUtils.isEmpty(subjectId) || subjectId == null) {
                whereClause = String
                    .format("AND S.Subcribe = 'Y' LIMIT %d OFFSET %d;", Integer.parseInt(map.get("limit")), Integer.parseInt(map.get("offset")));
            } else {
                whereClause = String.format(
                    "AND V.subjectId IN(%s) AND S.Subcribe = 'Y' LIMIT %d OFFSET %d;",
                    subjectId,
                    Integer.parseInt(map.get("limit")),
                    Integer.parseInt(map.get("offset")));
            }
            List<Object> resultData = dao.readObjectsWhereClause(SibConstants.SqlMapper.SQL_GET_VIDEO_STUDENT_SUBCRIBE, whereClause, params);
            if (!CollectionUtils.isEmpty(resultData)) {
                String count = String.valueOf(resultData.size());
                response = new SimpleResponse(SibConstants.SUCCESS, "GET", "getVideoStudentSubcribe", resultData, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideoStudentSubcribe", SibConstants.NO_DATA, "0");
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/setSubscribeMentor", method = RequestMethod.POST)
    public ResponseEntity<Response> setSubscribeMentor(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
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

            response = new SimpleResponse("" + status, message, request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);

    }

    public Map<String, String> getParamsSubcribe(final String sql, final String sql2, final Object[] queryParams, final RequestData request) throws Exception {
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
        SimpleResponse response = null;
        try {
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
            response = new SimpleResponse(SibConstants.SUCCESS, "count", request_data_method, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getCountFactory", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getCountHomeCategory", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Response> getCountHomeCategory(@RequestParam final long userId) {
        SimpleResponse response = null;
        try {
            String entityName = SibConstants.SqlMapper.SQL_COUNT_HOME_CATEGORY;
            Object[] params = { userId, userId, userId };
            Object count = dao.readObjects(entityName, params);
            response = new SimpleResponse(SibConstants.SUCCESS, "count", "getCountFactory", count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getCountHomeCategory", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getUnWatchedVideoSubcribe")
    @ResponseBody
    public ResponseEntity<Response> getUnWatchedVideoSubcribe(@RequestParam final long userId) {
        SimpleResponse response = null;
        try {
            String entityName = SibConstants.SqlMapper.SQL_GET_VIDEO_UNWATCHED;
            Object[] params = { userId, userId };
            List<Object> dataResult = dao.readObjects(entityName, params);
            String count = String.valueOf(dataResult.size());
            response = new SimpleResponse(SibConstants.SUCCESS, "video", "getUnWatchedVideoSubcribe", dataResult, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getUnWatchedVideoSubcribe", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getMentorSubscribed")
    @ResponseBody
    public ResponseEntity<Response> getMentorSubscribe(@RequestParam final long userId, @RequestParam final String limit, @RequestParam final String offset) {
        SimpleResponse response = null;
        try {
            String entityName = SibConstants.SqlMapper.SQL_GET_ALL_MENTOR_SUBSCRIBED;
            CommonUtil cmUtil = CommonUtil.getInstance();
            Map<String, String> map = cmUtil.getOffset(limit, offset);
            Object[] params = { userId, Integer.parseInt(map.get("limit")), Integer.parseInt(map.get("offset")) };
            List<Object> dataResult = dao.readObjects(entityName, params);
            String count = String.valueOf(dataResult.size());
            response = new SimpleResponse(SibConstants.SUCCESS, "video", "getMentorSubscribe", dataResult, count);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getMentorSubscribe", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideosBySubject", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideosBySubject(@RequestParam final long userid, @RequestParam final int videoType, @RequestParam final long subjectid,
            @RequestParam final int offset) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = new Object[] { userid, subjectid, offset };
            String entityName = "";
            if (videoType == 1) {
                entityName = SibConstants.SqlMapperBROT43.SQL_GET_NEWEST_VIDEOS_BY_SUBJECT;
            } else if (videoType == 2) {
                entityName = SibConstants.SqlMapperBROT43.SQL_GET_TOP_VIEWED_VIDEOS_BY_SUBJECT;
            } else {
                entityName = SibConstants.SqlMapperBROT43.SQL_GET_TOP_RATED_VIDEOS_BY_SUBJECT;
            }

            List<Object> readObject = dao.readObjects(entityName, queryParams);
            if (readObject != null && readObject.size() > 0) {
                List<Object> result = new ArrayList<Object>();
                List<Object> playlist = null;
                // get Playlist information of video
                Map<String, Object> map = null;
                Map<String, Object> tmp = new HashMap<String, Object>();
                for (Object object : readObject) {
                    map = (Map<String, Object>) object;
                    playlist = dao.readObjects(SibConstants.SqlMapperBROT163.SQL_GET_PLAYLIST_INFO_OF_VIDEO, new Object[] { map.get("vid") });
                    if (playlist != null && playlist.size() > 0) {
                        for (Object item : playlist) {
                            tmp = (Map<String, Object>) item;
                            map.put("plid", tmp.get("plid"));
                            map.put("playlistname", tmp.get("name"));
                        }
                    } else {
                        map.put("plid", null);
                        map.put("playlistname", null);
                    }
                    result.add(map);
                }
                response = new SimpleResponse(SibConstants.SUCCESS, "videos", "getVideosBySubject", result);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "videos", "getVideosBySubject", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideosBySubject", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideosTopRated", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideosTopRated(@RequestParam final long uid, @RequestParam final int offset) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = new Object[] { uid, offset };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapperBROT43.SQL_GET_VIDEOS_TOP_RATED, queryParams);
            if (readObject != null && readObject.size() > 0) {
                response = new SimpleResponse(SibConstants.SUCCESS, "videos", "getVideosTopRated", readObject);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "videos", "getVideosTopRated", SibConstants.NO_DATA);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideosTopRated", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideosTopViewed", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideosTopViewed(@RequestParam final long uid, @RequestParam final int offset) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = new Object[] { uid, offset };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapperBROT43.SQL_GET_VIDEOS_TOP_VIEWED, queryParams);
            if (readObject != null && readObject.size() > 0) {
                response = new SimpleResponse(SibConstants.SUCCESS, "videos", "getVideosTopViewed", readObject);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "videos", "getVideosTopViewed", SibConstants.NO_DATA);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideosTopViewed", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideosRecently/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideosRecently(@PathVariable(value = "id") final long id) {
        Object[] queryParams = new Object[] { "" + id };
        SimpleResponse response = null;
        try {

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapperBROT43.SQL_GET_VIDEOS_RECENTLY, queryParams);
            if (readObject != null && readObject.size() > 0) {
                response = new SimpleResponse(SibConstants.SUCCESS, "videos", "getVideosRecently", readObject);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "videos", "getVideosRecently", SibConstants.NO_DATA);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideosRecently", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/insertVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> insertVideo(@RequestBody final RequestData request) {
        String description = request.getRequest_data().getDescription();
        SimpleResponse response = null;
        try {
            String title = request.getRequest_data().getTitle();
            String duration = request.getRequest_data().getRunningTime();
            String subjectId = request.getRequest_data().getSubjectId();
            if (title == null || title.isEmpty()) {
                response = new SimpleResponse(SibConstants.FAILURE, "videos", "insertVideo", "Title can not be empty");
            } else if (duration == null || duration.isEmpty()) {
                response = new SimpleResponse(SibConstants.FAILURE, "videos", "insertVideo", "Running time can not be empty");
            } else if (subjectId == null || subjectId.isEmpty() || Integer.parseInt(subjectId) == 0) {
                response = new SimpleResponse(SibConstants.FAILURE, "videos", "insertVideo", "Subject is required");
            } else if (description != null && description.length() > 1024) {
                response = new SimpleResponse(SibConstants.FAILURE, "videos", "insertVideo", "Description can not longer than 1024 characters");
            } else {
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
                    queryParams = new Object[] { request.getRequest_data().getTitle(), request.getRequest_data().getDescription(), request
                        .getRequest_data()
                        .getUrl(), request.getRequest_data().getRunningTime(), request.getRequest_data().getImage(), request.getRequest_data().getSubjectId(), authorId };
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
                    activiLogService.insertActivityLog(new ActivityLogData(
                                                                           SibConstants.TYPE_VIDEO,
                                                                           "C",
                                                                           "You uploaded a new video",
                                                                           String.valueOf(authorId),
                                                                           String.valueOf(vid)));
                    transactionManager.commit(status);
                    response = new SimpleResponse(SibConstants.SUCCESS, "videos", "insertVideo", "Success");
                } catch (Exception e) {
                    response = new SimpleResponse(SibConstants.SUCCESS, "videos", "insertVideo", "Failed");
                    transactionManager.rollback(status);
                    logger.debug(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideosPlaylist", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideosPlaylist(@RequestParam final long uid) {
        String entityName = null;
        SimpleResponse response = null;
        try {
            Object[] queryParams = new Object[] { uid };

            entityName = SibConstants.SqlMapperBROT43.SQL_GET_VIDEOS_PLAYLIST;

            List<Object> readObject = dao.readObjects(entityName, queryParams);

            response = new SimpleResponse(SibConstants.SUCCESS, "videos", "getVideosPlaylist", readObject);

        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideosPlaylist", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoById", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoById(final long vid, final long userid) {
        String entityName = SibConstants.SqlMapperBROT43.SQL_GET_VIDEO_BY_ID;
        Object[] queryParams = { vid, userid };
        SimpleResponse response = null;
        try {
            List<Object> readObject = dao.readObjects(entityName, queryParams);
            if (readObject != null && readObject.size() > 0) {
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "getVideoById", readObject);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "getVideoById", SibConstants.NO_DATA);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideoByIdAndType", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoById/{vid}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoById(@PathVariable(value = "vid") final long vid, @PathVariable(value = "type") final String type) {
        String entityName = null;
        SimpleResponse response = null;
        try {
            Object[] queryParams = { vid };
            if (Parameters.TYPE_PLAY_SOLO_VIDEO.equalsIgnoreCase(type)) {
                entityName = SibConstants.SqlMapper.SQL_GET_USER_POST_VIDEO;
            } else {
                entityName = SibConstants.SqlMapper.SQL_GET_VIDEOS_BY_PLAYLIST;
            }
            List<Object> readObject = dao.readObjects(entityName, queryParams);
            response = new SimpleResponse(SibConstants.SUCCESS, "Video", "GetVideoById", readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideoById", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getVideoRecommend", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoRecommend(@RequestParam final long studentId) {
        SimpleResponse response = null;
        try {
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
            response = new SimpleResponse(SibConstants.SUCCESS, "video", "getVideoRecommend", readObject);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideoRecommend", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoBySubject", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoBySubject(@RequestParam final long userId, @RequestParam final String subjectId, @RequestParam final String limit,
            @RequestParam final String offset) {
        SimpleResponse response = null;
        try {
            Object result = getVideosFactory(subjectId, userId, limit, offset);
            response = new SimpleResponse(SibConstants.SUCCESS, "video", "getVideoBySubject", result);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideoBySubject", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     *
     * @param subjectId
     * @param userId
     * @param limit
     * @param offset
     * @return
     * @throws Exception
     */
    private Map<String, Object> getVideosFactory(final String subjectId, final long userId, final String limit, final String offset) throws Exception {
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
            // Get All Video By View If Don't Login
            if (subjectId.isEmpty() || subjectId.equals("-1")) {
                params = new Object[] { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
                List<Object> resultDataRecommended = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_BY_VIEW, params);
                List<Object> resultRecently = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_PLAYLIST_NEWEST, params);
                map.put("recommended", resultDataRecommended);
                map.put("recently", resultRecently);
            } else {
                String childSubjectId = CommonUtil.getAllChildCategory("" + subjectId, getAllSubjectIdCategory());
                params = new Object[] { childSubjectId, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
                List<Object> resultDataRecommended = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_VIEW_BY_SUBJECT, params);
                map.put("recommended", resultDataRecommended);
                String clauseWhere = formatQueryGetVideoPlaylist("bySubjectNotLogin", userId, childSubjectId, limit, offset);
                // params = new Object[] { childSubjectId, childSubjectId,
                // Integer.parseInt(pageLimit.get("limit")),
                // Integer.parseInt(pageLimit.get("offset")) };
                List<Object> resultRecently = dao.readObjectsWhereClause(SibConstants.SqlMapper.VIDEO_PLAYLIST_NEWEST_BY_SUBJECT, clauseWhere, new Object[] {});
                map.put("recently", resultRecently);
            }
        } else if (subjectId.equals("-1")) {
            if (subjectIds != null && !StringUtils.isEmpty(subjectIds)) {
                params = new Object[] { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
                String whereClause = "WHERE V.subjectId IN(" + subjectIds + ") ORDER BY V.timeStamp DESC LIMIT ? OFFSET ?;";
                List<Object> resultDataRecommended = dao.readObjectsWhereClause(SibConstants.SqlMapper.SQL_GET_VIDEO_BY_SUBJECT, whereClause, params);
                map.put("recommended", resultDataRecommended);
            } else {
                params = new Object[] { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
                List<Object> resultDataRecommended = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_BY_VIEW, params);
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
                    multiValueMap.put(obj.get("userid"), obj);
                }
            }
            Object[] key = multiValueMap.keySet().toArray();
            for (int i = 0; i < multiValueMap.keySet().size(); i++) {
                readObject.add(multiValueMap.get(key[i]));
            }
            map.put("recommended_for_you", readObject);
        } else if (isValidatedForm(userId, subjectId)) {
            if (subjectIds != null && !StringUtils.isEmpty(subjectIds)) {
                // Check subjectId contains in subjects student registered
                // String[] subjects = subjectIds.split(",");
                // if (ArrayUtils.contains(subjects, subjectId)) {
                    params = new Object[] { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };

                    // Get child category by subjectId
                    String childSubjectId = CommonUtil.getAllChildCategory("" + subjectId, getAllSubjectIdCategory());

                    String whereClause = "WHERE V.subjectId IN (" + childSubjectId + ") ORDER BY V.timeStamp DESC LIMIT ? OFFSET ?;";
                    List<Object> resultDataRecommended = dao.readObjectsWhereClause(SibConstants.SqlMapper.SQL_GET_VIDEO_BY_SUBJECT, whereClause, params);
                    map.put("recommended", resultDataRecommended);
                    params = new Object[] { userId, childSubjectId, userId, childSubjectId, Integer.parseInt(pageLimit.get("limit")), Integer
                        .parseInt(pageLimit.get("offset")) };
                    String clauseWhere = formatQueryGetVideoPlaylist("bySubjectLogin", userId, childSubjectId, limit, offset);
                    List<Object> resultRecently = dao
                        .readObjectsWhereClause(SibConstants.SqlMapper.SQL_NEW_VIDEO_PLAYLIST_MENTOR_SUBSCRIBED_BY_SUB, clauseWhere, new Object[] {});
                    map.put("recently", resultRecently != null ? resultRecently : null);

                    String entityName = SibConstants.SqlMapper.SQL_VIDEO_RECOMMENDED_FOR_YOU_WITH_SUB_ID;
                    params = new Object[] { childSubjectId, userId };
                    List<Object> results = dao.readObjects(entityName, params);
                    MultiValueMap multiValueMap = new MultiValueMap();
                    ArrayList<Object> readObject = new ArrayList<>();
                    if (results != null) {
                        for (int i = 0; i < results.size(); i++) {
                            Map obj = (Map) results.get(i);
                            multiValueMap.put(obj.get("userid"), obj);
                        }
                    }
                    Object[] key = multiValueMap.keySet().toArray();
                    for (int i = 0; i < multiValueMap.keySet().size(); i++) {
                        readObject.add(multiValueMap.get(key[i]));
                    }
                    map.put("recommended_for_you", readObject);
                // }
                // }
            } else {
                params = new Object[] { subjectId, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
                List<Object> resultDataRecommended = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_VIEW_BY_SUBJECT, params);
                map.put("recommended", resultDataRecommended);
            }
        }

        return map;
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getNewestVideoBySubject", method = RequestMethod.GET)
    public ResponseEntity<Response> getNewestVideoBySubject(@RequestParam final String subjectId, @RequestParam final String limit,
            @RequestParam final String offset) {
        SimpleResponse response = null;
        try {
            Map<String, String> pageLimit = CommonUtil.getInstance().getOffset(limit, offset);
            Object[] params = null;
            String entityName = "";
            params = new Object[] { Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
            String count = "0";
            if (subjectId.equals("-1")) {
                entityName = SibConstants.SqlMapper.SQL_GET_VIDEO_NEWEST;
                List<Object> readObjects = dao.readObjects(entityName, params);
                if (!CollectionUtils.isEmpty(readObjects)) {
                    count = String.valueOf(readObjects.size());
                    response = new SimpleResponse(SibConstants.SUCCESS, "video", "getNewestVideoBySubject", readObjects, count);
                } else {
                    response = new SimpleResponse(SibConstants.SUCCESS, "video", "getNewestVideoBySubject", SibConstants.NO_DATA, count);
                }

            } else if (!StringUtils.isEmpty(subjectId)) {
                entityName = SibConstants.SqlMapper.SQL_GET_NEWEST_VIDEO_SUBJECT;
                String whereClause = "V.subjectId IN(" + subjectId + ") ORDER BY timeStamp DESC LIMIT ? OFFSET ?;";
                List<Object> readObjects = dao.readObjectsWhereClause(entityName, whereClause, params);
                if (readObjects != null && readObjects.size() > 0) {
                    count = String.valueOf(readObjects.size());
                    response = new SimpleResponse(SibConstants.SUCCESS, "video", "getNewestVideoBySubject", readObjects, count);
                } else {
                    response = new SimpleResponse(SibConstants.SUCCESS, "video", "getNewestVideoBySubject", SibConstants.NO_DATA, count);
                }
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "getNewestVideoBySubject", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getNewestVideoBySubject", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);

    }

    /**
     *
     * @param userId
     * @param subjectId
     * @return
     */
    private boolean isValidatedForm(final long userId, final String subjectId) {
        if (userId != -1 && !subjectId.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public List<Object> getAllSubjectIdCategory() throws Exception {
        String entityName = SibConstants.SqlMapper.SQL_GET_ALL_SUBJECTID_CATEGORY;
        List<Object> subject = dao.readObjects(entityName, new Object[] {});
        return subject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/deleteMultipleVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteMultipleVideo(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String authorId = request.getRequest_data().getAuthorID();
            ArrayList<String> vids = request.getRequest_data().getVids();

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
                activiLogService.insertActivityLog(new ActivityLogData(SibConstants.TYPE_VIDEO, "D", "You have deleted video", authorId, null));
                String msg = String.format("Deleted success %d videos and fail %d videos", countSuccess, countFailed);
                response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), msg);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), "Missing authorId.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/addVideosToPlaylist", method = RequestMethod.POST)
    public ResponseEntity<Response> addVideosToPlaylist(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String authorId = request.getRequest_data().getAuthorID();
            String plid = request.getRequest_data().getPlid();
            ArrayList<String> vids = request.getRequest_data().getVids();
            if (authorId != null && authorId.length() > 0 && plid != null && plid.length() > 0) {
                int countSuccess = 0;
                int countFail = 0;
                boolean status = false;
                for (String vid : vids) {
                    Object[] queryParams = new Object[] { vid };
                    status = dao.insertUpdateObject(SibConstants.SqlMapperBROT43.SQL_DELETE_VIDEO_PLAYLIST, queryParams);

                    queryParams = new Object[] { plid, vid };
                    status = dao.insertUpdateObject(SibConstants.SqlMapperBROT126.SQL_ADD_VIDEOS_PLAYLIST, queryParams);
                    if (status) {
                        countSuccess++;
                    } else {
                        countFail++;
                    }
                }
                String msg = "";
                if (countSuccess == vids.size()) {
                    msg = "Success";
                } else {
                    msg = String.format("Insert success %d videos and fail %d videos into playlist", countSuccess, countFail);
                }

                response = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), msg);
            } else {
                response = new SimpleResponse(
                                              SibConstants.FAILURE,
                                              request.getRequest_data_type(),
                                              request.getRequest_data_method(),
                                              "Missing author or playlist information.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/searchVideo", method = RequestMethod.GET)
    public ResponseEntity<Response> searchVideo(@RequestParam final String keyword, @RequestParam final String type, @RequestParam final String limit,
            @RequestParam final String offset) {
        SimpleResponse response = null;
        try {
            Map<String, String> searchLimit = CommonUtil.getInstance().getOffset(limit, offset);
            String strEntity = "";
            Object[] params = null;
            String formatKeyWord = "%" + keyword + "%";
            if (type != null && !type.isEmpty()) {
                if (type.equals("playlist")) {
                    strEntity = SibConstants.SqlMapper.SQL_SEARCH_PLAYLIST;
                    params = new Object[] { formatKeyWord, Integer.valueOf(searchLimit.get("limit")), Integer.valueOf(searchLimit.get("offset")) };
                } else if (type.equals("video")) {
                    strEntity = SibConstants.SqlMapper.SQL_SEARCH_VIDEO;
                    params = new Object[] { formatKeyWord, Integer.valueOf(searchLimit.get("limit")), Integer.valueOf(searchLimit.get("offset")) };
                }
                List<Object> searchResult = dao.readObjects(strEntity, params);

                if (searchResult != null && !searchResult.isEmpty()) {
                    String lengthOfResult = "" + searchResult.size();
                    response = new SimpleResponse(SibConstants.SUCCESS, "video", "searchVideo", searchResult, lengthOfResult);
                } else {
                    response = new SimpleResponse("" + Boolean.TRUE, "video", "searchVideo", SibConstants.NO_DATA);
                }
            } else {
                response = new SimpleResponse("" + Boolean.TRUE, "video", "searchVideo", "type is not null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "searchVideo", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);

    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getAllVideos", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllVideos() {
        SimpleResponse response = null;
        try {
            String strEntity = SibConstants.SqlMapper.SQL_GET_ALL_VIDEO;

            List<Object> searchResult = dao.readObjects(strEntity, new Object[] {});

            if (searchResult != null && !searchResult.isEmpty()) {
                String lengthOfResult = "" + searchResult.size();
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "getAllVideos", searchResult, lengthOfResult);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "getAllVideos", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getAllVideos", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideosNonePlaylist", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideosNonePlaylist(final long plid, final long uid, final int offset) {
        SimpleResponse response = null;
        try {
            String strEntity = SibConstants.SqlMapperBROT163.SQL_GET_VIDEOS_NONE_PLAYLIST;

            List<Object> readObjects = dao.readObjects(strEntity, new Object[] { plid, uid, offset });

            if (readObjects != null && !readObjects.isEmpty()) {
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "getVideosNonePlaylist", readObjects);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "getVideosNonePlaylist", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideosNonePlaylist", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideosNonePlaylistBySubject", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideosNonePlaylistBySubject(final long plid, final long uid, final long subjectId, final int offset) {
        SimpleResponse response = null;
        try {
            String strEntity = SibConstants.SqlMapperBROT163.SQL_GET_VIDEOS_NONE_PLAYLIST_BY_SUBJECT;

            List<Object> readObjects = dao.readObjects(strEntity, new Object[] { plid, uid, subjectId, offset });

            if (readObjects != null && !readObjects.isEmpty()) {
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "getVideosNonePlaylistBySubject", readObjects);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "getVideosNonePlaylistBySubject", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "getVideosNonePlaylistBySubject", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/searchVideosNonePlaylist", method = RequestMethod.POST)
    public ResponseEntity<Response> searchVideosNonePlaylist(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String strEntity = "";
            Object[] params = null;
            // String term =
            // StringEscapeUtils.escapeJava(request.getRequest_data().getKeySearch().trim());
            String term = request.getRequest_data().getKeySearch().trim();
            term = term.replace("'", "\\'");
            int subjectId = request.getRequest_data().getSubjectId() != null ? Integer.parseInt(request.getRequest_data().getSubjectId()) : 0;
            int offset = request.getRequest_data().getOffset() != null ? Integer.parseInt(request.getRequest_data().getOffset()) : 0;

            if (subjectId > 0) {
                params = new Object[] { request.getRequest_data().getPlid(), request.getRequest_data().getUid(), subjectId };
                strEntity = SibConstants.SqlMapperBROT163.SQL_SEARCH_VIDEOS_NONE_PLAYLIST_WITH_SUBJECT;
            } else {
                params = new Object[] { request.getRequest_data().getPlid(), request.getRequest_data().getUid() };
                strEntity = SibConstants.SqlMapperBROT163.SQL_SEARCH_VIDEOS_NONE_PLAYLIST;
            }
            String whereClause = String.format(" and a.title like '%%%s%%' order by a.vid DESC limit 10 offset %d", term, offset);
            List<Object> readObjects = dao.readObjectsWhereClause(strEntity, whereClause, params);
            if (readObjects != null && !readObjects.isEmpty()) {
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "searchVideosNonePlaylist", readObjects);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "searchVideosNonePlaylist", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "videos", "searchVideosNonePlaylist", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/getVideoPlaylistRecently", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoPlaylistRecently(@RequestParam final long userId, @RequestParam final String limit,
            @RequestParam final String offset) {
        SimpleResponse response;
        try {
            Map<String, String> pageLimit = CommonUtil.getInstance().getOffset(limit, offset);
            if (userId < 1) {
                response = new SimpleResponse((SibConstants.SUCCESS), "video", "getVideoPlaylistRecently", SibConstants.USER_NOT_EXISTS);
                return new ResponseEntity<Response>(response, HttpStatus.OK);
            }
            Object[] params = { userId, userId, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
            List<Object> readObjects = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_PLAYLIST_RECENTLY, params);
            if (!CollectionUtils.isEmpty(readObjects)) {
                String count = String.valueOf(readObjects.size());
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "getVideoPlaylistRecently", readObjects, count);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "getVideoPlaylistRecently", SibConstants.NO_DATA, "0");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "video", "getVideoPlaylistRecently", SibConstants.NO_DATA, "0");
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    private String formatQueryGetVideoPlaylist(final String type, final long userId, final String subjectId, final String limit, final String offset) {
        String query = null;
        switch (type) {
            case "bySubjectLogin":
                query = "WHERE StudentId = " +
                        userId +
                        " AND Subcribe = 'Y' ) AND sv.subjectId IN(" +
                        subjectId +
                        ") UNION ALL SELECT sp.plid, sp.`CreateBy` authorID, sp.Image, sp.url, su.firstName, su.lastName, su.userName, sp.`Name` title, " +
                        "NULL numRatings, NULL numComments, sp.subjectId, NULL averageRating, NULL numViews, UNIX_TIMESTAMP(CreateDate) `timeStamp`, " +
                        "NULL runningTime, '2' type, count(spv.vid) countvid FROM Sib_PlayList sp, Sib_PlayList_Videos spv, Sib_Users su " +
                        "WHERE sp.plid = spv.plid AND sp.CreateBy = su.userid AND EXISTS ( SELECT 1 FROM Sib_PlayList_Videos spv WHERE sp.plid = spv.plid ) " +
                        "AND sp.`Status` = 'A' AND su.userid IN ( SELECT MentorId FROM Sib_Student_Subcribe WHERE StudentId = " +
                        userId +
                        " AND Subcribe = 'Y' ) " +
                        "AND sp.subjectId IN(" +
                        subjectId +
                        ") GROUP BY sp.plid ORDER BY timeStamp DESC LIMIT " +
                        limit +
                        " OFFSET " +
                        offset;
                break;
            case "bySubjectNotLogin":
                query = "sv.subjectId IN(" +
                        subjectId +
                        ") UNION ALL SELECT sp.plid, sp.`CreateBy` authorID, sp.Image, sp.url, su.firstName, su.lastName, su.userName, sp.`Name` title, " +
                        "NULL numRatings, NULL numComments, sp.subjectId, NULL averageRating, NULL numViews, UNIX_TIMESTAMP(CreateDate) `timeStamp`, " +
                        "NULL runningTime, '2' type, count(spv.vid) countvid FROM Sib_PlayList sp, Sib_PlayList_Videos spv, Sib_Users su " +
                        "WHERE sp.plid=spv.plid AND sp.CreateBy = su.userid AND EXISTS ( SELECT 1 FROM Sib_PlayList_Videos spv WHERE sp.plid = spv.plid ) " +
                        "AND sp.`Status` = 'A' AND sp.subjectId IN(" +
                        subjectId +
                        ") GROUP BY sp.plid ORDER BY TIMESTAMP DESC LIMIT " +
                        limit +
                        " OFFSET " +
                        offset;
                break;
        }
        return query;

    }

    @RequestMapping(value = "/getAllTitleVideoPlaylist", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllVideoTitle() {
        SimpleResponse response;
        try {
            List<Object> titles = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ALL_TITLE_VIDEO_PLAYLIST, new Object[] {});
            if (!CollectionUtils.isEmpty(titles)) {
                String count = String.valueOf(titles.size());
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "getAllTitleVideoPlaylist", titles, count);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "video", "getAllTitleVideoPlaylist", SibConstants.NO_DATA, "0");
            }
        } catch (DAOException e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "video", "getAllTitleVideoPlaylist", SibConstants.NO_DATA, "0");
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/searchVideoPlaylist", method = RequestMethod.GET)
    public ResponseEntity<Response> searchVideoPlaylist(@RequestParam final String keyword, @RequestParam final String limit, @RequestParam final String offset) {
        Map<String, String> searchLimit = CommonUtil.getInstance().getOffset(limit, offset);
        SimpleResponse response = null;
        if (!StringUtils.isEmpty(keyword)) {
            String formatKeyword = "%" + keyword + "%";
            Object[] params = { formatKeyword, formatKeyword, formatKeyword, Integer.parseInt(searchLimit.get("limit")), Integer
                .parseInt(searchLimit.get("offset")) };
            try {
                List<Object> searchResults = dao.readObjects(SibConstants.SqlMapper.SQL_SEARCH_VIDEO_PLAYLIST, params);
                if (!CollectionUtils.isEmpty(searchResults)) {
                    String count = String.valueOf(searchResults.size());
                    response = new SimpleResponse(SibConstants.SUCCESS, "video", "searchVideoPlaylist", searchResults, count);
                } else {
                    response = new SimpleResponse(SibConstants.SUCCESS, "video", "searchVideoPlaylist", SibConstants.NO_DATA, "0");
                }

            } catch (DAOException e) {
                logger.error(e.getMessage(), e.getCause());
                response = new SimpleResponse(SibConstants.FAILURE, "video", "searchVideoPlaylist", SibConstants.NO_DATA, "0");
            }
        } else {
            response = new SimpleResponse(SibConstants.SUCCESS, "video", "searchVideoPlaylist", SibConstants.NO_DATA, "0");
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

}
