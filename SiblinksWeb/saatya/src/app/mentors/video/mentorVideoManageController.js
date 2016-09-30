brotControllers.controller('MentorVideoManageController', ['$scope', '$modal', '$routeParams', '$http', '$location', 'VideoService', 'MentorService',
                                       function ($scope, $modal, $routeParams, $http, $location, VideoService, MentorService) {


    var userId = localStorage.getItem('userId');
    // var userId = 111;
    $scope.fullName = localStorage.getItem('firstName') + ' ' + localStorage.getItem('lastname');
    $scope.avatar = localStorage.getItem('imageUrl');
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

    $scope.video_subject = [0];
    $scope.video_playlist = [0];

    init();

    function init(){
      getDashboardInfo();
      loadVideos();
      // getStudentsSubscribe();
      getLatestRatings();
      getLatestComments();
    }

    function getDashboardInfo(){
      MentorService.getDashboardInfo(userId).then(function(data){
        if (data.data.request_data_result != null) {
          $scope.dashboard = data.data.request_data_result;
        }
      });
    }

    function getLatestComments(){
      MentorService.getLatestComments(userId, 5, 5).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data"){
          $scope.comments = formatCommentProfile(data.data.request_data_result);
        }
      });
    }

    function formatCommentProfile(data){      
      for (var i = 0; i < data.length; i++) {
        if (data[i].imageUrl == null || data[i].imageUrl.length == 0)
          data[i].imageUrl = 'http://www.capheseo.com/Content/T000/Images/no-avatar.png';
        else
          data[i].imageUrl = data[i].imageUrl.indexOf('http') == -1 ? $scope.baseIMAGEQ + data[i].imageUrl: data[i].imageUrl;

        data[i].fullName = data[i].firstName + ' ' + data[i].lastName;
        data[i].timestamp = convertUnixTimeToTime(data[i].timestamp);
      }
      return data;
    }

    function getLatestRatings(){
      MentorService.getLatestRatings(userId).then(function(data){
        console.log(data.data.request_data_result);
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data"){
          $scope.ratings = formatRatingProfile(data.data.request_data_result);
        }
      });
    }

    function formatRatingProfile(data){
      for (var i = 0; i < data.length; i++) {
        if (data[i].imageUrl == null || data[i].imageUrl.length == 0)
          data[i].imageUrl = 'http://www.capheseo.com/Content/T000/Images/no-avatar.png';
        else
          data[i].imageUrl = data[i].imageUrl.indexOf('http') == -1 ? $scope.baseIMAGEQ + data[i].imageUrl: data[i].imageUrl;

        data[i].fullName = data[i].firstName + ' ' + data[i].lastName;
      }
      return data;
    }

    // function getStudentsSubscribe(){
    //   MentorService.getStudentsSubscribe(userId, 5, 0).then(function(data){
    //     $scope.students = data.data.request_data_result;
    //   });
    // }

    // $scope.loadMoreStudents = function(){
    //   MentorService.getStudentsSubscribe(userId, 5, $scope.students.length).then(function(data){
    //     if (data.data.request_data_result != null && data.data.request_data_result.length > 0) 
    //       $scope.students.concat(data.data.request_data_result);
    //   });
    // }

    $scope.loadMoreComments = function(){
      MentorService.getLatestComments(userId, 5, $scope.comments,length).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result.length > 0) 
          $scope.comments = formatCommentProfile(data.data.request_data_result);
      });
    }

    function loadVideos(){
        // clearContent();
        VideoService.getVideos(userId, 10).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            $scope.videos = formatData(data.data.request_data_result);
            $scope.v = $scope.videos[0];
            $scope.newestPos = 0;
          }
        });

        VideoService.getVideosTopRated(userId, 0).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            $scope.videosTopRated = formatData(data.data.request_data_result);
            $scope.vTopRated = $scope.videosTopRated[0];
            $scope.topRatedPos = 0;
          }
        });

        VideoService.getVideosTopViewed(userId, 0).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            $scope.videosTopViewed = formatData(data.data.request_data_result);
            $scope.vTopViewed = $scope.videosTopViewed[0];
            $scope.topViewedPos = 0;
          }
        });
    }    

    function formatData(data){
      for (var i = 0; i < data.length; i++) {           
            var playlist = data[i].playlistname;

            if (playlist == null)
              data[i].playlist = 'none';

            data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
          }
      return data;
    }

    $scope.loadMoreVideos = function(){
      VideoService.getVideos(userId, $scope.videos.length).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result.length > 0) 
          $scope.videos.concat(formatData(data.request_data_result));
      });
    }

    $scope.delete = function(vid){
      if (confirm("Are you sure?")) {
            $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/deleteVideo/',
            data: {
              "request_data_type": "video",
              "request_data_method": "deleteVideo",
              "request_data":{
                "vid": vid,
                "authorID": 6  
              }            
            }            
            // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data) {
          loadVideos();
        }).error(function(data){
          console.log(data);
        });
    }
    
  }

  $scope.loadVideosBySubject = function(){
    clearContent();
    var subjectid = $scope.item;
    if (subjectid == 0){
      loadVideos();
    }
    else{
        id = 6;
        $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideosBySubject?userid='+id + '&subjectid='+$scope.item,
            data: {
              "request_data_type": "video",
              "request_data_method": "getVideosBySubject"           
          }
        
        // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data) {        
          $scope.videos = data.request_data_result;
          for (var i = 0; i < $scope.videos.length; i++) {
            var numViews = $scope.videos[i].numViews;
            var numComments = $scope.videos[i].numComments;
            var playlist = $scope.videos[i].playlistname;

            if (numViews == null) 
              $scope.videos[i].numViews = 0;
            if (numComments == null) 
              $scope.videos[i].numComments = 0;
            if (playlist == null)
              $scope.videos[i].playlist = 'none';

            $scope.videos[i].timeStamp = moment($scope.videos[i].timeStamp).format('MMMM Do YYYY, h:mm:ss a');
          }
          $scope.videoTopViewed = $scope.videos.sort(function(a, b){
            return parseInt(b.numViews) - parseInt(a.numViews);
          });
          $scope.videoTopRated = $scope.videos.sort(function(a, b){
            return parseInt(b.numRatings) - parseInt(a.numRatings);
          });
          
        }).error(function(data){
          console.log(data);
        });
      }     
    };

  $scope.newestPrev = function(pos){
    if (pos == 0) {
      $scope.newestPos = $scope.videos.length - 1;
      $scope.v = $scope.videos[$scope.videos.length - 1];
    }
    else{
      $scope.newestPos = pos - 1;
      $scope.v = $scope.videos[pos - 1];
    }
  }

  $scope.newestNext = function(pos){
    if (pos == $scope.videos.length - 1) {
      $scope.newestPos = 0;
      $scope.v = $scope.videos[0];
    }
    else{
      $scope.newestPos = pos + 1;
      $scope.v = $scope.videos[pos + 1];
    }
  }

  $scope.topRatedPre = function(pos){
    if (pos == 0) {
      $scope.topRatedPos = $scope.videosTopRated.length - 1;
      $scope.vTopRated = $scope.videosTopRated[$scope.videosTopRated.length - 1];
    }
    else{
      $scope.topRatedPos = pos - 1;
      $scope.vTopRated = $scope.videosTopRated[pos - 1];
    }
  }

  $scope.topRatedNext = function(pos){
    if (pos == $scope.videos.length - 1) {
      $scope.topRatedPos = 0;
      $scope.vTopRated = $scope.videosTopRated[0];
    }
    else{
      $scope.topRatedPos = pos + 1;
      $scope.vTopRated = $scope.videosTopRated[pos + 1];
    }
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