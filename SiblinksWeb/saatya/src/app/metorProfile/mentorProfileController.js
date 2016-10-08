brotControllers.controller('MentorProfileController',
    ['$sce', '$scope', '$modal', '$routeParams', '$rootScope', '$http', '$location', 'MentorService', 'TeamMentorService', 'VideoService', 'StudentService', 'myCache',
        function ($sce, $scope, $modal, $routeParams, $rootScope, $http, $location, MentorService, TeamMentorService, VideoService, StudentService, myCache) {

            var userId = localStorage.getItem('userId');
            var userType = localStorage.getItem('userType');
            var userName = localStorage.getItem('userName');

            var isInit = true;
            $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';
            // $scope.subscribeText = 'subscribed';
            // $scope.icon = 'N';

            var studentId = $routeParams.studentId;

            $scope.EMPTY_DATA = StatusError.MSG_UNKNOWN;

            $scope.isSubscribe = 0;

            $scope.isLoginViaFBOrGoogle = false;
            $scope.isHideMessage = true;

            init();


            function init() {
                if (studentId != undefined) {
                    getStudentInfo();
                    getMentorSubscribed(50, 0, "subscribe", studentId);
                }
                getStudentSubscribed();
                getMentorProfile();
            }

            // function checkStudentSubscribe(){
            //   if (userType == 'S') {
            //     MentorService.checkStudentSubscribe(mentorId, userId).then(function(data){
            //       $scope.subscribeInfo = data.data.request_data_result;
            //     });
            //   }
            // }


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
                        var subjects = myCache.get("subjects");
                        $scope.mentorInfo = dataResponse.data.request_data_result;
                        $scope.mentorInfo.imageUrl = $scope.mentorInfo.imageUrl != null ? $scope.mentorInfo.imageUrl : "assets/images/noavartar.jpg";
                        var gender = $scope.mentorInfo.gender;
                        $scope.gender = validateGender(gender);
                        var bioTimeStamp = $scope.mentorInfo.birthDay;
                        var registrationTime = $scope.mentorInfo.registrationTime;
                        $scope.birthDay = timeConverter(bioTimeStamp, FormatDateTimeType.DD_MM_YY);
                        $scope.sinceDay = timeConverter(registrationTime, FormatDateTimeType.MM_YY);
                        $scope.isLoginViaFBOrGoogle = $scope.mentorInfo.idFacebook != null || $scope.mentorInfo.idGoogle != null;
                        if (subjects) {
                            if (!isEmpty($scope.mentorInfo.defaultSubjectId) && $scope.mentorInfo.defaultSubjectId != null) {
                                var subsName = getSubjectNameById($scope.mentorInfo.defaultSubjectId, subjects);
                                if (subsName != undefined) {
                                    // $scope.objSubs = subsName;
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
                            }
                        } else {
                            $scope.subjects = "None";
                        }
                    }
                    displayInformation();
                });

            }

            function displayInformation() {
                $('input[name="firstname"]').val($scope.mentorInfo.firstname);
                $('input[name="lastname"]').val($scope.mentorInfo.lastName);
                $('input[name="email"]').val($scope.mentorInfo.email);
                $('input[id="bod"]').val($scope.birthDay);
                $('textarea[name="aboutme"]').val($scope.mentorInfo.bio);
                $('input[name="school"]').val($scope.mentorInfo.accomplishments);
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

                if ($scope.mentorInfo.favorite != null && $scope.mentorInfo.favorite !== undefined) {
                    var favorite = $scope.mentorInfo.favorite.split(',');
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
            }

            // function getNewestAnswer() {
            //     MentorService.getNewestAnswer(userId).then(function (data) {
            //         $scope.answers = formatData(data.data.request_data_result);
            //     });
            // }

            // function getVideosRecently() {
            //     VideoService.getVideosRecently(userId).then(function (data) {
            //         $scope.videos = data.data.request_data_result;
            //     });
            // }

            // function formatData(data) {
            //     for (var i = 0; i < data.length; i++) {
            //         data[i].numLike = data[i].numLike == null ? 0 : data[i].numLike;
            //         data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
            //     }
            //     return data;
            // }

            function getStudentSubscribed() {
                MentorService.getStudentSubscribed(userId, 6, 0).then(function (data) {
                    if (data.data.status) {
                        var response = data.data.request_data_result;
                        if (response && response != "Found no data") {
                            var students = [];
                            for (var i = 0; i < response.length; i++) {
                                var obj = {};
                                obj.userId = response[i].userid;
                                obj.lastName = (response[i].lastName == null || response[i].lastName === undefined) ? "" : response[i].lastName;
                                obj.firstName = (response[i].firstName == null || response[i].firstName === undefined) ? "" : response[i].firstName;
                                obj.userName = response[i].userName != null ? response[i].userName : ' ';
                                obj.avatar = response[i].imageUrl;
                                obj.defaultSubjectId = response[i].defaultSubjectId;
                                obj.school = response[i].school;
                                students.push(obj);
                            }
                            $scope.listStudentSubscribed = students;
                        }
                    }
                });
            }

            $scope.updateProfile = function () {
                var check = true;
                var error = '';

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
                if ($('input[name="gender"][value="male"]').is(':checked')) {
                    gender = "M";
                }
                else if ($('input[name="gender"][value="female"]').is(':checked')) {
                    gender = "F";
                }
                else {
                    gender = "O";
                }

                if (!isValidEmailAddress($('input[name="email"]').val())) {
                    check = false;
                    error += "Email is not valid";
                }

                var selected = [];
                var objSelected = $('.subject-field input:checked');
                for (var i = 0; i < objSelected.length; i++) {
                    selected.push(objSelected[i].defaultValue);
                }
                var strSubs = selected.join(',');

                if (check) {
                    var mentor = {
                        'userid': userId,
                        'firstName': $('input[name="firstname"]').val(),
                        'lastName': $('input[name="lastname"]').val(),
                        'email': $('input[name="email"]').val(),
                        'gender': gender,
                        'school': $('input[name="school"]').val(),
                        'bod': $('input[id="bod"]').val(),
                        'bio': $('textarea[name="aboutme"]').val(),
                        'favorite': favorite,
                        'defaultSubjectId': strSubs
                    };
                    StudentService.updateUserProfile(mentor).then(function (data) {
                        if (data.data.request_data_result == "Success") {
                            if (mentor) {
                                $scope.mentorInfo.firstname = mentor.firstName;
                                $scope.mentorInfo.lastName = mentor.lastName;
                                $scope.gender = validateGender(mentor.gender);
                                $scope.mentorInfo.favorite = mentor.favorite;
                                $scope.mentorInfo.accomplishments = mentor.school;
                                $scope.mentorInfo.email = mentor.email;
                                $scope.mentorInfo.bio = mentor.bio;
                                $scope.birthDay = mentor.bod;
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
                        $scope.isHideMessage = false;
                    });
                }
                else {
                    console.log(error);
                }
            };

            $scope.changeTab = function () {
                $scope.isHideMessage = true;
            };


            $scope.reset = function () {
                displayInformation();
            };

            $scope.resetFormPwd = function () {
                resetFormPwd();
            };

            function resetFormPwd() {
                $('#password').val('');
                $('#pass').val('');
                $('#confirm').val('');

            }

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
                        $scope.isHideMessage = false;
                    });
                }
            };


            // $scope.subcribeMentor = function () {
            //     VideoService.setSubscribeMentor(userId, mentorId).then(function (data) {
            //         console.log(data);
            //         if (data.data.request_data_type == "subs") {
            //             // change icon to unsubscribe
            //             $scope.mentor.count_subscribers += 1;
            //             $scope.mentor.subscribed = true;
            //         }
            //         else if (data.data.request_data_type == "unsubs") {
            //             // change icon to subscribe
            //             $scope.mentor.count_subscribers -= 1;
            //             $scope.mentor.subscribed = false;
            //         }
            //     });
            // }

            // $scope.hoverOut = function () {
            //     $scope.subscribeText = 'subscribed';
            //     $scope.icon = "N";
            // }
            // $scope.hoverIn = function () {
            //     $scope.subscribeText = 'unsubscribe';
            //     $scope.icon = "K";
            // }


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
                        var subjects = myCache.get("subjects");
                        $scope.studentInfo = dataResponse.data.request_data_result;
                        var gender = $scope.studentInfo.gender;
                        $scope.gender = validateGender(gender);
                        var bioTimeStamp = $scope.studentInfo.birthDay;
                        var registrationTime = $scope.studentInfo.registrationTime;
                        $scope.birthDay = timeConverter(bioTimeStamp, FormatDateTimeType.DD_MM_YY);
                        $scope.sinceDay = timeConverter(registrationTime, FormatDateTimeType.MM_YY);
                        if (subjects) {
                            var subsName = getSubjectNameById($scope.studentInfo.defaultSubjectId, subjects);
                            if (subsName.length != 0) {
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
            function getMentorSubscribed(limit, offset, type, studentId) {
                if (studentId) {
                    TeamMentorService.getTopMentorsByType(limit, offset, type, studentId).then(function (data) {
                        var listMentorSubscribed = [];
                        var strSubject;
                        var subjects = myCache.get("subjects");
                        if (data.data.status) {
                            $scope.countAll = data.data.request_data_result.length;

                            if ($scope.countAll == null) {
                                $scope.errorData = DATA_ERROR.noDataFound;
                            }
                            else {
                                var data_result = data.data.request_data_result;
                                for (var i = 0; i < data_result.length; i++) {
                                    var mentor = {};
                                    if (data_result[i].isSubs == 1) {
                                        mentor.userid = data_result[i].userid;
                                        mentor.lastName = (data_result[i].lastName == null || data_result[i].lastName === undefined) ? "" : data_result[i].lastName;
                                        mentor.firstName = (data_result[i].firstName == null || data_result[i].firstName === undefined) ? "" : data_result[i].firstName;
                                        mentor.accomplishments = (data_result[i].accomplishments == null || data_result[i].accomplishments === undefined) ? "" : data_result[i].accomplishments;
                                        mentor.bio = data_result[i].bio;
                                        mentor.numsub = data_result[i].numsub;
                                        mentor.imageUrl = data_result[i].imageUrl;
                                        mentor.numlike = data_result[i].numlike;
                                        mentor.numvideos = data_result[i].numvideos;
                                        mentor.numAnswers = data_result[i].numAnswers;
                                        mentor.avgrate = data_result[i].avgrate != null ? data_result[i].avgrate : 0;
                                        mentor.isSubs = data_result[i].isSubs;
                                        mentor.defaultSubjectId = data_result[i].defaultSubjectId;
                                        var listSubject = getSubjectNameById(data_result[i].defaultSubjectId, subjects);
                                        if (listSubject != null && listSubject !== undefined) {
                                            var listSubjectName = [];
                                            for (var j = 0; j < listSubject.length; j++) {
                                                listSubjectName.push(listSubject[j].name);
                                            }
                                            strSubject = listSubjectName.join(", ");
                                        }
                                        mentor.listSubject = strSubject;
                                        listMentorSubscribed.push(mentor);
                                    }
                                }
                                $scope.isReadyLoadPointSubscribed = true;
                                $scope.listMentorSubs = listMentorSubscribed;
                            }
                        }
                    });
                }
            }

            function getMentorSubscribedByCurrentId(userId, studentId, limit, offset) {
                if (userId && studentId) {
                    MentorService.getSubscribedMentorViewStudent(userId, studentId, limit, offset).then(function (response) {
                            if (response.data.request_data_result == StatusError.MSG_DATA_NOT_FOUND) {
                                $scope.listMentorSubs = null;
                                return;
                            }
                            var data_result = response.data.request_data_result;
                            var listMentorSubscribed = [];
                            var subjects = myCache.get("subjects");
                            for (var i = 0; i < data_result.length; i++) {
                                var mentor = {};
                                mentor.userid = data_result[i].userid;
                                mentor.lastName = (data_result[i].lastName == null || data_result[i].lastName === undefined) ? "" : data_result[i].lastName;
                                mentor.firstName = (data_result[i].firstName == null || data_result[i].firstName === undefined) ? "" : data_result[i].firstName;
                                mentor.accomplishments = (data_result[i].accomplishments == null || data_result[i].accomplishments === undefined) ? "" : data_result[i].accomplishments;
                                mentor.bio = data_result[i].bio;
                                mentor.numsub = data_result[i].numsub;
                                mentor.imageUrl = data_result[i].imageUrl;
                                mentor.numlike = data_result[i].numlike;
                                mentor.numvideos = data_result[i].numvideos;
                                mentor.numAnswers = data_result[i].numAnswers;
                                mentor.points = data_result[i].avgrate != null ? data_result[i].avgrate : 0;
                                mentor.school = data_result[i].accomplishments;
                                mentor.isSubs = data_result[i].isSubs;
                                mentor.defaultSubjectId = data_result[i].defaultSubjectId;
                                var listSubject = getSubjectNameById(data_result[i].defaultSubjectId, subjects);
                                var strSubject = "";
                                if (listSubject != null && listSubject !== undefined) {
                                    var listSubjectName = [];
                                    for (var j = 0; j < listSubject.length; j++) {
                                        listSubjectName.push(listSubject[j].name);
                                    }
                                    strSubject = listSubjectName.join(", ");
                                }
                                mentor.listSubject = strSubject;
                                listMentorSubscribed.push(mentor);
                            }
                            $scope.isReadyLoadPointSubscribed = true;
                            $scope.listMentorSubs = listMentorSubscribed;
                        }
                    );
                }
            }

            $scope.hoverProfileMentor = function () {
                $(".mentors-infor-img .mentors-img-hover").show();
            };

            $scope.unHoverProfileMentor = function () {
                $(".mentors-infor-img .mentors-img-hover").hide();
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


            $scope.onFileSelect = function ($files) {
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


        }]);
