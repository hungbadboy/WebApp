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
 * @author Tavv
 *
 */
public class Subscribe {

    private String studentId;
    private String mentorId;
    private String subscribe;
    private String limit;
    private String offset;
    private String request_data_type;
    private String request_data_method;

    public String getRequest_data_type() {
        return request_data_type;
    }

    public void setRequest_data_type(final String request_data_type) {
        this.request_data_type = request_data_type;
    }

    public String getRequest_data_method() {
        return request_data_method;
    }

    public void setRequest_data_method(final String request_data_method) {
        this.request_data_method = request_data_method;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(final String studentId) {
        this.studentId = studentId;
    }

    public String getMentorId() {
        return this.mentorId;
    }

    public void setMentorId(final String mentorId) {
        this.mentorId = mentorId;
    }

    public String getSubcribe() {
        return this.subscribe;
    }

    public void setSubcribe(final String subscribe) {
        this.subscribe = subscribe;
    }


    public String getLimit() {
        return limit;
    }

    public void setLimit(final String limit) {
        this.limit = limit;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(final String offset) {
        this.offset = offset;
    }

}
