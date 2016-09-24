package com.saatya.ws.service;

import org.springframework.http.ResponseEntity;

import com.saatya.ws.response.Response;




public interface faqService {

	
	
	
	public ResponseEntity<Response> fetchFaqs();
	
	
	public ResponseEntity<Response> insertFaq(String question, String answer);
	
	
	
	

	
}
