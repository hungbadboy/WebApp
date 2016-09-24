package com.saatya.ws.model;

import java.util.List;

public class SchoolCollegeDegree {
	private String sch_colle_degree_id;
	public String getSch_colle_degree_id() {
		return sch_colle_degree_id;
	}
	public void setSch_colle_degree_id(String sch_colle_degree_id) {
		this.sch_colle_degree_id = sch_colle_degree_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}
	@Override
	public String toString() {
		return "SchoolCollegeDegree [sch_colle_degree_id=" + sch_colle_degree_id + ", name=" + name + ", type=" + type + ", address1=" + address1 + ", address2=" + address2
				+ ", city=" + city + ", state=" + state + ", zip=" + zip + ", creation_date=" + creation_date + ", created_by=" + created_by + ", last_update_date="
				+ last_update_date + ", last_updated_by=" + last_updated_by + "]";
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
	private String name;
	private String type;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String creation_date;
	private String created_by;
	private String last_update_date;
	private String last_updated_by;

}
