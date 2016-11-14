brotServices.factory('VideoService', ['$http', function ($http) {
    var factory = {};

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

    factory.getUserRatingVideo = function (uid, vid) {
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getUserRatingVideo/'+uid+'/'+vid
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

    factory.getAllTitleVideoPlaylist = function () {
        return $http({
            method : 'GET',
            url : NEW_SERVICE_URL + 'video/getAllTitleVideoPlaylist'
        });
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


    factory.getMentorSubscribe = function (userId, limit, offset) {
        var rs = null;
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getMentorSubscribed?userId=' + userId + '&limit=' + limit + '&offset=' + offset
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
        return $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getVideosRecently/' + uid + ''
        });
    };

    factory.searchVideoPlaylist = function (keyword, limit, offset) {
        return $http({
          method : 'GET',
          url : NEW_SERVICE_URL + 'video/searchVideoPlaylist?keyword='+keyword+'&limit='+limit+'&offset='+offset
        });
    };

    factory.getVideoPlaylistRecently = function (userId, limit, offset) {
        return $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getVideoPlaylistRecently?userId=' + userId + '&limit=' + limit + '&offset=' + offset
        });
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

    factory.checkFavouriteVideo = function(uid, vid) {

        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'favourite/checkFavouriteVideo',
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

    factory.addfavourite = function(uid, vid) {

        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'favourite/addfavourite',
            data: {
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

    factory.getVideosBySubject = function(userid, videoType, subjectid, offset){
       return $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideosBySubject?userid='+userid + '&videoType='+videoType+'&subjectid='+subjectid+'&offset='+offset+''
        });
    }

    factory.searchVideosMentor = function(request){
        return $http({
          method: 'POST',
          url: NEW_SERVICE_URL + 'video/searchVideos',
          data: {
              "request_data_type": "video",
              "request_data_method": "deleteVideo",
              "request_data": request          
            }
        });
    }

    factory.getPlaylist = function(uid){
        return $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'video/getVideosPlaylist?uid='+uid+''
        });
    }

    factory.addVideosToPlaylist = function(request){
        return $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/addVideosToPlaylist',
            data: {
                "request_data_type": "video",
                "request_data_method": "deleteVideo",
                "request_data":request
            }
        });
    }

    factory.searchVideo = function(keyword, limit, offset){
        return $http({
            method : 'GET',
            url: NEW_SERVICE_URL +'video/searchVideo?keyword='+keyword+'&limit='+limit+'&offset='+offset
        });
    };

    factory.getAllVideos = function(){
        return $http({
            method : 'GET',
            url: NEW_SERVICE_URL +'/video/getAllVideos'
        });
    };

    factory.uploadTutorial = function(request){
        return $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/insertVideo',
            data: {
                "request_data_type": "video",
                "request_data_method": "insertVideo",
                "request_data": request
            } 
        });
    }

    factory.updateTutorial = function(request){
        return $http({
        method: 'POST',
        url: NEW_SERVICE_URL + 'video/updateVideo',
        data: {
            "request_data_type": "video",
            "request_data_method": "updateVideo",
            "request_data": request
        }  
      });
    }

    factory.getVideosNonePlaylist = function(plid, uid, offset){
        return $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideosNonePlaylist?plid='+plid+'&uid='+uid+'&offset='+offset+''
        });
    }

    factory.getVideosNonePlaylistBySubject = function(plid, uid, subjectid, offset){
        return $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideosNonePlaylistBySubject?plid='+plid+'&uid='+uid+'&subjectId='+subjectid+'&offset='+offset+''
        });
    }

    factory.searchVideosNonePlaylist = function(plid, uid, keyword, subjectId, offset){
        return $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/searchVideosNonePlaylist',
            data: {
                "request_data_type": "video",
                "request_data_method": "updateVideo",
                "request_data": {
                    "uid":uid,
                    "keySearch":keyword,
                    "subjectId":subjectId,
                    "offset":offset,
                    "plid": plid
                }
            } 
        });
    }

    factory.getVideoInfoFromYoutube = function(vid){
        var url = "https://www.googleapis.com/youtube/v3/videos?id=" + vid + "&key=AIzaSyDYwPzLevXauI-kTSVXTLroLyHEONuF9Rw&part=snippet,contentDetails";
        return $http({
            async: false,
            type: 'GET',
            url: url
        });
    }

    factory.getVideoById = function(vid, uid){
        return $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideoById?vid='+vid+'&userid='+uid+''
        });
    }
    /* API Favourite Video end */
    
    return factory;
}]);