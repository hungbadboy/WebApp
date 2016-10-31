brotControllers.controller('TopMentorCtrl', ['$scope', '$http', 'MentorService', function($scope, $http, MentorService) {
	var LIMIT_TOP_MENTORS = 5;
	var OFFSET = 0;
	var userId = localStorage.getItem('userId') || '-1';
	var subjects = JSON.parse(localStorage.getItem('subjects'));
	init();
	function init() {
        //get top mentors by subcribe
	    MentorService.getTopMentorsByLikeRateSubcrible(LIMIT_TOP_MENTORS, OFFSET, 'subcribe', userId).then(function (data) {
	        var data_result = data.data.request_data_result;
	        if (data.data.status == 'true') {
	            var listTopMentors = [];
	            for (var i = 0; i < data_result.length; i++) {
	                var mentor = {};
	                mentor.userid = data_result[i].userid;
	                mentor.fullName = displayUserName(mentor.firstName = data_result[i].firstName,
	                			mentor.lastName = data_result[i].lastName,
	                			data_result[i].loginName);
	                mentor.imageUrl = data_result[i].imageUrl;
	                mentor.numlike = data_result[i].numlike;
	                mentor.numsub = data_result[i].numsub;
	                mentor.numvideos = data_result[i].numvideos;
	                mentor.isOnline = data_result[i].isOnline;
	                mentor.defaultSubjectId = data_result[i].defaultSubjectId;
	                if(data_result[i].defaultSubjectId !== null && data_result[i].defaultSubjectId !== undefined) {
	                    mentor.listSubject = getSubjectNameById(data_result[i].defaultSubjectId, subjects);
	                }
	                mentor.numAnswers = data_result[i].numAnswers;
	                listTopMentors.push(mentor);
	            }
	        }
	        $scope.listTopmentors = listTopMentors;
	    });
	}
}]);