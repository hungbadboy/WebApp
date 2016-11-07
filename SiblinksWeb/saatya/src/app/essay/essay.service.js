brotServices.factory('EssayService', ['$http', function($http) {
  var factory = {};
  factory.getListUpdateEssay = function(userId, page) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'essay/getEssayByUserId',
      data: {
        "request_data_type": "essay",
        "request_data_method": "getEssayByUserId",
        "request_data": {
          "uid": userId,
          "pageno": page,
          "limit": 5,
          "totalCountFlag":true
        }
      }
    });
    return promise;
  };

  factory.getAllEssay = function(page, userType, userId) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'essay/getEssay',
      data: {
        "request_data_type": "essay",
        "request_data_method": "getEssay",
        "request_data": {
          "uid": userId,
          "pageno": page,
          "limit": 5,
          "usertype": userType,
          "totalCountFlag":true
        }
      }
    });
    return promise;
  };  

  factory.getEssayById = function(essayId, uid) {
    var promise = $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'essay/getEssayById?essayId='+essayId+'&uid='+uid
    });
    return promise;
  };

  factory.removeEssay = function(essayId) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'essay/removeEssay',
      data: {
        "request_data_type": "essay",
        "request_data_method": "removeEssay",
        "request_data": {
          "essayId": essayId
        }
      }
    });
    return promise;
  };

  factory.postEssayDiscusion = function(userId, content) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'essay/postDiscussion',
      data: {
        "request_data_type": "essay",
        "request_data_method": "postDiscussion",
        "request_data": {
            "uid": userId,
            "message": content
        }
      }
    });
    return promise;
  };

  factory.getEssayDiscussion = function(userId, page, limit) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'essay/getDiscussion',
      data: {
        "request_data_type": "essay",
        "request_data_method": "getDiscussion",
        "request_data": {
          "uid": userId,
          "pageno": page,
          "limit": limit,
          "totalCountFlag":true
        }
      }
    });
    return promise;
  };

  factory.getDiscussesOnEssay = function(essayId, pageno, limit) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'essay/getEssayCommentsPN',
      data: {
        "request_data_type": "essay",
        "request_data_method": "getEssayCommentsPN",
        "request_data": {
          "essayId": essayId,
          "pageno": pageno,
          "limit": limit
        }
      }
    });
    return promise;
  };

  factory.likeCommentEssay = function(userId, cid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'like/likeCommentEssay',
      data: {
        "request_data_type": "like",
        "request_data_method": "likeCommentEssay",
        "request_data": {
          "authorID": userId,
          "cid": cid
        }
      }
    });
    return promise;
  };

  factory.getImageReviewEssay = function(eid) {
    return NEW_SERVICE_URL + 'essay/getImageUploadEssay/' + eid;
  };

  factory.getNewestEssay = function(uid, schoolId, limit, offset){
    return $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'essay/getNewestEssay?userid='+uid+'&schoolId='+schoolId+'&limit='+limit+'&offset='+offset+''
    });
  };
  factory.getProcessingEssay = function(uid, schoolId, limit, offset){
    return $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'essay/getProcessingEssay?userid='+uid+'&schoolId='+schoolId+'&limit='+limit+'&offset='+offset+''
    });
  };
  factory.getIgnoredEssay = function(uid, schoolId, limit, offset){
    return $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'essay/getIgnoredEssay?userid='+uid+'&schoolId='+schoolId+'&limit='+limit+'&offset='+offset+''
    });
  };
  factory.getRepliedEssay = function(uid, schoolId, limit, offset){
    return $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'essay/getRepliedEssay?userid='+uid+'&schoolId='+schoolId+'&limit='+limit+'&offset='+offset+''
    });
  };
  factory.updateStatusEssay = function (eid, mentorid, status) {
    return $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'essay/updateStatusEssay',
      data:{
        'request_data': {
          'mentorId': mentorid,
          'essayId': eid,
          'status':status
        }        
      }
    });
  }
  factory.insertUpdateCommentEssay = function(fd){
    return $http({
      method: 'POST',
      url: NEW_SERVICE_URL +'essay/insertUpdateCommentEssay',
      headers:{
        'Content-Type': undefined
      },
      data: fd,
      transformRequest: function (data, headersGetterFunction) {
        return data;
      }
    });
  }

  factory.getCommentEssay = function(essayId, mentorId){
    return $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'essay/getCommentEssay?essayId='+essayId+'&mentorId='+mentorId+''
    });
  }

  factory.getSuggestionEssay = function(schoolId){
    return $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'essay/getSuggestionEssay?schoolId='+schoolId+''
    });
  }

  factory.search = function(request){
    return $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'essay/searchEssay',
      data:{
        'request_data': request        
      }
    });
  }
  return factory;
}]);