brotControllers.controller('VideoManagerController', 
  ['$scope', '$modal', '$routeParams', '$http', '$location', 'VideoService', 'MentorService', 'myCache', 'HomeService',
                                       function ($scope, $modal, $routeParams, $http, $location, VideoService, MentorService, myCache, HomeService) {


    var userId = localStorage.getItem('userId');
    
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

    $scope.subject = [0];

    var cacheVideos = [];

    init();

    function init(){
      loadSubjects();
      loadVideos();
    }

    function loadSubjects(){      
      MentorService.getAllSubjects(userId).then(function (data) {
        if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
          $scope.listSubjects = data.data.request_data_result;
        }
      });
    }

    function loadVideos(){
      VideoService.getVideos(userId, 10).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
          $scope.videos = formatData(data.data.request_data_result);
          cacheVideos = $scope.videos.slice(0);
        }
      });
    }    

    function formatData(data){
      for (var i = 0; i < data.length; i++) {           
        var playlist = data[i].playlistname;

        if (playlist == null)
          data[i].playlist = 'none';
        data[i].averageRating = data[i].averageRating != null ? data[i].averageRating : 0;
        data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
        data[i].selected = false;
      }
      return data;
    }

    $scope.loadMoreVideos = function(){
      VideoService.getVideos(userId, $scope.videos.length).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
          $scope.videos.concat(formatData(data.request_data_result));
          cacheVideos = $scope.videos.slice(0);
        }
      });
    }

    $scope.loadVideosBySubject = function(){
      if($scope.subject == 0){
        if(cacheVideos.length > 0) {
          $scope.videos.length = 0;
          $scope.videos = cacheVideos.slice(0);
        } else{
          loadVideos();
        }
      } else{
        $scope.videos.length = 0;
        VideoService.getVideosBySubject(userId, $scope.subject, 10).then(function(data){
          if(data.data.request_data_result != null && data.data.request_data_result.length > 0)
            $scope.videos = formatData(data.data.request_data_result);
        });
      }
    };

    $scope.loadMoreVideosBySubject = function(){
      VideoService.getVideosBySubject(userId, $scope.subject, $scope.videos.length).then(function(data){
        if(data.data.request_data_result != null && data.data.request_data_result.length > 0)
          $scope.videos.concat(formatData(data.data.request_data_result));
      });
    }

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
      var selectedVideos = checkSelectedVideos();

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

    function checkSelectedVideos(){
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
      return selectedVideos;
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

    $scope.search = function(){
      var keyword = $('#srch-term').val();
      if (keyword != null && keyword.trim().length > 0) {
        // call api
        VideoService.searchVideosMentor(userId, keyword, 10).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
            $scope.videos = formatData(data.data.request_data_result);
          }
        });
      }
    }

    $scope.addToPlaylist = function(vid){
      var selectedVideos = [vid];

      var modalInstance = $modal.open({
          templateUrl: 'src/app/mentors/choose_playlist_popup.tpl.html',
          controller: 'ChoosePlaylistController',
          resolve: {
              u_id: function () {
                  return userId;
              },
              v_ids: function(){
                return selectedVideos;
              }
          }
      });
    }

    $scope.addMultipleToPlaylist = function(){
      var selectedVideos = checkSelectedVideos();
      if (selectedVideos.length > 0) {
        var modalInstance = $modal.open({
          templateUrl: 'src/app/mentors/choose_playlist_popup.tpl.html',
          controller: 'ChoosePlaylistController',
          resolve: {
              u_id: function () {
                  return userId;
              },
              v_ids: function(){
                return selectedVideos;
              }
          }
        });
      }
    }

    function openAddPlaylistPopup(){

    }
}]);
