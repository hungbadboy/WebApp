package com.saatya.ws.dao;

import java.util.List;
import java.util.Map;

import com.saatya.ws.model.User;



public interface UserDao extends SaatyaDao {
	
	public List<Object> findAll(String dsConfigName, Map<String, String> params);
	
	public List<Object> getMajors(String dsConfigName, Map<String, String> params);

	public List<User> findByName();

	public boolean insertUser(String dsConfigName, Map<String, String> params);

	public boolean OneONOneChatNotes(String dsConfigName, Map<String, String> params);
	
	public boolean updateUser(String dsConfigName, Map<String, String> params);

	public boolean deleteUser(String dsConfigName, Map<String, String> params);
	
	public Object findUserByUserId(String dsConfigName, Map<String, String> params);
	
	public List<Object> getProfile(String dsConfigName, Map<String, String> params);
	
	public List<Object> getLisEsay(String dsConfigName, Map<String, String> params);
	
	public List<Object> getEducationSettings(String dsConfigName, Map<String, String> params);
	
	public List<Object> getNotificationSettings(String dsConfigName, Map<String, String> params);
	
	public List<Object> getBasicSettings(String dsConfigName, Map<String, String> params);
	


}
