brotServices.factory('VideoService', ['$http', function ($http) {
    var factory = {};

    var GetCountType = {
        SUBSCRIPTION: 0,
        HISTORY: 1,
        FAVOURITE: 2
    };

    factory.getListCategorySubscription = function () {
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getListCategorySubscription'
        }).success(function (data) {
            return data.response;
        });
        return promise;
    };

    factory.getListVideoSubscription = function (userId, subjectId) {
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getListVideoSubscription?userId=' + userId + '&subjectId=' + subjectId
        }).success(function (data) {
            return data.response;
        });
        return promise;
    };


    factory.getListVideoOfRecent = function (subjectId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/getListVideoOfRecent?subjectId=' + subjectId
        }).success(function (data) {
            return data.response;
        });
        return promise;
    };

    factory.getListVideoOfWeek = function (subjectId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/getListVideoOfWeek?subjectId=' + subjectId
        }).success(function (data) {
            return data.response;
        });
        return promise;
    };

    factory.getListVideoOlder = function (userId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/getListVideoOlder?userId=' + userId
        }).success(function (data) {
            return data.response;
        });
        return promise;
    };

    factory.getAllVideoSubscription = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/getAllVideoSubscription'
        }).success(function (data) {
            return data.response;
        });
        return promise;
    };
    
    var nameOfUser = "";

    factory.getVideoBySubject = function (userId, subjectId, limit, offset) {
        var rs = null ;
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getVideoBySubject?userId=' + userId + '&subjectId=' + subjectId + '&limit=' + limit + '&offset=' + offset
        }).success(function (json) {
            rs = json;
            return rs;
        });
        return promise;
    };

    factory.getSubjects = function () {
        var rs = null;
        var promise = $http({
            method : 'GET',
            url : NEW_SERVICE_URL + 'categoryService/getSubjects'
        }).success(function (response) {
            rs = response;
            return rs;
        });
        return promise;
    }

    factory.getVideosWithTopic = function (idTopic) {
        var pathWS = NEW_SERVICE_URL + 'video/getVideosWithTopic';
        return $http.post(pathWS, {
            "request_data_type": "video",
            "request_data_method": "get_using_topic",
            "request_data": {
                "topicId": idTopic
            }

        }).success(function (data) {
        });
    };

    factory.getVideoWithTopicPN = function (subjectId, topicId, page, order) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/getVideosWithTopicPN',
            data: {
                "request_data_type": "video",
                "request_data_method": "getVideosWithTopicPN",
                "request_data": {
                    "subjectId": subjectId,
                    "topicId": topicId,
                    "pageno": page,
                    "limit": 10,
                    "order": order,
                    "totalCountFlag": true
                }
            }
        });
        return promise;
    };

    // factory.getVideosWithTopic = function(vid, subject,topic){
    //      var promise = $http({
    //         // method: 'POST',
    //         // url: NEW_SERVICE_URL + 'video/getVideosWithTopic',
    //         // data: {
    //         //     "request_data_type": "video",
    //         //     "request_data_method": "get_using_topic",
    //         //     "request_data": {
    //         //         "vid": vid,
    //         //         "subject": subject,
    //         //         "topic": topic
    //         //     }
    //         // }


    //         method: 'POST',
    //         url: NEW_SERVICE_URL + 'video/searchVideosUsingTag',
    //         data: {
    //             "request_data_type": "video",
    //             "request_data_method": "search_videos",
    //             "request_data": {
    //                 "tag": "math",
    //                 "vid": 1
    //             }
    //         }
    //     }).success(function(data){
    //         return data.response;
    //     });
    //     return promise;
    // }

    // factory.searchVideos = function(title, vid){
    //      var promise = $http({

    //         method: 'POST',
    //         url: NEW_SERVICE_URL + 'video/searchVideos',
    //         data: {
    //             "request_data_type": "video",
    //             "request_data_method": "search_videos",
    //             "request_data": {
    //                 "title": title,
    //                 "vid": vid
    //             }
    //         }
    //     }).success(function(data){
    //         return data.response;
    //     });
    //     alert("server don't support");
    //     return promise;
    // }

    factory.searchAllVideos = function (title) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/searchVideos',
            data: {
                "request_data_type": "video",
                "request_data_method": "search_videos",
                "request_data": {
                    "title": title
                }
            }
        });
        return promise;
    };

    factory.searchVideos = function (title, page) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/searchVideosPN',
            data: {
                "request_data_type": "video",
                "request_data_method": "search_videos_with_PN",
                "request_data": {
                    "title": title,
                    "pageno": page,
                    "limit": 10,
                    "totalCountFlag": true
                }
            }
        });
        return promise;
    };

    factory.markVideoAsWatched = function (uid, vid) {
        alert("server don't support");
        return $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'markVideoAsWatched?uid=' + uid + '&vid=' + vid
        });
    };

    factory.searchAllVideo = function (keySearch, page, order) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/searchAllVideo',
            data: {
                "request_data_type": "video",
                "request_data_method": "searchAllVideo",
                "request_data": {
                    "title": keySearch,
                    "description": keySearch,
                    "pageno": page,
                    "order": order,
                    "limit": 10
                }
            }
        });
        return promise;
    };

    factory.getVideoUserWatched = function (uid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/getIdVideoWatched',
            data: {
                "request_data_type": "video",
                "request_data_method": "getIdVideoWatched",
                "request_data": {
                    "uid": uid
                }
            }
        });
        return promise;
    };


    factory.setSubscribeMentor = function (studentId, mentorId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/setSubscribeMentor',
            data: {
                "request_data_type": "video",
                "request_data_method": "setSubscribeMentor",
                "request_data": {
                    "studentId": studentId,
                    "mentorId": mentorId
                }
            }
        });
        return promise;
    };

    factory.rateVideo = function (uid, vid, rate) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/rateVideo',
            data: {
                "request_data_type": "video",
                "request_data_method": "rateVideo",
                "request_data": {
                    "uid": uid,
                    "vid": vid,
                    "rating": rate
                }
            }
        });
        return promise;
    };

    factory.checkUserRatingVideo = function (uid, vid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/checkUserRatingVideo',
            data: {
                "request_data_type": "video",
                "request_data_method": "checkUserRatingVideo",
                "request_data": {
                    "uid": uid,
                    "vid": vid
                }
            }
        });
        return promise;
    };

    factory.getRatingVideo = function (vid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/getRating',
            data: {
                "request_data_type": "video",
                "request_data_method": "getRating",
                "request_data": {
                    "vid": vid
                }
            }
        });
        return promise;
    };


    factory.getVideoByUserSubject = function (uid, limit, offset) {
        var rs = null;
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getVideoByUserSubject?uId=' + uid + '&limit=' + limit + '&offset=' + offset
        }).success(function (json) {
            rs = json;
            return rs;
        });
        return promise;
    };


    factory.getVideoByViews = function (limit, offset) {
        var rs = null;
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getVideoByViews?limit=' + limit + '&offset=' + offset
        }).success(function (json) {
            rs = json;
            return rs;
        });
        return promise;
    };

    factory.getVideoByRate = function (limit, offset) {
        var rs = null;
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getVideoByRate?limit=' + limit + '&offset=' + offset
        }).success(function (json) {
            rs = json;
            return rs;
        });
        return promise;
    };


    factory.getVideoPlaylistNewest = function (limit, offset) {
        var rs = null;
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getVideoPlaylistNewest?limit=' + limit + '&offset=' + offset
        }).success(function (json) {
            rs = json;
            return rs;
        });
        return promise;
    };

    factory.getVideoStudentSubcribe = function (userId, limit, offset) {
        var rs = null;
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getVideoStudentSubcribe?userId=' + userId + '&limit=' + limit + '&offset=' + offset
        }).success(function (json) {
            rs = json;
            return rs;
        });
        return promise;
    };

    factory.getMentorSubscribed = function (studentId, limit, offset) {
        var rs = null;
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'student/getMentorSubscribed?studentId=' + studentId + '&limit=' + limit + '&offset=' + offset
        }).success(function (data) {
            rs = data;
            return data;
        });
        return promise;
    };


    factory.getNewVideoMentorSubscribe = function (userId, limit, offset) {
        var rs = null;
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getNewVideoMentorSubscribe?userId=' + userId + '&limit=' + limit + '&offset=' + offset
        }).success(function (response) {
            rs = response;
            return rs;
        });
        return promise;
    };

    factory.addComment = function (userId, nameOfUser, content, videoId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'comments/addComment',
            data: {
                "request_data_type": "video",
                "request_data_method": "add_comment",
                "request_data": {
                    "author": nameOfUser,
                    "authorID": userId,
                    "content": encodeURIComponent(content),
                    "vid": videoId
                }
            }
        });
        return promise;
    };


    factory.getVideoRecommendedForYou = function (studentId) {
        var rs;
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getVideoRecommend?studentId=' + studentId
        }).success(function (response) {
            rs = response;
            return rs;
        });
        return promise;
    };


    factory.getCountFactory = function (userId, typeGet) {
        var rs = null;
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getCountFactory?userId=' + userId + '&typeGet=' + typeGet
        }).success(function (response) {
            rs = response;
            return rs;
        });
        return promise;
    };
    
    factory.getVideosRecently = function (uid) {
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getVideosRecently/'+uid+''
        });
        return promise;
    };
    
    //MTDU
    factory.getHistoryVideosList = function (uid) {
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getHistoryVideosList?uid='+uid
        }).success(function (response) {
            rs = response;
            return rs;
        });
        return promise;
    };
    
    factory.deleteHistoryVideo = function (uid,vid) {
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/clearHistoryVideosList?uid='+uid+'&vid='+vid
        }).success(function (response) {
            rs = response;
            return rs;
        });
        return promise;
    };

    factory.getVideos = function (uid, offset) {
        var promise = $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideos?uid='+ uid +'&offset='+offset+'',
          data: {
            "request_data_type": "video",
            "request_data_method": "getVideos"
           }
        });
        return promise;
    };

    factory.getVideosTopViewed = function (uid, offset) {
        var promise = $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideosTopViewed?uid='+ uid +'&offset='+offset+'',
          data: {
            "request_data_type": "video",
            "request_data_method": "getVideosTopViewed"
            }
        });
        return promise;
    };

    factory.getVideosTopRated = function (uid, offset) {
        var promise = $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideosTopRated?uid='+ uid +'&offset='+offset+'',
          data: {
            "request_data_type": "video",
            "request_data_method": "getVideosTopRated"
            }
        });
        return promise;
    };
    
    /* API Favourite Video begin */
    factory.getFavouriteVideosList = function(uid) {
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'favourite/allFavourite/'+uid
        }).success(function (response) {
            rs = response;
            return rs;
        });
        return promise;
    };
    
    factory.deleteFavouriteVideo = function(uid, vid) {

    	var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'favourite/delFavourite',
            data:{
            	"uid":uid,
            	"vid":vid
            }
            
        }).success(function (response) {
            rs = response;
            return rs;
        });
        return promise;
    };

    factory.deleteMultipleVideo = function(vids, authorid){
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/deleteMultipleVideo',
            data:{
                "request_data_type": "video",
                "request_data_method": "deleteMultipleVideo",
                "request_data":{
                    "vids": vids,
                    "authorID": authorid
                }
            }
            
        });
        return promise;
    }

    factory.deleteVideo = function(vid, authorid){
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/deleteVideo/',
            data: {
              "request_data_type": "video",
              "request_data_method": "deleteVideo",
              "request_data":{
                "vid": vid,
                "authorID": authorid  
              }            
            }
        });
        return promise;
    }

    factory.getVideosBySubject = function(userid, subjectid, offset){
       return $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideosBySubject?userid='+userid + '&subjectid='+subjectid+'&offset='+offset+''
        });
    }

    factory.searchVideosMentor = function(uid, keyword, offset){
        return $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/searchVideos?uid='+uid+'&keyword='+keyword+'&offset='+offset+''
        });
    }

    factory.getPlaylist = function(uid){
        return $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getVideosPlaylist?uid='+uid+''
        });
    }

    /* API Favourite Video end */
    
    return factory;
}]);