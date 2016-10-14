package com.siblinks.ws.service;

import org.springframework.http.ResponseEntity;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;

/**
 * @author bkagidal
 *
 */
public interface VideoService {
     public ResponseEntity<Response> uploadVideo(RequestData video);

     public ResponseEntity<Response> getVideoInfo(RequestData video);

     public ResponseEntity<Response> getVideoComments(RequestData video);

     public ResponseEntity<Response> getVideoCommentsPN(RequestData video);

     public ResponseEntity<Response> editVideoInfo(RequestData video);

     public ResponseEntity<Response> getVideosWithTopic(RequestData video);

     public ResponseEntity<Response> getVideosPN(RequestData video);

     public ResponseEntity<Response> getVideosList(RequestData video);

     public ResponseEntity<Response> getVideosListPN(RequestData video);

     public ResponseEntity<Response> getVideosWithTopicPN(RequestData video);

     public ResponseEntity<Response> getVideosWithSubTopic(RequestData video);

     public ResponseEntity<Response> getVideosWithSubTopicPN(RequestData video);

     public ResponseEntity<Response> getVideosWithSubject(RequestData video);

     public ResponseEntity<Response> getVideosWithSubjectPN(RequestData video);

     public ResponseEntity<Response> searchVideos(RequestData video);

     public ResponseEntity<Response> searchVideosPN(RequestData video);

     public ResponseEntity<Response> searchVideosUsingTag(RequestData video);

     public ResponseEntity<Response> searchVideosUsingTagPN(RequestData video);

     public ResponseEntity<Response> removeVideo(RequestData video);

     public ResponseEntity<Response> rateVideo(RequestData paramRequestData);

     public ResponseEntity<Response> createVideo(RequestData video);

     public ResponseEntity<Response> getVideoDetails(RequestData video);

     public ResponseEntity<Response> getVideoDetailsPN(RequestData video);

     public ResponseEntity<Response> updateVideoDetails(RequestData video);

     public ResponseEntity<Response> deleteVideo(RequestData video);

     public ResponseEntity<Response> getSubCategoryData(RequestData video);

     public ResponseEntity<Response> saveSubCategory(RequestData video);

     public ResponseEntity<Response> updateSubCategory(RequestData video);

     public ResponseEntity<Response> deleteSubCategory(RequestData video);

     public ResponseEntity<Response> getMentorsOfVideo(RequestData video);

     public ResponseEntity<Response> getMentorReviewsPN(RequestData sid);

     public ResponseEntity<Response> getVideosListByUser(RequestData request);

     public ResponseEntity<Response> updateVideo(RequestData request);

     public ResponseEntity<Response> searchAllVideo(RequestData request);

     public ResponseEntity<Response> updateViewVideo(RequestData request);

     public ResponseEntity<Response> updateVideoWatched(RequestData request);

     public ResponseEntity<Response> getIdVideoWatched(RequestData request);

     public ResponseEntity<Response> getRating(RequestData request);

     public ResponseEntity<Response> checkUserRatingVideo(RequestData request);

     public ResponseEntity<Response> getAllVideoByUserPN(RequestData request);

     public ResponseEntity<Response> getVideoById(long vid, String type);

     public ResponseEntity<Response> setSubscribeMentor(RequestData request);

     /*
      * @author Hoai Nguyen
      */
     public ResponseEntity<Response> getVideosBySubject(long userid, long subjectid, int offset);

     public ResponseEntity<Response> getVideos(long uid, int offset);

    public ResponseEntity<Response> getVideoById(long vid, long userid);

     public ResponseEntity<Response> getVideosPlaylist(long uid);

     public ResponseEntity<Response> getVideosTopRated(long uid, int offset);

     public ResponseEntity<Response> getVideosTopViewed(long uid, int offset);

     public ResponseEntity<Response> getVideosRecently(long uid);

     public ResponseEntity<Response> insertVideo(RequestData request);

     public ResponseEntity<Response> getHistoryVideosList(String uid);

     public ResponseEntity<Response> clearHistoryVideosList(String uid, String vid);

     public ResponseEntity<Response> deleteMultipleVideo(RequestData request);

     public ResponseEntity<Response> searchVideos(long uid, String keyword, int offset);

     public ResponseEntity<Response> addVideosToPlaylist(RequestData request);

    public ResponseEntity<Response> getVideosNonePlaylist(long uid, int offset);

    public ResponseEntity<Response> getVideosNonePlaylistBySubject(long uid, long subjectId, int offset);

    public ResponseEntity<Response> searchVideosNonePlaylist(long uid, String keyword, int offset);

     /*
      * @author Tavv
      */
    public ResponseEntity<Response> getVideoBySubject(long userId, String subjectId, String limit, String offset);

    public ResponseEntity<Response> checkUserRatingVideoAdmission(RequestData request);

    public ResponseEntity<Response> rateVideoAdmission(RequestData request);

}
