brotControllers
    .controller(
        'QuestionController',
        [
            '$sce',
            '$route',
            '$scope',
            '$rootScope',
            '$timeout',
            '$http',
            '$routeParams',
            '$location',
            '$log',
            'SubjectServices',
            'QuestionsService',
            'CategoriesService',
            'MentorService',
            'AnswerService',
            'HomeService',
            'CommentService',
            'StudentService',
            'VideoService',
            'myCache',
            function ($sce, $route, $scope, $rootScope, $timeout,
                      $http, $routeParams, $location, $log,
                      SubjectServices, QuestionsService,
                      CategoriesService, MentorService,
                      AnswerService, HomeService, CommentService,
                      StudentService, VideoService, myCache) {

                var subjectid = $routeParams.subjectid;
                if (isEmpty(subjectid)) {
                    subjectid = "-1";
                }
                $scope.limitAnswes = 0;
                var listPosted = [];

                var userId = localStorage.getItem('userId');
                $scope.userId = userId;

                $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';
                $scope.totalQuestion = 0;
                var oldQid = '-1';
                $scope.curentOrderType = "newest";
                var LIMIT_TOP_MENTORS = 5;
                var LIMIT_TOP_VIDEOS = 5;
                var idRemove;
                var eventRemove;
                var questionForAnswer;

                var limit = 10;
                var offset = 0;
                var currentPage = 0;
                var isLoadMore = false;
                $scope.isDisplayMore = false;
                init();
                function init() {

                    $scope.curentOrderType = "newest";
                    //$scope.ordertype = "answered";
                    if (isEmpty(userId)) {
                        userId = -1;
                    }
                    if (isEmpty(subjectid)) {
                        subjectid = "-1";
                    }

                    getQuestions(userId, limit, offset, $scope.curentOrderType, oldQid, subjectid);

                    QuestionsService.countQuestions(userId, $scope.curentOrderType, subjectid).then(function (data) {
                        $scope.totalQuestion = data.data.request_data_result[0].numquestion;
                        if (userId == "-1") {
                            window.location.href = '/#/first-ask';
                            return;
                        }
                        if ($scope.totalQuestion == 0) {
                            if (subjectid == -1) {
                                window.location.href = '/#/first-ask';
                                return;
                            }
                        }
                    });

                    MentorService.getTopMentorsByLikeRateSubcrible(LIMIT_TOP_MENTORS, offset, 'subcribe', userId).then(function (data) {
                        var data_result = data.data.request_data_result;
                        var subjects = myCache.get("subjects");
                        if (data_result) {
                            var listTopMentors = [];
                            for (var i = 0; i < data_result.length; i++) {
                                var mentor = {};
                                mentor.userid = data_result[i].userid;
                                mentor.userName = data_result[i].userName;
                                mentor.lastName = data_result[i].lastName;
                                mentor.firstName = data_result[i].firstName;
                                mentor.imageUrl = data_result[i].imageUrl;
                                mentor.numlike = data_result[i].numlike;
                                mentor.numsub = data_result[i].numsub;
                                mentor.numvideos = data_result[i].numvideos;
                                mentor.defaultSubjectId = data_result[i].defaultSubjectId;
                                mentor.listSubject = getSubjectNameById(data_result[i].defaultSubjectId, subjects);
                                mentor.numAnswers = data_result[i].numAnswers;
                                listTopMentors.push(mentor);
                            }
                        }
                        $scope.listTopmentors = listTopMentors;
                    });

                    VideoService.getVideoByRate(LIMIT_TOP_VIDEOS, offset).then(function (data) {
                        if (data.data.status) {
                            var result_data = data.data.request_data_result;
                            if (result_data) {
                                var listVideoRate = [];
                                for (var i = 0; i < result_data.length; i++) {
                                    var element = result_data[i];
                                    var video = {};
                                    video.title = element.title;
                                    video.image = element.image;
                                    video.url = element.url;
                                    video.rating = element.rating;
                                    video.uid = element.uid;
                                    video.vid = element.vid;
                                    listVideoRate.push(video);
                                }
                            }
                            $scope.listVideoRate = listVideoRate;
                        }

                    });


                }


                function getQuestions(userId, limit, offset, type, oldQid, subjectid) {

                    QuestionsService.getQuestionByUserId(userId, limit, offset, type, oldQid, subjectid).then(function (data) {
                        if (data.data.status) {
                            var result = data.data.request_data_result;
                            if (!isLoadMore) {
                                listPosted = [];
                            }
                            if (result == null) {
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

                function detectMultiImage(imagePath) {
                    if (isEmpty(imagePath)) {
                        return null;
                    }
                    var result = [];
                    if (imagePath != null && imagePath.indexOf(";") != -1) {
                        for (var i = 0; i < imagePath.split(";").length; i++) {
                            var str = imagePath.split(";")[i];
                            if (!isEmpty(str)) {
                                result.push(str);
                            }

                        }
                        return result;
                    }
                    var listImage = [];
                    listImage.push(imagePath)
                    return listImage;

                };

                $scope.detailQuestion = function (id) {
                    window.location.href = '/#/question_detail/' + id + "";
                }
                $scope.showFormAdd = function () {
                    $(".form-ask-question").css({"left": 0});
                }
                $scope.closePopupAskQuestion = function () {
                    $(".popup-images, .form-ask-question").css({"left": "100%"});
                }

                $scope.viewAnswer = function (num) {
                    if(num == 0){
                        return;
                    }
                    if($scope.limitAnswes == num){
                        $scope.limitAnswes = 0;
                    }else {
                        $scope.limitAnswes = num;
                    }
                }

                $scope.isShowOrder = false;

                $scope.orderQuestions = function (type) {
                    var limitOder = 10;
                    var offsetOder = 0;

                    if (type == $scope.curentOrderType) {
                        $scope.isShowOrder = false;
                        return;
                    }
                    $scope.isDisplayMore = false;
                    currentPage = 0;
                    oldQid = '-1';
                    $scope.curentOrderType = type + '';

                    if (isEmpty(userId)) {
                        userId = -1;
                    }
                    isLoadMore = false;
                    QuestionsService.countQuestions(userId, $scope.curentOrderType, subjectid).then(function (data) {
                        $scope.totalQuestion = data.data.request_data_result[0].numquestion;
                        if ($scope.totalQuestion != '0') {
                            getQuestions(userId, limitOder, offsetOder, $scope.curentOrderType, oldQid, subjectid);
                        }
                        else {
                            $scope.askQuestion = [];
                        }
                    });

                    $scope.isShowOrder = false;
                }


                $scope.isShowImageHover = false;
                $scope.currentImage = "";
                $scope.toggleShowOrder = function () {
                    $scope.isShowOrder = $scope.isShowOrder === false ? true : false;
                };
                $scope.zoomImage = function (img) {
                    $scope.currentImage = ( img );
                    $(".popup-images").css({"left": 0});
                }

                $scope.closeImage = function () {
                    $(".popup-images, .form-ask-question").css({"left": "100%"});
                }
                $scope.imageHoverIn = function () {
                    $scope.isShowImageHover = true;
                }
                $scope.imageHoverOut = function () {
                    $scope.isShowImageHover = false;
                }


                $scope.likeCount = 0;
                $scope.likeAnswer = function (aid) {
                    if (!isEmpty(userId) && userId != -1) {
                        AnswerService.likeAnswer(userId, aid + "").then(function (data) {
                            if (data.data.status == 'Fail') {
                                $scope.likeCount = -1;
                            }
                            else {
                                $scope.likeCount = 1;
                            }

                        });
                    }
                    else {
                        $scope.likeCount = 0;
                    }
                };

                $scope.isLoadMore = false;


                $scope.loadMorePost = function (ev) {
                    currentPage++;
                    isLoadMore = true;
                    if ($scope.isDisplayMore) {
                        return;
                    }
                    $scope.isDisplayMore = true;

                    var newoffset = limit * currentPage;
                    console.log(newoffset);
                    if (newoffset > $scope.totalQuestion) {
                        $scope.isDisplayMore = true;
                        return;
                    }
                    else {
                        $scope.isDisplayMore = false;
                        getQuestions(userId, limit, newoffset, $scope.curentOrderType, oldQid, subjectid);
                    }


                };


                $scope.deletePost = function (event, pid) {
                    $scope.typeItem = 'Post';
                    $('#deleteItem').modal('show');
                    idRemove = pid;
                    eventRemove = event;
                };

                $scope.deleteAnswer = function (aid, q) {
                    questionForAnswer = q;
                    $scope.typeItem = 'Answer';
                    $('#deleteItem').modal('show');
                    idRemove = aid;
                    eventRemove = event;
                };


            }]);
