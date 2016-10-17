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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonParser;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.MentorService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.RandomString;
import com.siblinks.ws.util.ReadProperties;
import com.siblinks.ws.util.SibConstants;
import com.siblinks.ws.util.StringUtil;

/**
 *
 *
 * @author hungpd
 * @version 1.0
 */
@RestController
@RequestMapping("/siblinks/services/mentor")
public class MentorServiceImpl implements MentorService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private HttpServletRequest context;

    @Autowired
    ObjectDao dao;

    @Override
    @RequestMapping(value = "/topMetorEachSubject", method = RequestMethod.POST)
    public ResponseEntity<Response> topMetorEachSubject(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            SimpleResponse simpleResponse = new SimpleResponse("" + Boolean.FALSE, "Authentication required.");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("subjectId", request.getRequest_data().getSubjectId());

        String entityName = SibConstants.SqlMapper.SQL_TOP_MENTOR;
        List<Object> readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public ResponseEntity<Response> getList(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            SimpleResponse simpleResponse = new SimpleResponse("" + Boolean.FALSE, "Authentication required.");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();

        if ("userid".equalsIgnoreCase(request.getRequest_data().getOrder())) {
            queryParams.put("order", "userid");
        } else if ("school".equalsIgnoreCase(request.getRequest_data().getOrder())) {
            queryParams.put("order", "school");
        } else if ("name".equalsIgnoreCase(request.getRequest_data().getOrder())) {
            queryParams.put("order", "firstName");
        } else if ("lastName".equalsIgnoreCase(request.getRequest_data().getOrder())) {
            queryParams.put("order", "lastName");
        } else {
            queryParams.put("order", request.getRequest_data().getOrder());
        }

        queryParams.put("offset", request.getRequest_data().getPage());
        queryParams.put("limit", request.getRequest_data().getLimit());

        String entityName = SibConstants.SqlMapper.SQL_MENTOR_LIST;
        List<Object> readObject = null;
        readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<Response> search(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            SimpleResponse simpleResponse = new SimpleResponse("" + Boolean.FALSE, "Authentication required.");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            return entity;
        }

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("searchKey", request.getRequest_data().getKeySearch());
        queryParams.put("from", map.get("from"));
        queryParams.put("to", map.get("to"));
        if ("userid".equalsIgnoreCase(request.getRequest_data().getOrder())) {
            queryParams.put("order", "userid");
        } else if ("school".equalsIgnoreCase(request.getRequest_data().getOrder())) {
            queryParams.put("order", "school");
        } else if ("name".equalsIgnoreCase(request.getRequest_data().getOrder())) {
            queryParams.put("order", "firstName");
        } else if ("lastName".equalsIgnoreCase(request.getRequest_data().getOrder())) {
            queryParams.put("order", "lastName");
        } else {
            queryParams.put("order", "points");
        }

        String[] fieldSearch = request.getRequest_data().getFieldSearch();
        if (fieldSearch.length == 0) {
            queryParams.put("filter", "");
        } else {
            StringBuffer strFilter = new StringBuffer("AND (");
            for (String str : fieldSearch) {
                if (strFilter.length() > 5) {
                    strFilter.append(" OR ");
                }
                strFilter.append(str + " LIKE '%" + queryParams.get("searchKey") + "%'");
            }
            strFilter.append(")");
            queryParams.put("filter", strFilter.toString());
        }
        String entityName = "";
        if ("online".equalsIgnoreCase(request.getRequest_data().getOrder())) {
            entityName = SibConstants.SqlMapper.SQL_MENTOR_LIST_FILTER_ONLINE;
        } else {
            entityName = SibConstants.SqlMapper.SQL_MENTOR_LIST_FILTER;
        }
        List<Object> readObject = null;
        readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> uploadFile(@RequestParam("uploadfile") final MultipartFile uploadfile) throws IOException {

        String filename;
        String name;
        String filepath;
        String directory = ReadProperties.getProperties("directoryImageArticle");
        String sample = ".png .jpg .gif .bmp .tga";

        name = uploadfile.getOriginalFilename();
        int index = name.indexOf(".");
        name = name.substring(index, name.length());
        boolean status = sample.contains(name.toLowerCase());

        if (directory != null && status) {
            try {
                RandomString randomName = new RandomString();
                //
                filename = randomName.random() + name;
                filepath = Paths.get(directory, filename).toString();
                // // Save the file locally
                // BufferedOutputStream stream =
                // new BufferedOutputStream(new FileOutputStream(new
                // File(filepath)));
                // stream.write(uploadfile.getBytes());
                // stream.close();
            }

            catch (Exception e) {
                logger.error(e.getMessage());
                return new ResponseEntity<Response>(HttpStatus.BAD_REQUEST);
            }

            SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE, filepath);
            return new ResponseEntity<Response>(reponse, HttpStatus.OK);
        } else {
            SimpleResponse reponse = new SimpleResponse(
                                                        "" +
                                                        Boolean.FALSE,
                                                        "Your photos couldn't be uploaded. Photos should be saved as JPG, PNG, GIF or BMP files.");
            return new ResponseEntity<Response>(reponse, HttpStatus.OK);
        }
    } // method uploadFile

    @Override
    @RequestMapping(value = "/createAboutMentor", method = RequestMethod.POST)
    public ResponseEntity<Response> createAboutMentor(@RequestBody final RequestData aboutMentor) throws FileNotFoundException {

        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("description", aboutMentor.getRequest_data_aboutMentor().getDescription());
        queryParams.put("introduce", aboutMentor.getRequest_data_aboutMentor().getIntroduce());
        if (aboutMentor.getRequest_data_aboutMentor().getImage() == null) {
            queryParams.put("image", ReadProperties.getProperties("directoryImageAvatar"));
        } else {
            queryParams.put("image", aboutMentor.getRequest_data_aboutMentor().getImage());
        }

        String entityName = SibConstants.SqlMapper.SQL_CREATE_ABOUT_MENTOR;

        boolean status = dao.insertUpdateObject(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    Boolean.TRUE,
                                                    aboutMentor.getRequest_data_type(),
                                                    aboutMentor.getRequest_data_method(),
                                                    status);
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/getAllAboutMentor", method = RequestMethod.POST)
    public ResponseEntity<Response> getAllAboutMentor(@RequestBody final RequestData aboutMentor) {

        String entityName = null;

        Map<String, String> queryParams = new HashMap<String, String>();

        entityName = SibConstants.SqlMapper.SQL_GET_ALL_ABOUT_MENTOR;

        List<Object> readObject = null;
        readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    Boolean.TRUE,
                                                    aboutMentor.getRequest_data_type(),
                                                    aboutMentor.getRequest_data_method(),
                                                    readObject);
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/deleteAboutMentor", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteAboutMentor(@RequestBody final RequestData aboutMentor) {

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("id", aboutMentor.getRequest_data_aboutMentor().getId());
        String entityName = SibConstants.SqlMapper.SQL_GET_IMAGE_ABOUT_MENTOR;

        List<Object> readObject = null;
        readObject = dao.readObjects(entityName, queryParams);

        File file = new File((String) ((Map) readObject.get(0)).get("image"));

        entityName = SibConstants.SqlMapper.SQL_DELETE_ABOUT_MENTOR;

        boolean flag = dao.insertUpdateObject(entityName, queryParams);

        String fileName = (String) ((Map) readObject.get(0)).get("image");

        if (flag && !fileName.contains("default_avatar")) {
            file.delete();
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    Boolean.TRUE,
                                                    aboutMentor.getRequest_data_type(),
                                                    aboutMentor.getRequest_data_method(),
                                                    flag);
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/updateAboutMentor", method = RequestMethod.POST)
    public ResponseEntity<Response> updateAboutMentor(@RequestBody final RequestData aboutMentor) {

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("id", aboutMentor.getRequest_data_aboutMentor().getId());
        queryParams.put("image", aboutMentor.getRequest_data_aboutMentor().getImage());
        queryParams.put("description", aboutMentor.getRequest_data_aboutMentor().getDescription());
        queryParams.put("introduce", aboutMentor.getRequest_data_aboutMentor().getIntroduce());

        String entityName = "";
        List<Object> readObject = null;
        if (aboutMentor.getRequest_data_aboutMentor().getImage() != null) {
            entityName = SibConstants.SqlMapper.SQL_GET_IMAGE_ABOUT_MENTOR;
            readObject = dao.readObjects(entityName, queryParams);

            File file = new File((String) ((Map) readObject.get(0)).get("image"));

            String fileName = (String) ((Map) readObject.get(0)).get("image");

            if (file != null && !fileName.contains("default_avatar")) {
                file.delete();
            }
            entityName = SibConstants.SqlMapper.SQL_UPDATE_ABOUT_MENTOR;
        } else {
            entityName = SibConstants.SqlMapper.SQL_UPDATE_ABOUT_MENTOR_NOT_IMAGE;
        }

        boolean flag = dao.insertUpdateObject(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    Boolean.TRUE,
                                                    aboutMentor.getRequest_data_type(),
                                                    aboutMentor.getRequest_data_method(),
                                                    flag);
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/getImageAboutMentor/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageAboutMentor(@PathVariable(value = "id") final String id) {

        Map<String, String> queryParams = new HashMap<String, String>();
        List<Object> readObject = null;

        queryParams.put("id", id);
        String entityName = SibConstants.SqlMapper.SQL_GET_IMAGE_ABOUT_MENTOR;
        String path = null;

        readObject = dao.readObjects(entityName, queryParams);
        if (((Map) readObject.get(0)).get("image") != null) {
            path = ((Map) readObject.get(0)).get("image").toString();

            RandomAccessFile t = null;
            byte[] r = null;
            try {
                t = new RandomAccessFile(path, "r");
                r = new byte[(int) t.length()];
                t.readFully(r);
            } catch (FileNotFoundException e) {
                logger.debug("file not found : " + path);
                return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
            } catch (IOException e) {
                logger.debug("Some thing wrong", e);
                return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
            }

            HttpHeaders headers = new HttpHeaders();

            return new ResponseEntity<byte[]>(r, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
        }
    }

    @Override
    @RequestMapping(value = "/getInforTopMentor", method = RequestMethod.POST)
    public ResponseEntity<Response> getInforTopMentor(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("uid", request.getRequest_data().getUid());

        String entityName = SibConstants.SqlMapper.SQL_GET_USER_BY_ID;

        List<Object> readObject = null;
        readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.siblinks.ws.service.MentorService#getNewestAnswer(com.siblinks.ws
     * .model.RequestData)
     */
    @Override
    @RequestMapping(value = "/getNewestAnswer/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response> getNewestAnswer(@PathVariable(value = "id") final long id) {
        ResponseEntity<Response> entity = null;
        // if (!AuthenticationFilter.isAuthed(context)) {
        // entity = new ResponseEntity<Response>(
        // new SimpleResponse("" + Boolean.FALSE, "Authentication required."),
        // HttpStatus.FORBIDDEN);
        // return entity;
        // }

        Object[] queryParams = { id };

        String entityName = SibConstants.SqlMapperBROT27.SQL_GET_NEWEST_MENTOR_ANSWER;

        List<Object> readObjects = null;
        readObjects = dao.readObjects(entityName, queryParams);
        System.out.println(readObjects);

        SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE, "mentor", "getNewestAnswer", readObjects);
        entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getTopMentorsMostLike", method = RequestMethod.POST)
    public ResponseEntity<Response> getTopMentorsMostLike(@RequestBody final RequestData request) {
        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }
        String limit = request.getRequest_data().getLimit();
        String offset = request.getRequest_data().getOffset();
        Object[] queryParams = {};

        String entityName = SibConstants.SqlMapper.SQL_GET_TOP_MENTORS_MOST_LIKE;

        List<Object> readObject = null;
        String whereClause = "";
        if (!StringUtil.isNull(limit)) {
            whereClause += " LIMIT " + limit;
        }
        if (!StringUtil.isNull(offset)) {
            whereClause += " OFFSET " + offset;
        }
        readObject = dao.readObjectsWhereClause(entityName, whereClause, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getStudentsSubcribe", method = RequestMethod.POST)
    public ResponseEntity<Response> getStudentsSubcribe(@RequestBody final RequestData request) {
        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }
        String limit = request.getRequest_data().getLimit();
        String offset = request.getRequest_data().getOffset();
        String mentorId = request.getRequest_data().getMentorid();
        Object[] queryParams = { mentorId };

        String entityName = SibConstants.SqlMapper.SQL_GET_STUDENT_SUBCRIBE;

        List<Object> readObject = null;
        String whereClause = "";
        whereClause += " ORDER BY S.datetime DESC";
        if (!StringUtil.isNull(limit)) {
            whereClause += " LIMIT " + limit;
        }
        if (!StringUtil.isNull(offset)) {
            whereClause += " OFFSET " + offset;
        }

        readObject = dao.readObjectsWhereClause(entityName, whereClause, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getTotalViewLikeViewByMentorId/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response> getTotalViewLikeViewByMentorId(@PathVariable(value = "id") final long id) {

        Object[] queryParams = { "" + id };

        String entityName = SibConstants.SqlMapper.SQL_GET_TOTAL_LIKE_VIEW_VIDEO;

        List<Object> readObject = null;

        readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE, "Mentor", "getTotalViewLikeViewByMentorId", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getTotalAnswersByMentorId/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response> getTotalAnswersByMentorId(@PathVariable(value = "id") final long id) {

        Object[] queryParams = { "" + id };

        String entityName = SibConstants.SqlMapper.SQL_GET_TOTAL_ANSWERS;

        List<Object> readObject = null;

        readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE, "Mentor", "getTotalAnswersByMentorId", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getTopMentorsByLikeRateSubcrible", method = RequestMethod.POST)
    public ResponseEntity<Response> getTopMentorsByLikeRateSubcrible(@RequestBody final RequestData request) {
        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }
        String limit = request.getRequest_data().getLimit();
        String offset = request.getRequest_data().getOffset();
        String type = request.getRequest_data().getType();
        String userId = request.getRequest_data().getUid();
        String content = request.getRequest_data().getContent();
        String subjectId = request.getRequest_data().getSubjectId();
        Object[] queryParams = {};

        String entityName = SibConstants.SqlMapper.SQL_GET_TOP_MENTORS_BY_LIKE_RATE_SUBS;
        if (StringUtil.isNull(userId)) {
            userId = "-1";
        }
        List<Object> readObject = null;
        String whereClause = "";
        if (!"-1".equalsIgnoreCase(userId)) {
            whereClause += ",(SELECT count(*) isSubs FROM Sib_Student_Subcribe where Subcribe ='Y' AND " +
                           " MentorId = U.userid AND StudentId =" +
                           userId +
                           ") isSubs";
        } else {
            whereClause += ",('-1') as isSubs ";
        }
        whereClause += " FROM Sib_Users U LEFT JOIN Sib_Videos V ON U.userid = V.authorID " +
                       "WHERE U.userType = 'M' GROUP BY U.userid, U.lastName, U.imageUrl, U.firstName)X ";
        if(!StringUtil.isNull(content)){
            content = StringEscapeUtils.escapeJava(content);
            whereClause += " WHERE X.userName like '%" + content + "%' ";
        }
        if(!StringUtil.isNull(subjectId)){
            whereClause += " WHERE FIND_IN_SET(" + subjectId + ",X.defaultSubjectId)";
        }

        if (Parameters.LIKE.equalsIgnoreCase(type)) {
            whereClause += " ORDER BY X.numlike DESC";
        } else if (Parameters.RATE.equalsIgnoreCase(type)) {
            whereClause += " ORDER BY X.avgrate DESC";
        } else {// type is Subscribe
            whereClause += " ORDER BY X.numsub DESC";
        }

        if (!StringUtil.isNull(limit)) {
            whereClause += " LIMIT " + limit;
        }
        if (!StringUtil.isNull(offset)) {
            whereClause += " OFFSET " + offset;
        }

        readObject = dao.readObjectsWhereClause(entityName, whereClause, queryParams);

        dao.readObjects(SibConstants.SqlMapper.SQL_GET_ALL_CATEGORY, new Object[] {});

        // List<Map<String, Object>>

        SimpleResponse reponse = new SimpleResponse(
                                                    "" +
                                                    Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.siblinks.ws.service.MentorService#checkStudentSubcribe(long,
     * long)
     */
    @Override
    @RequestMapping(value = "/checkStudentSubcribe", method = RequestMethod.GET)
    public ResponseEntity<Response> checkStudentSubcribe(@RequestParam final long mentorid, @RequestParam final long studentid) {
        Object[] queryParams = new Object[] { mentorid, studentid };

        List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_SUBSCRIBE_STAT, queryParams);
        Map<String, Object> data_return = new HashMap<>();
        JsonParser parser = new JsonParser();

        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                String val = parser.parse(object.toString()).getAsJsonObject().get("subcribe").getAsString();
                if (val.equalsIgnoreCase("Y")) {
                    data_return.put("subscribed", true);
                } else {
                    data_return.put("subscribed", false);
                }
            }
        } else {
            data_return.put("subscribed", false);
        }

        SimpleResponse reponse = new SimpleResponse("" + true, data_return);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/getStudentSubscribed", method = RequestMethod.GET)
    public ResponseEntity<Response> getStudentSubscribed(@RequestParam final long mentorId, @RequestParam final String limit,
            @RequestParam final String offset) {

        CommonUtil utils = CommonUtil.getInstance();
        SimpleResponse response = null;

        Map<String, String> limitObject = utils.getOffset(limit, offset);

        Object[] params = { mentorId, Integer.parseInt(limitObject.get("limit")), Integer.parseInt(limitObject.get("offset")) };

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_STUDENT_SUBSCRIBED, params);

        if (readObject != null && readObject.size() > 0) {
            response = new SimpleResponse("" + true, "mentor", "getStudentSubscribed", readObject);
        } else {
            response = new SimpleResponse("" + true, "mentor", "getStudentSubscribed", SibConstants.NO_DATA);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getMainDashboardInfo", method = RequestMethod.GET)
    public ResponseEntity<Response> getMainDashboardInfo(@RequestParam final long uid) {
        Object[] queryParams = new Object[] { uid };
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> tmp = new HashMap<String, Object>();
        List<Object> readObject = null;
        String value = null;

        // count video
        readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_COUNT_VIDEOS, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("count_videos", tmp.get("numVideos"));
            }
        } else {
            result.put("count_videos", 0);
        }

        // count video current date
        readObject = dao.readObjects(SibConstants.SqlMapperBROT124.SQL_GET_COUNT_VIDEOS_CURDATE, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("count_videos_today", tmp.get("numVideos"));
            }
        } else {
            result.put("count_videos_today", 0);
        }

        // count likes of answer
        readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_COUNT_LIKES, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("count_likes", tmp.get("numLikes"));
            }
        } else {
            result.put("count_likes", 0);
        }

        // count likes of answer current date
        readObject = dao.readObjects(SibConstants.SqlMapperBROT124.SQL_GET_COUNT_LIKES_CURDATE, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("count_likes_today", tmp.get("numLikes"));
            }
        } else {
            result.put("count_likes_today", 0);
        }

        // count ratings of video
        readObject = dao.readObjects(SibConstants.SqlMapperBROT124.SQL_GET_COUNT_RATINGS, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("count_ratings", tmp.get("numRatings"));
            }
        } else {
            result.put("count_ratings", 0);
        }

        // count ratings of video current date
        readObject = dao.readObjects(SibConstants.SqlMapperBROT124.SQL_GET_COUNT_RATINGS_CURDATE, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("count_ratings_today", tmp.get("numRatings"));
            }
        } else {
            result.put("count_ratings_today", 0);
        }

        // count answers of mentor
        readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_COUNT_ANSWERS, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("count_answers", tmp.get("numAnswers"));
            }
        } else {
            result.put("count_answers", 0);
        }

        // count answers current date
        readObject = dao.readObjects(SibConstants.SqlMapperBROT124.SQL_GET_COUNT_ANSWERS_CURDATE, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("count_answers_today", tmp.get("numAnswers"));
            }
        } else {
            result.put("count_answers_today", 0);
        }

        // count subcribers of mentor
        readObject = dao.readObjects(SibConstants.SqlMapperBROT124.SQL_GET_COUNT_SUBSCRIBERS, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("count_subscribers", tmp.get("countSubscribers"));
            }
        } else {
            result.put("count_subcribers", 0);
        }

        // count subcribers current date
        readObject = dao.readObjects(SibConstants.SqlMapperBROT124.SQL_GET_COUNT_SUBSCRIBERS_CURDATE, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("count_subscribers_today", tmp.get("countSubscribers"));
            }
        } else {
            result.put("count_subcribers_today", 0);
        }

        SimpleResponse reponse = new SimpleResponse("" + true, "mentor", "getMainDashboardInfo", result);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getNewestQuestions", method = RequestMethod.GET)
    public ResponseEntity<Response> getNewestQuestions(@RequestParam final long uid) {
        Object[] queryParams = new Object[] { uid };
        SimpleResponse reponse;
        List<Object> readObject = null;
        String value = null;

        // get subject of user
        readObject = dao.readObjects(SibConstants.SqlMapperBROT124.SQL_GET_USER_SUBJECT, queryParams);

        if (readObject != null && readObject.size() > 0) {
            Map<String, Object> result = new HashMap<>();
            for (Object object : readObject) {
                Map<String, Object> map = (Map<String, Object>) object;
                value = (map.get("defaultSubjectId") != null) ? "" + map.get("defaultSubjectId") : "";
            }

            String entityName = SibConstants.SqlMapper.SQL_GET_ALL_SUBJECTID_CATEGORY;
            List<Object> subjects = dao.readObjects(entityName, new Object[] {});
            StringBuilder subjectId = new StringBuilder();
            for (String item : value.split(",")) {
                String out = CommonUtil.getAllChildCategory(item, subjects);
                if (subjectId.length() > 0) {
                    subjectId.append("," + out);
                } else {
                    subjectId.append(out);
                }
            }

            if (subjectId != null && subjectId.length() > 0) {
                String whereClause = "in(" + subjectId + ") order by p.timeStamp DESC limit 5";
                readObject = dao
                    .readObjectsWhereClause(SibConstants.SqlMapperBROT124.SQL_GET_NEWEST_QUESTIONS, whereClause, null);
                if (readObject != null && readObject.size() > 0) {
                    reponse = new SimpleResponse("" + true, "mentor", "getNewestQuestions", readObject);
                } else {
                    reponse = new SimpleResponse("" + true, "mentor", "getNewestQuestions", SibConstants.NO_DATA);
                }
            } else {
                reponse = new SimpleResponse("" + true, "mentor", "getNewestQuestions", SibConstants.NO_DATA);
            }
        } else {
            reponse = new SimpleResponse("" + true, "mentor", "getNewestQuestions", SibConstants.NO_DATA);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.siblinks.ws.service.MentorService#getLatestRatings(long)
     */
    @Override
    @RequestMapping(value = "/getLatestRatings", method = RequestMethod.GET)
    public ResponseEntity<Response> getLatestRatings(@RequestParam final long mentorid) {
        Object[] queryParams = new Object[] { mentorid };

        List<Object> readObject = null;
        SimpleResponse reponse = null;

        readObject = dao.readObjects(SibConstants.SqlMapperBROT126.SQL_GET_LATEST_RATING_IN_MANAGE_VIDEO, queryParams);

        if (readObject != null && readObject.size() > 0) {
            reponse = new SimpleResponse("" + true, "mentor", "getLatestRatings", readObject);
        } else {
            reponse = new SimpleResponse("" + true, "mentor", "getLatestRatings", SibConstants.NO_DATA);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.siblinks.ws.service.MentorService#getLatestComments(long, int,
     * int)
     */
    @Override
    @RequestMapping(value = "/getLatestComments", method = RequestMethod.GET)
    public ResponseEntity<Response> getLatestComments(final long mentorid, final int limit, final int offset) {
        Object[] queryParams = new Object[] { mentorid, limit, offset };

        List<Object> readObject = null;
        SimpleResponse reponse = null;

        readObject = dao.readObjects(SibConstants.SqlMapperBROT126.SQL_GET_LATEST_COMMENTS_IN_MANAGE_VIDEO, queryParams);

        if (readObject != null && readObject.size() > 0) {
            reponse = new SimpleResponse("" + true, "mentor", "getLatestComments", readObject);
        } else {
            reponse = new SimpleResponse("" + true, "mentor", "getLatestComments", SibConstants.NO_DATA);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.siblinks.ws.service.MentorService#getDashboardInfo(long)
     */
    @Override
    @RequestMapping(value = "/getDashboardInfo", method = RequestMethod.GET)
    public ResponseEntity<Response> getDashboardInfo(@RequestParam final long uid) {
        Object[] queryParams = new Object[] { uid };
        Map<String, Object> result = new HashMap<>();
        List<Object> readObject = null;
        Map<String, Object> tmp = new HashMap<String, Object>();

        // count video
        readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_COUNT_VIDEOS, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("count_videos", tmp.get("numVideos"));
            }
        } else {
            result.put("count_videos", 0);
        }

        // count views of video
        readObject = dao.readObjects(SibConstants.SqlMapperBROT126.SQL_GET_COUNT_VIEW_VIDEO, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("count_views", tmp.get("numViews"));
            }
        } else {
            result.put("count_views", 0);
        }

        // count comments of video
        readObject = dao.readObjects(SibConstants.SqlMapperBROT126.SQL_GET_COUNT_COMMENT_VIDEO, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("count_comments", tmp.get("numComments"));
            }
        } else {
            result.put("count_comments", 0);
        }

        // count playlist
        readObject = dao.readObjects(SibConstants.SqlMapperBROT126.SQL_GET_COUNT_VIDEO_PLAYLIST, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("count_playlist", tmp.get("numPlaylist"));
            }
        } else {
            result.put("count_playlist", 0);
        }

        readObject = dao.readObjects(SibConstants.SqlMapperBROT126.SQL_GET_COUNT_AVG_RATING_VIDEO, queryParams);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                tmp = (Map<String, Object>) object;
                result.put("avg_rating", tmp.get("averageRating"));
            }
        } else {
            result.put("avg_rating", 0);
        }

        SimpleResponse reponse = new SimpleResponse("" + true, "mentor", "getDashboardInfo", result);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/getActivityStudent", method = RequestMethod.GET)
    public ResponseEntity<Response> getActivityStudent(@RequestParam final long mentorId, @RequestParam final String limit,
            @RequestParam final String offset) {

        Map<String, String> pageLimit = CommonUtil.getInstance().getOffset(limit, offset);

        Object[] params = { mentorId, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };

        List<Object> readObjects = dao.readObjects(SibConstants.SqlMapper.SQL_MENTOR_GET_ACTIVITY_STUDENT, params);

        SimpleResponse response = new SimpleResponse("" + Boolean.TRUE, "mentor", "getActivityStudent", readObjects);

        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);

        return entity;

    }

    @Override
    @RequestMapping(value = "/getAllSubjects", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllSubjects(@RequestParam final long uid) {
        Object[] queryParams = new Object[] { uid };
        SimpleResponse reponse;
        List<Object> readObject = null;
        String value = null;

        // get subject of user
        readObject = dao.readObjects(SibConstants.SqlMapperBROT124.SQL_GET_USER_SUBJECT, queryParams);

        if (readObject != null && readObject.size() > 0) {
            Map<String, Object> result = new HashMap<>();
            for (Object object : readObject) {
                Map<String, Object> map = (Map<String, Object>) object;
                value = (map.get("defaultSubjectId") != null) ? "" + map.get("defaultSubjectId") : "";
            }

            String entityName = SibConstants.SqlMapper.SQL_GET_ALL_SUBJECTID_CATEGORY;
            List<Object> subjects = dao.readObjects(entityName, new Object[] {});
            StringBuilder subjectId = new StringBuilder();
            for (String item : value.split(",")) {
                String out = CommonUtil.getAllChildCategory(item, subjects);
                if (subjectId.length() > 0) {
                    subjectId.append("," + out);
                } else {
                    subjectId.append(out);
                }
            }

            if (subjectId != null && subjectId.length() > 0) {
                String whereClause = "in(" + subjectId + ") and active = 'A'";
                readObject = dao.readObjectsWhereClause(SibConstants.SqlMapperBROT126.SQL_GET_ALL_SUBJECTS, whereClause, null);
                if (readObject != null && readObject.size() > 0) {
                    reponse = new SimpleResponse("" + true, "mentor", "getAllSubjects", readObject);
                } else {
                    reponse = new SimpleResponse("" + true, "mentor", "getAllSubjects", SibConstants.NO_DATA);
                }
            } else {
                reponse = new SimpleResponse("" + true, "mentor", "getAllSubjects", SibConstants.NO_DATA);
            }
        } else {
            reponse = new SimpleResponse("" + true, "mentor", "getAllSubjects", SibConstants.NO_DATA);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/getNewestAnswers", method = RequestMethod.GET)
    public ResponseEntity<Response> getNewestAnswers(@RequestParam final long authorId, @RequestParam final String limit,
            @RequestParam final String offset) {
        Map<String, String> limitItem = CommonUtil.getInstance().getOffset(limit, offset);
        Object[] params = null;
        boolean status;
        if (authorId != 0) {
            params = new Object[] { authorId, Integer
                .parseInt(limitItem.get("limit")), Integer.parseInt(limitItem.get("offset")) };
            status = true;
        } else {
            status = false;
        }
        String strEntity = SibConstants.SqlMapper.SQL_GET_NEWEST_ANSWERS;
        List<Object> readObjects = null;
        if (status) {
            readObjects = dao.readObjects(strEntity, params);
        }
        SimpleResponse response = new SimpleResponse("" + status, "mentor", "getNewestAnswers", readObjects);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;

    }

    @Override
    @RequestMapping(value = "/getAllStudentSubscribed", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllStudentSubscribed(final long uid) {
        SimpleResponse response;

        List<Object> readObjects = dao.readObjects(SibConstants.SqlMapperBROT163.SQL_GET_ALL_STUDENT_SUBSCRIBED, new Object[] { uid });
        if (readObjects != null && readObjects.size() > 0) {
            response = new SimpleResponse("" + true, "mentor", "getAllStudentSubscribed", readObjects);
        } else {
            response = new SimpleResponse("" + true, "mentor", "getAllStudentSubscribed", SibConstants.NO_DATA);
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

}
