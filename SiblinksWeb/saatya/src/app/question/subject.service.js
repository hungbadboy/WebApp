brotServices.factory('SubjectServices', ['$http', function($http) {
  var factory = {};
  var subjects = [];
  factory.getSubjects = function(){
    return $http.post(NEW_SERVICE_URL + 'subjects/listOfSubjects', {
                        "request_data_type": "subjects",
                        "request_data_method": "listofSubjects",
                        "request_data": {
                      }
    }).success(function(data) {
      for(var i = 0; i < data.request_data_result.length; i++) {
        subjects.push(data.request_data_result[i]);
      }
    });
  };

  factory.getSubjectById = function(id) {
    var subject;

    var promise = factory.getSubjects().then(function(json) {
      var obj = json.data.request_data_result;
      for (var i = 0; i < obj.length; i++) {
        if(obj[i].subject_id == id){
          subject = obj[i];
          return subject;
        }
      }
    });
    return promise;
  };
  return factory;
}]);