brotControllers.controller('TopMentorCtrl', ['$scope', '$http', 'MentorService', function($scope, $http, MentorService) {
	var LIMIT_TOP_MENTORS = 5;
	var OFFSET = 0;
	var userId = localStorage.getItem('userId') || '-1';
	var subjects = JSON.parse(localStorage.getItem('subjects'));
	$scope.listTopmentors = [];
	
	init();
	function init() {
        //get top mentors by subcribe
	    MentorService.getTopMentorsByLikeRateSubcrible(LIMIT_TOP_MENTORS, OFFSET, 'subcribe', userId).then(function (data) {
	        var data_result = data.data.request_data_result;
	        if (data.data.status == 'true') {
	            for (var i = 0; i < data_result.length; i++) {
	            	if(data_result[i].defaultSubjectId !== null && data_result[i].defaultSubjectId !== undefined) {
	            		data_result[i].listSubject = getSubjectNameById(data_result[i].defaultSubjectId, subjects);
	                }
	            	$scope.listTopmentors.push(data_result[i]);
	            }
	        } else {
	        	$scope.listTopmentors ={};
	        }
	    });
	}
}]);