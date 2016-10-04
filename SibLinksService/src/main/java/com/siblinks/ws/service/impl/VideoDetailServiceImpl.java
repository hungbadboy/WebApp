package com.siblinks.ws.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.SibConstants;
import com.siblinks.ws.util.StringUtil;

@RestController
@RequestMapping("/siblinks/services/videodetail")
public class VideoDetailServiceImpl implements VideoDetailService {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ObjectDao dao;

    private final LoggedInChecker loggedInChecker;

    @Autowired
    VideoDetailServiceImpl(final LoggedInChecker loggedInChecker) {
        this.loggedInChecker = loggedInChecker;
    }

    @Override
    @RequestMapping(value = "/getVideoDetailById/{vid}", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoDetailById(@PathVariable(value = "vid") final long vid) {
        String entityName = SibConstants.SqlMapper.SQL_GET_VIDEODETAIL_BY_ID;
        Object[] queryParams = { vid };

        List<Object> readObject = dao.readObjects(entityName, queryParams);
        SimpleResponse reponse = new SimpleResponse("" + true, "Video", "getVideoById", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getCommentVideoById/{vid}", method = RequestMethod.GET)
    public ResponseEntity<Response> getCommentVideoById(@PathVariable(value = "vid") final long vid) {
        String entityName = SibConstants.SqlMapper.SQL_GET_COMMENT_VIDEO_BY_VID;
        Object[] queryParams = { vid };

        List<Object> readObject = dao.readObjects(entityName, queryParams);
        SimpleResponse reponse = new SimpleResponse("" + true, "Video", "getCommentVideoById", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateVideoHistory", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> updateVideoHistory(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse("" + false, "Authentication required."), HttpStatus.FORBIDDEN);
            return entity;
        }

        Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getVid() };

        boolean status = false;
        String entityName = SibConstants.SqlMapper.SQL_CHECK_USER_HISTORY_VIDEO;
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        if (readObject.size() < 1) {
            entityName = SibConstants.SqlMapper.SQL_INSERT_HISTORY_VIDEO;
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
    @RequestMapping(value = "/getVideoByCategoryId", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoByCategoryId(@RequestBody final RequestData request) {
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
        SimpleResponse reponse = new SimpleResponse("" + true, "Video", "getVideoByCategoryId", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/checkSubscribe", method = RequestMethod.GET)
    public ResponseEntity<Response> checkSubscribe(@RequestParam("mentorid") final String mentorid, @RequestParam("studentid") final String studentid) {
        String entityName = SibConstants.SqlMapper.SQL_CHECK_SUBSCRIBE;
        Object[] queryParams = { mentorid, studentid };

        List<Object> readObject = dao.readObjects(entityName, queryParams);
        SimpleResponse reponse = new SimpleResponse("" + true, "Video", "checkSubscribe", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getVideoByPlaylistId/{pid}", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoByPlaylistId(@PathVariable(value = "pid") final long vid) {

        Object[] queryParams = { vid };
        SimpleResponse reponse = null;

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEOS_BY_PLAYLIST, queryParams);
        if (readObject != null && readObject.size() > 0) {
            reponse = new SimpleResponse("" + true, "Video", "getVideoByPlaylistId", readObject);
        } else {
            reponse = new SimpleResponse("" + true, "Video", "getVideoByPlaylistId", SibConstants.NO_DATA);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getVideoDetailMentor", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoDetailMentor(final long vid, final long uid) {
        SimpleResponse reponse = null;
        if (vid > 0 && uid > 0) {
            String entityName = SibConstants.SqlMapperBROT163.SQL_GET_VIDEO_DETAIL_MENTOR;
            Object[] queryParams = { vid, uid };

            List<Object> readObject = dao.readObjects(entityName, queryParams);
            if (readObject != null && readObject.size() > 0) {
                reponse = new SimpleResponse("" + true, "Video", "getVideoById", readObject);
            } else {
                reponse = new SimpleResponse("" + true, "Video", "getVideoById", SibConstants.NO_DATA);
            }
        } else {
            reponse = new SimpleResponse("" + true, "Video", "getVideoById", "Missing vid or userid.");
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getVideoRelatedMentor", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoRelatedMentor(final long subjectId, final long uid, final int offset) {

        String entityName = SibConstants.SqlMapper.SQL_GET_ALL_SUBJECTID_CATEGORY;
        List<Object> subjects = dao.readObjects(entityName, new Object[] {});

        ArrayList<String> subIds = new ArrayList<String>();
        String Ids = getAllParentId(subjectId + "", subjects, subIds);

        String whereClause = String.format("and A.subjectId IN (%s) limit 10 offset %d", Ids, offset);
        List<Object> readObjects = dao.readObjectsWhereClause(SibConstants.SqlMapperBROT163.SQL_GET_VIDEO_RELATED_MENTOR, whereClause, new Object[] { uid });
        SimpleResponse reponse = null;
        if (readObjects != null && readObjects.size() > 0) {
            reponse = new SimpleResponse("" + true, "Video", "getVideoById", readObjects);
        } else {
            reponse = new SimpleResponse("" + true, "Video", "getVideoById", SibConstants.NO_DATA);
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    private String getAllParentId(final String subjectId, final List<?> categoryList, final ArrayList<String> subIds) {
        subIds.add(subjectId);

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
        return StringUtils.join(subIds, ",");
    }

}
