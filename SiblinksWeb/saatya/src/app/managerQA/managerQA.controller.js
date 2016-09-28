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
                //$scope.subjects = myCache.get("subjects");
               // $scope.initCategory = {subject: obj[0].subject, subjectId: obj[0].subjectId};
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

        $scope.answerQuestion = function (qid, authorId,subjectId) {
            if (userId == null) {
                return;
            }
            var content = $('#txtAnswer').val();
            AnswerService.postAnswer(userId, authorId, qid, content,subjectId).then(function (data) {
                var rs = data.data.status;
                if(rs){
                    $('#txtAnswer').val("");
                    QuestionsService.getAnswerByQid(qid, typeOrderAnswer, "", "").then(function (data) {
                        var answers = data.data.request_data_result;
                        $scope.listAnswer = answers;
                    });
                }
            });
        }


        $scope.selectedSubject = function (selected) {
            //selectCategory = selected;
            for (var i = 0; i < listDefaultSubjectId.length; i++) {
                if (listDefaultSubjectId[i].parentId == selected) {
                    $scope.subjectsChild.push(listDefaultSubjectId[i]);
                }
            }
        };
        $scope.stepsModel = [];
        $scope.onFileSelect = function ($files) {
            $scope.askErrorMsg = "";
            if ($files != null) {
                for (var i = 0; i < $files.length; i++) {
                    var file = $files[i];
                   // $scope.filesArray.push(file);
                    var reader = new FileReader();
                    reader.onload = $scope.imageIsLoaded;
                    reader.readAsDataURL(file);

                }
            }
        };
        $scope.removeImg = function (index) {
            //$scope.filesArray.splice(index, 1);
            $scope.stepsModel.splice(index, 1);

        }

        $scope.imageIsLoaded = function (e) {
            $scope.$apply(function () {
                $scope.stepsModel.push(e.target.result);
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

    }]);