package com.brot.admin.model;

public class SubCategoryModel {
	private Integer subject_sub_category_id;
	private String subject_sub_category_name;
	private String description;
	private Integer popular_video;
	private String mentor_name;
	private Object image;
	private String video_link;
	private String email_id;
	private Integer subject_category_id;
	/**
	 * 
	 */
	public Integer getSubject_sub_category_id() {
		return subject_sub_category_id;
	}

	public void setSubject_sub_category_id(Integer subject_sub_category_id) {
		this.subject_sub_category_id = subject_sub_category_id;
	}

	public String getSubject_sub_category_name() {
		return subject_sub_category_name;
	}

	public void setSubject_sub_category_name(String subject_sub_category_name) {
		this.subject_sub_category_name = subject_sub_category_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	public String getMentor_name() {
		return mentor_name;
	}

	public void setMentor_name(String mentor_name) {
		this.mentor_name = mentor_name;
	}

	public Object getImage() {
		return image;
	}

	public void setImage(Object image) {
		this.image = image;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getVideo_link() {
		return video_link;
	}

	public void setVideo_link(String video_link) {
		this.video_link = video_link;
	}

	/**
	 * @return the subject_category_id
	 */
	public Integer getSubject_category_id() {
		return subject_category_id;
	}

	/**
	 * @param subject_category_id the subject_category_id to set
	 */
	public void setSubject_category_id(Integer subject_category_id) {
		this.subject_category_id = subject_category_id;
	}
	

	public Integer getPopular_video() {
		return popular_video;
	}

	public void setPopular_video(Integer popular_video) {
		this.popular_video = popular_video;
	}
	
}
