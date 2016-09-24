brotServices.factory('SubCategoryVideoService', ['$http', function($http) {
  var factory = {};
  factory.getSubCategoryVideo = function(subCategoryId) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'video/getVideoInfo',
      data: {
        "request_data_type": "video",
        "request_data_method": "getVideoInfo",
        "request_data": {
          "vid": subCategoryId
        }
      }
    });
    return promise;
  };

  factory.getListSubCategoryVideo = function(subjectId, categoryId, pageno, limit, totalCountFlag) {
    var pathWS = NEW_SERVICE_URL + 'subjects/listOfSubTopicsPn';
    return $http.post(pathWS, {
                                "request_data_type": "subjects",
                                "request_data_method": "listofSubTopicsPn",
                                "request_data": {
                                "subjectId": subjectId,
                                "cid": categoryId,
                                "pageno": pageno,
                                "limit": limit,
                                "totalCountFlag": totalCountFlag
                            }
                    });
  };

  factory.getMentorOnVideo = function(subjectId) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'video/getMentorsOfVideo',
      data: {
        "request_data_type": "video",
        "request_data_method": "get_using_topic",
        "request_data": {
          "subjectId": subjectId
        }
      }
    });
    return promise;
  };

  factory.getDiscussesOnVideo = function(video_id, pageno, limit, totalCountFlag) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'video/getVideoCommentsPN',
      data: {
        "request_data_type": "video",
        "request_data_method": "get_comments",
        "request_data": {
          "vid":video_id,
          "pageno": pageno,
          "limit": limit,
          "totalCountFlag": totalCountFlag
        }
      }
    });
    return promise;
  };

  factory.addComment = function(userName, userId, content, videoId) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'comments/addComment',
      data: {
        "request_data_type":"video",
        "request_data_method":"add_comment",
        "request_data": {
          "author": userName,
          "authorID": userId,
          "content": encodeURIComponent(content),
          "vid": videoId
        }
      }
    });
    return promise;
  };

  factory.likeCommentVideo = function(userId, cid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'like/likeComment',
      data: {
        "request_data_type": "like",
        "request_data_method": "likeComment",
        "request_data": {
          "authorID": userId,
          "cid": cid
        }
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