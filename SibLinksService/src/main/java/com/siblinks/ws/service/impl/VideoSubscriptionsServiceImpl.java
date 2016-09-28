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

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.VideoSubscriptionsService;
import com.siblinks.ws.util.DateUtil;
import com.siblinks.ws.util.SibConstants;

/**
 * @author kypv
 * @version 1.0
 */

@RestController
@RequestMapping("/siblinks/services/video")
public class VideoSubscriptionsServiceImpl implements VideoSubscriptionsService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ObjectDao dao;

    @Override
    @RequestMapping(value = "/getListCategorySubscription", method = RequestMethod.GET)
    public ResponseEntity<Response> getListCategorySubscription() {
        String method = "getListCategorySubscription()";

        logger.debug(method + " start");

        Object[] queryParams = {};
        String entityName = SibConstants.SqlMapper.SQL_SIB_GET_LIST_CATEGORY_SUBSCRIPTION;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("true", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);

        logger.debug(method + " end");
        return entity;
    }

    @Override
    @RequestMapping(value = "/getListVideoSubscription", method = RequestMethod.GET)
    public ResponseEntity<Response> getListVideoSubscription(@RequestParam("userId") final String userId,
            @RequestParam("subjectId") final String subjectId) {
        String method = "getListVideoSubscription()";
        logger.debug(method + " start");

        String entityName = null;
        List<Object> readObject = null;
        String currentDate = "";
        String firstDayOfCurrentWeek = "";
        Object[] queryParams = null;
        Map<String, List<Object>> mapListVideo = new HashMap<String, List<Object>>();

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            Date firstDayOfTheWeek = cal.getTime();

            currentDate = DateUtil.date2YYYYMMDD000000(new Date());
            firstDayOfCurrentWeek = DateUtil.date2YYYYMMDD000000(firstDayOfTheWeek);

            if ("-2".equals(subjectId)) {
                entityName = SibConstants.SqlMapper.SQL_SIB_GET_ALL_VIDEO_SUBSCRIPTION;
                queryParams = new Object[] { userId, currentDate, userId, firstDayOfCurrentWeek, currentDate, userId };
            } else {
                entityName = SibConstants.SqlMapper.SQL_SIB_GET_ALL_VIDEO_SUBSCRIPTION_BY_CATEGORY;
                queryParams = new Object[] { userId, currentDate, subjectId, userId, firstDayOfCurrentWeek, currentDate, subjectId, userId, subjectId };
            }

            readObject = dao.readObjects(entityName, queryParams);

            if (readObject == null) {
                readObject = new ArrayList<Object>();
            }

            JSONArray jsonAraay = new JSONArray(readObject);

            for (int i = 0; i < jsonAraay.length(); i++) {
                JSONObject jsonObj = jsonAraay.getJSONObject(i);
                ObjectMapper mapper = new ObjectMapper();
                Object obj = mapper.readValue(jsonObj.toString(), Object.class);      
                addMapVideo(mapListVideo, jsonObj.get("flag").toString(), obj);
            }

        } catch (ParseException | IOException e) {
            logger.error(method + " - error : " + e.getMessage());
        }

        SimpleResponse reponse = new SimpleResponse("true", mapListVideo);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);

        logger.debug(method + " end");
        return entity;
    }

    private static void addMapVideo(final Map<String, List<Object>> map, final String key, final Object value)
            throws ParseException {
        if (map.containsKey(key)) {
            List<Object> lst = map.get(key);
            lst.add(value);
        } else {
            List<Object> lst = new ArrayList<Object>();
            lst.add(value);
            map.put(key, lst);
        }
    }

    @Override
    @RequestMapping(value = "/getListVideoOfRecent", method = RequestMethod.POST)
    public ResponseEntity<Response> getListVideoOfRecent(@RequestParam("subjectId") final String subjectId) {
        String method = "getListVideoOfRecent()";

        logger.debug(method + " start");

        String entityName = null;
        List<Object> readObject = null;

        try {
            String currentDate = DateUtil.date2YYYYMMDD000000(new Date());
            System.out.println(currentDate);

            entityName = SibConstants.SqlMapper.SQL_SIB_GET_LIST_VIDEO_OF_RECENT;

            Object[] queryParams = { subjectId, currentDate };

            readObject = dao.readObjects(entityName, queryParams);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleResponse reponse = new SimpleResponse("true", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);

        logger.debug(method + " end");
        return entity;
    }

    @Override
    @RequestMapping(value = "/getListVideoOfWeek", method = RequestMethod.POST)
    public ResponseEntity<Response> getListVideoOfWeek(@RequestParam("subjectId") final String subjectId) {
        String method = "getListVideoOfWeek()";

        logger.debug(method + " start");

        String entityName = null;
        List<Object> readObject = null;
        String currentDate = "";
        String firstDayOfCurrentWeek = "";

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            Date firstDayOfTheWeek = cal.getTime();

            currentDate = DateUtil.date2YYYYMMDD000000(new Date());
            firstDayOfCurrentWeek = DateUtil.date2YYYYMMDD000000(firstDayOfTheWeek);

            System.out.println(firstDayOfCurrentWeek);

            entityName = SibConstants.SqlMapper.SQL_SIB_GET_LIST_VIDEO_OF_WEEK;

            Object[] queryParams = { subjectId, firstDayOfCurrentWeek, currentDate };

            readObject = dao.readObjects(entityName, queryParams);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleResponse reponse = new SimpleResponse("true", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);

        logger.debug(method + " end");
        return entity;
    }

    @Override
    @RequestMapping(value = "/getListVideoOlder", method = RequestMethod.POST)
    public ResponseEntity<Response> getListVideoOlder(@RequestParam("userId") final String userId) {
        String method = "getListVideoOlder()";

        logger.debug(method + " start");

        Object[] queryParams = { userId };
        String entityName = SibConstants.SqlMapper.SQL_SIB_GET_LIST_VIDEO_OLDER;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("true", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);

        logger.debug(method + " end");
        return entity;
    }

}
