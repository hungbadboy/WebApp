package com.siblinks.ws.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;




public interface SubjectsService {

	
	public ResponseEntity<Response> listOfSubjects(RequestData video);
	public ResponseEntity<Response> listOfTopics(RequestData video);
	//public ResponseEntity<Response> getVidioInfo(RequestData video);
	public ResponseEntity<Response> listOfSubTopicsPn(RequestData video);
	
	@Secured({ "ROLE_ADMIN"})
	public ResponseEntity<Response> createSubject(RequestData video);
	@Secured({ "ROLE_ADMIN"})
	public ResponseEntity<Response> deleteSubject(RequestData video);
	
	public ResponseEntity<Response> fetchSubjects(RequestData video);
	
	public ResponseEntity<Response> listOfSubjectsWithTag(RequestData request);

    /**
     * @return
     */
    ResponseEntity<Response> getAllCategory();
}
