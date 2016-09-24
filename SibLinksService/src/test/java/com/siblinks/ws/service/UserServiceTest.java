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

public class UserServiceTest {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	private String serviceUrl = "http://localhost:8070/siblinks/services/user/";
	private RestTemplate restTemplate = new RestTemplate();
	
	@BeforeClass
	public static void setUp() {
		String[] tr = {};
		Application.main(tr);
	}
	
	/**
	 * Test API /siblinks/services/user/listOfMajors
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listOfMajors() throws JSONException {
		JSONObject tr = new JSONObject();
		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "listOfMajors");
		request.put("request_data", tr);

		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "listOfMajors", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		
		Assert.assertEquals("Architecture", b.get("majorName"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("List of majors: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/listOfActivity
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listOfActivity() throws JSONException {
		JSONObject tr = new JSONObject();
		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "listOfActivity");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "listOfActivity", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		
		Assert.assertEquals("Acting", b.get("activityName"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("List of activity: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/listCategory
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void listCategory() throws JSONException {
		JSONObject tr = new JSONObject();
		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "listCategory");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "listCategory", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		JSONArray m = new JSONArray(new Gson().toJson(a));
		org.json.JSONObject b = (org.json.JSONObject) m.get(0);
		Assert.assertEquals("Algebra 1", b.get("topic"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("List category: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/getTerms
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getTerms() throws JSONException {
		JSONObject tr = new JSONObject();
		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "getTerms");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getTerms", request, JSONObject.class);
		
		Assert.assertEquals("true", entity.getBody().get("status"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get terms: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/getPolicy
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getPolicy() throws JSONException {
		JSONObject tr = new JSONObject();
		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "getPolicy");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "getPolicy", request, JSONObject.class);
		
		Assert.assertEquals("true", entity.getBody().get("status"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Get Policy: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/updateUserProfileBasic
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateUserProfileBasic() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("username", "siblinks@siblinks.com");
		tr.put("firstname", "siblinks");
		tr.put("lastname", "siblinks");
		tr.put("bio", "Bio current");
		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "updateUserProfileBasic");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "updateUserProfileBasic", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		String m = (String) a.get(0);
		
		Assert.assertEquals("User Profile Basic has been updated", m);
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Update user profile basic: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/updateUserProfile
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateUserProfile() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("uid", 6);
		tr.put("username", "siblinks@siblinks.com");
		tr.put("currentclass", "Class 11");
		tr.put("accomplishments", "accomplishments");
		tr.put("majorid", "3, 6");
		tr.put("activityid", "2, 4");
		tr.put("subjectId", "4, 7");
		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "updateUserProfile");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "updateUserProfile", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		String m = (String) a.get(0);
		
		Assert.assertEquals("User Profile has been updated", m);
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Update user profile: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/signupcomplete
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void signupcomplete() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("email", "test10@gmail.com");
		tr.put("password", "siblinks2015");
		tr.put("firstname", "siblinks");
		tr.put("lastname", "siblinks");
		tr.put("usertype", "M");
		tr.put("dob", "11/11/1992");
		tr.put("education", "Harvard, Cambridge");
		tr.put("accomp", "Football Team");
		tr.put("colmajor", "3, 6");
		tr.put("activities", "2, 4");
		tr.put("helpin", "4, 7");
		tr.put("familyincome", "Family");
		tr.put("yourdream", "Your Dream");
		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "signupMentor");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "signupcomplete", request, JSONObject.class);
		
		Assert.assertEquals("true", entity.getBody().get("status"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Signup mentor: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/confirmToken
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void confirmToken() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("email", "akmyyjrnutjmjhehdanccorokudalltmxdgjolwbl");
		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "confirmToken");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "confirmToken", request, JSONObject.class);
		
		Assert.assertEquals("true", entity.getBody().get("status"));
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Confirm token: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/forgotPassword
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void forgotPassword() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("email", "siblinks@siblinks.com");
		tr.put("password", "siblinks2015");
		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "forgotPassword");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "forgotPassword", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		Object m = a.get(0);
		
		Assert.assertEquals("Password has been updated", m);
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Forgot password: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/updateAccomplishmentMobile
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateAccomplishmentMobile() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("uid", 54);
		tr.put("accomplishments", "accomplishments");

		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "updateAccomplishmentMobile");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "updateAccomplishmentMobile", request, JSONObject.class);
		
		Object a = entity.getBody().get("request_data_result");

		Assert.assertEquals(true, a);
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Update user accomplishment: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/updateMajorsMobile
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateMajorsMobile() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("uid", 54);
		tr.put("majorid", "2,5,3");

		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "updateMajorsMobile");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "updateMajorsMobile", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		String m = (String) a.get(0);
		
		Assert.assertEquals("Major has been updated", m);
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Update user majors: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/updateActivitiesMobile
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateActivitiesMobile() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("uid", 54);
		tr.put("activityid", "2,4");

		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "updateActivitiesMobile");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "updateActivitiesMobile", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		String m = (String) a.get(0);
		
		Assert.assertEquals("Activities has been updated", m);
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Update user activities: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/updateHelpInMobile
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateHelpInMobile() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("uid", 54);
		tr.put("topicId", "4,7");

		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "updateHelpInMobile");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "updateHelpInMobile", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		String m = (String) a.get(0);
		
		Assert.assertEquals("HelpIn has been updated", m);
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Update user help in: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/updateGradeMobile
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateGradeMobile() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("uid", 54);
		tr.put("currentclass", "Class 11");

		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "updateGradeMobile");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "updateGradeMobile", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		String m = (String) a.get(0);
		
		Assert.assertEquals("Grade has been updated", m);
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Update user grade: " + entity);
	}
	
	/**
	 * Test API /siblinks/services/user/updateSchoolMobile
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateSchoolMobile() throws JSONException {
		JSONObject tr = new JSONObject();
		tr.put("uid", 54);
		tr.put("education", "Harvard, Cambridge");

		JSONObject request = new JSONObject();
		request.put("request_data_type", "user");
		request.put("request_data_method", "updateSchoolMobile");
		request.put("request_data", tr);
		
		ResponseEntity<JSONObject> entity = restTemplate.postForEntity(serviceUrl + "updateSchoolMobile", request, JSONObject.class);
		
		ArrayList a = (ArrayList) entity.getBody().get("request_data_result");
		String m = (String) a.get(0);
		
		Assert.assertEquals("School has been updated", m);
		Assert.assertNotEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
		logger.info("Update user school: " + entity);
	}
}
