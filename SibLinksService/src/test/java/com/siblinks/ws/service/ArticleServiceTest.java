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

public class ArticleServiceTest {

	private final Log logger = LogFactory.getLog(getClass());
	
	private String serviceUrl = "http://localhost:8070/siblinks/services/article/";
	private RestTemplate restTemplate = new RestTemplate();
	
	@BeforeClass
	public static void setUp() {
		String[] tr = {};
		Application.main(tr);
	}
	
	/**
	 * Test API /siblinks/services/article/getArticles
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getArticles() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("idAdmission", 2);
		tr.put("pageno", 1);
		tr.put("limit", 5);
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "article");
		request.put("request_data_method", "getArticles");
		request.put("request_data_article", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getArticles", request, JSONObject.class);
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		
		Assert.assertEquals("siblinks", b.get("firstName"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get article PN: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/article/getArticleDetail
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getArticleDetail() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("arId", 52);
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "article");
		request.put("request_data_method", "getArticleDetail");
		request.put("request_data_article", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getArticleDetail", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		
		Assert.assertEquals("siblinks", b.get("firstName"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get article detail: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/article/getArticleCommentsPN
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getArticleCommentsPN() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("arId", 52);
		tr.put("pageno", 1);
		tr.put("limit", 5);
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "article");
		request.put("request_data_method", "getArticleCommentsPN");
		request.put("request_data_article", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getArticleCommentsPN", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		
		Assert.assertEquals("Tien", b.get("firstName"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get comment article: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/article/getAllArticles
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getAllArticles() throws JSONException {
		JSONObject tr = new JSONObject();
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "article");
		request.put("request_data_method", "getAllArticles");
		request.put("request_data_article", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getAllArticles", request, JSONObject.class);
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		
		Assert.assertEquals("6", b.get("authorId"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get all article: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/article/deleteArticle
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void deleteArticle() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("arId", 71);
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "article");
		request.put("request_data_method", "deleteArticle");
		request.put("request_data_article", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "deleteArticle", request, JSONObject.class);
		
		Assert.assertEquals(true, entity.getBody().get("request_data_result"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Delete article: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/article/updateArticle
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateArticle() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("arId", 55);
		tr.put("title", "Test webservice");
		tr.put("image", "/home/cheops/images/default_article.png");
		tr.put("description", "Test webservice");
		tr.put("content", "Test webservice");
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "article");
		request.put("request_data_method", "updateArticle");
		request.put("request_data_article", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "updateArticle", request, JSONObject.class);
		
		Assert.assertEquals(true, entity.getBody().get("request_data_result"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Update article: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/article/createArticle
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void createArticle() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("title", "Test webservice");
		tr.put("description", "Test webservice");
		tr.put("content", "Test webservice");
		tr.put("image", "/home/cheops/images/default_article.png");
		
		JSONObject request = new JSONObject();
		request.put("request_data_type", "article");
		request.put("request_data_method", "createArticle");
		request.put("request_data_article", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "createArticle", request, JSONObject.class);
		
		Assert.assertEquals(true, entity.getBody().get("request_data_result"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Update article: " + entity);
	}
}
