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
 * This is object set/get video admission
 * 
 * @author hungpd
 * @version 1.0
 */
public class VideoAdmission {
	
    private String vId;
    private String authorId;
	private String youtubeUrl;
	private String idTopicSubAdmission;
	private String idSubAdmission;
    private String title;
    private String description;
    private String image;
    private String runningTime;

    private String active;

    public String getvId() {
        return vId;
    }

    public void setvId(final String vId) {
        this.vId = vId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(final String authorId) {
        this.authorId = authorId;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(final String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getIdTopicSubAdmission() {
        return idTopicSubAdmission;
    }

    public void setIdTopicSubAdmission(final String idTopicSubAdmission) {
        this.idTopicSubAdmission = idTopicSubAdmission;
    }

    public String getIdSubAdmission() {
        return idSubAdmission;
    }

    public void setIdSubAdmission(final String idSubAdmission) {
        this.idSubAdmission = idSubAdmission;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public String getActive() {
        return active;
    }

    public void setActive(final String active) {
        this.active = active;
    }

    public String getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(final String runningTime) {
        this.runningTime = runningTime;
    }
}
