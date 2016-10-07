package com.siblinks.ws.service;

import org.springframework.http.ResponseEntity;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;

public interface VideoDetailService {
	public ResponseEntity<Response> getVideoDetailById(long id);

	public ResponseEntity<Response> getCommentVideoById(long vid);

	public ResponseEntity<Response> updateVideoHistory(RequestData request);
	
	public ResponseEntity<Response> getVideoByCategoryId(RequestData request);

	public ResponseEntity<Response> checkSubscribe(String mentorid, String studentid);

	public ResponseEntity<Response> getVideoByPlaylistId(long vid);
	
    public ResponseEntity<Response> getVideoDetailMentor(long vid, long uid);

    public ResponseEntity<Response> getVideoRelatedMentor(long subjectId, long uid, int offset);

    public ResponseEntity<Response> getVideoAdmissionDetailById(long vid);

    public ResponseEntity<Response> getCommentVideoAdmissionById(long vid);

    public ResponseEntity<Response> updateViewVideoAdmission(RequestData request);

    public ResponseEntity<Response> getVideoByAdmissionId(RequestData request);
}