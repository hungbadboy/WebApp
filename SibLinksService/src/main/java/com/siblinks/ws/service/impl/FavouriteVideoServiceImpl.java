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

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.common.LoggedInChecker;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.model.FavouriteData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.FavouriteVideoService;
import com.siblinks.ws.util.SibConstants;

/**
 * This class API handle crud relate to video favourite
 * 
 * @author hungpd
 * @version 1.0
 */

@RestController
@RequestMapping("/siblinks/services/favourite")
public class FavouriteVideoServiceImpl implements FavouriteVideoService {

    private final Log logger = LogFactory.getLog(AdminServiceImpl.class);

    @Autowired
    private ObjectDao dao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    LoggedInChecker logged;

    @Autowired
    private HttpServletRequest context;

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/addfavourite", method = RequestMethod.POST)
    public ResponseEntity<Response> addFavouriteVideo(@RequestBody final FavouriteData favourite) {
        logger.info("addFavouriteVideo " + new Date());
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        boolean isAdd = false;
        String message = "";
        try {
            // Insert in the video_favourite
            isAdd = dao.insertUpdateObject(
                SibConstants.SqlMapper.SQL_VIDEO_FAVOURITE_INSERT,
                new Object[] { favourite.getUid(), favourite.getVid() });

            // Update in the videos table
            dao.insertUpdateObject(SibConstants.SqlMapper.SQL_VIDEO_FAVOURITE_UPDATE, new Object[] { favourite.getVid() });

            transactionManager.commit(status);
            message = "Favourite add successful";
            logger.info("addfavourite success " + new Date());
        } catch (DataAccessException e) {
            message = e.getMessage();
            logger.error("addfavourite error " + new Date());
            transactionManager.rollback(status);
        }
        SimpleResponse reponse = new SimpleResponse("" + isAdd, "favouriteService", "deleteMenuData", message);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/checkFavouriteVideo", method = RequestMethod.POST)
    public ResponseEntity<Response> checkFavouriteVideo(@RequestBody final FavouriteData favourite) {
       
        String message = "false";
        try {
            List<Object> readObjects = dao.readObjects( SibConstants.SqlMapper.SQL_CHECK_VIDEO_FAVOURITE,
                new Object[] {favourite.getVid(), favourite.getUid()});
            if (readObjects != null && readObjects.size() > 0) {
                message = "true";
            }
        } catch (Exception e) {
            message = e.getMessage();
        }
        SimpleResponse reponse = new SimpleResponse("" + message, "checkFavouriteVideo", "checkFavouriteVideo", message);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/delFavourite", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteFavouriteVideo(@RequestBody final FavouriteData favourite) {

        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        boolean isDelete = false;
        String message ="";
        try {
            if (favourite.getVid() != null && !"".equals(favourite.getVid())) {

                // Update in the videos table
                dao.insertUpdateObject(SibConstants.SqlMapper.SQL_VIDEO_UNFAVOURITE_UPDATE, new Object[] { favourite.getVid() });

                // Delete in the video_favourite
                isDelete = dao.insertUpdateObject(
                    SibConstants.SqlMapper.SQL_VIDEO_FAVOURITE_DELETE,
                    new Object[] { favourite.getUid(), favourite.getVid() });
            } else {

                Object[] params = new Object[] { favourite.getUid() };
                // Update in the videos table
                dao.insertUpdateObject(SibConstants.SqlMapper.SQL_VIDEO_UNFAVOURITE_UPDATE_ALL_BY_USER, params);

                // Delete in the video_favourite
                isDelete = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_VIDEO_FAVOURITE_DELETE_ALL, params);
            }
            
            // commit
            transactionManager.commit(status);
            message =((isDelete)? " Delete favourite successful": "Delete favourite failure");
            logger.info("delfavourite success " + new Date());

        } catch (Exception e) {
            logger.error("addfavourite error " + new Date());
            message = "System error " + e.getMessage();
            transactionManager.rollback(status);
        }
        SimpleResponse reponse = new SimpleResponse("" + isDelete, "adminService", "deleteFavouriteVideo", message);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/countFavourite/{uid}", method = RequestMethod.GET)
    public ResponseEntity<Response> getCountFavourite(@PathVariable(value = "uid") final String uid) throws Exception {
        // Get in the videos table
        List<Object> readObjects = dao
            .readObjects(SibConstants.SqlMapper.SQL_VIDEO_FAVOURITE_COUNT_BY_USER, new Object[] { uid });
        SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE, "favouriteService", "getCountFavourite", readObjects);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        logger.info("getCountFavourite success " + new Date());
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/allFavourite/{uid}", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllFavouriteByUid(@PathVariable(value = "uid") final String uid) throws Exception {
        // Get all favourite by user id
        List<Object> readObjects = dao.readObjects(
            SibConstants.SqlMapper.SQL_VIDEO_FAVOURITE_READ_ALL_BY_USER,
            new Object[] { uid });
        SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE, "favouriteService", "getAllFavouriteByUid", readObjects);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        logger.info("getAllFavouriteByUid success " + new Date());
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/favouriteLimit", method = RequestMethod.GET)
    public ResponseEntity<Response> getFavouriteByUid(@RequestParam(value = "uid") final int uid,
            @RequestParam(value = "limit") final int limit, @RequestParam(value = "favouritetime") final long favouritetime)
            throws Exception {
        // Cast to Date
        Date dateFavourite = new Date();
        dateFavourite.setTime(favouritetime);

        // Get all favourite by user id
        List<Object> readObjects = dao.readObjects(
            SibConstants.SqlMapper.SQL_VIDEO_FAVOURITE_READ_LIMIT,
            new Object[] { uid, dateFavourite, limit });

        logger.info("getAllFavouriteByUid success " + new Date());

        SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE, "favouriteService", "getAllFavouriteByUid", readObjects);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }
}
