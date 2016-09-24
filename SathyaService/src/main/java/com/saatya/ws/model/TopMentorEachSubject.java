package com.saatya.ws.model;



public class TopMentorEachSubject {
	
	private String mentor_id;
	public String getMentor_id() {
		return mentor_id;
	}
	public void setMentor_id(String mentor_id) {
		this.mentor_id = mentor_id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getCollege_name() {
		return college_name;
	}
	public void setCollege_name(String college_name) {
		this.college_name = college_name;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	
	public String getUser_status() {
		return user_status;
	}
	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	public String getLast_activity_time() {
		return last_activity_time;
	}
	public void setLast_activity_time(String last_activity_time) {
		this.last_activity_time = last_activity_time;
	}
	
	
	public String getUser_picture() {
		return user_picture;
	}
	public void setUser_picture(String user_picture) {
		this.user_picture = user_picture;
	}


	private String first_name;
	private String last_name;
	private String college_name;
	private String rating;
	private String points;
	private String user_status;
	private String last_activity_time;
	private String user_picture;
	

}
