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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        SimpleResponse response = new SimpleResponse(
                                                     "" +
                                                     Boolean.TRUE,
                                                     "student",
                                                     "getMentorSubscribed",
                                                     listMentorSubsribed,
                                                     count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

}
