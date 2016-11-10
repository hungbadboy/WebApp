package com.siblinks.ws.model;

public class Article {
	
	private String arId;
	private String authorId;
	private String title;
	private String description;
	private String image;
	private String idAdmission;
	private String limit;
	private String pageno;
	private String content;
	private String cid;
    private String active;
    private String createBy;
    private String createDate;
    private String numRate;
    private String averageRating;
    // rate
    private String uid;
    private String rating;
    
    public String getNumRate() {
        return numRate;
    }

    public void setNumRate(final String numRate) {
        this.numRate = numRate;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(final String averageRating) {
        this.averageRating = averageRating;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(final String rating) {
        this.rating = rating;
    }

    public String getArId() {
		return arId;
	}

	public void setArId(final String arId) {
		this.arId = arId;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(final String authorId) {
		this.authorId = authorId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
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

	public String getIdAdmission() {
		return idAdmission;
	}

	public void setIdAdmission(final String idAdmission) {
		this.idAdmission = idAdmission;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(final String limit) {
		this.limit = limit;
	}

	public String getPageno() {
		return pageno;
	}

	public void setPageno(final String pageno) {
		this.pageno = pageno;
	}

	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(final String cid) {
		this.cid = cid;
	}

    public String getActive() {
        return active;
    }

    public void setActive(final String active) {
        this.active = active;
    }

    /**
     * @return the createBy
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * @param createBy
     *            the createBy to set
     */
    public void setCreateBy(final String createBy) {
        this.createBy = createBy;
    }

    /**
     * @return the createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     *            the createDate to set
     */
    public void setCreateDate(final String createDate) {
        this.createDate = createDate;
    }
}
