package com.siblinks.ws.model;

public class VideoAdmission extends Video {
	
	private String vId;
	private String authorId;
	private String youtubeUrl;
	private String idTopicSubAdmission;
	private String idSubAdmission;
    private String title;
    private String description;
    private String image;
    private String active;
	
	public String getvId() {
		return vId;
	}
	
	public void setvId(final String vId) {
		this.vId = vId;
	}
	
	public String getAuthorId() {
		return authorId;
	}
	
	public void setAuthorId(final String authorId) {
		this.authorId = authorId;
	}

	public String getYoutubeUrl() {
		return youtubeUrl;
	}

	public void setYoutubeUrl(final String youtubeUrl) {
		this.youtubeUrl = youtubeUrl;
	}

	public String getIdTopicSubAdmission() {
		return idTopicSubAdmission;
	}

	public void setIdTopicSubAdmission(final String idTopicSubAdmission) {
		this.idTopicSubAdmission = idTopicSubAdmission;
	}

	public String getIdSubAdmission() {
		return idSubAdmission;
	}

	public void setIdSubAdmission(final String idSubAdmission) {
		this.idSubAdmission = idSubAdmission;
	}

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public void setImage(final String image) {
        this.image = image;
    }

    public String getActive() {
        return active;
    }

    public void setActive(final String active) {
        this.active = active;
    }
	
}
