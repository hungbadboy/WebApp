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
            '$modal',
            'QuestionsService',
            'MentorService',
            'AnswerService',
            'HomeService',
            'VideoService',
            'myCache',
            function ($sce, $route, $scope, $rootScope, $timeout,
                      $http, $routeParams, $location, $log,$modal,
                       QuestionsService,
                       MentorService,
                      AnswerService, HomeService,
                       VideoService, myCache) {

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

                var limit = 20;
                var offset = 0;
                var currentPage = 0;
                var isLoadMore = false;
                $scope.isDisplayMore = false;
                var oldImagePath="";
                var oldImagePathEdited="";
                var qidEdit;
                var imagePathOld_BK;
                //10M
                var MAX_SIZE_IMG_UPLOAD = 10485760;
                var MAX_IMAGE = 4;
                
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
                        $scope.subjects = myCache.get("subjects");
                        if (userId == "-1") {
                            window.location.href = '/#/first-ask';
                            return;
                        }
                        if ($scope.totalQuestion == 0 && $location.path().indexOf('question')>=0) {
                            if (subjectid == -1) {
                                window.location.href = '/#/first-ask';
                                return;
                            }
                        }
                    });

                    MentorService.getTopMentorsByLikeRateSubcrible(LIMIT_TOP_MENTORS, offset, 'subcribe', userId).then(function (data) {
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
                                    video.numRatings = element.numRatings;
                                    video.averageRating = element.averageRating;
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
                            if (result == null || result.question === undefined) {
                                return;
                            }
                            for (var i = 0; i < result.question.length; i++) {
                                $scope.isDisplayMore = false;
                                var objPosted = {};
                                var questionData = result.question[i];
                                objPosted.id = questionData.PID;
                                oldQid = questionData.PID;
                                objPosted.title = questionData.TITLE;
                                objPosted.subject = questionData.SUBJECT;
                                objPosted.subjectid = questionData.SUBJECTID;
                                objPosted.name = questionData.FIRSTNAME;
                                objPosted.firstName = questionData.FIRSTNAME;
                                objPosted.lastName = questionData.LASTNAME;
                                objPosted.content = questionData.CONTENT;
                                objPosted.count_answer = questionData.NUMREPLIES;

                                objPosted.numviews = questionData.NUMVIEWS == null ? 0 : questionData.NUMVIEWS;
                                objPosted.time = convertUnixTimeToTime(questionData.TIMESTAMP);
                                objPosted.image = detectMultiImage(questionData.IMAGEPATH);
                                objPosted.imagepath = questionData.IMAGEPATH;
                                objPosted.authorId = questionData.AUTHORID;
                                //objPosted.count = questionData.length;
                                if (result.answers !== undefined) {
                                    if (result.answers[i] != null) {
                                        var answer = result.answers[i];
                                        var answer_result = answer.body.request_data_result;
                                        var listAnswer = [];
                                        for (var y = 0; y < answer_result.length; y++) {

                                            var objAnswer = {};
                                            objAnswer.authorID = answer_result[y].authorID;
                                            objAnswer.aid = answer_result[y].aid;
                                            objAnswer.pid = answer_result[y].pid;
                                            objAnswer.name = answer_result[y].firstName + " " + answer_result[y].lastName;
                                            objAnswer.content = answer_result[y].content;
                                            objAnswer.avatar = answer_result[y].imageUrl;
                                            objAnswer.countLike = answer_result[y].numlike;
                                            objAnswer.imageAnswer = detectMultiImage(answer_result[y].imageAnswer);
                                            objAnswer.like = answer_result[y].like;
                                            objAnswer.time = convertUnixTimeToTime(answer_result[y].TIMESTAMP);
                                            listAnswer.push(objAnswer);
                                        }
                                        objPosted.answers = listAnswer;
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
                    $scope.titlePopupAsk = "Ask a question";
                    $scope.isEdit = false;
                    //$scope.initCategory = {};
                    $scope.$broadcast('angucomplete-alt:clearInput', "autocompleteCate");
                    $scope.imagePathOld = [];
                    $('#autocompleteQuest_value').val("");
                    $(".form-ask-question").css({"left": 0});
                    $scope.filesArray = [];
                    $scope.stepsModel = [];
                }
                $scope.closePopupAskQuestion = function () {
                    $(".popup-images, .form-ask-question").css({"left": "100%"});
                }

                $scope.viewAnswer = function (qid) {
                    if( $('.detail-answer-question_'+qid).hasClass('hide'))
                    {
                        $('#spamview_'+qid).text('Hide');
                        $('.detail-answer-question_'+qid).removeClass('hide');

                    }
                    else {
                        $('#spamview_'+qid).text('View');
                        $('.detail-answer-question_'+qid).addClass('hide');
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
                $scope.imageHoverIn = function (eId) {
                	angular.element("#"+eId).addClass('show');
                }
                $scope.imageHoverOut = function (eId) {
                	angular.element("#"+eId).removeClass('show');
                }


                $scope.likeAnswer = function (aid,pid) {
                    if (!isEmpty(userId) && userId != -1) {
                        AnswerService.likeAnswer(userId, aid + "").then(function (data) {
                            if (data.data.status == 'true') {
                                if (data.data.request_data_type == "like") {
                                    $('.heart'+pid+aid).attr('id','heart');
                                }
                                else {
                                    $('.heart'+pid+aid).attr('id','');
                                }
                            }
                        });
                    }
                };

                $scope.loadMorePost = function (ev) {
                    if ($scope.isDisplayMore) {
                        return;
                    }
                    currentPage++;
                    isLoadMore = true;
                    $scope.isDisplayMore = true;
                    var newoffset = limit * currentPage;
                    console.log(newoffset);
                    getQuestions(userId, limit, newoffset, $scope.curentOrderType, oldQid, subjectid);

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
                

                $scope.isEditQuestion = false;
                $scope.showEditQuestionId="";
                $scope.showEditQuestion = function(qid) {
                	$scope.isEditQuestion = $scope.isEditQuestion ? false : true;
                	if(qid == $scope.showEditQuestionId) {
                		$scope.showEditQuestionId= "";
                	} else {
                		$scope.isEditQuestion = true;
                		$scope.showEditQuestionId= qid;
                	}
          	  	}

                $scope.filesArray = [];
                $scope.removeImg = function (index) {
                    $scope.filesArray.splice(index, 1);
                    $scope.stepsModel.splice(index, 1);

                }
                $scope.stepsModel = [];
                $scope.onFileSelect = function ($files) {
                    $scope.askErrorMsg= "";
                    if ($files != null) {
                        for (var i = 0; i < $files.length; i++) {
                            var file = $files[i];
                            $scope.filesArray.push(file);
                            var reader = new FileReader();
                            reader.onload = $scope.imageIsLoaded;
                            reader.readAsDataURL(file);

                        }
                    }
                };

                $scope.imageIsLoaded = function (e) {
                    $scope.$apply(function () {
                        $scope.stepsModel.push(e.target.result);
                    });
                }


                $scope.editQuestion = function (qid, image, imagepath, content, subjectid, subject) {
                    $scope.titlePopupAsk = "Edit question";
                    $scope.isEdit = true;
                    qidEdit = qid;
                    $scope.imagePathOld = image;
                    imagePathOld_BK = image;
                    oldImagePath = imagepath;
                    $scope.initCategory = {subject: subject, subjectId: subjectid};
                    $scope.$broadcast('angucomplete-alt:changeInput',"autocompleteCate", $scope.initCategory);
                    //$scope.initCategory.originalObject.subjectId = subjectid;
                   // $scope.initCategory.originalObject = $scope.initCategory;
                    $('#autocompleteQuest_value').val(content);
                    $(".form-ask-question").css({"left": 0});

                }

                $scope.removeImgOld = function (index) {
                    if(!isEmpty($scope.imagePathOld)){
                        oldImagePathEdited += $scope.imagePathOld[index];
                    }
                    $scope.imagePathOld.splice(index, 1);

                }
                $scope.redirectForum = function () {
                    // get question of student

                    if ($scope.selectedSubject == null || $scope.selectedSubject === undefined || $scope.selectedSubject.originalObject == null) {
                        $scope.askErrorMsg='Please choose category';
                        $("#autocompleteCate_value").focus();
                        $rootScope.myVarC = !$scope.myVarC;
                        $timeout(function () {
                            $rootScope.myVarC = false;
                        }, 2500);
                        return;
                    }
                    var questions = $('#autocompleteQuest_value').val();
                    if (!questions) {
                        $rootScope.myVarQ = !$scope.myVarQ;
                        $timeout(function () {
                            $rootScope.myVarQ = false;
                        }, 2500);
                        $scope.askErrorMsg='You enter text or upload for your question';
                        $("#autocompleteQuest_value").focus();
                        return;
                    }

                    if (isEmpty(userId) ||userId=='-1') {
                        $scope.askErrorMsg='Please login before you ask a question';
                        $rootScope.myVarU = !$scope.myVarU;
                        $timeout(function () {
                            $rootScope.myVarU = false;
                        }, 2500);
                        return;
                    }
                    fd = new FormData();
                    var totalSize = 0;
                    if ($scope.filesArray != null) {
                        for (var i = 0; i < $scope.filesArray.length; i++) {
                            file = $scope.filesArray[i];
                            totalSize += file.size;
                            fd.append('file', file);
                        }
                    }
                    if ($scope.filesArray.length > MAX_IMAGE) {
                        $scope.askErrorMsg='You only upload ' + MAX_IMAGE +' image';
                        $rootScope.myVarU = !$scope.myVarU;
                        $timeout(function () {
                            $rootScope.myVarU = false;
                        }, 2500);
                        return;
                    }

                    if(totalSize > MAX_SIZE_IMG_UPLOAD){
                        $scope.askErrorMsg='Image over 10M';
                        $rootScope.myVarU = !$scope.myVarU;
                        $timeout(function () {
                            $rootScope.myVarU = false;
                        }, 2500);
                        return;
                    }

                    fd.append('userId', userId);
                    fd.append('content', questions);

                    fd.append('subjectId', $scope.selectedSubject.originalObject.subjectId);
                    HomeService.addQuestion(fd).then(function (data) {
                        if (data.data.status == "true") {
                            $(".popup-images, .form-ask-question").css({"left": "100%"});
                            window.location.href = '/#/ask_a_question/-1';
                            window.location.reload();
                        }
                        else {
                            $scope.askErrorMsg =data.data.request_data_result;
                        }
                    });


                };

                $scope.updateQuestion = function () {
                    // get question of student

                    if ($scope.selectedSubject == null || $scope.selectedSubject === undefined || $scope.selectedSubject.originalObject == null) {
                        $scope.askErrorMsg='Please choose category';
                        $("#autocompleteCate_value").focus();
                        $rootScope.myVarC = !$scope.myVarC;
                        $timeout(function () {
                            $rootScope.myVarC = false;
                        }, 2500);
                        return;
                    }
                    var questions = $('#autocompleteQuest_value').val();
                    if (!questions) {
                        $rootScope.myVarQ = !$scope.myVarQ;
                        $timeout(function () {
                            $rootScope.myVarQ = false;
                        }, 2500);
                        $scope.askErrorMsg='You enter text to your question';
                        $("#autocompleteQuest_value").focus();
                        return;
                    }

                    if (isEmpty(userId) ||userId=='-1') {
                        $scope.askErrorMsg='Please login before edit a question';
                        $rootScope.myVarU = !$scope.myVarU;
                        $timeout(function () {
                            $rootScope.myVarU = false;
                        }, 2500);
                        return;
                    }
                    fd = new FormData();
                    var totalSize = 0;
                    if ($scope.filesArray != null) {
                        for (var i = 0; i < $scope.filesArray.length; i++) {
                            file = $scope.filesArray[i];
                            totalSize += file.size;
                            fd.append('file', file);
                        }
                    }

                    var totalLenth = $scope.filesArray.length;
                    if(isEmpty(oldImagePathEdited)){
                        oldImagePathEdited = oldImagePathEdited.substr(0,oldImagePathEdited.length-1);
                        totalLenth += oldImagePathEdited.split(";");
                    }

                    if (totalLenth > MAX_IMAGE) {
                        $scope.askErrorMsg='You only upload ' + MAX_IMAGE +' image';
                        $rootScope.myVarU = !$scope.myVarU;
                        $timeout(function () {
                            $rootScope.myVarU = false;
                        }, 2500);
                        return;
                    }

                    if(totalSize > MAX_SIZE_IMG_UPLOAD){
                        $scope.askErrorMsg='Image over 10M';
                        $rootScope.myVarU = !$scope.myVarU;
                        $timeout(function () {
                            $rootScope.myVarU = false;
                        }, 2500);
                        return;
                    }

                    fd.append('qid', qidEdit);
                    fd.append('content', questions);
                    fd.append('oldImagePathEdited', oldImagePathEdited);
                    fd.append('oldImagePath', oldImagePath);

                    fd.append('subjectId', $scope.selectedSubject.originalObject.subjectId);
                    QuestionsService.updateQuestion(fd).then(function (data) {
                        if (data.data.status == "true") {
                            $(".popup-images, .form-ask-question").css({"left": "100%"});
                            // window.location.href = '/#/ask_a_question';
                            window.location.reload();
                        }
                        else {
                            $scope.askErrorMsg =data.data.request_data_result;
                        }
                    });


                };
                $scope.stepsModel = [];
                $scope.onFileSelect = function ($files) {
                    $scope.askErrorMsg= "";
                    if ($files != null) {
                        for (var i = 0; i < $files.length; i++) {
                            var file = $files[i];
                            $scope.filesArray.push(file);
                            var reader = new FileReader();
                            reader.onload = $scope.imageIsLoaded;
                            reader.readAsDataURL(file);

                        }
                    }
                };
                $scope.deleteQuestion = function (qid) {
	                QuestionsService.removePost(qid).then(function (data) {
	                    if (data.data.status == "true") {
	                        window.location.href = '#/ask_a_question/-1';
                            window.location.reload();
	                    } else {
	                       $scope.errorMessage = "Can't delete question";
	                    }
	                });
                }
 }]);
