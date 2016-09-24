/**
 * 
 */
package com.brot.admin.model;

/**
 * @author viddasar
 *
 */
public class ManageSubjectModel {
	private int subject_id;
	private String subject_name;
	private String description;
	private String creation_date;
	private String active;
	
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
	 * @return the subject_name
	 */
	public String getSubject_name() {
		return subject_name;
	}
	/**
	 * @param subject_name the subject_name to set
	 */
	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}
	/**
	 * @return the creation_date
	 */
	public String getCreation_date() {
		return creation_date;
	}
	/**
	 * @param creation_date the creation_date to set
	 */
	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}
	public int getSubject_id() {
		return subject_id;
	}
	public void setSubject_id(int subject_id) {
		this.subject_id = subject_id;
	}
	
	

}
