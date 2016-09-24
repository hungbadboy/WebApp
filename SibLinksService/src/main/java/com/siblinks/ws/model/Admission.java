package com.siblinks.ws.model;

public class Admission {
	
	private String name;
	private String idAdmission;
	private String idSubAdmission;
	
	public String getIdAdmission() {
		return idAdmission;
	}

	public void setIdAdmission(String idAdmission) {
		this.idAdmission = idAdmission;
	}

	public String getIdSubAdmission() {
		return idSubAdmission;
	}

	public void setIdSubAdmission(String idSubAdmission) {
		this.idSubAdmission = idSubAdmission;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
