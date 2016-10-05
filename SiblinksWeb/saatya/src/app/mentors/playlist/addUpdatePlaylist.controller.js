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
        console.log(data.data.request_data_result);
      });
    }

    $scope.add = function(){
      
      var fd = validate();
      PlaylistService.insertPlaylist(fd).then(function(data){
        console.log(data.data);
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
    
    $scope.update = function(){
      var fd = validate();
      fd.append('plid', pl_id);
      PlaylistService.insertPlaylist(fd).then(function(data){
        console.log(data.data);
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

    function validate(){
      var title = $('#txtTitle').val();
      var subject = $('#playlistSubject').val();

      if (title == null || title.trim().length == 0) {
        $scope.error = 'Please input playlist title. \n'; 
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

      fd.append('image', files);
      fd.append('title', title);
      fd.append('description', $('#txtDescription').val());
      fd.append('url', null);
      fd.append('subjectId', subject);
      fd.append('createBy', userId);

      return fd;
    }

    $scope.cancel = function(){
      $modalInstance.dismiss('cancel');
    }

    var files;
    $scope.onFileSelect = function($files){
      if($files != null)
        files = $files[0];
    }
}]);
