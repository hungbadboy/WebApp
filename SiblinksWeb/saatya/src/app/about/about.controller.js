brotControllers.controller('AboutCtrl',['$scope','MentorService', 'VideoService', function ($scope, MentorService, VideoService) {
	var LIMIT_TOP_MENTORS = 5;
	var LIMIT_TOP_VIDEOS = 4;
	var OFFSET = 0;
	init();
	
	function init() {
		 //get top mentors by subcribe
	    MentorService.getTopMentorsByLikeRateSubcrible(LIMIT_TOP_MENTORS, OFFSET, 'subcribe', '-1').then(function (data) {
	        var data_result = data.data.request_data_result;
	        if (data_result) {
	            var listTopMentors = [];
	            for (var i = 0; i < data_result.length; i++) {
	                var mentor = {};
	                mentor.userid = data_result[i].userid;
	                mentor.userName = data_result[i].userName ? data_result[i].userName : '';
	                mentor.lastName = data_result[i].lastName ? data_result[i].lastName : '';
	                mentor.firstName = data_result[i].firstName ? data_result[i].firstName : '';
	                mentor.fullName = mentor.firstName + ' ' +mentor.lastName;
	                mentor.imageUrl = data_result[i].imageUrl;
	                mentor.numlike = data_result[i].numlike;
	                mentor.numsub = data_result[i].numsub;
	                mentor.numvideos = data_result[i].numvideos;
	                mentor.isOnline = data_result[i].isOnline;
	                mentor.defaultSubjectId = data_result[i].defaultSubjectId;
	                if(data_result[i].defaultSubjectId !== null && data_result[i].defaultSubjectId !== undefined) {
	                    mentor.listSubject = getSubjectNameById(data_result[i].defaultSubjectId, $scope.subjects);
	                }
	                mentor.numAnswers = data_result[i].numAnswers;
	                listTopMentors.push(mentor);
	            }
	        }
	        $scope.listTopmentors = listTopMentors;
	    });
	
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