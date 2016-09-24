package com.saatya.ws.dao;

import java.util.List;
import java.util.Map;

import com.saatya.ws.model.Mentor;



public interface MentorDao extends SaatyaDao {
	
	public List<Mentor> findAll();

	public List<Mentor> findByName();

	public boolean insertMentor(String dsConfigName, Map<String, String> params);
	
	public  List<Object> getOnlineMentors(String dsConfigName, Map<String, String> params);
	
	public  List<Object> getTopMentorsSubjectWise(String dsConfigName, Map<String, String> params);
	
	public  List<Object> searchMentors(String dsConfigName, Map<String, String> params);

	public boolean updateMentor(Mentor user);

	public boolean deleteMentor(String userId);
	
	public Mentor findMentorByMentorId(String userId);
	
	public boolean insMentorReviewdStud(String dsConfigName, Map<String, String> params);
	
	public boolean updMentorReviewdStud(String dsConfigName, Map<String, String> params);
	
	public List<Object> getMentorReviewdStud(String dsConfigName, Map<String, String> params);
	
	

}
