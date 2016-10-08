brotControllers.controller('managerQAController', ['$scope', '$http', '$location', '$rootScope', '$timeout', '$log', 'HomeService', 'NotificationService',
    'StudentService', 'myCache', 'managerQAService', 'QuestionsService','AnswerService',
    function ($scope, $http, $location, $rootScope, $timeout, $log, HomeService, NotificationService, StudentService,
              myCache, managerQAService, QuestionsService,AnswerService) {


        $scope.subjectId = "-1";
        var LIMIT = 30;
        var lastQId = "";
        var userId = localStorage.getItem('userId');
        var defaultSubjectId = localStorage.getItem('defaultSubjectId');
        $scope.mentorAvatar  = localStorage.getItem('imageUrl');
        $scope.userId = userId;
        var typeOrderAnswer = "newest";
        var listDefaultSubjectId;
        $scope.subjectsChild = [];
        var oldImagePath = "";
        var imagePathOld_BK;
        var MAX_SIZE_IMG_UPLOAD = 10485760;
        var MAX_IMAGE = 4;
        $scope.currentTab = 'all';
        $scope.textSearch = "";
        $scope.notFound = "";
        var selectedSubsId = "";
        var currentPage = 0;
        var currentPageAnswer = 0;
        $scope.isLoadMore = true;
        $scope.isLoadMoreAnswer =true;
        $scope.currentIndexAnswer = 0;
        init();


        function init() {
            var offset = 0;
            managerQAService.getListQuestionQA(selectedSubsId, userId, offset, $scope.currentTab, LIMIT,$scope.textSearch).then(function (data) {
                var allSubjects = myCache.get("subjects");
                listDefaultSubjectId = getSubjectNameByIdQA(defaultSubjectId, allSubjects);
                $scope.subjectsParent = [];
                $scope.subjectsParent = listDefaultSubjectId;
                // for (var i = 0; i < listDefaultSubjectId.length; i++) {
                //     if (listDefaultSubjectId[i].level == '0') {
                //         $scope.subjectsParent.push(listDefaultSubjectId[i]);
                //     }
                // }
                if (data.data.status) {
                    $scope.listQuestions = data.data.request_data_result;
                    $scope.listQuestions = data.data.request_data_result;
                    if ($scope.listQuestions != null && $scope.listQuestions.length > 0) {
                        lastQId = $scope.listQuestions[$scope.listQuestions.length - 1].pid;
                        $scope.currentPid = $scope.listQuestions[0].pid;
                        getQuestionById($scope.currentPid);
                        $scope.notFound = "";
                    }
                    else {
                        $scope.questionDetail = null;
                        $scope.notFound = "Not found";
                    }

                }
            });

        }

        $scope.loadMorePost = function () {
            if (!$scope.isLoadMore) {
                return;
            }
            currentPage++;
            var newOffset = currentPage * LIMIT;
            managerQAService.getListQuestionQA(selectedSubsId, userId, newOffset, $scope.currentTab, LIMIT,$scope.textSearch).then(function (data) {
                var result = data.data;
                if (result && result.status) {
                    if (result.request_data_result != null && result.request_data_result.length > 0) {
                        $scope.listQuestions.push(result.request_data_result);
                        lastQId = $scope.listQuestions[$scope.listQuestions.length - 1].pid;
                        $scope.isLoadMore =true;
                    }
                    else{
                        $scope.isLoadMore = false;
                    }
                }
            });

        }

        $scope.loadMoreAnswer = function () {
            if (!$scope.isLoadMore) {
                return;
            }
            currentPageAnswer++;
            var newOffset = currentPageAnswer * LIMIT;
            QuestionsService.getAnswerByQid($scope.currentPid, typeOrderAnswer, LIMIT, newOffset).then(function (data) {
                var result = data.data;
                if (result && result.status) {
                    if (result.request_data_result != null && result.request_data_result.length > 0) {
                        $scope.listAnswer.push(result.request_data_result);
                        $scope.isLoadMoreAnswer =true;
                    }
                    else{
                        $scope.isLoadMoreAnswer = false;
                    }

                }
            });
            managerQAService.getListQuestionQA(selectedSubsId, userId, newOffset, $scope.currentTab, LIMIT,$scope.textSearch).then(function (data) {
                var result = data.data;
                if (result && result.status) {
                    if (result.request_data_result != null && result.request_data_result.length > 0) {
                        $scope.listQuestions.push(result.request_data_result);
                        lastQId = $scope.listQuestions[$scope.listQuestions.length - 1].pid;
                        $scope.isLoadMore =true;
                    }
                    else{
                        $scope.isLoadMore = false;
                    }

                }
            });

        }
        $scope.showAnswer = function (index) {
            $scope.currentIndexAnswer = index;
            angular.element(document.getElementById('answer-detail')).modal();

        }
        $scope.openFile= function () {
            $('#file1').click();
        }

        $scope.selectTab= function (tab) {
            if($scope.currentTab == tab){
                return ;
            }
            $scope.currentTab = tab;
            lastQId = "";
            $scope.isLoadMore =true;
            currentPage = 0;
            init();
        }

        $scope.convertUnixTimeToTime = function (datetime) {
            return convertUnixTimeToTime(datetime);
        }
        $scope.searchQuestion = function () {
            getListQuestionAndDetail("",$scope.textSearch);
        }

        // function getQuestionQA(subjectId, userId, offset, type, limit,textSearch) {
        //     managerQAService.getListQuestionQA(subjectId, userId, offset, type, limit,textSearch).then(function (data) {
        //         if (data.data.status) {
        //             $scope.listQuestions = data.data.request_data_result;
        //             if ($scope.listQuestions != null && $scope.listQuestions.length > 0) {
        //                 lastQId = $scope.listQuestions[$scope.listQuestions.length - 1].pid;
        //             }
        //
        //         }
        //     });
        // }

        // $scope.currentType = "all";
        // $scope.orderQuestions = function (type) {
        //     var offset = "";
        //     if ($scope.currentType == type) {
        //         return;
        //     }
        //     $scope.currentType = type;
        //
        //     getQuestionQA($scope.subjectId, userId, lastQId, type, LIMIT,"");
        //
        // }
        $scope.selectQuestion = function (qid) {
            if($scope.currentPid == qid){
                return;
            }
            getQuestionById(qid);
            QuestionsService.updateViewQuestion(qid, "view").then(function (data) {
            });
        }

        function getQuestionById(qid) {
            $scope.QAErrorMsg = "";
            QuestionsService.getQuestionById(qid).then(function (data) {
                var obj = data.data.request_data_result;
                if (obj == null || obj.length == 0) {
                    $scope.QAErrorMsg = "Not found question";
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
            QuestionsService.getAnswerByQid(qid, typeOrderAnswer, LIMIT, 0).then(function (data) {
                var answers = data.data.request_data_result;
                $scope.isLoadMoreAnswer = true;
                $scope.listAnswer = answers;
            });

        }
        $scope.convertToArrayImage = function (str) {
            return detectMultiImage(str);
        }

        $scope.focusSubject = function () {
            $('#autocompleteSubs').focus();
        }
        $scope.answerQuestion = function (pid) {
            $scope.QAErrorMsg="";
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
                $scope.QAErrorMsg='Image over 10M';
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
                    //clear content when answer suscces
                    $('#txtAnswer').val("");
                    $scope.stepsModel = [];
                    $scope.filesArray = [];
                    QuestionsService.getAnswerByQid(pid, typeOrderAnswer, "", "").then(function (data) {
                        var answers = data.data.request_data_result;
                        $scope.isLoadMoreAnswer = true;
                        $scope.listAnswer = answers;

                    });
                }
                else {
                    $scope.QAErrorMsg = "Can't answer";
                }
            });

        };

        $scope.selectedSubject = function (selected) {
            if(isEmpty(selected)){
                return;
            }
            currentPage = 0;
            $scope.isLoadMore = true;
            selectedSubsId = selected.originalObject.id;
            getListQuestionAndDetail(selectedSubsId,"");
        };

        $scope.fillerSubject = function () {
            currentPage = 0;
            $scope.isLoadMore = true;
            if(isEmpty($('#autocompleteSubs').val())){
                selectedSubsId = "";
            }
            if(isEmpty(selectedSubsId)){
                init();
            }
            getListQuestionAndDetail(selectedSubsId,"");
        }

        $scope.nextQuestion = function (str) {
            $scope.QAErrorMsg="";
            if($scope.listQuestions == null && $scope.listQuestions.length == 0){
                return;
            }
            var index = 0;
            var len = $scope.listQuestions.length;
            for (var i = 0 ; i< len; i++){
                if($scope.currentPid ==  $scope.listQuestions[i].pid)
                {
                    index = i;
                    $scope.currentPid = $scope.listQuestions[i].pid;
                    break;
                }
            }
            if(str=='next'){
                if(index == len){
                    $scope.currentPid = $scope.listQuestions[0].pid;
                    getQuestionById($scope.currentPid);
                }
                else{
                    $scope.currentPid = $scope.listQuestions[index+1].pid;
                    getQuestionById($scope.currentPid);
                }
            }
            else {
                if(index == 0){
                    $scope.currentPid = $scope.listQuestions[len-1].pid;
                    getQuestionById($scope.currentPid);
                }
                else{
                    $scope.currentPid = $scope.listQuestions[index-1].pid;
                    getQuestionById($scope.currentPid);
                }
            }
        }

        function getListQuestionAndDetail(selectedSubsId,txtSearch) {
            managerQAService.getListQuestionQA(selectedSubsId, userId, 0, $scope.currentTab, LIMIT,txtSearch).then(function (data) {

                if (data.data.status) {
                    var result =  data.data.request_data_result;
                    if (result != null && result.length > 0) {
                        $scope.listQuestions = result;
                        lastQId = $scope.listQuestions[$scope.listQuestions.length - 1].qid;
                        $scope.currentPid = $scope.listQuestions[0].pid;
                        getQuestionById($scope.currentPid);
                        $scope.notFound = "";
                    }
                    else {
                        $scope.notFound = "Not found question";
                        $scope.questionDetail = null;
                        return;
                    }

                }

            });
        }
        $scope.stepsModel = [];
        $scope.filesArray = [];
        $scope.onFileSelect = function ($files) {
            $scope.QAErrorMsg = "";
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

        // get subject from default subject
        function getSubjectNameByIdQA(strSubjectId, listcate) {
            if (strSubjectId == null || strSubjectId === undefined) {
                return;
            }
            var subject = {};
            var listSubject = [];
            if (isEmpty(strSubjectId)) {
                listSubject.push(subject);
                return listSubject;
            }
            if (strSubjectId.indexOf(',') < 0) {
                for (var y = 0; y < listcate.length; y++) {
                    if (listcate[y].subjectId == strSubjectId || listcate[y].parentId == strSubjectId) {
                        subject.id = strSubjectId;
                        subject.name = listcate[y].subject;
                        subject.level = listcate[y].level;
                        subject.parentId = listcate[y].parentId;
                        listSubject.push(subject);
                        return listSubject;
                    }

                }
            }
            else {
                var list = strSubjectId.split(',');
                for (var i = 0; i < list.length; i++) {
                    for (var y = 0; y < listcate.length; y++) {
                        if (listcate[y].subjectId == list[i] || listcate[y].parentId == list[i]) {
                            subject = [];
                            subject.name = listcate[y].subject;
                            subject.id = listcate[y].subjectId;
                            subject.level = listcate[y].level;
                            subject.parentId = listcate[y].parentId;
                            listSubject.push(subject);
                        }

                    }
                }
            }

            return listSubject;
        }

    }]);