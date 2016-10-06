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

import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.StudentService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.SibConstants;

/**
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.siblinks.ws.service.StudentService#getMentorSubscribed(com.siblinks.
     * ws.model.RequestData)
     */
    @Override
    @RequestMapping(value = "/getMentorSubscribed", method = RequestMethod.GET)
    public ResponseEntity<Response> getMentorSubscribed(final long studentId, final String limit, final String offset) {
        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" +
                                                                                              false,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }
        CommonUtil util = CommonUtil.getInstance();
        Map<String, String> pageLimit = util.getOffset(limit, offset);
        Object[] params = { studentId, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
        String entityName = SibConstants.SqlMapper.SQL_MENTOR_STUDENT_SUBSCRIBED;
        List<Object> listMentorSubsribed = dao.readObjects(entityName, params);
        String count = "0";
        if (listMentorSubsribed != null) {
            count = String.valueOf(listMentorSubsribed.size());
        }
        SimpleResponse response;
        if (listMentorSubsribed.size() > 0) {
            response = new SimpleResponse("" + Boolean.TRUE, "student", "getMentorSubscribed", listMentorSubsribed, count);
        } else {
            response = new SimpleResponse("" + Boolean.TRUE, "student", "getMentorSubscribed", SibConstants.NO_DATA, count);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    /**
     * 
     * @param userId
     * @param studentId
     * @param limit
     * @param offset
     * @return listMentorSubscribed see with Mentor view profile Student
     */
    @RequestMapping(value = "/getSubscribedMentorViewStudent", method = RequestMethod.GET)
    public ResponseEntity<Response> getSubscribedMentorViewStudent(@RequestParam final long userId,
            @RequestParam final long studentId, @RequestParam final String limit, @RequestParam final String offset) {

        Map<String, String> pageLimit = CommonUtil.getInstance().getOffset(limit, offset);

        Object[] params = null;
        params = new Object[] { studentId, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };
        String entityName = SibConstants.SqlMapper.SQL_SUBSCRIBED_FROM_MENTOR_VIEW_STUDENT;

        List<Object> listMentorSubscribedOfStudent = dao.readObjects(entityName, params);

        params = new Object[] { userId, Integer.parseInt(pageLimit.get("limit")), Integer.parseInt(pageLimit.get("offset")) };

        List<Object> listMentorSubscribedOfCurrentId = dao
            .readObjects(SibConstants.SqlMapper.SQL_MENTOR_STUDENT_SUBSCRIBED, params);

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
            for (Object obj : listMentorSubscribedOfStudent) {
                Map objConvert = (Map) obj;
                System.out.println((int) objConvert.get("userid"));
                System.out.println(data.get((int) objConvert.get("userid")));
                if (data.get((int) objConvert.get("userid")) != null) {
                    objConvert.put("isSubs", 1);
                } else {
                    objConvert.put("isSubs", 0);
                }
                readObject.add(objConvert);
            }
        } else {
            for (Object obj : listMentorSubscribedOfStudent) {
                Map objConvert = (Map) obj;
                if ((long) objConvert.get("isSubs") == 1) {
                    objConvert.put("isSubs", 0);
                    readObject.add(objConvert);
                }
            }
        }
        SimpleResponse response;
        if (readObject.size() > 0) {
            response = new SimpleResponse("" + true, "student", "getSubscribedMentorViewStudent", readObject);
        } else {
            response = new SimpleResponse("" + true, "student", "getSubscribedMentorViewStudent", SibConstants.NO_DATA);
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;

    }

    @RequestMapping(value = "/checkStudentSubscribe", method = RequestMethod.GET)
    public ResponseEntity<Response> checkStudentSubscribe(@RequestParam final long studentId, @RequestParam final long mentorId) {
        boolean status;
        String message = "";
        SimpleResponse response;
        ResponseEntity<Response> entity;
        if (studentId == 0 || mentorId == 0) {
            status = false;
            message = SibConstants.USER_NOT_EXISTS;
            response = new SimpleResponse("" + status, "student", "checkStudentSubscribe", message);
            entity = new ResponseEntity<Response>(response, HttpStatus.OK);
            return entity;
        }
        status = true;
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
        response = new SimpleResponse("" + status, "student", "checkStudentSubscribe", isSubscribed);
        entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

}
