package com.saatya.ws.service;


import org.springframework.http.ResponseEntity;

import com.saatya.ws.Notification.Helper.NotificationInfo;
import com.saatya.ws.response.Response;




public interface NotificationService {

	

	
	
	public ResponseEntity<Response> sendEMail(String userid,String email);
	
	public ResponseEntity<Response> openchat(String mentorID,String StudentId,String regId);
	
	public ResponseEntity<Response> notifyMentorIsReady(String chatRoomId,String mentorID,String regId);
	
	public ResponseEntity<Response> readNotification(String userid);
	public ResponseEntity<Response> updateNotification(String notify_id,String active);
	public ResponseEntity<Response> insertNotification(String userid,String title_of_notify,String content_of_notify);
	
	public ResponseEntity<Response> contactUs(String name,String Email,String Subject,String Message);
	
	public ResponseEntity<Response> forgotPassword(String email);

	
	
}
