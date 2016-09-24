package com.saatya.ws.model;

import java.util.List;

public class UserEducationSettings {

	private String sch_colle_degree_id_privacy; 
	private String grade_privacy; 
	private String special_accomplishments_privacy; 
	private String potential_college_major_privacy; 
	private String extra_curricular_activity_privacy; 
	private String want_help_in_privacy;
	

	

	@Override
	public String toString() {
		return "UserEducationSettings [sch_colle_degree_id_privacy="
				+ sch_colle_degree_id_privacy + ", grade_privacy="
				+ grade_privacy + ", special_accomplishments_privacy="
				+ special_accomplishments_privacy
				+ ", potential_college_major_privacy="
				+ potential_college_major_privacy
				+ ", extra_curricular_activity_privacy="
				+ extra_curricular_activity_privacy + ", want_help_in_privacy="
				+ want_help_in_privacy + "]";
	}

	public String getSch_colle_degree_id_privacy() {
		return sch_colle_degree_id_privacy;
	}

	public void setSch_colle_degree_id_privacy(String sch_colle_degree_id_privacy) {
		this.sch_colle_degree_id_privacy = sch_colle_degree_id_privacy;
	}

	public String getGrade_privacy() {
		return grade_privacy;
	}

	public void setGrade_privacy(String grade_privacy) {
		this.grade_privacy = grade_privacy;
	}

	public String getSpecial_accomplishments_privacy() {
		return special_accomplishments_privacy;
	}

	public void setSpecial_accomplishments_privacy(
			String special_accomplishments_privacy) {
		this.special_accomplishments_privacy = special_accomplishments_privacy;
	}

	public String getPotential_college_major_privacy() {
		return potential_college_major_privacy;
	}

	public void setPotential_college_major_privacy(
			String potential_college_major_privacy) {
		this.potential_college_major_privacy = potential_college_major_privacy;
	}

	public String getExtra_curricular_activity_privacy() {
		return extra_curricular_activity_privacy;
	}

	public void setExtra_curricular_activity_privacy(
			String extra_curricular_activity_privacy) {
		this.extra_curricular_activity_privacy = extra_curricular_activity_privacy;
	}

	public String getWant_help_in_privacy() {
		return want_help_in_privacy;
	}

	public void setWant_help_in_privacy(String want_help_in_privacy) {
		this.want_help_in_privacy = want_help_in_privacy;
	}

	
	
}
