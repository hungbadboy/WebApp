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


import org.springframework.http.ResponseEntity;

import com.siblinks.ws.common.DAOException;
import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;

/**
 * This class will to send email for user when user request a Contact or Forgot
 * password
 *
 * @author hungpd
 * @version 1.0
 */
public interface NotificationEmailService {

    /**
     * This method send email for user.
     *
     * @param request
     *            Parameter is user name that is email
     * @return Message alert for user sent to email
     */
    public ResponseEntity<Response> contact(RequestData contact) throws DAOException;

    /**
     * This method is update token in DB then send email for verify to new
     * password
     *
     * @param request
     *            Parameter is user name that is email
     * @return Message alert for user sent to email
     */
    public ResponseEntity<Response> forgotPassword(RequestData request) throws DAOException;
}
