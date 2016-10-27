brotServices.factory('ContactService', ['$http', function($http) {
  var factory = {};

  factory.sendContact = function(contact){
  	var promise = $http({
  		method : 'POST',
  		url : NEW_SERVICE_URL + 'contact/contact',
  		data: {
			"request_data_type": "essay",
			"request_data_method": "contact",
			"contact_data": {
				"name":contact.name,
				"email":contact.email,
				"phone":contact.phone,
				"subject":contact.subject,
				"message":contact.message
			}
  		}
	});
  	return promise;
  }

  return factory;
}]);