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
package com.brot.admin.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;

import com.brot.admin.client.MobileServiceManagerClient;
import com.brot.admin.exception.ServiceConnectionException;
import com.brot.admin.resource.AppConfigManager;

/**
 * @author Tavv
 *
 */
public class MenuHelper {

    public String getListMenuAdmin() throws HttpException, IOException, ServiceConnectionException {
        String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
        Map<String, String> parameter = new HashMap<String, String>();
        System.out.println("inside doFilter.....------------------------" + endPointUrl);
        endPointUrl = endPointUrl + "menu/admin";
        // Call web services to get menu
        MobileServiceManagerClient _instance = MobileServiceManagerClient.getInstance();
        // response data
        String response = _instance.callServiceAPI("GET", endPointUrl, parameter);
        System.out.println("response === " + response);
        return response;
    }
}
