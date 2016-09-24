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
import com.saatya.ws.dao.QuestionsDao;
import com.saatya.ws.dao.UserDao;
import com.saatya.ws.model.Answers;
import com.saatya.ws.model.Questions;
import com.saatya.ws.model.User;
import com.saatya.ws.util.CommonUtil;



public class QuestionsDaoImpl  implements QuestionsDao {

	@Override
	public List<Object> findAll(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> getLatestQuestion(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);

		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object> listUser =null;
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery();
			listUser = util.getObjects(rs,"com.saatya.ws.model.Questions");
	
		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			
			
			try {
				DatabaseUtils.close(rs, stmt, conn);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

        return (listUser!= null && listUser.size() > 0) ?listUser:null;
		//return listUser;
	}
	
	@Override
	public List<Object> getAnswers(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);

		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object> listUser =null;
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery();
			listUser = util.getObjects(rs,"com.saatya.ws.model.Answers");
	
		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			
			
			try {
				DatabaseUtils.close(rs, stmt, conn);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

        return (listUser!= null && listUser.size() > 0) ?listUser:null;
		//return listUser;
	}

	@Override
	public List<Object> getLatestUserQuestions(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);

		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object> listUser =null;
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery();
			listUser = util.getObjects(rs,"com.saatya.ws.model.Questions");
	
		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			
			
			try {
				DatabaseUtils.close(rs, stmt, conn);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return (listUser!= null && listUser.size() > 0) ?listUser:null;
		//return listUser;
	}
	
	@Override
	public boolean postQuestion(String dsConfigName, Map<String, String> params) {
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
	public boolean postAnswer(String dsConfigName, Map<String, String> params) {
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
	public List<Object> getRelatedQuestions(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);

		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object> listUser =null;
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery();
			listUser = util.getObjects(rs,"com.saatya.ws.model.Questions");
	
		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			
			
			try {
				DatabaseUtils.close(rs, stmt, conn);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return (listUser!= null && listUser.size() > 0) ?listUser:null;
		//return listUser;
	}

	@Override
	public List<Object> findByName(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertQuestions(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateQuestions(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteQuestions(String dsConfigName, Map<String, String> params) {
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
	public boolean deleteAnswer(String dsConfigName, Map<String, String> params) {
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
	public Object findQuestionsByQuestionsId(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
