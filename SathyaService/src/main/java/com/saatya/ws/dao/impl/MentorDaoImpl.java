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
import com.saatya.ws.dao.MentorDao;
import com.saatya.ws.dao.UserDao;
import com.saatya.ws.model.Answers;
import com.saatya.ws.model.Mentor;
import com.saatya.ws.model.User;
import com.saatya.ws.util.CommonUtil;



public class MentorDaoImpl  implements MentorDao {

	@Override
	public List<Mentor> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Mentor> findByName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertMentor(String dsConfigName, Map<String, String> params) {
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

	
	// Mentor Reviewed students
	@Override
	public boolean insMentorReviewdStud(String dsConfigName, Map<String, String> params) {
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
	
	
	// Update Mentor Reviewed students
	@Override
	public boolean updMentorReviewdStud(String dsConfigName, Map<String, String> params) {
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
	
	
	
	//select Mentor Reviewed students 
	@Override
	public List<Object> getMentorReviewdStud(String dsConfigName,
			Map<String, String> params) {
		// TODO Auto-generated method stub
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);

		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object> listMentorReviewd =null;
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery();
			listMentorReviewd = util.getObjects(rs,"com.saatya.ws.model.Mentor");
	
		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			
			// close(rs,stmt,conn);
			try {
				DatabaseUtils.close(rs, stmt, conn);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		return listMentorReviewd;
	}
	
	@Override
	public boolean updateMentor(Mentor user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteMentor(String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Mentor findMentorByMentorId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public  List<Object> getOnlineMentors(String dsConfigName,
			Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.Mentor");
	
		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			
			// close(rs,stmt,conn);
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
	public  List<Object> getTopMentorsSubjectWise(String dsConfigName,
			Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.TopMentorEachSubject");
	
		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			
			// close(rs,stmt,conn);
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
	public List<Object> searchMentors(String dsConfigName,
			Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.Mentor");
	
		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			
			// close(rs,stmt,conn);
			try {
				DatabaseUtils.close(rs, stmt, conn);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		return listUser;
	}
	
}
