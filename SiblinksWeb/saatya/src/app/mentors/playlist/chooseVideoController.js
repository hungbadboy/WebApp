brotControllers.controller('ChooseVideoController', 
  ['$scope', '$modalInstance', '$routeParams', '$http', '$location', 'PlaylistService', 'VideoService', 'myCache', 'pl_id',
                                       function ($scope, $modalInstance, $routeParams, $http, $location, PlaylistService, VideoService, myCache, pl_id) {

    var userId = localStorage.getItem('userId'); 
    $scope.subject = [0];
    var cacheVideos = [];
    
    init();

    function init(){
        initSubject();
        getVideos();
    }

    function getVideos(){
        VideoService.getVideosNonePlaylist(userId, 10).then(function(data){
            if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                $scope.videos = formatVideos(data.data.request_data_result);
                cacheVideos = $scope.videos.slice(0);
            }
        });
    }

    $scope.loadMoreVideos = function(){
        VideoService.getVideosNonePlaylist(userId, $scope.videos.length + 10).then(function(data){
            if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                $scope.videos.concat(formatVideos(data.data.request_data_result));
                cacheVideos = $scope.videos.slice(0);
            }
        });
    }

    function formatVideos(data){
        for (var i = data.length - 1; i >= 0; i--) {
            data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
            data[i].playlist = "none";
            data[i].selected = false;
        }
        return data;
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

    $scope.loadVideoBySubject = function(){
        var sid = $('#subject').val();
        if (sid == 0) {
            if(cacheVideos.length > 0)
                $scope.videos = cacheVideos; 
        } else{            
            VideoService.getVideosNonePlaylistBySubject(userId, sid, 10).then(function(data){
                console.log(data.data.request_data_result);
                if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                    $scope.videos = formatVideos(data.data.request_data_result);
                } else
                    $scope.videos = null;
            });
        }
    }

    $scope.loadMoreVideoBySubject = function(){
        VideoService.getVideosNonePlaylistBySubject(userId, sid, $scope.videos.length + 10).then(function(data){
            if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                $scope.videos.concat(formatVideos(data.data.request_data_result));
            }
        });
    }

    $scope.search = function(){
        var key = $('#keyword').val();
        if (key.length > 0) {
            VideoService.searchVideosNonePlaylist(userId, key, 10).then(function(data){
                if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                    $scope.videos = formatVideos(data.data.request_data_result);
                } else
                    $scope.videos = null;
            });
        } else
            $scope.videos = cacheVideos;
    }
    
    $scope.checkAll = function(){
        var status = !$scope.selectedAll;
        angular.forEach($scope.videos, function(v){
            v.selected = status;
        })
    }

    $scope.optionSelected = function(){
        $scope.selectedAll = $scope.videos.every(function(v){
            return v.selected;
        });
    }

    function checkSelectedVideos(){
        var selectedVideos = [];
        angular.forEach($scope.videos, function(v){
            if (!!v.selected)
                selectedVideos.push(v.vid);
            else{
                var index = selectedVideos.indexOf(v.vid);
                if (index != -1)
                    selectedVideos.splice(index, 1);
            }
        });
        return selectedVideos;
    }

    $scope.addToPlaylist = function(){
        var selectedVideos = checkSelectedVideos();
        if (selectedVideos.length > 0) {
            var request = {
            'authorID': userId,
            'plid': pl_id,
            'vids': selectedVideos
          }
          VideoService.addVideosToPlaylist(request).then(function(data){
            var result = data.data.request_data_result;
            if (result != null && result == "Success") {
                alert("Add video to playlist successful.");
                $modalInstance.dismiss('cancel');
            } else
                alert(result);
          });
        }
    }

    $scope.changeTab = function(tab){
        console.log('changeTab');
        if (tab == 'lib') {
            $("#lib").addClass('active');
            $("#up").removeClass('active');
        } else{
            $("#lib").removeClass('active');
            $("#up").addClass('active');
        }
    }
}]);
