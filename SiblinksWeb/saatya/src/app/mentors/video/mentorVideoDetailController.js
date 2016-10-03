brotControllers.controller('MentorVideoDetailController', 
  ['$scope', '$modal', '$routeParams', '$http', '$location', 'VideoService', 'MentorService', 'myCache', 'HomeService',
                                       function ($scope, $modal, $routeParams, $http, $location, VideoService, MentorService, myCache, HomeService) {


    var vid = $routeParams.vid;
    var plid = $routeParams.plid;
    var userId = localStorage.getItem('userId');
    
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

    init();

    function init(){
    	if (!isNaN(vid) && vid > 0) {
    		// get video detail
            $scope.vid = vid;
            initYoutubePlayer();
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
    
    function initYoutubePlayer(){
        onYouTubeIframeAPIReady('Lo3rrP8u7Mw');
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
