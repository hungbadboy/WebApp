package com.saatya.ws.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.saatya.ws.biz.DataServiceManager;
import com.saatya.ws.response.Response;
import com.saatya.ws.response.SimpleResponse;
import com.saatya.ws.service.MobileService;




@Controller
@RequestMapping("/saatya/services/mobile")
public class MobileServiceImpl implements MobileService {

	private final Log logger = LogFactory.getLog(getClass());

	@Override
	@RequestMapping("/read")
	public @ResponseBody
	String sampleService(
			@RequestParam(value = "message", required = false, defaultValue = "Hello") String message) {
		System.out.println("message==="+message);
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("fname", "Somesh");
		queryParams.put("lname", "K");
		String entityName = "USER_READ";
		
	
		String drs = (String)DataServiceManager.getInstance().processEntityService(entityName, queryParams);
		
		

		return drs;
	}

	



	

}
