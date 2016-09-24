package com.siblinks.ws.model;

/**
 * @author hungpd
 * @version 1.0
 */
public class MenuData {
    private String name;
    private String image;
    private String description;
    private String action;
    private String status;
    private String displaysort;
    private String oper;
    private String id;
    private String parentid;
    private String usertype;

    /**
     * @return the usertype
     */
    public String getUsertype() {
        return usertype;
    }

    /**
     * @param usertype
     *            the usertype to set
     */
    public void setUsertype(final String usertype) {
        this.usertype = usertype;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
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

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getDisplaysort() {
        return displaysort;
    }

    public void setDisplaysort(final String displaysort) {
        this.displaysort = displaysort;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(final String oper) {
        this.oper = oper;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @return the parentid
     */
    public String getParentid() {
        return parentid;
    }

    /**
     * @param parentid
     *            the parentid to set
     */
    public void setParentid(final String parentid) {
        this.parentid = parentid;
    }
}
