brotControllers.controller('ChooseVideoController', 
  ['$rootScope','$scope', '$modalInstance', '$routeParams', '$http', '$location', 'PlaylistService', 'VideoService','HomeService', 'myCache', 'pl_id',
                                       function ($rootScope, $scope, $modalInstance, $routeParams, $http, $location, PlaylistService, VideoService, HomeService, myCache, pl_id) {

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

    var sub = myCache.get("subjects");
    function initSubject(){
      if (sub !== undefined) {
        var arr = angular.copy(sub);
         if (arr[0].subjectId != 0) {
            arr.splice(0, 0, {
              'subjectId': 0,
              'subject' : 'Select a Subject'
            });
         }
         $scope.subjects = arr;
         $scope.insertSubject = $scope.subjects[0].subjectId;
      } else {
         HomeService.getAllCategory().then(function (data) {
             if (data.data.status) {
                 var arr = angular.copy(data.data.request_data_result);
                 arr.splice(0, 0, {
                  'subjectId': 0,
                  'subject' : 'Select a Subject'
                 });
                 $scope.subjects = arr;
                 $scope.insertSubject = $scope.subjects[0].subjectId;
             }
         });
      }      
    }

    $scope.loadVideoBySubject = function(e){
        var sid = e;
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
          $rootScope.$broadcast('open');
          VideoService.addVideosToPlaylist(request).then(function(data){
            var result = data.data.request_data_result;
            if (result != null && result == "Success") {
                $rootScope.$broadcast('addVideo');
                $modalInstance.dismiss('cancel');
            } else
                alert(result);
            $rootScope.$broadcast('close');
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
      if (link == null || link.trim().length == 0) {
        check = false;
        $scope.error = "Please input Link. \n";
        angular.element('#txtTutLink').trigger('focus');
        return;
      } else if (!$scope.vid) {
        check = false;
        $scope.error = "Please input valid link. \n";
        angular.element('#txtTutLink').trigger('focus');
        return;
      } else if (title == null || title.trim().length == 0) {
        check = false;
        $scope.error = "Please input Title. \n";
        angular.element('#txtTutTitle').trigger('focus');
        return;
      } else if (description && description.length > 1024) {
        check = false;
        $scope.error = "Description can not longer than 1024 characters. \n";
        angular.element('#txtTutDescription').trigger('focus');    
        return;    
      } else if ($scope.insertSubject == 0) {
        check = false;
        $scope.error = "Please select subject. \n";
        angular.element('#insertSubject').trigger('focus');     
        return;   
      }

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
      $rootScope.$broadcast('open');
      VideoService.uploadTutorial(request).then(function(data){
        if (data.data.request_data_result === "Success") {
          $scope.success = "Upload Tutorial successful.";
          $rootScope.$broadcast('addVideoFromPlaylist');
          $modalInstance.dismiss('cancel');
        } else{
          $scope.error = data.data.request_data_result;
        }
        $rootScope.$broadcast('close');
      });
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
        getVideoInfo($scope.vid);
      }
    }

    function getVideoInfo(vid){
       VideoService.getVideoInfoFromYoutube(vid).then(function(data){
          var result = data.data.items;
          var contentDetails = result[0].contentDetails;
          $scope.duration = convertTime(contentDetails.duration);
          $scope.title = result[0].snippet.title;
          $scope.description = result[0].snippet.description;
       });
    }

    function convertTime(duration) {
        var a = duration.match(/\d+/g);

        if (duration.indexOf('M') >= 0 && duration.indexOf('H') == -1 && duration.indexOf('S') == -1) {
            a = [0, a[0], 0];
        }

        if (duration.indexOf('H') >= 0 && duration.indexOf('M') == -1) {
            a = [a[0], 0, a[1]];
        }
        if (duration.indexOf('H') >= 0 && duration.indexOf('M') == -1 && duration.indexOf('S') == -1) {
            a = [a[0], 0, 0];
        }

        duration = 0;

        if (a.length == 3) {
            duration = duration + parseInt(a[0]) * 3600;
            duration = duration + parseInt(a[1]) * 60;
            duration = duration + parseInt(a[2]);
        }

        if (a.length == 2) {
            duration = duration + parseInt(a[0]) * 60;
            duration = duration + parseInt(a[1]);
        }

        if (a.length == 1) {
            duration = duration + parseInt(a[0]);
        }
        var h = Math.floor(duration / 3600);
        var m = Math.floor(duration % 3600 / 60);
        var s = Math.floor(duration % 3600 % 60);
        return ((h > 0 ? h + ":" + (m < 10 ? "0" : "") : "") + m + ":" + (s < 10 ? "0" : "") + s);
    }

    var player;
    function onYouTubeIframeAPIReady(youtubeId) {
      player = new YT.Player('validate', {
          height: '310',
          width: '100%',
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
