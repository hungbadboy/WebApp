package com.saatya.ws.dao;

import java.util.List;
import java.util.Map;

import com.saatya.ws.model.QAVoteInfo;



public interface QAVoteInfoDao extends SaatyaDao {
	
	public List<QAVoteInfo> findAll();

	public List<QAVoteInfo> findByName();

	//public boolean insertQAVoteInfo(QAVoteInfo user);
	public boolean insertQAVoteInfo(String dsConfigName, Map<String, String> params);
	
	public boolean insertVoteanswer(String dsConfigName, Map<String, String> params);
	
	

	public boolean updateQAVoteInfo(QAVoteInfo user);

	public boolean deleteQAVoteInfo(String userId);
	
	public QAVoteInfo findQAVoteInfoByQAVoteInfoId(String userId);
	
	

}
