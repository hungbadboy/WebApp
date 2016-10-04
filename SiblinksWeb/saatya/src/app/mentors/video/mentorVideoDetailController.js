brotControllers.controller('MentorVideoDetailController', 
  ['$scope', '$modal', '$routeParams', '$http', '$location', 'videoDetailService','PlaylistService',
                                       function ($scope, $modal, $routeParams, $http, $location, videoDetailService, PlaylistService) {


    var vid = $routeParams.vid;
    var plid = $routeParams.plid;
    var userId = localStorage.getItem('userId');
    $scope.avatar = localStorage.getItem('imageUrl');
    
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

    init();

    function init(){
    	if (!isNaN(vid) && vid > 0) {
    		// get video detail
            $scope.vid = vid;
            getVideoDetail(vid, userId);
            getCommentVideoDetail();            
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

                getVideoDetail($scope.videos[0].vid, userId);                
            }
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

    function getVideoDetail(id, userId){
        console.log(id);
        videoDetailService.getVideoDetailMentor(id, userId).then(function(data){
            var result = data.data.request_data_result;
            console.log(result);
            if (result && result.length > 0 && result != "Found no data") {
                $scope.video = result[0];
                $scope.video.averageRating = $scope.video.averageRating != null ? $scope.video.averageRating : 0;
                $scope.video.numViews = $scope.video.numViews != null ? $scope.video.numViews : 0;
                $scope.video.timeStamp = convertUnixTimeToTime($scope.video.timeStamp);
                initYoutubePlayer($scope.video.url);
                getVideoRelated();
            }
        });
    }

    function getCommentVideoDetail(){
        videoDetailService.getCommentVideoById(vid).then(function(data){
            var result = data.data.request_data_result;
            if (result && result.length > 0 && result != "Found no data") {
                $scope.comments = formatDat(result);
            }
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
