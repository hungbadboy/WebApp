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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.AdmissionService;
import com.siblinks.ws.util.SibConstants;

/**
 *
 *
 * @author hungpd
 * @version 1.0
 */
@RestController
@RequestMapping("/siblinks/services/admission")
public class AdmissionServiceImpl implements AdmissionService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    ObjectDao dao;

    @Autowired
    Environment env;

    @Override
    @RequestMapping(value = "/getAdmission", method = RequestMethod.GET)
    public ResponseEntity<Response> getAdmission() {
        String method = "getAdmission()";
        logger.debug(method + " start");
        Object[] queryParams = {};
        
        String entityName = SibConstants.SqlMapper.SQL_GET_ADMISSION;
        List<Object> readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("true", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);

        logger.debug(method + " end");
        return entity;
    }

    /*@Override
    @RequestMapping(value = "/getSubAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> getSubAdmission(@RequestBody final RequestData request) {
        Object[] queryParams = { request.getRequest_data_admission().getIdAdmission() };
        List<List<Object>> resParentObject = new ArrayList<List<Object>>();
        List<Object> readObject = dao.readObjects(env.getProperty(SibConstants.SqlMapper.SQL_GET_SUB_ADMISSION), queryParams);

        if (readObject != null) {
            for (Object object : readObject) {
                List<Object> resChildObject = new ArrayList<Object>();
                Map<String, Object> map = (Map) object;
                String idSubAdmission = (String) map.get(Parameters.ID);
                queryParams[0] = idSubAdmission;
                List<Object> readObject1 = dao.readObjects(
                    env.getProperty(SibConstants.SqlMapper.SQL_GET_TOPIC_FIRST_ADMISSION),
                    queryParams);

                Map<String, Object> topic = null;

                try {
                    if (readObject1 != null) {
                        for (Object obj : readObject1) {
                            topic = (Map) obj;
                            Iterator<Entry<String, Object>> it = topic.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pairs = it.next();
                                if (pairs.getKey().equals("idSubAdmission")) {
                                    it.remove();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error(e);
                }

                Map<String, Object> mymap = new HashMap<String, Object>();
                mymap.put("topic", readObject1);
                resChildObject.add(map);
                resChildObject.add(mymap);
                resParentObject.add(resChildObject);
            }
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + true,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    resParentObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getTopicSubAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> getTopicSubAdmission(@RequestBody final RequestData request) {
        Object[] queryParams = { request.getRequest_data_admission().getIdSubAdmission() };
        List<List<Object>> resParentObject = new ArrayList<List<Object>>();
        List<Object> readObject = dao.readObjects(
            env.getProperty(SibConstants.SqlMapper.SQL_GET_TOPIC_SUB_ADMISSION),
            queryParams);
        if (readObject != null) {
            for (Object object : readObject) {
                List<Object> resChildObject = new ArrayList<Object>();
                Map<String, Object> mymap = (Map) object;
                String idTopicSubAdmission = (String) mymap.get(Parameters.ID);
                queryParams[0] = idTopicSubAdmission;

                List<Object> readObject1 = dao.readObjects(
                    env.getProperty(SibConstants.SqlMapper.SQL_GET_VIDEO_ADMISSION_WITH_TOPIC),
                    queryParams);

                Map<String, Object> mymap1 = new HashMap<String, Object>();
                mymap1.put("videos", readObject1);
                resChildObject.add(mymap);
                resChildObject.add(mymap1);
                resParentObject.add(resChildObject);
            }
        }
        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    resParentObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getAllTopicSubAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> getAllTopicSubAdmission(@RequestBody final RequestData request) {
        List<Object> readObject = dao.readObjectsNotResource(SibConstants.SqlMapper.SQL_GET_ALL_TOPIC_SUB_ADMISSION);
        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }*/
}
