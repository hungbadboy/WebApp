package com.saatya.ws.model;


public class Video {

	private String subject_id;

	public String getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}

	public String getSubject_name() {
		return subject_name;
	}

	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

	public String getSubject_sub_category_id() {
		return subject_sub_category_id;
	}

	public void setSubject_sub_category_id(String subject_sub_category_id) {
		this.subject_sub_category_id = subject_sub_category_id;
	}

	public String getVideo_id() {
		return video_id;
	}

	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}

	public String getSubject_sub_category_name() {
		return subject_sub_category_name;
	}

	public void setSubject_sub_category_name(String subject_sub_category_name) {
		this.subject_sub_category_name = subject_sub_category_name;
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

	public String getVideo_link() {
		return video_link;
	}

	public void setVideo_link(String video_link) {
		this.video_link = video_link;
	}

	private String subject_name;

	@Override
	public String toString() {
		return "Video [subject_id=" + subject_id + ", subject_name=" + subject_name + ", subject_sub_category_id=" + subject_sub_category_id + ", video_id=" + video_id
				+ ", subject_sub_category_name=" + subject_sub_category_name + ", video_name=" + video_name + ", description=" + description + ", video_link=" + video_link + "]";
	}

	private String subject_sub_category_id;
	private String video_id;
	private String subject_sub_category_name;
	private String video_name;
	private String description;
	private String video_link;
}
