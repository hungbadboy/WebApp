package com.siblinks.ws.model;

import java.awt.Menu;

/**
 * @author Hima
 *
 */
public class RequestData{
	
	private String request_data_type;
	private String request_data_method;
	private Video request_data;
	private Admission request_data_admission;
	private Article request_data_article;
	private VideoAdmission request_data_videoAdmission;
	private AnswerArticle request_data_answerArticle;
	private AboutMentor request_data_aboutMentor;
    private Menu request_data_menu;
    private Playlist request_playlist;
    private Contact contact_data;

    public Contact getContact_data() {
        return contact_data;
    }

    public void setContact_data(final Contact contact_data) {
        this.contact_data = contact_data;
    }

    private Subscribe request_subscribe;
    private User request_user;
	
    public User getRequest_user() {
        return request_user;
    }

    public void setRequest_user(final User request_user) {
        this.request_user = request_user;
    }

    public Playlist getRequest_playlist() {
        return request_playlist;
    }

    public void setRequest_playlist(final Playlist request_playlist) {
        this.request_playlist = request_playlist;
    }

    public Menu getRequest_data_menu() {
        return request_data_menu;
    }

    public void setRequest_data_menu(final Menu request_data_menu) {
        this.request_data_menu = request_data_menu;
    }

    public String getRequest_data_type() {
		return request_data_type;
	}
	public void setRequest_data_type(final String request_data_type) {
		this.request_data_type = request_data_type;
	}
	public String getRequest_data_method() {
		return request_data_method;
	}
	public void setRequest_data_method(final String request_data_method) {
		this.request_data_method = request_data_method;
	}
	public Video getRequest_data() {
		return request_data;
	}
	public void setRequest_data(final Video request_data) {
		this.request_data = request_data;
	}
	public Admission getRequest_data_admission() {
		return request_data_admission;
	}
	public void setRequest_data_admission(final Admission request_data_admission) {
		this.request_data_admission = request_data_admission;
	}
	public Article getRequest_data_article() {
		return request_data_article;
	}
	public void setRequest_data_article(final Article request_data_article) {
		this.request_data_article = request_data_article;
	}
	public VideoAdmission getRequest_data_videoAdmission() {
		return request_data_videoAdmission;
	}
	public void setRequest_data_videoAdmission(
			final VideoAdmission request_data_videoAdmission) {
		this.request_data_videoAdmission = request_data_videoAdmission;
	}
	public AnswerArticle getRequest_data_answerArticle() {
		return request_data_answerArticle;
	}
	public void setRequest_data_answerArticle(final AnswerArticle request_data_answerArticle) {
		this.request_data_answerArticle = request_data_answerArticle;
	}
	public AboutMentor getRequest_data_aboutMentor() {
		return request_data_aboutMentor;
	}
	public void setRequest_data_aboutMentor(final AboutMentor request_data_aboutMentor) {
		this.request_data_aboutMentor = request_data_aboutMentor;
	}

    public Subscribe getRequest_subscribe() {
        return request_subscribe;
    }

    public void setRequest_subscribe(final Subscribe request_subscribe) {
        this.request_subscribe = request_subscribe;
    }
}
