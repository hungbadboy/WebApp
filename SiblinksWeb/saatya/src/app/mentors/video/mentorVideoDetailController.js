brotControllers.controller('MentorVideoDetailController', 
  ['$scope', '$modal', '$routeParams', '$http', '$location', 'VideoService', 'MentorService', 'myCache', 'HomeService',
                                       function ($scope, $modal, $routeParams, $http, $location, VideoService, MentorService, myCache, HomeService) {


    var vid = $routeParams.vid;
    var userId = localStorage.getItem('userId');
    
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';

    init();

    function init(){
    	if (!inNaN(vid) && vid > 0) {
    		// get video detail
    	} else{
    		window.location.href = '#/mentor/dashboard';
        	window.location.reload();
    	}
    }
    

    $scope.addToPlaylist = function(vid){

    }
}]);
