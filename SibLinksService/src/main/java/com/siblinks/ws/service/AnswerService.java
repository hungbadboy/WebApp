package com.siblinks.ws.service;

import org.springframework.http.ResponseEntity;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;

public interface AnswerService {

	public ResponseEntity<Response> answerArticle(RequestData request);

	public ResponseEntity<Response> getAnswerArticle(RequestData request);

	public ResponseEntity<Response> editAnswer(RequestData request);

}
