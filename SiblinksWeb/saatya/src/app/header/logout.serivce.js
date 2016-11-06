//=========================================== LOGOUT.SERVICE.JS==============
brotServices.factory('LogoutService', ['$http', function($http) {
  var factory = {};

  factory.logout = function(uid) {
    var promise = $http({
      method: 'GET',
      url: NEW_SERVICE_URL + '/user/logout?uid='+uid,
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