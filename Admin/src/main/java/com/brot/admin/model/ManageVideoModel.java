/**
 * 
 */
package com.brot.admin.model;

/**
 * @author viddasar
 *
 */
public class ManageVideoModel {
	private Integer subject_category_id;
	private String subject_category_name;
	private Integer video_id;
	private String video_name;
	private String description;
	private String video_link;
	private String active;
	private Integer subject_id;
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}
	/**
	 * @return the video_name
	 */
	public String getVideo_name() {
		return video_name;
	}
	/**
	 * @param video_name the video_name to set
	 */
	public void setVideo_name(String video_name) {
		this.video_name = video_name;
	}
	/**
	 * @return the video_link
	 */
	public String getVideo_link() {
		return video_link;
	}
	/**
	 * @param video_link the video_link to set
	 */
	public void setVideo_link(String video_link) {
		this.video_link = video_link;
	}
	/**
	 * @return the subject_id
	 */
	public Integer getSubject_id() {
		return subject_id;
	}
	/**
	 * @param subject_id the subject_id to set
	 */
	public void setSubject_id(Integer subject_id) {
		this.subject_id = subject_id;
	}
	/**
	 * @return the video_id
	 */
	public Integer getVideo_id() {
		return video_id;
	}
	/**
	 * @param video_id the video_id to set
	 */
	public void setVideo_id(Integer video_id) {
		this.video_id = video_id;
	}
	public Integer getSubject_category_id() {
		return subject_category_id;
	}
	public void setSubject_category_id(Integer subject_category_id) {
		this.subject_category_id = subject_category_id;
	}
	public String getSubject_category_name() {
		return subject_category_name;
	}
	public void setSubject_category_name(String subject_category_name) {
		this.subject_category_name = subject_category_name;
	}
}
