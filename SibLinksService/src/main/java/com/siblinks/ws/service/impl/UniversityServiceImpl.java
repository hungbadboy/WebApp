package com.siblinks.ws.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.ActivityLogService;
import com.siblinks.ws.service.UniversityService;
import com.siblinks.ws.util.SibConstants;

@RestController
@RequestMapping("/siblinks/services/univertisy")
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
    public ResponseEntity<Response> getAllUniversities(@RequestParam(value = "_search") final String search,
            @RequestParam(value = "nd") final String nd, @RequestParam(value = "rows") final int rows, @RequestParam(
                    value = "page") final int page, @RequestParam(value = "sidx") final String sidx,
            @RequestParam(value = "sord") final String sord) {

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
}
