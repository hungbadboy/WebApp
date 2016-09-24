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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.siblinks.ws.service.UserService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.RandomString;
import com.siblinks.ws.util.SibConstants;
import com.siblinks.ws.util.StringUtil;

/**
 * 
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

    @Override
    @RequestMapping(value = "/getUsers", method = RequestMethod.POST)
    public ResponseEntity<Response> getUsers(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        // try {
        // //String properties =
        // ReadProperties.getProperties("directoryReviewDefaultUploadEssay");
        // } catch (FileNotFoundException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        Object[] queryParams = { request.getRequest_data().getUsertype() };

        List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_USERID, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
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

        if (!AuthenticationFilter.isAuthed(context)) {
            SimpleResponse simpleResponse = new SimpleResponse("" + Boolean.FALSE, "Authentication required.");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            return entity;
        }

        // String whereClase = " WHERE userType=? ";
        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ALL_USERS, new Object[] {});

        SimpleResponse reponse = new SimpleResponse(readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getUserNotes", method = RequestMethod.POST)
    public ResponseEntity<Response> getUserNotes(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }
        Object[] queryParams = { request.getRequest_data().getUid() };
        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_NOTE_USER, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getStudentMentors", method = RequestMethod.POST)
    public ResponseEntity<Response> getStudentMentors(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }
        Object[] queryParams = { request.getRequest_data().getUid() };

        List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_STUDENT_MENTORS, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/collegesOrUniversities", method = RequestMethod.POST)
    public ResponseEntity<Response> collegesOrUniversities(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        // DaoFactory factory = DaoFactory.getDaoFactory();
        // ObjectDao dao = factory.getObjectDao();
        Object[] queryParams = {};

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_COL_UNIVERSITIES, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/majors", method = RequestMethod.POST)
    public ResponseEntity<Response> majors(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }
        Object[] queryParams = {};

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_MAJORS, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/extracurricularActivities", method = RequestMethod.POST)
    public ResponseEntity<Response> extracurricularActivities(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Object[] queryParams = {};

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_EXTRA_ACTIVITIES, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings("rawtypes")
    @Override
    @RequestMapping(value = "/signupcomplete", method = RequestMethod.POST)
    public ResponseEntity<Response> signupcomplete(@RequestBody final RequestData request) throws FileNotFoundException {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
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
            String userId = null;
            status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIGNUP_COMPLETE_USER, queryParams);

            if (status) {
                msgs1 = dao.readObjects(SibConstants.SqlMapper.SQL_GET_USERID, queryParams);
                if (msgs1 != null && msgs1.size() > 0) {
                    userId = ((Map) msgs1.get(0)).get(Parameters.USERID).toString();
                }
            }

            if (request.getRequest_data().getColmajor() != null) {
                List<String> myListMajorId = new ArrayList<String>(Arrays.asList(request
                    .getRequest_data()
                    .getColmajor()
                    .split(",")));
                insertNotResource(myListMajorId, userId, "INSERT_SIB_USER_MAJOR");
            }

            if (request.getRequest_data().getActivities() != null) {
                List<String> myListActivityId = new ArrayList<String>(Arrays.asList(request
                    .getRequest_data()
                    .getActivities()
                    .split(",")));
                insertNotResource(myListActivityId, userId, "INSERT_SIB_USER_ACTIVITY");
            }

            if (request.getRequest_data().getHelpin() != null) {
                List<String> myListHelpId = new ArrayList<String>(Arrays.asList(request.getRequest_data().getHelpin().split(",")));
                insertNotResource(myListHelpId, userId, "INSERT_SIB_USER_SUBJECT");
            }
        } else {
            readObject = new ArrayList<Object>();
            readObject.add("Email Address is Already Registered");
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + status,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping("/adminRegisterUser")
    public @ResponseBody
    ResponseEntity<Response> adminRegisterUser(@RequestParam(value = "username") final String username, @RequestParam(
            value = "password") final String password, @RequestParam(value = "firstname") final String firstname, @RequestParam(
            value = "lastname") final String lastname) {

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

        SimpleResponse reponse = new SimpleResponse("" + status, readObject.get(0).toString());
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public @ResponseBody
 ResponseEntity<Response> registerUser(@RequestBody final String jsonRegister) {
        JSONObject jsonObject = new JSONObject(jsonRegister);
        String username = jsonObject.getString(Parameters.USER_NAME);
        BCryptPasswordEncoder ecy = new BCryptPasswordEncoder(SibConstants.LENGHT_AUTHENTICATION);
        Object[] queryParams = { username, ecy.encode(jsonObject.getString(Parameters.PASSWORD)), environment
            .getProperty("directoryImageAvatar") };
        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_REGISTER_USER_EXIST, new Object[] { username });
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

        SimpleResponse reponse = new SimpleResponse("" + status, readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping("/isUsernameAvailable")
    public @ResponseBody
    ResponseEntity<Response> isUsernameAvailable(@RequestParam(value = "username") final String username) {
        Object[] queryParams = { username };
        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_IS_USERNAME_AVAIBALE, queryParams);
        boolean status = false;
        if (readObject.size() > 0) {
            status = true;
            readObject.add("User is  available");
        } else {
            readObject = new ArrayList<Object>();
            readObject.add("User is not available");
        }

        SimpleResponse reponse = new SimpleResponse("" + status, readObject);

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping("/adminloginUser")
    public @ResponseBody
    ResponseEntity<Response> adminloginUser(@RequestParam(value = "username") final String username, @RequestParam(
            value = "password") final String password) {

        Object[] queryParams = { username, password };
        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_ADMIN_LOGIN_USER, queryParams);

        boolean status = false;

        if (readObject.size() > 0) {
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

        SimpleResponse reponse = new SimpleResponse("" + status, readObject.get(0).toString());

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping("/loginUser")
    public @ResponseBody
    ResponseEntity<Response> loginUser(@RequestParam(value = "username") final String username,
            @RequestParam(value = "password") final String password) {
        Object[] queryParams = { username, password };

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_LOGIN_USER, queryParams);

        boolean status = Boolean.FALSE;

        if (readObject.size() > 0) {
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

        SimpleResponse reponse = new SimpleResponse("" + status, readObject);

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
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
    // msg.add("User Name  is not correct");
    // }
    //
    // SimpleResponse reponse = new SimpleResponse("" + status, msg);
    // ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
    // HttpStatus.OK);
    // return entity;
    // }

    @Override
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity<Response> changePassword(@RequestBody final RequestData request) {
        BCryptPasswordEncoder ecy = new BCryptPasswordEncoder(SibConstants.LENGHT_AUTHENTICATION);
        // check old password correct or not
        Object[] queryParams = { request.getRequest_user().getUsername() };
        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_LOGIN_USER, queryParams);
        SimpleResponse reponse = null;
        if (!CollectionUtils.isEmpty(readObject)) {
            // Verify password
            String rawPwd = ecy.encode(request.getRequest_user().getPassword());
            Map<String, String> user = (HashMap<String, String>) readObject.get(SibConstants.NUMBER.ZERO);
            String encryptPwd = user.get(Parameters.PASSWORD);
            if (encryptPwd != null && "".equals(encryptPwd)) {
                // Verify old password
                if (CommonUtil.verifyPassword(rawPwd, encryptPwd)) {
                    // Update new password
                    boolean status = dao.insertUpdateObject(
                        SibConstants.SqlMapper.SQL_SIB_RESET_PASSWORD,
                        new Object[] { ecy.encode(request.getRequest_user().getNewpassword()), request
                            .getRequest_user()
                            .getUsername() });
                    if (status) {
                        reponse = new SimpleResponse(
                                                     "" + Boolean.TRUE,
                                                     request.getRequest_data_type(),
                                                     request.getRequest_data_method(),
                                                     "Success");
                    } else {
                        // update db error
                        reponse = new SimpleResponse(
                                                     "" + Boolean.FALSE,
                                                     request.getRequest_data_type(),
                                                     request.getRequest_data_method(),
                                                     "Change password is failed. Please contace with administrator");
                    }
                } else {
                    // Don't match old password
                    reponse = new SimpleResponse(
                                                 "" + Boolean.FALSE,
                                                 request.getRequest_data_type(),
                                                 request.getRequest_data_method(),
                                                 "old password is not correct");
                }
            } else {
                // User register Google or FaceBook
                reponse = new SimpleResponse(
                                             "" + Boolean.FALSE,
                                             request.getRequest_data_type(),
                                             request.getRequest_data_method(),
                                             "Your user can not change password. User registered by Facebook or Google.");
            }
        } else {
            // User is not exist
            reponse = new SimpleResponse(
                                         "" + Boolean.FALSE,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         "User is not exist");
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/changePasswordForgot", method = RequestMethod.POST)
    public ResponseEntity<Response> changePasswordForgot(@RequestBody final String jsonData) {

        // read json data
        JSONObject jsonObject = new JSONObject(jsonData);
        String token = jsonObject.getString("token");
        String newPwd = jsonObject.getString("newPwd");

        BCryptPasswordEncoder ecy = new BCryptPasswordEncoder(SibConstants.LENGHT_AUTHENTICATION);
        // check old password correct or not
        SimpleResponse reponse = null;
        boolean status = dao.insertUpdateObject(
            SibConstants.SqlMapper.SQL_SIB_RESET_PASSWORD,
            new Object[] { ecy.encode(newPwd), token });
        if (status) {
            reponse = new SimpleResponse("" + Boolean.TRUE, "", "changePasswordForgot", "Success");
        } else {
            reponse = new SimpleResponse("" + Boolean.FALSE, "", "changePasswordForgot", "Failure");
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateUserProfileBasic", method = RequestMethod.POST)
    public ResponseEntity<Response> updateUserProfileBasic(@RequestBody final RequestData request) {
        // DaoFactory factory = DaoFactory.getDaoFactory();
        // ObjectDao dao = factory.getObjectDao();
        Object[] queryParams = {

        request.getRequest_data().getUsername(), request.getRequest_data().getFirstname(), request
            .getRequest_data()
            .getLastname(), request.getRequest_data().getBio() };

        List<Object> msg = null;
        msg = new ArrayList<Object>();
        boolean status = Boolean.TRUE;
        status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_UPDATE_USER_PROFILE_BASIC, queryParams);
        if (status) {
            msg.add("User Profile Basic has been updated");
        } else {
            msg.add("Failed to update profile");
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    msg);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateUserProfile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateUserProfile(@RequestBody final RequestData request) {
        // DaoFactory factory = DaoFactory.getDaoFactory();
        // ObjectDao dao = factory.getObjectDao();
        Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getUsername(), request
            .getRequest_data()
            .getCurrentclass(), request.getRequest_data().getAccomplishments(), request.getRequest_data().getMajorid(), request
            .getRequest_data()
            .getActivityid(), request.getRequest_data().getSubjectId() };

        String entityName = null;
        boolean delete = true;
        String userId = request.getRequest_data().getUid();

        if (request.getRequest_data().getMajorid().length() != 0) {
            List<String> myListMajorId = new ArrayList<String>(Arrays.asList(request.getRequest_data().getMajorid().split(",")));
            entityName = "DELETE_USER_MAJOR";
            delete = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_USER_MAJOR, queryParams);
            if (delete) {
                insertNotResource(myListMajorId, userId, SibConstants.SqlMapper.SQL_INSERT_SIB_USER_MAJOR);
            }
        }

        if (request.getRequest_data().getActivityid().length() != 0) {
            List<String> myListActivityId = new ArrayList<String>(Arrays.asList(request
                .getRequest_data()
                .getActivityid()
                .split(",")));
            delete = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_USER_ACTIVITY, queryParams);
            if (delete) {
                insertNotResource(myListActivityId, userId, SibConstants.SqlMapper.SQL_INSERT_SIB_USER_ACTIVITY);
            }
        }

        if (request.getRequest_data().getSubjectId().length() != 0) {
            List<String> myListHelpId = new ArrayList<String>(Arrays.asList(request.getRequest_data().getSubjectId().split(",")));
            delete = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_USER_SUBJECT, queryParams);
            if (delete) {
                insertNotResource(myListHelpId, userId, SibConstants.SqlMapper.SQL_INSERT_SIB_USER_SUBJECT);
            }
        }

        List<Object> msg = null;
        msg = new ArrayList<Object>();
        boolean status = Boolean.TRUE;
        status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_UPDATE_USER_PROFILE, queryParams);
        if (status) {
            msg.add("User Profile has been updated");
        } else {
            msg.add("Failed to update profile");
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    msg);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping("/updateLastOnlineTime")
    public @ResponseBody
    ResponseEntity<Response> updateLastOnlineTime(@RequestParam(value = "username") final String username) {

        Object[] queryParams = { username };

        boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATELASTONLINETIME, queryParams);
        SimpleResponse reponse = new SimpleResponse("" + status, "");
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping("/saveDefaultSubject")
    public @ResponseBody
    ResponseEntity<Response> saveDefaultSubject(@RequestParam(value = "uid") final String uid,
            @RequestParam(value = "sid") final String sid) {
        // DaoFactory factory = DaoFactory.getDaoFactory();
        // ObjectDao dao = factory.getObjectDao();
        Object[] queryParams = { uid, sid };

        String entityName = "SAVE_DEFAULT_SUBJECT";
        boolean status = dao.insertUpdateObject(entityName, queryParams);
        SimpleResponse reponse = new SimpleResponse("" + status, "");
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping("/addUserNotes")
    public ResponseEntity<Response> addUserNotes(@RequestBody final RequestData request) {
        Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getMentorid(), request
            .getRequest_data()
            .getNote() };

        boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_INSERT_USER_NOTE, queryParams);
        SimpleResponse reponse = new SimpleResponse("" + status, "");
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping("/findUser")
    public @ResponseBody
    ResponseEntity<Response> findUser(@RequestParam(value = "name") final String name) {
        Object[] queryParams = { name };
        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_FIND_USER, queryParams);

        boolean status = true;

        if (readObject == null || readObject.size() == 0) {
            status = false;
            readObject = new ArrayList<Object>();
            readObject.add("User  is empty");
        }

        SimpleResponse reponse = new SimpleResponse("" + status, readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getUserProfile", method = RequestMethod.GET)
    public ResponseEntity<Response> getUserProfile(@RequestParam final long userid) {

        Map<String, Object> result = null;
        SimpleResponse reponse = null;

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
                // Map<String, Object> mymap = new HashMap<String, Object>();
                // mymap.put(Parameters.MAJORS, readObject1);
                // mymap.put(Parameters.ACTIVITY, readObject2);
                // mymap.put(Parameters.HELP, readObject3);
                // resChildObject.add(map1);
                // resChildObject.add(mymap);
                // resParentObject.add(resChildObject);
            }
            // Get information relate
            result.putAll(getRelateUserProfile(result.get(Parameters.USER_TYPE).toString(), queryParams));
            reponse = new SimpleResponse("" + true, result);
        } else {
            reponse = new SimpleResponse("" + Boolean.FALSE, "User is not exists");
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
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
     */
    private Map<String, Object> getRelateUserProfile(final String userType, final Object[] queryParams) {
        Map<String, Object> result = null;
        JsonParser jsonParser = new JsonParser();
        List<Object> readObject = null;
        // Check Role
        if (SibConstants.ROLE_TYPE.S.toString().equals(userType)) {
            result = new HashMap<String, Object>();
            // Count Question
            readObject = dao.readObjects(SibConstants.SqlMapperBROT71.SQL_GET_COUNT_QUESTION, queryParams);
            result.put("count_question", 0);
            for (Object object : readObject) {
                result.put("count_question", jsonParser.parse(object.toString()).getAsJsonObject().get("count(*)").toString());
            }

            // Count Subscribe
            readObject = dao.readObjects(SibConstants.SqlMapperBROT71.SQL_GET_COUNT_SUBSCIBE, queryParams);
            result.put("count_subscribe", 0);
            for (Object object : readObject) {
                result.put("count_subscribe", jsonParser.parse(object.toString()).getAsJsonObject().get("count(*)").toString());
            }
            // Count Essasy
            readObject = dao.readObjects(SibConstants.SqlMapperBROT71.SQL_GET_COUNT_ESSAY, queryParams);
            result.put("count_essay", 0);
            for (Object object : readObject) {
                result.put("count_essay", jsonParser.parse(object.toString()).getAsJsonObject().get("count(*)").toString());
            }
            // UserType = M
        } else if (SibConstants.ROLE_TYPE.M.toString().equals(userType)) {
            result = new HashMap<String, Object>();
            // Count subscribe
            readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_COUNT_SUBSCIBERS, queryParams);
            result.put("count_subscribers", 0);
            for (Object object : readObject) {
                result.put("count_subscribers", jsonParser.parse(object.toString()).getAsJsonObject().get("count(*)").toString());
            }

            // Count answer
            readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_COUNT_ANSWERS, queryParams);
            result.put("count_answers", 0);
            for (Object object : readObject) {
                result.put("count_answers", jsonParser.parse(object.toString()).getAsJsonObject().get("count(*)").toString());
            }

            // Count videos
            readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_COUNT_VIDEOS, queryParams);
            result.put("count_videos", 0);
            for (Object object : readObject) {
                result.put("count_videos", jsonParser.parse(object.toString()).getAsJsonObject().get("count(*)").toString());
            }

            // Count Like
            readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_COUNT_LIKES, queryParams);
            result.put("count_likes", 0);
            for (Object object : readObject) {
                result.put("count_likes", jsonParser.parse(object.toString()).getAsJsonObject().get("count(*)").toString());
            }

            // List skill
            readObject = dao.readObjects(SibConstants.SqlMapperBROT70.SQL_GET_MENTOR_SKILLS, queryParams);
            String skills = "";
            for (Object object : readObject) {
                if (skills.length() == 0) {
                    skills += jsonParser.parse(object.toString()).getAsJsonObject().get("subject").toString();
                } else {
                    skills += ", " + jsonParser.parse(object.toString()).getAsJsonObject().get("subject").toString();
                }
            }
            result.put("skills", skills);
        } else {
            // do nothing
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/getAccomplishment", method = RequestMethod.POST)
    public ResponseEntity<Response> getAccomplishment(@RequestBody final RequestData request) {

        String entityName = null;

        Object[] queryParams = { request.getRequest_data().getUid() };

        entityName = "ACCOMPLISHMENT_READ";

        List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_ACCOMPLISHMENT_READ, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/listOfMajors", method = RequestMethod.POST)
    public ResponseEntity<Response> listOfMajors(@RequestBody final RequestData request) {

        // DaoFactory factory = DaoFactory.getDaoFactory();
        // ObjectDao dao = factory.getObjectDao();
        Object[] queryParams = {};

        List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_MAJOR_READ, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/listOfActivity", method = RequestMethod.POST)
    public ResponseEntity<Response> listOfActivity(@RequestBody final RequestData request) {

        Object[] queryParams = {};

        List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_ACTIVITY_READ, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/listCategory", method = RequestMethod.POST)
    public ResponseEntity<Response> listCategory(@RequestBody final RequestData request) {

        Object[] queryParams = {};

        List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_HELP_READ, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
    public ResponseEntity<Response> forgotPassword(@RequestBody final RequestData request) {
        Object[] queryParams = { request.getRequest_data().getEmail(), request.getRequest_data().getPassword() };

        List<Object> msg = new ArrayList<Object>();
        boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_PASSWORD, queryParams);
        if (status) {
            dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_TOKEN_FORGOT_PASSWORD, queryParams);
            msg.add("Password has been updated");
        } else {
            msg.add("Failed to update password");
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    msg);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/confirmToken", method = RequestMethod.POST)
    public ResponseEntity<Response> confirmToken(@RequestBody final String jsontoken) {
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
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timeStamp = formatter.format(Calendar.getInstance().getTime());
                Date dateCurrent = formatter.parse(timeStamp);
                Date dateForgot = formatter.parse(timeForgot);
                noDay = (dateCurrent.getTime() - dateForgot.getTime()) / (24 * 3600 * 1000);
            } catch (ParseException e) {
                logger.error(e);
            }

            if (noDay > SibConstants.NUMBER.ONE) {
                dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_TOKEN_FORGOT_PASSWORD, queryParams);
            } else {
                readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_TOKEN_FORGOT_PASSWORD, queryParams);
            }
        }
        SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE, "", "", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getAddressPage", method = RequestMethod.POST)
    public ResponseEntity<Response> getAddressPage(@RequestBody final RequestData request) {

        // DaoFactory factory = DaoFactory.getDaoFactory();
        // ObjectDao dao = factory.getObjectDao();

        List<Object> readObject = null;
        readObject = dao.readObjectsNotResource(SibConstants.SqlMapper.SQL_GET_ADDRESS_WEB);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getPolicy", method = RequestMethod.POST)
    public ResponseEntity<Response> getPolicy(@RequestBody final RequestData request) {

        List<Object> readObject = null;
        readObject = dao.readObjectsNotResource(SibConstants.SqlMapper.SQL_GET_POLICY);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getTerms", method = RequestMethod.POST)
    public ResponseEntity<Response> getTerms(@RequestBody final RequestData request) {

        List<Object> readObject = null;
        readObject = dao.readObjectsNotResource(SibConstants.SqlMapper.SQL_GET_TERMS);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateAccomplishmentMobile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateAccomplishmentMobile(@RequestBody final RequestData request) {

        // DaoFactory factory = DaoFactory.getDaoFactory();
        // ObjectDao dao = factory.getObjectDao();
        Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getAccomplishments() };

        boolean status = Boolean.TRUE;
        status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_ACCOMPLISHMENT_MOBILE, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    status);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateMajorsMobile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateMajorsMobile(@RequestBody final RequestData request) {
        // DaoFactory factory = DaoFactory.getDaoFactory();
        // ObjectDao dao = factory.getObjectDao();
        Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getMajorid() };

        List<String> myListMajorId = new ArrayList<String>(Arrays.asList(request.getRequest_data().getMajorid().split(",")));

        boolean status = Boolean.TRUE;
        List<Object> msg = null;
        msg = new ArrayList<Object>();
        String userId = request.getRequest_data().getUid();

        status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_USER_MAJOR, queryParams);

        insertNotResource(myListMajorId, userId, SibConstants.SqlMapper.SQL_INSERT_SIB_USER_MAJOR);

        if (status) {
            msg.add("Major has been updated");
        } else {
            msg.add("Failed to update major");
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    msg);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateActivitiesMobile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateActivitiesMobile(@RequestBody final RequestData request) {
        Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getActivityid() };

        List<String> myListActivityId = new ArrayList<String>(Arrays.asList(request.getRequest_data().getActivityid().split(",")));

        boolean status = true;
        List<Object> msg = null;
        msg = new ArrayList<Object>();
        String userId = request.getRequest_data().getUid();

        status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_USER_ACTIVITY, queryParams);

        insertNotResource(myListActivityId, userId, SibConstants.SqlMapper.SQL_INSERT_SIB_USER_ACTIVITY);

        if (status) {
            msg.add("Activities has been updated");
        } else {
            msg.add("Failed to update activities");
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    msg);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateHelpInMobile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateHelpInMobile(@RequestBody final RequestData request) {
        // DaoFactory factory = DaoFactory.getDaoFactory();
        // ObjectDao dao = factory.getObjectDao();
        Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getTopicId() };

        List<String> myListHelpId = new ArrayList<String>(Arrays.asList(request.getRequest_data().getTopicId().split(",")));

        boolean status = Boolean.TRUE;
        List<Object> msg = null;
        msg = new ArrayList<Object>();
        String userId = request.getRequest_data().getUid();

        status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_USER_TOPIC, queryParams);

        insertNotResource(myListHelpId, userId, SibConstants.SqlMapper.SQL_INSERT_SIB_USER_TOPIC);

        if (status) {
            msg.add("HelpIn has been updated");
        } else {
            msg.add("Failed to update HelpIn");
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    msg);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateGradeMobile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateGradeMobile(@RequestBody final RequestData request) {
        // DaoFactory factory = DaoFactory.getDaoFactory();
        // ObjectDao dao = factory.getObjectDao();
        Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getCurrentclass() };

        List<Object> msg = null;
        msg = new ArrayList<Object>();
        boolean status = Boolean.TRUE;
        status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_GRADE_MOBILE, queryParams);
        if (status) {
            msg.add("Grade has been updated");
        } else {
            msg.add("Failed to update grade");
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    msg);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateSchoolMobile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateSchoolMobile(@RequestBody final RequestData request) {
        Object[] queryParams = { request.getRequest_data().getUid(), request.getRequest_data().getEducation() };

        List<Object> msg = null;
        msg = new ArrayList<Object>();
        boolean status = Boolean.TRUE;
        status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_SCHOOL_MOBILE, queryParams);
        if (status) {
            msg.add("School has been updated");
        } else {
            msg.add("Failed to update school");
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    msg);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/loginFacebook", method = RequestMethod.POST)
    public ResponseEntity<Response> loginFacebook(@RequestBody final RequestData request) throws FileNotFoundException {

        if (!AuthenticationFilter.isAuthed(context)) {
            SimpleResponse simpleResponse = new SimpleResponse("" + Boolean.FALSE, "Authentication required.");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            return entity;
        }

        Object[] queryParams = { request.getRequest_data().getFirstname(), request.getRequest_data().getLastname(), request
            .getRequest_data()
            .getImage(), request.getRequest_data().getFacebookid() };

        boolean status = false;

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_CHECK_USER, new Object[] { request
            .getRequest_data()
            .getUsername() });
        SimpleResponse reponse = null;
        if (CollectionUtils.isEmpty(readObject)) {
            Object[] queryParamsFB = { request.getRequest_data().getUsername(), request.getRequest_data().getUsertype(), request
                .getRequest_data()
                .getFirstname(), request.getRequest_data().getLastname(), request.getRequest_data().getImage(), request
                .getRequest_data()
                .getFacebookid() };
            status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_CREATE_USER_FACEBOOK, queryParamsFB);
            if (status) {
                readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_USER_BY_USERNAME, new Object[] { request
                    .getRequest_data()
                    .getUsername() });
            }

            reponse = new SimpleResponse(
                                         "" + status,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         readObject);

        } else {
            Map<String, String> mapUser = (HashMap<String, String>) readObject.get(SibConstants.NUMBER.ZERO);
            // Check Facebook id for update
            if (mapUser.get("idFacebook") != null && mapUser.get("idFacebook").equals(request.getRequest_data().getFacebookid())) {// Registered
                status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_INFO_FACEBOOK, queryParams);
                reponse = new SimpleResponse(
                                             "" + status,
                                             request.getRequest_data_type(),
                                             request.getRequest_data_method(),
                                             readObject);
            } else {

                reponse = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), "");
            }
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/loginGoogle", method = RequestMethod.POST)
    public ResponseEntity<Response> loginGoogle(@RequestBody final RequestData request) throws FileNotFoundException {

        if (!AuthenticationFilter.isAuthed(context)) {
            SimpleResponse simpleResponse = new SimpleResponse("" + Boolean.FALSE, "Authentication required.");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            return entity;
        }

        boolean status = Boolean.FALSE;

        List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_CHECK_USER, new Object[] { request
            .getRequest_data()
            .getUsername() });
        SimpleResponse reponse = null;
        if (CollectionUtils.isEmpty(readObject)) {
            Object[] queryParamsGG = { request.getRequest_data().getUsername(), request.getRequest_data().getUsertype(), request
                .getRequest_data()
                .getFirstname(), request.getRequest_data().getLastname(), request.getRequest_data().getImage(), request
                .getRequest_data()
                .getGoogleid() };
            status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_CREATE_USER_GOOGLE, queryParamsGG);
            if (status) {
                readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_USER_BY_USERNAME, new Object[] { request
                    .getRequest_data()
                    .getUsername() });
            }
            reponse = new SimpleResponse(
                                         "" + status,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         readObject);
        } else {
            Map<String, String> mapUser = (HashMap<String, String>) readObject.get(SibConstants.NUMBER.ZERO);
         // Check google id for update
            if (mapUser.get("idGoogle") != null && mapUser.get("idGoogle").equals(request.getRequest_data().getGoogleid())) {// Registered
                status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_INFO_GOOGLE, new Object[] { request
                .getRequest_data()
                .getFirstname(), request.getRequest_data().getLastname(), request.getRequest_data().getImage(), request
                .getRequest_data()
                .getGoogleid() });
                reponse = new SimpleResponse(
                                             "" + status,
                                             request.getRequest_data_type(),
                                             request.getRequest_data_method(),
                                             readObject);
            } else {
                reponse = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), "");
            }
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> uploadFile(@RequestParam("uploadfile") final MultipartFile uploadfile, @RequestParam(
            value = "userid") final String userid) throws IOException {

        // if (!AuthenticationFilter.isAuthed(context)) {
        // ResponseEntity<Response> entity = new ResponseEntity<Response>(
        // new SimpleResponse(
        // "" + Boolean.FALSE,
        // "Authentication required."),
        // HttpStatus.FORBIDDEN);
        // return entity;
        // }
        String filename = "";
        String name;
        String filepath = "";
        String directory = environment.getProperty("directoryAvatar");
        String service = environment.getProperty("directoryGetAvatar");
        String sample = environment.getProperty("file.upload.image.type");
        name = uploadfile.getOriginalFilename();
        String nameExt = FilenameUtils.getExtension(name);
        name.toLowerCase();
        boolean status = sample.contains(nameExt.toLowerCase());
        if (directory != null && status) {
            try {
                RandomString randomName = new RandomString();
                filename = randomName.random();

                filepath = Paths.get(directory, filename + "." + "png").toString();
                // Save the file locally
                File file = new File(filepath);
                File parentDir = file.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
                stream.write(uploadfile.getBytes());
                stream.close();

				Object[] queryParams = { service + filename + ".png", userid };
                dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_AVARTAR_USER, queryParams);
            }

            catch (Exception e) {
                e.printStackTrace();
            }
            SimpleResponse reponse = new SimpleResponse("" + status, service + filename + ".png");
            return new ResponseEntity<Response>(reponse, HttpStatus.OK);

        } else {
            return new ResponseEntity<Response>(HttpStatus.NO_CONTENT);
        }
    }

    @SuppressWarnings("resource")
    @Override
    @RequestMapping(value = "/getAvatar/{path}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getAvatar(@PathVariable(value = "path") final String path) {

		logger.info("Call service get avatar");
		// //DaoFactory factory = DaoFactory.getDaoFactory();
		// // ObjectDao dao = factory.getObjectDao();

		if (StringUtil.isNull(path)) {
			RandomAccessFile t = null;
			byte[] r = null;
			try {
				t = new RandomAccessFile(path, "r");
				r = new byte[(int) t.length()];
				t.readFully(r);
			} catch (FileNotFoundException e) {
				logger.debug("File not found");
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

    public List<Object> getElementOfUser(final Map<String, Object> map, final String entityName) {

        String userid = (String) map.get(Parameters.USERID);
        Object[] queryParams = { userid };
        List<Object> readObject = null;

        readObject = dao.readObjects(entityName, queryParams);

        Map<String, Object> major = null;
        try {
            if (readObject != null) {
                for (Object obj : readObject) {
                    major = (Map) obj;
                    Iterator<Entry<String, Object>> it = major.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pairs = it.next();
                        if (pairs.getKey().equals(Parameters.USERID)) {
                            it.remove();
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return readObject;
    }

    public void insertNotResource(final List<String> listId, final String userId, final String QueryProperties) {
        // DaoFactory factory = DaoFactory.getDaoFactory();
        // ObjectDao dao = factory.getObjectDao();
        String INSERT_USERID_X = null;
        boolean statusUserX = true;

        for (int i = 0; i < listId.size(); i++) {
            INSERT_USERID_X = QueryProperties;
            statusUserX = dao.insertObjectNotResource(INSERT_USERID_X, userId, listId.get(i));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.siblinks.ws.service.UserService#updateUserInfodmation(com.siblinks
     * .ws.model.RequestData)
     */
    @Override
    @RequestMapping(value = "/updateStudentProfile", method = RequestMethod.POST)
    public ResponseEntity<Response> updateStudentProfile(@RequestBody final RequestData request) {
        Object[] queryParams = { request.getRequest_user().getFirstName(), request.getRequest_user().getLastName(), request
            .getRequest_user()
            .getEmail(), request.getRequest_user().getGender(), request.getRequest_user().getSchool(), request
            .getRequest_user()
            .getBio(), request.getRequest_user().getDescription(), request.getRequest_user().getFavorite(), request
            .getRequest_user()
            .getUserid() };

        String msg;
        boolean status = Boolean.TRUE;
        status = dao.insertUpdateObject(SibConstants.SqlMapperBROT71.SQL_UPDATE_STUDENT_PROFILE, queryParams);
        if (status) {
            msg = "Success";
        } else {
            msg = "Failed";
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    msg);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }
    
    @Override
    @RequestMapping(value = "/getListMentor", method = RequestMethod.GET)
    public ResponseEntity<Response> getListMentor() {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(new SimpleResponse(
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_LIST_MENTOR, new Object[]{});

        SimpleResponse reponse = new SimpleResponse(readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }
}
