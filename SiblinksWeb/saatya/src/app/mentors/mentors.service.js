brotServices.factory('MentorService', ['$http', function ($http) {
    var factory = {};
    factory.getTopMentors = function (subjectId) {
        var mentors;
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'mentor/topMetorEachSubject',
            data: {
                "request_data_type": "mentor",
                "request_data_method": "topMetorEachSubject",
                "request_data": {
                    "subjectId": subjectId
                }
            }
        });
        return promise;
    };

    factory.getListMentors = function (name) {
        var mentors;
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + '/mentor/getList',
            data: {
                "request_data": {
                    "order": name,
                    "page": 1,
                    "limit": 30
                }
            }
        }).success(function (data) {
            mentors = data.request_data_result;
            return mentors;
        });
        return promise;
    };

    factory.searchMentors = function (key_search, a, order, page) {
        var mentors;
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'mentor/search',
            data: {
                "request_data": {
                    "fieldSearch": a,// ["firstName","lastName","school"],
                    "keySearch": key_search,
                    "pageno": page,
                    "limit": 9,
                    "order": order
                }
            }
        }).success(function (data) {
            mentors = data.request_data_result;
            return mentors;
        });
        return promise;
    };


    factory.getTopMentorsByLikeRateSubcrible = function (limit, offset, type) {
        var mentors;
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'mentor/getTopMentorsByLikeRateSubcrible',
            data: {
                "request_data": {
                    "limit": limit,
                    "offset": offset,
                    "type": type
                }
            }
        }).success(function (data) {
            mentors = data.request_data_result;
            return mentors;
        });
        return promise;
    };

    factory.getMentorProfile = function (userid) {
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'mentor/getMentorProfile?mentorid=' + userid + ''
        });
        return promise;
    };

    factory.getNewestAnswer = function (userid) {
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'mentor/getNewestAnswer/' + userid + ''
        });
        return promise;
    };

    factory.getStudentSubscribed = function (mentorId, limit, offset) {
        var rs;
        var promise = $http({
            method: 'GET',
            url: NEW_SERVICE_URL + 'mentor/getStudentSubscribed?mentorId=' + mentorId + '&limit=' + limit + '&offset=' + offset
        }).success(function (response) {
            rs = response;
            return rs;
        });
        return promise;
    };

  factory.checkStudentSubscribe = function(mentorid, studentid){
    return $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'mentor/checkStudentSubcribe?mentorid='+mentorid+'&studentid='+studentid+''
    });
  }

  factory.getStudentsSubscribe = function(mentorid, limit, offset){
    return $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'mentor/getStudentsSubcribe?mentorId='+mentorid+'&limit='+limit+'&offset='+offset+''
    });
  }

  factory.getLatestRatings = function(mentorid){
    return $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'mentor/getLatestRatings?mentorid='+mentorid+''
    });
  }

  factory.getLatestComments = function(mentorid, limit, offset){
    return $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'mentor/getLatestComments?mentorid='+mentorid+'&limit='+limit+'&offset='+offset+''
    });
  }

  factory.getDashboardInfo = function(mentorid){
    return $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'mentor/getDashboardInfo?uid='+mentorid+''
    });
  }

  factory.getMainDashboardInfo = function(mentorid){
    return $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'mentor/getMainDashboardInfo?uid='+mentorid+''
    });
  }
    // factory.searchMentors = function(key) {

    //     var mentors;
    //     var promise = $http({
    //         method: 'POST',
    //         url: 'http://192.168.1.100:8181/siblinks/services' + '/mentor/search',
    //         data: {
    //             "request_data_type": "essay",
    //             "request_data_method": "searchMentors",
    //             "request_data": {
    //                 "keySearch": key_search,
    //                 "pageno": 1,
    //                 "limit": 5,
    //                 "totalCountFlag":true
    //             }
    //         }
    //     }).success(function(data) {
    //         mentors = data.request_data_result;
    //         return mentors;
    //     });
    //     return promise;
    // };
    return factory;
}]);