package com.saatya.ws.model;

import java.sql.Blob;

public class SubjectCategories {
	
	private String subject_category_id;
	private String subject_category_name;
	private String subject_id;
	private String active;
	private String creation_date;
	private String created_by;
	private String last_update_date;
	private String last_updated_by;
	private String attribute1;
	private String attribute2;
	private String attribute3;
	private String category_description;
	private String subject_name;
	
	public String getSubject_name() {
		return subject_name;
	}
	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}
	public String getCategory_description() {
		return category_description;
	}
	public void setCategory_description(String category_description) {
		this.category_description = category_description;
	}
	private String subcategory_description;
	private String category_icon;	
	private String subject_icon;

	public String getSubject_category_id() {
		return subject_category_id;
	}
	public void setSubject_category_id(String subject_category_id) {
		this.subject_category_id = subject_category_id;
	}
	public String getSubject_category_name() {
		return subject_category_name;
	}
	public void setSubject_category_name(String subject_category_name) {
		this.subject_category_name = subject_category_name;
	}
	
	public String getSubject_id() {
		return subject_id;
	}
	
	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
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
	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
	
	
	public String getSubcategory_description() {
		return subcategory_description;
	}
	public void setSubcategory_description(String subcategory_description) {
		this.subcategory_description = subcategory_description;
	}
	
	
	public String getCategory_icon() {
		return category_icon;
	}
	public void setCategory_icon(String category_icon) {
		this.category_icon = category_icon;
	}
	public String getSubject_icon() {
		return subject_icon;
	}
	public void setSubject_icon(String subject_icon) {
		this.subject_icon = subject_icon;
	}
	@Override
	public String toString() {
		return "SubjectCategories [subject_category_id=" + subject_category_id
				+ ", subject_category_name=" + subject_category_name
				+ ", subject_id=" + subject_id + ", active=" + active
				+ ", creation_date=" + creation_date + ", created_by="
				+ created_by + ", last_update_date=" + last_update_date
				+ ", last_updated_by=" + last_updated_by + ", attribute1="
				+ attribute1 + ", attribute2=" + attribute2 + ", attribute3="
				+ attribute3 + ", category_description=" + category_description
				+ ", subcategory_description=" + subcategory_description
				+ ", category_icon=" + category_icon + ", subject_icon="
				+ subject_icon + "]";
	}
	
	
}
