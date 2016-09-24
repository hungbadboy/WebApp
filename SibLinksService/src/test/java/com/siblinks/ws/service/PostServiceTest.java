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

public class PostServiceTest {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	private String serviceUrl = "http://localhost:8070/siblinks/services/post/";
	private RestTemplate restTemplate = new RestTemplate();
	
	@BeforeClass
	public static void setUp() {
		String[] tr = {};
		Application.main(tr);
	}
	
	/**
	 * Test API /siblinks/services/post/createPost
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void createPost() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("authorID", 6);
		tr.put("title", "Post question");
		tr.put("subjectId", 2);
		tr.put("topicId", 4);
		tr.put("content", "Question");
		JSONObject request = new JSONObject();
		request.put("request_data_type", "post");
		request.put("request_data_method", "createPost");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "createPost", request, JSONObject.class);
		
		Assert.assertEquals("true", entity.getBody().get("status"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Create post: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/post/getSubjectIdWithTopicId
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getSubjectIdWithTopicId() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("topicId", 17);
		JSONObject request = new JSONObject();
		request.put("request_data_type", "post");
		request.put("request_data_method", "getSubjectIdWithTopicId");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getSubjectIdWithTopicId", request, JSONObject.class);

		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		
		Assert.assertEquals("2", b.get("subjectId"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get Subject Id With Topic Id: " + entity);
	}
}
