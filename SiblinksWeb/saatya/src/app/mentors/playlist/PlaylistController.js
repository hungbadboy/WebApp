brotControllers.controller('PlaylistController', 
  ['$scope', '$modal', '$routeParams', '$http', '$location', 'PlaylistService', 'HomeService', 'myCache',
                                       function ($scope, $modal, $routeParams, $http, $location, PlaylistService, HomeService, myCache) {


    var userId = localStorage.getItem('userId'); 
    var cachePlaylist = [];

    init();

    function init(){
      loadPlaylist();
      initSubject();
      getAllPlaylist();
    }

    function initSubject(){
      var item = {
        'subjectId': 0,
        'subject' : 'Select Subject'
      }
      if (myCache.get("subjects") !== undefined) {
         $scope.subjects = myCache.get("subjects");
         $scope.subjects.splice(0, 0, item);
         $scope.subject = $scope.subjects[0].subjectId;
         $scope.addSubject = $scope.subjects[0].subjectId;
      } else {
         HomeService.getAllCategory().then(function (data) {
           if (data.data.status) {
             $scope.subjects = data.data.request_data_result;                 
             myCache.put("subjects", data.data.request_data_result);

             if ($scope.subjects) {
              $scope.subjects.splice(0, 0, item);
              $scope.subject = $scope.subjects[0].subjectId;
              $scope.addSubject = $scope.subjects[0].subjectId;
             } 
           }
         });
      }
    }

    function getAllPlaylist(){
      PlaylistService.getAllPlaylist().then(function(data){
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

    function loadMorePlaylist(){
      PlaylistService.loadPlaylist(userId, $scope.playlist.length).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            var oldArr = $scope.playlist;
            var newArr = parseData(data.data.request_data_result);
            var totalArr = oldArr.concat(newArr);
            $scope.playlist = totalArr;
            cachePlaylist.length = 0;
            cachePlaylist = $scope.playlist.slice(0);
          }
      });
    }
    
    function parseData(data){
        for (var i = 0; i < data.length; i++) {
            data[i].numView = data[i].numView == null ? 0 : data[i].numView;
            data[i].numComment = data[i].numComment == null ? 0 : data[i].numComment;
            data[i].numLike = data[i].numLike == null ? 0 : data[i].numLike;
            data[i].numRate = data[i].numRate == null ? 0 : data[i].numRate;
            data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
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

    $scope.delete = function(p){
      if (p.count_videos > 0) {
        alert('Please remove all videos in the playlist first.');
      } else{
        if (confirm("Are you sure?")) {
          PlaylistService.deletePlaylist(p.plid, userId).then(function(data){
            if (data.data.status) {
               loadPlaylist();         
            }
          });
        }
      }      
    }

    $scope.deleteMultiplePlaylist = function(){
      var selectedPlaylist = checkSelectedPlaylist();
      if (selectedPlaylist.length > 0) {
        if (confirm("Are you sure?")) {
          PlaylistService.deleteMultiplePlaylist(selectedPlaylist).then(function(data){
            loadPlaylist();
          });
        }        
      }
    }

    function checkSelectedPlaylist(){
      var selectedPlaylist = [];
      angular.forEach($scope.playlist, function(p){
        
          if (!!p.selected) {
            if (p.count_videos > 0) {
              alert('Please remove all videos in the playlist first.');
              return [];
            } else{}
              selectedPlaylist.push(p.plid);
          }else{
            var i = selectedPlaylist.indexOf(p.plid);
            if(i != -1){
              selectedPlaylist.splice(i, 1);
            }
          }
      });
      return selectedPlaylist;
    }

    $scope.loadPlaylistBySubject = function(e){
      $scope.subject = e;
      if($scope.subject == 0){
        if(cachePlaylist.length > 0) {
          if ($scope.playlist && $scope.playlist.length > 0)
            $scope.playlist = null;
          $scope.playlist = cachePlaylist.slice(0);
        } else{
          loadPlaylist();
        }
      }else{
        PlaylistService.getPlaylistBySubject(userId, $scope.subject, 0).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
             $scope.playlist = parseData(data.data.request_data_result);   
          } else
            $scope.playlist = null;
        });
      }      
    }

    $scope.onSelect = function(selected){
      if (selected !== undefined) {
        PlaylistService.searchPlaylist(userId, selected.title, 0).then(function(data){
          var result = data.data.request_data_result;
          if (result && result != "Found no data") {
            $scope.playlist = parseData(result);
          } else
            $scope.playlist = null;
        });
      }      
    }

    $scope.search = function(){
      var keyword = $('input#srch-term').val();
      if (keyword && keyword.trim().length > 0) {
        PlaylistService.searchPlaylist(userId, keyword, 0).then(function(data){
          var result = data.data.request_data_result;
          if (result && result != "Found no data") {
            $scope.playlist = parseData(result);
          } else
            $scope.playlist = null;
        });
      }
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

    function clearContent(){
      $('#txtTitle').val('');
      $('#changeImg').val('');
      $('#txtDescription').val('')
      $scope.playlistSubject = [0];
    }
}]);
