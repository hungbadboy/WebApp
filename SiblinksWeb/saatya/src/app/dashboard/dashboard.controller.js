brotControllers.controller('DashboardController',['$rootScope','$scope', '$location', 'MentorService', 'VideoService', 'EssayService', 'HomeService',
  function($rootScope, $scope, $location, MentorService, VideoService, EssayService, HomeService) {

	$scope.data =[
	               { imageUrl:"assets/images/mentor-04.png", name:"Student1", caption:""},
	               { imageUrl:"assets/images/mentor-04.png", name:"Student2", caption:""},
	               { imageUrl:"assets/images/mentor-04.png", name:"Student3", caption:""},
	               { imageUrl:"assets/images/mentor-04.png", name:"Student4", caption:""},
	               { imageUrl:"assets/images/mentor-04.png", name:"Student5", caption:""},
	            ];
  //Author: Hoai Nguyen;
  //event click on "Submit" button;
  var userId = localStorage.getItem('userId');
  var schoolId = localStorage.getItem('school');
  var NO_DATA = "Found no data";

  init();

  function init(){
    if (userId && userId > 0) {
      getMainDashboardInfo();
      getVideosTopViewed();
      getNewestQuestions();
      getStudentsSubcribed();
      getNewestEssay();
      initPlaylist();
      initSubject();
    } else {
      window.localStorage.clear();
      window.location.href = '/';
    }    
  }

  function initSubject(){
    HomeService.getAllCategory().then(function (data) {
      if (data.data.status) {
        var subjects = angular.copy(data.data.request_data_result);
        subjects = removeItem(subjects);
        subjects.splice(0, 0, {
          'subjectId': 0,
          'subject' : 'Select a Subject'
        });
        localStorage.setItem("subjects", JSON.stringify(subjects), 10)
      }
    });
  }

  function initPlaylist(){
    VideoService.getPlaylist(userId).then(function(data){
      if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
        var playlists = data.data.request_data_result;
        playlists.splice(0,0,{
          'plid':0,
          'name': "Select a Playlist"
        });
        localStorage.setItem("playlists", JSON.stringify(playlists), 10);
      }
    });
  }

  function getNewestEssay(){
    if (schoolId == undefined) {
      $scope.newestEssays = null;
      return;
    } else {
      EssayService.getNewestEssay(userId, schoolId, 5, 0).then(function(data){
        var result = data.data.request_data_result;
        if (result && result != NO_DATA) {
          $scope.newestEssays = formatEssay(result);
        } else
          $scope.newestEssays = null;
      });
    }
  }

  function formatEssay(data){
    for (var i = data.length - 1; i >= 0; i--) {
      data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
      var fullName = data[i].firstName + ' ' + data[i].lastName;
      data[i].fullName = fullName != ' ' ? fullName : data[i].userName.substr(0, data[i].userName.indexOf('@'));;
    }
    return data;
  }

  function getStudentsSubcribed(){
    MentorService.getAllStudentSubscribed(userId).then(function(data){
      var result = data.data.request_data_result;
      if (result && result !== NO_DATA) {
        for (var i = result.length - 1; i >= 0; i--) {
          var fullname = result[i].firstName + ' ' + result[i].lastName;
          result[i].name = fullname != ' ' ? fullname : result[i].userName.substr(0, result[i].userName.indexOf('@'));
        }
        $scope.data = result;
      } else
        $scope.data = null;
    });
  }

  function getNewestQuestions(){
    MentorService.getNewestQuestions(userId).then(function(data){
      var result = data.data.request_data_result;
      if (result && result != NO_DATA) {
        for (var i = result.length - 1; i >= 0; i--) {
          result[i].timeStamp = convertUnixTimeToTime(data.data.request_data_result[i].timeStamp);
          var fullname = result[i].firstName + ' ' + result[i].lastName;
          result[i].fullName = fullname != ' ' ? fullname : result[i].userName.substr(0, result[i].userName.indexOf('@'));
          result[i].imageUrl = result[i].imageUrl != null ? result[i].imageUrl : 'assets/images/noavartar.jpg';
        }
        $scope.questions = result;
      } else
        $scope.questions = null;   
    });
  }

  function getMainDashboardInfo(){
    MentorService.getMainDashboardInfo(userId).then(function(data){
      if (data.data.request_data_result != null && data.data.request_data_result != NO_DATA) {
        $scope.dashboard = data.data.request_data_result;
      }
    });
  }

  function getVideosTopViewed(){
    VideoService.getVideosTopViewed(userId, 0).then(function(data){
      if (data.data.request_data_result != null && data.data.request_data_result != NO_DATA) {
        $scope.videosTopViewed = data.data.request_data_result;
        $scope.vTopViewed = $scope.videosTopViewed[0];            
        $scope.topViewedPos = 0;
        initYoutubePlayer($scope.vTopViewed.url);
      }
    });
  }

  function initYoutubePlayer(youtubeUrl){
    var videoid = youtubeUrl.match(/(?:https?:\/{2})?(?:w{3}\.)?youtu(?:be)?\.(?:com|be)(?:\/watch\?v=|\/)([^\s&]+)/);
    if (videoid != null) {
      if (typeof(player) == "undefined") {
        onYouTubeIframeAPIReady(videoid[1]);
      } else {
        player.cueVideoById(videoid[1]);
      }      
    }
  }

  $scope.topViewedPre = function(pos){
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

  $scope.changeStatus = function (eid,status) {
    EssayService.updateStatusEssay(eid, userId, status).then(function (data) {
      if (data.data.request_data_result == "Success") {
        getNewestEssay();
      } else {
        console.log(data.data.request_data_result);
      }
    });
  }
  
  var player;
  function onYouTubeIframeAPIReady(youtubeId) {
    player = new YT.Player('player', {
        height: '300',
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
  }

  function onPlayerStateChange(event) {
  }

  $scope.$on('uploadNew', function(){
    getMainDashboardInfo();
  })
}]);