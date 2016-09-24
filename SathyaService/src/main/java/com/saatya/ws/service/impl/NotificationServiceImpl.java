package com.saatya.ws.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Message.Builder;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.saatya.ws.Notification.Helper.NotificationInfo;
import com.saatya.ws.Notification.Helper.NotifyByEmail;
import com.saatya.ws.dao.DaoFactory;
import com.saatya.ws.dao.NotificationDao;
import com.saatya.ws.dao.StudentDao;
import com.saatya.ws.dao.SubjectsDao;
import com.saatya.ws.dao.UserDao;
import com.saatya.ws.model.User;
import com.saatya.ws.response.Response;
import com.saatya.ws.response.SimpleResponse;
import com.saatya.ws.response.Status;
import com.saatya.ws.service.NotificationService;
import com.saatya.ws.util.StringUtil;

@Controller
@RequestMapping("/saatya/services/notification")
public class NotificationServiceImpl implements NotificationService {

	private final Log logger = LogFactory.getLog(getClass());

	/*
	 * @Override
	 * 
	 * @RequestMapping("/login")
	 * 
	 * public @ResponseBody Response login(
	 * 
	 * @RequestParam(value = "email") String email,
	 * 
	 * @RequestParam(value = "password") String password
	 * 
	 * ) {
	 */
	
 
	@Override
	@RequestMapping("/insertNotification")
	public @ResponseBody ResponseEntity<Response> insertNotification(
			@RequestParam(value = "userid") String userid,
			@RequestParam(value = "title_of_notify") String title_of_notify,
			@RequestParam(value = "content_of_notify") String content_of_notify
			) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		NotificationDao dao = factory.getNotificationDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("user_id", userid);
		queryParams.put("title_of_notify", title_of_notify);
		queryParams.put("content_of_notify", content_of_notify);
		
		String entityName = "NOTIFICATION_INSERT";
		
	
		boolean msgs = dao.insertNotification(entityName, queryParams);
	    SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}	
	
	@Override
	@RequestMapping("/updateNotification")
	public @ResponseBody ResponseEntity<Response> updateNotification(
			@RequestParam(value = "notify_id") String notify_id,
			@RequestParam(value = "active") String active
			) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		NotificationDao dao = factory.getNotificationDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("notify_id", notify_id);
		queryParams.put("active", active);
		
		
		String entityName = "NOTIFICATION_UPDATE";
		
	
		boolean msgs = dao.updateNotification(entityName, queryParams);
	    SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	@Override
	@RequestMapping("/readNotification")
	public @ResponseBody 
	ResponseEntity<Response> readNotification(
			@RequestParam(value = "userid") String userid
				) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		NotificationDao dao = factory.getNotificationDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("user_id", userid);
		String entityName = "NOTIFICATION_READ";
		List<Object> msgs = dao.readNotification(entityName,queryParams);
		SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);

		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	// public ResponseEntity<Response> contactUs(String name,String Email,String Subject,String Message);
	@Override
	@RequestMapping("/contactUs")
	public @ResponseBody
	ResponseEntity<Response> contactUs(
			@RequestParam(value = "name") String name,
			@RequestParam(value = "Email") String Email,
			@RequestParam(value = "Subject") String Subject,
			@RequestParam(value = "Message") String Message

	) {
		// TODO Auto-generated method stub
		NotificationInfo info = new NotificationInfo();
		// GREEN, "#00ff00" RED, "#ff0000" YELLOW, "#ffff00"
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("###Name###", name);
		map.put("###BODY###", Message);
		map.put("subject", Subject);
		map.put("Host", "");
		map.put("From", "raja_seelam@yahoo.com");
		map.put("To", "raja_seelam@yahoo.com");
		map.put("DomainName", "gmail.com");
		map.put("Cc", Email);
		map.put("Bcc", "");
		map.put("templateName", "MAIL_Notify_3.template");
		System.out.println(map);
		info.setTemplateMap(map);
		NotifyByEmail notify = new NotifyByEmail(info);
		String status = null;
		try {
			status = notify.sendMail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Successfully Sent the msg");
		// SimpleResponse reponse = new SimpleResponse(String.valueOf(status),
		// status);
		SimpleResponse reponse = new SimpleResponse(
				(!"SUCCESS".equalsIgnoreCase(status)) ? String.valueOf(Status.ERROR)
						: String.valueOf(Status.SUCCESS), status);
		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				headers, HttpStatus.OK);
		return entity;

		// return null;
	}
	//public ResponseEntity<Response> forgotPassword(String email);

	@Override
	@RequestMapping("/forgotPassword")
	public @ResponseBody
	ResponseEntity<Response> forgotPassword(
			@RequestParam(value = "email") String email
			

	) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("emailid", email);
		String entityName = "FORGOT_PASSWORD";
		List<Object> users =null;
		users =	dao.findAll(entityName, queryParams);
		String password=null;;
		if(users != null && users.size() >0){
			Object obj= (Object)users.get(0);
			if (obj instanceof User) {
				User new_name = (User) obj;
				password=new_name.getPassword();
				
			}
		}
		String status = null;
	    if(password != null){		 
		// TODO Auto-generated method stub
		NotificationInfo info = new NotificationInfo();
		// GREEN, "#00ff00" RED, "#ff0000" YELLOW, "#ffff00"
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("###FORGOT###", password);
		
		map.put("subject", "FORGOT PASSWORD NOTIFICATION EMAIL");
		map.put("Host", "");
		map.put("From", "raja_seelam@yahoo.com");
		map.put("To", email);
		map.put("DomainName", "gmail.com");
		map.put("Cc", "");
		map.put("Bcc", "");
		map.put("templateName", "MAIL_Notify_4.template");
		System.out.println(map);
		info.setTemplateMap(map);
		NotifyByEmail notify = new NotifyByEmail(info);
		
		try {
			status = notify.sendMail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Successfully Sent the msg");
	    }
		// SimpleResponse reponse = new SimpleResponse(String.valueOf(status),
		// status);
		SimpleResponse reponse = new SimpleResponse(
				(!"SUCCESS".equalsIgnoreCase(status)) ? String.valueOf(Status.ERROR)
						: String.valueOf(Status.SUCCESS), status);
		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				headers, HttpStatus.OK);
		return entity;

		// return null;
	}
	
	@Override
	@RequestMapping("/sendEMail")
	public @ResponseBody
	ResponseEntity<Response> sendEMail(
			@RequestParam(value = "userid") String userid,
			@RequestParam(value = "email") String email

	) {
		// TODO Auto-generated method stub
		NotificationInfo info = new NotificationInfo();
		// GREEN, "#00ff00" RED, "#ff0000" YELLOW, "#ffff00"
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("###USER_ID###", userid);
		map.put("subject", "SAATYA User Activation");
		map.put("Host", "");
		map.put("From", "raja_seelam@yahoo.com");
		map.put("To", email);
		map.put("DomainName", "gmail.com");
		map.put("Cc", "");
		map.put("Bcc", "");
		map.put("templateName", "MAIL_Notify_1.template");
		System.out.println(map);
		info.setTemplateMap(map);
		NotifyByEmail notify = new NotifyByEmail(info);
		String status = null;
		try {
			status = notify.sendMail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Successfully Sent the msg");
		// SimpleResponse reponse = new SimpleResponse(String.valueOf(status),
		// status);
		SimpleResponse reponse = new SimpleResponse(
				(!"SUCCESS".equalsIgnoreCase(status)) ? String.valueOf(Status.ERROR)
						: String.valueOf(Status.SUCCESS), status);
		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				headers, HttpStatus.OK);
		return entity;

		// return null;
	}

	@Override
	@RequestMapping("/openchat")
	public @ResponseBody
	ResponseEntity<Response> openchat(
			@RequestParam(value = "mentorID") String mentorID,
			@RequestParam(value = "StudentId") String StudentId,
			@RequestParam(value = "regId") String regId

	) {

		DaoFactory factory = DaoFactory.getDaoFactory();
		UserDao dao = factory.getUserDao();

		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("mentorId", mentorID);
		queryParams.put("subjectId", StudentId);

		String entityName = "STUDENT_MENTOR";
		List<Object> users = dao.findAll(entityName, queryParams);
		Map<String, String> userMap = new HashMap<String, String>();

		if (users != null && users.size() > 0) {

			for (Object obj : users) {
				if (obj instanceof User) {
					User new_name = (User) obj;

					if (new_name != null) {

						if ("M".equalsIgnoreCase(new_name.getUser_type())) {
							userMap.put("MN", new_name.getFirst_name() + " "
									+ new_name.getLast_name());
							userMap.put("ME", new_name.getEmail_id());
						} else {
							userMap.put("SN", new_name.getFirst_name() + " "
									+ new_name.getLast_name());
							userMap.put("FN", new_name.getFirst_name());
						}

					}

				}
			}

		}

		// TODO Auto-generated method stub
		NotificationInfo info = new NotificationInfo();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("###StudentName###", userMap.get("SN"));
		map.put("###FirstName###", userMap.get("FN"));
		String roomid = null;
		try {
			roomid = "" + (userMap.get("SN")).hashCode();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		map.put("###RoomId_ID###", roomid);
		map.put("###MentorName###", userMap.get("MN"));
		map.put("###Mentor_ID###", mentorID);
		map.put("###regId###", regId);

		map.put("subject", "Open chat");
		map.put("Host", "");
		map.put("From", "raja_seelam@yahoo.com");

		map.put("To", userMap.get("ME"));
		map.put("DomainName", "gmail.com");
		map.put("Cc", "");
		map.put("Bcc", "");
		map.put("templateName", "MAIL_Notify_2.template");
		System.out.println(map);
		info.setTemplateMap(map);
		NotifyByEmail notify = new NotifyByEmail(info);
		String status = null;
		try {
			status = notify.sendMail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Successfully Sent the msg");
		SimpleResponse reponse = new SimpleResponse(
				(!"SUCCESS".equalsIgnoreCase(status)) ? String.valueOf(Status.ERROR)
						: String.valueOf(Status.SUCCESS), status);
		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				headers, HttpStatus.OK);
		return entity;

	}

	@Override
	@RequestMapping("/notifyMentorIsReady")
	public @ResponseBody
	ResponseEntity<Response> notifyMentorIsReady(
			@RequestParam(value = "chatRoomId") String chatRoomId,
			@RequestParam(value = "mentorID") String mentorID,
			@RequestParam(value = "regId") String regId

	) {

		StringBuilder sb = new StringBuilder();
		// if (msg != null && !"".equals(msg) && connectedDevices.size() > 0) {
		Sender sender = new Sender("AIzaSyD3jLkwybDVe7Kv7AQODdafzVpalOVT6lQ");
		// Message message = new
		// Message.Builder().addData(GCMUtilsConstants.DATA_KEY_MSG,
		// "msg").build();

		// TODO get mentorname from mentorID
		String mentorName = "raja";

		Builder builder = new Message.Builder();
		builder.addData("msg", "msg");
		builder.addData("mentor", mentorName);
		builder.addData("roomid", chatRoomId);
		Message message = builder.build();

		System.out.println("Data==" + message.getData());
		// Set<String> regIds = connectedDevices.keySet();
		Set<String> regIds = new HashSet<String>();
		regIds.add(regId);
		// MulticastResult result = sender.send(message, new
		// ArrayList<String>()(regIds), 0);
		MulticastResult result = null;
		try {
			result = sender.send(message, new ArrayList<String>(regIds), 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// logger.info("Message sent: '{}'", msg);
		logger.info(result.toString());

		if (result.getFailure() == 0)
			sb.append("<p><strong>- Message sent:</strong> ").append("msg")
					.append("</p>");
		else
			sb.append("<p>Problem sending message: ").append(result.toString())
					.append("</p>");
		// }

		SimpleResponse reponse = new SimpleResponse(
				(!"SUCCESS".equalsIgnoreCase("SUCCESS")) ? String.valueOf(Status.ERROR)
						: String.valueOf(Status.SUCCESS), sb.toString());
		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,
				headers, HttpStatus.OK);
		return entity;

	}

	// public ResponseEntity<Response> openchat(String mentorID,String
	// StudentId);

}
