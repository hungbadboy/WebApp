brotControllers
    .controller(
        'QuestionDetailCtrl',
        [
            '$sce',
            '$http',
            '$scope',
            '$rootScope',
            '$routeParams',
            '$location',
            '$timeout',
            '$log',
            '$window',
            'QuestionsService',
            'AnswerService',
            'HomeService',
            'CommentService',
            'StudentService',
            'myCache','$document',
            function ($sce, $http, $scope, $rootScope, $routeParams,
                      $location, $timeout, $log, $window,
                      QuestionsService, AnswerService,

                      HomeService, CommentService, StudentService,
                      myCache,$document) {
                var userId = localStorage.getItem('userId');
                $scope.userId = userId;
                var question_id = $routeParams.question_id;
                $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';
                var oldImagePath="";
                var oldImagePathEdited="";
                var qidEdit;
                var imagePathOld_BK;

                var type = "newest";
                $scope.countAnswer = 0;
                $scope.isShowOrder = false;
                $scope.isShowEdit = false;
                $scope.propertyName = 'TIMESTAMP';
                $scope.reverse = true;
                var bodyRef = angular.element( $document[0].body );
                $scope.subjects = JSON.parse(localStorage.getItem('subjects'));
                init();

                function init() {
                    if (isEmpty(question_id)) {
                        return;
                    }

                    QuestionsService.getQuestionById(question_id).then(function (data) {
                        var obj = data.data.request_data_result;
                        if(obj.length == 0){
                            $scope.errorMessage = "Not found question";
                            return ;
                        }
                        var viewNew = parseInt(obj[0].numViews, 10) + 1;
                        $scope.initCategory = {subject: obj[0].subject, subjectId: obj[0].subjectId};
                        //$('#autocompleteQuest_value').val(obj[0].content);
                        $scope.imagePathOld = detectMultiImage(obj[0].imagePath);
                        imagePathOld_BK = $scope.imagePathOld;
                        oldImagePath = obj[0].imagePath;

                        var question = {
                            "authorId": obj[0].authorID,
                            "question_id": obj[0].pid,
                            "question_text": obj[0].content,
                            "views": viewNew,
                            "question_creation_date": moment(obj[0].updateTime, 'YYYY-MM-DD h:mm:ss').startOf('hour').fromNow(),
                            "timeStamp": convertUnixTimeToTime(obj[0].TIMESTAMP),
                            "author": obj[0].userName,
                            "firstName": obj[0].firstName,
                            "lastName": obj[0].lastName,
                            "avatar": obj[0].imageUrl,
                            "subject": obj[0].subject,
                            "subjectId": obj[0].subjectId,
                            "numview": obj[0].numViews,
                            "numlike": obj[0].countLike,
                            "imagePath": detectMultiImage(obj[0].imagePath)

                        };
                        $scope.question = question;
                        $scope.arr = encodeURIComponent(question.question_text);


                    });

                    var listAnswer = [];
                    QuestionsService.getAnswerByQid(question_id, type, "", "",userId).then(function (data) {

                        var answers = data.data.request_data_result;
                        $scope.countAnswer = answers.length;
                        if (answers != null) {
                            for (var i = 0; i < answers.length; i++) {
                                var an = answers[i];

                                an.timeStamp = convertUnixTimeToTime(an.TIMESTAMP);
                                an.imageAnswerPath = detectMultiImage(an.imageAnswer);
                                listAnswer.push(an);
                            }
                        } else {
                            $scope.countAnswer = 0;
                        }
                        $scope.listAnswer = listAnswer;
                    });

                    // get data when edit
                }

                $scope.removeImg = function (index) {
                    $scope.filesArray.splice(index, 1);

                }

                $scope.removeImgOld = function (index) {
                    if(!isEmpty($scope.imagePathOld)){
                        oldImagePathEdited += $scope.imagePathOld[index];
                    }
                    $scope.imagePathOld.splice(index, 1);

                }

                $scope.zoomImage = function (img) {
                    $scope.currentImage = ( img + "");
                    $(".popup-images").css({"left": 0});
                }

                function detectMultiImage(imagePath) {
                    if(isEmpty(imagePath)){
                        return null;
                    }
                    var result=[];
                    if (imagePath != null && imagePath.indexOf(";") != -1) {
                        for(var i = 0;i<imagePath.split(";").length; i++){
                            var str = imagePath.split(";")[i];
                            if(!isEmpty(str)){
                                result.push(str);
                            }

                        }
                        return result;
                    }
                    var listImage = [];
                    listImage.push(imagePath)
                    return listImage;

                };


                $scope.likeAnswer = function (aid) {
                    if (!isEmpty(userId) && userId != -1) {
                        AnswerService.likeAnswer(userId, aid + "").then(function (data) {
                            if (data.data.status == 'true') {
                                if (data.data.request_data_type == "like") {
                                    $('.heart'+aid).attr('id','heart');
                                }
                                else {
                                    $('.heart'+aid).attr('id','');
                                }
                            }
                        });
                    }
                };

                $scope.editQuestion = function (qid) {
                    bodyRef.addClass('disableScroll');
                    $scope.titlePopupAsk = "Edit question";
                    $scope.isEdit = true;
                    qidEdit = qid;
                    $scope.imagePathOld = imagePathOld_BK;
                    $scope.initCategory = {subject: $scope.question.subject, subjectId: $scope.question.subjectId};
                    $scope.$broadcast('angucomplete-alt:changeInput',"autocompleteCate", $scope.initCategory);
                    $('#autocompleteQuest_value').val($scope.question.question_text);
                    $(".form-ask-question").css({"left": 0});

                }
                $scope.deleteQuestion = function (qid) {
                    $rootScope.$broadcast('open');
                        QuestionsService.removePost(qid).then(function (data) {
                            if (data.data.status == "true") {
                                window.location.href = '#/ask_a_question/-1';
                            }
                            else {
                               $scope.errorMessage = "Can't delete question";
                            }
                            $rootScope.$broadcast('close');
                        });

                }
                $scope.filesArray = [];
                $scope.removeImg = function (index) {
                    $scope.filesArray.splice(index, 1);

                }
                // $scope.selectedSubject = function (selected) {
                //     $scope.initCategory = selected;
                // };

                $scope.updateQuestion = function () {
                    // detail question
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
                        $scope.askErrorMsg='Please input your question';
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

                    fd.append('qid', $scope.question.question_id);
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
                $scope.onFileSelect = function ($files,errFiles) {
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

                $scope.redirectForum = function () {
                    if (isEmpty(userId) ||userId=='-1') {
                        $scope.askErrorMsg='Please login before you ask a question';
                        $rootScope.myVarU = !$scope.myVarU;
                        $timeout(function () {
                            $rootScope.myVarU = false;
                        }, 2500);
                        return;
                    }
                    // add question detail
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
                        $scope.askErrorMsg='Please input your question';
                        $("#autocompleteQuest_value").focus();
                        return;
                    }


                    if ($scope.filesArray.length > MAX_IMAGE) {
                        $scope.askErrorMsg='You only upload ' + MAX_IMAGE +' image';
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
                            fd.append('file', file);
                            totalSize += file.size;
                        }
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

                    fd.append('subjectId',$scope.selectedSubject.originalObject .subjectId);
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


                $scope.showFormAdd = function () {
                    // fix in IE
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
                    $scope.askErrorMsg = '';
                }
                
                $scope.imageHoverIn = function (eId) {
                	angular.element("#"+eId).addClass('show');
                }
                
                $scope.imageHoverOut = function (eId) {
                	angular.element("#"+eId).removeClass('show');
                }
            }]);