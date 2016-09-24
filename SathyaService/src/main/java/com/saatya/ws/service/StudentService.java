package com.saatya.ws.service;

import org.springframework.http.ResponseEntity;

import com.saatya.ws.response.Response;




public interface StudentService {

	
	
	
	
	public ResponseEntity<Response> insertStudentProfile(String userId, String dateOfBirth, String gendor, String schColleDegreeId,String grade,String specialAccomplishments,String approximateFamilyIncome,String ChildhoodAspiration,String createdBy);
	
	
	public ResponseEntity<Response> editStudentProfile(String userId, String firstName,String lastName ,String password,String dateOfBirth,
			String schColleDegreeId,String grade,String specialAccomplishments,
			String potentialolleMajor,String extraCurricularActivity,String helpIn,
			String approximateFamilyIncome,
			String childhood_aspiration);
	
	

	

}
