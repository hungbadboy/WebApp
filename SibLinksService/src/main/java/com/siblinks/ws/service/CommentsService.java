package com.siblinks.ws.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;

public interface CommentsService {

	public ResponseEntity<Response> getNestedComments(RequestData comment);

	public ResponseEntity<Response> addNestedComment(RequestData comment);

	public ResponseEntity<Response> addComment(RequestData video);
	
	public ResponseEntity<Response> addCommentMobile(RequestData video);

	public ResponseEntity<Response> update(RequestData comment);

	public ResponseEntity<Response> remove(RequestData comment);

	public ResponseEntity<Response> addCommentArticle(RequestData request);

	public ResponseEntity<Response> addCommentVideoAdmission(RequestData request);

	public ResponseEntity<Response> getAllComment(RequestData request);

	public ResponseEntity<Response> deleteComment(RequestData request);

	public ResponseEntity<Response> uploadFile(MultipartFile uploadfile) throws IOException;
	
	public ResponseEntity<Response> uploadMultiFile(MultipartFile[] uploadfile) throws IOException;
	
	public ResponseEntity<byte[]> getImageComment(String name) throws IOException;
	
	public ResponseEntity<byte[]> getImageQuestion(String name) throws IOException;
	
	public ResponseEntity<Response> addCommentEssay(RequestData request);

}
