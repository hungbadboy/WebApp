package com.saatya.ws.dao;

import java.util.List;
import java.util.Map;

import com.saatya.ws.model.Subjects;



public interface SubjectsDao extends SaatyaDao {
	
	public List<Object> findAll(String dsConfigName, Map<String, String> params);
	
	public List<Object> listofVideos(String dsConfigName, Map<String, String> params);
	
	public List<Object> listofCategeris(String dsConfigName, Map<String, String> params);
	
	public List<Object> listofTopSubCategories(String dsConfigName, Map<String, String> params);
	
	public List<Object> listofSubCategories(String dsConfigName, Map<String, String> params);

	public List<Object> filterSubCategories(String dsConfigName, Map<String, String> params);
	
	public List<Object> SearchCategories(String dsConfigName, Map<String, String> params);

	public List<Object> getVedioInfo(String dsConfigName, Map<String, String> params);
	
	public List<Object> getVedioComments(String dsConfigName, Map<String, String> params);
	
	public List<Object> findByName(String dsConfigName, Map<String, String> params);
	
	public boolean ratingVedio(String dsConfigName, Map<String, String> params);
	public boolean postVideoComments(String dsConfigName, Map<String, String> params);

	

	public boolean updateSubjects(String dsConfigName, Map<String, String> params);

	public boolean deleteSubjectsr(String dsConfigName, Map<String, String> params);
	
	public Object findSubjectsBySubjectsId(String dsConfigName, Map<String, String> params);
	
	
	public boolean insertSubjects(String dsConfigName, Map<String, String> params);
	public boolean insertSubjectCategories(String dsConfigName, Map<String, String> params);
	public boolean insertSubjectSubcategories(String dsConfigName, Map<String, String> params);
	
	

}
