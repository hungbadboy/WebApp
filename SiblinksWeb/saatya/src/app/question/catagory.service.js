brotServices.factory('CategoriesService', ['$http', function($http) {
  var factory = {};
  factory.getCategories = function(subjectId) {
    var categories;
    var pathWS = NEW_SERVICE_URL + 'subjects/listOfTopics?subject=' + subjectId;
    return $http.post(pathWS, {
                                "request_data_type": "subjects",
                                "request_data_method": "listofTopics",
                                "request_data": {
                                    "subjectId": subjectId

                                }
    }).success(function(data) {
      categories = data.request_data_result;
    });
  };

  factory.getCategoriById = function(categoryId) {
    var promise = $http({
      method: 'GET',
      url: 'json/category_id.json'
    }).success(function(json) {
      return json.response;
    });
    return promise;
  };
  
  return factory;
}]);