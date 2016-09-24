package com.saatya.ws.service;

import org.springframework.http.ResponseEntity;

import com.saatya.ws.response.Response;




public interface FeedbackService {

	
	
	
	
	public ResponseEntity<Response> insertFeedback(String heading, String description, String feedback_by);
	
	
	public ResponseEntity<Response> getFeedbacksPn(String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> getFeedbacks();
	
	
	
	

	

	

}
