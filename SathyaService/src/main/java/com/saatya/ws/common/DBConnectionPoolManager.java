package com.saatya.ws.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.saatya.ws.resource.AppConfigManager;


public class DBConnectionPoolManager {
    
    protected static Log logger = LogFactory.getLog(DBConnectionPoolManager.class);
    
    private static PoolingDataSource dataSource = null;
    
    
    public Connection getConnection() {
        
        Connection con = null;
        try {
            if(dataSource != null) {
                con = dataSource.getConnection();
            }
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return con;
    }
    
    public void releaseConnection(Connection con) {
        
        try {
            if(con != null) {
                con.close();
            }
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
    }


    static {
        String uri = AppConfigManager.getConfigVal("disp.db.url");
        String userName = AppConfigManager.getConfigVal("disp.db.user");
        String password = AppConfigManager.getConfigVal("disp.db.password");
        String driverClassName = AppConfigManager.getConfigVal("disp.db.driver");
        int maxActive = AppConfigManager.getIntValue("disp.db.maxactive", 50);
        int maxIdle = AppConfigManager.getIntValue("disp.db.maxidle", 10);
        int erm = AppConfigManager.getIntValue("disp.db.evictionRunMillis", 10000);
        Properties props = new Properties();
        props.put("url", uri);
        props.put("user", userName);
        props.put("password", password);
        props.put("driverClassName", driverClassName);

        init(uri, driverClassName, erm, props, maxActive, maxIdle);
    }
    
    private static void init(String uri, String driverClassName, int erm, Properties props, int maxActive, int maxIdle) {
        

        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(uri, props);
        
        GenericObjectPool connectionPool = new GenericObjectPool();
        
        PoolableConnectionFactory poolableConnectionFactory =
            new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
        
        connectionPool.setFactory(poolableConnectionFactory);
        connectionPool.setTimeBetweenEvictionRunsMillis(erm);
        connectionPool.setMaxActive(maxActive);
        connectionPool.setMaxIdle(maxIdle);
        
        dataSource = new PoolingDataSource(connectionPool);
        logger.info("DB connection pool initialized successfully.");

    }
    
 
    
    public static void main(String[] a) {
        Connection con = null;
        try {
            DBConnectionPoolManager pool = new DBConnectionPoolManager();
            con = pool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from DISP_INDEX");
            while(rs.next()) {
                System.out.println(rs.getObject(1));
            }

        }catch(Exception e) {
            e.printStackTrace();
            
        }finally {
            if(con != null) {
                try {
                    con.close();
                }
                catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    con = null;
                }
            }
        }
    }
    
}