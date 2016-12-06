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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.StudentService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.SibConstants;
import com.siblinks.ws.util.StringUtil;

/**
 * {@link StudentService}
 *
 * @author Tavv
 *
 */
@RestController
@RequestMapping("/siblinks/services/student")
public class StudentServiceImpl implements StudentService {

    @Autowired
    private ObjectDao dao;

    @Autowired
    private HttpServletRequest context;

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getMentorSubscribed", method = RequestMethod.GET)
    public ResponseEntity<Response> getMentorSubscribed(final long studentId, final String limit, final String offset) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                // Return authentication
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }
            CommonUtil util = CommonUtil.getInstance();
            Map<String, String> pageLimit = util.getOffset(limit, offset);
            Object[] params = { studentId, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };

            List<Object> listMentorSubsribed = dao.readObjects(SibConstants.SqlMapper.SQL_MENTOR_STUDENT_SUBSCRIBED, params);
            if (!CollectionUtils.isEmpty(listMentorSubsribed)) {
                simpleResponse = new SimpleResponse(
                                                    SibConstants.SUCCESS,
                                                    "student",
                                                    "getMentorSubscribed",
                                                    listMentorSubsribed,
                                                    "" + listMentorSubsribed.size());
            } else {
                simpleResponse = new SimpleResponse(
                                                    SibConstants.SUCCESS,
                                                    "student",
                                                    "getMentorSubscribed",
                                                    SibConstants.NO_DATA,
                                                    "0");
            }
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "student", "getMentorSubscribed", e.getMessage());
        }

        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    @RequestMapping(value = "/getSubscribedMentorViewStudent", method = RequestMethod.GET)
    public ResponseEntity<Response> getSubscribedMentorViewStudent(@RequestParam final long userId,
            @RequestParam final long studentId, @RequestParam final String limit, @RequestParam final String offset) {
        SimpleResponse simpleResponse = null;
        try {
            Map<String, String> pageLimit = CommonUtil.getInstance().getOffset(limit, offset);
            // Get subscribed of student
            Object[] params = new Object[] { studentId, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit
                .get("offset")) };
            List<Object> listMentorSubscribedOfStudent = dao.readObjects(
                SibConstants.SqlMapper.SQL_SUBSCRIBED_FROM_MENTOR_VIEW_STUDENT,
                params);

            // Get student subscribed
            params = new Object[] { userId, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
            List<Object> listMentorSubscribedOfCurrentId = dao.readObjects(
                SibConstants.SqlMapper.SQL_MENTOR_STUDENT_SUBSCRIBED,
                params);

            Map<Integer, Object> data = new HashMap<>();
            if (!CollectionUtils.isEmpty(listMentorSubscribedOfStudent)) {
                for (Object obj : listMentorSubscribedOfStudent) {
                    Map objConvert = (Map) obj;
                    if ((long) objConvert.get("isSubs") == 1) {
                        data.put((int) objConvert.get("userid"), obj);
                    }
                }
            }

            List<Object> readObject = new ArrayList<>();
            if (!CollectionUtils.isEmpty(listMentorSubscribedOfCurrentId)) {
                if (!CollectionUtils.isEmpty(listMentorSubscribedOfStudent)) {
                    for (Object obj : listMentorSubscribedOfStudent) {
                        Map objConvert = (Map) obj;
                        // System.out.println((int) objConvert.get("userid"));
                        // System.out.println(data.get((int)
                        // objConvert.get("userid")));
                        if (data.get((int) objConvert.get("userid")) != null) {
                            objConvert.put("isSubs", 1);
                        } else {
                            objConvert.put("isSubs", 0);
                        }
                        readObject.add(objConvert);
                    }
                } else {
                    // Do nothing
                }
            } else {
                if (!CollectionUtils.isEmpty(listMentorSubscribedOfStudent)) {
                    for (Object obj : listMentorSubscribedOfStudent) {
                        Map objConvert = (Map) obj;
                        if ((long) objConvert.get("isSubs") == 1) {
                            objConvert.put("isSubs", 0);
                            readObject.add(objConvert);
                        }
                    }
                } else {
                    // Do nothing
                }
            }
            if (readObject.size() > 0) {
                simpleResponse = new SimpleResponse("" + true, "student", "getSubscribedMentorViewStudent", readObject);
            } else {
                simpleResponse = new SimpleResponse("" + true, "student", "getSubscribedMentorViewStudent", SibConstants.NO_DATA);
            }
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "student", "getSubscribedMentorViewStudent", e.getMessage());
        }

        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/checkStudentSubscribe", method = RequestMethod.GET)
    public ResponseEntity<Response> checkStudentSubscribe(@RequestParam final long studentId, @RequestParam final long mentorId) {

        String message = "";
        SimpleResponse simpleResponse = null;
        try {
            if (studentId == 0 || mentorId == 0) {
                message = SibConstants.USER_NOT_EXISTS;
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "student", "checkStudentSubscribe", message);
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
            }
            Object[] params = { studentId, mentorId };

            String entityName = SibConstants.SqlMapper.SQL_CHECK_STUDENT_SUBSCRIBE;

            List<Object> readObject = dao.readObjects(entityName, params);
            boolean isSubscribed = false;
            if (!CollectionUtils.isEmpty(readObject)) {
                for (Object object : readObject) { // Only one object
                    Map obj = (Map) object;
                    isSubscribed = (long) obj.get("count(*)") > 0;
                }
            }
            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "student", "checkStudentSubscribe", isSubscribed);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "student", "getSubscribedMentorViewStudent", e.getMessage());
        }

        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.siblinks.ws.service.StudentService#getAllInfoMentorSubscribed(long,
     * java.lang.String, java.lang.String)
     */
    @Override
    @RequestMapping(value = "/getAllInfoMentorSubscribed", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllInfoMentorSubscribed(@RequestParam final long studentId, @RequestParam(
            value = "keyWord",
            required = false) final String keyword, @RequestParam final String limit, @RequestParam final String offset,
            @RequestParam(value = "isCount", required = false, defaultValue = SibConstants.FAILURE) final boolean isCount) {

        SimpleResponse response;
        try {
            Map<String, String> pageLimit = CommonUtil.getInstance().getOffset(limit, offset);
            List<Object> params = new ArrayList<Object>();
            List<Object> paramsCount = new ArrayList<Object>();
            String entityName = "";
            String entityNameCount = "";
            params.add(studentId);
            paramsCount.add(studentId);
            if (StringUtil.isNull(keyword)) {
                entityName = SibConstants.SqlMapper.SQL_GET_ALL_INFO_MENTOR_SUBSCRIBED;
                entityNameCount = SibConstants.SqlMapper.SQL_GET_COUNT_STUDENT_SUBSCRIBED_MENTOR;
            } else {
                params.add("%" + keyword + "%");
                params.add("%" + keyword + "%");

                paramsCount.add("%" + keyword + "%");
                paramsCount.add("%" + keyword + "%");

                entityName = SibConstants.SqlMapper.SQL_SEARCH_INFO_MENTOR_SUBSCRIBED;
                entityNameCount = SibConstants.SqlMapper.SQL_SEARCH_COUNT_STUDENT_SUBSCRIBED_MENTOR;
            }
            params.add(Integer.parseInt(pageLimit.get("limit")));
            params.add(Integer.parseInt(pageLimit.get("offset")));

            List<Object> readObjects = dao.readObjects(entityName, params.toArray());
            String count = "0";
            if (!CollectionUtils.isEmpty(readObjects)) {
                if (isCount) {
                    List<Object> countList = dao.readObjects(entityNameCount, paramsCount.toArray());
                    if (!CollectionUtils.isEmpty(countList)) {
                        count = "" + ((HashMap<String, Long>) countList.get(0)).get(Parameters.COUNT);
                    }
                }
                response = new SimpleResponse(SibConstants.SUCCESS, "student", "getAllInfoMentorSubscribed", readObjects, count);
            } else {
                response = new SimpleResponse(
                                              SibConstants.SUCCESS,
                                              "student",
                                              "getAllInfoMentorSubscribed",
                                              SibConstants.NO_DATA,
                                              count);
            }
        } catch (DAOException e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "student", "getAllInfoMentorSubscribed", SibConstants.NO_DATA);
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

}
