package com.saatya.ws.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.saatya.ws.response.Response;




public interface UserService {

	
	
	/**
	 * Fetch Outbox Service
	 * 
	 */
	/*public Response fetchUsersAll();*/
	
	public ResponseEntity<Response> registerSN(String email, String firstname, String lastname,String usertype);
	
	public ResponseEntity<Response> register(String email, String firstname, String lastname, String password,String usertype);
	
	public ResponseEntity<Response> insert1ON1Notes(String userId, String mentorId, String notes);
	
	public ResponseEntity<Response> registerNotify(String email, String firstname, String lastname, String password,String usertype);
	
	public ResponseEntity<Response> studentActive(String userid);
	
	public ResponseEntity<Response> mentorActive(String userid);
	
	public ResponseEntity<Response> updateUsers(String userid, String firtname, String lastname, String emailid, String active);
	
	public ResponseEntity<Response> updatePassword(String emailid, String password);
	
	public ResponseEntity<Response> signupcomplete(String email, String firstname, String lastname, String grade,String password,String dob,String education,String speaccomp,String colmajor,String activities,String helpin,String familyincome,String yourdream,String username,String bio,String usertype);
	
	public ResponseEntity<Response> getProfile(String userid);

	public ResponseEntity<Response> login(HttpServletRequest request,
			HttpServletResponse response, String email, String password);
	
	public ResponseEntity<Response> logout(HttpServletRequest request, HttpServletResponse response);
	
	public ResponseEntity<Response> getLisEsayBUserId(String userId);
	

	public ResponseEntity<Response> saveUserEducationSettings(String highSchool, String grade, String specialAccomplishments, String potentialCollegeMajor, String extraCurriculumnActivities, String wantHelpIn, String userId);
	
	public ResponseEntity<Response> saveUserBasicSettings(String userName, String email, String firstName, String lastName, String dob, String bio, String userId);
	
	public ResponseEntity<Response> saveUserNotifySettings(String eassyNotes, String videoUpdates, String forumQuestions, String systemAlerts, String OneOnOnechat, String messages, String userId);
	
	public ResponseEntity<Response> updateUserEducationData(String highSchoolName, String highSchoolCity, String grade, String specialAccomplishments, String potentialCollegeMajor, String extraCurriculumnActivities, String wantHelpIn, String userId);
	
	public ResponseEntity<Response> updateUserBasicData(String userName, String email, String firstName, String lastName, String dob, String bio, String userId);
	
	
	public ResponseEntity<Response> getSettings(String userid, String type);
	
	public ResponseEntity<Response> majors();
	
	public ResponseEntity<Response> collegesOrUniversities();
	
	public ResponseEntity<Response> extracurricularActivities();
	
	public ResponseEntity<Response> tutoringInterestes();
	
}
