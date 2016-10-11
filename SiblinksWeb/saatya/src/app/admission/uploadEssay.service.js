
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


    return factory;
}]);