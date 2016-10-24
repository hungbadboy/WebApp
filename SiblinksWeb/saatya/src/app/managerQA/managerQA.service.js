brotServices.factory('managerQAService', ['$http', function ($http) {
    var factory = {};


    factory.getListQuestionQA = function (subjectId, userId, offset, type, limit,content,subjects) {
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
                    "offset": offset,
                    "type": type,
                    "content":content,
                    "subjects":subjects
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

    factory.updateAnswer = function (fd) {
        var url = NEW_SERVICE_URL + 'post/editAnswer';
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