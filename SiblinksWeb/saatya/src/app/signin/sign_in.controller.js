brotControllers.controller('SignInCtrl', function ($scope, $location, $rootScope, $http, $timeout, $routeParams,$window, StudentService) {
    $scope.loginMess = "";
    $scope.bestDeals = [
                       {src: 'http://placehold.it/110x110&text=Best%20Deal%201', title: 'Best Deal 1' },
                       {src: 'http://placehold.it/110x110&text=Best%20Deal%202', title: 'Best Deal 2' },
                       {src: 'http://placehold.it/110x110&text=Best%20Deal%203', title: 'Best Deal 3' },
                       {src: 'http://placehold.it/110x110&text=Best%20Deal%204', title: 'Best Deal 4' },
                       {src: 'http://placehold.it/110x110&text=Best%20Deal%205', title: 'Best Deal 5' },
                       {src: 'http://placehold.it/110x110&text=Best%20Deal%206', title: 'Best Deal 6 Best Deal 6 Best Deal 6' },
                       {src: 'http://placehold.it/110x110&text=Best%20Deal%207', title: 'Best Deal 7' },
                       {src: 'http://placehold.it/110x110&text=Best%20Deal%208', title: 'Best Deal 8' },
                       {src: 'http://placehold.it/110x110&text=Best%20Deal%209', title: 'Best Deal 9' },
                       {src: 'http://placehold.it/110x110&text=Best%20Deal%2010', title: 'Best Deal 10' }
                     ];
    
    $scope.login = function() {
    	login(null);
    };
    
    $scope.loginMentor = function() {
    	login('M');
    };

    init();
    $scope.nextFocus = function () {
    	angular.element('#passWord').trigger('focus');
    }
    
    function init() {
        $scope.username = 'Username*';
        $scope.password = 'Password*';
    }

    $scope.showForgotPassword = function () {
//  $('.wrap_info .loadingdivForgot').find('#loading').addClass('hide');
//    $('.wrap_info .wrap_event').find('.forgot').removeAttr('disabled');
//    $('#popSignIn').modal('hide');
//    var userName = $('#popSignIn').find('.userName').val();
//    $('#forgotPassword').modal('show');
//    $('#forgotPassword .userName').val(userName);
//
//    $('#forgotPassword .wrap_info').removeClass('hide');
//    $('#forgotPassword .wrap_alert').addClass('hide');
        $location.path('/forgotPwd');
    };

    $scope.loginFacebook = function () {
        loginFBService(function (data) {
            $scope.userName = data.email;
            $scope.firstName = data.first_name;
            $scope.lastName = data.last_name;
            $scope.facebookId = data.id;
            $scope.image = data.picture.data.url;
            $rootScope.$broadcast('open');
            var token = angular.element("#token").text();
            StudentService.loginFacebook($scope.userName, 'S', $scope.firstName, $scope.lastName, $scope.image, $scope.facebookId, token).then(function (data) {
            	$rootScope.$broadcast('close');
            	if(data.data.status == 'true') {
            		var dataUser = data.data.request_data_result[0];
	                setStorage('userName', dataUser['userName'], 30);
	                setStorage('userId', dataUser['userid'], 30);
	                setStorage('userType', dataUser['userType'], 10);
	                setStorage('imageUrl', dataUser['imageUrl'], 10);
	                setStorage('firstName', dataUser['firstName'], 30);
                    setStorage('lastname', dataUser['lastName'], 30);
	                setStorage('defaultSubjectId', dataUser['defaultSubjectId'], 10);
	                var nameHome = $scope.firstName + ' ' + $scope.lastName;
	                setStorage('nameHome', nameHome, 30);
	                $('#header .log_out .current').text(nameHome);
	                // Redirect to old link
	                if(redirectToOldLink()) {
	                	$window.location.reload();
                    	return;
                    }
                    
	                // Redirect to home page
	                if (dataUser['userType'] == 'S') { // login student
	                	window.location.href = '/';
	                } else if(dataUser['userType'] == 'M') { // login mentor
	                	window.location.href = '/';
	                }
            	} else {
            		$scope.loginMess = data.data.request_data_result;
            		angular.element('#userName').trigger('focus');
            		return;
            	}
            });
        });
    };

    function onLoadCallback() {
        gapi.client.setApiKey(GOOGLESC); //set your API KEY
        gapi.client.load('plus', 'v1', function () {
        });//Load Google + API
    }

    function loginCallback(result) {
        if (result['status']['signed_in']) {
            gapi.client.load('plus', 'v1', function () {
                var request = gapi.client.plus.people.get({
                    'userId': 'me'
                });
                request.execute(function (resp) {
                    $scope.userName = resp.emails[0].value;
                    $scope.firstName = resp.name.givenName;
                    $scope.lastName = resp.name.familyName;
                    $scope.googleid = resp.id;
                    $scope.image = resp.image.url;
                    var nameHome = resp.displayName;
                    $rootScope.$broadcast('open');
                    var token = angular.element("#token").text();
                    StudentService.loginGoogle($scope.userName, 'S', $scope.firstName, $scope.lastName, $scope.image, $scope.googleid, token).then(function (data) {
                    	$rootScope.$broadcast('close');
                    	if(data.data.status=='true') {
	                        var dataUser = data.data.request_data_result[0];
	                        setStorage('userName', dataUser['userName'], 30);
	                        setStorage('userId', dataUser['userid'], 30);
	                        setStorage('userType', dataUser['userType'], 10);
	                        setStorage('imageUrl', dataUser['imageUrl'], 10);
	                        setStorage('nameHome', nameHome, 30);
	                        setStorage('firstName', dataUser['firstName'], 30);
                            setStorage('lastname', dataUser['lastName'], 30);
	                        setStorage('defaultSubjectId', dataUser['defaultSubjectId'], 10);
	                        // Redirect to old link
	                        if(redirectToOldLink()) {
	                        	$window.location.reload();
	                        	return;
	                        }
	                        // Redirect to home page
	                        if (dataUser['userType'] == 'S') { // login student
	                        	window.location.href = '/';
	                        } else if(dataUser['userType'] == 'M') { // login mentor
	                        	window.location.href = '/';
	                        }
                    	} else {
                    		$scope.loginMess = data.data.request_data_result;
                    		angular.element('#userName').trigger('focus');
                    		return;
                    	}
                    });
                });
            });
        }
    }

    $scope.loginGoogle = function () {
        var myParams = {
            //'clientid' : '667014101821-v1f9a583qgmt280gtci8n4a87k7uja2m.apps.googleusercontent.com', //You need to set client id
            'clientid': GOOGLEID,
            'cookiepolicy': 'single_host_origin',
            'callback': loginCallback, //callback function
            'approvalprompt': 'force',
            'scope': 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.profile.emails.read'
        };
        gapi.auth.signIn(myParams);
    };
    
   /**
    * Redirect to page previous by continue on parameter
    */
    function redirectToOldLink() {
    	var linkRedirect = $routeParams.continue;
    	if(linkRedirect != null && linkRedirect !== undefined && linkRedirect !== '') {
    		window.location.href = decodeURIComponent(linkRedirect);
    		return true;
    	} else {
    		return false;
    	}
    }
    /**
     * Valid login
     * 
     */
    function validLogin(){
    	var userName = angular.element('#userName').val();
    	 if (userName == null || userName === '') {
             $scope.loginMess = 'Your email is required';
             angular.element('#userName').trigger('focus');
             return false;
         }

         if (!(/^[a-zA-Z0-9-.-_--]{3,}\@[a-zA-Z0-9--]{2,16}\.[a-zA-Z0-9]{2,8}$/.test(userName))) {
             $scope.loginMess = 'Your email is invalid';
             angular.element('#userName').trigger('focus');
             return false;
         }

         var password = angular.element('#passWord').val();
         if (password == null || password === '') {
             $scope.loginMess = 'Password is required';
             angular.element('#passWord').trigger('focus');
             return false;
         }         
    }
    
    function login(userType) {
    	var userName = angular.element('#userName').val();
    	var password = angular.element('#passWord').val();
    	if(validLogin() == false ) {
    		return;
    	};
    	
        var token = angular.element("#token").text();
        
        $rootScope.$broadcast('open');
        StudentService.loginUser(userName,  password, token, userType, function (data) {
        	$rootScope.$broadcast('close');
            if (data.status == 'true') {
                var dataUser = data;
                console.log(dataUser);
                var firstName = dataUser['firstname'];
                var lastName = dataUser['lastname'];
                setStorage('userName', dataUser['username'], 30);
                setStorage('userId', dataUser['userid'], 30);
                setStorage('userType', dataUser['userType'], 10);
                setStorage('imageUrl', dataUser['imageUrl'], 10);
                setStorage('school', dataUser['school'], 30);
                setStorage('firstName', (firstName != null && firstName !== undefined)?firstName:'', 30);
                setStorage('lastname', (lastName != null && lastName !== undefined)?lastName:'', 30);
                setStorage('defaultSubjectId', dataUser['defaultSubjectId'], 10);
                var nameHome = '';
                if (firstName == null || firstName === undefined || lastName == null || lastName === undefined) {
                    nameHome = capitaliseFirstLetter(dataUser['username'].substring(0, dataUser['username'].indexOf('@')));
                } else {
                    nameHome = capitaliseFirstLetter(firstName) + ' ' + capitaliseFirstLetter(lastName);
                }
                setStorage('nameHome', nameHome, 30);
                // Redirect to old link
                if(redirectToOldLink()) {
                	$window.location.reload();
                	return;
                }
                
                // Redirect to home page
                if (dataUser['userType'] == 'S') { // login student
                	window.location.href = '/';
                } else if(dataUser['userType'] == 'M') { // login mentor
                	window.location.href = '#/mentor/dashboard';
                	window.location.reload();
                }
            } else {
            	$rootScope.$broadcast('close');
                $scope.loginMess = "Incorrect email or password";
                $scope.$apply();
                angular.element('#userName').trigger('focus');
                return;
            }
        });
    }
});