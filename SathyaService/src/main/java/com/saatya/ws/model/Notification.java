package com.saatya.ws.model;


public class Notification {
	
	private String notify_id;
	public String getNotify_id() {
		return notify_id;
	}
	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getTitle_of_notify() {
		return title_of_notify;
	}
	public void setTitle_of_notify(String title_of_notify) {
		this.title_of_notify = title_of_notify;
	}
	public String getContent_of_notify() {
		return content_of_notify;
	}
	public void setContent_of_notify(String content_of_notify) {
		this.content_of_notify = content_of_notify;
	}
	public String getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getLast_update_date() {
		return last_update_date;
	}
	public void setLast_update_date(String last_update_date) {
		this.last_update_date = last_update_date;
	}
	private String user_id;
	@Override
	public String toString() {
		return "Notification [notify_id=" + notify_id + ", user_id=" + user_id + ", title_of_notify=" + title_of_notify + ", content_of_notify=" + content_of_notify
				+ ", creation_date=" + creation_date + ", active=" + active + ", last_update_date=" + last_update_date + "]";
	}
	private String title_of_notify;
	private String content_of_notify;
	private String creation_date;
	private String active;
	private String last_update_date;
	

	
}
