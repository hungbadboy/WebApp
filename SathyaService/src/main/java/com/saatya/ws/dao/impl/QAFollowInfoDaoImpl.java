package com.saatya.ws.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saatya.ws.biz.DatabaseUtils;
import com.saatya.ws.common.DAOException;
import com.saatya.ws.dao.AnswersDao;
import com.saatya.ws.dao.QAFollowInfoDao;
import com.saatya.ws.dao.UserDao;
import com.saatya.ws.model.Answers;
import com.saatya.ws.model.QAFollowInfo;
import com.saatya.ws.model.User;
import com.saatya.ws.util.CommonUtil;



public class QAFollowInfoDaoImpl  implements QAFollowInfoDao {

	@Override
	public List<QAFollowInfo> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QAFollowInfo> findByName() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public boolean insertQAFollowInfo(String dsConfigName, Map<String, String> params) {
		boolean flag=false;
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);

		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			int numRows = stmt.executeUpdate();
			if(numRows > 0){
				flag=true;
			}
	
		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			
			
			try {
				DatabaseUtils.close(stmt, conn);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		

		return flag;
	}

	
	@Override
	public boolean updateQAViews(String dsConfigName, Map<String, String> params) {
		boolean flag=false;
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);

		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			int numRows = stmt.executeUpdate();
			if(numRows > 0){
				flag=true;
			}
	
		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			
			
			try {
				DatabaseUtils.close(stmt, conn);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		

		return flag;
	}
	
	@Override
	public boolean updateQAFollowInfo(QAFollowInfo user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteQAFollowInfo(String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public QAFollowInfo findQAFollowInfoByQAFollowInfoId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}}
