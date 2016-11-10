brotControllers.controller('MentorVideoDetailViewOnlyController', 
  ['$rootScope','$scope', '$modal', '$routeParams', '$location', 'videoDetailService','PlaylistService', 'VideoService', 'CommentService',
                                       function ($rootScope,$scope, $modal, $routeParams, $location, videoDetailService, PlaylistService, VideoService, CommentService) {


    var vid = $routeParams.vid;
    var plid = $routeParams.plid;
    var userId = $routeParams.authorId;
    $scope.currentId = localStorage.getItem('userId');
    $scope.averageRating = 0.1;

    $scope.currentId = 0;
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
            $location.path('/mentor/dashboard');
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
                        $location.path('/mentor/video/view/detail/'+userId+'/'+$scope.videos[0].vid+'/'+plid);
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
        $scope.video.averageRating = parseAvgRating($scope.video.averageRating);
        $scope.averageRating = $scope.video.averageRating;
        $scope.video.numViews = $scope.video.numViews != null ? $scope.video.numViews : 0;
        initYoutubePlayer($scope.video.url);
        getCommentVideoDetail(v.vid);
        getVideoRelated();
        angular.element(document.getElementById('videos-in-list')).mCustomScrollbar('scrollTo','#listPlaylist' + $scope.currentId);
    }

    function getVideoDetail(id, userId){
        VideoService.getVideoById(id, userId).then(function(data){
            var result = data.data.request_data_result;
            if (result && result.length > 0 && result != "Found no data") {
                $scope.video = result[0];
                $scope.video.averageRating = parseAvgRating($scope.video.averageRating);
                $scope.averageRating = $scope.video.averageRating;
                $scope.video.numViews = $scope.video.numViews != null ? $scope.video.numViews : 0;
                $scope.video.timeStamp = convertUnixTimeToTime($scope.video.timeStamp);
                if ($scope.video.plid && $scope.video.plid > 0) {
                    $location.path('/mentor/video/view/detail/'+userId+'/'+id+'/'+$scope.video.plid);
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
        loadData(v);
    }

    function loadData(v){
        if (v.plid && v.plid > 0) {
            $location.path('/mentor/video/view/detail/'+userId+'/'+v.vid+'/'+$scope.video.plid);
        } else{
            $location.path('/mentor/video/view/detail/'+userId+'/'+v.vid);
        }
    }

    $scope.goToProfile = function(id){
        if (id == userId) {
        	$location.path('/mentor/mentorProfile');
        } else{
        	$location.path('/mentor/studentProfile/'+id);
        }
    }

    $scope.loadTo = function(){
        angular.element(document.getElementById('videos-in-list')).mCustomScrollbar('scrollTo','#listPlaylist' + $scope.currentId);
    }

    $scope.preVideo = function(pos){
        if ($scope.videos && $scope.videos.length > 0) {
            if (pos == 0) {
                $scope.pos = $scope.videos.length - 1;
                loadVideoDetail($scope.videos[$scope.pos]);
            } else{
                $scope.pos = pos - 1;
                loadVideoDetail($scope.videos[$scope.pos]);
            }
        }
        $location.path('/mentor/video/view/detail/'+userId+'/'+$scope.videos[$scope.pos].vid+'/'+plid, false);
        angular.element(document.getElementById('videos-in-list')).mCustomScrollbar('scrollTo','#listPlaylist' + $scope.currentId);
    }

    $scope.nextVideo = function(pos){
        if ($scope.videos && $scope.videos.length > 0) {
            if (pos == $scope.videos.length - 1) {
                $scope.pos = 0;
                loadVideoDetail($scope.videos[$scope.pos]);
            } else{
                $scope.pos = pos + 1;
                loadVideoDetail($scope.videos[$scope.pos]);
            }
            $location.path('/mentor/video/view/detail/'+userId+'/'+$scope.videos[$scope.pos].vid+'/'+plid, false);
            angular.element(document.getElementById('videos-in-list')).mCustomScrollbar('scrollTo','#listPlaylist' + $scope.currentId);
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
}]);
