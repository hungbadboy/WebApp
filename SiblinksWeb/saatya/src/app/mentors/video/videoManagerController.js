brotControllers.controller('VideoManagerController', 
  ['$rootScope','$scope', '$modal', '$routeParams', '$http', '$location', 'VideoService', 'MentorService', 'myCache', 'HomeService',
                                       function ($rootScope,$scope, $modal, $routeParams, $http, $location, VideoService, MentorService, myCache, HomeService) {


    var userId = localStorage.getItem('userId');
    
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

    // $scope.loadMore = new LoadMore();

    $scope.subject = [0];

    var cacheVideos = [];

    init();

    function init(){
      loadSubjects();
      loadVideos();
      getAllVideos();
    }

    function loadSubjects(){      
      MentorService.getAllSubjects(userId).then(function (data) {
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          $scope.listSubjects = data.data.request_data_result;
        }
      });
    }

    function loadVideos(){
      VideoService.getVideos(userId, 0).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          $scope.videos = formatData(data.data.request_data_result);
          cacheVideos = $scope.videos.slice(0);
        } else
          $scope.videos = null;
        console.log($scope.videos);
      });
    }    

    function formatData(data){
      for (var i = 0; i < data.length; i++) {    
        data[i].playlistname = data[i].playlistname == null ? 'none' : data[i].playlistname;
        data[i].averageRating = data[i].averageRating != null ? data[i].averageRating : 0;
        data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
        data[i].selected = false;
      }
      return data;
    }

    $scope.loadMoreVideos = function(){
      var offset = 0;
      if ($scope.videos && $scope.videos.length > 0) 
        offset = $scope.videos.length;
      VideoService.getVideos(userId, offset + 10).then(function(data){        
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          var oldArr = $scope.videos;
          var newArr = formatData(data.data.request_data_result);
          var totalArr = oldArr.concat(newArr);
          $scope.videos = totalArr;
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
        VideoService.getVideosBySubject(userId, $scope.subject, 0).then(function(data){
          if(data.data.request_data_result != null && data.data.request_data_result != "Found no data"){
            $scope.videos = formatData(data.data.request_data_result);
          } else
            $scope.videos = null;
        });
      }
    };

    $scope.loadMoreVideosBySubject = function(){
      VideoService.getVideosBySubject(userId, $scope.subject, $scope.videos.length).then(function(data){
        if(data.data.request_data_result != null && data.data.request_data_result != "Found no data")
          var oldArr = $scope.videos;
          var newArr = formatData(data.data.request_data_result);
          var totalArr = oldArr.concat(newArr);
          $scope.videos = totalArr;
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
      var keyword = $('input#srch-term').val();
      if (keyword != null && keyword.trim().length > 0) {
        VideoService.searchVideosMentor(userId, keyword, 0).then(function(data){
          console.log(data.data.request_data_result);
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            $scope.videos = formatData(data.data.request_data_result);
          } else{
            $scope.videos = null;
          }
        });
      } else{
        $scope.videos = cacheVideos;
      }
    }

    $scope.addToPlaylist = function(v){
      if (v.playlistname != 'none') 
        alert("This video already in another playlist.");
      else{
        var selectedVideos = [v.vid];
        openAddPlaylistPopup(selectedVideos);
      }
    }

    $scope.addMultipleToPlaylist = function(){
      var selectedVideos = checkSelectedVideos();
      if (selectedVideos.length > 0) {
        openAddPlaylistPopup(selectedVideos);
      }
    }

    function openAddPlaylistPopup(selectedVideos){
      var modalInstance = $modal.open({
        templateUrl: 'src/app/mentors/video/choose_playlist_popup.tpl.html',
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

    function getAllVideos() {
        VideoService.getAllVideos().then(function (response) {
            if (response.data.status) {
                $scope.listAllVideos = response.data.request_data_result;
            }
        });
    }

    $scope.onSelect = function (selected) {
      if (selected !== undefined) {
        VideoService.searchVideosMentor(userId, selected.title, 10).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            $scope.videos = formatData(data.data.request_data_result);
          }
        });
      }      
    };

    $scope.edit = function(vid){
      var modalInstance = $modal.open({
        templateUrl: 'src/app/mentors/video/upload_tutorial_popup.tpl.html',
        controller: 'UploadTutorialController',
        resolve:{
          u_id: function () {
              return userId;
          },
          v_id: function(){
            return vid;
          }
        }
      });
    }

    $scope.goToDetail = function(v){
      if (v.plid && v.plid > 0) {
        setStorage('vidInPlaylist', v.vid, 30);
        window.location.href = '#/mentor/playlist/playall/'+v.plid+'';
        window.location.reload();
      } else{
        window.location.href = '#/mentor/video/detail/'+v.vid+'';
        window.location.reload();
      }
    }

    $scope.$on('passing', function(e,a){
      var result = $.grep($scope.videos, function(v){
            return v.vid == a.vid;
        });

        var index = $scope.videos.indexOf(result[0]);
        if (index != -1) {
            $scope.videos[index].title = a.title;
            $scope.videos[index].description = a.description;

        }
    });

    $scope.$on('addPlaylist', function(){
      loadVideos();
    })
}]);