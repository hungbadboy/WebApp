brotControllers.controller('MentorVideoDetailController', 
  ['$rootScope','$scope', '$modal', '$routeParams', '$http', '$location', 'videoDetailService','PlaylistService', 'VideoService', 'CommentService',
                                       function ($rootScope,$scope, $modal, $routeParams, $http, $location, videoDetailService, PlaylistService, VideoService, CommentService) {


    var vid = $routeParams.vid;
    var plid = $routeParams.plid;
    var userId = localStorage.getItem('userId');
    var vidInPlaylist = localStorage.getItem('vidInPlaylist');
    $scope.avatar = localStorage.getItem('imageUrl');
    
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

    $scope.averageRating = 0.1;

    $scope.currentId = 0;
    init();

    function init(){
    	if (!isNaN(vid) && vid > 0) {
    		// get video detail
            $scope.vid = vid;
            getVideoDetail(vid, userId);
            getCommentVideoDetail(vid);            
    	} else if (!isNaN(plid) && plid > 0) {
            // get playlist detail
            $scope.plid = plid;
            angular.element(document.querySelector('#video_balance')).remove();            
            loadPlaylistDetail();
            getVideosInPlaylist();
        } 
        else{
    		window.location.href = '#/mentor/dashboard';
        	window.location.reload();
    	}
    }
    
    function loadPlaylistDetail(){
        PlaylistService.loadPlaylistById(plid).then(function(data){            
            if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                var data = data.data.request_data_result;

                data.numView = data.numView != null ? data.numView : 0;
                data.numComment = data.numComment != null ? data.numComment : 0;
                data.timeStamp = convertUnixTimeToTime(data.timeStamp); 
                $scope.playlist = data;                
            }
        });
    }

    function getVideosInPlaylist(){
        videoDetailService.getVideoByPlaylistId(plid).then(function (data) {
            if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
                $scope.videos = data.data.request_data_result;
                for (var i = $scope.videos.length - 1; i >= 0; i--) {
                    $scope.videos[i].timeStamp = convertUnixTimeToTime($scope.videos[i].timeStamp);
                }
                if (vidInPlaylist && vidInPlaylist > 0) {
                    var result = $.grep($scope.videos, function(v){
                        return v.vid == vidInPlaylist;
                    });

                    var index = $scope.videos.indexOf(result[0]);
                    if (index != -1) {
                        $scope.currentId = $scope.videos[index].vid;
                        loadVideoDetail($scope.videos[index]);
                        $scope.pos = index;
                    }
                } else{
                    loadVideoDetail($scope.videos[0]);
                    $scope.pos = 0;
                }
            } else
                $scope.videos = null;
        });
    }
    function getVideoRelated(){
        videoDetailService.getVideoRelatedMentor($scope.video.subjectId, userId, 0).then(function(data){
            var result = data.data.request_data_result;
            if (result && result != "Found no data") {
                $scope.videosRelated = result;        
            }
        });
    }

    $scope.loadMoreVideoRelated = function(){
        videoDetailService.getVideoRelatedMentor($scope.video.subjectId, userId, $scope.videosRelated.length).then(function(data){
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
        $scope.video.timeStamp = convertUnixTimeToTime($scope.video.timeStamp);
        initYoutubePlayer($scope.video.url);
        getVideoRelated();
        getCommentVideoDetail(v.vid);
    }

    function getVideoDetail(id, userId){
        videoDetailService.getVideoDetailMentor(id, userId).then(function(data){
            var result = data.data.request_data_result;
            if (result && result.length > 0 && result != "Found no data") {
                $scope.video = result[0];
                console.log($scope.video);
                $scope.video.averageRating = parseAvgRating($scope.video.averageRating);
                $scope.averageRating = $scope.video.averageRating;
                $scope.video.numViews = $scope.video.numViews != null ? $scope.video.numViews : 0;
                $scope.video.timeStamp = convertUnixTimeToTime($scope.video.timeStamp);
                initYoutubePlayer($scope.video.url);
                getVideoRelated();
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
                $scope.comments = formatDat(result);
            } else
                $scope.comments = null;
        });
    }

    function formatDat(data){
        for (var i = data.length - 1; i >= 0; i--) {
            data[i].timestamp = convertUnixTimeToTime(data[i].timestamp);
            var firstname = data[i].firstName;
            if (firstname == null || firstname.length == 0)
                firstname = '';
            
            var lastname = data[i].lastName;
            if (lastname == null || lastname.length == 0)
                lastname = '';
            if (data[i].userid == userId)
                data[i].fullName = 'You';
            else
                data[i].fullName = firstname + ' ' + lastname;
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
            player.cueVideoById($scope.vid);
        }
    }

    $scope.loadVideo = function(v){
        loadData(v);
    }

    function loadData(v){
        if (v.plid && v.plid > 0) {
            setStorage('vidInPlaylist', v.vid, 30);
            window.location.href = '#/mentor/playlist/playall/'+v.plid+'';
            window.location.reload();
        } else{
            window.location.href = '#/mentor/video/detail/'+v.vid+'';
            window.location.reload();
        }
    }

    $scope.goToProfile = function(id){
        if (id == userId) {
            window.location.href = '#/mentor/mentorProfile';
            window.location.reload();
        } else{
            window.location.href = '#/mentor/studentProfile/'+id+'';
            window.location.reload();
        }
    }

    $scope.class = 'show-less';
    $scope.textShow = "Show more";

    $scope.showMore = function(){
        if ($scope.class == 'show-more') {
            $scope.class = 'show-less';
            $scope.textShow = 'Show more';
        } else{
            $scope.class = 'show-more';
            $scope.textShow = "Show less";
        }
    }

    $scope.$on('passing', function(e, a){
        $scope.video.title = a.title;
        $scope.description = a.description;
    })

    $scope.editVideo = function(vid){
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
        var selectedVideos = [];
        selectedVideos.push(vid);
        VideoService.deleteMultipleVideo(selectedVideos, userId).then(function(data){
           if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
                if ($scope.videos) {
                    $scope.videos.splice($scope.pos, 1);
                    if ($scope.videos.length == 0) {
                        window.location.href = '#/mentor/mentorVideo';
                        window.location.reload();
                    } else{
                        $scope.currentId = $scope.videos[$scope.pos].vid;
                        loadVideoDetail($scope.videos[$scope.pos]); 
                    }                      
                } else{
                    if ($scope.videosRelated) {
                        loadData($scope.videosRelated[0]);
                    } else{
                        window.location.href = '#/mentor/mentorVideo';
                        window.location.reload();
                    }
                }
           }
        });
    }

    $scope.deleteComment = function(cid, vid){
        CommentService.deleteComment(cid).then(function(data){
            if (data.data.status) {
                $scope.video.numComments -= 1;
                getCommentVideoDetail(vid);
            }
        });
    }

    $scope.showButton = function(){
        $(".comment-action").show();
    }

    $scope.cancelComment = function(){
        $('#txtComment').val('');
        $(".comment-action").hide();
    }

    $scope.addComment = function(){
        var content = $('#txtComment').val();
        if (isEmpty(content)) {
            return;
        }
        if (isEmpty(userId)) {
            $scope.errorVideo = "Please login";
            return;
        }

        videoDetailService.addCommentVideo(userId, content, vid).success(function (data) {
            if (data.status == 'true') {
                $("#txtComment").val('');
                $(".comment-action").hide();
                videoDetailService.getCommentVideoById(vid).then(function (data) {
                    if (data.data.status == 'true') {
                        if (data.data.request_data_result.length > 0) {
                            $scope.comments = formatDat(data.data.request_data_result);
                        }
                    }

                });
            }
        });

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
        }
    }

    $scope.addToPlaylist = function(vid){

    }

    var player;
    function onYouTubeIframeAPIReady(youtubeId) {
      player = new YT.Player('detailPlayer', {
          height: '450',
          width: '640',
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
