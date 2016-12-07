brotControllers.controller('StudentSubcribedCtrl',['$scope', 'VideoService',function ($scope, VideoService) {
	var userId = localStorage.getItem('userId');
	var subjects = JSON.parse(localStorage.getItem('subjects'));
	$scope.totalPageSubscribed;
	$scope.countAll;
	$scope.pageSubscribed = 1;
	$scope.defaultLimit = 5;
	$scope.listMentorBySubs = [];
    init();

	function init() {
	  	getMentorSubscribedInfo();
	}
	
	function getMentorSubscribedInfo() {
	    VideoService.getMentorSubscribe(userId, $scope.pageSubscribed, $scope.defaultLimit, true).then(function (data) {
	        var data_result = data.data.request_data_result;
	        if (data.data.status=='true' && (data.data.request_data_result != null && data.data.request_data_result.length > 0)) {
	            for (var i = 0; i < data_result.length; i++) {
	                if (subjects != null || subjects !== undefined) {
	                    if (data_result[i].defaultSubjectId !== null && data_result[i].defaultSubjectId !== undefined) {
	                    	data_result[i].listSubject = getSubjectNameById(data_result[i].defaultSubjectId, subjects);
	                    }
	                } else {
	                	data_result[i].listSubject.push([{id: 1, subject: "None"}])
	                }
	            }
	            $scope.listMentorBySubs = $scope.listMentorBySubs.concat(data_result);
	            $scope.countAll = data.data.count;
	            $scope.totalPageSubscribed = Math.ceil($scope.countAll / $scope.defaultLimit);
	        }
	    });
	}
	
	/**
     * Cancel subscriber
     */
    $scope.unSubscribeMentor = function (idx, mentorId) {
        if (isEmpty(userId)) {
            return;
        }
        VideoService.setSubscribeMentor(userId, mentorId).then(function (response) {
            if (response.data.status == "true") {
                if (response.data.request_data_type == "unsubs") {
                    $scope.isSubscribe = 1;
                    $scope.listMentorBySubs.splice(idx, 1);
                    $scope.countAll=$scope.countAll -1;
                    $('#num-subscribed').html($scope.countAll);
                    $scope.totalPageSubscribed = Math.ceil($scope.countAll / $scope.defaultLimit);
                    if($scope.listMentorBySubs.length == $scope.defaultLimit -1 && $scope.countAll >= $scope.defaultLimit) {
                    	// reload
                    	$scope.listMentorBySubs = [];
                    	$scope.pageSubscribed = 1;
                    	init();
                    }
                }
                else {
                    $scope.isSubscribe = -1;
                }
            }
        });
    };
    
    $scope.loadMoreSubscribed = function() {
    	$scope.pageSubscribed = $scope.pageSubscribed + 1;
    	var newOffset = $scope.defaultLimit * $scope.pageSubscribed;
    	if ($scope.pageSubscribed > $scope.totalPageSubscribed) {
    		return;
    	}
    	getMentorSubscribedInfo();
    }
}]);