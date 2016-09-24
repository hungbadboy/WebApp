package com.saatya.ws.dao;

import java.util.List;

import com.saatya.ws.model.UserVideoInfo;



public interface UserVideoInfoDao extends SaatyaDao {
	
	public List<UserVideoInfo> findAll();

	public List<UserVideoInfo> findByName();

	public boolean insertUserVideoInfo(UserVideoInfo user);

	public boolean updateUserVideoInfo(UserVideoInfo user);

	public boolean deleteUserVideoInfo(String userId);
	
	public UserVideoInfo findUserVideoInfoByUserVideoInfoId(String userId);
	
	
}
