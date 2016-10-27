brotServices.factory('videoDetailService', ['$http', function ($http) {
    var factory = {};


    factory.getVideoDetailById = function (vid) {
        var promise = $http({
            method: 'get',
            url: NEW_SERVICE_URL + 'videodetail/getVideoDetailById/' + vid
        });
        return promise;
    };

    factory.getVideoDetailMentor = function (vid, uid) {
        var promise = $http({
            method: 'get',
            url: NEW_SERVICE_URL + 'videodetail/getVideoDetailMentor?vid='+vid+'&uid='+uid+''
        });
        return promise;
    };

    factory.getVideoRelatedMentor = function (vid,subid, uid, offset) {
        var promise = $http({
            method: 'get',
            url: NEW_SERVICE_URL + 'videodetail/getVideoRelatedMentor?vid='+vid+'&subjectId='+subid+'&uid='+uid+'&offset='+offset+''
        });
        return promise;
    };
    factory.getVideoByPlaylistId = function (pid) {
        var promise = $http({
            method: 'get',
            url: NEW_SERVICE_URL + 'videodetail/getVideoByPlaylistId/' + pid
        });
        return promise;
    };
    factory.getCommentVideoById = function (vid) {
        var promise = $http({
            method: 'get',
            url: NEW_SERVICE_URL + 'videodetail/getCommentVideoById/' + vid
        });
        return promise;
    };

    factory.checkSubscribe = function (mentorid,studentid) {
        var promise = $http({
            method: 'get',
            url: NEW_SERVICE_URL + 'videodetail/checkSubscribe?mentorid=' + mentorid+''+'&studentid='+studentid
        });
        return promise;
    };


    factory.addCommentVideo = function(objRequest) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'comments/addComment',
            data: {
                "request_data_type":"video",
                "request_data_method":"add_comment",
                "request_data": objRequest
            }
        });
        return promise;
    };


    factory.updateViewVideo = function(vid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/updateViewVideo',
            data: {
                "request_data_type": "video",
                "request_data_method": "updateViewVideo",
                "request_data": {
                    "vid": vid
                }
            }
        });
        return promise;
    };

    factory.updateVideoHistory = function( vid,userId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'videodetail/updateVideoHistory',
            data: {
                "request_data_type": "video",
                "request_data_method": "updateVideoHistory",
                "request_data": {
                    "vid": vid,
                    "uid":userId
                }
            }
        });
        return promise;
    };

    factory.getVideoByCategoryId = function( sid,limit,offset) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'videodetail/getVideoByCategoryId',
            data: {
                "request_data_type": "video",
                "request_data_method": "getVideoByCategoryId",
                "request_data": {
                    "subjectId": sid,
                    "limit":limit,
                    "offset":offset
                }
            }
        });
        return promise;
    };
    factory.updateWatchedVideo = function(uid, vid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/updateVideoWatched',
            data: {
                "request_data_type": "video",
                "request_data_method": "updateVideoWatched",
                "request_data": {
                    "uid": uid,
                    "vid": vid
                }
            }
        });
        return promise;
    };


    return factory;
}]);