package com.siblinks.ws.service;

import org.springframework.http.ResponseEntity;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;

public interface LikeService {

    // public ResponseEntity<Response> likeComment(RequestData video);
	
    // public ResponseEntity<Response> likeCommentMobile(RequestData video);
	
    // public ResponseEntity<Response> unlikeComment(RequestData video);
	
    // public ResponseEntity<Response> likeVideo(RequestData video);
	
    // public ResponseEntity<Response> unlikeVideo(RequestData video);
	
    // public ResponseEntity<Response> likeQuestion(RequestData video);
	
	public ResponseEntity<Response> likeAnswer(RequestData video);
	
    // public ResponseEntity<Response> markVideoAsWatched(RequestData video);
	
	public ResponseEntity<Response> getPostLikeByUser(RequestData request);

    // public ResponseEntity<Response> likeCommentArticle(RequestData request);

    // public ResponseEntity<Response> likeCommentVideoAdmission(RequestData
    // request);

    // public ResponseEntity<Response> likeCommentEssay(RequestData request);
	
	
	
	
}
