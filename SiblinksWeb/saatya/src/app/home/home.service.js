//=========================================== HOME.SERVICE.JS==============
brotServices.factory('HomeService', ['$http', function ($http) {
    var factory = {};

    factory.postQuestion = function (userId, subjectId, content) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'post/createPost',
            data: {
                "request_data_type": "post",
                "request_data_method": "createPost",
                "request_data": {
                    "authorID": userId,
                    "subjectId": subjectId,
                    "content": content
                }
            }
        });
        return promise;
    };

    factory.getSubjectIdWithTopicId = function (topicId) {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'post/getSubjectIdWithTopicId',
            data: {
                "request_data_type": "post",
                "request_data_method": "getSubjectIdWithTopicId",
                "request_data": {
                    "topicId": topicId
                }
            }
        });
        return promise;
    };

    factory.getSubjectsWithTag = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'subjects/listOfSubjectsWithTag',
            data: {
                "request_data_type": "subjects",
                "request_data_method": "listOfSubjectsWithTag",
                "request_data": {}
            }
        });
        return promise;
    };
    factory.getAllCategory = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'subjects/getAllCategory',
            data: {
                "request_data_type": "subjects",
                "request_data_method": "getAllCategory",
                "request_data": {}
            }
        });
        return promise;
    };

    factory.getPolicy = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/getPolicy',
            data: {
                "request_data_type": "user",
                "request_data_method": "getPolicy",
                "request_data": {}
            }
        });
        return promise;
    };

    factory.getTerms = function () {
        var promise = $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'user/getTerms',
            data: {
                "request_data_type": "user",
                "request_data_method": "getTerms",
                "request_data": {}
            }
        });
        return promise;
    };

    factory.addQuestion = function (fd) {
        var url = NEW_SERVICE_URL + 'post/createPost';
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
//=========================================== HOME.SERVICE.JS==============