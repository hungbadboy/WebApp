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
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
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

import com.siblinks.ws.common.LoggedInChecker;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.VideoDetailService;
import com.siblinks.ws.service.VideoService;
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
@RequestMapping("/siblinks/services/videodetail")
public class VideoDetailServiceImpl implements VideoDetailService {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ObjectDao dao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    VideoDetailServiceImpl(final LoggedInChecker loggedInChecker) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoDetailById/{vid}", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoDetailById(@PathVariable(value = "vid") final long vid) {
        SimpleResponse response = null;
        TransactionStatus statusBD = null;
        try {
            TransactionDefinition def = new DefaultTransactionDefinition();
            statusBD = transactionManager.getTransaction(def);
            Object[] queryParams = { vid };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEODETAIL_BY_ID, queryParams);
            response = new SimpleResponse(SibConstants.SUCCESS, "Video", "getVideoDetailById", readObject);
            String entityName = SibConstants.SqlMapper.SQL_UPDATE_NUMVIEW_VIDEO;
            dao.insertUpdateObject(entityName, queryParams);
            transactionManager.commit(statusBD);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            transactionManager.rollback(statusBD);
            response = new SimpleResponse(SibConstants.FAILURE, "Video", "getVideoDetailById", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoAdmissionDetailById/{vid}", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoAdmissionDetailById(@PathVariable(value = "vid") final long vid) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { vid };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_ADMISSION_DETAIL_BY_ID, queryParams);
            response = new SimpleResponse(SibConstants.SUCCESS, "Video", "getVideoDetailById", readObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "videoAdmission", "getVideoAdmissionDetailById", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getCommentVideoById/{vid}", method = RequestMethod.GET)
    public ResponseEntity<Response> getCommentVideoById(@PathVariable(value = "vid") final long vid) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { vid };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_COMMENT_VIDEO_BY_VID, queryParams);
            response = new SimpleResponse(SibConstants.SUCCESS, "Video", "getCommentVideoById", readObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "videoAdmission", "getCommentVideoById", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getCommentVideoAdmissionById/{vid}", method = RequestMethod.GET)
    public ResponseEntity<Response> getCommentVideoAdmissionById(@PathVariable(value = "vid") final long vid) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { vid };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_COMMENT_VIDEO_ADMISSION_BY_VID, queryParams);
            response = new SimpleResponse(SibConstants.SUCCESS, "Video", "getCommentVideoById", readObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "videoAdmission", "getCommentVideoAdmissionById", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateVideoHistory", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> updateVideoHistory(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getVid() };

            boolean status = false;
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_CHECK_USER_HISTORY_VIDEO, queryParams);
            if (CollectionUtils.isEmpty(readObject)) {
                status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_INSERT_HISTORY_VIDEO, queryParams);
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
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "videoAdmission", "updateVideoHistory", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateViewVideoAdmission", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> updateViewVideoAdmission(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            // Map<String, String> queryParams = new HashMap<String, String>();
            // queryParams.put("vid", request.getRequest_data().getVid());
            Object[] queryParams = { request.getRequest_data().getVid() };
            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_VIEW_VIDEO_ADMISSION, queryParams);
            String message = "";
            if (status) {
                message = "Done";
            } else {
                message = "Fail";
            }

            response = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), message);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "videoAdmission", "updateViewVideoAdmission", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoByCategoryId", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoByCategoryId(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            String entityName = SibConstants.SqlMapper.SQL_GET_VIDEO_BY_SUBJECTID;
            String limit = request.getRequest_data().getLimit();
            String offset = request.getRequest_data().getOffset();
            String sid = request.getRequest_data().getSubjectId();
            String type = request.getRequest_data().getType();

            String whereClause = "";
            if (!StringUtil.isNull(type)) {

                whereClause += " ORDER BY A.TIMESTAMP DESC";
            }
            if (!StringUtil.isNull(limit)) {
                whereClause += " LIMIT " + limit;
            }
            if (!StringUtil.isNull(offset)) {
                whereClause += " OFFSET " + offset;
            }
            Object[] queryParams = { sid };

            List<Object> readObject = dao.readObjectsWhereClause(entityName, whereClause, queryParams);
            response = new SimpleResponse(SibConstants.SUCCESS, "Video", "getVideoByCategoryId", readObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "videoAdmission", "getVideoByCategoryId", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoByAdmissionId", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoByAdmissionId(@RequestParam final String aId) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { aId };

            List<Object> readObject = dao
                .readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_ADMISSION_BY_ADMISSION_ID, queryParams);
            response = new SimpleResponse(SibConstants.SUCCESS, "Video", "getVideoByAdmissionId", readObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "videoAdmission", "getVideoByAdmissionId", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/checkSubscribe", method = RequestMethod.GET)
    public ResponseEntity<Response> checkSubscribe(@RequestParam("mentorid") final String mentorid,
            @RequestParam("studentid") final String studentid) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { mentorid, studentid };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_CHECK_SUBSCRIBE, queryParams);
            response = new SimpleResponse(SibConstants.SUCCESS, "Video", "checkSubscribe", readObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "videoAdmission", "checkSubscribe", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoByPlaylistId/{pid}", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoByPlaylistId(@PathVariable(value = "pid") final long vid) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { vid };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEOS_BY_PLAYLIST, queryParams);
            if (readObject != null && readObject.size() > 0) {
                response = new SimpleResponse(SibConstants.SUCCESS, "Video", "getVideoByPlaylistId", readObject);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "Video", "getVideoByPlaylistId", SibConstants.NO_DATA);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "videoAdmission", "getVideoByPlaylistId", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoDetailMentor", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoDetailMentor(final long vid, final long uid) {
        SimpleResponse response = null;
        try {
            if (vid > 0 && uid > 0) {
                Object[] queryParams = { vid, uid };

                List<Object> readObject = dao.readObjects(SibConstants.SqlMapperBROT163.SQL_GET_VIDEO_DETAIL_MENTOR, queryParams);
                if (readObject != null && readObject.size() > 0) {
                    response = new SimpleResponse(SibConstants.SUCCESS, "Video", "getVideoById", readObject);
                } else {
                    response = new SimpleResponse(SibConstants.SUCCESS, "Video", "getVideoById", SibConstants.NO_DATA);
                }
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "Video", "getVideoById", "Missing vid or userid.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "videoAdmission", "getVideoById", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoRelatedMentor", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoRelatedMentor(final long vid, final long subjectId, final long uid, final int offset) {
        SimpleResponse response = null;
        try {
            List<Object> subjects = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ALL_SUBJECTID_CATEGORY, new Object[] {});

            ArrayList<String> subIds = new ArrayList<String>();
            String Ids = getAllParentId(subjectId + "", subjects, subIds);
            String whereClause = "";
            if (!StringUtil.isNull(Ids)) {
                whereClause += String.format(" and A.subjectId IN (%s) and A.vid != %d limit 10 offset %d", Ids, vid, offset);
            }

            List<Object> readObjects = dao.readObjectsWhereClause(
                SibConstants.SqlMapperBROT163.SQL_GET_VIDEO_RELATED_MENTOR,
                whereClause,
                new Object[] { uid });
            if (readObjects != null && readObjects.size() > 0) {
                response = new SimpleResponse(SibConstants.SUCCESS, "videos", "getVideosBySubject", readObjects);
            } else {
                response = new SimpleResponse(SibConstants.SUCCESS, "videodetail", "getVideoRelatedMentor", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "videoAdmission", "getVideoRelatedMentor", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     *
     * @param subjectId
     * @param categoryList
     * @param subIds
     * @return
     */
    private String getAllParentId(final String subjectId, final List<?> categoryList, final ArrayList<String> subIds) {
        subIds.add(subjectId);
        if (!CollectionUtils.isEmpty(categoryList)) {
            for (Object item : categoryList) {
                Map<String, Integer> map = (Map<String, Integer>) item;
                if (map != null && map.containsKey(Parameters.SUBJECT_ID)) {
                    String parentId = map.get(Parameters.PARENT_ID) + "";
                    String subId = map.get(Parameters.SUBJECT_ID) + "";
                    if (subId.equals(subjectId) && !parentId.equals("null") && parentId.trim().length() > 0) {
                        System.out.println("Have parent id");
                        getAllParentId(parentId, categoryList, subIds);
                    }
                }
            }
        }
        return StringUtils.join(subIds, ",");
    }

}
