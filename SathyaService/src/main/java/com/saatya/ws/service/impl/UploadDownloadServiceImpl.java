package com.saatya.ws.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.saatya.ws.biz.DataServiceManager;
import com.saatya.ws.dao.DaoFactory;
import com.saatya.ws.dao.FeedbackDao;
import com.saatya.ws.dao.MentorDao;
import com.saatya.ws.dao.UploadDownloadDao;
import com.saatya.ws.model.Download;
import com.saatya.ws.response.Response;
//import com.saatya.ws.response.Response;
import com.saatya.ws.response.SimpleResponse;
import com.saatya.ws.response.Status;
import com.saatya.ws.service.UploadDownloadService;
import com.saatya.ws.util.StringUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response.ResponseBuilder;

@Controller
@RequestMapping("/saatya/services/upload")
public class UploadDownloadServiceImpl implements UploadDownloadService {

	private final Log logger = LogFactory.getLog(getClass());
	private static final int BUFFER_SIZE = 4096;
	
	
	//Mentor Reviewed students implementation ( update)
		@RequestMapping("/remove")
		public @ResponseBody ResponseEntity<Response> remove(
				@RequestParam(value = "userId") String userId, 
				@RequestParam(value = "essayId") String essayId
				
				) {
			// TODO Auto-generated method stub
			DaoFactory factory = DaoFactory.getDaoFactory();
    		UploadDownloadDao dao = factory.getUploadDownloadDao();
			Map<String, String> queryParams = new HashMap<String, String>() ;
			queryParams.put("userId", userId);
			queryParams.put("essayId", essayId);
			
			String entityName = "ESSAYS_REMOVE";

			
			boolean msgs = dao.remove(entityName, queryParams);
			// SimpleResponse reponse = new SimpleResponse(String.valueOf(Status.SUCCESS),	msgs);
	        SimpleResponse reponse = new SimpleResponse((msgs)?String.valueOf(Status.SUCCESS):String.valueOf(Status.ERROR),	msgs);
	        HttpHeaders headers = StringUtil.addAccessControllAllowOrigin();
			ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse,headers, HttpStatus.OK);
			return entity;
		}
	
	
	  @RequestMapping(value="/download", method=RequestMethod.GET)
	  public void download(@RequestParam("userId") String userId,
			  @RequestParam("essayId") String essayId,
			  @RequestParam("status") String status,
			  HttpServletRequest request,
            HttpServletResponse response)  {
 
		
		DaoFactory factory = DaoFactory.getDaoFactory();
		UploadDownloadDao dao = factory.getUploadDownloadDao();
		Map<String, String> queryParams = new HashMap<String, String>() ;
		

	
        String entityName="STUDENT_UPLOAD";
    	
    	if("PENDING".equalsIgnoreCase(status)){
    		entityName = "STUDENT_DOWNLOAD";
       	}else if("REVIEWED".equalsIgnoreCase(status)){ 
    		entityName = "MENTOR_DOWNLOAD";
       	}
    	queryParams.put("userId", userId);
    	queryParams.put("essayId", essayId);
		Download  file = dao.download(entityName, queryParams);
         
        // get MIME type of the file
        String mimeType = file.getMimeType();
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
             mimeType = "application/octet-stream";
          //  mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            
        }
      
        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength(Integer.parseInt(file.getFilesize()));
 
        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",file.getFileName());
        response.setHeader(headerKey, headerValue);
 
        // get output stream of the response
        OutputStream outStream = null;
		try {
			outStream = response.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        
        InputStream inputStream = file.getInputStream();
		 
        // write bytes read from the input stream into the output stream
        try {
			while ((bytesRead = inputStream.read(buffer)) != -1) {
			    outStream.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
        try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
    }
	
	
	
	
	 @RequestMapping(value="/upload", method=RequestMethod.POST)
	
	    public @ResponseBody String upload(@RequestParam("name") String name,@RequestParam("userId") String userId,@RequestParam("userType") String userType, @RequestParam("file") MultipartFile file){
		 
	       if (!file.isEmpty()) {
	            try {
	            	
	            	DaoFactory factory = DaoFactory.getDaoFactory();
	        		UploadDownloadDao dao = factory.getUploadDownloadDao();
	        		Map<String, String> queryParams = new HashMap<String, String>() ;
	        		
	       
	        	
	                String entityName="STUDENT_UPLOAD";
	            	
	            	if("S".equalsIgnoreCase(userType)){
	            		entityName = "STUDENT_UPLOAD";
	            		queryParams.put("userId", userId);
		        		queryParams.put("name", name);
		        		queryParams.put("fileName", file.getOriginalFilename());
		        		queryParams.put("filesize", ""+(int)file.getSize());
		        		queryParams.put("mimeType", file.getContentType());
	            		
		            		
	            	}else if("M".equalsIgnoreCase(userType)){ 
	            		entityName = "MENTOR_UPLOAD";
	            		queryParams.put("userId", userId);
	            		queryParams.put("filesize",""+(int)file.getSize());
		        				            	
	            	}
	        		boolean  msgs = dao.upload(entityName, queryParams,file);
	            	if(msgs){
	            		 return "You successfully uploaded ";
	            	}else{
	            		 return "You failed to upload ";
	            	}
	            	
	               
	            } catch (Exception e) {
	                return "You failed to upload " + name + " => " + e.getMessage();
	            }
	        } else {
	            return "You failed to upload " + name + " because the file was empty.";
	        }
	    }



	

	

	 



	

}
