
brotControllers.controller('managerQAController', ['$scope', '$http', '$location', '$rootScope', '$timeout', '$log', 'HomeService', 'NotificationService',
    'StudentService', 'myCache', 'managerQAService', 'QuestionsService','AnswerService','$document','$window',
    function ($scope, $http, $location, $rootScope, $timeout, $log, HomeService, NotificationService, StudentService,
              myCache, managerQAService, QuestionsService,AnswerService,$document,$window) {


        $scope.subjectId = "-1";
        var LIMIT = 200;
        var lastQId = "";
        var question_id = $location.search().pid;
        var userId = localStorage.getItem('userId');
        var defaultSubjectId = localStorage.getItem('defaultSubjectId');
        $scope.mentorAvatar  = localStorage.getItem('imageUrl');
        $scope.userId = userId;
        var typeOrderAnswer = "newest";
        var listDefaultSubjectId;
        $scope.subjectsChild = [];
        var oldImagePath = "";
        $scope.currentTab = 'all';
        $scope.textSearch = "";
        $scope.notFound = "";
        var selectedSubsId = "";
        var currentPage = 0;
        var currentPageAnswer = 0;
        $scope.isLoadMore = true;
        $scope.isLoadMoreAnswer =true;
        $scope.currentIndexAnswer = 0;
        var allSubjects = JSON.parse(localStorage.getItem('subjects'));
        $scope.subjectsParent = [];
        var oldImagePathEdited="";
        var aidEdit;
        $scope.isEdit = false;
        var isInit = true;
        var listSubject = "";
        init();


        function init() {

            var offset = 0;
            listDefaultSubjectId = getSubjectNameByIdQA(defaultSubjectId, allSubjects);
            listSubject = "";
            // get child subjectID from default subject
            for (var i = 0; i < listDefaultSubjectId.length; i++) {
                listSubject += listDefaultSubjectId[i].id;
                if (i < listDefaultSubjectId.length - 1) {
                    listSubject += ',';
                }
            }
            // add element All to list
            $scope.initSubject = {name:'All',id:''};
            listDefaultSubjectId.unshift($scope.initSubject);
            $scope.subjectsParent = listDefaultSubjectId;
            managerQAService.getListQuestionQA(selectedSubsId, userId, offset, $scope.currentTab, LIMIT,$scope.textSearch,listSubject).then(function (data) {
                // for (var i = 0; i < listDefaultSubjectId.length; i++) {
                //     if (listDefaultSubjectId[i].level == '0') {
                //         $scope.subjectsParent.push(listDefaultSubjectId[i]);
                //     }
                // }
                if (data.data.status) {
                    $scope.listQuestions = data.data.request_data_result;
                    if ($scope.listQuestions != null && $scope.listQuestions.length > 0) {
                        lastQId = $scope.listQuestions[$scope.listQuestions.length - 1].pid;
                        $scope.currentPid = $scope.listQuestions[0].pid;
                        if(!isEmpty(question_id)) {
                            $scope.currentPid = question_id;
                        }
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
        $timeout(function () {
            $('#autocompleteSubsQA_dropdown').width($('#autocompleteSubsQA').width());
        }, 300);

        $document.on('scroll', function() {
            // do your things like logging the Y-axis
            // var height = '73vh';
            var heighttab = $(window).height() - 305;
                heighttab_scroll = $(window).height() - 365;
            console.log(heighttab);
            // if($window.innerWidth < 1601){
                // height = '67vh';
                if ($window.scrollY > 70) {
                    $(".mentor-manage-qa-content .left-qa").css({"top":"105px"});
                    $(".mentor-manage-qa-content .left-qa .tab-answered .tab-content .tab-answered-tab").css({"height":+ heighttab + "px"});
                }
                else {
                    $(".mentor-manage-qa-content .left-qa").css({"top":"auto"});
                    $(".mentor-manage-qa-content .left-qa .tab-answered .tab-content .tab-answered-tab").css({"height":+ heighttab_scroll + "px"});
                }         
            
        });

        $scope.nextAnswer = function (str) {
            if (str == 'next') {
                if ($scope.currentIndexAnswer == $scope.listAnswer.length - 1) {
                    $scope.currentIndexAnswer = 0;

                } else {
                    $scope.currentIndexAnswer = $scope.currentIndexAnswer + 1;
                }

            }
            if (str == 'prev') {
                if ($scope.currentIndexAnswer == 0) {
                    $scope.currentIndexAnswer = $scope.listAnswer.length - 1;
                } else {
                    $scope.currentIndexAnswer = $scope.currentIndexAnswer - 1;
                }
            }
            $scope.currentAnswer = $scope.listAnswer[$scope.currentIndexAnswer];

        }
        
        $scope.loadMorePost = function () {
            if (!$scope.isLoadMore) {
                return;
            }
            currentPage++;
            var newOffset = currentPage * LIMIT;
            managerQAService.getListQuestionQA(selectedSubsId, userId, newOffset, $scope.currentTab, LIMIT,$scope.textSearch,listSubject).then(function (data) {
                var result = data.data;
                if (result && result.status) {
                    if (result.request_data_result != null && result.request_data_result.length > 0) {
                        $scope.listQuestions.push.apply($scope.listQuestions, result.request_data_result);
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
            QuestionsService.getAnswerByQid($scope.currentPid, typeOrderAnswer, LIMIT, newOffset,userId).then(function (data) {
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
            managerQAService.getListQuestionQA(selectedSubsId, userId, newOffset, $scope.currentTab, LIMIT,$scope.textSearch,listSubject).then(function (data) {
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
            $scope.currentAnswer = $scope.listAnswer[index];
            angular.element(document.getElementById('answer-detail')).modal();

        }

        $scope.convertName= function (name,userName) {
            var result = name;
            if(isEmpty(name)){
                result =  userName.split('@')[0];
            }
            return result;
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
            getListQuestionAndDetail(selectedSubsId,$scope.textSearch);
        }


        $scope.selectQuestion = function (qid) {
            if($scope.currentPid == qid){
                return;
            }
            cleanContentEdit();
            getQuestionById(qid);
        }

        $scope.loadTo = function () {
            angular.element(document.getElementById('listQA')).mCustomScrollbar('scrollTo','#qa'+$scope.currentPid);
        }

        $scope.cleanAnswer = function () {
            cleanContentEdit();
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
                cleanContentEdit();
                if(isEmpty(obj[0].firstName +obj[0].lastName)){
                    obj[0].firstName =  obj[0].userName.split('@')[0];
                }
                var question = {
                    "authorId": obj[0].authorID,
                    "question_id": obj[0].pid,
                    "question_text": obj[0].content,
                    "views": obj[0].numViews,
                    "question_creation_date": moment(obj[0].updateTime, 'YYYY-MM-DD h:mm:ss').startOf('hour').fromNow(),
                    "timeStamp": convertUnixTimeToTime(obj[0].TIMESTAMP),
                    "author": obj[0].userName,
                    "firstName": obj[0].firstName,
                    "lastName": obj[0].lastName,
                    "userName": obj[0].userName,
                    "avatar": obj[0].imageUrl,
                    "subject": obj[0].subject,
                    "subjectId": obj[0].subjectId,
                    "numview": obj[0].numViews,
                    "imagePath": detectMultiImage(obj[0].imagePath),
                    "numReplies" :obj[0].numReplies

                };
                $location.search('pid', $scope.currentPid);
                $scope.questionDetail = question;
            });
            QuestionsService.getAnswerByQid(qid, typeOrderAnswer, LIMIT, 0,userId).then(function (data) {
                var answers = data.data.request_data_result;
                $scope.listAnswer = [];
                $scope.isLoadMoreAnswer = true;
                $scope.listAnswer = answers;

                if($scope.listAnswer.length > 0){
                    $scope.currentIndexAnswer = 0;
                    $scope.currentAnswer = $scope.listAnswer[ $scope.currentIndexAnswer];
                }
                else {
                    $scope.currentAnswer = null;
                }
            });
        }
        $scope.convertToArrayImage = function (str) {
            return detectMultiImage(str);
        }


        $scope.removeAnswer = function (aid) {
            $rootScope.$broadcast('open');
            managerQAService.removeAnswer(aid).then(function (data) {
                if(data.data.status =='true'){
                    QuestionsService.getAnswerByQid($scope.currentPid, typeOrderAnswer, "", "",userId).then(function (data) {
                        var answers = data.data.request_data_result;
                        $scope.isLoadMoreAnswer = true;
                        $scope.listAnswer = answers;
                        angular.element(document.getElementById('answer-detail')).modal('hide');

                        if($scope.isEdit){
                            if(aid == aidEdit){
                                cleanContentEdit();
                            }
                        }

                    });
                }
                $rootScope.$broadcast('close');
            });
        }

        $scope.editAnswer = function (aid) {
            var answeredit;
            for(var i = 0; i < $scope.listAnswer.length; i++){
                if($scope.listAnswer[i].aid == aid){
                    answeredit = $scope.listAnswer[i];
                    break;
                }
            }
            if(isEmpty(answeredit)){
                $scope.QAErrorMsg='Can not edit this answer';
                return;
            }

            aidEdit = answeredit.aid;
            $('#txtAnswer').val(answeredit.content);
            $('#txtAnswer').focus();
            $scope.isEdit = true;
            oldImagePath = answeredit.imageAnswer;
            $scope.imagePathOld = [];
            $scope.filesArray = [];
            $scope.imagePathOld = detectMultiImage(answeredit.imageAnswer);

        }

        $scope.updateAnswer = function () {
            var fd = new FormData();
            var totalSize = 0;
            if ($scope.filesArray != null) {
                for (var i = 0; i < $scope.filesArray.length; i++) {
                    file = $scope.filesArray[i];
                    totalSize += file.size;
                    fd.append('file', file);
                }
            }
            var contentAnswer = $('#txtAnswer').val();

            var totalLenth = $scope.filesArray.length;
            if(isEmpty(oldImagePathEdited)){
                oldImagePathEdited = oldImagePathEdited.substr(0,oldImagePathEdited.length-1);
                totalLenth += oldImagePathEdited.split(";");
            }

            if (totalLenth > MAX_IMAGE) {
                $scope.QAErrorMsg='You cannot attach more than ' + MAX_IMAGE +' images';
                return;
            }

            if(totalSize > MAX_SIZE_IMG_UPLOAD){
                $scope.QAErrorMsg='Image over 10M';
                return;
            }
            if(isEmpty(contentAnswer)){
                $scope.QAErrorMsg='Please enter your answer';
                return;
            }
            fd.append('aid', aidEdit);
            fd.append('content', contentAnswer);
            fd.append('oldImagePathEdited', oldImagePathEdited);
            fd.append('oldImagePath', oldImagePath);
            $rootScope.$broadcast('open');
            managerQAService.updateAnswer(fd).then(function (data) {
                if (data.data.status == "true") {
                    QuestionsService.getAnswerByQid($scope.currentPid, typeOrderAnswer, "", "",userId).then(function (data) {
                        var answers = data.data.request_data_result;
                        $scope.isLoadMoreAnswer = true;
                        $scope.listAnswer = answers;
                        cleanContentEdit();
                    });
                }
                else {
                    $scope.QAErrorMsg =data.data.request_data_result;
                }
                $rootScope.$broadcast('close');
            });
        }

        $scope.answerQuestion = function (pid) {
            $scope.QAErrorMsg="";
            var content = $('#txtAnswer').val();
            if (isEmpty(content)) {
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
                $scope.QAErrorMsg='You cannot attach more than ' + MAX_IMAGE +' images';
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
                $scope.QAErrorMsg='Image over 5M';
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
            $rootScope.$broadcast('open');
            managerQAService.postAnswer(fd).then(function (data) {
                if (data.data.status == "true") {
                    QuestionsService.getAnswerByQid($scope.currentPid, typeOrderAnswer, "", "",userId).then(function (data) {
                        var answers = data.data.request_data_result;
                        $scope.isLoadMoreAnswer = true;
                        $scope.listAnswer = answers;
                        cleanContentEdit();
                    });
                }
                else {
                    $scope.QAErrorMsg =data.data.request_data_result;
                }
                $rootScope.$broadcast('close');
            });

        };
        
        function cleanContentEdit() {
            $('#txtAnswer').val("");
            $scope.filesArray = [];
            $scope.imagePathOld = [];
            $scope.isEdit = false;
        }

        $scope.selectedSubject = function (selected) {
            if(isEmpty(selected) || isInit){
                isInit = false;
                return;
            }
            currentPage = 0;
            $scope.isLoadMore = true;
            selectedSubsId = selected.originalObject.id;
            getListQuestionAndDetail(selectedSubsId,$scope.textSearch);
        };

        $scope.changeWidth = function () {
            $('#autocompleteSubsQA_dropdown').width($('#autocompleteSubsQA').width());
        }

        $scope.clickSubject = function (subjectId,subjectName) {
            $scope.$broadcast('angucomplete-alt:changeInput', 'autocompleteSubsQA', {name:subjectName,id:subjectId});
        }

        $scope.fillerSubject = function () {
            currentPage = 0;
            $scope.isLoadMore = true;
            if(isEmpty($('input#autocompleteSubsQA').val())){
                selectedSubsId = "";
            }
            if(!isEmpty($('input#autocompleteSubsQA').val())&& isEmpty(selectedSubsId)){
                $scope.notFound = "Not found question";
                $scope.listQuestions = null;
                $scope.questionDetail = null;
                return;
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
            cleanContentEdit();
            angular.element(document.getElementById('listQA')).mCustomScrollbar('scrollTo','#qa'+$scope.currentPid);
        }

        function getListQuestionAndDetail(selectedSubsId,txtSearch) {
            managerQAService.getListQuestionQA(selectedSubsId, userId, 0, $scope.currentTab, LIMIT,txtSearch,listSubject).then(function (data) {
                cleanContentEdit();
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
                        $scope.listQuestions = null;
                        $scope.questionDetail = null;
                        return;
                    }

                }

            });
        }
        $scope.filesArray = [];
        $scope.onFileSelect = function ($files,errFiles) {
            $scope.QAErrorMsg = "";
            var errFile = errFiles && errFiles[0];
            if(!isEmpty(errFile)){
                $scope.QAErrorMsg = 'File wrong format. Please select file image!';
                return;
            }
            if ($files!=null && $files.length > MAX_IMAGE){
                $scope.QAErrorMsg = 'You cannot attach more than ' + MAX_IMAGE +' images';
                return ;
            }
            if ($files != null) {
                for (var i = 0; i < $files.length; i++) {
                    var file = $files[i];
                    $scope.filesArray.push(file);
                }
            }
        };

        $scope.currentIndexImage = 0;
        $scope.zoomImage = function (img, index) {
            $scope.currentIndexImage = index;
            $scope.currentImage = img;
            angular.element(document.getElementById('modalImage')).modal();
        };
        $scope.setCurrentIndex = function (index) {
            $scope.currentIndexImage = index;
        };
        $scope.nextImageSlide = function (str) {
            if (isEmpty($scope.currentImage) || $scope.currentImage.length == 1) {
                return;
            }
            var len = $scope.currentImage.length;
            if (str == 'next') {
                if ($scope.currentIndexImage == len - 1) {
                    $scope.currentIndexImage = 0;
                }
                else {
                    $scope.currentIndexImage = $scope.currentIndexImage + 1;
                }
            } else {
                if ($scope.currentIndexImage == 0) {
                    $scope.currentIndexImage = len - 1;
                }
                else {
                    $scope.currentIndexImage = $scope.currentIndexImage - 1;
                }
            }
        };

        $scope.transferPage = function (path, id) {
            angular.element(document.getElementById('answer-detail')).modal('toggle');
            if(id+'' == userId){
                $timeout(function () {
                    $window.location.href ='#/mentor/mentorProfile';
                }, 300);

                return;
            }
            $timeout(function () {
                $window.location.href ='#'+path+id;
            }, 300);

        }

        // show confirm when click other page
        // $scope.$on('$locationChangeStart', function (event) {
        //     // var answer = confirm("Are you sure you want to leave this page?")
        //     // if (!answer) {
        //     //     event.preventDefault();
        //     // }
        //     if (isEmpty($('#txtAnswer').val()) && $scope.filesArray.length == 0) {
        //         return;
        //     }
        //     event.preventDefault();
        //     angular.element(document.getElementById('dialog-confirm')).dialog({
        //         height: "auto",
        //         width: 400,
        //         modal: true,
        //
        //         buttons: {
        //             "Leave this page": {
        //                 'class':'btn btn-primary',
        //                 text:'Leave this page',
        //                 click:function () {
        //                     angular.element(document.getElementById('dialog-confirm')).dialog("close");
        //                     event.target.submit();
        //
        //             }},
        //             Cancel: function() {
        //                 angular.element(document.getElementById('dialog-confirm')).dialog("close");
        //             },
        //
        //         },
        //         create:function () {
        //             $(this).closest(".ui-dialog")
        //                 .find(".ui-button:first") // the first button
        //                 .addClass("btn btn-primary");
        //         }
        //     });
        // });
       
        $scope.removeImg = function (index) {
            $scope.filesArray.splice(index, 1);

        }
        $scope.removeImgOld = function (index) {
            if(!isEmpty($scope.imagePathOld)){
                oldImagePathEdited += $scope.imagePathOld[index];
            }
            $scope.imagePathOld.splice(index, 1);

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

        $scope.likeAnswer = function (aid) {
            if (!isEmpty(userId) && userId != -1) {
                AnswerService.likeAnswer(userId, aid + "").then(function (data) {
                    if (data.data.status == 'true') {
                        if (data.data.request_data_type == "like") {
                            $('#heart'+aid).addClass('like-h');
                        }
                        else {
                            $('#heart'+aid).removeClass('like-h');
                        }
                    }
                });
            }
        };

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
