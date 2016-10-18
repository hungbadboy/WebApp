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
package com.brot.admin.model;

import java.io.Serializable;

/**
 * @author Tavv
 *
 *         This class save info of user logged to session
 */
public class UserInfoModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String userid;
    private String username;
    private String firstname;
    private String lastname;
    private String status;
    private String password;
    private String userType;
    private String imageUrl;
    private String defaultSubjectId;
    private String email;
    private String activeFlag;
    private String birthDay;

    /**
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid
     *            the userid to set
     */
    public void setUserid(final String userid) {
        this.userid = userid;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname
     *            the firstname to set
     */
    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname
     *            the lastname to set
     */
    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * @return the userType
     */
    public String getUserType() {
        return userType;
    }

    /**
     * @param userType
     *            the userType to set
     */
    public void setUserType(final String userType) {
        this.userType = userType;
    }

    /**
     * @return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl
     *            the imageUrl to set
     */
    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * @return the defaultSubjectId
     */
    public String getDefaultSubjectId() {
        return defaultSubjectId;
    }

    /**
     * @param defaultSubjectId
     *            the defaultSubjectId to set
     */
    public void setDefaultSubjectId(final String defaultSubjectId) {
        this.defaultSubjectId = defaultSubjectId;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * @return the enableFlag
     */
    public String getActiveFlag() {
        return activeFlag;
    }

    /**
     * @param enableFlag
     *            the enableFlag to set
     */
    public void setActiveFlag(final String activeFlag) {
        this.activeFlag = activeFlag;
    }

    /**
     * @return the birthDay
     */
    public String getBirthDay() {
        return birthDay;
    }

    /**
     * @param birthDay
     *            the birthDay to set
     */
    public void setBirthDay(final String birthDay) {
        this.birthDay = birthDay;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "UserInfoModel [userid=" +
               userid +
               ", username=" +
               username +
               ", firstname=" +
               firstname +
               ", lastname=" +
               lastname +
               ", status=" +
               status +
               ", password=" +
               password +
               ", userType=" +
               userType +
               ", imageUrl=" +
               imageUrl +
               ", defaultSubjectId=" +
               defaultSubjectId +
               ", email=" +
               email +
               ", activeFlag=" +
               activeFlag +
               ", birthDay=" +
               birthDay +
               "]";
    }

}
