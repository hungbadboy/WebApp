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
                    "qid": lastQId,
                    "type": type
                }
            }
        });
        return promise;
    };

    return factory;
}]);