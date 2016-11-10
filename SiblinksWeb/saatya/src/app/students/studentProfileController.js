brotControllers.controller('StudentProfileController',
    ['$scope', '$modal', '$routeParams', '$rootScope', '$http', '$location', 'StudentService', 'QuestionsService', 'MentorService', 'TeamMentorService', 'myCache', 'VideoService', 'HomeService', 'uploadEssayService',
        function ($scope, $modal, $routeParams, $rootScope, $http, $location, StudentService, QuestionsService, MentorService, TeamMentorService, myCache, VideoService, HomeService, uploadEssayService) {
            var userId = localStorage.getItem('userId');
            var userName = localStorage.getItem('userName');
            var userType = localStorage.getItem('userType');
            var subjects = JSON.parse(localStorage.getItem('subjects'));
            $scope.isLogged = userId !== undefined && userId != null;
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
            // Subject
            $scope.masterSubjects = [];

            // Student Profile
            $scope.studentInfo = {};

            $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

            $scope.schoolSelect = null;

            var defaultSubjectChecked = [];
            var defaultFavouriteChecked = [];
            var bod = "";

            $scope.currentTab = $location.search().tab;


            init();
            function init() {
                getMentorSubscribedInfo();
                if (mentorId != undefined) {
                    getVideosRecently();
                    isSubscribed();
                    getNewestAnswers(mentorId, 6, 0);
                    getInfoMentorProfile();
                }
                getStudentProfile();
                getEssayProfile();
                getMyQuestions(userId, limit, offset, "newest", "-1", "-1");
                uploadEssayService.collegesOrUniversities().then(function (data) {
                    if (data.data.status) {
                        $scope.listSchools = data.data.request_data_result;
                    }
                });
                if(mentorId === undefined){
                    showTabContent($scope.currentTab);
                }
            }


            function getMentorSubscribedInfo() {
                VideoService.getMentorSubscribe(userId, 5, 0).then(function (data) {
                    var data_result = data.data.request_data_result;
                    if (data_result) {
                        var listTopMentors = [];
                        for (var i = 0; i < data_result.length; i++) {
                            var mentor = {};
                            mentor.userid = data_result[i].userid;
                            mentor.fullName = data_result[i].name;
                            mentor.firstName = data_result[i].firstName;
                            mentor.lastName = data_result[i].lastName;
                            mentor.userName = data_result[i].userName;
                            mentor.imageUrl = data_result[i].imageUrl;
                            mentor.isOnline = data_result[i].isOnline;
                            mentor.defaultSubjectId = data_result[i].defaultSubjectId;
                            mentor.numsub = data_result[i].numSubs;
                            mentor.numAnswers = data_result[i].numAnswers;
                            mentor.accomplishments = data_result[i].accomplishments;
                            if (subjects != null || subjects !== undefined) {
                                if (data_result[i].defaultSubjectId !== null && data_result[i].defaultSubjectId !== undefined) {
                                    mentor.listSubject = getSubjectNameById(data_result[i].defaultSubjectId, subjects);
                                }
                            } else {
                                mentor.listSubject.push([{id: 1, subject: "None"}])
                            }
                            listTopMentors.push(mentor);
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
                        if (data.data.request_data_result) {
                            var result_data = data.data.request_data_result;
                            $scope.studentMentorProfile = result_data;
                            var gender = $scope.studentMentorProfile.gender;
                            $scope.GenderMentor = validateGender(gender);
                            var birthDay = calculateBirthDay(result_data.birthDay);
                            $scope.bod = birthDay;
                            $scope.isEmptyNameMentor = false;
                            if (isNameEmpty($scope.studentMentorProfile.firstname, $scope.studentMentorProfile.lastName)) {
                                $scope.isEmptyNameMentor = true;
                                $scope.studentMentorProfile.fullNameMentor = splitUserName($scope.studentMentorProfile.username);
                            }
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

            function isNameEmpty(firstName, lastName) {
                return !!(isEmpty(firstName) && isEmpty(lastName));
            }

            function splitUserName(userName) {
                return userName.indexOf('@') > -1 ? capitaliseFirstLetter(userName.substr(0, userName.indexOf('@'))) : userName;
            }

            $scope.hoverVideo = function (vid) {
                $("#" + vid + " .hover-video").show();
            };

            $scope.unHoverVideo = function (vid) {
                $("#" + vid + " .hover-video").hide();
            };

            function getVideosRecently() {
                VideoService.getVideoPlaylistRecently(mentorId, 6, 0).then(function (data) {
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
                            $scope.errorMessage = "";
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
                $scope.msgError = "";
                $scope.msgSuccess = "";

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
                    $scope.msgError = "New Password must difference Old Password.";
                } else if (newPwd !== confirmPwd) {
                    angular.element('#pass').trigger('focus');
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
                            $scope.msgSuccess = "Changed password successfully.";
                        } else {
                            // $scope.msgError = "Change password failure. Please try again !";
                            $scope.msgError = data.data.request_data_result;
                        }
                    });
                }
            };

            function resetFormPwd() {
                $('#password').val('');
                $('#pass').val('');
                $('#confirm').val('');
            }

            /**
             * Click button reset in the change password form
             */
            $scope.resetFormPwd = function () {
                resetFormPwd();
            };

            /**
             * Update profile information of user
             */
            $scope.updateProfile = function () {
                var check = true;
                var error = '';
                // Reset message
                $scope.msgError = "";
                $scope.msgSuccess = "";

                var favorite = "";
                var strSubs = "";
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

                if (!isEmpty(email)) {
                    if (!isValidEmailAddress($('input[name="email"]').val())) {
                        check = false;
                        error += "Email is not valid";
                    }
                } else {
                    email = "";
                }

                // Selected Subject
                var arrSubjectSelected = [];
                var subjectSelected = angular.element('.masterSubject:checked');
                for (var i = 0; i < subjectSelected.length; i++) {
                    arrSubjectSelected.push(subjectSelected[i].defaultValue);
                }

                // Selected Favourite
                var arrFavouriteSelected = [];
                var favSelected = angular.element('.masterFavourite:checked');
                for (var i = 0; i < favSelected.length; i++) {
                    arrFavouriteSelected.push(favSelected[i].defaultValue);
                    arrSubjectSelected.push(favSelected[i].defaultValue);
                }
                favorite = arrFavouriteSelected.join(',');
                strSubs = arrSubjectSelected.join(',');
                var school = $scope.schoolSelect != null && !isEmpty($scope.schoolSelect) ? $scope.schoolSelect.id : null;
                var firstName = $('input[name="firstname"]').val();
                var lastName = $('input[name="lastname"]').val();
                if((!isEmpty(firstName) && firstName.length > 50) || (!isEmpty(lastName) && lastName.length > 50)){
                    check = false;
                    error += "First name or last name must not exceed 50 characters,";
                }
                var bio = $('#about').val();
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
                if (check) {
                    var student = {
                        'role': "S",
                        'userid': userId,
                        'firstName': $('#firstName').val(),
                        'lastName': $('#lastName').val(),
                        'email': $('#email').val(),
                        'gender': gender,
                        'school': school,
                        'bod': $('#bod').val(),
                        'bio': bio,
                        'favorite': favorite,
                        'defaultSubjectId': strSubs
                    };
                    $rootScope.$broadcast('open');
                    StudentService.updateUserProfile(student).then(function (data) {
                        $rootScope.$broadcast('close');
                        if (data.data.request_data_result == "Success") {
                            if (student) {
                                if ($scope.isEmptyName) {
                                    if (!isEmpty(firstName) || !isEmpty(firstName)) {
                                        $scope.studentInfo.fullName = "";
                                        $scope.isEmptyName = false;
                                    }
                                }
                                $scope.studentInfo.firstname = student.firstName;
                                $scope.studentInfo.lastName = student.lastName;
                                $scope.gender = student.gender;
                                $scope.birthDay = student.bod;
                                $scope.studentInfo.favorite = student.favorite;
                                $scope.studentInfo.school = school;
                                $scope.studentInfo.email = student.email;
                                $scope.studentInfo.bio = student.bio;
                                $scope.studentInfo.schoolName = $scope.schoolSelect != null ? $scope.schoolSelect.name : null;
                                localStorage.setItem('defaultSubjectId', strSubs);
                                localStorage.setItem('firstName', student.firstName);
                                localStorage.setItem('lastname', student.lastName);
                                localStorage.setItem('school', $scope.schoolSelect.id);
                                if (subjects != null || subjects !== undefined) {
                                    $scope.objSubs = getSubjectNameById(strSubs, subjects);
                                }
                                updateDefaultFavsSubs();
                            }
                            $scope.msgSuccess = "Update Profile Successful !";
                        } else {
                            if (error != '') {
                                $scope.msgError = error.substr(0, error.lastIndexOf(','));
                            } else {
                                $scope.msgError = "Failed to update your profile";
                            }
                        }
                    });
                } else {
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
             * function Change Tab setting profile
             */
            $scope.changeTab = function () {
                // Clear message
                $scope.msgError = "";
                $scope.msgSuccess = "";
                // Clear input change password
                $('#password').val("");
                $('#pass').val("");
                $('#confirm').val("");
            };

            /**
             * function click button reset in the setting profile form
             */
            $scope.resetProfile = function () {
                if ($scope.studentInfo) {
                    angular.element('#firstName').val($scope.studentInfo.firstname);
                    angular.element('#lastName').val($scope.studentInfo.lastName);
                    angular.element('#email').val($scope.studentInfo.email);
                    $scope.schoolSelect = $scope.studentInfo.school != null ? {id: parseInt($scope.studentInfo.school, 10)} : null;
                    angular.element('#bod').val(bod);
                    angular.element('#about').val($scope.studentInfo.bio);
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
                    switch ($scope.studentInfo.gender) {
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
                            objPosted.userName = questionData.userName;
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
                                        if (isNameEmpty(answer_result[y].firstName, answer_result[y].lastName)) {
                                            objAnswer.name = splitUserName(answer_result[y].userName);
                                        } else {
                                            objAnswer.name = answer_result[y].firstName.trim() + " " + answer_result[y].lastName.trim();
                                        }
                                        objAnswer.firstName = answer_result[y].firstName;
                                        objAnswer.lastName = answer_result[y].lastName;
                                        objAnswer.userName = answer_result[y].userName;
                                        objAnswer.content = answer_result[y].content;
                                        objAnswer.avatar = answer_result[y].imageUrl;
                                        objAnswer.countLike = answer_result[y].countLike;
                                        var imageAnswers = answer_result[y].imageAnswer;
                                        if (imageAnswers != null && imageAnswers !== undefined && imageAnswers != '') {
                                            var arrImage = imageAnswers.split(';');
                                            objAnswer.images = arrImage;
                                        }
                                        if (answer_result[y].likeAnswer == null || answer_result[y].likeAnswer === "N") {
                                            objAnswer.like = false;
                                        } else {
                                            objAnswer.like = true;
                                        }
                                        objAnswer.time = convertUnixTimeToTime(answer_result[y].TIMESTAMP);
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
                        $scope.studentInfo.count_essay = $scope.studentInfo.count_essay != null && $scope.studentInfo.count_essay != "" ? $scope.studentInfo.count_essay : 0;
                        $scope.studentInfo.count_subscribe = $scope.studentInfo.count_subscribe != null && $scope.studentInfo.count_subscribe != "" ? $scope.studentInfo.count_subscribe : 0;
                        if ($scope.studentInfo.school != null && !isEmpty($scope.studentInfo.school)) {
                            $scope.schoolSelect = {id: parseInt($scope.studentInfo.school, 10)};
                        }
                        $scope.isEmptyName = false;
                        if (isNameEmpty($scope.studentInfo.firstname, $scope.studentInfo.lastName)) {
                            $scope.isEmptyName = true;
                            $scope.studentInfo.fullName = splitUserName($scope.studentInfo.username);
                        }
                        $scope.studentInfo.imageUrl = $scope.studentInfo.imageUrl != null ? $scope.studentInfo.imageUrl : "assets/images/noavartar.jpg";
                        $scope.birthDay = timeConverter($scope.studentInfo.birthDay, FormatDateTimeType.DD_MM_YY);
                        $scope.sinceDay = timeConverter($scope.studentInfo.registrationTime, FormatDateTimeType.MM_YY);
                        $scope.isLoginViaFBOrGoogle = $scope.studentInfo.idFacebook != null || $scope.studentInfo.idGoogle != null;
                    }
                    //displayInformation();

                    // This call method to return $scope.masterSubjects selected
                    $scope.masterSubjects = putMasterSubjectSelected(subjects, $scope.studentInfo.defaultSubjectId, false);
                    $scope.masterFavourite = putMasterSubjectSelected(subjects, $scope.studentInfo.favorite, true);
                    defaultSubjectChecked = $scope.masterSubjects;
                    defaultFavouriteChecked = $scope.masterFavourite;
                    bod = $scope.birthDay;
                });

            }

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
             * Show hide question
             */
            $scope.showAnswerQuestion = function showAnswerQuestion(id) {
                if (angular.element("#" + id).hasClass('hidden')) {
                    angular.element("#" + id).removeClass('hidden');
                } else {
                    angular.element("#" + id).addClass('hidden');
                }
            }

            $scope.displayUserName = function (firstName, lastName, userName) {
                return displayUserName(firstName, lastName, userName);
            };

            /**
             * event change tab
             */
            $scope.changeTabMenu = function (tabName) {
                showTabContent(tabName);
            };


            function showTabContent(tabName) {
                if(isEmpty(tabName)){
                    $scope.currentTab = 'question';
                    $location.search('tab', 'question');
                    return;
                }
                $scope.currentTab = tabName;
                $location.search('tab', tabName);
            }


            $scope.setSubscribeMentor = function (mentorId) {
                if(isEmpty(userId)||userId == -1){
                    return ;
                }
                VideoService.setSubscribeMentor(userId, mentorId+"").then(function (data) {
                    if(data.data.status =="true") {
                        if (data.data.request_data_type == "subs") {
                            $scope.isSubscribe = true;
                        }
                        else {
                            $scope.isSubscribe = false;
                            $("#span_"+mentorId).text('Subscribe');
                            $("#subscribers_"+mentorId).attr("data-icon","N");
                            $('#subscribers_'+mentorId).removeClass('unsubcrib');
                        }
                    }
                });
            };

            $scope.hoverSubscribe = function (isSub) {
                if(!isSub){
                    return;
                }
                angular.element('.subscribers').attr('data-icon', 'M');
                angular.element('#spansubs').text('Unsubscribe');
            };

            $scope.unHoverSubscribe = function (isSub) {
                if(!isSub){
                    return;
                }
                angular.element('.subscribers').attr('data-icon', 'N');
                angular.element('#spansubs').text('Subscribed');
            };


        }]);