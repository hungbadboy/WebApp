//=========================================== LOGOUT.SERVICE.JS==============
brotServices.factory('LogoutService', ['$http', function($http) {
  var factory = {};

  factory.logout = function(username) {
    var promise = $http({
      method: 'POST',
      url: NEW_SERVICE_URL + '/user/logout',
      data:{'username':username},
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