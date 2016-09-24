package com.saatya.ws.dao;

import java.util.List;
import java.util.Map;

import com.saatya.ws.model.QAFollowInfo;



public interface QAFollowInfoDao extends SaatyaDao {
	
	public List<QAFollowInfo> findAll();

	public List<QAFollowInfo> findByName();

	
	public boolean insertQAFollowInfo(String dsConfigName, Map<String, String> params);

	public boolean updateQAViews(String dsConfigName, Map<String, String> params);
	
	public boolean updateQAFollowInfo(QAFollowInfo user);

	public boolean deleteQAFollowInfo(String userId);
	
	public QAFollowInfo findQAFollowInfoByQAFollowInfoId(String userId);
	
	

}
