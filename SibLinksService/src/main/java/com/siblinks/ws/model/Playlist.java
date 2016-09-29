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
package com.siblinks.ws.model;

import java.util.ArrayList;

/**
 * @author Hoai Nguyen
 * @version v1.0
 */
public class Playlist {
    private String plid;
    private String name;
    private String description;
    private String image;
    private String url;
    private String subjectId;
    private String createBy;
    private ArrayList<String> plids;

    // private String numView;
    // private String numComment;
    // private String numLike;
    // private String numRate;

    public ArrayList<String> getPlids() {
        return plids;
    }

    public void setPlids(final ArrayList<String> plids) {
        this.plids = plids;
    }

    public String getPlid() {
        return plid;
    }

    public void setPlid(final String plid) {
        this.plid = plid;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(final String subjectId) {
        this.subjectId = subjectId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(final String createBy) {
        this.createBy = createBy;
    }

    // public String getNumView() {
    // return numView;
    // }
    //
    // public void setNumView(final String numView) {
    // this.numView = numView;
    // }
    // public String getNumComment() {
    // return numComment;
    // }
    //
    // public void setNumComment(final String numComment) {
    // this.numComment = numComment;
    // }
    // public String getNumLike() {
    // return numLike;
    // }
    //
    // public void setNumLike(final String numLike) {
    // this.numLike = numLike;
    // }
    // public String getNumRate() {
    // return numRate;
    // }
    //
    // public void setNumRate(final String numRate) {
    // this.numRate = numRate;
    // }
}
