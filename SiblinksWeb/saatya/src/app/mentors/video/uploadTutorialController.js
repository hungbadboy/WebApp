brotControllers.controller('UploadTutorialController', 
  ['$rootScope','$scope', '$modalInstance', '$routeParams', '$http', '$location', 'VideoService', 'videoDetailService', 'HomeService', 'myCache', 'u_id' , 'v_id',
                                       function ($rootScope, $scope, $modalInstance, $routeParams, $http, $location, VideoService, videoDetailService, HomeService, myCache, u_id, v_id) {


    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';
    $scope.uploadPlaylist = [0];
    $scope.editVideo = null;

    var sub = myCache.get("subjects");

    init();

    function init(){
      if (!isNaN(v_id) && v_id > 0) {
        getVideoDetail();
      } else{
        initSubject();
        initPlaylist();
        loadVideoRecently();
      }      
    }

    function getVideoDetail(){
      videoDetailService.getVideoDetailById(v_id).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          displayEdit(data.data.request_data_result);
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
            $scope.uploadTutSubjects = arr;
         }
         $scope.uploadTutSubject = $scope.uploadTutSubjects[0].subjectId;
      } else{
        HomeService.getAllCategory().then(function (data) {
           if (data.data.status) {
              localStorage.setItem("subjects", data.data.request_data_result);
              var arr = angular.copy(data.data.request_data_result);
              arr.splice(0, 0, {
                'subjectId': 0,
                'subject' : 'Select a Subject'
              }); 
              $scope.uploadTutSubject = arr;
              $scope.uploadTutSubject = $scope.uploadTutSubjects[0].subjectId;
           }
       });
      }      
    }

    function initPlaylist(){
      if (myCache.get("playlist") !== undefined) {
        $scope.playlists = myCache.get("playlists");
      } else{
        VideoService.getPlaylist(u_id).then(function(data){
          if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
            $scope.playlists = data.data.request_data_result;
            myCache.put("playlists", data.data.request_data_result);
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
      
      $('#txtUploadTitle').val($scope.editVideo.title);
      $scope.link = $scope.editVideo.url;
      checkLink($scope.link);
      $('#txtUploadDescription').val($scope.editVideo.description);
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

    $scope.update = function(){
      var title = $('#txtUploadTitle').val();
      var description = $('#txtUploadDescription').val();

      var check = true;

      if (title == null || title.trim().length == 0) {
        check = false;
        $scope.error = "Please input Title. \n";
        angular.element('#txtUploadTitle').trigger('focus');
      }

      if (check) {
        var request = {
          "vid": $scope.editVideo.vid,
          "title": title.trim(),
          "description": description
        }

        VideoService.updateTutorial(request).then(function(data){
          if (data.data.request_data_result === "Success") {
            if (!isNaN(v_id) && v_id > 0) {
               var video = {
                 'vid': v_id,
                 'title': title.trim(),
                 'description': description
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
        });
      }
    }

    $scope.upload = function(){
      var title = $('#txtUploadTitle').val();
      var link = $('#txtUploadLink').val();
      var description = $('#txtUploadDescription').val();

      var check = true;
      $scope.error = '';
      if (title == null || title.trim().length == 0) {
        check = false;
        $scope.error = "Please input Title. \n";
        angular.element('#txtUploadTitle').trigger('focus');
      } else if (link == null || link.trim().length == 0) {
        check = false;
        $scope.error = "Please input Link. \n";
        angular.element('#txtUploadLink').trigger('focus');
      } else if (!$scope.vid) {
        check = false;
        $scope.error = "Please input valid link. \n";
        angular.element('#txtUploadLink').trigger('focus');
      } else if ($scope.uploadTutSubject == 0) {
        check = false;
        $scope.error = "Please select subject. \n";
        angular.element('#uploadSubject').trigger('focus');        
      }

      if (check) {
        var thumbnail = 'http://img.youtube.com/vi/'+$scope.vid+'/hqdefault.jpg'
        var request = {
          "authorID": u_id,
          "title": title.trim(),
          "url": link,
          "image": thumbnail,
          "description": description,
          "subjectId": $scope.uploadTutSubject,
          "plid": $('#uploadPlaylist').val() == 0 ? null : $('#uploadPlaylist').val()
        }
        VideoService.uploadTutorial(request).then(function(data){
          if (data.data.request_data_result === "Success") {
            $scope.success = "Upload Tutorial successful.";
            $rootScope.$broadcast("uploadNew");
            loadVideoRecently();
            clearContent();
          } else{
            $scope.error = data.data.request_data_result;
          }
        });
      }
    }

    $scope.changeSubject = function(e){
      $scope.uploadTutSubject = e;
    }

    $scope.delete = function(vid){
      if (confirm("Are you sure?")) {
        VideoService.deleteVideo(vid, u_id).then(function(data){
          if (data.data.status) {
             loadVideoRecently();       
          }
        });
      }
    }

    function clearContent(){
      $('#txtUploadTitle').val('');
      $('#txtUploadLink').val('');
      $('#txtUploadDescription').val('');

      $scope.uploadSubject = [0];
      $scope.uploadPlaylist = [0];
      $scope.vid = null;
      $scope.link = null;
    }

    $scope.cancel = function(){
      $modalInstance.dismiss('cancel');
    }

    $scope.validateLink = function(){
      checkLink($('#txtUploadLink').val());
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
      player = new YT.Player('uplad_player', {
          height: '260',
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
