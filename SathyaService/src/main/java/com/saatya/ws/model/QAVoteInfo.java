package com.saatya.ws.model;

import java.util.List;

public class QAVoteInfo {
	
	private String vote_id;
	public String getVote_id() {
		return vote_id;
	}
	public void setVote_id(String vote_id) {
		this.vote_id = vote_id;
	}
	public String getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	public String getAnswer_id() {
		return answer_id;
	}
	@Override
	public String toString() {
		return "QAVoteInfo [vote_id=" + vote_id + ", question_id=" + question_id + ", answer_id=" + answer_id + ", vote_text=" + vote_text + ", user_id=" + user_id + "]";
	}
	public void setAnswer_id(String answer_id) {
		this.answer_id = answer_id;
	}
	public String getVote_text() {
		return vote_text;
	}
	public void setVote_text(String vote_text) {
		this.vote_text = vote_text;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	private String  question_id;
	private String answer_id;
	private String vote_text;
	private String user_id;

}
