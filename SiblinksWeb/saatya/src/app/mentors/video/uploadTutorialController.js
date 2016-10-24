brotControllers.controller('UploadTutorialController', 
  ['$rootScope','$scope', '$modal', '$modalInstance', '$routeParams', '$http', '$location', 'VideoService', 'videoDetailService', 'HomeService', 'myCache', 'u_id' , 'v_id',
                                       function ($rootScope, $scope, $modal, $modalInstance, $routeParams, $http, $location, VideoService, videoDetailService, HomeService, myCache, u_id, v_id) {

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
      if (sub) {
        var arr = angular.copy(sub);

        if (arr[0].subjectId != 0) {
            arr.splice(0, 0, {
              'subjectId': 0,
              'subject' : 'Select a Subject'
            }); 
            $scope.uploadSubjects = arr;
            $scope.uploadSubject = $scope.uploadSubjects[0].subjectId;
         }
      } else{
        HomeService.getAllCategory().then(function (data) {
           if (data.data.status) {
              var arr = angular.copy(data.data.request_data_result);
              arr.splice(0, 0, {
                'subjectId': 0,
                'subject' : 'Select a Subject'
              }); 
              $scope.uploadSubjects = arr;
              $scope.uploadSubject = $scope.uploadSubjects[0].subjectId;
           }
       });
      }      
    }

    function initPlaylist(){
      if (myCache.get("playlist") !== undefined) {
        $scope.playlists = myCache.get("playlists");
        $scope.uploadPlaylist = $scope.playlists[0].plid;
      } else{
        VideoService.getPlaylist(u_id).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            myCache.put("playlists", data.data.request_data_result);
            $scope.playlists = data.data.request_data_result;
            $scope.playlists.splice(0,0,{
              'plid':0,
              'name': "Select a Playlist"
            })
            $scope.uploadPlaylist = $scope.playlists[0].plid;
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
      checkLink($scope.editVideo.url);

      $scope.uploadSubject = $scope.editVideo.subjectId;
      $('#uploadSubject').val(getPositionSubject($scope.uploadSubject));
      $scope.uploadPlaylist = $scope.editVideo.plid != null ? $scope.editVideo.plid : 0;
      $('#uploadPlaylist').val(getPositionPlaylist($scope.uploadPlaylist));
    }

    function getPositionSubject(subid){
      var result = $.grep($scope.uploadSubjects, function(s){
        return s.subjectId == subid;
      });

      return $scope.uploadSubjects.indexOf(result[0]);
    }

    function getPositionPlaylist(plid){
      var result = $.grep($scope.playlists, function(p){
        return p.plid == plid;
      });

      return $scope.playlists.indexOf(result[0]);
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
      if (title == null || title.length == 0) {
        $scope.error = "Please input Title. \n";
        angular.element('#txtUploadTitle').trigger('focus');
        return;
      } else if ($scope.uploadSubject == 0) {
        $scope.error = "Please select a Subject. \n";
        angular.element('#uploadSubject').trigger('focus');
        return;
      }

      var request = {
        "vid": $scope.editVideo.vid,
        "title": title,
        "description": description,
        "subjectId": $scope.uploadSubject,
        "plid": $scope.uploadPlaylist > 0 ? $scope.uploadPlaylist : null
      }
      $rootScope.$broadcast('open');
      VideoService.updateTutorial(request).then(function(data){
        if (data.data.request_data_result === "Success") {
          if (!isNaN(v_id) && v_id > 0) {
            
             var video = {
               'vid': v_id,
               'title': title,
               'description': description,
               'plid': $scope.uploadPlaylist > 0 ? $scope.uploadPlaylist : null,
               'playlistname': $scope.uploadPlaylist > 0 ? getPlaylistName() : null,
               'subjectId': $scope.uploadSubject
             } 
             $rootScope.$broadcast('passing', video);

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

    function getPlaylistName(){
      var name = null;
      var result = $.grep($scope.playlists, function(v){
        return v.plid == $scope.uploadPlaylist;
      });

      var index = $scope.playlists.indexOf(result[0]);
      if (index != -1) {
        name = $scope.playlists[index].name;
      }
      return name;
    }

    $scope.upload = function(link, title, description){
      $scope.error = '';
      if (link == null || link.length == 0) {
        $scope.error = "Please input Link. \n";
        angular.element('#txtUploadLink').trigger('focus');
        return;
      } else if (title == null || title.length == 0) {
        $scope.error = "Please input Title. \n";
        angular.element('#txtUploadTitle').trigger('focus');
        return;
      } else if (!$scope.vid) {
        $scope.error = "Please input valid link. \n";
        angular.element('#txtUploadLink').trigger('focus');
        return;
      } else if (description && description.length > 1024) {
        $scope.error = "Description cannot longer than 1024 characters. \n";
        angular.element('#txtUploadDescription').trigger('focus');        
        return;
      } else if ($scope.uploadSubject == 0) {
        $scope.error = "Please select subject. \n";
        angular.element('#uploadSubject').trigger('focus');        
        return;
      }

      var thumbnail = 'http://img.youtube.com/vi/'+$scope.vid+'/hqdefault.jpg'
      var request = {
        "authorID": u_id,
        "title": title,
        "url": link,
        "runningTime": $scope.duration,
        "image": thumbnail,
        "description": description,
        "subjectId": $scope.uploadSubject,
        "plid": $scope.uploadPlaylist > 0 ? $scope.uploadPlaylist : null
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
        console.log(result);
        $rootScope.$broadcast('close');
      });
    }

    $scope.changeSubject = function(e){
      $scope.uploadSubject = e;
    }

    $scope.changPlaylist = function(e){
      $scope.uploadPlaylist = e;
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
      $scope.title = '';
      $scope.duration = '';
      $scope.description = '';

      $scope.uploadSubject = 0;
      $('#uploadSubject').val(0);
      $scope.uploadPlaylist = 0;
      $('#uploadPlaylist').val(0);  
      $scope.vid = null;
      $scope.link = null;
    }

    $scope.cancel = function(){
      $modalInstance.dismiss('cancel');
      $rootScope.$broadcast('close');
    }

    $scope.validateLink = function(link){
      if (link.length == 0)
        clearContent();
      checkLink(link);
      $scope.uploadLink = link;
    }

    function checkLink(link){
      var videoid = link.match(/(?:https?:\/{2})?(?:w{3}\.)?youtu(?:be)?\.(?:com|be)(?:\/watch\?v=|\/)([^\s&]+)/);
      if (videoid != null) {
        $scope.vid = videoid[1];
        if (!$scope.editVideo)
          getVideoInfo($scope.vid);
        if (player === undefined){          
          onYouTubeIframeAPIReady($scope.vid);             
        }          
        else
          player.cueVideoById($scope.vid);
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

    $scope.$on('addPlaylistVideo', function(){
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
