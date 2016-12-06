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
	  if(newPass == null || newPass.trim() == '') {
		$scope.msgError="Please input New Password."
		angular.element('#newPassword1').trigger('focus');
		return;
	  }
	  if(confirmPass == null || confirmPass.trim() == '') {
		$scope.msgError="Please input Confirm Password."
		angular.element('#newPassword2').trigger('focus');
		return;
	  }
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
    	angular.element('#newPassword2').trigger('focus');
    	$scope.msgError="Passwords do not match."
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