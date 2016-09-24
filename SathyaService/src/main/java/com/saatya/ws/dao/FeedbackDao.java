package com.saatya.ws.dao;

import java.util.List;
import java.util.Map;

import com.saatya.ws.model.Mentor;



public interface FeedbackDao extends SaatyaDao {
	
	public List<Mentor> findAll();

	public List<Mentor> findByName();

	public boolean insertFeedback(String dsConfigName, Map<String, String> params);
	
	public  List<Object> getFeedbacks(String dsConfigName, Map<String, String> params);
	
	
	public boolean updateMentor(Mentor user);

	public boolean deleteMentor(String userId);
	
	public Mentor findMentorByMentorId(String userId);
	
	

}
