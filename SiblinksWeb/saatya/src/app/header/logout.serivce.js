//=========================================== LOGOUT.SERVICE.JS==============
brotServices.factory('LogoutService', ['$http', function($http) {
  var factory = {};

  factory.logout = function() {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'siblinks/service/logout',
      success : function(data) { 
          console.log("clicked");
      }, 
      error : function(data) {
          console.log(data);
      }
      });
    return promise;
    };
    
    return factory;
  }]);
//=========================================== LOGOUT.SERVICE.JS==============