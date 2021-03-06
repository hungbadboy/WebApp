brotServices.factory('StudentService', ['$http', function ($http) {
    var factory = {};

    factory.loginUser = function (userName, passWord, token, userType, callback) {
        $.ajax({
            url: NEW_SERVICE_URL + 'user/login',
            type: 'POST',
            dataType: 'json',
            data: {
                username: userName,
                password: passWord,
                userType: userType == null ? 'S' : userType,
                token:token
            }, success: function (data) {
                callback(data);
            }, error: function (xhr, ajaxOptions, thrownError) {
                callback(xhr);
            }
        });
    };

    factory.getListActivites = function () {
        return $http({
            method: 'GET',
            url: SERVICE_URL + '/user/extracurricularActivities',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
    };

    factory.getEssayProfile = function (userid, offset, limit) {
        return $http({
            method: 'GET',
            url: NEW_SERVICE_URL + '/essay/getEssayProfile?userid=' + userid + '&offset=' + offset + '&limit=' + limit + ''
        });
    };

    factory.getTutoringInterestes = function () {
        return $http({
            method: 'GET',
            url: SERVICE_URL + '/essay/tutoringInterestes',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
    };

    factory.getListCurrentMentor = function (userId) {
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

    factory.getListForumPost = function (userId, page) {
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

    factory.getMentorsReviewed = function (userId, limit) {
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

    factory.getUserProfile = function (userid) {
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'user/getUserProfile?userid=' + userid + ''
        });
        return promise;
    };

    factory.uploadAvatar = function (fd) {
        var url = NEW_SERVICE_URL + 'user/uploadAvatar';
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

    factory.updateUserProfile = function (student) {
        return $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/updateUserProfile',
            data: {
                "request_data_type": "user",
                "request_data_method": "updateUserProfile",
                "request_user": student
            }
        });
    };

    factory.getNewestAnswersById = function (authorId, limit, offset) {
        var rs;
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + '/mentor/getNewestAnswers?authorId=' + authorId + '&limit=' + limit + '&offset=' + offset
        }).success(function (response) {
            rs = response;
            return rs;
        });
        return promise;
    };

    factory.getListMajors = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/listOfMajors',
            data: {
                "request_data_type": "user",
                "request_data_method": "listOfMajors",
                "request_data": {}
            }
        });
        return promise;
    };

    factory.checkSubscribe = function (studentId, mentorId) {
        return $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'student/checkStudentSubscribe?studentId=' + studentId + '&mentorId=' + mentorId
        });
    };

    factory.getListActivities = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/listOfActivity',
            data: {
                "request_data_type": "user",
                "request_data_method": "listOfActivity",
                "request_data": {}
            }
        });
        return promise;
    };

    factory.getListCategory = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/listCategory',
            data: {
                "request_data_type": "user",
                "request_data_method": "listCategory",
                "request_data": {}
            }
        });
        return promise;
    };

    factory.changePasswordForgot = function (code, newPwd) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/changePasswordForgot',
            data: {
                "token": code,
                "newPwd": newPwd
            }
        });
        return promise;
    };

    factory.changePassword = function (user) {
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

    factory.confirmToken = function (code) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/confirmToken',
            data: {'token': code}
        });
        return promise;
    };

    factory.loginFacebook = function (username, usertype, firstname, lastname, image, facebookid, token) {
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
                    'facebookid': facebookid,
                    'token':token
                }
            }
        });
        return promise;
    };

    factory.loginGoogle = function (username, usertype, firstname, lastname, image, googleid, token) {
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
                    'googleid': googleid,
                    'token': token
                }
            }
        });
        return promise;
    };

    factory.getAvatar = function (userId) {
        return NEW_SERVICE_URL + 'user/getAvatar/' + userId;
    };
    
    
    factory.getInfoMentorSubscribed = function (studentId, limit, offset, isCount, keyWord) {
        return $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'student/getAllInfoMentorSubscribed?studentId='+studentId+'&limit='+limit+'&offset='+offset+'&isCount='+isCount+'&keyWord='+keyWord
        });
    };
    

    return factory;
}]);