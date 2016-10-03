brotControllers.controller('managerQAController', ['$scope', '$http', '$location', '$rootScope', '$timeout', '$log', 'HomeService', 'NotificationService',
    'StudentService', 'myCache', 'managerQAService', 'QuestionsService','AnswerService',
    function ($scope, $http, $location, $rootScope, $timeout, $log, HomeService, NotificationService, StudentService,
              myCache, managerQAService, QuestionsService,AnswerService) {


        $scope.subjectId = "-1";
        var LIMIT = 10;
        var lastQId = "";
        var userId = localStorage.getItem('userId');
        var defaultSubjectId = localStorage.getItem('defaultSubjectId');
        $scope.mentorAvatar  = localStorage.getItem('imageUrl');
        $scope.userId = userId;
        var type = "";
        var typeOrderAnswer = "newest";
        var listDefaultSubjectId;
        $scope.subjectsChild = [];
        var oldImagePath = "";
        var imagePathOld_BK;
        var MAX_SIZE_IMG_UPLOAD = 10485760;
        var MAX_IMAGE = 4;
        init();


        function init() {
            managerQAService.getListQuestionQA("-1", userId, lastQId, type, LIMIT).then(function (data) {
                var allSubjects = myCache.get("subjects");
                listDefaultSubjectId = getSubjectNameById(defaultSubjectId, allSubjects);
                $scope.subjectsParent = [];

                for (var i = 0; i < listDefaultSubjectId.length; i++) {
                    if (listDefaultSubjectId[i].level == '0') {
                        $scope.subjectsParent.push(listDefaultSubjectId[i]);
                    }
                }
                if (data.data.status) {
                    $scope.listQuestions = data.data.request_data_result;
                    if ($scope.listQuestions.length > 0) {
                        lastQId = $scope.listQuestions[$scope.listQuestions.length - 1].qid;
                    }

                }
                if ($scope.listQuestions != null && $scope.listQuestions.length > 0) {
                    $scope.currentPid = $scope.listQuestions[0].pid;
                    getQuestionById($scope.currentPid);
                }
            });


        }

        $scope.convertUnixTimeToTime = function (datetime) {
            return convertUnixTimeToTime(datetime);
        }
        $scope.selectParentChange = function () {
            $scope.subjectsChild = {};
            for (var i = 0; i < $scope.subjectsParent.length; i++) {
                var sub = $scope.subjectsParent[i];
                if (sub.parentId == $scope.selectedParent.parentId) {
                    $scope.subjectsChild.push(sub[i]);
                }
            }
        }

        function getQuestionQA(subjectId, userId, lastQId, type, limit, child) {
            //init();
            managerQAService.getListQuestionQA(subjectId, userId, lastQId, type, limit, child).then(function (data) {
                if (data.data.status) {
                    $scope.listQuestions = data.data.request_data_result;
                    if ($scope.listQuestions.length > 0) {
                        lastQId = $scope.listQuestions[$scope.listQuestions.length - 1].qid;
                    }

                }
            });
        }

        $scope.currentType = "all";
        $scope.orderQuestions = function (type) {
            lastQId = "";
            if ($scope.currentType == type) {
                return;
            }
            $scope.currentType = type;

            getQuestionQA($scope.subjectId, userId, lastQId, type, LIMIT);

        }
        $scope.selectQuestion = function (qid) {
            getQuestionById(qid);
        }

        function getQuestionById(qid) {
            QuestionsService.getQuestionById(qid).then(function (data) {
                var obj = data.data.request_data_result;
                if (obj.length == 0) {
                    $scope.errorMessage = "Not found question";
                    return;
                }
                $scope.currentPid = qid;
                var viewNew = parseInt(obj[0].numViews, 10) + 1;
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
                    "imagePath": detectMultiImage(obj[0].imagePath),
                    "numReplies" :obj[0].numReplies

                };
                $scope.questionDetail = question;


            });
            QuestionsService.updateViewQuestion(qid, "view").then(function (data) {
            });
            QuestionsService.getAnswerByQid(qid, typeOrderAnswer, "", "").then(function (data) {
                var answers = data.data.request_data_result;
                $scope.listAnswer = answers;
            });

        }

        $scope.answerQuestion = function (pid) {
            var content = $('#txtAnswer').val();
            if (!content) {
                $timeout(function () {
                    $rootScope.myVarQ = false;
                }, 2500);
                $scope.QAErrorMsg='You enter text your answer';

                return;
            }

            if (isEmpty(userId) ||userId=='-1') {
                $scope.QAErrorMsg='Please login before you ask a question';
                $timeout(function () {
                    $rootScope.myVarU = false;
                }, 2500);
                return;
            }
            if ($scope.filesArray.length > MAX_IMAGE) {
                $scope.QAErrorMsg='You only upload ' + MAX_IMAGE +' image';
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
                $scope.askErrorMsg='Image over 10M';
                $rootScope.myVarU = !$scope.myVarU;
                $timeout(function () {
                    $rootScope.myVarU = false;
                }, 2500);
                return;
            }

            fd.append('studentId', $scope.questionDetail.authorId);
            fd.append('mentorId', userId);
            fd.append('content', content);
            fd.append('subjectId', $scope.questionDetail.subjectId);
            fd.append('pid', pid);
            managerQAService.postAnswer(fd).then(function (data) {
                var rs = data.data.status;
                if(rs){
                    $('#txtAnswer').val("");
                    QuestionsService.getAnswerByQid(pid, typeOrderAnswer, "", "").then(function (data) {
                        var answers = data.data.request_data_result;
                        $scope.listAnswer = answers;
                        $scope.stepsModel = [];
                    });
                }
                else {
                    $scope.QAErrorMsg = "Can't answer";
                }
            });

        };


        $scope.selectedSubject = function (selected) {
            selected.originalObject.id;
            //selectCategory = selected;
            // for (var i = 0; i < listDefaultSubjectId.length; i++) {
            //     if (listDefaultSubjectId[i].parentId == selected) {
            //         $scope.subjectsChild.push(listDefaultSubjectId[i]);
            //     }
            // }
        };
        $scope.stepsModel = [];
        $scope.filesArray = [];
        $scope.onFileSelect = function ($files) {
            $scope.askErrorMsg = "";
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
        $scope.removeImg = function (index) {
            $scope.filesArray.splice(index, 1);
            $scope.stepsModel.splice(index, 1);

        }

        $scope.imageIsLoaded = function (e) {
            $scope.$apply(function () {
                $scope.stepsModel.push(e.target.result);
            });
        }
        $scope.zoomImage = function (img) {
            $scope.currentImage = ( img + "");
            $(".popup-images").css({"left": 0});
        }
        $scope.imageHoverIn = function (eId) {
            $("#"+eId).addClass('show');
        }

        $scope.imageHoverOut = function (eId) {
            $("#"+eId).removeClass('show');
        }
        $scope.closePopupAskQuestion = function () {
            $(".popup-images").css({"left": "100%"});
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

    }]);