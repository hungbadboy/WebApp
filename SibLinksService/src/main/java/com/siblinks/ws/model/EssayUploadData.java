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
 * @author hungpd
 * @version 1.0
 */
public class EssayUploadData {

    private String uploadEssayId;
    private String userId;
    private String status;
    private String originalDocument;
    private String reviewedDocument;
    private String docSubmittedDate;
    private String docReviewedDate;
    private String nameOfEssay;
    private String descriptionOfEssay;
    private String odFilesize;
    private String rdFilesize;
    private String mimeType;
    private String urlFile;
    private String urlReview;
    private String mentorId;
    private String schoolId;
    private String majorId;
    private String numRate;
    private String averageRating;

    // Rate
    private String uid;
    private String rating;

    public String getUploadEssayId() {
        return uploadEssayId;
    }

    public void setUploadEssayId(final String uploadEssayId) {
        this.uploadEssayId = uploadEssayId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getOriginalDocument() {
        return originalDocument;
    }

    public void setOriginalDocument(final String originalDocument) {
        this.originalDocument = originalDocument;
    }

    public String getReviewedDocument() {
        return reviewedDocument;
    }

    public void setReviewedDocument(final String reviewedDocument) {
        this.reviewedDocument = reviewedDocument;
    }

    public String getDocSubmittedDate() {
        return docSubmittedDate;
    }

    public void setDocSubmittedDate(final String docSubmittedDate) {
        this.docSubmittedDate = docSubmittedDate;
    }

    public String getDocReviewedDate() {
        return docReviewedDate;
    }

    public void setDocReviewedDate(final String docReviewedDate) {
        this.docReviewedDate = docReviewedDate;
    }

    public String getNameOfEssay() {
        return nameOfEssay;
    }

    public void setNameOfEssay(final String nameOfEssay) {
        this.nameOfEssay = nameOfEssay;
    }

    public String getDescriptionOfEssay() {
        return descriptionOfEssay;
    }

    public void setDescriptionOfEssay(final String descriptionOfEssay) {
        this.descriptionOfEssay = descriptionOfEssay;
    }

    public String getOdFilesize() {
        return odFilesize;
    }

    public void setOdFilesize(final String odFilesize) {
        this.odFilesize = odFilesize;
    }

    public String getRdFilesize() {
        return rdFilesize;
    }

    public void setRdFilesize(final String rdFilesize) {
        this.rdFilesize = rdFilesize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(final String urlFile) {
        this.urlFile = urlFile;
    }

    public String getUrlReview() {
        return urlReview;
    }

    public void setUrlReview(final String urlReview) {
        this.urlReview = urlReview;
    }

    public String getMentorId() {
        return mentorId;
    }

    public void setMentorId(final String mentorId) {
        this.mentorId = mentorId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(final String schoolId) {
        this.schoolId = schoolId;
    }

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(final String majorId) {
        this.majorId = majorId;
    }

    public String getNumRate() {
        return numRate;
    }

    public void setNumRate(final String numRate) {
        this.numRate = numRate;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(final String averageRating) {
        this.averageRating = averageRating;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(final String rating) {
        this.rating = rating;
    }
}
