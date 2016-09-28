package com.siblinks.ws.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;

public interface MentorService {

     ResponseEntity<Response> topMetorEachSubject(RequestData video);

     ResponseEntity<Response> getList(RequestData video);

     ResponseEntity<Response> search(RequestData request);

     ResponseEntity<Response> uploadFile(MultipartFile uploadfile) throws IOException;

     ResponseEntity<Response> createAboutMentor(RequestData aboutMentor) throws FileNotFoundException;

     ResponseEntity<Response> getAllAboutMentor(RequestData aboutMentor);

     ResponseEntity<Response> deleteAboutMentor(RequestData aboutMentor);

     ResponseEntity<Response> updateAboutMentor(RequestData aboutMentor);

     ResponseEntity<byte[]> getImageAboutMentor(String id);

     ResponseEntity<Response> getInforTopMentor(RequestData request);

     ResponseEntity<Response> getNewestAnswer(long id);

     /**
      * get top mentors most like
      * 
      * @param video
      *             {limit,offset}
      * @return "userid": "userName": "imageUrl": "firstName": "lastName":
      *         "numlike": "numanwser":
      */
     ResponseEntity<Response> getTopMentorsMostLike(RequestData video);

     /**
      * get list students subscribe mentor
      * 
      * @param video
      *             {mentorId,limit,offset}
      * @return U.userid,U.firstName,U.lastName,U.imageUrl
      */
     ResponseEntity<Response> getStudentsSubcribe(RequestData video);

     /**
      * get Total View Like View By MentorId
      * 
      * @param long {mentorId}
      * @return userid,totallike, totalview
      */
     ResponseEntity<Response> getTotalViewLikeViewByMentorId(long id);

     /**
      * get Total answers By MentorId
      * 
      * @param long {mentorId}
      * @return userid,total answer
      */
     ResponseEntity<Response> getTotalAnswersByMentorId(long id);

     /**
      * get top mentors by like, vote, subscribe
      * 
      * @param video
      *             {type,limit,offset}Type: {like,vote,subscribe}
      * @return
      */

     ResponseEntity<Response> getTopMentorsByLikeRateSubcrible(RequestData video);

     ResponseEntity<Response> checkStudentSubcribe(long mentorid, long studentid);

     ResponseEntity<Response> getLatestRatings(long mentorid);

     ResponseEntity<Response> getLatestComments(long mentorid, int limit, int offset);

     ResponseEntity<Response> getDashboardInfo(long uid);

     ResponseEntity<Response> getMainDashboardInfo(long uid);

     ResponseEntity<Response> getNewestQuestions(long uid);

     ResponseEntity<Response> getAllSubjects(long uid);
}
