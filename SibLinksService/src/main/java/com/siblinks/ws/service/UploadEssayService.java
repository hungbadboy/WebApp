package com.siblinks.ws.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;

/**
 *
 *
 */
public interface UploadEssayService {

    public ResponseEntity<Response> upload(String name, String userId, String userType, MultipartFile file);

    public ResponseEntity<Response> getEssayByUserId(RequestData video);

    // public void download(String userId, String essayId, String
    // status,HttpServletRequest request, HttpServletResponse response);

    public void download(String essayId, String type, HttpServletRequest request, HttpServletResponse response);

	public ResponseEntity<Response> removeEssay(RequestData video);

    public ResponseEntity<Response> getEssaybByStudentId(RequestData request);

    public ResponseEntity<Response> getEssayById(RequestData request);

	public ResponseEntity<Response> getEssayCommentsPN(RequestData request);

    public ResponseEntity<Response> getEssayProfile(long userid, long limit, long offset);

    /**
     * upload essay for student
     *
     * @param desc
     * @param userId
     * @param title
     * @param schoolId
     * @param majorId
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public ResponseEntity<Response> uploadEssayStudent(String desc, String userId, String fileName, String title, String schoolId,
            String majorId, MultipartFile file) throws FileNotFoundException;

    public ResponseEntity<byte[]> getFileReivewUploadEssay(String eid) throws IOException;

    public ResponseEntity<Response> getProcessingEssay(long userid, Integer schoolId, int limit, int offset);

    public ResponseEntity<Response> getInoredEssay(long userid, Integer schoolId, int limit, int offset);

    public ResponseEntity<Response> getRepliedEssay(long userid, Integer schoolId, int limit, int offset);

    public ResponseEntity<Response> getNewestEssay(long userid, Integer schoolId, int limit, int offset);

    public ResponseEntity<Response> updateStatusEssay(RequestData request);

    public ResponseEntity<Response> insertCommentEssay(MultipartFile file, long essayId, long mentorId, String comment);

    public ResponseEntity<Response> getCommentEssay(long essayId, long mentorId);

    /**
     * get mentor information for essay
     *
     * @param request
     * @return
     */
    public ResponseEntity<Response> getMentorEssayByUid(RequestData request);

    public ResponseEntity<Response> getSuggestionEssay(Integer schoolId);

    public ResponseEntity<Response> searchEssay(RequestData request);

    public ResponseEntity<Response> updateEssayStudent(String essayId, String desc, String userId, String fileName, String title,
            String schoolId,
            String majorId, MultipartFile file);
}