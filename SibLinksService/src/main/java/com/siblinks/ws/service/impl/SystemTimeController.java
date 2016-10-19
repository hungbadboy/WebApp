package com.siblinks.ws.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.SibConstants;

@RestController
@RequestMapping("/siblinks/services")
public class SystemTimeController {
    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private ObjectDao dao;

    /**
     * Get time Database
     * 
     * @return long time
     */
    @RequestMapping("/timeDB")
    @ResponseBody
    public String getTimeDB() {
        String time = "";
        try {
        List<Object> readObjectsNotResource = dao.readObjects(SibConstants.SqlMapper.SQL_GET_DATE_TIME, new Object[] {});
        Map<String, Long> mapTime = (HashMap<String, Long>) readObjectsNotResource.get(0);
            time += (mapTime.get(Parameters.CURRENT_TIME));
        } catch (DAOException e) {
            logger.error(e.getMessage());
            time += new Date().getTime();
        }
        return time;
    }

    /**
     * Get time application
     * 
     * @return long time
     */
    @RequestMapping("/timeAplication")
    @ResponseBody
    public String getTimeAppliction() {
        return "" + new Date().getTime();
    }
}
