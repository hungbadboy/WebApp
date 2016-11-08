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
package com.siblinks.ws.model;

/**
 * @author Hoai Nguyen
 * @version v1.0
 */
public class User {
    private String userid;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String school;
    private String bio;
    private String description;
    private String favorite;
    private String username;
    private String password;
    private String accomplishments;
    private String newpassword;
    private String bod;
    private String defaultSubjectId;
    private String role;
    private String activity;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(final String newpassword) {
        this.newpassword = newpassword;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(final String userid) {
        this.userid = userid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(final String school) {
        this.school = school;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(final String bio) {
        this.bio = bio;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(final String favorite) {
        this.favorite = favorite;
    }

    /**
     * @return the bod
     */
    public String getBod() {
        return bod;
    }

    /**
     * @param bod
     *            the bod to set
     */
    public void setBod(final String bod) {
        this.bod = bod;
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
     * @return the accomplishments
     */
    public String getAccomplishments() {
        return accomplishments;
    }

    /**
     * @param accomplishments
     *            the accomplishments to set
     */
    public void setAccomplishments(final String accomplishments) {
        this.accomplishments = accomplishments;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role
     *            the role to set
     */
    public void setRole(final String role) {
        this.role = role;
    }

    /**
     * @return the activity
     */
    public String getActivity() {
        return activity;
    }

    /**
     * @param activity
     *            the activity to set
     */
    public void setActivity(final String activity) {
        this.activity = activity;
    }

}
