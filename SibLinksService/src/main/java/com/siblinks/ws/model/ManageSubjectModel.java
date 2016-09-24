/**
 * 
 */
package com.siblinks.ws.model;

/**
 * @author hungpd
 *
 */
public class ManageSubjectModel {
    private String id;
	private String name;
    private String image;
	private String description;
	private String status;
	private String isForum;
    private String parentId;
    private String displaySort;
    /**
     * @return the isForum
     */
    public String getIsForum() {
        return isForum;
    }

    /**
     * @param isForum
     *            the isForum to set
     */
    public void setIsForum(final String isForum) {
        this.isForum = isForum;
    }

    /**
     * @return the parentId
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     *            the parentId to set
     */
    public void setParentId(final String parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the displaySort
     */
    public String getDisplaySort() {
        return displaySort;
    }

    /**
     * @param displaySort
     *            the displaySort to set
     */
    public void setDisplaySort(final String displaySort) {
        this.displaySort = displaySort;
    }

	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(final String image) {
        this.image = image;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(final String status) {
        this.status = status;
    }
	
	

}
