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
public class ActivityLogData {

    private String id, type, action, log, logDate, logBy, link;

    /**
     *
     */
    public ActivityLogData() {
        super();
    }

    /**
     * @param type
     * @param action
     * @param log
     * @param logDate
     * @param logBy
     */
    public ActivityLogData(final String type, final String action, final String log, final String logBy, final String link) {
        this.type = type;
        this.action = action;
        this.log = log;
        this.logBy = logBy;
        this.link = link;
    }


    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public String getLog() {
        return log;
    }

    public void setLog(final String log) {
        this.log = log;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(final String logDate) {
        this.logDate = logDate;
    }

    public String getLogBy() {
        return logBy;
    }

    public void setLogBy(final String logBy) {
        this.logBy = logBy;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link
     *            the link to set
     */
    public void setLink(final String link) {
        this.link = link;
    }

}
