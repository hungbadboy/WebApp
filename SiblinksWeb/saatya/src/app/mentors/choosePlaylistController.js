brotControllers.controller('ChoosePlaylistController', 
  ['$scope', '$modalInstance', '$routeParams', '$http', '$location', 'VideoService', 'MentorService', 'myCache', 'u_id',
                                       function ($scope, $modalInstance, $routeParams, $http, $location, VideoService, MentorService, myCache, u_id) {


    $scope.playlist = [0];
    init();

    function init(){
      getPlaylist();
    }

    function getPlaylist(){
      console.log(u_id);
      VideoService.getPlaylist(u_id).then(function(data){
        console.log(data.data.request_data_result);
        if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
          $scope.playlists = data.data.request_data_result;
        }
      });
    }
}]);
