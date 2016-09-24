package com.saatya.ws.dao;

import java.util.List;

import com.saatya.ws.model.SchoolCollegeDegree;



public interface SchoolCollegeDegreeDao extends SaatyaDao {
	
	public List<SchoolCollegeDegree> findAll();

	public List<SchoolCollegeDegree> findByName();

	public boolean insertSchoolCollegeDegree(SchoolCollegeDegree user);

	public boolean updateSchoolCollegeDegree(SchoolCollegeDegree user);

	public boolean deleteSchoolCollegeDegree(String userId);
	
	public SchoolCollegeDegree findSchoolCollegeDegreeBySchoolCollegeDegreeId(String userId);
	
	

}
