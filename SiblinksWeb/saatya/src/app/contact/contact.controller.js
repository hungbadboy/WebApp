brotControllers.controller('ContactController', ['$scope', '$http', function($scope, $http){
	
	var name = angular.element(document.querySelector('#txtName'));
	var email = angular.element(document.querySelector('#txtEmail'));
	var subject = angular.element(document.querySelector('#txtSubject'));
	var message = angular.element(document.querySelector('#txtMessage'));
	var userId = localStorage.getItem('userId');

	//Author: Nhut Nguyen;
	//set input is null when submit was successfully;
	$scope.setNullinput = function(){
		name.val("");
		email.val("");
		subject.val("");
		message.val("");
	};
	//Author: Nhut Nguyen;
	//validation all values;
	$scope.validate = function(name, email, subject, message){
		if(name.val().length === 0){
			$('#error-name').show();
			$('#error-email').hide();
			$('#error-subject').hide();
			$('#error-message').hide();
			name.focus();
			return false;

		}else  if(email.val().length === 0){
			$('#error-email').show();
			$('#error-name').hide();
			$('#error-subject').hide();
			$('#error-message').hide();
			email.focus();
			return false;
		}else if(subject.val().length === 0){
			$('#error-subject').show();
			$('#error-email').hide();
			$('#error-name').hide();
			$('#error-message').hide();
			subject.focus();
			return false;
		}else if(message.val().length === 0){
			$('#error-message').show();
			$('#error-email').hide();
			$('#error-subject').hide();
			$('#error-name').hide();
			message.focus();
			return false;
		}else if($scope.validateEmail(email.val()) === false){
			$('#error-email').show();
			$('#error-name').hide();
			$('#error-subject').hide();
			$('#error-message').hide();
			email.focus();
			return false;
		}
		$('#error-email').hide();
		$('#error-name').hide();
		$('#error-subject').hide();
		$('#error-message').hide();
		return true;
	};

	//Author: Nhut Nguyen;
	//Validation email address;
	$scope.validateEmail =  function(email) { 
	var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	return re.test(email);
	};

	//Author: Nhut Nguyen;
	//event click on "Submit" button;
	$scope.btnSubmit = function(){
		$('#alert').hide();
		$('#warning').hide();
		var vl = $scope.validate(name, email, subject, message);
		if(vl){
			$('#loading-dialog').css({display: 'block', width: '100%', height: '100%'});
			$http({
			method: 'POST',
			url: NEW_SERVICE_URL + 'contact/contact',
			data: {
				"request_data_type": "essay",
				"request_data_method": "contact",
				"request_data": {
					"name": name.val(),
					"uid": userId,
					"email": email.val(),
					"subject": subject.val(),
					"message": message.val()
				}	
			}
			// params: { 
			// name: name.val(),
			// Email: email.val(),
			// Subject: subject.val(),
			// Message: message.val()
			// },
			// headers: {'Content-Type': 'application/x-www-form-urlencoded'}
			}).success(function(data) {
				$('#loading-dialog').css({display: 'none', width: '100%', height: '100%'});
				if(data.status == "true") {
					$scope.setNullinput();
					
					setTimeout(function() {
						$('#alert').show();
					}, 
					1000);

					setTimeout(function() {
						$('#alert').hide();
					}, 
					4000);
				} else {
					$scope.setNullinput();
					$('#warning').show();
				}
			}).error(function(data){
				$('#loading-dialog').css({display: 'none', width: '100%', height: '100%'});
				$scope.setNullinput();
				$('#warning').show();
			});
		}
	};

	$scope.signIn = function(){
		console.log('singIn');
		// brot.signin.signin();
		$('#popSignIn').modal('show');
	};
}]);