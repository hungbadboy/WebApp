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
import com.saatya.ws.dao.SubjectsDao;
import com.saatya.ws.dao.UserDao;
import com.saatya.ws.model.Answers;
import com.saatya.ws.model.Subjects;
import com.saatya.ws.model.User;
import com.saatya.ws.util.CommonUtil;



public class SubjectsDaoImpl  implements SubjectsDao {

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
			listUser = util.getObjects(rs,"com.saatya.ws.model.Subjects");
	
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

		//return listUser;
        return (listUser!= null && listUser.size() > 0) ?listUser:null;
	}
	@Override
	public List<Object> listofVideos(String dsConfigName, Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.Video");
	
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

		//return listUser;
        return (listUser!= null && listUser.size() > 0) ?listUser:null;
	}
	@Override
	public List<Object> listofCategeris(String dsConfigName, Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.SubjectCategories");
	
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
	public List<Object> listofSubCategories(String dsConfigName, Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.SubjectSubCategories");
	
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
	public List<Object> listofTopSubCategories(String dsConfigName, Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.SubjectSubCategories");
	
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
	public List<Object> filterSubCategories(String dsConfigName, Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.SubjectSubCategories");
	
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
	public List<Object> SearchCategories(String dsConfigName, Map<String, String> params) {
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
			listUser = util.getObjects(rs,"com.saatya.ws.model.SubjectCategories");
	
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

		//return listUser;
		return (listUser!= null && listUser.size() > 0) ?listUser:null;
	}
	
	
	@Override
	public List<Object> getVedioInfo(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);

		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object> vedioInfo =null;
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery();
			vedioInfo = util.getObjects(rs,"com.saatya.ws.model.VideoSubMapping");
	
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
		return (vedioInfo!= null && vedioInfo.size() > 0) ?vedioInfo:null;
		//return vedioInfo;
	}

	
	
	@Override
	public List<Object> getVedioComments(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);

		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object> vedioInfo =null;
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery();
			vedioInfo = util.getObjects(rs,"com.saatya.ws.model.VideoSubMapping");
	
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
		return (vedioInfo!= null && vedioInfo.size() > 0) ?vedioInfo:null;
		//return vedioInfo;
	}
	
	@Override
	public boolean ratingVedio(String dsConfigName, Map<String, String> params) {
		
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);
		
		Connection conn = null;
		PreparedStatement stmt = null;
		int status=0;
		boolean serStatus;
	
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			status = stmt.executeUpdate();
		
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
		
		if(status>0)
			serStatus=true;
		else
			serStatus=false;
		
		return serStatus;
	}
	
	
	@Override
	public boolean postVideoComments(String dsConfigName, Map<String, String> params) {
		
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);
		
		Connection conn = null;
		PreparedStatement stmt = null;
		int status=0;
		boolean serStatus;
	
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			status = stmt.executeUpdate();
		
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
		
		if(status>0)
			serStatus=true;
		else
			serStatus=false;
		
		return serStatus;
	}
	
	
	@Override
	public List<Object> findByName(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertSubjects(String dsConfigName, Map<String, String> params) {
		
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);
		
		Connection conn = null;
		PreparedStatement stmt = null;
		int status=0;
		boolean serStatus;
	
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			status = stmt.executeUpdate();
		
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
		
		if(status>0)
			serStatus=true;
		else
			serStatus=false;
		
		return serStatus;
	}
	
	@Override
	public boolean insertSubjectCategories(String dsConfigName, Map<String, String> params) {
		
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);
		
		Connection conn = null;
		PreparedStatement stmt = null;
		int status=0;
		boolean serStatus;
	
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			status = stmt.executeUpdate();
		
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
		
		if(status>0)
			serStatus=true;
		else
			serStatus=false;
		
		return serStatus;
	}
	
	@Override
	public boolean insertSubjectSubcategories(String dsConfigName, Map<String, String> params) {
		
		CommonUtil util = CommonUtil.getInstance();
		String query = util.getQuery(dsConfigName, params);
		
		Connection conn = null;
		PreparedStatement stmt = null;
		int status=0;
		boolean serStatus;
	
		try {
			
			conn = DatabaseUtils.getConnection();
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			status = stmt.executeUpdate();
		
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
		
		if(status>0)
			serStatus=true;
		else
			serStatus=false;
		
		return serStatus;
	}

	@Override
	public boolean updateSubjects(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteSubjectsr(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Subjects findSubjectsBySubjectsId(String dsConfigName, Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}}
