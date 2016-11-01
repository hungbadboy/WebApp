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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.siblinks.ws.model.Article;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.ArticleService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.RandomString;
import com.siblinks.ws.util.ReadProperties;
import com.siblinks.ws.util.SibConstants;
import com.siblinks.ws.util.StringUtil;

/**
 *
 *
 * @author hungpd
 * @version 1.0
 */
@RestController
@RequestMapping("/siblinks/services/article")
public class ArticleServiceImpl implements ArticleService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ObjectDao dao;

    /**
     * {@inheritDoc}
     * 
     * @param request
     * @return
     */
    @Override
    @RequestMapping(value = "/getArticles", method = RequestMethod.POST)
    public ResponseEntity<Response> getArticles(@RequestBody final RequestData request) {
        SimpleResponse reponse = null;
        try {
            CommonUtil util = CommonUtil.getInstance();
            Map<String, String> map = util.getLimit(request.getRequest_data_article().getPageno(), request
                .getRequest_data_article()
                .getLimit());

            Object[] queryParams = { request.getRequest_data_article().getIdAdmission(), map.get(Parameters.FROM), map
                .get(Parameters.TO) };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ARTICLES_PN, queryParams);
            // Count article
            String count = dao.getCount(SibConstants.SqlMapper.SQL_GET_ARTICLES_COUNT, queryParams);

            reponse = new SimpleResponse(
                                         SibConstants.SUCCESS,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         readObject,
                                         count);
        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(
                                         SibConstants.FAILURE,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getArticleDetail", method = RequestMethod.POST)
    public ResponseEntity<Response> getArticleDetail(@RequestBody final RequestData request) {
        SimpleResponse reponse = null;
        try {
            CommonUtil util = CommonUtil.getInstance();
            util.getLimit(request.getRequest_data_article().getPageno(), request.getRequest_data_article().getLimit());

            Object[] queryParams = { request.getRequest_data_article().getArId() };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ARTICLE_DETAIL, queryParams);
            reponse = new SimpleResponse(
                                         SibConstants.SUCCESS,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         readObject);
        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(
                                         SibConstants.FAILURE,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    @RequestMapping(value = "/getArticleCommentsPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getArticleCommentsPN(@RequestBody final RequestData request) {
        SimpleResponse reponse = null;
        try {
            CommonUtil util = CommonUtil.getInstance();

            Map<String, String> map = util.getLimit(request.getRequest_data_article().getPageno(), request
                .getRequest_data_article()
                .getLimit());

            Object[] queryParams = { request.getRequest_data_article().getArId(), map.get(Parameters.FROM), map
                .get(Parameters.TO) };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_GET_ARTICLE_COMMENTS_PN, queryParams);
            String count = dao.getCount(SibConstants.SqlMapper.SQL_SIB_GET_ARTICLE_COMMENTS_PN_COUNT, queryParams);

            reponse = new SimpleResponse(
                                         SibConstants.SUCCESS,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         readObject,
                                         count);
        } catch (Exception e) {
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
     * 
     */
    @Override
    @RequestMapping(value = "/getAllArticles", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllArticles() {
        SimpleResponse reponse = null;
        try {
            Object[] queryParams = {};

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ALL_ARTICLE, queryParams);
            reponse = new SimpleResponse(readObject);
        } catch (DAOException e) {
            logger.debug(e.getMessage());
            reponse = new SimpleResponse(SibConstants.FAILURE, "ArticleServiceImpl", "getAllArticles", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     * 
     */
    @SuppressWarnings("rawtypes")
    @Override
    @RequestMapping(value = "/deleteArticle", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteArticle(@RequestBody final RequestData request) {
        SimpleResponse reponse = null;
        try {
            Object[] queryParams = { request.getRequest_data_article().getArId() };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_IMAGE_ARTICLE, queryParams);
            File file = new File((String) ((Map) readObject.get(0)).get(Parameters.IMAGE));
            boolean flag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_ARTICLE, queryParams);
            String fileName = (String) ((Map) readObject.get(0)).get(Parameters.IMAGE);
            if (flag && !fileName.contains("default_article")) {
                file.delete();
            }

            reponse = new SimpleResponse(
                                         SibConstants.SUCCESS,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         flag);
        } catch (DAOException e) {
            e.printStackTrace();
            reponse = new SimpleResponse(
                                         SibConstants.FAILURE,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     * 
     */
    @SuppressWarnings("rawtypes")
    @Override
    @RequestMapping(value = "/updateArticle", method = RequestMethod.POST)
    public ResponseEntity<Response> updateArticle(@RequestBody final RequestData request) {
        SimpleResponse reponse = null;
        try {
            String entityName = "";
            List<Object> readObject = null;
            boolean flag;
            Article articleObj = request.getRequest_data_article();
            if (request.getRequest_data_article().getImage() != null) {
                entityName = "GET_IMAGE_ARTICLE";
                readObject = dao.readObjects(entityName, new Object[] { "" + articleObj.getArId() });

                File file = new File((String) ((Map) readObject.get(0)).get("image"));

                String fileName = (String) ((Map) readObject.get(0)).get("image");

                if (file != null && !fileName.contains("default_article")) {
                    file.delete();
                }
                Object[] queryParams = { articleObj.getTitle(), articleObj.getDescription(), articleObj.getImage(), articleObj
                    .getContent(), articleObj.getAuthorId(), articleObj.getActive(), articleObj.getArId() };
                entityName = "UPDATE_ARTICLE";
                flag = dao.insertUpdateObject(entityName, queryParams);
            } else {
                Object[] queryParams = { articleObj.getTitle(), articleObj.getDescription(), articleObj.getContent(), articleObj
                    .getAuthorId(), articleObj.getActive(), articleObj.getArId() };
                entityName = "UPDATE_ARTICLE_NOT_IMAGE";
                flag = dao.insertUpdateObject(entityName, queryParams);
            }

            reponse = new SimpleResponse(
                                         SibConstants.SUCCESS,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         flag);
        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(
                                         SibConstants.FAILURE,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    @RequestMapping(value = "/updateViewArticle", method = RequestMethod.POST)
    public ResponseEntity<Response> updateViewArticle(@RequestBody final Article article) {
        SimpleResponse reponse = null;
        try {
            if (article != null && !StringUtil.isNull(article.getArId())) {
                Object[] params = { article.getArId() };
                boolean updateSuccess = dao.insertUpdateObject(
                    SibConstants.SqlMapper.SQL_SIB_UPDATE_NUMVIEW_ARTICLE_ADMISSION,
                    params);
                if (updateSuccess) {
                    List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ARTICLE_DETAIL, params);
                    reponse = new SimpleResponse(SibConstants.SUCCESS, "article", "updateViewArticle", readObject);
                } else {
                    reponse = new SimpleResponse(
                                                 SibConstants.FAILURE,
                                                 "article",
                                                 "updateViewArticle",
                                                 "Update number view artical failure");
                }
            } else {
                reponse = new SimpleResponse(SibConstants.FAILURE, "article", "updateViewArticle", "Artical Id is not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(SibConstants.FAILURE, "article", "updateViewArticle", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> uploadFile(@RequestParam("uploadfile") final MultipartFile uploadfile) throws IOException {
        SimpleResponse reponse = null;
        BufferedOutputStream stream = null;
        try {
            String directory = ReadProperties.getProperties("directoryImageArticle");
            String name = uploadfile.getContentType();
            boolean status = name.contains("image");
            if (directory != null && status) {
                RandomString randomName = new RandomString();
                String filename = randomName.random();
                String filepath = Paths.get(directory, filename + ".png").toString();
                // Save the file locally
                stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
                stream.write(uploadfile.getBytes());
                reponse = new SimpleResponse(SibConstants.SUCCESS, filepath);

            } else {
                reponse = new SimpleResponse(
                                             SibConstants.FAILURE,
                                             "Your photos couldn't be uploaded. Photos should be saved as JPG, PNG, GIF or BMP files.");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            reponse = new SimpleResponse(SibConstants.FAILURE, e.getMessage());
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }// method uploadFile

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    @RequestMapping(value = "/createArticle", method = RequestMethod.POST)
    public ResponseEntity<Response> createArticle(@RequestBody final RequestData request) throws FileNotFoundException {
        String imageArticle = "";
        SimpleResponse reponse = null;
        try {
            if (request.getRequest_data_article().getImage() == null) {
                imageArticle = ReadProperties.getProperties("directoryImageDefaultArticle");
            } else {
                imageArticle = request.getRequest_data_article().getImage();
            }

            Object[] queryParams = { request.getRequest_data_article().getAuthorId(), request
                .getRequest_data_article()
                .getTitle(), request.getRequest_data_article().getDescription(), imageArticle, request
                .getRequest_data_article()
                .getContent(), request.getRequest_data_article().getActive(), request.getRequest_data_article().getCreateBy() };

            boolean flag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_CREATE_ARTICLE, queryParams);

            reponse = new SimpleResponse(
                                         SibConstants.SUCCESS,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         flag);
        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(
                                         SibConstants.FAILURE,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     * 
     */
    @SuppressWarnings("resource")
    @Override
    @RequestMapping(value = "/getImageArticle/{arId}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageArticle(@PathVariable(value = "arId") final String arId) throws IOException {
        RandomAccessFile t = null;
        Object[] queryParams = { arId };
        String path = null;
        ResponseEntity<byte[]> responseEntity = null;
        try {
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_IMAGE_ARTICLE, queryParams);
            if (((Map) readObject.get(0)).get(Parameters.IMAGE) != null) {
                path = ((Map) readObject.get(0)).get(Parameters.IMAGE).toString();
                // System.out.print(Paths.get("").toAbsolutePath()+path);
                path = Paths.get("").toAbsolutePath() + path;
                byte[] r = null;
                try {
                    t = new RandomAccessFile(path, "r");
                    r = new byte[(int) t.length()];
                    t.readFully(r);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                final HttpHeaders headers = new HttpHeaders();

                responseEntity = new ResponseEntity<byte[]>(r, headers, HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
        } finally {
            if (t != null) {
                t.close();
            }
        }
        return responseEntity;
    }

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    @RequestMapping(value = "/getArticleByUserPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getArticleByUserPN(@RequestBody final RequestData request) {

        SimpleResponse reponse = null;
        try {
            if (!AuthenticationFilter.isAuthed(context)) {
                reponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(reponse, HttpStatus.FORBIDDEN);
            }
            CommonUtil util = CommonUtil.getInstance();

            Map<String, String> map = util.getLimit(request.getRequest_data_article().getPageno(), request
                .getRequest_data_article()
                .getLimit());

            Object[] queryParams = { request.getRequest_data_article().getAuthorId(), map.get(Parameters.FROM), map
                .get(Parameters.TO) };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ARTICLE_BY_USER_PN, queryParams);

            String count = dao.getCount(SibConstants.SqlMapper.SQL_GET_ARTICLE_BY_USER_PN_COUNT, queryParams);

            reponse = new SimpleResponse(
                                         SibConstants.SUCCESS,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         readObject,
                                         count);
        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(
                                         SibConstants.FAILURE,
                                         request.getRequest_data_type(),
                                         request.getRequest_data_method(),
                                         e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    @RequestMapping(value = "/getArticleAdmission", method = RequestMethod.GET)
    public ResponseEntity<Response> getArticleAdmission(@RequestParam("idAdmission") final String idAdmission) {
        String method = "getArticleAdmission()";
        SimpleResponse reponse = null;
        try {
            logger.debug(method + " start");

            Object[] queryParams = { idAdmission };
            String entityName = SibConstants.SqlMapper.SQL_SIB_GET_LIST_ARTICLE_ADMISSION;

            List<Object> readObject = dao.readObjects(entityName, queryParams);

            reponse = new SimpleResponse(SibConstants.SUCCESS, readObject);

        } catch (Exception e) {
            e.printStackTrace();
            reponse = new SimpleResponse(SibConstants.FAILURE, "ArticleServiceImpl", "getArticleAdmission", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }
}
