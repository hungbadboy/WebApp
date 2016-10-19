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

import com.siblinks.ws.common.DAOException;
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

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAdmission", method = RequestMethod.GET)
    public ResponseEntity<Response> getAdmission() {
        String method = "getAdmission()";
        logger.debug(method + " start");
        Object[] queryParams = {};
        SimpleResponse reponse = null;
        try {
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ADMISSION, queryParams);
            reponse = new SimpleResponse(SibConstants.SUCCESS, readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            logger.debug(method + " start");
            reponse = new SimpleResponse(SibConstants.FAILURE, e.getMessage());
        }
        
        logger.debug(method + " end");
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }
}
