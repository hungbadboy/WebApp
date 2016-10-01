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
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.PlaylistService;
import com.siblinks.ws.util.RandomString;
import com.siblinks.ws.util.SibConstants;

/**
 * @author Hoai Nguyen
 * @version v1.0
 */
@RestController
@RequestMapping("/siblinks/services/playlist")
public class PlayplistServiceImpl implements PlaylistService {

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ObjectDao dao;

    @Autowired
    private Environment environment;
    /*
     * (non-Javadoc)
     * 
     * @see com.siblinks.ws.service.PlaylistService#getPlaylist()
     */
    @Override
    @RequestMapping(value = "/getPlaylist", method = RequestMethod.GET)
    public ResponseEntity<Response> getPlaylist(@RequestParam final long userid, @RequestParam final int offset) throws Exception {
        Object[] queryParams = { userid, offset };
        String entityName = SibConstants.SqlMapperBROT44.SQL_GET_PLAYLIST;
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        SimpleResponse reponse = null;
        List<Object> dataReturn = new ArrayList<Object>();

        if (readObject != null && readObject.size() > 0) {
            Map<String, Object> playlistItem = null;
            for (Object object : readObject) {
                playlistItem = (Map<String, Object>) object;
                playlistItem.put("count_videos", getCountVideos(playlistItem.get("plid").toString()));
                dataReturn.add(playlistItem);
            }
            reponse = new SimpleResponse("" + true, "playlist", "getPlaylist", dataReturn);
        } else {
            reponse = new SimpleResponse("" + true, "playlist", "getPlaylist", SibConstants.NO_DATA);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }
    
    private long getCountVideos(final String plid) {
        long numVideos = 0;
        Object[] params = new Object[] { plid };
        List<Object> readObject = dao.readObjects(SibConstants.SqlMapperBROT163.SQL_GET_COUNT_VIDEOS_IN_PLAYLIST, params);
        if (readObject != null && readObject.size() > 0) {
            for (Object object : readObject) {
                Map<String, Object> map = (Map<String, Object>) object;
                numVideos = (long) map.get("numVideos");
            }
        } else {
            numVideos = 0;
        }
        return numVideos;
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
    public ResponseEntity<Response> insertPlaylist(@RequestParam final MultipartFile image, @RequestParam final String title,
            @RequestParam final String description, @RequestParam final String url, @RequestParam final long subjectId, @RequestParam final long createBy)
            throws Exception {
        String entityName = null;
        boolean insertObject;

        String fullPath = "";
        String filename = "";
        String name;
        String filepath = "";
        String directory = environment.getProperty("directoryPlaylistImage");
        String service = environment.getProperty("directoryGetPlaylistImage");
        String strExtenstionFile = environment.getProperty("file.upload.image.type");
        name = image.getOriginalFilename();
        String nameExt = FilenameUtils.getExtension(name);
        boolean status = strExtenstionFile.contains(nameExt.toLowerCase());
        BufferedOutputStream stream = null;
        SimpleResponse reponse = null;
        if (directory != null && status) {
            try {
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
                stream.write(image.getBytes());

                fullPath = service + filename;
            } catch (Exception e) {
                reponse = new SimpleResponse("" + Boolean.FALSE, "Upload playlist thumbnail failed");
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            if (fullPath.length() > 0) {
                try {
                    // insert playlist
                    Object[] queryParams = { title, description, fullPath, url, subjectId, createBy };
                    entityName = SibConstants.SqlMapperBROT44.SQL_INSERT_PLAYLIST;
                    insertObject = dao.insertUpdateObject(entityName, queryParams);

                    if (insertObject) {
                        reponse = new SimpleResponse("" + true, "playlist", "insertPlaylist", "success");
                    } else {
                        reponse = new SimpleResponse("" + true, "playlist", "insertPlaylist", "failed");
                    }
                } catch (Exception e) {
                    reponse = new SimpleResponse("" + true, "playlist", "insertPlaylist", "failed");
                }
            } else {
                reponse = new SimpleResponse("" + Boolean.FALSE, "Upload playlist thumbnail failed");
            }
            return new ResponseEntity<Response>(reponse, HttpStatus.OK);

        } else {
            return new ResponseEntity<Response>(HttpStatus.NO_CONTENT);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.siblinks.ws.service.PlaylistService#deletePlaylist(long)
     */
    @Override
    @RequestMapping(value = "/deletePlaylist", method = RequestMethod.POST)
    public ResponseEntity<Response> deletePlaylist(@RequestBody final RequestData request) throws Exception {
        SimpleResponse reponse = null;
        try {
            boolean status = deletePlaylist(request.getRequest_playlist().getPlid());
            if (status) {
                reponse = new SimpleResponse("" + true, "playlist", "deletePlaylist", "success");
            } else {
                reponse = new SimpleResponse("" + true, "playlist", "deletePlaylist", "failed");
            }
        } catch (Exception e) {
            throw e;
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    private boolean deletePlaylist(final String plid) {
        boolean flag = false;

        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            Object[] queryParams = { plid };

            dao.insertUpdateObject(SibConstants.SqlMapperBROT44.SQL_DELETE_PLAYLIST_VIDEO, queryParams);

            dao.insertUpdateObject(SibConstants.SqlMapperBROT44.SQL_DELETE_PLAYLIST, queryParams);
            transactionManager.commit(status);
            flag = true;
        } catch (Exception e) {
            flag = false;
            transactionManager.rollback(status);
        }
        return flag;
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
        Map<String, Object> objectReturn = null;
        SimpleResponse reponse = null;
        if (readObject != null && readObject.size() > 0) {
            objectReturn = (Map<String, Object>) readObject.get(0);
            reponse = new SimpleResponse("" + true, "playlist", "getPlaylist", objectReturn);
        } else {
            reponse = new SimpleResponse("" + true, "playlist", "getPlaylist", SibConstants.NO_DATA);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    // @RequestMapping(value = "/searchPlaylist", method = RequestMethod.GET)
    public ResponseEntity<Response> searchPlaylist(@RequestParam final long uid, @RequestParam final String keyword, @RequestParam final int offset)
            throws Exception {
        Object[] queryParams = { uid };
        String entityName = SibConstants.SqlMapperBROT163.SQL_SEARCH_PLAYLIST;
        String whereClause = String.format(
            "and p.name like '%%%s%%' or p.description like '%%%s%%' order by p.CreateDate DESC limit 10 offset %d",
            keyword,
            keyword,
            offset);

        List<Object> readObject = dao.readObjectsWhereClause(entityName, whereClause, queryParams);

        SimpleResponse reponse = null;
        List<Object> dataReturn = new ArrayList<Object>();
        if (readObject != null && readObject.size() > 0) {
            Map<String, Object> playlistItem = null;
            for (Object object : readObject) {
                playlistItem = (Map<String, Object>) object;
                playlistItem.put("count_videos", getCountVideos(playlistItem.get("plid").toString()));
                dataReturn.add(playlistItem);
            }
            reponse = new SimpleResponse("" + true, "playlist", "searchPlaylist", dataReturn);
        } else {
            reponse = new SimpleResponse("" + true, "playlist", "searchPlaylist", SibConstants.NO_DATA);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getPlaylistBySubject", method = RequestMethod.GET)
    public ResponseEntity<Response> getPlaylistBySubject(final long uid, final long subjectId, final int offset) throws Exception {
        Object[] queryParams = { uid, subjectId, offset };
        String entityName = SibConstants.SqlMapperBROT163.SQL_GET_PLAYLIST_BY_SUBJECT;
        List<Object> readObject = dao.readObjects(entityName, queryParams);
        SimpleResponse reponse = null;
        List<Object> dataReturn = new ArrayList<Object>();

        if (readObject != null && readObject.size() > 0) {
            Map<String, Object> playlistItem = null;
            for (Object object : readObject) {
                playlistItem = (Map<String, Object>) object;
                playlistItem.put("count_videos", getCountVideos(playlistItem.get("plid").toString()));
                dataReturn.add(playlistItem);
            }
            reponse = new SimpleResponse("" + true, "playlist", "getPlaylistBySubject", dataReturn);
        } else {
            reponse = new SimpleResponse("" + true, "playlist", "getPlaylistBySubject", SibConstants.NO_DATA);
        }

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/deleteMultiplePlaylist", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteMultiplePlaylist(@RequestBody final RequestData request) throws Exception {
        ArrayList<String> plids = request.getRequest_playlist().getPlids();
        SimpleResponse reponse = null;
        int countSuccess = 0;
        int countFail = 0;
        boolean status = false;
        for (String plid : plids) {
            status = deletePlaylist(plid);
            if (status) {
                countSuccess++;
            } else {
                countFail++;
            }
        }
        String msg = String.format("Delete success %d playlist and failed %d playlist", countSuccess, countFail);
        reponse = new SimpleResponse("" + true, "playlist", "deleteMultiplePlaylist", msg);

        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }
}
