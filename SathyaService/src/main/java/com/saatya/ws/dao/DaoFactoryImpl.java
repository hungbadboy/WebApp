package com.saatya.ws.dao;

import com.saatya.ws.dao.impl.AnswersDaoImpl;
import com.saatya.ws.dao.impl.FAQDaoImpl;
import com.saatya.ws.dao.impl.FeedbackDaoImpl;
import com.saatya.ws.dao.impl.MentorDaoImpl;
import com.saatya.ws.dao.impl.NotificationDaoImpl;
import com.saatya.ws.dao.impl.QAFollowInfoDaoImpl;
import com.saatya.ws.dao.impl.QAVoteInfoDaoImpl;
import com.saatya.ws.dao.impl.QuestionsDaoImpl;
import com.saatya.ws.dao.impl.SchoolCollegeDegreeDaoImpl;
import com.saatya.ws.dao.impl.StudentDaoImpl;
import com.saatya.ws.dao.impl.SubjectsDaoImpl;
import com.saatya.ws.dao.impl.UploadDownloadDaoImpl;
import com.saatya.ws.dao.impl.UserDaoImpl;
import com.saatya.ws.dao.impl.UserVideoInfoDaoImpl;
import com.saatya.ws.dao.impl.VideoChatDaoImpl;





public class DaoFactoryImpl extends DaoFactory{

	@Override
	public UserDao getUserDao() {
		// TODO Auto-generated method stub
		return  new UserDaoImpl();
	}

	@Override
	public AnswersDao getAnswersDao() {
		// TODO Auto-generated method stub
		return  new AnswersDaoImpl();
	}

	@Override
	public MentorDao getMentorDao() {
		// TODO Auto-generated method stub
		return  new MentorDaoImpl();
	}

	@Override
	public QAFollowInfoDao getQAFollowInfoDao() {
		// TODO Auto-generated method stub
		return  new QAFollowInfoDaoImpl();
	}

	@Override
	public QAFollowInfoDao updateQAViews() {
		// TODO Auto-generated method stub
		return  new QAFollowInfoDaoImpl();
	}
	
	@Override
	public QAVoteInfoDao getQAVoteInfoDao() {
		// TODO Auto-generated method stub
		return  new QAVoteInfoDaoImpl();
	}

	@Override
	public QuestionsDao getQuestionsDao() {
		// TODO Auto-generated method stub
		return  new QuestionsDaoImpl();
	}

	@Override
	public SchoolCollegeDegreeDao getSchoolCollegeDegreeDao() {
		// TODO Auto-generated method stub
		return  new SchoolCollegeDegreeDaoImpl();
	}

	@Override
	public StudentDao getStudentDao() {
		// TODO Auto-generated method stub
		return  new StudentDaoImpl();
	}

	@Override
	public UserVideoInfoDao getVideoInfoDao() {
		// TODO Auto-generated method stub
		return  new UserVideoInfoDaoImpl();
	}

	@Override
	public UserVideoInfoDao getUserVideoInfoDao() {
		// TODO Auto-generated method stub
		return  new UserVideoInfoDaoImpl();
	}

	@Override
	public SubjectsDao getSubjectsDao() {
		// TODO Auto-generated method stub
		return new SubjectsDaoImpl();
	}

	@Override
	public VideoChatDao getVideoChatDao() {
		// TODO Auto-generated method stub
		return new VideoChatDaoImpl();
	}
	@Override
	public FeedbackDao getFeedbackDao() {
		// TODO Auto-generated method stub
		return new FeedbackDaoImpl();
	}
	@Override
	public UploadDownloadDao getUploadDownloadDao() {
		// TODO Auto-generated method stub
		return new UploadDownloadDaoImpl();
	}
	@Override
	public FAQDao getFAQDao() {
		// TODO Auto-generated method stub
		return new FAQDaoImpl();
	}
	@Override
	public NotificationDao getNotificationDao() {
		// TODO Auto-generated method stub
		return new NotificationDaoImpl();
	}
	

}
