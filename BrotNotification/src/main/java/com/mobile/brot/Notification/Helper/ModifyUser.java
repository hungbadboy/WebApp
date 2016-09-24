package com.mobile.brot.Notification.Helper;

import java.util.HashMap;
import java.util.Map;

import com.mobile.brot.Notification.context.AppConfigManager;
//import com.mobile.brot.client.MobileServiceClient;


public class ModifyUser {

	@SuppressWarnings("unchecked")
	public String activateUser(long id) {
		
		//String endpointURL="http://ec2-54-200-200-106.us-west-2.compute.amazonaws.com:8080/MobileService-1.0/dataService/db/";
		String entityName="USER_ACTIVATE";
		String endpointURL = AppConfigManager.getConfigVal("mobileServiceURL");
		StringBuffer serviceURL = fixURL(endpointURL, "process");
		serviceURL.append("en=").append(entityName);
		String returnStatus;
		
		try {
			Map queryParams = new HashMap();
			queryParams.put("userId", id);
			//MobileServiceClient msc = MobileServiceClient.getInstance();
			//msc.mobileDataService(entityName, queryParams, endpointURL);			

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		returnStatus = "SUCCESS";
		return returnStatus;
	}
	
	public static StringBuffer fixURL(String endpointUrl, String serviceName) {
		StringBuffer url = new StringBuffer(endpointUrl);

		if (!endpointUrl.trim().endsWith("?")) {
			url.append(serviceName);
			url.append("?");
		}
		return url;
	}
}
