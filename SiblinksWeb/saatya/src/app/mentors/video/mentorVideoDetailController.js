brotControllers.controller('MentorVideoDetailController', 
  ['$scope', '$modal', '$routeParams', '$http', '$location', 'VideoService', 'MentorService', 'videoDetailService',
                                       function ($scope, $modal, $routeParams, $http, $location, VideoService, MentorService, videoDetailService) {


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
            getVideoDetail();
            getCommentVideoDetail();
    	} else if (!isNaN(plid) && plid > 0) {
            // get playlist detail
            $scope.plid = plid;
            initYoutubePlayer();
        } 
        else{
    		window.location.href = '#/mentor/dashboard';
        	window.location.reload();
    	}
    }
    
    function getVideoDetail(){
        videoDetailService.getVideoDetailById(vid).then(function(data){
            var result = data.data.request_data_result;
            if (result && result.length > 0 && result != "Found no data") {
                $scope.video = result[0];
                $scope.video.averageRating = $scope.video.averageRating != null ? $scope.video.averageRating : 0;
                $scope.video.numViews = $scope.video.numViews != null ? $scope.video.numViews : 0;
                $scope.video.timeStamp = convertUnixTimeToTime($scope.video.timeStamp);
                initYoutubePlayer($scope.video.url);
            }
            console.log($scope.video);
        });
    }

    function getCommentVideoDetail(){
        videoDetailService.getCommentVideoById(vid).then(function(data){
            var result = data.data.request_data_result;
            if (result && result.length > 0 && result != "Found no data") {
                $scope.comments = formatDat(result);
            }
            console.log($scope.comments);
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
