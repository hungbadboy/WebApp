package com.saatya.ws.model;

import java.util.List;

public class Questions {
	private String question_id;
	public String getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getQuestion_text() {
		return question_text;
	}
	public void setQuestion_text(String question_text) {
		this.question_text = question_text;
	}
	public String getViews() {
		return views;
	}
	public void setViews(String views) {
		this.views = views;
	}
	public String getQa_subject_id() {
		return qa_subject_id;
	}
	public void setQa_subject_id(String qa_subject_id) {
		this.qa_subject_id = qa_subject_id;
	}
	public String getQuestion_to_mentor() {
		return question_to_mentor;
	}
	public void setQuestion_to_mentor(String question_to_mentor) {
		this.question_to_mentor = question_to_mentor;
	}
	public String getRevised_question() {
		return revised_question;
	}
	public void setRevised_question(String revised_question) {
		this.revised_question = revised_question;
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
	private String user_id;
	@Override
	public String toString() {
		return "Questions [question_id=" + question_id + ", user_id=" + user_id + ", question_text=" + question_text + ", views=" + views + ", qa_subject_id=" + qa_subject_id
				+ ", question_to_mentor=" + question_to_mentor + ", revised_question=" + revised_question + ", creation_date=" + creation_date + ", created_by=" + created_by
				+ ", last_update_date=" + last_update_date + ", last_updated_by=" + last_updated_by + ", active=" + active + ", admin_warning=" + admin_warning + "]";
	}
	private String question_text;
	private String views;
	private String qa_subject_id;
	private String question_to_mentor;
	private String revised_question;
	private String creation_date;
	private String created_by;
	private String last_update_date;
	private String last_updated_by;
	private String active;
	private String admin_warning;
	
	
	private String question_heading;
	public String getQuestion_heading() {
		return question_heading;
	}
	public void setQuestion_heading(String question_heading) {
		this.question_heading = question_heading;
	}
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	public String getQuestion_creation_date() {
		return question_creation_date;
	}
	public void setQuestion_creation_date(String question_creation_date) {
		this.question_creation_date = question_creation_date;
	}
	public String getNo_of_answers() {
		return no_of_answers;
	}
	public void setNo_of_answers(String no_of_answers) {
		this.no_of_answers = no_of_answers;
	}
	private String email_id;
	private String question_creation_date;
	private String no_of_answers;

}
