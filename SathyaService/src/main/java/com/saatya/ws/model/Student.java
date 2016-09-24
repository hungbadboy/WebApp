package com.saatya.ws.model;

import java.util.List;

public class Student {
	
	private String student_id;
	public String getStudent_id() {
		return student_id;
	}
	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getDate_of_birth() {
		return date_of_birth;
	}
	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
	public String getGendor() {
		return gendor;
	}
	public void setGendor(String gendor) {
		this.gendor = gendor;
	}
	public String getSch_colle_degree_id() {
		return sch_colle_degree_id;
	}
	public void setSch_colle_degree_id(String sch_colle_degree_id) {
		this.sch_colle_degree_id = sch_colle_degree_id;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getSpecial_accomplishments() {
		return special_accomplishments;
	}
	public void setSpecial_accomplishments(String special_accomplishments) {
		this.special_accomplishments = special_accomplishments;
	}
	public String getApproximate_family_income() {
		return approximate_family_income;
	}
	public void setApproximate_family_income(String approximate_family_income) {
		this.approximate_family_income = approximate_family_income;
	}
	public String getChildhood_aspiration() {
		return childhood_aspiration;
	}
	public void setChildhood_aspiration(String childhood_aspiration) {
		this.childhood_aspiration = childhood_aspiration;
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
	private String user_id;
	private String date_of_birth;
	private String gendor;
	private String sch_colle_degree_id;
	private String grade;
	private String special_accomplishments;
	private String approximate_family_income;
	private String childhood_aspiration;
	private String creation_date;
	private String created_by;
	private String last_update_date;
	private String last_updated_by;
	private String attribute1;
	@Override
	public String toString() {
		return "Student [student_id=" + student_id + ", user_id=" + user_id + ", date_of_birth=" + date_of_birth + ", gendor=" + gendor + ", sch_colle_degree_id="
				+ sch_colle_degree_id + ", grade=" + grade + ", special_accomplishments=" + special_accomplishments + ", approximate_family_income=" + approximate_family_income
				+ ", childhood_aspiration=" + childhood_aspiration + ", creation_date=" + creation_date + ", created_by=" + created_by + ", last_update_date=" + last_update_date
				+ ", last_updated_by=" + last_updated_by + ", attribute1=" + attribute1 + ", attribute2=" + attribute2 + ", attribute3=" + attribute3 + "]";
	}
	private String attribute2;
	private String attribute3;

}
