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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.model.ActivityLogData;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.ActivityLogService;
import com.siblinks.ws.service.PlaylistService;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.RandomString;
import com.siblinks.ws.util.SibConstants;

/**
 * {@link PlaylistService}
 * 
 * @author Hoai Nguyen
 * @version v1.0
 */
@RestController
@RequestMapping("/siblinks/services/playlist")
public class PlaylistServiceImpl implements PlaylistService {
    private final Log logger = LogFactory.getLog(PlaylistServiceImpl.class);

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ObjectDao dao;

    @Autowired
    private Environment environment;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    ActivityLogService activiLogService;

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getPlaylist", method = RequestMethod.GET)
    public ResponseEntity<Response> getPlaylist(@RequestParam final long userid, @RequestParam final int offset) {
        SimpleResponse simpleResponse = null;
        try {
            Object[] queryParams = { userid, offset };
            String entityName = SibConstants.SqlMapperBROT44.SQL_GET_PLAYLIST;
            List<Object> readObject = dao.readObjects(entityName, queryParams);
            if (readObject != null && readObject.size() > 0) {
                // Get count playlist
                List<Object> countObject = dao.readObjects(
                    SibConstants.SqlMapperBROT126.SQL_GET_COUNT_PLAYLIST_BY_USER,
                    new Object[] { userid });
                simpleResponse = new SimpleResponse(
                                                    SibConstants.SUCCESS,
                                                    "playlist",
                                                    "getPlaylist",
                                                    readObject,
                                                    ((Map<String, Long>) countObject.get(0)).get(Parameters.COUNT).toString());
            } else {
                simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "getPlaylist", SibConstants.NO_DATA);
            }

        } catch (DAOException e) {
            logger.error(e);
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "playlist", "getPlaylist", e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    private long getCountVideos(final String plid) throws DAOException {
        long numVideos = 0;
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numVideos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/insertPlaylist", method = RequestMethod.POST)
    public ResponseEntity<Response> insertPlaylist(@RequestParam final MultipartFile image, @RequestParam final String title,
            @RequestParam final String description, @RequestParam final String url, @RequestParam final long subjectId,
            @RequestParam final long createBy, @RequestParam(required = false) final ArrayList<String> vids) throws Exception {
        String entityName = null;
        SimpleResponse reponse = null;
        try {
            String fullPath = uploadPlaylistThumbnail(image);
            if (fullPath.length() > 0) {
                // insert playlist
                Object[] queryParams = { title, description, fullPath, url, subjectId, createBy };
                entityName = SibConstants.SqlMapperBROT44.SQL_INSERT_PLAYLIST;
                long plid = dao.insertObject(entityName, queryParams);

                if (plid > 0) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("plid", plid);
                    if (vids != null && vids.size() > 0) {
                        for (String vid : vids) {
                            queryParams = new Object[] { vid };
                            dao.insertUpdateObject(SibConstants.SqlMapperBROT43.SQL_DELETE_VIDEO_PLAYLIST, queryParams);

                            queryParams = new Object[] { plid, vid };
                            dao.insertUpdateObject(SibConstants.SqlMapperBROT126.SQL_ADD_VIDEOS_PLAYLIST, queryParams);
                        }
                    }
                    activiLogService.insertActivityLog(new ActivityLogData(
                                                                           SibConstants.TYPE_PLAYLIST,
                                                                           "C",
                                                                           "You created a new playlist",
                                                                           String.valueOf(createBy),
                                                                           String.valueOf(plid)));
                    map.put("message", "success");
                    reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "insertPlaylist", map);
                } else {
                    reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "insertPlaylist", "failed");
                }
            } else {
                reponse = new SimpleResponse(SibConstants.FAILURE, "Upload playlist thumbnail failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(SibConstants.FAILURE, "playlist", "insertPlaylist", e.getMessage());
        }

        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    private String uploadPlaylistThumbnail(final MultipartFile image) throws Exception {
        String fullPath = "";
        String filename = "";
        String name;
        String filepath = "";
        String directory = environment.getProperty("directoryPlaylistImage");
        String service = environment.getProperty("directoryGetPlaylistImage");
        String strExtenstionFile = environment.getProperty("file.upload.image.type");
        String limitSize = environment.getProperty("file.upload.essay.size");

        if (image != null) {
            name = image.getOriginalFilename();
            String nameExt = FilenameUtils.getExtension(name);
            boolean status = strExtenstionFile.contains(nameExt.toLowerCase());
            if (image.getSize() > Long.parseLong(limitSize)) {
                throw new Exception("Thumbnail maximum is 5MB.");
            } else {
                BufferedOutputStream stream = null;
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
                        throw new Exception("Upload playlist thumbnail failed");
                    } finally {
                        if (stream != null) {
                            stream.close();
                        }
                    }
                }
            }
        } else {
            throw new Exception("Please select playlist thumbnail.");
        }

        return fullPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/deletePlaylist", method = RequestMethod.POST)
    public ResponseEntity<Response> deletePlaylist(@RequestBody final RequestData request) {
        SimpleResponse reponse = null;
        try {
            boolean status = deletePlaylist(request.getRequest_playlist().getPlid(), request.getRequest_playlist().getCreateBy());
            if (status) {
                activiLogService.insertActivityLog(new ActivityLogData(
                                                                       SibConstants.TYPE_PLAYLIST,
                                                                       "D",
                                                                       "You deleted a playlist",
                                                                       request.getRequest_playlist().getCreateBy(),
                                                                       null));

                reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "deletePlaylist", "success");
            } else {
                reponse = new SimpleResponse(SibConstants.FAILURE, "playlist", "deletePlaylist", "failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(SibConstants.FAILURE, "playlist", "deletePlaylist", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    private boolean deletePlaylist(final String plid, final String uid) {
        boolean flag = false;

        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            Object[] queryParams = { plid };

            dao.insertUpdateObject(SibConstants.SqlMapperBROT44.SQL_DELETE_PLAYLIST_VIDEO, queryParams);

            queryParams = new Object[] { plid, uid };
            dao.insertUpdateObject(SibConstants.SqlMapperBROT44.SQL_DELETE_PLAYLIST, queryParams);
            transactionManager.commit(status);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            transactionManager.rollback(status);
        }
        return flag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getPlaylistById/{plid}", method = RequestMethod.GET)
    public ResponseEntity<Response> getPlaylistById(@PathVariable(value = "plid") final long plid) throws Exception {
        String entityName = null;
        SimpleResponse reponse = null;
        try {
            Object[] queryParams = { plid };
            entityName = SibConstants.SqlMapperBROT44.SQL_GET_PLAYLIST_BY_ID;
            List<Object> readObject = dao.readObjects(entityName, queryParams);
            Map<String, Object> objectReturn = null;
            if (readObject != null && readObject.size() > 0) {
                objectReturn = (Map<String, Object>) readObject.get(0);
                reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "getPlaylist", objectReturn);
            } else {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "getPlaylist", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(SibConstants.FAILURE, "playlist", "getPlaylist", e.getMessage());
        }

        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/searchPlaylist", method = RequestMethod.POST)
    public ResponseEntity<Response> searchPlaylist(@RequestBody final RequestData request) throws Exception {
        Object[] queryParams = null;
        String entityName = "";
        SimpleResponse reponse = null;

        try {
            String term = StringEscapeUtils.escapeJava(request.getRequest_data().getKeySearch().trim());
            int subjectId = request.getRequest_data().getSubjectId() != null ? Integer.parseInt(request
                .getRequest_data()
                .getSubjectId()) : 0;
            int offset = request.getRequest_data().getOffset() != null ? Integer.parseInt(request.getRequest_data().getOffset()) : 0;

            if (subjectId == 0) {
                queryParams = new Object[] { request.getRequest_data().getUid() };
                entityName = SibConstants.SqlMapperBROT163.SQL_SEARCH_PLAYLIST_MENTOR;
            } else {
                queryParams = new Object[] { request.getRequest_data().getUid(), subjectId };
                entityName = SibConstants.SqlMapperBROT163.SQL_SEARCH_PLAYLIST_WITH_SUBJECT;
            }
            String whereClause = String.format(
                "and p.name like '%%%s%%' order by p.CreateDate DESC limit 10 offset %d",
                term,
                offset);

            List<Object> readObject = dao.readObjectsWhereClause(entityName, whereClause, queryParams);

            List<Object> dataReturn = new ArrayList<Object>();
            if (readObject != null && readObject.size() > 0) {
                Map<String, Object> playlistItem = null;
                for (Object object : readObject) {
                    playlistItem = (Map<String, Object>) object;
                    playlistItem.put("count_videos", getCountVideos(playlistItem.get("plid").toString()));
                    dataReturn.add(playlistItem);
                }
                reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "searchPlaylist", dataReturn);
            } else {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "searchPlaylist", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(SibConstants.FAILURE, "playlist", "searchPlaylist", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getPlaylistBySubject", method = RequestMethod.GET)
    public ResponseEntity<Response> getPlaylistBySubject(final long uid, final long subjectId, final int offset) {
        SimpleResponse reponse = null;
        try {
            Object[] queryParams = { uid, subjectId, offset };
            String entityName = SibConstants.SqlMapperBROT163.SQL_GET_PLAYLIST_BY_SUBJECT;
            List<Object> readObject = dao.readObjects(entityName, queryParams);
            List<Object> dataReturn = new ArrayList<Object>();

            if (readObject != null && readObject.size() > 0) {
                Map<String, Object> playlistItem = null;
                for (Object object : readObject) {
                    playlistItem = (Map<String, Object>) object;
                    playlistItem.put("count_videos", getCountVideos(playlistItem.get("plid").toString()));
                    dataReturn.add(playlistItem);
                }
                reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "getPlaylistBySubject", dataReturn);
            } else {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "getPlaylistBySubject", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(SibConstants.FAILURE, "playlist", "getPlaylistBySubject", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/deleteMultiplePlaylist", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteMultiplePlaylist(@RequestBody final RequestData request) {
        String uid = request.getRequest_playlist().getCreateBy();
        ArrayList<String> plids = request.getRequest_playlist().getPlids();
        SimpleResponse reponse = null;
        int countSuccess = 0;
        int countFail = 0;
        boolean status = false;
        for (String plid : plids) {
            status = deletePlaylist(plid, uid);
            if (status) {
                countSuccess++;
            } else {
                countFail++;
            }
        }
        try {
            activiLogService.insertActivityLog(new ActivityLogData(
                                                                   SibConstants.TYPE_PLAYLIST,
                                                                   "D",
                                                                   "You deleted a playlist",
                                                                   uid,
                                                                   null));
        } catch (Exception e) {
            logger.debug(e.getCause());
            e.printStackTrace();
        }
        String msg = String.format("Delete success %d playlist and failed %d playlist", countSuccess, countFail);
        reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "deleteMultiplePlaylist", msg);

        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/deleteVideoInPlaylist", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteVideoInPlaylist(@RequestBody final RequestData request) {
        ArrayList<String> vids = request.getRequest_data().getVids();
        SimpleResponse reponse = null;

        Object[] params = null;
        try {
            for (String vid : vids) {
                params = new Object[] { vid };
                dao.insertUpdateObject(SibConstants.SqlMapperBROT163.SQL_DELETE_VIDEO_IN_PLAYLIST, params);
            }
            reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "deleteVideoInPlaylist", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(SibConstants.FAILURE, "playlist", "deleteVideoInPlaylist", e.getMessage());
        }

        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updatePlaylist", method = RequestMethod.POST)
    public ResponseEntity<Response> updatePlaylist(@RequestParam(required = false) final MultipartFile image,
            @RequestParam final String oldImage, @RequestParam final String title, @RequestParam final String description,
            @RequestParam final long subjectId, @RequestParam final long createBy, @RequestParam final long plid) {
        String entityName = null;
        boolean updateObject;
        SimpleResponse reponse = null;
        if (subjectId <= 0) {
            reponse = new SimpleResponse(SibConstants.FAILURE, "playlist", "updatePlaylist", "Subject is not valid");
        } else {

            try {
                String newImage = null;
                Object[] queryParams = null;
                entityName = SibConstants.SqlMapperBROT44.SQL_UPDATE_PLAYLIST;
                if (image != null) {
                    newImage = uploadPlaylistThumbnail(image);
                }

                if (newImage != null) {
                    queryParams = new Object[] { title, description, newImage, subjectId, plid, createBy };
                } else {
                    queryParams = new Object[] { title, description, oldImage, subjectId, plid, createBy };
                }

                updateObject = dao.insertUpdateObject(entityName, queryParams);
                if (updateObject) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("status", "success");
                    if (newImage != null && newImage.length() > 0) {
                        map.put("newImage", newImage);
                    } else {
                        map.put("newImage", oldImage);
                    }
                    activiLogService.insertActivityLog(new ActivityLogData(
                                                                           SibConstants.TYPE_PLAYLIST,
                                                                           "U",
                                                                           "You have updated playlist",
                                                                           String.valueOf(createBy),
                                                                           String.valueOf(plid)));

                    reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "updatePlaylist", map);

                    if (newImage != null && !"".equals(newImage) && oldImage != null && !"".equals(oldImage)) {
                        String fileName = oldImage.substring(oldImage.lastIndexOf("/"), oldImage.length());
                        File fileOld = new File(environment.getProperty("directoryPlaylistImage") + fileName);
                        if (fileOld.exists()) {
                            FileUtils.forceDeleteOnExit(fileOld);
                        }
                    }
                } else {
                    reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "updatePlaylist", "failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                reponse = new SimpleResponse(SibConstants.FAILURE, "playlist", "updatePlaylist", e.getMessage());
            }
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAllPlaylist", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllPlaylist(@RequestParam final long uid) {
        SimpleResponse reponse = null;
        try {
            List<Object> readObjects = dao.readObjects(SibConstants.SqlMapperBROT163.SQL_GET_ALL_PLAYLIST, new Object[] { uid });
            if (readObjects != null && readObjects.size() > 0) {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "getAllPlaylist", readObjects);
            } else {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "playlist", "getAllPlaylist", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(SibConstants.FAILURE, "playlist", "getAllPlaylist", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getVideoInPlaylist", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoInPlaylist(@RequestParam final long plid, @RequestParam final int offset) {
        Object[] queryParams = { plid, offset };
        SimpleResponse reponse = null;
        try {
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapperBROT163.SQL_GET_VIDEOS_IN_PLAYLIST, queryParams);
            if (readObject != null && readObject.size() > 0) {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "Playlist", "getVideoInPlaylist", readObject);
            } else {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "Playlist", "getVideoInPlaylist", SibConstants.NO_DATA);
            }

        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(SibConstants.FAILURE, "playlist", "getVideoInPlaylist", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }
}
