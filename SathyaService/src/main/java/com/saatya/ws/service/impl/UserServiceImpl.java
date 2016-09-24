package com.saatya.ws.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.saatya.ws.dao.UserDao;
import com.saatya.ws.model.Profile;
import com.saatya.ws.model.User;
import com.saatya.ws.model.Utility;
import com.saatya.ws.response.Response;
import com.saatya.ws.response.SimpleResponse;
import com.saatya.ws.response.Status;
import com.saatya.ws.service.NotificationService;
import com.saatya.ws.service.UserService;
import com.saatya.ws.util.StringUtil;

@Controller
@RequestMapping("/saatya/services/user")
public class UserServiceImpl implements UserService{

	private final Log logger = LogFactory.getLog(getClass());


	
	
	@Override
	@RequestMapping("/registerSN")
	public @ResponseBody ResponseEntity<Response> registerSN(
			@RequestParam(value = "email") String email,
			@RequestParam(value = "firstname") String firstname,
			@RequestParam(value = "lastname") String lastname,
			@RequestParam(value = "usertype") String usertype
			) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("email", email);
		queryParams.put("fname", firstname);
		queryParams.put("lname", lastname);
		queryParams.put("usertype", usertype);
		SimpleResponse reponse =null;
		boolean msgs=false;
		List<Object> msgs1 =null;
		String entityName1 = "GET_USERID";
		 msgs1 =	dao.findAll(entityName1, queryParams);
		 System.out.println("msgs1==="+msgs1);
		if(msgs1 == null || msgs1.size() == 0){
			String entityName = "USER_REGISTER_SN_INSERT";
			 msgs = dao.insertUser(entityName, queryParams);
			
	    }
		System.out.println("msgs==="+msgs);
		 reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),null);
		//List<Object> msgs1 =null;
		/*if(msgs){
			String entityName1 = "GET_USERID";
			 msgs1 =	dao.findAll(entityName1, queryParams);
		}*/
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs1);
		

		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}

	@Override
	@RequestMapping("/register")
	public @ResponseBody ResponseEntity<Response> register(
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
		List<Object> msgs1 =null;
		if(msgs){
			String entityName1 = "GET_USERID";
			 msgs1 =	dao.findAll(entityName1, queryParams);
		}else{
			msgs1= new ArrayList<Object>();
			msgs1.add("Email Address is Already Registered");
		}
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs1);
		SimpleResponse reponse = new SimpleResponse(msgs1 == null || !msgs  ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs1);

		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	
	@Override
	@RequestMapping("/insert1ON1Notes")
	public @ResponseBody  ResponseEntity<Response> insert1ON1Notes(
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = "mentorId") String mentorId,
			@RequestParam(value = "notes") String notes
			) {
	
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("userId", userId);
		queryParams.put("mentorId", mentorId);
		queryParams.put("notes", notes);
		
		String entityName = "1ON1_CHAT_NOTES_INSERT";
		

		boolean msgs = dao.OneONOneChatNotes(entityName, queryParams);
		
		SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	
	@Override
	@RequestMapping("/registerNotify")
	public @ResponseBody ResponseEntity<Response> registerNotify(
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
		//System.out.println("msgs=="+msgs);
		ResponseEntity<Response> reponse =null;
		List<Object> msgs1 =null;
		if(msgs){
			String entityName1 = "GET_USERID";
			 msgs1 =	dao.findAll(entityName1, queryParams);
		
		
		if(msgs1 != null && msgs1.size() >0){
			Object obj= (Object)msgs1.get(0);
			if (obj instanceof User) {
				User new_name = (User) obj;
				NotificationService service= new NotificationServiceImpl();
				//System.out.println(new_name.getUser_id());
				//System.out.println(email);
				 reponse = (ResponseEntity<Response>)service.sendEMail(new_name.getUser_id(), email);
			}
		}
	   }else{
		   
		   SimpleResponse reponse1 = new SimpleResponse(String.valueOf(Status.ERROR),	"Email Address is Already Registered");
		   HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		   reponse = new ResponseEntity<Response>(reponse1,headers, HttpStatus.OK);
	   }
		

		
		return reponse;
	}

	@Override
	@RequestMapping("/login")
	public @ResponseBody ResponseEntity<Response> login(HttpServletRequest request, 
	        HttpServletResponse response,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password
			
			) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("email", email);
		queryParams.put("password", password);
		
		Object msgs = dao.findUserByUserId("LOGIN", queryParams);
		
		// Create session if login succeeded
		if(msgs != null){
		    HttpSession session = request.getSession();
		    if(session != null){
		    	session.setAttribute("email", email);
		    }
		}
		
		SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);

		// Update user' status
		if (reponse.getStatus().equals(Status.SUCCESS)) {
			queryParams.clear();
			queryParams.put("email", email);
			queryParams.put("userStatus", "Online");
			dao.updateUser("USER_STATUS_UPDATE", queryParams);
		}

		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	@Override
	@RequestMapping("/logout")
	public @ResponseBody ResponseEntity<Response> logout(
			HttpServletRequest request, HttpServletResponse response) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		HttpSession session = request.getSession(false);
		boolean sessionExists = session != null;
		SimpleResponse reponse;
		// Log the user out and update the user's status
		if(sessionExists){
			Map<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("email", session.getAttribute("email").toString());
			queryParams.put("userStatus", "Offline");
			Object daoResponse = dao.updateUser("USER_STATUS_UPDATE", queryParams);
			reponse = new SimpleResponse(String.valueOf(Status.SUCCESS), daoResponse);
		}else{
			reponse = new SimpleResponse(String.valueOf(Status.ERROR), null);
		}
		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}

	@Override
	@RequestMapping("/studentActive")
	public @ResponseBody ResponseEntity<Response> studentActive(
			@RequestParam(value = "userid") String userid
			) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("userid", userid);
		
		
		String entityName = "STUDENT_ACTIVE";
		Object msgs = dao.updateUser(entityName, queryParams);
		SimpleResponse reponse = new SimpleResponse(((Boolean)msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);

		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	@Override
	@RequestMapping("/mentorActive")
	public @ResponseBody ResponseEntity<Response> mentorActive(
			@RequestParam(value = "userid") String userid
			) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("userid", userid);
		String entityName = "MENTOR_ACTIVE";
		Object msgs = dao.updateUser(entityName, queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
		SimpleResponse reponse = new SimpleResponse(((Boolean)msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);

		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}

	@Override
	@RequestMapping("/updateUsers")
	public @ResponseBody ResponseEntity<Response> updateUsers(
			@RequestParam(value = "userid") String userid,
			@RequestParam(value = "firtname") String firtname,
			@RequestParam(value = "lastname") String lastname, 
			@RequestParam(value = "emailid") String emailid, 
			@RequestParam(value = "active") String active) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("user_id", userid);
		queryParams.put("first_name", firtname);
		queryParams.put("last_name", lastname);
		queryParams.put("email_id", emailid);
		queryParams.put("active", active);
		String entityName = "USER_UPDATE";
		boolean msgs = dao.updateUser(entityName, queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
		SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);

		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	@Override
	@RequestMapping("/updatePassword")
	public @ResponseBody ResponseEntity<Response> updatePassword(
			@RequestParam(value = "emailid") String emailid, 
			@RequestParam(value = "password") String password) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("email_id", emailid);
		queryParams.put("password", password);
		String entityName = "UPDATE_PASSWORD";
		boolean msgs = dao.updateUser(entityName, queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
		SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);

		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}

	@Override
	@RequestMapping("/getProfile")
	public @ResponseBody  ResponseEntity<Response> getProfile(
			@RequestParam(value = "userid") String userid) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("user_id", userid);
		String entityName = "GET_PROFILE";
		List<Object> msgs =null;
		msgs = dao.getProfile(entityName, queryParams);
		
		if(msgs != null && msgs.size() >0){
			for (Object object : msgs) {
				if (object instanceof Profile) {
					Profile new_name = (Profile) object;
					if(new_name != null){
					new_name.setExtra_curricular_activityList(new_name.getExtra_curricular_activity());
					new_name.setPotential_college_majorList(new_name.getPotential_college_major());
					new_name.setSpecial_accomplishmentsList(new_name.getSpecial_accomplishments());
					}
				}
			}
		
		}
		
		
		SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);

		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	@Override
	@RequestMapping("/getLisEsayBUserId")
	public @ResponseBody  ResponseEntity<Response> getLisEsayBUserId(
			@RequestParam(value = "userid") String userid) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("user_id", userid);
		String entityName = "GET_ESAY";
		List<Object> msgs =null;
		msgs = dao.getLisEsay(entityName, queryParams);
				
		SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);

		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	
	@Override
	@RequestMapping("/educationSettings")
	public @ResponseBody  ResponseEntity<Response> saveUserEducationSettings(
			@RequestParam(value = "highSchool") String highSchool,
			@RequestParam(value = "grade") String grade,
			@RequestParam(value = "specialAccomplishments") String specialAccomplishments,
			@RequestParam(value = "potentialCollegeMajor") String potentialCollegeMajor,
			@RequestParam(value = "extraCurriculumnActivities") String extraCurriculumnActivities,
			@RequestParam(value = "wantHelpIn") String wantHelpIn,
			@RequestParam(value = "userId") String userId
			) {
	
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("sch_colle_degree_id_privacy", highSchool);
		queryParams.put("grade_privacy", grade);
		queryParams.put("special_accomplishments_privacy", specialAccomplishments);
		queryParams.put("potential_college_major_privacy", potentialCollegeMajor);
		queryParams.put("extra_curricular_activity_privacy", extraCurriculumnActivities);
		queryParams.put("want_help_in_privacy", wantHelpIn);
		queryParams.put("userId", userId);
		
		String entityName = "USER_EDU_SETTING_UPDATE";
		

		boolean msgs = dao.OneONOneChatNotes(entityName, queryParams);
		
		SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	@Override
	@RequestMapping("/updateEducationData")
	public @ResponseBody  ResponseEntity<Response> updateUserEducationData(
			@RequestParam(value = "highSchoolName") String highSchoolName,
			@RequestParam(value = "highSchoolState") String highSchoolState,
			@RequestParam(value = "grade") String grade,
			@RequestParam(value = "specialAccomplishments") String specialAccomplishments,
			@RequestParam(value = "potentialCollegeMajor") String potentialCollegeMajor,
			@RequestParam(value = "extraCurriculumnActivities") String extraCurriculumnActivities,
			@RequestParam(value = "wantHelpIn") String wantHelpIn,
			@RequestParam(value = "userId") String userId
			) {
	
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("name", highSchoolName);
		queryParams.put("state", highSchoolState);
		queryParams.put("grade", grade);
		queryParams.put("special_accomplishments", specialAccomplishments);
		queryParams.put("potential_college_major", potentialCollegeMajor);
		queryParams.put("extra_curricular_activity", extraCurriculumnActivities);
		queryParams.put("want_help_in", wantHelpIn);
		queryParams.put("userId", userId);
		
		String entityName = "USER_EDUCATION_UPDATE";
		

		boolean msgs = dao.OneONOneChatNotes(entityName, queryParams);
		
		SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}

	
	@Override
	@RequestMapping("/basicSettings")
	public @ResponseBody  ResponseEntity<Response> saveUserBasicSettings(
			@RequestParam(value = "userName") String userName,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "firstName") String firstName,
			@RequestParam(value = "lastName") String lastName,
			@RequestParam(value = "dob") String dob,
			@RequestParam(value = "bio") String bio,
			@RequestParam(value = "userId") String userId
			) {
	
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("username_privacy", userName);
		queryParams.put("email_id_privacy", email);
		queryParams.put("first_name_privacy", firstName);
		queryParams.put("last_name_privacy", lastName);
		queryParams.put("DOB_privacy", dob);
		queryParams.put("Bio_privacy", bio);
		queryParams.put("userId", userId);
		String entityName = "USER_BASIC_SETTING_UPDATE";
		

		boolean msgs = dao.OneONOneChatNotes(entityName, queryParams);
		
		SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	@Override
	@RequestMapping("/updateUserBasicData")
	public @ResponseBody  ResponseEntity<Response> updateUserBasicData(
			@RequestParam(value = "userName") String userName,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "firstName") String firstName,
			@RequestParam(value = "lastName") String lastName,
			@RequestParam(value = "dob") String dob,
			@RequestParam(value = "bio") String bio,
			@RequestParam(value = "userId") String userId
			) {
	
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("username", userName);
		queryParams.put("email_id", email);
		queryParams.put("first_name", firstName);
		queryParams.put("last_name", lastName);
		queryParams.put("DOB", dob);
		queryParams.put("Bio", bio);
		queryParams.put("userId", userId);
		String entityName = "USER_BASIC_UPDATE";
		

		boolean msgs = dao.OneONOneChatNotes(entityName, queryParams);
		
		SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	@Override
	@RequestMapping("/notifySettings")
	public @ResponseBody  ResponseEntity<Response> saveUserNotifySettings(
			@RequestParam(value = "essayNotes") String essayNotes,
			@RequestParam(value = "videoUpdates") String videoUpdates,
			@RequestParam(value = "forumQuestions") String forumQuestions,
			@RequestParam(value = "systemAlerts") String systemAlerts,
			@RequestParam(value = "OneOnOnechat") String OneOnOnechat,
			@RequestParam(value = "messages") String messages,
			@RequestParam(value = "userId") String userId
			) {
	
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("essay_notes_privacy", essayNotes);
		queryParams.put("video_updates_privacy", videoUpdates);
		queryParams.put("forum_questions_privacy", forumQuestions);
		queryParams.put("system_alerts_privacy", systemAlerts);
		queryParams.put("one_on_one_chat_req_privacy", OneOnOnechat);
		queryParams.put("messages_privacy", messages);
		queryParams.put("user_id", userId);
		String entityName = "USER_NOTIFY_SETTING_UPDATE";
		

		boolean msgs = dao.OneONOneChatNotes(entityName, queryParams);
		
		SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
		
	
	@Override
	@RequestMapping("/getSettings")
	public @ResponseBody  ResponseEntity<Response> getSettings(
			@RequestParam(value = "userid") String userid,
			@RequestParam(value = "type") String type) {
		
		String entityName = null;
		List<Object> msgs =null;
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("user_id", userid);
		queryParams.put("type", type);
		if(type.equalsIgnoreCase("education")){
			entityName = "GET_EDUCATION_SETTINGS";
			msgs = dao.getEducationSettings(entityName, queryParams);
		} else if (type.equalsIgnoreCase("notification")) {
			entityName = "GET_NOTIFICATION_SETTINGS";
			msgs = dao.getNotificationSettings(entityName, queryParams);
		} else {
			entityName = "GET_BASIC_SETTINGS";
			msgs = dao.getBasicSettings(entityName, queryParams);
		}
		
		SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);

		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	
	@Override
	@RequestMapping("/signupcomplete")
	public ResponseEntity<Response> signupcomplete(@RequestParam(value = "email") String email,@RequestParam(value = "firstname") String firstname,@RequestParam(value = "lastname")  String lastname,@RequestParam(value = "grade") String grade,
												   @RequestParam(value = "password") String password,@RequestParam(value = "dob")  String dob,@RequestParam(value = "education") String education,
												   @RequestParam(value = "accomp")  String accomp,@RequestParam(value = "colmajor") String colmajor,@RequestParam(value = "activities")  String activities,@RequestParam(value = "helpin")  String helpin,
												   @RequestParam(value = "familyincome")   String familyincome,@RequestParam(value = "yourdream") String yourdream,@RequestParam(value = "username") String username,
												   @RequestParam(value = "bio")   String bio,@RequestParam(value = "usertype")   String usertype ) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		Map<String, String> queryParams1 = new HashMap<String, String>() ;
		
		queryParams.put("email_id", email);
		queryParams.put("first_name", firstname);
		queryParams.put("last_name", lastname);
		queryParams.put("password", password);
		queryParams.put("DOB", dob);
		queryParams.put("college_degree", education);
		queryParams.put("accomplish", accomp);
		queryParams.put("college_major", colmajor);
		queryParams.put("extra_activity", activities);
		queryParams.put("helpin", helpin);
		queryParams.put("family_income", familyincome);
		queryParams.put("childhood_aspiration", yourdream);
		queryParams.put("Bio", bio);
		queryParams.put("user_type", usertype);
		queryParams.put("username", username);
		
		queryParams1.put("email", email);
		
		String entityName = "SIGNUP_COMPLETE_USER";
		String entityUser ="GET_USERID";
		String entityProfile = "SIGNUP_COMPLETE_PROFILE";
		
		List<Object> msgs1 =null;
		
		boolean msgs = dao.insertUser(entityName, queryParams);
		boolean msgs2=false;
		
		if(msgs){
			msgs1 =	dao.findAll(entityUser, queryParams1);
			if(msgs1 != null && msgs1.size() >0){
				
				Object obj= (Object)msgs1.get(0);
				if (obj instanceof User) {
					User new_name = (User) obj;
					queryParams.put("userid", new_name.getUser_id());
					queryParams.put("user_id", new_name.getUser_id());
					 msgs2 = dao.insertUser(entityProfile, queryParams);
			}
		}
		
		}	
			
		
			SimpleResponse reponse = new SimpleResponse(msgs == false ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
			HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
			ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
			return entity;		
		
		
	
	}
	
	
	@Override
	@RequestMapping("/majors")
	public ResponseEntity<Response> majors(){
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		String entityName = "GET_MAJORS";
		
			List<Object> msgs =null;
		
			msgs= dao.getMajors(entityName, queryParams);
		
			SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
			HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
			ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
			return entity;
	}
	
	@Override
	@RequestMapping("/collegesOrUniversities")
	public ResponseEntity<Response> collegesOrUniversities(){
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		String entityName = "GET_COL_UNIVERSITIES";
		
			List<Object> msgs =null;
		
			msgs= dao.getMajors(entityName, queryParams);
		
			SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
			HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
			ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
			return entity;
	}
	
	@Override
	@RequestMapping("/extracurricularActivities")
	public ResponseEntity<Response> extracurricularActivities(){
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		String entityName = "GET_EXTRA_ACTIVITIES";
		
			List<Object> msgs =null;
		
			msgs= dao.getMajors(entityName, queryParams);
		
			SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
			HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
			ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
			return entity;
	}
	
	@Override
	@RequestMapping("/tutoringInterestes")
	public ResponseEntity<Response> tutoringInterestes(){
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		String entityName = "GET_TUTORING";
		
			List<Object> msgs =null;
		
			msgs= dao.getMajors(entityName, queryParams);
		
			SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
			HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
			ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
			return entity;
	}
	
}
