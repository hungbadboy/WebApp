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
package com.brot.admin.action;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brot.admin.exception.ServiceConnectionException;
import com.brot.admin.filter.BrotAuthenticationFilter;
import com.brot.admin.helper.MenuHelper;
import com.brot.admin.model.MenuItem;
import com.brot.admin.util.Parameters;
import com.opensymphony.xwork2.ActionSupport;



/**
 * This is class home action. It handle to get menu for administrator user
 *
 * @author Tavv
 *
 */
public class HomeAction extends BrotCommonAction {
    private final Logger logger = LoggerFactory.getLogger(BrotAuthenticationFilter.class);

    public Logger getLogger() {
        return logger;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String execute() throws HttpException, IOException {

        MenuHelper menuHelper = new MenuHelper();
        try {
            String strMenuAdmin = menuHelper.getListMenuAdmin();
            JSONObject jsonObject = new JSONObject(strMenuAdmin);
                JSONArray object = jsonObject.getJSONArray(Parameters.REQUEST_DATA_RESULT);

                if (!StringUtils.isEmpty(strMenuAdmin)) {
                    ObjectMapper mapper = new ObjectMapper();
                    List<MenuItem> listMenu = mapper.readValue(
                        object.toString().toLowerCase(),
                        new TypeReference<List<MenuItem>>() {
                        });
                    (ServletActionContext.getRequest().getSession()).setAttribute(Parameters.MENUS, listMenu);
                }
        } catch (ServiceConnectionException e) {
            e.printStackTrace();
        }

        return ActionSupport.SUCCESS;
    }

    @Override
    public void prepare() throws Exception {
        super.prepare();

    }
}
