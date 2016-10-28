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
package com.siblinks.ws.util;

/**
 *
 *
 * @author hungpd
 * @version 1.0
 */
public class SibConstants {
    public enum ROLE {
        ADMIN, STUDENT, MENTOR
    };

    public enum ROLE_TYPE {
        A, S, M
    };

    public class NUMBER {
        public static final int ZERO = 0;
        public static final int ONE = 1;
        public static final int TWO = 2;
        public static final int THREE = 3;
        public static final int NINE = 9;
    }


    public static final String URL_SEND_NOTIFICATION_FIREBASE = "https://fcm.googleapis.com/fcm/send";
    public static final String NOTIFICATION_ICON = "siblinks.icon";
    public static final String NOTIFICATION_PRIPORITY_HIGH = "high";
    public static final String NOTIFICATION_TITLE_ANSWER_QUESTION = "Answer to question";
    public static final String NOTIFICATION_TITLE_COMMENT_VIDEO = "Comment video";
    public static final String NOTIFICATION_TITLE_COMMENT_VIDEO_ADMISSION = "Comment video admission";
    public static final String NOTIFICATION_TITLE_REPLY_ESSAY = "Reply essay";

    public static final String NOTIFICATION_TYPE_ANSWER_QUESTION = "1";
    public static final String NOTIFICATION_TYPE_COMMENT_VIDEO = "2";
    public static final String NOTIFICATION_TYPE_COMMENT_VIDEO_ADMISSION = "3";
    public static final String NOTIFICATION_TYPE_REPLY_ESSAY = "4";

    public static final String DOMAIN = "domain";
    public static final String DELETE = "DELETE";
    public static final String SUCCESS = "true";
    public static final String FAILURE = "false";
    public static final int LENGHT_AUTHENTICATION = 10;
    public static final String ROOT = "/siblinks/services";
    public static final String NO_DATA = "Found no data";
    public static final String USER_NOT_EXISTS = "User is not exists";
    public static final String DEFAULT_PWD = "@123Abc";
    public static final String DOMAIN_NAME_ADMIN = "domainAdmin";
    public class MessageKey {
        public static final String REQUEST_DATA_RESUTL = "request_data_result";
    }

    public class User {
        public static final String LOGIN = "/user/login";
        public static final String LOGOUT = "/user/logout";
    }

    /**
     * Intercept request HTTP
     *
     * @author hungpd
     * @version 1.0
     */
    public class RequestAuthentication {
        public static final String PATH_AUTHENTICATION = "/siblink/services/**";
        public static final String CREATE_SUBJECT = "/subjects/createSubject";
        public static final String DELETE_SUBJECT = "/subjects/deleteSubject";
    }

    /**
     * Declare constant SQL Mapper
     *
     * @author hungpd
     * @version 1.0
     */
    public class SqlMapper {
        public static final String SQL_GET_DATE_TIME = "GET_DATE_TIME";
        public static final String SQL_VIDEO_DATA_READ = "VIDEO_DATA_READ";
        public static final String SQL_SIB_REGISTER_USER = "SIB_REGISTER_USER";
        public static final String SQL_SIB_ADMIN_REGISTER_USER = "SIB_ADMIN_REGISTER_USER";
        public static final String SQL_SIB_REGISTER_USER_EXIST = "SIB_REGISTER_USER_EXIST";
        public static final String SQL_SAVE_DEFAULT_SUBJECT = "SAVE_DEFAULT_SUBJECT";
        public static final String SQL_GET_PROFILE = "GET_PROFILE";
        public static final String SQL_FIND_USER = "FIND_USER";
        public static final String SQL_UPDATELASTONLINETIME = "UPDATELASTONLINETIME";
        public static final String SQL_GET_ALL_USERS = "GET_ALL_USERS";
        public static final String SQL_GET_USERS_BY_USER_TYPE = "GET_USERS_BY_USER_TYPE";
        public static final String SQL_GET_USER_BY_ID = "GET_USER_BY_ID";
        public static final String SQL_GET_TOKEN_BY_USERID = "GET_TOKEN_BY_USERID";
        public static final String SQL_CHECK_USER = "CHECK_USER";
        public static final String SQL_CREATE_USER_FACEBOOK = "CREATE_USER_FACEBOOK";
        public static final String SQL_UPDATE_INFO_FACEBOOK = "UPDATE_INFO_FACEBOOK";
        public static final String SQL_CREATE_USER_GOOGLE = "CREATE_USER_GOOGLE";
        public static final String SQL_UPDATE_INFO_GOOGLE = "UPDATE_INFO_GOOGLE";
        public static final String SQL_GET_USER_BY_USERNAME = "GET_USER_BY_USERNAME";
        public static final String SQL_SIB_LOGIN_USER = "SIB_LOGIN_USER";
        public static final String SQL_SIB_ADMIN_LOGIN_USER = "SIB_ADMIN_LOGIN_USER";
        public static final String SQL_SIB_RESET_PASSWORD = "SIB_RESET_PASSWORD";
        public static final String SQL_SIB_IS_USERNAME_AVAIBALE = "SIB_IS_USERNAME_AVAIBALE";
        public static final String SQL_SIB_UPDATE_USER_PROFILE = "SIB_UPDATE_USER_PROFILE";
        public static final String SQL_SIB_UPDATE_USER_PROFILE_BASIC = "SIB_UPDATE_USER_PROFILE_BASIC";
        public static final String SQL_SIB_INSERT_VIDEO = "SIB_INSERT_VIDEO";
        public static final String SQL_SIB_GET_VID = "SIB_GET_VID";
        public static final String SQL_SIB_VIDEO_MENTORS_READ = "SIB_VIDEO_MENTORS_READ";
        public static final String SQL_SIB_STUDENT_MENTORS = "SIB_STUDENT_MENTORS";
        public static final String SQL_GET_STUDENT_POST_LIST_PN = "GET_STUDENT_POST_LIST_PN";
        public static final String SQL_GET_STUDENT_POST_LIST_PN_COUNT = "GET_STUDENT_POST_LIST_PN_COUNT";
        public static final String SQL_SIB_MENTOR_REVIEWS_READ = "SIB_MENTOR_REVIEWS_READ";
        public static final String SQL_SIB_MENTOR_REVIEWS_READ_COUNT = "SIB_MENTOR_REVIEWS_READ_COUNT";
        public static final String SQL_GET_NOTE_USER = "GET_NOTE_USER";
        public static final String SQL_INSERT_USER_NOTE = "INSERT_USER_NOTE";
        public static final String SQL_SIB_INSERT_TAG = "SIB_INSERT_TAG";
        public static final String SQL_SIB_REMOVE_VIDEO = "SIB_REMOVE_VIDEO";
        public static final String SQL_SIB_GET_VIDEO = "SIB_GET_VIDEO";
        public static final String SQL_GET_RATING_VIDEO = "GET_RATING_VIDEO";
        public static final String SQL_GET_RATING_VIDEO_ADMISSION = "GET_RATING_VIDEO_ADMISSION";
        public static final String SQL_SIB_GET_TAGS = "SIB_GET_TAGS";
        public static final String SQL_SIB_GET_COMMENTS = "SIB_GET_COMMENTS";
        public static final String SQL_SIB_GET_COMMENTS_PN = "SIB_GET_COMMENTS_PN";
        public static final String SQL_SIB_GET_COMMENTS_PN_COUNT = "SIB_GET_COMMENTS_PN_COUNT";
        public static final String SQL_SIB_GET_VIDEO_ADMISSION_COMMENTS_PN = "SIB_GET_VIDEO_ADMISSION_COMMENTS_PN";
        public static final String SQL_SIB_GET_VIDEO_ADMISSION_COMMENTS_PN_COUNT = "SIB_GET_VIDEO_ADMISSION_COMMENTS_PN_COUNT";
        public static final String SQL_SIB_GET_ARTICLE_COMMENTS_PN = "SIB_GET_ARTICLE_COMMENTS_PN";
        public static final String SQL_SIB_GET_ARTICLE_COMMENTS_PN_COUNT = "SIB_GET_ARTICLE_COMMENTS_PN_COUNT";
        public static final String SQL_SIB_GET_ESSAY_COMMENTS_PN = "SIB_GET_ESSAY_COMMENTS_PN";
        public static final String SQL_SIB_GET_ESSAY_COMMENTS_PN_COUNT = "SIB_GET_ESSAY_COMMENTS_PN_COUNT";
        public static final String SQL_SIB_EDIT_VIDEO = "SIB_EDIT_VIDEO";
        public static final String SQL_SIB_DELETE_TAGS = "SIB_DELETE_TAGS";
        public static final String SQL_SIB_CHECK_RATE_VIDEO = "SIB_CHECK_RATE_VIDEO";
        public static final String SQL_SIB_CHECK_RATE_VIDEO_ADMISSION = "SIB_CHECK_RATE_VIDEO_ADMISSION";
        public static final String SQL_SIB_RATE_VIDEO = "SIB_RATE_VIDEO";
        public static final String SQL_SIB_RATE_VIDEO_ADMISSION = "SIB_RATE_VIDEO_ADMISSION";
        public static final String SQL_SIB_RATE_UPDATE_VIDEO = "SIB_RATE_UPDATE_VIDEO";
        public static final String SQL_SIB_GET_VIDEO_WITH_TOPIC = "SIB_GET_VIDEO_WITH_TOPIC";
        public static final String SQL_GET_VIDEO_ORDER_SYLLABUS_PN = "GET_VIDEO_ORDER_SYLLABUS_PN";
        public static final String SQL_GET_VIDEO_ORDER_RATING_PN = "GET_VIDEO_ORDER_RATING_PN";
        public static final String SQL_GET_VIDEO_ORDER_VIEW_PN = "GET_VIDEO_ORDER_VIEW_PN";
        public static final String SQL_GET_VIDEO_PN = "GET_VIDEO_PN";
        public static final String SQL_SIB_GET_VIDEO_WITH_TOPIC_PN_COUNT = "SIB_GET_VIDEO_WITH_TOPIC_PN_COUNT";
        public static final String SQL_SIB_GET_VIDEO_WITH_SUB_TOPIC = "SIB_GET_VIDEO_WITH_SUB_TOPIC";
        public static final String SQL_SIB_GET_VIDEO_WITH_SUB_TOPIC_PN = "SIB_GET_VIDEO_WITH_SUB_TOPIC_PN";
        public static final String SQL_SIB_GET_VIDEO_WITH_SUB_TOPIC_PN_COUNT = "SIB_GET_VIDEO_WITH_SUB_TOPIC_PN_COUNT";
        public static final String SQL_SIB_SEARCH_VIDEOS = "SIB_SEARCH_VIDEOS";
        public static final String SQL_SIB_GET_VIDEOS = "SIB_GET_VIDEOS";
        public static final String SQL_GET_VIDEOS_LIST = "GET_VIDEOS_LIST";
        public static final String SQL_GET_VIDEOS_LIST_SUBID = "GET_VIDEOS_LIST_SUBID";
        public static final String SQL_GET_VIDEOS_LIST_PN = "GET_VIDEOS_LIST_PN";
        public static final String SQL_GET_VIDEOS_LIST_SUBID_PN = "GET_VIDEOS_LIST_SUBID_PN";
        public static final String SQL_GET_VIDEOS_LIST_PN_COUNT = "GET_VIDEOS_LIST_PN_COUNT";
        public static final String SQL_GET_VIDEOS_LIST_SUBID_PN_COUNT = "GET_VIDEOS_LIST_SUBID_PN_COUNT";
        public static final String SQL_GET_HISTORY_VIDEOS_LIST = "GET_HISTORY_VIDEOS_LIST"; // MTDU
        public static final String SQL_CLEAR_HISTORY_VIDEOS_LIST = "CLEAR_HISTORY_VIDEOS_LIST"; // MTDU
        public static final String SQL_DELETE_HISTORY_VIDEO_BY_VID = "DELETE_HISTORY_VIDEO_BY_VID"; // MTDU
        public static final String SQL_SIB_GET_VIDEOS_PN = "SIB_GET_VIDEOS_PN";
        public static final String SQL_SIB_SEARCH_VIDEOS_PN = "SIB_SEARCH_VIDEOS_PN";
        public static final String SQL_SIB_SEARCH_VIDEOS_PN_COUNT = "SIB_SEARCH_VIDEOS_PN_COUNT";
        public static final String SQL_SIB_SEARCH_VIDEO_TAG = "SIB_SEARCH_VIDEO_TAG";
        public static final String SQL_SIB_SEARCH_VIDEO_TAG_PN = "SIB_SEARCH_VIDEO_TAG_PN";
        public static final String SQL_SIB_SEARCH_VIDEO_TAG_PN_COUNT = "SIB_SEARCH_VIDEO_TAG_PN_COUNT";
        public static final String SQL_SIB_GET_VIDEO_WITH_SUBJECT = "SIB_GET_VIDEO_WITH_SUBJECT";
        public static final String SQL_SIB_GET_VIDEO_WITH_SUBJECT_PN = "SIB_GET_VIDEO_WITH_SUBJECT_PN";
        public static final String SQL_SIB_GET_VIDEO_WITH_SUBJECT_PN_COUNT = "SIB_GET_VIDEO_WITH_SUBJECT_PN_COUNT";
        public static final String SQL_SIB_GET_VIDEO_WITH_TOPICS = "SIB_GET_VIDEO_WITH_TOPICS";
        public static final String SQL_SIB_GET_VIDEO_WITH_TOPICS_COUNT = "SIB_GET_VIDEO_WITH_TOPICS_COUNT";
        public static final String SQL_SIB_GET_LIST_CATEGORY_SUBSCRIPTION = "SIB_GET_LIST_CATEGORY_SUBSCRIPTION";
        public static final String SQL_SIB_GET_ALL_VIDEO_SUBSCRIPTION = "SIB_GET_ALL_VIDEO_SUBSCRIPTION";
        public static final String SQL_SIB_GET_ALL_VIDEO_SUBSCRIPTION_BY_CATEGORY = "SIB_GET_ALL_VIDEO_SUBSCRIPTION_BY_CATEGORY";
        public static final String SQL_SIB_GET_LIST_VIDEO_OF_RECENT = "SIB_GET_LIST_VIDEO_OF_RECENT";
        public static final String SQL_SIB_GET_LIST_VIDEO_OF_WEEK = "SIB_GET_LIST_VIDEO_OF_WEEK";
        public static final String SQL_SIB_GET_LIST_VIDEO_OLDER = "SIB_GET_LIST_VIDEO_OLDER";
        public static final String SQL_TOPICS_READ = "TOPICS_READ";
        public static final String SQL_SIB_ADD_COMMENT = "SIB_ADD_COMMENT";
        public static final String SQL_SIB_LAST_INSERTED_COMMENT = "SIB_LAST_INSERTED_COMMENT";
        public static final String SQL_SIB_INSERT_VIDEO_COMMENT = "SIB_INSERT_VIDEO_COMMENT";
        public static final String SQL_SIB_INSERT_VIDEO_ADMISSION_COMMENT = "SIB_INSERT_VIDEO_ADMISSION_COMMENT";
        public static final String SQL_SIB_INSERT_ESSAY_COMMENT = "SIB_INSERT_ESSAY_COMMENT";
        public static final String SQL_SIB_INSERT_ARTICLE_COMMENT = "SIB_INSERT_ARTICLE_COMMENT";
        public static final String SQL_SIB_INSERT_ANSWER_COMMENT = "SIB_INSERT_ANSWER_COMMENT";
        public static final String SQL_SEARCH_POST_ANSWERS = "SEARCH_POST_ANSWERS";
        public static final String SQL_SEARCH_ANSWER_COMMENTS = "SEARCH_ANSWER_COMMENTS";
        public static final String SQL_SIB_UPDATE_VIDEO_COMMENT = "SIB_UPDATE_VIDEO_COMMENT";
        public static final String SQL_SIB_UPDATE_ARTICLE_COMMENT = "SIB_UPDATE_ARTICLE_COMMENT";
        public static final String SQL_SIB_GET_NESTED_COMMENTS = "SIB_GET_NESTED_COMMENTS";
        public static final String SQL_SIB_INSERT_NESTED_COMMENT = "SIB_INSERT_NESTED_COMMENT";
        public static final String SQL_SIB_GET_NESTED_CID = "SIB_GET_NESTED_CID";
        public static final String SQL_SIB_INSERT_NESTED_TABLE = "SIB_INSERT_NESTED_TABLE";
        public static final String SQL_SIB_EDIT_COMMENT = "SIB_EDIT_COMMENT";
        public static final String SQL_SIB_REMOVE_COMMENT = "SIB_REMOVE_COMMENT";
        public static final String SQL_LIKE_COMMENT_UPDATE = "LIKE_COMMENT_UPDATE";
        public static final String SQL_UNLIKE_COMMENT_UPDATE = "UNLIKE_COMMENT_UPDATE";
        public static final String SQL_LIKE_COMMENT_READ = "LIKE_COMMENT_READ";
        public static final String SQL_LIKE_COMMENT_INSERT = "LIKE_COMMENT_INSERT";
        public static final String SQL_LIKE_COMEMENT_READ = "LIKE_COMEMENT_READ";
        public static final String SQL_LIKE_COMEMENT_READ_CHECK = "LIKE_UPDATE";
        public static final String SQL_LIKE_NUM_COMMENT_INSERT = "UNLIKE_UPDATE";
        public static final String SQL_LIKE_UPDATE = "LIKE_VIDEO_UPDATE";
        public static final String SQL_UNLIKE_UPDATE = "LIKE_VIDEO_INSERT";
        public static final String SQL_LIKE_VIDEO_UPDATE = "LIKE_VIDEO_READ";
        public static final String SQL_LIKE_VIDEO_INSERT = "LIKE_COMEMENT_READ_CHECK";
        public static final String SQL_LIKE_VIDEO_READ = "LIKE_NUM_COMMENT_INSERT";
        public static final String SQL_LIKE_VIDEO_READ_CHECK = "LIKE_VIDEO_READ_CHECK";
        public static final String SQL_LIKE_NUM_VIDEO_INSERT = "LIKE_NUM_VIDEO_INSERT";
        public static final String SQL_VIDEO_LIKE_UPDATE = "VIDEO_LIKE_UPDATE";
        public static final String SQL_VIDEO_UNLIKE_UPDATE = "VIDEO_UNLIKE_UPDATE";
        public static final String SQL_WATCH_VIDEO_READ = "WATCH_VIDEO_READ";
        public static final String SQL_WATCH_VIDEO_INSERT = "WATCH_VIDEO_INSERT";
        public static final String SQL_WATCH_VIDEO_UPDATE = "WATCH_VIDEO_UPDATE";
        public static final String SQL_CREATE_POST = "CREATE_POST";
        public static final String SQL_CREATE_ANSWER = "CREATE_ANSWER";
        public static final String SQL_SIB_NUM_REPLIES_UPDATE = "SIB_NUM_REPLIES_UPDATE";
        public static final String SQL_ANSWER_EDIT = "ANSWER_EDIT";
        public static final String SQL_REMOVE_ANSWER = "REMOVE_ANSWER";
        public static final String SQL_REMOVE_ANSWER_LIKE = "REMOVE_ANSWER_LIKE";
        public static final String SQL_GET_POST_INFO = "GET_POST_INFO";
        public static final String SQL_GET_POST_INFO_PN = "GET_POST_INFO_PN";
        public static final String SQL_GET_POST_INFO_PN_COUNT = "GET_POST_INFO_PN_COUNT";
        public static final String SQL_GET_POST_LIST = "GET_POST_LIST";
        public static final String SQL_GET_POST_SUBID_LIST = "GET_POST_SUBID_LIST";
        public static final String SQL_GET_POST_LIST_PN = "GET_POST_LIST_PN";
        public static final String SQL_GET_POST_LIST_SUBID_PN = "GET_POST_LIST_SUBID_PN";
        public static final String SQL_GET_POST_LIST_SUBJECTID_PN = "GET_POST_LIST_SUBJECTID_PN";
        public static final String SQL_GET_POST_LIST_SUBJECTID_PN_COUNT = "GET_POST_LIST_SUBJECTID_PN_COUNT";
        public static final String SQL_GET_POST_BY_ID = "GET_POST_BY_ID";
        public static final String SQL_GET_POST_LIST_SUBID_RELATED_PN = "GET_POST_LIST_SUBID_RELATED_PN";
        public static final String SQL_GET_POST_LIST_RELATED_PN = "GET_POST_LIST_RELATED_PN";
        public static final String SQL_GET_POST_LIST_SUBID_PN_COUNT = "GET_POST_LIST_SUBID_PN_COUNT";
        public static final String SQL_GET_POST_LIST_PN_COUNT = "GET_POST_LIST_PN_COUNT";
        public static final String SQL_GET_POST_LIST_SUBID_MOBILE = "GET_POST_LIST_SUBID_MOBILE";
        public static final String SQL_GET_POST_LIST_MOBILE = "GET_POST_LIST_MOBILE";
        public static final String SQL_GET_POST_LIST_MOBILE_COUNT = "GET_POST_LIST_MOBILE_COUNT";
        public static final String SQL_REMOVE_POST = "REMOVE_POST";
        public static final String SQL_REMOVE_ANSWER_BY_POST = "REMOVE_ANSWER_BY_POST";
        public static final String SQL_REMOVE_POST_LIKE = "REMOVE_POST_LIKE";
        public static final String SQL_GET_PID = "GET_PID";
        public static final String SQL_GET_POSTS = "GET_POSTS";
        public static final String SQL_POST_INSERT_TAG = "POST_INSERT_TAG";
        public static final String SQL_POST_GET_TAGS = "POST_GET_TAGS";
        public static final String SQL_POST_DELETE_TAGS = "POST_DELETE_TAGS";
        public static final String SQL_POST_EDIT = "POST_EDIT";
        public static final String SQL_POST_INSERT_FORUM_COMMENT = "POST_INSERT_FORUM_COMMENT";
        public static final String SQL_SEARCH_POSTS_IN_SUB_CATEGORY = "SEARCH_POSTS_IN_SUB_CATEGORY";
        public static final String SQL_POST_READ_FORUM_COMMENT = "POST_READ_FORUM_COMMENT";
        public static final String SQL_SEARCH_POST = "SEARCH_POST";
        public static final String SQL_SEARCH_POSTS_IN_SUBCATEGORY = "SEARCH_POSTS_IN_SUBCATEGORY";
        public static final String SQL_SEARCH_POSTS_WITH_TAG = "SEARCH_POSTS_WITH_TAG";
        public static final String SQL_GET_POST_LIST_SUBJECT_PN_COUNT = "GET_POST_LIST_SUBJECT_PN_COUNT";
        public static final String SQL_SUBJECTS_LIST_DATA_READ = "SUBJECTS_LIST_DATA_READ";
        public static final String SQL_SUBJECTS_READ = "SUBJECTS_READ";
        public static final String SQL_CATAGERY_READ = "CATAGERY_READ";
        public static final String SQL_GET_VEDIO_INFO = "GET_VEDIO_INFO";
        public static final String SQL_SUBCATAGERY_READ_PN = "SUBCATAGERY_READ_PN";
        public static final String SQL_SUBCATAGERY_READ_PN_COUNT = "SUBCATAGERY_READ_PN_COUNT";
        public static final String SQL_GET_COL_UNIVERSITIES = "GET_COL_UNIVERSITIES";
        public static final String SQL_GET_MAJORS = "GET_MAJORS";
        public static final String SQL_GET_EXTRA_ACTIVITIES = "GET_EXTRA_ACTIVITIES";
        public static final String SQL_MENTOR_LIST = "MENTOR_LIST";
        public static final String SQL_MENTOR_LIST_SEARCH = "MENTOR_LIST_SEARCH";
        public static final String SQL_MENTOR_LIST_FILTER = "MENTOR_LIST_FILTER";
        public static final String SQL_MENTOR_LIST_FILTER_ONLINE = "MENTOR_LIST_FILTER_ONLINE";
        public static final String SQL_TOP_MENTOR = "TOP_MENTOR";
        public static final String SQL_FETCH_FAQ = "FETCH_FAQ";
        public static final String SQL_FETCH_FAQ_TOP = "FETCH_FAQ_TOP";
        public static final String SQL_SIGNUP_COMPLETE_USER = "SIGNUP_COMPLETE_USER";
        public static final String SQL_GET_USERID = "GET_USERID";
        public static final String SQL_GET_LIST_MENTOR = "GET_LIST_MENTOR";// MTDU
        public static final String SQL_SUBJECT_DATA_INSERT = "SUBJECT_DATA_INSERT";
        public static final String SQL_SUBJECT_DATA_DELETE = "SUBJECT_DATA_DELETE";
        public static final String SQL_VIDEO_SUBJECT_MAPPING_DATA_INSERT = "VIDEO_SUBJECT_MAPPING_DATA_INSERT";
        public static final String SQL_VIDEO_SUBJECT_MAPPING_DATA_READ = "VIDEO_SUBJECT_MAPPING_DATA_READ";
        public static final String SQL_VIDEO_SUBJECT_MAPPING_DATA_READ_PN = "VIDEO_SUBJECT_MAPPING_DATA_READ_PN";
        public static final String SQL_VIDEO_SUBJECT_MAPPING_DATA_READ_PN_COUNT = "VIDEO_SUBJECT_MAPPING_DATA_READ_PN_COUNT";
        public static final String SQL_VIDEO_SUBJECT_MAPPING_DATA_UPDATE = "VIDEO_SUBJECT_MAPPING_DATA_UPDATE";
        public static final String SQL_VIDEO_SUBJECT_MAPPING_DATA_DELETE = "VIDEO_SUBJECT_MAPPING_DATA_DELETE";
        public static final String SQL_SUBJECT_SUB_CATEGORY_INSERT = "SUBJECT_SUB_CATEGORY_INSERT";
        public static final String SQL_VIDEO_SUBCATAGERY_READ = "VIDEO_SUBCATAGERY_READ";
        public static final String SQL_SUBJECT_SUB_CATEGORY_UPDATE = "SUBJECT_SUB_CATEGORY_UPDATE";
        public static final String SQL_SUBJECT_SUB_CATEGORY_DELETE = "SUBJECT_SUB_CATEGORY_DELETE";
        public static final String SQL_STUDENT_UPLOAD = "STUDENT_UPLOAD";
        public static final String SQL_MENTOR_UPLOAD = "MENTOR_UPLOAD";
        public static final String SQL_STUDENT_DOWNLOAD = "STUDENT_DOWNLOAD";
        public static final String SQL_MENTOR_DOWNLOAD = "MENTOR_DOWNLOAD";
        // public static final String SQL_ESSAY_DOWNLOAD = "ESSAY_DOWNLOAD";
        public static final String SQL_GET_URL_FILE_ESSAY = "GET_URL_FILE_ESSAY";
        public static final String SQL_GET_ESAY = "GET_ESAY";
        public static final String SQL_GET_ESAY_COUNT = "GET_ESAY_COUNT";
        public static final String SQL_GET_ESSAY_BY_ID = "GET_ESSAY_BY_ID";
        public static final String SQL_GET_ALL_ESAY = "GET_ALL_ESAY";
        public static final String SQL_GET_ALL_ESSAY_STUDENT = "GET_ALL_ESSAY_STUDENT";
        public static final String SQL_GET_ALL_ESAY_COUNT = "GET_ALL_ESAY_COUNT";
        public static final String SQL_GET_ALL_ESSAY_STUDENT_COUNT = "GET_ALL_ESSAY_STUDENT_COUNT";
        public static final String SQL_POST_DISCUSSION = "POST_DISCUSSION";
        public static final String SQL_GET_DISCUSSION = "GET_DISCUSSION";
        public static final String SQL_GET_DISCUSSION_COUNT = "GET_DISCUSSION_COUNT";
        public static final String SQL_REMOVE_ESAY = "REMOVE_ESAY";
        public static final String SQL_QUESTION_ID_LIKE = "QUESTION_ID_LIKE";
        public static final String SQL_SELECT_QUESTION = "SELECT_QUESTION";
        public static final String SQL_ANSWERID_LIKE = "ANSWERID_LIKE";
        public static final String SQL_GET_POST_ANSWER_LIST = "GET_POST_ANSWER_LIST";
        public static final String SQL_GET_POST_ANSWER_SUBID_LIST = "GET_POST_ANSWER_SUBID_LIST";
        public static final String SQL_GET_POST_ANSWER_LIST_PN = "GET_POST_ANSWER_LIST_PN";
        public static final String SQL_GET_POST_ANSWER_LIST_SUBID_PN = "GET_POST_ANSWER_LIST_SUBID_PN";
        public static final String SQL_GET_POST_ANSWER_LIST_PN_COUNT = "GET_POST_ANSWER_LIST_PN_COUNT";
        public static final String SQL_GET_POST_ANSWER_LASTEST = "GET_POST_ANSWER_LASTEST";
        public static final String SQL_UPDATE_VIEW_POST = "UPDATE_VIEW_POST";
        public static final String SQL_LIKE_POST_READ = "LIKE_POST_READ";
        public static final String SQL_UPDATE_LIKE_POST = "UPDATE_LIKE_POST";
        public static final String SQL_UPDATE_UNLIKE_POST = "UPDATE_UNLIKE_POST";
        public static final String SQL_ONE_ANSWER_FORUM = "ONE_ANSWER_FORUM";
        public static final String SQL_COUNT_ANSWER_POST_INDEX = "COUNT_ANSWER_POST_INDEX";
        public static final String SQL_LIKE_ANSWER_READ = "LIKE_ANSWER_READ";
        public static final String SQL_ANSWER_ID_LIKE = "ANSWER_ID_LIKE";
        public static final String SQL_SELECT_ANSWER = "SELECT_ANSWER";
        public static final String SQL_UPDATE_LIKE_ANSWER = "UPDATE_LIKE_ANSWER";
        public static final String SQL_UPDATE_UNLIKE_ANSWER = "UPDATE_UNLIKE_ANSWER";
        public static final String SQL_SUBJECT_GET_TAGS = "SUBJECT_GET_TAGS";
        public static final String SQL_GET_IDSUBJECT_WITH_IDCATEGORY = "GET_IDSUBJECT_WITH_IDCATEGORY";
        public static final String SQL_ACCOMPLISHMENT_READ = "ACCOMPLISHMENT_READ";
        public static final String SQL_MAJOR_READ = "MAJOR_READ";
        public static final String SQL_ACTIVITY_READ = "ACTIVITY_READ";
        public static final String SQL_HELP_READ = "HELP_READ";
        public static final String SQL_MAJOR_OF_USER = "MAJOR_OF_USER";
        public static final String SQL_ACTIVITY_OF_USER = "ACTIVITY_OF_USER";
        public static final String SQL_HELP_OF_USER = "HELP_OF_USER";
        public static final String SQL_DELETE_USER_MAJOR = "DELETE_USER_MAJOR";
        public static final String SQL_DELETE_USER_ACTIVITY = "DELETE_USER_ACTIVITY";
        public static final String SQL_DELETE_USER_TOPIC = "DELETE_USER_TOPIC";
        public static final String SQL_DELETE_USER_SUBJECT = "DELETE_USER_SUBJECT";
        public static final String SQL_INSERT_SIB_USER_MAJOR = "INSERT_SIB_USER_MAJOR";
        public static final String SQL_INSERT_SIB_USER_ACTIVITY = "INSERT_SIB_USER_ACTIVITY";
        public static final String SQL_INSERT_SIB_USER_TOPIC = "INSERT_SIB_USER_TOPIC";
        public static final String SQL_INSERT_SIB_USER_SUBJECT = "INSERT_SIB_USER_SUBJECT";
        public static final String SQL_UPDATE_PASSWORD = "UPDATE_PASSWORD";
        public static final String SQL_UPDATE_USER_CODE = "UPDATE_USER_CODE";
        public static final String SQL_GET_TOKEN_FORGOT_PASSWORD = "GET_TOKEN_FORGOT_PASSWORD";
        public static final String SQL_DELETE_TOKEN_FORGOT_PASSWORD = "DELETE_TOKEN_FORGOT_PASSWORD";
        public static final String SQL_SELECT_TIME_SEND_TOKEN = "SELECT_TIME_SEND_TOKEN";
        public static final String SQL_GET_ADDRESS_WEB = "GET_ADDRESS_WEB";
        public static final String SQL_GET_POLICY = "GET_POLICY";
        public static final String SQL_GET_TERMS = "GET_TERMS";
        // Notification
        public static final String SQL_CREATE_NOTIFICATION_QUESTION = "CREATE_NOTIFICATION_QUESTION";
        public static final String SQL_CREATE_NOTIFICATION_VIDEO = "CREATE_NOTIFICATION_VIDEO";
        public static final String SQL_CREATE_NOTIFICATION_VIDEO_ADMISSION = "CREATE_NOTIFICATION_VIDEO_ADMISSION";
        public static final String SQL_CREATE_NOTIFICATION_ARTICLE = "CREATE_NOTIFICATION_ARTICLE";
        public static final String SQL_CREATE_NOTIFICATION_ESSAY = "CREATE_NOTIFICATION_ESSAY";
        public static final String SQL_GET_NOTIFICATION_NOT_READED = "GET_NOTIFICATION_NOT_READED";
        public static final String SQL_GET_NOTIFICATION_NOT_READED_COUNT = "GET_NOTIFICATION_NOT_READED_COUNT";
        public static final String SQL_GET_NOTIFICATION_READED = "GET_NOTIFICATION_READED";
        public static final String SQL_GET_NOTIFICATION_READED_COUNT = "GET_NOTIFICATION_READED_COUNT";
        public static final String SQL_UPDATE_STATUS_NOTIFICATION = "UPDATE_STATUS_NOTIFICATION";
        public static final String SQL_UPDATE_STATUS_ALL_NOTIFICATION = "UPDATE_STATUS_ALL_NOTIFICATION";
        public static final String SQL_GET_ALL_NOTIFICATION = "GET_ALL_NOTIFICATION";
        public static final String SQL_GET_ALL_NOTIFICATION_COUNT = "GET_ALL_NOTIFICATION_COUNT";
        // End notification
        public static final String SQL_GET_LIKE_POST_BY_USER = "GET_LIKE_POST_BY_USER";
        public static final String SQL_GET_USER_POST_VIDEO = "GET_USER_POST_VIDEO";
        public static final String SQL_GET_INFO_ARTICLE = "GET_INFO_ARTICLE";
        public static final String SQL_GET_INFO_ESSAY = "GET_INFO_ESSAY";
        public static final String SQL_GET_INFO_VIDEO_ADMISSION = "GET_INFO_VIDEO_ADMISSION";
        public static final String SQL_GET_VIDEOID_OF_COMMENT = "GET_VIDEOID_OF_COMMENT";
        public static final String SQL_GET_VIDEO_ADMISSION_ID_OF_COMMENT = "GET_VIDEO_ADMISSION_ID_OF_COMMENT";
        public static final String SQL_GET_ARTICLEID_OF_COMMENT = "GET_ARTICLEID_OF_COMMENT";
        public static final String SQL_GET_ESSAYID_OF_COMMENT = "GET_ESSAYID_OF_COMMENT";
        public static final String SQL_GET_VIDEO_WITH_VIDEOID = "GET_VIDEO_WITH_VIDEOID";
        public static final String SQL_GET_VIDEO_ADMISSION_WITH_VIDEOID = "GET_VIDEO_ADMISSION_WITH_VIDEOID";
        public static final String SQL_GET_ARTICLE_WITH_ARTICLEID = "GET_ARTICLE_WITH_ARTICLEID";
        public static final String SQL_GET_ESSAY_WITH_ESSAYID = "GET_ESSAY_WITH_ESSAYID";
        public static final String SQL_GET_AUTHOR_COMMENT = "GET_AUTHOR_COMMENT";
        public static final String SQL_GET_POST_WITH_POSTID = "GET_POST_WITH_POSTID";
        public static final String SQL_GET_POSTID_WITH_ANSWER = "GET_POSTID_WITH_ANSWER";
        public static final String SQL_UPDATE_ACCOMPLISHMENT_MOBILE = "UPDATE_ACCOMPLISHMENT_MOBILE";
        public static final String SQL_UPDATE_GRADE_MOBILE = "UPDATE_GRADE_MOBILE";
        public static final String SQL_UPDATE_SCHOOL_MOBILE = "UPDATE_SCHOOL_MOBILE";
        public static final String SQL_GET_VIDEOS_LIST_BY_USER_PN = "GET_VIDEOS_LIST_BY_USER_PN";
        public static final String SQL_GET_VIDEOS_LIST_BY_USER_PN_COUNT = "GET_VIDEOS_LIST_BY_USER_PN_COUNT";
        public static final String SQL_UPDATE_VIDEO = "UPDATE_VIDEO";
        public static final String SQL_GET_ADMISSION = "GET_ADMISSION";
        public static final String SQL_GET_SUB_ADMISSION = "GET_SUB_ADMISSION";
        public static final String SQL_GET_TOPIC_FIRST_ADMISSION = "GET_TOPIC_FIRST_ADMISSION";
        public static final String SQL_GET_TOPIC_SUB_ADMISSION = "GET_TOPIC_SUB_ADMISSION";
        public static final String SQL_GET_VIDEO_ADMISSION_PN = "GET_VIDEO_ADMISSION_PN";
        public static final String SQL_GET_VIDEO_ADMISSION = "GET_VIDEO_ADMISSION";
        public static final String SQL_GET_VIDEO_ADMISSION_COUNT = "GET_VIDEO_ADMISSION_COUNT";
        public static final String SQL_GET_ARTICLES_PN = "GET_ARTICLES_PN";
        public static final String SQL_GET_ARTICLES_COUNT = "GET_ARTICLES_COUNT";
        public static final String SQL_ANSWER_ARTICLE = "ANSWER_ARTICLE";
        public static final String SQL_GET_ANSWERS_ARTICLE_PN = "GET_ANSWERS_ARTICLE_PN";
        public static final String SQL_GET_ANSWERS_ARTICLE_COUNT = "GET_ANSWERS_ARTICLE_COUNT";
        public static final String SQL_GET_ARTICLE_DETAIL = "GET_ARTICLE_DETAIL";
        public static final String SQL_GET_VIDEO_ADMISSION_WITH_TOPIC = "GET_VIDEO_ADMISSION_WITH_TOPIC";
        public static final String SQL_GET_VIDEO_ADMISSION_DETAIL = "GET_VIDEO_ADMISSION_DETAIL";
        public static final String SQL_GET_VIDEO_ADMISSION_VIEW_PN = "GET_VIDEO_ADMISSION_VIEW_PN";
        public static final String SQL_UPDATE_VIEW_VIDEO_ADMISSION = "UPDATE_VIEW_VIDEO_ADMISSION";
        public static final String SQL_UPDATE_AVARTAR_USER = "UPDATE_AVARTAR_USER";
        public static final String SQL_GET_AVATAR_USER = "GET_AVATAR_USER";
        public static final String SQL_GET_ARTICLE_BY_USER_PN = "GET_ARTICLE_BY_USER_PN";
        public static final String SQL_GET_ARTICLE_BY_USER_PN_COUNT = "GET_ARTICLE_BY_USER_PN_COUNT";
        public static final String SQL_GET_ALL_ARTICLE = "GET_ALL_ARTICLE";
        public static final String SQL_DELETE_ARTICLE = "DELETE_ARTICLE";
        public static final String SQL_GET_IMAGE_ARTICLE = "GET_IMAGE_ARTICLE";
        public static final String SQL_GET_IMAGE_UPLOAD_ESSAY = "GET_IMAGE_UPLOAD_ESSAY";
        public static final String SQL_UPDATE_ARTICLE = "UPDATE_ARTICLE";
        public static final String SQL_UPDATE_ARTICLE_NOT_IMAGE = "UPDATE_ARTICLE_NOT_IMAGE";
        public static final String SQL_CREATE_ARTICLE = "CREATE_ARTICLE";
        public static final String SQL_GET_ALL_TOPIC_SUB_ADMISSION = "GET_ALL_TOPIC_SUB_ADMISSION";
        public static final String SQL_DELETE_VIDEO_ADMISSION = "DELETE_VIDEO_ADMISSION";
        public static final String SQL_GET_IMAGE_VIDEO_ADMISSION = "GET_IMAGE_VIDEO_ADMISSION";
        public static final String SQL_GET_ALL_VIDEO_ADMISSION = "GET_LIST_ALL_VIDEO_ADMISSION";// MTDU
        public static final String SQL_UPDATE_VIDEO_ADMISSION = "UPDATE_VIDEO_ADMISSION";
        public static final String SQL_CREATE_VIDEO_ADMISSION = "CREATE_VIDEO_ADMISSION";
        public static final String SQL_GET_ALL_COMMENT = "GET_ALL_COMMENT";
        public static final String SQL_DELETE_COMMENT = "DELETE_COMMENT";
        public static final String SQL_SEARCH_ALL_VIDEO_BY_USER = "SEARCH_ALL_VIDEO_BY_USER";
        public static final String SQL_COUNT_SEARCH_ALL_VIDEO_BY_USER = "COUNT_SEARCH_ALL_VIDEO_BY_USER";
        public static final String SQL_SEARCH_ALL_VIDEO = "SEARCH_ALL_VIDEO";
        public static final String SQL_COUNT_SEARCH_ALL_VIDEO = "COUNT_SEARCH_ALL_VIDEO";
        public static final String SQL_CREATE_ABOUT_MENTOR = "CREATE_ABOUT_MENTOR";
        public static final String SQL_GET_ALL_ABOUT_MENTOR = "GET_ALL_ABOUT_MENTOR";
        public static final String SQL_GET_IMAGE_ABOUT_MENTOR = "GET_IMAGE_ABOUT_MENTOR";
        public static final String SQL_DELETE_ABOUT_MENTOR = "DELETE_ABOUT_MENTOR";
        public static final String SQL_UPDATE_ABOUT_MENTOR = "UPDATE_ABOUT_MENTOR";
        public static final String SQL_UPDATE_ABOUT_MENTOR_NOT_IMAGE = "UPDATE_ABOUT_MENTOR_NOT_IMAGE";
        public static final String SQL_UPDATE_ANSWER = "UPDATE_ANSWER";
        public static final String SQL_UPDATE_NUMVIEW_VIDEO = "UPDATE_NUMVIEW_VIDEO";
        public static final String SQL_CHECK_USER_WATCHED_VIDEO = "CHECK_USER_WATCHED_VIDEO";
        public static final String SQL_UPDATE_USER_WATCHED_VIDEO = "UPDATE_USER_WATCHED_VIDEO";
        public static final String SQL_GET_ID_VIDEO_USER_WATCHED = "GET_ID_VIDEO_USER_WATCHED";
        public static final String SQL_CHECK_USER_WATCHED_VIDEO_ADMISSION = "CHECK_USER_WATCHED_VIDEO_ADMISSION";
        public static final String SQL_UPDATE_USER_WATCHED_VIDEO_ADMISSION = "UPDATE_USER_WATCHED_VIDEO_ADMISSION";
        public static final String SQL_GET_ID_VIDEO_ADMISSION_USER_WATCHED = "GET_ID_VIDEO_ADMISSION_USER_WATCHED";
        public static final String SQL_MENU_ADMIN = "MENU_ADMIN";
        public static final String SQL_GET_STUDENT_POSTED = "GET_STUDENT_POSTED";
        public static final String SQL_CREATE_QUESTION = "CREATE_QUESTION";
        public static final String SQL_GET_NEWEST_QUESTION = "GET_NEWEST_QUESTION";
        public static final String SQL_GET_NEWEST_QUESTION_SUBJECT = "GET_NEWEST_QUESTION_BY_SUBJECT";
        public static final String SQL_GET_SUBJECT_REG = "GET_USER_SUBJECT";
        public static final String SQL_GET_ALL_CATEGORY_TOPIC = "GET_ALL_CATEGORY_TOPIC";
        public static final String SQL_GET_ALL_QUESTION = "GET_ALL_QUESTION";
        public static final String SQL_GET_VIDEO_BY_VIEW = "GET_VIDEO_BY_VIEW";
        public static final String SQL_GET_VIDEO_VIEW_BY_SUBJECT = "GET_VIDEO_VIEW_BY_SUBJECT";
        public static final String VIDEO_PLAYLIST_NEWEST_BY_SUBJECT = "GET_VIDEO_PLAYLIST_NEWEST_BY_SUBJECT";
        public static final String SQL_GET_NEWEST_VIDEO_SUBJECT = "GET_NEWEST_VIDEO_BY_SUBJECT";
        public static final String VIDEO_PLAYLIST_NEWEST_BY_USER_SUBS = "GET_VIDEO_PLAYLIST_NEWEST_BY_USER_SUBS";
        public static final String SQL_GET_VIDEO_BY_SUBJECT = "GET_VIDEO_BY_SUBJECT";
        public static final String SQL_GET_VIDEO_PLAYLIST_NEWEST = "GET_VIDEO_PLAYLIST_NEWEST";
        public static final String SQL_GET_VIDEO_PLAYLIST_RECENTLY = "GET_VIDEO_PLAYLIST_RECENTLY_UPLOAD";
        public static final String SQL_GET_VIDEO_WITH_SUBJECT_ID = "GET_VIDEO_WITH_SUBJECT_ID";
        public static final String SQL_GET_VIDEO_STUDENT_SUBCRIBE = "GET_VIDEO_STUDENT_SUBCRIBE";
        public static final String SQL_FIND_STUDENT_SUBCRIBE = "FIND_STUDENT_SUBCRIBE";
        public static final String SQL_VIDEO_RECOMMENDED_FOR_YOU = "VIDEO_RECOMMENDED_FOR_YOU";
        public static final String SQL_VIDEO_RECOMMENDED_FOR_YOU_WITH_SUB_ID = "VIDEO_RECOMMENDED_FOR_YOU_WITH_SUB_ID";
        public static final String SQL_SUBCRIBE_VIDEO_STUDENT = "SUBCRIBE_VIDEO_STUDENT";
        public static final String SQL_UN_SUBCRIBE_VIDEO_STUDENT = "UN_SUBCRIBE_VIDEO_STUDENT";
        public static final String SQL_UPDATE_SUBCRIBE_VIDEO_STUDENT = "UPDATE_SUBCRIBE_VIDEO_STUDENT";
        public static final String SQL_GET_NAME_USER = "GET_NAME_USER";
        public static final String SQL_CREATE_NOTIFICATION_SUBCRIBE = "CREATE_NOTIFICATION_SUBCRIBE";
        public static final String SQL_SELECT_SUBCRIBE = "SELECT_SUBCRIBE";
        public static final String SQL_METOR_SUBCRIBED = "GET_MENTOR_SUBCRIBED";
        public static final String SQL_COUNT_VIDEO_WATCHED = "GET_COUNT_VIDEO_WATCHED";
        public static final String SQL_COUNT_SUBCRIBED = "GET_COUNT_VIDEO_SUBCRIBE";
        public static final String SQL_COUNT_VIDEO_LIKE = "GET_COUNT_VIDEO_LIKE";
        public static final String SQL_GET_COUNT_INFO_MENTOR = "GET_COUNT_INFO_MENTOR";
        public static final String SQL_COUNT_HOME_CATEGORY = "GET_COUNT_CATEGORY_HOME";
        public static final String SQL_GET_VIDEO_UNWATCHED = "GET_VIDEO_NOT_WATCH";
        public static final String SQL_VIDEO_BY_RATE = "GET_VIDEO_BY_RATING";
        public static final String SQL_GET_MENU_BY_SUBJECT = "GET_MENU_BY_SUBJECT";
        public static final String SQL_GET_MENTOR_SUBSCRIBED_VIDEO = "GET_MENTOR_SUBSCRIBED_VIDEO";
        public static final String SQL_GET_ALL_MENTOR_SUBSCRIBED = "GET_ALL_MENTOR_SUBSCRIBED";
        public static final String SQL_MENTOR_STUDENT_SUBSCRIBED = "GET_MENTOR_STUDENT_SUBSCRIBED";
        public static final String SQL_NEW_VIDEO_MENTOR_SUBSCRIBE = "GET_NEW_VIDEO_MENTOR_SUBSCRIBED";
        public static final String SQL_NEW_VIDEO_PLAYLIST_MENTOR_SUBSCRIBED_BY_SUB = "GET_NEW_VIDEO_PLAYLIST_MENTOR_SUBSCRIBED_BY_SUB";
        public static final String SQL_GET_ALL_QUESTION_MENTOR_BY_SUBJ = "GET_ALL_QUESTION_MENTOR_BY_SUBJ";
        public static final String SQL_GET_TOP_MENTORS_MOST_LIKE = "GET_TOP_MENTORS_MOST_LIKE";
        public static final String SQL_GET_STUDENT_SUBCRIBE = "GET_STUDENT_SUBCRIBE";
        public static final String SQL_GET_TOTAL_LIKE_VIEW_VIDEO = "GET_TOTAL_LIKE_VIEW_VIDEO";
        public static final String SQL_GET_TOTAL_ANSWERS = "GET_TOTAL_ANSWERS";
        public static final String SQL_GET_TOP_MENTORS_BY_LIKE_RATE_SUBS = "GET_TOP_MENTORS_BY_LIKE_RATE_SUBS";
        public static final String SQL_GET_VIDEOS_BY_PLAYLIST = "GET_VIDEOS_BY_PLAYLIST";
        public static final String SQL_GET_STUDENT_SUBSCRIBED = "GET_STUDENT_SUBSCRIBED";
        public static final String SQL_MENTOR_GET_ACTIVITY_STUDENT = "MENTOR_GET_ACTIVITY_OF_STUDENT";
        public static final String SQL_SUBSCRIBED_FROM_MENTOR_VIEW_STUDENT = "GET_SUBSCRIBED_FROM_MENTOR_VIEW_STUDENT";
        public static final String SQL_GET_ALL_INFO_MENTOR_SUBSCRIBED = "GET_ALL_INFO_MENTOR_SUBSCRIBED";
        // Manager Category
        public static final String SQL_GET_ALL_CATEGORY = "GET_ALL_CATEGORY";
        public static final String SQL_GET_CATEGORY_BY_ID = "GET_CATEGORY_BY_ID";
        public static final String SQL_GET_LEVEL_CATEGORY_BY_ID = "GET_LEVEL_CATEGORY_BY_ID";
        public static final String SQL_DELETE_CATEGORY_DATA = "DELETE_CATEGORY_DATA";
        public static final String SQL_INSERT_CATEGORY_DATA = "INSERT_CATEGORY_DATA";
        public static final String SQL_UPDATE_CATEGORY_DATA = "UPDATE_CATEGORY_DATA";
        public static final String SQL_UPDATE_ISLEAF_CATEGORY = "UPDATE_ISLEAF_CATEGORY";
        public static final String SQL_COUNT_CHILD_CATEGORY_BY_PARENT_ID = "COUNT_CHILD_CATEGORY_BY_PARENT_ID";
        public static final String SQL_GET_STUDENT_POSTED_COUNT = "GET_STUDENT_POSTED_COUNT";
        public static final String SQL_SUBSCRIBE_UNSUBSCRIBE_MENTOR = "SUBSCRIBE_UNSUBSCRIBE_MENTOR";
        public static final String SQL_GET_ANSWER_BY_QID = "GET_ANSWER_BY_QID";
        public static final String SQL_GET_VIDEODETAIL_BY_ID = "GET_VIDEODETAIL_BY_ID";
        public static final String SQL_GET_COMMENT_VIDEO_BY_VID = "GET_COMMENT_VIDEO_BY_VID";
        public static final String SQL_CHECK_USER_HISTORY_VIDEO = "CHECK_USER_HISTORY_VIDEO";
        public static final String SQL_INSERT_HISTORY_VIDEO = "INSERT_HISTORY_VIDEO";
        public static final String SQL_SIB_GET_LIST_VIDEO_TUTTORIAL_ADMISSION = "SIB_GET_LIST_VIDEO_TUTTORIAL_ADMISSION";
        public static final String SQL_SIB_GET_LIST_ARTICLE_ADMISSION = "SIB_GET_LIST_ARTICLE_ADMISSION";
        public static final String SQL_GET_VIDEO_BY_SUBJECTID = "GET_VIDEO_BY_SUBJECTID";
        public static final String SQL_CHECK_SUBSCRIBE = "CHECK_SUBSCRIBE";
        public static final String SQL_UPDATE_AVG_RATE = "UPDATE_AVG_RATE";
        public static final String SQL_GET_ALL_SUBJECTID_CATEGORY = "GET_ALL_SUBJECTID_CATEGORY";
        public static final String SQL_CHECK_STUDENT_SUBSCRIBE = "CHECK_STUDENT_SUBSCRIBE";
        // Favourite
        public static final String SQL_VIDEO_FAVOURITE_INSERT = "VIDEO_FAVOURITE_INSERT";
        public static final String SQL_VIDEO_FAVOURITE_READ_ALL_BY_USER = "VIDEO_FAVOURITE_READ_ALL_BY_USER";
        public static final String SQL_VIDEO_FAVOURITE_READ_LIMIT = "VIDEO_FAVOURITE_READ_LIMIT";
        public static final String SQL_VIDEO_FAVOURITE_UPDATE = "VIDEO_FAVOURITE_UPDATE";
        public static final String SQL_VIDEO_UNFAVOURITE_UPDATE = "VIDEO_UNFAVOURITE_UPDATE";
        public static final String SQL_VIDEO_UNFAVOURITE_UPDATE_ALL_BY_USER = "VIDEO_UNFAVOURITE_UPDATE_ALL_BY_USER";
        public static final String SQL_VIDEO_FAVOURITE_DELETE = "VIDEO_FAVOURITE_DELETE";
        public static final String SQL_VIDEO_FAVOURITE_DELETE_ALL = "VIDEO_FAVOURITE_DELETE_ALL";
        public static final String SQL_VIDEO_FAVOURITE_COUNT_BY_USER = "VIDEO_FAVOURITE_COUNT_BY_USER";
        // Fogot Password
        public static final String SQL_CHECK_USER_FORGOT_PASSWORD = "CHECK_USER_FORGOT_PASSWORD";
        public static final String SQL_UPDATE_NUMREPLIES_QUESTION = "UPDATE_NUMREPLIES_QUESTION";

        // video
        public static final String SQL_SEARCH_VIDEO = "SEARCH_VIDEO";
        public static final String SQL_SEARCH_PLAYLIST = "SEARCH_PLAYLIST";
        public static final String SQL_GET_ALL_VIDEO = "GET_ALL_VIDEO";
        public static final String SQL_CHECK_VIDEO_FAVOURITE = "CHECK_VIDEO_FAVOURITE";

        public static final String SQL_GET_NEWEST_ANSWERS = "GET_NEWEST_ANSWERS";
        public static final String SQL_GET_VIDEO_ADMISSION_DETAIL_BY_ID = "GET_VIDEO_ADMISSION_DETAIL_BY_ID";
        public static final String SQL_GET_COMMENT_VIDEO_ADMISSION_BY_VID = "GET_COMMENT_VIDEO_ADMISSION_BY_VID";
        public static final String SQL_UPDATE_AVG_RATE_VIDEO_ADMISSION = "UPDATE_AVG_RATE_VIDEO_ADMISSION";
        public static final String SQL_GET_VIDEO_ADMISSION_BY_ADMISSION_ID = "GET_VIDEO_ADMISSION_BY_ADMISSION_ID";
        public static final String SQL_SIB_RATE_UPDATE_VIDEO_ADMISSION = "SIB_RATE_UPDATE_VIDEO_ADMISSION";
        public static final String SQL_SIB_NUM_REPLIES_UPDATE_DELETE = "SIB_NUM_REPLIES_UPDATE_DELETE";
        public static final String SQL_STUDENT_UPLOAD_ESSAY = "STUDENT_UPLOAD_ESSAY";
        public static final String SQL_STUDENT_UPDATE_ESSAY = "STUDENT_UPDATE_ESSAY";
        public static final String SQL_STUDENT_UPDATE_ESSAY_NOFILE = "STUDENT_UPDATE_ESSAY_NOFILE";

        public static final String SQL_VIDEO_COMMENT_UPDATE = "VIDEO_COMMENT_UPDATE";
        public static final String SQL_VIDEO_DEL_COMMENT_UPDATE = "VIDEO_DEL_COMMENT_UPDATE";

        // Admin Another Admin, Mentor
        public static final String SQL_ADMIN_ADD_ANOTHER_ADMIN = "SIB_ADMIN_ADD_ANOTHER_ADMIN";
        public static final String SQL_ADMIN_ADD_ANOTHER_MENTOR = "SIB_ADMIN_ADD_ANOTHER_MENTOR";
        public static final String SQL_CHECK_USER_EXISTS_BY_ID = "SQL_CHECK_USER_EXISTS_BY_ID";
        public static final String SQL_ADMIN_UPDATE_PROFILE_MENTOR = "ADMIN_UPDATE_PROFILE_MENTOR";
        public static final String SQL_SET_ENABLE_FLAG_USER = "SET_ENABLE_USER";
        public static final String SQL_UPDATE_ADMIN_INFO = "UPDATE_ADMIN_INFO";
        public static final String SQL_GET_MENTOR_ESSAY = "GET_MENTOR_ESSAY";
    }

    public class SqlMapperBROT4 {
        public static final String SQL_GET_ALL_MENU_DATA = "GET_ALL_MENU_DATA";
        public static final String SQL_GET_MENU_BY_ID = "GET_MENU_BY_ID";
        public static final String SQL_GET_LEVEL_MENU_BY_ID = "GET_LEVEL_MENU_BY_ID";
        public static final String SQL_DELETE_MENU_DATA = "DELETE_MENU_DATA";
        public static final String SQL_INSERT_MENU_DATA = "INSERT_MENU_DATA";
        public static final String SQL_UPDATE_MENU_DATA = "UPDATE_MENU_DATA";
        public static final String SQL_UPDATE_ISLEAF_MENU = "UPDATE_ISLEAF_MENU";
        public static final String SQL_COUNT_CHILD_MENU_BY_PARENT_ID = "COUNT_CHILD_MENU_BY_PARENT_ID";
    }

    public class SqlMapperBROT27 {
        public static final String SQL_GET_NEWEST_MENTOR_ANSWER = "GET_NEWEST_MENTOR_ANSWER";
    }

    public class SqlMapperBROT43 {
        public static final String SQL_GET_VIDEOS = "GET_VIDEOS";
        public static final String SQL_GET_VIDEOS_BY_SUBJECT = "GET_VIDEOS_BY_SUBJECT";
        public static final String SQL_GET_VIDEOS_TOP_VIEWED = "GET_VIDEOS_TOP_VIEWED";
        public static final String SQL_GET_VIDEOS_TOP_RATED = "GET_VIDEOS_TOP_RATED";
        public static final String SQL_GET_VIDEOS_RECENTLY = "GET_VIDEOS_RECENTLY";
        public static final String SQL_GET_STUDENT_SUBSCRIBE = "GET_STUDENT_SUBSCRIBE";
        public static final String SQL_GET_VIDEOS_PLAYLIST = "GET_VIDEOS_PLAYLIST";
        public static final String SQL_GET_VIDEO_BY_ID = "GET_VIDEO_BY_ID";
        public static final String SQL_GET_SUBJECT = "GET_SUBJECT";

        public static final String SQL_INSERT_VIDEO = "INSERT_VIDEO";
        public static final String SQL_INSERT_VIDEO_SUBCRIBE = "UPDATE_STUDENT_SUBSCRIBE";

        public static final String SQL_DELETE_VIDEO_LIKE = "DELETE_VIDEO_LIKE";
        public static final String SQL_DELETE_VIDEO_COMMENT = "DELETE_VIDEO_COMMENT";
        public static final String SQL_DELETE_VIDEO_NOTE = "DELETE_VIDEO_NOTE";
        public static final String SQL_DELETE_VIDEO_RATING = "DELETE_VIDEO_RATING";
        public static final String SQL_DELETE_VIDEO_SUBCRIBLE = "DELETE_VIDEO_SUBCRIBLE";
        public static final String SQL_DELETE_VIDEO_TAG = "DELETE_VIDEO_TAG";
        public static final String SQL_DELETE_VIDEO_PLAYLIST = "DELETE_VIDEO_PLAYLIST";
        public static final String SQL_DELETE_VIDEO = "DELETE_VIDEO";
    }

    public class SqlMapperBROT44 {
        public static final String SQL_GET_PLAYLIST = "GET_PLAYLIST";
        public static final String SQL_GET_PLAYLIST_BY_ID = "GET_PLAYLIST_BY_ID";
        public static final String SQL_INSERT_PLAYLIST = "INSERT_PLAYLIST";
        public static final String SQL_DELETE_PLAYLIST = "DELETE_PLAYLIST";
        public static final String SQL_DELETE_PLAYLIST_VIDEO = "DELETE_PLAYLIST_VIDEO";
        public static final String SQL_UPDATE_PLAYLIST = "UPDATE_PLAYLIST";
    }

    public class SqlMapperBROT70 {
        public static final String SQL_GET_COUNT_SUBSCRIBERS = "GET_COUNT_SUBSCRIBERS";
        public static final String SQL_GET_COUNT_ANSWERS = "GET_COUNT_ANSWERS";
        public static final String SQL_GET_COUNT_VIDEOS = "GET_COUNT_VIDEOS";
        public static final String SQL_GET_MENTOR_SKILLS = "GET_MENTOR_SKILLS";
        public static final String SQL_GET_COUNT_LIKES = "GET_COUNT_LIKES";
        public static final String SQL_GET_SUBSCRIBE_STAT = "GET_SUBSCRIBE_STAT";
    }

    public class SqlMapperBROT71 {
        public static final String SQL_GET_COUNT_QUESTION = "GET_COUNT_QUESTION";
        public static final String SQL_GET_COUNT_SUBSCIBE = "GET_COUNT_SUBSCIBE";
        public static final String SQL_GET_COUNT_ESSAY = "GET_COUNT_ESSAY";
        public static final String SQL_UPDATE_USER_PROFILE = "UPDATE_USER_PROFILE";
        public static final String SQL_GET_ESSAY = "GET_ESSAY";
    }

    public class SqlMapperBROT124 {
        public static final String SQL_GET_COUNT_RATINGS = "GET_COUNT_RATINGS";
        public static final String SQL_GET_COUNT_SUBSCRIBERS = "GET_COUNT_SUBSCRIBERS";
        public static final String SQL_GET_USER_SUBJECT = "GET_USER_SUBJECT";
        public static final String SQL_GET_NEWEST_QUESTIONS = "GET_NEWEST_QUESTIONS";
        public static final String SQL_GET_COUNT_RATINGS_CURDATE = "GET_COUNT_RATINGS_CURDATE";
        public static final String SQL_GET_COUNT_VIDEOS_CURDATE = "GET_COUNT_VIDEOS_CURDATE";
        public static final String SQL_GET_COUNT_ANSWERS_CURDATE = "GET_COUNT_ANSWERS_CURDATE";
        public static final String SQL_GET_COUNT_LIKES_CURDATE = "GET_COUNT_LIKES_CURDATE";
        public static final String SQL_GET_COUNT_SUBSCRIBERS_CURDATE = "GET_COUNT_SUBSCRIBERS_CURDATE";
    }

    public class SqlMapperBROT126 {
        public static final String SQL_GET_LATEST_RATING_IN_MANAGE_VIDEO = "GET_LATEST_RATING_IN_MANAGE_VIDEO";
        public static final String SQL_GET_LATEST_COMMENTS_IN_MANAGE_VIDEO = "GET_LATEST_COMMENTS_IN_MANAGE_VIDEO";
        public static final String SQL_GET_COUNT_VIEW_VIDEO = "GET_COUNT_VIEW_VIDEO";
        public static final String SQL_GET_COUNT_COMMENT_VIDEO = "GET_COUNT_COMMENT_VIDEO";
        public static final String SQL_GET_COUNT_AVG_RATING_VIDEO = "GET_COUNT_AVG_RATING_VIDEO";
        public static final String SQL_GET_COUNT_VIDEO_PLAYLIST = "GET_COUNT_VIDEO_PLAYLIST";
        public static final String SQL_GET_ALL_SUBJECTS = "GET_ALL_SUBJECTS";
        public static final String SQL_SEARCH_VIDEOS = "SEARCH_VIDEOS";
        public static final String SQL_ADD_VIDEOS_PLAYLIST = "ADD_VIDEOS_PLAYLIST";
        public static final String SQL_CHECK_VIDEO_IN_PLAYLIST = "CHECK_VIDEO_IN_PLAYLIST";
    }

    public class SqlMapperBROT163 {
        public static final String SQL_GET_COUNT_VIDEOS_IN_PLAYLIST = "GET_COUNT_VIDEOS_IN_PLAYLIST";
        public static final String SQL_SEARCH_PLAYLIST_MENTOR = "SEARCH_PLAYLIST_MENTOR";
        public static final String SQL_GET_PLAYLIST_BY_SUBJECT = "GET_PLAYLIST_BY_SUBJECT";
        public static final String SQL_GET_VIDEOS_NONE_PLAYLIST = "GET_VIDEOS_NONE_PLAYLIST";
        public static final String SQL_GET_VIDEOS_NONE_PLAYLIST_BY_SUBJECT = "GET_VIDEOS_NONE_PLAYLIST_BY_SUBJECT";
        public static final String SQL_SEARCH_VIDEOS_NONE_PLAYLIST = "SEARCH_VIDEOS_NONE_PLAYLIST";
        public static final String SQL_GET_VIDEO_DETAIL_MENTOR = "GET_VIDEO_DETAIL_MENTOR";
        public static final String SQL_GET_VIDEO_RELATED_MENTOR = "GET_VIDEO_RELATED_MENTOR";
        public static final String SQL_DELETE_VIDEO_IN_PLAYLIST = "DELETE_VIDEO_IN_PLAYLIST";
        public static final String SQL_GET_ALL_PLAYLIST = "GET_ALL_PLAYLIST";
        public static final String SQL_GET_ALL_STUDENT_SUBSCRIBED = "GET_ALL_STUDENT_SUBSCRIBED";
        public static final String SQL_GET_PLAYLIST_INFO_OF_VIDEO = "GET_PLAYLIST_INFO_OF_VIDEO";
        public static final String SQL_DELETE_COMMENT_VIDEO = "DELETE_COMMENT_VIDEO";
        public static final String SQL_GET_NEWEST_ESSAY = "GET_NEWEST_ESSAY";
        public static final String SQL_GET_PROCESSING_ESSAY = "GET_PROCESSING_ESSAY";
        public static final String SQL_GET_IGNORED_ESSAY = "GET_IGNORED_ESSAY";
        public static final String SQL_GET_REPLIED_ESSAY = "GET_REPLIED_ESSAY";
        public static final String SQL_UPDATE_STATUS_ESSAY = "UPDATE_STATUS_ESSAY";
        public static final String SQL_IGNORE_ESSAY = "IGNORE_ESSAY";
        public static final String SQL_INSERT_COMMENT_ESSAY_WITH_FILE = "INSERT_COMMENT_ESSAY_WITH_FILE";
        public static final String SQL_INSERT_COMMENT_ESSAY_WITHOUT_FILE = "INSERT_COMMENT_ESSAY_WITHOUT_FILE";
        public static final String SQL_INSERT_COMMENT_ESSAY_FK = "INSERT_COMMENT_ESSAY_FK";
        public static final String SQL_GET_COMMENT_ESSAY = "GET_COMMENT_ESSAY";
        public static final String SQL_GET_VIDEOS_IN_PLAYLIST = "GET_VIDEOS_IN_PLAYLIST";
        public static final String SQL_SEARCH_PLAYLIST_WITH_SUBJECT = "SEARCH_PLAYLIST_WITH_SUBJECT";
        public static final String SQL_SEARCH_VIDEOS_WITH_SUBJECT = "SEARCH_VIDEOS_WITH_SUBJECT";
        public static final String SQL_SEARCH_VIDEOS_NONE_PLAYLIST_WITH_SUBJECT = "SEARCH_VIDEOS_NONE_PLAYLIST_WITH_SUBJECT";
        public static final String SQL_CANCEL_ESSAY = "CANCEL_ESSAY";
        public static final String SQL_GET_SUGGESTION_ESSAY = "GET_SUGGESTION_ESSAY";
        public static final String SQL_SEARCH_REPLIED_ESSAY = "SEARCH_REPLIED_ESSAY";
        public static final String SQL_SEARCH_NEWEST_ESSAY = "SEARCH_NEWEST_ESSAY";
        public static final String SQL_SEARCH_PROCESSING_ESSAY = "SEARCH_PROCESSING_ESSAY";
        public static final String SQL_SEARCH_IGNORED_ESSAY = "SEARCH_IGNORED_ESSAY";
        public static final String SQL_GET_STATUS_ESSAY = "GET_STATUS_ESSAY";
    }

    public class SqlMapperActivityLog {
        public static final String SQL_SIB_GET_ACTIVITY_LOG_ALL = "SIB_GET_ACTIVITY_LOG_ALL";
        public static final String SQL_SIB_GET_ACTIVITY_LOG_BY_USERID = "SIB_GET_ACTIVITY_LOG_BY_USERID";
        public static final String SQL_SIB_INSERT_ACTIVITY_LOG = "SIB_INSERT_ACTIVITY_LOG";
        public static final String SQL_SIB_UPDATE_ACTIVITY_LOG = "SIB_UPDATE_ACTIVITY_LOG";
        public static final String SQL_SIB_DELETE_ACTIVITY_LOG = "SIB_DELETE_ACTIVITY_LOG";
        public static final String SQL_SIB_DELETE_ACTIVITY_LOG_BY_ID = "SIB_DELETE_ACTIVITY_LOG_BY_ID";
    }
}