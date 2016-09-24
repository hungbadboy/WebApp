package com.saatya.ws.model;

import java.util.List;

public class Subjects {
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
	@Override
	public String toString() {
		return "Subjects [subject_id=" + subject_id + ", subject_name=" + subject_name + ", description=" + description + ", active=" + active + ", creation_date=" + creation_date
				+ ", created_by=" + created_by + ", last_update_date=" + last_update_date + ", last_updated_by=" + last_updated_by + ", attribute1=" + attribute1 + ", attribute2="
				+ attribute2 + ", attribute3=" + attribute3 + "]";
	}
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
	private String subject_name;
	private String description;
	private String active;
	private String creation_date;
	private String created_by;
	private String last_update_date;
	private String last_updated_by;
	private String attribute1;
	private String attribute2;
	private String attribute3;
	private String image;
	

}
