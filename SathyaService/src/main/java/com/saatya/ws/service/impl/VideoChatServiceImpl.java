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
import com.saatya.ws.dao.VideoChatDao;
import com.saatya.ws.model.User;
import com.saatya.ws.response.Response;
import com.saatya.ws.response.SimpleResponse;
import com.saatya.ws.response.Status;
import com.saatya.ws.service.MentorService;
import com.saatya.ws.service.StudentService;
import com.saatya.ws.service.UserService;
import com.saatya.ws.service.VideoChatService;
import com.saatya.ws.util.StringUtil;

@Controller
@RequestMapping("/saatya/services/videochat")
public class VideoChatServiceImpl implements VideoChatService{

	private final Log logger = LogFactory.getLog(getClass());

	@Override
	@RequestMapping("/saveChatHistory")
	public @ResponseBody ResponseEntity<Response> saveChatHistory(
			@RequestParam(value = "subjectId") String subjectId, 
			@RequestParam(value = "senderId") String senderId,
			@RequestParam(value = "receiverId") String receiverId, 
			@RequestParam(value = "messageTest") String messageTest) {
		// TODO Auto-generated method stub
		DaoFactory factory = DaoFactory.getDaoFactory();
		VideoChatDao dao = factory.getVideoChatDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectId", subjectId);
		queryParams.put("senderId", senderId);
		queryParams.put("receiverId", receiverId);
		queryParams.put("messageTest", messageTest);
        String entityName = "VIDEO_CHAT_INSERT";
		boolean msgs = dao.saveChatHistory(entityName, queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
		
	}
	
	@Override
	@RequestMapping("/saveNotes")
	public @ResponseBody ResponseEntity<Response> saveNotes(
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = "notesMsg") String notesMsg) {
		// TODO Auto-generated method stub
		DaoFactory factory = DaoFactory.getDaoFactory();
		VideoChatDao dao = factory.getVideoChatDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("userId", userId);
		queryParams.put("notesMsg", notesMsg);
        String entityName = "VIDEO_CHAT_NOTES_INSERT";
		boolean msgs = dao.saveNotes(entityName, queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
		
	}


	
}
