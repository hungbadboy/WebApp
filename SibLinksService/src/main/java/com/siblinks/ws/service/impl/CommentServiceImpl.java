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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.CommentsService;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.RandomString;
import com.siblinks.ws.util.SibConstants;
import com.siblinks.ws.util.StringUtil;

/**
 *
 * {@link CommentsService}
 *
 * @author hungpd
 * @version 1.0
 */
@RestController
@RequestMapping("/siblinks/services/comments")
public class CommentServiceImpl implements CommentsService {

    private final Log logger = LogFactory.getLog(CommentServiceImpl.class);

    @Autowired
    private HttpServletRequest context;

    @Autowired
    ObjectDao dao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private Environment environment;

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = "/getNestedComments", method = RequestMethod.POST)
    public ResponseEntity<Response> getNestedComments(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = { request.getRequest_data().getCid() };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_GET_NESTED_COMMENTS, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (Exception e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/addNestedComment", method = RequestMethod.POST)
    public ResponseEntity<Response> addNestedComment(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {

            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }
            Object[] queryParams = { request.getRequest_data().getCid(), request.getRequest_data().getAuthor(), request
                .getRequest_data()
                .getAuthorID(), request.getRequest_data().getContent(), request.getRequest_data().getImage() };

            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_INSERT_NESTED_COMMENT, queryParams);

            List<Object> readObject = null;
            if (status) {
                Object[] queryParams1 = { request.getRequest_data().getAuthorID(), request.getRequest_data().getContent() };
                readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_GET_NESTED_CID, queryParams1);
                int childId = Integer.valueOf(((Map) readObject.get(0)).get("cid").toString());
                Object[] queryParams2 = { request.getRequest_data().getCid(), Integer.valueOf(childId).toString() };
                dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_INSERT_NESTED_TABLE, queryParams2);
            }

            simpleResponse = new SimpleResponse(
                                                "" + status,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (Exception e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public ResponseEntity<Response> addComment(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        TransactionStatus statusDB = null;
        try {

            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }
            TransactionDefinition def = new DefaultTransactionDefinition();
            statusDB = transactionManager.getTransaction(def);
            // Get request data
            String content = request.getRequest_data().getContent().replace("'", "\\\\'");
            content = content.replace("(", "\\\\(");
            content = content.replace(")", "\\\\)");
            String userName = request.getRequest_data().getAuthor();
            String authorId = request.getRequest_data().getAuthorID();
            boolean status = true;
            int cid = 0;
            // insert comment table
            Object[] queryParams = { userName, authorId, content };
            long idComent = dao.insertObject(SibConstants.SqlMapper.SQL_SIB_ADD_COMMENT, queryParams);
            if (idComent > 0) {
                // Insert comment video table
                String vid = request.getRequest_data().getVid();
                Object[] queryParamsIns2 = { idComent, vid };
                status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_INSERT_VIDEO_COMMENT, queryParamsIns2);

                // Insert notification table
                String userId = request.getRequest_data().getUid();
                String subjectId = request.getRequest_data().getSubjectId();
                Object[] queryParamsIns3 = { userId, authorId, "commentVideo", "New comment of video", content, subjectId, vid };
                status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_VIDEO, queryParamsIns3);
            }

            transactionManager.commit(statusDB);

            simpleResponse = new SimpleResponse(
                                                "" + status,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                cid);
        } catch (Exception e) {
            if (statusDB != null) {
                transactionManager.rollback(statusDB);
            }
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/addCommentMobile", method = RequestMethod.POST)
    public ResponseEntity<Response> addCommentMobile(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            Object[] queryParams = { request.getRequest_data().getAuthorID(), request.getRequest_data().getContent(), request
                .getRequest_data()
                .getImage() };

            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_ADD_COMMENT, queryParams);
            int cid = 0;
            if (status) {
                Object[] queryParams1 = { request.getRequest_data().getContent(), request.getRequest_data().getAuthorID() };

                List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_LAST_INSERTED_COMMENT, queryParams1);
                Map lastInsertComment = (Map) readObject.get(0);
                Object[] queryParamsIns = { lastInsertComment.get(Parameters.CID).toString(), request.getRequest_data().getVid() };
                boolean flag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_INSERT_VIDEO_COMMENT, queryParamsIns);

                if (flag) {
                    Object[] queryParamsUpdate = { request.getRequest_data().getVid() };
                    dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_UPDATE_VIDEO_COMMENT, queryParamsUpdate);
                }
            }

            simpleResponse = new SimpleResponse(
                                                "" + status,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                cid);
        } catch (Exception e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<Response> update(@RequestBody final RequestData request) {
        SimpleResponse reponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                SimpleResponse simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
                return entity;
            }

            Object[] queryParams = { request.getRequest_data().getCid(), request.getRequest_data().getContent() };
            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_EDIT_COMMENT, queryParams);
            reponse = new SimpleResponse(
                                         SibConstants.SUCCESS,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         status);
        } catch (DAOException e) {
            e.printStackTrace();
            reponse = new SimpleResponse(
                                         SibConstants.FAILURE,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         e.getMessage());
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ResponseEntity<Response> remove(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            SimpleResponse simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
            ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            return entity;
        }

        Object[] queryParams = { request.getRequest_data().getCid() };

        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        SimpleResponse reponse = null;
        try {
            dao.insertUpdateObject(SibConstants.SqlMapperBROT163.SQL_DELETE_COMMENT_VIDEO, queryParams);
            dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_REMOVE_COMMENT, queryParams);
            transactionManager.commit(status);
            reponse = new SimpleResponse(
                                         SibConstants.SUCCESS,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         "Success");
        } catch (Exception e) {
            transactionManager.rollback(status);
            reponse = new SimpleResponse(
                                         SibConstants.FAILURE,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         "Failed");
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/addCommentArticle", method = RequestMethod.POST)
    public ResponseEntity<Response> addCommentArticle(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }
            String content = request.getRequest_data_article().getContent().replace("'", "\\\\'");
            content = content.replace("(", "\\\\(");
            content = content.replace(")", "\\\\)");

            Object[] queryParams = { request.getRequest_data_article().getAuthorId(), content, request
                .getRequest_data_article()
                .getArId() };

            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_ADD_COMMENT, queryParams);
            int cid = 0;
            if (status) {
                List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_LAST_INSERTED_COMMENT, queryParams);
                cid = Integer.valueOf(((Map) readObject.get(0)).get(Parameters.CID).toString());
                Object[] queryParamsIns = { ((Map) readObject.get(0)).get(Parameters.CID).toString(), request
                    .getRequest_data_article()
                    .getArId() };
                boolean flag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_INSERT_ARTICLE_COMMENT, queryParamsIns);

                readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_INFO_ARTICLE, queryParamsIns);
                Object[] queryParamsIns1 = { ((Map) readObject.get(0)).get(Parameters.AUTHOR_ID).toString(), request
                    .getRequest_data_article()
                    .getAuthorId(), "commentArticle", "New comment of article", "commented a article: " +
                                                                                ((Map) readObject.get(0)).get("title").toString() };
                if (!((Map) readObject.get(0))
                    .get("authorId")
                    .toString()
                    .equalsIgnoreCase(request.getRequest_data_article().getAuthorId())) {
                    dao.insertUpdateObject(SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_ARTICLE, queryParamsIns1);
                }

                if (flag) {
                    Object[] queryParamsUpdate = { request.getRequest_data_article().getArId() };
                    dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_UPDATE_ARTICLE_COMMENT, queryParamsUpdate);
                }
            }

            simpleResponse = new SimpleResponse(
                                                "" + status,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                cid);
        } catch (Exception e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/addCommentVideoAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> addCommentVideoAdmission(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        TransactionStatus statusDB = null;
        try {

            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            String content = request.getRequest_data().getContent();
            if (!StringUtil.isNull(content)) {
                content = content.replace("'", "\\\\'");
                content = content.replace("(", "\\\\(");
                content = content.replace(")", "\\\\)");
            } else {
                simpleResponse = new SimpleResponse(
                                                    "" + false,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    "");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
            }

            Object[] queryParams = { "", request.getRequest_data().getAuthorID(), content };
            boolean flag = true;
            long id = dao.insertObject(SibConstants.SqlMapper.SQL_SIB_ADD_COMMENT, queryParams);
            TransactionDefinition def = new DefaultTransactionDefinition();
            statusDB = transactionManager.getTransaction(def);
            if (id > 0) {
                Object[] queryParamsIns = { request.getRequest_data().getVid(), "" + id };
                flag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_INSERT_VIDEO_ADMISSION_COMMENT, queryParamsIns);
                List<Object> readObject = dao.readObjects(
                    SibConstants.SqlMapper.SQL_GET_VIDEO_ADMISSION_DETAIL_BY_ID,
                    new Object[] { request.getRequest_data().getVid() });
                Map userPostVideoMap = (Map) readObject.get(0);

                Object[] queryParamsIns3 = { userPostVideoMap.get("userid"), request.getRequest_data().getAuthorID(), "commentVideoAdmssion", "New comment of video", "commented video : " +
                                                                                                                                                                      userPostVideoMap
                                                                                                                                                                          .get(
                                                                                                                                                                              "title")
                                                                                                                                                                          .toString(), userPostVideoMap
                    .get("idAdmission"), request.getRequest_data().getVid() };
                if (!userPostVideoMap.get("userid").toString().equalsIgnoreCase(request.getRequest_data().getAuthorID())) {
                    dao.insertUpdateObject(SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_VIDEO, queryParamsIns3);
                }

            }
            transactionManager.commit(statusDB);
            simpleResponse = new SimpleResponse("" + flag, request.getRequest_data_type(), request.getRequest_data_method(), "" +
                                                                                                                             id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (statusDB != null) {
                transactionManager.rollback(statusDB);
            }
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, e.getMessage());
        }

        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAllComment", method = RequestMethod.POST)
    public ResponseEntity<Response> getAllComment(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                ResponseEntity<Response> entity = new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
                return entity;
            }

            Object[] queryParams = {};

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ALL_COMMENT, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/deleteComment", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteComment(@RequestBody final RequestData request) {

        Object[] queryParams = { request.getRequest_data().getCid() };

        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        SimpleResponse reponse = null;
        try {
            dao.insertUpdateObject(SibConstants.SqlMapperBROT163.SQL_DELETE_COMMENT_VIDEO, queryParams);
            dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_COMMENT, queryParams);
            transactionManager.commit(status);
            reponse = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), "Success");
        } catch (Exception e) {
            transactionManager.rollback(status);
            reponse = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), "Failed");
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> uploadFile(@RequestParam("uploadfile") final MultipartFile uploadfile) {

        String filename = "";
        String name = "";
        String filepath = "";
        SimpleResponse reponse = null;
        try {
            String directory = environment.getProperty("directoryImageComment");
            String directoryGetImage = environment.getProperty("directoryGetImageComment");
            String linkToImage = null;

            name = uploadfile.getContentType();
            boolean status = name.contains("image");
            if (directory != null && status) {
                RandomString randomName = new RandomString();

                filename = randomName.random();
                filepath = Paths.get(directory, filename + ".png").toString();

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
                stream.write(uploadfile.getBytes());
                stream.close();
                linkToImage = directoryGetImage + filename;

                reponse = new SimpleResponse(SibConstants.SUCCESS, linkToImage);
            } else {
                reponse = new SimpleResponse(
                                             SibConstants.FAILURE,
                                             "Your photos couldn't be uploaded. Photos should be saved as JPG, PNG, GIF or BMP files.");

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            reponse = new SimpleResponse(SibConstants.FAILURE, e.getMessage());

        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/uploadMultiFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> uploadMultiFile(@RequestParam("file") final MultipartFile[] uploadfiles) {

        MultipartFile uploadfile = null;
        String filePath = "";
        SimpleResponse reponse = null;
        try {
            String directoryGetImage = environment.getProperty("directoryGetImageComment");
            if (uploadfiles != null & uploadfiles.length > 0) {
                for (int i = 0; i < uploadfiles.length; i++) {
                    uploadfile = uploadfiles[i];
                    filePath += directoryGetImage + uploadFile(uploadfile, "directoryImageComment");
                    if (i < uploadfiles.length - 1) {
                        filePath += ";;";
                    }
                }
            }
            reponse = new SimpleResponse(SibConstants.SUCCESS, filePath);
        } catch (Exception e) {
            logger.error(e.getMessage());
            reponse = new SimpleResponse(SibConstants.FAILURE, e.getMessage());
        }

        return new ResponseEntity<Response>(reponse, HttpStatus.OK);

    }

    /**
     * {@inheritDoc}
     */
    public String uploadFile(final MultipartFile uploadfile, final String path) throws FileNotFoundException {

        String filename = "";
        String name;
        String filepath = "";
        BufferedOutputStream stream = null;
        try {
            String directory = environment.getProperty(path);
            String sample = ".png .jpng .jpg .bmp";
            name = uploadfile.getOriginalFilename();
            String nameExt = FilenameUtils.getExtension(name);
            boolean status = sample.contains(nameExt);
            if (directory != null && status) {
                RandomString randomName = new RandomString();
                filename = randomName.random();
                filepath = Paths.get(directory, filename + "." + nameExt).toString();
                // Save the file locally
                File file = new File(filepath);
                File parentDir = file.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }

                stream = new BufferedOutputStream(new FileOutputStream(file));
                stream.write(uploadfile.getBytes());

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException io) {
                // Do nothing
            }
        }

        return filename;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getImageComment/{name}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageComment(@PathVariable(value = "name") final String name) {
        ResponseEntity<byte[]> responseEntity = null;
        RandomAccessFile randomAccessFile = null;
        try {
            if (name != null) {
                String directory = environment.getProperty("directoryImageComment");
                String path = directory + "/" + name + ".png";

                randomAccessFile = new RandomAccessFile(path, "r");
                byte[] r = new byte[(int) randomAccessFile.length()];
                randomAccessFile.readFully(r);
                responseEntity = new ResponseEntity<byte[]>(r, new HttpHeaders(), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                // Do nothing
            }
        }
        return responseEntity;

    }

    /**
     * This method to get image from path directory Image question
     *
     * @param image
     *            name
     *
     */
    @Override
    @RequestMapping(value = "/getImageQuestion/{name}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageQuestion(@PathVariable(value = "name") final String name) {
        RandomAccessFile t = null;
        ResponseEntity<byte[]> responseEntity = null;
        try {
            if (name != null) {
                String directory = environment.getProperty("directoryImageQuestion");

                String path = directory + "/" + name + ".png";

                t = new RandomAccessFile(path, "r");
                byte[] r = new byte[(int) t.length()];
                t.readFully(r);
                responseEntity = new ResponseEntity<byte[]>(r, new HttpHeaders(), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            // e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            try {
                if (t != null) {
                    t.close();
                }
            } catch (IOException e) {
                // do nothing
            }
        }
        return responseEntity;
    }

    @Override
    @RequestMapping(value = "/addCommentEssay", method = RequestMethod.POST)
    public ResponseEntity<Response> addCommentEssay(@RequestBody final RequestData request) {

        SimpleResponse simpleResponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            String content = request.getRequest_data().getContent().replace("'", "\\\\'");
            content = content.replace("(", "\\\\(");
            content = content.replace(")", "\\\\)");

            Object[] queryParams = { request.getRequest_data().getAuthorID(), content, request.getRequest_data().getEssayId() };
            int cid = 0;
            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_ADD_COMMENT, queryParams);
            if (status) {
                List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_LAST_INSERTED_COMMENT, queryParams);
                cid = Integer.valueOf(((Map) readObject.get(0)).get("cid").toString());

                Object[] queryParamsIns = { ((Map) readObject.get(0)).get("cid").toString(), request
                    .getRequest_data()
                    .getEssayId() };
                dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_INSERT_ESSAY_COMMENT, queryParamsIns);

                readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_INFO_ESSAY, queryParamsIns);
                ((Map) readObject.get(0)).get("userId").toString();
                request.getRequest_data().getAuthorID();
                ((Map) readObject.get(0)).get("nameOfEssay").toString();
                if (!((Map) readObject.get(0)).get("userId").toString().equalsIgnoreCase(request.getRequest_data().getAuthorID())) {
                    dao.insertUpdateObject(SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION_ESSAY, queryParamsIns);
                }

                // if(flag) {
                // SibConstants.SqlMapper.SQL_="SIB_UPDATE_ARTICLE_COMMENT";
                // Object[] queryParamsUpdate = null;
                // queryParamsUpdate = new HashMap<String, String>();
                // queryParamsUpdate.put("essayId",
                // request.getRequest_data().getEssayId());
                // boolean flagUpdate =
                // dao.insertUpdateObject(SibConstants.SqlMapper.SQL_,
                // queryParamsUpdate);
                // }
            }

            simpleResponse = new SimpleResponse(
                                                "" + status,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                cid);
        } catch (Exception e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

}
