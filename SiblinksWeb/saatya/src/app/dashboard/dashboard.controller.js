brotControllers.controller('DashboardController',['$scope','$http', 'MentorService', 'VideoService',
  function($scope, $http, MentorService, VideoService) {


  //Author: Hoai Nguyen;
  //event click on "Submit" button;
  var userId = localStorage.getItem('userId');

  init();

  function init(){
    getMainDashboardInfo();
    getVideosTopViewed();
  }

  function getMainDashboardInfo(){
    MentorService.getMainDashboardInfo(userId).then(function(data){
      if (data.data.request_data_result != null) {
        $scope.dashboard = data.data.request_data_result;;
      }
    });
  }

  function getVideosTopViewed(){
     VideoService.getVideosTopViewed(userId, 0).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
            $scope.videosTopViewed = data.data.request_data_result;
            $scope.vTopViewed = $scope.videosTopViewed[0];
            $scope.topViewedPos = 0;
          }
        });
  }

  $scope.topViewedPre = function(pos){
    if (pos == 0) {
      $scope.topViewedPos = $scope.videosTopViewed.length - 1;
      $scope.vTopViewed = $scope.videosTopViewed[$scope.videosTopViewed.length - 1];
    }
    else{
      $scope.topViewedPos = pos - 1;
      $scope.vTopViewed = $scope.videosTopViewed[pos - 1];
    }
  }

  $scope.topViewedNext = function(pos){
    if (pos == $scope.videosTopViewed.length - 1) {
      $scope.topViewedPos = 0;
      $scope.vTopViewed = $scope.videosTopViewed[0];
    }
    else{
      $scope.topViewedPos = pos + 1;
      $scope.vTopViewed = $scope.videosTopViewed[pos + 1];
    }
  }
}]);