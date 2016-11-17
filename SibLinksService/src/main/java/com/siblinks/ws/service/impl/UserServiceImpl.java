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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.Notification.Helper.NotifyByEmail;
import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.ActivityLogData;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.model.User;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.ActivityLogService;
import com.siblinks.ws.service.UserService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.RandomString;
import com.siblinks.ws.util.SibConstants;
import com.siblinks.ws.util.StringUtil;

/**
 *
 * {@link UserService}
 *
 * @author hungpd
 * @version 1.0
 */
@RestController
@RequestMapping("/siblinks/services/user")
public class UserServiceImpl implements UserService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ObjectDao dao;

    @Autowired
    private Environment environment;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private ActivityLogService activityLogService;

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getUsers", method = RequestMethod.POST)
    public ResponseEntity<Response> getUsers(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = { request.getRequest_data().getUsertype() };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_USERID, queryParams);

            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllUsers(@RequestParam(value = "_search") final String search,
            @RequestParam(value = "nd") final String nd, @RequestParam(value = "rows") final int rows, @RequestParam(
                    value = "page") final int page, @RequestParam(value = "sidx") final String sidx,
            @RequestParam(value = "sord") final String sord) {

        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            // String whereClase = " WHERE userType=? ";
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ALL_USERS, new Object[] {});

            // Return for rows
            response = new SimpleResponse(readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "users", "getAllUsers", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getUserNotes", method = RequestMethod.POST)
    public ResponseEntity<Response> getUserNotes(@RequestBody final RequestData request) {

        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }
            Object[] queryParams = { request.getRequest_data().getUid() };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_NOTE_USER, queryParams);

            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "users", "getUserNotes", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getStudentMentors", method = RequestMethod.POST)
    public ResponseEntity<Response> getStudentMentors(@RequestBody final RequestData request) {

        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }
            Object[] queryParams = { request.getRequest_data().getUid() };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_STUDENT_MENTORS, queryParams);

            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "users", "getStudentMentors", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/collegesOrUniversities", method = RequestMethod.POST)
    public ResponseEntity<Response> collegesOrUniversities(@RequestBody final RequestData request) {

        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = {};
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_COL_UNIVERSITIES, queryParams);

            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/majors", method = RequestMethod.POST)
    public ResponseEntity<Response> majors(@RequestBody final RequestData request) {

        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }
            Object[] queryParams = {};

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_MAJORS, queryParams);

            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/extracurricularActivities", method = RequestMethod.POST)
    public ResponseEntity<Response> extracurricularActivities(@RequestBody final RequestData request) {

        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }
            Object[] queryParams = {};
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_EXTRA_ACTIVITIES, queryParams);
            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    @RequestMapping(value = "/signupcomplete", method = RequestMethod.POST)
    public ResponseEntity<Response> signupcomplete(@RequestBody final RequestData request) throws FileNotFoundException {

        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = { request.getRequest_data().getEmail(), request.getRequest_data().getPassword(), request
                .getRequest_data()
                .getFirstname(), request.getRequest_data().getLastname(), request.getRequest_data().getUsertype(), request
                .getRequest_data()
                .getDob(), request.getRequest_data().getEducation(), request.getRequest_data().getAccomp(), request
                .getRequest_data()
                .getColmajor(), request.getRequest_data().getActivities(), request.getRequest_data().getHelpin(), request
                .getRequest_data()
                .getFamilyincome(), request.getRequest_data().getYourdream(), environment.getProperty("directoryImageAvatar") };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_REGISTER_USER_EXIST, queryParams);

            boolean status = Boolean.FALSE;
            if (readObject.size() == 0) {

                List<Object> msgs1 = null;
                status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIGNUP_COMPLETE_USER, queryParams);

                if (status) {
                    msgs1 = dao.readObjects(SibConstants.SqlMapper.SQL_GET_USERID, queryParams);
                    if (msgs1 != null && msgs1.size() > 0) {
                        ((Map) msgs1.get(0)).get(Parameters.USERID).toString();
                    }
                }

                if (request.getRequest_data().getColmajor() != null) {
                    new ArrayList<String>(Arrays.asList(request.getRequest_data().getColmajor().split(",")));
                }

                if (request.getRequest_data().getActivities() != null) {
                    new ArrayList<String>(Arrays.asList(request.getRequest_data().getActivities().split(",")));
                }

                if (request.getRequest_data().getHelpin() != null) {
                    new ArrayList<String>(Arrays.asList(request.getRequest_data().getHelpin().split(",")));
                }
            } else {
                readObject = new ArrayList<Object>();
                readObject.add("Email Address is Already Registered");
            }

            response = new SimpleResponse(
                                          "" + status,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/adminRegisterUser", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> adminRegisterUser(@RequestParam(value = "username") final String username,
            @RequestParam(value = "password") final String password, @RequestParam(value = "firstname") final String firstname,
            @RequestParam(value = "lastname") final String lastname) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { username, password, firstname, lastname };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_REGISTER_USER_EXIST, queryParams);
            boolean status = Boolean.FALSE;
            if (readObject.size() == 0) {
                boolean msgs = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_ADMIN_REGISTER_USER, queryParams);
                readObject = new ArrayList<Object>();
                if (msgs) {
                    readObject.add("Successfully Registered");
                    status = Boolean.TRUE;
                } else {
                    readObject.add("Fail Registration");
                }
            } else {
                readObject = new ArrayList<Object>();
                readObject.add("Email Address is Already Registered");
            }

            response = new SimpleResponse("" + status, readObject.get(0).toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "users", "adminRegisterUser", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/registerAdminMentor", method = RequestMethod.POST)
    public ResponseEntity<Response> registerAdminMentor(@RequestBody final String jsonRegister) {
        SimpleResponse response = null;
        try {
            JSONObject objRequest = new JSONObject(jsonRegister);
            String userName = objRequest.getString(Parameters.USER_NAME);
            String role = objRequest.getString(Parameters.ROLE);
            BCryptPasswordEncoder ecy = new BCryptPasswordEncoder(SibConstants.LENGHT_AUTHENTICATION);
            List<Object> userResponse = dao.readObjects(
                SibConstants.SqlMapper.SQL_SIB_REGISTER_USER_EXIST,
                new Object[] { userName });
            String message = "";
            boolean status = false;
            boolean isRegisterAdmin = role.equals("A");
            if (CollectionUtils.isEmpty(userResponse) || userResponse == null) {
                String dob = objRequest.getString(Parameters.DOB);
                if (!StringUtils.isEmpty(dob)) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                    Date date = formatter.parse(dob);
                    dob = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                } else {
                    dob = null;
                }

                String rawPwd = isRegisterAdmin ? objRequest.getString(Parameters.PASSWORD) : CommonUtil
                    .getInstance()
                    .getAutoGeneratePwd();
                String pwdEncrypt;
                Object[] queryParams = null;
                if (!StringUtils.isEmpty(rawPwd)) {
                    pwdEncrypt = ecy.encode(rawPwd);
                } else {
                    pwdEncrypt = ecy.encode(SibConstants.DEFAULT_PWD);
                    rawPwd = SibConstants.DEFAULT_PWD;
                }
                if (isRegisterAdmin) {
                    queryParams = new Object[] { userName, role, objRequest.getString(Parameters.FIRST_NAME), objRequest
                        .getString(Parameters.LAST_NAME), pwdEncrypt, dob, objRequest.getString(Parameters.ACTIVE_PLAG) };
                    status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_ADMIN_ADD_ANOTHER_ADMIN, queryParams);
                } else {
                    queryParams = new Object[] { userName, role, objRequest.getString(Parameters.FIRST_NAME), objRequest
                        .getString(Parameters.LAST_NAME), pwdEncrypt, dob, objRequest.getString(Parameters.BIO), objRequest
                        .getString(Parameters.SCHOOL), objRequest.getString(Parameters.DEFAULT_SUBJECT_ID), objRequest
                        .getString(Parameters.ACCOMPLISHMENT), objRequest.getString(Parameters.ACTIVE_PLAG) };
                    status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_ADMIN_ADD_ANOTHER_MENTOR, queryParams);
                }
                if (status) {
                    String urlLogin = "";
                    String paramGetUrlDomain = isRegisterAdmin ? SibConstants.DOMAIN_NAME_ADMIN : SibConstants.DOMAIN;
                    List<Object> readObjects = dao.readObjects(
                        SibConstants.SqlMapper.SQL_GET_ADDRESS_WEB,
                        new Object[] { paramGetUrlDomain });
                    for (Object object : readObjects) {
                        Map<String, String> mapObject = (HashMap<String, String>) object;
                        urlLogin = mapObject.get(Parameters.VALUE_OF);
                        break;
                    }

                    urlLogin = isRegisterAdmin ? urlLogin : urlLogin.concat(Parameters.LOGIN_MENTOR_URL);
                    // Send email
                    try {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("userName", userName);
                        map.put("password", rawPwd);
                        map.put("URL_LOGIN", urlLogin);
                        NotifyByEmail notify = new NotifyByEmail();
                        notify.setMailSender(mailSender);
                        notify.setVelocityEngine(velocityEngine);
                        notify.sendHmtlTemplateEmail(
                            null,
                            userName,
                            null,
                            null,
                            environment.getProperty("app.subject-email.registration-mentor"),
                            "MAIL_Notify_5.vm",
                            map);
                        message = "Successfully registered";
                    } catch (Exception e) {
                        logger.error(e);
                        status = false;
                        message = "Email not unavailable, Plz check !!";
                    }
                } else {
                    message = "Fail registration";
                }
            } else {
                status = false;
                message = "Email address is already registered";
            }
            response = new SimpleResponse("" + status, "user", "registerAdminMentor", message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "registerAdminMentor", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/adminUpdateProfileUser", method = RequestMethod.POST)
    public ResponseEntity<Response> adminUpdateProfileUser(@RequestBody final String jsonUpdate) {
        SimpleResponse response = null;
        try {
            JSONObject jsonRequest = new JSONObject(jsonUpdate);
            String userId = jsonRequest.getString(Parameters.USER_ID);
            String message = "";
            boolean status = false;
            if (userId != null && !StringUtils.isEmpty(userId)) {
                List<Object> readObjects = dao.readObjects(
                    SibConstants.SqlMapper.SQL_CHECK_USER_EXISTS_BY_ID,
                    new Object[] { userId });
                if (!CollectionUtils.isEmpty(readObjects)) {
                    // user is exists
                    String dob = jsonRequest.getString(Parameters.DOB);
                    try {
                        if (!StringUtils.isEmpty(dob)) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                            Date date = formatter.parse(dob);
                            dob = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                        } else {
                            dob = null;
                        }
                    } catch (Exception e) {
                        dob = null;
                    }
                    Object[] params = { jsonRequest.getString(Parameters.EMAIL), dob, jsonRequest
                        .getString(Parameters.FIRST_NAME), jsonRequest.getString(Parameters.LAST_NAME), jsonRequest
                        .getString(Parameters.SCHOOL), jsonRequest.getString(Parameters.DEFAULT_SUBJECT_ID), jsonRequest
                        .getString(Parameters.ACCOMPLISHMENT), jsonRequest.getString(Parameters.BIO), jsonRequest
                        .getString(Parameters.ACTIVE_PLAG), userId };
                    status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_ADMIN_UPDATE_PROFILE_MENTOR, params);
                    if (status) {
                        message = "Updated Profile Successfully";
                    } else {
                        message = "Updated Profile Failure";
                    }
                }
            } else {
                message = "Updated Profile Failure";
            }
            response = new SimpleResponse("" + status, "user", "adminUpdateProfileUser", message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "adminUpdateProfileMentor", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/adminUpdateProfileMentor", method = RequestMethod.POST)
    public ResponseEntity<Response> adminUpdateProfileMentor(@RequestBody final String jsonUpdate) {

        SimpleResponse response = null;
        try {
            JSONObject jsonRequest = new JSONObject(jsonUpdate);
            String userId = jsonRequest.getString(Parameters.USER_ID);
            String message = "";
            boolean status = false;
            if (userId != null && !StringUtils.isEmpty(userId)) {
                List<Object> readObjects = dao.readObjects(
                    SibConstants.SqlMapper.SQL_CHECK_USER_EXISTS_BY_ID,
                    new Object[] { userId });
                if (!CollectionUtils.isEmpty(readObjects)) {
                    // user is exists
                    String dob = jsonRequest.getString(Parameters.DOB);

                    if (!StringUtils.isEmpty(dob)) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                        Date date = formatter.parse(dob);
                        dob = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                    } else {
                        dob = null;
                    }
                    Object[] params = { jsonRequest.getString(Parameters.EMAIL), dob, jsonRequest
                        .getString(Parameters.FIRST_NAME), jsonRequest.getString(Parameters.LAST_NAME), jsonRequest
                        .getString(Parameters.SCHOOL), jsonRequest.getString(Parameters.DEFAULT_SUBJECT_ID), jsonRequest
                        .getString(Parameters.ACCOMPLISHMENT), jsonRequest.getString(Parameters.BIO), jsonRequest
                        .getString(Parameters.ACTIVE_PLAG), userId };
                    status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_ADMIN_UPDATE_PROFILE_MENTOR, params);
                    if (status) {
                        message = "Updated Profile Successfully";
                    } else {
                        message = "Updated Profile Failure";
                    }
                }
            } else {
                message = "Updated Profile Failure";
            }
            response = new SimpleResponse("" + status, "user", "adminUpdateProfileMentor", message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "adminUpdateProfileMentor", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/setStatusUser", method = RequestMethod.POST)
    public ResponseEntity<Response> setStatusUser(@RequestBody final String json) {
        SimpleResponse response = null;
        try {

            boolean status = false;
            JSONObject jsonObj = new JSONObject(json);
            long userId = jsonObj.getLong(Parameters.USER_ID);
            String enableFlag = jsonObj.getString(Parameters.ACTIVE_PLAG);
            if (userId > 0 && !StringUtils.isEmpty(enableFlag)) {
                List<Object> readObjects = dao.readObjects(
                    SibConstants.SqlMapper.SQL_CHECK_USER_EXISTS_BY_ID,
                    new Object[] { userId });
                if (!CollectionUtils.isEmpty(readObjects)) {
                    // user is exists
                    Object[] params = { enableFlag, userId };
                    String entityName = SibConstants.SqlMapper.SQL_SET_ENABLE_FLAG_USER;
                    status = dao.insertUpdateObject(entityName, params);

                    if (status) {
                    } else {
                    }
                }
            }
        } catch (Exception e) {
            response = new SimpleResponse(SibConstants.FAILURE, "user", "setDisableUser", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);

    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value = "/updateInfoAdmin", method = RequestMethod.POST)
    public ResponseEntity<Response> updateInfoAdmin(@RequestBody final String jsonUpdate) {
        SimpleResponse response = null;
        try {
            JSONObject jsonRequest = new JSONObject(jsonUpdate);
            String userId = jsonRequest.getString(Parameters.USER_ID);
            boolean status = false;
            if (userId != null && !StringUtils.isEmpty(userId)) {
                List<Object> readObjects = dao.readObjects(
                    SibConstants.SqlMapper.SQL_CHECK_USER_EXISTS_BY_ID,
                    new Object[] { userId });
                if (!CollectionUtils.isEmpty(readObjects)) {
                    // user is exists
                    String entityName = SibConstants.SqlMapper.SQL_UPDATE_ADMIN_INFO;
                    String dob = jsonRequest.getString(Parameters.DOB);
                    if (!StringUtils.isEmpty(dob)) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                        Date date = formatter.parse(dob);
                        dob = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                    } else {
                        dob = null;
                    }

                    Object[] params = { jsonRequest.getString(Parameters.USER_NAME), dob, jsonRequest
                        .getString(Parameters.FIRST_NAME), jsonRequest.getString(Parameters.LAST_NAME), jsonRequest
                        .getString(Parameters.ACTIVE_PLAG), userId };
                    status = dao.insertUpdateObject(entityName, params);
                    if (status) {
                    } else {
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "updateInfoAdmin", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> registerUser(@RequestBody final String jsonRegister) {
        SimpleResponse response = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonRegister);
            String username = jsonObject.getString(Parameters.USER_NAME);
            BCryptPasswordEncoder ecy = new BCryptPasswordEncoder(SibConstants.LENGHT_AUTHENTICATION);
            Object[] queryParams = { username, ecy.encode(jsonObject.getString(Parameters.PASSWORD)), environment
                .getProperty("directoryImageAvatar") };
            //
            List<Object> readObject = dao.readObjects(
                SibConstants.SqlMapper.SQL_SIB_REGISTER_USER_EXIST,
                new Object[] { username });
            //
            boolean status = Boolean.FALSE;
            if (CollectionUtils.isEmpty(readObject)) {
                boolean msgs = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_REGISTER_USER, queryParams);
                readObject = new ArrayList<Object>();
                if (msgs) {
                    readObject.add("Successfully registered");
                    status = Boolean.TRUE;
                } else {
                    readObject.add("Fail registration");
                }
            } else {
                readObject = new ArrayList<Object>();
                readObject.add("Email address is already registered");
            }

            response = new SimpleResponse("" + status, readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "registerUser", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping("/isUsernameAvailable")
    public @ResponseBody ResponseEntity<Response> isUsernameAvailable(@RequestParam(value = "username") final String username) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { username };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_IS_USERNAME_AVAIBALE, queryParams);
            boolean status = false;
            if (!CollectionUtils.isEmpty(readObject)) {
                status = true;
                readObject.add("User is  available");
            } else {
                readObject = new ArrayList<Object>();
                readObject.add("User is not available");
            }

            response = new SimpleResponse("" + status, readObject);

        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "isUsernameAvailable", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping("/adminloginUser")
    public @ResponseBody ResponseEntity<Response> adminloginUser(@RequestParam(value = "username") final String username,
            @RequestParam(value = "password") final String password) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { username, password };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_ADMIN_LOGIN_USER, queryParams);

            boolean status = false;

            if (!CollectionUtils.isEmpty(readObject)) {
                status = true;
                readObject = new ArrayList<Object>();
                readObject.add("User Name is " + username);

                // Create session if login succeeded
                HttpSession session = context.getSession();
                if (session != null) {
                    // Save the username to the session
                    session.setAttribute(Parameters.USER_NAME, username);
                }
                updateLastOnlineTime(username);
            } else {
                readObject = new ArrayList<Object>();
                readObject.add("User Name and Password is not correct");
            }

            response = new SimpleResponse("" + status, readObject.get(0).toString());

        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "adminloginUser", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping("/loginUser")
    public @ResponseBody ResponseEntity<Response> loginUser(@RequestParam(value = "username") final String username,
            @RequestParam(value = "password") final String password) {
        SimpleResponse response = null;
        try {

            Object[] queryParams = { username, password };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_LOGIN_USER, queryParams);

            boolean status = Boolean.FALSE;

            if (!CollectionUtils.isEmpty(readObject)) {
                status = Boolean.TRUE;
                // Create session if login succeeded
                HttpSession session = context.getSession();
                if (session != null) {
                    // Save the username to the session
                    session.setAttribute("username", username);
                }
                updateLastOnlineTime(username);
            } else {
                readObject = new ArrayList<Object>();
                readObject.add("User Name and Password is not correct");
            }

            response = new SimpleResponse("" + status, readObject);

        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "loginUser", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    // // Update password based on email id
    // @Override
    // @RequestMapping("/resetPassword")
    // public @ResponseBody
    // ResponseEntity<Response> resetPassword(@RequestParam(value = "username")
    // final String username, @RequestParam(
    // value = "password") final String password) {
    // Object[] queryParams = { username, password };
    //
    // // check is email id exist
    // ResponseEntity<Response> isUserExist = isUsernameAvailable(username);
    // String userStatus = isUserExist.getBody().getStatus();
    // logger.debug(userStatus);
    //
    // // record exist
    // boolean status = true;
    // List<Object> msg = null;
    // msg = new ArrayList<Object>();
    //
    // if (userStatus.equals(SibConstants.SUCCESS)) {
    // // update user entered password
    // status =
    // dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_RESET_PASSWORD,
    // queryParams);
    // if (status) {
    // msg.add("Password has been updated");
    // } else {
    // msg.add("Failed to update password");
    // }
    // } else {
    // status = Boolean.FALSE;
    // msg.add("User Name is not correct");
    // }
    //
    // response = new SimpleResponse("" + status, msg);
    // ResponseEntity<Response> entity = new ResponseEntity<Response>(response,
    // HttpStatus.OK);
    // return entity;
    // }
    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity<Response> changePassword(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            BCryptPasswordEncoder ecy = new BCryptPasswordEncoder(SibConstants.LENGHT_AUTHENTICATION);
            // check old password correct or not
            Object[] queryParams = { request.getRequest_user().getUsername() };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_LOGIN_USER, queryParams);
            if (!CollectionUtils.isEmpty(readObject)) {
                // Verify password
                // String rawPwd =
                // ecy.encode(request.getRequest_user().getPassword());
                Map<String, String> user = (HashMap<String, String>) readObject.get(SibConstants.NUMBER.ZERO);
                String encryptPwd = user.get(Parameters.PASSWORD);
                if (encryptPwd != null && !StringUtils.isEmpty(encryptPwd)) {
                    // Verify old password
                    if (CommonUtil.verifyPassword(request.getRequest_user().getPassword(), encryptPwd)) {
                        // Update new password
                        boolean status = dao.insertUpdateObject(
                            SibConstants.SqlMapper.SQL_UPDATE_PASSWORD,
                            new Object[] { ecy.encode(request.getRequest_user().getNewpassword()), request
                                .getRequest_user()
                                .getUsername() });
                        if (status) {
                            response = new SimpleResponse(
                                                          "" + Boolean.TRUE,
                                                          request.getRequest_data_type(),
                                                          request.getRequest_data_method(),
                                                          "Changed Password Successfully");
                        } else {
                            response = new SimpleResponse(
                                                          SibConstants.FAILURE,
                                                          request.getRequest_data_type(),
                                                          request.getRequest_data_method(),
                                                          "Change password is failed. Please contact with administrator");
                        }
                    } else {
                        response = new SimpleResponse(
                                                      SibConstants.FAILURE,
                                                      request.getRequest_data_type(),
                                                      request.getRequest_data_method(),
                                                      "Old password is not correctly");

                    }
                } else {
                    // User is exist
                    response = new SimpleResponse(
                                                  SibConstants.FAILURE,
                                                  request.getRequest_data_type(),
                                                  request.getRequest_data_method(),
                                                  "Your account can not change password.");
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "changePassword", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/changePasswordForgot", method = RequestMethod.POST)
    public ResponseEntity<Response> changePasswordForgot(@RequestBody final String jsonData) {
        SimpleResponse response = null;
        try {
            // read json data
            JSONObject jsonObject = new JSONObject(jsonData);
            String token = jsonObject.getString("token");
            String newPwd = jsonObject.getString("newPwd");

            BCryptPasswordEncoder ecy = new BCryptPasswordEncoder(SibConstants.LENGHT_AUTHENTICATION);
            // check old password correct or not
            boolean status = dao.insertUpdateObject(
                SibConstants.SqlMapper.SQL_SIB_RESET_PASSWORD,
                new Object[] { ecy.encode(newPwd), token });
            if (status) {
                response = new SimpleResponse(SibConstants.SUCCESS, "", "changePasswordForgot", "Success");
            } else {
                response = new SimpleResponse(SibConstants.FAILURE, "", "changePasswordForgot", "Failure");
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "changePasswordForgot", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    // @Override
    // @RequestMapping(value = "/updateUserProfileBasic", method =
    // RequestMethod.POST)
    // public ResponseEntity<Response> updateUserProfileBasic(@RequestBody final
    // RequestData request) {
    // // DaoFactory factory = DaoFactory.getDaoFactory();
    // // ObjectDao dao = factory.getObjectDao();
    // Object[] queryParams = {
    //
    // request.getRequest_data().getUsername(),
    // request.getRequest_data().getFirstname(), request
    // .getRequest_data()
    // .getLastname(), request.getRequest_data().getBio() };
    //
    // List<Object> msg = null;
    // msg = new ArrayList<Object>();
    // boolean status = Boolean.TRUE;
    // status =
    // dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_UPDATE_USER_PROFILE_BASIC,
    // queryParams);
    // if (status) {
    // msg.add("User Profile Basic has been updated");
    // } else {
    // msg.add("Failed to update profile");
    // }
    // response = new SimpleResponse(
    // SibConstants.SUCCESS,
    // request.getRequest_data_type(),
    // request.getRequest_data_method(),
    // msg);
    // ResponseEntity<Response> entity = new ResponseEntity<Response>(response,
    // HttpStatus.OK);
    // return entity;
    // }

    // @Override
    // @RequestMapping(value = "/updateUserProfile", method =
    // RequestMethod.POST)
    // public ResponseEntity<Response> updateUserProfile(@RequestBody final
    // RequestData request) {
    // // DaoFactory factory = DaoFactory.getDaoFactory();
    // // ObjectDao dao = factory.getObjectDao();
    // Object[] queryParams = { request.getRequest_data().getUid(),
    // request.getRequest_data().getUsername(), request
    // .getRequest_data()
    // .getCurrentclass(), request.getRequest_data().getAccomplishments(),
    // request.getRequest_data().getMajorid(), request
    // .getRequest_data()
    // .getActivityid(), request.getRequest_data().getSubjectId() };
    //
    // boolean delete = true;
    // String userId = request.getRequest_data().getUid();
    //
    // if (request.getRequest_data().getMajorid().length() != 0) {
    // List<String> myListMajorId = new
    // ArrayList<String>(Arrays.asList(request.getRequest_data().getMajorid().split(",")));
    // delete =
    // dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_USER_MAJOR,
    // queryParams);
    // if (delete) {
    // insertNotResource(myListMajorId, userId,
    // SibConstants.SqlMapper.SQL_INSERT_SIB_USER_MAJOR);
    // }
    // }
    //
    // if (request.getRequest_data().getActivityid().length() != 0) {
    // List<String> myListActivityId = new
    // ArrayList<String>(Arrays.asList(request
    // .getRequest_data()
    // .getActivityid()
    // .split(",")));
    // delete =
    // dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_USER_ACTIVITY,
    // queryParams);
    // if (delete) {
    // insertNotResource(myListActivityId, userId,
    // SibConstants.SqlMapper.SQL_INSERT_SIB_USER_ACTIVITY);
    // }
    // }
    //
    // if (request.getRequest_data().getSubjectId().length() != 0) {
    // List<String> myListHelpId = new
    // ArrayList<String>(Arrays.asList(request.getRequest_data().getSubjectId().split(",")));
    // delete =
    // dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_USER_SUBJECT,
    // queryParams);
    // if (delete) {
    // insertNotResource(myListHelpId, userId,
    // SibConstants.SqlMapper.SQL_INSERT_SIB_USER_SUBJECT);
    // }
    // }
    //
    // List<Object> msg = null;
    // msg = new ArrayList<Object>();
    // boolean status = Boolean.TRUE;
    // status =
    // dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_UPDATE_USER_PROFILE,
    // queryParams);
    // if (status) {
    // msg.add("User Profile has been updated");
    // } else {
    // msg.add("Failed to update profile");
    // }
    // response = new SimpleResponse(
    // SibConstants.SUCCESS,
    // request.getRequest_data_type(),
    // request.getRequest_data_method(),
    // msg);
    // ResponseEntity<Response> entity = new ResponseEntity<Response>(response,
    // HttpStatus.OK);
    // return entity;
    // }
    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping("/updateLastOnlineTime")
    public @ResponseBody ResponseEntity<Response> updateLastOnlineTime(@RequestParam(value = "username") final String username) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { username };

            dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATELASTONLINETIME, queryParams);
            response = new SimpleResponse(SibConstants.SUCCESS, "");
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "updateLastOnlineTime", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping("/saveDefaultSubject")
    public @ResponseBody ResponseEntity<Response> saveDefaultSubject(@RequestParam(value = "uid") final String uid,
            @RequestParam(value = "sid") final String sid) {
        SimpleResponse response = null;
        try {

            Object[] queryParams = { uid, sid };

            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SAVE_DEFAULT_SUBJECT, queryParams);
            response = new SimpleResponse("" + status, "");
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "saveDefaultSubject", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping("/addUserNotes")
    public ResponseEntity<Response> addUserNotes(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getMentorid(), request
                .getRequest_data()
                .getNote() };

            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_INSERT_USER_NOTE, queryParams);
            response = new SimpleResponse("" + status, "");
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping("/findUser")
    public @ResponseBody ResponseEntity<Response> findUser(@RequestParam(value = "name") final String name) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { name };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_FIND_USER, queryParams);

            boolean status = true;

            if (CollectionUtils.isEmpty(readObject)) {
                status = false;
                readObject = new ArrayList<Object>();
                readObject.add("User  is empty");
            }

            response = new SimpleResponse("" + status, readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "findUser", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getUserProfile", method = RequestMethod.GET)
    public ResponseEntity<Response> getUserProfile(@RequestParam final long userid) {
        SimpleResponse response = null;
        try {
            Map<String, Object> result = null;

            Object[] queryParams = { userid };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_PROFILE, queryParams);
            if (!CollectionUtils.isEmpty(readObject)) {
                for (Object object : readObject) {
                    result = (Map) object;
                    // List<Object> resChildObject = new ArrayList<Object>();
                    // Map<String, Object> map1 = (Map) object;

                    // List<Object> readObject1 = getElementOfUser(map1,
                    // Parameters.MAJOR_OF_USER);
                    //
                    // List<Object> readObject2 = getElementOfUser(map1,
                    // Parameters.ACTIVITY_OF_USER);
                    //
                    // List<Object> readObject3 = getElementOfUser(map1,
                    // Parameters.HELP_OF_USER);
                    //
                    // Map<String, Object> mymap = new HashMap<String,
                    // Object>();
                    // mymap.put(Parameters.MAJORS, readObject1);
                    // mymap.put(Parameters.ACTIVITY, readObject2);
                    // mymap.put(Parameters.HELP, readObject3);
                    // resChildObject.add(map1);
                    // resChildObject.add(mymap);
                    // resParentObject.add(resChildObject);
                }
                // Get information relate
                String userType = "" + result.get(Parameters.USER_TYPE);
                Map<String, Object> relateUserProfile = getRelateUserProfile(userType, queryParams);
                if (relateUserProfile != null) {
                    result.putAll(relateUserProfile);
                    response = new SimpleResponse(SibConstants.SUCCESS, "user", "getUserProfile", result);
                } else {
                    response = new SimpleResponse(SibConstants.SUCCESS, "user", "getUserProfile", SibConstants.USER_NOT_EXISTS);
                }
            } else {
                response = new SimpleResponse(SibConstants.FAILURE, SibConstants.NO_DATA);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "getUserProfile", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * This method get information relate of user By user type: Student: Count
     * question, Subscribe, Essay Mentor: count Subscribes, Answer, Video
     *
     * @param userType
     *            Type of user
     * @param queryParams
     *            User id
     * @return Map information of user
     * @throws DAOException
     */
    private Map<String, Object> getRelateUserProfile(final String userType, final Object[] queryParams) throws DAOException {

        Map<String, Object> result = null;
        List<Object> readObject = null;
        // Check Role
        if (SibConstants.ROLE_TYPE.S.toString().equals(userType)) {
            result = new HashMap<String, Object>();
            // Count Question
            readObject = dao.readObjects(SibConstants.SqlMapperBROT71.SQL_GET_COUNT_QUESTION, queryParams);
            result.put("count_question", 0);
            if (!CollectionUtils.isEmpty(readObject)) {
                for (Object object : readObject) {
                    Map<String, String> mapObject = (HashMap<String, String>) object;
                    result.put("count_question", mapObject.get("count(*)"));
                }
            }

            // Count Subscribe
            readObject = dao.readObjects(SibConstants.SqlMapperBROT71.SQL_GET_COUNT_SUBSCIBE, queryParams);
            result.put("count_subscribe", 0);
            if (!CollectionUtils.isEmpty(readObject)) {
                for (Object object : readObject) {
                    Map<String, String> mapObject = (HashMap<String, String>) object;
                    result.put("count_subscribe", mapObject.get("count(*)"));
                }
            }
            // Count Essasy
            readObject = dao.readObjects(SibConstants.SqlMapperBROT71.SQL_GET_COUNT_ESSAY, queryParams);
            result.put("count_essay", 0);
            if (!CollectionUtils.isEmpty(readObject)) {
                for (Object object : readObject) {
                    Map<String, String> mapObject = (HashMap<String, String>) object;
                    result.put("count_essay", mapObject.get("count(*)"));
                }
            }
            // UserType = M
        } else if (SibConstants.ROLE_TYPE.M.toString().equals(userType)) {
            result = new HashMap<String, Object>();
            // Count subscribe
            readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_COUNT_SUBSCRIBERS, queryParams);

            if (!CollectionUtils.isEmpty(readObject)) {
                for (Object object : readObject) {
                    Map<String, String> mapObject = (HashMap<String, String>) object;
                    result.put("count_subscribers", mapObject.get("countSubscribers"));
                }
            } else {
                result.put("count_subscribers", 0);
            }

            // Count answer
            readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_COUNT_ANSWERS, queryParams);
            if (!CollectionUtils.isEmpty(readObject)) {
                for (Object object : readObject) {
                    Map<String, String> mapObject = (HashMap<String, String>) object;
                    result.put("count_answers", mapObject.get("numAnswers"));
                }
            } else {
                result.put("count_answers", 0);
            }

            // Count videos
            readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_COUNT_VIDEOS, queryParams);

            if (!CollectionUtils.isEmpty(readObject)) {
                for (Object object : readObject) {
                    Map<String, String> mapObject = (HashMap<String, String>) object;
                    result.put("count_videos", mapObject.get("numVideos"));
                }
            } else {
                result.put("count_videos", 0);
            }
            // Count Like
            readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_COUNT_LIKES, queryParams);

            if (!CollectionUtils.isEmpty(readObject)) {
                for (Object object : readObject) {
                    Map<String, String> mapObject = (HashMap<String, String>) object;
                    result.put("count_likes", mapObject.get("numLikes"));
                }
            } else {
                result.put("count_likes", 0);
            }

            // List skill
            // readObject =
            // dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_MENTOR_SKILLS,
            // queryParams);
            // String skills = "";
            // if (!CollectionUtils.isEmpty(readObject)) {
            // for (Object object : readObject) {
            // Map<String, String> mapObject = (HashMap<String, String>) object;
            // if (skills.length() == 0) {
            // skills += mapObject.get("subject");
            // } else {
            // skills += ", " + mapObject.get("subject");
            // }
            // }
            // }
            // result.put("skills", skills);
        } else {
            // do nothing
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAccomplishment", method = RequestMethod.POST)
    public ResponseEntity<Response> getAccomplishment(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { request.getRequest_data().getUid() };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_ACCOMPLISHMENT_READ, queryParams);
            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/listOfMajors", method = RequestMethod.POST)
    public ResponseEntity<Response> listOfMajors(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = {};

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_MAJOR_READ, queryParams);

            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/listOfActivity", method = RequestMethod.POST)
    public ResponseEntity<Response> listOfActivity(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = {};

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_ACTIVITY_READ, queryParams);

            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/listCategory", method = RequestMethod.POST)
    public ResponseEntity<Response> listCategory(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = {};

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_HELP_READ, queryParams);

            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/confirmToken", method = RequestMethod.POST)
    public ResponseEntity<Response> confirmToken(@RequestBody final String jsontoken) {
        SimpleResponse response = null;
        try {
            JSONObject jsonObject = new JSONObject(jsontoken);
            String token = "" + jsonObject.getString("token");
            Object[] queryParams = { token };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SELECT_TIME_SEND_TOKEN, queryParams);
            if (readObject.size() > SibConstants.NUMBER.ZERO) {
                // Get time forgot
                String timeForgot = "";
                for (Object object : readObject) {
                    timeForgot = object.toString().substring(12, 31);
                    break;
                }

                // Calculate days
                long noDay = 0L;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timeStamp = formatter.format(Calendar.getInstance().getTime());
                Date dateCurrent = formatter.parse(timeStamp);
                Date dateForgot = formatter.parse(timeForgot);
                noDay = (dateCurrent.getTime() - dateForgot.getTime()) / (24 * 3600 * 1000);

                if (noDay > SibConstants.NUMBER.ONE) {
                    dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_TOKEN_FORGOT_PASSWORD, queryParams);
                } else {
                    readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_TOKEN_FORGOT_PASSWORD, queryParams);
                }
            }
            response = new SimpleResponse(SibConstants.SUCCESS, "user", "confirmToken", readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "user", "confirmToken", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAddressPage", method = RequestMethod.POST)
    public ResponseEntity<Response> getAddressPage(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            List<Map<String, Object>> readObject = dao.readObjectNoCondition(SibConstants.SqlMapper.SQL_GET_ADDRESS_WEB);
            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getPolicy", method = RequestMethod.POST)
    public ResponseEntity<Response> getPolicy(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            List<Map<String, Object>> readObject = dao.readObjectNoCondition(SibConstants.SqlMapper.SQL_GET_POLICY);

            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getTerms", method = RequestMethod.POST)
    public ResponseEntity<Response> getTerms(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            List<Map<String, Object>> readObject = dao.readObjectNoCondition(SibConstants.SqlMapper.SQL_GET_TERMS);

            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateAccomplishmentMobile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateAccomplishmentMobile(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getAccomplishments() };

            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_ACCOMPLISHMENT_MOBILE, queryParams);

            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          status);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateMajorsMobile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateMajorsMobile(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getMajorid() };
            new ArrayList<String>(Arrays.asList(request.getRequest_data().getMajorid().split(",")));
            request.getRequest_data().getUid();
            // insertNotResource(myListMajorId, userId,
            // SibConstants.SqlMapper.SQL_INSERT_SIB_USER_MAJOR);
            List<Object> msg = new ArrayList<Object>();
            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_USER_MAJOR, queryParams);
            if (status) {
                msg.add("Major has been updated");
            } else {
                msg.add("Failed to update major");
            }
            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          msg);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateActivitiesMobile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateActivitiesMobile(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getActivityid() };

            new ArrayList<String>(Arrays.asList(request.getRequest_data().getActivityid().split(",")));
            /*
             * String userId = request.getRequest_data().getUid();
             *
             * insertNotResource(myListActivityId, userId,
             * SibConstants.SqlMapper.SQL_INSERT_SIB_USER_ACTIVITY);
             */
            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_USER_ACTIVITY, queryParams);
            List<Object> msg = new ArrayList<Object>();
            if (status) {
                msg.add("Activities has been updated");
            } else {
                msg.add("Failed to update activities");
            }
            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          msg);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateHelpInMobile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateHelpInMobile(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getTopicId() };

            new ArrayList<String>(Arrays.asList(request.getRequest_data().getTopicId().split(",")));

            request.getRequest_data().getUid();

            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_USER_TOPIC, queryParams);

            // insertNotResource(myListHelpId, userId,
            // SibConstants.SqlMapper.SQL_INSERT_SIB_USER_TOPIC);

            List<Object> msg = new ArrayList<Object>();
            if (status) {
                msg.add("HelpIn has been updated");
            } else {
                msg.add("Failed to update HelpIn");
            }
            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          msg);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateGradeMobile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateGradeMobile(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getCurrentclass() };

            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_GRADE_MOBILE, queryParams);
            //
            List<Object> msg = new ArrayList<Object>();
            if (status) {
                msg.add("Grade has been updated");
            } else {
                msg.add("Failed to update grade");
            }
            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          msg);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateSchoolMobile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateSchoolMobile(@RequestBody final RequestData request) {
        SimpleResponse response = null;
        try {
            Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getEducation() };
            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_SCHOOL_MOBILE, queryParams);
            //
            List<Object> msg = new ArrayList<Object>();
            if (status) {
                msg.add("School has been updated");
            } else {
                msg.add("Failed to update school");
            }
            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          msg);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/loginFacebook", method = RequestMethod.POST)
    public ResponseEntity<Response> loginFacebook(@RequestBody final RequestData request) throws FileNotFoundException {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }
            boolean status = false;
            // Check user
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_CHECK_USER, new Object[] { request
                .getRequest_data()
                .getUsername() });

            // User is not exists
            if (CollectionUtils.isEmpty(readObject)) {
                Object[] queryParamsFB = { request.getRequest_data().getUsername(), request.getRequest_data().getUsertype(), request
                    .getRequest_data()
                    .getFirstname(), request.getRequest_data().getLastname(), request.getRequest_data().getImage(), request
                    .getRequest_data()
                    .getFacebookid(), request.getRequest_data().getToken() };
                status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_CREATE_USER_FACEBOOK, queryParamsFB);
                if (status) {
                    readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_USER_BY_USERNAME, new Object[] { request
                        .getRequest_data()
                        .getUsername() });
                }

                simpleResponse = new SimpleResponse(
                                                    "" + status,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);

            } else {
                Map<String, String> mapUser = (HashMap<String, String>) readObject.get(SibConstants.NUMBER.ZERO);
                // Check Facebook id for update
                if (mapUser.get(Parameters.ID_FACEBOOK) != null &&
                    mapUser.get(Parameters.ID_FACEBOOK).equals(request.getRequest_data().getFacebookid())) {// Registered
                    // Set parameter
                    Object[] queryParams = { request.getRequest_data().getToken(), request.getRequest_data().getFacebookid() };
                    status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_INFO_FACEBOOK, queryParams);
                    simpleResponse = new SimpleResponse(
                                                        SibConstants.SUCCESS,
                                                        request.getRequest_data_type(),
                                                        request.getRequest_data_method(),
                                                        readObject);
                } else {
                    simpleResponse = new SimpleResponse(
                                                        SibConstants.FAILURE,
                                                        request.getRequest_data_type(),
                                                        request.getRequest_data_method(),
                                                        "Facebook id is not match");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
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
    @RequestMapping(value = "/loginGoogle", method = RequestMethod.POST)
    public ResponseEntity<Response> loginGoogle(@RequestBody final RequestData request) throws FileNotFoundException {
        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_CHECK_USER, new Object[] { request
                .getRequest_data()
                .getUsername() });
            if (CollectionUtils.isEmpty(readObject)) {// For register
                Object[] queryParamsGG = { request.getRequest_data().getUsername(), request.getRequest_data().getUsertype(), request
                    .getRequest_data()
                    .getFirstname(), request.getRequest_data().getLastname(), request.getRequest_data().getImage(), request
                    .getRequest_data()
                    .getGoogleid(), request.getRequest_data().getToken() };
                boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_CREATE_USER_GOOGLE, queryParamsGG);
                if (status) {
                    readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_USER_BY_USERNAME, new Object[] { request
                        .getRequest_data()
                        .getUsername() });
                }
                response = new SimpleResponse(
                                              "" + status,
                                              request.getRequest_data_type(),
                                              request.getRequest_data_method(),
                                              readObject);
            } else {
                Map<String, String> mapUser = (HashMap<String, String>) readObject.get(SibConstants.NUMBER.ZERO);
                // Check google id for update
                if (mapUser.get(Parameters.ID_GOOGLE) != null &&
                    mapUser.get(Parameters.ID_GOOGLE).equals(request.getRequest_data().getGoogleid())) {// Registered
                    // Update token
                    dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_INFO_GOOGLE, new Object[] { request
                        .getRequest_data()
                        .getToken(), request.getRequest_data().getGoogleid() });
                    response = new SimpleResponse(
                                                  SibConstants.SUCCESS,
                                                  request.getRequest_data_type(),
                                                  request.getRequest_data_method(),
                                                  readObject);
                } else {
                    response = new SimpleResponse(
                                                  SibConstants.FAILURE,
                                                  request.getRequest_data_type(),
                                                  request.getRequest_data_method(),
                                                  "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("Upload avartar error " + e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> uploadAvatar(@RequestParam("uploadfile") final MultipartFile uploadfile, @RequestParam(
            value = "userid") final String userid, @RequestParam("imageUrl") final String oldNameImgAvatar) throws IOException {

        String filename = "";
        String name = "";
        String filepath = "";
        BufferedOutputStream stream = null;
        SimpleResponse response = null;
        try {
            String directory = environment.getProperty("directoryAvatar");
            String service = environment.getProperty("directoryGetAvatar");
            String strExtenstionFile = environment.getProperty("file.upload.image.type");
            name = uploadfile.getOriginalFilename();
            String nameExt = FilenameUtils.getExtension(name);
            boolean status = strExtenstionFile.contains(nameExt.toLowerCase());
            if (directory != null && status) {
                RandomString randomName = new RandomString();
                filename = randomName.random() + "." + "png";
                filepath = "" + Paths.get(directory, filename);
                // Save the file locally
                File file = new File(filepath);
                File parentDir = file.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                stream = new BufferedOutputStream(new FileOutputStream(file));
                stream.write(uploadfile.getBytes());

                Object[] queryParams = { service + filename, userid };
                dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_AVARTAR_USER, queryParams);
                boolean insertUpdateObject = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_AVARTAR_USER, queryParams);

                // Remove image avatar old
                if (insertUpdateObject && oldNameImgAvatar != null && !"".equals(oldNameImgAvatar)) {
                    String fileName = oldNameImgAvatar.substring(oldNameImgAvatar.lastIndexOf("/"), oldNameImgAvatar.length());
                    File fileOld = new File(directory + fileName);
                    if (fileOld.exists()) {
                        FileUtils.forceDeleteOnExit(fileOld);
                    }
                    activityLogService.insertActivityLog(new ActivityLogData(
                                                                             SibConstants.TYPE_PROFILE,
                                                                             "U",
                                                                             "You updated your avatar",
                                                                             userid,
                                                                             null));
                }
                // Successful return path image avatar
                response = new SimpleResponse(SibConstants.SUCCESS, service + filename);
            } else {
                response = new SimpleResponse(SibConstants.FAILURE, "Not found path or file is not exist");
            }
        } catch (Exception e) {
            response = new SimpleResponse(SibConstants.FAILURE, "Upload avatar error");
            logger.debug("Upload avartar error " + e.getMessage());
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException io) {
                // Do Nothing
            }
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     *
     */
    @SuppressWarnings("resource")
    @Override
    @RequestMapping(value = "/getAvatar/{path}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getAvatar(@PathVariable(value = "path") final String path) {

        logger.info("Call service get avatar");
        RandomAccessFile randomAccessFile = null;
        ResponseEntity<byte[]> responseEntity = null;
        try {
            if (StringUtil.isNull(path)) {
                // Reader avatar file
                randomAccessFile = new RandomAccessFile(path, "r");
                byte[] r = new byte[(int) randomAccessFile.length()];
                randomAccessFile.readFully(r);

                responseEntity = new ResponseEntity<byte[]>(r, new HttpHeaders(), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            logger.debug("File not found");
            responseEntity = new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException io) {
                // Do nothing
            }
        }
        return responseEntity;
    }

    // /**
    // *
    // * @param listId
    // * @param userId
    // * @param QueryProperties
    // * @throws DAOException
    // */
    // private void insertNotResource(final List<String> listId, final String
    // userId, final String QueryProperties)
    // throws DAOException {
    // String INSERT_USERID_X = null;
    // for (int i = 0; i < listId.size(); i++) {
    // INSERT_USERID_X = QueryProperties;
    // dao.insertObjectNotResource(INSERT_USERID_X, userId, listId.get(i));
    // }
    // }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    @RequestMapping(value = "/updateUserProfile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateUserProfile(@RequestBody final RequestData request) {
        SimpleResponse response;
        try {
            // Convert birth day
            String dateUpdate = request.getRequest_user().getDob();
            if (!StringUtils.isEmpty(dateUpdate) && !dateUpdate.equals("Unknown")) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                Date date = formatter.parse(dateUpdate);
                dateUpdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            } else {
                dateUpdate = null;
            }

            // Set parameter of update profile
            Object[] queryParams = null;
            User user = request.getRequest_user();
            String role = user.getRole();
            String school = user.getSchool() != null && (user.getSchool().equals("0") || StringUtils.isEmpty(user.getSchool())) ? null : user
                .getSchool();
            String firstName = StringUtils.isEmpty(user.getFirstName()) ? null : user.getFirstName();
            String lastName = StringUtils.isEmpty(user.getLastName()) ? null : user.getLastName();
            if (!StringUtils.isEmpty(role)) {
                if (role.equals("M")) {
                    queryParams = new Object[] { firstName, lastName, request.getRequest_user().getEmail(), user
                        .getGender(), school, user.getAccomplishments(), dateUpdate, request.getRequest_user().getBio(), user
                        .getFavorite(), user.getDefaultSubjectId(), request.getRequest_user().getUserid() };
                } else if (role.equals("S")) {
                    queryParams = new Object[] { firstName, lastName, request.getRequest_user().getEmail(), user
                        .getGender(), school, null, dateUpdate, request.getRequest_user().getBio(), user.getFavorite(), user
                        .getDefaultSubjectId(), request.getRequest_user().getUserid() };
                }
            }

            // Update profile
            boolean status = dao.insertUpdateObject(SibConstants.SqlMapperBROT71.SQL_UPDATE_USER_PROFILE, queryParams);
            String msg;
            if (status) {
                msg = "Success";
                String activityStatus = String.format("You updated your %s", user.getActivity());
                activityLogService.insertActivityLog(new ActivityLogData(SibConstants.TYPE_PROFILE, "U", activityStatus, user
                    .getUserid(), null));
            } else {
                msg = "Failed";
            }
            response = new SimpleResponse(
                                          SibConstants.SUCCESS,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          msg);

        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(
                                          SibConstants.FAILURE,
                                          request.getRequest_data_type(),
                                          request.getRequest_data_method(),
                                          e.getMessage());
        }

        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @RequestMapping(value = "/getListMentor", method = RequestMethod.GET)
    public ResponseEntity<Response> getListMentor() {

        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_LIST_MENTOR, new Object[] {});
            response = new SimpleResponse(readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTokenUser(final String user) {
        String token = "";
        try {
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_TOKEN_BY_USERID, new Object[] { user });
            if (!CollectionUtils.isEmpty(readObject)) {
                token = ((Map<String, String>) readObject.get(0)).get(Parameters.TOKEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
        return token;
    }
}
