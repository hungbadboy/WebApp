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
import com.saatya.ws.dao.FeedbackDao;
import com.saatya.ws.dao.MentorDao;
import com.saatya.ws.dao.StudentDao;
import com.saatya.ws.dao.UserDao;
import com.saatya.ws.model.User;
import com.saatya.ws.response.Response;
import com.saatya.ws.response.SimpleResponse;
import com.saatya.ws.response.Status;
import com.saatya.ws.service.FeedbackService;
import com.saatya.ws.service.MentorService;
import com.saatya.ws.service.StudentService;
import com.saatya.ws.service.UserService;
import com.saatya.ws.util.CommonUtil;
import com.saatya.ws.util.StringUtil;

@Controller
@RequestMapping("/saatya/services/feedback")
public class FeedbackServiceImpl implements FeedbackService{

	private final Log logger = LogFactory.getLog(getClass());
  // String heading, String description, String feedback_by
	@Override
	@RequestMapping("/insertFeedback")
	public @ResponseBody ResponseEntity<Response> insertFeedback(
			@RequestParam(value = "heading") String heading, 
			@RequestParam(value = "description") String description,
			@RequestParam(value = "feedback_by") String feedback_by) {
		// TODO Auto-generated method stub
		DaoFactory factory = DaoFactory.getDaoFactory();
		FeedbackDao dao = factory.getFeedbackDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("heading", heading);
		queryParams.put("description", description);
		queryParams.put("feedback_by", feedback_by);
		String entityName = "FEEDBACK_INSERT";

		
		boolean msgs = dao.insertFeedback(entityName, queryParams);
		// SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}

	@Override
	@RequestMapping("/getFeedbacksPn")
	public @ResponseBody  ResponseEntity<Response> getFeedbacksPn(@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag) {
		CommonUtil util=CommonUtil.getInstance();
		Map<String, String> map=util.getLimit(pageno,limit);
		DaoFactory factory = DaoFactory.getDaoFactory();
		FeedbackDao dao = factory.getFeedbackDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("from", map.get("from"));
		queryParams.put("to", map.get("to"));
		String entityName = "FEEDBACK_READ_PN";
		List<Object> msgs = dao.getFeedbacks(entityName, queryParams);
		String count=null;
		if("true".equalsIgnoreCase(totalCountFlag)){
			 count=util.getCount("FEEDBACK_READ_PN_COUNT", queryParams);
			
		}
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
		// TODO Auto-generated method stub
		
	}
	
	@Override
	@RequestMapping("/getFeedbacks")
	public @ResponseBody  ResponseEntity<Response> getFeedbacks() {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		FeedbackDao dao = factory.getFeedbackDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		String entityName = "FEEDBACK_READ";
		List<Object> msgs = dao.getFeedbacks(entityName, queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
		// TODO Auto-generated method stub
		
	}


	





	

	

	
	
	
	
}
