brotControllers.controller('StudentProfileController',
    ['$sce', '$scope', '$modal', '$routeParams', '$rootScope', '$http', '$location', 'StudentService', 'QuestionsService', 'MentorService', 'TeamMentorService', 'myCache', 'VideoService', 'HomeService',
        function ($sce, $scope, $modal, $routeParams, $rootScope, $http, $location, StudentService, QuestionsService, MentorService, TeamMentorService, myCache, VideoService, HomeService ) {
            var userId = localStorage.getItem('userId');
            var userName = localStorage.getItem('userName');
            var userType = localStorage.getItem('userType');
            var subjects = JSON.parse(localStorage.getItem('subjects'));
            
            var limit = 10;
            var offset = 0;
            var isLoadMore = false;
            var mentorId = $routeParams.mentorId;
            // Declare show message
            $scope.msgError = "";
            $scope.msgSuccess = "";

            $scope.currentPwd = "";
            $scope.newPwd = "";
            $scope.confirmPwd = "";
            $scope.isLoadMore = false;
            $scope.EMPTY_DATA = StatusError.MSG_UNKNOWN;
            $scope.isSubscribe = false;
            $scope.hasShowMessage = false;
            // Subject
            $scope.masterSubjects = [];

            var isInit = true;
            $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';
            // $scope.sections = [
            //     {name: 'My Questions'},
            //     {name: 'Essay'},
            //     {name: 'Setting'}
            // ];
            // $scope.selected = $scope.sections[0];

            init();
            // var subjects = myCache.get("subjects");
            function init() {
            	// Get subject
            	

                getMentorInfo();
                if (mentorId != undefined) {
                    getVideosRecently();
                    isSubscribed();
                    getNewestAnswers(mentorId, 6, 0);
                    getInfoMentorProfile();
                }
                getStudentProfile();
                getEssayProfile();
                getMyQuestions(userId, limit, offset, "newest", "-1", "-1");
                
                
            }


            function getMentorInfo() {
                TeamMentorService.getTopMentorsByType(5, 0, 'subcribe', userId).then(function (data) {
                    var data_result = data.data.request_data_result;
                    if (data_result) {
                        var listTopMentors = [];
                        for (var i = 0; i < data_result.length; i++) {
                            var mentor = {};
                            if (data_result[i].isSubs == 1) {
                                mentor.userid = data_result[i].userid;
                                mentor.userName = data_result[i].userName;
                                mentor.imageUrl = data_result[i].imageUrl;
                                mentor.numsub = data_result[i].numsub;
                                mentor.numvideos = data_result[i].numvideos;
                                mentor.isOnline = data_result[i].isOnline;
                                mentor.defaultSubjectId = data_result[i].defaultSubjectId;
                                if (subjects != null || subjects !== undefined) {
                                    if (data_result[i].defaultSubjectId !== null && data_result[i].defaultSubjectId !== undefined) {
                                        mentor.listSubject = getSubjectNameById(data_result[i].defaultSubjectId, subjects);
                                    }
                                }
                                mentor.numAnswers = data_result[i].numAnswers;
                                listTopMentors.push(mentor);
                            }
                        }
                        $scope.listMentorBySubs = listTopMentors;
                    }
                });
            }


            function getEssayProfile() {
                StudentService.getEssayProfile(userId, 0, 0).then(function (data) {
                    if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                        $scope.essays = data.data.request_data_result;
                    }
                });
            }

            $scope.isReadyLoadPoint = false;
            $scope.isReadyLoadRatingRecently = false;
            function getInfoMentorProfile() {
                MentorService.getStudentMentorProfile(mentorId).then(function (data) {
                    if (data.data.status == "true") {
                        var subjects = myCache.get("subjects");
                        if (data.data.request_data_result) {
                            var result_data = data.data.request_data_result;
                            $scope.studentMentorProfile = result_data;
                            var gender = $scope.studentMentorProfile.gender;
                            $scope.GenderMentor = validateGender(gender);
                            var birthDay = calculateBirthDay(result_data.birthDay);
                            $scope.bod = birthDay;
                            if (result_data.defaultSubjectId && (subjects !== undefined || subjects != null)) {
                                var subs = getSubjectNameById(result_data.defaultSubjectId, subjects);
                                $scope.mentorSubs = subs;
                            } else {
                                var skillNull = [{id: -1, name: "None"}];
                                $scope.mentorSubs = skillNull;
                            }
                        } else {
                            $scope.studentMentorProfile = null;
                        }
                        $scope.isReadyLoadPoint = true;

                    }
                });
            }


            $scope.hoverNewestVideo = function () {
                $(".feature-thumnail .hover-video").show();
            };

            $scope.unHoverNewestVideo = function () {
                $(".feature-thumnail .hover-video").hide();
            };

            function getVideosRecently() {
                VideoService.getVideosRecently(mentorId).then(function (data) {
                    if (data.data.status) {
                        if (data.data.request_data_result == StatusError.MSG_DATA_NOT_FOUND) {
                            $scope.videosRecently = null;
                            return;
                        }
                        $scope.videosRecently = data.data.request_data_result;
                        $scope.isReadyLoadRatingRecently = true;
                    }
                });
            }

            $scope.convertUnixTimeToTime = function (time) {
                return convertUnixTimeToTime(time);
            };

            function isSubscribed() {
                StudentService.checkSubscribe(userId, mentorId).then(function (dataResponse) {
                    if (dataResponse.data.status == "false" || dataResponse.data.request_data_result == false
                        || dataResponse.data.request_data_result == StatusError.MSG_USER_ID_NOT_EXIST) {
                        $scope.isSubscribe = false;
                    } else {
                        $scope.isSubscribe = dataResponse.data.request_data_result;
                    }
                });
            }

            function getNewestAnswers(authorId, limit, offset) {
                StudentService.getNewestAnswersById(authorId, limit, offset).then(function (data) {
                    if (data.data.status) {
                        if (data.data.request_data_result.length > 0) {
                            $scope.listNewestAnswers = data.data.request_data_result;
                        } else {
                            $scope.listNewestAnswers = null;
                        }
                    }
                });
            }

            function calculateBirthDay(timeStamp) {
                // var currentTime = new Date()
                // if (strBod) {
                //     var year = 0;
                //     if (strBod.lastIndexOf("-") != -1) {
                //         year = strBod.substring(strBod.lastIndexOf("-") + 1, strBod.length)
                //     } else if (strBod.lastIndexOf(",") != -1) {
                //         year = strBod.substring(strBod.lastIndexOf(",") + 1, strBod.length)
                //     }
                //     if (year != 0) {
                //         var currentYear = currentTime.getFullYear()
                //         return currentYear - year;
                //     }
                //     return null;
                // }
                if (!timeStamp) {
                    return null;
                }
                var _now = Math.floor(Date.now() / 1000);
                var d = new Date();
                d.setTime(587795857);
                var secondElapsed = parseInt(Math.floor((_now - timeStamp)));
                return Math.floor(secondElapsed / (3600 * 24 * 12 * 30));
            }


            // function getStudentProfile() {
            //     StudentService.getUserProfile(userId).then(function (data) {
            //         if (data.data.status) {
            //             $scope.student = data.data.request_data_result;
            //             $scope.student.fullName = (($scope.student.firstname == null) ? '' : $scope.student.firstname) + ' ' + (($scope.student.firstname == null) ? '' : $scope.student.lastName);
            //             //$scope.student.imageUrl =  //$scope.student.imageUrl.indexOf('http') == -1 ? $scope.baseIMAGEQ + $scope.student.imageUrl : $scope.student.imageUrl;
            //             $scope.student.registrationTime = moment($scope.student.registrationTime).format('MMM, YYYY ', 'en');
            //             if ($scope.student.gender == "M") {
            //                 $('#male').prop('checked', true);
            //                 $scope.student.gender = "Male";
            //             }
            //             if ($scope.student.gender == "F") {
            //                 $('#female').prop('checked', true);
            //                 $scope.student.gender = "Female";
            //             } else {
            //                 $('#other').prop('checked', true);
            //                 $scope.student.gender = "Other";
            //             }
            //             displaySetting();
            //         }
            //     });
            // }

            function displayInformation() {
                $("#firstName").val($scope.studentInfo.firstname);
                $("#lastName").val($scope.studentInfo.lastName);
                $("#about").val($scope.studentInfo.bio);
                $("#email").val($scope.studentInfo.email);
                $("#bod").val($scope.birthDay);
                $("#school").val($scope.studentInfo.school);

                if ($scope.gender) {
                    switch ($scope.gender) {
                        case "Male":
                            $('input[name="gender"][value="male"]').prop('checked', true);
                            break;
                        case "Female":
                            $('input[name="gender"][value="female"]').prop('checked', true);
                            break;
                        case "Other":
                            $('input[name="gender"][value="other"]').prop('checked', true);
                            break;
                    }
                }
                // Get favourite
                if ($scope.studentInfo.favorite != null && $scope.studentInfo.favorite !== undefined) {
                    var favorite = $scope.studentInfo.favorite.split(',');
                    for (var i = 0; i < favorite.length; i++) {
                        if (favorite[i] == 'Music') {
                            $('input[name="music"][value="Music"]').prop('checked', true);
                        }
                        if (favorite[i] == 'Art') {
                            $('input[name="art"][value="Art"]').prop('checked', true);
                        }
                        if (favorite[i] == 'Sport') {
                            $('input[name="sport"][value="Sport"]').prop('checked', true);
                        }
                    }
                }

                var objSelected = $(".subject-field input[type='checkbox']");
                for (var i = 0; i < objSelected.length; i++) {
                    var attrSub = objSelected[i];
                    $(attrSub).prop('checked', true);
                }
            }


            function detectMultiImage(imagePath) {
                if (isEmpty(imagePath)) {
                    return null;
                }
                if (imagePath != null && imagePath.indexOf(";") != -1) {
                    return imagePath.split(";");
                }
                var listImage = [];
                listImage.push(imagePath);
                return listImage;
            }

            // $scope.setMaster = function (section) {
            //     $scope.selected = section;

            //     if ($scope.selected.name == "Setting") {
            //         $("#setting").addClass('active');
            //         $("#essay").removeClass('active');
            //         $("#my-questions").removeClass('active');
            //     }
            //     else if ($scope.selected.name == "Essay") {
            //         $("#essay").addClass('active');
            //         $("#setting").removeClass('active');
            //         $("#my-questions").removeClass('active');
            //     }
            //     else {
            //         $("#my-questions").addClass('active');
            //         $("#essay").removeClass('active');
            //         $("#setting").removeClass('active');
            //     }
            // }

            // $scope.isSelected = function (section) {
            //     return $scope.selected === section;
            // }

            $scope.onFileSelect = function ($files) {
                var fd = new FormData();
                if ($files != null) {
                    fd.append('uploadfile', $files[0]);
                    fd.append("userid", userId);
                    fd.append("imageUrl", localStorage.getItem('imageUrl'));
                    StudentService.uploadAvatar(fd).then(function (data) {
                        if (data.data.status == "true") {
                            var urlImage = data.data.request_data_result;
                            setStorage('imageUrl', urlImage, 10);
                            $scope.studentInfo.imageUrl = urlImage;
                            $rootScope.imageUrl = urlImage;
                        }
                        else {
                            $scope.errorMessage = "Can't not upload avatar";
                        }
                    });
                }
            };

            $scope.changePassword = function () {
                //get value input
                var oldPwd = $('#password').val();
                var newPwd = $('#pass').val();
                var confirmPwd = $('#confirm').val();
                // Valid
                if (oldPwd == "" || oldPwd === undefined) {
                    $scope.msgError = "Password is required.";
                    angular.element('#password').trigger('focus');
                } else if (newPwd == "" || newPwd === undefined) {
                    $scope.msgError = "New password is required.";
                    angular.element('#newPwd').trigger('focus');
                } else if (confirmPwd == "" || confirmPwd === undefined) {
                    $scope.msgError = "Confirm password is required.";
                    angular.element('#confirmPwd').trigger('focus');
                } else if (oldPwd == newPwd) {
                    angular.element('#newPwd').trigger('focus');
                    $scope.msgError = "New Password must difference New Password.";
                } else if (newPwd !== confirmPwd) {
                    angular.element('#newPwd').trigger('focus');
                    $scope.msgError = "Password is not matched.";
                } else {// Request change pwd
                    var user = {
                        'username': userName,
                        'password': oldPwd,
                        'newpassword': newPwd
                    };

                    StudentService.changePassword(user).then(function (data) {
                        if (data.data.request_data_result == "Success") {
                            resetFormPwd();
                            $scope.msgSuccess = "Change password successful.";
                        } else {
                            $scope.msgError = "Change password failure. Please try again !";
                        }
                        $scope.hasShowMessage = true;
                    });
                }
            };

            function resetFormPwd() {
                $('#password').val('');
                $('#pass').val('');
                $('#confirm').val('');
            }

            $scope.resetFormPwd = function () {
                resetFormPwd();
            };

            $scope.updateProfile = function () {
                var check = true;
                var error = '';
                $scope.hasShowMessage = false;
                var favorite = "";
                if ($('input[name="music"][value="Music"]').is(':checked')) {
                    favorite += $('input[name="music"][value="Music"]').val();
                }
                if ($('input[name="art"][value="Art"]').is(':checked')) {
                    if (favorite.length > 0)
                        favorite += ',' + $('input[name="art"][value="Art"]').val();
                    else
                        favorite += $('input[name="art"][value="Art"]').val();
                }
                if ($('input[name="sport"][value="Sport"]').is(':checked')) {
                    if (favorite.length > 0)
                        favorite += ',' + $('input[name="sport"][value="Sport"]').val();
                    else
                        favorite += $('input[name="sport"][value="Sport"]').val();
                }

                var gender = '';
                if ($('#male').is(':checked')) {
                    gender = "M";
                }
                else if ($('#female').is(':checked')) {
                    gender = "F";
                }
                else {
                    gender = "O";
                }


                var email = $('input[name="email"]').val();

                if(!isEmpty(email)){
                    if (!isValidEmailAddress($('input[name="email"]').val())) {
                        check = false;
                        error += "Email is not valid";
                    }
                }else{
                    email = "";
                }

                // if (!isValidEmailAddress($('#email').val())) {
                //     check = false;
                //     $scope.msgError = "Email is not valid";
                //     angular.element('#email').trigger('focus');
                // }
                // var selected = [];
                // var objSelected = $('.subject-field input:checked');
                // for (var i = 0; i < objSelected.length; i++) {
                //     selected.push(objSelected[i].defaultValue);
                // }
                // var strSubs = selected.join(',');

                if (check) {
                    var student = {
                        'userid': userId,
                        'firstName': $('#firstName').val(),
                        'lastName': $('#lastName').val(),
                        'email': $('#email').val(),
                        'gender': gender,
                        'school': $('#school').val(),
                        'bod': $('#bod').val(),
                        'bio': $('#about').val(),
                        'favorite': favorite
                        // 'defaultSubjectId': strSubs
                    };
                    StudentService.updateUserProfile(student).then(function (data) {
                        if (data.data.request_data_result == "Success") {
                            var subjects = myCache.get("subjects");
                            if (student) {
                                $scope.studentInfo.firstname = student.firstName;
                                $scope.studentInfo.lastName = student.lastName;
                                $scope.gender = validateGender(student.gender);
                                $scope.birthDay = student.bod;
                                $scope.studentInfo.favorite = student.favorite;
                                $scope.studentInfo.school = student.school;
                                $scope.studentInfo.email = student.email;
                                $scope.studentInfo.bio = student.bio;
                                if (student.gender) {
                                    switch ($scope.gender) {
                                        case "Male":
                                            $('input[name="gender"][value="male"]').prop('checked', true);
                                            break;
                                        case "Female":
                                            $('input[name="gender"][value="female"]').prop('checked', true);
                                            break;
                                        case "Other":
                                            $('input[name="gender"][value="other"]').prop('checked', true);
                                            break;
                                    }
                                }
                                if (subjects != null || subjects !== undefined) {
                                    $scope.objSubs = getSubjectNameById(strSubs, subjects);
                                }
                            }
                            $scope.msgSuccess = "Update Profile Successful !";
                        }
                        else {
                            if (error != '') {
                                $scope.msgError = error;
                            } else {
                                $scope.msgError = "Update Profile Failure";
                            }
                        }
                        $scope.hasShowMessage = true;
                    });
                }
            };

            $scope.changeTab = function () {
                $scope.hasShowMessage = false;
            };

            $scope.resetProfile = function () {
                displayInformation();
            };

            /**
             * Get my question
             */
            function getMyQuestions(userId, limit, offset, type, oldQid, subjectid) {

                QuestionsService.getQuestionByUserId(userId, limit, offset, type, oldQid, subjectid).then(function (data) {
                    if (data.data.status) {
                        var result = data.data.request_data_result;
                        if (!isLoadMore) {
                            listPosted = [];
                        }
                        if (result == null || result.question === undefined) {
                            return;
                        }
                        for (var i = 0; i < result.question.length; i++) {
                            var objPosted = {};
                            var questionData = result.question[i];
                            objPosted.id = questionData.PID;
                            oldQid = questionData.PID;
                            objPosted.title = questionData.TITLE;
                            objPosted.subject = questionData.SUBJECT;
                            objPosted.subjectid = questionData.SUBJECTID;
                            objPosted.name = questionData.FIRSTNAME;
                            objPosted.content = questionData.CONTENT;
                            objPosted.numviews = questionData.NUMVIEWS == null ? 0 : questionData.NUMVIEWS;
                            objPosted.time = convertUnixTimeToTime(questionData.TIMESTAMP);
                            objPosted.image = detectMultiImage(questionData.IMAGEPATH);
                            //objPosted.count = questionData.length;
                            if (result.answers !== undefined) {
                                if (result.answers[i] != null) {
                                    var answer = result.answers[i];
                                    var answer_result = answer.body.request_data_result;
                                    objPosted.count_answer = answer_result.length;
                                    var listAnswer = [];
                                    for (var y = 0; y < answer_result.length; y++) {

                                        var objAnswer = {};
                                        objAnswer.authorID = answer_result[y].authorID;
                                        objAnswer.aid = answer_result[y].aid;
                                        objAnswer.pid = answer_result[y].pid;
                                        objAnswer.name = answer_result[y].firstName + " " + answer_result[y].lastName;
                                        var answer_text = decodeURIComponent(answer_result[y].content);
                                        answer_text = $sce.trustAsHtml(answer_text);
                                        objAnswer.content = answer_text;
                                        objAnswer.avatar = answer_result[y].imageUrl;
                                        objAnswer.countLike = answer_result[y].countLike;
                                        if (answer_result[y].likeAnswer == null || answer_result[y].likeAnswer === "N") {
                                            objAnswer.like = "No Like";
                                        } else {
                                            objAnswer.like = "Liked";
                                        }
                                        objAnswer.time = convertUnixTimeToTime(answer_result[y].timeStamp);
                                        listAnswer.push(objAnswer);
                                        objPosted.answers = listAnswer;
                                    }
                                }
                            } else {
                                objPosted.answers = null;
                                objPosted.count_answer = "0";
                            }
                            listPosted.push(objPosted);
                        }
                        if (result.question.length == 0) {
                            listPosted = [];
                            if (isLoadMore) {
                                return;
                            }
                        }
                        $scope.askQuestion = listPosted;
                    }
                });
            }

            /**
             * Cancel subscriber
             */
            $scope.unSubscribeMentor = function (idx, mentorId) {
                if (isEmpty(userId)) {
                    return;
                }
                VideoService.setSubscribeMentor(userId, mentorId).then(function (response) {
                    if (response.data.status == "true") {
                        if (response.data.request_data_type == "unsubs") {
                            $scope.isSubscribe = 1;
                            $scope.listMentorBySubs.splice(idx, 1);
                        }
                        else {
                            $scope.isSubscribe = -1;
                        }
                    }
                });
            };

            /**
             * set Subscriber
             */
            $scope.setSubscribeMentor = function () {
                if (isEmpty(userId)) {
                    return;
                }
                VideoService.setSubscribeMentor(userId, mentorId).then(function (response) {
                    if (response.data.status == "true") {
                        if (response.data.request_data_type == "subs") {
                            $scope.isSubscribe = 1;
                        }
                        else {
                            $scope.isSubscribe = -1;
                        }
                    }
                });
            };


            /**
             * Preview image
             */
            $scope.zoomImage = function (img) {
                $scope.currentImage = ( img );
                $(".popup-images").css({"left": 0});
            }

            $scope.closeImage = function () {
                $(".popup-images, .form-ask-question").css({"left": "100%"});
            }
            $scope.imageHoverIn = function (eId) {
                angular.element("#" + eId).addClass('show');
            }
            $scope.imageHoverOut = function (eId) {
                angular.element("#" + eId).removeClass('show');
            }

            /**
             * Link to question detail
             */
            $scope.detailQuestion = function (id) {
                window.location.href = '/#/question_detail/' + id + "";
            }
            /**
             * @author: Tavv
             */

            function getStudentProfile() {
                if (userId == undefined) {
                    return;
                }
                StudentService.getUserProfile(userId).then(function (dataResponse) {
                    if (dataResponse.data.status) {
                        if (dataResponse.data.request_data_result == StatusError.MSG_USER_ID_NOT_EXIST) {
                            $scope.studentInfo = null;
                            return;
                        }
                        $scope.studentInfo = dataResponse.data.request_data_result;
                        $scope.studentInfo.imageUrl = $scope.studentInfo.imageUrl != null ? $scope.studentInfo.imageUrl : "assets/images/noavartar.jpg";
                        var gender = $scope.studentInfo.gender;
                        $scope.gender = validateGender(gender);
                        var bioTimeStamp = $scope.studentInfo.birthDay;
                        var registrationTime = $scope.studentInfo.registrationTime;
                        $scope.birthDay = timeConverter(bioTimeStamp, FormatDateTimeType.DD_MM_YY);
                        $scope.sinceDay = timeConverter(registrationTime, FormatDateTimeType.MM_YY);
                        $scope.isLoginViaFBOrGoogle = $scope.studentInfo.idFacebook != null || $scope.studentInfo.idGoogle != null;
                        //var subjects = myCache.get("subjects");
                        if (subjects != undefined || subjects != null) {
                            var subsName = getSubjectNameById($scope.studentInfo.defaultSubjectId, subjects);

                            if (subsName != null && subsName != undefined) {
                            	var listSubs = [];
                                $scope.objSubs = subsName;
                                subsName.forEach(function (sub) {
                                    if (subsName.length - 1) {
                                        listSubs.push(sub.name);
                                    }
                                });
                                
                                $scope.subjectsName = listSubs.join(', ');
                            } else {
                                $scope.subjectsName = "None";
                            }
                        } else {
                            $scope.subjectsName = "None";
                        }
                    }
                    displayInformation();
                    
                    // This call method to return $scope.masterSubjects selected 
                       putMasterSubjectSelected(subjects);
                });

            }
        /**
         * Get master Subject
         */    
        function putMasterSubjectSelected(subjects) {
        	var subjectsSelected = [];
        	if(subjects != null && subjects !== undefined) {
        		$scope.masterSubjects =[];
        		subjectsSelected.push(subjects);
        		for (var i = 0; i < subjects.length; i ++) {
        			if (subjects[i].level == 0) {
        				var subject = subjects[i];
        				if(isCheckedSubject(subject.subjectId)) {
				    		subject.selected = "1";
				    	} else {
				    		subject.selected = "0";
				    	}
				    	$scope.masterSubjects.push(subject);
		            }
        		}
        	}
        }
        
        /**
         * checked is true or false
         */
        function isCheckedSubject(subid) {
        	if(subid !=null && subid !== undefined && subid !== '' && $scope.subjects !== undefined) {
            	for(var i=0; i< $scope.subjects; i++) {
            		if($scope.subjects[i].subjectId == subid){
            			return true;
            		} 
            	}
        	}
        	return false;
        }
        }]);
