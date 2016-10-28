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

import java.util.List;

import org.springframework.stereotype.Service;

import com.siblinks.ws.model.ActivityLogData;

/**
 * This is a service for crud activity log
 * 
 * @author hungpd
 * @version 1.0
 */

@Service("activityLog")
public interface ActivityLogService {
    /**
     * Insert active log
     * 
     * @param activityLog
     *            Activity data insert in to activity log
     * @return
     * 
     */
    public boolean insertActivityLog(ActivityLogData activityLog) throws Exception;

    /**
     * Update active log
     * 
     * @param type
     *            Type:
     * @param action
     *            Action:
     * @param log
     *            content log
     * @return Boolean
     */

    public boolean updateActivityLogById(final ActivityLogData activityLog) throws Exception;

    /**
     * delete active log by id
     * 
     * @param id
     *            Id auto generate
     * @return
     * 
     */
    public boolean deleteActivityLogById(int id) throws Exception;

    /**
     * Get active log by user id
     * 
     * @param uid
     *            User ID
     * @return
     * 
     */
    public List getAllActivityLogByUserId(int uid) throws Exception;
}
