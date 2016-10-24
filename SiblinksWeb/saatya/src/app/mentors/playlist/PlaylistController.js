brotControllers.controller('PlaylistController', 
  ['$rootScope','$scope', '$modal', '$routeParams', '$http', '$location', 'PlaylistService', 'HomeService', 'myCache',
                                       function ($rootScope, $scope, $modal, $routeParams, $http, $location, PlaylistService, HomeService, myCache) {


    var userId = localStorage.getItem('userId'); 
    var cachePlaylist = [];
    var sub = myCache.get("subjects");
    init();

    function init(){
      if (userId && userId > 0) {
        loadPlaylist();
        initSubject();
        getAllPlaylist();

        $('#txtDescription').val('');
      } else {
        window.localStorage.clear();
        window.location.href = '/';
      }      
    }

    function initSubject(){      
      if (sub){    
        var objArr = angular.copy(sub);
        var objArr2 = angular.copy(sub);

         if (objArr[0].subjectId != 0) {
            objArr.splice(0, 0, {
              'subjectId': 0,
              'subject' : 'Select a Subject'
            });
            $scope.subjects = objArr;
            $scope.addSubject = $scope.subjects[0].subjectId;  
         }           

         if (objArr2[0].subjectId != 0) {
            objArr2.splice(0, 0, {
              'subjectId': 0,
              'subject' : 'All'
            });
            $scope.filterSubjects = objArr2;
            $scope.subject = $scope.filterSubjects[0].subjectId;
         }         
      } else{
        HomeService.getAllCategory().then(function (data) {
           if (data.data.status) {
              var objArr = angular.copy(data.data.request_data_result);
              var objArr2 = angular.copy(data.data.request_data_result);

             objArr.splice(0, 0, {
              'subjectId': 0,
               'subject' : 'Select a Subject'
             });
             $scope.subjects = objArr;             
             $scope.addSubject = $scope.subjects[0].subjectId;  

             objArr2.splice(0, 0, {
              'subjectId': 0,
              'subject' : 'All'
             });
             $scope.filterSubjects = objArr2;
             $scope.subject = $scope.filterSubjects[0].subjectId;               
           }
         });
      }      
    }

    function getAllPlaylist(){
      PlaylistService.getAllPlaylist(userId).then(function(data){
        var result = data.data.request_data_result;
        if (result && result != "Found no data") {
          $scope.listAllPlaylist = result;
        }
      });
    }

    function loadPlaylist(){        
        PlaylistService.loadPlaylist(userId, 0).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
             $scope.playlist = parseData(data.data.request_data_result);   
             cachePlaylist.length = 0;
             cachePlaylist = $scope.playlist.slice(0);
          } else
            $scope.playlist = null;
        });
    }

    $scope.loadMorePlaylist = function(){
      var offset = 0;
      if ($scope.playlist)
        offset = $scope.playlist.length;

      var oldArr = $scope.playlist;
      var newArr = [];
      if ($scope.subject == 0) {
        PlaylistService.loadPlaylist(userId, offset).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            newArr = parseData(data.data.request_data_result);
            $scope.playlist = oldArr.concat(newArr);
            cachePlaylist.length = 0;
            cachePlaylist = $scope.playlist.slice(0);
          }
        });
      } else {
        PlaylistService.getPlaylistBySubject(userId, $scope.subject, offset).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
             newArr = parseData(data.data.request_data_result);   
             $scope.playlist = oldArr.concat(newArr);
          }
        });
      }      
    }
    
    function parseData(data){
      for (var i = 0; i < data.length; i++) {
          data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
          if ($scope.selectedAll == true)
            data[i].selected = true;
          else
            data[i].selected = false;
      }
      return data;
    }

    $scope.update = function(plid){
      var modalInstance = $modal.open({
        templateUrl: 'src/app/playlist/updatePlaylist.tpl.html',
        controller: 'updatePlaylistController',
        resolve:{
          pl_id: function(){
            return plid;
          }
        }
      });
    }

    $scope.checkAll = function(){
      var status = !$scope.selectedAll;

      angular.forEach($scope.playlist, function(v){
        v.selected = status;
      });
    };

    $scope.optionSelected = function(){
      $scope.selectedAll = $scope.playlist.every(function(v){
        return v.selected;
      });
    }

    $scope.delete = function(p){
      if (p.count_videos > 0) {
        var message = 'Please remove all videos in the playlist first.';
        showModal(message);
      } else{
        $rootScope.$broadcast('open');
        PlaylistService.deletePlaylist(p.plid, userId).then(function(data){
          if (data.data.status) {
             loadPlaylist();         
          }
          $rootScope.$broadcast('close');
        });
      }      
    }

    $scope.deleteMultiplePlaylist = function(){
      var selectedPlaylist = checkSelectedPlaylist();
      if (selectedPlaylist.length > 0) {
        var ModalInstanceCtrl = function($scope, $modalInstance) {
            $scope.ok = function() {
              $modalInstance.close();
              $rootScope.$broadcast('open');
              PlaylistService.deleteMultiplePlaylist(selectedPlaylist, userId).then(function(data){
                loadPlaylist();
                $rootScope.$broadcast('close');
              });
            };
            $scope.cancel = function() {
              $modalInstance.dismiss('cancel');
            };
        };
        var message = 'Are you sure you want to delete?';
        var modalHtml = ' <div class="modal-body">' + message + '</div>';
            modalHtml += '<div class="modal-footer"><button class="btn btn-danger" ng-click="ok()">' +
                'Delete</button><button class="btn btn-default" ng-click="cancel()">Cancel</button></div>';

        var modalInstance = $modal.open({
            template: modalHtml,
            size: 'sm',
            controller: ModalInstanceCtrl
        });        
      }
    }

    function showModal(message){
      var ModalInstanceCtrl = function($scope, $modalInstance) {
            $scope.ok = function() {
                $modalInstance.close();
            };
        };
        
        var modalHtml = ' <div class="modal-body">' + message + '</div>';
        modalHtml += '<div class="modal-footer"><button class="btn btn-default" ng-click="ok()">' +
            'OK</button></div>';

        var modalInstance = $modal.open({
            template: modalHtml,
            size: 'sm',
            controller: ModalInstanceCtrl
        });
    }

    function checkSelectedPlaylist(){
      var selectedPlaylist = [];
      var status = true;
      for (var i = 0; i < $scope.playlist.length; i++) {
        var data = $scope.playlist[i];
        if (!!data.selected) {
          if (data.count_videos > 0) {
            status = false;
            var message = 'Please remove all videos in the playlist first.';
            showModal(message);
            break;
          } else
            selectedPlaylist.push(data.plid);
        } else{
            var index = selectedPlaylist.indexOf(data.plid);
            if(index != -1){
              selectedPlaylist.splice(index, 1);
            }
        }
      }
      if (!status) 
        return [];
      else
        return selectedPlaylist;
    }

    function getPlaylistBySubject(){
      PlaylistService.getPlaylistBySubject(userId, $scope.subject, 0).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
           $scope.playlist = parseData(data.data.request_data_result);   
        } else
          $scope.playlist = null;
      });
    }

    $scope.loadPlaylistBySubject = function(e){
      $scope.subject = e;
      var keyword = $('input#srch-term').val();
      if($scope.subject == 0){
        if (keyword && keyword.length > 0) {
          searchPlaylist(keyword, $scope.subject);
        } else if(!keyword || keyword.length == 0 && cachePlaylist.length > 0) {
          if ($scope.playlist && $scope.playlist.length > 0)
            $scope.playlist = null;
          $scope.playlist = cachePlaylist.slice(0);
        } else{
          loadPlaylist();
        }
      }else{
        if (keyword && keyword.length > 0)
          searchPlaylist(keyword, $scope.subject);
        else
          getPlaylistBySubject();
      }      
    }

    $scope.onSelect = function(selected){
      if (selected !== undefined) {
        searchPlaylist(selected.title, $scope.subject);
      }      
    }

    $scope.search = function(){
      var keyword = $('input#srch-term').val();
      if (keyword && keyword.trim().length > 0) {
        searchPlaylist(keyword, $scope.subject);
      } else if(!keyword && $scope.subject > 0){
        getPlaylistBySubject();
      } else{
        $scope.playlist = angular.copy(cachePlaylist);
      }
    }

    function searchPlaylist(keyword, subjectId){
      PlaylistService.searchPlaylist(userId, keyword, subjectId, 0).then(function(data){
        var result = data.data.request_data_result;
        if (result && result != "Found no data") {
          $scope.playlist = parseData(result);
        } else
          $scope.playlist = null;
      });
    }

    var file;
    $scope.stepsModel = [];
    $scope.onFileSelect = function($files){
      if ($files != null && $files.length > 0) {
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

    $scope.add = function(){
      var title = $('#txtTitle').val();

      if (title == null || title.trim().length == 0) {
        $scope.error = 'Please input playlist title. \n'; 
        angular.element('#txtTitle').trigger('focus');
        return;
      } else if ($scope.addSubject == 0) {
        $scope.error = 'Please select playlist subject. \n';  
        angular.element('#addSubject').trigger('focus');       
        return;
      } else if (file == undefined) {
        $scope.error = 'Please select playlist thumbnail. \n';
        angular.element('#file1').trigger('focus');
        return;
      }
      
      $scope.error = null;
      var fd = new FormData();

      fd.append('image', file);
      fd.append('title', title);
      fd.append('description', $('#txtDescription').val());
      fd.append('url', null);
      fd.append('subjectId', $scope.addSubject);
      fd.append('createBy', userId);

      $rootScope.$broadcast('open');
      PlaylistService.insertPlaylist(fd).then(function(data){
        var result = data.data.request_data_result;
        if (result != null && result.message == "success") {
          //reload page
          $scope.success = "Insert playlist successful.";
          loadPlaylist();
          clearContent();
        } else{
          $scope.error = result;
        }
        console.log(result);
        $rootScope.$broadcast('close');
      });
    }

    $scope.clearMessage = function(){
      $scope.success = null;
    }

    $scope.changeAddValue = function(e){
      $scope.addSubject = e;
    }

    $scope.openEdit = function(plid){
      var modalInstance = $modal.open({
        templateUrl:'src/app/mentors/playlist/addUpdatePlaylist.tpl.html',
        controller:'AddUpdatePlaylistController',
        resolve:{
          pl_id: function(){
            return plid;
          },
          v_ids: function(){
            return null;
          }
        }
      });
    }

    $scope.addVideo = function(plid){
      var modalInstance = $modal.open({
        templateUrl: 'src/app/mentors/playlist/choose_video_popup.tpl.html',
        controller:'ChooseVideoController',
        resolve:{
            pl_id: function(){
            return plid;
          }
        }        
      });
    }

    $scope.$on('updatePlaylist', function(e, a){
      var item = $.grep($scope.playlist, function(p){
        return p.plid == a.plid;
      });

      var index = $scope.playlist.indexOf(item[0]);
      if (index != -1) {
        $scope.playlist[index].name = a.name;        
        $scope.playlist[index].subject = a.subject;
        if (a.newImage && a.newImage.length > 0) {}
          $scope.playlist[index].image = a.newImage;
      }
    });

    $scope.$on('addVideoFromPlaylist', function(){
      loadPlaylist();
    });

    $scope.$on('addVideo', function(){
      loadPlaylist();
    });

    function clearContent(){
      $('#txtTitle').val('');
      $('#changeImg').val('');
      $('#txtDescription').val('');
      $scope.playlistSubject = 0;
      $('#addSubject').val(0);
      $scope.stepsModel.splice(0, 1);
      file = null;
    }
}]);
