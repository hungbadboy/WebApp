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
package com.siblinks.ws.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.model.ActivityLogData;
import com.siblinks.ws.service.ActivityLogService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.SibConstants;

/**
 * @author hungpd
 * @version 1.0
 */

@RestController
@RequestMapping("/siblinks/services/activityLogService")
public class ActivityLogServiceImpl implements ActivityLogService {
    private final Log logger = LogFactory.getLog(ActivityLogServiceImpl.class);

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ObjectDao dao;

    /**
     * {@inheritDoc}
     *
     * @throws Exception
     */
    @Override
    public boolean insertActivityLog(final ActivityLogData activityLog) throws Exception {
        logger.info("Insert activity log " + new Date());
        try {
            Object[] params = { activityLog.getType(), activityLog.getAction(), activityLog.getLog(), activityLog
                .getLogBy(), activityLog.getLink() };
            return dao.insertUpdateObject(SibConstants.SqlMapperActivityLog.SQL_SIB_INSERT_ACTIVITY_LOG, params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateActivityLogById(final ActivityLogData activityLog) {
        logger.info("update activity log by id " + new Date());
        try {
            Object[] params = { activityLog.getType(), activityLog.getAction(), activityLog.getLog(), activityLog
                .getLogBy(), activityLog.getLink(), activityLog
                .getId() };
            dao.insertUpdateObject(SibConstants.SqlMapperActivityLog.SQL_SIB_UPDATE_ACTIVITY_LOG, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteActivityLogById(final int id) {
        logger.info("delete activity log by id " + new Date());
        try {
            Object[] params = {id};
            return dao.insertUpdateObject(SibConstants.SqlMapperActivityLog.SQL_SIB_DELETE_ACTIVITY_LOG_BY_ID, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping("/getAllActivityByuId")
    public List getAllActivityLogByUserId(@RequestParam final int uid) {
        logger.info("get all activity log by userId " + new Date());
        try {
            Object[] params = { uid };
            return dao.readObjects(SibConstants.SqlMapperActivityLog.SQL_SIB_GET_ALL_ACTIVITY_LOG_BY_USERID, params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping("/getActivityByUserId")
    public List<Object> getActivityLogByUserId(final int userId, final String limit, final String offset) {
        logger.info("get activity log by userId with limit - offset" + new Date());
        Map<String, String> limitActivity = CommonUtil.getInstance().getOffset(limit, offset);
        try {
            Object[] params = { userId, Integer.parseInt(limitActivity.get("limit")), Integer
                .parseInt(limitActivity.get("offset")) };
            return dao.readObjects(SibConstants.SqlMapperActivityLog.SQL_SIB_GET_ACTIVITY_LOG_BY_USERID, params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
