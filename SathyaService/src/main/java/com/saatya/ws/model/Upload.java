package com.saatya.ws.model;


public class Upload {
	
	
	private String name_of_essay;
	private String description_of_essay;
	private String doc_submitted_date;
	private String status;
	private String upload_essay_id;
	public String getUpload_essay_id() {
		return upload_essay_id;
	}
	public void setUpload_essay_id(String upload_essay_id) {
		this.upload_essay_id = upload_essay_id;
	}
	public String getName_of_essay() {
		return name_of_essay;
	}
	public void setName_of_essay(String name_of_essay) {
		this.name_of_essay = name_of_essay;
	}
	public String getDescription_of_essay() {
		return description_of_essay;
	}
	public void setDescription_of_essay(String description_of_essay) {
		this.description_of_essay = description_of_essay;
	}
	public String getDoc_submitted_date() {
		return doc_submitted_date;
	}
	public void setDoc_submitted_date(String doc_submitted_date) {
		this.doc_submitted_date = doc_submitted_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
