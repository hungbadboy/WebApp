brotControllers.controller('ContactCtrl', ['$scope', '$rootScope', '$http','MentorService','ContactService', function($scope, $rootScope, $http, MentorService, ContactService){
	
	var name = angular.element('#txtName');
	var email = angular.element('#txtEmail');
	var subject = angular.element('#txtSubject');
	var phone = angular.element('#txtPhone');
	var message = angular.element('#txtMessage');
	var userId = localStorage.getItem('userId');
	
	$scope.contact={};
	
	// initial get top mentor
	init();
	
	//set input is null when submit was successfully;
	$scope.setNullinput = function(){
		$scope.contact={};
	};
	
	//validation all values;
	$scope.validate = function(contact){
		
		if(contact.name == null || contact.name === undefined || contact.name.length === 0){
			$scope.msgError = "Name can not be empty";
			name.focus();
			return false;

		} else if(contact.email == null || contact.email === undefined ||contact.email.length === 0){
			$scope.msgError = "Please input a valid email";
			email.focus();
			return false;
		} else if($scope.validateEmail(contact.email) === false){
				$scope.msgError = "Email is invalid";
				email.focus();
				return false;
		} else if(contact.subject == null || contact.subject === undefined ||contact.subject.length === 0){
			$scope.msgError = "Subject can not be empty";
			subject.focus();
			return false;
		} else if(contact.phone == null || contact.phone === undefined ||contact.phone.length === 0){
			$scope.msgError = "Phone can not be empty";
			phone.focus();
			return false;
		} else if(contact.message == null || contact.message === undefined ||contact.message.length === 0){
			$scope.msgError = "Message can not be empty";
			message.focus();
			return false;
		}
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
	$scope.btnSubmit = function(contact){
		$scope.msgError = "";
		$scope.msgSuccess = "";
		var vl = $scope.validate(contact);
		if(vl){
			contact.uid= userId;
			$rootScope.$broadcast('open');
			ContactService.sendContact(contact).then(function (data) {
				$rootScope.$broadcast('close');
				$scope.setNullinput();
				if(data.data.status == 'true') {
					$scope.msgSuccess = "Thank you for your contact";
				} else {
					$scope.msgError = "There are some errors occur. Please try again later";
				}
			});
		}
	};
	
	
	function init() {
		name.focus();		 
	}
}]);