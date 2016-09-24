package com.saatya.ws.dao;

import java.util.List;
import java.util.Map;

import com.saatya.ws.model.Answers;



public interface FAQDao extends SaatyaDao {
	
   public boolean insertFaq(String dsConfigName, Map<String, String> params);
	
	public  List<Object> fetchFaqs(String dsConfigName, Map<String, String> params);

	
	
}
