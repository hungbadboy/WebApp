package com.saatya.ws.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import com.saatya.ws.biz.DatabaseUtils;
import com.saatya.ws.common.DAOException;





public class CommonUtil  {
	private static CommonUtil _instance = null;
	private static ResourceBundle appConfigRB = null;

	private CommonUtil() {
		appConfigRB = ResourceBundle.getBundle("DataServiceSQLMap");
		//LOG.info("initialized...");
	}

	public synchronized static CommonUtil getInstance() {
		if (_instance == null) {
			_instance = new CommonUtil();
		}
		return _instance;
	}
	
	

	public Map<String, String> getLimit(String pageno, String limit) {		
		
		Map<String, String> map=new HashMap<String, String>();
		if(pageno == null || "".equals(pageno.trim())){
			pageno = "1";
		}
		if(limit == null || "".equals(limit.trim())){
			limit = "10";
		}
	    try {
	    	
	       int pNo=Integer.parseInt(pageno.trim());
	       int lmt=Integer.parseInt(limit.trim());
	       
	       map.put("from", ""+((pNo*lmt) - (lmt)));
	       map.put("to", ""+(lmt));
	   	    	
	    } catch (Exception e) {
			//LOG.error("SQL:" + query, e);
			//return null;
		}
		//System.out.println(query);
		return map;
	}
	
	public String getQuery(String dsConfigName, Map<String, String> params) {		
		String query = null;
		
		//LOG.info("Data Service Entity Name : Puling data for the Key " + dsConfigName);
	    try {
	    	query = appConfigRB.getString(dsConfigName);
			
			if(dsConfigName != null && params != null) {
				for (String key : params.keySet()) {
					String pv = params.get(key);
					String pattern = "http." + key;
					try {
						query = query.replaceAll(pattern, pv.toString());
					} catch (Exception e) {
						//LOG.error(pv.toString(), e);
					}
				}
			} 

			
			
		} catch (Exception e) {
			//LOG.error("SQL:" + query, e);
			return null;
		}
		System.out.println(query);
		return query;
	}
	public void getObjectFromJson(Object obj, JSONObject json) throws IllegalAccessException, InvocationTargetException, JSONException{
		
		if(json != null){
			
			Iterator itr=json.keys();
			while(itr.hasNext()){
			String key=	(String)itr.next();
			BeanUtils.setProperty(obj,key,json.getString(key));
				
			}
			
			
		}
		
	}
public List<Object> getObjects(ResultSet rs, String className) throws IOException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, JSONException, InvocationTargetException {
	ResultSetMetaData rsmd = rs.getMetaData();
	int colCount = rsmd.getColumnCount();
	List<Object> listUser = new ArrayList<Object>();
	
	while (rs.next()) {
		JSONObject root = new JSONObject();
		Class c = Class.forName(className);  
		Object ob=c.newInstance();
		Object[] object = new Object[colCount];
		for (int ii = 1; ii <= colCount; ii++) {
			String name = rsmd.getColumnLabel(ii);

			String type = rsmd.getColumnTypeName(ii);

			if (type.equals("CLOB")) {
				Clob clob = rs.getClob(ii);
				if (clob != null) {
					if ((int) clob.length() > 0) {
						object[ii - 1] = (Object) clob.getSubString(1, (int) clob.length());
					}
				}
			} else if(type.equals("BLOB")) { 
				object[ii - 1] = (Object) readBLOBToFileStream((rs.getBlob(ii) != null)?rs.getBlob(ii):null);
			} else
				object[ii - 1] = (Object) rs.getObject(ii);

			root.put(name, object[ii - 1]);

		}
		getObjectFromJson(ob, root);
		listUser.add(ob);
		

	}
	
	return listUser;
}




	public String readBLOBToFileStream(Blob image) throws IOException, SQLException {
		int BUFFER_SIZE = 4096;
		String imageBase64 = null;
		byte[] buffer = null;
		if (image == null)
			return imageBase64;
		try {
			buffer = new byte[BUFFER_SIZE];
			buffer = image.getBytes(1, (int) image.length());
			imageBase64 = Base64.encodeBase64String(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageBase64;
	}

public  String  getCount(String dsConfigName,Map<String, String> params) {
	// TODO Auto-generated method stub
	CommonUtil util = CommonUtil.getInstance();
	String query = util.getQuery(dsConfigName, params);

	
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	String count=null;
	try {
		
		conn = DatabaseUtils.getConnection();
		stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		rs = stmt.executeQuery();
		
		while(rs.next()){
		 int cnt=rs.getInt("COUNT");
		 count=""+cnt;
		}

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
	return count;
	//return listUser;
}


}
