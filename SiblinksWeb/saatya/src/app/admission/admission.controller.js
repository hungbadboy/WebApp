// =========================================== ADMINSSION.CONTROLLER.JS==============
brotControllers.controller('AdmissionCtrl', ['$scope', '$rootScope', '$log', '$location', '$http', '$timeout', 'AdmissionService', 'StudentService','MentorService','myCache', '$sce',
	function ($scope, $rootScope, $log, $location, $http, $timeout, AdmissionService, StudentService, MentorService, myCache, $sce) {

	var userId = localStorage.getItem('userId');
	var userType = localStorage.getItem('userType');
	
	var LIMIT_TOP_MENTORS = 5;
	var limit = 10;
    var offset = 0;
    
	/* College Admission begin */
	$scope.displayNumberOfTutVideo = 3;
	$scope.displayNumberOfArticle = 3;
	$scope.flagShowMoreTutVideo = true;
	$scope.flagShowMoreArticle = true;
	$scope.flagShowDivTuttorial = false;
	$scope.flagShowDivArticle = false;
	$scope.showBackNextButton = false;
	$scope.listVideoTuttorialAdmission = {};
	$scope.listArticleAdmission = {};
	$scope.listAdmission = {};
	$scope.stepAdmission = 0;
	$scope.countNext = 0;
	$scope.indexAdmission = 0;
	
	init();
	function init() {
		// Get Admission content
		fillAdmissionContent();
		
		// Get Top Mentor
		MentorService.getTopMentorsByLikeRateSubcrible(LIMIT_TOP_MENTORS, offset, 'subcribe', userId).then(function (data) {
            var data_result = data.data.request_data_result;
            var subjects = myCache.get("subjects");
            if (data_result) {
                var listTopMentors = [];
                for (var i = 0; i < data_result.length; i++) {
                    var mentor = {};
                    mentor.userid = data_result[i].userid;
                    mentor.userName = data_result[i].userName;
                    mentor.lastName = data_result[i].lastName;
                    mentor.firstName = data_result[i].firstName;
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
	
	$scope.rangeAdmission = function(count){
		var ratings = []; 
		for (var i = 0; i < count; i++) { 
			ratings.push(i) 
		} 
		return ratings;
	}
    
    $scope.caculateTimeElapsed = function (time){
		return caculateTimeElapsed(time);
	}	
    
    function resetAtributes() {
    	$scope.displayNumberOfTutVideo = 3;
    	$scope.displayNumberOfArticle = 3;
    	$scope.flagShowMoreTutVideo = true;
    	$scope.flagShowMoreArticle = true;
    	$scope.flagShowDivTuttorial = false;
    	$scope.flagShowDivArticle = false;
    	$scope.showBackNextButton = false;
    	$scope.listVideoTuttorialAdmission = {};
    	$scope.listArticleAdmission = {};
    }
    
	$scope.showMoreTutVideo = function(){
		if ($scope.listVideoTuttorialAdmission.length > 0 && $scope.displayNumberOfTutVideo <= $scope.listVideoTuttorialAdmission.length) {
			$scope.displayNumberOfTutVideo += 3;
		}
		
		if($scope.displayNumberOfTutVideo > $scope.listVideoTuttorialAdmission.length) {
			$scope.flagShowMoreTutVideo = false;
		}
	}
	
	$scope.showMoreArticle = function(){
		if ($scope.listArticleAdmission.length > 0 && $scope.displayNumberOfArticle <= $scope.listArticleAdmission.length) {
			$scope.displayNumberOfArticle += 3;
		}
		
		if($scope.displayNumberOfArticle > $scope.listArticleAdmission.length) {
			$scope.flagShowMoreArticle = false;
		}
		
	}
	/**
	 * Next step Admission
	 */
	
	 $scope.viewStepAdmission = function(step){
		 resetAtributes();
		 $scope.stepAdmission = step;
		 $scope.indexAdmission = step-1;
		 fillAdmissionContent();
		 angular.element('.step li').removeClass('active');
		 angular.element('#admission' + step).addClass('active');
	 }
	 
	 /**
	  * Next step Admission
	  */
//	 function nextStepAdmission() {
//		if($scope.indexAdmission != $scope.listAdmission.length - 1) {
//			resetAtributes();
//		}
//		
//		if($scope.stepAdmission < $scope.listAdmission.length - 3) {
//			$scope.stepAdmission ++;
//		}
//		
//		if($scope.indexAdmission < $scope.listAdmission.length - 1) {
//			$scope.indexAdmission++;
//			fillAdmissionContentByAdmission();
//		}
//		
//		if($scope.stepAdmission == 0) {
//			if($scope.countNext < 2) {
//				$scope.countNext++;
//			}
//			if($scope.countNext == 1) {
//				var id2 = $scope.stepAdmission + 2;
//				$('#admission' + id2).addClass('in active');
//				var id3 = id2 + 1;
//				$('#admission' + id3).addClass('active');
//			} else if ($scope.countNext == 2) {
//				var id3 = $scope.stepAdmission + 3;
//				$('#admission' + id3).addClass('in active');
//			}
//		} else if($scope.stepAdmission == $scope.listAdmission.length - 3) {
//			if($scope.countNext < 3) {
//				$scope.countNext++;
//			}
//			if($scope.countNext == 1) {
//				var id1 = $scope.stepAdmission + 1;
//				$('#admission' + id1).addClass('in active');
//				var id2 = $scope.stepAdmission + 2;
//				$('#admission' + id2).addClass('active');
//			} else if ($scope.countNext == 2) {
//				var id2 = $scope.stepAdmission + 2;
//				$('#admission' + id2).addClass('in active');
//			} else if($scope.countNext == 3) {
//				var id3 = $scope.stepAdmission + 3;
//				$('#admission' + id3).addClass('in active');
//			}
//		} else {
//			var id1 = $scope.stepAdmission + 1;
//			$('#admission' + id1).addClass('in active');
//			var id2 = id1 + 1;
//			$('#admission' + id2).addClass('active');
//		}
//	}
	 /**
	  * Convert content CKEditor to html
	  */
	$scope.comvertToDisplay  = function(str){
		 return $sce.trustAsHtml(decodeURIComponent(str));
	}
	
	/**
	 * Back step admission
	 */
//	function backStepAdmission() {
//		if($scope.indexAdmission != 0) {
//			resetAtributes();
//		}
//		
//		if($scope.indexAdmission > 0) {
//			$scope.indexAdmission--;
//			fillAdmissionContentByAdmission();
//		}
//		
//		if($scope.listAdmission.length > 3 && $scope.stepAdmission == $scope.listAdmission.length - 3) {
//			if($scope.countNext == 3) {
//				var id3 = $scope.stepAdmission + 3;
//				$('#admission' + id3).removeClass('in');
//			} else if ($scope.countNext == 2) {
//				var id3 = $scope.stepAdmission + 3;
//				$('#admission' + id3).removeClass('active');
//				var id2 = $scope.stepAdmission + 2;
//				$('#admission' + id2).removeClass('in');
//			} else if ($scope.countNext == 1) {
//				var id2 = $scope.stepAdmission + 2;
//				$('#admission' + id2).removeClass('active');
//				var id1 = $scope.stepAdmission + 1;
//				$('#admission' + id1).removeClass('in');
//				$scope.stepAdmission --;
//			}
//			
//			$scope.countNext--;
//			setTimeout(function() {
//				var id1 = $scope.stepAdmission + 1;
//				$('#admission' + id1).addClass('in active');
//			},10);
//		} else if ($scope.listAdmission.length == 3 && $scope.stepAdmission == 0) {
//			if ($scope.countNext == 2) {
//				var id3 = $scope.stepAdmission + 3;
//				$('#admission' + id3).removeClass('in active');
//				var id2 = $scope.stepAdmission + 2;
//				$('#admission' + id2).removeClass('in');
//				$scope.countNext--;
//			} else if ($scope.countNext == 1) {
//				$scope.countNext--;
//			}
//		} else {
//			if($scope.stepAdmission > 0) {
//				$scope.stepAdmission --;
//			}
//			var id3 = $scope.stepAdmission + 3;
//			$('#admission' + id3).removeClass('active');
//			var id2 = $scope.stepAdmission + 2;
//			$('#admission' + id2).removeClass('in');
//			
//			setTimeout(function() {
//				var id1 = $scope.stepAdmission + 1;
//				$('#admission' + id1).addClass('in active');
//			},10);
//		}
//	}
	
//	$scope.fillAdmissionContentByDefault = function () {
//		fillAdmissionContent();
//	}
//	
//	$scope.fillAdmissionContentByDefault();
//	
//	function fillAdmissionContentByAdmission() {
//		fillAdmissionContent();
//	}
	
	function fillAdmissionContent() {
		// Get Admission
		AdmissionService.getAdmission().then(function(data) {
			
	    	$scope.listAdmission = data.data.request_data_result;
	    	if($scope.listAdmission.length > 0){
	    		// Get Tutorial
	    		AdmissionService.getVideoTuttorialAdmission($scope.listAdmission[$scope.indexAdmission].id).then(function(data) {
	    	    	$scope.listVideoTuttorialAdmission = data.data.request_data_result;
	    	    	if($scope.listVideoTuttorialAdmission.length > 0) {
	    	    		$scope.flagShowDivTuttorial = true;
//	    	    		$scope.showBackNextButton = true;
	    	    	}
	    	    	if($scope.listVideoTuttorialAdmission.length <= 3) {
	    	    		$scope.flagShowMoreTutVideo = false;
	    	    	}
	    		});
	    		
	    		// Get Article by 
	    		AdmissionService.getArticleAdmission($scope.listAdmission[$scope.indexAdmission].id).then(function(data) {
	    	    	$scope.listArticleAdmission = data.data.request_data_result;
	    	    	if($scope.listArticleAdmission.length > 0) {
	    	    		$scope.flagShowDivArticle = true;
//	    	    		$scope.showBackNextButton = true;
	    	    	}
	    	    	if($scope.listArticleAdmission.length <= 3) {
	    	        	$scope.flagShowMoreArticle = false;
	    	    	}
	    		});
	    	}
		});
	}
	
	$scope.showPopupArticle = function(event) {
		$('#ad' + event.currentTarget.getAttribute("id")).css({"left":"0"}); 
	}
	
	$scope.showProfile = function(event) {
		window.location.href = '#/student/mentorProfile/' + event.currentTarget.getAttribute("id");
	}
	
	$scope.closeArticlePopup = function() {
		$('.article-detail').css({"left":"100%"});
	}

   $scope.beforeApplying = function(idSubAdmission, idTopicSubAdmission) {
    $location.url('/admission/videoadmission/listvideoadmission/' + idSubAdmission + '/' + idTopicSubAdmission);
  };

  $scope.application = function(idSubAdmission) {
    $location.url('/admission/videoadmission/listvideoadmission/' + idSubAdmission + '/3');
  };

  $scope.uploadEssay = function() {
    if(userType == 'S') {
      window.location.href = '#/upload_essay';
    }
  };

  $scope.afterApplying = function(idSubAdmission) {
    $location.url('/admission/videoadmission/listvideoadmission/' + idSubAdmission + '/6');
  };

  $scope.showArticle = function() {
    $location.path('/admission/article');
  };

  $scope.showArticleDetail = function(idArtilce) {
    $location.url('/admission/article/articledetail/' + idArtilce);
  };

}]);
//===========================================END ADMINSSION.CONTROLLER.JS==============