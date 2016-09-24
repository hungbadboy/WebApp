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

public class VideoAdmissionServiceTest {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	private String serviceUrl = "http://localhost:8070/siblinks/services/videoAdmission/";
	private RestTemplate restTemplate = new RestTemplate();
	
	@BeforeClass
	public static void setUp() {
		String[] tr = {};
		Application.main(tr);
	}
	
	/**
	 * Test API /siblinks/services/videoAdmission/getVideoTopicSubAdmissionPN
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getVideoTopicSubAdmissionPN() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("idTopicSubAdmission", 1);
		tr.put("pageno", 1);
		tr.put("limit", 5);
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "videoAdmission");
		request.put("request_data_method", "getVideoTopicSubAdmissionPN");
		request.put("request_data_videoAdmission", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getVideoTopicSubAdmissionPN", request, JSONObject.class);
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		
		Assert.assertEquals("siblinks", b.get("firstName"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get list videos by user: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/videoAdmission/getVideoTopicSubAdmission
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getVideoTopicSubAdmission() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("idTopicSubAdmission", 1);
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "videoAdmission");
		request.put("request_data_method", "getVideoTopicSubAdmission");
		request.put("request_data_videoAdmission", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getVideoTopicSubAdmission", request, JSONObject.class);
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		
		Assert.assertEquals("siblinks", b.get("firstName"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get list videos by user: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/videoAdmission/getVideoAdmissionDetail
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getVideoAdmissionDetail() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("idSubAdmission", 1);
		tr.put("idTopicSubAdmission", 1);
		tr.put("vId", 1);
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "videoAdmission");
		request.put("request_data_method", "getVideoAdmissionDetail");
		request.put("request_data_videoAdmission", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getVideoAdmissionDetail", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		
		Assert.assertEquals("Choosing Where to Apply", b.get("title"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get list videos by user: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/videoAdmission/getVideoAdmissionCommentsPN
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getVideoAdmissionCommentsPN() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("vId", 7);
		tr.put("pageno", 1);
		tr.put("limit", 5);
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "videoAdmission");
		request.put("request_data_method", "getVideoAdmissionCommentsPN");
		request.put("request_data_videoAdmission", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getVideoAdmissionCommentsPN", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		
		Assert.assertEquals("Tien", b.get("firstName"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get list videos by user: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/videoAdmission/orderVideoAdmissionPN
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void orderVideoAdmissionPN() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("idTopicSubAdmission", 1);
		tr.put("order", "syllabus");
		tr.put("pageno", 1);
		tr.put("limit", 10);
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "videoAdmission");
		request.put("request_data_method", "orderVideoAdmissionPN");
		request.put("request_data_videoAdmission", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "orderVideoAdmissionPN", request, JSONObject.class);
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		
		Assert.assertEquals("siblinks", b.get("firstName"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get list videos by user: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/videoAdmission/updateViewVideoAdmission
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateViewVideoAdmission() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("vId", 1);
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "videoAdmission");
		request.put("request_data_method", "updateViewVideoAdmission");
		request.put("request_data_videoAdmission", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "updateViewVideoAdmission", request, JSONObject.class);
		
		String b = (String) entity.getBody().get("request_data_result");
		
		Assert.assertEquals("Done", b);
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get list videos by user: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/videoAdmission/deleteVideoAdmission
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void deleteVideoAdmission() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("vId", 73);
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "videoAdmission");
		request.put("request_data_method", "deleteVideoAdmission");
		request.put("request_data_videoAdmission", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "deleteVideoAdmission", request, JSONObject.class);
		
		Assert.assertEquals(true, entity.getBody().get("request_data_result"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get list videos by user: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/videoAdmission/updateVideoAdmission
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateVideoAdmission() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("vId", 33);
		tr.put("title", "Test webservice");
		tr.put("youtubeUrl", "https://www.youtube.com/watch?v=PHfZW9yZyQQ");
		tr.put("runningTime", "(1:30)");
		tr.put("image", "https://img.youtube.com/vi/PHfZW9yZyQQ/2.jpg");
		tr.put("description", "Test webservice");
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "videoAdmission");
		request.put("request_data_method", "updateVideoAdmission");
		request.put("request_data_videoAdmission", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "updateVideoAdmission", request, JSONObject.class);
		
		Assert.assertEquals(true, entity.getBody().get("request_data_result"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get list videos by user: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/videoAdmission/createVideoAdmission
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void createVideoAdmission() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("authorId", 6);
		tr.put("title", "Test webservice");
		tr.put("youtubeUrl", "https://www.youtube.com/watch?v=PHfZW9yZyQQ");
		tr.put("runningTime", "(1:30)");
		tr.put("image", "https://img.youtube.com/vi/PHfZW9yZyQQ/2.jpg");
		tr.put("description", "Test webservice");
		tr.put("idTopicSubAdmission", 1);
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "videoAdmission");
		request.put("request_data_method", "createVideoAdmission");
		request.put("request_data_videoAdmission", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "createVideoAdmission", request, JSONObject.class);
		
		Assert.assertEquals(true, entity.getBody().get("request_data_result"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get list videos by user: " + entity);
	}
}
