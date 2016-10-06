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
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.model.VideoAdmission;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.VideoAdmissionService;
import com.siblinks.ws.util.SibConstants;

/**
 *
 *
 * @author hungpd
 * @version 1.0
 */
@RestController
@RequestMapping("/siblinks/services/videoAdmission")
public class VideoAdmissionServiceImpl implements VideoAdmissionService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ObjectDao dao;

    @Autowired
    private Environment environment;

    @Override
    @RequestMapping(value = "/getVideoTuttorialAdmission", method = RequestMethod.GET)
    public ResponseEntity<Response> getVideoTuttorialAdmission(@RequestParam("idAdmission") final String idAdmission) {
        String method = "getVideoTuttorialAdmission()";

        logger.debug(method + " start");

        Object[] queryParams = { idAdmission };
        String entityName = SibConstants.SqlMapper.SQL_SIB_GET_LIST_VIDEO_TUTTORIAL_ADMISSION;

        List<Object> readObject = dao.readObjects(entityName, queryParams);

        SimpleResponse reponse = new SimpleResponse("true", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);

        logger.debug(method + " end");
        return entity;
    }
    
    @Override
    @RequestMapping(value = "/getAllVideosAdmission", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllVideosAdmission() {
        String method = "getAllVideosAdmission()";

        logger.debug(method + " start");

        String entityName = SibConstants.SqlMapper.SQL_GET_ALL_VIDEO_ADMISSION;

        List<Object> readObject = dao.readObjects(entityName, new Object[] {});

        SimpleResponse reponse = new SimpleResponse(readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);

        logger.debug(method + " end");
        return entity;
    }
    
    @Override
    @RequestMapping(value = "/deleteVideoAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteVideoAdmission(@RequestBody final RequestData request) {

        Object[] queryParams = { request.getRequest_data_videoAdmission().getvId() };

        boolean flag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_VIDEO_ADMISSION, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    flag);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }
    
    @Override
    @RequestMapping(value = "/updateVideoAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> updateVideoAdmission(@RequestBody final RequestData request) {

        VideoAdmission videoObj = request.getRequest_data_videoAdmission();
        Object[] queryParams = {videoObj.getTitle(), videoObj.getDescription(),
            videoObj.getImage(), videoObj.getYoutubeUrl(), 
            videoObj.getActive(),videoObj.getvId() };

        boolean flag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_VIDEO_ADMISSION, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    flag);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }
    
    @Override
    @RequestMapping(value = "/createVideoAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> createVideoAdmission(@RequestBody final RequestData request){

        VideoAdmission videoObj = request.getRequest_data_videoAdmission();
        Object[] queryParams = {videoObj.getAuthorId(), videoObj.getTitle(),
            videoObj.getDescription(), videoObj.getYoutubeUrl(),
            videoObj.getImage(),videoObj.getActive()};

        boolean flag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_CREATE_VIDEO_ADMISSION, queryParams);

        SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    flag);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }


    /*@Override
    @RequestMapping(value = "/getVideoTopicSubAdmissionPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoTopicSubAdmissionPN(@RequestBody final RequestData request) {

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data_videoAdmission().getPageno(), request
            .getRequest_data_videoAdmission()
            .getLimit());

        Object[] queryParams = {

        request.getRequest_data_videoAdmission().getIdTopicSubAdmission(), map.get(Parameters.FROM), map.get(Parameters.TO) };

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_ADMISSION_PN, queryParams);

        String count = dao.getCount(SibConstants.SqlMapper.SQL_GET_VIDEO_ADMISSION_COUNT, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject,
                                                    count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getVideoTopicSubAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoTopicSubAdmission(@RequestBody final RequestData request) {

        Object[] queryParams = { request.getRequest_data_videoAdmission().getIdTopicSubAdmission() };

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_ADMISSION, queryParams);

        String count = dao.getCount(SibConstants.SqlMapper.SQL_GET_VIDEO_ADMISSION_COUNT, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject,
                                                    count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getVideoAdmissionDetail", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoAdmissionDetail(@RequestBody final RequestData request) {

        Object[] queryParams = { request.getRequest_data_videoAdmission().getIdSubAdmission(), request
            .getRequest_data_videoAdmission()
            .getIdTopicSubAdmission(), request.getRequest_data_videoAdmission().getvId() };

        List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_ADMISSION_DETAIL, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getVideoAdmissionCommentsPN", method = RequestMethod.POST)
    public ResponseEntity<Response> getVideoAdmissionCommentsPN(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data_videoAdmission().getPageno(), request
            .getRequest_data_videoAdmission()
            .getLimit());

        Object[] queryParams = { request.getRequest_data_videoAdmission().getvId(), map.get(Parameters.FROM), map
            .get(Parameters.TO) };

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_ADMISSION_COMMENTS_PN, queryParams);
        String count = null;
        count = dao.getCount(SibConstants.SqlMapper.SQL_SIB_GET_VIDEO_ADMISSION_COMMENTS_PN_COUNT, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject,
                                                    count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/orderVideoAdmissionPN", method = RequestMethod.POST)
    public ResponseEntity<Response> orderVideoAdmissionPN(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        CommonUtil util = CommonUtil.getInstance();

        Map<String, String> map = util.getLimit(request.getRequest_data_videoAdmission().getPageno(), request
            .getRequest_data_videoAdmission()
            .getLimit());

        Object[] queryParams = { request.getRequest_data_videoAdmission().getIdTopicSubAdmission(), map.get(Parameters.FROM), map
            .get(Parameters.TO) };

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_VIDEO_ADMISSION_VIEW_PN, queryParams);
        String count = null;
        count = dao.getCount(SibConstants.SqlMapper.SQL_GET_VIDEO_ADMISSION_COUNT, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject,
                                                    count);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/updateViewVideoAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> updateViewVideoAdmission(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Object[] queryParams = { request.getRequest_data_videoAdmission().getvId() };
        boolean status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_VIEW_VIDEO_ADMISSION, queryParams);

        String message = "";

        if (status) {
            message = "Done";
        } else {
            message = "Fail";
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    message);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/deleteVideoAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteVideoAdmission(@RequestBody final RequestData request) {

        Object[] queryParams = { request.getRequest_data_videoAdmission().getvId() };

        String entityName = SibConstants.SqlMapper.SQL_GET_IMAGE_VIDEO_ADMISSION;

        List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_IMAGE_VIDEO_ADMISSION, queryParams);

        File file = new File((String) ((Map) readObject.get(0)).get(Parameters.IMAGE));

        boolean flag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_DELETE_VIDEO_ADMISSION, queryParams);

        String fileName = (String) ((Map) readObject.get(0)).get(Parameters.IMAGE);

        if (flag && !fileName.contains("default_video")) {
            file.delete();
        }

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    flag);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> uploadFile(@RequestParam("uploadfile") final MultipartFile uploadfile) throws IOException {

        String filename;
        String name;
        String filepath;
        String directory = environment.getProperty("directoryImageVideoAdmission");

        name = uploadfile.getContentType();
        boolean status = name.contains(Parameters.IMAGE);
        if (directory != null && status) {
            try {
                // Get the filename and build the local file path (be sure that
                // the
                // application have write permissions on such directory)
                name = uploadfile.getOriginalFilename();
                int index = name.indexOf(".");
                name = name.substring(index, name.length());

                RandomString randomName = new RandomString();

                filename = randomName.random();
                filepath = Paths.get(directory, filename + ".png").toString();
                // Save the file locally
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
                stream.write(uploadfile.getBytes());
                stream.close();
            }

            catch (Exception e) {
                logger.error(e.getMessage());
                return new ResponseEntity<Response>(HttpStatus.BAD_REQUEST);
            }

            SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE, filepath);
            return new ResponseEntity<Response>(reponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(HttpStatus.NO_CONTENT);
        }
    } // method uploadFile

    @Override
    @RequestMapping(value = "/updateVideoAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> updateVideoAdmission(@RequestBody final RequestData request) {

        Object[] queryParams = { request.getRequest_data_videoAdmission().getvId(), request
            .getRequest_data_videoAdmission()
            .getTitle(), request.getRequest_data_videoAdmission().getYoutubeUrl(), request
            .getRequest_data_videoAdmission()
            .getRunningTime(), request.getRequest_data_videoAdmission().getImage(), request
            .getRequest_data_videoAdmission()
            .getDescription() };

        List<Object> readObject = null;
        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_IMAGE_VIDEO_ADMISSION, queryParams);

        File file = new File((String) ((Map) readObject.get(0)).get(Parameters.IMAGE));

        String fileName = (String) ((Map) readObject.get(0)).get(Parameters.IMAGE);

        if (file != null && !fileName.contains("default_video")) {
            file.delete();
        }

        boolean flag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_VIDEO_ADMISSION, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    flag);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/createVideoAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> createVideoAdmission(@RequestBody final RequestData request) throws FileNotFoundException {
        String imgPath;
        if (request.getRequest_data_videoAdmission().getImage() == null) {
            imgPath = environment.getProperty("directoryImageDefaultVideoAdmission");
        } else {
            imgPath = request.getRequest_data_videoAdmission().getImage();
        }

        Object[] queryParams = { "6", request.getRequest_data_videoAdmission().getTitle(), request
            .getRequest_data_videoAdmission()
            .getYoutubeUrl(), request.getRequest_data_videoAdmission().getRunningTime(), request
            .getRequest_data_videoAdmission()
            .getDescription(), request.getRequest_data_videoAdmission().getIdTopicSubAdmission(), imgPath };

        boolean flag = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_CREATE_VIDEO_ADMISSION, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    flag);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @SuppressWarnings({ "resource", "rawtypes" })
    @Override
    @RequestMapping(value = "/getImageVideoAdmission/{vId}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageVideoAdmission(@PathVariable(value = "vId") final String vId) throws IOException {

        Object[] queryParams = { vId };
        List<Object> readObject = null;

        String path = null;

        readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_IMAGE_VIDEO_ADMISSION, queryParams);
        if (((Map) readObject.get(0)).get(Parameters.IMAGE) != null) {
            path = ((Map) readObject.get(0)).get(Parameters.IMAGE).toString();

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

    @Override
    @RequestMapping(value = "/updateVideoAdmissionWatched", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Response> updateVideoAdmissionWatched(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Object[] queryParams = { request.getRequest_data_videoAdmission().getUid(), request
            .getRequest_data_videoAdmission()
            .getvId() };

        boolean status = false;
        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_CHECK_USER_WATCHED_VIDEO_ADMISSION, queryParams);

        if (readObject.size() < 1) {
            status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_UPDATE_USER_WATCHED_VIDEO_ADMISSION, queryParams);
        }

        String message = "";
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

    @Override
    @RequestMapping(value = "/getIdVideoAdmissionWatched", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Response> getIdVideoAdmissionWatched(@RequestBody final RequestData request) {

        if (!AuthenticationFilter.isAuthed(context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse(
                                                                                              "" + Boolean.FALSE,
                                                                                              "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Object[] queryParams = { request.getRequest_data_videoAdmission().getUid() };

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_ID_VIDEO_ADMISSION_USER_WATCHED, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "",
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);

        return entity;
    }

    @Override
    @RequestMapping(value = "/rateVideoAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> rateVideoAdmission(@RequestBody final RequestData request) {

        boolean status = false;
        if (!AuthenticationFilter.isAuthed(this.context)) {
            ResponseEntity<Response> entity = new ResponseEntity<Response>(
                                                                           new SimpleResponse("false", "Authentication required."),
                                                                           HttpStatus.FORBIDDEN);
            return entity;
        }

        Object[] queryParams = { request.getRequest_data_videoAdmission().getvId(), request
            .getRequest_data_videoAdmission()
            .getUid(), request.getRequest_data_videoAdmission().getRating() };

        status = dao.insertUpdateObject(SibConstants.SqlMapper.SQL_SIB_RATE_VIDEO_ADMISSION, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + status,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    request.getRequest_data_videoAdmission().getvId());
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/getRatingVideoAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> getRatingVideoAdmission(@RequestBody final RequestData request) {

        Object[] queryParams = { request.getRequest_data_videoAdmission().getvId() };

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_GET_RATING_VIDEO_ADMISSION, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    @Override
    @RequestMapping(value = "/checkUserRatingVideoAdmission", method = RequestMethod.POST)
    public ResponseEntity<Response> checkUserRatingVideoAdmission(@RequestBody final RequestData request) {

        Object[] queryParams = { request.getRequest_data_videoAdmission().getUid(), request
            .getRequest_data_videoAdmission()
            .getvId() };

        List<Object> readObject = dao.readObjects(SibConstants.SqlMapper.SQL_SIB_CHECK_RATE_VIDEO_ADMISSION, queryParams);

        SimpleResponse reponse = new SimpleResponse(
                                                    "" + Boolean.TRUE,
                                                    request.getRequest_data_type(),
                                                    request.getRequest_data_method(),
                                                    readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }*/
}
