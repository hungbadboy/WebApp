/*
 * Copyright (c) 2016-2017, Tinhvan Outsourcing JSC. All rights reserved.
 *
 * No permission to use, copy, modify and distribute this software
 * and its documentation for any purpose is granted.
 * This software is provided under applicable license agreement only.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.siblinks.ws.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;

/**
 * the user service to CRUD user and relate to user
 * 
 * @author hungpd
 * @version 1.0
 */

@Service("userService")
public interface UserService {



	/**
	 * Fetch Out of box Service
	 *
	 */

	public ResponseEntity<Response> isUsernameAvailable(String usernames);

	public ResponseEntity<Response> adminRegisterUser(String username, String password,String firstname, String lastname);

	public ResponseEntity<Response> adminloginUser(String username, String password);

	public ResponseEntity<Response> loginUser(String username, String password);

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

	public ResponseEntity<Response> listOfMajors(RequestData request);

	public ResponseEntity<Response> listOfActivity(RequestData request);

	public ResponseEntity<Response> signupcomplete(RequestData request) throws FileNotFoundException;

	public ResponseEntity<Response> listCategory(RequestData request);


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

    /**
     * This method add new mentor or admin in Admin Module
     *
     * @param jsonRegister
     * @return Map object relate to user
     */
    public ResponseEntity<Response> registerAdminMentor(String jsonRegister);

    /**
     * This method Admin update profile mentor from Admin Module
     *
     * @param jsonUpdate
     * @return String status "true" if success or "false" if fail
     */
    public ResponseEntity<Response> adminUpdateProfileUser(String jsonUpdate);

    /**
     * This method to set Enable Or Disable Account User
     * @param userId
     * @return
     */
    public ResponseEntity<Response> setStatusUser(final String json);

    /**
     * This method to get token of user by user id
     * 
     * @param userId
     *            This is user id
     * @return Token
     */
    public String getTokenUser(final String uid);
}
