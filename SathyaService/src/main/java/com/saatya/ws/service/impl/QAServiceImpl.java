package com.saatya.ws.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.saatya.ws.dao.DaoFactory;
import com.saatya.ws.dao.QAFollowInfoDao;
import com.saatya.ws.dao.QAVoteInfoDao;
import com.saatya.ws.dao.QuestionsDao;
import com.saatya.ws.dao.SubjectsDao;
import com.saatya.ws.response.Response;
import com.saatya.ws.response.SimpleResponse;
import com.saatya.ws.response.Status;
import com.saatya.ws.service.QAService;
import com.saatya.ws.util.CommonUtil;
import com.saatya.ws.util.StringUtil;

@Controller
@RequestMapping("/saatya/services/qa")
public class QAServiceImpl implements QAService{

	@Override
	@RequestMapping("/getAnswersPn")
	public @ResponseBody ResponseEntity<Response> getAnswersPn( @RequestParam(value = "subject") String subject,
											  @RequestParam(value = "question") String question,
											  @RequestParam(value="pageno") String pageno,
											  @RequestParam(value="limit") String limit,
											  @RequestParam(value="totalCountFlag") String totalCountFlag
											  ) {
		CommonUtil util=CommonUtil.getInstance();
		Map<String, String> map=util.getLimit(pageno,limit);
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectid", subject);
		queryParams.put("questionid", question);
		queryParams.put("from", map.get("from"));
		queryParams.put("to", map.get("to"));
		String entityName = "GET_ANSWERS_PN";
		List<Object> msgs = dao.getAnswers(entityName,queryParams);
		String count=null;
		if("true".equalsIgnoreCase(totalCountFlag)){
			 count=util.getCount("GET_ANSWERS_PN_COUNT", queryParams);
			
		}
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
		SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
		
		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	@Override
	@RequestMapping("/getAnswers")
	public @ResponseBody ResponseEntity<Response> getAnswers( @RequestParam(value = "subject") String subject,
											  @RequestParam(value = "question") String question
											 
											  ) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectid", subject);
		queryParams.put("questionid", question);
		String entityName = "GET_ANSWERS";
		List<Object> msgs = dao.getAnswers(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
		SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
		
		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}

	@Override
	@RequestMapping("/getLatestQuestionPn")
	public @ResponseBody ResponseEntity<Response> getLatestQuestionPn(
			@RequestParam(value = "subjectId") String subjectId,
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
		) {
		CommonUtil util=CommonUtil.getInstance();
		Map<String, String> map=util.getLimit(pageno,limit);
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectId", subjectId);
		queryParams.put("from", map.get("from"));
		queryParams.put("to", map.get("to"));
		String entityName = "LATEST_QUESTIONS_PN";
		List<Object> msgs = dao.getLatestQuestion(entityName,queryParams);
		String count=null;
		if("true".equalsIgnoreCase(totalCountFlag)){
			 count=util.getCount("LATEST_QUESTIONS_PN_COUNT", queryParams);
			
		}
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	
	@Override
	@RequestMapping("/getLatestQuesOfCategoryPn")
	public @ResponseBody ResponseEntity<Response> getLatestQuesOfCategoryPn(
			@RequestParam(value = "subjectId") String subjectId,
			@RequestParam(value = "categoryId") String categoryId,
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
		) {
		CommonUtil util=CommonUtil.getInstance();
		Map<String, String> map=util.getLimit(pageno,limit);
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectId", subjectId);
		queryParams.put("categoryId", categoryId);
		queryParams.put("from", map.get("from"));
		queryParams.put("to", map.get("to"));
		String entityName = "LATEST_QUESTIONS_CATEGORY_PN";
		List<Object> msgs = dao.getLatestQuestion(entityName,queryParams);
		String count=null;
		if("true".equalsIgnoreCase(totalCountFlag)){
			 count=util.getCount("LATEST_QUESTIONS_CATEGORY_PN_COUNT", queryParams);
			
		}
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	@Override
	@RequestMapping("/getLatestQuestion")
	public @ResponseBody ResponseEntity<Response> getLatestQuestion(
			@RequestParam(value = "subjectId") String subjectId
			
		) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectId", subjectId);
		String entityName = "LATEST_QUESTIONS";
		List<Object> msgs = dao.getLatestQuestion(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}

	@Override
	@RequestMapping("/getLatestUserQuestionsPn")
	public @ResponseBody ResponseEntity<Response> getLatestUserQuestionsPn(
			@RequestParam(value = "subjectId") String subjectId,
			@RequestParam(value = "userId") String userId,
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
			) {
		CommonUtil util=CommonUtil.getInstance();
		Map<String, String> map=util.getLimit(pageno,limit);
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectId", subjectId);
		queryParams.put("userid", userId);
		queryParams.put("from", map.get("from"));
		queryParams.put("to", map.get("to"));
		String entityName = "LATEST_USER_QUESTIONS_PN";
		List<Object> msgs = dao.getLatestUserQuestions(entityName,queryParams);
		String count=null;
		if("true".equalsIgnoreCase(totalCountFlag)){
			 count=util.getCount("LATEST_USER_QUESTIONS_PN_COUNT", queryParams);
			
		}
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	
	@Override
	@RequestMapping("/getLatestAllUserQuestionsPn")
	public @ResponseBody ResponseEntity<Response> getLatestAllUserQuestionsPn(			
			@RequestParam(value = "userId") String userId,
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
			) {
		CommonUtil util=CommonUtil.getInstance();
		Map<String, String> map=util.getLimit(pageno,limit);
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("userid", userId);
		queryParams.put("from", map.get("from"));
		queryParams.put("to", map.get("to"));
		String entityName = "LATEST_ALL_USER_QUESTIONS_PN";
		List<Object> msgs = dao.getLatestUserQuestions(entityName,queryParams);
		String count=null;
		if("true".equalsIgnoreCase(totalCountFlag)){
			 count=util.getCount("LATEST_ALL_USER_QUESTIONS_PN_COUNT", queryParams);
			
		}
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	@Override
	@RequestMapping("/getLatestUserQuestions")
	public @ResponseBody ResponseEntity<Response> getLatestUserQuestions(
			@RequestParam(value = "subjectId") String subjectId,
			@RequestParam(value = "userId") String userId
			
			) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectId", subjectId);
		queryParams.put("userid", userId);
		
		String entityName = "LATEST_USER_QUESTIONS";
		List<Object> msgs = dao.getLatestUserQuestions(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	@Override
	@RequestMapping("/getLatestAllUserQuestions")
	public @ResponseBody ResponseEntity<Response> getLatestAllUserQuestions(			
			@RequestParam(value = "userId") String userId			
			) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("userid", userId);
		
		String entityName = "LATEST_ALL_USER_QUESTIONS";
		List<Object> msgs = dao.getLatestUserQuestions(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	// public Response postQuestion( String qsHeading,String user_id,String qsDesc,subjectId,String AskType);
	
	@Override
	@RequestMapping("/postQuestion")
	public @ResponseBody ResponseEntity<Response> postQuestion(
			@RequestParam(value = "qsHeading") String qsHeading,
			@RequestParam(value = "user_id") String user_id,
			@RequestParam(value = "qsDesc")  String qsDesc,
			@RequestParam(value = "subjectId") String subjectId,
			@RequestParam(value = "AskType") String AskType
			){
	
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("heading", qsHeading);
		queryParams.put("userid", user_id);
		queryParams.put("text", qsDesc);
		queryParams.put("subjectid", subjectId);
		if("yes".equalsIgnoreCase(AskType)){
			AskType = "N";
		}else if("m".equalsIgnoreCase(AskType)){
			AskType = "Y";
		}
		queryParams.put("qatomentor", AskType);
		String entityName = "POST_QUESTIONS";
		boolean msgs = dao.postQuestion(entityName,queryParams);
		SimpleResponse reponse = new SimpleResponse(String.valueOf((msgs == false)?Status.ERROR:Status.SUCCESS),	msgs);
		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	@Override
	@RequestMapping("/postAnswer")
	public @ResponseBody ResponseEntity<Response> postAnswer(
			@RequestParam(value = "questionid") String questionid,
			@RequestParam(value = "user_id") String user_id,
			@RequestParam(value = "answerText")  String answerText
		
			){
	
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("question_id", questionid);
		queryParams.put("user_id", user_id);
		queryParams.put("answer_text", answerText);
		String entityName = "POST_ANSWER";
		boolean msgs = dao.postAnswer(entityName,queryParams);
		SimpleResponse reponse = new SimpleResponse(String.valueOf((msgs == false)?Status.ERROR:Status.SUCCESS),	msgs);
		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}

	@Override
	@RequestMapping("/getRelatedQuestionsPn")
	public @ResponseBody ResponseEntity<Response> getRelatedQuestionsPn(
			@RequestParam(value = "subjectId") String subjectId,
			@RequestParam(value = "qsDesc") String qsDesc,
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
			) {
		CommonUtil util=CommonUtil.getInstance();
		Map<String, String> map=util.getLimit(pageno,limit);
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectid", subjectId);
		queryParams.put("text", qsDesc);
		queryParams.put("from", map.get("from"));
		queryParams.put("to", map.get("to"));
		String entityName = "RELATED_QUESTIONS_PN";
		List<Object> msgs = dao.getRelatedQuestions(entityName,queryParams);
		String count=null;
		if("true".equalsIgnoreCase(totalCountFlag)){
			 count=util.getCount("RELATED_QUESTIONS_PN_COUNT", queryParams);
			
		}
		SimpleResponse reponse = new SimpleResponse((msgs == null )?String.valueOf(Status.ERROR):String.valueOf(Status.SUCCESS),	msgs,count);
		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	@Override
	@RequestMapping("/getRelatedQuestions")
	public @ResponseBody ResponseEntity<Response> getRelatedQuestions(
			@RequestParam(value = "subjectId") String subjectId,
			@RequestParam(value = "qsDesc") String qsDesc
		
			) {
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subjectid", subjectId);
		queryParams.put("text", qsDesc);
		
		String entityName = "RELATED_QUESTIONS";
		List<Object> msgs = dao.getRelatedQuestions(entityName,queryParams);
		SimpleResponse reponse = new SimpleResponse((msgs == null )?String.valueOf(Status.ERROR):String.valueOf(Status.SUCCESS),	msgs);
		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}

	@Override
	@RequestMapping("/deleteQuestion")
	public @ResponseBody ResponseEntity<Response> deleteQuestion(@RequestParam(value = "questionid") String questionid) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("question_id", questionid);
		
		String entityName = "QUESTION_DELETE";
		boolean msgs = dao.deleteQuestions(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf((msgs == false)?Status.ERROR:Status.SUCCESS),	msgs);
		 SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
		 HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		 ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		 return entity;
	}

	@Override
	@RequestMapping("/deleteAnswer")
	public @ResponseBody  ResponseEntity<Response> deleteAnswer(@RequestParam(value = "answerid") String answerid) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		QuestionsDao dao = factory.getQuestionsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("answer_id", answerid);
	
		String entityName = "ANSWER_DELTE";
		boolean msgs = dao.deleteAnswer(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf((msgs == false)?Status.ERROR:Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	

	
	
	
	@Override
	@RequestMapping("/voteAnswer")
	public @ResponseBody ResponseEntity<Response> voteAnswer(@RequestParam(value = "answer_id") String answer_id,
			@RequestParam(value = "user_id") String user_id) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		QAVoteInfoDao dao = factory.getQAVoteInfoDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("answer_id", answer_id);
		queryParams.put("user_id", user_id);
		
		String entityName = "VOTE_ANSWER";
		boolean msgs = dao.insertVoteanswer(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf((msgs == false)?Status.ERROR:Status.SUCCESS),	msgs);
		 SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
		 HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		 ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		 return entity;
	}
	
	@Override
	@RequestMapping("/voteQuestion")
	public @ResponseBody ResponseEntity<Response> voteQuestion(@RequestParam(value = "question_id") String questionid,
			@RequestParam(value = "user_id") String user_id) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		QAVoteInfoDao dao = factory.getQAVoteInfoDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("question_id", questionid);
		queryParams.put("user_id", user_id);
		String entityName = "VOTE_QUESTION";
		boolean msgs = dao.insertQAVoteInfo(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf((msgs == false)?Status.ERROR:Status.SUCCESS),	msgs);
		 SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
		 HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		 ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		 return entity;
	}
	
	@Override
	@RequestMapping("/followQuestion")
	public @ResponseBody ResponseEntity<Response> followQuestion(@RequestParam(value = "questionid") String questionid,
			@RequestParam(value = "user_id") String user_id) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		QAFollowInfoDao dao = factory.getQAFollowInfoDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("question_id", questionid);
		queryParams.put("follow_user_id", user_id);
		
		String entityName = "FOLLOW_QUESTION";
		boolean msgs = dao.insertQAFollowInfo(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf((msgs == false)?Status.ERROR:Status.SUCCESS),	msgs);
		 SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
		 HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		 ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		 return entity;
	}

	@Override
	@RequestMapping("/viewQuestion")
	public @ResponseBody ResponseEntity<Response>  viewQuestion(@RequestParam(value = "questionid") String question_id) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		QAFollowInfoDao dao = factory.getQAFollowInfoDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("questionId", question_id);
		
		String entityName = "VIEWS_QUESTION";
		boolean msgs = dao.updateQAViews(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf((msgs == false)?Status.ERROR:Status.SUCCESS),	msgs);
		 SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
		 HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		 ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		 return entity;
	}
}
