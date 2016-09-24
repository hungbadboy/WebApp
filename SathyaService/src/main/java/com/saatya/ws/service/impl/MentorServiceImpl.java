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
import com.saatya.ws.dao.MentorDao;
import com.saatya.ws.dao.StudentDao;
import com.saatya.ws.dao.UserDao;
import com.saatya.ws.model.User;
import com.saatya.ws.response.Response;
import com.saatya.ws.response.SimpleResponse;
import com.saatya.ws.response.Status;
import com.saatya.ws.service.MentorService;
import com.saatya.ws.service.StudentService;
import com.saatya.ws.service.UserService;
import com.saatya.ws.util.CommonUtil;
import com.saatya.ws.util.StringUtil;

@Controller
@RequestMapping("/saatya/services/mentor")
public class MentorServiceImpl implements MentorService{

	private final Log logger = LogFactory.getLog(getClass());

	@Override
	@RequestMapping("/insertMentorProfile")
	public @ResponseBody ResponseEntity<Response> insertMentorProfile(
			@RequestParam(value = "userId") String userId, 
			@RequestParam(value = "dateOfBirth") String dateOfBirth,
			@RequestParam(value = "gendor") String gendor,
			@RequestParam(value = "schColleDegreeId") String schColleDegreeId,
			@RequestParam(value = "yearInCollege")  String yearInCollege,
			@RequestParam(value = "specialAccomplishments") String specialAccomplishments,
			@RequestParam(value = "tutoringInterests") String tutoringInterests,
			@RequestParam(value = "potentialCollegeMajor") String potentialCollegeMajor,
			@RequestParam(value = "childhoodAspirations") String childhoodAspirations,
			@RequestParam(value = "createdBy") String createdBy) {
		// TODO Auto-generated method stub
		DaoFactory factory = DaoFactory.getDaoFactory();
		MentorDao dao = factory.getMentorDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("userId", userId);
		queryParams.put("dateOfBirth", dateOfBirth);
		queryParams.put("gendor", gendor);
		queryParams.put("schColleDegreeId", schColleDegreeId);
		queryParams.put("yearInCollege", yearInCollege);
		queryParams.put("specialAccomplishments", specialAccomplishments);
		queryParams.put("tutoringInterests", tutoringInterests);
		queryParams.put("potentialCollegeMajor", potentialCollegeMajor);
		queryParams.put("childhoodAspirations", childhoodAspirations);
		queryParams.put("createdBy", createdBy);
		String entityName = "MENTOR_INSERT";

		
		boolean msgs = dao.insertMentor(entityName, queryParams);
		// SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}

	
	//Mentor Reviewed students implementation ( Insert)
	
	@Override
	@RequestMapping("/insMentorReviewdStud")
	public @ResponseBody ResponseEntity<Response> insMentorReviewdStud(
			@RequestParam(value = "mentorId") String mentorId, 
			@RequestParam(value = "studentId") String studentId,
			@RequestParam(value = "notes") String notes,
			@RequestParam(value = "rating") String rating
			) {
		// TODO Auto-generated method stub
		DaoFactory factory = DaoFactory.getDaoFactory();
		MentorDao dao = factory.getMentorDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("mentorId", mentorId);
		queryParams.put("studentId", studentId);
		queryParams.put("notes", notes);
		queryParams.put("rating", rating);
		String entityName = "MENTOR_REVIEWD_STUD_INSERT";

		
		boolean msgs = dao.insMentorReviewdStud(entityName, queryParams);
		// SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	//Mentor Reviewed students implementation ( update)
	@Override
	@RequestMapping("/updMentorReviewdStud")
	public @ResponseBody ResponseEntity<Response> updMentorReviewdStud(
			@RequestParam(value = "mentorId") String mentorId, 
			@RequestParam(value = "studentId") String studentId,
			@RequestParam(value = "notes") String notes,
			@RequestParam(value = "rating") String rating
			) {
		// TODO Auto-generated method stub
		DaoFactory factory = DaoFactory.getDaoFactory();
		MentorDao dao = factory.getMentorDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("mentorId", mentorId);
		queryParams.put("studentId", studentId);
		queryParams.put("notes", notes);
		queryParams.put("rating", rating);
		String entityName = "MENTOR_REVIEWD_STUD_UPDATE";

		
		boolean msgs = dao.updMentorReviewdStud(entityName, queryParams);
		// SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}

	//Mentor Reviewed students implementation ( update)
	@Override
	@RequestMapping("/getMentorReviewdStud")
	public @ResponseBody ResponseEntity<Response> getMentorReviewdStud(
			@RequestParam(value = "studentId") String studentId
			) {
		// TODO Auto-generated method stub
		DaoFactory factory = DaoFactory.getDaoFactory();
		MentorDao dao = factory.getMentorDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		//queryParams.put("mentorId", mentorId);
		queryParams.put("studentId", studentId);
		String entityName = "MENTOR_REVIEWD_STUD_SELECT";

		
		
		List<Object> msgs = dao.getOnlineMentors(entityName, queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	
	
	@Override
	@RequestMapping("/getOnlineMentorsPn")
	public @ResponseBody  ResponseEntity<Response> getOnlineMentorsPn(
			@RequestParam(value = "subjectId") String subjectId,
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
			) {
		CommonUtil util=CommonUtil.getInstance();
		Map<String, String> map=util.getLimit(pageno,limit);
		DaoFactory factory = DaoFactory.getDaoFactory();
		MentorDao dao = factory.getMentorDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectId", subjectId);
		queryParams.put("from", map.get("from"));
		queryParams.put("to", map.get("to"));
		String entityName = "MENTOR_ONLINE_READ_PN";
		List<Object> msgs = dao.getOnlineMentors(entityName, queryParams);
		String count=null;
		if("true".equalsIgnoreCase(totalCountFlag)){
			 count=util.getCount("MENTOR_ONLINE_READ_PN_COUNT", queryParams);
			
		}
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	@RequestMapping("/getCurrentMentorsOfStudentPn")
	public @ResponseBody  ResponseEntity<Response> getCurrentMentorsOfStudentPn(
			@RequestParam(value = "studentId") String studentId,
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
			) {
		CommonUtil util=CommonUtil.getInstance();
		Map<String, String> map=util.getLimit(pageno,limit);
		DaoFactory factory = DaoFactory.getDaoFactory();
		MentorDao dao = factory.getMentorDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("studentId", studentId);
		queryParams.put("from", map.get("from"));
		queryParams.put("to", map.get("to"));
		String entityName = "MENTOR_ONLINE_READ_USER_PN";
		List<Object> msgs = dao.getOnlineMentors(entityName, queryParams);
		String count=null;
		if("true".equalsIgnoreCase(totalCountFlag)){
			 count=util.getCount("MENTOR_ONLINE_READ_USER_PN_COUNT", queryParams);
			
		}
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	@RequestMapping("/getOnlineMentors")
	public @ResponseBody  ResponseEntity<Response> getOnlineMentors(
			@RequestParam(value = "subjectId") String subjectId
			
			) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		MentorDao dao = factory.getMentorDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectId", subjectId);
		
		String entityName = "MENTOR_ONLINE_READ";
		List<Object> msgs = dao.getOnlineMentors(entityName, queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
		// TODO Auto-generated method stub
		
	}

	@Override
	@RequestMapping("/searchMentorsPn")
	public @ResponseBody ResponseEntity<Response> searchMentorsPn(
			@RequestParam(value = "subjectId") String subjectId,
			@RequestParam(value = "Search") String Search,
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
			) {
		// TODO Auto-generated method stub
		CommonUtil util=CommonUtil.getInstance();
		Map<String, String> map=util.getLimit(pageno,limit);
		DaoFactory factory = DaoFactory.getDaoFactory();
		MentorDao dao = factory.getMentorDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectId", subjectId);
		queryParams.put("Search", Search);
		queryParams.put("from", map.get("from"));
		queryParams.put("to", map.get("to"));
		String entityName = "MENTOR_SEARCH_PN";
		List<Object> msgs = dao.getOnlineMentors(entityName, queryParams);
		String count=null;
		if("true".equalsIgnoreCase(totalCountFlag)){
			 count=util.getCount("MENTOR_SEARCH_PN_COUNT", queryParams);
			
		}
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
         SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
         HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
 		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
 		return entity;
	}
	
	@Override
	@RequestMapping("/searchMentors")
	public @ResponseBody ResponseEntity<Response> searchMentors(
			@RequestParam(value = "subjectId") String subjectId,
			@RequestParam(value = "Search") String Search
			
			) {
		// TODO Auto-generated method stub
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		MentorDao dao = factory.getMentorDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectId", subjectId);
		queryParams.put("Search", Search);
		
		String entityName = "MENTOR_SEARCH";
		List<Object> msgs = dao.getOnlineMentors(entityName, queryParams);
		
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
         SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
         HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
 		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
 		return entity;
	}



	@Override
	@RequestMapping("/topMetorEachSubject")
	public @ResponseBody ResponseEntity<Response> topMetorEachSubject(
			@RequestParam(value = "subjectId") String subjectId
			
			) {
		// TODO Auto-generated method stub
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		MentorDao dao = factory.getMentorDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectId", subjectId);
		
		String entityName = "TOP_MENTOR";
		List<Object> msgs = dao.getTopMentorsSubjectWise(entityName, queryParams);
		
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
         SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
         HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
 		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
 		return entity;
	}

	
	@Override
	@RequestMapping("/getList")
	public @ResponseBody  ResponseEntity<Response> getList(
			@RequestParam(value = "order") String order
			
			) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		MentorDao dao = factory.getMentorDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		//queryParams.put("subjectId", subjectId);
		//name, schol,onlie, topic
		if("name".equalsIgnoreCase(order)){
			queryParams.put("order", "first_name");
		}else if("school".equalsIgnoreCase(order)){
			queryParams.put("order", "sscd.name");
		}else if("online".equalsIgnoreCase(order)){
			queryParams.put("order", "user_status");
		}else if("topic".equalsIgnoreCase(order)){
			queryParams.put("order", "topic");
		}
		
		String entityName = "MENTOR_LIST";
		List<Object> msgs = dao.getOnlineMentors(entityName, queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	@RequestMapping("/search")
	public @ResponseBody  ResponseEntity<Response> search(
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "order") String order
			
			) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		MentorDao dao = factory.getMentorDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		
		queryParams.put("Search", keyword);
		
		if("name".equalsIgnoreCase(order)){
			queryParams.put("order", "first_name");
		}else if("school".equalsIgnoreCase(order)){
			queryParams.put("order", "sscd.name");
		}else if("online".equalsIgnoreCase(order)){
			queryParams.put("order", "user_status");
		}else if("topic".equalsIgnoreCase(order)){
			queryParams.put("order", "topic");
		}
		
		String entityName = "MENTOR_KEY_SEARCH";
		List<Object> msgs = dao.getOnlineMentors(entityName, queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
