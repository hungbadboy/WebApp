brotControllers.controller('UploadTutorialController', 
  ['$scope', '$modalInstance', '$routeParams', '$http', '$location', 'VideoService', 'MentorService', 'HomeService', 'myCache', 'u_id' ,
                                       function ($scope, $modalInstance, $routeParams, $http, $location, VideoService, MentorService, HomeService, myCache, u_id) {


    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';
    $scope.uploadSubject = [0];
    $scope.uploadPlaylist = [0];
    $scope.editVideo = null;

    init();

    function init(){
      initSubject();
      initPlaylist();
      loadVideoRecently();
    }

    function loadVideoRecently(){
      VideoService.getVideosRecently(u_id).then(function(data){
        if(data.data.request_data_result && data.data.request_data_result != "Found no data"){
          $scope.videos = data.data.request_data_result;
        }
      });
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

    $scope.openEdit = function (v){
      $scope.editVideo = v;
      $('#txtUploadTitle').val(v.title);
      $scope.link = v.url;
      checkLink($scope.link);
      $('#txtUploadDescription').val(v.description);
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
      var error = '';

      if (title == null || title.trim().length == 0) {
        check = false;
        error += "Please input Title. \n";
      }

      if (check) {
        var request = {
          "vid": $scope.editVideo.vid,
          "title": title.trim(),
          "description": description
        }

        VideoService.updateTutorial(request).then(function(data){
          loadVideoRecently();
          $scope.editVideo = null;
          clearContent();
        });
      } else{
        alert(error);
      }
    }

    $scope.upload = function(){
      var title = $('#txtUploadTitle').val();
      var link = $('#txtUploadLink').val();
      var description = $('#txtUploadDescription').val();

      var check = true;
      var error = '';
      if (title == null || title.trim().length == 0) {
        check = false;
        error += "Please input Title. \n";
      }
      if (link == null || link.trim().length == 0) {
        check = false;
        error += "Please input Link. \n";
      }
      if (!$scope.vid) {
        check = false;
        error += "Please input valid link. \n";
      }
      if ($('#uploadSubject').val() == 0) {
        check = false;
        error += "Please select subject. \n";
      }

      if (check) {
        var thumbnail = 'http://img.youtube.com/vi/'+$scope.vid+'/hqdefault.jpg'
        var request = {
          "authorID": u_id,
          "title": title.trim(),
          "url": link,
          "image": thumbnail,
          "description": description,
          "subjectId": $('#uploadSubject').val(),
          "plid": $('#uploadPlaylist').val() == 0 ? null : $('#uploadPlaylist').val()
        }
        VideoService.uploadTutorial(request).then(function(data){
          loadVideoRecently();
          clearContent();
        });
      } else{
        alert(error);
      }
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
