package com.saatya.ws.dao;

import java.util.List;

import com.saatya.ws.model.Answers;



public interface AnswersDao extends SaatyaDao {
	
	public List<Answers> findAll();

	public List<Answers> findByName();

	public boolean insertAnswers(Answers user);

	public boolean updateAnswers(Answers user);

	public boolean deleteAnswers(String userId);
	
	public Answers findUserByAnswersId(String userId);
	
	
}
