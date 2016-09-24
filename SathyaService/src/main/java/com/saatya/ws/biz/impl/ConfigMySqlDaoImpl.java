package com.saatya.ws.biz.impl;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saatya.ws.biz.ConfigDao;
import com.saatya.ws.biz.DatabaseUtils;
import com.saatya.ws.common.DAOException;
import com.saatya.ws.common.ErrorLevel;



public class ConfigMySqlDaoImpl implements ConfigDao{
	private static final Logger logger = LoggerFactory.getLogger(ConfigMySqlDaoImpl.class);
	private static final int fetchSize=10;
	public ConfigMySqlDaoImpl(){
		super();
	}
	
	

	
	
	public String processEntityService(String dsConfigName, String query) throws DAOException{
		logger.debug("DataService called.");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		JSONArray jarray1 = new JSONArray();
		try {			
			logger.info("Dataservice sql : " + query);
			conn = DatabaseUtils.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(query,  ResultSet.TYPE_SCROLL_SENSITIVE,
			           ResultSet.CONCUR_UPDATABLE);
			
			if(dsConfigName.contains("READ")){
				rs = stmt.executeQuery();
				/*int size = 0;
				if (rs != null)   
				{  
				  if(rs.last()){
					  size = rs.getRow();  
					  rs.beforeFirst();
				  }
					  
				}*/
				ResultSetMetaData rsmd = rs.getMetaData();
			      int colCount = rsmd.getColumnCount();
			      //Column[] cols = null; 
			     // Row[] rows = null; 
			      int j=0;	 
			     // int rowCount = 0;
			     // cols = new Column[colCount];
		    	 // rows = new Row[size];
			      while (rs.next()) {
			    	  JSONObject root = new JSONObject();
		    		  Object [] object=new Object [colCount];
				      for (int ii = 1; ii <= colCount; ii++) {
			          String name=rsmd.getColumnName(ii);
			           //cols[ii-1]=new Column();
			          // cols[ii-1].setName(name);
			           //cols[ii-1].setOrdinal(ii-1);
			           String type=rsmd.getColumnTypeName(ii);
			           /*if(type.equals("VARCHAR"))type="STRING";
			           if(type.equals("TIMESTAMP"))type="DATE";
			           if(type.equals("DATETIME"))type="DATE";
			           if(type.equals("CHAR"))type="STRING";
			           if(type.equals("INT"))type="NUMBER";
			           if(type.equals("BIGINT"))type="NUMBER";*/
			           if(type.equals("CLOB")){
			        	   Clob clob = rs.getClob(ii);  
			        	    if (clob != null) {  
			        	      if ((int) clob.length() > 0) {  
			        	    	  object[ii-1] = (Object)clob.getSubString(1, (int) clob.length());  
			        	      }  
			        	    }  
			           }else  object[ii-1]= (Object)rs.getObject(ii);
			           
			           root.put(name, object[ii-1]);
			          // cols[ii-1].setType(Column.Type.valueOf(type));
			          // cols[ii-1].setId(ii);
			        }
				      jarray1.put(root);
				      //rowCount++;
				      
			    	 //rows[j++]=new Row<Object>(object);
			    	 
		      }
			     /* if(cols != null && rows != null && rows.length > 0) {
			    	  SimpleResultSet srs = new SimpleResultSet("APP_ID",cols, rows, rowCount, 0);  
			    	  srs.setType("READ RESULT SET");
			    	  drs = srs;
			      } else {
			    	  drs = null;
			      }*/
			      
			  // logger.info("Result set: "+drs);
			} else if(dsConfigName.contains("INSERT") || dsConfigName.contains("DELETE") || dsConfigName.contains("UPDATE")){
				 JSONObject root = new JSONObject();
		    		
					int numRows = stmt.executeUpdate();
					 root.put(dsConfigName, numRows);
					 jarray1.put(root);
				
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Got error in data service." + e);
			throw new DAOException(e,
					"Got error in data service.", null,
					ErrorLevel.ERROR);
		}
		
		return jarray1.toString();
	}
	
			
}
