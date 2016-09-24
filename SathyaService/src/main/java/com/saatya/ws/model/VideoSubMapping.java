package com.saatya.ws.model;


public class VideoSubMapping {

	private String video_id;
	private String video_link;
	private String subject_id;
	private String video_name;
	private String description;
	private String active;
	private String counter;
	private String creation_date;
	private String created_by;
	private String last_update_date;
	private String last_updated_by;
	private String attribute1;
	private String attribute2;
	private String attribute3;
	private String subject_sub_category_name;
	private String email_id;
	private String comments;
	private String rating;
	private String first_name;
	private String last_name;
	private String subject_sub_category_id;
	private String subject_category_id;
	
	
	
	
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public String getVideo_id() {
		return video_id;
	}
	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}
	public String getVideo_link() {
		return video_link;
	}
	public void setVideo_link(String video_link) {
		this.video_link = video_link;
	}
	public String getSubject_id() {
		return subject_id;
	}
	@Override
	public String toString() {
		return "VideoSubMapping [video_id=" + video_id + ", video_link=" + video_link + ", subject_id=" + subject_id + ", video_name=" + video_name + ", description="
				+ description + ", active=" + active + ", counter=" + counter + ", creation_date=" + creation_date + ", created_by=" + created_by + ", last_update_date="
				+ last_update_date + ", last_updated_by=" + last_updated_by + ", attribute1=" + attribute1 + ", attribute2=" + attribute2 + ", attribute3=" + attribute3 + ",first_name=" 
				+ first_name + ", last_name=" + last_name + ", subject_sub_category_id=" + subject_sub_category_id + ", subject_category_id=" + subject_category_id + "]";
	}
	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}
	public String getVideo_name() {
		return video_name;
	}
	public void setVideo_name(String video_name) {
		this.video_name = video_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getCounter() {
		return counter;
	}
	public void setCounter(String counter) {
		this.counter = counter;
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
	public String getAttribute1() {
		return attribute1;
	}
	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
	public String getAttribute2() {
		return attribute2;
	}
	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
	public String getAttribute3() {
		return attribute3;
	}
	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	public String getSubject_sub_category_name() {
		return subject_sub_category_name;
	}
	public void setSubject_sub_category_name(String subject_sub_category_name) {
		this.subject_sub_category_name = subject_sub_category_name;
	}
	/**
	 * @return the first_name
	 */
	public String getFirst_name() {
		return first_name;
	}
	/**
	 * @param first_name the first_name to set
	 */
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	/**
	 * @return the last_name
	 */
	public String getLast_name() {
		return last_name;
	}
	/**
	 * @param last_name the last_name to set
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	/**
	 * @return the subject_sub_category_id
	 */
	public String getSubject_sub_category_id() {
		return subject_sub_category_id;
	}
	/**
	 * @param subject_sub_category_id the subject_sub_category_id to set
	 */
	public void setSubject_sub_category_id(String subject_sub_category_id) {
		this.subject_sub_category_id = subject_sub_category_id;
	}
	/**
	 * @return the subject_category_id
	 */
	public String getSubject_category_id() {
		return subject_category_id;
	}
	/**
	 * @param subject_category_id the subject_category_id to set
	 */
	public void setSubject_category_id(String subject_category_id) {
		this.subject_category_id = subject_category_id;
	}
	
	
	
}
