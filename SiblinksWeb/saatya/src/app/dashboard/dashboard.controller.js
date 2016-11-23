brotControllers.controller('DashboardController',['$rootScope','$scope', '$location', 'MentorService', 'VideoService', 'EssayService', 'HomeService',
  function($rootScope, $scope, $location, MentorService, VideoService, EssayService, HomeService) {

	$scope.data =[];
  //Author: Hoai Nguyen;
  //event click on "Submit" button;
  var userId = localStorage.getItem('userId');
  var schoolId = localStorage.getItem('school');
  var NO_DATA = "Found no data";
  $scope.rating = 0.1;

  init();

  function init(){
    if (userId && userId > 0) {
   	  getStudentsSubcribed();
      getMainDashboardInfo();
      getVideosTopViewed();
      getNewestQuestions();
      getNewestEssay();
      initPlaylist();
      initSubject();
    } else {
      window.localStorage.clear();
      window.location.href = '/';
    }    
  }

  function initSubject(){
    if (localStorage.getItem('mentorSubjects') == null) {
      HomeService.getAllCategory().then(function (data) {
        if (data.data.status) {
          var subjects = angular.copy(data.data.request_data_result);
          subjects = removeItem(subjects);
          subjects.splice(0, 0, {
            'subjectId': 0,
            'subject' : 'Select a Subject'
          })
          localStorage.setItem("mentorSubjects", JSON.stringify(subjects), 10)
        }
      });
    }
  }

  function initPlaylist(){
    if (localStorage.getItem('playlists') == null) {
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
  }

  function getNewestEssay(){
    if (schoolId == undefined) {
      $scope.newestEssays = null;
    } else {
      EssayService.getNewestEssay(userId, schoolId, 5, 0).then(function(data){
        var result = data.data.request_data_result;
        if (result && result != NO_DATA) {
          $scope.newestEssays = result;
        } else
          $scope.newestEssays = null;
      });
    }
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
        $scope.rating = parseFloat(Math.round($scope.vTopViewed.averageRating * 100) / 100).toFixed(1);
        $scope.topViewedPos = 0;
      }
    });
  }

  $scope.topViewedPre = function(pos){
    if (pos == 0) {
      $scope.topViewedPos = $scope.videosTopViewed.length - 1;
      $scope.vTopViewed = $scope.videosTopViewed[$scope.videosTopViewed.length - 1];
      $scope.rating = parseFloat(Math.round($scope.vTopViewed.averageRating * 100) / 100).toFixed(1);
    }
    else{
      $scope.topViewedPos = pos - 1;
      $scope.vTopViewed = $scope.videosTopViewed[pos - 1];
      $scope.rating = parseFloat(Math.round($scope.vTopViewed.averageRating * 100) / 100).toFixed(1);
    }
  }

  $scope.topViewedNext = function(pos){
    if (pos == $scope.videosTopViewed.length - 1) {
      $scope.topViewedPos = 0;
      $scope.vTopViewed = $scope.videosTopViewed[0];
      $scope.rating = parseFloat(Math.round($scope.vTopViewed.averageRating * 100) / 100).toFixed(1);
    }
    else{
      $scope.topViewedPos = pos + 1;
      $scope.vTopViewed = $scope.videosTopViewed[pos + 1];
      $scope.rating = parseFloat(Math.round($scope.vTopViewed.averageRating * 100) / 100).toFixed(1);
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
  
  $scope.$on('uploadNew', function(){
    getMainDashboardInfo();
  })
}]);