package com.siblinks.ws.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;
public interface PostService {

	
	/**
	 * create question
	 * 
	 * @param content
	 * @param userId
	 * @param files
	 * @param subjectId
	 * @return
	 */
	public ResponseEntity<Response> createPost(String content, String userId, MultipartFile[] files,
			String subjectId);
	
	public ResponseEntity<Response> editPost(String content, String pid, MultipartFile[] files, String subjectId,
			String oldImagePathEdited, String oldImagePath);
	
	public ResponseEntity<Response> getPostInfo(RequestData video);
	
	public ResponseEntity<Response> getPostById(RequestData video);
	
	public ResponseEntity<Response> getPostList(RequestData video);
	
	public ResponseEntity<Response> getAllQuestions(RequestData video); 
	
	public ResponseEntity<Response> getAnswerList(RequestData video);
	
	
	public ResponseEntity<Response> getPostListPN(RequestData video);
	public ResponseEntity<Response> getAnswerListPN(RequestData video);
	
	public ResponseEntity<Response> getPosts(RequestData video);
	
	public ResponseEntity<Response> getPostInfoPN(RequestData video);
	
	public ResponseEntity<Response> addPostComment(RequestData video);
	
	public ResponseEntity<Response> getPostComments(RequestData video);
	

	
	public ResponseEntity<Response> removePost(RequestData video);
	
	
	public ResponseEntity<Response> searchPosts(RequestData video);
	
	
	public ResponseEntity<Response> searchPostsWithTag(RequestData video);
	
	public ResponseEntity<Response> searchPostsInSubcategory(RequestData video);
	
	
	
	public ResponseEntity<Response> postAnswer(RequestData video);
	public ResponseEntity<Response> editAnswer(RequestData video);
	public ResponseEntity<Response> removeAnswer(RequestData video);
	
	public ResponseEntity<Response> addAnswerComment(RequestData video);
	
	public ResponseEntity<Response> searchPostWithAnswersComments(RequestData video);
	
	public ResponseEntity<Response> getStudentPostsPN(RequestData sid);

	public ResponseEntity<Response> getAnswerById(RequestData request);

	public ResponseEntity<Response> createAnswer(RequestData request);

	public ResponseEntity<Response> updateViewQuestion(RequestData request);

	public ResponseEntity<Response> getPostListSubjectPN(RequestData request);

	public ResponseEntity<Response> getPostListRelatedPN(RequestData request);

	public ResponseEntity<Response> getSubjectIdWithTopicId(RequestData request);
	
	public ResponseEntity<Response> getStudentPosted(long id, String limit, String offset, String orderType,
			String oldQid,String subjectid);

	public ResponseEntity<Response> countQuestions(long id, String orderType,String subjectid);
	
	public ResponseEntity<Response> getAnswerByQid(RequestData request);

	ResponseEntity<Response> getPostListMobile(long id, String limit, String offset, String orderType,
			String subjectid);


}
