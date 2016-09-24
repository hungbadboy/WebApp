package com.saatya.ws.dao;



public abstract class DaoFactory {

	public abstract UserDao  getUserDao();
	public abstract AnswersDao getAnswersDao();
	public abstract MentorDao getMentorDao();
	public abstract QAFollowInfoDao getQAFollowInfoDao();
	public abstract QAVoteInfoDao getQAVoteInfoDao();
	public abstract QAFollowInfoDao updateQAViews();
	public abstract QuestionsDao getQuestionsDao();
	public abstract SchoolCollegeDegreeDao getSchoolCollegeDegreeDao();
	public abstract StudentDao getStudentDao();
	public abstract UserVideoInfoDao getVideoInfoDao();
	public abstract UserVideoInfoDao getUserVideoInfoDao();
	public abstract SubjectsDao getSubjectsDao();
	public abstract VideoChatDao getVideoChatDao();
	public abstract FeedbackDao getFeedbackDao();
	public abstract UploadDownloadDao getUploadDownloadDao();
	public abstract FAQDao getFAQDao();
	public abstract NotificationDao getNotificationDao();
	
	
	public static DaoFactory getDaoFactory()
	{
		DaoFactory daoFactory = new DaoFactoryImpl();
		return daoFactory;
	}
}
