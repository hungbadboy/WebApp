brotControllers.controller('PlaylistController', 
  ['$scope', '$modal', '$routeParams', '$http', '$location', 'PlaylistService', 'HomeService', 'myCache',
                                       function ($scope, $modal, $routeParams, $http, $location, PlaylistService, HomeService, myCache) {


    var userId = localStorage.getItem('userId'); 
    $scope.subject = [0];
    var cachePlaylist = [];

    init();

    function init(){
      loadPlaylist();
      initSubject();
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

    function loadPlaylist(){        
        PlaylistService.loadPlaylist(userId, 0).then(function(data){
            if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
               $scope.playlist = parseData(data.data.request_data_result);   
               cachePlaylist = $scope.playlist.slice(0);
               $scope.nodata = false;
            } else{
              $scope.nodata = true;
            }   
        });
    }

    function loadMorePlaylist(){
      PlaylistService.loadPlaylist(userId, $scope.playlist.length).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
             $scope.playlist.concat(parseData(data.data.request_data_result));   
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
          PlaylistService.deletePlaylist(id).then(function(data){
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
        console.log(selectedPlaylist);
        if (confirm("Are you sure?")) {
          PlaylistService.deleteMultiplePlaylist(selectedPlaylist).then(function(data){
            console.log(data.data.request_data_result);
            loadPlaylist();
          });
        }        
      }
    }

    function checkSelectedPlaylist(){
      var selectedPlaylist = [];
      angular.forEach($scope.playlist, function(p){
        if (p.count_videos > 0) {
          alert('Please remove all videos in the playlist first.');
          return [];
        } else{
          if (!!p.selected) {
            selectedPlaylist.push(p.plid);
          }else{
            var i = selectedPlaylist.indexOf(p.plid);
            if(i != -1){
              selectedPlaylist.splice(i, 1);
            }
          }
        }        
      });
      return selectedPlaylist;
    }

    $scope.loadPlaylistBySubject = function(){
      console.log($scope.subject);
      if($scope.subject == 0){
        if(cachePlaylist.length > 0) {
          $scope.playlist.length = 0;
          $scope.playlist = cachePlaylist.slice(0);
        } else{
          loadPlaylist();
        }
      }else{
        PlaylistService.getPlaylistBySubject(userId, $scope.subject, 0).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
             $scope.playlist = parseData(data.data.request_data_result);   
             $scope.nodata = false;           
          } else{
            $scope.nodata = true;
          }
        });
      }      
    }

    $scope.search = function(){

    }

    var files;
    $scope.onFileSelect = function($files){
      if($files != null)
        files = $files[0];
    }

    $scope.add = function(){
      var title = $('#txtTitle').val();
      var subject = $('#playlist_subject').val();

      var check = true;
      var msg = '';
      if (files == undefined) {
        check = false;
        msg += 'Please select playlist thumbnail. \n';
      }
      if (title == null || title.trim().length == 0) {
        check = false;
        msg += 'Please input playlist title. \n'; 
      }
      if (subject == 0) {
        check = false;
        msg += 'Please select playlist subject. \n';  
      }

      if (check) {
        var fd = new FormData();

        fd.append('image', files);
        fd.append('title', title);
        fd.append('description', $('#txtDescription').val());
        fd.append('url', null);
        fd.append('subjectId', subject);
        fd.append('createBy', userId);

        PlaylistService.insertPlaylist(fd).then(function(data){
          console.log(data.data);
          if (data.data.request_data_result != null && data.data.request_data_result == "success") {
            //reload page
            loadPlaylist();
          } else{
            alert('Insert playlist failed.');
          }
        });
      } else{
        alert(msg);
      }
    }
}]);
