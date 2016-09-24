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
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siblinks.ws.common.LoggedInChecker;
import com.siblinks.ws.dao.ObjectDao;
import com.siblinks.ws.model.MenuData;
import com.siblinks.ws.model.SearchData;
import com.siblinks.ws.response.Response;
import com.siblinks.ws.response.SimpleResponse;
import com.siblinks.ws.service.AdminService;
import com.siblinks.ws.util.CommonUtil;
import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.SibConstants;

/**
 * @author Hoai Nguyen
 * 
 */
@RestController
@RequestMapping("/siblinks/services/adminService")
public class AdminServiceImpl implements AdminService {

    private Log logger = LogFactory.getLog(AdminServiceImpl.class);

    @Autowired
    private ObjectDao dao;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    LoggedInChecker logged;
    @Autowired
    private HttpServletRequest context;

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllMenuData() throws Exception {

        System.out.println("Method: " + "getAllMenuData");
        List<Object> readObject = null;

        Object[] pramsObjects = null;
        readObject = dao.readObjects(SibConstants.SqlMapperBROT4.SQL_GET_ALL_MENU_DATA, pramsObjects);
        SimpleResponse reponse = new SimpleResponse("" + true, "adminService", "getAllMenuData", readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.siblinks.ws.service.AdminService#deleteMenuData()
     */
    @Override
    @RequestMapping(value = "/deleteMenu", method = RequestMethod.POST)
    public ResponseEntity<Response> deleteMenuData(@RequestBody final MenuData request) throws Exception {
        logger.info("deleteMenuData " + new Date());
        logger.info("insertMenuData " + new Date());
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            int id = Integer.parseInt(request.getId());
            String userId = "6";// logged.getLoggedInUser().getUserid();
            // Get count child
            List<Object> listObject = dao.readObjects(
                SibConstants.SqlMapperBROT4.SQL_COUNT_CHILD_MENU_BY_PARENT_ID,
                new Object[] { id });

            dao.insertUpdateObject(SibConstants.SqlMapperBROT4.SQL_DELETE_MENU_DATA, new Object[] { id, id });

            // Update parent menu
            if (!CollectionUtils.isEmpty(listObject)) {
                long count = (long) ((HashMap<String, Object>) listObject.get(SibConstants.NUMBER.ZERO)).get(Parameters.COUNT);
                if (count == SibConstants.NUMBER.ONE) {
                    // Delete menu
                    dao.insertUpdateObject(
                        SibConstants.SqlMapperBROT4.SQL_UPDATE_ISLEAF_MENU,
                        new Object[] { SibConstants.NUMBER.ZERO, userId, id });
                }
            }
            transactionManager.commit(status);
            logger.info("Insert Menu success " + new Date());
        } catch (NullPointerException | NumberFormatException | DataAccessException e) {
            transactionManager.rollback(status);
            throw e;
        }
        SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE, "adminService", "deleteMenuData", "");
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        logger.info("deleteMenuData success " + new Date());
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.siblinks.ws.service.AdminService#insertMenuData(com.siblinks.ws.model
     * .RequestData)
     */
    @Override
    @RequestMapping(value = "/insertMenu", method = RequestMethod.POST)
    public ResponseEntity<Response> insertMenuData(@RequestBody final MenuData request) throws Exception {
        logger.info("insertMenuData " + new Date());
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            int level = 0;
            String userId = "6";// logged.getLoggedInUser().getUserid();
            // Update parent menu
            if (request.getParentid() != null && !"".equals(request.getParentid())) {
                dao.insertUpdateObject(
                    SibConstants.SqlMapperBROT4.SQL_UPDATE_ISLEAF_MENU,
                    new Object[] { SibConstants.NUMBER.ONE, userId, request.getParentid() });
                // Get Level parent
                List<Object> readObjects = dao.readObjects(
                    SibConstants.SqlMapperBROT4.SQL_GET_LEVEL_MENU_BY_ID,
                    new Object[] { request.getParentid() });
                level = 1 + (int) ((HashMap<String, Object>) readObjects.get(SibConstants.NUMBER.ZERO)).get(Parameters.LEVEL);
            }

            // Create new menu
            dao
                .insertUpdateObject(
                    SibConstants.SqlMapperBROT4.SQL_INSERT_MENU_DATA,
                    new Object[] { request.getName(), request.getImage(), request.getDescription(), request.getAction(), request
                        .getUsertype(), request.getDisplaysort(), request.getStatus(), userId, request.getParentid(), level, SibConstants.NUMBER.ZERO });

            transactionManager.commit(status);
            logger.info("Insert Menu success " + new Date());
        } catch (NullPointerException | DataAccessException e) {
            transactionManager.rollback(status);
            throw e;
        }
        SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE, "");
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.siblinks.ws.service.AdminService#updateMenuData(com.siblinks.ws.model
     * .RequestData)
     */
    @Override
    @RequestMapping(value = "/updateMenu", method = RequestMethod.POST)
    public ResponseEntity<Response> updateMenuData(@RequestBody final MenuData request) throws Exception {
        logger.info("updateMenuData " + new Date());
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        String userId = "6";// logged.getLoggedInUser().getUserid();
        try {
            // Update menu
            dao.insertUpdateObject(
                SibConstants.SqlMapperBROT4.SQL_UPDATE_MENU_DATA,
                new Object[] { request.getName(), request.getImage(), request.getDescription(), request.getStatus(), request
                    .getDisplaysort(), request.getAction(), request.getUsertype(), userId, request.getId() });

            transactionManager.commit(status);
            logger.info("updateMenu success " + new Date());
        } catch (NullPointerException | DataAccessException e) {
            transactionManager.rollback(status);
            throw e;
        }

        SimpleResponse reponse = new SimpleResponse("" + Boolean.TRUE, "");
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "/getAllMenu", method = RequestMethod.GET,
            params = { "_search", "nd", "rows", "page", "sidx", "sord", "filters" })
    public ResponseEntity<Response> getAllMenuSearch(@RequestParam(value = "_search") final boolean search, @RequestParam(
            value = "nd") final String nd, @RequestParam(value = "rows") final int rows,
            @RequestParam(value = "page") final int page, @RequestParam(value = "sidx") final String sidx, @RequestParam(
                    value = "sord") final String sord, @RequestParam(value = "filters") final String filters) throws Exception {
        System.out.println("Method: " + "getAllMenu Search");
        Object[] pramsObjects = null;
        String whereClause = "";
        if (search) {
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(filters);
            JSONArray rules = jsonObject.getJSONArray(Parameters.RULES);
            List<SearchData> readValue = mapper.readValue(rules.toString(), new TypeReference<List<SearchData>>() {
            });
            pramsObjects = new Object[readValue.size()];
            int i = 0;
            for (SearchData searchData : readValue) {
                whereClause += searchData.getField() + " like (?) OR ";
                pramsObjects[i] = "%" + searchData.getData() + "%";
                i++;
            }
            whereClause = whereClause.substring(0, whereClause.length() - SibConstants.NUMBER.THREE);
        }
        whereClause = CommonUtil.buildAppendQuery(whereClause, "displaysort", false, false);
        List<Object> readObject = dao.readObjectsWhereClause(
            SibConstants.SqlMapperBROT4.SQL_GET_ALL_MENU_DATA,
            whereClause,
            pramsObjects);
        SimpleResponse reponse = new SimpleResponse(readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.siblinks.ws.service.AdminService#getAllMenu(java.lang.String,
     * java.lang.String, int, int, java.lang.String, java.lang.String)
     */
    @Override
    @RequestMapping(value = "/getAllMenu", method = RequestMethod.GET)
    public ResponseEntity<Response> getAllMenu(@RequestParam(value = "_search") final String search,
            @RequestParam(value = "nd") final String nd, @RequestParam(value = "rows") final int rows, @RequestParam(
                    value = "page") final int page, @RequestParam(value = "sidx") final String sidx,
            @RequestParam(value = "sord") final String sord) throws Exception {
        System.out.println("Method: " + "getAllMenu No Search");
        Object[] pramsObjects = null;
        String whereClause = "";
        whereClause = CommonUtil.buildAppendQuery(whereClause, "displaysort", false, false);
        List<Object> readObject = dao.readObjectsWhereClause(
            SibConstants.SqlMapperBROT4.SQL_GET_ALL_MENU_DATA,
            whereClause,
            pramsObjects);
        SimpleResponse reponse = new SimpleResponse(readObject);
        ResponseEntity<Response> entity = new ResponseEntity<Response>(reponse, HttpStatus.OK);
        return entity;
    }

}
