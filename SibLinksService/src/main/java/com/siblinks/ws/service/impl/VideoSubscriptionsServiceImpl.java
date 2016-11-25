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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.VideoSubscriptionsService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.DateUtil;
import com.siblinks.ws.util.SibConstants;
import com.siblinks.ws.util.StringUtil;

/**
 * {@link VideoSubscriptionsService}
 * 
 * @author kypv
 * @version 1.0
 */

@RestController
@RequestMapping("/siblinks/services/video")
public class VideoSubscriptionsServiceImpl implements VideoSubscriptionsService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ObjectDao dao;

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getListCategorySubscription", method = RequestMethod.GET)
    public ResponseEntity<Response> getListCategorySubscription() {
        SimpleResponse response = null;
        try {
            String method = "getListCategorySubscription()";
            logger.debug(method + " start");
            Object[] queryParams = {};
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_GET_LIST_CATEGORY_SUBSCRIPTION, queryParams);
            response = new SimpleResponse(SibConstants.SUCCESS, readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "video", "getListCategorySubscription", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getListVideoSubscription", method = RequestMethod.GET)
    public ResponseEntity<Response> getListVideoSubscription(@RequestParam("userId") final String userId,
            @RequestParam("subjectId") final String subjectId) {
        SimpleResponse response = null;
        try {
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
                    queryParams = new Object[] { userId, currentDate, userId, firstDayOfCurrentWeek, currentDate, userId, firstDayOfCurrentWeek };
                    readObject = dao.readObjects(entityName, queryParams);
                } else {
                    // Get child category by subjectId
                    List<Map<String, Object>> readObjectNoCondition = dao
                        .readObjectNoCondition(SibConstants.SqlMapper.SQL_GET_ALL_CATEGORY_TOPIC);
                    
                    MapSqlParameterSource params = new MapSqlParameterSource();
                    
                    String allChildCategory = CommonUtil.getAllChildCategory(subjectId, readObjectNoCondition);
                    if (!StringUtil.isNull(allChildCategory)) {
                        List<Integer> listChildCategory = new ArrayList<Integer>();
                        String[] arrChildCategory = allChildCategory.split(",");
                        for (String string : arrChildCategory) {
                            listChildCategory.add(Integer.parseInt(string));
                        }
                        params.addValue("subjectID", listChildCategory);
                            
                    }
                    params.addValue("userID", userId);
                    params.addValue("currentDate", currentDate);
                    params.addValue("firstDayOfCurrentWeek", firstDayOfCurrentWeek);

                    entityName = SibConstants.SqlMapper.SQL_SIB_GET_ALL_VIDEO_SUBSCRIPTION_BY_CATEGORY;

                    readObject = dao.readObjectNamedParameter(entityName, params);
                }


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

            response = new SimpleResponse("true", mapListVideo);
        } catch (Exception e) {
            e.printStackTrace();
            response = new SimpleResponse(SibConstants.FAILURE, "video", "getListVideoSubscription", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * 
     * @param map
     * @param key
     * @param value
     * @throws ParseException
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getListVideoOfRecent", method = RequestMethod.POST)
    public ResponseEntity<Response> getListVideoOfRecent(@RequestParam("subjectId") final String subjectId) {
        SimpleResponse response = null;
        try {
            String method = "getListVideoOfRecent()";
            logger.debug(method + " start");
            String currentDate = DateUtil.date2YYYYMMDD000000(new Date());
            Object[] queryParams = { subjectId, currentDate };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_GET_LIST_VIDEO_OF_RECENT, queryParams);
            response = new SimpleResponse("true", readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "videoAdmission", "getVideoTuttorialAdmission", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getListVideoOfWeek", method = RequestMethod.POST)
    public ResponseEntity<Response> getListVideoOfWeek(@RequestParam("subjectId") final String subjectId) {
        SimpleResponse response = null;
        try {
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

            response = new SimpleResponse("true", readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "videoAdmission", "getVideoTuttorialAdmission", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getListVideoOlder", method = RequestMethod.POST)
    public ResponseEntity<Response> getListVideoOlder(@RequestParam("userId") final String userId) {
        SimpleResponse response = null;
        try {
            String method = "getListVideoOlder()";

            logger.debug(method + " start");

            Object[] queryParams = { userId };
            String entityName = SibConstants.SqlMapper.SQL_SIB_GET_LIST_VIDEO_OLDER;

            List<Object> readObject = dao.readObjects(entityName, queryParams);

            response = new SimpleResponse("true", readObject);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new SimpleResponse(SibConstants.FAILURE, "videoAdmission", "getVideoTuttorialAdmission", e.getMessage());
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

}
