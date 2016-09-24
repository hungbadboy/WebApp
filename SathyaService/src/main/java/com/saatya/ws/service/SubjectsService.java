package com.saatya.ws.service;

import org.springframework.http.ResponseEntity;

import com.saatya.ws.response.Response;




public interface SubjectsService {

	
	
	/**
	 * Fetch Outbox Service
	 * 
	 */
	 public ResponseEntity<Response> listofVideos(String pageno,String limit,String totalCountFlag);
	
	public ResponseEntity<Response> listofSubjectsPn(String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> listofSubjects();
	
	public ResponseEntity<Response> listofCategerisPn(String subject,String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> listofCategeris(String subject);
	
	public ResponseEntity<Response> listofSubCategoriesPn(String subject,String category,String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> listofSubCategories(String subject,String category);
	
	public ResponseEntity<Response> listofTopSubCategoriesPn(String subject,String category,String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> listofTopSubCategories(String subject,String category);
	
	public ResponseEntity<Response> filterSubCategoriesPn(String subject,String category,String searchtxt,String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> filterSubCategories(String subject,String category,String searchtxt);
	
	public ResponseEntity<Response> SearchCategoriesPn(String subject,String searchtxt,String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> SearchCategories(String subject,String searchtxt);
	
	public ResponseEntity<Response> getVedioInfoPn(String subject,String category,String subcategory,String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> getVedioInfo(String subject,String category,String subcategory);
	
	public ResponseEntity<Response> getVedioComments(String subject,String category,String subcategory);
	
	public ResponseEntity<Response> vedioComments(String videoId);
	
	public ResponseEntity<Response> rateVedio(String subcategory,String rating,String user);
	
	public ResponseEntity<Response> postVedioComments(String subcategory,String comments,String user); 
	
	public ResponseEntity<Response> insertSubjects(String subjectName, String description, String active);
	
	public ResponseEntity<Response> insertSubjectCategories(String subjectCategoryName, String description, String subjectId,  String active);
	
	public ResponseEntity<Response> insertSubjectSubcategories(String subjectSubCategoryName, String  description, String subjectCategoryId, String active,  String popularVideo);

	
	
	

	
}
