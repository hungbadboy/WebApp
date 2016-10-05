brotControllers.controller('MentorProfileController',
    ['$sce', '$scope', '$modal', '$routeParams', '$http', '$location', 'MentorService', 'TeamMentorService', 'VideoService', 'StudentService', 'myCache',
        function ($sce, $scope, $modal, $routeParams, $http, $location, MentorService, TeamMentorService, VideoService, StudentService, myCache) {

            var userId = localStorage.getItem('userId');
            var userType = localStorage.getItem('userType');

            var isInit = true;
            $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';
            // $scope.subscribeText = 'subscribed';
            // $scope.icon = 'N';

            var studentId = $routeParams.studentId;

            $scope.EMPTY_DATA = StatusError.MSG_UNKNOWN;

            $scope.isSubscribe = 0;

            init();


            function init() {
                // checkStudentSubscribe();
                // getMentorProfile();
                getStudentInfo();
                getMentorSubscribed(50, 0, "subscribe", studentId);
                // getMentorSubscribedByCurrentId(userId, studentId, 50, 0);
                // getNewestAnswer();
                // getVideosRecently();
            }

            // function checkStudentSubscribe(){
            //   if (userType == 'S') {
            //     MentorService.checkStudentSubscribe(mentorId, userId).then(function(data){
            //       $scope.subscribeInfo = data.data.request_data_result;
            //     });
            //   }
            // }


            function getMentorProfile() {
                StudentService.getUserProfile(userId).then(function (data) {
                    if (data.data.request_data_result && data.data.request_data_result != "Found no data") {
                        $scope.mentor = data.data.request_data_result;

                        if ($scope.mentor.imageUrl == null || $scope.mentor.imageUrl.length == 0) {
                            $scope.mentor.imageUrl = 'http://www.capheseo.com/Content/T000/Images/no-avatar.png';
                        } else {
                            $scope.mentor.imageUrl = $scope.mentor.imageUrl.indexOf('http') == -1 ? $scope.baseIMAGEQ + $scope.mentor.imageUrl : $scope.mentor.imageUrl;
                        }

                        var firstname = $scope.mentor.firstname != null ? $scope.mentor.firstname : '';
                        var lastname = $scope.mentor.lastName != null ? $scope.mentor.lastName : '';
                        $scope.mentor.fullName = firstname + ' ' + lastname;
                        // $scope.mentor.bio = moment($scope.mentor.bio, "MM/DD/YYYY").month(0).from(moment().month(0)).replace('years ago', '');
                        $scope.mentor.bio = $scope.mentor.bio != null ? moment($scope.mentor.bio).format("DD MMM, YYYY") : null;
                        $scope.mentor.registrationTime = moment($scope.mentor.registrationTime).format("MMM, YYYY");
                        if ($scope.mentor.skills != null) {
                            $scope.mentor.skills = $scope.mentor.skills.replace(/\"/g, '');
                        }
                        $scope.mentor.averageRating = $scope.mentor.averageRating == null ? 0 : $scope.mentor.averageRating;
                        $scope.mentor.numRatings = $scope.mentor.numRatings == null ? 0 : $scope.mentor.numRatings;
                        $scope.mentor.numViews = $scope.mentor.numViews == null ? 0 : $scope.mentor.numViews;

                        displayInformation($scope.mentor);
                    }
                });
            }

            function displayInformation(mentor) {
                $('#txtFirstName').val(mentor.firstname);
                $('#txtLastName').val(mentor.lastName);
                $('#txtEmail').val(mentor.email);
                $('#txtLastName').val(mentor.lastName);
                $('#txtBIO').val(mentor.bio);
                $('#txtDescription').val(mentor.description);

                if (mentor.gender == "M") {
                    $('#male').prop('checked', true);
                } else if (mentor.gender == "F") {
                    $('#female').prop('checked', true);
                } else {
                    $('#other').prop('checked', true);
                }

                if (mentor.favorite != null && mentor.favorite !== undefined) {
                    var favorite = mentor.favorite.split(',');
                    for (var i = 0; i < favorite.length; i++) {
                        if (favorite[i] == 'Music') {
                            $('#music').prop('checked', true);
                        }
                        if (favorite[i] == 'Art') {
                            $('#art').prop('checked', true);
                        }
                        if (favorite[i] == 'Sport') {
                            $('#sport').prop('checked', true);
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

            // MentorService.getStudentSubscribed(userId, 6, 0).then(function (data) {
            //     if (data.data.status) {
            //         var response = data.data.request_data_result;
            //         if (response && response != "Found no data") {
            //             var students = [];
            //             for (var i = 0; i < response.length; i++) {
            //                 var obj = {};
            //                 obj.userId = response[i].userid;
            //                 obj.lastName = (response[i].lastName == null || response[i].lastName === undefined) ? "" : response[i].lastName;
            //                 obj.firstName = (response[i].firstName == null || response[i].firstName === undefined) ? "" : response[i].firstName;
            //                 obj.userName = response[i].userName != null ? response[i].userName : ' ';
            //                 obj.avatar = response[i].imageUrl;
            //                 obj.defaultSubjectId = response[i].defaultSubjectId;
            //                 obj.school = response[i].school;
            //                 students.push(obj);
            //             }
            //             $scope.listStudentSubscribed = students;
            //         }
            //     }
            // });


            $scope.saveChange = function () {
                var check = true;
                var error = '';

                var favorite = "";
                if ($('#music').is(':checked')) {
                    favorite += $('#music').val();
                }
                if ($('#art').is(':checked')) {
                    if (favorite.length > 0)
                        favorite += ',' + $('#art').val();
                    else
                        favorite += $('#art').val();
                }
                if ($('#sport').is(':checked')) {
                    if (favorite.length > 0)
                        favorite += ',' + $('#sport').val();
                    else
                        favorite += $('#sport').val();
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

                if (!isValidEmailAddress($('#txtEmail').val())) {
                    check = false;
                    error += "Email is not valid";
                }

                if (check) {
                    var mentor = {
                        'userid': userId,
                        'firstName': $('#txtFirstName').val(),
                        'lastName': $('#txtLastName').val(),
                        'email': $('#txtEmail').val(),
                        'gender': gender,
                        'school': $('#txtSchool').val(),
                        'bio': $('#txtBIO').val(),
                        'description': $('#txtDescription').val(),
                        'favorite': favorite
                    };
                    console.log(mentor);
                    StudentService.updateUserProfile(mentor).then(function (data) {
                        if (data.data.request_data_result == "Success") {
                            console.log(data.data.request_data_result);
                        }
                        else {
                            console.log(data.data.request_data_result);
                        }
                    });
                }
                else {
                    console.log(error);
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
                        }else{
                            $scope.subjects = "None";
                        }
                    }
                })
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


        }])
;
