brotServices.factory('FaqsService', ['$http', function($http) {
  var factory = {};

  factory.getFaqsAbout = function(limit, offest) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'faqs/fetchFaqs',
      data: {
        "request_data_type": "faqs",
        "request_data_method": "fetchFaqs",
        "request_data": {
          "faqCategory": "About",
          "limit": limit,
          "page": offest
        }
      }
    });
    return promise;
  };

  factory.getFaqsHelp = function(limit, offest) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'faqs/fetchFaqs',
      data: {
        "request_data_type": "faqs",
        "request_data_method": "fetchFaqs",
        "request_data": {
          "faqCategory": "Help",
          "limit": limit,
          "page": offest
        }
      }
    });
    return promise;
  };

  factory.getFaqsMentor = function(limit, offest) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'faqs/fetchFaqs',
      data: {
        "request_data_type": "faqs",
        "request_data_method": "fetchFaqs",
        "request_data": {
          "faqCategory": "becomementor",
          "limit": limit,
          "page": offest
        }
      }
    });
    return promise;
  };

  factory.getTopQuestions = function(order, limit) {
    var faqs;
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'faqs/fetchFaqs/top',
      data: {
        "request_data_type": "faqs",
        "request_data_method": "TopFetchFaqs",
        "request_data": {
          "order": order,
          "limit": limit
        }
      }
      // method: 'GET',
      // url: '../json/top_questions_faqs.json',
      // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function(data) {
      faqs = data.response;
      return faqs;
    });
    return promise;
  };
  return factory;
}]);