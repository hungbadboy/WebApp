package com.siblinks.ws.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bkagidal
 *
 */
public class Video {
	
	private String vid;
	private String title;
	private String author;
	private String authorID;
	private String subject;
	private String topic;
	private String subtopic;
	private String description;
	private String image;
	private String url;
	private String runningTime;
	private String tag;
	private List<Tag> tags;
	private String content;
	private String rating;
	private String pageno;
	
	private String topicId;
	
	private String aid;
	private String faqCategory;
	
	private String stringJson;
	
	private String limit;
	private String page;
	private String[] fieldSearch;
	
	private String totalCountFlag;
	private String subjectId;
	private String order;
	private String name;
	private String message;
	
	private String email;
	private String firstname;
	private String lastname;
	private String password;
	private String dob;
	private String education;
	private String accomp;
	private String colmajor;
	
	private String activities;
	private String helpin;
	
	private String familyincome;
	private String yourdream;
	private String bio;
	private String usertype;
	private String username;
	private String accomplishments;
	private String imageurl;
	
	private String currentclass;
	
	private String subjects;

	private String helpsubjects;
	private String category;
	private String subcategory;
	private String uid;
	private String mentorid;
	private String studentid;
	private String note;
	private String cid;
	private String pid;
	
	private String essayId;
	
	private String status;
	
	private String[] fillterBy;
	private String keySearch;
	
	private String majorid;
	private String major;
	private String activityid;
	private String activity;
	
	private String nid;
	private String type;
	private String notification;
	
	private String facebookid;
	private String googleid;
	private String offset; 
	
    private String plid;

     private ArrayList<String> vids;

     public ArrayList<String> getVids() {
          return vids;
     }

     public void setVids(final ArrayList<String> vids) {
          this.vids = vids;
     }

     public String getPlid() {
        return plid;
    }

    public void setPlid(final String plid) {
        this.plid = plid;
    }

    private String studentId;
    private String mentorId;

	public String[] getFieldSearch() {
		return fieldSearch;
	}
	
	public void setFieldSearch(final String[] fieldSearch) {
		this.fieldSearch = fieldSearch;
	}
	
	public String[] getFillterBy() {
		return fillterBy;
	}
	
	public void setFillterBy(final String[] fillterBy) {
		this.fillterBy = fillterBy;
	}
	
	public String getKeySearch() {
		return keySearch;
	}
	
	public void setKeySearch(final String keySearch) {
		this.keySearch = keySearch;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(final String status) {
		this.status = status;
	}
	
	public String getEssayId() {
		return essayId;
	}
	
	public void setEssayId(final String essayId) {
		this.essayId = essayId;
	}
	
	public String getAccomp() {
		return accomp;
	}
	
	public String getAccomplishments() {
		return accomplishments;
	}
	
	public String getActivities() {
		return activities;
	}
	
	public String getAid() {
		return aid;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getAuthorID() {
		return authorID;
	}
	
	public String getBio() {
		return bio;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getCid() {
		return cid;
	}
	
	public String getColmajor() {
		return colmajor;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getCurrentclass() {
		return currentclass;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getDob() {
		return dob;
	}
	
	public String getEducation() {
		return education;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getFamilyincome() {
		return familyincome;
	}
	
	public String getFaqCategory() {
		return faqCategory;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public String getHelpin() {
		return helpin;
	}
	
	public String getHelpsubjects() {
		return helpsubjects;
	}
	
	public String getImage() {
		return image;
	}
	
	public String getImageurl() {
		return imageurl;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public String getLimit() {
		return limit;
	}
	
	public String getPage(){
		return (Integer.parseInt(page) - 1) * Integer.parseInt(limit) + "";
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getName() {
		return name;
	}
	
	public String getOrder() {
		return order;
	}
	
	public String getPageno() {
		return pageno;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getPid() {
		return pid;
	}
	
	public String getRating() {
	    return this.rating;
	}
	
	public String getRunningTime() {
		return runningTime;
	}
	
	public String getStringJson() {
		return stringJson;
	}
	
	public String getSubcategory() {
		return subcategory;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getSubjectId() {
		return subjectId;
	}
	
	public String getSubjects() {
		return subjects;
	}
	
	public String getSubtopic() {
		return subtopic;
	}
	
	public String getTag() {
		return tag;
	}
	
	public List<Tag> getTags() {
		return tags;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public String getTopicId() {
		return topicId;
	}
	
	public String getTotalCountFlag() {
		return totalCountFlag;
	}
	
	public String getUid() {
		return uid;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getUsertype() {
		return usertype;
	}
	
	public String getVid() {
		return vid;
	}
	
	public String getYourdream() {
		return yourdream;
	}
	
	public String getMentorid() {
		return mentorid;
	}
	
	public void setMentorid(final String mentorid) {
        this.mentorid = mentorid;
    }
	public String getStudentid() {
		return studentid;
	}

	public void setStudentid(final String studentid) {
		this.studentid = studentid;
	}
	
	public void setAccomp(final String accomp) {
		this.accomp = accomp;
	}
	
	public void setAccomplishments(final String accomplishments) {
		this.accomplishments = accomplishments;
	}
	
	public void setActivities(final String activities) {
		this.activities = activities;
	}
	
	public void setAid(final String aid) {
		this.aid = aid;
	}
	
	public void setAuthor(final String author) {
		this.author = author;
	}
	
	public void setAuthorID(final String authorID) {
		this.authorID = authorID;
	}
	
	public void setBio(final String bio) {
		this.bio = bio;
	}
	
	public void setCategory(final String category) {
		this.category = category;
	}
	
	public void setCid(final String cid) {
		this.cid = cid;
	}
	
	public void setColmajor(final String colmajor) {
		this.colmajor = colmajor;
	}
	
	public void setContent(final String content) {
		this.content = content;
	}
	
	public void setCurrentclass(final String currentclass) {
		this.currentclass = currentclass;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}

	public void setDob(final String dob) {
		this.dob = dob;
	}
	
	public void setEducation(final String education) {
		this.education = education;
	}
	
	public void setEmail(final String email) {
		this.email = email;
	}
	
	public void setFamilyincome(final String familyincome) {
		this.familyincome = familyincome;
	}
	
	public void setFaqCategory(final String faqCategory) {
		this.faqCategory = faqCategory;
	}
	
	public void setFirstname(final String firstname) {
		this.firstname = firstname;
	}
	
	public void setHelpin(final String helpin) {
		this.helpin = helpin;
	}
	
	public void setHelpsubjects(final String helpsubjects) {
		this.helpsubjects = helpsubjects;
	}
	
	public void setImage(final String image) {
		this.image = image;
	}
	
	public void setImageurl(final String imageurl) {
		this.imageurl = imageurl;
	}
	
	public void setLastname(final String lastname) {
		this.lastname = lastname;
	}
	
	public void setLimit(final String limit) {
		this.limit = limit;
	}
	
	public void setPage(final String page) {
		this.page = page;
	}
	
	public void setMessage(final String message) {
		this.message = message;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public void setOrder(final String order) {
		this.order = order;
	}
	
	public void setPageno(final String pageno) {
		this.pageno = pageno;
	}
	
	public void setPassword(final String password) {
		this.password = password;
	}
	public void setPid(final String pid) {
		this.pid = pid;
	}
	
	public void setRating(final String rating) {
	    this.rating = rating;
	}
	
	public void setRunningTime(final String runningTime) {
		this.runningTime = runningTime;
	}
	
	public void setStringJson(final String stringJson) {
		this.stringJson = stringJson;
	}
	
	public void setSubcategory(final String subcategory) {
		this.subcategory = subcategory;
	}
	
	public void setSubject(final String subject) {
		this.subject = subject;
	}
	
	public void setSubjectId(final String subjectId) {
		this.subjectId = subjectId;
	}
	
	public void setSubjects(final String subjects) {
		this.subjects = subjects;
	}
	
	public void setSubtopic(final String subtopic) {
		this.subtopic = subtopic;
	}
	
	public void setTag(final String tag) {
		this.tag = tag;
	}
	
	public void setTags(final List<Tag> tags) {
		this.tags = tags;
	}
	
	public void setTitle(final String title) {
		this.title = title;
	}
	
	public void setTopic(final String topic) {
		this.topic = topic;
	}
	
	public void setTopicId(final String topicId) {
		this.topicId = topicId;
	}
	
	public void setTotalCountFlag(final String totalCountFlag) {
		this.totalCountFlag = totalCountFlag;
	}
	
	public void setUid(final String uid) {
		this.uid = uid;
	}
	
	public void setUrl(final String url) {
		this.url = url;
	}
	
	public void setUsername(final String username) {
		this.username = username;
	}
	
	public void setUsertype(final String usertype) {
		this.usertype = usertype;
	}
	
	public void setVid(final String vid) {
		this.vid = vid;
	}
	
	public void setYourdream(final String yourdream) {
		this.yourdream = yourdream;
	}
	
	public String getNote() {
		return note;
	}
	
	public void setNote(final String note) {
		this.note = note;
	}
	
	public String getMajorid() {
		return majorid;
	}
	
	public void setMajorid(final String majorid) {
		this.majorid = majorid;
	}
	
	public String getMajor() {
		return major;
	}
	
	public void setMajor(final String major) {
		this.major = major;
	}
	
	public String getActivityid() {
		return activityid;
	}
	
	public void setActivityid(final String activityid) {
		this.activityid = activityid;
	}
	
	public String getActivity() {
		return activity;
	}
	
	public void setActivity(final String activity) {
		this.activity = activity;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(final String type) {
		this.type = type;
	}
	
	public String getNid() {
		return nid;
	}
	
	public void setNid(final String nid) {
		this.nid = nid;
	}
	
	public String getNotification() {
		return notification;
	}
	
	public void setNotification(final String notification) {
		this.notification = notification;
	}
	
	public String getFacebookid() {
		return facebookid;
	}
	
	public void setFacebookid(final String facebookid) {
		this.facebookid = facebookid;
	}

	public String getGoogleid() {
		return googleid;
	}

	public void setGoogleid(final String googleid) {
		this.googleid = googleid;
	}

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(final String studentId) {
        this.studentId = studentId;
    }

    public String getMentorId() {
        return this.mentorId;
    }

    public void setMentorId(final String mentorId) {
        this.mentorId = mentorId;
    }
	public String getOffset() {
		return offset;
	}

	public void setOffset(final String offset) {
		this.offset = offset;
	}

}
