brotControllers.controller('ChangePasswordCtrl', 
		['$scope', '$rootScope', '$location', '$timeout', '$routeParams', '$log', 'StudentService',
        function($scope, $rootScope, $location, $timeout, $routeParams, $log, StudentService) {

  var token = $routeParams.token;
  $scope.show = 0;
  $scope.notFound = 0;
  $scope.msgError="";
  $scope.msgSuccess="";
  
  StudentService.confirmToken(token).then(function(data) {
    if(data.data.request_data_result[0].count == 1) {
      $scope.notFound = 1;
    }
  });

  $scope.changePassword = function(newPass, confirmPass) {
	  $scope.msgError="";
	  $scope.msgSuccess="";
	  
    if(newPass == confirmPass && newPass !== undefined && confirmPass !== undefined) {
      StudentService.changePasswordForgot(token, newPass).then(function(data) {
    	 if(data.data='true') {
    		 //$log.info(data.data.request_data_result);
    		 $scope.msgSuccess='Change password is successful.';
    	     $scope.show = 1;
    	 } else {
    		 $scope.msgError=data.data.request_data_result;
    	 }
        
      });
    } else {
    	$scope.msgError="Passwords do not match."
//      $rootScope.myVar = !$scope.myVar;
//      $timeout(function () {
//        $rootScope.myVar = false;
//      }, 2500);
      return;
    }
  };

  $scope.goHomePage = function(type) {
	  if(type == 1) {
		  window.location.href='#/student/signin';
	  } else {
		  window.location.href='#/mentor/signin';
	  }
  };

}]);