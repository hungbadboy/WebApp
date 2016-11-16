brotControllers.controller('UploadTutorialController', 
  ['$rootScope','$scope', '$modal', '$modalInstance', '$routeParams', '$window', '$location', 'VideoService', 'videoDetailService', 'HomeService', 'myCache', 'u_id' , 'v_id',
                                       function ($rootScope, $scope, $modal, $modalInstance, $routeParams, $window, $location, VideoService, videoDetailService, HomeService, myCache, u_id, v_id) {

    var userId = localStorage.getItem('userId');
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';
    $scope.uploadPlaylist = [0];
    $scope.editVideo = null;
    var sub = myCache.get("subjects");
    

    init();

    function init(){
      if (userId && userId > 0) {
        initSubject();
        initPlaylist();
        if (!isNaN(v_id) && v_id > 0) {
          getVideoDetail();
        } else{
          angular.element('#txtTutorialUrl').trigger('focus');
          loadVideoRecently();
        } 
      } else {
        window.localStorage.clear();
        window.location.href = '/';
      }           
    }

    function getVideoDetail(){
      VideoService.getVideoById(v_id, u_id).then(function(data){
        var result = data.data.request_data_result;
        if (result  && result != "Found no data") {
          displayEdit(result);
        }
      });
    }

    function loadVideoRecently(){
      VideoService.getVideosRecently(u_id).then(function(data){
        if(data.data.request_data_result && data.data.request_data_result != "Found no data"){
          $scope.videos = data.data.request_data_result;
        }
      });
    }

    function initSubject(){
      var subjects = localStorage.getItem("mentorSubjects");
      if (subjects != null) {
        $scope.uploadSubjects = JSON.parse(subjects);
        $scope.uploadSubject = $scope.uploadSubjects[0].subjectId;
      } else{
        HomeService.getAllCategory().then(function (data) {
          if (data.data.status) {
            var subjects = angular.copy(data.data.request_data_result);
            subjects = removeItem(subjects);
            subjects.splice(0, 0, {
              'subjectId': 0,
              'subject' : 'Select a Subject'
            }); 
            $scope.uploadSubjects = subjects;
            localStorage.setItem("mentorSubjects", JSON.stringify($scope.uploadSubjects), 10)
            $scope.uploadSubject = $scope.uploadSubjects[0].subjectId;
            if ($scope.editVideo) 
              displayEdit($scope.editVideo);
          }
        });
      }      
    }

    function initPlaylist(){
      var playlists = localStorage.getItem("playlists");
      if (playlists !== null) {
        $scope.playlists = JSON.parse(playlists);
        $scope.uploadPlaylist = $scope.playlists[0].plid;
      } else{
        VideoService.getPlaylist(u_id).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            $scope.playlists = data.data.request_data_result;
            $scope.playlists.splice(0,0,{
              'plid':0,
              'name': "Select a Playlist"
            });
            localStorage.setItem("playlists", JSON.stringify($scope.playlists), 10);
            $scope.uploadPlaylist = $scope.playlists[0].plid;
            if ($scope.editVideo) 
              displayEdit($scope.editVideo);
          }
        });
      }      
    }

    function displayEdit(v){
      if (Array.isArray(v)) {
        $scope.editVideo = v[0];
      } else{
        $scope.editVideo = v;
      }
      $scope.error = null;
      $scope.success = null;
      
      $scope.link = $scope.editVideo.url;
      $scope.title = $scope.editVideo.title;
      $scope.duration = $scope.editVideo.runningTime;
      $scope.description = $scope.editVideo.description;
      $('#txtTutorialTitle').val($scope.title);
      $('#txtTutorialDescription').val($scope.description);
      checkLink($scope.editVideo.url);

      var subId = $scope.editVideo.subjectId;
      $('#uploadSubject').val(getPositionSubject(subId));
      var plid = $scope.editVideo.plid != null ? $scope.editVideo.plid : 0;
      $('#uploadPlaylist').val(getPositionPlaylist(plid));
    }

    function getPositionSubject(subid){
      var result = $.grep($scope.uploadSubjects, function(s){
        return s.subjectId == subid;
      });
      var index = $scope.uploadSubjects.indexOf(result[0]);
      return index != -1 ? index : 0;
    }

    function getPositionPlaylist(plid){
      var result = $.grep($scope.playlists, function(p){
        return p.plid == plid;
      });
      var index = $scope.playlists.indexOf(result[0]);
      return index != -1 ? index : 0;
    }

    $scope.openEdit = function (v){
      displayEdit(v);
    }

    function getSubjectIndex(id,data){
      for (var i = data.length - 1; i >= 0; i--) {
        if(data[i].subjectId == id)
          return i;
      }
    }

    $scope.update = function(title, description){
      var plPos = $('#uploadPlaylist').val();
      var subjectPos = $('#uploadSubject').val();
      var title = $('#txtTutorialTitle').val();
      var description = $('#txtTutorialDescription').val();
      if (title == null || title.length == 0) {
        $scope.error = "Please input Title. \n";
        angular.element('#txtTutorialTitle').trigger('focus');
        return;
      } else if (subjectPos == 0) {
        $scope.error = "Please select a Subject. \n";
        angular.element('#uploadSubject').trigger('focus');
        return;
      }

      var request = {
        "authorID": userId,
        "vid": $scope.editVideo.vid,
        "title": title,
        "description": description,
        "subjectId": $scope.uploadSubjects[subjectPos].subjectId,
        "plid": plPos > 0 ? $scope.playlists[plPos].plid : null
      }
      $rootScope.$broadcast('open');
      VideoService.updateTutorial(request).then(function(data){
        if (data.data.request_data_result === "Success") {
          if (!isNaN(v_id) && v_id > 0) {
             request['playlistname'] = plPos > 0 ? getPlaylistName($scope.playlists[plPos].plid) : null;
             request['subject'] = getSubjectName($scope.uploadSubjects[subjectPos].subjectId);
             $rootScope.$broadcast('passing', request);

             $modalInstance.dismiss('cancel');
          }else{
            $scope.success = "Update Tutorial successful.";
            $scope.editVideo = null;
            loadVideoRecently();
            clearContent(); 
          }            
        } else{
          $scope.error = data.data.request_data_result;
        }
        $rootScope.$broadcast('close');
      });
    }

    function getSubjectName(subid){
      var name = null;
      var result = $.grep($scope.uploadSubjects, function(v){
        return v.subjectId == subid;
      });

      var index = $scope.uploadSubjects.indexOf(result[0]);
      if (index != -1) {
        name = $scope.uploadSubjects[index].subject;
      }
      return name;
    }
    function getPlaylistName(plid){
      var name = null;
      var result = $.grep($scope.playlists, function(v){
        return v.plid == plid;
      });

      var index = $scope.playlists.indexOf(result[0]);
      if (index != -1) {
        name = $scope.playlists[index].name;
      }
      return name;
    }

    $scope.upload = function(){
      $scope.success = null;
      var link = $('#txtTutorialUrl').val();
      var title = $('#txtTutorialTitle').val();
      var description = $('#txtTutorialDescription').val();
      var duration = $('#txtTutorialDuration').val();
      var plPos = $('#uploadPlaylist').val();
      var subjectPos = $('#uploadSubject').val();
      
      if (link == null || link.length == 0) {
        $scope.error = "Please input Link. \n";
        angular.element('#txtTutorialUrl').trigger('focus');
        return;
      } else if ($scope.error != undefined && $scope.error == "Please input valid link.") {
        $scope.error = "Please input valid link.";
        angular.element('#txtTutorialUrl').trigger('focus');
        return;
      } else if (title == null || title.length == 0) {
        $scope.error = "Please input Title. \n";
        angular.element('#txtTutorialTitle').trigger('focus');
        return;
      } else if (description && description.length > 1024) {
        $scope.error = "Description cannot longer than 1024 characters. \n";
        angular.element('#txtTutorialDescription').trigger('focus');        
        return;
      } else if (subjectPos == 0) {
        $scope.error = "Please select subject. \n";
        angular.element('#uploadSubject').trigger('focus');        
        return;
      } else
        $scope.error = null;

      if ($scope.error == null || $scope.error == '') {
        var thumbnail = 'http://img.youtube.com/vi/'+$scope.vid+'/hqdefault.jpg'
        var request = {
          "authorID": u_id,
          "title": title,
          "url": link,
          "runningTime": duration,
          "image": thumbnail,
          "description": description,
          "subjectId": $scope.uploadSubjects[subjectPos].subjectId,
          "plid": plPos > 0 ? $scope.playlists[plPos].plid : null
        }
        $rootScope.$broadcast('open');
        VideoService.uploadTutorial(request).then(function(data){
          var result = data.data.request_data_result
          if (result === "Success") {
            $scope.success = "Upload Tutorial successful.";
            $rootScope.$broadcast("uploadNew");
            loadVideoRecently();
            clearContent();
          } else{
            $scope.error = result;
          }
          $rootScope.$broadcast('close');
        });
      }
    }

    $scope.delete = function(vid){
      VideoService.deleteVideo(vid, u_id).then(function(data){
        if (data.data.status) {
           loadVideoRecently();       
        }
      });
    }

    $scope.addToPlaylist = function(v){
      var selectedVideos = [v.vid];
      openAddPlaylistPopup(selectedVideos);
    }

    function openAddPlaylistPopup(selectedVideos){
      var modalInstance = $modal.open({
        templateUrl: 'src/app/mentors/video/choose_playlist_popup.tpl.html',
        controller: 'ChoosePlaylistController',
        resolve: {
            u_id: function () {
                return userId;
            },
            v_ids: function(){
              return selectedVideos;
            }
        }
      });
    }

    function clearContent(){
      $('#txtTutorialUrl').val('');
      $('#txtTutorialTitle').val('');
      $('#txtTutorialDescription').val('');
      $scope.uploadLink = null;
      $scope.title = null;
      $scope.duration = null;
      $scope.description = null;

      $('#uploadSubject').val(0);
      $('#uploadPlaylist').val(0);  
      $scope.vid = null;
      $scope.link = null;
    }

    $scope.cancel = function(){
      $modalInstance.dismiss('cancel');
      $rootScope.$broadcast('close');
    }

    $scope.validateLink = function(){
      $scope.success = null;
      var link = $('#txtTutorialUrl').val();
      if (link == null || link.length == 0){
        clearContent();
        return;
      }
      checkLink(link);
      $scope.uploadLink = link;
    }

    function checkLink(link){
      var videoid = link.match(/(?:https?:\/{2})?(?:w{3}\.)?youtu(?:be)?\.(?:com|be)(?:\/watch\?v=|\/)([^\s&]+)/);
      if (videoid != null) {
        $scope.error = null;
        $scope.vid = videoid[1];
        if (!$scope.editVideo)
          getVideoInfo($scope.vid);
        if (player === undefined){          
          onYouTubeIframeAPIReady($scope.vid);             
        }          
        else
          player.cueVideoById($scope.vid);
      } else{
        $scope.error = "Please input valid link.";
        angular.element('#txtTutorialUrl').trigger('focus');
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
            $('#txtTutorialTitle').val($scope.title);
            $('#txtTutorialDescription').val($scope.description);
          } else{
            $scope.title = null;
            $scope.duration = null;
            $scope.description = null;
            $('#txtTutorialTitle').val('');
            $('#txtTutorialDescription').val('');
            $('#txtTutorialDuration').val('');
            $scope.error = "Please input valid link.";
            angular.element('#txtTutorialUrl').trigger('focus');
          }
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

    $scope.$on('addPlaylistVideo', function(){
      initPlaylist();
      loadVideoRecently();
    });

    $scope.$on('addPlaylist', function(){
      loadVideoRecently();
    });

    var player;
    function onYouTubeIframeAPIReady(youtubeId) {
      player = new YT.Player('uplad_player', {
          height: '330',
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