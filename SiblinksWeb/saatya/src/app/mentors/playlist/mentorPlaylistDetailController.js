brotControllers.controller('MentorPlaylistDetailController', 
  ['$scope', '$modal', '$routeParams', '$http', '$location', 'PlaylistService', 'videoDetailService', 'VideoService',
                                       function ($scope, $modal, $routeParams, $http, $location, PlaylistService, videoDetailService, VideoService) {

    var plid = $routeParams.plid;
    var userId = localStorage.getItem('userId'); 

    init();

    function init(){
    	// check plid is interger or not
    	if (!isNaN(plid) && plid > 0) {
    		// load playlist information
    		loadPlaylistDetail();
    		getVideosInPlaylist();
    	} else{
    		// reload to home
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
    		}
    	});
    }

    $scope.delete = function(vid){
        if (confirm('Are you sure?')) {
            VideoService.deleteVideo(vid, userId).then(function(data){
              if (data.data.request_data_result != null && data.data.request_data_result.length > 0) {
                getVideosInPlaylist();
              }
            });
        }
    }

    $scope.openEdit = function(vid){
        var modalInstance = $modal.open({
            templateUrl: 'src/app/mentors/video/upload_tutorial_popup.tpl.html',
            controller: 'UploadTutorialController',
            resolve: {
                v_id: function(){
                    return vid;
                },
                u_id: function(){
                    return userId;
                }
            }
        });
    }

    $scope.openAddVideo = function(){
        var modalInstance = $modal.open({
            templateUrl: 'src/app/mentors/playlist/choose_video_popup.tpl.html',
            controller: 'ChooseVideoController',
            resolve:{
                pl_id: function(){
                    return plid;
                }
            }            
        });
    }
}]);
