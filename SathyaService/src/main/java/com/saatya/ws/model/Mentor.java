package com.saatya.ws.model;

import java.util.List;

public class Mentor {
	
	
	private  String first_name;
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private  String last_name;
	private  String name;

	private  String mentor_id;
	public String getMentor_id() {
		return mentor_id;
	}
	public void setMentor_id(String mentor_id) {
		this.mentor_id = mentor_id;
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
	private String school;
	
	private String user_status;
	
	public String getUser_status() {
		return user_status;
	}
	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	@Override
	public String toString() {
		return "Mentor [mentor_id=" + mentor_id + ", user_id=" + user_id + ", date_of_birth=" + date_of_birth + ", gendor=" + gendor + ", sch_colle_degree_id="
				+ sch_colle_degree_id + ", year_in_college=" + year_in_college + ", special_accomplishments=" + special_accomplishments + ", tutoring_interests="
				+ tutoring_interests + ", potential_college_major=" + potential_college_major + ", childhood_aspirations=" + childhood_aspirations + ", creation_date="
				+ creation_date + ", created_by=" + created_by + ", last_update_date=" + last_update_date + ", last_updated_by=" + last_updated_by + ", attribute1=" + attribute1
				+ ", attribute2=" + attribute2 + ", attribute3=" + attribute3 + "]";
	}
	public String getSch_colle_degree_id() {
		return sch_colle_degree_id;
	}
	public void setSch_colle_degree_id(String sch_colle_degree_id) {
		this.sch_colle_degree_id = sch_colle_degree_id;
	}
	public String getYear_in_college() {
		return year_in_college;
	}
	public void setYear_in_college(String year_in_college) {
		this.year_in_college = year_in_college;
	}
	public String getSpecial_accomplishments() {
		return special_accomplishments;
	}
	public void setSpecial_accomplishments(String special_accomplishments) {
		this.special_accomplishments = special_accomplishments;
	}
	public String getTutoring_interests() {
		return tutoring_interests;
	}
	public void setTutoring_interests(String tutoring_interests) {
		this.tutoring_interests = tutoring_interests;
	}
	public String getPotential_college_major() {
		return potential_college_major;
	}
	public void setPotential_college_major(String potential_college_major) {
		this.potential_college_major = potential_college_major;
	}
	public String getChildhood_aspirations() {
		return childhood_aspirations;
	}
	public void setChildhood_aspirations(String childhood_aspirations) {
		this.childhood_aspirations = childhood_aspirations;
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
	private  String user_id;
	private  String date_of_birth;
	private  String gendor;
	private  String sch_colle_degree_id;
	private  String year_in_college;
	private  String special_accomplishments;
	private  String tutoring_interests;
	private  String potential_college_major;
	private  String childhood_aspirations;
	private  String creation_date;
	private  String created_by;
	private  String last_update_date;
	private  String last_updated_by;
	private  String attribute1;
	private  String attribute2;
	private  String attribute3;

}
