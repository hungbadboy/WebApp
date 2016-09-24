package com.saatya.ws.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



import com.saatya.ws.dao.DaoFactory;
import com.saatya.ws.dao.StudentDao;
import com.saatya.ws.dao.UserDao;
import com.saatya.ws.model.User;
import com.saatya.ws.response.Response;
import com.saatya.ws.response.SimpleResponse;
import com.saatya.ws.response.Status;
import com.saatya.ws.service.StudentService;
import com.saatya.ws.service.UserService;
import com.saatya.ws.util.StringUtil;

@Controller
@RequestMapping("/saatya/services/student")
public class StudentServiceImpl implements StudentService{

	private final Log logger = LogFactory.getLog(getClass());

	@Override
	@RequestMapping("/insertStudentProfile")
	public @ResponseBody ResponseEntity<Response> insertStudentProfile(
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = "dateOfBirth") String dateOfBirth,
			@RequestParam(value = "gendor") String gendor, 
			@RequestParam(value = "schColleDegreeId") String schColleDegreeId,
			@RequestParam(value = "grade") String grade,
			@RequestParam(value = "specialAccomplishments") String specialAccomplishments, 
			@RequestParam(value = "approximateFamilyIncome") String approximateFamilyIncome,
			@RequestParam(value = "ChildhoodAspiration") String ChildhoodAspiration,
			@RequestParam(value = "createdBy") String createdBy) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		StudentDao dao = factory.getStudentDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("userId", userId);
		queryParams.put("dateOfBirth", dateOfBirth);
		queryParams.put("gendor", gendor);
		queryParams.put("schColleDegreeId", schColleDegreeId);
		queryParams.put("grade", grade);
		queryParams.put("specialAccomplishments", specialAccomplishments);
		queryParams.put("approximateFamilyIncome", approximateFamilyIncome);
		queryParams.put("ChildhoodAspiration", ChildhoodAspiration);
		queryParams.put("createdBy", createdBy);
		String entityName = "STUDENT_INSERT";
		
	
		boolean msgs = dao.insertStudent(entityName, queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}

	@Override
	@RequestMapping("/editStudentProfile")
	public @ResponseBody ResponseEntity<Response> editStudentProfile(
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = "firstName") String firstName,
			@RequestParam(value = "lastName") String lastName, 
			@RequestParam(value = "password") String password,
			@RequestParam(value = "dateOfBirth") String dateOfBirth,
			@RequestParam(value = "schColleDegreeId") String schColleDegreeId,
			@RequestParam(value = "grade") String grade,
			@RequestParam(value = "specialAccomplishments") String specialAccomplishments,
			@RequestParam(value = "potentialolleMajor") String potentialolleMajor,
			@RequestParam(value = "extraCurricularActivity") String extraCurricularActivity,
			@RequestParam(value = "helpIn") String helpIn,
			@RequestParam(value = "approximateFamilyIncome") String approximateFamilyIncome,
			@RequestParam(value = "childhood_aspiration") String childhood_aspiration
			) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		StudentDao dao = factory.getStudentDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("userId", userId);
		queryParams.put("firstName", firstName);
		queryParams.put("lastName", lastName);
		queryParams.put("password", password);
		queryParams.put("dateOfBirth", dateOfBirth);
		queryParams.put("schColleDegreeId", schColleDegreeId);
		queryParams.put("grade", grade);
		queryParams.put("specialAccomplishments", specialAccomplishments);
		queryParams.put("potentialolleMajor", potentialolleMajor);
		queryParams.put("extraCurricularActivity", extraCurricularActivity);
		queryParams.put("helpIn", helpIn);
		queryParams.put("approximateFamilyIncome", approximateFamilyIncome);
		queryParams.put("childhood_aspiration", childhood_aspiration);
		String entityName = "EDIT_STUDENT_PROFILE";
		
	
		boolean msgs = dao.insertStudent(entityName, queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	

/*	@Override
	@RequestMapping("/register")
	public @ResponseBody Response register(
			@RequestParam(value = "email") String email,
			@RequestParam(value = "firstname") String firstname,
			@RequestParam(value = "lastname") String lastname,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "usertype") String usertype
			) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("email", email);
		queryParams.put("fname", firstname);
		queryParams.put("lname", lastname);
		queryParams.put("password", password);
		queryParams.put("usertype", usertype);
		String entityName = "REGISTER";
		

		boolean msgs = dao.insertUser(entityName, queryParams);
		SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);

		return reponse;
	}*/

	

	

	
	
	
	
}
