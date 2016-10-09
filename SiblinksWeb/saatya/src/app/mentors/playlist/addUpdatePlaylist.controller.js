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
      if ($scope.updateSubjects == undefined) {
        if (myCache.get("subjects") !== undefined) {
           $scope.updateSubjects = myCache.get("subjects");
           if ($scope.updateSubjects[0].subjectId != 0) {
              $scope.updateSubjects.splice(0, 0, {
                'subjectId': 0,
                'subject' : 'Select a Subject'
              });
           }          
           $scope.updateSubject = $scope.updateSubjects[0].subjectId;
        } else {
           HomeService.getAllCategory().then(function (data) {
               if (data.data.status) {
                   $scope.updateSubjects = data.data.request_data_result;                 
                   $scope.updateSubjects.splice(0, 0, {
                    'subjectId': 0,
                    'subject' : 'Select a Subject'
                   });
                   $scope.updateSubject = $scope.updateSubjects[0].subjectId;
               }
           });
        }
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
      var item = $.grep($scope.updateSubjects, function(s){
        return s.subjectId == $scope.playlist.subjectId;
      });

      var index = $scope.updateSubjects.indexOf(item[0]);
      if (index != -1) {
        $scope.updateSubject = $scope.updateSubjects[index].subjectId;
      }
    }

    $scope.add = function(){      
      var title = $('#txtTitle').val();

      if (title == null || title.trim().length == 0) {
        $scope.error = 'Please input playlist Title. \n'; 
        angular.element('#txtTitle').trigger('focus');
        return;
      } else if ($scope.updateSubject == 0) {
        $scope.error = 'Please select playlist subject. \n';  
        angular.element('#updateSubject').trigger('focus');       
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
      fd.append('subjectId', $scope.updateSubject);
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
          if (result != null && result.status == "success") {
            $scope.success = "Update playlist successful.";

            var item = $.grep($scope.updateSubjects, function(s){
              return s.subjectId == $scope.updateSubject;
            });

            var index = $scope.updateSubjects.indexOf(item[0]);
            if (index != -1) {
              var subject = $scope.updateSubjects[index];
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
      if (file !== undefined)
        fd.append('image', file);
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

    var file;
    $scope.stepsModel = [];
    $scope.onFileSelect = function($files){
      if ($files != null) {
        file = $files[0];
        var reader = new FileReader();
        reader.onload = $scope.imageIsLoaded;
        reader.readAsDataURL(file);
      }
    }

    $scope.imageIsLoaded = function (e) {
      $scope.$apply(function () {
        $scope.stepsModel.splice(0, 1);
        $scope.stepsModel.push(e.target.result);
      });
    }

    $scope.removeImg = function (index) {
        $scope.stepsModel.splice(index, 1);
        file = null;
    }
}]);
