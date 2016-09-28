brotControllers.controller('DashboardController',['$scope','$http', 'MentorService', 'VideoService',
  function($scope, $http, MentorService, VideoService) {


  //Author: Hoai Nguyen;
  //event click on "Submit" button;
  // var userId = localStorage.getItem('userId');
  var userId = '111';

  init();

  function init(){
    getMainDashboardInfo();
    getVideosTopViewed();
    getNewestQuestions();
  }

  function getNewestQuestions(){
    MentorService.getNewestQuestions(userId).then(function(data){
      console.log(data.data.request_data_result);
      if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
        for (var i = data.data.request_data_result.length - 1; i >= 0; i--) {
          data.data.request_data_result[i].timeStamp = convertUnixTimeToTime(data.data.request_data_result[i].timeStamp);
        }
        $scope.questions = data.data.request_data_result;
      }
      else{
        $scope.nodata = true;
      }
    });
  }

  function getMainDashboardInfo(){
    MentorService.getMainDashboardInfo(userId).then(function(data){
      if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
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
            initYoutubePlayer($scope.vTopViewed.url)
          }
        });
  }

  function initYoutubePlayer(youtubeUrl){
    var videoid = youtubeUrl.match(/(?:https?:\/{2})?(?:w{3}\.)?youtu(?:be)?\.(?:com|be)(?:\/watch\?v=|\/)([^\s&]+)/);
    if (videoid != null) {
      if (typeof(player) == "undefined") {
        onYouTubeIframeAPIReady(videoid[1]);
      } else {
        player.loadVideoById(videoid[1]);
      }      
    }
  }

  $scope.topViewedPre = function(pos){
    console.log(pos);
    if (pos == 0) {
      $scope.topViewedPos = $scope.videosTopViewed.length - 1;
      $scope.vTopViewed = $scope.videosTopViewed[$scope.videosTopViewed.length - 1];
      initYoutubePlayer($scope.vTopViewed.url);
    }
    else{
      $scope.topViewedPos = pos - 1;
      $scope.vTopViewed = $scope.videosTopViewed[pos - 1];
      initYoutubePlayer($scope.vTopViewed.url);
    }
  }

  $scope.topViewedNext = function(pos){
    console.log(pos);
    if (pos == $scope.videosTopViewed.length - 1) {
      $scope.topViewedPos = 0;
      $scope.vTopViewed = $scope.videosTopViewed[0];
      initYoutubePlayer($scope.vTopViewed.url);
    }
    else{
      $scope.topViewedPos = pos + 1;
      $scope.vTopViewed = $scope.videosTopViewed[pos + 1];
      initYoutubePlayer($scope.vTopViewed.url);
    }
  }

  function onYouTubeIframeAPIReady(youtubeId) {
    player = new YT.Player('video', {
        height: '430',
        width: '100%',
        videoId: youtubeId,
        events: {
            'onReady': onPlayerReady,
            'onStateChange': onPlayerStateChange
        },
        playerVars: {
            showinfo: 0,
            autohide: 1,
            theme: 'dark'
        }
    });
  }

  function onPlayerReady(event) {
    // event.target.playVideo();
  }

  function onPlayerStateChange(event) {
  }

}]);