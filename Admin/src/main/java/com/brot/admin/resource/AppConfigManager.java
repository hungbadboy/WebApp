package com.brot.admin.resource;

import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brot.admin.util.DateUtil;


public class AppConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(AppConfigManager.class);

	public static final String SIB_ENV = "sib.env";
	private static ResourceBundle rb = null;

	public static ResourceBundle getRb() {
		return rb;
	}

	public static void setRb(final ResourceBundle rb) {
		AppConfigManager.rb = rb;


	}

	public static void init(final String env) {

		String fileName = null;

		try {

			if (env != null){
				fileName = "appconfig_" + env;
			}
			else{
				fileName = "appconfig";
			}

			System.out.println("fileName==="+fileName);


			rb = ResourceBundle.getBundle(fileName);

			/*String appConfig = System.getProperty("appConfig");
			String appName = System.getProperty("appName");
			String jvmName = System.getProperty("jvmName");

			if(appConfig == null)  appConfig = getConfigVal("appConfig");
			if(appName == null) appName = getConfigVal("appName");
			if(jvmName == null) jvmName = getConfigVal("jvmName");*/

           /* if(appConfig != null) {
                System.out.println("loading appconfig properties from admin:"+appConfig);
                String endPointURL = AppConfigManager.getConfigVal("dataServiceURL");
                Map queryParams = new HashMap();
                queryParams.put("jvmName", jvmName);
                queryParams.put("appName", appName);

                List response = (List) AdminServiceManagerClient.getInstance().dataService(null, "DISP_JVM_PROPS_READ", queryParams, endPointURL);

                if (response != null && response.size() > 0) {
                    DISPResultSet drs = (DISPResultSet) response.get(0);
                    Row<?> row = null;
                    while (drs.hasNextRow()) {
                        row = drs.getNextRow();
                        String appconfig = (String)row.getItem(3);
                        rb = null;
                        rb = new PropertyResourceBundle(new ByteArrayInputStream(appconfig.getBytes()) );
                    }
                    System.out.println("Properties loaded.");
                } else {
                    System.out.println("Eror loading the properties from Admin Service.");
                }

            } */



		} catch (Exception e) {
			e.printStackTrace();
            logger.error("Exception in loading the property File" + e.getMessage());
		}

	}

	public static String getConfigVal(final String key) {

		String value = null;
		try {
			if (rb.getString(key) != null) {
                value = rb.getString(key).toString();
            } else {
                value = null;
            }
		} catch (Exception e) {
		}
		return value;
	}

	public static String getConfigVal(final String key, final String defVal) {

		String value = null;
		try {
			if (rb.getString(key) != null) {
                value = rb.getString(key).toString();
            } else {
                value = defVal;
            }
		} catch (Exception e) {
		}
		return value;
	}

	public static long getLongValue(final String key, final long defVal) {
		try {
			return Long.parseLong(getConfigVal(key));
		} catch (Exception e) {
			return defVal;
		}
	}

	public static int getIntValue(final String key, final int defVal) {
		try {
			return Integer.parseInt(getConfigVal(key));
		} catch (Exception e) {
			return defVal;
		}
	}

	public static Date getDateValue(final String key, final Date defValue) {
		return DateUtil.utc2Date(getConfigVal(key), defValue);
	}


	public static Map<String, String> getAllProperties() {
	    Enumeration<String> keys = rb.getKeys();
	    Map<String, String> response = new TreeMap<String, String>();
	    while(keys.hasMoreElements()) {
	        String key = keys.nextElement();
	        response.put(key, rb.getString(key));
	    }
	    return response;
	}

}
