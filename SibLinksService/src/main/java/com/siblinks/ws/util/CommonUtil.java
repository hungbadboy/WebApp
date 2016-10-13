package com.siblinks.ws.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CommonUtil {
    private static CommonUtil _instance = null;
    // private static ResourceBundle appConfigRB = null;
    private final Log logger = LogFactory.getLog(getClass());

    // private CommonUtil() {
    // appConfigRB = ResourceBundle.getBundle("DataServiceSQLMap");
    // }

    public synchronized static CommonUtil getInstance() {
        if (_instance == null) {
            _instance = new CommonUtil();
        }
        return _instance;
    }

    public Map<String, String> getLimit(String pageno, String limit) {

        Map<String, String> map = new HashMap<String, String>();
        if (pageno == null || "".equals(pageno.trim()) || pageno.equals("0")) {
            pageno = "1";
        }
        if (limit == null || "".equals(limit.trim())) {
            limit = "10";
        }
        try {
            int pNo = Integer.parseInt(pageno.trim());
            int lmt = Integer.parseInt(limit.trim());

            map.put("from", "" + ((pNo * lmt) - (lmt)));
            map.put("to", "" + (lmt));
        } catch (Exception e) {
            logger.error("SQL:", e);
        }
        return map;
    }

    public Map<String, String> getOffset(String limit, String offset) {

        Map<String, String> map = new HashMap<String, String>();
        if (limit == null || "".equals(limit.trim()) || limit.equals("0")) {
            limit = "10";
        }
        if (offset == null || "".equals(offset.trim()) || offset.equals("0")) {
            offset = "0";
        }
        try {
            int pNo = Integer.parseInt(limit.trim());
            int os = Integer.parseInt(offset.trim());

            map.put("limit", "" + pNo);
            map.put("offset", "" + os);
        } catch (Exception e) {
            logger.error("SQL:", e);
        }
        return map;
    }

    public String getQuery(final String dsConfigName, final Map<String, String> params) {
        String query = null;
        //
        // try {
        // query = env.getProperty(dsConfigName);
        //
        // if (dsConfigName != null && params != null) {
        // for (String key : params.keySet()) {
        // String pv = params.get(key);
        // String pattern = "http." + key;
        // try {
        // query = query.replaceFirst(pattern, ((pv != null) ? pv.toString() :
        // "null"));
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // return null;
        // }
        // logger.info(query);
        return query;
    }

    public String getQueryNotParams(final String dsConfigName, final String userId, final String itemId) {
        String query = null;
        //
        // try {
        // query = env.getProperty(dsConfigName);
        // query = String.format(query, userId, itemId);
        // } catch (Exception e) {
        // e.printStackTrace();
        // return null;
        // }
        // logger.info(query);
        return query;
    }

    public String getQueryNotResource(final String dsConfigName) {
        String query = null;

        // try {
        // query = env.getProperty(dsConfigName);
        // query = String.format(query);
        // } catch (Exception e) {
        // e.printStackTrace();
        // return null;
        // }
        // logger.info(query);
        return query;
    }

    public void getObjectFromJson(final Object obj, final JSONObject json)
            throws IllegalAccessException, InvocationTargetException, JSONException {
        if (json != null) {
            Iterator itr = json.keys();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                BeanUtils.setProperty(obj, key, json.getString(key));
            }
        }
    }

    private Map<String, String> readFields(final ResultSetMetaData metaData) throws SQLException {
        Map<String, String> columnMap = new HashMap<String, String>();
        int count = metaData.getColumnCount();
        for (int i = 0; i < count; i++) {
            columnMap.put(metaData.getColumnLabel(i + 1), metaData.getColumnTypeName(i + 1));
        }
        return columnMap;
    }

    public List<Object> getObjects(final ResultSet rs) throws IOException, SQLException, ClassNotFoundException,
            InstantiationException, IllegalAccessException, JSONException, InvocationTargetException {
        ResultSetMetaData rsmd = rs.getMetaData();
        Map<String, String> columnMap = readFields(rsmd);
        List<Object> listUser = new ArrayList<Object>();

        while (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String name = null;
            Object val = null;
            for (Map.Entry<String, String> entry : columnMap.entrySet()) {
                name = entry.getKey();
                if (entry.getValue().equals("TIMESTAMP") || entry.getValue().equals("DATE")) {
                    try {
                        val = rs.getTimestamp(name);
                    } catch (Exception e) {
                        logger.error(name);
                    }

                } else if (entry.getValue().equals("CLOB")) {
                    Clob clob = rs.getClob(name);
                    if (clob != null) {
                        if ((int) clob.length() > 0) {
                            val = clob.getSubString(1, (int) clob.length());
                        }
                    }
                } else if (entry.getValue().equals("BLOB")) {
                    val = readBLOBToFileStream((rs.getBlob(name) != null) ? rs.getBlob(name) : null);
                } else {
                    val = rs.getObject(name);
                }
                map.put(name, val != null ? val.toString() : val);
            }
            listUser.add(map);
        }
        return listUser;
    }

    public String readBLOBToFileStream(final Blob image) throws IOException, SQLException {
        int BUFFER_SIZE = 4096;
        String imageBase64 = null;
        byte[] buffer = null;
        if (image == null) {
            return imageBase64;
        }
        try {
            buffer = new byte[BUFFER_SIZE];
            buffer = image.getBytes(1, (int) image.length());
            imageBase64 = Base64.encodeBase64String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageBase64;
    }

    public static String buildAppendQuery(final String whereClause, final String orderBy, final boolean limit,
            final boolean offset) {
        String sql = "";
        try {

            if (whereClause != null && !"".equals(whereClause)) {
                sql += " WHERE " + whereClause;
            }

            if (orderBy != null && !"".equals(orderBy)) {
                sql += " ORDER BY " + orderBy;
            }
            if (limit) {
                sql += " Limit ? ";
            }
            if (offset) {
                sql += " Offset ?";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql;
    }

    /**
     * This method verify password with encrypt password
     *
     * @param pwd
     *            This parameter is password that is not yet encrypt
     * @param encryptPwd
     *            This parameter is password that is encrypt
     * @return
     */
    public static boolean verifyPassword(final String pwd, final String encryptPwd) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(SibConstants.LENGHT_AUTHENTICATION);
        if (encoder.matches(pwd, encryptPwd)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method encode password
     *
     * @param rawPassword
     *            This parameter is password that is not yet encrypt
     *
     * @return Password encoded
     */
    public static String cryptPassword(final String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(SibConstants.LENGHT_AUTHENTICATION);
        return encoder.encode(rawPassword);
    }

    public static String generateToken() {
        Random ran = new Random();
        int top = 40;
        char data = ' ';
        String dat = "";

        for (int i = 0; i <= top; i++) {
            data = (char) (ran.nextInt(25) + 97);
            dat = data + dat;
        }
        return dat;
    }

    /**
     * This method use to get sub category
     *
     * @param id
     *            This parameter is parent id
     * @param strCategory
     *
     * @param listCategory
     * @return
     */

    public static String getAllChildCategory(final String id, final List<?> listCategory) {
        ArrayList<String> listSubject = new ArrayList<>();
        return getAllChildSubjects(id, listCategory, listSubject);
    }

    /**
     * @param id
     * @param listCategory
     */
    private static String getAllChildSubjects(final String id, final List<?> listCategory, final ArrayList<String> subjects) {
        subjects.add(id);
        for (Object object : listCategory) {
            Map<String, Integer> mapCategory = (HashMap<String, Integer>) object;
            if (mapCategory != null && mapCategory.containsKey(Parameters.SUBJECT_ID)) {
                String parentId = "" + mapCategory.get(Parameters.PARENT_ID);
                String subjectId = "" + mapCategory.get(Parameters.SUBJECT_ID);
                if (id.equals(parentId)) {
                    getAllChildSubjects(subjectId, listCategory, subjects);
                }
            }
        }
        return StringUtils.join(subjects, ",");
    }

    /**
     * @return random password when admin add new mentor
     */
    public String getAutoGeneratePwd() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&";
        String pwd = RandomStringUtils.random(8, 0, 0, false, false, characters.toCharArray(), new SecureRandom());
        return pwd;
    }

}
