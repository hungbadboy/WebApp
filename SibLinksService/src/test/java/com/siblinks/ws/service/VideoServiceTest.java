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


public class VideoServiceTest {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	private final String serviceUrl = "http://localhost:8070/siblinks/services/video/";
	private final RestTemplate restTemplate = new RestTemplate();
	
	@BeforeClass
	public static void setUp() {
		String[] tr = {};
		Application.main(tr);
	}
	
	/**
	 * Test API /siblinks/services/video/getVideosListByUser
	 * @throws JSONException 
	 */
	@Test
	public void getVideosListByUser() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("uid", 6);
		tr.put("pageno", 1);
		tr.put("limit", 5);
		tr.put("totalCountFlag", true);
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "video");
		request.put("request_data_method", "getVideosListByUser");
		request.put("request_data", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getVideosListByUser", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		
		Assert.assertEquals("siblinks@siblinks.com", b.get("author"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get list videos by user: " + entity);
	}
	
	    /**
     * Test API /siblinks/services/video/getHistoryVideosList
     * 
     * @throws JSONException
     */
    @Test
    public void getHistoryVideosList() throws JSONException {
        JSONObject tr = new JSONObject();
        tr.put("uid", 94);
        tr.put("pageno", 1);
        tr.put("limit", 5);
        tr.put("totalCountFlag", true);

        JSONObject request = new JSONObject();
        request.put("request_data_type", "video");
        request.put("request_data_method", "getVideosListByUser");
        request.put("request_data", tr);

        ResponseEntity<JSONObject> entity = restTemplate.postForEntity(
            serviceUrl + "getVideosListByUser",
            request,
            JSONObject.class);

        ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
        JSONArray m = new JSONArray(new Gson().toJson(a));

        org.json.JSONObject b = (org.json.JSONObject) m.get(0);

        Assert.assertEquals("siblinks@siblinks.com", b.get("author"));
        Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
        logger.info("Get list videos by user: " + entity);
    }

    /**
     * Test API /siblinks/services/video/updateVideo
     * 
     * @throws JSONException
     */
	@Test
	public void updateVideo() {
		JSONObject tr = new JSONObject();
		tr.put("vid", 46);
		tr.put("title", "RNA Update");
		tr.put("description", "RNA Update");
		JSONObject request = new JSONObject();
		request.put("request_data_type", "video");
		request.put("request_data_method", "updateVideo");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "updateVideo", request, JSONObject.class);
	
		Assert.assertEquals("Done", entity.getBody().get("request_data_result"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Update infor video: " + entity);
	}
}
