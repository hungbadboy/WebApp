brotControllers.controller('MentorVideoDetailController', 
  ['$rootScope','$scope', '$modal', '$routeParams', '$location', 'videoDetailService','PlaylistService', 'VideoService', 'CommentService',
                                       function ($rootScope,$scope, $modal, $routeParams, $location, videoDetailService, PlaylistService, VideoService, CommentService) {


    var vid = $routeParams.vid;
    var plid = $routeParams.plid;
    var userId = localStorage.getItem('userId');
    $scope.avatar = localStorage.getItem('imageUrl');
    var userName = localStorage.getItem('nameHome') != null ?  localStorage.getItem('nameHome') : "";

    $scope.averageRating = 0.1;
    $scope.loadRate = false;
    $scope.currentId = vid;
    init();

    function init(){
        if (userId && userId > 0) {
            if (!isNaN(vid) && vid > 0 ) {
                $scope.vid = vid;
                if (!isNaN(plid) && plid > 0) {
                    $scope.plid = plid;
                    loadPlaylistDetail();
                    getVideosInPlaylist();
                } else {
                    getVideoDetail(vid, userId);
                    getCommentVideoDetail(vid); 
                }
            } else {
                $location.path('/mentor/dashboard');
            }
        } else {
            window.localStorage.clear();
            $location.path('/');
        }
    }
    
    function loadPlaylistDetail(){
        PlaylistService.loadPlaylistById(plid).then(function(data){
            if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                var data = data.data.request_data_result;

                data.numView = data.numView != null ? data.numView : 0;
                data.numComment = data.numComment != null ? data.numComment : 0;
                data.timeStamp = convertUnixTimeToTime(data.timeStamp);
                var fullname = data.firstName + ' ' + data.lastName;
                data.fullName = fullname != ' ' ? fullname : data.userName.substr(0, data.userName.indexOf('@'));
                $scope.playlist = data;
            }
        });
    }

    function getVideosInPlaylist(){
        videoDetailService.getVideoByPlaylistId(plid).then(function (data) {
            var result = data.data.request_data_result;
            if (result && result != "Found no data") {
                for (var i = result.length - 1; i >= 0; i--) {
                    result[i].timeStamp = convertUnixTimeToTime(result[i].timeStamp);
                    result[i].subjectId = result[i].subjectVid;
                }
                $scope.videos = result;
                if (vid && vid > 0) {
                    var items = $.grep($scope.videos, function(v){
                        return v.vid == vid;
                    });
                    if (items.length > 0) {
                        var index = $scope.videos.indexOf(items[0]);
                        if (index != -1) {
                            $scope.currentId = $scope.videos[index].vid;
                            loadVideoDetail($scope.videos[index]);
                            $scope.pos = index;
                        }
                    } else {
                        $location.path('/mentor/video/detail/'+$scope.videos[0].vid+'/'+plid);
                    }                    
                } else{
                    loadVideoDetail($scope.videos[0]);
                    $scope.pos = 0;
                }
            } else
                $location.path('/mentor/playlist/detail/'+plid);
        });
    }

    function getVideoRelated(){
        videoDetailService.getVideoRelatedMentor($scope.video.vid, $scope.video.subjectId, userId, 0).then(function(data){
            var result = data.data.request_data_result;
            if (result && result != "Found no data") {
                $scope.videosRelated = result;
            }
        });
    }

    $scope.loadMoreVideoRelated = function(){
        videoDetailService.getVideoRelatedMentor($scope.video.vid, $scope.video.subjectId, userId, $scope.videosRelated.length).then(function(data){
            var result = data.data.request_data_result;
            if (result && result != "Found no data") {
                var oldData = $scope.videosRelated;
                var newData = result;
                var totalData = oldData.concat(newData);
                $scope.videosRelated = totalData;
            }
        });
    }

    function loadVideoDetail(v){
        $scope.currentId = v.vid;
        $scope.video = v;
        //$scope.video.averageRating = parseAvgRating($scope.video.averageRating);
       // $scope.averageRating = $scope.video.averageRating;
        $scope.video.numViews = $scope.video.numViews != null ? $scope.video.numViews : 0;
        initYoutubePlayer($scope.video.url);
        getCommentVideoDetail(v.vid);
        getVideoRelated();
        // $location.path('/mentor/video/detail/'+v.vid+'/'+plid, false);
        angular.element(document.getElementById('videos-in-list')).mCustomScrollbar('scrollTo','#listPlaylist' + $scope.currentId);
    }

    function getVideoDetail(id, userId){
        VideoService.getVideoById(id, userId).then(function(data){
            var result = data.data.request_data_result;
            if (result && result.length > 0 && result != "Found no data") {
                $scope.video = result[0];
                //$scope.video.averageRating = parseAvgRating($scope.video.averageRating);
                //$scope.video.numRatings = $scope.video.numRatings;
                //$scope.averageRating = $scope.video.averageRating;
                $scope.video.numViews = $scope.video.numViews != null ? $scope.video.numViews : 0;
                $scope.video.timeStamp = convertUnixTimeToTime($scope.video.timeStamp);
                if ($scope.video.plid && $scope.video.plid > 0) {
                    $location.path('/mentor/video/detail/'+id+'/'+$scope.video.plid);
                } else{
                    initYoutubePlayer($scope.video.url);
                    getCommentVideoDetail($scope.video.vid);
                    getVideoRelated();
                }
            }
        });
    }

    function parseAvgRating(data){
        var avg = data != null ? data : 0;
        return parseFloat(Math.round(avg * 100) / 100).toFixed(1);
    }

    function getCommentVideoDetail(vid){
        videoDetailService.getCommentVideoById(vid).then(function(data){
            var result = data.data.request_data_result;
            if (result && result.length > 0 && result != "Found no data") {
                $scope.comments = formatData(result);
            } else
                $scope.comments = null;
        });
    }

    function formatData(data){
        for (var i = data.length - 1; i >= 0; i--) {
            data[i].timestamp = convertUnixTimeToTime(data[i].timestamp);
            var firstname = data[i].firstName != null ? data[i].firstName : '';
            var lastname = data[i].lastName != null ? data[i].lastName : '';
            var fullname = firstname + ' ' + lastname;
            if (data[i].userid == userId)
                data[i].fullName = 'You';
            else
                data[i].fullName = fullname != ' ' ? fullname : data[i].userName.substr(0, data[i].userName.indexOf('@'));
            data[i].imageUrl = data[i].imageUrl != null ? data[i].imageUrl : 'assets/images/noavartar.jpg';
            data[i].content = decodeURIComponent(data[i].content);
        }
        return data;
    }

    $scope.selectIndex = function(index){
        if (index > $scope.videos.length) 
            index = 0;
        $scope.pos = index;
        loadVideoDetail($scope.videos[index]);
    }

    function initYoutubePlayer(url){
        var videoid = url.match(/(?:https?:\/{2})?(?:w{3}\.)?youtu(?:be)?\.(?:com|be)(?:\/watch\?v=|\/)([^\s&]+)/);
        if (videoid != null) {
            $scope.vid = videoid[1];
        if (player === undefined)
            onYouTubeIframeAPIReady($scope.vid);
        else
            player.loadVideoById($scope.vid);
        }
    }

    $scope.loadVideo = function(v){
    	resetMessage();
        loadData(v);
    }

    function loadData(v){
        if (v.plid && v.plid > 0) {
        	$location.path('/mentor/video/detail/'+v.vid+'/'+v.plid);
        } else{
        	$location.path('/mentor/video/detail/'+v.vid);
        }
    }

    $scope.goToProfile = function(id){
        if (id == userId) {
        	$location.path('/mentor/mentorProfile');
        } else{
        	$location.path('/mentor/studentProfile/'+id);
        }
    }

    $scope.openAddPlaylist = function(vid){
    	resetMessage();
        var selectedVideos = [];
        selectedVideos.push(vid);
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

    $scope.$on('passing', function(e, a){
        $scope.video.title = a.title;
        $scope.video.description = a.description;
        $scope.video.subject = a.subject;
    })

    $scope.editVideo = function(vid){
    	resetMessage();
        var modalInstance = $modal.open({
            templateUrl: 'src/app/mentors/video/upload_tutorial_popup.tpl.html',
            controller: 'UploadTutorialController',
            resolve:{
              u_id: function () {
                  return userId;
              },
              v_id: function(){
                return vid;
              }
            }
        });
    }

    $scope.deleteVideo = function(vid){
    	resetMessage();
        var selectedVideos = [];
        selectedVideos.push(vid);
        VideoService.deleteMultipleVideo(selectedVideos, userId).then(function(data){
           if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
                if ($scope.videos) {
                    $scope.videos.splice($scope.pos, 1);
                    if ($scope.videos.length == 0) {
                        $location.path('/mentor/mentorVideo');
                    } else{
                        $scope.currentId = $scope.videos[$scope.pos].vid;
                        loadVideoDetail($scope.videos[$scope.pos]); 
                    }                      
                } else{
                    if ($scope.videosRelated) {
                        loadData($scope.videosRelated[0]);
                    } else{
                        $location.path('/mentor/mentorVideo');
                    }
                }
           }
        });
    }

    $scope.deleteComment = function(cid, vid){
    	resetMessage();
        CommentService.deleteComment(cid).then(function(data){
            if (data.data.status) {
                $scope.video.numComments -= 1;
                getCommentVideoDetail(vid);
            }
        });
    }

    $scope.loadTo = function(){
    	resetMessage();
        angular.element(document.getElementById('videos-in-list')).mCustomScrollbar('scrollTo','#listPlaylist' + $scope.currentId);
    }

    $scope.showButton = function(){
    	resetMessage();
        $(".comment-action").show();
    }

    $scope.cancelComment = function(){
    	resetMessage();
        $('#txtComment').val('');
        $(".comment-action").hide();
    }

    $scope.validateText = function(){
    	resetMessage();
        if ($scope.contentText && $scope.contentText.length < 1024) 
        	$scope.msgError = null;
    }

    $scope.addComment = function(){
    	resetMessage();
        var content = $('#txtComment').val();
        if (isEmpty(content)) {
            return;
        }
        if (isEmpty(userId)) {
            $scope.errorVideo = "Please login";
            return;
        }
        try {
	        var objRequest = {
	            authorID: userId,
	            content: content,
	            vid: $scope.video.vid,
	            title: $scope.video.title,
	            subjectId: $scope.video.subjectId,
	            author: userName
	        };
	        $rootScope.$broadcast('open');
	        videoDetailService.addCommentVideo(objRequest).success(function (data) {
	            if (data.status == 'true') {
	                $("#txtComment").val('');
	                $(".comment-action").hide();
	                getCommentVideoDetail($scope.video.vid);
	                $scope.msgSuccess = "You have added comment successful.";
	            } else{
	                $scope.msgError = data.request_data_result;
	                console.log($scope.error);
	            }
	        });
        } catch(e) {
        	console.log(e.description)
        } finally {
        	$rootScope.$broadcast('close');
        }
    }

    $scope.preVideo = function(pos){
    	resetMessage();
        if ($scope.videos && $scope.videos.length > 0) {
            if (pos == 0) {
                $scope.pos = $scope.videos.length - 1;
                loadVideoDetail($scope.videos[$scope.pos]);
            } else{
                $scope.pos = pos - 1;
                loadVideoDetail($scope.videos[$scope.pos]);
            }
            $location.path('/mentor/video/detail/'+$scope.videos[$scope.pos].vid+'/'+plid, false);
        }
    }

    $scope.nextVideo = function(pos){
    	resetMessage();
        if ($scope.videos && $scope.videos.length > 0) {
            if (pos == $scope.videos.length - 1) {
                $scope.pos = 0;
                loadVideoDetail($scope.videos[$scope.pos]);
            } else{
                $scope.pos = pos + 1;
                loadVideoDetail($scope.videos[$scope.pos]);
            }
            $location.path('/mentor/video/detail/'+$scope.videos[$scope.pos].vid+'/'+plid, false);
        }
    }

    var player;
    function onYouTubeIframeAPIReady(youtubeId) {
      player = new YT.Player('detailPlayer', {
          height: '100%',
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
      event.target.playVideo();
    }

    function onPlayerStateChange(event) {
    }

    $scope.$on('addPlaylist', function(e, value){
        $location.path('/mentor/video/detail/'+$scope.video.vid+'/'+value);
    });

    $scope.$on('passing', function(e, video){
        if (video.plid != null){
            $location.path('/mentor/video/detail/'+$scope.video.vid+'/'+video.plid);
        }
        else{
            $location.path('/mentor/video/detail/'+$scope.video.vid);
        }
    });

    $scope.$on('addPlaylistVideo', function(e, data){
        reloadPlaylistCache();
    });

    function reloadPlaylistCache(data){
      VideoService.getPlaylist(userId).then(function(data){
        if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
          var playlists = data.data.request_data_result;
          playlists.splice(0,0,{
            'plid':0,
            'name': "Select a Playlist"
          });
          localStorage.removeItem("playlists");
          localStorage.setItem("playlists", JSON.stringify(playlists), 10);
          $location.path('/mentor/video/detail/'+$scope.video.vid+'/'+data.plid);
        }
      });
    }
    function resetMessage() {
    	$scope.msgError = "";
    	$scope.msgSuccess ="";
    }
}]);