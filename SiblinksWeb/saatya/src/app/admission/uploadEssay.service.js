
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


    return factory;
}]);