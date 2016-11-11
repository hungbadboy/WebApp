brotControllers.controller('ChooseVideoController', 
  ['$rootScope','$scope', '$modalInstance', '$routeParams', '$location', 'PlaylistService', 'VideoService','HomeService', 'pl_id',
                                       function ($rootScope, $scope, $modalInstance, $routeParams, $location, PlaylistService, VideoService, HomeService, pl_id) {

    var userId = localStorage.getItem('userId'); 
    $scope.subject = [0];
    var cacheVideos = [];
    
    init();

    function init(){
      if (!pl_id || isNaN(pl_id)) {
        $location.path('/mentor/dashboard');
      } else{
        if (userId && userId > 0) {
          initSubject();
          getVideos();
          getAllVideos();
        } else {
          window.localStorage.clear();
          window.location.href = '/';
      }
      }
    }

    function getAllVideos() {
      VideoService.getAllVideos().then(function (response) {
        if (response.data.status) {
            $scope.listAllVideos = response.data.request_data_result;
        }
      });
    }

    function getVideos(){
        VideoService.getVideosNonePlaylist(pl_id, userId, 0).then(function(data){
          var result = data.data.request_data_result;
          if (result && result != "Found no data") {
            $scope.videosNoPLaylist = formatVideos(result);
            cacheVideos = $scope.videosNoPLaylist.slice(0);
            console.log(cacheVideos);
          } else
            $scope.videosNoPLaylist = null;
        });
    }

    $scope.loadMoreVideos = function(){
        var offset = 0;
        if ($scope.videosNoPLaylist && $scope.videosNoPLaylist.length > 0)
            offset = $scope.videosNoPLaylist.length;

        var key = $('input#keyword').val();
        if ($scope.subject == 0) {
          if (key && key.length > 0) {
            searchMoreVideosNonePlaylist(key, $scope.subject);
          } else{
            loadMoreVideoNonePlaylist(offset);
          }
        } else {
          if (key && key.length > 0) {
            searchMoreVideoNonePlaylistBySubject(key, $scope.subject, offset);
          } else{
            loadMoreVideoNonePlaylistBySubject($scope.subject, offset);
          }
        }
    }

    function searchMoreVideoNonePlaylistBySubject(keyword, subject, offset){
      VideoService.searchVideosNonePlaylist(pl_id, userId, keyword, subject, offset).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          var oldArr = $scope.videosNoPLaylist;
          var newArr = formatVideos(data.data.request_data_result);
          $scope.videosNoPLaylist = oldArr.concat(newArr);
        }
      });
    }

    function loadMoreVideoNonePlaylistBySubject(subject, offset){
      VideoService.getVideosNonePlaylistBySubject(pl_id, userId, subject, offset).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          var oldArr = $scope.videosNoPLaylist;
          var newArr = formatVideos(data.data.request_data_result);
          $scope.videosNoPLaylist = oldArr.concat(newArr);
        }
      });
    }

    function loadMoreVideoNonePlaylist(offset){
      VideoService.getVideosNonePlaylist(pl_id, userId, offset).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          var oldArr = $scope.videosNoPLaylist;
          var newArr = formatVideos(data.data.request_data_result);
          $scope.videosNoPLaylist = oldArr.concat(newArr);
          cacheVideos.length = 0;
          cacheVideos = $scope.videosNoPLaylist.slice(0);
        }
      });
    }

    function formatVideos(data){
        for (var i = data.length - 1; i >= 0; i--) {
            data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
            // data[i].playlist = "None";
            data[i].playlistname = data[i].playlistname == null ? 'None' : data[i].playlistname;
            if ($scope.optionAll)
              data[i].selected = $scope.optionAll;
            else
              data[i].selected = false;
        }
        return data;
    }

    function initSubject(){
      var subjects = localStorage.getItem("mentorSubjects");
      if (subjects != null){    
        $scope.subjects = JSON.parse(subjects);
        $scope.addSubject = $scope.subjects[0].subjectId;    
        $scope.filterSubjects = JSON.parse(subjects);
        $scope.filterSubjects.splice(0, 1);
        $scope.filterSubjects.splice(0, 0, {
          'subjectId': 0,
          'subject' : 'All'
        });
        $scope.subject = $scope.filterSubjects[0].subjectId;
      } else{
        HomeService.getAllCategory().then(function (data) {
            if (data.data.status) {
              var objArr = angular.copy(data.data.request_data_result);
              var objArr2 = angular.copy(data.data.request_data_result);
              objArr = removeItem(objArr);
              objArr.splice(0, 0, {
               'subjectId': 0,
               'subject' : 'Select a Subject'
              });
              $scope.subjects = objArr;             
              $scope.addSubject = $scope.subjects[0].subjectId;  

              objArr2 = removeItem(objArr2);
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
        var key = $('input#keyword').val();
        if (e == 0) {
          if (key && key.length > 0) {
            searchVideosNonePlaylist(key, $scope.subject);
          } else if(!key || key.length == 0 && cacheVideos.length > 0){
            if ($scope.videosNoPLaylist){
              $scope.videosNoPLaylist = null;
              $scope.videosNoPLaylist = cacheVideos.slice(0);
            }
          } else{
            getVideos();
          }
        } else{            
          if (key && key.length > 0)
            searchVideosNonePlaylist(key, $scope.subject);
          else
            getVideosNonePlaylistBySubject();
        }
    }

    function getVideosNonePlaylistBySubject(){
      VideoService.getVideosNonePlaylistBySubject(pl_id, userId, $scope.subject, 0).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            $scope.videosNoPLaylist = formatVideos(data.data.request_data_result);
        } else
            $scope.videosNoPLaylist = null;
      });
    }

    $scope.onSelect = function(selected){
      if (selected)
        searchVideosNonePlaylist(selected.title, $scope.subject);
    }

    $scope.search = function(){
      var key = $('input#keyword').val();
      if (key.length > 0) {
        searchVideosNonePlaylist(key, $scope.subject);
      } else if(!key && $scope.subject > 0){
        getVideosNonePlaylistBySubject();
      } else
        $scope.videosNoPLaylist = cacheVideos;
    }

    function searchVideosNonePlaylist(keyword, subjectId){
      VideoService.searchVideosNonePlaylist(pl_id, userId, keyword, $scope.subject, 0).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          $scope.videosNoPLaylist = formatVideos(data.data.request_data_result);
        } else
          $scope.videosNoPLaylist = null;
      });
    }

    function searchMoreVideosNonePlaylist(keyword, subjectId){
      VideoService.searchVideosNonePlaylist(pl_id, userId, keyword, $scope.subject, 0).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          var oldArr = $scope.videosNoPLaylist;
          var newArr = formatVideos(data.data.request_data_result);
          $scope.videosNoPLaylist = oldArr.concat(newArr);
        }
      });
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
      }
      angular.forEach($scope.videosNoPLaylist, function(v){
          v.selected = status;
      })
    }

    // $scope.optionSelected = function(){
    //   // $scope.optionAll = $scope.videosNoPLaylist.every(function(v){
    //   //   return v.selected;
    //   // });
    //   var checked = true;
    //   for (var i = $scope.videosNoPLaylist.length - 1; i >= 0; i--) {
    //     checked =  $scope.videosNoPLaylist[i].selected;
    //     if (!checked)
    //       break;
    //   }
    //   $scope.optionAll = checked;
    // }

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
        $scope.addSubject = e;
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
      } else if ($scope.addSubject == 0) {
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
        "runningTime": $scope.duration,
        "description": description,
        "subjectId": $scope.addSubject,
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
        clearContent();
        return;    
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
      } else{
        $scope.error = "Please input valid link.";
        angular.element('#txtTutLink').trigger('focus');
      }
    }

    function getVideoInfo(vid){
      VideoService.getVideoInfoFromYoutube(vid).then(function(data){
        var result = data.data.items;
        if (result && result.length > 0) {            
          var contentDetails = result[0].contentDetails;
          $scope.duration = convertTime(contentDetails.duration);
          $scope.title = result[0].snippet.title;
          $scope.description = result[0].snippet.description;
          $('#txtTutTitle').val($scope.title);
          $('#txtTutDescription').val($scope.description);
          $('#txtTutDuration').val($scope.duration); 
        } else{
          clearContent();
          $scope.error = "Please input valid link. ";
          angular.element('#txtTutLink').trigger('focus');
        }
      });
    }

    function clearContent() {
      $('#txtTutTitle').val('');
      $('#txtTutDescription').val('');
      $('#txtTutDuration').val('');
      $scope.duration = null;
      $scope.description = null;
      $scope.title = null;
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
