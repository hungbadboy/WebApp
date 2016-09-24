package com.saatya.ws.model;

import java.util.List;

public class VideoChat {
	
	public String getSubject_id() {
		return subject_id;
	}
	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}
	public String getSender_id() {
		return sender_id;
	}
	public void setSender_id(String sender_id) {
		this.sender_id = sender_id;
	}
	public String getReceiver_id() {
		return receiver_id;
	}
	@Override
	public String toString() {
		return "VideoChat [subject_id=" + subject_id + ", sender_id="
				+ sender_id + ", receiver_id=" + receiver_id
				+ ", message_text=" + message_text + ", creation_date="
				+ creation_date + "]";
	}
	public void setReceiver_id(String receiver_id) {
		this.receiver_id = receiver_id;
	}
	public String getMessage_text() {
		return message_text;
	}
	public void setMessage_text(String message_text) {
		this.message_text = message_text;
	}
	public String getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}
	private String subject_id;	
	private String sender_id;	
	private String receiver_id;	
	private String message_text;
	private String creation_date;

}
