package com.siblinks.ws.model;

public class VideoAdmission extends Video {
	
	private String vId;
	private String authorId;
	private String youtubeUrl;
	private String idTopicSubAdmission;
	private String idSubAdmission;
	
	public String getvId() {
		return vId;
	}
	
	public void setvId(String vId) {
		this.vId = vId;
	}
	
	public String getAuthorId() {
		return authorId;
	}
	
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getYoutubeUrl() {
		return youtubeUrl;
	}

	public void setYoutubeUrl(String youtubeUrl) {
		this.youtubeUrl = youtubeUrl;
	}

	public String getIdTopicSubAdmission() {
		return idTopicSubAdmission;
	}

	public void setIdTopicSubAdmission(String idTopicSubAdmission) {
		this.idTopicSubAdmission = idTopicSubAdmission;
	}

	public String getIdSubAdmission() {
		return idSubAdmission;
	}

	public void setIdSubAdmission(String idSubAdmission) {
		this.idSubAdmission = idSubAdmission;
	}
	
}
