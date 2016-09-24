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
import com.saatya.ws.dao.FAQDao;
import com.saatya.ws.dao.SubjectsDao;
import com.saatya.ws.dao.UserDao;
import com.saatya.ws.model.User;
import com.saatya.ws.response.Response;
import com.saatya.ws.response.SimpleResponse;
import com.saatya.ws.response.Status;
import com.saatya.ws.service.SubjectsService;
import com.saatya.ws.service.UserService;
import com.saatya.ws.service.faqService;
import com.saatya.ws.util.CommonUtil;
import com.saatya.ws.util.StringUtil;

@Controller
@RequestMapping("/saatya/services/faqs")
public class faqServiceImpl implements faqService{

	private final Log logger = LogFactory.getLog(getClass());

	
	

	

	
	
	@Override
	@RequestMapping("/fetchFaqs")
	public @ResponseBody  ResponseEntity<Response> fetchFaqs(
			
		    ) {
		       
		        DaoFactory factory = DaoFactory.getDaoFactory();
				FAQDao dao = factory.getFAQDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				String entityName = "FETCH_FAQ";
				List<Object> msgs = dao.fetchFaqs(entityName,queryParams);
	            SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
                HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
        		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
        		return entity;
	}
	



	


	

	@Override
	@RequestMapping("/insertFaq")
	public @ResponseBody ResponseEntity<Response> insertFaq(
			@RequestParam(value="question") String question, 
			@RequestParam(value="answer") String answer
			) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		FAQDao dao = factory.getFAQDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("question_text", question);
		queryParams.put("answer_text", answer);
		String entityName = "INSERT_FAQ";

		boolean msgs = dao.insertFaq(entityName,queryParams);
	    SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);

        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
		
	
	
	
}
