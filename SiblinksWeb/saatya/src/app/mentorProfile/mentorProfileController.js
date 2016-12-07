brotControllers.controller('MentorProfileController',
    ['$sce', '$scope', '$modal', '$routeParams', '$rootScope', '$http', '$location', 'MentorService', 'TeamMentorService', 'VideoService', 'StudentService', 'myCache', 'uploadEssayService', '$window', '$timeout',
        function ($sce, $scope, $modal, $routeParams, $rootScope, $http, $location, MentorService, TeamMentorService, VideoService, StudentService, myCache, uploadEssayService, $window, $timeout) {

            var userId = localStorage.getItem('userId');
            var userType = localStorage.getItem('userType');
            var userName = localStorage.getItem('userName');

            $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

            $scope.isLogged = userId !== undefined && userId != null;
            $scope.query = "";

            var mentorId = $routeParams.mentorId;

            var studentId = $routeParams.studentId;

            $scope.EMPTY_DATA = StatusError.MSG_UNKNOWN;

            $scope.isSubscribe = 0;
            $scope.userId = userId;

            $scope.isLoginViaFBOrGoogle = false;
            var subjects = JSON.parse(localStorage.getItem('subjects'));

            $scope.schoolSelect = null;

            var defaultSubjectChecked = [];
            var defaultFavouriteChecked = [];
            var dob = "";
            $scope.currentPageStudentSubs = 1;
            $scope.isLoadMoreStudentSubs = false;
            
            var totalPageStudentSubs=0
            $scope.defaultLimit = 6;
            var hasLoadMore = false;
            $scope.listMentorSubsSize = 0;
            $scope.disableNext = true;
            var LIMIT_SUBJECT = 3;
            //
            $scope.listStudentSubscribed = [];
            $scope.totalPageStudentSubscribed = 0;
            $scope.pageStudentSubscribed = 1;
            
            init();

            function init() {
                if(!$scope.isLogged){
                    $window.location.href = "#/mentor/signin";
                }
                if (studentId != undefined) {
                    getStudentInfo();
                    getMentorSubscribed(studentId, $scope.defaultLimit, 0);
                }
                if (mentorId != undefined) {
                    getVideosRecently(mentorId);
                    isSubscribed();
                    getInfoAnotherMentor();
                    getNewestAnswers(mentorId, 6, 0);
                }
                getStudentSubscribed(userId, $scope.defaultLimit, 0);
                getMentorProfile();
                setHeightBoxInfo();
                uploadEssayService.collegesOrUniversities().then(function (data) {
                    if (data.data.status) {
                        $scope.listSchools = data.data.request_data_result;
                    }
                });
            }

            /* Start Functions Mentor View Mentor Profile*/
            function getVideosRecently(mentorId) {
                VideoService.getVideoPlaylistRecently(mentorId, 6, 0).then(function (data) {
                    if (data.data.status) {
                        if (data.data.request_data_result == StatusError.MSG_DATA_NOT_FOUND) {
                            $scope.videosMentorRecently = null;
                            return;
                        }
                        $scope.videosMentorRecently = data.data.request_data_result;
                        $scope.isReadyLoadMentorRatingRecently = true;
                    }
                });
            }

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


            function getInfoAnotherMentor() {
                MentorService.getStudentMentorProfile(mentorId).then(function (data) {
                    $scope.$broadcast('onGetInfoMentorCompleted');
                    if (data.data.status == "true") {
                        if (data.data.request_data_result) {
                            var result_data = data.data.request_data_result;
                            $scope.mentorMentorProfile = result_data;
                            var gender = $scope.mentorMentorProfile.gender;
                            $scope.GenderMentor = validateGender(gender);
                            var birthDay = calculateBirthDay(result_data.birthDay);
                            $scope.dob = birthDay;
                            $scope.isEmptyNameMentor = false;
                            if (isNameEmpty($scope.mentorMentorProfile.firstname,$scope.mentorMentorProfile.lastName)){
                                $scope.isEmptyNameMentor = true;
                                $scope.mentorMentorProfile.fullNameMentor = splitUserName($scope.mentorMentorProfile.username);
                            }
                            if (result_data.defaultSubjectId && (subjects !== undefined || subjects != null)) {
                                var subs = getSubjectNameById(result_data.defaultSubjectId, subjects);
                                $scope.listMentorSubs = subs;
                            } else {
                                var skillNull = [{id: -1, name: "None"}];
                                $scope.listMentorSubs = skillNull;
                            }
                        } else {
                            $scope.mentorMentorProfile = null;
                        }
                        $scope.isReadyLoadPoint = true;

                    }
                });
            }
            /* End Functions Mentor View Mentor Profile*/

            function getMentorProfile() {
                if (userId == undefined) {
                    return;
                }
                StudentService.getUserProfile(userId).then(function (dataResponse) {
                	$scope.$broadcast('getMentorProfileCompleted');
                    if (dataResponse.data.status) {
                        if (dataResponse.data.request_data_result == StatusError.MSG_USER_ID_NOT_EXIST) {
                            $scope.mentorInfo = null;
                            return;
                        }
                        $scope.mentorInfo = dataResponse.data.request_data_result;
                        $scope.mentorInfo.imageUrl = $scope.mentorInfo.imageUrl != null ? $scope.mentorInfo.imageUrl : "assets/images/noavartar.jpg";
                        if ($scope.mentorInfo.school != null && !isEmpty($scope.mentorInfo.school)) {
                            $scope.schoolSelect = {
                                id: parseInt($scope.mentorInfo.school, 10),
                                name: $scope.mentorInfo.schoolName
                            };
                        }
                        $scope.isLoginViaFBOrGoogle = $scope.mentorInfo.idFacebook != null || $scope.mentorInfo.idGoogle != null;
                        $scope.mentorInfo.fullName = displayUserName($scope.mentorInfo.fistname, $scope.mentorInfo.lastName, $scope.mentorInfo.username);
                        $scope.masterSubjects = putMasterSubjectSelected(subjects, $scope.mentorInfo.defaultSubjectId, false);
                        $scope.masterFavourite = putMasterSubjectSelected(subjects, $scope.mentorInfo.favorite, true);
                        defaultSubjectChecked = $scope.masterSubjects;
                        defaultFavouriteChecked = $scope.masterFavourite;
                        var subName = [];
                        for (var i = 0; i < $scope.masterSubjects.length; i++) {
                            if ($scope.masterSubjects[i].selected == "1") {
                                subName.push($scope.masterSubjects[i].subject);
                            }
                        }
                        if (subName.length > 0) {
                            $scope.mentorSubs = subName.join(', ');
                        } else {
                            $scope.mentorSubs = "None";
                        }

                        displayInformation();
                    }
                });

            }

            $scope.getBirthDay = function (bioTimeStamp) {
                return timeConverter(bioTimeStamp, FormatDateTimeType.DD_MM_YY);
            };

            $scope.getSinceDay = function (registrationTime) {
                return timeConverter(registrationTime, FormatDateTimeType.DD_MM_YY);
            };

            /**
             * Calculator old from birth day.
             */
            function calculateBirthDay(timeStamp) {
                if (!timeStamp) {
                    return null;
                }
                var _now = Math.floor(Date.now() / 1000);
                var d = new Date();
                d.setTime(587795857);
                var secondElapsed = parseInt(Math.floor((_now - timeStamp)));
                return Math.floor(secondElapsed / (3600 * 24 * 12 * 30));
            }

            function displayInformation() {
                $('#firstName').val($scope.mentorInfo.firstname);
                $('#lastName').val($scope.mentorInfo.lastName);
                $('#email').val($scope.mentorInfo.email);
                var convertDOB = $scope.getBirthDay($scope.mentorInfo.birthDay);
                $('#dob').val(convertDOB);
                $('#aboutme').val($scope.mentorInfo.bio);
                $('#accomplishments').val($scope.mentorInfo.accomplishments);
                if ($scope.mentorInfo.gender) {
                    switch ($scope.mentorInfo.gender) {
                        case "M":
                            $('#male').prop('checked', true);
                            break;
                        case "F":
                            $('#female').prop('checked', true);
                            break;
                        case "O":
                            $('#other').prop('checked', true);
                            break;
                    }
                }
            }

            /**
             *  getStudentSubscribed
             * @param userId
             * @param defaultLimit
             * @param offset
             */
            function getStudentSubscribed(userId, defaultLimit, offset) {
                MentorService.getStudentSubscribed(userId, defaultLimit, offset, offset==0, $scope.query.trim()).then(function (data) {
                    if (data.data.status == 'true') {
                        var response = data.data.request_data_result;
                        if (response && response != "Found no data") {
                            $scope.listStudentSubscribed = (!$scope.isLoadMoreStudentSubs)? response : $scope.listStudentSubscribed.concat(response);
                            if(offset==0) {
                            	$scope.countAll = data.data.count;
                            	$scope.totalPageStudentSubscribed = Math.ceil($scope.countAll / $scope.defaultLimit);
                            	$scope.isLoadMoreStudentSubs = $scope.totalPageStudentSubscribed > 1;
                            }
                        } else {
                        	$scope.listStudentSubscribed = [];
                        	$scope.totalStudentSubs = 0;
                        	$scope.isLoadMoreStudentSubs = false;
                        }
                    } else {
                    	$scope.listStudentSubscribed = [];
                    	$scope.totalStudentSubs = 0;
                    	$scope.isLoadMoreStudentSubs = false;
                    }
                });
            }
            
            /**
             * loadMoreStudentSubscribed
             */
            $scope.loadMoreStudentSubscribed = function() {
            	if ($scope.pageStudentSubscribed >= $scope.totalPageStudentSubscribed) {
            		return;
            	}
            	$scope.isLoadMoreStudentSubs = true;
            	getStudentSubscribed(userId, $scope.defaultLimit, $scope.defaultLimit * $scope.pageStudentSubscribed);
            	$scope.pageStudentSubscribed = $scope.pageStudentSubscribed + 1;
            }
            
            function getTimeZone() {
                var offset = new Date().getTimezoneOffset(), o = Math.abs(offset);
                return (offset < 0 ? "+" : "-") + ("00" + Math.floor(o / 60)).slice(-2) + ":" + ("00" + (o % 60)).slice(-2);
            }

            $scope.updateProfile = function () {
                var check = true;
                var error = "";
                $scope.msgError = "";
                $scope.msgSuccess = "";
                var activityType = 'profile';
                var gender = '';
                if ($('input[name="gender"][value="male"]').is(':checked')) {
                    gender = "M";
                }
                else if ($('input[name="gender"][value="female"]').is(':checked')) {
                    gender = "F";
                }
                else {
                    gender = "O";
                }

                var email = $('input[name="email"]').val();

                if (!isEmpty(email)) {
                    if (!isValidEmailAddress(email)) {
                        check = false;
                        error += "Email is not valid,";
                    }
                } else {
                    email = "";
                }
                var strSubsName = "";
                // Selected Subject
                var arrSubjectSelected = [];
                var subjectSelected = angular.element('.masterSubject:checked');
                for (var i = 0; i < subjectSelected.length; i++) {
                    arrSubjectSelected.push(subjectSelected[i].defaultValue);
                    strSubsName += subjectSelected[i].name + ', ';
                }
                // Selected Favourite
                var arrFavouriteSelected = [];
                var favSelected = angular.element('.masterFavourite:checked');
                for (var i = 0; i < favSelected.length; i++) {
                    arrFavouriteSelected.push(favSelected[i].defaultValue);
                    arrSubjectSelected.push(favSelected[i].defaultValue);
                }
                var strSubs = arrSubjectSelected.join(',');
                var favorite = arrFavouriteSelected.join(',');

                var school = $scope.schoolSelect != null && !isEmpty($scope.schoolSelect) ? $scope.schoolSelect.id : null;
                var bio = $('textarea[name="aboutme"]').val();
                var firstName = $('input[name="firstname"]').val();
                var lastName = $('input[name="lastname"]').val();
                if((!isEmpty(firstName) && firstName.length > 50) || (!isEmpty(lastName) && lastName.length > 50)){
                    check = false;
                    error += "First name or last name must not exceed 50 characters,";
                }
                if (isNotValidName(firstName) || isNotValidName(lastName)) {
                    check = false;
                    error += "First name or last name contains special characters or number,";
                }
                if (bio.length > 500) {
                    check = false;
                    error += "About me must not exceed 500 characters, ";
                }
                if(isEmpty(firstName)){
                    firstName = null;
                }
                if(isEmpty(lastName)){
                    lastName = null;
                }
                var dob = angular.element('input[id="dob"]').val();
                if (check) {
                    var timeStamp = null;
                    if(dob){
                        timeStamp = (Date.parse(dob)/1000);
                    }
                    var mentor = {
                        'role': "M",
                        'activity' : activityType,
                        'userid': userId,
                        'firstName': firstName,
                        'lastName': lastName,
                        'email': email,
                        'gender': gender,
                        'accomplishments': $('input[name="accomplishments"]').val(),
                        'school': school,
                        'dob': dob,
                        'bio': bio,
                        'favorite': favorite,
                        'defaultSubjectId': strSubs
                    };
                    $rootScope.$broadcast('open');
                    StudentService.updateUserProfile(mentor).then(function (data) {
                        $rootScope.$broadcast('close');
                        if (data.data.request_data_result == "Success") {
                            if (mentor) {
                                if ($scope.isEmptyName) {
                                    if (!isEmpty(firstName) || !isEmpty(firstName)) {
                                        $scope.mentorInfo.fullName = "";
                                        $scope.isEmptyName = false;
                                    }
                                }
                                $scope.mentorInfo.firstname = mentor.firstName;
                                $scope.mentorInfo.lastName = mentor.lastName;
                                $scope.mentorInfo.gender = mentor.gender;
                                $scope.mentorInfo.favorite = mentor.favorite;
                                $scope.mentorInfo.accomplishments = mentor.accomplishments;
                                $scope.mentorInfo.email = mentor.email;
                                $scope.mentorInfo.bio = mentor.bio;
                                $scope.mentorInfo.birthDay = timeStamp;
                                $scope.mentorInfo.schoolName = $scope.schoolSelect != null ? $scope.schoolSelect.name : null;
                                $scope.mentorSubs = strSubsName.substr(0, strSubsName.lastIndexOf(','));
                                localStorage.setItem('defaultSubjectId', strSubs);
                                localStorage.setItem('firstName', mentor.firstName);
                                localStorage.setItem('lastname', mentor.lastName);
                                localStorage.setItem('school', $scope.schoolSelect.id);
                                $rootScope.firstName = mentor.firstName;
                                updateDefaultFavsSubs();
                            }
                            $scope.msgSuccess = "Your profile has been updated successfully!";
                        }else {
                            if (error != '') {
                                $scope.msgError = error.substr(0, error.lastIndexOf(','));
                            } else {
                                $scope.msgError = "Failed to update your profile";
                            }
                        }
                    });
                }
                else {
                    $scope.msgError = error.substr(0, error.lastIndexOf(','));
                }
            };

            function updateDefaultFavsSubs() {
                var allCheckboxSubs = angular.element('.masterSubject');
                if(defaultSubjectChecked){
                    for (var i = 0; i < allCheckboxSubs.length; i++) {
                        defaultSubjectChecked[i].selected = allCheckboxSubs[i].checked ? '1' : '0';
                    }
                }
                if(defaultFavouriteChecked){
                    var allCheckboxFavs = angular.element('.masterFavourite');
                    for (var i = 0; i < allCheckboxFavs.length; i++) {
                        defaultFavouriteChecked[i].selected = allCheckboxFavs[i].checked ? '1' : '0';
                    }
                }
            }

            /**
             * event changing tab switch profile and password
             */
            $scope.changeTab = function () {
                $scope.msgError = "";
                $scope.msgSuccess = "";
                resetFormPwd();
            };

            /**
             * event perform button reset in profile
             */
            $scope.reset = function () {
                if ($scope.mentorInfo) {
                    angular.element('#firstName').val($scope.mentorInfo.firstname);
                    angular.element('#lastName').val($scope.mentorInfo.lastName);
                    angular.element('#email').val($scope.mentorInfo.email);
                    angular.element('#accomplishments').val($scope.mentorInfo.accomplishments);
                    $scope.schoolSelect = $scope.mentorInfo.school != null ? {id: parseInt($scope.mentorInfo.school, 10)} : null;
                    var convertDOB = $scope.getBirthDay($scope.mentorInfo.birthDay);
                    angular.element('#dob').val(convertDOB);
                    angular.element('#aboutme').val($scope.mentorInfo.bio);
                    var subjectChecked = angular.element('.masterSubject:checked');
                    for (var i = 0; i < subjectChecked.length; i++) {
                        subjectChecked[i].checked = false;
                    }
                    var allCheckboxSubs = angular.element('.masterSubject');
                    for (var i = 0; i < allCheckboxSubs.length; i++) {
                        if (defaultSubjectChecked[i].selected == "1") {
                            allCheckboxSubs[i].checked = true;
                        }
                    }
                    var favouriteChecked = angular.element('.masterFavourite:checked');
                    for (var i = 0; i < favouriteChecked.length; i++) {
                        favouriteChecked[i].checked = false;
                    }
                    var allCheckboxFavs = angular.element('.masterFavourite');
                    for (var i = 0; i < allCheckboxFavs.length; i++) {
                        if (defaultFavouriteChecked[i].selected == "1") {
                            allCheckboxFavs[i].checked = true;
                        }
                    }
                    switch ($scope.mentorInfo.gender) {
                        case "M":
                            angular.element('#male').prop('checked', true);
                            break;
                        case "F":
                            angular.element('#female').prop('checked', true);
                            break;
                        case "O":
                            angular.element('#other').prop('checked', true);
                            break;
                        default:
                            angular.element('#other').prop('checked', true);
                            break;
                    }
                }
            };

            /**
             * perform button reset in change password
             */
            $scope.resetFormPwd = function () {
                resetFormPwd();
            };

            function resetFormPwd() {
                $('#password').val('');
                $('#pass').val('');
                $('#confirm').val('');

            }

            /**
             * perform action change password
             */
            $scope.changePassword = function () {
                $scope.msgError = "";
                $scope.msgSuccess = "";
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
                    angular.element('#pass').trigger('focus');
                } else if (confirmPwd == "" || confirmPwd === undefined) {
                    $scope.msgError = "Confirm password is required.";
                    angular.element('#confirm').trigger('focus');
                } else if (oldPwd == newPwd) {
                    angular.element('#pass').trigger('focus');
                    $scope.msgError = "New Password must difference old Password.";
                } else if (newPwd !== confirmPwd) {
                    angular.element('#confirm').trigger('focus');
                    $scope.msgError = "Password is not matched.";
                } else {// Request change pwd
                    var user = {
                        'username': userName,
                        'password': oldPwd,
                        'newpassword': newPwd
                    };
                    $rootScope.$broadcast('open');
                    StudentService.changePassword(user).then(function (data) {
                        $rootScope.$broadcast('close');
                        if (data.data.status == "true") {
                            resetFormPwd();
                            $scope.msgSuccess = data.data.request_data_result;
                        } else {
                            $scope.msgError = data.data.request_data_result;
                        }
                    });
                }
            };

            /**
             * @author : Tavv
             * @description : functions using to render UI mentorStudentProfile template
             */

            function getStudentInfo() {
                StudentService.getUserProfile(studentId).then(function (dataResponse) {
                    if (dataResponse.data.status) {
                        if (dataResponse.data.request_data_result == StatusError.MSG_USER_ID_NOT_EXIST) {
                            $scope.studentInfo = null;
                            return;
                        }
                        // var subjects = myCache.get("subjects");
                        $scope.studentInfo = dataResponse.data.request_data_result;
                        var gender = $scope.studentInfo.gender;
                        $scope.gender = validateGender(gender);
                        $scope.isStudentEmptyName = false;
                        if (isNameEmpty($scope.studentInfo.firstname, $scope.studentInfo.lastName)) {
                            $scope.isStudentEmptyName = true;
                            $scope.studentInfo.fullName = splitUserName($scope.studentInfo.username);
                        }
                        if (subjects) {
                            var subsName = getSubjectNameById($scope.studentInfo.defaultSubjectId, subjects);
                            if (subsName !== undefined && subsName) {
                                var listSubs = [];
                                subsName.forEach(function (sub) {
                                    if (subsName.length - 1) {
                                        listSubs.push(sub.name);
                                    }
                                });
                                $scope.subjects = listSubs.join(', ');
                            } else {
                                $scope.subjects = "None";
                            }
                        } else {
                            $scope.subjects = "None";
                        }
                    }
                });
            }

            $scope.isReadyLoadPointSubscribed = false;
            
            /**
             * Methhod getMentorSubscribed
             * @param studentId
             * @param limit
             * @param offset
             */
            function getMentorSubscribed(studentId, limit, offset) {
            	StudentService.getInfoMentorSubscribed(studentId, limit, offset, offset == 0, $scope.query.trim()).then(function (data) {
                    if (data.data.status = "true" && data.data.request_data_result != StatusError.MSG_DATA_NOT_FOUND) {
                        var result = data.data.request_data_result;
                        $scope.isReadyLoadPointSubscribed = true;
                        $scope.listMentorSubs = $scope.isNextPage ? $scope.listMentorSubs.concat(result) : result;
                        // Total page
                        if(offset == 0) {
                        	totalPageStudentSubs = Math.ceil(data.data.count / limit);
                        }
                    } else {
                    	$scope.listMentorSubs = [];
                    }
                });
            }


            $scope.convertListSubsToString = function (defaultSubjectId) {
                var listSubject = getSubjectNameById(defaultSubjectId, subjects);
                if (listSubject != null && listSubject !== undefined) {
                    var listSubjectName = [];
                    var isMax = false;
                    var len = listSubject.length;
                    if (len > LIMIT_SUBJECT) {
                        len = LIMIT_SUBJECT;
                        isMax = true;
                    }
                    for (var j = 0; j < len; j++) {
                        listSubjectName.push(listSubject[j].name);
                    }
                    return isMax ? listSubjectName.join(", ") + '...' : listSubjectName.join(", ");
                }
            };


            $scope.hoverProfileMentor = function (mentorId) {
                $("#" + mentorId + " .mentors-img-hover").show();
            };

            $scope.unHoverProfileMentor = function (mentorId) {
                $("#" + mentorId + " .mentors-img-hover").hide();
            };


            $scope.hoverVideo = function (vid) {
                $("#"+vid+" .hover-video").show();
            };

            $scope.unHoverVideo = function (vid) {
                $("#"+vid+" .hover-video").hide();
            };


            $scope.hoverSubcribe = function (isSubs, userid) {
                if (isSubs != '1' || isEmpty(userid)) {
                    return;
                }
                $("#subscribers_" + userid).attr("data-icon", "M");

                $("#span_" + userid).text("unsubscribe");

            };
            $scope.unHoverSubcribe = function (isSubs, userid) {
                if (isSubs != '1' || isEmpty(userid)) {
                    return;
                }
                $("#subscribers_" + userid).attr("data-icon", "N");
                $("#span_" + userid).text("subscribe");

            };

            $scope.setSubscribeMentor = function (mentorId) {
                if (isEmpty(userId) || userId == -1) {
                    return;
                }
                VideoService.setSubscribeMentor(userId, mentorId + "").then(function (data) {
                    if (data.data.status == "true") {
                        if (data.data.request_data_type == "subs") {
                            $scope.isSubscribe = 1;
                            $("#subscribers_" + mentorId).attr("data-icon", "L");
                            $('#subscribers_' + mentorId).addClass('subcribed');
                        } else {
                            $scope.isSubscribe = 0;
                            $("#subscribers_" + mentorId).attr("data-icon", "N");
                            $('#subscribers_' + mentorId).addClass('subcribe');
                            $('#subscribers_' + mentorId).removeClass('unsubcrib');
                        }
                        for (var i = 0; i < $scope.listMentorSubscribed.length; i++) {
                            if (mentorId == $scope.listMentorSubscribed[i].userid) {
                                $scope.listMentorSubscribed[i].isSubs = $scope.isSubscribe;
                                $scope.listMentorSubscribed[i].numsub = ($scope.isSubscribe == 0)?$scope.listMentorSubscribed[i].numsub -1:$scope.listMentorSubscribed[i].numsub +1;
                            }
                        }


                    }
                });
            };


            $scope.onFileSelect = function ($files, errFiles) {
                $scope.errorMessage = "";
                var errFile = errFiles && errFiles[0];
                if (!isEmpty(errFile)) {
                    if (errFile.$error == "maxSize") {
                        $scope.errorMessage = 'File must not exceed 5 MB';
                        return;
                    }
                    $scope.errorMessage = 'File wrong format. Please select file image!';
                    return;
                }
                var fd = new FormData();
                if ($files != null) {
                    fd.append('uploadfile', $files[0]);
                    fd.append("userid", userId);
                    fd.append("imageUrl", localStorage.getItem('imageUrl'));
                    StudentService.uploadAvatar(fd).then(function (data) {
                        if (data.data.status == "true") {
                            $scope.mentorInfo.imageUrl = data.data.request_data_result;
                            setStorage('imageUrl', $scope.mentorInfo.imageUrl, 10);
                            $rootScope.imageUrl = data.data.request_data_result;
                            // window.location.href = '#mentor/mentorProfile';
                            // window.location.reload();
                        }
                        else {
                            $scope.errorMessage = "Can't not upload avatar";
                        }
                    });
                }

            };


            /**
             * Get master Subject with level = 0
             * If isForum = false it is subject else it is favourite
             */
            function putMasterSubjectSelected(subjects, subjectedId, isForum) {
                var tempSelected = [];
                var masterSubjects = [];
                if (subjects != null && subjects !== undefined) {
                    // Master subject
                    for (var i = 0; i < subjects.length; i++) {
                        if (subjects[i].level == '0' && subjects[i].isForum == isForum) {
                            tempSelected.push(subjects[i]);
                        }
                    }
                    // Subject was choosed
                    if (subjectedId !== null && subjectedId !== undefined) {
                        var arrSubjectSelected = subjectedId.split(',');
                        for (var i = 0; i < tempSelected.length; i++) {
                            var subject = tempSelected[i];
                            for (var j = 0; j < arrSubjectSelected.length; j++) {
                                if (subject.subjectId == arrSubjectSelected[j]) {
                                    subject.selected = "1";
                                    break;
                                } else {
                                    subject.selected = "0";
                                }
                            }
                            masterSubjects.push(subject);
                        }
                    } else { // No selected subject
                        for (var i = 0; i < tempSelected.length; i++) {
                            var subject = tempSelected[i];
                            subject.selected = "0";
                            masterSubjects.push(subject);
                        }
                    }
                }
                return masterSubjects;
            }

            /**
             * Next page
             */
            $scope.nextPageMentorSubs = function () {
            	if ($scope.currentPageStudentSubs +1 > totalPageStudentSubs) {
            		return;
            	} else {
            		getMentorSubscribed(studentId, $scope.defaultLimit, $scope.currentPageStudentSubs * $scope.defaultLimit);
            		$scope.currentPageStudentSubs ++;
            	}
            };
            
            /**
             * Previous page 
             */
            $scope.prevPageMentorSubs = function () {
            	if ($scope.currentPageStudentSubs == 1) {
            		return;
            	} else {
            		$scope.currentPageStudentSubs --;
            		getMentorSubscribed(studentId, $scope.defaultLimit, ($scope.currentPageStudentSubs-1) * $scope.defaultLimit);
            	}
            };

            /**
             * Search mentor subcribed 
             */
            $scope.searchNameMentor = function () {
            	$scope.currentPageStudentSubs = 1;
            	getMentorSubscribed(studentId, $scope.defaultLimit, 0);
            };
            
            
            /**
             * Search student subcribed 
             */
            $scope.searchNameStudent = function () {
            	$scope.isLoadMoreStudentSubs = false;
            	$scope.currentPageStudentSubs = 1;
            	getStudentSubscribed(userId, $scope.defaultLimit, 0);
            };
            
            $scope.disabledNextPrev = function () {
                if ($scope.offset == 0 && $scope.newLimit <= $scope.defaultLimit) {
                    return true;
                }
            };

            /**
             * goToProfileMentor 
             */
            $scope.goToProfileMentor = function (uid){
            	if(uid == userId) {
            		$location.path('/mentor/mentorProfile');
            	} else {
            		$location.path('/mentor/mentorProfile/'+uid);
            	}
            }
            
            /**
             * @param firstName
             * @param lastName
             * @returns {boolean} true or false
             */
            function isNameEmpty(firstName, lastName) {
                return !!(isEmpty(firstName) && isEmpty(lastName));
            }

            /**
             * @param userName
             * @returns {string} name of user after split @ character
             */
            function splitUserName(userName) {
                return userName.indexOf('@') > -1 ? capitaliseFirstLetter(userName.substr(0, userName.indexOf('@'))) : userName;
            }

            $scope.displayName = function (firstName, lastName, userName) {
                return displayUserName(firstName,lastName, userName).trim();
            };

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
            
            
            function setHeightBoxInfo(){
            	$scope.$on('getMentorProfileCompleted', function(){
            		 $timeout(function () {
            			 var height = $(".box-information").innerHeight();
            			 var width =  $window.innerWidth;
            			 if(width > 1199 && width < 1681){
            				 $(".box-avatar").css({"height": + height + "px"});
            			 }
            		 });
            	});
            }
            
        }]);
