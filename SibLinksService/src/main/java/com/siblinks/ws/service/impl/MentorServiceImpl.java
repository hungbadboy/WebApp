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
                                                    "" + Boolean.TRUE,
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
                                                    "" + Boolean.TRUE,
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
                                                    "" + Boolean.TRUE,
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
        name.toLowerCase();
        boolean status = sample.contains(name);

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
                                                        "" + Boolean.FALSE,
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
                                                    "" + Boolean.TRUE,
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
                                                    "" + Boolean.TRUE,
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
                                                    "" + Boolean.TRUE,
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
                                                    "" + Boolean.TRUE,
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

            final HttpHeaders headers = new HttpHeaders();

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
                                                                                              "" + Boolean.FALSE,
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
                                                    "" + Boolean.TRUE,
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
                                                                                              "" + Boolean.FALSE,
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
                                                    "" + Boolean.TRUE,
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
                                                                                              "" + Boolean.FALSE,
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
                                                    "" + Boolean.TRUE,
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
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }
        String limit = request.getRequest_data().getLimit();
        String offset = request.getRequest_data().getOffset();
        String type = request.getRequest_data().getType();
        String userId = request.getRequest_data().getUid();
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
        whereClause += " FROM Sib_Users U LEFT JOIN Sib_Videos V ON U.userid = V.authorID "
                       + "WHERE U.userType = 'M' GROUP BY U.userid, U.lastName, U.imageUrl, U.firstName)X ";
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

        List<Object> listCategories = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ALL_CATEGORY, new Object[] {});

        // List<Map<String, Object>>

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
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

        data_return.put("subscribed", false);
        for (Object object : readObject) {
            String val = parser.parse(object.toString()).getAsJsonObject().get("subcribe").getAsString();
            if (val.equalsIgnoreCase("Y")) {
                data_return.put("subscribed", true);
            } else {
                data_return.put("subscribed", false);
            }
        }

        SimpleResponse reponse = new SimpleResponse("" + true, data_return);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }
}
