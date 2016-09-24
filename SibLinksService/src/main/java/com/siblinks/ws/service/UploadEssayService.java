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
	
	public ResponseEntity<Response> upload(String name, String userId,String userType, MultipartFile file) throws FileNotFoundException;

	public ResponseEntity<Response> getEssayByUserId(RequestData video) throws FileNotFoundException;

	public void download(String userId, String essayId, String status,HttpServletRequest request, HttpServletResponse response);
	
	public ResponseEntity<Response> removeEssay(RequestData video);
	
	public ResponseEntity<Response> postDiscussion(RequestData video);
	
	public ResponseEntity<Response> getDiscussion(RequestData video);

	public ResponseEntity<Response> getEssay(RequestData request) throws FileNotFoundException;

	public ResponseEntity<Response> getEssayById(RequestData request) throws FileNotFoundException;

	public ResponseEntity<Response> getEssayCommentsPN(RequestData request);

	public ResponseEntity<byte[]> getImageUploadEssay(String arId) throws IOException;

    public ResponseEntity<Response> getEssayProfile(long userid, long limit, long offset) throws FileNotFoundException;
}
