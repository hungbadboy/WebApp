package com.saatya.ws.dao;

import java.util.List;

import com.saatya.ws.model.VideoSubMapping;



public interface VideoSubMappingDao extends SaatyaDao {
	
	public List<VideoSubMapping> findAll();

	public List<VideoSubMapping> findByName();

	public boolean insertVideoSubMapping(VideoSubMapping user);

	public boolean updateVideoSubMapping(VideoSubMapping user);

	public boolean deleteVideoSubMapping(String userId);
	
	public VideoSubMapping findVideoSubMappingByVideoSubMappingId(String userId);
	
	
}
