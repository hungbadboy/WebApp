brotServices.factory('TeamMentorService', ['$http', function($http) {
  var factory = {};

  factory.getAllAboutMentor = function() {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'mentor/getAllAboutMentor',
      data: {
        "request_data_type": "mentor",
        "request_data_method": "getAllAboutMentor",
        "request_data_aboutMentor": {

        }
      }
    });
    return promise;
  };

  factory.getImageAboutMentor = function(id) {
    return NEW_SERVICE_URL + 'mentor/getImageAboutMentor/' + id;
  };
  factory.getTopMentorsByType=function (limit, offset,type,id) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'mentor/getTopMentorsByLikeRateSubcrible',
      data: {
        "request_data_type": "mentor",
        "request_data_method": "getTopMentorsByLikeRateSubcrible",
        "request_data": {
          "limit":limit,
          "offset":offset,
          "type":type,
          "uid":id
        }
      }
    });
    return promise;
  };

  return factory;
}]);