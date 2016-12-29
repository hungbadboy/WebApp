package com.siblinks.ws.service;
import org.springframework.http.ResponseEntity;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;

/**
 * @author adam
 *
 */
public interface managerQAService {
	
	
	/**
	 * get question by categories and topics
	 * 
	 * @param video
	 * @return
	 */
	public ResponseEntity<Response> getListQuestionQA(RequestData video);

    /**
     * @param subjectId
     * @param userId
     * @param search
     * @param subjects
     * @return
     */
    ResponseEntity<Response> getCountQuestionAnswerByMentor(String subjectId, String userId, String search, String subjects);

}
