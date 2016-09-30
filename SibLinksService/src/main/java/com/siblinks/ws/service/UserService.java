package com.siblinks.ws.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;






public interface UserService {



	/**
	 * Fetch Out of box Service
	 *
	 */
	/*public Response fetchUsersAll();*/

	public ResponseEntity<Response> isUsernameAvailable(String usernames);
	public ResponseEntity<Response> adminRegisterUser(String username, String password,String firstname, String lastname);
	public ResponseEntity<Response> adminloginUser(String username, String password);
	//public ResponseEntity<Response> registerUser(RequestData video);
	public ResponseEntity<Response> loginUser(String username, String password);

    // public ResponseEntity<Response> resetPassword(String usernames,String
    // password);
	//public ResponseEntity<Response> updateUserProfile(String usernames,String type, String firstname, String lastname,String imageurl,String currentclass,String accomplishments,String subjects,String helpsubjects,String bio);

    // public ResponseEntity<Response> updateUserProfile(RequestData video);

    public ResponseEntity<Response> findUser(String name);
    public ResponseEntity<Response> updateLastOnlineTime(String username);

    public ResponseEntity<Response> saveDefaultSubject(String uid, String sid);


    public ResponseEntity<Response> collegesOrUniversities(RequestData video);

    public ResponseEntity<Response> majors(RequestData video);

    public ResponseEntity<Response> extracurricularActivities(RequestData video);

    public ResponseEntity<Response> getUsers(RequestData video);

    public ResponseEntity<Response> getUserNotes(RequestData video);

    public ResponseEntity<Response> addUserNotes(RequestData video);

    public ResponseEntity<Response> getStudentMentors(RequestData sid);
    //public ResponseEntity<Response> signupcomplete(RequestData video);

    // public ResponseEntity<Response> updateUserProfileBasic(RequestData
    // request);

	public ResponseEntity<Response> listOfMajors(RequestData request);

	public ResponseEntity<Response> listOfActivity(RequestData request);

	public ResponseEntity<Response> signupcomplete(RequestData request) throws FileNotFoundException;

	public ResponseEntity<Response> listCategory(RequestData request);

	public ResponseEntity<Response> forgotPassword(RequestData request);

    /**
     * This method is confirm token for forget password
     *
     * @param request
     *            Token sent by email
     * @return
     *
     */
    public ResponseEntity<Response> confirmToken(String token);

	public ResponseEntity<Response> getAddressPage(RequestData request);

	public ResponseEntity<Response> getPolicy(RequestData request);

	public ResponseEntity<Response> getTerms(RequestData request);

	public ResponseEntity<Response> changePassword(RequestData request);

	public ResponseEntity<Response> getAccomplishment(RequestData request);

	public ResponseEntity<Response> updateAccomplishmentMobile(RequestData request);

	public ResponseEntity<Response> updateMajorsMobile(RequestData request);

	public ResponseEntity<Response> updateActivitiesMobile(RequestData request);

	public ResponseEntity<Response> updateHelpInMobile(RequestData request);

	public ResponseEntity<Response> updateGradeMobile(RequestData request);

	public ResponseEntity<Response> updateSchoolMobile(RequestData request);

	public ResponseEntity<Response> loginFacebook(RequestData request) throws FileNotFoundException;

	public ResponseEntity<Response> loginGoogle(RequestData request) throws FileNotFoundException;

    /**
     * This method use to upload avatar for user
     * 
     * @param uploadfile
     *            This param is avatar file.
     * @param userid
     *            This param is user id
     * @return TrueOrFalse
     * @throws IOException
     */
    public ResponseEntity<Response> uploadAvatar(MultipartFile uploadfile, String userid, String fileNameOld) throws IOException;

	public ResponseEntity<byte[]> getAvatar(String userid) throws IOException;

    public ResponseEntity<Response> updateUserProfile(RequestData request);

    /**
     * @param jsonData
     * @return
     */
    ResponseEntity<Response> changePasswordForgot(String jsonData);

    /**
     * This method get all user DB
     *
     * @param search
     * @param nd
     * @param rows
     * @param page
     * @param sidx
     * @param sord
     * @return
     */
    ResponseEntity<Response> getAllUsers(String search, String nd, int rows, int page, String sidx, String sord);

    /**
     * This method register new user for brother hood
     *
     * @param jsonObject
     *            This parameter include user name and password
     * 
     * @return
     * @throws FileNotFoundException
     */
    ResponseEntity<Response> registerUser(String jsonRegister);

    /**
     * This method get user information
     * 
     * @param userid
     *            This is user id
     * @return Map object relate to user
     */
    ResponseEntity<Response> getUserProfile(long userid);

    /**
     * This method get list user type Mentor
     * 
     * @param
     * 
     * @return Map object relate to user
     */
    public ResponseEntity<Response> getListMentor();

}
