brotControllers.controller('yourEssayController', ['$scope', '$rootScope', '$log', '$location', '$http', '$timeout', 'AdmissionService', 'myCache', '$sce', 'uploadEssayService', '$window',
    function ($scope, $rootScope, $log, $location, $http, $timeout, AdmissionService, myCache, $sce, uploadEssayService, $window) {

        var userId = localStorage.getItem('userId');
        var LIMIT = "";
        var OFFSET = "";
        var subjects = JSON.parse(localStorage.getItem('subjects'));
        init();

        function init() {
            uploadEssayService.getEssaybByStudentId(userId,LIMIT,OFFSET).then(function (data) {
                if (data.data.status) {
                    $scope.listEssays = data.data.request_data_result;
                }
            });
        }
        
        $scope.convertUnixTimeToTime = function (datetime) {
            return convertUnixTimeToTime(datetime);
        }
        $rootScope.$on('reloadYourEssay', function(event, message) {
            if(message == 'load'){
                init();
            }
        });

        $scope.editEssay = function () {
            //$scope.$emit('editYourEssay', $scope.currentEssay);
            localStorage.setItem('currentEssay',JSON.stringify($scope.currentEssay));
            angular.element(document.getElementById('essay-detail')).modal('hide');
            $window.location.href = '/#/college_admission?tab=3';
            $window.location.reload();
        }
        
        $scope.showModal = function (index) {
            var uploadEssayId = $scope.listEssays[index].uploadEssayId;
            if(isEmpty(uploadEssayId)){
                $scope.currentEssay = null;
            }
            uploadEssayService.getEssayById(uploadEssayId).then(function (data) {
                if (data.data.status) {
                    $scope.currentEssay  = data.data.request_data_result[0];
                    if(!isEmpty($scope.currentEssay.status == 'P')){
                        uploadEssayService.getMentorEssayByUid($scope.currentEssay.mentorId).then(function (data) {
                        $scope.currentMentor = data.data.request_data_result[0];
                            // $scope.currentMentor.defaultSubject = getSubjectNameById($scope.currentMentor.defaultSubjectId, subjects);
                        });
                    }
                    angular.element(document.getElementById('essay-detail')).modal();
                }
            });
        }

        $scope.transferPage = function (path) {
            angular.element(document.getElementById('essay-detail')).modal('toggle');
            $timeout(function () {
                $window.location.href = '#/mentor/mentorProfile';
            }, 300);
        }

        $scope.removeEssay = function (essayId) {
            uploadEssayService.removeEssay(essayId).then(function (data) {
                if(data.data.status =='true'){
                    angular.element(document.getElementById('essay-detail')).modal('toggle');
                    init();
                }
            });
        }

    }]);