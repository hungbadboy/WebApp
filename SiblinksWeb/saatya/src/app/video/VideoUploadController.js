brotControllers.controller('VideoUploadController', ['$scope', '$modal', '$routeParams', '$http', '$location', 
                                       function ($scope, $modal, $routeParams, $http, $location) {


    $scope.video_subject = [0];
    $scope.video_playlist = [0];

    init();

    function init(){
      initImage();
      loadVideosRecently();
      fetchSubjects();
      getPlayList();
    }
     

    $scope.insert = function (){
        var check = true;
        var error = '';

        var title = $scope.video_title;
        var url = $scope.video_url;
        var imageurl = $scope.video_image;
        var description = $scope.video_description;

        if (title == null || title.trim().length == 0){
          error += 'Please input Title. \n';
          check = false;
        }
        if (url == null || url.trim().length == 0){
          error += 'Please input video url. \n';
          check = false;
        }
        if (imageurl == null || imageurl.trim().length == 0){
          error += 'Please input video image. \n';
          check = false;
        }        
        if($.isNumeric($scope.video_subject) == false){
          error += 'Please select subject';
          check = false;
        }

        var plid = $.isNumeric($scope.video_playlist) ? $scope.video_playlist : null;
        
        if(!check){
          alert(error);
          return;
        }
        else{
          var authorID = 6;
          $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/insertVideo',
            data: {
                "request_data_type": "video",
                "request_data_method": "insertVideo",
                "request_data":{
                  "authorID": authorID,
                  "title": title.trim(),
                  "url": url.trim(),
                  "image": imageurl.trim(),
                  "description": description,
                  "subjectId": $scope.video_subject,
                  "plid": plid
                }            
            }  
        
          // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
          }).success(function(data) {
            clearInput();
            loadVideosRecently();
          }).error(function(data){
            console.log(data);
          });
        }
    }

    function clearInput(){
      $('input').empty();
      $('textarea').empty();
    }

    function initImage(){
        $('#image_preview').hide();
    }

    $scope.check = function(){
        $("<img>", {
          src: $scope.video_image,
          error: function() { console.log($scope.image + ': ' + false); },
          load: function() { $('#image_preview').show(); }
      });
    }

    function getPlayList(){
        id = 6;
        $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideosPlaylist/'+ id +'',
          data: {
            "request_data_type": "video",
            "request_data_method": "getVideosPlaylist",
            "id": id
        }
        
        // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data) {
          $scope.playlist = data.request_data_result;          
        }).error(function(data){
          console.log(data);
        });
    }

    function fetchSubjects(){
        $http({
          method: 'POST',
          url: NEW_SERVICE_URL + 'subjects/fetchSubjects/',
          data: {
            "request_data_type": "subjects",
            "request_data_method": "fetchSubjects"
        }
        
        // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data) {
          $scope.subjects = data.request_data_result;
        }).error(function(data){
          console.log(data);
        });
    }

    function loadVideosRecently(){
        id = 6;
        $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideosRecently/'+ id +'',
          data: {
            "request_data_type": "video",
            "request_data_method": "getVideos",
            "id": id
        }
        
        // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data) {
          $scope.videosRecently = data.request_data_result;
        }).error(function(data){
          console.log(data);
        });

    }
    
}]);
