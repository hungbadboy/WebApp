/**
 * 
 */
package com.brot.admin.model;

import java.util.Date;


/**
 * @author someshkandula
 * 
 * select sq.question_id question_id, sq.user_id user_id, CONCAT(su.FIRST_NAME,' ', su.LAST_NAME) username, sq.question_text question_text, sq.question_to_mentor question_to_mentor, sq.creation_date creation_date, sq.active active, sq.admin_warning admin_warning from saatya_questions sq, saatya_user su where su.user_id = sq.user_id;
 */
public class ManageQAndAnsModel {
	private int question_id;
	private int user_id;
	private String username;
	private byte[] question_text;
	private String question_to_mentor;
	private String question_heading;
	private Date creation_date;
	private String active;
	private String admin_warning;
	private String action;
	private String user_status;
	
	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getQuestion_to_mentor() {
		return question_to_mentor;
	}
	public void setQuestion_to_mentor(String question_to_mentor) {
		this.question_to_mentor = question_to_mentor;
	}
	public Date getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getAdmin_warning() {
		return admin_warning;
	}
	public void setAdmin_warning(String admin_warning) {
		this.admin_warning = admin_warning;
	}
	public byte[] getQuestion_text() {
		return question_text;
	}
	public void setQuestion_text(byte[] question_text) {
		this.question_text = question_text;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getUser_status() {
		return user_status;
	}
	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	public String getQuestion_heading() {
		return question_heading;
	}
	public void setQuestion_heading(String question_heading) {
		this.question_heading = question_heading;
	}

}
