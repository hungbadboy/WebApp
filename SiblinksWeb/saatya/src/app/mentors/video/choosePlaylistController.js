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
      } else {
        window.localStorage.clear();
        window.location.href = '/';
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
      $rootScope.$broadcast('open');
      VideoService.addVideosToPlaylist(request).then(function(data){
        $rootScope.$broadcast('addPlaylist', plid);
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
