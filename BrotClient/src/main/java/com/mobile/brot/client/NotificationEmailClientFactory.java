package com.mobile.brot.client;

public class NotificationEmailClientFactory {

	/**
	 * Returns a local client
	 * 
	 * @param endPointUrl The endPointUrl of Notification Service
	 * @return
	 */
	public static NotificationEmailClient getNotifEmailClient(String endPointUrl) {
		return NotificationEmailClient.getInstance(endPointUrl);
	}
	
}
