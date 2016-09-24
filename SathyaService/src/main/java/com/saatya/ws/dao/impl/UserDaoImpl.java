package com.saatya.ws.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.saatya.ws.biz.DatabaseUtils;
import com.saatya.ws.common.DAOException;
import com.saatya.ws.dao.UserDao;
import com.saatya.ws.model.User;
import com.saatya.ws.util.CommonUtil;

public class UserDaoImpl implements UserDao {

	@Override
	public List<Object> findAll(String dsConfigName, Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.User");
	
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
	public List<Object> getMajors(String dsConfigName, Map<String, String> params){
		
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);

		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object> listUtility =null;
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery();
			listUtility = util.getObjects(rs,"com.saatya.ws.model.Utility");
	
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
		return (listUtility!= null && listUtility.size() > 0) ?listUtility:null; 
		
	}

	

	@Override
	public List<User> findByName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertUser(String dsConfigName, Map<String, String> params) {
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
	public boolean updateUser(String dsConfigName, Map<String, String> params) {
		
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
	
		}catch (Exception e) {
			e.printStackTrace();

		}finally{
			
			// close(rs,stmt,conn);
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
	public boolean OneONOneChatNotes(String dsConfigName, Map<String, String> params) {
		
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
	
		}catch (Exception e) {
			e.printStackTrace();

		}finally{
			
			// close(rs,stmt,conn);
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
	public boolean deleteUser(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object findUserByUserId(String dsConfigName, Map<String, String> params) {

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
			listUser = util.getObjects(rs,"com.saatya.ws.model.User");
	
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

		return (listUser!= null && listUser.size() > 0) ?listUser.get(0):null;
	
	}


	@Override
	public List<Object> getProfile(String dsConfigName, Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.Profile");
	
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
	public List<Object> getLisEsay(String dsConfigName, Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.Upload");
	
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
	public List<Object> getEducationSettings(String dsConfigName, Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.UserEducationSettings");
	
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
	public List<Object> getNotificationSettings(String dsConfigName, Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.UserNotificationSettings");
	
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
	public List<Object> getBasicSettings(String dsConfigName, Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.UserBasicSettings");
	
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
	
	
}
