brotServices.factory('QuestionsService', ['$http','$log', function($http, $log) {
  var factory = {};

  factory.getQuestionSubjectWithPN = function(userId, subjectId, page, limit) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'post/getPostListSubjectPN',
      data: {
        "request_data_type": "post",
        "request_data_method": "getPostListSubjectPN",
        "request_data": {
          "uid": userId,
          "subjectId": subjectId,
          "pageno": page,
          "limit": limit
        }
      }
    });
    return promise;
  };

  factory.getQuestionWithPN = function(userId, subjectId, topicId, page, limit) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'post/getPostListPN',
      async: false,
      data: {
        "request_data_type": "post",
        "request_data_method": "getPostListPN",
        "request_data": {
          "uid": userId,
          "subjectId": subjectId,
          "topicId": topicId,
          "pageno": page,
          "limit": limit
        }
      }
    });
    return promise;
  };

  factory.updateQuestion = function (fd) {
    var url = NEW_SERVICE_URL + 'post/editPost';
    var promise = $http({
      method: 'POST',
      url: url,
      headers: {
        'Content-Type': undefined
      },
      data: fd,
      transformRequest: function (data, headersGetterFunction) {
        return data;
      }

    });
    return promise;
  }
  
  factory.getAllQuestions = function() {
	    var promise = $http({
	      method: 'POST',
	      url: NEW_SERVICE_URL + 'post/getAllQuestions',
	      async: false,
	      data: {
	        "request_data_type": "post",
	        "request_data_method": "getAllQuestions",
	        "request_data": {
	      
	         
	        }
	      }
	    });
	    return promise;
	  };


  factory.getQuestionById = function(question_id) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'post/getPostById',
      data: {
        "request_data_type": "post",
        "request_data_method": "getPostById",
        "request_data": {
            "pid": question_id
        }
      }
    });
    return promise;
  };

  factory.updateViewQuestion = function(question_id, status) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'post/updateViewQuestion',
      data: {
        "request_data_type": "post",
        "request_data_method": "updateViewQuestion",
        "request_data": {
          "pid": question_id,
          "status": status
        }
      }
    });
    return promise;
  };

  factory.getAnswerById = function(question_id) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'post/getAnswerById',
      data: {
        "request_data_type": "post",
        "request_data_method": "getAnswerById",
        "request_data": {
          "pid": question_id
        }
      }
    });
    return promise;
  };

  factory.likeQuestion = function(userId, questionId) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'like/likeQuestion',
      data: {
        "request_data_type": "like",
        "request_data_method": "likeQuestion",
        "request_data": {
          "authorID": userId,
          "pid": questionId
        }
      }
    });
    return promise;
  };

  factory.getQuestionRelated = function(subjectId, page, limit) {
    var qa;
    var promise = $http({
      method: 'GET',
      url: SERVICE_URL + '/qa/getRelatedQuestionsPn?subjectId=' + subjectId + '&qsDesc=multiply&pageno=' + page + '&limit=' + limit + '&totalCountFlag=true'
    }).success(function(json) {
      qa = json.response;
      return qa;
    });
    return promise;
  };

  factory.followQuestion = function(user_id, question_id) {
    var rs;
    var promise = $http({
      method: 'GET',
      url: SERVICE_URL + '/qa/followQuestion?questionid=' + question_id + '&user_id=' + user_id
    }).success(function(json) {
      rs = json;
      return rs;
    });
    return promise;
  };

  factory.searchPostWithTag = function(subjectId, content, page, limit) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'post/searchPostsWithTag',
      data: {
        "request_data_type":"post",
        "request_data_method":"search_posts_with_tag",
        "request_data": {
          "subjectId": subjectId,
          "tag":content,
          "pageno": page,
          "limit": limit
        }
      }
    });
    return promise;
  };

  factory.getPostLikeByUser = function(userId, pid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'like/getPostLikeByUser',
      async: false,
      data: {
        "request_data_type": "like",
        "request_data_method": "getPostLikeByUser",
        "request_data": {
            "uid": userId
        }
      }
    });
    return promise;
  };

  factory.removePost = function(pid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'post/removePost',
      data: {
        "request_data_type": "post",
        "request_data_method": "removePost",
        "request_data": {
          "pid": pid
        }
      }
    });
    return promise;
  };

  factory.removeAnswer = function(aid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'post/removeAnswer',
      data: {
        "request_data_type": "post",
        "request_data_method": "removeAnswer",
        "request_data": {
          "aid": aid
        }
      }
    });
    return promise;
  };
  
  factory.getNewestQuestionSubject = function(userId, limit, offset){
	  var promise = $http({
		  method : 'GET',
		  url : NEW_SERVICE_URL + 'post/getNewestQuestionBySubject?uid='+userId+'&limit='+limit+'&offset='+offset,
	  }).success(function(json){
		  return json;
	  });
	  return promise;
  };
  
  
  factory.getQuestionByUserId = function(userId, limit, offset,ordertype,oldQid,subid){

	    var promise = $http({
	      method: 'GET',
	      url: NEW_SERVICE_URL +'post/getStudentPosted?uid='+userId+'&limit='+limit+'&offset='
          +offset+'&orderType='+ ordertype+'&oldQid='+oldQid+''+'&subjectid='+subid
	    }).success(function(json) {
          return json;

	    });
	    return promise;
  };
  factory.countQuestions = function(userId, ordertype,subid){
    var rs;
    var promise = $http({
      method: 'GET',
      url: NEW_SERVICE_URL +'post/countQuestions?uid='+userId+'&orderType='+ ordertype+'&subjectid='+subid
    }).success(function(json) {
      rs = json;
      return rs;
    });
    return promise;
  };


  factory.getAnswerByQid = function(question_id,type,limit,offset) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'post/getAnswerByQid',
      data: {
        "request_data_type": "post",
        "request_data_method": "getAnswerByQid",
        "request_data": {
          "pid": question_id,
          "type":type,
          "limit": limit,
          "offset": offset

        }
      }
    });
    return promise;
  };
  
  return factory;
}]);