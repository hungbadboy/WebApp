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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.PlaylistService;
import com.siblinks.ws.util.SibConstants;

/**
 * @author Hoai Nguyen
 * @version v1.0
 */
@RestController
@RequestMapping("/siblinks/services/playlist")
public class PlayplistServiceImpl implements PlaylistService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ObjectDao dao;

    /*
     * (non-Javadoc)
     * 
     * @see com.siblinks.ws.service.PlaylistService#getPlaylist()
     */
    @Override
    @RequestMapping(value = "/getPlaylist/{userid}", method = RequestMethod.GET)
    public ResponseEntity<Response> getPlaylist(@PathVariable(value = "userid") final long userid) throws Exception {
        String entityName = null;

        Object[] queryParams = { userid };
        entityName = SibConstants.SqlMapperBROT44.SQL_GET_PLAYLIST;
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        SimpleResponse reponse = null;
        if (readObject != null && readObject.size() > 0) {
            reponse = new SimpleResponse("" + true, "playlist", "getPlaylist", readObject);
        } else {
            reponse = new SimpleResponse("" + true, "playlist", "getPlaylist", "No data found");
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Autowired
    private PlatformTransactionManager transactionManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.siblinks.ws.service.PlaylistService#insertPlaylist(com.siblinks.ws
     * .model.RequestData)
     */
    @Override
    @RequestMapping(value = "/insertPlaylist", method = RequestMethod.POST)
    public ResponseEntity<Response> insertPlaylist(@RequestBody final RequestData request) throws Exception {
        String entityName = null;
        boolean insertObject;
        try {
            Object[] queryParams = { request.getRequest_playlist().getName(), request.getRequest_playlist().getDescription(), request
                .getRequest_playlist()
                .getImage(), request.getRequest_playlist().getUrl(), request.getRequest_playlist().getSubjectId(), request
                .getRequest_playlist()
                .getCreateBy() };
            entityName = SibConstants.SqlMapperBROT44.SQL_INSERT_PLAYLIST;
            insertObject = dao.insertUpdateObject(entityName, queryParams);
        } catch (Exception e) {
            throw e;
        }

        SimpleResponse reponse = null;
        if (insertObject) {
            reponse = new SimpleResponse("" + true, "playlist", "getPlaylist", "success");
        } else {
            reponse = new SimpleResponse("" + true, "playlist", "getPlaylist", "failed");
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.siblinks.ws.service.PlaylistService#deletePlaylist(long)
     */
    @Override
    @RequestMapping(value = "/deletePlaylist", method = RequestMethod.POST)
    public ResponseEntity<Response> deletePlaylist(@RequestBody final RequestData request) throws Exception {
        String entityName = null;

        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Object[] queryParams = { request.getRequest_playlist().getPlid() };

            entityName = SibConstants.SqlMapperBROT44.SQL_DELETE_PLAYLIST_VIDEO;
            dao.insertUpdateObject(entityName, queryParams);

            entityName = SibConstants.SqlMapperBROT44.SQL_DELETE_PLAYLIST;
            dao.insertUpdateObject(entityName, queryParams);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }

        SimpleResponse reponse = new SimpleResponse("" + true, "playlist", "deletePlaylist", "success");

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.siblinks.ws.service.PlaylistService#updatePlaylist(com.siblinks.ws
     * .model.RequestData)
     */
    @Override
    @RequestMapping(value = "/updatePlaylist", method = RequestMethod.POST)
    public ResponseEntity<Response> updatePlaylist(@RequestBody final RequestData request) throws Exception {
        String entityName = null;
        boolean insertObject;
        try {
            Object[] queryParams = { request.getRequest_playlist().getName(), request.getRequest_playlist().getDescription(), request
                .getRequest_playlist()
                .getImage(), request.getRequest_playlist().getPlid() };
            entityName = SibConstants.SqlMapperBROT44.SQL_UPDATE_PLAYLIST;
            insertObject = dao.insertUpdateObject(entityName, queryParams);
        } catch (Exception e) {
            throw e;
        }

        SimpleResponse reponse = null;
        if (insertObject) {
            reponse = new SimpleResponse("" + true, "playlist", "getPlaylist", "success");
        } else {
            reponse = new SimpleResponse("" + true, "playlist", "getPlaylist", "failed");
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.siblinks.ws.service.PlaylistService#deletePlaylist(long)
     */
    @Override
    @RequestMapping(value = "/getPlaylistById/{plid}", method = RequestMethod.GET)
    public ResponseEntity<Response> getPlaylistById(@PathVariable(value = "plid") final long plid) throws Exception {
        String entityName = null;

        Object[] queryParams = { plid };
        entityName = SibConstants.SqlMapperBROT44.SQL_GET_PLAYLIST_BY_ID;
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        SimpleResponse reponse = null;
        if (readObject != null && readObject.size() > 0) {
            reponse = new SimpleResponse("" + true, "playlist", "getPlaylist", readObject);
        } else {
            reponse = new SimpleResponse("" + true, "playlist", "getPlaylist", "No data found");
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

}
