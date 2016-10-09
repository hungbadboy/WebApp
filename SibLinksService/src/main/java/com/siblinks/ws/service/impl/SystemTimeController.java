package com.siblinks.ws.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.SibConstants;

@RestController
@RequestMapping("/siblinks/services")
public class SystemTimeController {

    @Autowired
    private ObjectDao dao;

    @RequestMapping("/timeDB")
    @ResponseBody
    public String getTimeDB() {
        List<Object> readObjectsNotResource = dao.readObjects(SibConstants.SqlMapper.SQL_GET_DATE_TIME, new Object[] {});
        Map<String, Long> mapTime = (HashMap<String, Long>) readObjectsNotResource.get(0);
        String time = "" + (mapTime.get(Parameters.CURRENT_TIME));
        return time;
    }

    @RequestMapping("/timeAplication")
    @ResponseBody
    public String getTimeAppliction() {
        return "" + new Date().getTime();
    }
}
