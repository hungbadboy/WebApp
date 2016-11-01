
brotServices.factory('uploadEssayService', ['$http', function($http) {
    var factory = {};


    factory.listOfMajors = function() {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/listOfMajors',
            data: {
                "request_data_type": "POST",
                "request_data_method": "listOfMajors",
                "request_data": {
                }
            }
        });
        return promise;
    };
    factory.collegesOrUniversities = function() {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/collegesOrUniversities',
            data: {
                "request_data_type": "POST",
                "request_data_method": "collegesOrUniversities",
                "request_data": {
                }
            }
        });
        return promise;
    };

    factory.uploadEssayStudent = function (fd) {
        var url = NEW_SERVICE_URL + 'essay/uploadEssayStudent';
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

    factory.updateEssayStudent = function (fd) {
        var url = NEW_SERVICE_URL + 'essay/updateEssayStudent';
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


    factory.getMentorEssayByUid = function(uid) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'essay/getMentorEssayByUid',
            data: {
                "request_data_type": "essay",
                "request_data_method": "getMentorEssayByUid",
                "request_data": {
                    "uid": uid
                }
            }
        });
        return promise;
    };


    factory.getEssaybByStudentId = function (userId, limit,offset) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'essay/getEssaybByStudentId',
            data: {
                "request_data_type": "essay",
                "request_data_method": "getEssayByUserId",
                "request_data": {
                    "uid": userId,
                    "limit": limit,
                    "offset": offset,
                    "totalCountFlag": true
                }
            }
        });
        return promise;
    };
    factory.getEssayById = function(essayId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'essay/getEssayById',
            data: {
                "request_data_type": "essay",
                "request_data_method": "getEssayById",
                "request_data": {
                    "essayId": essayId,
                }
            }
        });
        return promise;
    };

    factory.removeEssay = function(essayId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'essay/removeEssay',
            data: {
                "request_data_type": "POST",
                "request_data_method": "removeEssay",
                "request_data": {
                    "essayId": essayId
                }
            }
        });
        return promise;
    };


    return factory;
}]);