package com.saatya.ws.service;

import org.springframework.http.ResponseEntity;

import com.saatya.ws.response.Response;




public interface VideoChatService {

	
	
	
	
	public ResponseEntity<Response> saveChatHistory(String subjectId, String senderId, String receiverId, String messageTest);
	
	public ResponseEntity<Response> saveNotes(String userId, String notesMsg);
	
	

}
