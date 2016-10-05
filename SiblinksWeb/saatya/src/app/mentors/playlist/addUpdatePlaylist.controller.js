brotControllers.controller('AddUpdatePlaylistController', 
  ['$rootScope','$scope', '$modalInstance', '$routeParams', '$location', 'VideoService', 'PlaylistService', 'myCache', 'pl_id', 'v_ids',
                                       function ($rootScope,$scope, $modalInstance, $routeParams, $location, VideoService, PlaylistService, myCache, pl_id, v_ids) {

    var userId = localStorage.getItem('userId');
    
    $scope.vids = v_ids;

    init();

    function init(){
      initSubject();
      if (!isNaN(pl_id) && pl_id > 0) {
        $scope.plid = pl_id;
        getPlaylistById(pl_id);
      }
    }

    function initSubject(){
      if (myCache.get("subjects") !== undefined) {
         $scope.subjects = myCache.get("subjects");
      } else {
         HomeService.getAllCategory().then(function (data) {
             if (data.data.status) {
                 $scope.subjects = data.data.request_data_result;
                 myCache.put("subjects", data.data.request_data_result);
             }
         });
      }
    }

    function getPlaylistById(plid){
      PlaylistService.loadPlaylistById(plid).then(function(data){
        var result = data.data.request_data_result;
        if (result && result != "Found no data") {
          $scope.playlist = result;
          displayPlaylist($scope.playlist);
        }
      });
    }

    function displayPlaylist(p){
      $('#txtUpdateTitle').val(p.name);
      $('#txtUpdateDescription').val(p.description);
      var item = $.grep($scope.subjects, function(s){
        return s.subjectId == $scope.playlist.subjectId;
      });

      var index = $scope.subjects.indexOf(item[0]);
      if (index != -1) {
        $scope.subject = $scope.subjects[index];
        $scope.updateSubject = $scope.subjects[index].subjectId;
      }
    }

    $scope.add = function(){      
      var title = $('#txtTitle').val();
      var subject = $('#playlistSubject').val();

      if (title == null || title.trim().length == 0) {
        $scope.error = 'Please input playlist Title. \n'; 
        angular.element('#txtTitle').trigger('focus');
        return;
      } else if (subject == 0) {
        $scope.error = 'Please select playlist subject. \n';  
        angular.element('#playlistSubject').trigger('focus');       
        return;
      } else if (files == undefined) {
        $scope.error = 'Please select playlist thumbnail. \n';
        angular.element('#file1').trigger('focus');
        return;
      }
      
      $scope.error = null;
      var fd = new FormData();
      if (files !== undefined)
        fd.append('image', files);
      else
        fd.append('image', null);      
      fd.append('title', title);
      fd.append('description', $('#txtDescription').val());
      fd.append('url', null);
      fd.append('subjectId', subject);
      fd.append('createBy', userId);

      return fd;
      if (fd !== undefined) {
        PlaylistService.insertPlaylist(fd).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result == "success") {
            //reload page
            $scope.success = "Insert playlist successful.";
            loadPlaylist();
            clearContent();
          } else{
            $scope.error = data.data.request_data_result;
          }
        });
      }      
    }
    
    $scope.update = function(){
      var fd = validateUpdate();
      if (fd !== undefined){
        fd.append('plid', pl_id);
        fd.append('oldImage', $scope.playlist.image);
        PlaylistService.updatePlaylist(fd).then(function(data){
          var result = data.data.request_data_result;
          console.log(result);
          if (result != null && result.status == "success") {
            $scope.success = "Update playlist successful.";

            var item = $.grep($scope.subjects, function(s){
              return s.subjectId == $scope.updateSubject;
            });

            var index = $scope.subjects.indexOf(item[0]);
            if (index != -1) {
              var subject = $scope.subjects[index];
              var pl = {
                'plid': pl_id,
                'name': $('#txtUpdateTitle').val(),
                'description': $('#txtUpdateDescription').val(),
                'subject': subject.subject,
                'newImage' : result.newImage
              }
              $rootScope.$broadcast('updatePlaylist', pl);     
              $modalInstance.dismiss('cancel');   
            }
                
          } else{
            $scope.success = null;
            $scope.error = data.data.request_data_result;
          }
        });
      }      
    }

    function validateUpdate(){
      var title = $('#txtUpdateTitle').val();
      if (title == null || title.trim().length == 0) {
        $scope.error = 'Please input playlist Title. \n'; 
        angular.element('#txtUpdateTitle').trigger('focus');
        return;
      }
      
      $scope.error = null;
      var fd = new FormData();
      if (files !== undefined)
        fd.append('image', files);
      else
        fd.append('image', null);      
      fd.append('title', title);
      fd.append('description', $('#txtUpdateDescription').val());
      fd.append('url', null);
      fd.append('subjectId', $scope.updateSubject);
      fd.append('createBy', userId);

      return fd;
    }

    $scope.cancel = function(){
      $modalInstance.dismiss('cancel');
    }

    $scope.changeValue = function(e){
      $scope.updateSubject = e;
    }

    var files;
    $scope.onFileSelect = function($files){
      if($files != null)
        files = $files[0];
    }
}]);
