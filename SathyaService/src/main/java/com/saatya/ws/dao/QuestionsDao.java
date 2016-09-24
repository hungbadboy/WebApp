package com.saatya.ws.dao;

import java.util.List;
import java.util.Map;

import com.saatya.ws.model.Questions;



public interface QuestionsDao extends SaatyaDao {
	
	public List<Object> findAll(String dsConfigName, Map<String, String> params);
	
	public List<Object> getLatestQuestion(String dsConfigName, Map<String, String> params);
	
	public List<Object> getAnswers(String dsConfigName, Map<String, String> params);
	
	public List<Object> getLatestUserQuestions(String dsConfigName, Map<String, String> params);

	public List<Object> findByName(String dsConfigName, Map<String, String> params);

	public boolean insertQuestions(String dsConfigName, Map<String, String> params);

	public boolean updateQuestions(String dsConfigName, Map<String, String> params);

	public boolean deleteQuestions(String dsConfigName, Map<String, String> params);
	public boolean deleteAnswer(String dsConfigName, Map<String, String> params);
	
	public Object findQuestionsByQuestionsId(String dsConfigName, Map<String, String> params);
	
    public boolean postQuestion(String dsConfigName, Map<String, String> params);
    public boolean postAnswer(String dsConfigName, Map<String, String> params);
	public List<Object> getRelatedQuestions(String dsConfigName, Map<String, String> params);
	
	

}
