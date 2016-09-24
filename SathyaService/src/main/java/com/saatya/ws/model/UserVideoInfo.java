package com.saatya.ws.model;

import java.util.List;

public class UserVideoInfo {
	
	private String trans_id;
	public String getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(String trans_id) {
		this.trans_id = trans_id;
	}
	public String getVideo_id() {
		return video_id;
	}
	@Override
	public String toString() {
		return "UserVideoInfo [trans_id=" + trans_id + ", video_id=" + video_id + ", user_id=" + user_id + ", comments=" + comments + ", rating=" + rating + ", creation_date="
				+ creation_date + ", created_by=" + created_by + ", last_update_date=" + last_update_date + ", last_updated_by=" + last_updated_by + ", attribute1=" + attribute1
				+ ", attribute2=" + attribute2 + ", attribute3=" + attribute3 + "]";
	}
	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getLast_update_date() {
		return last_update_date;
	}
	public void setLast_update_date(String last_update_date) {
		this.last_update_date = last_update_date;
	}
	public String getLast_updated_by() {
		return last_updated_by;
	}
	public void setLast_updated_by(String last_updated_by) {
		this.last_updated_by = last_updated_by;
	}
	public String getAttribute1() {
		return attribute1;
	}
	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
	public String getAttribute2() {
		return attribute2;
	}
	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
	public String getAttribute3() {
		return attribute3;
	}
	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
	private String video_id;
	private String user_id;
	private String comments;
	private String rating;
	private String creation_date;
	private String created_by;
	private String last_update_date;
	private String last_updated_by;
	private String attribute1;
	private String attribute2;
	private String attribute3;

}
