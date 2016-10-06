package com.siblinks.ws.service;

import org.springframework.http.ResponseEntity;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;

public interface VideoAdmissionService {

	/*public ResponseEntity<Response> getVideoTopicSubAdmissionPN(RequestData request);

	public ResponseEntity<Response> getVideoTopicSubAdmission(RequestData request);
	
	public ResponseEntity<Response> getVideoAdmissionDetail(RequestData request);

	public ResponseEntity<Response> getVideoAdmissionCommentsPN(RequestData request);
	
	public ResponseEntity<Response> orderVideoAdmissionPN(RequestData request);

	public ResponseEntity<Response> updateViewVideoAdmission(RequestData request);

	public ResponseEntity<Response> deleteVideoAdmission(RequestData request);

	public ResponseEntity<Response> uploadFile(MultipartFile uploadfile)
			throws IOException;

	public ResponseEntity<Response> updateVideoAdmission(RequestData request);

	public ResponseEntity<Response> createVideoAdmission(RequestData request) throws FileNotFoundException;

	public ResponseEntity<byte[]> getImageVideoAdmission(String vId)
			throws IOException;

	public ResponseEntity<Response> updateVideoAdmissionWatched(RequestData request);

	public ResponseEntity<Response> getIdVideoAdmissionWatched(RequestData request);

	public ResponseEntity<Response> rateVideoAdmission(RequestData request);

	public ResponseEntity<Response> getRatingVideoAdmission(RequestData request);

	public ResponseEntity<Response> checkUserRatingVideoAdmission(RequestData request);*/

    /**
     * @param idAdmission
     * @return
     */
    public ResponseEntity<Response> getVideoTuttorialAdmission(String idAdmission);

    /**
     * @param
     * @return list all videos in Sib_Video_Admission table
     */
    public ResponseEntity<Response> getAllVideosAdmission();

    /**
     * @param vid
     * @return boolean
     */
    public ResponseEntity<Response> deleteVideoAdmission(RequestData request);

    /**
     * @param videoAdmission
     * @return boolean
     */
    public ResponseEntity<Response> updateVideoAdmission(RequestData request);

    /**
     * @param videoAdmission
     * @return boolean
     */
    public ResponseEntity<Response> createVideoAdmission(RequestData request);

}
