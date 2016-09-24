package com.saatya.ws.model;

import java.util.List;

public class UserBasicSettings {

	
	private String username_privacy; 
	private String first_name_privacy; 
	private String last_name_privacy; 
	private String email_id_privacy; 
	private String DOB_privacy; 
	private String Bio_privacy;
	

	@Override
	public String toString() {
		return "UserBasicSettings [username_privacy=" + username_privacy
				+ ", first_name_privacy=" + first_name_privacy
				+ ", last_name_privacy=" + last_name_privacy
				+ ", email_id_privacy=" + email_id_privacy + ", DOB_privacy="
				+ DOB_privacy + ", Bio_privacy=" + Bio_privacy + "]";
	}

	public String getUsername_privacy() {
		return username_privacy;
	}

	public void setUsername_privacy(String username_privacy) {
		this.username_privacy = username_privacy;
	}

	public String getFirst_name_privacy() {
		return first_name_privacy;
	}

	public void setFirst_name_privacy(String first_name_privacy) {
		this.first_name_privacy = first_name_privacy;
	}

	public String getLast_name_privacy() {
		return last_name_privacy;
	}

	public void setLast_name_privacy(String last_name_privacy) {
		this.last_name_privacy = last_name_privacy;
	}

	public String getEmail_id_privacy() {
		return email_id_privacy;
	}

	public void setEmail_id_privacy(String email_id_privacy) {
		this.email_id_privacy = email_id_privacy;
	}

	public String getDOB_privacy() {
		return DOB_privacy;
	}

	public void setDOB_privacy(String dOB_privacy) {
		DOB_privacy = dOB_privacy;
	}

	public String getBio_privacy() {
		return Bio_privacy;
	}

	public void setBio_privacy(String Bio_privacy) {
		this.Bio_privacy = Bio_privacy;
	}

	
}
