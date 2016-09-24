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
import org.springframework.stereotype.Service;

import com.siblinks.ws.model.MenuData;
import com.siblinks.ws.response.Response;

/**
 * @author Hoai Nguyen
 *
 */

@Service("adminService")
public interface AdminService {
    /**
     * This method is used to get all content form Menu table
     *
     * @author Hoai Nguyen
     * @return ResponseEntity<Response> All Menu data
     * @throws Exception
     */
    public ResponseEntity<Response> getAllMenuData() throws Exception;

    /**
     * This method is used to delete item from Menu table
     * 
     * @author Hoai Nguyen
     * @return
     * @throws Exception
     */
    public ResponseEntity<Response> deleteMenuData(MenuData request) throws Exception;

    /**
     * This method is used to insert item into Menu table
     * 
     * @author Hoai Nguyen
     * @return
     * @throws Exception
     */
    public ResponseEntity<Response> insertMenuData(MenuData request) throws Exception;

    /**
     * This method is used to update exist item in Menu table
     * 
     * @author Hoai Nguyen
     * @return
     * @throws Exception
     */
    public ResponseEntity<Response> updateMenuData(MenuData request) throws Exception;

    /**
     * This method is used to get all content form Menu table
     *
     * @author hungpd
     * 
     * @param search
     * @param nd
     * @param rows
     * @param page
     * @param sidx
     * @param sord
     * @return
     * @throws Exception
     */
    ResponseEntity<Response> getAllMenu(String search, String nd, int rows, int page, String sidx, String sord) throws Exception;

    /**
     * @param search
     * @param nd
     * @param rows
     * @param page
     * @param sidx
     * @param sord
     * @param filters
     * @return
     * @throws Exception
     */
    ResponseEntity<Response> getAllMenuSearch(boolean search, String nd, int rows, int page, String sidx, String sord,
            String filters) throws Exception;
}
