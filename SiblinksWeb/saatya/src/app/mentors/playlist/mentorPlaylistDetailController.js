brotControllers.controller('MentorPlaylistDetailController', 
  ['$scope', '$modal', '$routeParams', '$http', '$location', 'PlaylistService', 'videoDetailService', 'myCache',
                                       function ($scope, $modal, $routeParams, $http, $location, PlaylistService, videoDetailService, myCache) {

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
    			console.log(data);
    			$scope.playlist = data;
    		}
    	});
    }

    function getVideosInPlaylist(){
    	videoDetailService.getVideoByPlaylistId(plid).then(function (data) {
    		if (data.data.request_data_result != null && data.data.request_data_result != "Found no data") {
    			$scope.videos = data.data.request_data_result;
    			console.log($scope.videos);
    		}
    	});
    }
}]);
