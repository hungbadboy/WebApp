brotControllers.controller('ChooseVideoController', 
  ['$rootScope','$scope', '$modalInstance', '$routeParams', 'PlaylistService', 'VideoService','HomeService', 'myCache', 'pl_id',
                                       function ($rootScope, $scope, $modalInstance, $routeParams, PlaylistService, VideoService, HomeService, myCache, pl_id) {

    var userId = localStorage.getItem('userId'); 
    $scope.subject = [0];
    var cacheVideos = [];
    
    init();

    function init(){
      if (userId && userId > 0) {
        initSubject();
        getVideos();
      } else {
        window.localStorage.clear();
        window.location.href = '/';
      }
    }

    function getVideos(){
        VideoService.getVideosNonePlaylist(userId, 0).then(function(data){
          var result = data.data.request_data_result;
          if (result && result != "Found no data") {
            $scope.videosNoPLaylist = formatVideos(result);
            cacheVideos = $scope.videosNoPLaylist.slice(0);
          } else
            $scope.videosNoPLaylist = null;
        });
    }

    $scope.loadMoreVideos = function(){
        var offset = 0;
        if ($scope.videosNoPLaylist && $scope.videosNoPLaylist.length > 0)
            offset = $scope.videosNoPLaylist.length;

        var oldArr = $scope.videosNoPLaylist;
        var newArr = [];
        if ($scope.subject == 0) {
          VideoService.getVideosNonePlaylist(userId, offset).then(function(data){
            if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                newArr = formatVideos(data.data.request_data_result);
                $scope.videosNoPLaylist = oldArr.concat(newArr);
                cacheVideos.length = 0;
                cacheVideos = $scope.videosNoPLaylist.slice(0);
            }
          });
        } else {
          VideoService.getVideosNonePlaylistBySubject(userId, $scope.subject, offset).then(function(data){
            if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                newArr = formatVideos(data.data.request_data_result);
                $scope.videosNoPLaylist = oldArr.concat(newArr);
            }
          });
        }
    }

    function formatVideos(data){
        for (var i = data.length - 1; i >= 0; i--) {
            data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
            data[i].playlist = "None";
            data[i].selected = false;
        }
        return data;
    }

    var sub = myCache.get("subjects");
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
         }
         $scope.addSubject = $scope.subjects[0].subjectId;    

         if (objArr2[0].subjectId != 0) {
            objArr2.splice(0, 0, {
              'subjectId': 0,
              'subject' : 'All'
            });
            $scope.filterSubjects = objArr2;
         }           
         $scope.subject = $scope.filterSubjects[0].subjectId;
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

    $scope.loadVideoBySubject = function(e){
        $scope.subject = e;
        if (e == 0) {
          if(cacheVideos.length > 0)
              $scope.videosNoPLaylist = cacheVideos;
        } else{            
          getVideosNonePlaylistBySubject();
        }
    }

    function getVideosNonePlaylistBySubject(){
      VideoService.getVideosNonePlaylistBySubject(userId, $scope.subject, 0).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            $scope.videosNoPLaylist = formatVideos(data.data.request_data_result);
        } else
            $scope.videosNoPLaylist = null;
      });
    }

    $scope.onSelect = function(selected){
      VideoService.searchVideosNonePlaylist(userId, selected.title, $scope.subject, 0).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            $scope.videosNoPLaylist = formatVideos(data.data.request_data_result);
        } else
            $scope.videosNoPLaylist = null;
      });
    }

    $scope.search = function(){
        var key = $('#keyword').val();
        if (key.length > 0) {
          VideoService.searchVideosNonePlaylist(userId, key, $scope.subject, 0).then(function(data){
            if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                $scope.videosNoPLaylist = formatVideos(data.data.request_data_result);
            } else
                $scope.videosNoPLaylist = null;
          });
        } else if(!key && $scope.subject > 0){
          getVideosNonePlaylistBySubject();
        } else
          $scope.videosNoPLaylist = cacheVideos;
    }
    
    var status;
    $scope.checkAll = function(){
      if ($scope.optionAll == null) {
        if (status){
          status = false;
          $scope.optionAll = status;
        } else {
          status = true;
          $scope.optionAll = status;
        }
      } else{
        if (status){
          status = false;
          $scope.optionAll = status;
        } else {
          status = true;
          $scope.optionAll = status;
        }
        // status = !$scope.optionAll;
      }
      angular.forEach($scope.videosNoPLaylist, function(v){
          v.selected = status;
      })
    }

    $scope.optionSelected = function(){
        $scope.optionAll = $scope.videosNoPLaylist.every(function(v){
            return v.selected;
        });
    }

    function checkSelectedVideos(){
        var selectedVideos = [];
        angular.forEach($scope.videosNoPLaylist, function(v){
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
      if ($('#txtTutLink').val() == '') {
        $('#txtTutTitle').val('');
        $('#txtTutDescription').val('');
        $('#txtTutDuration').val('');        
      }
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
