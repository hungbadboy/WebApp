package com.saatya.ws.model;

import java.util.List;

public class UserNotificationSettings {

	private String essay_notes_privacy ; 
	private String video_updates_privacy; 
	private String forum_questions_privacy; 
	private String system_alerts_privacy ; 
	private String one_on_one_chat_req_privacy; 
	private String messages_privacy ;

	

	@Override
	public String toString() {
		return "UserNotificationSettings [essay_notes_privacy="
				+ essay_notes_privacy + ", video_updates_privacy="
				+ video_updates_privacy + ", forum_questions_privacy="
				+ forum_questions_privacy + ", system_alerts_privacy="
				+ system_alerts_privacy + ", one_on_one_chat_req_privacy="
				+ one_on_one_chat_req_privacy + ", messages_privacy="
				+ messages_privacy + "]";
	}

	public String getEssay_notes_privacy() {
		return essay_notes_privacy;
	}

	public void setEssay_notes_privacy(String essay_notes_privacy) {
		this.essay_notes_privacy = essay_notes_privacy;
	}

	public String getVideo_updates_privacy() {
		return video_updates_privacy;
	}

	public void setVideo_updates_privacy(String video_updates_privacy) {
		this.video_updates_privacy = video_updates_privacy;
	}

	public String getForum_questions_privacy() {
		return forum_questions_privacy;
	}

	public void setForum_questions_privacy(String forum_questions_privacy) {
		this.forum_questions_privacy = forum_questions_privacy;
	}

	public String getSystem_alerts_privacy() {
		return system_alerts_privacy;
	}

	public void setSystem_alerts_privacy(String system_alerts_privacy) {
		this.system_alerts_privacy = system_alerts_privacy;
	}

	public String getOne_on_one_chat_req_privacy() {
		return one_on_one_chat_req_privacy;
	}

	public void setOne_on_one_chat_req_privacy(String one_on_one_chat_req_privacy) {
		this.one_on_one_chat_req_privacy = one_on_one_chat_req_privacy;
	}

	public String getMessages_privacy() {
		return messages_privacy;
	}

	public void setMessages_privacy(String messages_privacy) {
		this.messages_privacy = messages_privacy;
	}
	
}
