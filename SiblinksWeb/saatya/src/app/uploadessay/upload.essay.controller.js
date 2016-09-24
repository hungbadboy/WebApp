
brotControllers.controller('UploadEssayCtrl', ['$scope', '$rootScope', '$routeParams', '$log', '$upload', '$location', '$http', '$timeout', 'EssayService', 'StudentService',
	function ($scope, $rootScope, $routeParams, $log, $upload, $location, $http, $timeout, EssayService, StudentService) {
	// $scope.page = $routeParams.page;
	var userId = localStorage.getItem('userId');
	var userType = localStorage.getItem('userType');
	var totalUpdateEssay;
	var elementLast = [];
	var essayRemoveId;
	var eventRemove;
	var click = 1;
	$scope.essayDelete = '';
	$scope.countDiscussion = '';
	$rootScope.page = 1;

	init($rootScope.page);

	$scope.showWithPage = function(page) {
		$rootScope.page = page;
		init($rootScope.page);
	};

	$scope.addClassActive = function(page) {
		if(page == $rootScope.page) {
			return 'span_active';
		}
		return '';
	};

	$scope.deleteEssay = function(event, essayId, essayName) {
		$scope.essayDelete = essayName;
		$('#delete').modal('show');
		essayRemoveId = essayId;
		eventRemove = event;
	};

	$scope.deleteEssayWithDialog = function() {
		var eleParent = $(eventRemove.currentTarget).parent().parent();

		EssayService.removeEssay(essayRemoveId).then(function(data) {
			if(data.data.request_data_result == 'Done') {
				$(eleParent).remove();
				EssayService.getListUpdateEssay(userId, $scope.page).then(function(data) {
					var objs = data.data.request_data_result;
					elementLast = $scope.listPage.pop();
					if(objs == null) {
						objs = [];
					}
					for(var i = 0; i < objs.length; i++) {
						var obj = objs[i];
						obj.reviewImage = EssayService.getImageReviewEssay(obj.uploadEssayId);
						var d = moment(obj.docSubmittedDate, 'YYYY-MM-DD hh:mm:ss').format('MM/DD/YYYY');
						obj.docSubmittedDate = d;
					}
					$scope.essays = objs;
					
					if(Math.ceil(data.data.count / 5) == elementLast) {
						$scope.listPage.push(elementLast);
					}
				});
			}
		});
	};

	$scope.hideHowToWorks = function() {
		$('#main .tutorial').addClass('hide');
	};

	$scope.onFileSelect = function($files) {
		$('#divProgress').removeClass('hide');
		var file = $files[0];
		var fd = new FormData();
		fd.append('file', file);
		fd.append('name', file.name);
		fd.append('userType', 'S');
		fd.append('userId', userId);
		var url = NEW_SERVICE_URL + 'essay/upload';
		$http.post(url, fd, {
			crossDomain:true,
			transformRequest: angular.identity,
			headers: {'Content-Type': undefined}
		})
		.success(function(data) {
			if(data.status === 'true') {
				$scope.progressMax = 100;
				$scope.messageUpload = UPLOAD_SUCCESS.uploadSuccess;
				$rootScope.myVar = !$scope.myVar;
				$timeout(function () {
					$rootScope.myVar = false;
					$scope.progressMax = 0;
					$('#divProgress').addClass('hide');
				}, 2500);

				EssayService.getListUpdateEssay(userId, $rootScope.page).then(function(data) {
					var objs = data.data.request_data_result;
					$scope.countEssay = data.data.count;
					totalUpdateEssay = Math.ceil(data.data.count / 5);
					if(Math.ceil(data.data.count) > 5) {
						elementLast = $scope.listPage.pop();
						if((data.data.count / 5) > elementLast && (data.data.count / 5) < (elementLast + 1)) {
							for(var j = elementLast; j <= (elementLast + 1); j++) {
								$scope.listPage.push(j);
							}
						} else {
							$scope.listPage.push(elementLast);
						}
						if(objs == null) {
							objs = [];
						}
						for(var i = 0; i < objs.length; i++) {
							var obj = objs[i];
							obj.reviewImage = EssayService.getImageReviewEssay(obj.uploadEssayId);
							var d = moment(obj.docSubmittedDate, 'YYYY-MM-DD hh:mm:ss').format('MM/DD/YYYY');
							obj.docSubmittedDate = d;
						}
					}
					$scope.essays = objs;
				});
			} else {
				$scope.messageUploadError = data.request_data_result;
				$rootScope.myVarE = !$scope.myVarE;
				$timeout(function () {
    $rootScope.myVarE = false;
    $scope.progressMax = 0;
    $('#divProgress').addClass('hide');
				}, 3000);
				$('#divProgress').addClass('hide');
			}
		});
	};

	$scope.addEssayDiscussion = function() {
		if($scope.essayDiscussion != null) {
			EssayService.postEssayDiscusion(userId, $scope.essayDiscussion).then(function(data) {
				if(data.data.request_data_result == 'Done') {
					EssayService.getEssayDiscussion(userId, 1, 5).then(function(data) {
						var objs = data.data.request_data_result;
						if(objs == null) {
							objs = [];
						}	

						for(var i = 0; i < objs.length; i++) {
							var obj = objs[i];
							obj.creationDate = convertUnixTimeToTime(obj.creationDate);
							obj.imageUrl = StudentService.getAvatar(userId);
						}
						$scope.essay_discutionses = objs;
					});
				}
			});
			$scope.essayDiscussion = null;
			if($scope.feature == 1) {
				$scope.feature = 0;
			}
		}
	};
	
	$scope.loadMoreEssayDiscussion = function() {
		click++;
		EssayService.getEssayDiscussion(userId, click, 5).then(function(data) {
			var objs = data.data.request_data_result;
			if(objs == null) {
				objs = [];
			}
			for(var i = 0; i < objs.length; i++) {
				var obj = objs[i];
				obj.reviewImage = EssayService.getImageReviewEssay(obj.uploadEssayId);
				obj.creationDate = convertUnixTimeToTime(obj.creationDate);
				obj.imageUrl = StudentService.getAvatar(userId);

				$scope.essay_discutionses.push(obj);	
			}
		});

		EssayService.getEssayDiscussion(userId, click + 1, 5).then(function(data) {
			var feature = data.data.request_data_result;
			if(feature.length === 0) {
				$scope.feature = 1;
			}
		});
	};

	$scope.dragOverClass = function($event) {
		var items = $event.dataTransfer.items;
		var hasFile = false;
		if(items != null) {
			for(var i = 0 ; i < items.length; i++) {
				if (items[i].kind == 'file') {
					hasFile = true;
					break;
				}
			}
		} else {
			hasFile = true;
		}
		return hasFile ? "dragover" : "dragover-err";
	};

	$scope.prevPage = function() {
		if($rootScope.page > 1) {
      $rootScope.page = parseInt($rootScope.page, 10) - 1;
      init($rootScope.page);
    }  
	};

	$scope.nextPage = function() {
		if($rootScope.page < totalUpdateEssay) {
      $rootScope.page = parseInt($rootScope.page, 10) + 1;
      init($rootScope.page);
    }  
	};

	function init (page) {

		EssayService.getListUpdateEssay(userId, page).then(function(data) {
			var objs = data.data.request_data_result;
			$scope.countEssay = data.data.count;
			totalUpdateEssay = Math.ceil(data.data.count / 5);

			showPage(totalUpdateEssay, page, function(response) {
        $scope.listPage = response;
      });

			if(objs == null) {
				objs = [];
			}
			for(var i = 0; i < objs.length; i++) {
				var obj = objs[i];
				var d = moment(obj.docSubmittedDate, 'YYYY-MM-DD hh:mm:ss').format('MM/DD/YYYY');
				obj.reviewImage = EssayService.getImageReviewEssay(obj.uploadEssayId);
				obj.docSubmittedDate = d;
			}
			$scope.essays = objs;
			
		});

		EssayService.getEssayDiscussion(userId, 1, 5).then(function(data) {
			var objs = data.data.request_data_result;
			$scope.countDiscussion = data.data.count;

			if(objs == null) {
				objs = [];
			}

			for(var i = 0; i < objs.length; i++) {
				var obj = objs[i];
				obj.creationDate = convertUnixTimeToTime(obj.creationDate);
				obj.imageUrl = StudentService.getAvatar(userId);
			}
			$scope.essay_discutionses = objs;
		});
	}

	$scope.essayDetail = function(eid) {
		$location.url('/essay_detail/' + eid);
	};

}]);