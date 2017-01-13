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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.Notification.Helper.NotifyByEmail;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.NotificationEmailService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.SibConstants;

/**
 * {@link NotificationEmailService}
 * 
 * @author hungpd
 * @version 1.0
 */
@RestController
@RequestMapping("/siblinks/services/contact")
public class NotificationEmailServiceImpl implements NotificationEmailService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private HttpServletRequest context;

    @Autowired
    ObjectDao dao;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private Environment env;

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public ResponseEntity<Response> contact(@RequestBody final RequestData request) {

        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Name", request.getContact_data().getName());
            map.put("BODY", request.getContact_data().getMessage());
            map.put("PHONE", request.getContact_data().getPhone());

            logger.debug(map);
            NotifyByEmail notify = new NotifyByEmail();
            notify.setMailSender(mailSender);
            notify.setVelocityEngine(velocityEngine);
            notify.sendHmtlTemplateEmail(
                request.getContact_data().getEmail(),
                env.getProperty("spring.mail.username"),
                null,
                null,
                request.getContact_data().getSubject(),
                "MAIL_Notify_3.vm",
                map);
            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                "Thank you for contacting us. We will get back to you soon");
        } catch (Exception e) {
            logger.error(e);
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
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
    public ResponseEntity<Response> forgotPassword(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse("" + Boolean.FALSE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }
            String email = request.getRequest_data().getEmail();

            // check email is exist
            List<Object> userType = dao.readObjects(
                SibConstants.SqlMapper.SQL_CHECK_USER_FORGOT_PASSWORD,
                new Object[] { email, email });
            if (!CollectionUtils.isEmpty(userType)) {
                String generateToken = CommonUtil.generateToken();

                // Update DB
                String add = "";
                boolean statusIn = dao.insertUpdateObject(
                    SibConstants.SqlMapper.SQL_UPDATE_USER_CODE,
                    new Object[] { generateToken, email });
                if (statusIn) {

                    // Get address web configuration DB
                    List<Object> readObjects = dao.readObjects(
                        SibConstants.SqlMapper.SQL_GET_ADDRESS_WEB,
                        new Object[] { SibConstants.DOMAIN });
                    for (Object object : readObjects) {
                        Map<String, String> mapObject = (HashMap<String, String>) object;
                        add = mapObject.get(Parameters.VALUE_OF);
                        break;
                    }
                }

                HashMap<String, String> map = new HashMap<String, String>();
                String strUserType = ((HashMap<String, String>) userType.get(0)).get(Parameters.USER_TYPE);
                if (strUserType != null && !strUserType.equals(SibConstants.ROLE_TYPE.M.toString())) {
                    map.put("FORGOT", add + "studentForgotPassword?token=" + generateToken);
                } else {
                    map.put("FORGOT", add + "mentor/mentorForgotPassword?token=" + generateToken);
                }

                NotifyByEmail notify = new NotifyByEmail();
                notify.setMailSender(mailSender);
                notify.setVelocityEngine(velocityEngine);
                notify.sendHmtlTemplateEmail(
                    null,
                    email,
                    null,
                    null,
                    env.getProperty("app.subject-email.forgot-password"),
                    "MAIL_Notify_4.vm",
                    map);
                simpleResponse = new SimpleResponse(
                                                    SibConstants.SUCCESS,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    "You password was successfully changed");
            } else {
                // Error email is not exist
                simpleResponse = new SimpleResponse(
                                                    SibConstants.FAILURE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    email + " is not exist");
            }
        } catch (Exception e) {
            logger.error(e);
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }
}
