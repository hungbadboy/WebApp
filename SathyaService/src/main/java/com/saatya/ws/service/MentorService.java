package com.saatya.ws.service;

import org.springframework.http.ResponseEntity;

import com.saatya.ws.response.Response;




public interface MentorService {

	
	
	
	
	public ResponseEntity<Response> insertMentorProfile(String userId, String dateOfBirth, String gendor, String schColleDegreeId,String yearInCollege,String specialAccomplishments,String tutoringInterests,String potentialCollegeMajor,String childhoodAspirations,String createdBy);
	
	
	public ResponseEntity<Response> getOnlineMentorsPn(String subjectId,String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> getCurrentMentorsOfStudentPn(String userId,String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> getOnlineMentors(String subjectId);
	
	public ResponseEntity<Response> searchMentorsPn(String subjectId,String Search,String pageno,String limit,String totalCountFlag);
	public ResponseEntity<Response> searchMentors(String subjectId,String Search);
	
	public ResponseEntity<Response> topMetorEachSubject(String subjectId);
	
	// Mentor Reviewed students
	public ResponseEntity<Response> insMentorReviewdStud(String mentorId, String studentId, String notes, String rating);
	
	public ResponseEntity<Response> updMentorReviewdStud(String mentorId, String studentId, String notes, String rating);
	
	//public ResponseEntity<Response> getMentorReviewdStud(String mentorId, String studentId);
	
	public ResponseEntity<Response> getMentorReviewdStud(String studentId);
	
	public ResponseEntity<Response> getList(String order);
	
	public ResponseEntity<Response> search(String keyword,String order);
	
	
	
	
	
	

	

	

}
