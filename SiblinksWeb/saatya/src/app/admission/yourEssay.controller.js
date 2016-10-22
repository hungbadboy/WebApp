brotControllers.controller('yourEssayController', ['$scope', '$rootScope', '$log', '$location', '$http', '$timeout', 'AdmissionService', 'myCache', '$sce', 'uploadEssayService', '$window',
    function ($scope, $rootScope, $log, $location, $http, $timeout, AdmissionService, myCache, $sce, uploadEssayService, $window) {

        var userId = localStorage.getItem('userId');
        var LIMIT = "";
        var OFFSET = "";
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
                        });
                    }
                    angular.element(document.getElementById('essay-detail')).modal();
                }
            });
        }

    }]);