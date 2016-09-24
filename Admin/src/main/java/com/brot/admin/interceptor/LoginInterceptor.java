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
package com.brot.admin.interceptor;

import java.util.Map;

import com.brot.admin.util.ApplicationConstants;
import com.brot.admin.util.Parameters;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * Checking session time out or
 *
 * @author hungpd
 * @version 1.0
 */
public class LoginInterceptor extends AbstractInterceptor {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String intercept(final ActionInvocation invocation) throws Exception {

        Map<String, Object> sessionAttributes = invocation.getInvocationContext().getSession();
        if (sessionAttributes == null ||
            sessionAttributes.get(Parameters.USER_INFO) == null ||
            sessionAttributes.get(Parameters.USER_INFO).equals("")) {
            return ApplicationConstants.STATUS_LOGIN;
        } else {
            return invocation.invoke();
        }
    }
}
