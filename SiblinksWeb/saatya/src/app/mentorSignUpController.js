brotControllers.controller('SignUpCompleteController', ['$scope', '$http', '$location', '$log', 'StudentService', 'MentorSignUpService', 'HomeService', 'myCache', function($scope, $http, $location, $log, StudentService, MentorSignUpService, HomeService, myCache){
	$scope.educations = {};
	var day = [];
	var month = [];
	var year = [];

	for(var i = 1; i < 32; i++) {
		day.push(i);
	}
	$scope.day = day;

	for(var j = 1; j < 13; j++) {
		month.push(j);
	}
	$scope.month = month;

	for(var k = 2015; k > 1975; k--) {
		year.push(k);
	}
	$scope.year = year;

	if(myCache.get("subjects") !== undefined) {
		$log.info("My cache already exists");
		$scope.subjects = myCache.get("subjects");
	} else {
		HomeService.getSubjectsWithTag().then(function(data) {
			if(data.data.status) {
				$scope.subjects = data.data.request_data_result;
				myCache.put("subjects", data.data.request_data_result);
			}
		});
	}

	//Get college or university
	// MentorSignUpService.getUniversity().then(function(data) {
	// if(data.data.status) {
	// $scope.educations.college_or_university = data.data.request_data_result;
	// }
	// });

	//Get major
	// StudentService.getListMajors().then(function(data) {
	// if(data.data.status) {
	// $scope.educations.major = data.data.request_data_result;
	// }
	// });

	//Get extracurricular activities
	// StudentService.getListActivities().then(function(data) {
	// if(data.data.status) {
	// $scope.educations.extracurricular_activities = data.data.request_data_result;
	// }
	// });

	// StudentService.getListCategory().then(function(data) {
	// if(data.data.status) {
	// $scope.educations.tutoring_interestes = data.data.request_data_result;
	// }
	// });

  $scope.loginFacebook = function() {
    loginFBService(function(data) {
      $scope.userName = data.email;
      $scope.firstName = data.first_name;
      $scope.lastName = data.last_name;
      $scope.facebookId = data.id;
      StudentService.loginFacebook($scope.userName, 'M', $scope.firstName, $scope.lastName, $scope.facebookId).then(function(data) {
        if(data.data.status == 'true') {
	var dataUser = data.data.request_data_result[0];
      setStorage('userName', dataUser['userName'], 30);
      setStorage('userId', dataUser['userid'], 30);
      setStorage('userType', dataUser['userType'], 10);
      var nameHome = $scope.lastName + ' ' + $scope.firstName;
      setStorage('nameHome', nameHome, 30);
      $('#header .login').addClass('hide');
      $('#header .log_out').removeClass('hide');
      $('#header .log_out .current').text(nameHome);
      window.location.href = '/';
      // window.location.href = '/#/editStudent/education/' + $scope.userName;	
        } else {
	$scope.error = data.data.request_data_result[0];
	$('.sign_up_complete_container .message .alert_error').removeClass('hide');
        }
        
      });
    });
  };

  function onLoadCallback() {
    gapi.client.setApiKey('AIzaSyCRzAc2Jd3r3mhrhBgiWShvEIZNXRWCoRk'); //set your API KEY
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
          $scope.firstName = resp.name.familyName;
          $scope.lastName = resp.name.givenName;
          $scope.googleid = resp.id;
          var nameHome = resp.displayName;
          StudentService.loginGoogle($scope.userName, 'M', $scope.firstName, $scope.lastName, $scope.googleid).then(function(data) {
	if(data.data.status == 'true') {
	var dataUser = data.data.request_data_result[0];
              setStorage('userName', dataUser['userName'], 30);
              setStorage('userId', dataUser['userid'], 30);
              setStorage('userType', dataUser['userType'], 10);
              setStorage('nameHome', nameHome, 30);
              $('#header .login').addClass('hide');
              $('#header .log_out').removeClass('hide');
              $('#header .log_out .current').text(nameHome);
              window.location.href = '/';
              // window.location.href = '/#/editStudent/education/' + $scope.userName;	
	} else {
		$scope.error = data.data.request_data_result[0];
		$('.sign_up_complete_container .message .alert_error').removeClass('hide');
	}
          });
        });
      });
    }  
  }

  $scope.loginGoogle = function() {
    var myParams = {
      'clientid' : '667014101821-v1f9a583qgmt280gtci8n4a87k7uja2m.apps.googleusercontent.com', //You need to set client id
      'cookiepolicy' : 'single_host_origin',
      'callback' : loginCallback, //callback function
      'approvalprompt':'force',
      'scope' : 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.profile.emails.read'
    };
    gapi.auth.signIn(myParams);
  };

	$scope.setFirst = function(index) {
		if(index % 6 === 0) {
			return {"margin-left": "-13px"};
		}
	};

	$scope.cancel = function() {
		$location.url('/');
	};

	$("input[name^='dob']").mask('99/99/9999');
	$scope.error = {};
	$scope.username_error = 0;
	$scope.email_error = 0;
	$scope.password_error = 0;
	$scope.firstname_error = 0;
	$scope.lastname_error = 0;
	$scope.dob_error = 0;
	$scope.specialAccomplishments_error = 0;
	$scope.dream_error = 0;
	$scope.income_error = 0;

	$scope.signUpEvent = function() {
		var check = 0;
		var data_send = {};

		if(mentorSignup.password1.value == null || mentorSignup.password1.value === '' || mentorSignup.password2.value == null || mentorSignup.password2.value === '') {
			$scope.error.password = 'Password is required';
			$scope.password_error = 1;
			check = 1;
		} else {
			if(mentorSignup.password1.toString() == mentorSignup.password2.toString()) {
				$scope.password_error = 0;
				data_send.password = mentorSignup.password1.value;
			} else {
				$scope.password_error = 1;
				$scope.error.password = 'Password confirm incorrect';
			}
		}

		if(mentorSignup.firstname.value == null || mentorSignup.firstname.value === '') {
			$scope.error.firstname = 'Firstname is required';
			$scope.firstname_error = 1;
			check = 1;
		} else {
			$scope.firstname_error = 0;
			data_send.firstname = mentorSignup.firstname.value;
		}

		if(mentorSignup.lastname.value == null || mentorSignup.lastname.value === '') {
			$scope.error.lastname = 'Lastname is required';
			$scope.lastname_error = 1;
			check = 1;
		} else {
			$scope.lastname_error = 0;
			data_send.lastname = mentorSignup.lastname.value;
		}

		if(mentorSignup.dob.value == null || mentorSignup.dob.value === '') {
			$scope.error.dob = 'Birthday is required';
			$scope.dob_error = 1;
			check = 1;
		} else {
			var validateDob = moment(mentorSignup.dob.value, 'MM/DD/YYYY', true).isValid();
			if(!validateDob) {
				$scope.error.dob = 'Birthday not valid mm/dd/yyyy';
				$scope.dob_error = 1;
				check = 1;
			} else {
				$scope.dob_error = 0;
				data_send.dob = mentorSignup.dob.value;
			}
		}

		// if(mentorSignup.specialAccomplishments.value == null || mentorSignup.specialAccomplishments.value === '') {
		// $scope.error.specialAccomplishments = 'Special Accomplishments is required';
		// $scope.specialAccomplishments_error = 1;
		// check = 1;
		// } else {
		// $scope.specialAccomplishments_error = 0;
		// data_send.accomp = mentorSignup.specialAccomplishments.value;
		// }

		// if(mentorSignup.dream.value == null || mentorSignup.dream.value === '') {
		// $scope.error.dream = "This field is required";
		// $scope.dream_error = 1;
		// check = 1;
		// } else {
		// $scope.dream_error = 0;
		// data_send.yourdream = mentorSignup.dream.value;
		// }

		// if(mentorSignup.income.value == null || mentorSignup.income.value === '') {
		// $scope.error.income = "This field is required";
		// $scope.income_error = 1;
		// check = 1;
		// } else {
		// $scope.income_error = 0;
		// data_send.familyincome = mentorSignup.income.value;
		// }

		// var major_data = '';
		// for(var i in mentorSignup.major) {
		// if(mentorSignup.major[i].checked) {
		// major_data += ','+mentorSignup.major[i].value;
		// }
		// }

		// if(major_data === '') {
		// $scope.error.major = 'Major is required';
		// $scope.major_error = 1;
		// check = 1;
		// } else {
		// $scope.major_error = 0;
		// data_send.colmajor = major_data.substring(1);
		// }

		// var col_or_uni_data = '';
		// for(i in mentorSignup.collegeOrUniversity) {
		// if(mentorSignup.collegeOrUniversity[i].checked) {
		// col_or_uni_data += ','+mentorSignup.collegeOrUniversity[i].value;
		// }
		// }
		// if(col_or_uni_data === '') {
		// $scope.error.col_or_uni = 'College or University is required';
		// $scope.col_or_uni_error = 1;
		// check = 1;
		// } else {
		// $scope.col_or_uni_error = 0;
		// data_send.education = col_or_uni_data.substring(1);
		// }

		// var exAct_data = '';
		// for(i in mentorSignup.extracurricularActivities) {
		// if(mentorSignup.extracurricularActivities[i].checked) {
		// exAct_data += ','+mentorSignup.extracurricularActivities[i].value;
		// }
		// }
		// if(exAct_data === '') {
		// $scope.error.exAct = 'Extracurricular Activities is required';
		// $scope.exAct_error = 1;
		// check = 1;
		// } else {
		// $scope.exAct_error = 0;
		// data_send.activities = exAct_data.substring(1);
		// }

		var tut_data = '';
		for(var i in mentorSignup.tutoringInterestes) {
			if(mentorSignup.tutoringInterestes[i].checked) {
				tut_data += ','+mentorSignup.tutoringInterestes[i].value;
			}
		}

		if(tut_data === '') {
			$scope.error.tut = 'Tutoring Interestes is required';
			$scope.tut_error = 1;
			check = 1;
		} else {
			$scope.tut_error = 0;
			data_send.tutoringinterestes = tut_data.substring(1);
		}

		if(mentorSignup.email.value == null || mentorSignup.email.value === '') {
			$scope.error.email = 'Email is required';
			$scope.email_error = 1;
			check = 1;
		} else {
			// if(new IsEmail(mentorSignup.email.value) === true) {
			if(/^[a-zA-Z0-9-.-_--]{3,}\@[a-zA-Z0-9--]{2,16}\.[a-zA-Z0-9]{2,8}$/.test(mentorSignup.email.value) || /^[a-zA-Z0-9-.-_--]{3,}\@[a-zA-Z0-9--]{2,16}\.[a-zA-Z0-9]{2,8}\.[a-zA-Z0-9]{2,8}$/.test(mentorSignup.email.value)) {
				$scope.email_error = 0;
				data_send.email = mentorSignup.email.value;
			} else {
				$scope.error.email = 'Email is invalid';
				$scope.email_error = 1;
				check = 1;
			}
		}

		$scope.error_signup = '';
		if(check === 0) {
			// data_send.familyincome = mentorSignup.income;
			data_send.grade = 0;
			data_send.usertype = 'M';
			MentorSignUpService.signupMentor(data_send.email, data_send.password, data_send.firstname, data_send.lastname, data_send.usertype, data_send.dob, data_send.education, data_send.accomp, data_send.colmajor, data_send.activities, data_send.tutoringinterestes, data_send.familyincome, data_send.yourdream).then(function(data) {
				if(data.data.status == 'true') {
					var url = NEW_SERVICE_URL + '/user/loginUser?username=' + data_send.email + '&password=' + data_send.password;
					$http.get(url).success(function(data) {
						if(data.status == 'true') {
                      var dataUser = data.request_data_result[0];
                      setStorage('userName', dataUser['username'], 30);
                      setStorage('userId', dataUser['userid'], 30);
                      setStorage('userType', dataUser['usertype'], 10);
                      $('#header .login').addClass('hide');
                      $('#header .log_out').removeClass('hide');

                      var nameHome = '';
                      if (dataUser['firstname'] == null || dataUser['lastname'] == null) {
                          nameHome = capitaliseFirstLetter(dataUser['username'].substring(0, dataUser['username'].indexOf('@')));
                      } else {
                          nameHome = capitaliseFirstLetter(dataUser['firstname']) + ' ' + capitaliseFirstLetter(dataUser['lastname']);
                      }
                      setStorage('nameHome', nameHome, 30);
                      $('#header .log_out .current').text(nameHome);
							window.location.href = '/';
						} else {
							$log.info('Can not active student');
						}
					});
				} else {
					$scope.error = data.data.request_data_result[0];
					$('.sign_up_complete_container .infor .alert_error').removeClass('hide');
				}
			});
		} else {
			$scope.error_signup = "Regist fail";
			jQuery('html').animate({scrollTop:0}, 'slow');
			jQuery("body").animate({ scrollTop: 0 }, 'slow');
		}
	};
	$scope.$on('$viewContentLoaded', function() {
		$(".nano").nanoScroller();
	});
}]);
