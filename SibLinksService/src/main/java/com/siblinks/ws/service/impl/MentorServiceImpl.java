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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import com.siblinks.ws.common.DAOException;
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
 * {@link MentorService}
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

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/topMetorEachSubject", method = RequestMethod.POST)
    public ResponseEntity<Response> topMetorEachSubject(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_TOP_MENTOR, new Object[] { request
                .getRequest_data()
                .getSubjectId() });
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
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public ResponseEntity<Response> getList(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
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

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_MENTOR_LIST, queryParams);

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
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<Response> search(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
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
            List<Object> readObject = dao.readObjects(entityName, queryParams);

            simpleResponse = new SimpleResponse(
                                                "" + Boolean.TRUE,
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
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> uploadFile(@RequestParam("uploadfile") final MultipartFile uploadfile) throws IOException {

        String filename = "";
        String name = "";
        String directory = ReadProperties.getProperties("directoryImageArticle");
        String sample = ".png .jpg .gif .bmp .tga";
        SimpleResponse reponse = null;
        try {

            name = uploadfile.getOriginalFilename();
            int index = name.indexOf(".");
            name = name.substring(index, name.length());
            boolean status = sample.contains(name.toLowerCase());

            if (directory != null && status) {
                RandomString randomName = new RandomString();
                //
                filename = randomName.random() + name;
                Path path = Paths.get(directory, filename);
                String filepath = (path != null) ? path.toString() : "";
                // // Save the file locally
                // BufferedOutputStream stream =
                // new BufferedOutputStream(new FileOutputStream(new
                // File(filepath)));
                // stream.write(uploadfile.getBytes());
                // stream.close();

                reponse = new SimpleResponse(SibConstants.SUCCESS, filepath);

            } else {
                reponse = new SimpleResponse(
                                             SibConstants.FAILURE,
                                             "Your photos couldn't be uploaded. Photos should be saved as JPG, PNG, GIF or BMP files.");
                return new ResponseEntity<Response>(reponse, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            reponse = new SimpleResponse(SibConstants.FAILURE, e.getMessage());

        }
        return new ResponseEntity<Response>(HttpStatus.BAD_REQUEST);
    } // method uploadFile

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/createAboutMentor", method = RequestMethod.POST)
    public ResponseEntity<Response> createAboutMentor(@RequestBody final RequestData aboutMentor) throws FileNotFoundException {
        SimpleResponse simpleResponse = null;
        try {
            Map<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("description", aboutMentor.getRequest_data_aboutMentor().getDescription());
            queryParams.put("introduce", aboutMentor.getRequest_data_aboutMentor().getIntroduce());
            if (aboutMentor.getRequest_data_aboutMentor().getImage() == null) {
                queryParams.put("image", ReadProperties.getProperties("directoryImageAvatar"));
            } else {
                queryParams.put("image", aboutMentor.getRequest_data_aboutMentor().getImage());
            }

            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_CREATE_ABOUT_MENTOR, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                aboutMentor.getRequest_data_type(),
                                                aboutMentor.getRequest_data_method(),
                                                status);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                aboutMentor.getRequest_data_type(),
                                                aboutMentor.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAllAboutMentor", method = RequestMethod.POST)
    public ResponseEntity<Response> getAllAboutMentor(@RequestBody final RequestData aboutMentor) {
        SimpleResponse simpleResponse = null;
        try {
            String entityName = null;

            Map<String, String> queryParams = new HashMap<String, String>();

            entityName = SibConstants.SqlMapper.SQL_GET_ALL_ABOUT_MENTOR;

            List<Object> readObject = dao.readObjects(entityName, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                aboutMentor.getRequest_data_type(),
                                                aboutMentor.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                aboutMentor.getRequest_data_type(),
                                                aboutMentor.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/deleteAboutMentor", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteAboutMentor(@RequestBody final RequestData aboutMentor) {
        SimpleResponse simpleResponse = null;
        try {

            Object[] queryParams = new Object[] { aboutMentor.getRequest_data_aboutMentor().getId() };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_IMAGE_ABOUT_MENTOR, queryParams);

            File file = new File((String) ((Map) readObject.get(0)).get("image"));

            boolean flag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_ABOUT_MENTOR, queryParams);

            String fileName = (String) ((Map) readObject.get(0)).get("image");

            if (flag && !fileName.contains("default_avatar")) {
                file.delete();
            }

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                aboutMentor.getRequest_data_type(),
                                                aboutMentor.getRequest_data_method(),
                                                flag);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                aboutMentor.getRequest_data_type(),
                                                aboutMentor.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateAboutMentor", method = RequestMethod.POST)
    public ResponseEntity<Response> updateAboutMentor(@RequestBody final RequestData aboutMentor) {
        SimpleResponse simpleResponse = null;
        try {
            Object[] queryParams = new Object[] { aboutMentor.getRequest_data_aboutMentor().getId(), aboutMentor
                .getRequest_data_aboutMentor()
                .getImage(), aboutMentor.getRequest_data_aboutMentor().getDescription(), aboutMentor
                .getRequest_data_aboutMentor()
                .getIntroduce() };

            String entityName = "";
            if (aboutMentor.getRequest_data_aboutMentor().getImage() != null) {
                List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_IMAGE_ABOUT_MENTOR, queryParams);
                //
                File file = new File((String) ((Map) readObject.get(0)).get(Parameters.IMAGE));
                String fileName = (String) ((Map) readObject.get(0)).get(Parameters.IMAGE);
                if (file != null && !fileName.contains("default_avatar")) {
                    file.delete();
                }
                entityName = SibConstants.SqlMapper.SQL_UPDATE_ABOUT_MENTOR;
            } else {
                entityName = SibConstants.SqlMapper.SQL_UPDATE_ABOUT_MENTOR_NOT_IMAGE;
            }

            boolean flag = dao.insertUpdateObject(entityName, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                aboutMentor.getRequest_data_type(),
                                                aboutMentor.getRequest_data_method(),
                                                flag);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                aboutMentor.getRequest_data_type(),
                                                aboutMentor.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getImageAboutMentor/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageAboutMentor(@PathVariable(value = "id") final String id) {
        ResponseEntity<byte[]> responseEntity = null;
        try {
            Map<String, String> queryParams = new HashMap<String, String>();
            List<Object> readObject = null;

            queryParams.put("id", id);
            String entityName = SibConstants.SqlMapper.SQL_GET_IMAGE_ABOUT_MENTOR;
            String path = null;

            readObject = dao.readObjects(entityName, queryParams);
            if (((Map) readObject.get(0)).get(Parameters.IMAGE) != null) {
                path = ((Map) readObject.get(0)).get(Parameters.IMAGE).toString();

                RandomAccessFile t = null;
                byte[] r = null;
                try {
                    t = new RandomAccessFile(path, "r");
                    r = new byte[(int) t.length()];
                    t.readFully(r);
                    responseEntity = new ResponseEntity<byte[]>(r, new HttpHeaders(), HttpStatus.OK);
                } catch (IOException e) {
                    logger.debug("Some thing wrong", e);
                    responseEntity = new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
                } finally {
                    // Close file
                    if (t != null) {
                        try {
                            t.close();
                        } catch (IOException e) {
                            // Do nothing
                        }
                    }
                }
            } else {
                responseEntity = new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
            }
        } catch (DAOException e) {
            e.printStackTrace();
            new SimpleResponse(SibConstants.FAILURE, "Mentor", "getImageAboutMentor", e.getMessage());
        }
        return responseEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getInforTopMentor", method = RequestMethod.POST)
    public ResponseEntity<Response> getInforTopMentor(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_USER_BY_ID, new Object[] { request
                .getRequest_data()
                .getUid() });

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
        ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getNewestAnswer/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response> getNewestAnswer(@PathVariable(value = "id") final long id) {

        SimpleResponse simpleResponse = null;
        try {
            Object[] queryParams = { id };
            List<Object> readObjects = dao.readObjects(SibConstants.SqlMapperBROT27.SQL_GET_NEWEST_MENTOR_ANSWER, queryParams);
            simpleResponse = new SimpleResponse("" + Boolean.TRUE, "mentor", "getNewestAnswer", readObjects);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Mentor", "getNewestAnswer", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getTopMentorsMostLike", method = RequestMethod.POST)
    public ResponseEntity<Response> getTopMentorsMostLike(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }
            String limit = request.getRequest_data().getLimit();
            String offset = request.getRequest_data().getOffset();
            Object[] queryParams = {};
            List<Object> readObject = null;
            String whereClause = "";
            if (!StringUtil.isNull(limit)) {
                whereClause += " LIMIT " + limit;
            }
            if (!StringUtil.isNull(offset)) {
                whereClause += " OFFSET " + offset;
            }
            readObject = dao.readObjectsWhereClause(
                SibConstants.SqlMapper.SQL_GET_TOP_MENTORS_MOST_LIKE,
                whereClause,
                queryParams);

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
    @RequestMapping(value = "/getStudentsSubcribe", method = RequestMethod.POST)
    public ResponseEntity<Response> getStudentsSubcribe(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
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

            new SimpleResponse("" + Boolean.TRUE, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
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
    @RequestMapping(value = "/getTotalViewLikeViewByMentorId/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response> getTotalViewLikeViewByMentorId(@PathVariable(value = "id") final long id) {
        SimpleResponse simpleResponse = null;
        try {
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_TOTAL_LIKE_VIEW_VIDEO, new Object[] { "" +
                                                                                                                           id });

            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "Mentor", "getTotalViewLikeViewByMentorId", readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Mentor", "getTotalViewLikeViewByMentorId", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getTotalAnswersByMentorId/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response> getTotalAnswersByMentorId(@PathVariable(value = "id") final long id) {
        SimpleResponse simpleResponse = null;
        try {
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_TOTAL_ANSWERS, new Object[] { "" + id });
            simpleResponse = new SimpleResponse("" + Boolean.TRUE, "Mentor", "getTotalAnswersByMentorId", readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Mentor", "getTotalAnswersByMentorId", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getTopMentorsByLikeRateSubcrible", method = RequestMethod.GET)
    public ResponseEntity<Response> getTopMentorsByLikeRateSubcrible(@RequestParam final String subjectId,
            @RequestParam final String content, @RequestParam final String uid, @RequestParam final String type,
            @RequestParam final String limit, @RequestParam final String offset) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }
            List<Object> listParam = new ArrayList<Object>();
            String tempUserId = uid;
            String entityName = SibConstants.SqlMapper.SQL_GET_TOP_MENTORS_BY_LIKE_RATE_SUBS;
            if (StringUtil.isNull(tempUserId)) {
                tempUserId = "-1";
            }
            String whereClause = "";
            if (!"-1".equalsIgnoreCase(tempUserId)) {
                whereClause += ",(SELECT count(*) isSubs FROM Sib_Student_Subcribe where Subcribe ='Y' AND " +
                               " MentorId = U.userid AND StudentId =" +
                               tempUserId +
                               ") isSubs";
            } else {
                whereClause += ",('-1') as isSubs ";
            }
            whereClause += " FROM Sib_Users U LEFT JOIN Sib_School_College_Degree SCD ON U.school = SCD.sch_colle_degree_id LEFT JOIN Sib_Videos V ON U.userid = V.authorID "
                           + "WHERE U.userType = 'M' GROUP BY U.userid, U.lastName, U.imageUrl, U.firstName)X ";
            if (!StringUtil.isNull(content)) {
                String keySearch = "%" + content + "%";
                listParam.add(keySearch);
                listParam.add(keySearch);
                whereClause += " WHERE ((firstName is null AND lastName is null AND SUBSTRING_INDEX(X.loginName,'@',1) LIKE (?)) OR concat(firstname,' ',lastName) like (?))";

            }
            if (!StringUtil.isNull(subjectId)) {
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
                listParam.add(Integer.parseInt(limit));
                whereClause += " LIMIT ?";
            }

            if (!StringUtil.isNull(offset)) {
                listParam.add(Integer.parseInt(offset));
                whereClause += " OFFSET ?";
            }

            List<Object> readObject = dao.readObjectsWhereClause(entityName, whereClause, listParam.toArray());

            // dao.readObjects(SibConstants.SqlMapper.SQL_GET_ALL_CATEGORY, new
            // Object[] {});
            String count = "0";

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                "mentor",
                                                "getTopMentorsByLikeRateSubcrible",
                                                readObject,
                                                count);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                "mentor",
                                                "getTopMentorsByLikeRateSubcrible",
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/checkStudentSubcribe", method = RequestMethod.GET)
    public ResponseEntity<Response> checkStudentSubcribe(@RequestParam final long mentorid, @RequestParam final long studentid) {
        SimpleResponse simpleResponse = null;
        try {
            Object[] queryParams = new Object[] { mentorid, studentid };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_SUBSCRIBE_STAT, queryParams);

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

            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, data_return);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Mentor", "checkStudentSubcribe", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getStudentSubscribed", method = RequestMethod.GET)
    public ResponseEntity<Response> getStudentSubscribed(@RequestParam final long mentorId, @RequestParam final String limit,
            @RequestParam final String offset) {
        SimpleResponse simpleResponse = null;
        try {
            CommonUtil utils = CommonUtil.getInstance();
            Map<String, String> limitObject = utils.getOffset(limit, offset);

            Object[] params = { mentorId, Integer.parseInt(limitObject.get("limit")), Integer.parseInt(limitObject.get("offset")) };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_STUDENT_SUBSCRIBED, params);

            if (readObject != null && readObject.size() > 0) {
                simpleResponse = new SimpleResponse("" + true, "mentor", "getStudentSubscribed", readObject);
            } else {
                simpleResponse = new SimpleResponse("" + true, "mentor", "getStudentSubscribed", SibConstants.NO_DATA);
            }
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Mentor", "getStudentSubscribed", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getMainDashboardInfo", method = RequestMethod.GET)
    public ResponseEntity<Response> getMainDashboardInfo(@RequestParam final long uid) {
        Object[] queryParams = new Object[] { uid };
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> tmp = new HashMap<String, Object>();
        List<Object> readObject = null;
        SimpleResponse simpleResponse = null;
        try {
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

            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "mentor", "getMainDashboardInfo", result);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Mentor", "getStudentSubscribed", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getNewestQuestions", method = RequestMethod.GET)
    public ResponseEntity<Response> getNewestQuestions(@RequestParam final long uid) {
        Object[] queryParams = new Object[] { uid };
        List<Object> readObject = null;
        String value = null;
        SimpleResponse simpleResponse = null;
        try {
            // get subject of user
            readObject = dao.readObjects(SibConstants.SqlMapperBROT124.SQL_GET_USER_SUBJECT, queryParams);

            if (readObject != null && readObject.size() > 0) {
                new HashMap<>();
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
                    readObject = dao.readObjectsWhereClause(
                        SibConstants.SqlMapperBROT124.SQL_GET_NEWEST_QUESTIONS,
                        whereClause,
                        null);
                    if (readObject != null && readObject.size() > 0) {
                        simpleResponse = new SimpleResponse("" + true, "mentor", "getNewestQuestions", readObject);
                    } else {
                        simpleResponse = new SimpleResponse("" + true, "mentor", "getNewestQuestions", SibConstants.NO_DATA);
                    }
                } else {
                    simpleResponse = new SimpleResponse("" + true, "mentor", "getNewestQuestions", SibConstants.NO_DATA);
                }
            } else {
                simpleResponse = new SimpleResponse("" + true, "mentor", "getNewestQuestions", SibConstants.NO_DATA);
            }

        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Mentor", "getStudentSubscribed", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getLatestRatings", method = RequestMethod.GET)
    public ResponseEntity<Response> getLatestRatings(@RequestParam final long mentorid) {
        Object[] queryParams = new Object[] { mentorid };
        SimpleResponse simpleResponse = null;
        try {

            List<Object> readObject = dao.readObjects(
                SibConstants.SqlMapperBROT126.SQL_GET_LATEST_RATING_IN_MANAGE_VIDEO,
                queryParams);

            if (readObject != null && readObject.size() > 0) {
                simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "mentor", "getLatestRatings", readObject);
            } else {
                simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "mentor", "getLatestRatings", SibConstants.NO_DATA);
            }

        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Mentor", "getLatestRatings", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getLatestComments", method = RequestMethod.GET)
    public ResponseEntity<Response> getLatestComments(final long mentorid, final int limit, final int offset) {
        Object[] queryParams = new Object[] { mentorid, limit, offset };
        SimpleResponse simpleResponse = null;
        try {

            List<Object> readObject = dao.readObjects(
                SibConstants.SqlMapperBROT126.SQL_GET_LATEST_COMMENTS_IN_MANAGE_VIDEO,
                queryParams);

            if (readObject != null && readObject.size() > 0) {
                int size = readObject.size();
                List<Object> playlist = null;
                List<Object> result = new ArrayList<Object>();
                Map<String, Object> map = new HashMap<String, Object>();
                Map<String, Object> tmp = new HashMap<String, Object>();
                for (int i = 0; i < size; i++) {
                    map = (Map<String, Object>) readObject.get(i);
                    playlist = dao.readObjects(
                        SibConstants.SqlMapperBROT163.SQL_GET_PLAYLIST_INFO_OF_VIDEO,
                        new Object[] { map.get("vid") });
                    if (playlist != null && playlist.size() > 0) {
                        for (Object object : playlist) {
                            tmp = (Map<String, Object>) object;
                            map.put("plid", tmp.get("plid"));
                        }
                    } else {
                        map.put("plid", null);
                    }
                    result.add(map);
                }
                simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "mentor", "getLatestComments", result);
            } else {
                simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "mentor", "getLatestComments", SibConstants.NO_DATA);
            }

        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Mentor", "getLatestComments", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getDashboardInfo", method = RequestMethod.GET)
    public ResponseEntity<Response> getDashboardInfo(@RequestParam final long uid) {
        Object[] queryParams = new Object[] { uid };
        Map<String, Object> result = new HashMap<>();
        List<Object> readObject = null;
        Map<String, Object> tmp = new HashMap<String, Object>();
        SimpleResponse simpleResponse = null;
        try {
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

            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "mentor", "getDashboardInfo", result);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Mentor", "getLatestComments", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getActivityStudent", method = RequestMethod.GET)
    public ResponseEntity<Response> getActivityStudent(@RequestParam final long mentorId, @RequestParam final String limit,
            @RequestParam final String offset) {
        SimpleResponse simpleResponse = null;
        try {
            Map<String, String> pageLimit = CommonUtil.getInstance().getOffset(limit, offset);

            Object[] params = { mentorId, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };

            List<Object> readObjects = dao.readObjects(SibConstants.SqlMapper.SQL_MENTOR_GET_ACTIVITY_STUDENT, params);

            simpleResponse = new SimpleResponse("" + Boolean.TRUE, "mentor", "getActivityStudent", readObjects);

        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Mentor", "getActivityStudent", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAllSubjects", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllSubjects(@RequestParam final long uid) {
        Object[] queryParams = new Object[] { uid };
        List<Object> readObject = null;
        String value = null;
        SimpleResponse simpleResponse = null;
        try {
            // get subject of user
            readObject = dao.readObjects(SibConstants.SqlMapperBROT124.SQL_GET_USER_SUBJECT, queryParams);

            if (readObject != null && readObject.size() > 0) {
                new HashMap<>();
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
                    readObject = dao
                        .readObjectsWhereClause(SibConstants.SqlMapperBROT126.SQL_GET_ALL_SUBJECTS, whereClause, null);
                    if (readObject != null && readObject.size() > 0) {
                        simpleResponse = new SimpleResponse("" + true, "mentor", "getAllSubjects", readObject);
                    } else {
                        simpleResponse = new SimpleResponse("" + true, "mentor", "getAllSubjects", SibConstants.NO_DATA);
                    }
                } else {
                    simpleResponse = new SimpleResponse("" + true, "mentor", "getAllSubjects", SibConstants.NO_DATA);
                }
            } else {
                simpleResponse = new SimpleResponse("" + true, "mentor", "getAllSubjects", SibConstants.NO_DATA);
            }

        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Mentor", "getAllSubjects", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/getNewestAnswers", method = RequestMethod.GET)
    public ResponseEntity<Response> getNewestAnswers(@RequestParam final long authorId, @RequestParam final String limit,
            @RequestParam final String offset) {
        Map<String, String> limitItem = CommonUtil.getInstance().getOffset(limit, offset);
        Object[] params = null;
        boolean status;
        SimpleResponse simpleResponse = null;
        try {
            if (authorId != 0) {
                params = new Object[] { authorId, Integer.parseInt(limitItem.get("limit")), Integer.parseInt(limitItem
                    .get("offset")) };
                status = true;
            } else {
                status = false;
            }
            String strEntity = SibConstants.SqlMapper.SQL_GET_NEWEST_ANSWERS;
            List<Object> readObjects = null;
            if (status) {
                readObjects = dao.readObjects(strEntity, params);
            }
            simpleResponse = new SimpleResponse("" + status, "mentor", "getNewestAnswers", readObjects);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Mentor", "getNewestAnswers", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAllStudentSubscribed", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllStudentSubscribed(final long uid) {
        SimpleResponse simpleResponse = null;
        try {
            List<Object> readObjects = dao.readObjects(
                SibConstants.SqlMapperBROT163.SQL_GET_ALL_STUDENT_SUBSCRIBED,
                new Object[] { uid });
            if (readObjects != null && readObjects.size() > 0) {
                simpleResponse = new SimpleResponse("" + true, "mentor", "getAllStudentSubscribed", readObjects);
            } else {
                simpleResponse = new SimpleResponse("" + true, "mentor", "getAllStudentSubscribed", SibConstants.NO_DATA);
            }
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Mentor", "getNewestAnswers", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

}