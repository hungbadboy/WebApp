brotServices.factory('StudentService', ['$http', function($http) {
  var factory = {};

  factory.loginUser = function(userName, passWord, callback) {
    $.ajax({
      url: NEW_SERVICE_URL + 'user/login',
      type: 'POST',
      dataType: 'json',
      data: {
          username: userName,
          password: passWord
      }, success: function (data) {
        callback(data);
      },error: function (xhr, ajaxOptions, thrownError) {
    	  callback(xhr);
        }
    });
  };

  factory.getListActivites = function() {
    return $http({
      method: 'GET',
      url: SERVICE_URL + '/user/extracurricularActivities',
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    });
  };

  factory.getEssayProfile = function(userid, offset, limit) {
    return $http({
      method: 'GET',
      url: NEW_SERVICE_URL + '/essay/getEssayProfile?userid='+userid+'&offset='+offset+'&limit='+limit+''      
    });
  };

  factory.getTutoringInterestes= function(){
    return $http({
      method: 'GET',
      url: SERVICE_URL + '/essay/tutoringInterestes',
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    });
  };

  factory.getListCurrentMentor = function(userId) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'user/getStudentMentors',
      data: {
        "request_data_type": "user",
        "request_data_method": "getMentorsOfVideo",
        "request_data": {
          "uid": userId
        }
      }
    });
    return promise;
  };

  factory.getListForumPost = function(userId, page) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'post/getStudentPostsPN',
      data: {
        "request_data_type": "post",
        "request_data_method": "getStudentPosts",
        "request_data": {
          "uid": userId,
          "pageno": page,
          "limit": 3,
          "totalCountFlag": true
        }
      }
    });
    return promise;
  };

  factory.getMentorsReviewed = function(userId, limit) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'video/getMentorReviewsPN',
      data: {
        "request_data_type": "post",
        "request_data_method": "getMentorReviewsPN",
        "request_data": {
          "uid": userId,
          "pageno": 1,
          "limit": limit,
          "totalCountFlag": true
        }
      }
    });
    return promise;
  };

  factory.getUserProfile = function(userid) {
    var promise = $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'user/getUserProfile?userid='+userid+''
    });
    return promise;
  };

    // factory.updateUserProfile = function(email, type, firstName, lastName, imageUrl, school, currentClass, accomplishments,  subjects, helpSubjects,  bio) {
    //     if(currentClass == null) {
    //         currentClass = '';
    //     }
    //     if(accomplishments == null) {
    //         accomplishments = '';
    //     }
    //     if(school == null) {
    //         school = '';
    //     }
    //     return $http({
    //         method: 'POST',
    //         url: NEW_SERVICE_URL + 'user/updateUserProfile?username=' + email + '&type=' + type + '&firstname=' + firstName + '&lastname=' + lastName + '&imageurl=' + imageUrl + '&currentclass=' + currentClass + '&accomplishments=' + accomplishments + '&subjects=' + subjects + '&helpsubjects=' + helpSubjects + '&bio=' + bio + '&school=' + school,
    //         headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    //     });
    // };

  factory.updateUserProfileBasic = function(email, firstName, lastName, bio) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'user/updateUserProfileBasic',
      data: {
        "request_data_type": "user",
        "request_data_method": "updateUserProfileBasic",
        "request_data": {
          "username": email,
          "firstname": firstName,
          "lastname": lastName,
          "bio": bio
        }
      }
    });
    return promise;
  };

  factory.uploadAvatar = function (fd) {
    var url = NEW_SERVICE_URL + 'user/uploadFile';
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

  factory.updateUserProfile = function(userId, email, currentClass, accomplishments, majorId, activityId, helpSubjectId) {
    if(currentClass == null) {
      currentClass = '';
    }
    if(accomplishments == null) {
      accomplishments = '';
    }
    return $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'user/updateUserProfile',
      data: {
        "request_data_type": "user",
        "request_data_method": "updateUserProfile",
        "request_data": {
          "uid": userId,
          "username": email,
          "currentclass": currentClass,
          "accomplishments": accomplishments,
          "majorid": majorId,
          "activityid": activityId,
          "subjectId": helpSubjectId
        }
      }
    });
  };

  factory.updateStudentProfile = function(student){
    return $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'user/updateStudentProfile',
      data: {
        "request_data_type": "user",
        "request_data_method": "updateStudentProfile",
        "request_user":student
      }
    });
  };

  factory.getListMajors = function() {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'user/listOfMajors',
      data: {
        "request_data_type": "user",
        "request_data_method": "listOfMajors",
        "request_data": {
        }
      }
    });
    return promise;
  };

  factory.getListActivities = function() {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'user/listOfActivity',
      data: {
        "request_data_type": "user",
        "request_data_method": "listOfActivity",
        "request_data": {
        }
      }
    });
    return promise;
  };

  factory.getListCategory = function() {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'user/listCategory',
      data: {
        "request_data_type": "user",
        "request_data_method": "listCategory",
        "request_data": {
        }
      }
    });
    return promise;
  };

  factory.changePasswordForgot = function(token, newPwd) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'user/changePasswordForgot',
      data: {
        "token": token,
        "newPwd": newPwd
      }
    });
    return promise;
  };

  factory.changePassword = function(user) {
	    var promise = $http({
	      method: 'POST',
	      url: NEW_SERVICE_URL + 'user/changePassword',
	      data: {
	        "request_data_type": "user",
	        "request_data_method": "changePassword",
	        "request_user": user
	      }
	    });
	    return promise;
	  };

  factory.confirmToken = function(token) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'user/confirmToken',
      data: {'token': token}
     });
    return promise;
  };

  factory.loginFacebook = function(username, usertype, firstname, lastname, image, facebookid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'user/loginFacebook',
      data: {
        "request_data_type": "user",
        "request_data_method": "loginFacebook",
        "request_data": {
          'username': username,
          'firstname': firstname,
          'lastname': lastname,
          'usertype': usertype,
          'image': image,
          'facebookid': facebookid
        }
      }
    });
    return promise;
  };

  factory.loginGoogle = function(username, usertype, firstname, lastname, image, googleid) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'user/loginGoogle',
      data: {
        "request_data_type": "user",
        "request_data_method": "loginGoogle",
        "request_data": {
          'username': username,
          'firstname': firstname,
          'lastname': lastname,
          'usertype': usertype,
          'image': image,
          'googleid': googleid
        }
      }
    });
    return promise;
  };

  factory.getAvatar = function(userId) {
    return NEW_SERVICE_URL + 'user/getAvatar/' + userId;
  };

  return factory;
}]);