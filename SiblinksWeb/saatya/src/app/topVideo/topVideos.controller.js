brotControllers.controller('TopVideoCtrl', ['$scope', '$http', 'VideoService', function($scope, $http, VideoService) {
	var LIMIT_TOP_VIDEOS = 4;
	var OFFSET = 0;
	init();
	function init() {
		//get videos by top rate
	    VideoService.getVideoByRate(LIMIT_TOP_VIDEOS, OFFSET).then(function (data) {
	        if (data.data.status) {
	            var result_data = data.data.request_data_result;
	            if (result_data) {
	                var listVideoRate = [];
	                for (var i = 0; i < result_data.length; i++) {
	                    var element = result_data[i];
	                    var video = {};
	                    video.title = element.title;
	                    video.image = element.image;
	                    video.url = element.url;
	                    video.numRatings = element.numRatings;
	                    video.averageRating = element.averageRating;
	                    video.uid = element.uid;
	                    video.vid = element.vid;
	                    listVideoRate.push(video);
	                }
	            }
	            $scope.listVideoRate = listVideoRate;
	        }
	
	    });
	}
}]);