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

public class AdmissionServiceTest {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	private String serviceUrl = "http://localhost:8070/siblinks/services/admission/";
	private RestTemplate restTemplate = new RestTemplate();
	
	@BeforeClass
	public static void setUp() {
		String[] tr = {};
		Application.main(tr);
	}
	
	/**
	 * Test API /siblinks/services/admission/getAdmission
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getAdmission() throws JSONException {
		JSONObject tr = new JSONObject();
		JSONObject request = new JSONObject();
		request.put("request_data_type", "admission");
		request.put("request_data_method", "getAdmission");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getAdmission", request, JSONObject.class);

		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		Assert.assertEquals("Video Resources", b.get("name"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get admission: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/admission/getSubAdmission
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getSubAdmission() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("idAdmission", 1);
		JSONObject request = new JSONObject();
		request.put("request_data_type", "admission");
		request.put("request_data_method", "getAdmission");
		request.put("request_data_admission", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getSubAdmission", request, JSONObject.class);

		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		m = m.getJSONArray(0);
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		Assert.assertEquals("Before Applying", b.get("name"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get admission: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/admission/getTopicSubAdmission
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getTopicSubAdmission() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("idSubAdmission", 1);
		JSONObject request = new JSONObject();
		request.put("request_data_type", "admission");
		request.put("request_data_method", "getTopicSubAdmission");
		request.put("request_data_admission", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getTopicSubAdmission", request, JSONObject.class);

		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		m = m.getJSONArray(0);
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		Assert.assertEquals("Applying to College Info & Timeline", b.get("name"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get admission: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/admission/getAllTopicSubAdmission
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getAllTopicSubAdmission() throws JSONException {
		JSONObject tr = new JSONObject();
		JSONObject request = new JSONObject();
		request.put("request_data_type", "admission");
		request.put("request_data_method", "getAllTopicSubAdmission");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getAllTopicSubAdmission", request, JSONObject.class);

		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(3);
		Assert.assertEquals("Letter of Recommendations", b.get("name"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get admission: " + entity);
	}
}
