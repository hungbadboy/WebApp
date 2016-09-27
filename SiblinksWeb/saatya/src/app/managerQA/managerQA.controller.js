brotControllers.controller('managerQAController', ['$scope', '$http', '$location', '$rootScope', '$timeout', '$log', 'HomeService', 'NotificationService',
    'StudentService', 'myCache', 'managerQAService', 'QuestionsService',
    function ($scope, $http, $location, $rootScope, $timeout, $log, HomeService, NotificationService, StudentService,
               myCache, managerQAService, QuestionsService) {


        $scope.subjectId = "-1";
        var LIMIT = 10;
        var lastQId = "";
        var userId = localStorage.getItem('userId');
        $scope.userId = userId;
        var type = "";
        init();


        function init() {
            managerQAService.getListQuestionQA("-1", userId, lastQId, type, LIMIT).then(function (data) {
                var subjects = myCache.get("subjects");
                $scope.subjectsParent = [];
                $scope.subjectsChild = [];
                for(var i = 0; i < subjects.length;i++){
                    if(subjects[i].level == '1'){
                        $scope.subjectsParent.push(subjects[i]);
                    }
                    else {
                        $scope.subjectsChild.push(subjects[i]);
                    }
                }
                if (data.data.status) {
                    $scope.listQuestions = data.data.request_data_result;
                    if ($scope.listQuestions.length > 0) {
                        lastQId = $scope.listQuestions[$scope.listQuestions.length - 1].qid;
                    }

                }
            });
        }

        $scope.selectParentChange = function () {
            $scope.subjectsChild = {};
            for(var i = 0; i < $scope.subjectsParent.length;i++){
                var sub =  $scope.subjectsParent[i];
                if(sub.parentId == $scope.selectedParent.parentId){
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
            if($scope.currentType == type){
                return ;
            }
            $scope.currentType = type;

            managerQAService.getListQuestionQA($scope.subjectId, userId, lastQId, type, LIMIT).then(function (data) {
                if (data.data.status) {
                    $scope.listQuestions = data.data.request_data_result;
                    if ($scope.listQuestions.length > 0) {
                        lastQId = $scope.listQuestions[$scope.listQuestions.length - 1].qid;
                    }
                }
            });
        }
        $scope.selectQuestion = function (qid) {
            QuestionsService.updateViewQuestion(qid, "view").then(function (data) {
            });
            QuestionsService.getAnswerByQid(qid, "", "", "").then(function (data) {
                if(data.data.status) {
                    $scope.answers = data.data.request_data_result;
                }

            });
        }

        // $scope.openModalAnswer = function (qid) {
        //     var modalInstance = $modal.open({
        //         templateUrl: 'src/app/managerQA/popupAnswer.tpl.html',
        //         controller: 'popupAnswerController',
        //         resolve: {
        //             q_id: function () {
        //                 return qid;
        //             }
        //         }
        //     });
        // };


        $scope.selectedSubject = function (selected) {
            selectCategory = selected;
        };

    }])
;