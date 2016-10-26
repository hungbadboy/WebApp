brotControllers.controller('MentorVideoDetailController', 
  ['$rootScope','$scope', '$modal', '$routeParams', '$http', '$location', 'videoDetailService','PlaylistService', 'VideoService', 'CommentService',
                                       function ($rootScope,$scope, $modal, $routeParams, $http, $location, videoDetailService, PlaylistService, VideoService, CommentService) {


    var vid = $routeParams.vid;
    var plid = $routeParams.plid;
    var userId = localStorage.getItem('userId');
    $scope.avatar = localStorage.getItem('imageUrl');
    var userName = localStorage.getItem('nameHome') != null ?  localStorage.getItem('nameHome') : "";
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

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
                window.location.href = '#/mentor/dashboard';
            }
        } else {
            window.localStorage.clear();
            window.location.href = '/';
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
            console.log(result);
            if (result && result != "Found no data") {
                for (var i = result.length - 1; i >= 0; i--) {
                    result[i].timeStamp = convertUnixTimeToTime(result[i].timeStamp);
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
                        window.location.href = '#/mentor/video/detail/'+$scope.videos[0].vid+'/list/'+plid+'';
                    }                    
                } else{
                    loadVideoDetail($scope.videos[0]);
                    $scope.pos = 0;
                }
            } else
                window.location.href = '#/mentor/playlist/detail/'+plid+'';
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
        // $scope.video.timeStamp = convertUnixTimeToTime($scope.video.timeStamp);
        initYoutubePlayer($scope.video.url);
        getCommentVideoDetail(v.vid);
        getVideoRelated();
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
                    window.location.href = '#/mentor/video/detail/'+id+'/list/'+$scope.video.plid+'';
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
            player.cueVideoById($scope.vid);
        }
    }

    $scope.loadVideo = function(v){
        loadData(v);
    }

    function loadData(v){
        if (v.plid && v.plid > 0) {
            window.location.href = '#/mentor/video/detail/'+v.vid+'/list/'+v.plid+'';
        } else{
            window.location.href = '#/mentor/video/detail/'+v.vid+'';
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

    $scope.openAddPlaylist = function(vid){
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
                    } else{
                        $scope.currentId = $scope.videos[$scope.pos].vid;
                        loadVideoDetail($scope.videos[$scope.pos]); 
                    }                      
                } else{
                    if ($scope.videosRelated) {
                        loadData($scope.videosRelated[0]);
                    } else{
                        window.location.href = '#/mentor/mentorVideo';
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

    $scope.loadTo = function(){
        angular.element(document.getElementById('videos-in-list')).mCustomScrollbar('scrollTo','#listPlaylist' + $scope.currentId);
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
        var objRequest = {
            authorID: userId,
            content: content,
            vid: $scope.video.vid,
            title: $scope.video.title,
            subjectId: $scope.video.subjectId,
            author: userName
        };
        videoDetailService.addCommentVideo(objRequest).success(function (data) {
            if (data.status == 'true') {
                $("#txtComment").val('');
                $(".comment-action").hide();
                videoDetailService.getCommentVideoById(vid).then(function (data) {
                    if (data.data.status == 'true') {
                        if (data.data.request_data_result.length > 0) {
                            $scope.comments = formatData(data.data.request_data_result);
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
            angular.element(document.getElementById('videos-in-list')).mCustomScrollbar('scrollTo','#listPlaylist' + $scope.currentId);
        }
    }

    $scope.addToPlaylist = function(vid){

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
        window.location.href = '#/mentor/video/detail/'+$scope.video.vid+'/list/'+value+'';
        // if (!plid) {
        // } else {
        // var index = $scope.videos.indexOf($scope.video);
        // console.log('index: '+index);
        // if (index != -1)
        //     $scope.videos.splice(index, 1);
        // var size = $scope.videos.length;
        // if (size == 0) {
        //     window.location.href = '#/mentor/playlist/detail/'+plid+'';
        // } else {
        //     if (index < size - 1) {
        //         window.location.href = '#/mentor/video/detail/'+$scope.videos[index].vid+'/list/'+plid+'';
        //     } else {
        //         window.location.href = '#/mentor/video/detail/'+$scope.videos[index-1].vid+'/list/'+plid+'';
        //     }
        // }
        // }        
    });

    $scope.$on('passing', function(e, video){
        window.location.href = '#/mentor/video/detail/'+$scope.video.vid+'/list/'+video.plid+'';
    });
}]);
