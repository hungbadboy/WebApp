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

import com.siblinks.ws.model.SibUniversity;
import com.siblinks.ws.response.Response;


@Service("universityService")
public interface UniversityService {

    /**
     * Fetch Out of box Service
     * 
     */

    ResponseEntity<Response> getAllUniversities(String search, String nd, int rows, int page, String sidx, String sord);

    public ResponseEntity<Response> registerSchool(SibUniversity data);

    /**
     * @param data
     * @return
     */
    ResponseEntity<Response> getSchoolInfo(long schoolid);

    /**
     * @param data
     * @return
     */
    ResponseEntity<Response> updateSchoolInfo(SibUniversity data);

    /**
     * @param data
     * @return
     */
    ResponseEntity<Response> deleteSChool(SibUniversity data);

}
