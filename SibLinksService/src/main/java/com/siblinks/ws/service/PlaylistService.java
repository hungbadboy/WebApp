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
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;

/**
 * @author Hoai Nguyen
 * @version v1.0
 */
@Service("playlist")
public interface PlaylistService {
    /**
     * This method is used to get all content form Playlist table
     * 
     * @author Hoai Nguyen
     * @return ResponseEntity<Response> All Playlist data
     * @throws Exception
     */
    public ResponseEntity<Response> getPlaylist(long userid, int offset) throws Exception;

    /**
     * This method is used to insert new record into Playlist table
     * 
     * @author Hoai Nguyen
     * @return ResponseEntity<Response>
     * @throws Exception
     */
    public ResponseEntity<Response> updatePlaylist(RequestData request) throws Exception;

    /**
     * This method is used to update record from Playlist
     * 
     * @author Hoai Nguyen
     * @return ResponseEntity<Response>
     * @throws Exception
     */
    public ResponseEntity<Response> insertPlaylist(MultipartFile image, String title, String description, String url, long subjectId, long createBy)
            throws Exception;

    /**
     * This method is used to delete Playlist
     * 
     * @author Hoai Nguyen
     * @return ResponseEntity<Response>
     * @throws Exception
     */
    public ResponseEntity<Response> deletePlaylist(RequestData request) throws Exception;

    /**
     * This method is used to get Playlist by id
     * 
     * @author Hoai Nguyen
     * @return ResponseEntity<Response>
     * @throws Exception
     */
    public ResponseEntity<Response> getPlaylistById(long plid) throws Exception;

    public ResponseEntity<Response> searchPlaylist(long uid, String keyword, int offset) throws Exception;

    public ResponseEntity<Response> getPlaylistBySubject(long uid, long subjectId, int offset) throws Exception;

    public ResponseEntity<Response> deleteMultiplePlaylist(RequestData request) throws Exception;
}
