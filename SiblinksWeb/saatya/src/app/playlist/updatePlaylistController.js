brotControllers.controller('updatePlaylistController', ['$scope', '$routeParams', '$http', '$location','$modalInstance','$window', 'PlaylistService', 'pl_id',
                                       function ($scope, $routeParams, $http, $location, $modalInstance,$window, PlaylistService, pl_id) {


    $scope.video_subject = [0];
    var userId = localStorage.getItem('userId'); 

    init();

    function init(){      
        PlaylistService.loadPlaylistById(pl_id).then(function(data){
          if (data.data.status) {
            $scope.playlist = data.data.request_data_result;
            console.log($scope.playlist);
            if ($scope.playlist == "No data found") {

            } else{
              $('#title').val($scope.playlist[0].name);
              $('#image').val($scope.playlist[0].image);
              $('#description').val($scope.playlist[0].description);
            }
          }
        });
    }

    $scope.save = function(){
      var check = true;
      var error = '';

      var title = $('#title').val();
      var imageurl = $('#image').val();
      var description = $('#description').val();

      if (title == null || title.trim().length == 0){
        error += 'Please input Title. \n';
        check = false;
      }
      if (imageurl == null || imageurl.trim().length == 0){
        error += 'Please input image. \n';
        check = false;
      }        

      if (check) {
        var playlist = {
          'name': title,
          'image': imageurl,
          'description': description,
          'plid': $scope.playlist[0].plid
        }

        console.log(playlist);        
        PlaylistService.updatePlaylist(playlist).then(function(data){
          if (data.data.status) {
             window.location.href = '#/playlist';       
             $modalInstance.dismiss('cancel');
             window.location.reload();
          }
        });
      }
      else{
        alert(error);
        return;
      }
    }

    $scope.check = function(){
        $("<img>", {
          src: $scope.playlist_image,
          error: function() { console.log($scope.playlist_image + ': ' + false); },
          load: function() { $('#image_preview').show(); }
      });
    }
}]);
