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

import com.siblinks.ws.model.FavouriteData;
import com.siblinks.ws.response.Response;

/**
 * 
 * @author hungpd
 * @version 1.0
 */
public interface FavouriteVideoService {

    /**
     * This method count total favourite by user
     * 
     * @param video
     * @return
     */
    public ResponseEntity<Response> getCountFavourite(String uid) throws Exception;

    /**
     * This method get all favourite by user
     * 
     * @param video
     * @return
     */
    public ResponseEntity<Response> getAllFavouriteByUid(String uid) throws Exception;

    /**
     * This method get limit favourites by user
     * 
     * @param video
     * @return
     */
    public ResponseEntity<Response> getFavouriteByUid(int uid, int limit, long favouritetime) throws Exception;

    /**
     * This method add favourite video
     * 
     * @param video
     * @return
     */
    public ResponseEntity<Response> addFavouriteVideo(FavouriteData favouriteData) throws Exception;
	
    /**
     * This method delete favourites video
     * 
     * @param video
     * @return
     */
    public ResponseEntity<Response> deleteFavouriteVideo(final FavouriteData favouriteData);
	
}
