brotControllers.controller('ContactController', ['$scope', '$http','MentorService', function($scope, $http, MentorService){
	
	var name = angular.element(document.querySelector('#txtName'));
	var email = angular.element(document.querySelector('#txtEmail'));
	var subject = angular.element(document.querySelector('#txtSubject'));
	var message = angular.element(document.querySelector('#txtMessage'));
	var userId = localStorage.getItem('userId');
	
	var LIMIT_TOP_MENTORS = 5;
	var OFFSET = 0;
	
	// initial get top mentor
	init();
	
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
	
	
	function init() {
		 //get top mentors by subcribe
	    MentorService.getTopMentorsByLikeRateSubcrible(LIMIT_TOP_MENTORS, OFFSET, 'subcribe', '-1').then(function (data) {
	        var data_result = data.data.request_data_result;
	        if (data_result) {
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