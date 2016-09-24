package com.mobile.brot.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

public class MobileServiceClient {

	private static MobileServiceClient _instance = null;
	
	public synchronized static MobileServiceClient getInstance() {
		if (_instance == null) {
			_instance = new MobileServiceClient();
		}
		return _instance;
	}
	
	
	public List mobileDataService(String entityName,
			Map<String, String> queryParams, String endpointURL)
			throws HttpException, IOException {
		List responseLst = new ArrayList();
			try {
				//LOG.debug("~~~~~~Request URL for Data Service : " + env);
				
				StringBuffer serviceURL = fixURL(endpointURL, "process");
				serviceURL.append("en=").append(entityName);
				HttpClient client = new HttpClient();
				PostMethod method = new PostMethod(serviceURL.toString());
				method.addParameter("REQ_ACCEPTED", "true");
				if (queryParams != null) {
					Iterator it = queryParams.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						if (null != entry.getValue())
							method.addParameter(entry.getKey().toString(), entry.getValue().toString());
						else
							method.addParameter(entry.getKey().toString(), "");
		
					}
				}
				int code = client.executeMethod(method);
				String jsonStringResponse = method.getResponseBodyAsString();
				System.out.println("DataService JSON Response:" + jsonStringResponse);
				if(jsonStringResponse.indexOf("RS.ROWS\":[[1]]") != -1){
					System.out.println("S");
				}else{
					System.out.println("N");
				}
		
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			return responseLst;
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
