brotServices.factory('managerQAService', [ '$http', function($http) {
    var factory = {};


    factory.getListQuestionQA = function(categoryID,topicId) {
        var promise = $http({
            method : 'POST',
            url : NEW_SERVICE_URL + 'managerQA/getListQuestionQA',
            data : {
                "request_data_type" : "post",
                "request_data_method" : "getListQuestionQA",
                "request_data" : {
                    "subjectId":categoryID,
                    "topicId":topicId
                }
            }
        });
        return promise;
    };

    return factory;
} ]);