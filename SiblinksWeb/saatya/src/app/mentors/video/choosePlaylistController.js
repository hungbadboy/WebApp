brotControllers.controller('ChoosePlaylistController', 
  ['$rootScope','$scope', '$modal', '$modalInstance', '$routeParams', '$http', '$location', 'VideoService', 'MentorService', 'myCache', 'u_id', 'v_ids',
                                       function ($rootScope,$scope, $modal, $modalInstance, $routeParams, $http, $location, VideoService, MentorService, myCache, u_id, v_ids) {


    $scope.playlist = [0];
    init();

    function init(){
      initPlaylist();
    }

    function initPlaylist(){
      if (myCache.get("playlist") !== undefined) {
        $scope.playlists = myCache.get("playlist");
      } else{
        VideoService.getPlaylist(u_id).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            $scope.playlists = data.data.request_data_result;
            myCache.put("playlist", data.data.request_data_result);
          }
        });
      }      
    }

    $scope.apply = function(){
      var plid = $('#playlist').val();
      if(plid == 0)
        alert('Choose playlist first.');

      var request = {
        'authorID': u_id,
        'plid': plid,
        'vids': v_ids
      }

      VideoService.addVideosToPlaylist(request).then(function(data){
        $rootScope.$broadcast('addPlaylist');
        $modalInstance.dismiss('cancel');
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
}]);
