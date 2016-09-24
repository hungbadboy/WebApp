package com.saatya.ws.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;




import com.saatya.ws.dao.DaoFactory;
import com.saatya.ws.dao.SubjectsDao;
import com.saatya.ws.dao.UserDao;
import com.saatya.ws.model.User;
import com.saatya.ws.response.Response;
import com.saatya.ws.response.SimpleResponse;
import com.saatya.ws.response.Status;
import com.saatya.ws.service.SubjectsService;
import com.saatya.ws.service.UserService;
import com.saatya.ws.util.CommonUtil;
import com.saatya.ws.util.StringUtil;

@Controller
@RequestMapping("/saatya/services/subjects")
public class SubjectsServiceImpl implements SubjectsService{

	private final Log logger = LogFactory.getLog(getClass());

	
	/*@Override
	public Response fetchUsersAll() {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	@RequestMapping("/listofVideos")
	public @ResponseBody 
	ResponseEntity<Response> listofVideos(
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
			) {
		
		CommonUtil util=CommonUtil.getInstance();
		Map<String, String> map=util.getLimit(pageno,limit);
		DaoFactory factory = DaoFactory.getDaoFactory();
		SubjectsDao dao = factory.getSubjectsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("from", map.get("from"));
		queryParams.put("to", map.get("to"));
		String entityName = "LIST_OF_VIDEOS_PN";
		List<Object> msgs = dao.listofVideos(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
		String count=null;
		if("true".equalsIgnoreCase(totalCountFlag)){
			 count=util.getCount("LIST_OF_VIDEOS_COUNT", null);
			
		}
		
		SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);

		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}

	@Override
	@RequestMapping("/listofSubjectsPn")
	public @ResponseBody 
	ResponseEntity<Response> listofSubjectsPn(
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
			) {
		
		CommonUtil util=CommonUtil.getInstance();
		Map<String, String> map=util.getLimit(pageno,limit);
		DaoFactory factory = DaoFactory.getDaoFactory();
		SubjectsDao dao = factory.getSubjectsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("from", map.get("from"));
		queryParams.put("to", map.get("to"));
		String entityName = "SUBJECTS_READ_PN";
		List<Object> msgs = dao.findAll(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
		String count=null;
		if("true".equalsIgnoreCase(totalCountFlag)){
			 count=util.getCount("SUBJECTS_READ_PN_COUNT", queryParams);
			
		}
		
		SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);

		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	@Override
	@RequestMapping("/listofSubjects")
	public @ResponseBody 
	ResponseEntity<Response> listofSubjects(
				) {
		
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		SubjectsDao dao = factory.getSubjectsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		String entityName = "SUBJECTS_READ";
		List<Object> msgs = dao.findAll(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
		
		SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);

		HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}


	@Override
	@RequestMapping("/listofCategerisPn")
	public @ResponseBody  ResponseEntity<Response> listofCategerisPn(
			@RequestParam(value = "subject") String subject,
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
		    ) {
		
		        CommonUtil util=CommonUtil.getInstance();
		        Map<String, String> map=util.getLimit(pageno,limit);
		        
				DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("subjectid", subject);
				queryParams.put("from", map.get("from"));
				queryParams.put("to", map.get("to"));
				
				String entityName = "CATAGERY_READ_PN";
				List<Object> msgs = dao.listofCategeris(entityName,queryParams);
				String count=null;
				if("true".equalsIgnoreCase(totalCountFlag)){
					 count=util.getCount("CATAGERY_READ_PN_COUNT", queryParams);
					
				}
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
				SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
				HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
				ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
				return entity;
	}
	
	@Override
	@RequestMapping("/listofCategeris")
	public @ResponseBody  ResponseEntity<Response> listofCategeris(
			@RequestParam(value = "subject") String subject
			
		    ) {
		
		       
		        
				DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("subjectid", subject);
				
				
				String entityName = "CATAGERY_READ";
				List<Object> msgs = dao.listofCategeris(entityName,queryParams);
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
				SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
				HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
				ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
				return entity;
	}


	
	
	@Override
	@RequestMapping("/listofSubCategoriesPn")
	public @ResponseBody  ResponseEntity<Response> listofSubCategoriesPn(
			@RequestParam(value="subject") String subject,
			@RequestParam(value="category") String category,
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
		    ) {
		        CommonUtil util=CommonUtil.getInstance();
		        Map<String, String> map=util.getLimit(pageno,limit);   
		        DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("subjectid", subject);
				queryParams.put("categoryid", category);
				queryParams.put("from", map.get("from"));
				queryParams.put("to", map.get("to"));
				String entityName = "SUBCATAGERY_READ_PN";
				List<Object> msgs = dao.listofSubCategories(entityName,queryParams);
				String count=null;
				if("true".equalsIgnoreCase(totalCountFlag)){
					 count=util.getCount("SUBCATAGERY_READ_PN_COUNT", queryParams);
					
				}
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
				SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
				HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
				ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
				return entity;
	}
	
	@Override
	@RequestMapping("/listofSubCategories")
	public @ResponseBody  ResponseEntity<Response> listofSubCategories(
			@RequestParam(value="subject") String subject,
			@RequestParam(value="category") String category
			
		    ) {
		       
		        DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("subjectid", subject);
				queryParams.put("categoryid", category);
				String entityName = "SUBCATAGERY_READ";
				List<Object> msgs = dao.listofSubCategories(entityName,queryParams);
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
				SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
				HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
				ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
				return entity;
	}


	
	@Override
	@RequestMapping("/listofTopSubCategoriesPn")
	public @ResponseBody  ResponseEntity<Response> listofTopSubCategoriesPn(
			@RequestParam(value="subject") String subject,
			@RequestParam(value="category") String category,
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
		    ) {
	
		        CommonUtil util=CommonUtil.getInstance();
		        Map<String, String> map=util.getLimit(pageno,limit);
		        DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("subjectid", subject);
				queryParams.put("categoryid", category);
				queryParams.put("from", map.get("from"));
				queryParams.put("to", map.get("to"));
				String entityName = "SUBCATAGERY_TOP_READ_PN";
				List<Object> msgs = dao.listofTopSubCategories(entityName,queryParams);
				String count=null;
				if("true".equalsIgnoreCase(totalCountFlag)){
					 count=util.getCount("SUBCATAGERY_TOP_READ_PN_COUNT", queryParams);
					
				}
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
                SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
                HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
        		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
        		return entity;
	}
	
	@Override
	@RequestMapping("/listofTopSubCategories")
	public @ResponseBody  ResponseEntity<Response> listofTopSubCategories(
			@RequestParam(value="subject") String subject,
			@RequestParam(value="category") String category
			
		    ) {
	
		       
		        DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("subjectid", subject);
				queryParams.put("categoryid", category);
				String entityName = "SUBCATAGERY_TOP_READ";
				List<Object> msgs = dao.listofTopSubCategories(entityName,queryParams);
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
                SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
                HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
        		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
        		return entity;
	}
	
	
	@Override
	@RequestMapping("/filterSubCategoriesPn")
	public @ResponseBody  ResponseEntity<Response> filterSubCategoriesPn(
			@RequestParam(value="subject") String subject,
			@RequestParam(value="category") String category,
			@RequestParam(value="searchtxt") String searchtxt,
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
		    ) {
	
		        CommonUtil util=CommonUtil.getInstance();
		        Map<String, String> map=util.getLimit(pageno,limit);
		        DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("subjectid", subject);
				queryParams.put("categoryid", category);
				queryParams.put("searchtxt", searchtxt);
				queryParams.put("from", map.get("from"));
				queryParams.put("to", map.get("to"));
				String entityName = "FILTER_SUB_CATEGORY_PN";
				List<Object> msgs = dao.filterSubCategories(entityName,queryParams);
				
				String count=null;
				if("true".equalsIgnoreCase(totalCountFlag)){
					 count=util.getCount("FILTER_SUB_CATEGORY_PN_COUNT", queryParams);
					
				}

                SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
                HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
        		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
        		return entity;
	}
	
	@Override
	@RequestMapping("/filterSubCategories")
	public @ResponseBody  ResponseEntity<Response> filterSubCategories(
			@RequestParam(value="subject") String subject,
			@RequestParam(value="category") String category,
			@RequestParam(value="searchtxt") String searchtxt
			
		    ) {
	
		        DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("subjectid", subject);
				queryParams.put("categoryid", category);
				queryParams.put("searchtxt", searchtxt);
				
				String entityName = "FILTER_SUB_CATEGORY";
				List<Object> msgs = dao.filterSubCategories(entityName,queryParams);

                SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
                HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
        		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
        		return entity;
	}
	
	@Override
	@RequestMapping("/SearchCategoriesPn")
	public @ResponseBody  ResponseEntity<Response> SearchCategoriesPn(
			@RequestParam(value="subject") String subject,
			@RequestParam(value="searchtxt") String searchtxt,
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
		    ) {
		        CommonUtil util=CommonUtil.getInstance();
		        Map<String, String> map=util.getLimit(pageno,limit);  
		        DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("subjectid", subject);
				queryParams.put("searchtxt", searchtxt);
				queryParams.put("from", map.get("from"));
				queryParams.put("to", map.get("to"));
				String entityName = "SEARCH_CATEGORY_PN";
				List<Object> msgs = dao.SearchCategories(entityName,queryParams);
				String count=null;
				if("true".equalsIgnoreCase(totalCountFlag)){
					 count=util.getCount("SEARCH_CATEGORY_PN_COUNT", queryParams);
					
				}
                SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
                HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
        		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
        		return entity;
	}
	
	@Override
	@RequestMapping("/SearchCategories")
	public @ResponseBody  ResponseEntity<Response> SearchCategories(
			@RequestParam(value="subject") String subject,
			@RequestParam(value="searchtxt") String searchtxt
			
		    ) {
		        
		        DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("subjectid", subject);
				queryParams.put("searchtxt", searchtxt);
				String entityName = "SEARCH_CATEGORY";
				List<Object> msgs = dao.SearchCategories(entityName,queryParams);
                SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
                HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
        		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
        		return entity;
	}
	
	@Override
	@RequestMapping("/getVedioInfoPn")
	public @ResponseBody  ResponseEntity<Response> getVedioInfoPn(
			@RequestParam(value="subject") String subject,
			@RequestParam(value="category") String category,
			@RequestParam(value="subcategory") String subcategory,
			@RequestParam(value="pageno") String pageno,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="totalCountFlag") String totalCountFlag
		    ) {
		        CommonUtil util=CommonUtil.getInstance();
		        Map<String, String> map=util.getLimit(pageno,limit);
		        DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("subjectid", subject);
				queryParams.put("categoryid", category);
				queryParams.put("subjectsubcatid", subcategory);
				queryParams.put("from", map.get("from"));
				queryParams.put("to", map.get("to"));
				
				
				String entityName = "GET_VEDIO_INFO_PN";
				List<Object> msgs = dao.getVedioInfo(entityName,queryParams);
				String count=null;
				if("true".equalsIgnoreCase(totalCountFlag)){
					 count=util.getCount("GET_VEDIO_INFO_PN_COUNT", queryParams);
					
				}
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
                SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs,count);
                HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
        		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
        		return entity;
	}
	
	@Override
	@RequestMapping("/getVedioInfo")
	public @ResponseBody  ResponseEntity<Response> getVedioInfo(
			@RequestParam(value="subject") String subject,
			@RequestParam(value="category") String category,
			@RequestParam(value="subcategory") String subcategory
			
		    ) {
		       
		        DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("subjectid", subject);
				queryParams.put("categoryid", category);
				queryParams.put("subjectsubcatid", subcategory);
				
				
				
				String entityName = "GET_VEDIO_INFO";
				List<Object> msgs = dao.getVedioInfo(entityName,queryParams);
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
                SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
                HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
        		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
        		return entity;
	}
	
	@Override
	@RequestMapping("/getVedioComments")
	public @ResponseBody  ResponseEntity<Response> getVedioComments(
			@RequestParam(value="subject") String subject,
			@RequestParam(value="category") String category,
			@RequestParam(value="subcategory") String subcategory
			
		    ) {
		       
		        DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("subjectid", subject);
				queryParams.put("categoryid", category);
				queryParams.put("subjectsubcatid", subcategory);
				
				
				
				String entityName = "GET_VEDIO_COMMENTS";
				List<Object> msgs = dao.getVedioComments(entityName,queryParams);
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
                SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
                HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
        		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
        		return entity;
	}
	
	//public ResponseEntity<Response> vedioComments(String videoId);
	@Override
	@RequestMapping("/vedioComments")
	public @ResponseBody  ResponseEntity<Response> vedioComments(
			@RequestParam(value="videoId") String videoId
			
			
		    ) {
		       
		        DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("video_id", videoId);
				
				
				
				
				String entityName = "ONLY_VIDEO_COMMENT";
				List<Object> msgs = dao.getVedioComments(entityName,queryParams);
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
                SimpleResponse reponse = new SimpleResponse(msgs == null ? String.valueOf(Status.ERROR): String.valueOf(Status.SUCCESS),msgs);
                HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
        		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
        		return entity;
	}
	@Override
	@RequestMapping("/rateVedio")
	public @ResponseBody  ResponseEntity<Response> rateVedio(
			@RequestParam(value="subcategory") String subcategory,
			@RequestParam(value="rating") String rating,
			@RequestParam(value="user") String user
		    ) {
	
		        DaoFactory factory = DaoFactory.getDaoFactory();
				SubjectsDao dao = factory.getSubjectsDao();
				Map<String, String> queryParams = new HashMap<String, String>() ;
				queryParams.put("video_id", subcategory);
				queryParams.put("rating", rating);
				queryParams.put("user_id", user);
				
			
				String entityName = "VIDEO_RATING";
				boolean status = dao.ratingVedio(entityName,queryParams);
		        SimpleResponse reponse = new SimpleResponse((status)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	status);
		        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
				ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
				return entity;
				//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
				
	}


	@Override
	@RequestMapping("/insertSubjects")
	public @ResponseBody ResponseEntity<Response> insertSubjects(
			@RequestParam(value="subjectName") String subjectName,
			@RequestParam(value="description") String description,
			@RequestParam(value="active") String active) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		SubjectsDao dao = factory.getSubjectsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subject_name", subjectName);
		queryParams.put("description", description);
		queryParams.put("active", active);
		
		String entityName = "SUBJECT_INSERT";
		boolean msgs = dao.insertSubjects(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);

        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}


	@Override
	@RequestMapping("/insertSubjectCategories")
	public @ResponseBody ResponseEntity<Response> insertSubjectCategories(
			@RequestParam(value="subjectCategoryName") String subjectCategoryName, 
			@RequestParam(value="description") String description, 
			@RequestParam(value="subjectId") String subjectId, 
			@RequestParam(value="active") String active) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		SubjectsDao dao = factory.getSubjectsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subject_category_name", subjectCategoryName);
		queryParams.put("description", description);
		queryParams.put("subject_id", subjectId);
		queryParams.put("active", active);
		String entityName = "SUBJECT_CATEGORY_INSERT";
		boolean msgs = dao.insertSubjectCategories(entityName,queryParams);
		//SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);

        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}


	@Override
	@RequestMapping("/insertSubjectSubcategories")
	public @ResponseBody ResponseEntity<Response> insertSubjectSubcategories(
			@RequestParam(value="subjectSubCategoryName") String subjectSubCategoryName, 
			@RequestParam(value="description") String description, 
			@RequestParam(value="subjectCategoryId") String subjectCategoryId, 
			@RequestParam(value="active") String active, 
			@RequestParam(value="popularVideo") String popularVideo) {
		DaoFactory factory = DaoFactory.getDaoFactory();
		SubjectsDao dao = factory.getSubjectsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("subject_sub_category_name", subjectSubCategoryName);
		queryParams.put("description", description);
		queryParams.put("subject_category_id", subjectCategoryId);
		queryParams.put("active", active);
		queryParams.put("popular_video", popularVideo);
		String entityName = "SUBJECT_SUB_CATEGORY";

		boolean msgs = dao.insertSubjectSubcategories(entityName,queryParams);
		// SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
        SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);

        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
		
	@Override
	@RequestMapping("/postVideoComments")
	public @ResponseBody  ResponseEntity<Response> postVedioComments(
												@RequestParam(value="subcategory") String subcategory,
												@RequestParam(value="comments") String comments,
												@RequestParam(value="user") String user) {
	
		DaoFactory factory = DaoFactory.getDaoFactory();
		SubjectsDao dao = factory.getSubjectsDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		queryParams.put("video_id", subcategory);
		queryParams.put("comments", comments);
		queryParams.put("user_id", user);

		String entityName = "VIDEO_COMMENT";
		boolean status = dao.postVideoComments(entityName,queryParams);
        SimpleResponse reponse = new SimpleResponse((status)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	status);
        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
		ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
		return entity;
	}
	
	
	
}
