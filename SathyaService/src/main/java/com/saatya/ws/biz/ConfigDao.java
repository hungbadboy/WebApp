package com.saatya.ws.biz;


import java.util.Map;

import com.saatya.ws.common.DAOException;

public interface ConfigDao {
	public String processEntityService(String entityName, String sql) throws DAOException;
	

}
