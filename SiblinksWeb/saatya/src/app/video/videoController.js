brotControllers.controller('VideoController', ['$scope', '$modal', '$routeParams', '$http', '$location', 
                                       function ($scope, $modal, $routeParams, $http, $location) {


    $scope.video_subject = [0];
    $scope.video_playlist = [0];

    init();

    function init(){
      // initImage();
      loadVideos();
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

    function loadVideos(){
        clearContent();
        id = 6;
        $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideos/'+ id +'',
          data: {
            "request_data_type": "video",
            "request_data_method": "getVideos",
            "id": id
        }
        
        // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data) {
          $scope.videos = formatData(data.request_data_result);          
        }).error(function(data){
          console.log(data);
        });

        $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideosTopRated/'+ id +'',
          data: {
            "request_data_type": "video",
            "request_data_method": "getVideosTopRated",
            "id": id
        }
        
        // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data) {
          $scope.videosTopRated = formatData(data.request_data_result);
        }).error(function(data){
          console.log(data);
        });

        $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideosTopViewed/'+ id +'',
          data: {
            "request_data_type": "video",
            "request_data_method": "getVideosTopViewed",
            "id": id
        }
        
        // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data) {
          $scope.videosTopViewed = formatData(data.request_data_result);
        }).error(function(data){
          console.log(data);
        });
    }
    

    function formatData(data){
      for (var i = 0; i < data.length; i++) {           
            var playlist = data[i].playlistname;

            if (playlist == null)
              data[i].playlist = 'none';

            data[i].timeStamp = moment(data[i].timeStamp).format('MMMM Do YYYY, h:mm:ss a');
          }
      return data;
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
          // $scope.videos = formatData(data.request_data_result);
          
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
