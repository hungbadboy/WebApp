height();

  function height(){
        var width_win = $(window).width();
        var heightInfo = $(".top-mentors-info-detail").height() - 30;
        if (width_win < 1601) {
            $(".center-content.mentor-view-mentor .content .mentors-profile-content").css({"margin-top": + heightInfo + "px"});
         }
        else {
            $(".center-content.mentor-view-mentor .content .mentors-profile-content").css({"margin-top": "30px"});
        } 
  }

brotControllers.filter('filterSub', function () {
    return function (items, name) {
        var filtered = [];
        var fullName = "";
        var nameSearch = name;
        if (!isEmpty(name)) {
            nameSearch = name.toLowerCase();
        }
        angular.forEach(items, function (el) {
            fullName = checkNameToTrim(el.firstName) + ' ' + checkNameToTrim(el.lastName);
            if ((!isEmpty(fullName) && fullName.toLowerCase().indexOf(nameSearch) > -1)
                || isEmpty(name)) {
                filtered.push(el);
            }
        });
        return filtered;
    }
});
brotControllers.controller('MentorProfileController',
    ['$sce', '$scope', '$modal', '$routeParams', '$rootScope', '$http', '$location', 'MentorService', 'TeamMentorService', 'VideoService', 'StudentService', 'myCache', 'uploadEssayService',
        function ($sce, $scope, $modal, $routeParams, $rootScope, $http, $location, MentorService, TeamMentorService, VideoService, StudentService, myCache, uploadEssayService) {

            var userId = localStorage.getItem('userId');
            var userType = localStorage.getItem('userType');
            var userName = localStorage.getItem('userName');

            $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

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
            var bod = "";
            var currentPageStudentSubs = 0;
            $scope.isLoadMoreStudentSubs = false;

            $scope.defaultLimit = 6;
            var hasLoadMore = false;
            $scope.listMentorSubsSize = 0;
            $scope.disableNext = true;
            var LIMIT_SUBJECT = 3;
            init();

            function init() {
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
                    if (data.data.status == "true") {
                        if (data.data.request_data_result) {
                            var result_data = data.data.request_data_result;
                            $scope.mentorMentorProfile = result_data;
                            var gender = $scope.mentorMentorProfile.gender;
                            $scope.GenderMentor = validateGender(gender);
                            var birthDay = calculateBirthDay(result_data.birthDay);
                            $scope.bod = birthDay;
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
                        var bioTimeStamp = $scope.mentorInfo.birthDay;
                        var registrationTime = $scope.mentorInfo.registrationTime;
                        $scope.birthDay = timeConverter(bioTimeStamp, FormatDateTimeType.DD_MM_YY);
                        $scope.sinceDay = timeConverter(registrationTime, FormatDateTimeType.MM_YY);
                        $scope.isLoginViaFBOrGoogle = $scope.mentorInfo.idFacebook != null || $scope.mentorInfo.idGoogle != null;
                        $scope.isEmptyName = false;
                        if (isNameEmpty($scope.mentorInfo.firstname, $scope.mentorInfo.lastName)) {
                            $scope.isEmptyName = true;
                            $scope.mentorInfo.fullName = splitUserName($scope.mentorInfo.username);
                        }
                        $scope.masterSubjects = putMasterSubjectSelected(subjects, $scope.mentorInfo.defaultSubjectId, false);
                        $scope.masterFavourite = putMasterSubjectSelected(subjects, $scope.mentorInfo.favorite, true);
                        defaultSubjectChecked = $scope.masterSubjects;
                        defaultFavouriteChecked = $scope.masterFavourite;
                        bod = $scope.birthDay;
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
                $('input[name="firstname"]').val($scope.mentorInfo.firstname);
                $('input[name="lastname"]').val($scope.mentorInfo.lastName);
                $('input[name="email"]').val($scope.mentorInfo.email);
                $('input[id="bod"]').val($scope.birthDay);
                $('textarea[name="aboutme"]').val($scope.mentorInfo.bio);
                $('input[name="accomplishments"]').val($scope.mentorInfo.accomplishments);
                if ($scope.mentorInfo.gender) {
                    switch ($scope.mentorInfo.gender) {
                        case "M":
                            $('input[name="gender"][value="male"]').prop('checked', true);
                            break;
                        case "F":
                            $('input[name="gender"][value="female"]').prop('checked', true);
                            break;
                        case "O":
                            $('input[name="gender"][value="other"]').prop('checked', true);
                            break;
                    }
                }
            }


            function getStudentSubscribed(userId, defaultLimit, offset) {
                MentorService.getStudentSubscribed(userId, defaultLimit, offset).then(function (data) {
                    if (data.data.status) {
                        var response = data.data.request_data_result;
                        if (response && response != "Found no data") {
                            var students = [];
                            for (var i = 0; i < response.length; i++) {
                                var obj = {};
                                obj.userId = response[i].userid;
                                obj.userName = displayUserName(response[i].firstName, response[i].lastName, response[i].userName);
                                obj.avatar = response[i].imageUrl;
                                obj.defaultSubjectId = response[i].defaultSubjectId;
                                obj.schoolId = response[i].school;
                                obj.schoolName = response[i].schoolName;
                                students.push(obj);
                            }
                            $scope.listStudentSubscribed = hasLoadMore ? $scope.listStudentSubscribed.concat(students) : students;
                            $scope.totalStudentSubs = $scope.listStudentSubscribed.length;
                            $scope.isLoadMoreStudentSubs = $scope.listStudentSubscribed.length < defaultLimit;
                        }
                    }
                });
            }

            $scope.updateProfile = function () {
                var check = true;
                var error = "";
                $scope.msgError = "";
                $scope.msgSuccess = "";
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
                if (check) {
                    var mentor = {
                        'role': "M",
                        'userid': userId,
                        'firstName': firstName,
                        'lastName': lastName,
                        'email': email,
                        'gender': gender,
                        'accomplishments': $('input[name="accomplishments"]').val(),
                        'school': school,
                        'bod': $('input[id="bod"]').val(),
                        'bio': bio,
                        'favorite': favorite,
                        'defaultSubjectId': strSubs
                    };
                    $rootScope.$broadcast('open');
                    StudentService.updateUserProfile(mentor).then(function (data) {
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
                                $scope.birthDay = mentor.bod;
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
                        $rootScope.$broadcast('close');
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
                    angular.element('#bod').val(bod);
                    angular.element('#about').val($scope.mentorInfo.bio);
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
                        var bioTimeStamp = $scope.studentInfo.birthDay;
                        var registrationTime = $scope.studentInfo.registrationTime;
                        $scope.isStudentEmptyName = false;
                        if (isNameEmpty($scope.studentInfo.firstname, $scope.studentInfo.lastName)) {
                            $scope.isStudentEmptyName = true;
                            $scope.studentInfo.fullName = splitUserName($scope.studentInfo.username);
                        }
                        $scope.birthDay = timeConverter(bioTimeStamp, FormatDateTimeType.DD_MM_YY);
                        $scope.sinceDay = timeConverter(registrationTime, FormatDateTimeType.MM_YY);
                        if (subjects) {
                            var subsName = getSubjectNameById($scope.studentInfo.defaultSubjectId, subjects);
                            if (subsName != undefined && !subsName) {
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
            function getMentorSubscribed(studentId, limit, offset) {
                StudentService.getInfoMentorSubscribed(studentId, limit, offset).then(function (data) {
                    if (data.data.status = "true" && data.data.request_data_result != StatusError.MSG_DATA_NOT_FOUND) {
                        var result = data.data.request_data_result;
                        $scope.isReadyLoadPointSubscribed = true;
                        $scope.listMentorSubs = $scope.isNextPage ? $scope.listMentorSubs.concat(result) : result;
                        $scope.listMentorSubsSize = $scope.listMentorSubs.length;
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

                            //$('#subscribers_'+mentorId).addClass('unsubcrib');
                        }
                        else {
                            $scope.isSubscribe = 0;
                            $("#subscribers_" + mentorId).attr("data-icon", "N");
                            $('#subscribers_' + mentorId).removeClass('unsubcrib');
                        }
                        for (var i = 0; i < $scope.listMentorSubscribed.length; i++) {
                            if (mentorId == $scope.listMentorSubscribed[i].userid) {
                                $scope.listMentorSubscribed[i].isSubs = $scope.isSubscribe;
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


            $scope.loadMoreStudentSubscribed = function () {
                hasLoadMore = true;
                currentPageStudentSubs++;
                if ($scope.isLoadMoreStudentSubs)
                    return;
                var newoffset = defaultLimit * currentPageStudentSubs;
                if (newoffset > $scope.totalStudentSubs) {
                    $scope.totalStudentSubs = newoffset;
                } else {
                    getStudentSubscribed(userId, $scope.defaultLimit, newoffset);
                }
            };

            var currentMentorSubPage = 0;
            $scope.isNextPage = false;
            $scope.offset = 0;
            $scope.newLimit = $scope.defaultLimit;
            var newOffset = 0;
            $scope.nextPageMentorSubs = function () {
                currentMentorSubPage++;
                newOffset = $scope.defaultLimit * currentMentorSubPage;
                if (newOffset > $scope.listMentorSubsSize) {
                    return;
                }
                $scope.isNextPage = true;
                $scope.offset = newOffset;
                $scope.newLimit = newOffset + $scope.defaultLimit;
                getMentorSubscribed(studentId, $scope.defaultLimit, newOffset);
            };

            $scope.prevPageMentorSubs = function () {
                currentMentorSubPage--;
                if (currentMentorSubPage < 0) {
                    return;
                }
                $scope.listMentorSubs.splice(newOffset, $scope.newLimit);
                $scope.offset = $scope.listMentorSubs.length - $scope.defaultLimit;
                newOffset = $scope.offset;
                $scope.newLimit = $scope.newLimit - $scope.defaultLimit;
                $scope.listMentorSubsSize = $scope.listMentorSubs.length;
            };

            $scope.disabledNextPrev = function () {
                if ($scope.offset == 0 && $scope.newLimit <= $scope.defaultLimit) {
                    return true;
                }
            };

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

        }]);
