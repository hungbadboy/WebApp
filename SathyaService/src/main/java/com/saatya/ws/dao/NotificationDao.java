package com.saatya.ws.dao;

import java.util.List;
import java.util.Map;

import com.saatya.ws.model.Answers;



public interface NotificationDao extends SaatyaDao {
	
    public boolean insertNotification(String dsConfigName, Map<String, String> params);
	
	public  List<Object> readNotification(String dsConfigName, Map<String, String> params);
	
	 public boolean updateNotification(String dsConfigName, Map<String, String> params);

	
	
}
