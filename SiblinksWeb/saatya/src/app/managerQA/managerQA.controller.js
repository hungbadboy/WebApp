brotControllers.controller('managerQAController', ['$scope', '$http', '$location', '$rootScope', '$timeout', '$log', 'HomeService', 'NotificationService',
    'StudentService', 'VideoService', 'myCache', 'managerQAService','$modal',
    function ($scope, $http, $location, $rootScope, $timeout, $log, HomeService, NotificationService, StudentService,
              VideoService, myCache, managerQAService,$modal) {
        var arrCategoryID="";
        var arrTopicID ="";

        init();

        var selectCategory = null;


        function init() {
            if (myCache.get("subjects") !== undefined) {
                $log.info("My cache already exists");
                $scope.subjects = myCache.get("subjects");
                $.each($scope.subjects,function (index,subj) {
                    if(subj.type =="1") {
                        arrCategoryID+=(subj.id)+",";
                    }
                    else {
                        arrTopicID+=(subj.id)+",";
                    }
                });
                getALQuestion();
            } else {
                HomeService.getAllCategory().then(function (data) {
                    if (data.data.status) {
                        $log.info("Get service subject with category");
                        $scope.subjects = data.data.request_data_result;
                        $.each($scope.subjects,function (index,subj) {
                            if(subj.type =="1") {
                                arrCategoryID+=(subj.id)+",";
                            }
                            else {
                                arrTopicID+=(subj.id)+",";
                            }
                        });
                        getALQuestion();
                        myCache.put("subjects", data.data.request_data_result);

                    }
                });
            }


        }

        function  getALQuestion() {
            //init();
            if(!arrCategoryID==""||!arrTopicID=="") {

                managerQAService.getListQuestionQA(arrCategoryID, arrTopicID).then(function (data) {
                    if (data.data.status) {
                        $log.info("Get service subject with category");
                        $scope.listQuestions = data.data.request_data_result;

                    }
                });
            }
        }
        $scope.openModalAnswer = function (qid) {
            var modalInstance = $modal.open({
                templateUrl: 'src/app/managerQA/popupAnswer.tpl.html',
                controller: 'popupAnswerController',
                resolve: {
                    q_id: function () {
                        return qid;
                    }
                }
            });
        };


        $scope.selectedSubject = function (selected) {
            selectCategory = selected;
        };

    }]);