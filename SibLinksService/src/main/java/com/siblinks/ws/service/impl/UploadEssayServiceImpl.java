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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.Download;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.UploadEssayService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.RandomString;
import com.siblinks.ws.util.ReadProperties;
import com.siblinks.ws.util.SibConstants;

/**
 * 
 * 
 * @author hungpd
 * @version 1.0
 */
@RestController
@RequestMapping("/siblinks/services/essay")
public class UploadEssayServiceImpl implements UploadEssayService {

    private final Log logger = LogFactory.getLog(getClass());
    private static final int BUFFER_SIZE = 4096;

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ObjectDao dao;

    @Autowired
    Environment env;

    @Override
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam("userId") final String userId, @RequestParam("essayId") final String essayId,
            @RequestParam("status") final String status, final HttpServletRequest request, final HttpServletResponse response) {

        try {
            String entityName = SibConstants.SqlMapper.SQL_STUDENT_UPLOAD;
            if ("PENDING".equalsIgnoreCase(status)) {
                entityName = SibConstants.SqlMapper.SQL_STUDENT_DOWNLOAD;
            } else if ("REVIEWED".equalsIgnoreCase(status)) {
                entityName = SibConstants.SqlMapper.SQL_MENTOR_DOWNLOAD;
            }
            Object[] queryParams = { userId, essayId };

            Download file = dao.download(entityName, queryParams);

            // get MIME type of the file
            String mimeType = file.getMimeType();
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }
            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength(Integer.parseInt(file.getFilesize()));

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", file.getFileName());
            response.setHeader(headerKey, headerValue);

            // get output stream of the response
            OutputStream outStream = null;
            try {
                outStream = response.getOutputStream();
            } catch (IOException e) {
                logger.error(e);
            }
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            InputStream inputStream = file.getInputStream();
            // write bytes read from the input stream into the output stream
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                logger.error(e);
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error(e);
            }
            try {
                outStream.close();
            } catch (IOException e) {
                logger.error(e);
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @RequestMapping(value = "/getEssayByUserId", method = RequestMethod.POST)
    public ResponseEntity<Response> getEssayByUserId(@RequestBody final RequestData request) throws FileNotFoundException {

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Object[] queryParams = { request.getRequest_data().getUid(), map.get(Parameters.FROM), map.get(Parameters.TO) };

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ESAY, queryParams);
        if (readObject != null) {
            Map dataMap = null;

            String directory = env.getProperty("directoryDowloadEssay");
            for (Object obj : readObject) {
                dataMap = (Map) obj;

                String status = dataMap.get("status").toString();
                String uploadEssayId = dataMap.get("uploadEssayId").toString();
                String uid = request.getRequest_data().getUid();
                if ("PENDING".equalsIgnoreCase(status)) {
                    dataMap.put("downloadLink", directory + "?userId=" + uid + "&essayId=" + uploadEssayId + "&status=" + status);
                } else {
                    dataMap.put("downloadLink", "");
                }
            }
        }

        String count = null;
        if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
            count = dao.getCount(SibConstants.SqlMapper.SQL_GET_ESAY_COUNT, queryParams);
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + true,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject,
                                                    count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<Response> upload(@RequestParam("name") final String name, @RequestParam("userId") final String userId,
            @RequestParam("userType") final String userType, @RequestParam("file") final MultipartFile file)
            throws FileNotFoundException {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        String statusMessage = null;
        boolean status = true;
        if (!file.isEmpty()) {
            ResponseEntity<Response> msg = uploadFile(file);

            String urlFile = (String) msg.getBody().getRequest_data_result();
            String review = env.getProperty("directoryReviewDefaultUploadEssay");
            // int index = url.indexOf(" ");
            // String urlFile = url.substring(0, index);
            // String review = url.substring(index + 1, url.length());

            if (msg.getBody().getStatus() == "true") {
                try {

                    boolean msgs = true;
                    if ("S".equalsIgnoreCase(userType)) {
                        Object[] queryParams = { userId, name, file.getOriginalFilename(), "" + file.getSize(), file
                            .getContentType(), urlFile, review };
                        msgs = dao.upload(SibConstants.SqlMapper.SQL_STUDENT_UPLOAD, queryParams, file);
                    } else if ("M".equalsIgnoreCase(userType)) {
                        Object[] queryParamsM = { userId, file.getSize() + "" };
                        dao.upload(SibConstants.SqlMapper.SQL_MENTOR_UPLOAD, queryParamsM, file);
                    }
                    if (msgs) {
                        statusMessage = "Done";
                    } else {
                        status = false;
                        statusMessage = "You failed to upload ";
                    }

                } catch (Exception e) {
                    status = false;
                    statusMessage = "You failed to upload " + name + " => " + e.getMessage();
                }
            } else {
                status = false;
                statusMessage = (String) msg.getBody().getRequest_data_result();
            }
        } else {
            status = false;
            statusMessage = "You failed to upload " + name + " because the file was empty.";
        }

        SimpleResponse reponse = new SimpleResponse("" + status, "essay", "upload", statusMessage);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/postDiscussion", method = RequestMethod.POST)
    public ResponseEntity<Response> postDiscussion(@RequestBody final RequestData request) {

        String message = null;
        String msg = request.getRequest_data().getMessage();
        ;
        // msg = msg.replace("(", "\\(");
        // msg = msg.replace(")", "\\)");
        Object[] queryParams = { request.getRequest_data().getUid(), msg };

        boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_POST_DISCUSSION, queryParams);
        if (status) {
            message = "Done";
        } else {
            message = "Fail";
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + status,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    message);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @RequestMapping(value = "/getDiscussion", method = RequestMethod.POST)
    public ResponseEntity<Response> getDiscussion(@RequestBody final RequestData request) {

        String entityName = null;

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Object[] queryParams = { request.getRequest_data().getUid(), map.get("from"), map.get("to") };

        entityName = SibConstants.SqlMapper.SQL_GET_DISCUSSION;

        List<Object> readObject = null;
        readObject = dao.readObjects(entityName, queryParams);

        String count = null;
        if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
            count = dao.getCount(SibConstants.SqlMapper.SQL_GET_DISCUSSION, queryParams);
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + true,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject,
                                                    count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/removeEssay", method = RequestMethod.POST)
    public ResponseEntity<Response> removeEssay(@RequestBody final RequestData request) {

        Object[] queryParams = { request.getRequest_data().getEssayId() };

        List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_URL_FILE_ESSAY, queryParams);

        File file = new File((String) ((Map) readObject.get(0)).get("urlFile"));

        boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_REMOVE_ESAY, queryParams);

        String message = null;
        if (status) {
            file.delete();
            message = "Done";
        } else {
            message = "Fail";
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + status,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    message);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    public ResponseEntity<Response> uploadFile(final MultipartFile uploadfile) throws FileNotFoundException {

        String filename;
        String name;
        String filepath;
        String directory = ReadProperties.getProperties("directoryUploadEssay");
        String review = ReadProperties.getProperties("directoryReviewUploadEssay");
        String sample = ".doc .docx .pdf .xls .xlsx";
        String params[] = new String[4];
        name = uploadfile.getOriginalFilename();
        int index = name.indexOf(".");
        name = name.substring(index, name.length());
        name.toLowerCase();
        boolean status = sample.contains(name);
        if (directory != null && status) {
            try {
                RandomString randomName = new RandomString();
                String rdn = randomName.random();
                filename = rdn + name;
                filepath = Paths.get(directory, filename).toString();
                // Save the file locally
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
                stream.write(uploadfile.getBytes());
                stream.close();

                params[0] = "-size";
                params[1] = "160x120";
                params[2] = filepath;
                params[3] = review + rdn;
                // Thumbnailer thumb = new Thumbnailer();
                // thumb.CaptureImage(params);
                // total = filepath + " " + params[3];
            }

            catch (Exception e) {
                logger.error(e.getMessage());
                new SimpleResponse("" + false, "Fail");
                return new ResponseEntity<Response>(HttpStatus.BAD_REQUEST);
            }

            SimpleResponse reponse = new SimpleResponse("" + true, filepath);
            return new ResponseEntity<Response>(reponse, HttpStatus.OK);
        } else {
            SimpleResponse reponse = new SimpleResponse(
                                                        "" + false,
                                                        "Your file couldn't be uploaded. File should be saved as doc, docx, xls, xlsx or pdf files.");
            return new ResponseEntity<Response>(reponse, HttpStatus.OK);
        }
    }

    @Override
    @RequestMapping(value = "/getEssay", method = RequestMethod.POST)
    public ResponseEntity<Response> getEssay(@RequestBody final RequestData request) throws FileNotFoundException {

        String entityName = null;

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Object[] queryParams = null;
        if ("M".equalsIgnoreCase(request.getRequest_data().getUsertype())) {
            entityName = SibConstants.SqlMapper.SQL_GET_ALL_ESAY;
            queryParams = new Object[] { Integer.parseInt(map.get(Parameters.FROM)), Integer.parseInt(map.get(Parameters.TO)) };
        } else {
            entityName = SibConstants.SqlMapper.SQL_GET_ALL_ESSAY_STUDENT;
            queryParams = new Object[] { request.getRequest_data().getUid(), map.get(Parameters.FROM), map.get(Parameters.TO) };
        }

        List<Object> readObject = null;
        readObject = dao.readObjects(entityName, queryParams);
        if (readObject != null) {
            Map dataMap = null;
            String directory = env.getProperty("directoryDowloadEssay");
            for (Object obj : readObject) {
                dataMap = (Map) obj;

                String status = dataMap.get("status").toString();
                String uploadEssayId = dataMap.get("uploadEssayId").toString();
                String uid = request.getRequest_data().getUid();
                if ("PENDING".equalsIgnoreCase(status)) {
                    dataMap.put("downloadLink", directory + "?userId=" + uid + "&essayId=" + uploadEssayId + "&status=" + status);
                } else {
                    dataMap.put("downloadLink", "");
                }
            }
        }

        String count = null;
        if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
            if ("M".equalsIgnoreCase(request.getRequest_data().getUsertype())) {
                count = dao.getCount(SibConstants.SqlMapper.SQL_GET_ALL_ESAY_COUNT, new Object[]{});
            } else {
                count = dao.getCount(SibConstants.SqlMapper.SQL_GET_ALL_ESSAY_STUDENT_COUNT, queryParams);
            }

        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + true,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject,
                                                    count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getEssayById", method = RequestMethod.POST)
    public ResponseEntity<Response> getEssayById(@RequestBody final RequestData request) throws FileNotFoundException {

        Object[] queryParams = { request.getRequest_data().getEssayId() };

        List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ESSAY_BY_ID, queryParams);
        if (readObject != null) {
            Map dataMap = null;
            String directory = env.getProperty("directoryDowloadEssay");
            for (Object obj : readObject) {
                dataMap = (Map) obj;

                String status = dataMap.get("status").toString();
                String uploadEssayId = dataMap.get("uploadEssayId").toString();
                String uid = request.getRequest_data().getUid();
                if ("PENDING".equalsIgnoreCase(status)) {
                    dataMap.put("downloadLink", directory + "?userId=" + uid + "&essayId=" + uploadEssayId + "&status=" + status);
                } else {
                    dataMap.put("downloadLink", "");
                }
            }
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + true,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getEssayCommentsPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getEssayCommentsPN(@RequestBody final RequestData request) {

        

        // DaoFactory factory = DaoFactory.getDaoFactory();
        //

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

        Object[] queryParams = {

        request.getRequest_data().getEssayId(), map.get(Parameters.FROM), map.get(Parameters.TO) };

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_GET_ESSAY_COMMENTS_PN, queryParams);
        String count = null;
        count = dao.getCount(SibConstants.SqlMapper.SQL_SIB_GET_ESSAY_COMMENTS_PN_COUNT, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + true,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject,
                                                    count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getImageUploadEssay/{eid}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageUploadEssay(@PathVariable(value = "eid") final String eid) throws IOException {

        Object[] queryParams = { eid };
        List<Object> readObject = null;

        String path = null;

        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_IMAGE_UPLOAD_ESSAY, queryParams);
        if (((Map) readObject.get(0)).get(Parameters.URLREVIEW) != null) {
            path = ((Map) readObject.get(0)).get(Parameters.URLREVIEW).toString();

            RandomAccessFile t = null;
            byte[] r = null;
            try {
                t = new RandomAccessFile(path, "r");
                r = new byte[(int) t.length()];
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            t.readFully(r);
            final HttpHeaders headers = new HttpHeaders();

            return new ResponseEntity<byte[]>(r, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.siblinks.ws.service.UploadEssayService#getEssayProfile(long,
     * long, long)
     */
    @Override
    @RequestMapping(value = "/getEssayProfile", method = RequestMethod.GET)
    public ResponseEntity<Response> getEssayProfile(@RequestParam final long userid, @RequestParam final long limit,
            @RequestParam final long offset) throws FileNotFoundException {
        String entityName = null;

        Object[] queryParams = { userid, limit, offset };

        entityName = SibConstants.SqlMapperBROT71.SQL_GET_ESSAY;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = null;
        if (readObject != null && readObject.size() > 0) {
            reponse = new SimpleResponse("" + true, "essay", "getEssayProfile", readObject);
        } else {
            reponse = new SimpleResponse("" + true, "essay", "getEssayProfile", "No data found");
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }
}
