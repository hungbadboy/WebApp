package com.siblinks.ws.service;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

public class NotificationUserServiceTest {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	private String serviceUrl = "http://localhost:8070/siblinks/services/notification/";
	private RestTemplate restTemplate = new RestTemplate();
	
	@BeforeClass
	public static void setUp() {
		String[] tr = {};
		Application.main(tr);
	}
	
	/**
	 * Test API /siblinks/services/notification/updateStatusNotification
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateStatusNotification() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("nid", 5);
		tr.put("status", "Y");
		JSONObject request = new JSONObject();
		request.put("request_data_type", "notification");
		request.put("request_data_method", "updateStatusNotification");
		request.put("request_data", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "updateStatusNotification", request, JSONObject.class);
		
		Assert.assertEquals(true, entity.getBody().get("request_data_result"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Update status notification: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/notification/getNotificationNotReaded
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getNotificationNotReaded() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("uid", 6);
		tr.put("status", "N");
		JSONObject request = new JSONObject();
		request.put("request_data_type", "notification");
		request.put("request_data_method", "getNotificationNotReaded");
		request.put("request_data", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getNotificationNotReaded", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		Assert.assertEquals("commentArticle", b.get("type"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get notification not readed: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/notification/getAllNotification
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getAllNotification() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("uid", 6);
		tr.put("pageno", 1);
		tr.put("limit", 7);
		JSONObject request = new JSONObject();
		request.put("request_data_type", "notification");
		request.put("request_data_method", "getAllNotification");
		request.put("request_data", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getAllNotification", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		Assert.assertEquals("commentArticle", b.get("type"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get all notification: " + entity);
	}
}
