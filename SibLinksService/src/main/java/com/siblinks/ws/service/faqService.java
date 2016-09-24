package com.siblinks.ws.service;

import org.springframework.http.ResponseEntity;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;






public interface faqService {

	
	
	
	
	
	public ResponseEntity<Response> fetchFaqs(RequestData video);

	public ResponseEntity<Response> topFetchFaqs(RequestData request);
	
	
	
	
	

	
}
