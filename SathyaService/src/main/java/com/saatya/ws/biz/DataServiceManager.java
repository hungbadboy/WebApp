package com.saatya.ws.biz;

import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saatya.ws.common.DAOException;
import com.saatya.ws.biz.ConfigDao;
import com.saatya.ws.biz.impl.ConfigMySqlDaoImpl;

public class DataServiceManager {

	private static final Logger LOG = LoggerFactory.getLogger(DataServiceManager.class);

	private static DataServiceManager _instance = null;
	private static ResourceBundle appConfigRB = null;

	private DataServiceManager() {
		appConfigRB = ResourceBundle.getBundle("DataServiceSQLMap");
		LOG.info("initialized...");
	}

	public synchronized static DataServiceManager getInstance() {
		if (_instance == null) {
			_instance = new DataServiceManager();
		}
		return _instance;
	}
	
	
	public String processEntityService(String dsConfigName, Map<String, String> params) {		
		String query = null;
		String drs = null;
		LOG.info("Data Service Entity Name : Puling data for the Key " + dsConfigName);
	    try {
	    	query = appConfigRB.getString(dsConfigName);
			
			if(dsConfigName != null) {
				for (String key : params.keySet()) {
					String pv = params.get(key);
					String pattern = "http." + key;
					try {
						query = query.replaceAll(pattern, pv.toString());
					} catch (Exception e) {
						LOG.error(pv.toString(), e);
					}
				}
			} 

			
			ConfigDao dao = new ConfigMySqlDaoImpl();
	    	 drs = dao.processEntityService(dsConfigName, query);
		} catch (DAOException e) {
			LOG.error("SQL:" + query, e);
			return null;
		}
		
		return drs;
	}
	

	
}