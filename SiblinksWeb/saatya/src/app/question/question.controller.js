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
            '$document',
            function ($sce, $route, $scope, $rootScope, $timeout,
                      $http, $routeParams, $location, $log,$modal,
                       QuestionsService,
                       MentorService,
                      AnswerService, HomeService,
                       VideoService, myCache,$document) {

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
                var LIMIT_TOP_VIDEOS = 4;
                var idRemove;
                var eventRemove;
                var questionForAnswer;

                var LIMIT = 50;
                var OFFSET = 0;
                var currentPage = 0;
                var isLoadMore = false;
                $scope.isDisplayMore = false;
                var oldImagePath="";
                var oldImagePathEdited="";
                var qidEdit;
                var imagePathOld_BK;
                $scope.displayOrder = 'Newest';
                var bodyRef = angular.element( $document[0].body );
                $scope.subjects = JSON.parse(localStorage.getItem('subjects'));
                init();
                function init() {

                    $scope.curentOrderType = "newest";

                    if (isEmpty(subjectid)) {
                        subjectid = "-1";
                    }

                    if (isEmpty(userId)) {
                        userId = -1;
                        return;
                    }

                    getQuestions(userId, LIMIT, OFFSET, $scope.curentOrderType, oldQid, subjectid);

                    QuestionsService.countQuestions(userId, $scope.curentOrderType, subjectid).then(function (data) {
                        $scope.totalQuestion = data.data.request_data_result[0].numquestion;
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

                //fix height in first ask
                $timeout(function(){
                    if ($location.absUrl().indexOf('first-ask') > -1 && $("#top-mentors-videos").height() > 0) {
                        $(".create-ask").height($("#top-mentors-videos").height());
                    }
                });

                $scope.detailQuestion = function (id) {
                    window.location.href = '/#/question_detail/' + id + "";
                }
                $scope.showFormAdd = function () {
                    $("#autocompleteCate_value_dropdown").addClass('ng-hide');
                    bodyRef.addClass('disableScroll');
                    $scope.titlePopupAsk = "Ask a question";
                    $scope.isEdit = false;
                    //$scope.initCategory = {};
                    $scope.$broadcast('angucomplete-alt:clearInput', "autocompleteCate");
                    $scope.imagePathOld = [];
                    $('#autocompleteQuest_value').val("");
                    $(".form-ask-question").css({"left": 0});
                    $scope.filesArray = [];
                }
                $scope.closePopupAskQuestion = function () {
                    bodyRef.removeClass('disableScroll');
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
                    $rootScope.$broadcast('open');
                    QuestionsService.countQuestions(userId, $scope.curentOrderType, subjectid).then(function (data) {
                        $scope.totalQuestion = data.data.request_data_result[0].numquestion;
                        if ($scope.totalQuestion != '0') {
                            QuestionsService.getQuestionByUserId(userId, LIMIT, OFFSET, $scope.curentOrderType, oldQid, subjectid).then(function (data) {
                                $rootScope.$broadcast('close');
                                if (data.data.status) {
                                    var result = data.data.request_data_result;
                                    listPosted = [];
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
                                            $rootScope.$broadcast('close');
                                            return;
                                        }
                                    }
                                    $scope.askQuestion = listPosted;
                                }
                            });
                        }
                        else {
                            $rootScope.$broadcast('close');
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
                    var newoffset = LIMIT * currentPage;
                    console.log(newoffset);
                    getQuestions(userId, LIMIT, newoffset, $scope.curentOrderType, oldQid, subjectid);

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
                
                $scope.showEditQuestionId="";
                $scope.showEditQuestion = function(qid) {
                	$scope.isEditQuestion = true;
                	if(qid == $scope.showEditQuestionId) {
                		$scope.showEditQuestionId= "";
                	} else {
                		$scope.showEditQuestionId= qid;
                	}
          	  	}

                $scope.filesArray = [];
                $scope.removeImg = function (index) {
                    $scope.filesArray.splice(index, 1);

                }
                $scope.onFileSelect = function ($files) {
                    $scope.askErrorMsg= "";
                    var errFile = errFiles && errFiles[0];
                    if(!isEmpty(errFile)){
                        $scope.askErrorMsg = 'File wrong format. Please select file image!';
                        return;
                    }
                    if ($files!=null && $files.length > MAX_IMAGE){
                        $scope.askErrorMsg = 'You only upload ' + MAX_IMAGE +' image';
                        return ;
                    }
                    if ($files != null) {
                        for (var i = 0; i < $files.length; i++) {
                            var file = $files[i];
                            $scope.filesArray.push(file);
                        }
                    }
                };

                $scope.editQuestion = function (qid, image, imagepath, content, subjectid, subject) {
                    bodyRef.addClass('disableScroll');
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
                    if (isEmpty(userId) ||userId=='-1') {
                        $scope.askErrorMsg='Please login before you ask a question';
                        $rootScope.myVarU = !$scope.myVarU;
                        $timeout(function () {
                            $rootScope.myVarU = false;
                        }, 2500);
                        return;
                    }
                    // get question of student of ask question
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
                        $scope.askErrorMsg='Please enter your question';
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
                        $scope.askErrorMsg='Image over 5Mb';
                        $rootScope.myVarU = !$scope.myVarU;
                        $timeout(function () {
                            $rootScope.myVarU = false;
                        }, 2500);
                        return;
                    }

                    fd.append('userId', userId);
                    fd.append('content', questions);

                    fd.append('subjectId', $scope.selectedSubject.originalObject.subjectId);
                    $rootScope.$broadcast('open');
                    HomeService.addQuestion(fd).then(function (data) {
                        if (data.data.status == "true") {
                            $(".popup-images, .form-ask-question").css({"left": "100%"});
                            window.location.href = '/#/ask_a_question/-1';
                            window.location.reload();
                        }
                        else {
                            $scope.askErrorMsg =data.data.request_data_result;
                        }
                        $rootScope.$broadcast('close');
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
                        $scope.askErrorMsg='You enter your question';
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
                        $scope.askErrorMsg='Image over 5Mb';
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
                    $rootScope.$broadcast('open');
                    QuestionsService.updateQuestion(fd).then(function (data) {
                        if (data.data.status == "true") {
                            $(".popup-images, .form-ask-question").css({"left": "100%"});
                            // window.location.href = '/#/ask_a_question';
                            window.location.reload();
                        }
                        else {
                            $scope.askErrorMsg =data.data.request_data_result;
                        }
                        $rootScope.$broadcast('close');
                    });


                };
                $scope.onFileSelect = function ($files) {
                    $scope.askErrorMsg= "";
                    if ($files != null) {
                        for (var i = 0; i < $files.length; i++) {
                            var file = $files[i];
                            $scope.filesArray.push(file);


                        }
                    }
                };
                $scope.deleteQuestion = function (qid) {
                    $rootScope.$broadcast('open');
	                QuestionsService.removePost(qid).then(function (data) {
	                    if (data.data.status == "true") {
	                        window.location.href = '#/ask_a_question/-1';
                            window.location.reload();
	                    } else {
	                       $scope.errorMessage = "Can't delete question";
	                    }
                        $rootScope.$broadcast('close');
	                });
                }
 }]);
