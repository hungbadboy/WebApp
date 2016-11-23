package com.siblinks.ws.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.model.Download;

/**
 * This is repository handling CRUD
 *
 * @author hungpd
 * @version 1.0
 */

public interface ObjectDao {
    /**
     * This method update execute. Insert or Update or Delete
     *
     * @param dsConfigName
     *            This parameter will map to get script SQL
     * @param params
     *            This is array object which are parameters.
     * @return It will return True if executing SQL script is success else it is
     *         False
     */
    public boolean insertUpdateObject(String dsConfigName, Object[] params) throws DAOException;

    /**
     * This is method to retrieve data
     *
     * @param dsConfigName
     *            This parameter will map to get script SQL
     * @param params
     *            This is array object which are parameters.
     * @return List object
     */
    public List<Object> readObjects(String dsConfigName, Object[] params) throws DAOException;

    /**
     * This is method to retrieve data
     *
     * @param dsConfigName
     *            This parameter will map to get script SQL
     * @param params
     *            This is map key/value object to map and set parameters.
     * @return List object
     */
    public List<Object> readObjects(final String dsConfigName, final Map<String, String> params) throws DAOException;

    /**
     * This is the uploading file data. User can upload images, videos.
     *
     * @param dsConfigName
     *            this parameter will map to get script SQL
     * @param params
     *            this is array object which are parameters.
     * @return It will return True if executing SQL script is success else it is
     *         False
     */
    public boolean upload(String dsConfigName, Object[] params, MultipartFile file) throws DAOException;

    /**
     * This method is handling download file.
     *
     * @param dsConfigName
     *            this parameter will map to get script SQL
     * @param params
     *            this is array object which are parameters.
     * @return Return Download object
     */
    public Download download(String dsConfigName, Object[] params) throws DAOException;

    /**
     *
     * @param dsConfigName
     *            this parameter will map to get script SQL
     * @param params
     *            this is array object which are parameters.
     * @return It will return True if executing SQL script is success else it is
     *         False
     */
    public boolean insertUpdateObject(final String dsConfigName, final Map<String, String> params) throws DAOException;

    /**
     * This method count data
     *
     * @param strSQLMapper
     *            Key mapping to SQL script
     * @param params
     *            Array object set SQL script
     *
     * @return Counter number of data
     */
    public String getCount(String strSQLMapper, final Object[] params) throws DAOException;

    /**
     * Insert object get generate auto Id Key
     *
     * @param dsConfigName
     *            SQL Mapper key
     * @param params
     *            Parameters of object
     * @return return auto generate key id
     */
    public long insertObject(String dsConfigName, Object[] params) throws DAOException;

    /**
     * This method append where clause to SQL
     *
     * @param dsConfigName
     *            SQL KEY mapping
     * @param whereClause
     *            script SQL where
     * @param params
     *            ParamaterObject
     * @return
     */
    public List<Object> readObjectsWhereClause(String dsConfigName, String whereClause, Object[] params) throws DAOException;

    /**
     * @param dsConfigName
     * @param listParams
     */
    public void insertUpdateBatch(String dsConfigName, List<Object[]> listParams) throws DAOException;

    /**
     * This method get query no condition
     * 
     * @param dsConfigName
     * @return
     * @throws DAOException
     */
    public List<Map<String, Object>> readObjectNoCondition(String dsConfigName) throws DAOException;

    /**
     * @param dsConfigName
     * @param parameterSource
     * @return
     * @throws DAOException
     */
    List readObjectNamedParameter(String dsConfigName, MapSqlParameterSource parameterSource)
            throws DAOException;
}
