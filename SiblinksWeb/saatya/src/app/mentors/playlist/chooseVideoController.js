brotControllers.controller('ChooseVideoController', 
  ['$rootScope','$scope', '$modalInstance', '$routeParams', '$http', '$location', 'PlaylistService', 'VideoService', 'myCache', 'pl_id',
                                       function ($rootScope, $scope, $modalInstance, $routeParams, $http, $location, PlaylistService, VideoService, myCache, pl_id) {

    var userId = localStorage.getItem('userId'); 
    $scope.subject = [0];
    var cacheVideos = [];
    
    init();

    function init(){
        initSubject();
        getVideos();
    }

    function getVideos(){
        VideoService.getVideosNonePlaylist(userId, 0).then(function(data){
            if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                $scope.videos = formatVideos(data.data.request_data_result);
                cacheVideos = $scope.videos.slice(0);
            }
        });
    }

    $scope.loadMoreVideos = function(){
        var offset = 0;
        if ($scope.videos && $scope.videos.length > 0)
            offset = $scope.videos.length;

        VideoService.getVideosNonePlaylist(userId, offset + 10).then(function(data){
            if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                var oldArr = $scope.videos;
                var newArr = formatVideos(data.data.request_data_result);
                var totalArr = oldArr.concat(newArr);
                $scope.videos = totalArr;
                cacheVideos.length = 0;
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
      var item = {
        'subjectId': 0,
        'subject' : 'Select Subject'
      }
      $scope.subjects.splice(0, 0, item);
      $scope.insertSubject = $scope.subjects[0].subjectId;
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
                $rootScope.$broadcast('addVideo');
                $modalInstance.dismiss('cancel');
            } else
                alert(result);
          });
        }
    }

    $scope.changeTab = function(tab){
        if (tab == 'lib') {
            $("#lib").addClass('active');
            $("#up").removeClass('active');
        } else{
            $("#lib").removeClass('active');
            $("#up").addClass('active');
        }
    }

    $scope.changeValue = function(e){
        $scope.insertSubject = e;
    }

    $scope.upload = function(){
      var title = $('#txtTutTitle').val();
      var link = $('#txtTutLink').val();
      var description = $('#txtTutDescription').val();

      var check = true;
      $scope.error = '';
      if (title == null || title.trim().length == 0) {
        check = false;
        $scope.error = "Please input Title. \n";
        angular.element('#txtTutTitle').trigger('focus');
      } else if (link == null || link.trim().length == 0) {
        check = false;
        $scope.error = "Please input Link. \n";
        angular.element('#txtTutLink').trigger('focus');
      } else if (!$scope.vid) {
        check = false;
        $scope.error = "Please input valid link. \n";
        angular.element('#txtTutLink').trigger('focus');
      } else if ($scope.insertSubject == 0) {
        check = false;
        $scope.error = "Please select subject. \n";
        angular.element('#insertSubject').trigger('focus');        
      }

      if (check) {
        var thumbnail = 'http://img.youtube.com/vi/'+$scope.vid+'/hqdefault.jpg'
        var request = {
          "authorID": userId,
          "title": title.trim(),
          "url": link,
          "image": thumbnail,
          "description": description,
          "subjectId": $scope.insertSubject,
          "plid": pl_id
        }
        VideoService.uploadTutorial(request).then(function(data){
          if (data.data.request_data_result === "Success") {
            $scope.success = "Upload Tutorial successful.";
            $rootScope.$broadcast('addVideoFromPlaylist');
            $modalInstance.dismiss('cancel');
          } else{
            $scope.error = data.data.request_data_result;
          }
        });
      }
    }

    $scope.cancel = function(){
        $modalInstance.dismiss('cancel');
    }

    $scope.validateLink = function(){
      checkLink($('#txtTutLink').val());
    }

    function checkLink(link){
      var videoid = link.match(/(?:https?:\/{2})?(?:w{3}\.)?youtu(?:be)?\.(?:com|be)(?:\/watch\?v=|\/)([^\s&]+)/);
      if (videoid != null) {
        $scope.vid = videoid[1];
        if (player === undefined)
          onYouTubeIframeAPIReady($scope.vid);             
        else
          player.cueVideoById($scope.vid);
      }
    }

    var player;
    function onYouTubeIframeAPIReady(youtubeId) {
      player = new YT.Player('validate', {
          height: '280',
          width: '360',
          videoId: youtubeId,
          events: {
              'onReady': onPlayerReady,
              'onStateChange': onPlayerStateChange
          },
          playerVars: {
              showinfo: 0,
              autohide: 1,
              theme: 'dark'
          }
      });
    }

    function onPlayerReady(event) {
    }

    function onPlayerStateChange(event) {
    }
}]);
