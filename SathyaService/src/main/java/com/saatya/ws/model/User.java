package com.saatya.ws.model;

import java.util.List;

public class User {
	
	private String user_id;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
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
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getMentor_approved_flag() {
		return mentor_approved_flag;
	}
	public void setMentor_approved_flag(String mentor_approved_flag) {
		this.mentor_approved_flag = mentor_approved_flag;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getLast_update_date() {
		return last_update_date;
	}
	public void setLast_update_date(String last_update_date) {
		this.last_update_date = last_update_date;
	}
	public String getLast_updated_by() {
		return last_updated_by;
	}
	public void setLast_updated_by(String last_updated_by) {
		this.last_updated_by = last_updated_by;
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
	public String getAttribute1() {
		return attribute1;
	}
	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
	private String first_name;
	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", first_name=" + first_name + ", last_name=" + last_name + ", email_id=" + email_id + ", password=" + password + ", user_type="
				+ user_type + ", mentor_approved_flag=" + mentor_approved_flag + ", active=" + active + ", creation_date=" + creation_date + ", created_by=" + created_by
				+ ", last_update_date=" + last_update_date + ", last_updated_by=" + last_updated_by + ", user_status=" + user_status + ", last_activity_time=" + last_activity_time
				+ ", attribute1=" + attribute1 + "]";
	}
	private String last_name;
	private String email_id;
	private String password;
	private String user_type;
	private String mentor_approved_flag;
	private String active;
	private String creation_date;
	private String created_by;
	private String last_update_date;
	private String last_updated_by;
	private String user_status;
	private String last_activity_time;
	private String attribute1;

}
