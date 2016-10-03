brotServices.factory('AnswerService', ['$http', '$log', function($http) {
  var factory = {};

  // factory.postAnswer = function(userId, authorId, question_id, content,subjectId) {
  //   var rs;
  //   var promise = $http({
  //     method: 'POST',
  //     url: NEW_SERVICE_URL + 'post/createAnswer',
  //     data: {
  //       "request_data_type": "post",
  //       "request_data_method": "createAnswer",
  //       "request_data": {
  //         "studentId": authorId,
  //         "mentorId":userId,
  //         "pid": question_id,
  //         "subjectId":subjectId,
  //         "content": content
  //
  //       }
  //     }
  //   });
  //   return promise;
  // };


  factory.getAnswersByQuestion = function(question_id, page, limit) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'post/getAnswerListPN',
      data: {
        "request_data_type": "post",
        "request_data_method": "getAnswerListPN",
        "request_data": {
          "pid": question_id,
          "pageno": page,
          "limit": limit
        }
      }
    });
    return promise;
  };
  
  
  factory.getAnswersById = function(question_id, page, limit){
	  var promise = $http({
	      method: 'GET',
	      url: NEW_SERVICE_URL + 'post/getAnswersList?pid='+question_id+"&page="+page+"&limit="+limit,
	      data: {
	        "request_data_type": "get",
	        "request_data_method": "getAnswersList",
	        "request_data": {
	          "pid": question_id,
	          "pageno": page,
	          "limit": limit
	        }
	      }
	    });
	    return promise;
  }
  

  factory.likeAnswer = function(authorID, answer_id) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'like/likeAnswer',
      data: {
        "request_data_type": "like",
        "request_data_method": "likeAnswer",
        "request_data": {
          "authorID": authorID,
          "aid": answer_id
        }
      }
    });
    return promise;
  };
  
  // factory.likeAnswerByQuestion = function(uid, aid){
  //     var data = new FormData();
  //     data.append("uid", uid);
  //     data.append("aid", aid);
	//   var promise = $http({
	// 	  method : 'POST',
	// 	  url : NEW_SERVICE_URL + 'like/likeAnswerByQuestion',
	// 	  data : {
	// 		  "request_data_type":"post",
	// 		  "request_data_method":"likeAnswerByQuestion",
	// 		  "request_data":{
	// 			  "uid": ,
	// 			  "aid": "1"
	// 		  }
	// 	  }
	//   });
	//   return promise;
  // };
  

  factory.editAnswer = function(aid, content) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'answer/editAnswer',
      data: {
        "request_data_type": "answer",
        "request_data_method": "editAnswer",
        "request_data": {
          "aid": aid,
          "content": encodeURIComponent(content)
        }
      }
    });
    return promise;
  };

  return factory;
}]);