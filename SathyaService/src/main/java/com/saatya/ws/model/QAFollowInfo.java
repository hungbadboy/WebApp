package com.saatya.ws.model;

import java.util.List;

public class QAFollowInfo {
	
	private String  qa_follow_id;
	public String getQa_follow_id() {
		return qa_follow_id;
	}
	public void setQa_follow_id(String qa_follow_id) {
		this.qa_follow_id = qa_follow_id;
	}
	public String getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	public String getFollow_user_id() {
		return follow_user_id;
	}
	public void setFollow_user_id(String follow_user_id) {
		this.follow_user_id = follow_user_id;
	}
	private String  question_id;
	private String  follow_user_id;
	@Override
	public String toString() {
		return "QAFollowInfo [qa_follow_id=" + qa_follow_id + ", question_id=" + question_id + ", follow_user_id=" + follow_user_id + "]";
	}

}
