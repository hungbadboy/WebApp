/**
 * 
 */
package com.brot.admin.model;

import java.sql.Date;

/**
 * @author viddasar
 * 
 */
public class ManageUserModel {
	private int user_id;
	private String username;
	private Date creation_date;
	private String mentor_approved_flag;
	private String active;

	/**
	 * @return the userName
	 */

	/**
	 * @return the startDate
	 */
	public String getUsername() {
		return username;
	}

	public String getMentor_approved_flag() {
		return mentor_approved_flag;
	}

	public void setMentor_approved_flag(String mentor_approved_flag) {
		this.mentor_approved_flag = mentor_approved_flag;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
}
