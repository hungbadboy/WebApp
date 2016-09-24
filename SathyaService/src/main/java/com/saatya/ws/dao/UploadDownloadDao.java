package com.saatya.ws.dao;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.saatya.ws.model.Download;
import com.saatya.ws.model.Mentor;



public interface UploadDownloadDao extends SaatyaDao {
	
	

	public boolean upload(String dsConfigName, Map<String, String> params,MultipartFile file);
	
	public  Download download(String dsConfigName, Map<String, String> params);
	
	public boolean remove(String dsConfigName, Map<String, String> params);
	
	
	

}
