package com.saatya.ws.model;


public class Answers {
	private String questioned_by;
	private String answered_by;
	private String answer_id;
	private String question_id;
	private String answer_text;
	private String user_id;
	private String revised_answer;
	private String question_heading;
	private String question_text;
	private String revised_question;
	private String email_id;
	private String views;
	private String question_creation_date;
	private String qa_subject_id;
	private String answer_creation_date;
	
	public String getAnswer_id() {
		return answer_id;
	}
	public void setAnswer_id(String answer_id) {
		this.answer_id = answer_id;
	}
	public String getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	public String getAnswer_text() {
		return answer_text;
	}
	public void setAnswer_text(String answer_text) {
		this.answer_text = answer_text;
	}
	public String getUser_id() {
		return user_id;
	}
	@Override
	public String toString() {
		return "Answers [answer_id=" + answer_id + ", question_id=" + question_id + ", answer_text=" + answer_text + ", user_id=" + user_id + ", revised_answer=" + revised_answer
				+ "]";
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getRevised_answer() {
		return revised_answer;
	}
	public void setRevised_answer(String revised_answer) {
		this.revised_answer = revised_answer;
	}
	public String getQuestion_heading() {
		return question_heading;
	}
	public void setQuestion_heading(String question_heading) {
		this.question_heading = question_heading;
	}
	public String getQuestion_text() {
		return question_text;
	}
	public void setQuestion_text(String question_text) {
		this.question_text = question_text;
	}
	public String getRevised_question() {
		return revised_question;
	}
	public void setRevised_question(String revised_question) {
		this.revised_question = revised_question;
	}
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	public String getViews() {
		return views;
	}
	public void setViews(String views) {
		this.views = views;
	}
	public String getQuestion_creation_date() {
		return question_creation_date;
	}
	public void setQuestion_creation_date(String question_creation_date) {
		this.question_creation_date = question_creation_date;
	}
	public String getQa_subject_id() {
		return qa_subject_id;
	}
	public void setQa_subject_id(String qa_subject_id) {
		this.qa_subject_id = qa_subject_id;
	}
	public String getAnswer_creation_date() {
		return answer_creation_date;
	}
	public void setAnswer_creation_date(String answer_creation_date) {
		this.answer_creation_date = answer_creation_date;
	}
	/**
	 * @return the questioned_by
	 */
	public String getQuestioned_by() {
		return questioned_by;
	}
	/**
	 * @param questioned_by the questioned_by to set
	 */
	public void setQuestioned_by(String questioned_by) {
		this.questioned_by = questioned_by;
	}
	/**
	 * @return the answered_by
	 */
	public String getAnswered_by() {
		return answered_by;
	}
	/**
	 * @param answered_by the answered_by to set
	 */
	public void setAnswered_by(String answered_by) {
		this.answered_by = answered_by;
	}

}
