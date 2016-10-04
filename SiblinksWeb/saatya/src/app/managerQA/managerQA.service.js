brotServices.factory('managerQAService', ['$http', function ($http) {
    var factory = {};


    factory.getListQuestionQA = function (subjectId, userId, lastQId, type, limit) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'managerQA/getListQuestionQA',
            data: {
                "request_data_type": "post",
                "request_data_method": "getListQuestionQA",
                "request_data": {
                    "subjectId": subjectId,
                    "uid": userId,
                    "limit": limit,
                    "pid": lastQId,
                    "type": type
                }
            }
        });
        return promise;
    };
    factory.postAnswer = function (fd) {
        var url = NEW_SERVICE_URL + 'post/createAnswer';
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