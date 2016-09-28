brotControllers.controller('ChoosePlaylistController', 
  ['$scope', '$modalInstance', '$routeParams', '$http', '$location', 'VideoService', 'MentorService', 'myCache', 'u_id', 'v_ids',
                                       function ($scope, $modalInstance, $routeParams, $http, $location, VideoService, MentorService, myCache, u_id, v_ids) {


    $scope.playlist = [0];
    init();

    function init(){
      console.log(v_ids);
      getPlaylist();
    }

    function getPlaylist(){
      VideoService.getPlaylist(u_id).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
          $scope.playlists = data.data.request_data_result;
        }
      });
    }
}]);
