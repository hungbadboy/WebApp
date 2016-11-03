brotControllers.controller('ChoosePlaylistController', 
  ['$rootScope','$scope', '$modal', '$modalInstance', '$routeParams', '$http', '$location', 'VideoService', 'MentorService', 'myCache', 'u_id', 'v_ids',
                                       function ($rootScope,$scope, $modal, $modalInstance, $routeParams, $http, $location, VideoService, MentorService, myCache, u_id, v_ids) {

    var userId = localStorage.getItem('userId');
    $scope.playlist = [0];
    init();

    function init(){
      initPlaylist();
    }

    function initPlaylist(){
      if (userId && userId > 0) {
        var selectPlaylistSubjects = localStorage.getItem('selectPlaylistSubjects');
        if (selectPlaylistSubjects !== null) {
          $scope.playlists = JSON.parse(selectPlaylistSubjects);
          $scope.playlist = $scope.playlists[0].plid;
        } else{
          VideoService.getPlaylist(u_id).then(function(data){
            if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
              $scope.playlists = data.data.request_data_result;
              $scope.playlists.splice(0,0,{
                'plid':0,
                'name': "Select a Playlist"
              });
              localStorage.setItem("selectPlaylistSubjects", JSON.stringify($scope.playlists), 2);
              $scope.playlist = $scope.playlists[0].plid;
            }
          });
        }  
      } else {
        window.localStorage.clear();
        window.location.href = '/';
      }          
    }

    $scope.selectPlaylist = function(e){
      $scope.playlist = e;
      $scope.error = null;
    }

    $scope.apply = function(){
      if($scope.playlist == 0){
        $scope.error = 'Choose playlist first.';
        return;
      }

      var request = {
        'authorID': u_id,
        'plid': $scope.playlist,
        'vids': v_ids
      }
      $rootScope.$broadcast('open');
      VideoService.addVideosToPlaylist(request).then(function(data){
        $rootScope.$broadcast('addPlaylist', $scope.playlist);
        $modalInstance.dismiss('cancel');
        $rootScope.$broadcast('close');
      });
    }

    $scope.addNewPlaylist = function(){
      var modalInstance = $modal.open({
        templateUrl: 'src/app/mentors/playlist/addUpdatePlaylist.tpl.html',
        controller: 'AddUpdatePlaylistController',
        resolve:{
          pl_id: function(){
            return null;
          },
          v_ids: function(){
            return v_ids;
          }
        }
      });

      $modalInstance.dismiss('cancel');
    }

    $scope.cancel = function(){
      $modalInstance.dismiss('cancel')
    }
}]);
