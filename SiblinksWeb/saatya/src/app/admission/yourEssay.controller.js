brotControllers.controller('yourEssayController', ['$scope', '$rootScope', '$log', '$location', '$http', '$timeout', 'AdmissionService', 'myCache', '$sce', 'uploadEssayService', '$window',
    function ($scope, $rootScope, $log, $location, $http, $timeout, AdmissionService, myCache, $sce, uploadEssayService, $window) {

        var userId = localStorage.getItem('userId');
        var LIMIT = 6;
        var OFFSET = 0;
        var subjects = JSON.parse(localStorage.getItem('subjects'));
        var eid = $location.search().eid;
        $scope.listEssaysSize = 0;
        $scope.defaultLimitEssay = 6;
        var isHasLoadMoreEssay = false;
        $scope.listEssays = [];
        init();

        function init() {
            getEssays(userId, LIMIT, OFFSET);
            // get user rating
            uploadEssayService.getUserRatingEssay(userId, $scope.uploadEssayId).then(function (data) {
                if (data.data.status == 'true') {
                    if (data.data.request_data_result.length > 0) {
                    	$scope.rating = data.data.request_data_result[0].rating;
                    } else {
                    	$scope.rating = 0;
                    }
                }
            });
        }


        function getEssays(userId, LIMIT, OFFSET) {
            if(!userId){
                return;
            }
            uploadEssayService.getEssayByStudentId(userId, LIMIT, OFFSET).then(function (data) {
                if (data.data.status) {
                    if(data.data.request_data_result){
                        var result = data.data.request_data_result;
                        $scope.listEssays = isHasLoadMoreEssay ? $scope.listEssays.concat(result) : result;
                        $scope.listEssaysSize =  $scope.listEssays.length;
                    }
                }
            });
        }

        $rootScope.$on('reloadYourEssay', function (event, message) {
            if (message == 'load') {
                init();
            }
        });

        $scope.editEssay = function () {
            //$scope.$emit('editYourEssay', $scope.currentEssay);
            localStorage.setItem('currentEssay', JSON.stringify($scope.currentEssay));
            angular.element(document.getElementById('essay-detail')).modal('hide');
            $window.location.href = '/#/college_admission?tab=3';
            $window.location.reload();
        };

        $scope.showModal = function (index) {
        	$scope.index = index;
            if (isEmpty(index)) {
                return;
            }
            var uploadEssayId = eid;
            if (index != -1) {
                uploadEssayId = $scope.listEssays[index].uploadEssayId;
            }
            if (isEmpty(uploadEssayId)) {
                $scope.currentEssay = null;
            }
            uploadEssayService.getEssayById(uploadEssayId).then(function (data) {
                if (data.data.status) {
                    $scope.currentEssay = data.data.request_data_result[0];
                    if (!isEmpty($scope.currentEssay.status == 'P')) {
                        uploadEssayService.getMentorEssayByUid($scope.currentEssay.mentorId).then(function (data) {
                            $scope.currentMentor = data.data.request_data_result[0];
                            $scope.uploadEssayId=uploadEssayId;
                            // $scope.currentMentor.defaultSubject = getSubjectNameById($scope.currentMentor.defaultSubjectId, subjects);
                        });
                    }
                    angular.element(document.getElementById('essay-detail')).modal();
                }
            });
        };

        $scope.transferPage = function (path) {
            angular.element(document.getElementById('essay-detail')).modal('toggle');
            $timeout(function () {
                $window.location.href = '#'+path;
            }, 300);
        };

        $scope.removeEssay = function (essayId) {
        	try {
        		$rootScope.$broadcast('open');
	            uploadEssayService.removeEssay(essayId).then(function (data) {
	                if (data.data.status == 'true') {
	                    angular.element(document.getElementById('essay-detail')).modal('toggle');
	                    $window.location.reload();
	                }
	            });
        	} catch(er) {
            	console.log(er.description);
            } finally {
            	$rootScope.$broadcast('close');
            }
        };

        // Notification view essay
        if (!isEmpty(eid)) {
            $scope.showModal(-1);
        }

        var currentPage = 0;
        $scope.loadMoreEssay = function () {
            currentPage++;
            var newOffset = $scope.defaultLimitEssay * currentPage;
            if(newOffset > $scope.listEssaysSize){
                return;
            }
            isHasLoadMoreEssay = true;
            if(userId){
                getEssays(userId, $scope.defaultLimitEssay, newOffset);
            }
        };
        
        /**
         * Rating article
         */
        $scope.rateFunction = function (rate) {
            var ratenumOld = $scope.rateNum;
            if (isEmpty(userId) || userId == "-1") {
            	window.location.href ='#/student/signin?continue='+encodeURIComponent($location.absUrl());
                return;
            }
            try {
            	$rootScope.$broadcast('open');
            	uploadEssayService.rateEssay($scope.uploadEssayId, userId, parseInt(rate)).then(function (data) {
            		if (data.data.status == 'true') {
            			$scope.currentEssay.averageRating=parseInt(rate);
            			$scope.listEssays[$scope.index].averageRating=parseInt(rate);
            		}
            	}); 
            	
            } catch (e){
            	console.log(e.description)
            } finally {
            	$rootScope.$broadcast('close');
            }
        }
    }]);