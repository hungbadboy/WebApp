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

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brot.admin.client.MobileServiceManagerClient;
import com.brot.admin.exception.ServiceConnectionException;
import com.brot.admin.resource.AppConfigManager;
import com.brot.admin.util.ApplicationConstants;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Clear session
 *
 * @author hungpd
 * @version 1.0
 */
public class LogoutAction extends BrotCommonAction {
    private final Logger logger = LoggerFactory.getLogger(LogoutAction.class);
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String execute() {
        HttpSession session = request.getSession();
        session.invalidate();
        String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
        endPointUrl = endPointUrl + ApplicationConstants.RequestMappingAPI.LOGOUT;
        MobileServiceManagerClient _instance = MobileServiceManagerClient.getInstance();
        try {
            _instance.write(null, endPointUrl);
        } catch (ServiceConnectionException e) {
            logger.debug("Couldn't access to server " + e.getMessage());
        }
        return ActionSupport.SUCCESS;
    }
}
