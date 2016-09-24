brotControllers.controller('AddPlaylistController', ['$scope', '$routeParams', '$http', '$location','$modalInstance','$window', 'PlaylistService',
                                       function ($scope, $routeParams, $http, $location, $modalInstance,$window, PlaylistService) {


    $scope.video_subject = [0];
    var userId = localStorage.getItem('userId'); 

    init();

    function init(){      
      loadSubject();
    }
   
    $scope.save = function(){
      var check = true;
      var error = '';

      var title = $('#title').val();
      // var url = $scope.playlist_url;
      var imageurl = $('#image').val();
      var description = $('#description').val();
      var subjectId = $( "#subject" ).val();

      if (title == null || title.trim().length == 0){
        error += 'Please input Title. \n';
        check = false;
      }
      // if (url == null || url.trim().length == 0){
      //   error += 'Please input video url. \n';
      //   check = false;
      // }
      if (imageurl == null || imageurl.trim().length == 0){
        error += 'Please input image. \n';
        check = false;
      }        
      if(subjectId == 0){
          error += 'Please select subject';
          check = false;
      }

      if (check) {
        var playlist = {
          'name': title,
          'image': imageurl,
          'description': description,
          'url': null,
          'subjectId': subjectId,
          'createBy': userId
        }

        console.log(playlist);        
        PlaylistService.insertPlaylist(playlist).then(function(data){
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

    function loadSubject(){
      PlaylistService.loadSubject().then(function(data){
          if (data.data.status) {
             $scope.subjects = data.data.request_data_result;
          }
      });
    }

    $scope.check = function(){
        $("<img>", {
          src: $scope.playlist_image,
          error: function() { console.log($scope.playlist_image + ': ' + false); },
          load: function() { $('#image_preview').show(); }
      });
    }
}]);
