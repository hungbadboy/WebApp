package com.mobile.brot.Notification.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mobile.brot.Notification.Helper.ModifyUser;
import com.mobile.brot.Notification.Helper.NotifyByEmail;
import com.mobile.brot.Notification.Helper.NotificationInfo;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


@Path("/notify/")
public class NotificationService {

	private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);
	
	@POST
	@Path("/sendEMail")
	public String sendEMail(NotificationInfo info) throws Throwable{		
		NotifyByEmail notify = new NotifyByEmail(info);
		notify.sendMail();
		LOG.info("Successfully Sent the msg");
		
		return "SUCCESS";
	}
	
	@POST
	@Path("{id}")
	public String activateUser(@PathParam("id") long id) throws Throwable {
		ModifyUser modify = new ModifyUser();
		String status = modify.activateUser(id);
		String returnStatus ;
		if(status.equalsIgnoreCase("SUCCESS")) {
			returnStatus = "Thanks. You're registered.";
		} else {
			returnStatus = "Problem in registering";
		}
		return returnStatus;
	}
}
