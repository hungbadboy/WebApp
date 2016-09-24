package com.saatya.ws.service;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;







/**
 *
 *
 */
public interface UploadDownloadService {



	
	
	
	

	public String upload( String name,String userId,String userType, MultipartFile file);
	
	//public void download(String userId,String userType,HttpServletRequest request, HttpServletResponse response) ;
	
	public void download(String userId,String essayId,String status,HttpServletRequest request, HttpServletResponse response) ;
	
	public ResponseEntity<com.saatya.ws.response.Response> remove(String userId,String essayId) ;
	
	
	
}
