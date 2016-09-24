//=========================================== FOGETPASSWORD.CONTROLLER.JS==============
brotApp.controller('ForgotPassword', function($scope, $http) {
  $scope.showItem = true;
  $scope.errormsg = "";
  $scope.sucessmsg= "";
  $scope.forgot = function() {
    var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    var email = angular.element('#username').val();
    if(!regex.test(email)) {
      $scope.errormsg = 'Email address not valid';
      return;
    }
    
    $http({
      method: 'POST',
      url: NEW_SERVICE_URL + 'contact/forgotPassword',
      data: {
          "request_data_type": "contact",
          "request_data_method": "forgotPassword",
          "request_data": {
        	  "email": email
          }
      }
    }).success(function(json) {
        if(json.status) {
          $scope.showItem = true;
          $scope.sucessmsg='Forget password is successful. We sent to reset password in your email';
          
        } else {
        	$scope.errormsg = 'Email address is not exist';
        }
    });
  };
});
//=========================================== FOGETPASSWORD.CONTROLLER.JS==============