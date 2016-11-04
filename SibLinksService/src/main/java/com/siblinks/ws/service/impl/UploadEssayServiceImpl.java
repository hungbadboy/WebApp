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
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
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

import com.siblinks.ws.Notification.Helper.FireBaseNotification;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.filter.AuthenticationFilter;
import com.siblinks.ws.model.ActivityLogData;
import com.siblinks.ws.model.Download;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.ActivityLogService;
import com.siblinks.ws.service.UploadEssayService;
import com.siblinks.ws.service.UserService;
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

    @Autowired
    ActivityLogService activiLogService;

    @Autowired
    private UserService userservice;

    @Autowired
    private FireBaseNotification fireBaseNotification;

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam("essayId") final String essayId, final String type, final HttpServletRequest request, final HttpServletResponse response) {

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
            e.printStackTrace();
            logger.error(e, e.getCause());
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
                    dataMap.put("downloadYourEssay", directory + "?userId=" + uid + "&essayId=" + uploadEssayId + "&status=" + status);
                    if (!"W".equalsIgnoreCase(status)) {
                        dataMap.put("downloadYourReview", directory + "?userId=" + uid + "&essayId=" + uploadEssayId + "&status=" + status);
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
                                                "" +
                                                true,
                                                request.getRequest_data_type(),
                                                request.getRequest_data_method(),
                                                readObject,
                                                count);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e.getCause());
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
        boolean status = true;
        String statusMessage = null;
        try {

            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            if (!file.isEmpty()) {
                ResponseEntity<Response> msg = uploadFile(file);

                String urlFile = (String) msg.getBody().getRequest_data_result();
                String review = env.getProperty("directoryReviewDefaultUploadEssay");

                if (msg.getBody().getStatus() == "true") {

                    boolean msgs = true;
                    if ("S".equalsIgnoreCase(userType)) {
                        Object[] queryParams = { userId, name, file.getOriginalFilename(), "" + file.getSize(), file.getContentType(), urlFile, review };
                        msgs = dao.upload(SibConstants.SqlMapper.SQL_STUDENT_UPLOAD, queryParams, file);
                    } else if ("M".equalsIgnoreCase(userType)) {
                        Object[] queryParamsM = { "" + userId, file.getSize() };
                        dao.upload(SibConstants.SqlMapper.SQL_MENTOR_UPLOAD, queryParamsM, file);
                    }
                    if (msgs) {
                        statusMessage = "Done";
                    } else {
                        status = false;
                        statusMessage = "You failed to upload ";
                    }
                } else {
                    status = false;
                    statusMessage = (String) msg.getBody().getRequest_data_result();
                }
            } else {
                status = false;
                statusMessage = "You failed to upload " + name + " because the file was empty.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
            statusMessage = "You failed to upload " + name + " => " + e.getMessage();
            logger.error(e.getMessage(), e.getCause());
        }

        simpleResponse = new SimpleResponse("" + status, "essay", "upload", statusMessage);
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/uploadEssayStudent", method = RequestMethod.POST)
    public ResponseEntity<Response> uploadEssayStudent(@RequestParam("desc") final String desc, @RequestParam("userId") final String userId,
            @RequestParam("fileName") final String fileName, @RequestParam("title") final String title, @RequestParam("schoolId") final String schoolId,
            @RequestParam("majorId") final String majorId, @RequestParam("file") final MultipartFile file) {
        SimpleResponse simpleResponse = null;
        String statusMessage = "";
        boolean status = true;
        try {

            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            statusMessage = validateEssay(file);
            if (StringUtil.isNull(desc)) {
                statusMessage = "Essay description can't blank!";
            } else {
                if (desc.length() > 1000) {
                    statusMessage = "Essay description can't over 1000 characters!";
                }
            }

            if (StringUtil.isNull(title)) {
                statusMessage = "Essay title can't blank!";
            } else {
                if (title.length() > 250) {
                    statusMessage = "Essay title can't over 250 characters!";
                }
            }
            if (StringUtil.isNull(statusMessage)) {

                boolean msgs = true;
                Object[] queryParams = { userId, file.getInputStream(), desc, file.getContentType(), fileName, title, file.getSize(), schoolId, majorId };
                msgs = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_STUDENT_UPLOAD_ESSAY, queryParams);
                if (msgs) {
                    statusMessage = "Done";
                } else {
                    status = false;
                    statusMessage = "You failed to upload ";
                }

            } else {
                status = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
            statusMessage = "You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage();
            logger.error(e.getMessage(), e.getCause());
        }

        simpleResponse = new SimpleResponse("" + status, "essay", "upload", statusMessage);
        return new ResponseEntity<Response>(simpleResponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/updateEssayStudent", method = RequestMethod.POST)
    public ResponseEntity<Response> updateEssayStudent(@RequestParam("essayId") final String essayId, @RequestParam("desc") final String desc,
            @RequestParam("userId") final String userId, @RequestParam(required = false) final String fileName, @RequestParam("title") final String title,
            @RequestParam("schoolId") final String schoolId, @RequestParam("majorId") final String majorId,
            @RequestParam(required = false) final MultipartFile file) {
        SimpleResponse simpleResponse = null;
        String statusMessage = "";
        boolean status = true;
        try {

            if (!AuthenticationFilter.isAuthed(context)) {
                simpleResponse = new SimpleResponse(SibConstants.FAILURE, "Authentication required.");
                return new ResponseEntity<Response>(simpleResponse, HttpStatus.FORBIDDEN);
            }

            if (StringUtil.isNull(desc)) {
                statusMessage = "Essay description can't blank!";
            } else {
                if (desc.length() > 1000) {
                    statusMessage = "Essay description can't over 1000 characters!";
                }
            }

            if (StringUtil.isNull(title)) {
                statusMessage = "Essay title can't blank!";
            } else {
                if (title.length() > 250) {
                    statusMessage = "Essay title can't over 250 characters!";
                }
            }
            if (StringUtil.isNull(essayId)) {
                statusMessage = "EssayId null!";
            }
            statusMessage = validateEssay(file);
            boolean msgs = true;
            if (StringUtil.isNull(statusMessage)) {
                Object[] queryParams = { file.getInputStream(), desc, file.getContentType(), fileName, title, file.getSize(), schoolId, majorId, essayId };
                msgs = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_STUDENT_UPDATE_ESSAY, queryParams);
                if (msgs) {
                    statusMessage = "Done";
                } else {
                    status = false;
                    statusMessage = "You failed to upload ";
                }

            } else {
                status = false;
                if (statusMessage.equals("File is empty")) {
                    status = true;
                    statusMessage = "";
                    Object[] queryParams = { desc, title, schoolId, majorId, essayId };
                    msgs = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_STUDENT_UPDATE_ESSAY_NOFILE, queryParams);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
            statusMessage = "You failed to upload " + e.getMessage();
            logger.error(e.getMessage(), e.getCause());
        }

        simpleResponse = new SimpleResponse("" + status, "essay", "upload", statusMessage);
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
            boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_REMOVE_ESAY, queryParams);
            String message = null;
            if (status) {
                message = "Done";
            } else {
                message = "Fail";
            }

            simpleResponse = new SimpleResponse("" + status, request.getRequest_data_type(), request.getRequest_data_method(), message);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e.getCause());
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
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
    @RequestMapping(value = "/getEssayByStudentId", method = RequestMethod.GET)
    public ResponseEntity<Response> getEssayByStudentId(final String userId, final String limit, final String offset,
            final String totalCountFlag) {
        SimpleResponse simpleResponse = null;
        try {
            Object[] queryParams = { userId };
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
                    String uid = userId;
                    dataMap.put("downloadYourEssay", directory + "?userId=" + uid + "&essayId=" + uploadEssayId + "&status=W");
                    if (!"W".equalsIgnoreCase(status)) {
                        dataMap.put("downloadYourReview", directory + "?userId=" + uid + "&essayId=" + uploadEssayId + "&status=A");
                    }
                }
            }

            String count = null;
            if ("true".equalsIgnoreCase(totalCountFlag)) {
                count = dao.getCount(SibConstants.SqlMapper.SQL_GET_ALL_ESSAY_STUDENT_COUNT, queryParams);
            }

            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getEssayByStudentId", readObject, count);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e.getCause());
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, "essay", "getEssayByStudentId", e.getMessage());
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

            simpleResponse = new SimpleResponse(SibConstants.SUCCESS, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e.getCause());
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
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

            simpleResponse = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), readObject, count);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e.getCause());
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
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

            simpleResponse = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), readObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e.getCause());
            simpleResponse = new SimpleResponse(SibConstants.FAILURE, request.getRequest_data_type(), request.getRequest_data_method(), e.getMessage());
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
            logger.debug(e.getMessage(), e.getCause());
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
    public ResponseEntity<Response> getEssayProfile(@RequestParam final long userid, @RequestParam final long limit, @RequestParam final long offset) {
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
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
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
                if (isUTF8MisInterpreted(name, "Windows-1252")) {
                    String nameExt = FilenameUtils.getExtension(name.toLowerCase());
                    boolean status = sample.contains(nameExt);
                    if (!status) {
                        return "Error Format";
                    }
                } else {
                    error = "File name is not valid";
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

    private boolean isUTF8MisInterpreted(final String input, final String encoding) {
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        CharsetEncoder encoder = Charset.forName(encoding).newEncoder();
        ByteBuffer tmp;
        try {
            tmp = encoder.encode(CharBuffer.wrap(input));
        } catch (CharacterCodingException e) {
            return false;
        }
        try {
            decoder.decode(tmp);
            return true;
        } catch (CharacterCodingException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getNewestEssay", method = RequestMethod.GET)
    public ResponseEntity<Response> getNewestEssay(final long userid, final Integer schoolId, final int limit, final int offset) {
        SimpleResponse reponse = null;
        try {
            if (schoolId != null && schoolId > 0) {
                reponse = getEssay(SibConstants.SqlMapperBROT163.SQL_GET_NEWEST_ESSAY, userid, schoolId, limit, offset, "getNewestEssay");
            } else {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getNewestEssay", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e.getCause());
            reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getNewestEssay", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getProcessingEssay", method = RequestMethod.GET)
    public ResponseEntity<Response> getProcessingEssay(final long userid, final Integer schoolId, final int limit, final int offset) {
        SimpleResponse reponse = null;
        try {
            if (schoolId != null && schoolId > 0) {
                reponse = getEssay(SibConstants.SqlMapperBROT163.SQL_GET_PROCESSING_ESSAY, userid, schoolId, limit, offset, "getProcessingEssay");
            } else {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getProcessingEssay", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e.getCause());
            reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getProcessingEssay", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getIgnoredEssay", method = RequestMethod.GET)
    public ResponseEntity<Response> getInoredEssay(final long userid, final Integer schoolId, final int limit, final int offset) {
        SimpleResponse reponse = null;
        try {
            if (schoolId != null && schoolId > 0) {
                reponse = getEssay(SibConstants.SqlMapperBROT163.SQL_GET_IGNORED_ESSAY, userid, schoolId, limit, offset, "getIgnoredEssay");
            } else {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getIgnoredEssay", SibConstants.NO_DATA);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e.getCause());
            reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getIgnoredEssay", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getRepliedEssay", method = RequestMethod.GET)
    public ResponseEntity<Response> getRepliedEssay(final long userid, final Integer schoolId, final int limit, final int offset) {
        SimpleResponse reponse = null;
        try {
            if (schoolId != null && schoolId > 0) {
                reponse = getEssay(SibConstants.SqlMapperBROT163.SQL_GET_REPLIED_ESSAY, userid, schoolId, limit, offset, "getRepliedEssay");
            } else {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getRepliedEssay", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e.getCause());
            reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getRepliedEssay", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
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

    private SimpleResponse getEssay(final String entityName, final long userid, final int schoolId, final int limit, final int offset, final String from) {
        SimpleResponse reponse = null;
        try {
            Object[] params = null;
            List<Object> readObject = null;

            if (entityName.equals(SibConstants.SqlMapperBROT163.SQL_GET_NEWEST_ESSAY) || entityName.equals(SibConstants.SqlMapperBROT163.SQL_GET_IGNORED_ESSAY)) {
                params = new Object[] { userid, schoolId, limit, offset };
                readObject = dao.readObjects(entityName, params);
            } else {
                params = new Object[] { schoolId, userid, limit, offset };
                readObject = dao.readObjects(entityName, params);
            }
            if (readObject != null && readObject.size() > 0) {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", from, readObject);
            } else {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", from, SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e.getCause());
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
        String essayId = request.getRequest_data().getEssayId();
        String mentorId = request.getRequest_data().getMentorId();
        try {
            if (essayId == null || essayId.isEmpty() || Integer.parseInt(essayId) == 0) {
                reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "updateStatusEssay", "Essay can not be empty");
            } else if (mentorId == null || mentorId.isEmpty() || Integer.parseInt(mentorId) == 0) {
                reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "updateStatusEssay", "Mentor can not be empty");
            } else {
                String status = request.getRequest_data().getStatus();
                Object[] params = null;
                boolean flag = false;
                if (status != null && status.equals("I")) {
                    params = new Object[] { essayId, mentorId };
                    flag = dao.insertUpdateObject(SibConstants.SqlMapperBROT163.SQL_IGNORE_ESSAY, params);
                    params = new Object[] { "W", essayId };
                    flag = dao.insertUpdateObject(SibConstants.SqlMapperBROT163.SQL_CANCEL_ESSAY, params);
                } else {
                    List<Object> readObject = dao.readObjects(SibConstants.SqlMapperBROT163.SQL_GET_STATUS_ESSAY, new Object[] { essayId });
                    String essayStatus = "";
                    if (readObject != null && readObject.size() > 0) {
                        for (int i = 0; i < readObject.size(); i++) {
                            Map<String, Object> map = (Map<String, Object>) readObject.get(i);
                            essayStatus = (String) map.get("status");
                        }
                    }
                    if (essayStatus.equals("P")) {
                        reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "updateStatusEssay", "Processed");
                    } else {
                        params = new Object[] { status, mentorId, essayId };
                        flag = dao.insertUpdateObject(SibConstants.SqlMapperBROT163.SQL_UPDATE_STATUS_ESSAY, params);
                        if (flag) {
                            activiLogService.insertActivityLog(new ActivityLogData(SibConstants.TYPE_ESSAY, "U", "You have updated essay", mentorId, essayId));
                            reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "updateStatusEssay", "Success");
                        } else {
                            reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "updateStatusEssay", "Failed");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e.getCause());
            reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "updateStatusEssay", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/insertUpdateCommentEssay", method = RequestMethod.POST)
    public ResponseEntity<Response> insertUpdateCommentEssay(@RequestParam(required = false) final MultipartFile file, @RequestParam final long essayId,
            @RequestParam final long mentorId, @RequestParam(required = false) final Long studentId, @RequestParam final String comment, @RequestParam(
                    required = false) final Long commentId, final boolean isUpdate) {
        SimpleResponse reponse = null;
        TransactionStatus status = null;
        try {
            if (comment != null && comment.length() > 1000) {
                reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "insertCommentEssay", "Content can not longer than 1000 characters");
            } else {
                boolean flag = false;
                Object[] params = null;
                TransactionDefinition def = new DefaultTransactionDefinition();
                status = transactionManager.getTransaction(def);
                String statusMsg = validateEssay(file);
                if (statusMsg.equals("Error Format")) {
                    reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "insertCommentEssay", "Your file is not valid.");
                } else if (statusMsg.equals("File over 10M")) {
                    reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "insertCommentEssay", "Your file is lager than 10MB.");
                } else if (statusMsg.equals("File name is not valid")) {
                    reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "insertCommentEssay", "File name is not valid.");
                } else {
                    if (!isUpdate) {
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
                            String contentNofi = comment;
                            if (!StringUtil.isNull(comment) && comment.length() > Parameters.MAX_LENGTH_TO_NOFICATION) {
                                contentNofi = comment.substring(0, Parameters.MAX_LENGTH_TO_NOFICATION);
                            }
                            Object[] queryParamsIns3 = { mentorId, studentId, SibConstants.NOTIFICATION_TYPE_REPLY_ESSAY, SibConstants.NOTIFICATION_TITLE_REPLY_ESSAY, contentNofi, null, essayId };
                            dao.insertUpdateObject(SibConstants.SqlMapper.SQL_CREATE_NOTIFICATION, queryParamsIns3);

                            // send message fire base
                            String toTokenId = userservice.getTokenUser(String.valueOf(studentId));
                            if (!StringUtil.isNull(toTokenId)) {

                                fireBaseNotification.sendMessage(
                                    toTokenId,
                                    SibConstants.NOTIFICATION_TITLE_REPLY_ESSAY,
                                    SibConstants.TYPE_VIDEO,
                                    String.valueOf(essayId),
                                    contentNofi,
                                    SibConstants.NOTIFICATION_ICON,
                                    SibConstants.NOTIFICATION_PRIPORITY_HIGH);
                            }
                            activiLogService.insertActivityLog(new ActivityLogData(SibConstants.TYPE_ESSAY, "C", "You have replied essay", String
                                .valueOf(mentorId), String.valueOf(essayId)));
                        } else {
                            transactionManager.rollback(status);
                            reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "insertCommentEssay", "Failed");
                        }
                    } else {
                        params = new Object[] { comment, commentId };
                        dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_EDIT_COMMENT, params);

                        if (StringUtil.isNull(statusMsg)) {
                            params = new Object[] { mentorId, file.getInputStream(), file.getSize(), file.getOriginalFilename(), essayId };
                            dao.insertUpdateObject(SibConstants.SqlMapperBROT163.SQL_INSERT_COMMENT_ESSAY_WITH_FILE, params);
                        }
                    }
                    transactionManager.commit(status);
                    reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "insertCommentEssay", "Success");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
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
            e.printStackTrace();
            logger.debug(e.getMessage(), e.getCause());
            reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "getRepliedEssay", e.getMessage());
        }

        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/getSuggestionEssay", method = RequestMethod.GET)
    public ResponseEntity<Response> getSuggestionEssay(final Integer schoolId) {
        SimpleResponse reponse = null;
        try {
            if (schoolId != null && schoolId > 0) {
                List<Object> readObjects = dao.readObjects(SibConstants.SqlMapperBROT163.SQL_GET_SUGGESTION_ESSAY, new Object[] { schoolId });
                if (readObjects != null && readObjects.size() > 0) {
                    reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getSuggestionEssay", readObjects);
                } else {
                    reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getSuggestionEssay", SibConstants.NO_DATA);
                }
            } else {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getSuggestionEssay", SibConstants.NO_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e.getCause());
            reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "getSuggestionEssay", e.getMessage());
        }
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/searchEssay", method = RequestMethod.POST)
    public ResponseEntity<Response> searchEssay(@RequestBody final RequestData request) {
        String schoolId = request.getRequest_data().getSchoolId();
        String mentorId = request.getRequest_data().getMentorId();
        SimpleResponse reponse = null;
        try {
            if (schoolId == null || schoolId.isEmpty() || Integer.parseInt(schoolId) == 0) {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "searchEssay", SibConstants.NO_DATA);
            } else if (mentorId == null || mentorId.isEmpty() || Integer.parseInt(mentorId) == 0) {
                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "searchEssay", SibConstants.NO_DATA);
            } else {
                String entityString = "";
                String whereClause = "";
                String term = StringEscapeUtils.escapeJava(request.getRequest_data().getKeySearch());
                int offset = request.getRequest_data().getOffset() != null ? Integer.parseInt(request.getRequest_data().getOffset()) : 0;

                Map<String, Object> result = new HashMap<String, Object>();
                List<Object> readObjects = null;
                // search newest essay
                entityString = SibConstants.SqlMapperBROT163.SQL_SEARCH_NEWEST_ESSAY;
                whereClause = String.format("and e.nameOfEssay like '%%%s%%' order by e.uploadEssayId DESC limit %s offset %d", term, request
                    .getRequest_data()
                    .getLimit(), offset);
                readObjects = dao.readObjectsWhereClause(entityString, whereClause, new Object[] { mentorId, schoolId });
                if (readObjects != null && readObjects.size() > 0) {
                    result.put("newestEssay", readObjects);
                } else {
                    result.put("newestEssay", SibConstants.NO_DATA);
                }

                // search processing essay
                entityString = SibConstants.SqlMapperBROT163.SQL_SEARCH_PROCESSING_ESSAY;
                whereClause = String.format("and e.nameOfEssay like '%%%s%%' order by e.uploadEssayId DESC limit %s offset %d", term, request
                    .getRequest_data()
                    .getLimit(), offset);
                readObjects = dao.readObjectsWhereClause(entityString, whereClause, new Object[] { schoolId, mentorId });
                if (readObjects != null && readObjects.size() > 0) {
                    result.put("processingEssay", readObjects);
                } else {
                    result.put("processingEssay", SibConstants.NO_DATA);
                }

                // search ignored essay
                entityString = SibConstants.SqlMapperBROT163.SQL_SEARCH_IGNORED_ESSAY;
                whereClause = String.format("and e.nameOfEssay like '%%%s%%' order by e.uploadEssayId DESC limit %s offset %d", term, request
                    .getRequest_data()
                    .getLimit(), offset);
                readObjects = dao.readObjectsWhereClause(entityString, whereClause, new Object[] { mentorId, schoolId });
                if (readObjects != null && readObjects.size() > 0) {
                    result.put("ignoredEssay", readObjects);
                } else {
                    result.put("ignoredEssay", SibConstants.NO_DATA);
                }

                // search replied essay
                entityString = SibConstants.SqlMapperBROT163.SQL_SEARCH_REPLIED_ESSAY;
                whereClause = String.format("and e.nameOfEssay like '%%%s%%' order by c.cid DESC limit %s offset %d", term, request
                    .getRequest_data()
                    .getLimit(), offset);
                readObjects = dao.readObjectsWhereClause(entityString, whereClause, new Object[] { schoolId, mentorId });
                if (readObjects != null && readObjects.size() > 0) {
                    result.put("repliedEssay", readObjects);
                } else {
                    result.put("repliedEssay", SibConstants.NO_DATA);
                }

                reponse = new SimpleResponse(SibConstants.SUCCESS, "essay", "searchEssay", result);

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e.getMessage(), e.getCause());
            reponse = new SimpleResponse(SibConstants.FAILURE, "essay", "searchEssay", e.getMessage());
        }
        return new ResponseEntity<Response>(reponse, HttpStatus.OK);
    }
}
