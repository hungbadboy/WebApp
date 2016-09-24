brotControllers.controller('VideoController', ['$scope', '$modal', '$routeParams', '$http', '$location', 'VideoService', 'MentorService',
                                       function ($scope, $modal, $routeParams, $http, $location, VideoService, MentorService) {


    var userId = localStorage.getItem('userId');
    $scope.fullName = localStorage.getItem('firstName') + ' ' + localStorage.getItem('lastname');

    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

    $scope.video_subject = [0];
    $scope.video_playlist = [0];

    init();

    function init(){
      // initImage();
      getDashboardInfo();
      loadVideos();
      getStudentsSubscribe();
      getLatestRatings();
      getLatestComments();
      // loadVideosRecently();
      // fetchSubjects();
      // getPlayList();
    }

     $scope.openModalUpdate = function (vid) {
        var modalInstance = $modal.open({
            templateUrl: 'src/app/video/video_update.tpl.html',
            controller: 'VideoUpdateController',
            resolve: {
                vid: function () {
                    return vid;
                }
            }
        });
    };

    function getDashboardInfo(){
      MentorService.getDashboardInfo(userId).then(function(data){
        if (data.data.request_data_result != null) 
          $scope.dashboard = data.data.request_data_result;
      });
    }

    function getLatestComments(){
      MentorService.getLatestComments(userId, 5, 5).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result.length > 0) 
          $scope.comments = formatCommentProfile(data.data.request_data_result);
      });
    }

    function formatCommentProfile(data){      
      for (var i = 0; i < data.length; i++) {
        if (data[i].imageUrl == null || data[i].imageUrl.length == 0)
          data[i].imageUrl = 'http://www.capheseo.com/Content/T000/Images/no-avatar.png';
        else
          data[i].imageUrl = data[i].imageUrl.indexOf('http') == -1 ? $scope.baseIMAGEQ + data[i].imageUrl: data[i].imageUrl;

        data[i].fullName = data[i].firstName + ' ' + data[i].lastName;
        console.log(data[i].timestamp);
        console.log(convertUnixTimeToTime(data[i].timestamp));
        data[i].timestamp = convertUnixTimeToTime(data[i].timestamp);
      }
      return data;
    }

    function getLatestRatings(){
      MentorService.getLatestRatings(userId).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result.length > 0) 
          $scope.ratings = formatRatingProfile(data.data.request_data_result);
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

    function getStudentsSubscribe(){
      MentorService.getStudentsSubscribe(userId, 5, 0).then(function(data){
        $scope.students = data.data.request_data_result;
      });
    }

    $scope.loadMoreStudents = function(){
      MentorService.getStudentsSubscribe(userId, 5, $scope.students.length).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result.length > 0) 
          $scope.students.concat(data.data.request_data_result);
      });
    }

    function loadVideos(){
        // clearContent();
        VideoService.getVideos(userId, 10).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result.length > 0) 
            $scope.videos = formatData(data.data.request_data_result);
        });

        VideoService.getVideosTopRated(userId, 0).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result.length > 0) 
            $scope.videosTopRated = formatData(data.data.request_data_result);
        });

        VideoService.getVideosTopViewed(userId, 0).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result.length > 0) 
            $scope.videosTopViewed = formatData(data.data.request_data_result);
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

  function clearContent(){
    $('.newest').empty();
    $('.most_viewed').empty();
    $('.top_rates').empty();
  }

}]);
