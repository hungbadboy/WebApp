package com.saatya.ws.service;

import org.springframework.http.ResponseEntity;

import com.saatya.ws.response.Response;

public interface QAService {

	public ResponseEntity<Response> getAnswersPn(String subject, String question,String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> getAnswers(String subject, String question);
	
	public ResponseEntity<Response> getLatestQuestionPn(String subjectId,String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> getLatestQuesOfCategoryPn(String subjectId, String categoryId, String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> getLatestQuestion(String subjectId);
	
	public ResponseEntity<Response> getLatestUserQuestionsPn(String subjectId, String userId,String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> getLatestAllUserQuestionsPn(String userId,String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> getLatestUserQuestions(String subjectId, String userId);
	public ResponseEntity<Response> getLatestAllUserQuestions(String userId);
	
	public ResponseEntity<Response> postQuestion(String qsHeading,String user_id,String qsDesc,String subjectId,String AskType);
	public ResponseEntity<Response> postAnswer(String questionid,String user_id,String answerText);

	public ResponseEntity<Response> getRelatedQuestionsPn(String subjectId, String qsDesc,String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> getRelatedQuestions(String subjectId, String qsDesc);
	
	
	public ResponseEntity<Response> deleteQuestion(String questionid);
	public ResponseEntity<Response> deleteAnswer(String answerid);
	public ResponseEntity<Response> viewQuestion(String question_id);
	
	public ResponseEntity<Response> voteQuestion(String question_id, String user_id);
	public ResponseEntity<Response> followQuestion(String questionid, String userId);
	
	public ResponseEntity<Response> voteAnswer(String answer_id, String user_id);
	
	
	


}
