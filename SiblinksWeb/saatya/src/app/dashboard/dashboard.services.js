brotServices.factory('DashboardService', ['$http', '$log', function($http) {
  var factory = {};

  factory.getNewestAnswers = function(id){
  	var promise = $http({
  		method : 'GET',
  		url : NEW_SERVICE_URL + 'mentor/getNewestAnswer/{'+id+'}',
  		data:{
  			  "request_data_type": "get",
	        "request_data_method": "getNewestAnswers",
	        "request_data": {
          "id": id
	     	}
	    }
  	});
  	return promise;
  }

  return factory;
}]);


