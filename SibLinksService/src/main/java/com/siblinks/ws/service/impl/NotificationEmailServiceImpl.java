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

import com.siblinks.ws.Notification.Helper.NotificationInfo;
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

            NotificationInfo info = new NotificationInfo();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("###Name###", request.getRequest_data().getName());
            map.put("###BODY###", request.getRequest_data().getMessage());
            map.put("subject", request.getRequest_data().getSubject());
            map.put("Host", "");
            map.put("From", request.getRequest_data().getEmail());
            map.put("To", "siblinks.brot@gmail.com");
            map.put("DomainName", "gmail.com");
            map.put("Cc", "");
            map.put("Bcc", "");
            map.put("templateName", "MAIL_Notify_3.template");
            logger.debug(map);
            info.setTemplateMap(map);
            // NotifyByEmail notify = new NotifyByEmail(info);
            String status = "SUCCESS";
            boolean statusCheck = true;
            // status = notify.sendMail();
            String msg = null;
            if ("SUCCESS".equalsIgnoreCase(status)) {
                msg = "Thank you for contacting us. We will get back to you soon";
            } else {
                msg = "FAIL";
                statusCheck = false;
            }
            simpleResponse = new SimpleResponse(
                                                "" + statusCheck,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                msg);
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
            List<Object> readObjects = dao.readObjects(
                SibConstants.SqlMapper.SQL_CHECK_USER_FORGOT_PASSWORD,
                new Object[] { email, email });
            if (!CollectionUtils.isEmpty(readObjects)) {
                String generateToken = CommonUtil.generateToken();

                // Update DB
                String add = "";
                boolean statusIn = dao.insertUpdateObject(
                    SibConstants.SqlMapper.SQL_UPDATE_USER_CODE,
                    new Object[] { generateToken, email });
                if (statusIn) {

                    // Get address web configuration DB
                    readObjects = dao.readObjects(
                        SibConstants.SqlMapper.SQL_GET_ADDRESS_WEB,
                        new Object[] { SibConstants.DOMAIN });
                    for (Object object : readObjects) {
                        Map<String, String> mapObject = (HashMap<String, String>) object;
                        add = mapObject.get(Parameters.VALUE_OF);
                        break;
                    }
                }

                // Send email
                boolean isSendSuccess = true;
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("FORGOT", add + "forgotPassword?token=" + generateToken);

                NotifyByEmail notify = new NotifyByEmail();
                notify.setMailSender(mailSender);
                notify.setVelocityEngine(velocityEngine);
                notify
                    .sendHmtlTemplateEmail(email, env.getProperty("app.subject-email.forgot-password"), "MAIL_Notify_4.vm", map);

                //
                String msg = (isSendSuccess) ? "You password was successfully changed" : "System cannot send email";
                simpleResponse = new SimpleResponse(
                                                    "" + isSendSuccess,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    msg);
            } else {
                // Error email is not exist
                simpleResponse = new SimpleResponse(
                                                    SibConstants.FAILURE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    email + " is not exits");
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
