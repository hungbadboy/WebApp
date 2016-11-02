//=========================================== NOTIFICATION.CONTROLLER.JS==============
brotControllers.controller('NotificationCtrl', ['$sce','$scope', '$rootScope', '$location', '$log', '$routeParams', 'NotificationService', function ($sce, $scope, $rootScope, $location, $log, $routeParams, NotificationService) {
var userId = localStorage.getItem('userId');
$scope.userType = localStorage.getItem('userType');
var totalPageNotification;
var listPage = [];
var listReaded = [];
$rootScope.page = 1;

init();

function init() {
	if(userId == null || userId === undefined) {
		return;
	}
	
/**
 * Get Notification read
 */
  NotificationService.getNotificationReaded(userId).then(function(data) {
    if(data.data.request_data_result.length > 0) {
      $scope.listNotifications = data.data.request_data_result;
      for(var i = 0; i < $scope.listNotifications.length; i++) {
        if($scope.listNotifications[i].notification.length > 70) {
          $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
        }
      }
      $rootScope.notifications = $scope.listNotifications;
    } else {
      $scope.emptyNotification = 1;
      $scope.errorData = DATA_ERROR_NOTIFICATION.noNewNotification;
    }
  });
  
/**
 * Get all notification of user
 */
  NotificationService.getAllNotification(userId, $rootScope.page).then(function(data) {
    $scope.notificationsAll = data.data.request_data_result;
    $scope.countAll = data.data.count;
    if($scope.countAll == null) {
      $scope.errorData = DATA_ERROR_NOTIFICATION.noNewNotification;
    } else {
      totalPageNotification = Math.ceil($scope.countAll / 7);
      showPage(totalPageNotification, $rootScope.page, function(response) {
        $scope.listPage = response;
      });
    }

    $scope.showItem = true;
  });
}

/**
 * Get Detail
 */
$scope.notificationDetail = function(nid, subjectId, topicId, videoId, questionId, articleId, idSubAdmission, idTopicSubAdmission, idEssay) {
  NotificationService.getNotificationByUserId(userId).then(function(data) {
    $scope.listNotifications = data.data.request_data_result;
    $rootScope.countNotification = data.data.count;
    if($rootScope.countNotification == null) {
      NotificationService.getNotificationReaded(userId).then(function(data) {
        if(data.data.request_data_result.length > 0) {
          $scope.listNotifications = data.data.request_data_result;
          for(var i = 0; i < $scope.listNotifications.length; i++) {
            if($scope.listNotifications[i].notification.length > 70) {
              $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
            }
          }
          $rootScope.notifications = $scope.listNotifications;
        } else {
          $scope.emptyNotification = 1;
          $scope.errorData = DATA_ERROR_NOTIFICATION.noNewNotification;
        }
      });
    } else {
      for(var i = 0; i < $scope.listNotifications.length; i++) {
        if($scope.listNotifications[i].notification.length > 70) {
          $scope.listNotifications[i].notification = $scope.listNotifications[i].notification.substring(0, 70) + ' ...';
        }
      }
      $rootScope.notifications = $scope.listNotifications;  
    }

    if(subjectId == null) {
      if(articleId != null) {
        window.location.href = '#/admission/article/articledetail/' + articleId;
      } else if(idEssay != null) {
        window.location.href = '#/essay_detail/' + idEssay;
      } else {
        window.location.href = '#/admission/videoadmission/videodetail/' + idSubAdmission + '/' + idTopicSubAdmission + '/' + videoId;  
      }
    } else {
      if(videoId != null && questionId == null) {
        window.location.href = '#/detailVideo/' + subjectId + '/' + topicId + '/' + videoId;
      } else {
        window.location.href = '#/question_detail?subject=' + subjectId + '&question_id=' + questionId;
      }  
    }
  });
};

$scope.prevPage = function() {
  if($rootScope.page > 1) {
    $rootScope.page = parseInt($rootScope.page, 10) - 1;
    init();
  }  
};

$scope.nextPage = function() {
  if($rootScope.page < totalPageNotification) {
    $rootScope.page = parseInt($rootScope.page, 10) + 1;
    init();
  }  
};

$scope.showWithPage = function(page) {
  $rootScope.page = page;
  init();
};

$scope.addClassActive = function(page) {
  if(page == $rootScope.page) {
    return 'span_active';
  }
  return '';
};
$scope.linkRedirect= function(type,linkToId) {
	var linkRedirectTo ='#';
	switch (type) {
	case "1":
		linkRedirectTo +='/question_detail/'+linkToId;
		break;
	case "2":
		linkRedirectTo+='/mentor/video/detail/'+linkToId;
		break;
	case "3":
		linkRedirectTo+='/video_admission/'+linkToId;
		break;
	case "4":
		linkRedirectTo+='/college_admission?tab=3&eid='+linkToId;
		break;
	default:
		break;
	}
	
	$('.notification-content').hide();
	window.location.href = linkRedirectTo;
}
$scope.linkToProfile = function(userType, senderId) {
	$('.notification-content').hide();
	window.location.href = ((userType=='S')? '#/student/mentorProfile/':'#mentor/studentProfile/') +senderId;
}
}]);
//=========================================== NOTIFICATION.CONTROLLER.JS==============