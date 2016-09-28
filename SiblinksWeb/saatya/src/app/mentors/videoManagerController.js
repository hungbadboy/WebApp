brotControllers.controller('VideoManagerController', 
  ['$scope', '$modal', '$routeParams', '$http', '$location', 'VideoService', 'MentorService', 'myCache',
                                       function ($scope, $modal, $routeParams, $http, $location, VideoService, MentorService, myCache) {


    var userId = localStorage.getItem('userId');
    
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

    var subjects = myCache.get('subjects');
    var defaultSubjectId = localStorage.getItem('defaultSubjectId');

    $scope.video_playlist = [0];

    init();

    function init(){
      $scope.listSubjects = getSubjectNameById(defaultSubjectId, subjects);
      loadVideos();
    }

    function loadVideos(){
      VideoService.getVideos(userId, 10).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
          $scope.videos = formatData(data.data.request_data_result);
        }
      });

    }    

    function formatData(data){
      for (var i = 0; i < data.length; i++) {           
        var playlist = data[i].playlistname;

        if (playlist == null)
          data[i].playlist = 'none';

        data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
        data[i].selected = false;
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

    

    $scope.checkAll = function(){
      var status = !$scope.selectedAll;

      angular.forEach($scope.videos, function(v){
        v.selected = status;
      });
    };

    $scope.optionSelected = function(){
      $scope.selectedAll = $scope.videos.every(function(v){
        return v.selected;
      });
    }

    $scope.deleteMultiple = function(){
      var selectedVideos = [];
      angular.forEach($scope.videos, function(v){
        if (!!v.selected) {
          selectedVideos.push(v.vid);
        }else{
          var i = selectedVideos.indexOf(v.vid);
          if(i != -1){
            selectedVideos.splice(i, 1);
          }
        }
      });
      console.log(selectedVideos);

      if (selectedVideos.length > 0) {
        if (confirm("Are you sure?")) {
          VideoService.deleteMultipleVideo(selectedVideos, userId).then(function(data){
            if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
              loadVideos();
            }
          });
        }
      }
    }

    $scope.deleteVideo = function(vid){
      if (confirm("Are you sure?")) {
        VideoService.deleteVideo(vid, userId).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
            loadVideos();
          }
        });
      }
    }
}]);
