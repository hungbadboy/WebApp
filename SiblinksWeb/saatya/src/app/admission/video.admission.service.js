brotServices.factory('videoAdmissionService', ['$http', function ($http) {
    var factory = {};


    factory.getVideoDetailById = function (vid) {
        var promise = $http({
            method: 'get',
            url: NEW_SERVICE_URL + 'videodetail/getVideoAdmissionDetailById/' + vid
        });
        return promise;
    };


    factory.getVideoRelatedMentor = function (subid, uid, offset) {
        var promise = $http({
            method: 'get',
            url: NEW_SERVICE_URL + 'videodetail/getVideoRelatedMentor?subjectId='+subid+'&uid='+uid+'&offset='+offset+''
        });
        return promise;
    };

    factory.getCommentVideoById = function (vid) {
        var promise = $http({
            method: 'get',
            url: NEW_SERVICE_URL + 'videodetail/getCommentVideoAdmissionById/' + vid
        });
        return promise;
    };


    factory.addCommentVideo = function(userId, content, videoId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'comments/addCommentVideoAdmission',
            data: {
                "request_data_type":"video",
                "request_data_method":"add_comment",
                "request_data": {
                    "authorID": userId,
                    "content": content,
                    "vid": videoId
                }
            }
        });
        return promise;
    };

    factory.checkUserRatingVideo = function (uid, vid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/checkUserRatingVideoAdmission',
            data: {
                "request_data_type": "video",
                "request_data_method": "checkUserRatingVideoAdmission",
                "request_data": {
                    "uid": uid,
                    "vid": vid
                }
            }
        });
        return promise;
    };
    factory.rateVideo = function (uid, vid, rate) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/rateVideoAdmission',
            data: {
                "request_data_type": "video",
                "request_data_method": "rateVideoAdmission",
                "request_data": {
                    "uid": uid,
                    "vid": vid,
                    "rating": rate
                }
            }
        });
        return promise;
    };

    factory.updateViewVideo = function(vid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'videodetail/updateViewVideoAdmission',
            data: {
                "request_data_type": "video",
                "request_data_method": "updateViewVideoAdmission",
                "request_data": {
                    "vid": vid
                }
            }
        });
        return promise;
    };

    factory.getVideoByAdmissionId = function( sid,limit,offset) {
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'videodetail/getVideoByAdmissionId?aId='+sid
        });
        return promise;
    };



    return factory;
}]);