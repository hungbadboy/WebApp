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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.Download;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.UploadEssayService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.RandomString;
import com.siblinks.ws.util.SibConstants;
import com.siblinks.ws.util.StringUtil;

/**
 * {@link UploadEssayService}
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
    private Environment env;

    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam("essayId") final String essayId, final String type, final HttpServletRequest request,
            final HttpServletResponse response) {

        // get output stream of the response
        OutputStream outStream = null;
        InputStream inputStream = null;
        try {
            String entityName = "";
            if (type.equals("S")) {
                entityName = SibConstants.SqlMapper.SQL_STUDENT_DOWNLOAD;
            } else if (type.equals("M")) {
                entityName = SibConstants.SqlMapper.SQL_MENTOR_DOWNLOAD;
            }
            Object[] queryParams = { essayId };

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
            outStream = response.getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            inputStream = file.getInputStream();
            // write bytes read from the input stream into the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                // Do nothing
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    @RequestMapping(value = "/getEssayByUserId", method = RequestMethod.POST)
    public ResponseEntity<Response> getEssayByUserId(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
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
                    dataMap.put("downloadYourEssay", directory +
                                                     "?userId=" +
                                                     uid +
                                                     "&essayId=" +
                                                     uploadEssayId +
                                                     "&status=" +
                                                     status);
                    if (!"W".equalsIgnoreCase(status)) {
                        dataMap.put("downloadYourReview", directory +
                                                          "?userId=" +
                                                          uid +
                                                          "&essayId=" +
                                                          uploadEssayId +
                                                          "&status=" +
                                                          status);
                    } else {
                        dataMap.put("downloadYourReview", "");
                    }
                }
            }

            String count = null;
            if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
                count = dao.getCount(SibConstants.SqlMapper.SQL_GET_ESAY_COUNT, queryParams);
            }

            simpleResponse = new SimpleResponse(
                                                "" + true,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject,
                                                count);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<Response> upload(@RequestParam("name") final String name, @RequestParam("userId") final String userId,
            @RequestParam("userType") final String userType, @RequestParam("file") final MultipartFile file) {
        SimpleResponse simpleResponse = null;
        if (!AuthenticationFilter.isAuthed(context)) {
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
            return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
        }

        String statusMessage = null;
        boolean status = true;
        if (!file.isEmpty()) {
            ResponseEntity<Response> msg = uploadFile(file);

            String urlFile = (String) msg.getBody().getRequest_data_result();
            String review = env.getProperty("directoryReviewDefaultUploadEssay");

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

        simpleResponse = new SimpleResponse("" + status, "essay", "upload", statusMessage);
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/uploadEssayStudent", method = RequestMethod.POST)
    public ResponseEntity<Response> uploadEssayStudent(@RequestParam("desc") final String desc,
            @RequestParam("userId") final String userId, @RequestParam("fileName") final String fileName,
            @RequestParam("title") final String title, @RequestParam("schoolId") final String schoolId,
            @RequestParam("majorId") final String majorId, @RequestParam("file") final MultipartFile file) {
        SimpleResponse simpleResponse = null;
        if (!AuthenticationFilter.isAuthed(context)) {
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
            return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
        }

        String statusMessage = "";
        boolean status = true;
        statusMessage = validateEssay(file);
        if (StringUtil.isNull(statusMessage)) {

            try {
                boolean msgs = true;
                Object[] queryParams = { userId, file.getInputStream(), desc, file.getContentType(), fileName, title, file
                    .getSize(), schoolId, majorId };
                msgs = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_STUDENT_UPLOAD_ESSAY, queryParams);
                if (msgs) {
                    statusMessage = "Done";
                } else {
                    status = false;
                    statusMessage = "You failed to upload ";
                }

            } catch (Exception e) {
                status = false;
                statusMessage = "You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage();
            }
        } else {
            status = false;
        }

        simpleResponse = new SimpleResponse("" + status, "essay", "upload", statusMessage);
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/postDiscussion", method = RequestMethod.POST)
    public ResponseEntity<Response> postDiscussion(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            String msg = request.getRequest_data().getMessage();
            ;
            // msg = msg.replace("(", "\\(");
            // msg = msg.replace(")", "\\)");
            Object[] queryParams = { request.getRequest_data().getUid(), msg };

            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_POST_DISCUSSION, queryParams);
            String message = null;
            if (status) {
                message = "Done";
            } else {
                message = "Fail";
            }

            simpleResponse = new SimpleResponse(
                                                "" + status,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                message);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getDiscussion", method = RequestMethod.POST)
    public ResponseEntity<Response> getDiscussion(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
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

            simpleResponse = new SimpleResponse(
                                                "" + true,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject,
                                                count);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/removeEssay", method = RequestMethod.POST)
    public ResponseEntity<Response> removeEssay(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
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

            simpleResponse = new SimpleResponse(
                                                "" + status,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                message);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     *
     * @param uploadfile
     * @return
     * @throws FileNotFoundException
     */
    private ResponseEntity<Response> uploadFile(final MultipartFile uploadfile) {

        String filename = "";
        String name = "";
        String filepath = "";
        SimpleResponse simpleResponse = null;
        BufferedOutputStream stream = null;
        String directory = env.getProperty("directoryUploadEssay");
        String review = env.getProperty("directoryReviewUploadEssay");
        String sample = ".doc .docx .pdf .xls .xlsx";
        String params[] = new String[4];
        name = uploadfile.getOriginalFilename();
        int index = name.indexOf(".");
        name = name.substring(index, name.length());
        boolean status = sample.contains(name.toLowerCase());
        if (directory != null && status) {
            RandomString randomName = new RandomString();
            String rdn = randomName.random();
            try {
                filename = rdn + name;
                filepath = Paths.get(directory, filename).toString();
                // Save the file locally
                File file = new File(filepath);

                File parentDir = file.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                stream = new BufferedOutputStream(new FileOutputStream(file));
                stream.write(uploadfile.getBytes());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    // Do nothing
                }
            }

            params[0] = "-size";
            params[1] = "160x120";
            params[2] = filepath;
            params[3] = review + rdn;
            // Thumbnailer thumb = new Thumbnailer();
            // thumb.CaptureImage(params);
            // total = filepath + " " + params[3];
            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, filepath);
        } else {
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                "Your file couldn't be uploaded. File should be saved as doc, docx, xls, xlsx or pdf files.");
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getEssaybByStudentId", method = RequestMethod.POST)
    public ResponseEntity<Response> getEssaybByStudentId(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            String limit = request.getRequest_data().getLimit();
            String offset = request.getRequest_data().getOffset();
            Object[] queryParams = { request.getRequest_data().getUid() };
            String whereClause = "";
            if (!StringUtil.isNull(limit)) {
                whereClause += " LIMIT " + limit;
            }
            if (!StringUtil.isNull(offset)) {
                whereClause += " OFFSET " + offset;
            }
            List<Object> readObject = null;
            readObject = dao.readObjectsWhereClause(SibConstants.SqlMapper.SQL_GET_ALL_ESSAY_STUDENT, whereClause, queryParams);
            if (readObject != null) {
                Map<String, Object> dataMap = null;
                String directory = env.getProperty("directoryDowloadEssay");
                for (Object obj : readObject) {
                    dataMap = (Map) obj;

                    String status = dataMap.get("status").toString();
                    String uploadEssayId = dataMap.get("uploadEssayId").toString();
                    String uid = request.getRequest_data().getUid();
                    dataMap.put("downloadYourEssay", directory + "?userId=" + uid + "&essayId=" + uploadEssayId + "&status=W");
                    if (!"W".equalsIgnoreCase(status)) {
                        dataMap.put("downloadYourReview", directory +
                                                          "?userId=" +
                                                          uid +
                                                          "&essayId=" +
                                                          uploadEssayId +
                                                          "&status=A");
                    }
                }
            }

            String count = null;
            if ("true".equalsIgnoreCase(request.getRequest_data().getTotalCountFlag())) {
                count = dao.getCount(SibConstants.SqlMapper.SQL_GET_ALL_ESSAY_STUDENT_COUNT, queryParams);
            }

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject,
                                                count);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getEssayById", method = RequestMethod.POST)
    public ResponseEntity<Response> getEssayById(@RequestBody final RequestData request) {
        SimpleResponse simpleResponse = null;
        try {
            Object[] queryParams = { request.getRequest_data().getEssayId() };

            List<Object> readObject = null;
            readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ESSAY_BY_ID, queryParams);
            if (readObject != null) {
                Map<String, Object> dataMap = null;
                String directory = env.getProperty("directoryDowloadEssay");
                for (Object obj : readObject) {
                    dataMap = (Map) obj;
                    String uploadEssayId = dataMap.get("uploadEssayId").toString();
                    dataMap.put("downloadLinkS", directory + "?essayId=" + uploadEssayId + "&type=S");
                    int size = (dataMap.get("rdFilesize") != null ? (int) dataMap.get("rdFilesize") : 0);
                    if (size > 0) {
                        dataMap.put("downloadLinkM", directory + "?essayId=" + uploadEssayId + "&type=M");
                    }
                }
            }

            simpleResponse = new SimpleResponse(
                                                SibConstants.SUCCESS,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getEssayCommentsPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getEssayCommentsPN(@RequestBody final RequestData request) {

        SimpleResponse simpleResponse = null;
        try {
            CommonUtil util = CommonUtil.getInstance();

            Map<String, String> map = util.getLimit(request.getRequest_data().getPageno(), request.getRequest_data().getLimit());

            Object[] queryParams = {

            request.getRequest_data().getEssayId(), map.get(Parameters.FROM), map.get(Parameters.TO) };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_GET_ESSAY_COMMENTS_PN, queryParams);
            String count = dao.getCount(SibConstants.SqlMapper.SQL_SIB_GET_ESSAY_COMMENTS_PN_COUNT, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject,
                                                count);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getMentorEssayByUid", method = RequestMethod.POST)
    public ResponseEntity<Response> getMentorEssayByUid(@RequestBody final RequestData request) {

        SimpleResponse simpleResponse = null;
        try {

            Object[] queryParams = { request.getRequest_data().getUid() };

            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_MENTOR_ESSAY, queryParams);

            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject);
        } catch (DAOException e) {
            e.printStackTrace();
            simpleResponse = new SimpleResponse(
                                                SibConstants.FAILURE,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                e.getMessage());
        }
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getFileReivewUploadEssay/{eid}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getFileReivewUploadEssay(@PathVariable(value = "eid") final String eid) throws IOException {

        RandomAccessFile randomAccessFile = null;
        ResponseEntity<byte[]> responseEntity = null;
        try {
            // Get file Essay
            Object[] queryParams = { eid };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_IMAGE_UPLOAD_ESSAY, queryParams);
            if (((Map) readObject.get(0)).get(Parameters.URLREVIEW) != null) {

                // Read file Essay
                String path = ((Map) readObject.get(0)).get(Parameters.URLREVIEW).toString();
                randomAccessFile = new RandomAccessFile(path, "r");
                byte[] r = new byte[(int) randomAccessFile.length()];
                randomAccessFile.readFully(r);
                responseEntity = new ResponseEntity<byte[]>(r, new HttpHeaders(), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            // File essay not found
            logger.error(e.getMessage());
            responseEntity = new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException io) {
                // Do nothing
            }
        }
        return responseEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getEssayProfile", method = RequestMethod.GET)
    public ResponseEntity<Response> getEssayProfile(@RequestParam final long userid, @RequestParam final long limit,
            @RequestParam final long offset) {
        SimpleResponse reponse = null;
        try {
            // Get essay
            Object[] queryParams = { userid, limit, offset };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapperBROT71.SQL_GET_ESSAY, queryParams);

            if (!CollectionUtils.isEmpty(readObject)) {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getEssayProfile", readObject);
            } else {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getEssayProfile", SibConstants.NO_DATA);
            }
        } catch (DAOException e) {
            logger.error(e.getMessage());
            reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "getEssayProfile", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * validate essay type
     *
     * @param file
     *            File input
     * @return Name file type
     */
    private String validateEssay(final MultipartFile file) {
        String error = "";
        String name = "";
        String sample = env.getProperty("file.upload.essay.type");
        String limitSize = env.getProperty("file.upload.essay.size");
        if (file != null) {
            name = file.getOriginalFilename();
            if (!StringUtil.isNull(name)) {
                String nameExt = FilenameUtils.getExtension(name.toLowerCase());
                boolean status = sample.contains(nameExt);
                if (!status) {
                    return "Error Format";
                }
            }
            if (file.getSize() > Long.parseLong(limitSize)) {
                error = "File over 10M";
            }
        } else {
            error = "File is empty";
        }
        return error;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getNewestEssay", method = RequestMethod.GET)
    public ResponseEntity<Response> getNewestEssay(final long userid, final int schoolId, final int limit, final int offset) {
        SimpleResponse reponse = getEssay(
            SibConstants.SqlMapperBROT163.SQL_GET_NEWEST_ESSAY,
            userid,
            schoolId,
            limit,
            offset,
            "getNewestEssay");
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getProcessingEssay", method = RequestMethod.GET)
    public ResponseEntity<Response> getProcessingEssay(final long userid, final int schoolId, final int limit, final int offset) {
        SimpleResponse reponse = getEssay(
            SibConstants.SqlMapperBROT163.SQL_GET_PROCESSING_ESSAY,
            userid,
            schoolId,
            limit,
            offset,
            "getProcessingEssay");
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getIgnoredEssay", method = RequestMethod.GET)
    public ResponseEntity<Response> getInoredEssay(final long userid, final int schoolId, final int limit, final int offset) {
        SimpleResponse reponse = getEssay(
            SibConstants.SqlMapperBROT163.SQL_GET_IGNORED_ESSAY,
            userid,
            schoolId,
            limit,
            offset,
            "getIgnoredEssay");
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getRepliedEssay", method = RequestMethod.GET)
    public ResponseEntity<Response> getRepliedEssay(final long userid, final int schoolId, final int limit, final int offset) {
        SimpleResponse reponse = getEssay(
            SibConstants.SqlMapperBROT163.SQL_GET_REPLIED_ESSAY,
            userid,
            schoolId,
            limit,
            offset,
            "getRepliedEssay");
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /**
     * This method get essay by user id
     *
     * @param entityName
     *            Script SQL SELECT
     * @param userid
     *            User id
     * @param offset
     *            Offset to get
     * @param from
     *            Get from
     * @return SimpleResponse
     */

    private SimpleResponse getEssay(final String entityName, final long userid, final int schoolId, final int limit,
            final int offset, final String from) {
        SimpleResponse reponse = null;
        try {
            Object[] params = null;
            List<Object> readObject = null;

            if (entityName.equals(SibConstants.SqlMapperBROT163.SQL_GET_NEWEST_ESSAY) ||
                entityName.equals(SibConstants.SqlMapperBROT163.SQL_GET_IGNORED_ESSAY)) {
                params = new Object[] { userid, schoolId, limit, offset };
                readObject = dao.readObjects(entityName, params);
            } else {
                params = new Object[] { schoolId, limit, offset };
                readObject = dao.readObjects(entityName, params);
            }
            if (readObject != null && readObject.size() > 0) {
                reponse = new SimpleResponse("" + true, "essay", from, readObject);
            } else {
                reponse = new SimpleResponse("" + true, "essay", from, SibConstants.NO_DATA);
            }
        } catch (DAOException e) {
            logger.error(e.getMessage());
            reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "getEssay", e.getMessage());
        }
        return reponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateStatusEssay", method = RequestMethod.POST)
    public ResponseEntity<Response> updateStatusEssay(@RequestBody final RequestData request) {

        SimpleResponse reponse = null;
        try {
            String mentorId = request.getRequest_data().getMentorId();
            String status = request.getRequest_data().getStatus();
            Object[] params = null;
            boolean flag = false;
            if (status != null && status.equals("I")) {
                params = new Object[] { request.getRequest_data().getEssayId(), mentorId };
                flag = dao.insertUpdateObject(SibConstants.SqlMapperBROT163.SQL_IGNORE_ESSAY, params);
            } else if (status != null && status.equals("W")) {
                params = new Object[] { status, request.getRequest_data().getEssayId() };
                flag = dao.insertUpdateObject(SibConstants.SqlMapperBROT163.SQL_CANCEL_ESSAY, params);
            } else {
                params = new Object[] { status, mentorId, request.getRequest_data().getEssayId() };
                flag = dao.insertUpdateObject(SibConstants.SqlMapperBROT163.SQL_UPDATE_STATUS_ESSAY, params);
            }
            if (flag) {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "updateStatusEssay", "Success");
            } else {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "updateStatusEssay", "Failed");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "updateStatusEssay", e.getMessage());
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/insertCommentEssay", method = RequestMethod.POST)
    public ResponseEntity<Response> insertCommentEssay(@RequestParam(required = false) final MultipartFile file,
            @RequestParam final long essayId, @RequestParam final long mentorId, @RequestParam final String comment) {
        SimpleResponse reponse = null;
        boolean flag = false;
        Object[] params = null;
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            String statusMsg = validateEssay(file);
            if (statusMsg.equals("Error Format")) {
                reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "insertCommentEssay", "Your file is not valid.");
            } else if (statusMsg.equals("File over 10M")) {
                reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "insertCommentEssay", "Your file is lager than 10MB.");
            } else {
                params = new Object[] { "", mentorId, comment };
                long cid = dao.insertObject(SibConstants.SqlMapper.SQL_SIB_ADD_COMMENT, params);
                params = new Object[] { essayId, cid };
                flag = dao.insertUpdateObject(SibConstants.SqlMapperBROT163.SQL_INSERT_COMMENT_ESSAY_FK, params);

                if (StringUtil.isNull(statusMsg)) {
                    params = new Object[] { mentorId, file.getInputStream(), file.getSize(), file.getOriginalFilename(), essayId };
                    flag = dao.insertUpdateObject(SibConstants.SqlMapperBROT163.SQL_INSERT_COMMENT_ESSAY_WITH_FILE, params);
                } else {
                    params = new Object[] { mentorId, essayId };
                    flag = dao.insertUpdateObject(SibConstants.SqlMapperBROT163.SQL_INSERT_COMMENT_ESSAY_WITHOUT_FILE, params);
                }

                if (flag) {
                    transactionManager.commit(status);
                    reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "insertCommentEssay", "Success");
                } else {
                    transactionManager.rollback(status);
                    reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "insertCommentEssay", "Failed");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getCause());
            logger.error(e.getMessage());
            if (status != null) {
                transactionManager.rollback(status);
            }
            reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "insertCommentEssay", e.getMessage());
        }

        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getCommentEssay", method = RequestMethod.GET)
    public ResponseEntity<Response> getCommentEssay(final long essayId, final long mentorId) {
        SimpleResponse reponse = null;
        try {
            Object[] params = new Object[] { mentorId, essayId };
            List<Object> readObject = dao.readObjects(SibConstants.SqlMapperBROT163.SQL_GET_COMMENT_ESSAY, params);
            if (readObject != null && readObject.size() > 0) {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getRepliedEssay", readObject);
            } else {
                reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "getRepliedEssay", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "getRepliedEssay", e.getMessage());
        }

        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }
}
