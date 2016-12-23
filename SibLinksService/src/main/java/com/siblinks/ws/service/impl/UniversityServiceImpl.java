package com.siblinks.ws.service.impl;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.SibUniversity;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.ActivityLogService;
import com.siblinks.ws.service.UniversityService;
import com.siblinks.ws.util.SibConstants;

@RestController
@RequestMapping("/siblinks/services/university")
public class UniversityServiceImpl implements UniversityService {

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
    @RequestMapping(value = "/getAllUniversities", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllUniversities(@RequestParam(value = "_search") final String search, @RequestParam(
            value = "nd") final String nd, @RequestParam(value = "rows") final int rows,
            @RequestParam(value = "page") final int page, @RequestParam(value = "sidx") final String sidx, @RequestParam(
                    value = "sord") final String sord) {

        SimpleResponse response = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                response = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
            }

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ALL_UNIVERSITIES, new Object[] {});

            // Return for rows
            response = new SimpleResponse(readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "universities", "getAllUniversities", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);

    }

    /**
     * {@inherit}
     */
    @Override
    @RequestMapping(value = "/registerSchool", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Response> registerSchool(@RequestBody final SibUniversity data) {
        SimpleResponse response = null;
        try {
            List<Object> schoolResponse = dao.readObjects(
                SibConstants.SqlMapper.SQL_REGISTER_EXIST_SCHOOL,
                new Object[] { data.getName() });
            String message = "";
            boolean status = false;
            if (CollectionUtils.isEmpty(schoolResponse) || schoolResponse == null) {
                if (data.getName() != "") {
                    status = dao
                        .insertUpdateObject(
                            SibConstants.SqlMapper.SQL_ADD_ANOTHER_SCHOOL,
                            new Object[] { data.getName(), data.getType(), data.getAddress1(), data.getAddress2(), data.getCity(), data
                                .getState(), data.getZip() });
                } else {
                    status = false;
                    message = "Name can't blank";
                }

            } else {
                status = false;
                message = "Name is already registered";
            }
            response = new SimpleResponse("" + status, "school", "registerSchool", message);

        } catch (DAOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/getSchoolInfo", method = RequestMethod.GET)
    public ResponseEntity<Response> getSchoolInfo(@RequestParam final long schoolid) {
        SimpleResponse response = null;
        try {
            Map<String, Object> result = null;
            Object[] queryParams = { schoolid };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_SCHOOL_INFO, queryParams);
            if (!CollectionUtils.isEmpty(readObject)) {
                for (Object object : readObject) {
                    result = (Map) object;
                }
                response = new SimpleResponse(SibConstants.SUCCESS, "school", "getSchoolInfo", result);
            } else {
                response = new SimpleResponse(SibConstants.FAILURE, SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "school", "getSchoolInfo", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/updateSchoolInfo", method = RequestMethod.POST)
    public ResponseEntity<Response> updateSchoolInfo(@RequestBody final SibUniversity data) {
        SimpleResponse response = null;
        try {
            List<Object> schoolResponse = dao.readObjects(
                SibConstants.SqlMapper.SQL_CHECK_SCHOOL_EXISTS_BY_ID,
                new Object[] { data.getSchoolId(), data.getName() });
            String message = "";
            boolean status = false;
            if (CollectionUtils.isEmpty(schoolResponse) || schoolResponse == null) {
                if (data.getName()!=""){
                    status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_SCHOOL_INFO, new Object[] { data.getName(), data.getType(), data.getAddress1(), data.getAddress2(), data.getCity(), data
                        .getState(), data.getZip(), data.getSchoolId() });
                    message = "Updated Profile Successful";
                } else {
                    status = false;
                    message = "Name can't blank";
                }
            } else {
                status = false;
                message = "Name is already registered";
            }
            response = new SimpleResponse("" + status, "school", "updateSchoolInfo", message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "school", "updateSchoolInfo", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/deleteSChool", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteSChool(@RequestBody final SibUniversity data) {
        SimpleResponse response = null;
        try {
            Object[] params = { data.getSchoolId() };
            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_SCHOOL, params);
            response = new SimpleResponse(SibConstants.SUCCESS, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/getStates")
    @ResponseBody
    public ResponseEntity<Response> getStates() {
        SimpleResponse response = null;
        try {
            String entityName = SibConstants.SqlMapper.SQL_GET_STATES;
            List<Object> states = dao.readObjects(entityName, new Object[] {});
            response = new SimpleResponse(SibConstants.SUCCESS, "states", "getStates", states);
        } catch (DAOException e) {
            logger.debug(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "UniversityServiceImpl", "getStates", e.getMessage());
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/getCities")
    @ResponseBody
    public ResponseEntity<Response> getCities(@RequestParam final String state_name) {
        SimpleResponse response = null;
        try {
            Map<String, Object> result = null;
            Object[] queryParams = { state_name };
            List<Object> cities = dao.readObjects(SibConstants.SqlMapper.SQL_GET_CITIES, queryParams);
            response = new SimpleResponse(SibConstants.SUCCESS, "cities", "getCities", cities);
        } catch (DAOException e) {
            logger.debug(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "UniversityServiceImpl", "getCities", e.getMessage());
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(response, HttpStatus.OK);
        return entity;
    }
}
