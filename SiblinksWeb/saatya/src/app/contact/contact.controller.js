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
			$scope.msgError = "Please enter your Name.";
			name.val('');
			name.focus();
			return false;
		} else if(!$scope.validateEmail(contact.email)){
			return false;
		} else if(contact.phone == null || contact.phone === undefined ||contact.phone.length === 0){
			$scope.msgError = "Please enter a your phone number.";
			phone.focus();
			return false;
		} else if(contact.subject == null || contact.subject === undefined ||contact.subject.length === 0){
			$scope.msgError = "Please enter a Subject.";
			subject.val('');
			subject.focus();
			return false;
		} else if(contact.subject != null && contact.subject.trim().length < 20){
			$scope.msgError = "Subject should be enter more than 20 characters";
			subject.val('');
			subject.focus();
			return false;
		} else if(contact.message == null || contact.message === undefined ||contact.message.length === 0){
			$scope.msgError = "Please enter a Message.";
			message.val('');
			message.focus();
			return false;
		}
		return true;
	};
	
	
	//Validation email address;
	$scope.validateEmail =  function(emailValue) {
		if(emailValue == null || emailValue === undefined || emailValue.trim().length === 0){
			$scope.msgError = "Please enter your email address.";
			email.val('');
			email.focus();
			return false;
		} else {
			var re = /^(([^<>()[\]\\~`^*&/'|{}.,;:+=!%$#_\s@\"]+(\.[^<>()[\]\\~`^*&/|{}.,;:+=!%$#_\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			if(!re.test(emailValue)) {
				$scope.msgError = "Your email address is invalid.";
				email.focus();
				return false;
			} else {
				$scope.msgError = "";
				return true;
			}
		}
	};

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