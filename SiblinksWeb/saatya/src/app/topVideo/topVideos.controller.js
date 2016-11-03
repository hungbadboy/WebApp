brotControllers.controller('TopVideoCtrl', ['$scope', '$http', 'VideoService', function($scope, $http, VideoService) {
	var LIMIT_TOP_VIDEOS = 4;
	var OFFSET = 0;
	init();
	function init() {
		//get videos by top rate
	    VideoService.getVideoByRate(LIMIT_TOP_VIDEOS, OFFSET).then(function (data) {
	        if (data.data.status) {
	            var result_data = data.data.request_data_result;
	            	$scope.listVideoRate = result_data
	            } else {
	            	$scope.listVideoRate ={};
	            }
	    });
	}
}]);