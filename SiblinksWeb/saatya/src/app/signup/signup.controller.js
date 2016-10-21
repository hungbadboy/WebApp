brotControllers.controller('SignUpController', ['$scope','$rootScope', '$location', '$window', '$http', '$log','StudentService', function($scope, $rootScope, $location, $window, $http, $log, StudentService) {
  $scope.title = 'Please complete the information below in order to login';
  $scope.user = {};

  // $('#signUpEmail .field .dob').mask('99/99/9999');

  function IsEmail(email) {
    var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
    return emailReg.test(email);
  }
  
  $scope.SignUp = function(user) {
	$scope.error ="";
    if(user.email == null || user.email === '') {
      $scope.error = 'Email is required';
      $("#email").focus();
      return;
    }
    
    if(!(/^[a-zA-Z0-9-.-_--]{3,}\@[a-zA-Z0-9--]{2,16}\.[a-zA-Z0-9]{2,8}$/.test(user.email))) {
      $scope.error = 'Email is invalid';
      $("#email").focus();
        return;
    }

    if(user.password == null || user.password === '') {
      $scope.error = 'Password is required';
      $("#password").focus();
      return;
    }
    if(user.confirmpassword == null || user.confirmpassword === '') {
        $scope.error = 'Confirm password is required';
        $("#confirmpassword").focus();
        return;
     }
    if(user.confirmpassword != user.password) {
        $scope.error = 'Confirm passowrd confirm is not match password';
        $("#confirmpassword").focus();
        return;
     }
    var url = NEW_SERVICE_URL + '/user/registerUser';
    var token = angular.element("#token").text();
    var dataReg = {'username':user.email, 'password' :user.password};
    $http.post(url, dataReg).success(function(data) {
      if(data.status == 'true') {
        //active student
    	$rootScope.$broadcast('open');
        StudentService.loginUser(user.email, user.password, token, function(data) {
        	$rootScope.$broadcast('close');
            if(data.status == 'true') {
              var dataUser = data;
              setStorage('userName', dataUser['username'], 30);
              setStorage('userId', dataUser['userid'], 30);
              setStorage('userType', dataUser['userType'], 10);
              setStorage('imageUrl', dataUser['imageUrl'], 10);
              setStorage('firstName', dataUser['firstName'], 30);
              setStorage('lastname', dataUser['lastname'], 30);
              var nameHome = '';
              if (dataUser['firstname'] == null || dataUser['lastname'] == null) {
                nameHome = capitaliseFirstLetter(dataUser['username'].substring(0, dataUser['username'].indexOf('@')));
              }
              else {
                nameHome = capitaliseFirstLetter(dataUser['firstname']) + ' ' + capitaliseFirstLetter(dataUser['lastname']);
              }
              setStorage('nameHome', nameHome, 30);
              $window.location.href='#/student/studentProfile';
              $window.location.reload();
            } else {
            	$scope.error = 'Your email is already exists';
            }
          });
      } else {
        $scope.error = data.request_data_result[0];
      }
    });
  };

  $scope.Cancel = function() {
    $scope.user = {};
    $scope.error = {};
  };
  
  // Sign Up Facebook
  $scope.loginFacebook = function() {
	    loginFBService(function(data) {
	      $scope.userName = data.email;
	      $scope.firstName = data.first_name;
	      $scope.lastName = data.last_name;
	      $scope.facebookId = data.id;
	      $scope.image = data.picture.data.url;
	      var token = angular.element("#token").text();
	      $rootScope.$broadcast('open');
	      StudentService.loginFacebook($scope.userName, 'S', $scope.firstName, $scope.lastName, $scope.image, $scope.facebookId, token).then(function(data) {
	    	  $rootScope.$broadcast('close');
	    	  if(data.status == 'true') {
	    		  var dataUser = data.data.request_data_result[0];
	    		  setStorage('userName', dataUser['userName'], 30);
	    		  setStorage('userId', dataUser['userid'], 30);
	    		  setStorage('userType', dataUser['userType'], 10);
	    		  setStorage('imageUrl', dataUser['imageUrl'], 10);
	    		  setStorage('fullName', dataUser['firstName'], 30);
	    		  setStorage('lastname', dataUser['lastname'], 30);
	    		  var nameHome = $scope.firstName + ' ' + $scope.lastName;
	    		  setStorage('nameHome', nameHome, 30);
	              $window.location.href='#/student/studentProfile';
	              $window.location.reload();

	    		  
	    	  } else {
	    		  $scope.loginMess = 'Your email is already exists';
	    	  }
	      });
	    });
	};

		
	  function onLoadCallback() {
	    gapi.client.setApiKey(GOOGLESC); //set your API KEY
	    gapi.client.load('plus', 'v1',function(){});//Load Google + API
	  }

	  function loginCallback(result) {
	    if(result['status']['signed_in']) {
	      gapi.client.load('plus','v1', function(){
	        var request = gapi.client.plus.people.get({
	          'userId': 'me'
	        });
	        request.execute(function(resp) {
	          $scope.userName = resp.emails[0].value;
	          $scope.firstName = resp.name.givenName;
	          $scope.lastName = resp.name.familyName;
	          $scope.googleid = resp.id;
	          $scope.image = resp.image.url;
	          var nameHome = resp.displayName;
	          var token = angular.element("#token").text();
	          $rootScope.$broadcast('open');
	          StudentService.loginGoogle($scope.userName, 'S', $scope.firstName, $scope.lastName, $scope.image, $scope.googleid, token).then(function(data) {
	        	$rootScope.$broadcast('close');
	            var dataUser = data.data.request_data_result[0];
	            setStorage('userName', dataUser['userName'], 30);
	            setStorage('userId', dataUser['userid'], 30);
	            setStorage('userType', dataUser['userType'], 10);
	            setStorage('imageUrl', dataUser['imageUrl'], 10);
	            setStorage('fullName', dataUser['firstName'], 30);
	            setStorage('lastname', dataUser['lastname'], 30);
	            setStorage('nameHome', nameHome, 30);
	            $window.location.href='#/student/studentProfile';
	            $window.location.reload();  
	          });
	        });
	      });
	    }  
	  }
	  
	  // SignUp Google
	  $scope.loginGoogle = function() {
		    var myParams = {
		     // 'clientid' : '667014101821-v1f9a583qgmt280gtci8n4a87k7uja2m.apps.googleusercontent.com', //You need to set client id
		      'clientid' : GOOGLEID,
		      'cookiepolicy' : 'single_host_origin',
		      'callback' : loginCallback, //callback function
		      'approvalprompt':'force',
		      'scope' : 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.profile.emails.read'
		    };
		    gapi.auth.signIn(myParams);
		  };
}]);
