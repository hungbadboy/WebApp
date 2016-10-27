brotControllers.controller('ContactController', ['$scope', '$rootScope', '$http','MentorService','ContactService', function($scope, $rootScope, $http, MentorService, ContactService){
	
	var name = angular.element('#txtName');
	var email = angular.element('#txtEmail');
	var subject = angular.element('#txtSubject');
	var phone = angular.element('#txtPhone');
	var message = angular.element('#txtMessage');
	var userId = localStorage.getItem('userId');
	
	var LIMIT_TOP_MENTORS = 5;
	var OFFSET = 0;
	
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
		 //get top mentors by subcribe
	    MentorService.getTopMentorsByLikeRateSubcrible(LIMIT_TOP_MENTORS, OFFSET, 'subcribe', '-1').then(function (data) {
	        var data_result = data.data.request_data_result;
	        if (data.data.status == 'true') {
	            var listTopMentors = [];
	            for (var i = 0; i < data_result.length; i++) {
	                var mentor = {};
	                mentor.userid = data_result[i].userid;
	                mentor.userName = data_result[i].userName ? data_result[i].userName : '';
	                mentor.lastName = data_result[i].lastName ? data_result[i].lastName : '';
	                mentor.firstName = data_result[i].firstName ? data_result[i].firstName : '';
	                mentor.fullName = mentor.firstName + ' ' +mentor.lastName;
	                mentor.imageUrl = data_result[i].imageUrl;
	                mentor.numlike = data_result[i].numlike;
	                mentor.numsub = data_result[i].numsub;
	                mentor.numvideos = data_result[i].numvideos;
	                mentor.isOnline = data_result[i].isOnline;
	                mentor.defaultSubjectId = data_result[i].defaultSubjectId;
	                if(data_result[i].defaultSubjectId !== null && data_result[i].defaultSubjectId !== undefined) {
	                    mentor.listSubject = getSubjectNameById(data_result[i].defaultSubjectId, $scope.subjects);
	                }
	                mentor.numAnswers = data_result[i].numAnswers;
	                listTopMentors.push(mentor);
	            }
	        }
	        $scope.listTopmentors = listTopMentors;
	    });
	}
}]);