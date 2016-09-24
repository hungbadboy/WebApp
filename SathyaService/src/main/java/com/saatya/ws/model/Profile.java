package com.saatya.ws.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Profile {
	
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
	public String getDate_of_birth() {
		return date_of_birth;
	}
	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSpecial_accomplishmentsList(String special_accomplishments) {
		System.out.println("special_accomplishments====================="+special_accomplishments);
		
		List<String> list=new ArrayList<String>();
		if(special_accomplishments != null && special_accomplishments.indexOf(":") != 1){
			String[] str = special_accomplishments.split(":");
			//list = Arrays.asList(str);
			for (String string : str) {
				if(string != null && !"".equals(string)){
					list.add(string);
				}
				
			}
			
		}else{
			list.add(potential_college_major);
		}
		System.out.println("list=="+list);
		this.special_accomplishmentsList =list;
	}
	public String getPotential_college_major() {
		return potential_college_major;
	}
	public void setPotential_college_majorList(String potential_college_major) {
		System.out.println("potential_college_major"+potential_college_major);
		List<String> list=new ArrayList<String>();
		if(potential_college_major != null && potential_college_major.indexOf(":") != 1){
			String[] str = potential_college_major.split(":");
			//list = Arrays.asList(str);
			for (String string : str) {
				if(string != null && !"".equals(string)){
					list.add(string);
				}
				
			}
			
		}else{
			list.add(potential_college_major);
		}
		this.potential_college_majorList = list;
	}
	public String getCollege_school_name() {
		return college_school_name;
	}
	public void setCollege_school_name(String college_school_name) {
		this.college_school_name = college_school_name;
	}
	public String getHelp_in() {
		return help_in;
	}
	public void setHelp_in(String help_in) {
		this.help_in = help_in;
	}
	public List<String> getExtra_curricular_activityList() {
		return extra_curricular_activityList;
	}
	public void setExtra_curricular_activityList(String extra_curricular_activity) {
		System.out.println("extra_curricular_activity=="+extra_curricular_activity);
		List<String> list=new ArrayList<String>();
		if(extra_curricular_activity != null && extra_curricular_activity.indexOf(":") != 1){
			String[] str = extra_curricular_activity.split(":");
			//list = Arrays.asList(str);
			for (String string : str) {
				if(string != null && !"".equals(string)){
					list.add(string);
				}
				
			}
			
		}else{
			list.add(potential_college_major);
		}
		this.extra_curricular_activityList = list;
	}
	private String first_name;
	private String last_name;
	private String date_of_birth;
	private String name;
	private List<String> special_accomplishmentsList;
	private List<String> potential_college_majorList; 
	private String college_school_name;
	private String help_in;
	private List<String> extra_curricular_activityList;
	private String user_status;
	private String member_since;
	private String grade;
	private String subject_points;
	private String about_me;
	
	
	public List<String> getSpecial_accomplishmentsList() {
		return special_accomplishmentsList;
	}
	
	public List<String> getPotential_college_majorList() {
		return potential_college_majorList;
	}
	
	
	
	public String getUser_status() {
		return user_status;
	}
	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	public String getMember_since() {
		return member_since;
	}
	public void setMember_since(String member_since) {
		this.member_since = member_since;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getSubject_points() {
		return subject_points;
	}
	public void setSubject_points(String subject_points) {
		this.subject_points = subject_points;
	}
	public String getAbout_me() {
		return about_me;
	}
	public void setAbout_me(String about_me) {
		this.about_me = about_me;
	}
	public void setSpecial_accomplishmentsList(
			List<String> special_accomplishmentsList) {
		this.special_accomplishmentsList = special_accomplishmentsList;
	}
	public void setPotential_college_majorList(
			List<String> potential_college_majorList) {
		this.potential_college_majorList = potential_college_majorList;
	}
	public void setExtra_curricular_activityList(
			List<String> extra_curricular_activityList) {
		this.extra_curricular_activityList = extra_curricular_activityList;
	}
	private String potential_college_major; 
	
	public String getExtra_curricular_activity() {
		return extra_curricular_activity;
	}
	public void setExtra_curricular_activity(String extra_curricular_activity) {
		this.extra_curricular_activity = extra_curricular_activity;
	}
	public String getSpecial_accomplishments() {
		return special_accomplishments;
	}
	public void setSpecial_accomplishments(String special_accomplishments) {
		this.special_accomplishments = special_accomplishments;
	}
	public void setPotential_college_major(String potential_college_major) {
		this.potential_college_major = potential_college_major;
	}
	private String extra_curricular_activity;
	private String special_accomplishments;
	
}
